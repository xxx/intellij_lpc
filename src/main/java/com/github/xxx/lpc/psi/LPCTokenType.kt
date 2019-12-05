package com.github.xxx.lpc.psi

import com.github.xxx.lpc.LPCLanguage
import com.intellij.psi.tree.IElementType

class LPCTokenType(debugName: String) : IElementType(debugName, LPCLanguage.INSTANCE) {
    override fun toString() : String {
        return "LpcTokenType " + super.toString()
    }
}