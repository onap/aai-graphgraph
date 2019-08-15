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

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class ArgumentParser {
  @Option(name = "-g", usage = "schema to be generated", metaVar = "schema")
  private String schemaVersion;

  public ArgumentParser parseArguments(String [] args){
    CmdLineParser parser = new CmdLineParser(this);

    try {
      parser.parseArgument(args);
    } catch( CmdLineException e ) {
      System.err.println(e.getMessage());
      parser.printUsage(System.err);
    }

    return this;
  }

  public String getSchemaVersion() {
    return schemaVersion;
  }

  public void setSchemaVersion(String schemaVersion) {
    this.schemaVersion = schemaVersion;
  }

  public boolean shoudGenerateUml(){
    return schemaVersion != null;
  }
}
