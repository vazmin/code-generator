package com.github.vazmin.code.generator.config;

/**
 * App Constants
 * Created by wangzm on 2020/3/24.
 */
public interface Constants {
    String datePattern = "yyyy/M/d";

    interface JavaTypeResolver {
        Boolean forceBigDecimals = true;
        Boolean useJSR310Types = true;
    }
}
