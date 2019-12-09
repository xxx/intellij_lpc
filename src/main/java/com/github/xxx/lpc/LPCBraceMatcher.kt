package com.github.xxx.lpc


import com.github.xxx.lpc.LPCParserDefinition.Companion.tokens
import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import lpc.LPCLexer

class LPCBraceMatcher : PairedBraceMatcher {
     override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean = true

     override fun getPairs(): Array<BracePair> {
          return arrayOf(
               BracePair(tokens[LPCLexer.LeftParen], tokens[LPCLexer.RightParen], false),
               BracePair(tokens[LPCLexer.LeftBrace], tokens[LPCLexer.RightBrace], true),
               BracePair(tokens[LPCLexer.LeftBracket], tokens[LPCLexer.RightBracket], false)
               // BracePair(tokens[LPCLexer.MappingOpen], tokens[LPCLexer.MappingClose], false),
               // BracePair(tokens[LPCLexer.ArrayOpen], tokens[LPCLexer.ArrayClose], false)
          )
     }

     override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int {
          if (file == null) {
               return openingBraceOffset
          }

          val element = file.findElementAt(openingBraceOffset)
          println(element)

          return openingBraceOffset
     }
}
