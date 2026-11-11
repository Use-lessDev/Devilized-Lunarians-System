/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.Events
 *  arc.func.Cons
 *  arc.scene.Element
 *  arc.scene.style.Drawable
 *  arc.scene.ui.TextButton
 *  arc.scene.ui.layout.Table
 *  arc.util.Log
 *  arc.util.Timer
 *  arc.util.Timer$Task
 *  arc.util.serialization.Jval
 *  mindustry.Vars
 *  mindustry.game.EventType$Trigger
 *  mindustry.gen.Icon
 *  mindustry.ui.Styles
 */
package lunarians.hub;

import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import arc.util.Timer;
import arc.util.serialization.Jval;
import java.lang.reflect.Field;
import lunarians.hub.Access;
import lunarians.hub.GlobalBanner;
import lunarians.hub.HubClient;
import lunarians.hub.HubText;
import lunarians.hub.HubUi;
import lunarians.hub.Skins;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.ui.Styles;

public class LunariansHub {
    private static Timer.Task task;
    private static Field betaField;
    private static Field panelField;
    private static final String BTN = "lunarians-hub-chat";
    private static boolean started;
    private static boolean blockedShown;
    private static int lastReq;
    private static int lastChatN;
    private static boolean betaFailed;

    public static void init() {
        if (started) {
            return;
        }
        started = true;
        if (Vars.headless) {
            return;
        }
        if (!HubClient.decided()) {
            Core.app.post(() -> Access.showConsent(null));
        }
        LunariansHub.startLoop();
        LunariansHub.hookBetaHud();
        Skins.init();
        Access.initAll();
        try {
            Vars.ui.menufrag.addButton(HubText.get("hub"), (Drawable)Icon.players, HubUi::open);
        }
        catch (Throwable throwable) {
            // empty catch block
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
            HubClient.heartbeat((Cons<Jval>)((Cons)jval -> {
                for (Jval jval2 : jval.get("global").asArray()) {
                    HubClient.setLastGlobal(Math.max(HubClient.lastGlobal(), jval2.getInt("id", 0)));
                    GlobalBanner.show(jval2.getString("text", ""));
                }
            }));
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
        }, (float)5.0f, (float)45.0f);
    }

    private static void hookBetaHud() {
        Events.run((Object)EventType.Trigger.update, () -> {
            if (betaFailed || Core.graphics.getFrameId() % 30L != 0L) {
                return;
            }
            try {
                Table table;
                Object object;
                if (betaField == null) {
                    betaField = Class.forName("lunarians.LunariansMod").getField("betaHud");
                }
                if ((object = betaField.get(null)) == null) {
                    return;
                }
                if (panelField == null) {
                    panelField = object.getClass().getDeclaredField("panel");
                    panelField.setAccessible(true);
                }
                if ((table = (Table)panelField.get(object)) == null) {
                    return;
                }
                String string = HubText.get("beta.chat") + (!HubClient.consented() || HubClient.newChat <= 0 ? "" : " (" + HubClient.newChat + ")");
                TextButton textButton = (TextButton)table.find(BTN);
                if (textButton instanceof TextButton) {
                    textButton.setText(string);
                    return;
                }
                TextButton textButton2 = new TextButton(string, Styles.defaultt);
                textButton2.name = BTN;
                textButton2.clicked(HubUi::openChats);
                table.row();
                table.add((Element)textButton2).growX().height(44.0f).pad(2.0f);
            }
            catch (Throwable throwable) {
                betaFailed = true;
                Log.warn((String)"[Lunarians Hub] Beta UI hook off: @", (Object[])new Object[]{throwable.toString()});
            }
        });
    }

    public static void stop() {
        if (task != null) {
            task.cancel();
        }
    }

    static {
        started = false;
        blockedShown = false;
        lastReq = 0;
        lastChatN = 0;
        betaFailed = false;
    }
}

