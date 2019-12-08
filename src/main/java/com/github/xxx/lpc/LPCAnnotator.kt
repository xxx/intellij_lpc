package com.github.xxx.lpc

import com.google.wireless.android.sdk.stats.StudioPatchUpdaterEvent.IssueDialog.Issue
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.antlr.intellij.adaptor.lexer.RuleIElementType
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import org.antlr.intellij.adaptor.psi.Trees
import org.antlr.intellij.adaptor.xpath.XPath
import java.util.ArrayList

class LPCAnnotator : Annotator {
    // this shares a single instance, so no state outside of the function.

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        println(element);
        // if (element is ANTLRPsiNode) {
            // (element.node.elementType as RuleIElementType).ruleIndex == LPCParser.RULE_function_name
            // println(element.node.elementType.toString())
            // val file = element.containingFile;
            // val funcNameNodes : Collection<PsiElement?> =
            //     XPath.findAll(LPCLanguage.INSTANCE, file, "//function_definition/identifier")
            // val funcCallNameNodes: Collection<PsiElement?> =
            //     XPath.findAll(LPCLanguage.INSTANCE, file, "//function_call/function_name") +
            //         XPath.findAll(LPCLanguage.INSTANCE, file, "//function_arrow_call/identifier")
            //
            // val funcNames: Map<String, PsiElement?> = Trees.toMap(funcNameNodes)
            // val funcCalls: Map<String, PsiElement> = Trees.toMap(funcCallNameNodes)
            //
            // for (name in funcCalls.keys) {
            //     if (!funcNames.containsKey(name)) {
            //
            //         val range = TextRange(0, element.textRange.endOffset - 1)
            //         holder.createErrorAnnotation(range, "Unknown function: ${element.text}")
            //     }
            // }
            // println(element.text)
            // println(funcNameNodes.map { it?.text })
        // }
    //     if (element is PsiLiteralExpression) {
    //         val literalExpression: PsiLiteralExpression = element as PsiLiteralExpression
    //         val value = if (literalExpression.getValue() is String) literalExpression.getValue() else null
    //         if (value != null && value.startsWith("simple" + ":")) {
    //             val project = element.project
    //             val key = value.substring(7)
    //             val properties: List<SimpleProperty> = LPCUtil.findProperties(project, key)
    //             if (properties.size == 1) {
    //                 val range = TextRange(element.textRange.startOffset + 8, element.textRange.endOffset - 1)
    //                 val annotation = holder.createInfoAnnotation(range, null)
    //                 annotation.setTextAttributes(DefaultLanguageHighlighterColors.LINE_COMMENT)
    //             } else if (properties.size == 0) {
    //                 val range = TextRange(element.textRange.startOffset + 8, element.textRange.endOffset - 1)
    //                 holder.createErrorAnnotation(range, "Unresolved property")
    //             }
    //         }
    //     }
    }
}