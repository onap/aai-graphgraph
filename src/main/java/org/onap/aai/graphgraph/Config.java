package org.onap.aai.graphgraph;


import java.util.Map;
import org.onap.aai.graphgraph.reader.BasicSchemaReader;
import org.onap.aai.graphgraph.reader.DummySchemaReader;
import org.onap.aai.graphgraph.reader.SchemaRepository;
import org.onap.aai.introspection.Introspector;
import org.onap.aai.introspection.MoxyLoader;
import org.onap.aai.introspection.exceptions.AAIUnknownObjectException;
import org.onap.aai.nodes.NodeIngestor;
import org.onap.aai.setup.SchemaVersion;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
@ComponentScan(basePackages = {
    "org.onap.aai.config",
    "org.onap.aai.setup",
    "org.onap.aai.graphgraph"
})
public class Config {


private BeanFactory beanFactory;
    @Bean
    SchemaRepository createSchemaRepository(){
        DummySchemaReader dummyReader = new DummySchemaReader();
        BasicSchemaReader reader = new BasicSchemaReader();
        SchemaRepository repository = new SchemaRepository(Collections.singletonList(reader));
        return repository;
    }

}


