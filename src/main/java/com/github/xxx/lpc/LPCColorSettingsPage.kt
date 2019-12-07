package com.github.xxx.lpc

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

class LPCColorSettingsPage : ColorSettingsPage {
    override fun getIcon(): Icon? {
        return LPCIcons.FILE
    }

    override fun getHighlighter(): SyntaxHighlighter {
        return LPCSyntaxHighlighter()
    }

    override fun getDemoText(): String {
        return """
            /*
             * A BASTARDIZATION OF: 
             * container.c
             *
             * Contains all routines relating to objects that can hold other objects
             *
             * mask create_armour_and_container() in your object to configure.
             * Coram, 4th August 2002.
             */
            #pragma strict_types
            
            inherit "/std/armour" armour;
            inherit "/std/container_base" container_base;
            
            #include <stdproperties.h>
            
            /*
             * Function name: create_armour_and_container
             * Description:   Overrideable initializer for armours that can
             *                also hold objects, like in pockets.
             */
            void create_armour_and_container() {}
            
            /*
             * Function name: create_armour
             * Description:   Create the container (constructor)
             */
            public nomask void
            create_armour()
            {
                initialize_container_base();
            
                add_prop(CONT_I_WEIGHT, 1000);      /* 1 Kg is weight of empty container */
                add_prop(CONT_I_VOLUME, 1000);      /* 1 liter is volume of empty cont. */
                add_prop(CONT_I_MAX_WEIGHT, 1000); /* 1 Kg is max total weight */
                add_prop(CONT_I_MAX_VOLUME, 1000); /* 1 liter is max total volume */
            
                create_armour_and_container();
            }
            
            /*
             * Function name: leave_env
             * Description:   The armour&container leaves an environment
             * Arguments:     from - From what object
             *                to  - To what object
             */
            void
            leave_env(object from, object to)
            {
                container_base::leave_env(from, to);
                armour::leave_env(from, to);
            }
            
            /*
             * Function name: stat_object
             * Description:   This function is called when a wizard wants to get more
             *                information about an object via the 'stat' command.
             * Returns:       str - The string to write.
             */
            public string
            stat_object()
            {
                return armour::stat_object() + "\n" + container_base::stat_object();
            }
        """.trimIndent()
    }

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey>? {
        return null
    }

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> {
        return DESCRIPTORS
    }

    override fun getColorDescriptors(): Array<ColorDescriptor> {
        return ColorDescriptor.EMPTY_ARRAY
    }

    override fun getDisplayName(): String {
        return "LPC"
    }

    companion object {
        private val DESCRIPTORS = arrayOf(AttributesDescriptor("Keywords", LPCSyntaxHighlighter.KEYWORD),
                AttributesDescriptor("Operator", LPCSyntaxHighlighter.OPERATOR),
                AttributesDescriptor("Strings", LPCSyntaxHighlighter.STRING))
    }
}
