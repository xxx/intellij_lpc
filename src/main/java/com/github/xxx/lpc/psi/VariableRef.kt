package com.github.xxx.lpc.psi

import com.intellij.psi.PsiElement

class VariableRef(element: IdentifierPSINode) : LPCElementRef(element) {
    override fun isDefSubtree(def: PsiElement?): Boolean {
        return def is VarDefSubtree
    }
}