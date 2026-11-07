package lunarians;

import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Scl;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import lunarians.ui.SlotButton;
import mindustry.Vars;
import mindustry.core.World;
import mindustry.entities.units.BuildPlan;
import mindustry.game.EventType;
import mindustry.game.Schematic;
import mindustry.input.Binding;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/SchematicPlacer.class */
public class SchematicPlacer {
    public static SlotButton selectedSlot;
    public static Schematic schematic;
    private static boolean tracking;
    private static boolean startedOnUi;
    private static boolean moved;
    private static boolean multiTouch;
    private static float downX;
    private static float downY;
    private static float downTime;
    private static float clock;
    private static boolean errorLogged;
    private static boolean armed;

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run(EventType.Trigger.update, new Runnable() { // from class: lunarians.SchematicPlacer.1
            @Override // java.lang.Runnable
            public void run() {
                SchematicPlacer.update();
            }
        });
        Events.on(EventType.WorldLoadEvent.class, new Cons<EventType.WorldLoadEvent>() { // from class: lunarians.SchematicPlacer.2
            public void get(EventType.WorldLoadEvent worldLoadEvent) {
                SchematicPlacer.deselect();
            }
        });
    }

    public static void setPanelVisible(boolean z) {
        LunariansSettings.showSchematicPanel = z;
        LunariansSettings.save();
        if (!z) {
            deselect();
        }
    }

    public static boolean isActive() {
        return schematic != null;
    }

    public static boolean isSelected(SlotButton slotButton) {
        return (slotButton == null || selectedSlot != slotButton || schematic == null) ? false : true;
    }

    public static void select(SlotButton slotButton, Schematic schematic2) {
        selectedSlot = slotButton;
        schematic = schematic2;
        tracking = false;
        armed = false;
    }

    public static void deselect() {
        if (schematic != null && Vars.control != null && Vars.control.input != null && Vars.control.input.lastSchematic == schematic) {
            Vars.control.input.selectPlans.clear();
        }
        selectedSlot = null;
        armed = false;
        schematic = null;
        tracking = false;
    }

    public static int rotation() {
        return LunariansSettings.schemRotation;
    }

    public static void cycleRotation() {
        LunariansSettings.schemRotation = (LunariansSettings.schemRotation + 1) & 3;
        LunariansSettings.save();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void update() {
        if (schematic == null) {
            return;
        }
        try {
            clock += Time.delta / 60.0f;
            if (Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
                deselect();
                return;
            }
            if (Vars.control.input.block != null) {
                deselect();
                return;
            }
            if (!Vars.mobile && Core.input.keyTap(Binding.deselect) && !Core.scene.hasMouse()) {
                deselect();
                return;
            }
            if (!Vars.mobile) {
                boolean z = (Core.scene.hasMouse() || Core.scene.hasDialog()) ? false : true;
                if (!armed) {
                    if (z && Core.input.justTouched()) {
                        armed = true;
                        stageDesktop();
                        return;
                    }
                    return;
                }
                if (Vars.control.input.selectPlans.isEmpty() && z) {
                    stageDesktop();
                    return;
                }
                return;
            }
            boolean isTouched = Core.input.isTouched();
            float mouseX = Core.input.mouseX();
            float mouseY = Core.input.mouseY();
            if (isTouched && !tracking) {
                tracking = true;
                downX = mouseX;
                downY = mouseY;
                downTime = clock;
                moved = false;
                multiTouch = Core.input.isTouched(1);
                startedOnUi = Core.scene.hasMouse(mouseX, mouseY) || Core.scene.hasDialog() || Core.scene.hasField();
            } else if (isTouched) {
                if (Core.input.isTouched(1)) {
                    multiTouch = true;
                }
                if (Math.abs(mouseX - downX) > Scl.scl(14.0f) || Math.abs(mouseY - downY) > Scl.scl(14.0f)) {
                    moved = true;
                }
            } else if (tracking) {
                tracking = false;
                if (!startedOnUi && !moved && !multiTouch && clock - downTime < 0.6f) {
                    stageAt(downX, downY);
                }
            }
        } catch (Throwable th) {
            if (!errorLogged) {
                errorLogged = true;
                Log.err("[Lunarians] SchematicPlacer error", th);
            }
            deselect();
        }
    }

    private static void stageAt(float f, float f2) {
        Vec2 mouseWorld = Core.input.mouseWorld(f, f2);
        int tile = World.toTile(mouseWorld.x);
        int tile2 = World.toTile(mouseWorld.y);
        Seq plans = Vars.schematics.toPlans(schematic, tile, tile2);
        for (int i = 0; i < rotation(); i++) {
            rotatePlans(plans, tile, tile2);
        }
        Seq seq = Vars.control.input.selectPlans;
        if (!seq.isEmpty() && !plans.isEmpty()) {
            BuildPlan buildPlan = (BuildPlan) plans.first();
            for (int i2 = 0; i2 < seq.size; i2++) {
                BuildPlan buildPlan2 = (BuildPlan) seq.get(i2);
                if (buildPlan2.x == buildPlan.x && buildPlan2.y == buildPlan.y && buildPlan2.block == buildPlan.block) {
                    return;
                }
            }
        }
        seq.addAll(plans);
        Vars.control.input.lastSchematic = schematic;
    }

    private static void stageDesktop() {
        Vars.control.input.useSchematic(schematic);
        Vec2 mouseWorld = Core.input.mouseWorld();
        int tile = World.toTile(mouseWorld.x);
        int tile2 = World.toTile(mouseWorld.y);
        for (int i = 0; i < rotation(); i++) {
            rotatePlans(Vars.control.input.selectPlans, tile, tile2);
        }
    }

    private static void rotatePlans(Seq<BuildPlan> seq, int i, int i2) {
        for (int i3 = 0; i3 < seq.size; i3++) {
            BuildPlan buildPlan = (BuildPlan) seq.get(i3);
            if (!buildPlan.breaking && buildPlan.block != null) {
                final float f = buildPlan.block.size % 2 == 0 ? -0.5f : 0.0f;
                buildPlan.pointConfig(new Cons<Point2>() { // from class: lunarians.SchematicPlacer.3
                    public void get(Point2 point2) {
                        point2.set(Mathf.floor((-(point2.y + f)) - f), Mathf.floor((point2.x + f) - f));
                    }
                });
                float f2 = ((buildPlan.x - i) * 8) + buildPlan.block.offset;
                buildPlan.x = World.toTile((-(((buildPlan.y - i2) * 8) + buildPlan.block.offset)) - buildPlan.block.offset) + i;
                buildPlan.y = World.toTile(f2 - buildPlan.block.offset) + i2;
                buildPlan.rotation = buildPlan.block.planRotation(Mathf.mod(buildPlan.rotation + 1, 4));
            }
        }
    }
}
