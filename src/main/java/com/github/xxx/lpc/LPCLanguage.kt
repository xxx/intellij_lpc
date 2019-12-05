package com.github.xxx.lpc

import com.intellij.lang.Language

class LPCLanguage private constructor() : Language("LPC") {
    companion object {
        val INSTANCE = LPCLanguage()
    }
}