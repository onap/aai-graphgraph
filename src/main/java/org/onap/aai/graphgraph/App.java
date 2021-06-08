/*
  ============LICENSE_START=======================================================
  org.onap.aai
  ================================================================================
  Copyright © 2019-2020 Orange Intellectual Property. All rights reserved.
  ================================================================================
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  ============LICENSE_END=========================================================
 */
package org.onap.aai.graphgraph;

import org.apache.commons.io.FileUtils;
import org.onap.aai.restclient.PropertyPasswordConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@SpringBootApplication
public class App {

    public static void main(String[] args) throws IOException {
        ArgumentParser parser = new ArgumentParser().parseArguments(args);

        if (parser.isPrintHelp()) {
            parser.printHelp();
            return;
        }

        SpringApplication app = new SpringApplication(App.class);

        if (parser.isRunLocally()) {
            copyKeystore(app);
        }
        app.addInitializers(new PropertyPasswordConfiguration());

        ConfigurableApplicationContext context = app.run(args);
        if (parser.shouldGenerateUrl()) {
            ModelExporter modelExporter = context.getBean(ModelExporter.class);
            modelExporter.writeExportedModel(modelExporter.exportModel(parser.getSchemaVersion()));
            SpringApplication.exit(context);
        }

    }

    private static void copyKeystore(SpringApplication app) throws IOException {
        Path path = Paths.get("etc/auth/aai_keystore");
        if (Files.notExists(path)) {
            FileUtils.copyInputStreamToFile(
                    Objects.requireNonNull(app.getClassLoader().getResourceAsStream("etc/auth/aai_keystore")),
                    path.toFile()
            );
        }
    }
}
