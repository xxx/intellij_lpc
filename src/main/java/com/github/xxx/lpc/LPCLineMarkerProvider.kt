package com.github.xxx.lpc

import com.github.xxx.lpc.psi.FunctionPrototypeSubtree
import com.github.xxx.lpc.psi.IdentifierPSINode
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.Trees
import org.antlr.intellij.adaptor.xpath.XPath

class LPCLineMarkerProvider : RelatedItemLineMarkerProvider() {
    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>?>
    ) {
        if (element is PsiWhiteSpace || element is PsiComment) {
            return
        }
        if (element is IdentifierPSINode &&
            PsiTreeUtil.getParentOfType<FunctionPrototypeSubtree>(element, FunctionPrototypeSubtree::class.java) != null) {

            val file = element.containingFile
            val funcNameNodes : Collection<PsiElement?> =
                XPath.findAll(LPCLanguage.INSTANCE, file, "//function_implementation/Identifier")

            val funcNames: Map<String, PsiElement?> = Trees.toMap(funcNameNodes)

            if (funcNames.containsKey(element.text)) {
                val builder =
                    NavigationGutterIconBuilder
                        .create(LPCIcons.FILE)
                        .setTargets(funcNames[element.text])
                        .setTooltipText("Navigate to implementation")
                result.add(builder.createLineMarkerInfo(element))
            }
        }
    }
}
