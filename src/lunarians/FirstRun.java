package lunarians;

import arc.Core;
import arc.util.Log;

public class FirstRun {
  private static final String DONE = "lunarians-first-run-done";
  
  public static void run() {
    try {
      if (Core.settings.getBool("lunarians-first-run-done", false))
        return; 
      boolean bool = false;
      for (String str : Core.settings.keys()) {
        if (str.startsWith("lunarians") && !str.equals("lunarians-first-run-done")) {
          bool = true;
          break;
        } 
      } 
      Core.settings.put("lunarians-first-run-done", Boolean.valueOf(true));
      if (bool)
        return; 
      String[] arrayOfString1 = { 
          "lunarians-auto-attack", "lunarians-auto-unit", "lunarians-auto-unit-heal", "lunarians-auto-moon", "lunarians-auto-conveyor", "lunarians-auto-drill", "lunarians-auto-drill-conveyor", "lunarians-drill-perfect", "lunarians-dock-auto", "lunarians-econ-order", 
          "lunarians-auto-cargo", "lunarians-auto-container", "lunarians-auto-defence", "lunarians-auto-shield", "lunarians-auto-heal", "lunarians-autofill", "lunarians-freecam", "lunarians-camfollow", "lunarians-prod-alert", "lunarians-loss-warn", 
          "lunarians-enemy-core-alert", "lunarians-auto-delete", "lunarians-presence", "lunarians-chat-on" };
      for (String str : arrayOfString1)
        Core.settings.put(str, Boolean.valueOf(false)); 
      String[] arrayOfString2 = { "lunarians-as-fac-on-graphite", "lunarians-as-fac-on-silicon", "lunarians-as-fac-on-metaglass", "lunarians-as-fac-drill", "lunarians-as-steam", "lunarians-as-power", "lunarians-as-bignode" };
      for (String str : arrayOfString2)
        Core.settings.put(str, Integer.valueOf(0)); 
      Core.settings.put("lunarians-form-mode", Integer.valueOf(0));
      Core.settings.put("lunarians-form-reform", Boolean.valueOf(false));
      Log.info("[Lunarians] primeiro uso: todos os recursos automaticos iniciam desligados.");
    } catch (Throwable throwable) {
      Log.err("[Lunarians] FirstRun", throwable);
    } 
  }
}
