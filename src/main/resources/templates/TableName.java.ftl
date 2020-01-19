package ${TableNamePackage};

import java.io.Serializable;

/**
* ${table.clearRemark} Bean
*
* Created by ${app.author} on ${genDate}.
*/
public class ${table.upperCamelCaseName} implements Serializable {

<#list columnList as column>
    /** ${column.remarks} */
    private ${column.javaType} ${column.lowerCamelCaseName};
</#list>

<#list columnList as column>
    public ${column.javaType} get${column.upperCamelCaseName}() {
        return ${column.lowerCamelCaseName};
    }

    public void set${column.upperCamelCaseName}(${column.javaType} ${column.lowerCamelCaseName}) {
        this.${column.lowerCamelCaseName} = ${column.lowerCamelCaseName};
    }

</#list>
}