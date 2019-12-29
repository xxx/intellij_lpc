package com.github.xxx.lpc

import com.github.xxx.lpc.psi.IdentifierPSINode
import com.intellij.lang.refactoring.RefactoringSupportProvider
import com.intellij.psi.PsiElement

class LPCRefactoringSupportProvider : RefactoringSupportProvider() {
    override fun isMemberInplaceRenameAvailable(element: PsiElement, context: PsiElement?): Boolean {
        return element is IdentifierPSINode
    }

    override fun isInplaceRenameAvailable(element: PsiElement, context: PsiElement?): Boolean {
        return element is IdentifierPSINode
    }

    override fun isSafeDeleteAvailable(element: PsiElement): Boolean {
        return element is IdentifierPSINode
    }
}