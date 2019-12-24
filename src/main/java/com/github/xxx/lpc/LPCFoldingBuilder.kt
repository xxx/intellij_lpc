package com.github.xxx.lpc

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.antlr.intellij.adaptor.xpath.XPath
import java.util.ArrayList

class LPCFoldingBuilder : FoldingBuilderEx() {
    override fun buildFoldRegions(
        root: PsiElement,
        document: Document,
        quick: Boolean
    ): Array<FoldingDescriptor> {
        val file = root.containingFile
        val blocks : Collection<PsiElement?> = XPath.findAll(LPCLanguage.INSTANCE, file, "//block")

        val descriptors: MutableList<FoldingDescriptor> = ArrayList()
        blocks.filterNotNull().forEach {
            val startOffset = it.textRange.startOffset + 1
            val endOffset = it.textRange.endOffset - 1
            if (endOffset - startOffset > 0) {
                descriptors.add(FoldingDescriptor(it, TextRange(startOffset, endOffset)))
            }
        }
        return descriptors.toTypedArray()
    }

    override fun getPlaceholderText(node: ASTNode): String? {
        return "..."
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return false
    }
}

