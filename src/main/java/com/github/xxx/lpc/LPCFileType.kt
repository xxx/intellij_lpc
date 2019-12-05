package com.github.xxx.lpc

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon


class LPCFileType : LanguageFileType(LPCLanguage.INSTANCE) {
    companion object {
        val INSTANCE = LPCFileType()
    }

    override fun getName(): String {
        return "LPC file"
    }

    override fun getDescription(): String {
        return "LPC language file"
    }

    override fun getDefaultExtension(): String {
        return "c"
    }

    override fun getIcon(): Icon? {
        return LPCIcons.FILE
    }
}