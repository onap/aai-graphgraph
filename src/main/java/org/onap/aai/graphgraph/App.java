package org.onap.aai.graphgraph;

import org.onap.aai.introspection.MoxyLoader;
import org.onap.aai.introspection.exceptions.AAIUnknownObjectException;
import org.onap.aai.nodes.NodeIngestor;
import org.onap.aai.setup.SchemaVersion;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class App{
    public static MoxyLoader loader;

    public static void main( String[] args ) throws AAIUnknownObjectException {
        ConfigurableApplicationContext context = SpringApplication.run(App.class, args);

        loader = new MoxyLoader(new SchemaVersion("v12"), (NodeIngestor) context.getBean("nodeIngestor"));
    }
}
