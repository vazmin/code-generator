package com.github.vazmin.code.generator.internal.db;

import java.util.Objects;

/**
 * the name of this database product.
 * Created by wangzm on 2020/12/30.
 */
public enum DatabaseProduct {
    MYSQL("MySQL"),
    SQL_SERVER("Microsoft SQL Server"),
    UNKNOWNS("")
    ;

    public static DatabaseProduct nameOf(String productName) {
        for (DatabaseProduct value : values()) {
            if (Objects.equals(productName, value.getProductName()))
                return value;
        }
        return UNKNOWNS;
    }

    private final String productName;

    DatabaseProduct(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }
}
