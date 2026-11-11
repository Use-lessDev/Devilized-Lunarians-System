/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.Events
 *  arc.func.Boolp
 *  arc.func.Cons
 *  arc.scene.Element
 *  arc.scene.style.Drawable
 *  arc.scene.ui.Label
 *  arc.scene.ui.TextArea
 *  arc.struct.ObjectMap
 *  arc.util.Log
 *  arc.util.Timer
 *  arc.util.serialization.Jval
 *  mindustry.Vars
 *  mindustry.game.EventType$Trigger
 *  mindustry.gen.Icon
 *  mindustry.ui.dialogs.BaseDialog
 */
package lunarians.hub;

import arc.Core;
import arc.Events;
import arc.func.Boolp;
import arc.func.Cons;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.ui.Label;
import arc.scene.ui.TextArea;
import arc.struct.ObjectMap;
import arc.util.Log;
import arc.util.Timer;
import arc.util.serialization.Jval;
import java.lang.reflect.Field;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import lunarians.UiStyle;
import lunarians.hub.HubClient;
import lunarians.hub.HubText;
import lunarians.hub.Plus;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.ui.dialogs.BaseDialog;

public class Access {
    private static final String K_STATUS = "lunarians-access";
    private static boolean inited;
    private static boolean askedThisSession;
    private static boolean fetchedOnce;
    private static boolean warned;
    private static Field betaField;
    private static Boolp wrapper;
    private static Boolean schemForced;
    private static Boolean schemOrig;
    private static final ObjectMap<String, String[]> T;

    public static String status() {
        return Core.settings.getString(K_STATUS, "none");
    }

    public static boolean approved() {
        return HubClient.consented() && "approved".equals(Access.status());
    }

    public static void initAll() {
        Plus.init();
        Access.init();
    }

