package ${TableNameServicePackage};

import com.github.vazmin.framework.core.dao.GenericMapper;
import com.github.vazmin.framework.core.service.LongPKBaseService;
import ${TableNameMapperPackage}.${table.upperCamelCaseName}Mapper;
import ${TableNamePackage}.${table.upperCamelCaseName};
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* ${table.clearRemark} 业务处理类
*
* Created by ${app.author} on ${genDate}.
*/
@Service
public class ${table.upperCamelCaseName}Service
        extends LongPKBaseService<${table.upperCamelCaseName}> {
    private static final Logger log = LoggerFactory.getLogger(${table.upperCamelCaseName}Service.class);

    @Autowired
    private ${table.upperCamelCaseName}Mapper ${table.lowerCamelCaseName}Mapper;

    /**
    * 获取数据层mapper接口对象，子类必须实现该方法
    *
    * @return GenericMapper<E, PK> 数据层mapper接口对象
    */
    @Override
    protected GenericMapper<${table.upperCamelCaseName}, Long> getMapper() {
        return ${table.lowerCamelCaseName}Mapper;
    }

    /**
    * 根据id获取对象，如果为空，返回空对象
    * @param id Long 公司id
    * @return ${table.upperCamelCaseName} ${table.clearRemark}对象
    */
    public ${table.upperCamelCaseName} getSafety(Long id) {
        ${table.upperCamelCaseName} ${table.lowerCamelCaseName} = get(id);
        if (${table.lowerCamelCaseName} == null) {
            ${table.lowerCamelCaseName} = new ${table.upperCamelCaseName}();
        }
        return ${table.lowerCamelCaseName};
    }
}