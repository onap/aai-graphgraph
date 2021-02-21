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

import org.apache.commons.io.IOUtils;
import org.mockito.Mockito;
import org.onap.aai.restclient.RestClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@TestConfiguration
public class TestConfig {

    @Bean(name = "restClient")
    public RestClient getRestClient() throws IOException {
        RestClient restClient = Mockito.mock(RestClient.class);
        Mockito.when(restClient.getGetRequest(Mockito.anyString(),
                Mockito.eq("versions"),
                Mockito.any(Map.class)))
                .thenReturn(new ResponseEntity("{\"versions\":[\"v10\"],\"edge-version\":\"v10\",\"default-version\":\"v10\",\"depth-version\":\"v10\",\"app-root-version\":\"v10\",\"related-link-version\":\"v10\",\"namespace-change-version\":\"v10\"}", HttpStatus.OK));

        //extend test in the future with the following versions:
        //{"versions":["v10","v11","v12","v13","v14","v15","v16","v17","v18","v19","v20","v21","v22","v23"],"edge-version":"v12","default-version":"v23","depth-version":"v10","app-root-version":"v11","related-link-version":"v10","namespace-change-version":"v12"}

        setupRestClientNodesResponse("v10", restClient);
        setupRestClientEdgerulesResponse("v10", restClient);

        return restClient;
    }

    private void setupRestClientNodesResponse(String version, RestClient restClient) throws IOException {
        ClassPathResource cpr = new ClassPathResource("test-data/aai-oxm-" + version + ".xml");
        byte[] bytes = IOUtils.toByteArray(cpr.getInputStream());

        Mockito.when(restClient.getGetResource(Mockito.anyString(),
                Mockito.eq("nodes?version=" + version),
                Mockito.any(Map.class)))
                .thenReturn(new ResponseEntity<Resource>(new ByteArrayResource(bytes), HttpStatus.OK));
    }

    private void setupRestClientEdgerulesResponse(String version, RestClient restClient) throws IOException {
        ClassPathResource cpr = new ClassPathResource("test-data/edge-rules-" + version + ".json");
        byte[] bytes = IOUtils.toByteArray(cpr.getInputStream());


        Mockito.when(restClient.getGetRequest(Mockito.anyString(),
                Mockito.eq("edgerules?version=" + version),
                Mockito.any(Map.class)))
                .thenReturn(new ResponseEntity(new String(bytes, StandardCharsets.UTF_8), HttpStatus.OK));
    }

}
