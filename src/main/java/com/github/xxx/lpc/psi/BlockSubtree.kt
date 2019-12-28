package com.github.xxx.lpc.psi

import com.github.xxx.lpc.LPCLanguage
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import org.antlr.intellij.adaptor.SymtabUtils
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import org.antlr.intellij.adaptor.psi.ScopeNode

class BlockSubtree(node: ASTNode) : ANTLRPsiNode(node), ScopeNode {
    override fun resolve(element: PsiNamedElement): PsiElement? {
        //		System.out.println(getClass().getSimpleName()+
        //		                   ".resolve("+element.getName()+
        //		                   " at "+Integer.toHexString(element.hashCode())+")");\
        return SymtabUtils.resolve(
            this, LPCLanguage.INSTANCE,
            element, "/block//local_declare_statement//Identifier"
        )
    }
}