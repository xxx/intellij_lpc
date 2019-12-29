package com.github.xxx.lpc

import com.github.xxx.lpc.LPCParserDefinition.Companion.tokens
import com.github.xxx.lpc.psi.ExpressionSubtree
import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import lpc.LPCLexer
import org.antlr.intellij.adaptor.lexer.TokenIElementType

class LPCBraceMatcher : PairedBraceMatcher {
     override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean {
          if (lbraceType is TokenIElementType && contextType is TokenIElementType &&
               lbraceType.antlrTokenType == LPCLexer.LeftBracket && contextType.antlrTokenType == LPCLexer.RightParen) {
               return false
          }

          return true
     }

     override fun getPairs(): Array<BracePair> {
          return arrayOf(
               BracePair(tokens[LPCLexer.LeftParen], tokens[LPCLexer.RightParen], false),
               BracePair(tokens[LPCLexer.LeftBrace], tokens[LPCLexer.RightBrace], true),
               BracePair(tokens[LPCLexer.LeftBracket], tokens[LPCLexer.RightBracket], false)
               // BracePair(tokens[LPCLexer.MappingOpen], tokens[LPCLexer.RightBracket], false),
               // BracePair(tokens[LPCLexer.ArrayOpen], tokens[LPCLexer.RightBrace], false)
          )
     }

     override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int {
          if (file == null) return openingBraceOffset

          val element = file.findElementAt(openingBraceOffset)

          val node = PsiTreeUtil.getParentOfType(element, ExpressionSubtree::class.java)

          return node?.textOffset ?: openingBraceOffset
     }
}
