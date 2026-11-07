package lunarians;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.util.Log;
import arc.util.Time;
import java.lang.reflect.Field;
import java.util.HashMap;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.world.Tile;

public class UsedArea {
  private static final float DELAY = 420.0F;
  
  private static boolean registered;
  
  private static boolean hasArea;
  
  private static int ax;
  
  private static int ay;
  
  private static float ar;
  
  private static float next;
  
  private static final HashMap<Integer, Float> seen = new HashMap<>();
  
  private static boolean[] red;
  
  public static void init() {
    if (registered)
      return; 
    registered = true;
    Events.run(EventType.Trigger.update, UsedArea::update);
    Events.run(EventType.Trigger.draw, UsedArea::draw);
    Events.on(EventType.WorldLoadEvent.class, paramWorldLoadEvent -> reset());
  }
  
  private static void reset() {
    hasArea = false;
    seen.clear();
    red = null;
  }
  
  private static Field fld(String paramString) throws Exception {
    Field field = AutoSet.class.getDeclaredField(paramString);
    field.setAccessible(true);
    return field;
  }
  
  private static void update() {
    if (Vars.state == null || !Vars.state.isGame() || Vars.world == null)
      return; 
    try {
      if (AutoSet.isRunning()) {
        int m = fld("cx").getInt(null), n = fld("cy").getInt(null);
        float f = fld("radius").getFloat(null);
        if (!hasArea || m != ax || n != ay || f != ar) {
          seen.clear();
          red = null;
        } 
        ax = m;
        ay = n;
        ar = f;
        hasArea = true;
      } 
      if (!hasArea || Time.time < next)
        return; 
      next = Time.time + 60.0F;
      int i = Vars.world.width();
      if (red == null || red.length != i * Vars.world.height())
        red = new boolean[i * Vars.world.height()]; 
      int j = (int)Math.ceil(ar);
      for (int k = ay - j; k <= ay + j; k++) {
        for (int m = ax - j; m <= ax + j; m++) {
          if (Vars.world.tiles.in(m, k) && Mathf.dst(m, k, ax, ay) <= ar) {
            int n = k * i + m;
            Tile tile = Vars.world.tile(m, k);
            if (tile != null && tile.build != null) {
              Float float_ = seen.get(Integer.valueOf(n));
              if (float_ == null) {
                seen.put(Integer.valueOf(n), Float.valueOf(Time.time));
              } else if (Time.time - float_.floatValue() >= 420.0F) {
                red[n] = true;
              } 
            } else if (seen.remove(Integer.valueOf(n)) != null || red[n]) {
              red[n] = false;
            } 
          } 
        } 
      } 
    } catch (Throwable throwable) {
      Log.err("[Lunarians] UsedArea", throwable);
      reset();
    } 
  }
  
  private static void draw() {
    if (red == null || !hasArea || Vars.state == null || !Vars.state.isGame())
      return; 
    int i = Vars.world.width();
    float f1 = Core.camera.position.x, f2 = Core.camera.position.y;
    float f3 = Core.camera.width / 2.0F + 16.0F, f4 = Core.camera.height / 2.0F + 16.0F;
    int j = Math.max(0, (int)((f1 - f3) / 8.0F)), k = Math.min(i - 1, (int)((f1 + f3) / 8.0F));
    int m = Math.max(0, (int)((f2 - f4) / 8.0F)), n = Math.min(Vars.world.height() - 1, (int)((f2 + f4) / 8.0F));
    Draw.z(119.0F);
    Draw.color(Color.scarlet, 0.45F);
    for (int i1 = m; i1 <= n; i1++) {
      for (int i2 = j; i2 <= k; i2++) {
        if (red[i1 * i + i2])
          Fill.rect(i2 * 8.0F, i1 * 8.0F, 8.0F, 8.0F); 
      } 
    } 
    Draw.reset();
  }
}
