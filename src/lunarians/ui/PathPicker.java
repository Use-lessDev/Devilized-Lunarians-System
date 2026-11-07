package lunarians.ui;

import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.input.KeyCode;
import arc.scene.ui.layout.Table;
import arc.struct.IntSeq;
import arc.util.Time;
import lunarians.LunariansI18n;
import mindustry.Vars;
import mindustry.core.World;
import mindustry.game.EventType;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/PathPicker.class */
public final class PathPicker {
    private static Cons<IntSeq> callback;
    private static Runnable cancelled;
    private static float armedAt;
    private static boolean registered;
    private static Table hud;
    private static final IntSeq points = new IntSeq();
    private static boolean cameraMode = true;

    private PathPicker() {
    }

    public static void pick(Cons<IntSeq> cons, Runnable runnable) {
        callback = cons;
        cancelled = runnable;
        points.clear();
        armedAt = Time.time;
        cameraMode = true;
        showHud();
        if (!registered) {
            registered = true;
            Events.run(EventType.Trigger.update, new Runnable() { // from class: lunarians.ui.PathPicker.1
                @Override // java.lang.Runnable
                public void run() {
                    PathPicker.update();
                }
            });
            Events.run(EventType.Trigger.drawOver, new Runnable() { // from class: lunarians.ui.PathPicker.2
                @Override // java.lang.Runnable
                public void run() {
                    PathPicker.draw();
                }
            });
        }
        toast(LunariansI18n.get("pp_hint"));
    }

    private static void showHud() {
        hideHud();
        hud = new Table(Styles.black6);
        hud.defaults().pad(4.0f);
        rebuildHud();
        Core.scene.add(hud);
        hud.pack();
        hud.setPosition((Core.graphics.getWidth() / 2.0f) - (hud.getWidth() / 2.0f), 40.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void rebuildHud() {
        if (hud == null) {
            return;
        }
        hud.clearChildren();
        hud.add(LunariansI18n.get("pp_points", String.valueOf(points.size / 2))).colspan(3).padBottom(4.0f).row();
        hud.button(cameraMode ? LunariansI18n.get("pp_cam") : LunariansI18n.get("pp_unit"), new Runnable() { // from class: lunarians.ui.PathPicker.3
            @Override // java.lang.Runnable
            public void run() {
                boolean unused = PathPicker.cameraMode = !PathPicker.cameraMode;
                PathPicker.rebuildHud();
                PathPicker.toast(PathPicker.cameraMode ? LunariansI18n.get("pp_cam") : LunariansI18n.get("pp_unit"));
            }
        }).width(150.0f).height(40.0f).get().getLabel().setFontScale(0.85f);
        hud.button(LunariansI18n.get("pp_add"), new Runnable() { // from class: lunarians.ui.PathPicker.4
            @Override // java.lang.Runnable
            public void run() {
                PathPicker.addAtCurrent();
            }
        }).width(150.0f).height(40.0f);
        hud.button(LunariansI18n.get("pp_back"), new Runnable() { // from class: lunarians.ui.PathPicker.5
            @Override // java.lang.Runnable
            public void run() {
                if (PathPicker.points.size >= 2) {
                    PathPicker.points.removeIndex(PathPicker.points.size - 1);
                    PathPicker.points.removeIndex(PathPicker.points.size - 1);
                    PathPicker.rebuildHud();
                }
            }
        }).width(110.0f).height(40.0f).row();
        hud.button(LunariansI18n.get("pp_done"), new Runnable() { // from class: lunarians.ui.PathPicker.6
            @Override // java.lang.Runnable
            public void run() {
                if (PathPicker.points.size < 4) {
                    PathPicker.toast(LunariansI18n.get("pp_need2"));
                } else {
                    PathPicker.finish(false);
                }
            }
        }).width(150.0f).height(40.0f).colspan(2);
        hud.button(LunariansI18n.get("pp_cancel"), new Runnable() { // from class: lunarians.ui.PathPicker.7
            @Override // java.lang.Runnable
            public void run() {
                PathPicker.finish(true);
            }
        }).width(110.0f).height(40.0f);
        hud.pack();
        hud.setPosition((Core.graphics.getWidth() / 2.0f) - (hud.getWidth() / 2.0f), 40.0f);
    }

    private static void hideHud() {
        if (hud != null) {
            hud.remove();
            hud = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addAtCurrent() {
        int tile;
        int tile2;
        if (!cameraMode && Vars.player != null && Vars.player.unit() != null) {
            tile = Vars.player.unit().tileX();
            tile2 = Vars.player.unit().tileY();
        } else {
            tile = World.toTile(Core.camera.position.x);
            tile2 = World.toTile(Core.camera.position.y);
        }
        points.add(tile);
        points.add(tile2);
        rebuildHud();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void update() {
        if (callback == null) {
            return;
        }
        if (Vars.state == null || !Vars.state.isGame()) {
            finish(true);
        } else {
            if (Time.time - armedAt < 15.0f) {
                return;
            }
            if (Core.input.keyTap(KeyCode.escape) || Core.input.keyTap(KeyCode.back)) {
                finish(true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void draw() {
        if (callback == null) {
            return;
        }
        Draw.z(120.0f);
        Draw.color(Pal.accent);
        Lines.stroke(2.0f);
        float f = 0.0f;
        float f2 = 0.0f;
        for (int i = 0; i + 1 < points.size; i += 2) {
            float f3 = (points.get(i) * 8) + 4.0f;
            float f4 = (points.get(i + 1) * 8) + 4.0f;
            Lines.circle(f3, f4, 6.0f);
            if (i > 0) {
                Lines.line(f, f2, f3, f4);
            }
            f = f3;
            f2 = f4;
        }
        if (cameraMode) {
            float f5 = Core.camera.position.x;
            float f6 = Core.camera.position.y;
            Lines.line(f5 - 8.0f, f6, f5 + 8.0f, f6);
            Lines.line(f5, f6 - 8.0f, f5, f6 + 8.0f);
        }
        Draw.reset();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void finish(boolean z) {
        hideHud();
        Cons<IntSeq> cons = callback;
        Runnable runnable = cancelled;
        callback = null;
        cancelled = null;
        if (z) {
            if (runnable != null) {
                runnable.run();
            }
        } else if (cons != null) {
            IntSeq intSeq = new IntSeq();
            intSeq.addAll(points);
            cons.get(intSeq);
        }
        points.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void toast(String str) {
        if (Vars.ui != null) {
            Vars.ui.showInfoToast(str, 2.0f);
        }
    }
}
