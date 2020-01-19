package com.github.vazmin.code.generator.engine;

import com.github.vazmin.code.generator.api.IntrospectedTable;
import com.github.vazmin.code.generator.config.AppProperties;
import com.github.vazmin.code.generator.model.TplFile;
import com.github.vazmin.code.generator.utils.FileUtils;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Abstract Template Engine
 * Created by Chwing on 2020/1/9.
 */
public abstract class AbstractTplEngine implements TplEngine {

    private static final Logger log = LoggerFactory.getLogger(AbstractTplEngine.class);

    protected AppProperties appProperties;

    enum ResultLog {
        NEW, SKIP, OVERRIDE
    }

    private final Map<ResultLog, Set<String>> resultLogMap = new HashMap<>();

    AbstractTplEngine(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    abstract void process(Map<String, Object> model, File file, String tplPath) throws IOException, TemplateException;

    @Override
    public void process(Map<String, Object> model, Map<String, Set<TplFile>> targetTemplateMap,
                        IntrospectedTable table)
            throws IOException, TemplateException {
        for (Map.Entry<String, Set<TplFile>> entry: targetTemplateMap.entrySet()) {
            for (TplFile tplFile : entry.getValue()) {
                String path = entry.getKey() + getOutputFileName(tplFile.getTplName(), table);
                File file = new File(path);
                if (file.exists()) {
                    if (appProperties.isOverride()) {
                        addLog(ResultLog.OVERRIDE, file.getAbsolutePath());
                    } else {
                        addLog(ResultLog.SKIP, file.getAbsolutePath());
                        continue;
                    }
                } else {
                    addLog(ResultLog.NEW, file.getAbsolutePath());
                }
                process(model, FileUtils.mkParent(file), tplFile.getTplPath());
            }
        }
        log.info(resultLogMapToString());
    }


    private String getOutputFileName(String fileName, IntrospectedTable table) {
        return File.separator + fileName.replace("table-name", table.getLowerCaseSubName())
                .replace("TableName", table.getUpperCamelCaseName())
                .replace("tableName", table.getLowerCamelCaseName())
                .replace(".ftl", "");
    }

    private void addLog(ResultLog resultLog, String path) {
        resultLogMap.computeIfAbsent(resultLog, k -> new TreeSet<>()).add(path);
    }

    private String resultLogMapToString () {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (Map.Entry<ResultLog, Set<String>> entry: resultLogMap.entrySet()){
            sb.append(entry.getKey()).append(":\n[\n");
            for (String path: entry.getValue()) {
                sb.append("\t").append(path).append("\n");
            }
            sb.append("]").append("\n");
        }
        return sb.toString();
    }
}
