/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.util.Log
 */
package lunarians;

import arc.Core;
import arc.util.Log;

public class FirstRun {
    private static final String DONE = "lunarians-first-run-done";

    public static void run() {
        try {
            String[] stringArray;
            if (Core.settings.getBool(DONE, false)) {
                return;
            }
            boolean bl = false;
            String[] stringArray2 = Core.settings.keys().iterator();
            while (stringArray2.hasNext()) {
                stringArray = (String[])stringArray2.next();
                if (!stringArray.startsWith("lunarians") || stringArray.equals(DONE)) continue;
                bl = true;
                break;
            }
            Core.settings.put(DONE, (Object)true);
            if (bl) {
                return;
            }
            stringArray = stringArray2 = new String[]{"lunarians-auto-attack", "lunarians-auto-unit", "lunarians-auto-unit-heal", "lunarians-auto-moon", "lunarians-auto-conveyor", "lunarians-auto-drill", "lunarians-auto-drill-conveyor", "lunarians-drill-perfect", "lunarians-dock-auto", "lunarians-econ-order", "lunarians-auto-cargo", "lunarians-auto-container", "lunarians-auto-defence", "lunarians-auto-shield", "lunarians-auto-heal", "lunarians-autofill", "lunarians-freecam", "lunarians-camfollow", "lunarians-prod-alert", "lunarians-loss-warn", "lunarians-enemy-core-alert", "lunarians-auto-delete", "lunarians-presence", "lunarians-chat-on"};
            int n = stringArray.length;
            for (int i = 0; i < n; ++i) {
                String string = stringArray[i];
                Core.settings.put(string, (Object)false);
            }
            for (String string : stringArray = new String[]{"lunarians-as-fac-on-graphite", "lunarians-as-fac-on-silicon", "lunarians-as-fac-on-metaglass", "lunarians-as-fac-drill", "lunarians-as-steam", "lunarians-as-power", "lunarians-as-bignode"}) {
                Core.settings.put(string, (Object)0);
            }
            Core.settings.put("lunarians-form-mode", (Object)0);
            Core.settings.put("lunarians-form-reform", (Object)false);
            Log.info((Object)"[Lunarians] primeiro uso: todos os recursos automaticos iniciam desligados.");
        }
        catch (Throwable throwable) {
            Log.err((String)"[Lunarians] FirstRun", (Throwable)throwable);
        }
    }
}

