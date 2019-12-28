package com.github.xxx.lpc.psi

import com.github.xxx.lpc.LPCLanguage
import com.intellij.psi.PsiElement
import com.intellij.util.containers.toArray
import org.antlr.intellij.adaptor.xpath.XPath

/** A reference object associated with (referring to) a IdentifierPSINode
 * underneath a CallSubtree root.
 */
class FunctionRef(element: IdentifierPSINode) : LPCElementRef(element) {
    override fun isDefSubtree(def: PsiElement?): Boolean {
        return def is FunctionImplementationSubtree
    }

    // override fun getVariants(): Array<Any> {
    //     val collection = XPath.findAll(LPCLanguage.INSTANCE, element.containingFile,
    //         "//function_implementation/Identifier").filterNotNull()
    //
    //     return collection.toArray(arrayOf<Any>(collection.size))
    // }
}