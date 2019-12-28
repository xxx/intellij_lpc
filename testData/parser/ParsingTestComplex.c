/*
 * green.c
 * A vial of toxic green goo... yummy!
 * Found in the SewerCorp Area, it can be used by
 * Ginsu Assassins
 * Uses the std_poison from the Ginsu directory is Suburb,
 * copied over.
 *
 * Druid
 * 26 June 1997
 *
 */

#pragma strict_types

#include <poison_types.h>
#include <stdproperties.h>

inherit "/std/poison";

void
create_poison()
{
    set_adj("green");
    add_adj("small");
    set_short("small vial of a thick green goo");
    add_name(({"vial", "liquid", "goo", "substance"}));
    add_adj(({"thick", "yucky", "small", "glass"}));
    set_long("This small glass vial contains a quantity of "+
        "a yucky green substance, a thick consistency you would "+
        "definitely call 'goo'.  It's probably pretty toxic.\n");

    add_prop(OBJ_I_VALUE, 4); // was 500 <- being exploited
                               // temp fix by coram, 22nd july '98
    add_prop(OBJ_I_WEIGHT, 60);
    add_prop(OBJ_I_VOLUME, 12);

    set_poison_max_uses(1);
    set_poison_time(300);
    set_poison_interval(5);
    set_poison_strength(200);
    set_poison_cure_difficulty(70);
    set_poison_damage(({POISON_HP, 250}));
    set_poison_id_difficulty(50); // Pretty simple.
    set_poison_name("toxic green goo");
    set_poison_extra_info("This isn't really a poison, just "+
        "a vial of a toxic green substance.  You REALLY don't "+
        "want to get any on yourself.\n");
}

