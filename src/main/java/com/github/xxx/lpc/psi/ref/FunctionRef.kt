package com.github.xxx.lpc.psi.ref

import com.github.xxx.lpc.psi.FunctionImplementationSubtree
import com.github.xxx.lpc.psi.IdentifierPSINode
import com.intellij.psi.PsiElement

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