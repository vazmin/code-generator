package com.github.vazmin.code.generator.model;

/**
 * Template File Path and Name
 * Created by Chwing on 2020/1/12.
 */
public class TplFile {
    /** template path. eg: TableName.java.ftl, view/table-name.service.ts.ftl */
    private String tplPath;
    /** template file name. eg: TableName.java.ftl, table-name.service.ts.ftl  */
    private String tplName;

    private String alias;

    public TplFile(String tplPath, String tplName) {
        this.tplPath = tplPath;
        this.tplName = tplName;
    }

    public String getTplPath() {
        return tplPath;
    }

    public void setTplPath(String tplPath) {
        this.tplPath = tplPath;
    }

    public String getTplName() {
        return tplName;
    }

    public void setTplName(String tplName) {
        this.tplName = tplName;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        if (alias == null) {
            return  tplName.split("\\.")[0];
        }
        return alias;
    }

    public String getPackageAlias() {
        return getAlias() + "Package";
    }

    @Override
    public String toString() {
        return "[tplName:" + tplName + ", tplPath:" + tplPath + "] "
                + super.toString();
    }
}
