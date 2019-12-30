package com.github.vazmin.code.generator.model;

/**
 *
 * Created by Chwing on 2019/12/29.
 */
public class TableConfiguration {

    private String catalog;

    private String schema;

    private String tableName;

    private String alias;

    private boolean isDelimitIdentifiers;

    public TableConfiguration(String catalog, String schema, String tableName) {
        this.catalog = catalog;
        this.schema = schema;
        this.tableName = tableName;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isDelimitIdentifiers() {
        return isDelimitIdentifiers;
    }

    public void setDelimitIdentifiers(boolean delimitIdentifiers) {
        isDelimitIdentifiers = delimitIdentifiers;
    }
}
