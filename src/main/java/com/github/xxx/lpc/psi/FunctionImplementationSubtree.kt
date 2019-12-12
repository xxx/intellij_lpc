package com.github.xxx.lpc.psi

import com.github.xxx.lpc.LPCLanguage
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.SymtabUtils
import org.antlr.intellij.adaptor.psi.IdentifierDefSubtree
import org.antlr.intellij.adaptor.psi.ScopeNode

/** A subtree associated with a function definition.
 * Its scope is the set of arguments.
 */
class FunctionImplementationSubtree(node: ASTNode, idElementType: IElementType) :
    IdentifierDefSubtree(node, idElementType), ScopeNode {
    override fun resolve(element: PsiNamedElement): PsiElement? {
        //		System.out.println(getClass().getSimpleName()+
        //			                   ".resolve("+myElement.getName()+
        //			                   " at "+Integer.toHexString(myElement.hashCode())+")");
        return SymtabUtils.resolve(
            this, LPCLanguage.INSTANCE,
            element, "//function_implementation//identifier"
        )
    }
}