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
import org.antlr.intellij.adaptor.xpath.XPath
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
        val callSubtree = PsiTreeUtil.getParentOfType(element, CallSubtree::class.java)
        return if (callSubtree != null) {
            if (callSubtree.namespace != null) {
                /*
                    Inheritance namespaces can only be defined within file's global namespace,
                    so only check there, and do not attempt to walk up the inheritance chain.
                    Additionally, namespaces and variables are not shared, so it's valid LPC to
                    have a variable with the same name as an inheritance namespace.
                */
                val nodes = XPath.findAll(LPCLanguage.INSTANCE, this, "//inheritance/Identifier")

                // TODO: handle namespaced functions themselves. They resolve to the namespace at the moment.
                return nodes.find {
                    it.text == callSubtree.namespace
                }
            } else {
                resolveFunctionId(element)
            }
        } else if (PsiTreeUtil.getParentOfType(element, FunctionPrototypeSubtree::class.java) != null ||
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
