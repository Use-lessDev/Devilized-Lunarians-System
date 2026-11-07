package lunarians;

import arc.Events;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.gen.WeatherState;

public class WeatherSafe {
  private static boolean registered;
  
  private static float timer;
  
  public static void init() {
    if (registered || Vars.headless)
      return; 
    registered = true;
    Events.run(EventType.Trigger.update, WeatherSafe::tick);
  }
  
  private static void tick() {
    timer++;
    if (timer < 120.0F)
      return; 
    timer = 0.0F;
    try {
      if (Groups.weather == null)
        return; 
      Groups.weather.each(paramWeatherState -> {
            try {
              if (paramWeatherState == null)
                return; 
              if (paramWeatherState.weather == null)
                paramWeatherState.remove(); 
            } catch (Throwable throwable) {}
          });
    } catch (Throwable throwable) {}
  }
}
