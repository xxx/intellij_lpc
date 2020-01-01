package com.github.xxx.lpc.psi.ref

import com.github.xxx.lpc.psi.IdentifierPSINode
import com.github.xxx.lpc.psi.InheritSubtree
import com.intellij.psi.PsiElement
import org.antlr.intellij.adaptor.psi.ScopeNode

class InheritNamespaceRef(element: IdentifierPSINode) : LPCElementRef(element) {
    override fun isDefSubtree(def: PsiElement?): Boolean {
        return def is InheritSubtree
    }

    override fun resolve(): PsiElement? {
        // Inheritance namespaces can only be defined at the file level, so skip interim scopes.
        val scope = myElement!!.containingFile as ScopeNode? ?: return null
        return scope.resolve(myElement)
    }
}