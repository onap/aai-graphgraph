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
