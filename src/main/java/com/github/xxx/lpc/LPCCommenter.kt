package com.github.xxx.lpc

import com.intellij.lang.Commenter

class LPCCommenter : Commenter {
    override fun getLineCommentPrefix(): String? {
        return "//"
    }

    override fun getBlockCommentPrefix(): String? {
        return "/*"
    }

    override fun getBlockCommentSuffix(): String? {
        return "*/"
    }

    override fun getCommentedBlockCommentPrefix(): String? {
        return "/* /*"
    }

    override fun getCommentedBlockCommentSuffix(): String? {
        return "*/"
    }
}