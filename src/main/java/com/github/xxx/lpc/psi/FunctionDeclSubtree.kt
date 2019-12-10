package com.github.xxx.lpc.psi

import com.github.xxx.lpc.LPCIcons
import com.intellij.ide.projectView.PresentationData
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class FunctionDeclSubtree(node: ASTNode) : ANTLRPsiNode(node) {
    override fun getPresentation(): ItemPresentation? {
        return PresentationData(
            "${children[0].text} ${children[1].text}${children[2].text}(${children[4].text})",
            "",
            LPCIcons.FILE,
            null
        )
    }
}