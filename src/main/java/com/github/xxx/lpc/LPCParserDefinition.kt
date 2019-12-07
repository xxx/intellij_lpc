package com.github.xxx.lpc

import com.github.xxx.lpc.psi.LPCPSIFileRoot
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import lpc.LPCLexer
import lpc.LPCParser
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory.createTokenSet
import org.antlr.intellij.adaptor.parser.ANTLRParserAdaptor
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.tree.ParseTree

class LPCParserDefinition : ParserDefinition {
    companion object {
        val FILE = IFileElementType(LPCLanguage.INSTANCE)

        init {
            PSIElementTypeFactory.defineLanguageIElementTypes(LPCLanguage.INSTANCE,
//                LPCParser.VOCABULARY,
                    LPCLexer.tokenNames,
                    LPCParser.ruleNames
            )
        }


        val COMMENTS = createTokenSet(
                LPCLanguage.INSTANCE,
                LPCLexer.BlockComment,
                LPCLexer.LineComment)!!

        val WHITESPACE = createTokenSet(
                LPCLanguage.INSTANCE,
                LPCLexer.Whitespace,
                LPCLexer.Newline)!!

        val STRING = createTokenSet(
                LPCLanguage.INSTANCE,
                LPCLexer.String)!!
    }

    override fun createLexer(project: Project): Lexer {
        val lexer = LPCLexer(null)
        return ANTLRLexerAdaptor(LPCLanguage.INSTANCE, lexer)
    }

    override fun createParser(project: Project): PsiParser {
        val parser = LPCParser(null)
        return object : ANTLRParserAdaptor(LPCLanguage.INSTANCE, parser) {
            override fun parse(parser: Parser, root: IElementType): ParseTree {
                return (parser as LPCParser).lpc_program()
            }
        }
    }

    /** "Tokens of those types are automatically skipped by PsiBuilder."  */
    override fun getWhitespaceTokens(): TokenSet {
        return WHITESPACE
    }

    override fun getCommentTokens(): TokenSet {
        return COMMENTS
    }

    override fun getStringLiteralElements(): TokenSet {
        return STRING
    }

    override fun createElement(node: ASTNode?): PsiElement {
        return ANTLRPsiNode(node!!)
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        return LPCPSIFileRoot(viewProvider)
    }

    override fun spaceExistanceTypeBetweenTokens(left: ASTNode, right: ASTNode): ParserDefinition.SpaceRequirements {
        return ParserDefinition.SpaceRequirements.MAY
    }

    /** What is the IFileElementType of the root parse tree node? It
     * is called from [.createFile] at least.
     */
    override fun getFileNodeType(): IFileElementType? {
        return FILE
    }
}