package com.github.xxx.lpc.psi

import com.github.xxx.lpc.LPCConstants
import com.github.xxx.lpc.LPCLanguage
import com.github.xxx.lpc.LPCParserDefinition
import com.github.xxx.lpc.psi.ref.FunctionPrototypeRef
import com.github.xxx.lpc.psi.ref.FunctionRef
import com.github.xxx.lpc.psi.ref.InheritNamespaceRef
import com.github.xxx.lpc.psi.ref.VariableRef
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.PsiFileFactoryImpl
import com.intellij.psi.search.LocalSearchScope
import com.intellij.psi.search.SearchScope
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.IncorrectOperationException
import lpc.LPCParser
import org.antlr.intellij.adaptor.lexer.RuleIElementType
import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode
import org.antlr.intellij.adaptor.xpath.XPath
import org.jetbrains.annotations.NonNls

/** From doc: "Every element which can be renamed or referenced
 * needs to implement com.intellij.psi.PsiNamedElement interface."
 *
 * So, all leaf nodes that represent variables, functions, classes, or
 * whatever in your plugin language must be instances of this not just
 * LeafPsiElement.  Your ASTFactory should create this kind of object for
 * ID tokens. This node is for references *and* definitions because you can
 * highlight a function and say "jump to definition". Also we want defs
 * to be included in "find usages." Besides, there is no context information
 * in the AST factory with which you could decide whether this node
 * is a definition or a reference.
 *
 * You can click on an ID in the editor and ask for a rename for any node
 * of this type.
 */
class IdentifierPSINode(type: IElementType?, text: CharSequence?) : ANTLRPsiLeafNode(type, text),
    PsiNameIdentifierOwner {
    val usageType : String
        get() = CachedValuesManager.getCachedValue(this) {
            var elm = this
            reference?.let { ref ->
                ref.resolve()?.let {
                    if (it is IdentifierPSINode) {
                        elm = it
                    }
                }
            }

            val theType = when {
                PsiTreeUtil.getParentOfType(elm, VarNameDeclSubtree::class.java) != null -> "Variable"
                PsiTreeUtil.getParentOfType(elm, FunctionImplementationSubtree::class.java) != null -> "Function"
                else ->  "ID"
            }

            CachedValueProvider.Result.create(theType, PsiModificationTracker.MODIFICATION_COUNT)
        }

    override fun getUseScope(): SearchScope {
        val ctx = reference?.resolve()?.context ?: context ?: return super.getUseScope()

        return if (ctx is BlockSubtree) {
            LocalSearchScope(ctx)
        } else {
            LocalSearchScope(containingFile)
        }
    }

    // This needs to be here for in-place renaming
    override fun getNameIdentifier(): PsiElement? {
        return this
    }

    override fun getName(): String? {
        return text
    }

    /** Alter this node to have text specified by the argument. Do this by
     * creating a new node through parsing of an ID and then doing a
     * replace.
     */
    @Throws(IncorrectOperationException::class)
    override fun setName(@NonNls name: String): PsiElement {
        if (parent == null) return this // weird but it happened once
        /*
		IElementType elType = getParent().getNode().getElementType();
		String kind = "??? ";
		if ( elType instanceof RuleIElementType ) {
			int ruleIndex = ((RuleIElementType) elType).getRuleIndex();
			if ( ruleIndex == RULE_call_expr ) {
				kind = "call ";
			}
			else if ( ruleIndex == RULE_statement ) {
				kind = "assign ";
			}
			else if ( ruleIndex == RULE_function ) {
				kind = "func def ";
			}
		}
		System.out.println("IdentifierPSINode.setName("+name+") on "+
			                   kind+this+" at "+Integer.toHexString(this.hashCode()));
		*/

        val factory = PsiFileFactory.getInstance(project) as PsiFileFactoryImpl
        val el = factory.createElementFromText(
            name, LPCLanguage.INSTANCE, LPCParserDefinition.ID, context) ?: return this

        val nodes = XPath.findAll(LPCLanguage.INSTANCE, el, "//Identifier")

        return if (nodes.isNotEmpty()) {
            // use replace on leaves but replaceChild on ID nodes that are part of defs/decls.
            this.replace(nodes.first())
        } else this
    }

    /** Create and return a PsiReference object associated with this ID
     * node. The reference object will be asked to resolve this ref
     * by using the text of this node to identify the appropriate definition
     * site. The definition site is typically a subtree for a function
     * or variable definition whereas this reference is just to this ID
     * leaf node.
     *
     * As the AST factory has no context and cannot create different kinds
     * of PsiNamedElement nodes according to context, every ID node
     * in the tree will be of this type. So, we distinguish references
     * from definitions or other uses by looking at context in this method
     * as we have parent (context) information.
     */
    override fun getReference(): PsiReference? {
        val elType = parent.node.elementType

        // do not return a reference for the ID nodes in a definition
        if (elType is RuleIElementType) {
            when (elType.ruleIndex) {
                LPCParser.RULE_inheritance,
                LPCParser.RULE_new_name,
                LPCParser.RULE_argument_definition,
                LPCParser.RULE_single_new_local_def,
                LPCParser.RULE_new_local_def -> return null

                LPCParser.RULE_function_call_namespace -> return InheritNamespaceRef(this)

                LPCParser.RULE_efun_override,
                LPCParser.RULE_function_arrow_pointer,
                LPCParser.RULE_function_pointer,
                LPCParser.RULE_function_arrow_call,
                LPCParser.RULE_function_name -> return FunctionRef(this)

                LPCParser.RULE_function_prototype -> return FunctionPrototypeRef(this)
                else -> {
                    // Immediate context is ambiguous, so we shoot into the dark a bit
                    PsiTreeUtil.getParentOfType(this, ExpressionSubtree::class.java)?.let { expNode ->
                        PsiTreeUtil.getParentOfType(expNode, VarDefSubtree::class.java)?.let { defNode ->
                            if (defNode.lpcType == LPCConstants.TYPE_FUNCTION) {
                                return FunctionRef(this)
                            }
                        }

                        return VariableRef(this)
                    }
                }
            }
        }
        return null
    }

    @Throws(IncorrectOperationException::class)
    override fun delete() {
        val parentNode: ASTNode = parent.node!!
        val node: ASTNode = node
        val prev: ASTNode = node.treePrev
        val next: ASTNode = node.treeNext
        parentNode.removeChild(node)
        if (prev.elementType === LPCParserDefinition.WHITESPACE &&
            next.elementType === LPCParserDefinition.WHITESPACE) {
            parentNode.removeChild(next)
        }
    }
}