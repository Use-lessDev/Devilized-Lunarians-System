/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Events
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.content.Blocks
 *  mindustry.content.Items
 *  mindustry.game.EventType$Trigger
 *  mindustry.gen.Building
 *  mindustry.gen.Groups
 *  mindustry.world.blocks.power.PowerGenerator$GeneratorBuild
 */
package lunarians;

import arc.Events;
import arc.util.Log;
import arc.util.Time;
import lunarians.LunariansI18n;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.world.blocks.power.PowerGenerator;

public class EnergyWatch {
    private static boolean registered;
    private static float next;
    private static float lastWarn;

    public static void init() {
        if (registered) {
            return;
        }
        registered = true;
        Events.run((Object)EventType.Trigger.update, EnergyWatch::update);
    }

    private static void update() {
        if (Vars.state == null || !Vars.state.isGame() || Vars.player == null || Time.time < next) {
            return;
        }
        next = Time.time + 600.0f;
        try {
            if (Vars.player.closestCore() == null || Vars.player.closestCore().items.get(Items.coal) >= 50) {
                return;
            }
            for (Building building : Groups.build) {
                if (building.team != Vars.player.team() || building.block != Blocks.combustionGenerator || !(building instanceof PowerGenerator.GeneratorBuild) || !(((PowerGenerator.GeneratorBuild)building).productionEfficiency <= 0.0f)) continue;
                if (Time.time - lastWarn > 3600.0f && Vars.ui != null) {
                    lastWarn = Time.time;
                    String string = LunariansI18n.getLanguage();
                    Vars.ui.showInfoToast("pt".equals(string) ? "Energia: o n\u00facleo est\u00e1 sem carv\u00e3o, geradores parados." : ("es".equals(string) ? "Energ\u00eda: el n\u00facleo sin carb\u00f3n, generadores parados." : ("ru".equals(string) ? "\u042d\u043d\u0435\u0440\u0433\u0438\u044f: \u0432 \u044f\u0434\u0440\u0435 \u043d\u0435\u0442 \u0443\u0433\u043b\u044f, \u0433\u0435\u043d\u0435\u0440\u0430\u0442\u043e\u0440\u044b \u0441\u0442\u043e\u044f\u0442." : "Power: core is out of coal, generators idle.")), 5.0f);
                }
                return;
            }
        }
        catch (Throwable throwable) {
            Log.err((String)"[Lunarians] EnergyWatch", (Throwable)throwable);
        }
    }

    static {
        lastWarn = -99999.0f;
    }
}

