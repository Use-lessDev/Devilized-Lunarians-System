package lunarians;

import arc.Events;
import arc.util.Log;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.content.UnitTypes;
import mindustry.game.EventType;
import mindustry.type.Item;
import mindustry.type.UnitType;

public class MinerFix {
  private static Boolean savedAutoAttack = null;
  
  public static void init() {
    apply();
    Events.on(EventType.WorldLoadEvent.class, paramWorldLoadEvent -> {
          apply();
          pvpGuard();
        });
    Events.on(EventType.ResetEvent.class, paramResetEvent -> restoreAutoAttack());
  }
  
  private static void restoreAutoAttack() {
    if (savedAutoAttack != null) {
      LunariansSettings.enableAutoAttack = savedAutoAttack.booleanValue();
      savedAutoAttack = null;
    } 
  }
  
  private static void pvpGuard() {
    try {
      boolean bool = (Vars.state != null && Vars.state.rules != null && Vars.state.rules.pvp) ? true : false;
      if (bool) {
        if (savedAutoAttack == null)
          savedAutoAttack = Boolean.valueOf(LunariansSettings.enableAutoAttack); 
        LunariansSettings.enableAutoAttack = false;
      } else if (savedAutoAttack != null) {
        LunariansSettings.enableAutoAttack = savedAutoAttack.booleanValue();
        savedAutoAttack = null;
      } 
    } catch (Throwable throwable) {
      Log.err("[Lunarians] pvpGuard falhou", throwable);
    } 
  }
  
  public static void apply() {
    allow(UnitTypes.mono, new Item[] { Items.sand });
    allow(UnitTypes.poly, new Item[] { Items.sand, Items.coal });
    allow(UnitTypes.mega, new Item[] { Items.sand, Items.coal, Items.titanium });
  }
  
  private static void allow(UnitType paramUnitType, Item... paramVarArgs) {
    if (paramUnitType == null)
      return; 
    try {
      if (paramUnitType.mineItems == null)
        return; 
      for (Item item : paramVarArgs) {
        if (item.hardness <= paramUnitType.mineTier && !paramUnitType.mineItems.contains(item))
          paramUnitType.mineItems.add(item); 
      } 
    } catch (Throwable throwable) {
      Log.err("[Lunarians] MinerFix falhou em " + paramUnitType.name, throwable);
    } 
  }
}
