package lunarians;

import arc.Core;
import arc.Events;
import arc.math.geom.Position;
import arc.util.Log;
import arc.util.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.entities.units.BuildPlan;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Unit;

public class ConveyorUpgrade {
  private static boolean registered;
  
  private static boolean running;
  
  private static float next;
  
  public static boolean enabled() {
    return Core.settings.getBool("lunarians-ti-conv", true);
  }
  
  public static void setEnabled(boolean paramBoolean) {
    Core.settings.put("lunarians-ti-conv", Boolean.valueOf(paramBoolean));
    if (!paramBoolean)
      running = false; 
  }
  
  public static void setThreshold(int paramInt) {
    Core.settings.put("lunarians-ti-conv-min", Integer.valueOf(Math.max(200, Math.min(10000, paramInt))));
  }
  
  public static int threshold() {
    return Math.max(100, Core.settings.getInt("lunarians-ti-conv-min", 2000));
  }
  
  public static String title() {
    String str = LunariansI18n.getLanguage();
    if ("pt".equals(str))
      return "Esteira de titanio automatica (2k)"; 
    if ("es".equals(str))
      return "Cinta de titanio automatica (2k)"; 
    if ("ru".equals(str))
      return "Авто-титановый конвейер (2k)"; 
    return "Auto titanium conveyor (2k)";
  }
  
  public static void init() {
    if (registered)
      return; 
    registered = true;
    Events.run(EventType.Trigger.update, ConveyorUpgrade::update);
    Events.on(EventType.WorldLoadEvent.class, paramWorldLoadEvent -> running = false);
  }
  
  private static void update() {
    if (!enabled() || Vars.state == null || !Vars.state.isGame() || Vars.player == null || Time.time < next)
      return; 
    next = Time.time + 180.0F;
    try {
      Unit unit1 = Vars.player.unit();
      if (unit1 == null || unit1.dead || !unit1.canBuild() || Vars.player.closestCore() == null)
        return; 
      if (AutoSet.isActive() || AutoEconomy.active)
        return; 
      int i = (Vars.player.closestCore()).items.get(Items.titanium);
      if (!running && i >= threshold())
        running = true; 
      if (running && i < threshold() / 4)
        running = false; 
      int j = (Vars.player.closestCore()).items.get(Items.copper), k = (Vars.player.closestCore()).items.get(Items.lead);
      if (!running || (unit1.plans()).size > 1 || j < 300 || k < 300)
        return; 
      final Unit u = unit1;
      ArrayList<Building> arrayList = new ArrayList();
      float f = unit1.type.buildRange * 0.9F;
      for (Building building : Groups.build) {
        if (building.team == Vars.player.team() && building.block == Blocks.conveyor && building.within((Position)unit1, f))
          arrayList.add(building); 
      } 
      if (arrayList.isEmpty())
        return; 
      Collections.sort(arrayList, new Comparator<Building>() {
            public int compare(Building param1Building1, Building param1Building2) {
              return Float.compare(param1Building1.dst2((Position)u), param1Building2.dst2((Position)u));
            }
          });
      int m = Math.min(8, Math.min(arrayList.size(), (Math.min(j, k) - 200) / 2));
      for (byte b = 0; b < m; b++) {
        Building building = arrayList.get(b);
        unit1.addBuild(new BuildPlan(building.tileX(), building.tileY(), building.rotation, Blocks.titaniumConveyor), true);
      } 
    } catch (Throwable throwable) {
      Log.err("[Lunarians] ConveyorUpgrade", throwable);
    } 
  }
}