    private static void init() {
        if (inited || Vars.headless) {
            return;
        }
        inited = true;
        Events.run((Object)EventType.Trigger.update, Access::gate);
        Timer.schedule(Access::poll, (float)4.0f, (float)30.0f);
        Timer.schedule(Access::enforce, (float)3.0f, (float)2.0f);
        try {
            Vars.ui.menufrag.addButton(Access.tr("access.btn"), (Drawable)Icon.lock, Access::menuClick);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static void gate() {
        block7: {
            if (Core.graphics.getFrameId() % 20L != 0L) {
                return;
            }
            try {
                Element element;
                if (betaField == null) {
                    betaField = Class.forName("lunarians.LunariansMod").getField("betaHud");
                }
                if ((element = (Element)betaField.get(null)) == null) {
                    return;
                }
                if (element.visibility != wrapper) {
                    Boolp boolp = element.visibility;
                    wrapper = () -> Access.approved() && (boolp == null || boolp.get());
                    element.visible(wrapper);
                }
                if (!Access.approved()) {
                    element.visible = false;
                }
            }
            catch (Throwable throwable) {
                if (warned) break block7;
                warned = true;
                Log.warn((String)"[Lunarians Hub] Beta UI gate: @", (Object[])new Object[]{throwable.toString()});
            }
        }
    }

    private static void poll() {
        if (!HubClient.consented()) {
            if (!"none".equals(Access.status())) {
                Core.settings.put(K_STATUS, (Object)"none");
            }
            Access.applyFeatures(null);
            return;
        }
        if (HubClient.myId().isEmpty() || HubClient.blocked) {
            return;
        }
        HubClient.call("GET", "/api/access/status", null, (Cons<Jval>)((Cons)jval -> {
            Access.apply(jval.getString("status", "none"), jval.getString("note", ""));
            Access.applyFeatures(jval.has("features") ? jval.get("features") : null);
        }), (Cons<String>)((Cons)string -> {}));
    }

    private static void apply(String string, String string2) {
        String string3 = Access.status();
        Core.settings.put(K_STATUS, (Object)string);
        if (fetchedOnce && !string.equals(string3)) {
            if (string.equals("approved")) {
                Vars.ui.showInfoToast(Access.tr("access.approved"), 5.0f);
            } else if (string.equals("rejected")) {
                Vars.ui.showInfoToast(Access.tr("access.rejected") + (string2.isEmpty() ? "" : ": " + string2), 6.0f);
            }
        }
        fetchedOnce = true;
        if (string.equals("none") && !askedThisSession) {
            askedThisSession = true;
            Access.build(false, null);
        }
    }

    private static void applyFeatures(Jval jval) {
        try {
            if (jval != null && jval.has("showSchematicPanel")) {
                boolean bl = jval.getBool("showSchematicPanel", true);
                if (schemForced == null) {
                    schemOrig = LunariansSettings.showSchematicPanel;
                }
                schemForced = bl;
                Access.setSchem(bl);
            } else if (schemForced != null) {
                if (schemOrig != null) {
                    Access.setSchem(schemOrig);
                }
                schemForced = null;
                schemOrig = null;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static void setSchem(boolean bl) {
        if (LunariansSettings.showSchematicPanel != bl) {
            LunariansSettings.showSchematicPanel = bl;
        }
    }

    private static void enforce() {
        if (schemForced != null) {
            Access.setSchem(schemForced);
        }
    }

    private static void menuClick() {
        if (!HubClient.consented()) {
            Access.showConsent(null);
            return;
        }
        String string = Access.status();
        if (string.equals("approved")) {
            Vars.ui.showInfoToast(Access.tr("access.ok"), 3.0f);
        } else if (string.equals("pending")) {
            Vars.ui.showInfoToast(Access.tr("access.waiting"), 3.0f);
        } else {
            Access.build(false, null);
        }
    }

    public static void showConsent(Runnable runnable) {
        Access.build(true, runnable);
    }

    private static Label wrap(String string) {
        Label label = new Label((CharSequence)string);
        label.setWrap(true);
        return label;
    }

    private static void build(boolean bl, Runnable runnable) {
        BaseDialog baseDialog = new BaseDialog(bl ? HubText.get("consent.title") : Access.tr("access.title"));
        UiStyle.register(baseDialog);
        float f = Math.min(Core.scene.getWidth() - 40.0f, 520.0f);
        TextArea textArea = new TextArea("");
        textArea.setMessageText(Access.tr("access.hint"));
        textArea.setMaxLength(300);
        baseDialog.cont.pane(table -> {
            if (bl) {
                table.add((Element)Access.wrap(HubText.get("consent.body"))).width(f).left().pad(8.0f).row();
            } else {
                table.add((Element)Access.wrap(Access.tr("access.intro"))).width(f).left().pad(8.0f).row();
            }
            table.add((Element)Access.wrap(Access.tr("access.why"))).width(f).left().padTop(8.0f).padLeft(8.0f).row();
            table.add((Element)textArea).width(f).height(120.0f).pad(8.0f).row();
        }).maxHeight(Core.scene.getHeight() * 0.7f).row();
        baseDialog.buttons.defaults().size(Math.min(f / 2.0f - 6.0f, 220.0f), 56.0f).pad(4.0f);
        baseDialog.buttons.button(bl ? HubText.get("decline") : Access.tr("access.later"), (Drawable)Icon.cancel, () -> {
            if (bl) {
                HubClient.setConsent(false);
            }
            baseDialog.hide();
        });
        baseDialog.buttons.button(Access.tr("access.send"), (Drawable)Icon.upload, () -> {
            String string = textArea.getText().trim();
            if (string.length() < 5) {
                Vars.ui.showInfoToast(Access.tr("access.short"), 3.0f);
                return;
            }
            baseDialog.hide();
            if (bl) {
                HubClient.setConsent(true);
                HubClient.ensureRegistered(() -> {
                    HubClient.heartbeat(null);
                    Access.send(string);
                    if (runnable != null) {
                        runnable.run();
                    }
                });
            } else {
                Access.send(string);
            }
        });
        baseDialog.closeOnBack(() -> {
            if (bl && !HubClient.decided()) {
                HubClient.setConsent(false);
            }
        });
        baseDialog.show();
    }

    private static void send(String string2) {
        Jval jval2 = Jval.newObject();
        jval2.add("reason", string2);
        HubClient.call("POST", "/api/access/request", jval2, (Cons<Jval>)((Cons)jval -> {
            Core.settings.put(K_STATUS, (Object)jval.getString("status", "pending"));
            Vars.ui.showInfoToast(Access.tr("access.sent"), 5.0f);
        }), (Cons<String>)((Cons)string -> Vars.ui.showInfoToast(Access.tr("access.fail"), 4.0f)));
    }

    private static void a(String string, String string2, String string3, String string4, String string5) {
        T.put((Object)string, (Object)new String[]{string2, string3, string4, string5});
    }

    private static String tr(String string) {
        String[] stringArray = (String[])T.get((Object)string);
        if (stringArray == null) {
            return string;
        }
        String string2 = LunariansI18n.getLanguage();
        return stringArray[string2.equals("pt") ? 1 : (string2.equals("es") ? 2 : (string2.equals("ru") ? 3 : 0))];
    }

    static {
        T = new ObjectMap();
        Access.a("access.title", "Request Beta UI access", "Pedir acesso ao Beta UI", "Pedir acceso a la Beta UI", "\u0417\u0430\u043f\u0440\u043e\u0441\u0438\u0442\u044c \u0434\u043e\u0441\u0442\u0443\u043f \u043a Beta UI");
        Access.a("access.intro", "The Beta UI is unlocked by the developer. Tell him why you want to use the mod and send the request.", "O Beta UI \u00e9 liberado pelo desenvolvedor. Conte por que voc\u00ea quer usar o mod e envie o pedido.", "La Beta UI la desbloquea el desarrollador. Cu\u00e9ntale por qu\u00e9 quieres usar el mod y env\u00eda la solicitud.", "Beta UI \u043e\u0442\u043a\u0440\u044b\u0432\u0430\u0435\u0442 \u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a. \u041d\u0430\u043f\u0438\u0448\u0438\u0442\u0435, \u0437\u0430\u0447\u0435\u043c \u0432\u0430\u043c \u043c\u043e\u0434, \u0438 \u043e\u0442\u043f\u0440\u0430\u0432\u044c\u0442\u0435 \u0437\u0430\u044f\u0432\u043a\u0443.");
        Access.a("access.why", "Why do you want to use the mod?", "Por que voc\u00ea quer usar o mod?", "\u00bfPor qu\u00e9 quieres usar el mod?", "\u0417\u0430\u0447\u0435\u043c \u0432\u044b \u0445\u043e\u0442\u0438\u0442\u0435 \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u044c \u043c\u043e\u0434?");
        Access.a("access.hint", "Write your reason here...", "Escreva o motivo aqui...", "Escribe el motivo aqu\u00ed...", "\u041d\u0430\u043f\u0438\u0448\u0438\u0442\u0435 \u043f\u0440\u0438\u0447\u0438\u043d\u0443 \u0437\u0434\u0435\u0441\u044c...");
        Access.a("access.send", "Send", "Enviar", "Enviar", "\u041e\u0442\u043f\u0440\u0430\u0432\u0438\u0442\u044c");
        Access.a("access.later", "Later", "Depois", "Despu\u00e9s", "\u041f\u043e\u0437\u0436\u0435");
        Access.a("access.short", "Write at least a few words", "Escreva pelo menos algumas palavras", "Escribe al menos unas palabras", "\u041d\u0430\u043f\u0438\u0448\u0438\u0442\u0435 \u0445\u043e\u0442\u044f \u0431\u044b \u043f\u0430\u0440\u0443 \u0441\u043b\u043e\u0432");
        Access.a("access.sent", "Request sent. You will be notified when it is approved.", "Pedido enviado. Voc\u00ea ser\u00e1 avisado quando for aprovado.", "Solicitud enviada. Se te avisar\u00e1 cuando sea aprobada.", "\u0417\u0430\u044f\u0432\u043a\u0430 \u043e\u0442\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u0430. \u0412\u044b \u043f\u043e\u043b\u0443\u0447\u0438\u0442\u0435 \u0443\u0432\u0435\u0434\u043e\u043c\u043b\u0435\u043d\u0438\u0435 \u043f\u043e\u0441\u043b\u0435 \u043e\u0434\u043e\u0431\u0440\u0435\u043d\u0438\u044f.");
        Access.a("access.fail", "Could not send. Try again.", "N\u00e3o foi poss\u00edvel enviar. Tente de novo.", "No se pudo enviar. Int\u00e9ntalo de nuevo.", "\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043e\u0442\u043f\u0440\u0430\u0432\u0438\u0442\u044c. \u041f\u043e\u043f\u0440\u043e\u0431\u0443\u0439\u0442\u0435 \u0435\u0449\u0451 \u0440\u0430\u0437.");
        Access.a("access.approved", "Beta UI unlocked!", "Beta UI liberado!", "\u00a1Beta UI desbloqueada!", "Beta UI \u0440\u0430\u0437\u0431\u043b\u043e\u043a\u0438\u0440\u043e\u0432\u0430\u043d!");
        Access.a("access.rejected", "Beta UI request rejected", "Pedido do Beta UI recusado", "Solicitud de Beta UI rechazada", "\u0417\u0430\u044f\u0432\u043a\u0430 \u043d\u0430 Beta UI \u043e\u0442\u043a\u043b\u043e\u043d\u0435\u043d\u0430");
        Access.a("access.waiting", "Your request is waiting for the developer.", "Seu pedido est\u00e1 aguardando o desenvolvedor.", "Tu solicitud espera al desarrollador.", "\u0412\u0430\u0448\u0430 \u0437\u0430\u044f\u0432\u043a\u0430 \u0436\u0434\u0451\u0442 \u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a\u0430.");
        Access.a("access.ok", "Beta UI is already unlocked.", "O Beta UI j\u00e1 est\u00e1 liberado.", "La Beta UI ya est\u00e1 desbloqueada.", "Beta UI \u0443\u0436\u0435 \u0440\u0430\u0437\u0431\u043b\u043e\u043a\u0438\u0440\u043e\u0432\u0430\u043d.");
        Access.a("access.btn", "Beta UI access", "Acesso ao Beta UI", "Acceso a la Beta UI", "\u0414\u043e\u0441\u0442\u0443\u043f \u043a Beta UI");
    }
}

