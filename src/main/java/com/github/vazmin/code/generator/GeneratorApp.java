package com.github.vazmin.code.generator;

import com.github.vazmin.code.generator.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

/**
 * code generator application
 * Created by Chwing on 2019/12/28.
 */
@SpringBootApplication
@EnableConfigurationProperties({AppProperties.class})
public class GeneratorApp {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(GeneratorApp.class, args);
        GeneratorTask generatorTask = context.getBean(GeneratorTask.class);
        generatorTask.start();
    }
}
