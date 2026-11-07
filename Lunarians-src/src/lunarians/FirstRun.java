package lunarians;

import arc.Core;
import arc.util.Log;
import java.util.Iterator;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/FirstRun.class */
public class FirstRun {
    private static final String DONE = "lunarians-first-run-done";

    public static void run() {
        try {
            if (Core.settings.getBool(DONE, false)) {
                return;
            }
            boolean z = false;
            Iterator it = Core.settings.keys().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String str = (String) it.next();
                if (str.startsWith("lunarians") && !str.equals(DONE)) {
                    z = true;
                    break;
                }
            }
            Core.settings.put(DONE, true);
            if (z) {
                return;
            }
            for (String str2 : new String[]{"lunarians-auto-attack", "lunarians-auto-unit", "lunarians-auto-unit-heal", "lunarians-auto-moon", "lunarians-auto-conveyor", "lunarians-auto-drill", "lunarians-auto-drill-conveyor", "lunarians-drill-perfect", "lunarians-dock-auto", AutoEconomy.KEY, "lunarians-auto-cargo", "lunarians-auto-container", "lunarians-auto-defence", "lunarians-auto-shield", "lunarians-auto-heal", "lunarians-autofill", "lunarians-freecam", "lunarians-camfollow", "lunarians-prod-alert", "lunarians-loss-warn", "lunarians-enemy-core-alert", "lunarians-auto-delete", LunariansPresence.KEY, ChatCommands.KEY_ON}) {
                Core.settings.put(str2, false);
            }
            for (String str3 : new String[]{"lunarians-as-fac-on-graphite", "lunarians-as-fac-on-silicon", "lunarians-as-fac-on-metaglass", "lunarians-as-fac-drill", "lunarians-as-steam", "lunarians-as-power", "lunarians-as-bignode"}) {
                Core.settings.put(str3, 0);
            }
            Core.settings.put("lunarians-form-mode", 0);
            Core.settings.put("lunarians-form-reform", false);
            Log.info("[Lunarians] primeiro uso: todos os recursos automaticos iniciam desligados.");
        } catch (Throwable th) {
            Log.err("[Lunarians] FirstRun", th);
        }
    }
}
