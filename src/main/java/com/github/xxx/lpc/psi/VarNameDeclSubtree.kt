package com.github.xxx.lpc.psi

import com.github.xxx.lpc.LPCLanguage
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.psi.IdentifierDefSubtree
import org.antlr.intellij.adaptor.xpath.XPath

/**
 * This class is the naming node for variables. We differentiate between
 * this and VarDefSubtree in cases where an entire list of variables are
 * being declared at once. There will be one of these nodes per name,
 * but only a single VarDefSubtree for the entire list.
 */
open class VarNameDeclSubtree(node: ASTNode, idElementTyp: IElementType) : IdentifierDefSubtree(node, idElementTyp) {
    override fun getNameIdentifier(): PsiElement? {
        val ids : Collection<PsiElement?> = XPath.findAll(
            LPCLanguage.INSTANCE, this,
            "//Identifier")
        if (ids.isNotEmpty()) {
            return ids.first()
        }

        return null
    }
}
