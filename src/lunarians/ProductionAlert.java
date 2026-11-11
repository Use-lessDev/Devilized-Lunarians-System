/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Events
 *  arc.func.Cons
 *  arc.struct.IntFloatMap
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.ctype.UnlockableContent
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.EventType$WorldLoadEvent
 *  mindustry.game.Team
 *  mindustry.gen.Building
 *  mindustry.gen.Groups
 *  mindustry.type.ItemStack
 *  mindustry.type.UnitType
 *  mindustry.world.blocks.production.Drill$DrillBuild
 *  mindustry.world.blocks.production.GenericCrafter
 *  mindustry.world.blocks.production.GenericCrafter$GenericCrafterBuild
 *  mindustry.world.blocks.units.Reconstructor$ReconstructorBuild
 *  mindustry.world.blocks.units.UnitFactory
 *  mindustry.world.blocks.units.UnitFactory$UnitFactoryBuild
 *  mindustry.world.blocks.units.UnitFactory$UnitPlan
 */
package lunarians;

import arc.Events;
import arc.func.Cons;
import arc.struct.IntFloatMap;
import arc.util.Log;
import arc.util.Time;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import mindustry.Vars;
import mindustry.ctype.UnlockableContent;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.world.blocks.production.Drill;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.units.Reconstructor;
import mindustry.world.blocks.units.UnitFactory;

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
        Events.run((Object)EventType.Trigger.update, (Runnable)new Runnable(){

            @Override
            public void run() {
                ProductionAlert.update();
            }
        });
        Events.on(EventType.WorldLoadEvent.class, (Cons)new Cons<EventType.WorldLoadEvent>(){

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

    private static void update() {
        block4: {
            float f;
            if (!LunariansSettings.enableProductionAlert || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
                return;
            }
            float f2 = Time.delta / 60.0f;
            clock += f2;
            timer += f2;
            if (f < 1.5f) {
                return;
            }
            timer = 0.0f;
            try {
                ProductionAlert.scan();
            }
            catch (Throwable throwable) {
                if (errorLogged) break block4;
                errorLogged = true;
                Log.err((String)"[Lunarians] ProductionAlert error", (Throwable)throwable);
            }
        }
    }

    private static void scan() {
        notifiedThisScan = 0;
        boolean bl = silentScans > 0;
        Team team = Vars.player.team();
        int n = Groups.build.size();
        for (int i = 0; i < n; ++i) {
            Building building = (Building)Groups.build.index(i);
            if (building == null || !building.isValid() || building.team == null || building.team == Team.derelict || LunariansSettings.productionIgnoreOwn && building.team == team) continue;
            ProductionAlert.inspect(building, bl);
        }
        if (silentScans > 0) {
            --silentScans;
        }
    }

    private static void inspect(Building building, boolean bl) {
        if (building instanceof UnitFactory.UnitFactoryBuild) {
            UnitFactory.UnitFactoryBuild unitFactoryBuild = (UnitFactory.UnitFactoryBuild)building;
            UnitFactory unitFactory = (UnitFactory)building.block;
            if (unitFactoryBuild.currentPlan >= 0 && unitFactoryBuild.currentPlan < unitFactory.plans.size && unitFactoryBuild.efficiency > 0.0f && unitFactoryBuild.shouldConsume()) {
                UnitFactory.UnitPlan unitPlan = (UnitFactory.UnitPlan)unitFactory.plans.get(unitFactoryBuild.currentPlan);
                ProductionAlert.found(building.team, (UnlockableContent)(unitPlan == null ? null : unitPlan.unit), bl);
            }
        } else if (building instanceof Reconstructor.ReconstructorBuild) {
            Reconstructor.ReconstructorBuild reconstructorBuild = (Reconstructor.ReconstructorBuild)building;
            if (reconstructorBuild.constructing() && reconstructorBuild.efficiency > 0.0f) {
                UnitType unitType = reconstructorBuild.unit();
                ProductionAlert.found(building.team, (UnlockableContent)unitType, bl);
            }
        } else if (building instanceof GenericCrafter.GenericCrafterBuild) {
            if (building.efficiency > 0.0f && building.shouldConsume()) {
                GenericCrafter genericCrafter = (GenericCrafter)building.block;
                if (genericCrafter.outputItems != null) {
                    for (ItemStack itemStack : genericCrafter.outputItems) {
                        if (itemStack == null) continue;
                        ProductionAlert.found(building.team, (UnlockableContent)itemStack.item, bl);
                    }
                } else if (genericCrafter.outputItem != null) {
                    ProductionAlert.found(building.team, (UnlockableContent)genericCrafter.outputItem.item, bl);
                }
                if (genericCrafter.outputLiquids != null) {
                    for (ItemStack itemStack : genericCrafter.outputLiquids) {
                        if (itemStack == null) continue;
                        ProductionAlert.found(building.team, (UnlockableContent)itemStack.liquid, bl);
                    }
                } else if (genericCrafter.outputLiquid != null) {
                    ProductionAlert.found(building.team, (UnlockableContent)genericCrafter.outputLiquid.liquid, bl);
                }
            }
        } else if (building instanceof Drill.DrillBuild) {
            Drill.DrillBuild drillBuild = (Drill.DrillBuild)building;
            if (drillBuild.dominantItem != null && drillBuild.warmup > 0.3f) {
                ProductionAlert.found(drillBuild.team, (UnlockableContent)drillBuild.dominantItem, bl);
            }
        }
    }

    private static void found(Team team, UnlockableContent unlockableContent, boolean bl) {
        if (team == null || unlockableContent == null) {
            return;
        }
        int n = team.id << 20 | unlockableContent.getContentType().ordinal() << 14 | unlockableContent.id & 0x3FFF;
        boolean bl2 = lastSeen.containsKey(n);
        float f = lastSeen.get(n, 0.0f);
        boolean bl3 = !bl2 || clock - f > 45.0f;
        boolean bl4 = bl3;
        if (!bl3) {
            lastSeen.put(n, clock);
            return;
        }
        if (bl) {
            lastSeen.put(n, clock);
            return;
        }
        if (notifiedThisScan >= 3) {
            return;
        }
        ++notifiedThisScan;
        lastSeen.put(n, clock);
        ProductionAlert.announce(team, unlockableContent);
    }

    public static String teamName(Team team) {
        String string = "[#" + team.color + "]";
        if (team.name == null || team.name.startsWith("team#")) {
            return string + "Team " + team.id + "[]";
        }
        return string + team.localized() + "[]";
    }

    private static void announce(Team team, UnlockableContent unlockableContent) {
        String string = unlockableContent.emoji();
        String string2 = LunariansI18n.get("prod_started", ProductionAlert.teamName(team), unlockableContent.localizedName);
        try {
            Vars.ui.chatfrag.addMessage(string2 + " " + string);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }
}

