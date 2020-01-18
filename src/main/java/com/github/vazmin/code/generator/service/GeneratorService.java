package com.github.vazmin.code.generator.service;

import com.github.vazmin.code.generator.api.IntrospectedTable;
import com.github.vazmin.code.generator.config.AppProperties;
import com.github.vazmin.code.generator.engine.TplEngine;
import com.github.vazmin.code.generator.model.TplFile;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by Chwing on 2020/1/14.
 */
@Component
public class GeneratorService {
    private static final Logger log = LoggerFactory.getLogger(GeneratorService.class);

    private final AppProperties appProperties;

    private final TplEngine tplEngine;

    protected final Map<String, Object> model = new HashMap<>();

    protected final Map<String, Set<TplFile>> targetTemplateMap = new HashMap<>();

    private final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    public GeneratorService(AppProperties appProperties, TplEngine tplEngine) throws IOException {
        this.appProperties = appProperties;
        this.tplEngine = tplEngine;
        this.initTargetTemplate();
    }


    public void process(List<IntrospectedTable> introspectedTableList) throws IOException, TemplateException {
//        Map<String, Object> model = new HashMap<>();
        model.put("app", appProperties);
        model.put("genDate", DateTimeFormatter.ofPattern("yyyy/M/d").format(LocalDate.now()));
        model.put("searchColumnName", appProperties.getSearchColumnName());
        model.put("basePkg", appProperties.getPkg());
        for(IntrospectedTable table : introspectedTableList) {
            model.put("table", table);
            model.put("columnList", table.getAllColumns());
            tplEngine.process(model, targetTemplateMap, table);
        }
    }


    protected void initTargetTemplate() throws IOException {
        List<AppProperties.Folder> folders = appProperties.getFolders();
        String templateLoaderPath = appProperties.getTemplateLoaderPath();
        Resource resource = resolver.getResource(templateLoaderPath);
        String absolutePath = resource.getFile().getAbsolutePath();
        for (AppProperties.Folder folder: folders) {
            String target = appProperties.getOutputRootDir() + folder.getPath();
            boolean isJava = folder.getPath().contains(appProperties.getPkgDir());
            for (AppProperties.TplIO tplIO : folder.getTplIOList()) {
                Set<TplFile> fileSet = getResources(tplIO.getTplPath(), absolutePath);
                if (isJava) {
                    for (TplFile tplFile: fileSet) {
                        tplFile.getTplPkg();
                    }
                }
                targetTemplateMap.computeIfAbsent(
                        target  + tplIO.getTarget(), k -> new HashSet<>())
                        .addAll(fileSet);
            }
        }
    }

    public Set<TplFile> getResources(String tplPath, String absoluteTemplateLoaderPath)  {
        String tfPath = appProperties.getTemplateFilePath(tplPath);
        Set<TplFile> files = new HashSet<>();
        try {
            Resource[] resources = resolver.getResources(tfPath);
            for (Resource resource : resources) {
                if (resource.exists()) {
                    File file = resource.getFile();
                    files.add(
                            new TplFile(
                                    file.getAbsolutePath().replace(absoluteTemplateLoaderPath, ""),
                                    file.getName()));
                } else {
                    log.error("template file no exists. {}", resource.getURL());
                }
            }
        } catch (IOException e) {
            log.error("get template resource error", e);
        }
        return files;
    }
}
