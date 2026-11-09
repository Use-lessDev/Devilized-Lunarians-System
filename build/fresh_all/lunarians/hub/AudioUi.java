/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.Events
 *  arc.audio.Music
 *  arc.audio.Sound
 *  arc.files.Fi
 *  arc.func.Cons
 *  arc.scene.Element
 *  arc.scene.style.Drawable
 *  arc.scene.ui.Label
 *  arc.scene.ui.TextButton
 *  arc.scene.ui.layout.Table
 *  arc.struct.ObjectMap
 *  arc.struct.ObjectMap$Entry
 *  arc.util.Log
 *  arc.util.Timer
 *  arc.util.serialization.Base64Coder
 *  arc.util.serialization.Jval
 *  arc.util.serialization.Jval$JsonArray
 *  mindustry.Vars
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.EventType$WorldLoadEvent
 *  mindustry.gen.Icon
 *  mindustry.type.UnitType
 *  mindustry.type.Weapon
 *  mindustry.ui.FileChooser$FileChooserParams
 *  mindustry.ui.Styles
 *  mindustry.ui.dialogs.BaseDialog
 */
package lunarians.hub;

import arc.Core;
import arc.Events;
import arc.audio.Music;
import arc.audio.Sound;
import arc.files.Fi;
import arc.func.Cons;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.ui.Label;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectMap;
import arc.util.Log;
import arc.util.Timer;
import arc.util.serialization.Base64Coder;
import arc.util.serialization.Jval;
import java.lang.reflect.Field;
import lunarians.LunariansI18n;
import lunarians.hub.HubClient;
import lunarians.hub.HubText;
import lunarians.hub.HubUi;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.ui.FileChooser;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

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
    private static final ObjectMap<Weapon, Sound> orig;
    private static Music music;
    private static int musicLoaded;
    private static Sound prevSound;
    private static Music prevMusic;
    private static final ObjectMap<String, String[]> X;

    private static void x(String string, String string2, String string3) {
        X.put((Object)string, (Object)new String[]{string2, string3});
    }

    static String tr(String string, String string2) {
        String[] stringArray;
        String string3 = LunariansI18n.getLanguage();
        if ("pt".equals(string3)) {
            return string;
        }
        if (("es".equals(string3) || "ru".equals(string3)) && (stringArray = (String[])X.get((Object)string)) != null) {
            return stringArray["es".equals(string3) ? 0 : 1];
        }
        return string2;
    }

    static Fi dir() {
        Fi fi = Core.settings.getDataDirectory().child("lunarians-audio");
        if (!fi.exists()) {
            fi.mkdirs();
        }
        return fi;
    }

    public static void init() {
        if (inited || Vars.headless) {
            return;
        }
        inited = true;
        Events.run((Object)EventType.Trigger.update, () -> {
            try {
                if (Core.graphics.getFrameId() % 30L == 0L) {
                    AudioUi.hookButton();
                    AudioUi.tickShots();
                    AudioUi.tickMusic();
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        });
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> AudioUi.stopMusic());
    }

    private static void hookButton() {
        if (hookFailed) {
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
            if ((table = (Table)panelField.get(object)) == null || table.find(BTN) != null) {
                return;
            }
            TextButton textButton = new TextButton(AudioUi.tr("Sons", "Sounds"), Styles.defaultt);
            textButton.name = BTN;
            textButton.clicked(AudioUi::open);
            table.row();
            table.add((Element)textButton).growX().height(44.0f).pad(2.0f);
        }
        catch (Throwable throwable) {
            hookFailed = true;
            Log.warn((String)"[Lunarians Audio] botao off: @", (Object[])new Object[]{throwable.toString()});
        }
    }

    static void ensure(int n, String string2, Cons<Fi> cons) {
        Fi fi = AudioUi.dir().child(n + "." + string2);
        if (!fi.exists() || fi.length() <= 0L) {
            HubClient.call("GET", "/api/audio/get?id=" + n, null, (Cons<Jval>)((Cons)jval -> {
                try {
                    byte[] byArray = Base64Coder.decode((String)jval.getString("data", ""));
                    if (byArray.length < 64) {
                        throw new RuntimeException("vazio");
                    }
                    fi.writeBytes(byArray, false);
                    cons.get((Object)fi);
                }
                catch (Throwable throwable) {
                    Vars.ui.showInfoToast(AudioUi.tr("Falha ao baixar o som", "Could not download sound"), 2.5f);
                }
            }), (Cons<String>)((Cons)string -> Vars.ui.showInfoToast(AudioUi.tr("Falha ao baixar o som", "Could not download sound"), 2.5f)));
        } else {
            cons.get((Object)fi);
        }
    }

    private static void restoreWeapons() {
        for (ObjectMap.Entry entry : orig.entries()) {
            ((Weapon)entry.key).shootSound = (Sound)entry.value;
        }
        orig.clear();
        curType = null;
    }

    private static void tickShots() {
        UnitType unitType;
        int n = Core.settings.getInt(K_SHOT, 0);
        if (n <= 0 || !HubClient.consented()) {
            if (!orig.isEmpty()) {
                AudioUi.restoreWeapons();
            }
            shotSnd = null;
            shotLoaded = 0;
            return;
        }
        if (Vars.player == null || Vars.state == null || !Vars.state.isGame()) {
            return;
        }
        if (shotLoaded != n) {
            if (!loadingShot) {
                loadingShot = true;
                AudioUi.restoreWeapons();
                AudioUi.ensure(n, Core.settings.getString(K_SHOT_FMT, "ogg"), (Cons<Fi>)((Cons)fi -> {
                    try {
                        shotSnd = new Sound(fi);
                        shotLoaded = n;
                    }
                    catch (Throwable throwable) {
                        Log.warn((String)"[Lunarians Audio] tiro: @", (Object[])new Object[]{throwable.toString()});
                    }
                    loadingShot = false;
                }));
                Timer.schedule(() -> {
                    loadingShot = false;
                }, (float)20.0f);
                return;
            }
            return;
        }
        UnitType unitType2 = unitType = Vars.player.unit() == null ? null : Vars.player.unit().type;
        if (unitType != null && unitType != curType && shotSnd != null) {
            AudioUi.restoreWeapons();
            curType = unitType;
            for (Weapon weapon : unitType.weapons) {
                orig.put((Object)weapon, (Object)weapon.shootSound);
                weapon.shootSound = shotSnd;
            }
        }
    }

    public static void stopMusic() {
        try {
            if (music != null && music.isPlaying()) {
                music.stop();
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static void tickMusic() {
        AudioUi.stopMusic();
    }

    private static Label wrap(String string) {
        Label label = new Label((CharSequence)string);
        label.setWrap(true);
        label.setAlignment(8);
        return label;
    }

    public static void open() {
        if (!HubClient.consented()) {
            HubUi.open();
            return;
        }
        BaseDialog baseDialog = new BaseDialog(AudioUi.tr("Sons", "Sounds"));
        baseDialog.addCloseButton();
        baseDialog.hidden(AudioUi::stopPreview);
        baseDialog.shown(() -> AudioUi.rebuild(baseDialog));
        baseDialog.show();
    }

    private static void stopPreview() {
        try {
            if (prevMusic != null) {
                prevMusic.stop();
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static void pick(String string, BaseDialog baseDialog) {
        new FileChooser.FileChooserParams().open(true).title(string.equals("shot") ? AudioUi.tr("Som de tiro (ogg/mp3/wav)", "Shot sound (ogg/mp3/wav)") : AudioUi.tr("M\u00fasica (ogg/mp3/wav)", "Music (ogg/mp3/wav)")).extensions(new String[]{"ogg", "mp3", "wav"}).submit(fi -> {
            try {
                int n;
                byte[] byArray = fi.readBytes();
                int n2 = n = string.equals("shot") ? 122880 : 921600;
                if (byArray.length > n) {
                    Vars.ui.showInfoToast(AudioUi.tr("Arquivo grande demais (m\u00e1x. ", "File too large (max ") + n / 1024 + " KB)", 3.0f);
                    return;
                }
                Jval jval2 = Jval.newObject();
                String string3 = fi.nameWithoutExtension();
                jval2.add("name", string3.length() > 32 ? string3.substring(0, 32) : string3);
                jval2.add("kind", string);
                jval2.add("data", new String(Base64Coder.encode((byte[])byArray)));
                HubClient.call("POST", "/api/audio/upload", jval2, (Cons<Jval>)((Cons)jval -> {
                    Vars.ui.showInfoToast(AudioUi.tr("Som enviado!", "Sound uploaded! Waiting for admin approval."), 2.0f);
                    AudioUi.rebuild(baseDialog);
                }), (Cons<String>)((Cons)string -> Vars.ui.showInfoToast(AudioUi.tr("Falha no envio: ", "Upload failed: ") + string, 3.0f)));
            }
            catch (Throwable throwable) {
                Vars.ui.showInfoToast(AudioUi.tr("Arquivo inv\u00e1lido", "Invalid file"), 3.0f);
            }
        });
    }

    public static void preview(int n, String string, boolean bl) {
        AudioUi.ensure(n, string, (Cons<Fi>)((Cons)fi -> {
            try {
                AudioUi.stopPreview();
                if (bl) {
                    prevMusic = new Music(fi);
                    prevMusic.setVolume((float)Core.settings.getInt("musicvol", 100) / 100.0f);
                    prevMusic.play();
                } else {
                    if (prevSound != null) {
                        prevSound.dispose();
                    }
                    prevSound = new Sound(fi);
                    prevSound.play((float)Core.settings.getInt("sfxvol", 100) / 100.0f);
                }
            }
            catch (Throwable throwable) {
                Vars.ui.showInfoToast(AudioUi.tr("N\u00e3o foi poss\u00edvel tocar", "Could not play"), 2.5f);
            }
        }));
    }

    public static void rebuild(BaseDialog baseDialog) {
        baseDialog.cont.clear();
        float f = Math.min(Core.scene.getWidth() - 40.0f, 520.0f);
        baseDialog.cont.add((Element)AudioUi.wrap(HubText.get("audio.help"))).width(f).padBottom(6.0f).row();
        baseDialog.cont.table(table -> {
            table.defaults().height(48.0f).growX().pad(2.0f);
            table.button(AudioUi.tr("Enviar som de tiro", "Upload shot sound"), (Drawable)Icon.upload, () -> AudioUi.pick("shot", baseDialog));
            table.button(AudioUi.tr("Enviar m\u00fasica", "Upload music"), (Drawable)Icon.upload, () -> AudioUi.pick("music", baseDialog));
        }).width(f).row();
        Table table2 = new Table();
        baseDialog.cont.pane((Element)table2).width(f).maxHeight(Core.scene.getHeight() * 0.55f).row();
        table2.add((Element)AudioUi.wrap("...")).row();
        HubClient.call("GET", "/api/audio/mine", null, (Cons<Jval>)((Cons)jval -> {
            table2.clear();
            Jval.JsonArray jsonArray = jval.get("audio").asArray();
            if (jsonArray.size == 0) {
                table2.add((Element)AudioUi.wrap(AudioUi.tr("Nenhum som enviado ainda.", "No sounds uploaded yet."))).width(f - 20.0f).pad(6.0f).row();
                return;
            }
            for (Jval jval2 : jsonArray) {
                boolean bl;
                int n = jval2.getInt("id", 0);
                String string = jval2.getString("kind", "shot");
                String string2 = jval2.getString("fmt", "ogg");
                String string3 = jval2.getString("status", "pending");
                String string4 = jval2.getString("note", "");
                boolean bl2 = string.equals("music");
                boolean bl3 = string3.equals("approved");
                boolean bl4 = bl = !bl2 && Core.settings.getInt(K_SHOT, 0) == n;
                String string5 = bl3 ? "[lime]" : (string3.equals("rejected") ? "[scarlet]" : "[gray]");
                String string6 = jval2.getString("name", "?") + "\n" + (bl2 ? AudioUi.tr("M\u00fasica", "Music") : AudioUi.tr("Tiro", "Shot")) + " \u2022 " + jval2.getInt("size", 0) / 1024 + " KB" + (bl ? "  [" + AudioUi.tr("em uso", "in use") + "]" : "") + "\n" + string5 + HubText.get("skin.status." + string3) + "[]" + (string3.equals("rejected") && !string4.isEmpty() ? " - " + string4 : "");
                table2.table(Styles.grayPanel, table -> {
                    table.add((Element)AudioUi.wrap(string6)).growX().pad(6.0f);
                    table.button((Drawable)Icon.play, Styles.cleari, () -> AudioUi.preview(n, string2, bl2)).size(38.0f);
                    if (!bl2 && bl3) {
                        table.button(bl ? AudioUi.tr("Parar", "Stop") : AudioUi.tr("Usar", "Use"), () -> {
                            if (bl) {
                                Core.settings.put(K_SHOT, (Object)0);
                            } else {
                                Core.settings.put(K_SHOT, (Object)n);
                                Core.settings.put(K_SHOT_FMT, (Object)string2);
                            }
                            AudioUi.rebuild(baseDialog);
                        }).size(78.0f, 38.0f);
                    }
                    table.button((Drawable)Icon.trash, Styles.cleari, () -> Vars.ui.showConfirm(AudioUi.tr("Apagar som", "Delete sound"), AudioUi.tr("Apagar este som do site?", "Delete this sound from the site?"), () -> {
                        if (bl) {
                            Core.settings.put(K_SHOT, (Object)0);
                        }
                        Jval jval2 = Jval.newObject();
                        jval2.add("id", Jval.valueOf((int)n));
                        AudioUi.dir().child(n + "." + string2).delete();
                        HubClient.call("POST", "/api/audio/delete", jval2, (Cons<Jval>)((Cons)jval -> AudioUi.rebuild(baseDialog)), (Cons<String>)((Cons)string -> Vars.ui.showInfoToast(AudioUi.tr("Falha", "Failed"), 2.0f)));
                    })).size(38.0f);
                }).width(f - 10.0f).pad(2.0f).row();
            }
        }), (Cons<String>)((Cons)string -> {
            table2.clear();
            table2.add((Element)AudioUi.wrap(AudioUi.tr("N\u00e3o foi poss\u00edvel carregar.", "Could not load."))).row();
        }));
    }

    static {
        orig = new ObjectMap();
        X = new ObjectMap();
        AudioUi.x("Sons", "Sonidos", "\u0417\u0432\u0443\u043a\u0438");
        AudioUi.x("Falha ao baixar o som", "No se pudo descargar el sonido", "\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0441\u043a\u0430\u0447\u0430\u0442\u044c \u0437\u0432\u0443\u043a");
        AudioUi.x("Som de tiro (ogg/mp3/wav)", "Sonido de disparo (ogg/mp3/wav)", "\u0417\u0432\u0443\u043a \u0432\u044b\u0441\u0442\u0440\u0435\u043b\u0430 (ogg/mp3/wav)");
        AudioUi.x("M\u00fasica (ogg/mp3/wav)", "M\u00fasica (ogg/mp3/wav)", "\u041c\u0443\u0437\u044b\u043a\u0430 (ogg/mp3/wav)");
        AudioUi.x("Arquivo grande demais (m\u00e1x. ", "Archivo demasiado grande (m\u00e1x. ", "\u0424\u0430\u0439\u043b \u0441\u043b\u0438\u0448\u043a\u043e\u043c \u0431\u043e\u043b\u044c\u0448\u043e\u0439 (\u043c\u0430\u043a\u0441. ");
        AudioUi.x("Som enviado!", "\u00a1Sonido enviado! Espera la aprobaci\u00f3n.", "\u0417\u0432\u0443\u043a \u043e\u0442\u043f\u0440\u0430\u0432\u043b\u0435\u043d! \u0416\u0434\u0438\u0442\u0435 \u043e\u0434\u043e\u0431\u0440\u0435\u043d\u0438\u044f.");
        AudioUi.x("Falha no envio: ", "Error al enviar: ", "\u041e\u0448\u0438\u0431\u043a\u0430 \u043e\u0442\u043f\u0440\u0430\u0432\u043a\u0438: ");
        AudioUi.x("Arquivo inv\u00e1lido", "Archivo inv\u00e1lido", "\u041d\u0435\u0432\u0435\u0440\u043d\u044b\u0439 \u0444\u0430\u0439\u043b");
        AudioUi.x("N\u00e3o foi poss\u00edvel tocar", "No se pudo reproducir", "\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0432\u043e\u0441\u043f\u0440\u043e\u0438\u0437\u0432\u0435\u0441\u0442\u0438");
        AudioUi.x("Enviar som de tiro", "Subir sonido de disparo", "\u0417\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u044c \u0437\u0432\u0443\u043a \u0432\u044b\u0441\u0442\u0440\u0435\u043b\u0430");
        AudioUi.x("Enviar m\u00fasica", "Subir m\u00fasica", "\u0417\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u044c \u043c\u0443\u0437\u044b\u043a\u0443");
        AudioUi.x("Nenhum som enviado ainda.", "A\u00fan no has subido sonidos.", "\u0417\u0432\u0443\u043a\u043e\u0432 \u043f\u043e\u043a\u0430 \u043d\u0435\u0442.");
        AudioUi.x("M\u00fasica", "M\u00fasica", "\u041c\u0443\u0437\u044b\u043a\u0430");
        AudioUi.x("Tiro", "Disparo", "\u0412\u044b\u0441\u0442\u0440\u0435\u043b");
        AudioUi.x("em uso", "en uso", "\u0438\u0441\u043f\u043e\u043b\u044c\u0437\u0443\u0435\u0442\u0441\u044f");
        AudioUi.x("Parar", "Dejar", "\u0412\u044b\u043a\u043b.");
        AudioUi.x("Usar", "Usar", "\u0418\u0441\u043f.");
        AudioUi.x("Apagar som", "Borrar sonido", "\u0423\u0434\u0430\u043b\u0438\u0442\u044c \u0437\u0432\u0443\u043a");
        AudioUi.x("Apagar este som do site?", "\u00bfBorrar este sonido del sitio?", "\u0423\u0434\u0430\u043b\u0438\u0442\u044c \u044d\u0442\u043e\u0442 \u0437\u0432\u0443\u043a \u0441 \u0441\u0430\u0439\u0442\u0430?");
        AudioUi.x("Falha", "Error", "\u041e\u0448\u0438\u0431\u043a\u0430");
        AudioUi.x("N\u00e3o foi poss\u00edvel carregar.", "No se pudo cargar.", "\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0437\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u044c.");
    }
}

