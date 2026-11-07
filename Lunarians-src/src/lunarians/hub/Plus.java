package lunarians.hub;

import arc.Core;
import arc.Events;
import arc.files.Fi;
import arc.func.Cons;
import arc.graphics.Pixmap;
import arc.graphics.PixmapIO;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Image;
import arc.scene.ui.Label;
import arc.scene.ui.TextButton;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectMap;
import arc.util.Log;
import arc.util.Scaling;
import arc.util.Timer;
import arc.util.serialization.Base64Coder;
import arc.util.serialization.Jval;
import java.lang.reflect.Field;
import java.util.Iterator;
import lunarians.LunariansSettings;
import lunarians.UiStyle;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.ui.FileChooser;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/hub/Plus.class */
public final class Plus {
    private static final String K_G = "lunarians-hub-gchat-after";
    private static boolean inited;
    private static boolean globalOpen;
    private static Field betaField;
    private static Field panelField;
    private static boolean betaFailed;
    private static final String[] FLAGS = {"enableEnemyCoreAlert", "enableAutoFill", "enableFreeCam", "enableProductionAlert", "enableCamFollow", "enableLossWarn", "enableAutoUnit", "enableAutoCargo", "enableAutoContainer", "enableAutoDefence", "enableAutoShield", "enableAutoHeal", "enableAutoAttack", "enableAutoUnitHeal", "enableAutoMoon", "enableAutoConnectConveyor", "enableAutoDrill", "enableAutoDrillConveyor", "dockAuto", "pvpMode"};
    public static volatile int gnew = 0;
    private static int lastGnew = 0;
    private static final ObjectMap<String, Boolean> orig = new ObjectMap<>();
    private static final ObjectMap<String, Boolean> forced = new ObjectMap<>();
    private static final ObjectMap<Integer, TextureRegionDrawable> imgCache = new ObjectMap<>();

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Plus$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            Plus.openGlobal();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Plus$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass1 implements Cons {
        public final /* synthetic */ Cons f$0;

        public final void get(Object obj) {
            this.f$0.get((byte[]) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Plus$10, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass10 implements Cons {
        public final /* synthetic */ TextField f$0;
        public final /* synthetic */ String f$1;

        public /* synthetic */ AnonymousClass10(TextField textField, String str) {
            this.f$0 = textField;
            this.f$1 = str;
        }

        public final void get(Object obj) {
            Plus.lambda$openChat$11(this.f$0, this.f$1, (String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Plus$11, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass11 implements Cons {
        public final /* synthetic */ int f$0;
        public final /* synthetic */ Image f$1;

        public /* synthetic */ AnonymousClass11(int i, Image image) {
            this.f$0 = i;
            this.f$1 = image;
        }

        public final void get(Object obj) {
            Plus.lambda$loadInto$3(this.f$0, this.f$1, (Jval) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Plus$12, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass12 implements Cons {
        public final void get(Object obj) {
            Plus.lambda$loadInto$4((String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Plus$13, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass13 implements Runnable {
        public final /* synthetic */ boolean[] f$0;
        public final /* synthetic */ boolean f$1;
        public final /* synthetic */ int[] f$2;
        public final /* synthetic */ String f$3;
        public final /* synthetic */ Table f$4;
        public final /* synthetic */ String f$5;

        public /* synthetic */ AnonymousClass13(boolean[] zArr, boolean z, int[] iArr, String str, Table table, String str2) {
            this.f$0 = zArr;
            this.f$1 = z;
            this.f$2 = iArr;
            this.f$3 = str;
            this.f$4 = table;
            this.f$5 = str2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Plus.lambda$openChat$9(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Plus$14, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass14 implements Cons {
        public final /* synthetic */ TextField f$0;
        public final /* synthetic */ boolean f$1;
        public final /* synthetic */ String f$2;
        public final /* synthetic */ Runnable[] f$3;

        public /* synthetic */ AnonymousClass14(TextField textField, boolean z, String str, Runnable[] runnableArr) {
            this.f$0 = textField;
            this.f$1 = z;
            this.f$2 = str;
            this.f$3 = runnableArr;
        }

        public final void get(Object obj) {
            Plus.lambda$openChat$12(this.f$0, this.f$1, this.f$2, this.f$3, (byte[]) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Plus$15, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass15 implements Cons {
        public final /* synthetic */ TextField f$0;
        public final /* synthetic */ Cons f$1;

        public /* synthetic */ AnonymousClass15(TextField textField, Cons cons) {
            this.f$0 = textField;
            this.f$1 = cons;
        }

        public final void get(Object obj) {
            Plus.lambda$openChat$16(this.f$0, this.f$1, obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Plus$16, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass16 implements Runnable {
        public final /* synthetic */ boolean f$0;
        public final /* synthetic */ Runnable[] f$1;

        public /* synthetic */ AnonymousClass16(boolean z, Runnable[] runnableArr) {
            this.f$0 = z;
            this.f$1 = runnableArr;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Plus.lambda$openChat$17(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Plus$17, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass17 implements Runnable {
        public final /* synthetic */ BaseDialog f$0;
        public final /* synthetic */ Runnable[] f$1;

        public /* synthetic */ AnonymousClass17(BaseDialog baseDialog, Runnable[] runnableArr) {
            this.f$0 = baseDialog;
            this.f$1 = runnableArr;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Plus.lambda$openChat$18(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Plus$18, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass18 implements Runnable {
        public final /* synthetic */ Timer.Task f$0;
        public final /* synthetic */ boolean f$1;

        public /* synthetic */ AnonymousClass18(Timer.Task task, boolean z) {
            this.f$0 = task;
            this.f$1 = z;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Plus.lambda$openChat$19(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Plus$19, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass19 implements Cons {
        public final void get(Object obj) {
            Plus.lambda$sync$1((Jval) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Plus$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass2 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            Plus.sync();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Plus$20, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass20 implements Cons {
        public final void get(Object obj) {
            Plus.lambda$sync$2((String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Plus$21, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass21 implements Runnable {
        public final /* synthetic */ int f$0;

        @Override // java.lang.Runnable
        public final void run() {
            Plus.showFull(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Plus$22, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass22 implements Cons {
        public final /* synthetic */ Cons f$0;

        public final void get(Object obj) {
            Plus.lambda$pickImage$5(this.f$0, obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Plus$3, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass3 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            Plus.$r8$lambda$6ujlpaVYiRs6bGAdxm07PJYtKFw();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Plus$4, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass4 implements Cons {
        public final /* synthetic */ boolean[] f$0;
        public final /* synthetic */ int[] f$1;
        public final /* synthetic */ Table f$2;
        public final /* synthetic */ boolean f$3;
        public final /* synthetic */ String f$4;

        public /* synthetic */ AnonymousClass4(boolean[] zArr, int[] iArr, Table table, boolean z, String str) {
            this.f$0 = zArr;
            this.f$1 = iArr;
            this.f$2 = table;
            this.f$3 = z;
            this.f$4 = str;
        }

        public final void get(Object obj) {
            Plus.lambda$openChat$7(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, (Jval) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Plus$5, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass5 implements Cons {
        public final /* synthetic */ boolean[] f$0;

        public final void get(Object obj) {
            Plus.lambda$openChat$8(this.f$0, (String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Plus$6, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass6 implements Runnable {
        public final /* synthetic */ Cons f$0;

        @Override // java.lang.Runnable
        public final void run() {
            Plus.lambda$openChat$14(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Plus$7, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass7 implements Runnable {
        public final /* synthetic */ Cons f$0;

        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.get(null);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Plus$8, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass8 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            Plus.lambda$hookBeta$0();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Plus$9, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass9 implements Cons {
        public final /* synthetic */ Runnable[] f$0;

        public final void get(Object obj) {
            Plus.lambda$openChat$10(this.f$0, (Jval) obj);
        }
    }

    static String tr(String str, String str2) {
        return AudioUi.tr(str, str2);
    }

    private static float width() {
        return Math.min(Core.scene.getWidth() - 40.0f, 520.0f);
    }

    public static void init() {
        if (inited || Vars.headless) {
            return;
        }
        inited = true;
        Room.init();
        try {
            Vars.ui.menufrag.addButton(tr("Chat global", "Global chat"), Icon.chat, Plus::openGlobal);
        } catch (Throwable th) {
        }
        hookBeta();
        Timer.schedule(Plus::sync, 8.0f, 30.0f);
        Timer.schedule(Plus::enforce, 3.0f, 2.0f);
    }

    public static void openChatId(String str, String str2) {
        openChat(str2, str);
    }

    public static void openGlobal() {
        if (!HubClient.decided() || !HubClient.consented()) {
            Access.showConsent(Plus::openGlobal);
        } else {
            openChat(null, null);
        }
    }

    private static void hookBeta() {
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
                String str = tr("Chat global", "Global chat") + ((gnew <= 0 || !HubClient.consented()) ? "" : " (" + gnew + ")");
                TextButton find = table.find("lunarians-hub-gchat");
                if (find instanceof TextButton) {
                    find.setText(str);
                    return;
                }
                TextButton textButton = new TextButton(str, Styles.defaultt);
                textButton.name = "lunarians-hub-gchat";
                textButton.clicked(Plus::openGlobal);
                table.row();
                table.add(textButton).growX().height(44.0f).pad(2.0f);
            } catch (Throwable th) {
                betaFailed = true;
                Log.warn("[Lunarians Hub] Global chat hook off: @", new Object[]{th.toString()});
            }
        });
    }

    private static Boolean getFlag(String str) {
        try {
            return Boolean.valueOf(LunariansSettings.class.getField(str).getBoolean(null));
        } catch (Throwable th) {
            return null;
        }
    }

    private static void setFlag(String str, boolean z) {
        try {
            LunariansSettings.class.getField(str).setBoolean(null, z);
        } catch (Throwable th) {
            Log.debug("[LunariansHub] flag @ indisponivel", new Object[]{str});
        }
    }

    static void sync() {
        if (!HubClient.consented() || HubClient.myId().isEmpty() || HubClient.blocked) {
            return;
        }
        HubClient.call("GET", "/api/plus/sync?gafter=" + Core.settings.getInt(K_G, 0), null, jval -> {
            Boolean flag;
            try {
                Jval jval = jval.get("features");
                boolean z = false;
                for (String str : FLAGS) {
                    boolean bool = jval.getBool(str, true);
                    if (bool == jval.getBool(str, false)) {
                        if (!forced.containsKey(str) && (flag = getFlag(str)) != null) {
                            orig.put(str, flag);
                        }
                        forced.put(str, Boolean.valueOf(bool));
                        setFlag(str, bool);
                    } else if (forced.containsKey(str)) {
                        Boolean bool2 = (Boolean) orig.get(str);
                        if (bool2 != null) {
                            setFlag(str, bool2.booleanValue());
                            z = true;
                        }
                        forced.remove(str);
                        orig.remove(str);
                    }
                }
                if (z) {
                    LunariansSettings.save();
                }
                gnew = jval.getInt("gnew", 0);
                if (gnew > lastGnew && !globalOpen) {
                    Vars.ui.showInfoToast(tr("Novas mensagens no chat global", "New global chat messages"), 3.0f);
                }
                lastGnew = gnew;
            } catch (Throwable th) {
                Log.debug("[LunariansHub] sync: @", new Object[]{th.toString()});
            }
        }, str -> {
        });
    }

    private static void enforce() {
        if (forced.size == 0) {
            return;
        }
        for (ObjectMap.Entry entry : forced.entries()) {
            Boolean flag = getFlag((String) entry.key);
            if (flag != null && flag.booleanValue() != ((Boolean) entry.value).booleanValue()) {
                setFlag((String) entry.key, ((Boolean) entry.value).booleanValue());
            }
        }
    }

    private static void loadInto(Image image, int i) {
        TextureRegionDrawable textureRegionDrawable = (TextureRegionDrawable) imgCache.get(Integer.valueOf(i));
        if (textureRegionDrawable != null) {
            image.setDrawable(textureRegionDrawable);
        } else {
            HubClient.call("GET", "/api/chat/img?id=" + i, null, jval -> {
                try {
                    TextureRegionDrawable fromPng = Avatar.fromPng(Base64Coder.decode(jval.getString("png", "")));
                    imgCache.put(Integer.valueOf(i), fromPng);
                    image.setDrawable(fromPng);
                } catch (Throwable th) {
                }
            }, str -> {
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void showFull(int i) {
        BaseDialog baseDialog = new BaseDialog("");
        UiStyle.register(baseDialog);
        Image image = new Image();
        image.setScaling(Scaling.fit);
        loadInto(image, i);
        baseDialog.cont.add(image).size(Math.min(Core.scene.getWidth() - 40.0f, 480.0f), Core.scene.getHeight() * 0.6f).row();
        baseDialog.addCloseButton();
        baseDialog.show();
    }

    private static byte[] shrink(Pixmap pixmap) {
        int length = new int[]{384, 256, 192, 128, 96}.length;
        for (int i = 0; i < length; i++) {
            float min = Math.min(1.0f, r0[i] / Math.max(pixmap.width, pixmap.height));
            int max = Math.max(1, Math.round(pixmap.width * min));
            int max2 = Math.max(1, Math.round(pixmap.height * min));
            Pixmap pixmap2 = new Pixmap(max, max2);
            pixmap2.draw(pixmap, 0, 0, pixmap.width, pixmap.height, 0, 0, max, max2);
            byte[] writePngBytes = PixmapIO.writePngBytes(pixmap2);
            pixmap2.dispose();
            if (writePngBytes.length <= 59392) {
                return writePngBytes;
            }
        }
        return null;
    }

    private static void pickImage(Cons<byte[]> cons) {
        new FileChooser.FileChooserParams().open(true).title(tr("Enviar imagem", "Send image")).extensions(new String[]{"png", "jpg", "jpeg"}).submit(obj -> {
            Fi fi = (Fi) obj;
            try {
                if (fi.length() > 8000000) {
                    Vars.ui.showInfoToast(tr("Imagem muito grande", "Image too large"), 3.0f);
                    return;
                }
                Pixmap pixmap = new Pixmap(fi);
                byte[] shrink = shrink(pixmap);
                pixmap.dispose();
                if (shrink == null) {
                    Vars.ui.showInfoToast(tr("Nao foi possivel reduzir a imagem", "Could not shrink the image"), 3.0f);
                } else {
                    cons.get(shrink);
                }
            } catch (Throwable th) {
                Vars.ui.showInfoToast(tr("Imagem invalida", "Invalid image"), 3.0f);
            }
        });
    }

    private static Label wrap(String str) {
        Label label = new Label(str);
        label.setWrap(true);
        return label;
    }

    private static void addMsg(Table table, Jval jval, boolean z, String str) {
        table.add(wrap((jval.getBool("mine", false) ? tr("Voce", "You") : z ? jval.getString("name", "?") : str) + ": " + jval.getString("text", ""))).width(width() - 20.0f).left().row();
        int i = jval.getInt("img", 0);
        if (i > 0) {
            Image image = new Image();
            image.setScaling(Scaling.fit);
            image.clicked(() -> {
                showFull(i);
            });
            loadInto(image, i);
            table.add(image).size(160.0f).left().row();
        }
    }

    public static void openChat(String str, String str2) {
        boolean z = str2 == null;
        BaseDialog baseDialog = new BaseDialog(z ? tr("Chat global", "Global chat") : HubText.get("chat") + ": " + str);
        UiStyle.register(baseDialog);
        Table table = new Table();
        int[] iArr = {0};
        boolean[] zArr = {false};
        Runnable[] runnableArr = {() -> {
            if (zArr[0]) {
                return;
            }
            zArr[0] = true;
            HubClient.call("GET", z ? "/api/gchat?after=" + iArr[0] : "/api/chat?with=" + str2 + "&after=" + iArr[0], null, jval -> {
                zArr[0] = false;
                Iterator it = jval.get("messages").asArray().iterator();
                while (it.hasNext()) {
                    Jval jval = (Jval) it.next();
                    int i = jval.getInt("id", 0);
                    if (i > iArr[0]) {
                        iArr[0] = i;
                        addMsg(table, jval, z, str);
                        if (z) {
                            Core.settings.put(K_G, Integer.valueOf(Math.max(Core.settings.getInt(K_G, 0), i)));
                        } else {
                            HubClient.setLastChat(Math.max(HubClient.lastChat(), i));
                        }
                    }
                }
                if (z) {
                    gnew = 0;
                    lastGnew = 0;
                }
            }, str3 -> {
                zArr[0] = false;
            });
        }};
        baseDialog.cont.pane(table).width(width()).height(Core.scene.getHeight() * 0.4f).row();
        TextField textField = new TextField();
        textField.setMessageText(HubText.get("msg.hint"));
        textField.setMaxLength(300);
        Cons cons = bArr -> {
            String trim = textField.getText().trim();
            if (trim.isEmpty() && bArr == null) {
                return;
            }
            Jval newObject = Jval.newObject();
            if (z) {
                newObject.add("text", trim);
            } else {
                newObject.add("to", str2);
                newObject.add("text", trim);
            }
            if (bArr != null) {
                newObject.add("png", new String(Base64Coder.encode(bArr)));
            }
            textField.setText("");
            HubClient.call("POST", z ? "/api/gchat/send" : "/api/chat/send", newObject, jval -> {
                runnableArr[0].run();
            }, str3 -> {
                textField.setText(trim);
                Vars.ui.showInfoToast("muted".equals(str3) ? tr("Voce foi silenciado pelo admin", "You were muted by the admin") : "rate_limited".equals(str3) ? tr("Devagar! Aguarde um instante", "Slow down, wait a moment") : HubText.get("sent.fail"), 2.5f);
            });
        };
        baseDialog.cont.table(obj -> {
            Table table2 = (Table) obj;
            table2.add(textField).growX().height(46.0f);
            table2.button("Img", () -> {
                pickImage(bArr2 -> {
                    cons.get(bArr2);
                });
            }).size(60.0f, 46.0f);
            table2.button(HubText.get("send"), () -> {
                cons.get((Object) null);
            }).size(90.0f, 46.0f);
        }).width(width());
        baseDialog.addCloseButton();
        baseDialog.shown(() -> {
            if (z) {
                globalOpen = true;
            }
            runnableArr[0].run();
        });
        Timer.Task schedule = Timer.schedule(() -> {
            if (baseDialog.isShown()) {
                runnableArr[0].run();
            }
        }, 4.0f, 4.0f);
        baseDialog.hidden(() -> {
            schedule.cancel();
            if (z) {
                globalOpen = false;
            }
        });
        baseDialog.show();
    }
}
