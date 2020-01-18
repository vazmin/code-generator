package com.github.vazmin.code.generator.engine;

import com.github.vazmin.code.generator.api.IntrospectedTable;
import com.github.vazmin.code.generator.model.TplFile;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Chwing on 2020/1/1.
 */
public interface TplEngine {

    void process(Map<String, Object> model, Map<String, Set<TplFile>> targetTemplateMap,
                 IntrospectedTable table) throws IOException, TemplateException;

}
