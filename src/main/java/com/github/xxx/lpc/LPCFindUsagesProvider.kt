package com.github.xxx.lpc

import com.github.xxx.lpc.psi.FunctionImplementationSubtree
import com.intellij.lang.HelpID
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement

class LPCFindUsagesProvider : FindUsagesProvider {
    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        return getDescriptiveName(element)
    }

    override fun getDescriptiveName(element: PsiElement): String {
        return if (element is PsiNamedElement) {
            StringUtil.notNullize(element.name)
        } else element.text
    }

    override fun getType(element: PsiElement): String {
        return if (element is FunctionImplementationSubtree) {
            "Function"
        } else "thinger"
    }

    override fun getHelpId(psiElement: PsiElement): String? {
        return HelpID.FIND_OTHER_USAGES
    }

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return psiElement is PsiNamedElement;
    }
}