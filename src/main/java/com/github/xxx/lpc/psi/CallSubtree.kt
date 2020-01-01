package com.github.xxx.lpc.psi

import com.github.xxx.lpc.LPCLanguage
import com.intellij.lang.ASTNode
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import org.antlr.intellij.adaptor.xpath.XPath

class CallSubtree(node: ASTNode) : ANTLRPsiNode(node) {
    val functionName : String?
        get() {
            val ids = XPath.findAll(LPCLanguage.INSTANCE, this, "//Identifier")
            return when (ids.size) {
                1 -> ids.first().text
                2 -> ids.last().text
                else -> null
            }
        }

    val namespace : String?
        get() {
            val ids = XPath.findAll(LPCLanguage.INSTANCE, this, "//Identifier")
            return when (ids.size) {
                2 -> ids.first().text
                else -> null
            }
        }
}