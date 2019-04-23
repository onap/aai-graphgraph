package org.onap.aai.graphgraph;


import org.onap.aai.graphgraph.reader.BasicSchemaReader;
import org.onap.aai.graphgraph.reader.SchemaRepository;
import org.springframework.beans.factory.BeanFactory;
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
        BasicSchemaReader reader = new BasicSchemaReader();
        SchemaRepository repository = new SchemaRepository(Collections.singletonList(reader));
        return repository;
    }

}


