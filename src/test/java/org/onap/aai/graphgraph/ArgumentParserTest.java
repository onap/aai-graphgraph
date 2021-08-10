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

import org.junit.Assert;
import org.junit.Test;

public class ArgumentParserTest {

    @Test
    public void parsePrintHelpTest() {
        ArgumentParser argumentParser = new ArgumentParser();
        argumentParser.parseArguments(new String[] {"-h"});
        Assert.assertTrue(argumentParser.isPrintHelp());
    }

    @Test
    public void parseLocalRunTest() {
        ArgumentParser argumentParser = new ArgumentParser();
        argumentParser.parseArguments(new String[] {"-d"});
        Assert.assertTrue(argumentParser.isRunLocally());
        Assert.assertFalse(argumentParser.isPrintHelp());
    }

    @Test
    public void parseGenerateModelTest() {
        ArgumentParser argumentParser = new ArgumentParser();
        argumentParser.parseArguments(new String[] {"-g", "v15"});
        Assert.assertFalse(argumentParser.isRunLocally());
        Assert.assertFalse(argumentParser.isPrintHelp());
        Assert.assertTrue(argumentParser.shouldGenerateUrl());
        Assert.assertEquals("v15", argumentParser.getSchemaVersion());
    }
}
