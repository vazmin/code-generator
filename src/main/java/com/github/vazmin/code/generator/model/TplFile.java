package com.github.vazmin.code.generator.model;

import java.io.File;
import java.net.URI;

/**
 * Created by Chwing on 2020/1/12.
 */
public class TplFile {

    private String tplPath;

    private String tplName;

    public TplFile(String tplPath, String tplName) {
        this.tplPath = tplPath;
        this.tplName = tplName;
    }

    public String getTplPath() {
        return tplPath;
    }

    public String getAlias() {
        String name = tplName.split(".")[0];
        name.replace("")
    }

    public String getTplPkg() {
        String tplPkg =  tplPath != null ?
                tplPath.replace("\\\\", ".")
                        .replace("\\", ".")
                        .replace("/", ".") : "";
        return tplPkg.startsWith(".") || tplPkg.equals("")? tplPkg : "." + tplPkg;
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
}
