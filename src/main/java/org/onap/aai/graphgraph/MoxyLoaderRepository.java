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

import org.onap.aai.introspection.MoxyLoader;
import org.onap.aai.nodes.NodeIngestor;
import org.onap.aai.setup.SchemaVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MoxyLoaderRepository {

    @Autowired
    private Config appConfiguration;

    @Autowired
    private NodeIngestor nodeIngestor;

    private Map<String, MoxyLoader> moxyLoaders = new HashMap<>();

    public Map<String, MoxyLoader> getMoxyLoaders() {
        return moxyLoaders;
    }

    @PostConstruct
    public void initMoxyLoaders() {
        List<String> schemaVersions = appConfiguration.getSchemaVersions();
        for (String version : schemaVersions) {
            moxyLoaders.put(version, new MoxyLoader(new SchemaVersion(version), nodeIngestor));
        }
    }

}
