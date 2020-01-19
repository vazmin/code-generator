package com.github.vazmin.code.generator.config;

import com.github.vazmin.code.generator.utils.StringUtility;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import java.io.File;
import java.util.List;

/**
 * Properties specific to Code generator.
 * <p>
 *
 * Properties are configured in the {@code application.yml} file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = true)
public class AppProperties {

    private String author;
    /** table name list */
    private String [] tableNames;

    private String pkg;

    private String pkgDir;

    private String templateLoaderPath;

    private String outputRootDir;

    private String[] searchColumnNames;

    private boolean override = false;

    private String datePattern = "yyyy/M/d";

    private List<Folder> folders;

    private final Url url = new Url();

    /** java type resolver config */
    private final JavaTypeResolver javaTypeResolver = new JavaTypeResolver();

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
        this.pkgDir = pkg.replace(".", File.separator);
    }

    public String getPkgDir() {
        return pkgDir;
    }

    public String getTemplateLoaderPath() {
        return templateLoaderPath;
    }

    public String getTemplateFilePath(String path) {
        return templateLoaderPath + path;
    }

    public void setTemplateLoaderPath(String templateLoaderPath) {
        this.templateLoaderPath = templateLoaderPath;
    }

    public String getOutputRootDir() {
        return outputRootDir;
    }

    public void setOutputRootDir(String outputRootDir) {
        this.outputRootDir = outputRootDir;
    }

    public String[] getSearchColumnNames() {
        return searchColumnNames;
    }

    public void setSearchColumnNames(String[] searchColumnNames) {
        this.searchColumnNames = searchColumnNames;
    }

    public String getSearchColumnName(String tableName) {
        if (tableNames.length == searchColumnNames.length) {
            for(int i = 0; i < tableNames.length; i++) {
                if (tableName.equals(tableNames[i])) {
                    return searchColumnNames[i];
                }
            }
        }
        return "";
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    public String[] getTableNames() {
        Assert.notEmpty(tableNames, "application.table-names is empty.");
        return tableNames;
    }

    public void setTableNames(String[] tableNames) {
        this.tableNames = tableNames;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Url getUrl() {
        return url;
    }

    public JavaTypeResolver getJavaTypeResolver() {
        return javaTypeResolver;
    }

    public boolean isOverride() {
        return override;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }

    public static class Url {
        private String value;
        private String backendPrefix;
        private String frontendPrefix;

        public String getValue() {
            return value;
        }

        public String getSlashValue() {
            return value.startsWith("/") ? value : "/" + value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getBackend() {
            return backendPrefix + getSlashValue();
        }

        public String getFrontend() {
            return frontendPrefix + getSlashValue();
        }

        public String getBackendPrefix() {
            return backendPrefix;
        }

        public void setBackendPrefix(String backendPrefix) {
            this.backendPrefix = backendPrefix;
        }

        public String getFrontendPrefix() {
            return frontendPrefix;
        }

        public void setFrontendPrefix(String frontendPrefix) {
            this.frontendPrefix = frontendPrefix;
        }
    }

    public static class Tpl {

    }

    public static class Folder {

        private String path;

        private boolean java = false;

        private List<TplIO> tplIOList;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path.replace(".", File.separator);
        }

        public List<TplIO> getTplIOList() {
            return tplIOList;
        }

        public void setTplIOList(List<TplIO> tplIOList) {
            this.tplIOList = tplIOList;
        }

        public boolean isJava() {
            return java;
        }

        public void setJava(boolean java) {
            this.java = java;
        }

        public String getPkg(String basePkg) {
            String pkg = StringUtility.pathToPackage(this.path);
            return pkg.substring(pkg.indexOf(basePkg));
        }
    }

    public static class TplIO {
        private String tplPath;

        private String target;

        public String getTplPath() {
            return tplPath;
        }

        public void setTplPath(String tplPath) {
            this.tplPath = tplPath;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getTplPkg() {
            String tplPkg =  target != null ?
                    StringUtility.pathToPackage(target) : "";
            return tplPkg.startsWith(".") || tplPkg.equals("") ? tplPkg : "." + tplPkg;
        }
    }



    public static class JavaTypeResolver {

        private Boolean forceBigDecimals;

        private Boolean useJSR310Types;

        public Boolean getForceBigDecimals() {
            return forceBigDecimals;
        }

        public void setForceBigDecimals(Boolean forceBigDecimals) {
            this.forceBigDecimals = forceBigDecimals;
        }

        public Boolean getUseJSR310Types() {
            return useJSR310Types;
        }

        public void setUseJSR310Types(Boolean useJSR310Types) {
            this.useJSR310Types = useJSR310Types;
        }
    }


}
