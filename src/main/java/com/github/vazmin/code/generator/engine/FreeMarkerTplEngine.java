package com.github.vazmin.code.generator.engine;

import com.github.vazmin.code.generator.config.AppProperties;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * FreeMarker Template Engine
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
    void process(Map<String, Object> model, File file, String tplPath) throws IOException, TemplateException {
        FileWriter writer = null;
        try {
            Template template = cfg.getTemplate(tplPath);
            writer =  new FileWriter(file);
            template.process(model, writer);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }



}
