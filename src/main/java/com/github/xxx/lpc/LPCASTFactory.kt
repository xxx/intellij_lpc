package com.github.xxx.lpc

import com.github.xxx.lpc.psi.IdentifierPSINode
import com.intellij.lang.DefaultASTFactoryImpl
import com.intellij.psi.impl.source.tree.CompositeElement
import com.intellij.psi.impl.source.tree.LeafElement
import com.intellij.psi.tree.IElementType
import lpc.LPCLexer
import org.antlr.intellij.adaptor.lexer.TokenIElementType

/** How to create parse tree nodes (Jetbrains calls them AST nodes). Later
 * non-leaf nodes are converted to PSI nodes by the [ParserDefinition].
 * Leaf nodes are already considered PSI nodes.  This is mostly just
 * [CoreASTFactory] but with comments on the methods that you might want
 * to override.
 */
class LPCASTFactory : DefaultASTFactoryImpl() {
    /** Create an internal parse tree node. FileElement for root or a parse tree CompositeElement (not
     * PSI) for the token.
     * The FileElement is a parse tree node, which is converted to a PsiFile
     * by [ParserDefinition.createFile].
     */
    override fun createComposite(type: IElementType): CompositeElement {
        return super.createComposite(type)
    }

    /** Create a parse tree (AST) leaf node from a token. Doubles as a PSI leaf node.
     * Does not see whitespace tokens.  Default impl makes [LeafPsiElement]
     * or [PsiCoreCommentImpl] depending on [ParserDefinition.getCommentTokens].
     */
    override fun createLeaf(type: IElementType, text: CharSequence): LeafElement {
        // found an ID node; here we do not distinguish between definitions and references
        return if (type is TokenIElementType && type.antlrTokenType == LPCLexer.Identifier) {
            // because we have no context information here. All we know is that
            // we have an identifier node that will be connected somewhere in a tree.
            //
            // You can only rename, find usages, etc... on leaves implementing PsiNamedElement
            //
            // TODO: try not to create one for IDs under def subtree roots like vardef, function
            IdentifierPSINode(type, text)
        } else super.createLeaf(type, text)
    }
}