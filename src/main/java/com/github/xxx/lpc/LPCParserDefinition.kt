package com.github.xxx.lpc

import com.github.xxx.lpc.psi.ArgDefSubtree
import com.github.xxx.lpc.psi.BlockSubtree
import com.github.xxx.lpc.psi.CallSubtree
import com.github.xxx.lpc.psi.FunctionImplementationSubtree
import com.github.xxx.lpc.psi.FunctionPrototypeSubtree
import com.github.xxx.lpc.psi.LPCPSIFileRoot
import com.github.xxx.lpc.psi.VarDefSubtree
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
import org.antlr.intellij.adaptor.lexer.RuleIElementType
import org.antlr.intellij.adaptor.lexer.TokenIElementType
import org.antlr.intellij.adaptor.parser.ANTLRParserAdaptor
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.tree.ParseTree

class LPCParserDefinition : ParserDefinition {
    companion object {
        val FILE = IFileElementType(LPCLanguage.INSTANCE)
        var ID : TokenIElementType

        init {
            PSIElementTypeFactory.defineLanguageIElementTypes(LPCLanguage.INSTANCE,
                    LPCLexer.tokenNames,
                    LPCParser.ruleNames
            )

            val tokenIElementTypes = PSIElementTypeFactory.getTokenIElementTypes(LPCLanguage.INSTANCE)
            ID = tokenIElementTypes[LPCLexer.Identifier]

            // val ruleIElementTypes = PSIElementTypeFactory.getRuleIElementTypes(LPCLanguage.INSTANCE)
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

        val tokens : List<TokenIElementType> = PSIElementTypeFactory.getTokenIElementTypes(LPCLanguage.INSTANCE)
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
        val elType = node!!.elementType
        if (elType is TokenIElementType) {
            return ANTLRPsiNode(node)
        }
        if (elType !is RuleIElementType) {
            return ANTLRPsiNode(node)
        }

        return when (elType.ruleIndex) {
            LPCParser.RULE_function_prototype -> FunctionPrototypeSubtree(node)
            LPCParser.RULE_function_implementation -> FunctionImplementationSubtree(node, ID)
            LPCParser.RULE_new_name,
            LPCParser.RULE_single_new_local_def,
            LPCParser.RULE_single_new_local_def_with_init,
            LPCParser.RULE_new_local_def  -> VarDefSubtree(node, ID)
            LPCParser.RULE_new_arg -> ArgDefSubtree(node, ID)
            LPCParser.RULE_block -> BlockSubtree(node)
            LPCParser.RULE_function_call -> CallSubtree(node)
            else -> ANTLRPsiNode(node)
        }
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