package com.github.vazmin.code.generator;

import com.github.vazmin.code.generator.api.JavaTypeResolver;
import com.github.vazmin.code.generator.config.ApplicationProperties;
import com.github.vazmin.code.generator.internal.db.DatabaseIntrospector;
import com.github.vazmin.code.generator.api.IntrospectedTable;
import com.github.vazmin.code.generator.model.TableConfiguration;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Chwing on 2019/12/30.
 */
@Component
public class GeneratorTask {

    @Autowired
    ApplicationProperties applicationProperties;

    private static final Logger log = LoggerFactory.getLogger(GeneratorTask.class);

    @Autowired
    HikariDataSource dataSource;

    @Autowired
    JavaTypeResolver javaTypeResolver;

    List<IntrospectedTable> introspectedTableList;

    public void start() throws SQLException {
       introspectedTableList = calculateTableAndColumnSchema();
    }

    public List<IntrospectedTable> calculateTableAndColumnSchema() throws SQLException {
        List<IntrospectedTable> introspectedTableList = new ArrayList<>();
        Connection connection = dataSource.getConnection();
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        final String catalog = connection.getCatalog();
        final String schema = connection.getSchema();
        List<TableConfiguration> tableConfigurationList = applicationProperties.getTableNames()
                .stream()
                .map(name -> new TableConfiguration(catalog, schema, name))
                .collect(Collectors.toList());
        DatabaseIntrospector databaseIntrospector = new DatabaseIntrospector(databaseMetaData, javaTypeResolver);

        for (TableConfiguration tableConfiguration: tableConfigurationList) {
            introspectedTableList.addAll(databaseIntrospector.introspectTables(tableConfiguration));
        }
        return introspectedTableList;
    }
}
