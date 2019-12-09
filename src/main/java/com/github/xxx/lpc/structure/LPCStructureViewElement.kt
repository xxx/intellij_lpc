package com.github.xxx.lpc.structure

import com.github.xxx.lpc.LPCIcons
import com.github.xxx.lpc.LPCLanguage
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiFile
import org.antlr.intellij.adaptor.xpath.XPath
import com.github.xxx.lpc.LPCUtils.Companion.matchingRule
import lpc.LPCParser

class LPCStructureViewElement(private val element: NavigatablePsiElement) : StructureViewTreeElement,
    SortableTreeElement {
    override fun getValue(): Any {
        return element
    }

    override fun navigate(requestFocus: Boolean) {
        element.navigate(requestFocus)
    }

    override fun canNavigate(): Boolean {
        return element.canNavigate()
    }

    override fun canNavigateToSource(): Boolean {
        return element.canNavigateToSource()
    }

    override fun getAlphaSortKey(): String {
        val name = element.name
        return name ?: ""
    }

    override fun getPresentation(): ItemPresentation {
        val presentation = element.presentation
        if (presentation != null) {
            return presentation
        }

        if (matchingRule(element) == LPCParser.RULE_function_decl) {
            val kids = element.children

            return PresentationData(
                "${kids[0].text} ${kids[1].text}${kids[2].text}(${kids[4].text})",
                "",
                LPCIcons.FILE,
                null
            )
        }

        return PresentationData()
    }

    override fun getChildren(): Array<TreeElement> {
        if (element is PsiFile) {
            val functions =
                XPath.findAll(LPCLanguage.INSTANCE, element, "//function_implementation/function_decl")
            val treeElements: MutableList<TreeElement> = ArrayList(functions.size)
            functions.forEach {
                treeElements.add(LPCStructureViewElement(it as NavigatablePsiElement))
            }
            return treeElements.toTypedArray()
        }

        return arrayOf()
    }
}
