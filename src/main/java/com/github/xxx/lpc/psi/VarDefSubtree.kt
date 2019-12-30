package com.github.xxx.lpc.psi

import com.github.xxx.lpc.LPCConstants
import com.github.xxx.lpc.LPCLanguage
import com.intellij.lang.ASTNode
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import org.antlr.intellij.adaptor.psi.IdentifierDefSubtree
import org.antlr.intellij.adaptor.xpath.XPath

open class VarDefSubtree(node: ASTNode, idElementTyp: IElementType) : IdentifierDefSubtree(node, idElementTyp) {
    val lpcType : String
        get() = CachedValuesManager.getCachedValue(this) {
            val nodes = XPath.findAll(LPCLanguage.INSTANCE, this, "//BasicType")

            val lpcType = if (nodes.isNotEmpty()) {
                nodes.first().text
            } else LPCConstants.TYPE_MIXED

            CachedValueProvider.Result.create(lpcType, PsiModificationTracker.MODIFICATION_COUNT)
        }
}
