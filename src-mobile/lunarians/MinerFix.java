package lunarians;

import arc.Events;
import arc.util.Log;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.content.UnitTypes;
import mindustry.core.GameState;
import mindustry.game.EventType;
import mindustry.game.Rules;
import mindustry.type.Item;
import mindustry.type.UnitType;

public class MinerFix {
  private static Boolean savedAutoAttack;
  
  private static void allow(UnitType paramUnitType, Item... paramVarArgs) {
    if (paramUnitType != null)
      try {
        return;
      } finally {
        paramVarArgs = null;
        String str = paramUnitType.name;
        StringBuilder stringBuilder = new StringBuilder();
        this("[Lunarians] MinerFix falhou em ");
        stringBuilder.append(str);
        Log.err(stringBuilder.toString(), (Throwable)paramVarArgs);
      }  
  }
  
  public static void apply() {
    UnitType unitType = UnitTypes.mono;
    boolean bool = true;
    Item[] arrayOfItem1 = new Item[bool];
    Item item1 = Items.sand;
    arrayOfItem1[0] = item1;
    allow(unitType, arrayOfItem1);
    unitType = UnitTypes.poly;
    byte b = 2;
    Item[] arrayOfItem2 = new Item[b];
    Item item2 = Items.sand;
    arrayOfItem2[0] = item2;
    item2 = Items.coal;
    arrayOfItem2[bool] = item2;
    allow(unitType, arrayOfItem2);
    unitType = UnitTypes.mega;
    arrayOfItem2 = new Item[3];
    item2 = Items.sand;
    arrayOfItem2[0] = item2;
    Item item3 = Items.coal;
    arrayOfItem2[bool] = item3;
    Item item4 = Items.titanium;
    arrayOfItem2[b] = item4;
    allow(unitType, arrayOfItem2);
  }
  
  public static void init() {
    apply();
    MinerFix$0 minerFix$0 = new MinerFix$0();
    this();
    Events.on(EventType.WorldLoadEvent.class, minerFix$0);
    MinerFix$1 minerFix$1 = new MinerFix$1();
    this();
    Events.on(EventType.ResetEvent.class, minerFix$1);
  }
  
  private static void pvpGuard() {
    try {
      GameState gameState = Vars.state;
      if (gameState != null) {
        gameState = Vars.state;
        Rules rules = gameState.rules;
        if (rules != null) {
          GameState gameState1 = Vars.state;
          Rules rules1 = gameState1.rules;
          boolean bool1 = rules1.pvp;
          if (bool1) {
            Boolean bool2 = savedAutoAttack;
            if (bool2 == null) {
              bool1 = LunariansSettings.enableAutoAttack;
              bool2 = Boolean.valueOf(bool1);
              savedAutoAttack = bool2;
            } 
            bool1 = false;
            return;
          } 
        } 
      } 
      return;
    } finally {
      Exception exception = null;
      Log.err("[Lunarians] pvpGuard falhou", exception);
    } 
  }
  
  private static void restoreAutoAttack() {
    Boolean bool = savedAutoAttack;
    if (bool != null) {
      LunariansSettings.enableAutoAttack = bool.booleanValue();
      bool = null;
      savedAutoAttack = null;
    } 
  }
}
