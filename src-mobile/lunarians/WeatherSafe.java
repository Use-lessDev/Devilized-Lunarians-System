package lunarians;

import arc.Events;
import mindustry.Vars;
import mindustry.entities.EntityGroup;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.gen.WeatherState;
import mindustry.type.Weather;

public class WeatherSafe {
  private static boolean registered;
  
  private static float timer;
  
  public static void init() {
    boolean bool = registered;
    if (!bool) {
      bool = Vars.headless;
      if (!bool) {
        bool = true;
        registered = bool;
        EventType.Trigger trigger = EventType.Trigger.update;
        WeatherSafe$0 weatherSafe$0 = new WeatherSafe$0();
        this();
        Events.run(trigger, weatherSafe$0);
      } 
    } 
  }
  
  private static void tick() {
    float f1 = timer + 1.0F;
    timer = f1;
    float f2 = 120.0F;
    int i = f1 cmp f2;
    if (i >= 0) {
      i = 0;
      f1 = 0.0F;
      EntityGroup entityGroup = null;
      timer = 0.0F;
      try {
        entityGroup = Groups.weather;
        if (entityGroup != null) {
          entityGroup = Groups.weather;
          WeatherSafe$1 weatherSafe$1 = new WeatherSafe$1();
          this();
          entityGroup.each(weatherSafe$1);
        } 
      } finally {}
    } 
  }
}
