package lunarians;

import arc.Events;
import arc.util.Time;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.game.EventType;
import mindustry.gen.Player;

public class EnergyWatch {
  private static float lastWarn = -99999.0F;
  
  private static float next;
  
  private static boolean registered;
  
  public static void init() {
    boolean bool = registered;
    if (bool)
      return; 
    registered = true;
    EventType.Trigger trigger = EventType.Trigger.update;
    EnergyWatch$0 energyWatch$0 = new EnergyWatch$0();
    this();
    Events.run(trigger, energyWatch$0);
  }
  
  private static void update() {
    GameState gameState = Vars.state;
    if (gameState != null) {
      gameState = Vars.state;
      boolean bool = gameState.isGame();
      if (bool) {
        Player player = Vars.player;
        if (player != null) {
          float f1 = Time.time;
          float f2 = next;
          int i = f1 cmp f2;
          if (i >= 0) {
            f1 = Time.time;
            int j = 1142292480;
            f2 = 600.0F;
            f1 += f2;
            next = f1;
            try {
              player = Vars.player;
            } finally {
              player = null;
              String str = "[Lunarians] EnergyWatch";
            } 
          } 
        } 
      } 
    } 
  }
}
