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
import javax.swing.Icon

class LPCPSIFileRoot(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, LPCLanguage.INSTANCE), ScopeNode {
    override fun getFileType(): FileType {
        return LPCFileType.INSTANCE
    }

    override fun toString(): String {
        return "LPC Language file"
    }

    override fun getIcon(flags: Int): Icon? {
        return LPCIcons.FILE
    }

    /** Return null since a file scope has no enclosing scope. It is
     * not itself in a scope.
     */
    override fun getContext(): ScopeNode? {
        return null
    }

    override fun resolve(element: PsiNamedElement): PsiElement? {
        return if (element.parent is CallSubtree) {
            SymtabUtils.resolve(this, LPCLanguage.INSTANCE,
                    element, "/script/function/ID")
        } else SymtabUtils.resolve(this, LPCLanguage.INSTANCE,
                element, "/script/vardef/ID")
    }
}
