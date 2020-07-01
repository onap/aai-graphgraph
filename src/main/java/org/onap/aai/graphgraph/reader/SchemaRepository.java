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
package org.onap.aai.graphgraph.reader;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SchemaRepository {

    private final List<SchemaReader> readers;

    public SchemaRepository(List<SchemaReader> readers) {
        this.readers = readers;
    }

    public List<String> getAllSchemaNames() {
        return readers.stream().map(SchemaReader::getSchemaName).collect(Collectors.toList());
    }

    public SchemaReader getSchemaReader(String schemaName) {
        Optional<SchemaReader> reader = readers.stream().filter(r -> schemaName.equals(r.getSchemaName())).findFirst();
        if (!reader.isPresent()) {
            throw new IllegalArgumentException("Schema " + schemaName + " not found");
        }

        return reader.get();
    }
}
