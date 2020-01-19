package com.github.vazmin.code.generator;

import com.github.vazmin.code.generator.config.AppProperties;
import com.github.vazmin.code.generator.api.IntrospectedTable;
import com.github.vazmin.code.generator.service.GeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Generator Task
 * Created by Chwing on 2019/12/30.
 */
@Component
public class GeneratorTask {

    private static final Logger log = LoggerFactory.getLogger(GeneratorTask.class);

    @Autowired
    GeneratorService generatorService;


    public void start() throws Exception {
        List<IntrospectedTable> introspectedTableList = generatorService.calculateTableAndColumnSchema();
        generatorService.initIOInfo();
        generatorService.process(introspectedTableList);
    }

}
