/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.Events
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.game.EventType$ClientLoadEvent
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.EventType$WorldLoadEvent
 */
package lunarians;

import arc.Core;
import arc.Events;
import arc.util.Log;
import arc.util.Time;
import lunarians.LunariansSettings;
import mindustry.Vars;
import mindustry.game.EventType;

public class ServerGuard {
    private static boolean registered;
    private static boolean forced;
    private static float lastCheck;

    public static void init() {
        if (registered) {
            return;
        }
        registered = true;
        Events.on(EventType.ClientLoadEvent.class, clientLoadEvent -> ServerGuard.check());
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> ServerGuard.check());
        Events.run((Object)EventType.Trigger.update, () -> {
            if (Vars.state != null && Vars.state.isGame() && ServerGuard.TimeOk()) {
                ServerGuard.checkLight();
            }
        });
    }

    private static boolean TimeOk() {
        float f = Time.time;
        if (f - lastCheck < 180.0f) {
            return false;
        }
        lastCheck = f;
        return true;
    }

    private static void checkLight() {
        try {
            if (ServerGuard.isRestrictedServer() && !LunariansSettings.pvpMode) {
                LunariansSettings.pvpMode = true;
                LunariansSettings.save();
                forced = true;
                if (Vars.ui != null) {
                    Vars.ui.showInfoToast("pt".equals(LunariansSettings.language) ? "Servidor restrito \u2192 Balanced ON" : "Restricted server \u2192 Balanced ON", 4.0f);
                }
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static void check() {
        try {
            if (ServerGuard.isRestrictedServer() && !LunariansSettings.pvpMode) {
                LunariansSettings.pvpMode = true;
                LunariansSettings.save();
                forced = true;
                Log.info((Object)"[Lunarians] Restricted server detected \u2192 Balanced mode forced");
                if (Vars.ui != null) {
                    Vars.ui.showInfoToast("pt".equals(LunariansSettings.language) ? "Servidor tipo Fish \u2192 modo Balanced" : "Fish-like server \u2192 Balanced mode", 4.0f);
                }
            }
        }
        catch (Throwable throwable) {
            Log.err((String)"[Lunarians] ServerGuard", (Throwable)throwable);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static boolean isRestrictedServer() {
        if (Vars.net == null) return false;
        if (!Vars.net.client()) {
            return false;
        }
        try {
            String string;
            String string2;
            String string3 = "";
            try {
                if (Vars.ui != null && Vars.ui.join == null) {
                    // empty if block
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            try {
                string2 = null;
                if (Vars.platform != null) {
                    // empty if block
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            string2 = Core.settings.getString("last-server-ip", "");
            String string4 = Core.settings.getString("last-server-name", "");
            String string5 = (string2 + " " + string4).toLowerCase();
            if (string5.contains("fish")) return true;
            if (string5.contains("xpdustry")) return true;
            if (string5.contains("darkdustry")) return true;
            if (string5.contains("mindustry.ddns")) {
                return true;
            }
            try {
                string = "";
                if (Vars.player != null && Vars.player.name == null) {
                    // empty if block
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            try {
                if (Vars.net != null && (string = String.valueOf(Vars.net)).toLowerCase().contains("fish")) {
                    return true;
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            try {
                if (Vars.state == null) return string5.contains("fish");
                if (Vars.state.rules == null) return string5.contains("fish");
                String string6 = string = Vars.state.rules.modeName != null ? Vars.state.rules.modeName : "";
                if (!string.toLowerCase().contains("fish")) {
                    if (!string.toLowerCase().contains("pvp")) return string5.contains("fish");
                }
                if (!Vars.net.client()) return string5.contains("fish");
                if (string5.contains("fish")) return true;
                if (string.toLowerCase().contains("fish")) return true;
                return false;
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            return string5.contains("fish");
        }
        catch (Throwable throwable) {
            return false;
        }
    }
}

