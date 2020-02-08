package com.github.xxx.lpc

import com.github.xxx.lpc.psi.FunctionImplementationSubtree
import com.github.xxx.lpc.psi.IdentifierPSINode
import com.github.xxx.lpc.psi.VarNameDeclSubtree
import com.intellij.lang.refactoring.RefactoringSupportProvider
import com.intellij.psi.PsiElement

class LPCRefactoringSupportProvider : RefactoringSupportProvider() {
    override fun isMemberInplaceRenameAvailable(element: PsiElement, context: PsiElement?): Boolean {
        return element is IdentifierPSINode || element is VarNameDeclSubtree || element is FunctionImplementationSubtree
    }

    override fun isInplaceRenameAvailable(element: PsiElement, context: PsiElement?): Boolean {
        return element is IdentifierPSINode || element is VarNameDeclSubtree || element is FunctionImplementationSubtree
    }

    override fun isSafeDeleteAvailable(element: PsiElement): Boolean {
        return element is IdentifierPSINode || element is VarNameDeclSubtree || element is FunctionImplementationSubtree
    }
}