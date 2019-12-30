package com.github.xxx.lpc.psi.ref

import com.github.xxx.lpc.psi.IdentifierPSINode
import com.github.xxx.lpc.psi.VarDefSubtree
import com.intellij.psi.PsiElement

class VariableRef(element: IdentifierPSINode) : LPCElementRef(element) {
    override fun isDefSubtree(def: PsiElement?): Boolean {
        return def is VarDefSubtree
    }
}