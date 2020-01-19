package ${TableNameValidatorPackage};

import ${TableNamePackage}.${table.upperCamelCaseName};
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
* ${table.upperCamelCaseName} 对象验证器
* Created by ${app.author} on ${genDate}.
*/
@Component
public class ${table.upperCamelCaseName}Validator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return ${table.upperCamelCaseName}.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object obj, Errors errors) {
<#list columnList as column>
    <#if column.lowerCamelCaseName != "id">
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "${column.lowerCamelCaseName}", "${table.lowerCamelCaseName}.${column.lowerCamelCaseName}.error", "${column.enComment} is required.");
    </#if>
</#list>
    }
}