package com.github.xxx.lpc

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import org.antlr.intellij.adaptor.lexer.TokenIElementType
import lpc.LPCLexer
import lpc.LPCParser

class LPCSyntaxHighlighter : SyntaxHighlighterBase() {
    companion object {
        private val EMPTY_KEYS = arrayOfNulls<TextAttributesKey>(0)
        val ID = TextAttributesKey.createTextAttributesKey("LPC_ID", DefaultLanguageHighlighterColors.IDENTIFIER)
        val CONSTANT = TextAttributesKey.createTextAttributesKey("LPC_CONSTANT", DefaultLanguageHighlighterColors.CONSTANT)
        val KEYWORD = TextAttributesKey.createTextAttributesKey("LPC_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val OPERATOR = TextAttributesKey.createTextAttributesKey("LPC_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val STRING = TextAttributesKey.createTextAttributesKey("LPC_STRING", DefaultLanguageHighlighterColors.STRING)
        val NUMBER = TextAttributesKey.createTextAttributesKey("LPC_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val LINE_COMMENT = TextAttributesKey.createTextAttributesKey("LPC_LINE_COMMENT",
                DefaultLanguageHighlighterColors.LINE_COMMENT)
        val BLOCK_COMMENT = TextAttributesKey.createTextAttributesKey("LPC_BLOCK_COMMENT",
                DefaultLanguageHighlighterColors.BLOCK_COMMENT)
        val PARENTHESES = TextAttributesKey.createTextAttributesKey("LPC_PARENTHESES",
                DefaultLanguageHighlighterColors.PARENTHESES)
        val BRACES = TextAttributesKey.createTextAttributesKey("LPC_BRACES",
                DefaultLanguageHighlighterColors.BRACES)
        val BRACKETS = TextAttributesKey.createTextAttributesKey("LPC_BRACKETS",
                DefaultLanguageHighlighterColors.BRACKETS)
        val SEMICOLON = TextAttributesKey.createTextAttributesKey("LPC_SEMICOLON",
                DefaultLanguageHighlighterColors.SEMICOLON)
        val BAD_CHARS = TextAttributesKey.createTextAttributesKey("LPC_BAD_CHARS",
                HighlighterColors.BAD_CHARACTER)

        init {
            PSIElementTypeFactory.defineLanguageIElementTypes(LPCLanguage.INSTANCE,
                    LPCParser.tokenNames,
                    LPCParser.ruleNames)
        }
    }

    override fun getHighlightingLexer(): Lexer {
        val lexer = LPCLexer(null)
        return ANTLRLexerAdaptor(LPCLanguage.INSTANCE, lexer)
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey?> {
        if (tokenType !is TokenIElementType) return EMPTY_KEYS
        val ttype = tokenType.antlrTokenType
        val attrKey: TextAttributesKey
        attrKey = when (ttype) {
            LPCLexer.Identifier -> ID

            LPCLexer.ComplexDefine -> CONSTANT

            LPCLexer.BasicType,
            LPCLexer.TypeModifier,
            LPCLexer.Inherit,
            LPCLexer.For,
            LPCLexer.While,
            LPCLexer.Continue,
            LPCLexer.If,
            LPCLexer.Else,
            LPCLexer.Return,
            LPCLexer.Efun,
            LPCLexer.Foreach,
            LPCLexer.Catch,
            LPCLexer.Do,
            LPCLexer.Switch,
            LPCLexer.Case,
            LPCLexer.Default,
            LPCLexer.Break,
            LPCLexer.Arrow,
            LPCLexer.Colon,
            LPCLexer.Ellipsis -> KEYWORD

            LPCLexer.Assign,
            LPCLexer.PlusPlus,
            LPCLexer.MinusMinus,
            LPCLexer.And,
            LPCLexer.AndAnd,
            LPCLexer.Caret,
            LPCLexer.Or,
            LPCLexer.OrOr,
            LPCLexer.Equal,
            LPCLexer.LeftShift,
            LPCLexer.RightShift,
            LPCLexer.Not,
            LPCLexer.NotEqual,
            LPCLexer.Compare,
            LPCLexer.Less,
            LPCLexer.LessEqual,
            LPCLexer.Great,
            LPCLexer.GreatEqual,
            LPCLexer.Range,
            LPCLexer.Comma,
            LPCLexer.ColonColon -> OPERATOR

            LPCLexer.LeftParen,
            LPCLexer.RightParen -> PARENTHESES

            LPCLexer.LeftBrace,
            LPCLexer.RightBrace -> BRACES

            LPCLexer.LeftBracket,
            LPCLexer.RightBracket -> BRACKETS

            LPCLexer.SemiColon -> SEMICOLON

            LPCLexer.CharacterConstant,
            LPCLexer.String -> STRING

            LPCLexer.Number,
            LPCLexer.Real -> NUMBER

            LPCLexer.LineComment -> LINE_COMMENT
            LPCLexer.BlockComment -> BLOCK_COMMENT

            LPCLexer.BadChar -> BAD_CHARS
            else -> return EMPTY_KEYS
        }
        return arrayOf(attrKey)
    }
}