/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.Events
 *  arc.graphics.Color
 *  arc.graphics.g2d.Draw
 *  arc.graphics.g2d.Lines
 *  arc.math.geom.Vec2
 *  arc.struct.IntFloatMap
 *  arc.struct.IntIntMap
 *  arc.struct.Seq
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.ai.UnitCommand
 *  mindustry.ai.types.CommandAI
 *  mindustry.core.World
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.EventType$WorldLoadEvent
 *  mindustry.gen.Building
 *  mindustry.gen.Call
 *  mindustry.gen.Groups
 *  mindustry.gen.Payloadc
 *  mindustry.gen.Player
 *  mindustry.gen.Unit
 *  mindustry.graphics.Pal
 *  mindustry.ui.Fonts
 *  mindustry.world.blocks.payloads.BuildPayload
 *  mindustry.world.blocks.payloads.Payload
 */
package lunarians;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.geom.Vec2;
import arc.struct.IntFloatMap;
import arc.struct.IntIntMap;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import lunarians.LunariansSettings;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.core.World;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Payloadc;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.ui.Fonts;
import mindustry.world.blocks.payloads.BuildPayload;
import mindustry.world.blocks.payloads.Payload;

public class AutoContainer {
    public static final int UNSET = Integer.MIN_VALUE;
    private static final int GO_A = 0;
    private static final int LOAD_A = 1;
    private static final int GO_B = 2;
    private static final int UNLOAD_B = 3;
    private static final int WAIT1 = 4;
    private static final int GO_B2 = 5;
    private static final int LOAD_B = 6;
    private static final int GO_A2 = 7;
    private static final int UNLOAD_A = 8;
    private static final int WAIT2 = 9;
    private static final float REACH = 6.0f;
    private static final float STAGE_TIMEOUT = 10.0f;
    private static float timer;
    private static boolean errorLogged;
    private static int count;
    private static float markUntil;
    private static int origAx;
    private static int origAy;
    private static int blockSize;
    private static final IntIntMap phase;
    private static final IntFloatMap stageT;
    private static final IntFloatMap lastCmd;
    private static final Seq<Unit> tmp;

    public static float waitAfterReturn() {
        return Math.max(0.0f, Core.settings.getFloat("lunarians-container-wait2", 5.0f));
    }

    public static void setWaitAfterReturn(float f) {
        Core.settings.put("lunarians-container-wait2", (Object)Float.valueOf(f));
    }

    public static float waitAfterPlace() {
        return Math.max(0.0f, LunariansSettings.containerWaitSec);
    }

