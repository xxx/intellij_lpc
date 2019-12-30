package com.github.xxx.lpc.psi

import com.github.xxx.lpc.LPCFileType
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import org.antlr.intellij.adaptor.SymtabUtils
import org.antlr.intellij.adaptor.psi.ScopeNode
import com.github.xxx.lpc.LPCIcons
import com.github.xxx.lpc.LPCLanguage
import com.intellij.psi.util.PsiTreeUtil
import javax.swing.Icon

class LPCPSIFileRoot(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, LPCLanguage.INSTANCE), ScopeNode {
    override fun getFileType(): FileType {
        return LPCFileType.INSTANCE
    }

    override fun toString(): String {
        return "LPC Language file"
    }

    override fun getIcon(flags: Int): Icon? {
        return when(virtualFile.extension) {
            "c" -> LPCIcons.C_FILE
            "h" -> LPCIcons.H_FILE
            "o" -> LPCIcons.O_FILE
            else -> null
        }
    }

    // TODO - try to resolve for each file inherited from
    override fun getContext(): ScopeNode? {
        return null
    }

    override fun resolve(element: PsiNamedElement): PsiElement? {
        return if (PsiTreeUtil.getParentOfType(element, CallSubtree::class.java) != null ||
            PsiTreeUtil.getParentOfType(element, FunctionPrototypeSubtree::class.java) != null ||
            PsiTreeUtil.getParentOfType(element, FunctionPointerSubtree::class.java) != null) {

            resolveFunctionId(element)
        } else if (PsiTreeUtil.getParentOfType(element, ExpressionSubtree::class.java) != null) {
            resolveFunctionId(element) ?: resolveVarId(element)
        } else resolveVarId(element)
    }

    private fun resolveVarId(element: PsiNamedElement) = SymtabUtils.resolve(
        this, LPCLanguage.INSTANCE,
        element, "//name_list//Identifier"
    )

    private fun resolveFunctionId(element: PsiNamedElement): PsiElement? {
        return SymtabUtils.resolve(
            this, LPCLanguage.INSTANCE,
            element, "//function_implementation/Identifier"
        ) ?: SymtabUtils.resolve(
            this, LPCLanguage.INSTANCE,
            element, "//function_prototype/Identifier"
        )
    }
}
