package com.github.vazmin.code.generator.service;

import com.github.vazmin.code.generator.api.IntrospectedTable;
import com.github.vazmin.code.generator.api.JavaTypeResolver;
import com.github.vazmin.code.generator.config.AppProperties;
import com.github.vazmin.code.generator.engine.TplEngine;
import com.github.vazmin.code.generator.internal.db.DatabaseIntrospector;
import com.github.vazmin.code.generator.model.TableConfiguration;
import com.github.vazmin.code.generator.model.TplFile;
import com.zaxxer.hikari.HikariDataSource;
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
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Generator Component
 * Created by Chwing on 2020/1/14.
 */
@Component
public class GeneratorService {
    private static final Logger log = LoggerFactory.getLogger(GeneratorService.class);

    private final AppProperties appProperties;

    @Autowired
    HikariDataSource dataSource;

    @Autowired
    JavaTypeResolver javaTypeResolver;

    private final TplEngine tplEngine;
    /** template model data*/
    protected final Map<String, Object> model = new HashMap<>();
    /** target path and template file mapping */
    protected final Map<String, Set<TplFile>> targetTemplateMap = new HashMap<>();
    /** Resource Pattern Resolver */
    private final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    public GeneratorService(AppProperties appProperties, TplEngine tplEngine) throws IOException {
        this.appProperties = appProperties;
        this.tplEngine = tplEngine;
    }


    public void process(List<IntrospectedTable> introspectedTableList) throws IOException, TemplateException {
        model.put("app", appProperties);
        model.put("genDate", DateTimeFormatter.ofPattern(appProperties.getDatePattern()).format(LocalDate.now()));
        model.put("basePkg", appProperties.getPkg());
        for(IntrospectedTable table: introspectedTableList) {
            model.put("table", table);
            model.put("columnList", table.getAllColumns());
            model.put("searchColumnName", appProperties.getSearchColumnName(table.getTableName()));
            tplEngine.process(model, targetTemplateMap, table);
        }
    }

    /**
     * 初始化模板文件输入输出信息，支持通配
     * @throws IOException in case of general resolution/reading failures
     */
    public void initIOInfo() throws IOException {
        List<AppProperties.Folder> folders = appProperties.getFolders();
        String templateLoaderPath = appProperties.getTemplateLoaderPath();
        Resource resource = resolver.getResource(templateLoaderPath);
        String absolutePath = resource.getFile().getAbsolutePath();
        for (AppProperties.Folder folder: folders) {
            String target = appProperties.getOutputRootDir() + folder.getPath();
            for (AppProperties.TplIO tplIO : folder.getTplIOList()) {
                Set<TplFile> fileSet = getResources(tplIO.getTplPath(), absolutePath);
                if (folder.isJava()) {
                    for (TplFile tplFile: fileSet) {
                        // set up class alias and package mapping
                        model.put(tplFile.getPackageAlias(), folder.getPkg(appProperties.getPkg()) + tplIO.getTplPkg());
                    }
                }
                targetTemplateMap.computeIfAbsent(
                        target  + tplIO.getTarget(), k -> new HashSet<>())
                        .addAll(fileSet);
            }
        }
    }

    /**
     * 根据配置文件获取模板文件
     * @param tplPath 模板路径
     * @param absoluteTemplateLoaderPath 模板根目录绝对路径
     * @return TplFile 文件set
     */
    private Set<TplFile> getResources(String tplPath, String absoluteTemplateLoaderPath)  {
        String tfPath = appProperties.getTemplateFilePath(tplPath);
        Set<TplFile> files = new HashSet<>();
        try {
            Resource[] resources = resolver.getResources(tfPath);
            for (Resource resource : resources) {
                if (resource.exists()) {
                    File file = resource.getFile();
                    files.add(new TplFile(
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

    /**
     * calculate table and column schema from DatabaseMetaData
     * @return table list
     * @throws SQLException if a database access error occurs
     * or this method is called on a closed connection
     */
    public List<IntrospectedTable> calculateTableAndColumnSchema() throws SQLException {
        List<IntrospectedTable> introspectedTableList = new ArrayList<>();
        Connection connection = dataSource.getConnection();
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        final String catalog = connection.getCatalog();
        final String schema = connection.getSchema();
        List<TableConfiguration> tableConfigurationList = Arrays.stream(appProperties.getTableNames())
                .map(name -> new TableConfiguration(catalog, schema, name))
                .collect(Collectors.toList());
        DatabaseIntrospector databaseIntrospector = new DatabaseIntrospector(databaseMetaData, javaTypeResolver);

        for (TableConfiguration tableConfiguration: tableConfigurationList) {
            introspectedTableList.addAll(databaseIntrospector.introspectTables(tableConfiguration));
        }
        return introspectedTableList;
    }
}