    public static boolean hasPoints() {
        return LunariansSettings.containerPickX != Integer.MIN_VALUE && LunariansSettings.containerDropX != Integer.MIN_VALUE;
    }

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run((Object)EventType.Trigger.update, AutoContainer::update);
        Events.run((Object)EventType.Trigger.draw, AutoContainer::draw);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> AutoContainer.reset());
    }

    public static int count() {
        return count;
    }

    public static void reset() {
        timer = 0.0f;
        count = 0;
        origAy = Integer.MIN_VALUE;
        origAx = Integer.MIN_VALUE;
        phase.clear();
        stageT.clear();
        lastCmd.clear();
    }

    public static void showMarks() {
        markUntil = Time.time + 1200.0f;
    }

    public static void setPoints(int n, int n2, int n3, int n4) {
        LunariansSettings.containerPickX = n;
        LunariansSettings.containerPickY = n2;
        LunariansSettings.containerDropX = n3;
        LunariansSettings.containerDropY = n4;
        origAy = Integer.MIN_VALUE;
        origAx = Integer.MIN_VALUE;
        phase.clear();
        stageT.clear();
        AutoContainer.showMarks();
        LunariansSettings.save();
    }

    public static void clearPoints() {
        LunariansSettings.containerPickX = Integer.MIN_VALUE;
        LunariansSettings.containerPickY = Integer.MIN_VALUE;
        LunariansSettings.containerDropX = Integer.MIN_VALUE;
        LunariansSettings.containerDropY = Integer.MIN_VALUE;
        origAy = Integer.MIN_VALUE;
        origAx = Integer.MIN_VALUE;
        phase.clear();
        stageT.clear();
        LunariansSettings.save();
    }

    private static float centerOf(int n, int n2) {
        return (float)n * 8.0f + (float)((n2 + 1) % 2) * 4.0f;
    }

    private static void box(int n, int n2, int n3, Color color, String string) {
        float f = AutoContainer.centerOf(n, n3);
        float f2 = AutoContainer.centerOf(n2, n3);
        float f3 = (float)n3 * 4.0f;
        Lines.stroke((float)2.0f);
        Draw.color((Color)Color.black, (float)0.6f);
        Lines.rect((float)(f - f3 - 0.6f), (float)(f2 - f3 - 0.6f), (float)((float)n3 * 8.0f + 1.2f), (float)((float)n3 * 8.0f + 1.2f));
        Draw.color((Color)color, (float)0.95f);
        Lines.rect((float)(f - f3), (float)(f2 - f3), (float)((float)n3 * 8.0f), (float)((float)n3 * 8.0f));
        try {
            Fonts.outline.draw((CharSequence)string, f, f2 + 4.0f, Color.white, 0.22f, false, 1);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        Draw.color();
    }

    private static void draw() {
        if (Vars.state == null || !Vars.state.isGame()) {
            return;
        }
        if (!AutoContainer.hasPoints()) {
            return;
        }
        if (!LunariansSettings.enableAutoContainer && Time.time > markUntil) {
            return;
        }
        int n = LunariansSettings.containerPickX;
        int n2 = LunariansSettings.containerPickY;
        int n3 = LunariansSettings.containerDropX;
        int n4 = LunariansSettings.containerDropY;
        int n5 = blockSize;
        int n6 = n;
        int n7 = n2;
        try {
            Building building = Vars.world.build(n, n2);
            if (building != null && building.block != null) {
                n5 = building.block.size;
                n6 = building.tileX();
                n7 = building.tileY();
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        Draw.z((float)120.0f);
        Lines.stroke((float)1.5f);
        Draw.color((Color)Pal.accent, (float)0.7f);
        Lines.dashLine((float)AutoContainer.centerOf(n6, n5), (float)AutoContainer.centerOf(n7, n5), (float)AutoContainer.centerOf(n3, n5), (float)AutoContainer.centerOf(n4, n5), (int)14);
        AutoContainer.box(n6, n7, n5, Color.orange, "A");
        AutoContainer.box(n3, n4, n5, Color.cyan, "B");
        Draw.reset();
    }

    private static void update() {
        block4: {
            float f;
            if (!LunariansSettings.enableAutoContainer || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
                count = 0;
                return;
            }
            timer += Time.delta / 60.0f;
            if (f < LunariansSettings.actionInterval(0.3f)) {
                return;
            }
            timer = 0.0f;
            try {
                AutoContainer.scan();
            }
            catch (Throwable throwable) {
                if (errorLogged) break block4;
                errorLogged = true;
                Log.err((String)"[Lunarians] AutoContainer", (Throwable)throwable);
            }
        }
    }

    private static void scan() {
        int n;
        count = 0;
        if (LunariansSettings.containerUnit == null || LunariansSettings.containerUnit.isEmpty() || !AutoContainer.hasPoints()) {
            return;
        }
        tmp.clear();
        int n2 = Groups.unit.size();
        int n3 = LunariansSettings.containerMaxUnits;
        for (n = 0; n < n2; ++n) {
            Unit unit = (Unit)Groups.unit.index(n);
            if (unit == null || !unit.isValid() || unit.team != Vars.player.team() || !(unit instanceof Payloadc) || !(unit.controller() instanceof CommandAI) || unit.type == null || !LunariansSettings.containerUnit.equals(unit.type.name)) continue;
            tmp.add((Object)unit);
            if (n3 > 0 && AutoContainer.tmp.size >= n3) break;
        }
        for (n = 0; n < AutoContainer.tmp.size; ++n) {
            AutoContainer.drive((Unit)tmp.get(n), (Payloadc)tmp.get(n));
        }
        count = AutoContainer.tmp.size;
    }

    private static void setStage(int n, int n2) {
        phase.put(n, n2);
        stageT.put(n, Time.time);
        lastCmd.remove(n, 0.0f);
    }

    private static float stageSecs(int n) {
        return (Time.time - stageT.get(n, Time.time)) / 60.0f;
    }

    private static void setCommand(Unit unit, UnitCommand unitCommand) {
        try {
            CommandAI commandAI = (CommandAI)unit.controller();
            if (commandAI.command != unitCommand) {
                Call.setUnitCommand((Player)Vars.player, (int[])new int[]{unit.id}, (UnitCommand)unitCommand);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static void goTo(Unit unit, float f, float f2, boolean bl) {
        float f3 = lastCmd.get(unit.id, -9999.0f);
        if (!bl && (Time.time - f3) / 60.0f < 2.5f) {
            return;
        }
        lastCmd.put(unit.id, Time.time);
        Call.commandUnits((Player)Vars.player, (int[])new int[]{unit.id}, null, null, (Vec2)new Vec2(f, f2), (boolean)false, (boolean)true);
    }

    private static float dropOffset(Payloadc payloadc) {
        try {
            Payload payload = (Payload)payloadc.payloads().peek();
            if (payload instanceof BuildPayload) {
                return ((BuildPayload)payload).block().offset;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return (float)((blockSize + 1) % 2) * 4.0f;
    }

    private static boolean approachExact(Unit unit, int n, int n2, float f) {
        float f2 = (float)n * 8.0f + f;
        float f3 = (float)n2 * 8.0f + f;
        if (!unit.within(f2, f3, 40.0f)) {
            AutoContainer.setCommand(unit, UnitCommand.moveCommand);
            AutoContainer.goTo(unit, f2, f3, false);
            return false;
        }
        AutoContainer.setCommand(unit, UnitCommand.loadBlocksCommand);
        AutoContainer.goTo(unit, f2, f3, false);
        return World.toTile((float)(unit.x - f)) == n && World.toTile((float)(unit.y - f)) == n2 && unit.within(f2, f3, 2.0f);
    }

    private static float[] pickCenter(int n, int n2) {
        Building building = Vars.world.build(n, n2);
        if (building != null) {
            return new float[]{building.x, building.y};
        }
        return new float[]{(float)n * 8.0f, (float)n2 * 8.0f};
    }

    private static void pickStep(Unit unit, int n, int n2) {
        Building building = Vars.world.build(n, n2);
        Building building2 = Vars.world.buildWorld(unit.x, unit.y);
        float[] fArray = AutoContainer.pickCenter(n, n2);
        if (building != null && building2 == building && unit.within(fArray[0], fArray[1], 6.0f)) {
            AutoContainer.goTo(unit, unit.x, unit.y, true);
        } else {
            AutoContainer.goTo(unit, fArray[0], fArray[1], false);
        }
    }

    private static void drive(Unit unit, Payloadc payloadc) {
        int n = unit.id;
        int n2 = phase.get(n, -1);
        int n3 = LunariansSettings.containerPickX;
        int n4 = LunariansSettings.containerPickY;
        int n5 = LunariansSettings.containerDropX;
        int n6 = LunariansSettings.containerDropY;
        float[] fArray = AutoContainer.pickCenter(n3, n4);
        float[] fArray2 = AutoContainer.pickCenter(n5, n6);
        float f = fArray[0];
        float f2 = fArray[1];
        float f3 = fArray2[0];
        float f4 = fArray2[1];
        float f5 = AutoContainer.dropOffset(payloadc);
        boolean bl = payloadc.hasPayload();
        if (n2 < 0) {
            AutoContainer.setStage(n, bl ? 2 : 0);
            n2 = phase.get(n, 0);
        }
        switch (n2) {
            case 0: {
                if (bl) {
                    AutoContainer.setStage(n, 2);
                    break;
                }
                AutoContainer.setCommand(unit, UnitCommand.moveCommand);
                AutoContainer.goTo(unit, f, f2, false);
                if (!unit.within(f, f2, 40.0f)) break;
                try {
                    Building building = Vars.world.build(n3, n4);
                    if (building != null && building.block != null) {
                        origAx = building.tileX();
                        origAy = building.tileY();
                        blockSize = building.block.size;
                    }
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
                AutoContainer.setCommand(unit, UnitCommand.loadBlocksCommand);
                AutoContainer.setStage(n, 1);
                AutoContainer.goTo(unit, f, f2, true);
                break;
            }
            case 1: {
                if (bl) {
                    AutoContainer.setCommand(unit, UnitCommand.moveCommand);
                    AutoContainer.setStage(n, 2);
                    break;
                }
                if (AutoContainer.stageSecs(n) > 10.0f) {
                    AutoContainer.setCommand(unit, UnitCommand.moveCommand);
                    AutoContainer.setStage(n, 0);
                    break;
                }
                AutoContainer.setCommand(unit, UnitCommand.loadBlocksCommand);
                AutoContainer.pickStep(unit, n3, n4);
                break;
            }
            case 2: {
                if (!bl) {
                    AutoContainer.setStage(n, 0);
                    break;
                }
                if (!AutoContainer.approachExact(unit, n5, n6, f5)) break;
                AutoContainer.setCommand(unit, UnitCommand.unloadPayloadCommand);
                AutoContainer.setStage(n, 3);
                break;
            }
            case 3: {
                if (!bl) {
                    AutoContainer.setCommand(unit, UnitCommand.moveCommand);
                    AutoContainer.setStage(n, 4);
                    break;
                }
                if (!(AutoContainer.stageSecs(n) > 10.0f)) break;
                AutoContainer.setCommand(unit, UnitCommand.moveCommand);
                AutoContainer.setStage(n, 2);
                break;
            }
            case 4: {
                AutoContainer.setCommand(unit, UnitCommand.moveCommand);
                if (!(AutoContainer.stageSecs(n) >= AutoContainer.waitAfterPlace())) break;
                AutoContainer.setStage(n, 5);
                break;
            }
            case 5: {
                if (bl) {
                    AutoContainer.setStage(n, 7);
                    break;
                }
                AutoContainer.setCommand(unit, UnitCommand.moveCommand);
                AutoContainer.goTo(unit, f3, f4, false);
                if (!unit.within(f3, f4, 40.0f)) break;
                AutoContainer.setCommand(unit, UnitCommand.loadBlocksCommand);
                AutoContainer.setStage(n, 6);
                AutoContainer.goTo(unit, f3, f4, true);
                break;
            }
            case 6: {
                if (bl) {
                    AutoContainer.setCommand(unit, UnitCommand.moveCommand);
                    AutoContainer.setStage(n, 7);
                    break;
                }
                if (AutoContainer.stageSecs(n) > 10.0f) {
                    AutoContainer.setCommand(unit, UnitCommand.moveCommand);
                    AutoContainer.setStage(n, 5);
                    break;
                }
                AutoContainer.setCommand(unit, UnitCommand.loadBlocksCommand);
                AutoContainer.pickStep(unit, n5, n6);
                break;
            }
            case 7: {
                if (!bl) {
                    AutoContainer.setStage(n, 5);
                    break;
                }
                int n7 = origAx == Integer.MIN_VALUE ? n3 : origAx;
                int n8 = origAy == Integer.MIN_VALUE ? n4 : origAy;
                int n9 = n8;
                if (!AutoContainer.approachExact(unit, n7, n8, f5)) break;
                AutoContainer.setCommand(unit, UnitCommand.unloadPayloadCommand);
                AutoContainer.setStage(n, 8);
                break;
            }
            case 8: {
                if (!bl) {
                    AutoContainer.setCommand(unit, UnitCommand.moveCommand);
                    AutoContainer.setStage(n, 9);
                    break;
                }
                if (!(AutoContainer.stageSecs(n) > 10.0f)) break;
                AutoContainer.setCommand(unit, UnitCommand.moveCommand);
                AutoContainer.setStage(n, 7);
                break;
            }
            case 9: {
                AutoContainer.setCommand(unit, UnitCommand.moveCommand);
                if (!(AutoContainer.stageSecs(n) >= AutoContainer.waitAfterReturn())) break;
                AutoContainer.setStage(n, 0);
                break;
            }
            default: {
                AutoContainer.setStage(n, 0);
            }
        }
    }

    static {
        origAx = Integer.MIN_VALUE;
        origAy = Integer.MIN_VALUE;
        blockSize = 2;
        phase = new IntIntMap();
        stageT = new IntFloatMap();
        lastCmd = new IntFloatMap();
        tmp = new Seq();
    }
}

