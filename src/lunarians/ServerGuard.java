package lunarians;

import arc.Core;
import arc.Events;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.EventType;

public class ServerGuard {
  private static boolean registered;
  
  private static boolean forced;
  
  private static float lastCheck;
  
  public static void init() {
    if (registered)
      return; 
    registered = true;
    Events.on(EventType.ClientLoadEvent.class, paramClientLoadEvent -> check());
    Events.on(EventType.WorldLoadEvent.class, paramWorldLoadEvent -> check());
    Events.run(EventType.Trigger.update, () -> {
          if (Vars.state != null && Vars.state.isGame() && TimeOk())
            checkLight(); 
        });
  }
  
  private static boolean TimeOk() {
    float f = Time.time;
    if (f - lastCheck < 180.0F)
      return false; 
    lastCheck = f;
    return true;
  }
  
  private static void checkLight() {
    try {
      if (isRestrictedServer() && !LunariansSettings.pvpMode) {
        LunariansSettings.pvpMode = true;
        LunariansSettings.save();
        forced = true;
        if (Vars.ui != null)
          Vars.ui.showInfoToast(
              "pt".equals(LunariansSettings.language) ? 
              "Servidor restrito → Balanced ON" : 
              "Restricted server → Balanced ON", 4.0F); 
      } 
    } catch (Throwable throwable) {}
  }
  
  public static void check() {
    try {
      if (isRestrictedServer() && 
        !LunariansSettings.pvpMode) {
        LunariansSettings.pvpMode = true;
        LunariansSettings.save();
        forced = true;
        Log.info("[Lunarians] Restricted server detected → Balanced mode forced");
        if (Vars.ui != null)
          Vars.ui.showInfoToast(
              "pt".equals(LunariansSettings.language) ? 
              "Servidor tipo Fish → modo Balanced" : 
              "Fish-like server → Balanced mode", 4.0F); 
      } 
    } catch (Throwable throwable) {
      Log.err("[Lunarians] ServerGuard", throwable);
    } 
  }
  
  public static boolean isRestrictedServer() {
    if (Vars.net == null || !Vars.net.client())
      return false; 
    try {
      String str1 = "";
      try {
        if (Vars.ui == null || Vars.ui.join != null);
      } catch (Throwable throwable) {}
      try {
        Object object = null;
        if (Vars.platform != null);
      } catch (Throwable throwable) {}
      String str2 = Core.settings.getString("last-server-ip", "");
      String str3 = Core.settings.getString("last-server-name", "");
      String str4 = (str2 + " " + str2).toLowerCase();
      if (str4.contains("fish") || str4.contains("xpdustry") || str4
        .contains("darkdustry") || str4.contains("mindustry.ddns"))
        return true; 
      try {
        String str = "";
        if (Vars.player == null || Vars.player.name != null);
      } catch (Throwable throwable) {}
      try {
        if (Vars.net != null) {
          String str = String.valueOf(Vars.net);
          if (str.toLowerCase().contains("fish"))
            return true; 
        } 
      } catch (Throwable throwable) {}
      try {
        if (Vars.state != null && Vars.state.rules != null) {
          String str = (Vars.state.rules.modeName != null) ? Vars.state.rules.modeName : "";
          if (str.toLowerCase().contains("fish") || str.toLowerCase().contains("pvp"))
            if (Vars.net.client())
              return (str4.contains("fish") || str.toLowerCase().contains("fish"));  
        } 
      } catch (Throwable throwable) {}
      return str4.contains("fish");
    } catch (Throwable throwable) {
      return false;
    } 
  }
}
