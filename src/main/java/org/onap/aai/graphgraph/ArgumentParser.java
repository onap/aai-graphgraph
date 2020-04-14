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

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class ArgumentParser {

    CmdLineParser parser;
    @Option(name = "-g", usage = "generates schema model for Papyrus and exits where XY is the version",
            metaVar = "vXY")
    private String schemaVersion;
    @Option(name = "-d", usage = "connect to dev version of schema-service (use JAR bundled keystore)")
    private boolean runLocally;
    @Option(name = "-h", usage = "print help and exit")
    private boolean printHelp;

    public ArgumentParser parseArguments(String[] args) {
        parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
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

    public boolean shouldGenerateUrl() {
        return schemaVersion != null;
    }

    public boolean isRunLocally() {
        return runLocally;
    }

    public boolean isPrintHelp() {
        return printHelp;
    }

    public void printHelp() {
        parser.printUsage(System.out);
    }
}
