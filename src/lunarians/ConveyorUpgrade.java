/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.Events
 *  arc.math.geom.Position
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.content.Blocks
 *  mindustry.content.Items
 *  mindustry.entities.units.BuildPlan
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.EventType$WorldLoadEvent
 *  mindustry.gen.Building
 *  mindustry.gen.Groups
 *  mindustry.gen.Unit
 */
package lunarians;

import arc.Core;
import arc.Events;
import arc.math.geom.Position;
import arc.util.Log;
import arc.util.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import lunarians.AutoEconomy;
import lunarians.AutoSet;
import lunarians.LunariansI18n;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.entities.units.BuildPlan;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Unit;

public class ConveyorUpgrade {
    private static boolean registered;
    private static boolean running;
    private static float next;

    public static boolean enabled() {
        return Core.settings.getBool("lunarians-ti-conv", true);
    }

    public static void setEnabled(boolean bl) {
        Core.settings.put("lunarians-ti-conv", (Object)bl);
        if (!bl) {
            running = false;
        }
    }

    public static void setThreshold(int n) {
        Core.settings.put("lunarians-ti-conv-min", (Object)Math.max(200, Math.min(10000, n)));
    }

    public static int threshold() {
        return Math.max(100, Core.settings.getInt("lunarians-ti-conv-min", 2000));
    }

    public static String title() {
        String string = LunariansI18n.getLanguage();
        if ("pt".equals(string)) {
            return "Esteira de titanio automatica (2k)";
        }
        if ("es".equals(string)) {
            return "Cinta de titanio automatica (2k)";
        }
        if ("ru".equals(string)) {
            return "\u0410\u0432\u0442\u043e-\u0442\u0438\u0442\u0430\u043d\u043e\u0432\u044b\u0439 \u043a\u043e\u043d\u0432\u0435\u0439\u0435\u0440 (2k)";
        }
        return "Auto titanium conveyor (2k)";
    }

    public static void init() {
        if (registered) {
            return;
        }
        registered = true;
        Events.run((Object)EventType.Trigger.update, ConveyorUpgrade::update);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            running = false;
        });
    }

    private static void update() {
        if (!ConveyorUpgrade.enabled() || Vars.state == null || !Vars.state.isGame() || Vars.player == null || Time.time < next) {
            return;
        }
        next = Time.time + 180.0f;
        try {
            Unit unit = Vars.player.unit();
            if (unit == null || unit.dead || !unit.canBuild() || Vars.player.closestCore() == null) {
                return;
            }
            if (AutoSet.isActive() || AutoEconomy.active) {
                return;
            }
            int n = Vars.player.closestCore().items.get(Items.titanium);
            if (!running && n >= ConveyorUpgrade.threshold()) {
                running = true;
            }
            if (running && n < ConveyorUpgrade.threshold() / 4) {
                running = false;
            }
            int n2 = Vars.player.closestCore().items.get(Items.copper);
            int n3 = Vars.player.closestCore().items.get(Items.lead);
            if (!running || unit.plans().size > 1 || n2 < 300 || n3 < 300) {
                return;
            }
            final Unit unit2 = unit;
            ArrayList<Building> arrayList = new ArrayList<Building>();
            float f = unit.type.buildRange * 0.9f;
            for (Building building : Groups.build) {
                if (building.team != Vars.player.team() || building.block != Blocks.conveyor || !building.within((Position)unit, f)) continue;
                arrayList.add(building);
            }
            if (arrayList.isEmpty()) {
                return;
            }
            Collections.sort(arrayList, new Comparator<Building>(){

                @Override
                public int compare(Building building, Building building2) {
                    return Float.compare(building.dst2((Position)unit2), building2.dst2((Position)unit2));
                }
            });
            int n4 = Math.min(8, Math.min(arrayList.size(), (Math.min(n2, n3) - 200) / 2));
            for (int i = 0; i < n4; ++i) {
                Building building = (Building)arrayList.get(i);
                unit.addBuild(new BuildPlan(building.tileX(), building.tileY(), building.rotation, Blocks.titaniumConveyor), true);
            }
        }
        catch (Throwable throwable) {
            Log.err((String)"[Lunarians] ConveyorUpgrade", (Throwable)throwable);
        }
    }
}

