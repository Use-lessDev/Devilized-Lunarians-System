package lunarians.hub;

import arc.Core;
import arc.Events;
import arc.audio.Music;
import arc.audio.Sound;
import arc.files.Fi;
import arc.func.Cons;
import arc.scene.ui.Label;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectMap;
import arc.util.Log;
import arc.util.Timer;
import arc.util.serialization.Base64Coder;
import arc.util.serialization.Jval;
import java.lang.reflect.Field;
import java.util.Iterator;
import lunarians.LunariansI18n;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.ui.FileChooser;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/hub/AudioUi.class */
public class AudioUi {
    private static final String BTN = "lunarians-audio-btn";
    private static final String K_SHOT = "lun-shot-id";
    private static final String K_SHOT_FMT = "lun-shot-fmt";
    private static final String K_MUSIC = "lun-music-id";
    private static final String K_MUSIC_FMT = "lun-music-fmt";
    private static final int MAX_SHOT = 122880;
    private static final int MAX_MUSIC = 921600;
    private static boolean inited;
    private static boolean hookFailed;
    private static boolean loadingShot;
    private static boolean loadingMusic;
    private static Field betaField;
    private static Field panelField;
    private static Sound shotSnd;
    private static int shotLoaded;
    private static UnitType curType;
    private static Music music;
    private static int musicLoaded;
    private static Sound prevSound;
    private static Music prevMusic;
    private static final ObjectMap<Weapon, Sound> orig = new ObjectMap<>();
    private static final ObjectMap<String, String[]> X = new ObjectMap<>();

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.AudioUi$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Cons {
        public final /* synthetic */ int f$0;

        public final void get(Object obj) {
            AudioUi.lambda$tickShots$4(this.f$0, (Fi) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.AudioUi$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass1 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AudioUi.loadingShot = false;
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.AudioUi$10, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass10 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AudioUi.$r8$lambda$0MJF01RQkip2DObzekSMQ-YV-Mc();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.AudioUi$11, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass11 implements Runnable {
        public final /* synthetic */ BaseDialog f$0;

        @Override // java.lang.Runnable
        public final void run() {
            AudioUi.rebuild(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.AudioUi$12, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass12 implements Cons {
        public final /* synthetic */ Fi f$0;
        public final /* synthetic */ Cons f$1;

        public /* synthetic */ AnonymousClass12(Fi fi, Cons cons) {
            this.f$0 = fi;
            this.f$1 = cons;
        }

        public final void get(Object obj) {
            AudioUi.lambda$ensure$2(this.f$0, this.f$1, (Jval) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.AudioUi$13, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass13 implements Cons {
        public final void get(Object obj) {
            AudioUi.lambda$ensure$3((String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.AudioUi$14, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass14 implements Runnable {
        public final /* synthetic */ BaseDialog f$0;

        @Override // java.lang.Runnable
        public final void run() {
            AudioUi.pick("shot", this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.AudioUi$15, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass15 implements Runnable {
        public final /* synthetic */ BaseDialog f$0;

        @Override // java.lang.Runnable
        public final void run() {
            AudioUi.pick("music", this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.AudioUi$16, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass16 implements Cons {
        public final /* synthetic */ boolean f$0;

        public final void get(Object obj) {
            AudioUi.lambda$preview$10(this.f$0, (Fi) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.AudioUi$17, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass17 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AudioUi.open();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.AudioUi$18, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass18 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AudioUi.lambda$init$0();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.AudioUi$19, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass19 implements Cons {
        public final void get(Object obj) {
            AudioUi.stopMusic();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.AudioUi$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass2 implements Cons {
        public final /* synthetic */ String f$0;
        public final /* synthetic */ int f$1;
        public final /* synthetic */ String f$2;
        public final /* synthetic */ boolean f$3;
        public final /* synthetic */ boolean f$4;
        public final /* synthetic */ boolean f$5;
        public final /* synthetic */ BaseDialog f$6;

        public /* synthetic */ AnonymousClass2(String str, int i, String str2, boolean z, boolean z2, boolean z3, BaseDialog baseDialog) {
            this.f$0 = str;
            this.f$1 = i;
            this.f$2 = str2;
            this.f$3 = z;
            this.f$4 = z2;
            this.f$5 = z3;
            this.f$6 = baseDialog;
        }

        public final void get(Object obj) {
            AudioUi.lambda$rebuild$20(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, (Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.AudioUi$20, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass20 implements Cons {
        public final /* synthetic */ BaseDialog f$0;

        public final void get(Object obj) {
            AudioUi.rebuild(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.AudioUi$21, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass21 implements Cons {
        public final void get(Object obj) {
            AudioUi.lambda$rebuild$17((String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.AudioUi$22, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass22 implements Runnable {
        public final /* synthetic */ int f$0;
        public final /* synthetic */ String f$1;
        public final /* synthetic */ boolean f$2;

        public /* synthetic */ AnonymousClass22(int i, String str, boolean z) {
            this.f$0 = i;
            this.f$1 = str;
            this.f$2 = z;
        }

        @Override // java.lang.Runnable
        public final void run() {
            AudioUi.preview(this.f$0, this.f$1, this.f$2);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.AudioUi$23, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass23 implements Runnable {
        public final /* synthetic */ boolean f$0;
        public final /* synthetic */ int f$1;
        public final /* synthetic */ String f$2;
        public final /* synthetic */ BaseDialog f$3;

        public /* synthetic */ AnonymousClass23(boolean z, int i, String str, BaseDialog baseDialog) {
            this.f$0 = z;
            this.f$1 = i;
            this.f$2 = str;
            this.f$3 = baseDialog;
        }

        @Override // java.lang.Runnable
        public final void run() {
            AudioUi.lambda$rebuild$15(this.f$0, this.f$1, this.f$2, this.f$3);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.AudioUi$24, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass24 implements Runnable {
        public final /* synthetic */ boolean f$0;
        public final /* synthetic */ int f$1;
        public final /* synthetic */ String f$2;
        public final /* synthetic */ BaseDialog f$3;

        public /* synthetic */ AnonymousClass24(boolean z, int i, String str, BaseDialog baseDialog) {
            this.f$0 = z;
            this.f$1 = i;
            this.f$2 = str;
            this.f$3 = baseDialog;
        }

        @Override // java.lang.Runnable
        public final void run() {
            AudioUi.lambda$rebuild$19(this.f$0, this.f$1, this.f$2, this.f$3);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.AudioUi$3, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass3 implements Runnable {
        public final /* synthetic */ boolean f$0;
        public final /* synthetic */ int f$1;
        public final /* synthetic */ String f$2;
        public final /* synthetic */ BaseDialog f$3;

        public /* synthetic */ AnonymousClass3(boolean z, int i, String str, BaseDialog baseDialog) {
            this.f$0 = z;
            this.f$1 = i;
            this.f$2 = str;
            this.f$3 = baseDialog;
        }

        @Override // java.lang.Runnable
        public final void run() {
            AudioUi.lambda$rebuild$18(this.f$0, this.f$1, this.f$2, this.f$3);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.AudioUi$4, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass4 implements Cons {
        public final /* synthetic */ String f$0;
        public final /* synthetic */ BaseDialog f$1;

        public /* synthetic */ AnonymousClass4(String str, BaseDialog baseDialog) {
            this.f$0 = str;
            this.f$1 = baseDialog;
        }

        public final void get(Object obj) {
            AudioUi.lambda$pick$9(this.f$0, this.f$1, (Fi) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.AudioUi$5, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass5 implements Cons {
        public final /* synthetic */ BaseDialog f$0;

        public final void get(Object obj) {
            AudioUi.lambda$rebuild$13(this.f$0, (Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.AudioUi$6, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass6 implements Cons {
        public final /* synthetic */ Table f$0;
        public final /* synthetic */ float f$1;
        public final /* synthetic */ BaseDialog f$2;

        public /* synthetic */ AnonymousClass6(Table table, float f, BaseDialog baseDialog) {
            this.f$0 = table;
            this.f$1 = f;
            this.f$2 = baseDialog;
        }

        public final void get(Object obj) {
            AudioUi.lambda$rebuild$21(this.f$0, this.f$1, this.f$2, (Jval) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.AudioUi$7, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass7 implements Cons {
        public final /* synthetic */ Table f$0;

        public final void get(Object obj) {
            AudioUi.lambda$rebuild$22(this.f$0, (String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.AudioUi$8, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass8 implements Cons {
        public final /* synthetic */ BaseDialog f$0;

        public final void get(Object obj) {
            AudioUi.lambda$pick$7(this.f$0, (Jval) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.AudioUi$9, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass9 implements Cons {
        public final void get(Object obj) {
            AudioUi.lambda$pick$8((String) obj);
        }
    }

    static {
        x("Sons", "Sonidos", "Звуки");
        x("Falha ao baixar o som", "No se pudo descargar el sonido", "Не удалось скачать звук");
        x("Som de tiro (ogg/mp3/wav)", "Sonido de disparo (ogg/mp3/wav)", "Звук выстрела (ogg/mp3/wav)");
        x("Música (ogg/mp3/wav)", "Música (ogg/mp3/wav)", "Музыка (ogg/mp3/wav)");
        x("Arquivo grande demais (máx. ", "Archivo demasiado grande (máx. ", "Файл слишком большой (макс. ");
        x("Som enviado!", "¡Sonido enviado! Espera la aprobación.", "Звук отправлен! Ждите одобрения.");
        x("Falha no envio: ", "Error al enviar: ", "Ошибка отправки: ");
        x("Arquivo inválido", "Archivo inválido", "Неверный файл");
        x("Não foi possível tocar", "No se pudo reproducir", "Не удалось воспроизвести");
        x("Enviar som de tiro", "Subir sonido de disparo", "Загрузить звук выстрела");
        x("Enviar música", "Subir música", "Загрузить музыку");
        x("Nenhum som enviado ainda.", "Aún no has subido sonidos.", "Звуков пока нет.");
        x("Música", "Música", "Музыка");
        x("Tiro", "Disparo", "Выстрел");
        x("em uso", "en uso", "используется");
        x("Parar", "Dejar", "Выкл.");
        x("Usar", "Usar", "Исп.");
        x("Apagar som", "Borrar sonido", "Удалить звук");
        x("Apagar este som do site?", "¿Borrar este sonido del sitio?", "Удалить этот звук с сайта?");
        x("Falha", "Error", "Ошибка");
        x("Não foi possível carregar.", "No se pudo cargar.", "Не удалось загрузить.");
    }

    private static void x(String str, String str2, String str3) {
        X.put(str, new String[]{str2, str3});
    }

    static String tr(String str, String str2) {
        String[] strArr;
        String language = LunariansI18n.getLanguage();
        if ("pt".equals(language)) {
            return str;
        }
        if (("es".equals(language) || "ru".equals(language)) && (strArr = (String[]) X.get(str)) != null) {
            return strArr["es".equals(language) ? (char) 0 : (char) 1];
        }
        return str2;
    }

    static Fi dir() {
        Fi child = Core.settings.getDataDirectory().child("lunarians-audio");
        if (!child.exists()) {
            child.mkdirs();
        }
        return child;
    }

    public static void init() {
        if (inited || Vars.headless) {
            return;
        }
        inited = true;
        Events.run(EventType.Trigger.update, () -> {
            try {
                if (Core.graphics.getFrameId() % 30 == 0) {
                    hookButton();
                    tickShots();
                    tickMusic();
                }
            } catch (Throwable th) {
            }
        });
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            stopMusic();
        });
    }

    private static void hookButton() {
        if (hookFailed) {
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
            if (table == null || table.find(BTN) != null) {
                return;
            }
            TextButton textButton = new TextButton(tr("Sons", "Sounds"), Styles.defaultt);
            textButton.name = BTN;
            textButton.clicked(AudioUi::open);
            table.row();
            table.add(textButton).growX().height(44.0f).pad(2.0f);
        } catch (Throwable th) {
            hookFailed = true;
            Log.warn("[Lunarians Audio] botao off: @", new Object[]{th.toString()});
        }
    }

    static void ensure(int i, String str, Cons<Fi> cons) {
        Fi child = dir().child(i + "." + str);
        if (!child.exists() || child.length() <= 0) {
            HubClient.call("GET", "/api/audio/get?id=" + i, null, jval -> {
                try {
                    byte[] decode = Base64Coder.decode(jval.getString("data", ""));
                    if (decode.length < 64) {
                        throw new RuntimeException("vazio");
                    }
                    child.writeBytes(decode, false);
                    cons.get(child);
                } catch (Throwable th) {
                    Vars.ui.showInfoToast(tr("Falha ao baixar o som", "Could not download sound"), 2.5f);
                }
            }, str2 -> {
                Vars.ui.showInfoToast(tr("Falha ao baixar o som", "Could not download sound"), 2.5f);
            });
        } else {
            cons.get(child);
        }
    }

    private static void restoreWeapons() {
        ObjectMap.Entries it = orig.entries().iterator();
        while (it.hasNext()) {
            ObjectMap.Entry next = it.next();
            ((Weapon) next.key).shootSound = (Sound) next.value;
        }
        orig.clear();
        curType = null;
    }

    private static void tickShots() {
        int i = Core.settings.getInt(K_SHOT, 0);
        if (i <= 0 || !HubClient.consented()) {
            if (!orig.isEmpty()) {
                restoreWeapons();
            }
            shotSnd = null;
            shotLoaded = 0;
            return;
        }
        if (Vars.player == null || Vars.state == null || !Vars.state.isGame()) {
            return;
        }
        if (shotLoaded != i) {
            if (!loadingShot) {
                loadingShot = true;
                restoreWeapons();
                ensure(i, Core.settings.getString(K_SHOT_FMT, "ogg"), fi -> {
                    try {
                        shotSnd = new Sound(fi);
                        shotLoaded = i;
                    } catch (Throwable th) {
                        Log.warn("[Lunarians Audio] tiro: @", new Object[]{th.toString()});
                    }
                    loadingShot = false;
                });
                Timer.schedule(() -> {
                    loadingShot = false;
                }, 20.0f);
                return;
            }
            return;
        }
        UnitType unitType = Vars.player.unit() == null ? null : Vars.player.unit().type;
        if (unitType != null && unitType != curType && shotSnd != null) {
            restoreWeapons();
            curType = unitType;
            Iterator it = unitType.weapons.iterator();
            while (it.hasNext()) {
                Weapon weapon = (Weapon) it.next();
                orig.put(weapon, weapon.shootSound);
                weapon.shootSound = shotSnd;
            }
        }
    }

    public static void stopMusic() {
        try {
            if (music != null && music.isPlaying()) {
                music.stop();
            }
        } catch (Throwable th) {
        }
    }

    private static void tickMusic() {
        stopMusic();
    }

    private static Label wrap(String str) {
        Label label = new Label(str);
        label.setWrap(true);
        label.setAlignment(8);
        return label;
    }

    public static void open() {
        if (!HubClient.consented()) {
            HubUi.open();
            return;
        }
        BaseDialog baseDialog = new BaseDialog(tr("Sons", "Sounds"));
        baseDialog.addCloseButton();
        baseDialog.hidden(AudioUi::stopPreview);
        baseDialog.shown(() -> {
            rebuild(baseDialog);
        });
        baseDialog.show();
    }

    private static void stopPreview() {
        try {
            if (prevMusic != null) {
                prevMusic.stop();
            }
        } catch (Throwable th) {
        }
    }

    public static void pick(String str, BaseDialog baseDialog) {
        new FileChooser.FileChooserParams().open(true).title(str.equals("shot") ? tr("Som de tiro (ogg/mp3/wav)", "Shot sound (ogg/mp3/wav)") : tr("Música (ogg/mp3/wav)", "Music (ogg/mp3/wav)")).extensions(new String[]{"ogg", "mp3", "wav"}).submit(fi -> {
            try {
                byte[] readBytes = fi.readBytes();
                int i = str.equals("shot") ? MAX_SHOT : MAX_MUSIC;
                if (readBytes.length > i) {
                    Vars.ui.showInfoToast(tr("Arquivo grande demais (máx. ", "File too large (max ") + (i / 1024) + " KB)", 3.0f);
                    return;
                }
                Jval newObject = Jval.newObject();
                String nameWithoutExtension = fi.nameWithoutExtension();
                newObject.add("name", nameWithoutExtension.length() > 32 ? nameWithoutExtension.substring(0, 32) : nameWithoutExtension);
                newObject.add("kind", str);
                newObject.add("data", new String(Base64Coder.encode(readBytes)));
                HubClient.call("POST", "/api/audio/upload", newObject, jval -> {
                    Vars.ui.showInfoToast(tr("Som enviado!", "Sound uploaded! Waiting for admin approval."), 2.0f);
                    rebuild(baseDialog);
                }, str2 -> {
                    Vars.ui.showInfoToast(tr("Falha no envio: ", "Upload failed: ") + str2, 3.0f);
                });
            } catch (Throwable th) {
                Vars.ui.showInfoToast(tr("Arquivo inválido", "Invalid file"), 3.0f);
            }
        });
    }

    public static void preview(int i, String str, boolean z) {
        ensure(i, str, fi -> {
            try {
                stopPreview();
                if (z) {
                    prevMusic = new Music(fi);
                    prevMusic.setVolume(Core.settings.getInt("musicvol", 100) / 100.0f);
                    prevMusic.play();
                } else {
                    if (prevSound != null) {
                        prevSound.dispose();
                    }
                    prevSound = new Sound(fi);
                    prevSound.play(Core.settings.getInt("sfxvol", 100) / 100.0f);
                }
            } catch (Throwable th) {
                Vars.ui.showInfoToast(tr("Não foi possível tocar", "Could not play"), 2.5f);
            }
        });
    }

    public static void rebuild(BaseDialog baseDialog) {
        baseDialog.cont.clear();
        float min = Math.min(Core.scene.getWidth() - 40.0f, 520.0f);
        baseDialog.cont.add(wrap(HubText.get("audio.help"))).width(min).padBottom(6.0f).row();
        baseDialog.cont.table(table -> {
            table.defaults().height(48.0f).growX().pad(2.0f);
            table.button(tr("Enviar som de tiro", "Upload shot sound"), Icon.upload, () -> {
                pick("shot", baseDialog);
            });
            table.button(tr("Enviar música", "Upload music"), Icon.upload, () -> {
                pick("music", baseDialog);
            });
        }).width(min).row();
        Table table2 = new Table();
        baseDialog.cont.pane(table2).width(min).maxHeight(Core.scene.getHeight() * 0.55f).row();
        table2.add(wrap("...")).row();
        HubClient.call("GET", "/api/audio/mine", null, jval -> {
            table2.clear();
            Jval.JsonArray asArray = jval.get("audio").asArray();
            if (asArray.size == 0) {
                table2.add(wrap(tr("Nenhum som enviado ainda.", "No sounds uploaded yet."))).width(min - 20.0f).pad(6.0f).row();
                return;
            }
            Iterator it = asArray.iterator();
            while (it.hasNext()) {
                Jval jval = (Jval) it.next();
                int i = jval.getInt("id", 0);
                String string = jval.getString("kind", "shot");
                String string2 = jval.getString("fmt", "ogg");
                String string3 = jval.getString("status", "pending");
                String string4 = jval.getString("note", "");
                boolean equals = string.equals("music");
                boolean equals2 = string3.equals("approved");
                boolean z = !equals && Core.settings.getInt(K_SHOT, 0) == i;
                String str = jval.getString("name", "?") + "\n" + (equals ? tr("Música", "Music") : tr("Tiro", "Shot")) + " • " + (jval.getInt("size", 0) / 1024) + " KB" + (z ? "  [" + tr("em uso", "in use") + "]" : "") + "\n" + (equals2 ? "[lime]" : string3.equals("rejected") ? "[scarlet]" : "[gray]") + HubText.get("skin.status." + string3) + "[]" + ((!string3.equals("rejected") || string4.isEmpty()) ? "" : " - " + string4);
                table2.table(Styles.grayPanel, table3 -> {
                    table3.add(wrap(str)).growX().pad(6.0f);
                    table3.button(Icon.play, Styles.cleari, () -> {
                        preview(i, string2, equals);
                    }).size(38.0f);
                    if (!equals && equals2) {
                        table3.button(z ? tr("Parar", "Stop") : tr("Usar", "Use"), () -> {
                            if (z) {
                                Core.settings.put(K_SHOT, 0);
                            } else {
                                Core.settings.put(K_SHOT, Integer.valueOf(i));
                                Core.settings.put(K_SHOT_FMT, string2);
                            }
                            rebuild(baseDialog);
                        }).size(78.0f, 38.0f);
                    }
                    table3.button(Icon.trash, Styles.cleari, () -> {
                        Vars.ui.showConfirm(tr("Apagar som", "Delete sound"), tr("Apagar este som do site?", "Delete this sound from the site?"), () -> {
                            if (z) {
                                Core.settings.put(K_SHOT, 0);
                            }
                            Jval newObject = Jval.newObject();
                            newObject.add("id", Jval.valueOf(i));
                            dir().child(i + "." + string2).delete();
                            HubClient.call("POST", "/api/audio/delete", newObject, jval2 -> {
                                rebuild(baseDialog);
                            }, str2 -> {
                                Vars.ui.showInfoToast(tr("Falha", "Failed"), 2.0f);
                            });
                        });
                    }).size(38.0f);
                }).width(min - 10.0f).pad(2.0f).row();
            }
        }, str -> {
            table2.clear();
            table2.add(wrap(tr("Não foi possível carregar.", "Could not load."))).row();
        });
    }
}
