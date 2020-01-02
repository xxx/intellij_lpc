package com.github.xxx.lpc.psi.ref

import com.github.xxx.lpc.psi.IdentifierPSINode
import com.github.xxx.lpc.psi.InheritSubtree
import com.intellij.psi.PsiElement
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import org.antlr.intellij.adaptor.psi.ScopeNode

class InheritNamespaceRef(element: IdentifierPSINode) : LPCElementRef(element) {
    override fun isDefSubtree(def: PsiElement?): Boolean {
        return def is InheritSubtree
    }

    override fun resolve(): PsiElement? {
        return CachedValuesManager.getCachedValue(myElement as PsiElement) {
            // Inheritance namespaces can only be defined at the file level, so skip interim scopes.
            val result = (myElement.containingFile as ScopeNode?)?.resolve(myElement)
            CachedValueProvider.Result.create(result, PsiModificationTracker.MODIFICATION_COUNT)
        }
    }
}