package com.github.xxx.lpc

import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import org.antlr.intellij.adaptor.lexer.RuleIElementType
import org.antlr.intellij.adaptor.lexer.TokenIElementType
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class LPCUtils {
    companion object {
        fun matchingRule(element : PsiElement) : Int {
            return when (element) {
                is ANTLRPsiNode -> {
                    (element.node.elementType as RuleIElementType).ruleIndex
                }
                is LeafPsiElement -> {
                    (element.node.elementType as TokenIElementType).antlrTokenType
                }
                else -> {
                    element.node.elementType.index.toInt()
                    // throw(Exception("some other type received: $element"))
                }
            }
        }
    }
}