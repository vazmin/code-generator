package com.github.vazmin.code.generator.other;

import com.github.vazmin.code.generator.GeneratorApp;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.*;
import java.util.Arrays;

/**
 * Created by Chwing on 2019/12/29.
 */
@SpringBootTest(classes = GeneratorApp.class)
public class DatabaseMetaDataTest {

    private static final Logger log = LoggerFactory.getLogger(DatabaseMetaDataTest.class);


    @Autowired
    HikariDataSource dataSource;

    @Test
    public void foo() throws SQLException {
        String schema = "manage_platform";
        String table = "group_info";
        Connection connection = dataSource.getConnection();
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet resultSet = databaseMetaData.getTables(schema, schema, table, new String[]{"TABLE"});
        ResultSetMetaData tableMetaData = resultSet.getMetaData();
        String[] tableMeta = metaData(tableMetaData);
        log.debug("table meta data: \n" + Arrays.toString(tableMeta));
        while (resultSet.next()) {
            for (String meta: tableMeta) {
                log.debug("{} = {}", meta, resultSet.getString(meta));
            }
        }

        ResultSet rs = databaseMetaData.getColumns(schema, schema, table, "%");
        ResultSetMetaData rsMetaData = rs.getMetaData();
        String[] columnMeta = metaData(rsMetaData);
        log.debug("column meta data: \n" + Arrays.toString(columnMeta));
        while (rs.next()) {
            for (String meta: columnMeta) {
                log.debug("{} = {}", meta, rs.getString(meta));
            }
            log.debug("================================");
        }

    }

    private String[] metaData(ResultSetMetaData metaData) throws SQLException {
        int count = metaData.getColumnCount();
        String [] columns = new String [count];
        for (int i = 1; i <= count; i++) {
            columns[i - 1] = metaData.getColumnName(i);
        }
        return columns;
    }
}
