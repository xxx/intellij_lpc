package com.github.xxx.lpc.psi

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.PsiReferenceBase
import com.intellij.util.IncorrectOperationException
import org.antlr.intellij.adaptor.psi.ScopeNode
import kotlin.reflect.KClass

abstract class LPCElementRef(element: IdentifierPSINode) :
    PsiReferenceBase<IdentifierPSINode?>(element, TextRange(0, element.text.length)) {
    override fun getVariants(): Array<Any> {
        @Suppress("UNCHECKED_CAST")
        return arrayOfNulls<Any>(0) as Array<Any>
    }

    /** Change the REFERENCE's ID node (not the targeted def's ID node)
     * to reflect a rename.
     *
     * Without this method, we get an error ("Cannot find manipulator...").
     *
     * getElement() refers to the identifier node that references the definition.
     */
    @Throws(IncorrectOperationException::class)
    override fun handleElementRename(newElementName: String): PsiElement { //		System.out.println(getClass().getSimpleName()+".handleElementRename("+myElement.getName()+"->"+newElementName+
//			                   ") on "+myElement+" at "+Integer.toHexString(myElement.hashCode()));
        return myElement!!.setName(newElementName)
    }

    /** Resolve a reference to the definition subtree (subclass of
     * IdentifierDefSubtree), do not resolve to the ID child of that
     * definition subtree root.
     */
    override fun resolve(): PsiElement? {
        //		System.out.println(getClass().getSimpleName()+
//		                   ".resolve("+myElement.getName()+
//		                   " at "+Integer.toHexString(myElement.hashCode())+")");
        val scope = myElement!!.context as ScopeNode? ?: return null
        return scope.resolve(myElement)
    }

    override fun isReferenceTo(def: PsiElement): Boolean {
        // return def == resolve()

        var myDef = def
        val refName = myElement!!.name
        System.out.println(KClass::class.simpleName+".isReferenceTo("+refName+"->"+def.text +")")
        // sometimes def comes in pointing to ID node itself. depends on what you click on
        if (myDef is IdentifierPSINode) {
            val defSubtree = findDefSubtree(myDef.parent)
            if (defSubtree != null) {
                myDef = defSubtree
            }
        }
        if (isDefSubtree(myDef)) {
            val id = (myDef as PsiNameIdentifierOwner).nameIdentifier
            val defName = id?.text
            return refName != null && refName == defName
        }

        println("We're here returning false?")
        return false
    }

    /**
     * Get to the root of the defining subtree
     */
    private fun findDefSubtree(def: PsiElement?): PsiElement? {
        if (def == null) return null
        var current = def

        while (current != null && !isDefSubtree(current)) {
            current = current.parent
        }

        return current
    }

    /** Is the targeted def a subtree associated with this ref's kind of node?
     * E.g., for a variable def, this should return true for VardefSubtree.
     */
    abstract fun isDefSubtree(def: PsiElement?): Boolean
}