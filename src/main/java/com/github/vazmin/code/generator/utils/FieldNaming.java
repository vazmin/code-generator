package com.github.vazmin.code.generator.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 数据库字段与类字段转换工具
 * Created by Chwing on 2019/12/30.
 */
public class FieldNaming {
    /**
     * 将源字符串（以下划线分隔）转换成驼峰式字符串，第一个字母小写
     * @param name String 源字符串
     * @return String 转换后的字符串
     */
    public static String toCamelCase(String name) {
        StringBuilder translation = new StringBuilder();
        String[] partArray = name.split("_");
        for (String part: partArray) {
            if (StringUtils.isNotBlank(part)) {
                if (translation.length() != 0) {
                    translation.append(upperCaseFirstLetter(part.trim()));
                } else {
                    translation.append(part.trim());
                }
            }
        }
        return translation.toString();
    }

    /**
     * Converts the field name that uses camel-case define word separation into
     * separate words that are separated by the provided {@code separatorString}.
     */
    public static String separateCamelCase(String name, String separator) {
        StringBuilder translation = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char character = name.charAt(i);
            if (Character.isUpperCase(character) && translation.length() != 0) {
                translation.append(separator);
            }
            translation.append(character);
        }
        return translation.toString();
    }

    /**
     * Ensures the JSON field names begins with an upper case letter.
     */
    public static String upperCaseFirstLetter(String name) {
        StringBuilder fieldNameBuilder = new StringBuilder();
        int index = 0;
        char firstCharacter = name.charAt(index);

        while (index < name.length() - 1) {
            if (Character.isLetter(firstCharacter)) {
                break;
            }

            fieldNameBuilder.append(firstCharacter);
            firstCharacter = name.charAt(++index);
        }

        if (!Character.isUpperCase(firstCharacter)) {
            String modifiedTarget = modifyString(Character.toUpperCase(firstCharacter), name, ++index);
            return fieldNameBuilder.append(modifiedTarget).toString();
        } else {
            return name;
        }
    }

    public static String modifyString(char firstCharacter, String srcString, int indexOfSubstring) {
        return (indexOfSubstring < srcString.length())
                ? firstCharacter + srcString.substring(indexOfSubstring)
                : String.valueOf(firstCharacter);
    }
}
