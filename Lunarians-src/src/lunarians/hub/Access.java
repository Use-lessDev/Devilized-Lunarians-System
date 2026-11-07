package lunarians.hub;

import arc.Core;
import arc.Events;
import arc.func.Boolp;
import arc.func.Cons;
import arc.scene.Element;
import arc.scene.ui.Label;
import arc.scene.ui.TextArea;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectMap;
import arc.util.Log;
import arc.util.Timer;
import arc.util.serialization.Jval;
import java.lang.reflect.Field;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import lunarians.UiStyle;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.ui.dialogs.BaseDialog;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/hub/Access.class */
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
    private static final ObjectMap<String, String[]> T = new ObjectMap<>();

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Access$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Cons {
        public final void get(Object obj) {
            Access.lambda$send$8((Jval) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Access$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass1 implements Cons {
        public final void get(Object obj) {
            Access.lambda$send$9((String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Access$10, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass10 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            Access.$r8$lambda$uCZPasvv6gv_uXxNXwbsOANMz2o();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Access$11, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass11 implements Runnable {
        public final /* synthetic */ String f$0;
        public final /* synthetic */ Runnable f$1;

        public /* synthetic */ AnonymousClass11(String str, Runnable runnable) {
            this.f$0 = str;
            this.f$1 = runnable;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Access.lambda$build$5(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Access$12, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass12 implements Cons {
        public final void get(Object obj) {
            Access.lambda$poll$1((Jval) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Access$13, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass13 implements Cons {
        public final void get(Object obj) {
            Access.lambda$poll$2((String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Access$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass2 implements Boolp {
        public final /* synthetic */ Boolp f$0;

        public final boolean get() {
            return Access.lambda$gate$0(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Access$3, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass3 implements Cons {
        public final /* synthetic */ boolean f$0;
        public final /* synthetic */ float f$1;
        public final /* synthetic */ TextArea f$2;

        public /* synthetic */ AnonymousClass3(boolean z, float f, TextArea textArea) {
            this.f$0 = z;
            this.f$1 = f;
            this.f$2 = textArea;
        }

        public final void get(Object obj) {
            Access.lambda$build$3(this.f$0, this.f$1, this.f$2, (Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Access$4, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass4 implements Runnable {
        public final /* synthetic */ boolean f$0;
        public final /* synthetic */ BaseDialog f$1;

        public /* synthetic */ AnonymousClass4(boolean z, BaseDialog baseDialog) {
            this.f$0 = z;
            this.f$1 = baseDialog;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Access.lambda$build$4(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Access$5, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass5 implements Runnable {
        public final /* synthetic */ TextArea f$0;
        public final /* synthetic */ BaseDialog f$1;
        public final /* synthetic */ boolean f$2;
        public final /* synthetic */ Runnable f$3;

        public /* synthetic */ AnonymousClass5(TextArea textArea, BaseDialog baseDialog, boolean z, Runnable runnable) {
            this.f$0 = textArea;
            this.f$1 = baseDialog;
            this.f$2 = z;
            this.f$3 = runnable;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Access.lambda$build$6(this.f$0, this.f$1, this.f$2, this.f$3);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Access$6, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass6 implements Runnable {
        public final /* synthetic */ boolean f$0;

        @Override // java.lang.Runnable
        public final void run() {
            Access.lambda$build$7(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Access$7, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass7 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            Access.$r8$lambda$tKDJ2uhsPRFv_aeuWRhZsCXuTJU();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Access$8, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass8 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            Access.$r8$lambda$mTsPNBkm8FRTGr2SLRr1Kkc2Scw();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Access$9, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass9 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            Access.$r8$lambda$2-uLtPNdvNgR9VCfXPVgeCjNkLs();
        }
    }

    public static String status() {
        return Core.settings.getString(K_STATUS, "none");
    }

    public static boolean approved() {
        return HubClient.consented() && "approved".equals(status());
    }

    public static void initAll() {
        Plus.init();
        init();
    }

    private static void init() {
        if (inited || Vars.headless) {
            return;
        }
        inited = true;
        Events.run(EventType.Trigger.update, Access::gate);
        Timer.schedule(Access::poll, 4.0f, 30.0f);
        Timer.schedule(Access::enforce, 3.0f, 2.0f);
        try {
            Vars.ui.menufrag.addButton(tr("access.btn"), Icon.lock, Access::menuClick);
        } catch (Throwable th) {
        }
    }

    private static void gate() {
        if (Core.graphics.getFrameId() % 20 != 0) {
            return;
        }
        try {
            if (betaField == null) {
                betaField = Class.forName("lunarians.LunariansMod").getField("betaHud");
            }
            Element element = (Element) betaField.get(null);
            if (element == null) {
                return;
            }
            if (element.visibility != wrapper) {
                Boolp boolp = element.visibility;
                wrapper = () -> {
                    return approved() && (boolp == null || boolp.get());
                };
                element.visible(wrapper);
            }
            if (!approved()) {
                element.visible = false;
            }
        } catch (Throwable th) {
            if (!warned) {
                warned = true;
                Log.warn("[Lunarians Hub] Beta UI gate: @", new Object[]{th.toString()});
            }
        }
    }

    private static void poll() {
        if (!HubClient.consented()) {
            if (!"none".equals(status())) {
                Core.settings.put(K_STATUS, "none");
            }
            applyFeatures(null);
        } else {
            if (HubClient.myId().isEmpty() || HubClient.blocked) {
                return;
            }
            HubClient.call("GET", "/api/access/status", null, jval -> {
                apply(jval.getString("status", "none"), jval.getString("note", ""));
                applyFeatures(jval.has("features") ? jval.get("features") : null);
            }, str -> {
            });
        }
    }

    private static void apply(String str, String str2) {
        String status = status();
        Core.settings.put(K_STATUS, str);
        if (fetchedOnce && !str.equals(status)) {
            if (str.equals("approved")) {
                Vars.ui.showInfoToast(tr("access.approved"), 5.0f);
            } else if (str.equals("rejected")) {
                Vars.ui.showInfoToast(tr("access.rejected") + (str2.isEmpty() ? "" : ": " + str2), 6.0f);
            }
        }
        fetchedOnce = true;
        if (!str.equals("none") || askedThisSession) {
            return;
        }
        askedThisSession = true;
        build(false, null);
    }

    private static void applyFeatures(Jval jval) {
        if (jval != null) {
            try {
                if (jval.has("showSchematicPanel")) {
                    boolean bool = jval.getBool("showSchematicPanel", true);
                    if (schemForced == null) {
                        schemOrig = Boolean.valueOf(LunariansSettings.showSchematicPanel);
                    }
                    schemForced = Boolean.valueOf(bool);
                    setSchem(bool);
                }
            } catch (Throwable th) {
                return;
            }
        }
        if (schemForced != null) {
            if (schemOrig != null) {
                setSchem(schemOrig.booleanValue());
            }
            schemForced = null;
            schemOrig = null;
        }
    }

    private static void setSchem(boolean z) {
        if (LunariansSettings.showSchematicPanel != z) {
            LunariansSettings.showSchematicPanel = z;
        }
    }

    private static void enforce() {
        if (schemForced != null) {
            setSchem(schemForced.booleanValue());
        }
    }

    private static void menuClick() {
        if (!HubClient.consented()) {
            showConsent(null);
            return;
        }
        String status = status();
        if (!status.equals("approved")) {
            if (!status.equals("pending")) {
                build(false, null);
                return;
            } else {
                Vars.ui.showInfoToast(tr("access.waiting"), 3.0f);
                return;
            }
        }
        Vars.ui.showInfoToast(tr("access.ok"), 3.0f);
    }

    public static void showConsent(Runnable runnable) {
        build(true, runnable);
    }

    private static Label wrap(String str) {
        Label label = new Label(str);
        label.setWrap(true);
        return label;
    }

    private static void build(boolean z, Runnable runnable) {
        BaseDialog baseDialog = new BaseDialog(z ? HubText.get("consent.title") : tr("access.title"));
        UiStyle.register(baseDialog);
        float min = Math.min(Core.scene.getWidth() - 40.0f, 520.0f);
        TextArea textArea = new TextArea("");
        textArea.setMessageText(tr("access.hint"));
        textArea.setMaxLength(300);
        baseDialog.cont.pane(table -> {
            if (z) {
                table.add(wrap(HubText.get("consent.body"))).width(min).left().pad(8.0f).row();
            } else {
                table.add(wrap(tr("access.intro"))).width(min).left().pad(8.0f).row();
            }
            table.add(wrap(tr("access.why"))).width(min).left().padTop(8.0f).padLeft(8.0f).row();
            table.add(textArea).width(min).height(120.0f).pad(8.0f).row();
        }).maxHeight(Core.scene.getHeight() * 0.7f).row();
        baseDialog.buttons.defaults().size(Math.min((min / 2.0f) - 6.0f, 220.0f), 56.0f).pad(4.0f);
        baseDialog.buttons.button(z ? HubText.get("decline") : tr("access.later"), Icon.cancel, () -> {
            if (z) {
                HubClient.setConsent(false);
            }
            baseDialog.hide();
        });
        baseDialog.buttons.button(tr("access.send"), Icon.upload, () -> {
            String trim = textArea.getText().trim();
            if (trim.length() < 5) {
                Vars.ui.showInfoToast(tr("access.short"), 3.0f);
                return;
            }
            baseDialog.hide();
            if (z) {
                HubClient.setConsent(true);
                HubClient.ensureRegistered(() -> {
                    HubClient.heartbeat(null);
                    send(trim);
                    if (runnable != null) {
                        runnable.run();
                    }
                });
            } else {
                send(trim);
            }
        });
        baseDialog.closeOnBack(() -> {
            if (!z || HubClient.decided()) {
                return;
            }
            HubClient.setConsent(false);
        });
        baseDialog.show();
    }

    private static void send(String str) {
        Jval newObject = Jval.newObject();
        newObject.add("reason", str);
        HubClient.call("POST", "/api/access/request", newObject, jval -> {
            Core.settings.put(K_STATUS, jval.getString("status", "pending"));
            Vars.ui.showInfoToast(tr("access.sent"), 5.0f);
        }, str2 -> {
            Vars.ui.showInfoToast(tr("access.fail"), 4.0f);
        });
    }

    static {
        a("access.title", "Request Beta UI access", "Pedir acesso ao Beta UI", "Pedir acceso a la Beta UI", "Запросить доступ к Beta UI");
        a("access.intro", "The Beta UI is unlocked by the developer. Tell him why you want to use the mod and send the request.", "O Beta UI é liberado pelo desenvolvedor. Conte por que você quer usar o mod e envie o pedido.", "La Beta UI la desbloquea el desarrollador. Cuéntale por qué quieres usar el mod y envía la solicitud.", "Beta UI открывает разработчик. Напишите, зачем вам мод, и отправьте заявку.");
        a("access.why", "Why do you want to use the mod?", "Por que você quer usar o mod?", "¿Por qué quieres usar el mod?", "Зачем вы хотите использовать мод?");
        a("access.hint", "Write your reason here...", "Escreva o motivo aqui...", "Escribe el motivo aquí...", "Напишите причину здесь...");
        a("access.send", "Send", "Enviar", "Enviar", "Отправить");
        a("access.later", "Later", "Depois", "Después", "Позже");
        a("access.short", "Write at least a few words", "Escreva pelo menos algumas palavras", "Escribe al menos unas palabras", "Напишите хотя бы пару слов");
        a("access.sent", "Request sent. You will be notified when it is approved.", "Pedido enviado. Você será avisado quando for aprovado.", "Solicitud enviada. Se te avisará cuando sea aprobada.", "Заявка отправлена. Вы получите уведомление после одобрения.");
        a("access.fail", "Could not send. Try again.", "Não foi possível enviar. Tente de novo.", "No se pudo enviar. Inténtalo de nuevo.", "Не удалось отправить. Попробуйте ещё раз.");
        a("access.approved", "Beta UI unlocked!", "Beta UI liberado!", "¡Beta UI desbloqueada!", "Beta UI разблокирован!");
        a("access.rejected", "Beta UI request rejected", "Pedido do Beta UI recusado", "Solicitud de Beta UI rechazada", "Заявка на Beta UI отклонена");
        a("access.waiting", "Your request is waiting for the developer.", "Seu pedido está aguardando o desenvolvedor.", "Tu solicitud espera al desarrollador.", "Ваша заявка ждёт разработчика.");
        a("access.ok", "Beta UI is already unlocked.", "O Beta UI já está liberado.", "La Beta UI ya está desbloqueada.", "Beta UI уже разблокирован.");
        a("access.btn", "Beta UI access", "Acesso ao Beta UI", "Acceso a la Beta UI", "Доступ к Beta UI");
    }

    private static void a(String str, String str2, String str3, String str4, String str5) {
        T.put(str, new String[]{str2, str3, str4, str5});
    }

    private static String tr(String str) {
        String[] strArr = (String[]) T.get(str);
        if (strArr == null) {
            return str;
        }
        String language = LunariansI18n.getLanguage();
        return strArr[language.equals("pt") ? (char) 1 : language.equals("es") ? (char) 2 : language.equals("ru") ? (char) 3 : (char) 0];
    }
}
