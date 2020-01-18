package com.github.vazmin.code.generator.api;

import com.github.vazmin.code.generator.internal.db.ActualTableName;
import com.github.vazmin.code.generator.model.TableConfiguration;
import com.github.vazmin.code.generator.utils.FieldNaming;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * the table schema
 * Created by Chwing on 2019/12/30.
 */
public class IntrospectedTable {

    private String tableName;

    /** 表名转换后的驼峰式名称（首字母小写）tableName */
    private String lowerCamelCaseName;
    /** 表名转换后的驼峰式名称（首字母大写） TableName */
    private String upperCamelCaseName;
    /** 表名转换后的小写带减号名称 table-name*/
    private String lowerCaseSubName;

    private String remarks;

    protected TableConfiguration tableConfiguration;

    private String tableType;

    public IntrospectedTable(ActualTableName atn, TableConfiguration tableConfiguration) {
        this.tableConfiguration = tableConfiguration;
        setTableName(atn.getTableName());
    }

    protected List<IntrospectedColumn> primaryKeyColumns = new ArrayList<>();

    protected List<IntrospectedColumn> baseColumns = new ArrayList<>();

    protected List<IntrospectedColumn> blobColumns = new ArrayList<>();

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
        this.lowerCamelCaseName = FieldNaming.toCamelCase(tableName);
        this.upperCamelCaseName = FieldNaming.upperCaseFirstLetter(this.lowerCamelCaseName);
        this.lowerCaseSubName = tableName.replace('_', '-');
    }

    public String getLowerCamelCaseName() {
        return lowerCamelCaseName;
    }

    public void setLowerCamelCaseName(String lowerCamelCaseName) {
        this.lowerCamelCaseName = lowerCamelCaseName;
    }

    public String getUpperCamelCaseName() {
        return upperCamelCaseName;
    }

    public void setUpperCamelCaseName(String upperCamelCaseName) {
        this.upperCamelCaseName = upperCamelCaseName;
    }

    public String getLowerCaseSubName() {
        return lowerCaseSubName;
    }

    public void setLowerCaseSubName(String lowerCaseSubName) {
        this.lowerCaseSubName = lowerCaseSubName;
    }

    public String getClearRemark() {
        if (remarks.endsWith("表")) {
            return remarks.substring(0, remarks.lastIndexOf("表"));
        }
        return remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public TableConfiguration getTableConfiguration() {
        return tableConfiguration;
    }

    public void setTableConfiguration(TableConfiguration tableConfiguration) {
        this.tableConfiguration = tableConfiguration;
    }

    public List<IntrospectedColumn> getPrimaryKeyColumns() {
        return primaryKeyColumns;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public void setPrimaryKeyColumns(List<IntrospectedColumn> primaryKeyColumns) {
        this.primaryKeyColumns = primaryKeyColumns;
    }

    public List<IntrospectedColumn> getBaseColumns() {
        return baseColumns;
    }

    public void setBaseColumns(List<IntrospectedColumn> baseColumns) {
        this.baseColumns = baseColumns;
    }

    public List<IntrospectedColumn> getBlobColumns() {
        return blobColumns;
    }

    public void setBlobColumns(List<IntrospectedColumn> blobColumns) {
        this.blobColumns = blobColumns;
    }



    public void addColumn(IntrospectedColumn introspectedColumn) {
        if (introspectedColumn.isBLOBColumn()) {
            blobColumns.add(introspectedColumn);
        } else {
            baseColumns.add(introspectedColumn);
        }

//        introspectedColumn.setIntrospectedTable(this);
    }

    public void addPrimaryKeyColumn(String columnName) {
        boolean found = false;
        // first search base columns
        Iterator<IntrospectedColumn> iter = baseColumns.iterator();
        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();
            if (introspectedColumn.getActualColumnName().equals(columnName)) {
                primaryKeyColumns.add(introspectedColumn);
                iter.remove();
                found = true;
                break;
            }
        }

        // search blob columns in the weird event that a blob is the primary key
        if (!found) {
            iter = blobColumns.iterator();
            while (iter.hasNext()) {
                IntrospectedColumn introspectedColumn = iter.next();
                if (introspectedColumn.getActualColumnName().equals(columnName)) {
                    primaryKeyColumns.add(introspectedColumn);
                    iter.remove();
                    break;
                }
            }
        }
    }

    /**
     * Returns all columns in the table (for use by the select by primary key and
     * select by example with BLOBs methods).
     *
     * @return a List of ColumnDefinition objects for all columns in the table
     */
    public List<IntrospectedColumn> getAllColumns() {
        return Stream.of(primaryKeyColumns.stream(), baseColumns.stream(), blobColumns.stream())
                .flatMap(Function.identity())
                .collect(Collectors.toList());
    }

    /**
     * Returns all columns except BLOBs (for use by the select by example without BLOBs method).
     *
     * @return a List of ColumnDefinition objects for columns in the table that are non BLOBs
     */
    public List<IntrospectedColumn> getNonBLOBColumns() {
        return Stream.of(primaryKeyColumns.stream(), baseColumns.stream())
                .flatMap(Function.identity())
                .collect(Collectors.toList());
    }

    public boolean hasPrimaryKeyColumns() {
        return !primaryKeyColumns.isEmpty();
    }

    public boolean hasBLOBColumns() {
        return !blobColumns.isEmpty();
    }

    public boolean hasBaseColumns() {
        return !baseColumns.isEmpty();
    }

    public boolean hasAnyColumns() {
        return hasPrimaryKeyColumns() || hasBaseColumns() || hasBLOBColumns();
    }
}
