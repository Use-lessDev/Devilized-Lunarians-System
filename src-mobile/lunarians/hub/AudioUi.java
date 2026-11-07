package lunarians.hub;

import arc.Core;
import arc.Events;
import arc.Graphics;
import arc.Settings;
import arc.audio.Music;
import arc.audio.Sound;
import arc.files.Fi;
import arc.func.Cons;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectMap;
import arc.util.Log;
import arc.util.Timer;
import arc.util.serialization.Base64Coder;
import arc.util.serialization.Jval;
import java.lang.reflect.Field;
import java.util.Iterator;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.core.UI;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.ui.FileChooser;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public class AudioUi {
  private static final String BTN = "lunarians-audio-btn";
  
  private static final String K_MUSIC = "lun-music-id";
  
  private static final String K_MUSIC_FMT = "lun-music-fmt";
  
  private static final String K_SHOT = "lun-shot-id";
  
  private static final String K_SHOT_FMT = "lun-shot-fmt";
  
  private static final int MAX_MUSIC = 921600;
  
  private static final int MAX_SHOT = 122880;
  
  private static final ObjectMap X;
  
  private static Field betaField;
  
  private static UnitType curType;
  
  private static boolean hookFailed;
  
  private static boolean inited;
  
  private static boolean loadingMusic;
  
  private static boolean loadingShot;
  
  private static Music music;
  
  private static int musicLoaded;
  
  private static final ObjectMap orig;
  
  private static Field panelField;
  
  private static Music prevMusic;
  
  private static Sound prevSound;
  
  private static int shotLoaded;
  
  private static Sound shotSnd;
  
  static {
    ObjectMap objectMap = new ObjectMap();
    this();
    orig = objectMap;
    objectMap = new ObjectMap();
    this();
    X = objectMap;
    x("Sons", "Sonidos", "Звуки");
    x("Falha ao baixar o som", "No se pudo descargar el sonido", "Не удалось скачать звук");
    x("Som de tiro (ogg/mp3/wav)", "Sonido de disparo (ogg/mp3/wav)", "Звук выстрела (ogg/mp3/wav)");
    String str = "Música (ogg/mp3/wav)";
    x(str, str, "Музыка (ogg/mp3/wav)");
    x("Arquivo grande demais (máx. ", "Archivo demasiado grande (máx. ", "Файл слишком большой (макс. ");
    x("Som enviado!", "¡Sonido enviado! Espera la aprobación.", "Звук отправлен! Ждите одобрения.");
    x("Falha no envio: ", "Error al enviar: ", "Ошибка отправки: ");
    x("Arquivo inválido", "Archivo inválido", "Неверный файл");
    x("Não foi possível tocar", "No se pudo reproducir", "Не удалось воспроизвести");
    x("Enviar som de tiro", "Subir sonido de disparo", "Загрузить звук выстрела");
    x("Enviar música", "Subir música", "Загрузить музыку");
    x("Nenhum som enviado ainda.", "Aún no has subido sonidos.", "Звуков пока нет.");
    str = "Música";
    x(str, str, "Музыка");
    x("Tiro", "Disparo", "Выстрел");
    x("em uso", "en uso", "используется");
    x("Parar", "Dejar", "Выкл.");
    str = "Usar";
    x(str, str, "Исп.");
    x("Apagar som", "Borrar sonido", "Удалить звук");
    x("Apagar este som do site?", "¿Borrar este sonido del sitio?", "Удалить этот звук с сайта?");
    x("Falha", "Error", "Ошибка");
    x("Não foi possível carregar.", "No se pudo cargar.", "Не удалось загрузить.");
  }
  
  static Fi dir() {
    Fi fi = Core.settings.getDataDirectory();
    String str = "lunarians-audio";
    fi = fi.child(str);
    boolean bool = fi.exists();
    if (!bool)
      fi.mkdirs(); 
    return fi;
  }
  
  static void ensure(int paramInt, String paramString, Cons paramCons) {
    Fi fi2 = dir();
    StringBuilder stringBuilder2 = new StringBuilder();
    this();
    stringBuilder2.append(paramInt);
    String str1 = ".";
    stringBuilder2.append(str1);
    stringBuilder2.append(paramString);
    paramString = stringBuilder2.toString();
    Fi fi1 = fi2.child(paramString);
    boolean bool = fi1.exists();
    if (bool) {
      long l1 = fi1.length();
      long l2 = 0L;
      int i = l1 cmp l2;
      if (i > 0) {
        paramCons.get(fi1);
        return;
      } 
    } 
    StringBuilder stringBuilder1 = new StringBuilder();
    this("/api/audio/get?id=");
    stringBuilder1.append(paramInt);
    String str2 = stringBuilder1.toString();
    AudioUi$12 audioUi$12 = new AudioUi$12();
    this(fi1, paramCons);
    AudioUi$13 audioUi$13 = new AudioUi$13();
    this();
    HubClient.call("GET", str2, null, audioUi$12, audioUi$13);
  }
  
  private static void hookButton() {
    String str = "lunarians-audio-btn";
    boolean bool = hookFailed;
    if (!bool) {
      bool = true;
      try {
        return;
      } finally {
        str = null;
        hookFailed = bool;
        str = str.toString();
        Object[] arrayOfObject = new Object[bool];
        arrayOfObject[0] = str;
        Log.warn("[Lunarians Audio] botao off: @", arrayOfObject);
      } 
    } 
  }
  
  public static void init() {
    boolean bool = inited;
    if (!bool) {
      bool = Vars.headless;
      if (!bool) {
        bool = true;
        inited = bool;
        EventType.Trigger trigger = EventType.Trigger.update;
        AudioUi$18 audioUi$18 = new AudioUi$18();
        this();
        Events.run(trigger, audioUi$18);
        Class<EventType.WorldLoadEvent> clazz = EventType.WorldLoadEvent.class;
        AudioUi$19 audioUi$19 = new AudioUi$19();
        this();
        Events.on(clazz, audioUi$19);
      } 
    } 
  }
  
  public static void open() {
    boolean bool = HubClient.consented();
    if (!bool) {
      HubUi.open();
      return;
    } 
    BaseDialog baseDialog = new BaseDialog();
    String str = tr("Sons", "Sounds");
    this(str);
    baseDialog.addCloseButton();
    AudioUi$10 audioUi$10 = new AudioUi$10();
    this();
    baseDialog.hidden(audioUi$10);
    AudioUi$11 audioUi$11 = new AudioUi$11();
    this(baseDialog);
    baseDialog.shown(audioUi$11);
    baseDialog.show();
  }
  
  public static void pick(String paramString, BaseDialog paramBaseDialog) {
    FileChooser.FileChooserParams fileChooserParams = new FileChooser.FileChooserParams();
    this();
    String str2;
    fileChooserParams = fileChooserParams.open(true);
    String str1 = "shot";
    boolean bool = paramString.equals(str1);
    if (bool) {
      str1 = "Som de tiro (ogg/mp3/wav)";
      str2 = "Shot sound (ogg/mp3/wav)";
    } else {
      str1 = "Música (ogg/mp3/wav)";
      str2 = "Music (ogg/mp3/wav)";
    } 
    str1 = tr(str1, str2);
    fileChooserParams = fileChooserParams.title(str1);
    String[] arrayOfString = { "ogg", "mp3", "wav" };
    fileChooserParams = fileChooserParams.extensions(arrayOfString);
    AudioUi$4 audioUi$4 = new AudioUi$4();
    this(paramString, paramBaseDialog);
    fileChooserParams.submit(audioUi$4);
  }
  
  public static void preview(int paramInt, String paramString, boolean paramBoolean) {
    AudioUi$16 audioUi$16 = new AudioUi$16();
    this(paramBoolean);
    ensure(paramInt, paramString, audioUi$16);
  }
  
  public static void rebuild(BaseDialog paramBaseDialog) {
    paramBaseDialog.cont.clear();
    float f1 = Math.min(Core.scene.getWidth() - 40.0F, 520.0F);
    Table table = paramBaseDialog.cont;
    Label label2 = wrap(HubText.get("audio.help"));
    table.add((Element)label2).width(f1).padBottom(6.0F).row();
    table = paramBaseDialog.cont;
    AudioUi$5 audioUi$5 = new AudioUi$5();
    this(paramBaseDialog);
    table.table(audioUi$5).width(f1).row();
    table = new Table();
    this();
    Cell cell = paramBaseDialog.cont.pane((Element)table).width(f1);
    float f2 = Core.scene.getHeight() * 0.55F;
    cell.maxHeight(f2).row();
    Label label1 = wrap("...");
    table.add((Element)label1).row();
    AudioUi$6 audioUi$6 = new AudioUi$6();
    this(table, f1, paramBaseDialog);
    AudioUi$7 audioUi$7 = new AudioUi$7();
    this(table);
    HubClient.call("GET", "/api/audio/mine", null, audioUi$6, audioUi$7);
  }
  
  private static void restoreWeapons() {
    ObjectMap.Entries entries = orig.entries().iterator();
    while (true) {
      boolean bool = entries.hasNext();
      if (bool) {
        ObjectMap.Entry entry = entries.next();
        Weapon weapon = (Weapon)entry.key;
        Sound sound = (Sound)entry.value;
        weapon.shootSound = sound;
        continue;
      } 
      orig.clear();
      curType = null;
      return;
    } 
  }
  
  public static void stopMusic() {
    try {
      Music music = music;
      if (music != null) {
        boolean bool = music.isPlaying();
        if (bool) {
          music = music;
          music.stop();
        } 
      } 
    } finally {}
  }
  
  private static void stopPreview() {
    try {
      Music music = prevMusic;
      if (music != null)
        music.stop(); 
    } finally {}
  }
  
  private static void tickMusic() {
    stopMusic();
  }
  
  private static void tickShots() {
    Settings settings = Core.settings;
    boolean bool1 = false;
    Player player = null;
    int i = settings.getInt("lun-shot-id", 0);
    boolean bool3 = false;
    String str = null;
    if (i > 0) {
      boolean bool = HubClient.consented();
      if (bool) {
        player = Vars.player;
        if (player != null) {
          GameState gameState = Vars.state;
          if (gameState != null) {
            gameState = Vars.state;
            bool1 = gameState.isGame();
            if (bool1) {
              int j = shotLoaded;
              if (j != i) {
                bool3 = loadingShot;
                if (!bool3) {
                  loadingShot = true;
                  restoreWeapons();
                  str = Core.settings.getString("lun-shot-fmt", "ogg");
                  AudioUi$0 audioUi$0 = new AudioUi$0();
                  this(i);
                  ensure(i, str, audioUi$0);
                  AudioUi$1 audioUi$1 = new AudioUi$1();
                  this();
                  Timer.schedule(audioUi$1, 20.0F);
                  return;
                } 
              } else {
                UnitType unitType;
                Unit unit = Vars.player.unit();
                if (unit != null) {
                  unit = Vars.player.unit();
                  unitType = unit.type;
                } 
                if (unitType != null) {
                  UnitType unitType1 = curType;
                  if (unitType != unitType1) {
                    Sound sound = shotSnd;
                    if (sound != null) {
                      restoreWeapons();
                      curType = unitType;
                      Iterator<Weapon> iterator = unitType.weapons.iterator();
                      while (true) {
                        bool3 = iterator.hasNext();
                        if (bool3) {
                          Weapon weapon = iterator.next();
                          ObjectMap objectMap1 = orig;
                          Sound sound2 = weapon.shootSound;
                          objectMap1.put(weapon, sound2);
                          Sound sound1 = shotSnd;
                          weapon.shootSound = sound1;
                          continue;
                        } 
                        break;
                      } 
                    } 
                  } 
                } 
              } 
            } 
          } 
        } 
        return;
      } 
    } 
    ObjectMap objectMap = orig;
    boolean bool2 = objectMap.isEmpty();
    if (!bool2)
      restoreWeapons(); 
    shotSnd = null;
    shotLoaded = 0;
  }
  
  static String tr(String paramString1, String paramString2) {
    // Byte code:
    //   0: invokestatic getLanguage : ()Ljava/lang/String;
    //   3: astore_2
    //   4: ldc_w 'pt'
    //   7: astore_3
    //   8: aload_3
    //   9: aload_2
    //   10: invokevirtual equals : (Ljava/lang/Object;)Z
    //   13: istore #4
    //   15: iload #4
    //   17: ifeq -> 22
    //   20: aload_0
    //   21: areturn
    //   22: ldc_w 'es'
    //   25: astore_3
    //   26: aload_3
    //   27: aload_2
    //   28: invokevirtual equals : (Ljava/lang/Object;)Z
    //   31: istore #5
    //   33: iload #5
    //   35: ifne -> 56
    //   38: ldc_w 'ru'
    //   41: astore #6
    //   43: aload #6
    //   45: aload_2
    //   46: invokevirtual equals : (Ljava/lang/Object;)Z
    //   49: istore #5
    //   51: iload #5
    //   53: ifeq -> 89
    //   56: getstatic lunarians/hub/AudioUi.X : Larc/struct/ObjectMap;
    //   59: astore #6
    //   61: aload #6
    //   63: aload_0
    //   64: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   67: checkcast [Ljava/lang/String;
    //   70: astore_0
    //   71: aload_0
    //   72: ifnull -> 89
    //   75: aload_3
    //   76: aload_2
    //   77: invokevirtual equals : (Ljava/lang/Object;)Z
    //   80: iconst_1
    //   81: ixor
    //   82: istore #7
    //   84: aload_0
    //   85: iload #7
    //   87: aaload
    //   88: areturn
    //   89: aload_1
    //   90: areturn
  }
  
  private static Label wrap(String paramString) {
    Label label = new Label();
    this(paramString);
    label.setWrap(true);
    label.setAlignment(8);
    return label;
  }
  
  private static void x(String paramString1, String paramString2, String paramString3) {
    ObjectMap objectMap = X;
    String[] arrayOfString = { paramString2, paramString3 };
    objectMap.put(paramString1, arrayOfString);
  }
}
