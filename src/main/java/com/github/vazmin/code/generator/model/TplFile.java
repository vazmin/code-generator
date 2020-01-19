package com.github.vazmin.code.generator.model;

import com.github.vazmin.code.generator.utils.StringUtility;

import java.io.File;
import java.net.URI;

/**
 * Created by Chwing on 2020/1/12.
 */
public class TplFile {

    private String tplPath;

    private String tplName;

    private String alias;

    public TplFile(String tplPath, String tplName) {
        this.tplPath = tplPath;
        this.tplName = tplName;
    }

    public TplFile(String tplPath, String tplName, String alias) {
        this.tplPath = tplPath;
        this.tplName = tplName;
        this.alias = alias;
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
}
