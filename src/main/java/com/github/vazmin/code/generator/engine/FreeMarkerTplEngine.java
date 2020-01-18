package com.github.vazmin.code.generator.engine;

import com.github.vazmin.code.generator.api.IntrospectedTable;
import com.github.vazmin.code.generator.config.AppProperties;
import com.github.vazmin.code.generator.model.TplFile;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Created by Chwing on 2020/1/1.
 */
public class FreeMarkerTplEngine extends AbstractTplEngine {

    private static final Logger log = LoggerFactory.getLogger(FreeMarkerTplEngine.class);

    private Configuration cfg;

    public FreeMarkerTplEngine(Configuration configuration, AppProperties appProperties) {
        super(appProperties);
        this.cfg = configuration;
    }

    @Override
    public void process(Map<String, Object> model, Map<String, Set<TplFile>> targetTemplateMap,
                        IntrospectedTable table)
            throws IOException, TemplateException {
        FileWriter writer = null;
        for (Map.Entry<String, Set<TplFile>> entry: targetTemplateMap.entrySet()) {
            for (TplFile tplFile : entry.getValue()) {

                log.debug("{}, {}", entry.getKey(), tplFile.getTplPath());
                try {
                    Template template = cfg.getTemplate(tplFile.getTplPath());
                    writer =  new FileWriter(new File(getOutputFileName(tplFile.getTplName(), table)));
                    template.process(model, writer);
                } finally {
                    if (writer != null) {
                        writer.close();
                    }
                }
            }
        }

    }



}
