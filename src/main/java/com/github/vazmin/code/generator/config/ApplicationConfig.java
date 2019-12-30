package com.github.vazmin.code.generator.config;

import com.github.vazmin.code.generator.api.JavaTypeResolver;
import com.github.vazmin.code.generator.internal.types.JavaTypeResolverDefaultImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Chwing on 2019/12/31.
 */
@Configuration
public class ApplicationConfig {

    @Bean
    public JavaTypeResolver javaTypeResolver() {
        return new JavaTypeResolverDefaultImpl();
    }


}
