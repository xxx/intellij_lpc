package com.github.xxx.lpc.psi

import com.intellij.lang.ASTNode
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.psi.IdentifierDefSubtree

open class VarDefSubtree(node: ASTNode, idElementTyp: IElementType) : IdentifierDefSubtree(node, idElementTyp)
