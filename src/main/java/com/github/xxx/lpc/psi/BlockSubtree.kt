package com.github.xxx.lpc.psi

import com.github.xxx.lpc.LPCLanguage
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.SymtabUtils
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import org.antlr.intellij.adaptor.psi.ScopeNode

class BlockSubtree(node: ASTNode) : ANTLRPsiNode(node), ScopeNode {
    override fun resolve(element: PsiNamedElement): PsiElement? {
        //		System.out.println(getClass().getSimpleName()+
        //		                   ".resolve("+element.getName()+
        //		                   " at "+Integer.toHexString(element.hashCode())+")");\
        val node = SymtabUtils.resolve(
            this, LPCLanguage.INSTANCE,
            element, "/block//local_declare_statement//new_local_name"
        )

        // This is an obnoxious hack because of the grammar.
        // We have both prototypes and implementations to have to deal with, and it's nice to be able to factor
        // the duplication into a single rule. It just breaks right here though.
        if (node is FunctionDeclSubtree) {
            return PsiTreeUtil.getParentOfType(element, FunctionImplementationSubtree::class.java)
        }

        return node
    }
}