/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Events
 *  arc.util.Log
 *  mindustry.Vars
 *  mindustry.content.Items
 *  mindustry.content.UnitTypes
 *  mindustry.game.EventType$ResetEvent
 *  mindustry.game.EventType$WorldLoadEvent
 *  mindustry.type.Item
 *  mindustry.type.UnitType
 */
package lunarians;

import arc.Events;
import arc.util.Log;
import lunarians.LunariansSettings;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.content.UnitTypes;
import mindustry.game.EventType;
import mindustry.type.Item;
import mindustry.type.UnitType;

public class MinerFix {
    private static Boolean savedAutoAttack = null;

    public static void init() {
        MinerFix.apply();
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            MinerFix.apply();
            MinerFix.pvpGuard();
        });
        Events.on(EventType.ResetEvent.class, resetEvent -> MinerFix.restoreAutoAttack());
    }

    private static void restoreAutoAttack() {
        if (savedAutoAttack != null) {
            LunariansSettings.enableAutoAttack = savedAutoAttack;
            savedAutoAttack = null;
        }
    }

    private static void pvpGuard() {
        try {
            boolean bl;
            boolean bl2 = bl = Vars.state != null && Vars.state.rules != null && Vars.state.rules.pvp;
            if (bl) {
                if (savedAutoAttack == null) {
                    savedAutoAttack = LunariansSettings.enableAutoAttack;
                }
                LunariansSettings.enableAutoAttack = false;
            } else if (savedAutoAttack != null) {
                LunariansSettings.enableAutoAttack = savedAutoAttack;
                savedAutoAttack = null;
            }
        }
        catch (Throwable throwable) {
            Log.err((String)"[Lunarians] pvpGuard falhou", (Throwable)throwable);
        }
    }

    public static void apply() {
        MinerFix.allow(UnitTypes.mono, Items.sand);
        MinerFix.allow(UnitTypes.poly, Items.sand, Items.coal);
        MinerFix.allow(UnitTypes.mega, Items.sand, Items.coal, Items.titanium);
    }

    private static void allow(UnitType unitType, Item ... itemArray) {
        if (unitType == null) {
            return;
        }
        try {
            if (unitType.mineItems == null) {
                return;
            }
            for (Item item : itemArray) {
                if (item.hardness > unitType.mineTier || unitType.mineItems.contains((Object)item)) continue;
                unitType.mineItems.add((Object)item);
            }
        }
        catch (Throwable throwable) {
            Log.err((String)("[Lunarians] MinerFix falhou em " + unitType.name), (Throwable)throwable);
        }
    }
}

