package lunarians;

import arc.Events;
import arc.func.Cons;
import arc.struct.IntFloatMap;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.ctype.UnlockableContent;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.blocks.production.Drill;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.units.Reconstructor;
import mindustry.world.blocks.units.UnitFactory;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ProductionAlert.class */
public class ProductionAlert {
    private static final float SCAN_INTERVAL = 1.5f;
    private static final float IDLE_RESET = 45.0f;
    private static final int MAX_PER_SCAN = 3;
    private static final IntFloatMap lastSeen = new IntFloatMap();
    private static float clock;
    private static float timer;
    private static int silentScans;
    private static int notifiedThisScan;
    private static boolean errorLogged;

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run(EventType.Trigger.update, new Runnable() { // from class: lunarians.ProductionAlert.1
            @Override // java.lang.Runnable
            public void run() {
                ProductionAlert.update();
            }
        });
        Events.on(EventType.WorldLoadEvent.class, new Cons<EventType.WorldLoadEvent>() { // from class: lunarians.ProductionAlert.2
            public void get(EventType.WorldLoadEvent worldLoadEvent) {
                ProductionAlert.reset();
            }
        });
    }

    public static void reset() {
        lastSeen.clear();
        silentScans = 2;
        timer = 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void update() {
        if (!LunariansSettings.enableProductionAlert || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
            return;
        }
        float f = Time.delta / 60.0f;
        clock += f;
        float f2 = timer + f;
        timer = f2;
        if (f2 < SCAN_INTERVAL) {
            return;
        }
        timer = 0.0f;
        try {
            scan();
        } catch (Throwable th) {
            if (!errorLogged) {
                errorLogged = true;
                Log.err("[Lunarians] ProductionAlert error", th);
            }
        }
    }

    private static void scan() {
        notifiedThisScan = 0;
        boolean z = silentScans > 0;
        Team team = Vars.player.team();
        int size = Groups.build.size();
        for (int i = 0; i < size; i++) {
            Building index = Groups.build.index(i);
            if (index != null && index.isValid() && index.team != null && index.team != Team.derelict && (!LunariansSettings.productionIgnoreOwn || index.team != team)) {
                inspect(index, z);
            }
        }
        if (silentScans > 0) {
            silentScans--;
        }
    }

    private static void inspect(Building building, boolean z) {
        if (building instanceof UnitFactory.UnitFactoryBuild) {
            UnitFactory.UnitFactoryBuild unitFactoryBuild = (UnitFactory.UnitFactoryBuild) building;
            UnitFactory unitFactory = building.block;
            if (unitFactoryBuild.currentPlan >= 0 && unitFactoryBuild.currentPlan < unitFactory.plans.size && unitFactoryBuild.efficiency > 0.0f && unitFactoryBuild.shouldConsume()) {
                UnitFactory.UnitPlan unitPlan = (UnitFactory.UnitPlan) unitFactory.plans.get(unitFactoryBuild.currentPlan);
                found(building.team, unitPlan == null ? null : unitPlan.unit, z);
                return;
            }
            return;
        }
        if (building instanceof Reconstructor.ReconstructorBuild) {
            Reconstructor.ReconstructorBuild reconstructorBuild = (Reconstructor.ReconstructorBuild) building;
            if (reconstructorBuild.constructing() && reconstructorBuild.efficiency > 0.0f) {
                found(building.team, reconstructorBuild.unit(), z);
                return;
            }
            return;
        }
        if (!(building instanceof GenericCrafter.GenericCrafterBuild)) {
            if (building instanceof Drill.DrillBuild) {
                Drill.DrillBuild drillBuild = (Drill.DrillBuild) building;
                if (drillBuild.dominantItem != null && drillBuild.warmup > 0.3f) {
                    found(drillBuild.team, drillBuild.dominantItem, z);
                    return;
                }
                return;
            }
            return;
        }
        if (building.efficiency > 0.0f && building.shouldConsume()) {
            GenericCrafter genericCrafter = building.block;
            if (genericCrafter.outputItems != null) {
                for (ItemStack itemStack : genericCrafter.outputItems) {
                    if (itemStack != null) {
                        found(building.team, itemStack.item, z);
                    }
                }
            } else if (genericCrafter.outputItem != null) {
                found(building.team, genericCrafter.outputItem.item, z);
            }
            if (genericCrafter.outputLiquids != null) {
                for (LiquidStack liquidStack : genericCrafter.outputLiquids) {
                    if (liquidStack != null) {
                        found(building.team, liquidStack.liquid, z);
                    }
                }
                return;
            }
            if (genericCrafter.outputLiquid != null) {
                found(building.team, genericCrafter.outputLiquid.liquid, z);
            }
        }
    }

    private static void found(Team team, UnlockableContent unlockableContent, boolean z) {
        if (team == null || unlockableContent == null) {
            return;
        }
        int ordinal = (team.id << 20) | (unlockableContent.getContentType().ordinal() << 14) | (unlockableContent.id & 16383);
        if (!(!lastSeen.containsKey(ordinal) || clock - lastSeen.get(ordinal, 0.0f) > IDLE_RESET)) {
            lastSeen.put(ordinal, clock);
            return;
        }
        if (z) {
            lastSeen.put(ordinal, clock);
        } else {
            if (notifiedThisScan >= 3) {
                return;
            }
            notifiedThisScan++;
            lastSeen.put(ordinal, clock);
            announce(team, unlockableContent);
        }
    }

    public static String teamName(Team team) {
        String str = "[#" + team.color + "]";
        if (team.name == null || team.name.startsWith("team#")) {
            return str + "Team " + team.id + "[]";
        }
        return str + team.localized() + "[]";
    }

    private static void announce(Team team, UnlockableContent unlockableContent) {
        try {
            Vars.ui.chatfrag.addMessage(LunariansI18n.get("prod_started", teamName(team), unlockableContent.localizedName) + " " + unlockableContent.emoji());
        } catch (Throwable th) {
        }
    }
}
