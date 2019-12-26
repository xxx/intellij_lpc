package com.github.xxx.lpc.psi

import com.intellij.psi.PsiElement

/**
 * Executive decision - the prototype references the implementation, so that's
 * the source of truth for these IDs
 */
class FunctionPrototypeRef(element: IdentifierPSINode) : LPCElementRef(element) {
    override fun isDefSubtree(def: PsiElement?): Boolean {
        return def is FunctionImplementationSubtree
    }
}