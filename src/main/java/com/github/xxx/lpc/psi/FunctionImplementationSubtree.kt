package com.github.xxx.lpc.psi

import com.github.xxx.lpc.LPCIcons
import com.github.xxx.lpc.LPCLanguage
import com.intellij.ide.projectView.PresentationData
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.search.LocalSearchScope
import com.intellij.psi.search.SearchScope
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.SymtabUtils
import org.antlr.intellij.adaptor.psi.IdentifierDefSubtree
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.antlr.intellij.adaptor.xpath.XPath

/** A subtree associated with a function definition.
 * Its scope is the set of arguments.
 */
class FunctionImplementationSubtree(node: ASTNode, idElementType: IElementType) :
    IdentifierDefSubtree(node, idElementType), ScopeNode {
    override fun resolve(element: PsiNamedElement): PsiElement? {
        		// println(
                //     KClass::class.simpleName+
        		// 	                   ".resolve("+element.name+
        		// 	                   " at "+Integer.toHexString(element.hashCode())+")")
        return SymtabUtils.resolve(
            this, LPCLanguage.INSTANCE,
            element, "//function_implementation/Identifier"
        ) ?: SymtabUtils.resolve(
            this, LPCLanguage.INSTANCE,
            element, "//function_implementation/argument//Identifier"
        )
    }

    override fun getNameIdentifier(): PsiElement? {
        val ids : Collection<PsiElement?> = XPath.findAll(LPCLanguage.INSTANCE, this,
            "//function_implementation/Identifier")
        if (ids.isNotEmpty()) {
            return ids.first()
        }

        return null
    }

    override fun getPresentation(): ItemPresentation? {
        return PresentationData(
            "${children[0].text} ${children[1].text}${children[2].text}(${children[4].text})",
            "",
            LPCIcons.C_FILE,
            null
        )
    }

    override fun getUseScope(): SearchScope {
        return if (context != null) {
            LocalSearchScope(context!!)
        } else {
            super.getUseScope()
        }
    }
}