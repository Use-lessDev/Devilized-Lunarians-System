package lunarians.ui;

import arc.Core;
import arc.Events;
import arc.func.Cons;
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
  
  private static final IntSeq points = new IntSeq();
  
  private static boolean cameraMode = true;
  
  public static void pick(Cons<IntSeq> paramCons, Runnable paramRunnable) {
    callback = paramCons;
    cancelled = paramRunnable;
    points.clear();
    armedAt = Time.time;
    cameraMode = true;
    showHud();
    if (!registered) {
      registered = true;
      Events.run(EventType.Trigger.update, new Runnable() {
            public void run() {
              PathPicker.update();
            }
          });
      Events.run(EventType.Trigger.drawOver, new Runnable() {
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
    hud.defaults().pad(4.0F);
    rebuildHud();
    Core.scene.add((Element)hud);
    hud.pack();
    hud.setPosition(Core.graphics.getWidth() / 2.0F - hud.getWidth() / 2.0F, 40.0F);
  }
  
  private static void rebuildHud() {
    if (hud == null)
      return; 
    hud.clearChildren();
    hud.add(LunariansI18n.get("pp_points", String.valueOf(points.size / 2))).colspan(3).padBottom(4.0F).row();
    ((TextButton)hud.button(cameraMode ? LunariansI18n.get("pp_cam") : LunariansI18n.get("pp_unit"), new Runnable() {
          public void run() {
            PathPicker.cameraMode = !PathPicker.cameraMode;
            PathPicker.rebuildHud();
            PathPicker.toast(PathPicker.cameraMode ? LunariansI18n.get("pp_cam") : LunariansI18n.get("pp_unit"));
          }
        }).width(150.0F).height(40.0F).get()).getLabel().setFontScale(0.85F);
    hud.button(LunariansI18n.get("pp_add"), new Runnable() {
          public void run() {
            PathPicker.addAtCurrent();
          }
        }).width(150.0F).height(40.0F);
    hud.button(LunariansI18n.get("pp_back"), new Runnable() {
          public void run() {
            if (PathPicker.points.size >= 2) {
              PathPicker.points.removeIndex(PathPicker.points.size - 1);
              PathPicker.points.removeIndex(PathPicker.points.size - 1);
              PathPicker.rebuildHud();
            } 
          }
        }).width(110.0F).height(40.0F).row();
    hud.button(LunariansI18n.get("pp_done"), new Runnable() {
          public void run() {
            if (PathPicker.points.size < 4) {
              PathPicker.toast(LunariansI18n.get("pp_need2"));
            } else {
              PathPicker.finish(false);
            } 
          }
        }).width(150.0F).height(40.0F).colspan(2);
    hud.button(LunariansI18n.get("pp_cancel"), new Runnable() {
          public void run() {
            PathPicker.finish(true);
          }
        }).width(110.0F).height(40.0F);
    hud.pack();
    hud.setPosition(Core.graphics.getWidth() / 2.0F - hud.getWidth() / 2.0F, 40.0F);
  }
  
  private static void hideHud() {
    if (hud != null) {
      hud.remove();
      hud = null;
    } 
  }
  
  private static void addAtCurrent() {
    int i, j;
    if (!cameraMode && Vars.player != null && Vars.player.unit() != null) {
      i = Vars.player.unit().tileX();
      j = Vars.player.unit().tileY();
    } else {
      i = World.toTile(Core.camera.position.x);
      j = World.toTile(Core.camera.position.y);
    } 
    points.add(i);
    points.add(j);
    rebuildHud();
  }
  
  private static void update() {
    if (callback == null)
      return; 
    if (Vars.state == null || !Vars.state.isGame()) {
      finish(true);
      return;
    } 
    if (Time.time - armedAt < 15.0F)
      return; 
    if (Core.input.keyTap(KeyCode.escape) || Core.input.keyTap(KeyCode.back))
      finish(true); 
  }
  
  private static void draw() {
    if (callback == null)
      return; 
    Draw.z(120.0F);
    Draw.color(Pal.accent);
    Lines.stroke(2.0F);
    float f1 = 0.0F;
    float f2 = 0.0F;
    for (byte b = 0; b + 1 < points.size; b += 2) {
      float f3 = (points.get(b) * 8) + 4.0F;
      float f4 = (points.get(b + 1) * 8) + 4.0F;
      Lines.circle(f3, f4, 6.0F);
      if (b > 0)
        Lines.line(f1, f2, f3, f4); 
      f1 = f3;
      f2 = f4;
    } 
    if (cameraMode) {
      float f3 = Core.camera.position.x;
      float f4 = Core.camera.position.y;
      Lines.line(f3 - 8.0F, f4, f3 + 8.0F, f4);
      Lines.line(f3, f4 - 8.0F, f3, f4 + 8.0F);
    } 
    Draw.reset();
  }
  
  private static void finish(boolean paramBoolean) {
    hideHud();
    Cons<IntSeq> cons = callback;
    Runnable runnable = cancelled;
    callback = null;
    cancelled = null;
    if (paramBoolean) {
      if (runnable != null)
        runnable.run(); 
    } else if (cons != null) {
      IntSeq intSeq = new IntSeq();
      intSeq.addAll(points);
      cons.get(intSeq);
    } 
    points.clear();
  }
  
  private static void toast(String paramString) {
    if (Vars.ui != null)
      Vars.ui.showInfoToast(paramString, 2.0F); 
  }
}
