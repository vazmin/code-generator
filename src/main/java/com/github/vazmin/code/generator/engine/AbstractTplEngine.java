package com.github.vazmin.code.generator.engine;

import com.github.vazmin.code.generator.api.IntrospectedTable;
import com.github.vazmin.code.generator.config.AppProperties;
import com.github.vazmin.code.generator.model.TplFile;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by Chwing on 2020/1/9.
 */
public abstract class AbstractTplEngine implements TplEngine {

    private static final Logger log = LoggerFactory.getLogger(AbstractTplEngine.class);

    protected AppProperties appProperties;

    public AbstractTplEngine(AppProperties appProperties) {
        this.appProperties = appProperties;
    }


//    abstract void process(Map<String, Object> model, Map<String, Set<TplFile>> targetTemplateMap,
//                          IntrospectedTable table) throws IOException, TemplateException;



    protected String getOutputFileName(String fileName, IntrospectedTable table) {
        return fileName.replace("table-name", table.getLowerCaseSubName())
                .replace("TableName", table.getUpperCamelCaseName())
                .replace("tableName", table.getLowerCamelCaseName());
    }
}
