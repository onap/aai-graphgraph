/**
 * ============LICENSE_START=======================================================
 * org.onap.aai
 * ================================================================================
 * Copyright © 2019 Orange Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */
package org.onap.aai.graphgraph;

import java.util.LinkedList;
import java.util.List;
import org.onap.aai.graphgraph.reader.BasicSchemaReader;
import org.onap.aai.graphgraph.reader.SchemaReader;
import org.onap.aai.graphgraph.reader.SchemaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
    "org.onap.aai.config",
    "org.onap.aai.setup",
    "org.onap.aai.graphgraph"
})
public class Config {

    @Value("${schema.version.list}")
    String schemaVersions;

    @Bean
    SchemaRepository createSchemaRepository(){
        List<SchemaReader> readers = new LinkedList<>();
        for (String s : schemaVersions.split(",")) {
            readers.add(new BasicSchemaReader(s));
        }
        return new SchemaRepository(readers);
    }
}

