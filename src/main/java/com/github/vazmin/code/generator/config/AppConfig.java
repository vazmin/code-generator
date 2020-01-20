package com.github.vazmin.code.generator.config;

import com.github.vazmin.code.generator.api.JavaTypeResolver;
import com.github.vazmin.code.generator.engine.FreeMarkerTplEngine;
import com.github.vazmin.code.generator.engine.TplEngine;
import com.github.vazmin.code.generator.internal.types.JavaTypeResolverDefaultImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

/**
 * Created by Chwing on 2019/12/31.
 */
@Configuration
public class AppConfig {

    @Bean
    public JavaTypeResolver javaTypeResolver(AppProperties appProperties) {
        JavaTypeResolver javaTypeResolver =  new JavaTypeResolverDefaultImpl();
        javaTypeResolver.addConfigurationProperties(appProperties.getJavaTypeResolver());
        return javaTypeResolver;
    }

    @Bean
    public TplEngine tplEngine(
            FreeMarkerConfigurationFactoryBean freeMarkerConfigurationFactoryBean,
            AppProperties appProperties) {
        freemarker.template.Configuration configuration = freeMarkerConfigurationFactoryBean.getObject();
        return new FreeMarkerTplEngine(configuration, appProperties);
    }
}
