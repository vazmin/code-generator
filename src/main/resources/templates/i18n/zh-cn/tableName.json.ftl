{
  "${table.lowerCamelCaseName}": {
<#list columnList as column>
    "${column.lowerCamelCaseName}": "${column.clearRemark}"<#if column_has_next>, </#if>
</#list>
  }
}