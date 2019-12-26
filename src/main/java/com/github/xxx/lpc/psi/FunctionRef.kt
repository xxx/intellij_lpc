package com.github.xxx.lpc.psi

import com.intellij.psi.PsiElement

/** A reference object associated with (referring to) a IdentifierPSINode
 * underneath a CallSubtree root.
 */
class FunctionRef(element: IdentifierPSINode) : LPCElementRef(element) {
    override fun isDefSubtree(def: PsiElement?): Boolean {
        return def is FunctionImplementationSubtree
    }
}