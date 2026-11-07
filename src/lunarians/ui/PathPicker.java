/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.Events
 *  arc.func.Cons
 *  arc.graphics.Color
 *  arc.graphics.g2d.Draw
 *  arc.graphics.g2d.Lines
 *  arc.input.KeyCode
 *  arc.scene.Element
 *  arc.scene.ui.TextButton
 *  arc.scene.ui.layout.Table
 *  arc.struct.IntSeq
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.core.World
 *  mindustry.game.EventType$Trigger
 *  mindustry.graphics.Pal
 *  mindustry.ui.Styles
 */
package lunarians.ui;

import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.input.KeyCode;
import arc.scene.Element;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.IntSeq;
import arc.util.Time;
import lunarians.LunariansI18n;
import mindustry.Vars;
import mindustry.core.World;
import mindustry.game.EventType;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;

public final class PathPicker {
    private static Cons<IntSeq> callback;
    private static Runnable cancelled;
    private static float armedAt;
    private static boolean registered;
    private static Table hud;
    private static final IntSeq points;
    private static boolean cameraMode;

    private PathPicker() {
    }

    public static void pick(Cons<IntSeq> cons, Runnable runnable) {
        callback = cons;
        cancelled = runnable;
        points.clear();
        armedAt = Time.time;
        cameraMode = true;
        PathPicker.showHud();
        if (!registered) {
            registered = true;
            Events.run((Object)EventType.Trigger.update, (Runnable)new Runnable(){

                @Override
                public void run() {
                    PathPicker.update();
                }
            });
            Events.run((Object)EventType.Trigger.drawOver, (Runnable)new Runnable(){

                @Override
                public void run() {
                    PathPicker.draw();
                }
            });
        }
        PathPicker.toast(LunariansI18n.get("pp_hint"));
    }

    private static void showHud() {
        PathPicker.hideHud();
        hud = new Table(Styles.black6);
        hud.defaults().pad(4.0f);
        PathPicker.rebuildHud();
        Core.scene.add((Element)hud);
        hud.pack();
        hud.setPosition((float)Core.graphics.getWidth() / 2.0f - hud.getWidth() / 2.0f, 40.0f);
    }

    private static void rebuildHud() {
        if (hud == null) {
            return;
        }
        hud.clearChildren();
        hud.add((CharSequence)LunariansI18n.get("pp_points", String.valueOf(PathPicker.points.size / 2))).colspan(3).padBottom(4.0f).row();
        ((TextButton)hud.button(cameraMode ? LunariansI18n.get("pp_cam") : LunariansI18n.get("pp_unit"), new Runnable(){

            @Override
            public void run() {
                cameraMode = !cameraMode;
                PathPicker.rebuildHud();
                PathPicker.toast(cameraMode ? LunariansI18n.get("pp_cam") : LunariansI18n.get("pp_unit"));
            }
        }).width(150.0f).height(40.0f).get()).getLabel().setFontScale(0.85f);
        hud.button(LunariansI18n.get("pp_add"), new Runnable(){

            @Override
            public void run() {
                PathPicker.addAtCurrent();
            }
        }).width(150.0f).height(40.0f);
        hud.button(LunariansI18n.get("pp_back"), new Runnable(){

            @Override
            public void run() {
                if (points.size >= 2) {
                    points.removeIndex(points.size - 1);
                    points.removeIndex(points.size - 1);
                    PathPicker.rebuildHud();
                }
            }
        }).width(110.0f).height(40.0f).row();
        hud.button(LunariansI18n.get("pp_done"), new Runnable(){

            @Override
            public void run() {
                if (points.size < 4) {
                    PathPicker.toast(LunariansI18n.get("pp_need2"));
                } else {
                    PathPicker.finish(false);
                }
            }
        }).width(150.0f).height(40.0f).colspan(2);
        hud.button(LunariansI18n.get("pp_cancel"), new Runnable(){

            @Override
            public void run() {
                PathPicker.finish(true);
            }
        }).width(110.0f).height(40.0f);
        hud.pack();
        hud.setPosition((float)Core.graphics.getWidth() / 2.0f - hud.getWidth() / 2.0f, 40.0f);
    }

    private static void hideHud() {
        if (hud != null) {
            hud.remove();
            hud = null;
        }
    }

    private static void addAtCurrent() {
        int n;
        int n2;
        if (!cameraMode && Vars.player != null && Vars.player.unit() != null) {
            n2 = Vars.player.unit().tileX();
            n = Vars.player.unit().tileY();
        } else {
            n2 = World.toTile((float)Core.camera.position.x);
            n = World.toTile((float)Core.camera.position.y);
        }
        points.add(n2);
        points.add(n);
        PathPicker.rebuildHud();
    }

    private static void update() {
        if (callback == null) {
            return;
        }
        if (Vars.state == null || !Vars.state.isGame()) {
            PathPicker.finish(true);
            return;
        }
        if (Time.time - armedAt < 15.0f) {
            return;
        }
        if (Core.input.keyTap(KeyCode.escape) || Core.input.keyTap(KeyCode.back)) {
            PathPicker.finish(true);
        }
    }

    private static void draw() {
        float f;
        if (callback == null) {
            return;
        }
        Draw.z((float)120.0f);
        Draw.color((Color)Pal.accent);
        Lines.stroke((float)2.0f);
        float f2 = 0.0f;
        float f3 = 0.0f;
        int n = 0;
        while (n + 1 < PathPicker.points.size) {
            f = (float)(points.get(n) * 8) + 4.0f;
            float f4 = (float)(points.get(n + 1) * 8) + 4.0f;
            Lines.circle((float)f, (float)f4, (float)6.0f);
            if (n > 0) {
                Lines.line((float)f2, (float)f3, (float)f, (float)f4);
            }
            f2 = f;
            f3 = f4;
            n += 2;
        }
        if (cameraMode) {
            float f5 = Core.camera.position.x;
            f = Core.camera.position.y;
            Lines.line((float)(f5 - 8.0f), (float)f, (float)(f5 + 8.0f), (float)f);
            Lines.line((float)f5, (float)(f - 8.0f), (float)f5, (float)(f + 8.0f));
        }
        Draw.reset();
    }

    private static void finish(boolean bl) {
        PathPicker.hideHud();
        Cons<IntSeq> cons = callback;
        Runnable runnable = cancelled;
        callback = null;
        cancelled = null;
        if (bl) {
            if (runnable != null) {
                runnable.run();
            }
        } else if (cons != null) {
            IntSeq intSeq = new IntSeq();
            intSeq.addAll(points);
            cons.get((Object)intSeq);
        }
        points.clear();
    }

    private static void toast(String string) {
        if (Vars.ui != null) {
            Vars.ui.showInfoToast(string, 2.0f);
        }
    }

    static {
        points = new IntSeq();
        cameraMode = true;
    }
}

