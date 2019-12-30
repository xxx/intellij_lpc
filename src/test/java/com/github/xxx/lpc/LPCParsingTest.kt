package com.github.xxx.lpc

import com.intellij.testFramework.ParsingTestCase

class LPCParsingTest : ParsingTestCase("", "c", LPCParserDefinition()) {
    fun testParsingTestFunction() {
        doTest(true)
    }

    fun testParsingTestComplex() {
        doTest(true)
    }

    override fun getTestDataPath(): String {
        return "testData/parser"
    }

    override fun skipSpaces(): Boolean {
        return true
    }

    override fun includeRanges(): Boolean {
        return true
    }
}