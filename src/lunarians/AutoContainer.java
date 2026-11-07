package lunarians;

import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.geom.Vec2;
import arc.struct.IntFloatMap;
import arc.struct.IntIntMap;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.core.World;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Payloadc;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.ui.Fonts;
import mindustry.world.blocks.payloads.BuildPayload;
import mindustry.world.blocks.payloads.Payload;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoContainer.class */
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
    private static int origAx = Integer.MIN_VALUE;
    private static int origAy = Integer.MIN_VALUE;
    private static int blockSize = 2;
    private static final IntIntMap phase = new IntIntMap();
    private static final IntFloatMap stageT = new IntFloatMap();
    private static final IntFloatMap lastCmd = new IntFloatMap();
    private static final Seq<Unit> tmp = new Seq<>();

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoContainer$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoContainer.$r8$lambda$Du9ibmhpULYWjgoZ2bLmUvQtlLM();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoContainer$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass1 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoContainer.$r8$lambda$sRtOXp3HJOMUh4aj9IToqPImwkw();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoContainer$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass2 implements Cons {
        public final void get(Object obj) {
            AutoContainer.reset();
        }
    }

    public static float waitAfterReturn() {
        return Math.max(0.0f, Core.settings.getFloat("lunarians-container-wait2", 5.0f));
    }

    public static void setWaitAfterReturn(float f) {
        Core.settings.put("lunarians-container-wait2", Float.valueOf(f));
    }

    public static float waitAfterPlace() {
        return Math.max(0.0f, LunariansSettings.containerWaitSec);
    }

    public static boolean hasPoints() {
        return (LunariansSettings.containerPickX == Integer.MIN_VALUE || LunariansSettings.containerDropX == Integer.MIN_VALUE) ? false : true;
    }

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run(EventType.Trigger.update, AutoContainer::update);
        Events.run(EventType.Trigger.draw, AutoContainer::draw);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            reset();
        });
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

    public static void setPoints(int i, int i2, int i3, int i4) {
        LunariansSettings.containerPickX = i;
        LunariansSettings.containerPickY = i2;
        LunariansSettings.containerDropX = i3;
        LunariansSettings.containerDropY = i4;
        origAy = Integer.MIN_VALUE;
        origAx = Integer.MIN_VALUE;
        phase.clear();
        stageT.clear();
        showMarks();
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

    private static float centerOf(int i, int i2) {
        return (i * 8.0f) + (((i2 + 1) % 2) * 4.0f);
    }

    private static void box(int i, int i2, int i3, Color color, String str) {
        float centerOf = centerOf(i, i3);
        float centerOf2 = centerOf(i2, i3);
        float f = i3 * 4.0f;
        Lines.stroke(2.0f);
        Draw.color(Color.black, 0.6f);
        Lines.rect((centerOf - f) - 0.6f, (centerOf2 - f) - 0.6f, (i3 * 8.0f) + 1.2f, (i3 * 8.0f) + 1.2f);
        Draw.color(color, 0.95f);
        Lines.rect(centerOf - f, centerOf2 - f, i3 * 8.0f, i3 * 8.0f);
        try {
            Fonts.outline.draw(str, centerOf, centerOf2 + 4.0f, Color.white, 0.22f, false, 1);
        } catch (Throwable th) {
        }
        Draw.color();
    }

    private static void draw() {
        if (Vars.state == null || !Vars.state.isGame() || !hasPoints()) {
            return;
        }
        if (!LunariansSettings.enableAutoContainer && Time.time > markUntil) {
            return;
        }
        int i = LunariansSettings.containerPickX;
        int i2 = LunariansSettings.containerPickY;
        int i3 = LunariansSettings.containerDropX;
        int i4 = LunariansSettings.containerDropY;
        int i5 = blockSize;
        int i6 = i;
        int i7 = i2;
        try {
            Building build = Vars.world.build(i, i2);
            if (build != null && build.block != null) {
                i5 = build.block.size;
                i6 = build.tileX();
                i7 = build.tileY();
            }
        } catch (Throwable th) {
        }
        Draw.z(120.0f);
        Lines.stroke(1.5f);
        Draw.color(Pal.accent, 0.7f);
        Lines.dashLine(centerOf(i6, i5), centerOf(i7, i5), centerOf(i3, i5), centerOf(i4, i5), 14);
        box(i6, i7, i5, Color.orange, "A");
        box(i3, i4, i5, Color.cyan, "B");
        Draw.reset();
    }

    private static void update() {
        if (!LunariansSettings.enableAutoContainer || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
            count = 0;
            return;
        }
        float f = timer + (Time.delta / 60.0f);
        timer = f;
        if (f < LunariansSettings.actionInterval(0.3f)) {
            return;
        }
        timer = 0.0f;
        try {
            scan();
        } catch (Throwable th) {
            if (!errorLogged) {
                errorLogged = true;
                Log.err("[Lunarians] AutoContainer", th);
            }
        }
    }

    private static void scan() {
        count = 0;
        if (LunariansSettings.containerUnit == null || LunariansSettings.containerUnit.isEmpty() || !hasPoints()) {
            return;
        }
        tmp.clear();
        int size = Groups.unit.size();
        int i = LunariansSettings.containerMaxUnits;
        for (int i2 = 0; i2 < size; i2++) {
            Unit index = Groups.unit.index(i2);
            if (index != null && index.isValid() && index.team == Vars.player.team() && (index instanceof Payloadc) && (index.controller() instanceof CommandAI) && index.type != null && LunariansSettings.containerUnit.equals(index.type.name)) {
                tmp.add(index);
                if (i > 0 && tmp.size >= i) {
                    break;
                }
            }
        }
        for (int i3 = 0; i3 < tmp.size; i3++) {
            drive((Unit) tmp.get(i3), (Payloadc) tmp.get(i3));
        }
        count = tmp.size;
    }

    private static void setStage(int i, int i2) {
        phase.put(i, i2);
        stageT.put(i, Time.time);
        lastCmd.remove(i, 0.0f);
    }

    private static float stageSecs(int i) {
        return (Time.time - stageT.get(i, Time.time)) / 60.0f;
    }

    private static void setCommand(Unit unit, UnitCommand unitCommand) {
        try {
            if (unit.controller().command != unitCommand) {
                Call.setUnitCommand(Vars.player, new int[]{unit.id}, unitCommand);
            }
        } catch (Throwable th) {
        }
    }

    private static void goTo(Unit unit, float f, float f2, boolean z) {
        float f3 = lastCmd.get(unit.id, -9999.0f);
        if (!z && (Time.time - f3) / 60.0f < 2.5f) {
            return;
        }
        lastCmd.put(unit.id, Time.time);
        Call.commandUnits(Vars.player, new int[]{unit.id}, (Building) null, (Unit) null, new Vec2(f, f2), false, true);
    }

    private static float dropOffset(Payloadc payloadc) {
        try {
            BuildPayload buildPayload = (Payload) payloadc.payloads().peek();
            if (buildPayload instanceof BuildPayload) {
                return buildPayload.block().offset;
            }
        } catch (Throwable th) {
        }
        return ((blockSize + 1) % 2) * 4.0f;
    }

    private static boolean approachExact(Unit unit, int i, int i2, float f) {
        float f2 = (i * 8.0f) + f;
        float f3 = (i2 * 8.0f) + f;
        if (!unit.within(f2, f3, 40.0f)) {
            setCommand(unit, UnitCommand.moveCommand);
            goTo(unit, f2, f3, false);
            return false;
        }
        setCommand(unit, UnitCommand.loadBlocksCommand);
        goTo(unit, f2, f3, false);
        return World.toTile(unit.x - f) == i && World.toTile(unit.y - f) == i2 && unit.within(f2, f3, 2.0f);
    }

    private static float[] pickCenter(int i, int i2) {
        Building build = Vars.world.build(i, i2);
        if (build != null) {
            return new float[]{build.x, build.y};
        }
        return new float[]{i * 8.0f, i2 * 8.0f};
    }

    private static void pickStep(Unit unit, int i, int i2) {
        Building build = Vars.world.build(i, i2);
        Building buildWorld = Vars.world.buildWorld(unit.x, unit.y);
        float[] pickCenter = pickCenter(i, i2);
        if (build != null && buildWorld == build && unit.within(pickCenter[0], pickCenter[1], REACH)) {
            goTo(unit, unit.x, unit.y, true);
        } else {
            goTo(unit, pickCenter[0], pickCenter[1], false);
        }
    }

    private static void drive(Unit unit, Payloadc payloadc) {
        int i = unit.id;
        int i2 = phase.get(i, -1);
        int i3 = LunariansSettings.containerPickX;
        int i4 = LunariansSettings.containerPickY;
        int i5 = LunariansSettings.containerDropX;
        int i6 = LunariansSettings.containerDropY;
        float[] pickCenter = pickCenter(i3, i4);
        float[] pickCenter2 = pickCenter(i5, i6);
        float f = pickCenter[0];
        float f2 = pickCenter[1];
        float f3 = pickCenter2[0];
        float f4 = pickCenter2[1];
        float dropOffset = dropOffset(payloadc);
        boolean hasPayload = payloadc.hasPayload();
        if (i2 < 0) {
            setStage(i, hasPayload ? 2 : 0);
            i2 = phase.get(i, 0);
        }
        switch (i2) {
            case 0:
                if (hasPayload) {
                    setStage(i, 2);
                    break;
                } else {
                    setCommand(unit, UnitCommand.moveCommand);
                    goTo(unit, f, f2, false);
                    if (unit.within(f, f2, 40.0f)) {
                        try {
                            Building build = Vars.world.build(i3, i4);
                            if (build != null && build.block != null) {
                                origAx = build.tileX();
                                origAy = build.tileY();
                                blockSize = build.block.size;
                            }
                        } catch (Throwable th) {
                        }
                        setCommand(unit, UnitCommand.loadBlocksCommand);
                        setStage(i, 1);
                        goTo(unit, f, f2, true);
                        break;
                    }
                }
                break;
            case 1:
                if (!hasPayload) {
                    if (stageSecs(i) > STAGE_TIMEOUT) {
                        setCommand(unit, UnitCommand.moveCommand);
                        setStage(i, 0);
                        break;
                    } else {
                        setCommand(unit, UnitCommand.loadBlocksCommand);
                        pickStep(unit, i3, i4);
                        break;
                    }
                } else {
                    setCommand(unit, UnitCommand.moveCommand);
                    setStage(i, 2);
                    break;
                }
            case 2:
                if (!hasPayload) {
                    setStage(i, 0);
                    break;
                } else if (approachExact(unit, i5, i6, dropOffset)) {
                    setCommand(unit, UnitCommand.unloadPayloadCommand);
                    setStage(i, 3);
                    break;
                }
                break;
            case 3:
                if (!hasPayload) {
                    setCommand(unit, UnitCommand.moveCommand);
                    setStage(i, 4);
                    break;
                } else if (stageSecs(i) > STAGE_TIMEOUT) {
                    setCommand(unit, UnitCommand.moveCommand);
                    setStage(i, 2);
                    break;
                }
                break;
            case 4:
                setCommand(unit, UnitCommand.moveCommand);
                if (stageSecs(i) >= waitAfterPlace()) {
                    setStage(i, 5);
                    break;
                }
                break;
            case 5:
                if (hasPayload) {
                    setStage(i, GO_A2);
                    break;
                } else {
                    setCommand(unit, UnitCommand.moveCommand);
                    goTo(unit, f3, f4, false);
                    if (unit.within(f3, f4, 40.0f)) {
                        setCommand(unit, UnitCommand.loadBlocksCommand);
                        setStage(i, LOAD_B);
                        goTo(unit, f3, f4, true);
                        break;
                    }
                }
                break;
            case LOAD_B /* 6 */:
                if (!hasPayload) {
                    if (stageSecs(i) > STAGE_TIMEOUT) {
                        setCommand(unit, UnitCommand.moveCommand);
                        setStage(i, 5);
                        break;
                    } else {
                        setCommand(unit, UnitCommand.loadBlocksCommand);
                        pickStep(unit, i5, i6);
                        break;
                    }
                } else {
                    setCommand(unit, UnitCommand.moveCommand);
                    setStage(i, GO_A2);
                    break;
                }
            case GO_A2 /* 7 */:
                if (!hasPayload) {
                    setStage(i, 5);
                    break;
                } else {
                    if (approachExact(unit, origAx == Integer.MIN_VALUE ? i3 : origAx, origAy == Integer.MIN_VALUE ? i4 : origAy, dropOffset)) {
                        setCommand(unit, UnitCommand.unloadPayloadCommand);
                        setStage(i, 8);
                        break;
                    }
                }
                break;
            case 8:
                if (!hasPayload) {
                    setCommand(unit, UnitCommand.moveCommand);
                    setStage(i, WAIT2);
                    break;
                } else if (stageSecs(i) > STAGE_TIMEOUT) {
                    setCommand(unit, UnitCommand.moveCommand);
                    setStage(i, GO_A2);
                    break;
                }
                break;
            case WAIT2 /* 9 */:
                setCommand(unit, UnitCommand.moveCommand);
                if (stageSecs(i) >= waitAfterReturn()) {
                    setStage(i, 0);
                    break;
                }
                break;
            default:
                setStage(i, 0);
                break;
        }
    }
}
