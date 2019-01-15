package org.onap.aai.graphgraph;


import org.onap.aai.graphgraph.reader.DummySchemaReader;
import org.onap.aai.graphgraph.reader.SchemaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
@ComponentScan("org.onap.aai.graphgraph")
public class Config {

    @Bean
    SchemaRepository createSchemaRepository(){
        DummySchemaReader dummyReader = new DummySchemaReader();
        SchemaRepository repository = new SchemaRepository(Collections.singletonList(dummyReader));
        return repository;
    }

}


