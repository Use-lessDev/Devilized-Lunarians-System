/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.Events
 *  arc.func.Cons
 *  arc.math.Mathf
 *  arc.math.geom.Point2
 *  arc.math.geom.Vec2
 *  arc.scene.ui.layout.Scl
 *  arc.struct.Seq
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.core.World
 *  mindustry.entities.units.BuildPlan
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.EventType$WorldLoadEvent
 *  mindustry.game.Schematic
 *  mindustry.input.Binding
 */
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
import lunarians.LunariansSettings;
import lunarians.ui.SlotButton;
import mindustry.Vars;
import mindustry.core.World;
import mindustry.entities.units.BuildPlan;
import mindustry.game.EventType;
import mindustry.game.Schematic;
import mindustry.input.Binding;

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
        Events.run((Object)EventType.Trigger.update, (Runnable)new Runnable(){

            @Override
            public void run() {
                SchematicPlacer.update();
            }
        });
        Events.on(EventType.WorldLoadEvent.class, (Cons)new Cons<EventType.WorldLoadEvent>(){

            public void get(EventType.WorldLoadEvent worldLoadEvent) {
                SchematicPlacer.deselect();
            }
        });
    }

    public static void setPanelVisible(boolean bl) {
        LunariansSettings.showSchematicPanel = bl;
        LunariansSettings.save();
        if (!bl) {
            SchematicPlacer.deselect();
        }
    }

    public static boolean isActive() {
        return schematic != null;
    }

    public static boolean isSelected(SlotButton slotButton) {
        return slotButton != null && selectedSlot == slotButton && schematic != null;
    }

    public static void select(SlotButton slotButton, Schematic schematic) {
        selectedSlot = slotButton;
        SchematicPlacer.schematic = schematic;
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
        LunariansSettings.schemRotation = LunariansSettings.schemRotation + 1 & 3;
        LunariansSettings.save();
    }

    private static void update() {
        if (schematic == null) {
            return;
        }
        try {
            clock += Time.delta / 60.0f;
            if (Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
                SchematicPlacer.deselect();
                return;
            }
            if (Vars.control.input.block != null) {
                SchematicPlacer.deselect();
                return;
            }
            if (!Vars.mobile && Core.input.keyTap(Binding.deselect) && !Core.scene.hasMouse()) {
                SchematicPlacer.deselect();
                return;
            }
            if (!Vars.mobile) {
                boolean bl = !Core.scene.hasMouse() && !Core.scene.hasDialog();
                boolean bl2 = bl;
                if (!armed) {
                    if (bl && Core.input.justTouched()) {
                        armed = true;
                        SchematicPlacer.stageDesktop();
                    }
                } else if (Vars.control.input.selectPlans.isEmpty() && bl) {
                    SchematicPlacer.stageDesktop();
                }
                return;
            }
            boolean bl = Core.input.isTouched();
            float f = Core.input.mouseX();
            float f2 = Core.input.mouseY();
            if (bl && !tracking) {
                tracking = true;
                downX = f;
                downY = f2;
                downTime = clock;
                moved = false;
                multiTouch = Core.input.isTouched(1);
                startedOnUi = Core.scene.hasMouse(f, f2) || Core.scene.hasDialog() || Core.scene.hasField();
            } else if (bl) {
                if (Core.input.isTouched(1)) {
                    multiTouch = true;
                }
                if (Math.abs(f - downX) > Scl.scl((float)14.0f) || Math.abs(f2 - downY) > Scl.scl((float)14.0f)) {
                    moved = true;
                }
            } else if (tracking) {
                tracking = false;
                if (!startedOnUi && !moved && !multiTouch && clock - downTime < 0.6f) {
                    SchematicPlacer.stageAt(downX, downY);
                }
            }
        }
        catch (Throwable throwable) {
            if (!errorLogged) {
                errorLogged = true;
                Log.err((String)"[Lunarians] SchematicPlacer error", (Throwable)throwable);
            }
            SchematicPlacer.deselect();
        }
    }

    private static void stageAt(float f, float f2) {
        Vec2 vec2 = Core.input.mouseWorld(f, f2);
        int n = World.toTile((float)vec2.x);
        int n2 = World.toTile((float)vec2.y);
        Seq seq = Vars.schematics.toPlans(schematic, n, n2);
        for (int i = 0; i < SchematicPlacer.rotation(); ++i) {
            SchematicPlacer.rotatePlans((Seq<BuildPlan>)seq, n, n2);
        }
        Seq seq2 = Vars.control.input.selectPlans;
        if (!seq2.isEmpty() && !seq.isEmpty()) {
            BuildPlan buildPlan = (BuildPlan)seq.first();
            for (int i = 0; i < seq2.size; ++i) {
                BuildPlan buildPlan2 = (BuildPlan)seq2.get(i);
                if (buildPlan2.x != buildPlan.x || buildPlan2.y != buildPlan.y || buildPlan2.block != buildPlan.block) continue;
                return;
            }
        }
        seq2.addAll(seq);
        Vars.control.input.lastSchematic = schematic;
    }

    private static void stageDesktop() {
        Vars.control.input.useSchematic(schematic);
        Vec2 vec2 = Core.input.mouseWorld();
        int n = World.toTile((float)vec2.x);
        int n2 = World.toTile((float)vec2.y);
        for (int i = 0; i < SchematicPlacer.rotation(); ++i) {
            SchematicPlacer.rotatePlans((Seq<BuildPlan>)Vars.control.input.selectPlans, n, n2);
        }
    }

    private static void rotatePlans(Seq<BuildPlan> seq, int n, int n2) {
        for (int i = 0; i < seq.size; ++i) {
            BuildPlan buildPlan = (BuildPlan)seq.get(i);
            if (buildPlan.breaking || buildPlan.block == null) continue;
            final float f = buildPlan.block.size % 2 == 0 ? -0.5f : 0.0f;
            buildPlan.pointConfig((Cons)new Cons<Point2>(){

                public void get(Point2 point2) {
                    float f4 = (float)point2.x + f;
                    float f2 = (float)point2.y + f;
                    float f3 = f4;
                    f4 = -f2;
                    f2 = f3;
                    point2.set(Mathf.floor((float)(f4 - f)), Mathf.floor((float)(f2 - f)));
                }
            });
            float f2 = (float)((buildPlan.x - n) * 8) + buildPlan.block.offset;
            float f3 = (float)((buildPlan.y - n2) * 8) + buildPlan.block.offset;
            float f4 = f2;
            f2 = -f3;
            f3 = f4;
            buildPlan.x = World.toTile((float)(f2 - buildPlan.block.offset)) + n;
            buildPlan.y = World.toTile((float)(f3 - buildPlan.block.offset)) + n2;
            buildPlan.rotation = buildPlan.block.planRotation(Mathf.mod((int)(buildPlan.rotation + 1), (int)4));
        }
    }
}

