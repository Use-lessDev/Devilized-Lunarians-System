package lunarians;

import arc.Core;
import arc.Events;
import arc.Settings;
import arc.util.Time;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.game.EventType;
import mindustry.gen.Player;

public class ConveyorUpgrade {
  private static float next;
  
  private static boolean registered;
  
  private static boolean running;
  
  public static boolean enabled() {
    return Core.settings.getBool("lunarians-ti-conv", true);
  }
  
  public static void init() {
    boolean bool = registered;
    if (bool)
      return; 
    registered = true;
    EventType.Trigger trigger = EventType.Trigger.update;
    ConveyorUpgrade$0 conveyorUpgrade$0 = new ConveyorUpgrade$0();
    this();
    Events.run(trigger, conveyorUpgrade$0);
    ConveyorUpgrade$2 conveyorUpgrade$2 = new ConveyorUpgrade$2();
    this();
    Events.on(EventType.WorldLoadEvent.class, conveyorUpgrade$2);
  }
  
  public static void setEnabled(boolean paramBoolean) {
    Settings settings = Core.settings;
    String str = "lunarians-ti-conv";
    Boolean bool = Boolean.valueOf(paramBoolean);
    settings.put(str, bool);
    if (!paramBoolean) {
      paramBoolean = false;
      running = false;
    } 
  }
  
  public static void setThreshold(int paramInt) {
    Settings settings = Core.settings;
    paramInt = Math.min(10000, paramInt);
    Integer integer = Integer.valueOf(Math.max(200, paramInt));
    settings.put("lunarians-ti-conv-min", integer);
  }
  
  public static int threshold() {
    int i = Core.settings.getInt("lunarians-ti-conv-min", 2000);
    return Math.max(100, i);
  }
  
  public static String title() {
    String str1 = LunariansI18n.getLanguage();
    String str2 = "pt";
    boolean bool1 = str2.equals(str1);
    if (bool1)
      return "Esteira de titanio automatica (2k)"; 
    str2 = "es";
    bool1 = str2.equals(str1);
    if (bool1)
      return "Cinta de titanio automatica (2k)"; 
    str2 = "ru";
    boolean bool2 = str2.equals(str1);
    return bool2 ? "Авто-титановый конвейер (2k)" : "Auto titanium conveyor (2k)";
  }
  
  private static void update() {
    boolean bool = enabled();
    if (bool) {
      GameState gameState = Vars.state;
      if (gameState != null) {
        gameState = Vars.state;
        bool = gameState.isGame();
        if (bool) {
          Player player = Vars.player;
          if (player != null) {
            float f1 = Time.time;
            float f2 = next;
            int i = f1 cmp f2;
            if (i >= 0) {
              f1 = Time.time;
              int j = 1127481344;
              f2 = 180.0F;
              f1 += f2;
              next = f1;
              try {
                player = Vars.player;
              } finally {
                player = null;
                String str = "[Lunarians] ConveyorUpgrade";
              } 
            } 
          } 
        } 
      } 
    } 
  }
}
