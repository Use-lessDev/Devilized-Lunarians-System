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
  
  private static final ObjectMap<Weapon, Sound> orig = new ObjectMap();
  
  private static Music music;
  
  private static int musicLoaded;
  
  private static Sound prevSound;
  
  private static Music prevMusic;
  
  private static final ObjectMap<String, String[]> X = new ObjectMap();
  
  private static void x(String paramString1, String paramString2, String paramString3) {
    X.put(paramString1, new String[] { paramString2, paramString3 });
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
  
  static String tr(String paramString1, String paramString2) {
    String str = LunariansI18n.getLanguage();
    if ("pt".equals(str))
      return paramString1; 
    if ("es".equals(str) || "ru".equals(str)) {
      String[] arrayOfString = (String[])X.get(paramString1);
      if (arrayOfString != null)
        return arrayOfString["es".equals(str) ? 0 : 1]; 
    } 
    return paramString2;
  }
  
  static Fi dir() {
    Fi fi = Core.settings.getDataDirectory().child("lunarians-audio");
    if (!fi.exists())
      fi.mkdirs(); 
    return fi;
  }
  
  public static void init() {
    if (inited || Vars.headless)
      return; 
    inited = true;
    Events.run(EventType.Trigger.update, () -> {
          try {
            if (Core.graphics.getFrameId() % 30L == 0L) {
              hookButton();
              tickShots();
              tickMusic();
            } 
          } catch (Throwable throwable) {}
        });
    Events.on(EventType.WorldLoadEvent.class, paramWorldLoadEvent -> stopMusic());
  }
  
  private static void hookButton() {
    if (hookFailed)
      return; 
    try {
      if (betaField == null)
        betaField = Class.forName("lunarians.LunariansMod").getField("betaHud"); 
      Object object = betaField.get(null);
      if (object == null)
        return; 
      if (panelField == null) {
        panelField = object.getClass().getDeclaredField("panel");
        panelField.setAccessible(true);
      } 
      Table table = (Table)panelField.get(object);
      if (table == null || table.find("lunarians-audio-btn") != null)
        return; 
      TextButton textButton = new TextButton(tr("Sons", "Sounds"), Styles.defaultt);
      textButton.name = "lunarians-audio-btn";
      textButton.clicked(AudioUi::open);
      table.row();
      table.add((Element)textButton).growX().height(44.0F).pad(2.0F);
    } catch (Throwable throwable) {
      hookFailed = true;
      Log.warn("[Lunarians Audio] botao off: @", new Object[] { throwable.toString() });
    } 
  }
  
  static void ensure(int paramInt, String paramString, Cons<Fi> paramCons) {
    Fi fi = dir().child(paramInt + "." + paramString);
    if (!fi.exists() || fi.length() <= 0L) {
      HubClient.call("GET", "/api/audio/get?id=" + paramInt, null, paramJval -> {
            try {
              byte[] arrayOfByte = Base64Coder.decode(paramJval.getString("data", ""));
              if (arrayOfByte.length < 64)
                throw new RuntimeException("vazio"); 
              paramFi.writeBytes(arrayOfByte, false);
              paramCons.get(paramFi);
            } catch (Throwable throwable) {
              Vars.ui.showInfoToast(tr("Falha ao baixar o som", "Could not download sound"), 2.5F);
            } 
          }paramString -> Vars.ui.showInfoToast(tr("Falha ao baixar o som", "Could not download sound"), 2.5F));
    } else {
      paramCons.get(fi);
    } 
  }
  
  private static void restoreWeapons() {
    ObjectMap.Entries entries = orig.entries().iterator();
    while (entries.hasNext()) {
      ObjectMap.Entry entry = entries.next();
      ((Weapon)entry.key).shootSound = (Sound)entry.value;
    } 
    orig.clear();
    curType = null;
  }
  
  private static void tickShots() {
    int i = Core.settings.getInt("lun-shot-id", 0);
    if (i <= 0 || !HubClient.consented()) {
      if (!orig.isEmpty())
        restoreWeapons(); 
      shotSnd = null;
      shotLoaded = 0;
      return;
    } 
    if (Vars.player == null || Vars.state == null || !Vars.state.isGame())
      return; 
    if (shotLoaded != i) {
      if (!loadingShot) {
        loadingShot = true;
        restoreWeapons();
        ensure(i, Core.settings.getString("lun-shot-fmt", "ogg"), paramFi -> {
              try {
                shotSnd = new Sound(paramFi);
                shotLoaded = paramInt;
              } catch (Throwable throwable) {
                Log.warn("[Lunarians Audio] tiro: @", new Object[] { throwable.toString() });
              } 
              loadingShot = false;
            });
        Timer.schedule(() -> loadingShot = false, 20.0F);
        return;
      } 
      return;
    } 
    UnitType unitType = (Vars.player.unit() == null) ? null : (Vars.player.unit()).type;
    if (unitType != null && unitType != curType && shotSnd != null) {
      restoreWeapons();
      curType = unitType;
      Iterator<Weapon> iterator = unitType.weapons.iterator();
      while (iterator.hasNext()) {
        Weapon weapon = iterator.next();
        orig.put(weapon, weapon.shootSound);
        weapon.shootSound = shotSnd;
      } 
    } 
  }
  
  public static void stopMusic() {
    try {
      if (music != null && music.isPlaying())
        music.stop(); 
    } catch (Throwable throwable) {}
  }
  
  private static void tickMusic() {
    stopMusic();
  }
  
  private static Label wrap(String paramString) {
    Label label = new Label(paramString);
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
    baseDialog.shown(() -> rebuild(paramBaseDialog));
    baseDialog.show();
  }
  
  private static void stopPreview() {
    try {
      if (prevMusic != null)
        prevMusic.stop(); 
    } catch (Throwable throwable) {}
  }
  
  public static void pick(String paramString, BaseDialog paramBaseDialog) {
    (new FileChooser.FileChooserParams()).open(true).title(paramString.equals("shot") ? tr("Som de tiro (ogg/mp3/wav)", "Shot sound (ogg/mp3/wav)") : tr("Música (ogg/mp3/wav)", "Music (ogg/mp3/wav)")).extensions(new String[] { "ogg", "mp3", "wav" }).submit(paramFi -> {
          try {
            byte[] arrayOfByte = paramFi.readBytes();
            int i = paramString.equals("shot") ? 122880 : 921600;
            if (arrayOfByte.length > i) {
              Vars.ui.showInfoToast(tr("Arquivo grande demais (máx. ", "File too large (max ") + (i / 1024) + " KB)", 3.0F);
              return;
            } 
            Jval jval = Jval.newObject();
            String str = paramFi.nameWithoutExtension();
            jval.add("name", (str.length() > 32) ? str.substring(0, 32) : str);
            jval.add("kind", paramString);
            jval.add("data", new String(Base64Coder.encode(arrayOfByte)));
            HubClient.call("POST", "/api/audio/upload", jval, (), ());
          } catch (Throwable throwable) {
            Vars.ui.showInfoToast(tr("Arquivo inválido", "Invalid file"), 3.0F);
          } 
        });
  }
  
  public static void preview(int paramInt, String paramString, boolean paramBoolean) {
    ensure(paramInt, paramString, paramFi -> {
          try {
            stopPreview();
            if (paramBoolean) {
              prevMusic = new Music(paramFi);
              prevMusic.setVolume(Core.settings.getInt("musicvol", 100) / 100.0F);
              prevMusic.play();
            } else {
              if (prevSound != null)
                prevSound.dispose(); 
              prevSound = new Sound(paramFi);
              prevSound.play(Core.settings.getInt("sfxvol", 100) / 100.0F);
            } 
          } catch (Throwable throwable) {
            Vars.ui.showInfoToast(tr("Não foi possível tocar", "Could not play"), 2.5F);
          } 
        });
  }
  
  public static void rebuild(BaseDialog paramBaseDialog) {
    paramBaseDialog.cont.clear();
    float f = Math.min(Core.scene.getWidth() - 40.0F, 520.0F);
    paramBaseDialog.cont.add((Element)wrap(HubText.get("audio.help"))).width(f).padBottom(6.0F).row();
    paramBaseDialog.cont.table(paramTable -> {
          paramTable.defaults().height(48.0F).growX().pad(2.0F);
          paramTable.button(tr("Enviar som de tiro", "Upload shot sound"), (Drawable)Icon.upload, ());
          paramTable.button(tr("Enviar música", "Upload music"), (Drawable)Icon.upload, ());
        }).width(f).row();
    Table table = new Table();
    paramBaseDialog.cont.pane((Element)table).width(f).maxHeight(Core.scene.getHeight() * 0.55F).row();
    table.add((Element)wrap("...")).row();
    HubClient.call("GET", "/api/audio/mine", null, paramJval -> {
          paramTable.clear();
          Jval.JsonArray jsonArray = paramJval.get("audio").asArray();
          if (jsonArray.size == 0) {
            paramTable.add((Element)wrap(tr("Nenhum som enviado ainda.", "No sounds uploaded yet."))).width(paramFloat - 20.0F).pad(6.0F).row();
            return;
          } 
          for (Jval jval : jsonArray) {
            int i = jval.getInt("id", 0);
            String str1 = jval.getString("kind", "shot");
            String str2 = jval.getString("fmt", "ogg");
            String str3 = jval.getString("status", "pending");
            String str4 = jval.getString("note", "");
            boolean bool1 = str1.equals("music");
            boolean bool2 = str3.equals("approved");
            boolean bool = (!bool1 && Core.settings.getInt("lun-shot-id", 0) == i) ? true : false;
            String str5 = bool2 ? "[lime]" : (str3.equals("rejected") ? "[scarlet]" : "[gray]");
            String str6 = jval.getString("name", "?") + "\n" + (bool1 ? tr("Música", "Music") : tr("Tiro", "Shot")) + " • " + (jval.getInt("size", 0) / 1024) + " KB" + (bool ? ("  [" + tr("em uso", "in use") + "]") : "") + "\n" + str5 + HubText.get("skin.status." + str3) + "[]" + ((str3.equals("rejected") && !str4.isEmpty()) ? (" - " + str4) : "");
            paramTable.table(Styles.grayPanel, ()).width(paramFloat - 10.0F).pad(2.0F).row();
          } 
        }paramString -> {
          paramTable.clear();
          paramTable.add((Element)wrap(tr("Não foi possível carregar.", "Could not load."))).row();
        });
  }
}
