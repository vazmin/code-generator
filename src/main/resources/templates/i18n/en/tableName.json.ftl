{
  "${table.lowerCamelCaseName}": {
<#list columnList as column>
    "${column.lowerCamelCaseName}": "${column.enRemark}"<#if column_has_next>, </#if>
</#list>
  }
}