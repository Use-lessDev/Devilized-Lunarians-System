package lunarians.hub;

import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import arc.util.Timer;
import arc.util.serialization.Jval;
import java.lang.reflect.Field;
import java.util.Iterator;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.ui.Styles;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/hub/LunariansHub.class */
public class LunariansHub {
    private static Timer.Task task;
    private static Field betaField;
    private static Field panelField;
    private static final String BTN = "lunarians-hub-chat";
    private static boolean started = false;
    private static boolean blockedShown = false;
    private static int lastReq = 0;
    private static int lastChatN = 0;
    private static boolean betaFailed = false;

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.LunariansHub$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            LunariansHub.lambda$startLoop$2();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.LunariansHub$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass1 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            Access.showConsent(null);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.LunariansHub$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass2 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            LunariansHub.lambda$hookBetaHud$3();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.LunariansHub$3, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass3 implements Cons {
        public final void get(Object obj) {
            LunariansHub.lambda$startLoop$1((Jval) obj);
        }
    }

    public static void init() {
        if (started) {
            return;
        }
        started = true;
        if (Vars.headless) {
            return;
        }
        if (!HubClient.decided()) {
            Core.app.post(() -> {
                Access.showConsent(null);
            });
        }
        startLoop();
        hookBetaHud();
        Skins.init();
        Access.initAll();
        try {
            Vars.ui.menufrag.addButton(HubText.get("hub"), Icon.players, HubUi::open);
        } catch (Throwable th) {
        }
    }

    private static void startLoop() {
        if (task != null) {
            task.cancel();
        }
        task = Timer.schedule(() -> {
            if (!HubClient.consented()) {
                return;
            }
            if (HubClient.myId().isEmpty()) {
                HubClient.ensureRegistered(null);
                return;
            }
            HubClient.heartbeat(jval -> {
                Iterator it = jval.get("global").asArray().iterator();
                while (it.hasNext()) {
                    Jval jval = (Jval) it.next();
                    HubClient.setLastGlobal(Math.max(HubClient.lastGlobal(), jval.getInt("id", 0)));
                    GlobalBanner.show(jval.getString("text", ""));
                }
            });
            if (HubClient.blocked && !blockedShown) {
                blockedShown = true;
                HubUi.showBlocked();
            }
            if (!HubClient.blocked) {
                blockedShown = false;
            }
            if (HubClient.pendingRequests > lastReq) {
                Vars.ui.showInfoToast(HubText.get("newreq"), 3.0f);
            }
            if (HubClient.newChat > lastChatN) {
                Vars.ui.showInfoToast(HubText.get("newchat"), 3.0f);
            }
            lastReq = HubClient.pendingRequests;
            lastChatN = HubClient.newChat;
        }, 5.0f, 45.0f);
    }

    private static void hookBetaHud() {
        Events.run(EventType.Trigger.update, () -> {
            if (betaFailed || Core.graphics.getFrameId() % 30 != 0) {
                return;
            }
            try {
                if (betaField == null) {
                    betaField = Class.forName("lunarians.LunariansMod").getField("betaHud");
                }
                Object obj = betaField.get(null);
                if (obj == null) {
                    return;
                }
                if (panelField == null) {
                    panelField = obj.getClass().getDeclaredField("panel");
                    panelField.setAccessible(true);
                }
                Table table = (Table) panelField.get(obj);
                if (table == null) {
                    return;
                }
                String str = HubText.get("beta.chat") + ((!HubClient.consented() || HubClient.newChat <= 0) ? "" : " (" + HubClient.newChat + ")");
                TextButton find = table.find(BTN);
                if (find instanceof TextButton) {
                    find.setText(str);
                    return;
                }
                TextButton textButton = new TextButton(str, Styles.defaultt);
                textButton.name = BTN;
                textButton.clicked(HubUi::openChats);
                table.row();
                table.add(textButton).growX().height(44.0f).pad(2.0f);
            } catch (Throwable th) {
                betaFailed = true;
                Log.warn("[Lunarians Hub] Beta UI hook off: @", new Object[]{th.toString()});
            }
        });
    }

    public static void stop() {
        if (task != null) {
            task.cancel();
        }
    }
}
