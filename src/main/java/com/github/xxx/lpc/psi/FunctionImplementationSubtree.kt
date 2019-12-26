package com.github.xxx.lpc.psi

import com.github.xxx.lpc.LPCLanguage
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.SymtabUtils
import org.antlr.intellij.adaptor.psi.IdentifierDefSubtree
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.antlr.intellij.adaptor.xpath.XPath

/** A subtree associated with a function definition.
 * Its scope is the set of arguments.
 */
class FunctionImplementationSubtree(node: ASTNode, idElementType: IElementType) :
    IdentifierDefSubtree(node, idElementType), ScopeNode {
    override fun resolve(element: PsiNamedElement): PsiElement? {
        		// println(
                //     KClass::class.simpleName+
        		// 	                   ".resolve("+element.name+
        		// 	                   " at "+Integer.toHexString(element.hashCode())+")")
        return SymtabUtils.resolve(
            this, LPCLanguage.INSTANCE,
            element, "//function_implementation/function_decl/identifier"
        )
    }

    override fun getNameIdentifier(): PsiElement? {
        val ids : Collection<PsiElement?> = XPath.findAll(LPCLanguage.INSTANCE, this,
            "//function_decl/identifier")
        if (ids.isNotEmpty()) {
            return PsiTreeUtil.findChildOfType(this, IdentifierPSINode::class.java)
        }

        return null
    }
}