package com.github.vazmin.code.generator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Properties specific to Code generator.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private List<String> tableNames;

    private final JavaTypeResolver javaTypeResolver = new JavaTypeResolver();

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

    public List<String> getTableNames() {
        Assert.notEmpty(tableNames, "application.table-names is empty.");
        return tableNames;
    }

    public void setTableNames(List<String> tableNames) {
        this.tableNames = tableNames;
    }

    public JavaTypeResolver getJavaTypeResolver() {
        return javaTypeResolver;
    }
}
