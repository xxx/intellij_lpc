package com.github.xxx.lpc

import com.intellij.lang.Language
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.antlr.intellij.adaptor.psi.Trees
import org.antlr.intellij.adaptor.xpath.XPath

object LPCUtils {
    /**
     * basically SymtabUtils.resolve, but instead of assuming the parent is what we want,
     * we look for an ancestor of a particular type
     */
    fun <T : PsiElement?> symtabResolveToType(
        scope : ScopeNode,
        language : Language,
        namedElement : PsiNamedElement,
        xpathToIDNodes : String,
        myClass : Class<out T>
    ) : PsiElement? {

        val defIDNodes =
            XPath.findAll(language, scope, xpathToIDNodes)
        val id = namedElement.name
        val idNode =
            Trees.toMap(defIDNodes)[id] // Find identifier node of variable definition

        if (idNode != null) {
            val ret = PsiTreeUtil.getParentOfType(idNode, myClass)
            if (ret != null) {
                return ret
            }
        }

        val context = scope.context

        return if (context != null) {
            context.resolve(namedElement)!!
        } else null
    }
}
