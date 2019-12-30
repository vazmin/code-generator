/**
 *    Copyright 2006-2019 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.github.vazmin.code.generator.internal.db;

import com.github.vazmin.code.generator.api.IntrospectedColumn;
import com.github.vazmin.code.generator.api.JavaTypeResolver;

import com.github.vazmin.code.generator.api.dom.java.FullyQualifiedJavaType;
import com.github.vazmin.code.generator.api.IntrospectedTable;
import com.github.vazmin.code.generator.model.TableConfiguration;
import com.github.vazmin.code.generator.utils.FieldNaming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

import static com.github.vazmin.code.generator.utils.StringUtility.*;


public class DatabaseIntrospector {
    private static final Logger log = LoggerFactory.getLogger(DatabaseIntrospector.class);

    private DatabaseMetaData databaseMetaData;

    private JavaTypeResolver javaTypeResolver;

    public DatabaseIntrospector(
            DatabaseMetaData databaseMetaData,
            JavaTypeResolver javaTypeResolver) {
        super();
        this.databaseMetaData = databaseMetaData;
        this.javaTypeResolver = javaTypeResolver;
    }



    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }


    /**
     * Returns a List of IntrospectedTable elements that matches the specified table configuration.
     *
     * @param tc
     *            the table configuration
     * @return a list of introspected tables
     * @throws SQLException
     *             if any errors in introspection
     */
    public List<IntrospectedTable> introspectTables(TableConfiguration tc)
            throws SQLException {

        // get the raw columns from the DB
        Map<ActualTableName, List<IntrospectedColumn>> columns = getColumns(tc);

        if (columns.isEmpty()) {
            log.warn("Table configuration with catalog {}, schema {}," +
                    " and table {} did not resolve to any tables", tc.getCatalog(), 
                    tc.getSchema(), tc.getTableName());
            return Collections.emptyList();
        }

//        removeIgnoredColumns(tc, columns);
        calculateExtraColumnInformation(columns);

        List<IntrospectedTable> introspectedTables = calculateIntrospectedTables(
                tc, columns);

        // now introspectedTables has all the columns from all the
        // tables in the configuration. Do some validation...

        Iterator<IntrospectedTable> iter = introspectedTables.iterator();
        while (iter.hasNext()) {
            IntrospectedTable introspectedTable = iter.next();

            if (!introspectedTable.hasAnyColumns()) {
                log.warn("Table {} does not exist, this table will be ignored",
                        introspectedTable.getTableConfiguration().toString());
                iter.remove();
            } else if (!introspectedTable.hasPrimaryKeyColumns()
                    && !introspectedTable.hasBaseColumns()) {
                log.warn("Table {} contains only LOB fields, this table will be ignored",
                        introspectedTable.getTableConfiguration().toString());
                iter.remove();
            }
        }

        return introspectedTables;
    }

    private Map<ActualTableName, List<IntrospectedColumn>> getColumns(
            TableConfiguration tc) throws SQLException {
        
        Map<ActualTableName, List<IntrospectedColumn>> answer = new HashMap<>();

        String fullTableName = composeFullyQualifiedTableName(tc.getCatalog(), tc.getSchema(),
                tc.getTableName(), '.');
        log.debug("Retrieving column information for table {}", fullTableName);

        ResultSet rs = databaseMetaData.getColumns(tc.getCatalog(), tc.getSchema(),
                tc.getTableName(), "%"); 

        boolean supportsIsAutoIncrement = false;
        boolean supportsIsGeneratedColumn = false;
        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();
        for (int i = 1; i <= colCount; i++) {
            if ("IS_AUTOINCREMENT".equals(rsmd.getColumnName(i))) { 
                supportsIsAutoIncrement = true;
            }
            if ("IS_GENERATEDCOLUMN".equals(rsmd.getColumnName(i))) { 
                supportsIsGeneratedColumn = true;
            }
        }

        while (rs.next()) {
            IntrospectedColumn introspectedColumn = new IntrospectedColumn();

            introspectedColumn.setTableAlias(tc.getAlias());
            introspectedColumn.setJdbcType(rs.getInt("DATA_TYPE")); 
            introspectedColumn.setActualTypeName(rs.getString("TYPE_NAME")); 
            introspectedColumn.setLength(rs.getInt("COLUMN_SIZE")); 
            introspectedColumn.setActualColumnName(rs.getString("COLUMN_NAME")); 
            introspectedColumn
                    .setNullable(rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable); 
            introspectedColumn.setScale(rs.getInt("DECIMAL_DIGITS")); 
            introspectedColumn.setRemarks(rs.getString("REMARKS")); 
            introspectedColumn.setDefaultValue(rs.getString("COLUMN_DEF")); 

            if (supportsIsAutoIncrement) {
                introspectedColumn.setAutoIncrement(
                        "YES".equals(rs.getString("IS_AUTOINCREMENT")));  //$NON-NLS-2$
            }

            if (supportsIsGeneratedColumn) {
                introspectedColumn.setGeneratedColumn(
                        "YES".equals(rs.getString("IS_GENERATEDCOLUMN")));  //$NON-NLS-2$
            }

            ActualTableName atn = new ActualTableName(
                    rs.getString("TABLE_CAT"), 
                    rs.getString("TABLE_SCHEM"), 
                    rs.getString("TABLE_NAME")); 

            List<IntrospectedColumn> columns = answer.computeIfAbsent(atn, k -> new ArrayList<>());

            columns.add(introspectedColumn);

            log.trace("Found column \"{}\", data type {}, in table \"{}\"",
                    introspectedColumn.getActualColumnName(), introspectedColumn.getJdbcType(),
                    atn.toString());

        }

        closeResultSet(rs);

        if (answer.size() > 1
                && !stringContainsSQLWildcard(tc.getSchema())
                && !stringContainsSQLWildcard(tc.getTableName())) {
            // issue a warning if there is more than one table and
            // no wildcards were used
            ActualTableName inputAtn = new ActualTableName(tc.getCatalog(), tc
                    .getSchema(), tc.getTableName());

            StringBuilder sb = new StringBuilder();
            boolean comma = false;
            for (ActualTableName atn : answer.keySet()) {
                if (comma) {
                    sb.append(',');
                } else {
                    comma = true;
                }
                sb.append(atn.toString());
            }
            log.warn("Table Configuration {} matched more than one table ({})",
                    inputAtn.toString(), sb.toString());

        }

        return answer;
    }

    private void calculateExtraColumnInformation(Map<ActualTableName, List<IntrospectedColumn>> columns) {

        for (Map.Entry<ActualTableName, List<IntrospectedColumn>> entry : columns
                .entrySet()) {
            for (IntrospectedColumn introspectedColumn : entry.getValue()) {
                String calculatedColumnName = introspectedColumn
                        .getActualColumnName();
                introspectedColumn.setJavaProperty(
                        FieldNaming.toCamelCase(calculatedColumnName));

                FullyQualifiedJavaType fullyQualifiedJavaType = javaTypeResolver
                        .calculateJavaType(introspectedColumn);

                if (fullyQualifiedJavaType != null) {
                    introspectedColumn
                            .setFullyQualifiedJavaType(fullyQualifiedJavaType);
                    introspectedColumn.setJdbcTypeName(javaTypeResolver
                            .calculateJdbcTypeName(introspectedColumn));
                } else {
                    // type cannot be resolved. Check for ignored or overridden
                    log.warn("Unsupported Data Type {} in table {}, column: {}, property defaults to Object type.",
                            introspectedColumn.getJdbcType(),
                            entry.getKey().toString(),
                            introspectedColumn.getActualColumnName());

                    introspectedColumn
                            .setFullyQualifiedJavaType(FullyQualifiedJavaType
                                    .getObjectInstance());
                    introspectedColumn.setJdbcTypeName("OTHER");
                }
            }
        }
    }


    private List<IntrospectedTable> calculateIntrospectedTables(
            TableConfiguration tc,
            Map<ActualTableName, List<IntrospectedColumn>> columns) {

        List<IntrospectedTable> answer = new ArrayList<>();

        for (Map.Entry<ActualTableName, List<IntrospectedColumn>> entry : columns
                .entrySet()) {
            ActualTableName atn = entry.getKey();


            IntrospectedTable introspectedTable = new IntrospectedTable(atn, tc);
            for (IntrospectedColumn introspectedColumn : entry.getValue()) {
                introspectedTable.addColumn(introspectedColumn);
            }

            calculatePrimaryKey(atn, introspectedTable);

            enhanceIntrospectedTable(introspectedTable);

            answer.add(introspectedTable);
        }

        return answer;
    }

    private void calculatePrimaryKey(ActualTableName actualTableName, IntrospectedTable introspectedTable) {
        ResultSet rs;
//        TableConfiguration tc = introspectedTable.getTableConfiguration();
        try {
            rs = databaseMetaData.getPrimaryKeys(
                    actualTableName.getCatalog(), actualTableName.getSchema(), actualTableName.getTableName());
        } catch (SQLException e) {
            log.warn("Cannot obtain primary key information from the database, generated objects may be incomplete");
            return;
        }

        try {
            // keep primary columns in key sequence order
            Map<Short, String> keyColumns = new TreeMap<>();
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME"); //$NON-NLS-1$
                short keySeq = rs.getShort("KEY_SEQ"); //$NON-NLS-1$
                keyColumns.put(keySeq, columnName);
            }

            for (String columnName : keyColumns.values()) {
                introspectedTable.addPrimaryKeyColumn(columnName);
            }
        } catch (SQLException e) {
            // ignore the primary key if there's any error
        } finally {
            closeResultSet(rs);
        }
    }

    /**
     * Calls database metadata to retrieve extra information about the table
     * such as remarks associated with the table and the type.
     *
     * <p>If there is any error, we just add a warning and continue.
     *
     * @param introspectedTable the introspected table to enhance
     */
    private void enhanceIntrospectedTable(IntrospectedTable introspectedTable) {
        try {
            TableConfiguration tc = introspectedTable.getTableConfiguration();

            ResultSet rs = databaseMetaData.getTables(tc.getCatalog(), tc.getSchema(),
                    tc.getTableName(), null);
            if (rs.next()) {
                String remarks = rs.getString("REMARKS"); 
                String tableType = rs.getString("TABLE_TYPE"); 
                introspectedTable.setRemarks(remarks);
                introspectedTable.setTableType(tableType);

            }
            closeResultSet(rs);
        } catch (SQLException e) {
            log.error("Exception retrieving table metadata", e);
        }
    }
}
