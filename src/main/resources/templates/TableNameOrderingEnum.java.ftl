package ${TableNameOrderingEnumPackage};

import org.apache.commons.lang3.StringUtils;

/**
* ${table.clearRemark} 列表排序枚举类
*
* Created by ${app.author} on ${genDate}.
*/
public enum ${table.upperCamelCaseName}OrderingEnum {
<#assign defaultEnum = "">
<#assign index = 1>
<#list columnList as column>
    <#if column.actualColumnName != "id" && defaultEnum == "">
        <#assign defaultEnum = column.upperName + "_ASC">
    </#if>
    ${column.upperName}_ASC(${index}, "a.${column.actualColumnName} ASC", "${table.lowerCamelCaseName}.${column.actualColumnName}.asc", "${column.clearRemark} 升序"),
    <#assign index=index + 1>
    ${column.upperName}_DESC(${index}, "a.${column.actualColumnName} DESC", "${table.lowerCamelCaseName}.${column.actualColumnName}.desc", "${column.clearRemark} 降序")<#if column_has_next>,<#else>;</#if>
    <#assign index=index + 1>
</#list>

    /** id */
    private Integer id;
    /** option选项值 */
    private String value;
    /** 本地化标识 */
    private String textCode;
    /** 描述 */
    private String description;

    ${table.upperCamelCaseName}OrderingEnum(
            Integer id, String value, String textCode, String description) {
        this.id = id;
        this.value = value;
        this.textCode = textCode;
        this.description = description;
    }

    /**
    * 根据id值获取对应的枚举值
    * @param id String id值
    * @return ${table.upperCamelCaseName}OrderingEnum 枚举值，如果不存在返回 null
    */
    public static ${table.upperCamelCaseName}OrderingEnum valueOf(Integer id) {
        if (id != null) {
            for (${table.upperCamelCaseName}OrderingEnum orderingEnum: values()) {
                if (orderingEnum.getId().intValue() == id.intValue()) {
                    return orderingEnum;
                }
            }
        }
        return null;
    }

    /**
    * 根据value获取对应的枚举值
    * @param value String 值字符串
    * @return ${table.upperCamelCaseName}OrderingEnum 排序枚举值，如果不存在，返回 null
    */
    public static ${table.upperCamelCaseName}OrderingEnum enumOfValue(String value) {
        if (StringUtils.isNotBlank(value)) {
            for (${table.upperCamelCaseName}OrderingEnum orderingEnum: values()) {
                if (orderingEnum.getValue().equals(value)) {
                    return orderingEnum;
                }
            }
        }
        return null;
    }

    public Integer getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public String getTextCode() {
        return textCode;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据id 直接获取描述信息
     * @param id Integer 数字编号
     * @return String 对应枚举的描述，如果不是有效枚举返回null
     */
    public static String getDescription(Integer id) {
        ${table.upperCamelCaseName}OrderingEnum orderingEnum = valueOf(id);
        return orderingEnum != null ? orderingEnum.getDescription() : null;
    }

    /**
     * 根据value 直接获取描述信息
     * @param value String 值字符串
     * @return String 对应枚举的描述，如果不是有效枚举返回null
     */
    public static String getDescriptionOfValue(String value) {
        ${table.upperCamelCaseName}OrderingEnum orderingEnum = enumOfValue(value);
        return orderingEnum != null ? orderingEnum.getDescription() : null;
    }

    /**
     * 返回默认枚举值
     * @return ${table.upperCamelCaseName}OrderingEnum 默认枚举值
     */
    public static ${table.upperCamelCaseName}OrderingEnum getDefaultEnum() {
        return ${defaultEnum};
    }
}