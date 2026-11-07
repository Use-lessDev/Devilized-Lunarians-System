package lunarians;

import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.core.UI;
import mindustry.game.EventType;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ServerGuard.class */
public class ServerGuard {
    private static boolean registered;
    private static boolean forced;
    private static float lastCheck;

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ServerGuard$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Cons {
        public final void get(Object obj) {
            ServerGuard.check();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ServerGuard$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass1 implements Cons {
        public final void get(Object obj) {
            ServerGuard.check();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ServerGuard$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass2 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            ServerGuard.lambda$init$2();
        }
    }

    public static void init() {
        if (registered) {
            return;
        }
        registered = true;
        Events.on(EventType.ClientLoadEvent.class, clientLoadEvent -> {
            check();
        });
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            check();
        });
        Events.run(EventType.Trigger.update, () -> {
            if (Vars.state == null || !Vars.state.isGame() || !TimeOk()) {
                return;
            }
            checkLight();
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
        String str;
        try {
            if (isRestrictedServer() && !LunariansSettings.pvpMode) {
                LunariansSettings.pvpMode = true;
                LunariansSettings.save();
                forced = true;
                if (Vars.ui != null) {
                    UI ui = Vars.ui;
                    if ("pt".equals(LunariansSettings.language)) {
                        str = "Servidor restrito → Balanced ON";
                    } else {
                        str = "Restricted server → Balanced ON";
                    }
                    ui.showInfoToast(str, 4.0f);
                }
            }
        } catch (Throwable th) {
        }
    }

    public static void check() {
        String str;
        try {
            if (isRestrictedServer() && !LunariansSettings.pvpMode) {
                LunariansSettings.pvpMode = true;
                LunariansSettings.save();
                forced = true;
                Log.info("[Lunarians] Restricted server detected → Balanced mode forced");
                if (Vars.ui != null) {
                    UI ui = Vars.ui;
                    if ("pt".equals(LunariansSettings.language)) {
                        str = "Servidor tipo Fish → modo Balanced";
                    } else {
                        str = "Fish-like server → Balanced mode";
                    }
                    ui.showInfoToast(str, 4.0f);
                }
            }
        } catch (Throwable th) {
            Log.err("[Lunarians] ServerGuard", th);
        }
    }

    public static boolean isRestrictedServer() {
        if (Vars.net == null || !Vars.net.client()) {
            return false;
        }
        try {
            try {
                if (Vars.ui != null) {
                    if (Vars.ui.join != null) {
                    }
                }
            } catch (Throwable th) {
                return false;
            }
        } catch (Throwable th2) {
        }
        try {
            if (Vars.platform != null) {
            }
        } catch (Throwable th3) {
        }
        String lowerCase = (Core.settings.getString("last-server-ip", "") + " " + Core.settings.getString("last-server-name", "")).toLowerCase();
        if (lowerCase.contains("fish") || lowerCase.contains("xpdustry") || lowerCase.contains("darkdustry") || lowerCase.contains("mindustry.ddns")) {
            return true;
        }
        try {
            if (Vars.player != null) {
                if (Vars.player.name != null) {
                }
            }
        } catch (Throwable th4) {
        }
        try {
            if (Vars.net != null) {
                if (String.valueOf(Vars.net).toLowerCase().contains("fish")) {
                    return true;
                }
            }
        } catch (Throwable th5) {
        }
        try {
            if (Vars.state != null && Vars.state.rules != null) {
                String str = Vars.state.rules.modeName != null ? Vars.state.rules.modeName : "";
                if ((str.toLowerCase().contains("fish") || str.toLowerCase().contains("pvp")) && Vars.net.client()) {
                    if (!lowerCase.contains("fish")) {
                        if (!str.toLowerCase().contains("fish")) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        } catch (Throwable th6) {
        }
        return lowerCase.contains("fish");
    }
}
