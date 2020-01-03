package com.github.xxx.lpc.psi

import com.github.xxx.lpc.LPCConstants
import com.github.xxx.lpc.LPCLanguage
import com.intellij.lang.ASTNode
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import org.antlr.intellij.adaptor.xpath.XPath
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

/**
 * This node type exists to allow variable declarations to get access to the type info.
 */
class VarDefSubtree(node: ASTNode) : ANTLRPsiNode(node) {
    val lpcType : String
        get() = CachedValuesManager.getCachedValue(this) {
            // TODO arrays
            val nodes = XPath.findAll(LPCLanguage.INSTANCE, this, "//BasicType")

            val lpcType = if (nodes.isNotEmpty()) {
                nodes.first().text
            } else LPCConstants.TYPE_MIXED

            CachedValueProvider.Result.create(lpcType, PsiModificationTracker.MODIFICATION_COUNT)
        }
}
