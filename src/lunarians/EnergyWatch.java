package lunarians;

import arc.Events;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.world.blocks.power.PowerGenerator;

public class EnergyWatch {
  private static boolean registered;
  
  private static float next;
  
  private static float lastWarn = -99999.0F;
  
  public static void init() {
    if (registered)
      return; 
    registered = true;
    Events.run(EventType.Trigger.update, EnergyWatch::update);
  }
  
  private static void update() {
    if (Vars.state == null || !Vars.state.isGame() || Vars.player == null || Time.time < next)
      return; 
    next = Time.time + 600.0F;
    try {
      if (Vars.player.closestCore() == null || (Vars.player.closestCore()).items.get(Items.coal) >= 50)
        return; 
      for (Building building : Groups.build) {
        if (building.team == Vars.player.team() && building.block == Blocks.combustionGenerator && 
          building instanceof PowerGenerator.GeneratorBuild && ((PowerGenerator.GeneratorBuild)building).productionEfficiency <= 0.0F) {
          if (Time.time - lastWarn > 3600.0F && Vars.ui != null) {
            lastWarn = Time.time;
            String str = LunariansI18n.getLanguage();
            Vars.ui.showInfoToast("pt".equals(str) ? "Energia: o núcleo está sem carvão, geradores parados." : ("es".equals(str) ? "Energía: el núcleo sin carbón, generadores parados." : ("ru".equals(str) ? "Энергия: в ядре нет угля, генераторы стоят." : "Power: core is out of coal, generators idle.")), 5.0F);
          } 
          return;
        } 
      } 
    } catch (Throwable throwable) {
      Log.err("[Lunarians] EnergyWatch", throwable);
    } 
  }
}
