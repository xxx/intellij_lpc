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

    /** Return null since a file scope has no enclosing scope. It is
     * not itself in a scope.
     */
    override fun getContext(): ScopeNode? {
        return null
    }

    override fun resolve(element: PsiNamedElement): PsiElement? {
        return if (PsiTreeUtil.getParentOfType<CallSubtree>(element, CallSubtree::class.java) != null ||
            PsiTreeUtil.getParentOfType<FunctionPrototypeSubtree>(element, FunctionPrototypeSubtree::class.java) != null) {

            SymtabUtils.resolve(this, LPCLanguage.INSTANCE,
                element, "//function_implementation/Identifier") ?:
            SymtabUtils.resolve(this, LPCLanguage.INSTANCE,
                element, "//function_prototype/Identifier")
        } else SymtabUtils.resolve(this, LPCLanguage.INSTANCE,
            element, "//name_list//Identifier")
    }
}
