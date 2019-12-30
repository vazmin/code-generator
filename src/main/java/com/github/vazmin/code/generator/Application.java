package com.github.vazmin.code.generator;

import com.github.vazmin.code.generator.config.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

import java.sql.SQLException;

/**
 * code generator application
 * Created by Chwing on 2019/12/28.
 */
@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class Application {

    public static void main(String[] args) throws SQLException {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        GeneratorTask generatorTask = context.getBean(GeneratorTask.class);
        generatorTask.start();
    }
}
