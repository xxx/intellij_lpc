package com.github.xxx.lpc.psi

import com.intellij.lang.ASTNode
import com.intellij.psi.tree.IElementType

class ArgDefSubtree(node: ASTNode, idElementType: IElementType) :
    VarDefSubtree(node, idElementType)