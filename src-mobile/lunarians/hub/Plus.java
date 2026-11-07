package lunarians.hub;

import arc.Core;
import arc.Events;
import arc.Settings;
import arc.files.Fi;
import arc.func.Cons;
import arc.graphics.Pixmap;
import arc.graphics.PixmapIO;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Image;
import arc.scene.ui.Label;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Cell;
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
import mindustry.core.UI;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.ui.FileChooser;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.ui.fragments.MenuFragment;

public final class Plus {
  private static final String[] FLAGS = new String[] { 
      "enableEnemyCoreAlert", "enableAutoFill", "enableFreeCam", "enableProductionAlert", "enableCamFollow", "enableLossWarn", "enableAutoUnit", "enableAutoCargo", "enableAutoContainer", "enableAutoDefence", 
      "enableAutoShield", "enableAutoHeal", "enableAutoAttack", "enableAutoUnitHeal", "enableAutoMoon", "enableAutoConnectConveyor", "enableAutoDrill", "enableAutoDrillConveyor", "dockAuto", "pvpMode" };
  
  private static final String K_G = "lunarians-hub-gchat-after";
  
  private static boolean betaFailed;
  
  private static Field betaField;
  
  private static final ObjectMap forced;
  
  private static boolean globalOpen;
  
  public static volatile int gnew = 0;
  
  private static final ObjectMap imgCache;
  
  private static boolean inited;
  
  private static int lastGnew = 0;
  
  private static final ObjectMap orig;
  
  private static Field panelField;
  
  static {
    ObjectMap objectMap = new ObjectMap();
    this();
    orig = objectMap;
    objectMap = new ObjectMap();
    this();
    forced = objectMap;
    objectMap = new ObjectMap();
    this();
    imgCache = objectMap;
  }
  
  private static void addMsg(Table paramTable, Jval paramJval, boolean paramBoolean, String paramString) {
    String str1 = "mine";
    boolean bool = paramJval.getBool(str1, false);
    if (bool) {
      String str = "Voce";
      paramString = tr(str, "You");
    } else if (paramBoolean) {
      String str = "name";
      paramString = paramJval.getString(str, "?");
    } 
    String str3 = paramJval.getString("text", "");
    StringBuilder stringBuilder = new StringBuilder();
    this();
    stringBuilder.append(paramString);
    paramString = ": ";
    stringBuilder.append(paramString);
    stringBuilder.append(str3);
    Label label = wrap(stringBuilder.toString());
    Cell cell = paramTable.add((Element)label);
    float f1 = width();
    int i = 1101004800;
    float f2 = 20.0F;
    f1 -= f2;
    cell.width(f1).left().row();
    String str2 = "img";
    int j = paramJval.getInt(str2, 0);
    if (j > 0) {
      Image image = new Image();
      this();
      Scaling scaling = Scaling.fit;
      image.setScaling(scaling);
      Plus$21 plus$21 = new Plus$21();
      this(j);
      image.clicked(plus$21);
      loadInto(image, j);
      Cell cell1 = paramTable.add((Element)image);
      j = 1126170624;
      float f = 160.0F;
      cell1 = cell1.size(f).left();
      cell1.row();
    } 
  }
  
  private static void enforce() {
    ObjectMap objectMap = forced;
    int i = objectMap.size;
    if (i != 0) {
      Iterator<ObjectMap.Entry> iterator = objectMap.entries().iterator();
      while (true) {
        boolean bool = iterator.hasNext();
        if (bool) {
          ObjectMap.Entry entry = iterator.next();
          Boolean bool1 = getFlag((String)entry.key);
          if (bool1 != null) {
            boolean bool2 = bool1.booleanValue();
            Boolean bool3 = (Boolean)entry.value;
            boolean bool4 = bool3.booleanValue();
            if (bool2 != bool4) {
              String str = (String)entry.key;
              Boolean bool5 = (Boolean)entry.value;
              bool = bool5.booleanValue();
              setFlag(str, bool);
            } 
          } 
          continue;
        } 
        break;
      } 
    } 
  }
  
  private static Boolean getFlag(String paramString) {
    Class<LunariansSettings> clazz = LunariansSettings.class;
    try {
      Field field = clazz.getField(paramString);
      return Boolean.valueOf(bool);
    } finally {
      null;
    } 
  }
  
  private static void hookBeta() {
    EventType.Trigger trigger = EventType.Trigger.update;
    Plus$8 plus$8 = new Plus$8();
    this();
    Events.run(trigger, plus$8);
  }
  
  public static void init() {
    boolean bool = inited;
    if (!bool) {
      bool = Vars.headless;
      if (!bool) {
        bool = true;
        inited = bool;
        Room.init();
        try {
          UI uI = Vars.ui;
          MenuFragment menuFragment = uI.menufrag;
          String str1 = "Chat global";
          String str2 = "Global chat";
          str1 = tr(str1, str2);
          TextureRegionDrawable textureRegionDrawable = Icon.chat;
          Plus$0 plus$0 = new Plus$0();
          this();
          menuFragment.addButton(str1, (Drawable)textureRegionDrawable, plus$0);
        } finally {}
        hookBeta();
        Plus$2 plus$2 = new Plus$2();
        this();
        Timer.schedule(plus$2, 8.0F, 30.0F);
        Plus$3 plus$3 = new Plus$3();
        this();
        float f1 = 3.0F;
        float f2 = 2.0F;
        Timer.schedule(plus$3, f1, f2);
      } 
    } 
  }
  
  private static void loadInto(Image paramImage, int paramInt) {
    ObjectMap objectMap = imgCache;
    Integer integer = Integer.valueOf(paramInt);
    TextureRegionDrawable textureRegionDrawable = (TextureRegionDrawable)objectMap.get(integer);
    if (textureRegionDrawable != null) {
      paramImage.setDrawable((Drawable)textureRegionDrawable);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    this("/api/chat/img?id=");
    stringBuilder.append(paramInt);
    String str = stringBuilder.toString();
    Plus$11 plus$11 = new Plus$11();
    this(paramInt, paramImage);
    Plus$12 plus$12 = new Plus$12();
    this();
    HubClient.call("GET", str, null, plus$11, plus$12);
  }
  
  public static void openChat(String paramString1, String paramString2) {
    boolean bool1 = true;
    Table table1 = null;
    if (paramString2 == null) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    BaseDialog baseDialog = new BaseDialog();
    if (bool2) {
      String str = "Global chat";
      str1 = tr("Chat global", str);
    } else {
      StringBuilder stringBuilder = new StringBuilder();
      this();
      String str = HubText.get("chat");
      stringBuilder.append(str);
      str = ": ";
      stringBuilder.append(str);
      stringBuilder.append(paramString1);
      str1 = stringBuilder.toString();
    } 
    this(str1);
    boolean bool2;
    String str1;
    UiStyle.register(baseDialog);
    Table table2 = new Table();
    this();
    int[] arrayOfInt = { 0 };
    boolean[] arrayOfBoolean = new boolean[bool1];
    arrayOfBoolean[0] = false;
    Runnable[] arrayOfRunnable = new Runnable[bool1];
    Plus$13 plus$13 = new Plus$13();
    String str2 = paramString2;
    this(arrayOfBoolean, bool2, arrayOfInt, paramString2, table2, paramString1);
    arrayOfRunnable[0] = plus$13;
    Cell cell2 = baseDialog.cont.pane((Element)table2);
    float f = width();
    cell2 = cell2.width(f);
    f = Core.scene.getHeight() * 0.4F;
    cell2.height(f).row();
    TextField textField = new TextField();
    this();
    paramString2 = HubText.get("msg.hint");
    textField.setMessageText(paramString2);
    textField.setMaxLength(300);
    Plus$14 plus$14 = new Plus$14();
    this(textField, bool2, str2, arrayOfRunnable);
    table1 = baseDialog.cont;
    Plus$15 plus$15 = new Plus$15();
    this(textField, plus$14);
    Cell cell1 = table1.table(plus$15);
    f = width();
    cell1.width(f);
    baseDialog.addCloseButton();
    Plus$16 plus$16 = new Plus$16();
    this(bool2, arrayOfRunnable);
    baseDialog.shown(plus$16);
    Plus$17 plus$17 = new Plus$17();
    this(baseDialog, arrayOfRunnable);
    f = 4.0F;
    Timer.Task task = Timer.schedule(plus$17, f, f);
    Plus$18 plus$18 = new Plus$18();
    this(task, bool2);
    baseDialog.hidden(plus$18);
    baseDialog.show();
  }
  
  public static void openChatId(String paramString1, String paramString2) {
    openChat(paramString2, paramString1);
  }
  
  public static void openGlobal() {
    boolean bool = HubClient.decided();
    if (bool) {
      bool = HubClient.consented();
      if (bool) {
        openChat(null, null);
        return;
      } 
    } 
    Plus$0 plus$0 = new Plus$0();
    this();
    Access.showConsent(plus$0);
  }
  
  private static void pickImage(Cons paramCons) {
    FileChooser.FileChooserParams fileChooserParams = new FileChooser.FileChooserParams();
    this();
    fileChooserParams = fileChooserParams.open(true);
    String str = tr("Enviar imagem", "Send image");
    fileChooserParams = fileChooserParams.title(str);
    String[] arrayOfString = { "png", "jpg", "jpeg" };
    fileChooserParams = fileChooserParams.extensions(arrayOfString);
    Plus$22 plus$22 = new Plus$22();
    this(paramCons);
    fileChooserParams.submit(plus$22);
  }
  
  private static void setFlag(String paramString, boolean paramBoolean) {
    Class<LunariansSettings> clazz = LunariansSettings.class;
    try {
      return;
    } finally {
      null;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = paramString;
      Log.debug("[LunariansHub] flag @ indisponivel", arrayOfObject);
    } 
  }
  
  private static void showFull(int paramInt) {
    BaseDialog baseDialog = new BaseDialog();
    this("");
    UiStyle.register(baseDialog);
    Image image = new Image();
    this();
    Scaling scaling = Scaling.fit;
    image.setScaling(scaling);
    loadInto(image, paramInt);
    Cell cell = baseDialog.cont.add((Element)image);
    float f1 = Math.min(Core.scene.getWidth() - 40.0F, 480.0F);
    float f2 = Core.scene.getHeight() * 0.6F;
    cell.size(f1, f2).row();
    baseDialog.addCloseButton();
    baseDialog.show();
  }
  
  private static byte[] shrink(Pixmap paramPixmap) {
    char c = '';
    int i = 384;
    float f1 = 5.38E-43F;
    int j = 256;
    float f2 = 3.59E-43F;
    int k = 192;
    int[] arrayOfInt = { i, j, k, c, 96 };
    int m = 0;
    while (true) {
      i = 5;
      f1 = 7.0E-45F;
      if (m < i) {
        f1 = arrayOfInt[m];
        j = paramPixmap.width;
        k = paramPixmap.height;
        f2 = Math.max(j, k);
        f1 /= f2;
        f1 = Math.min(1.0F, f1);
        j = Math.round(paramPixmap.width * f1);
        k = 1;
        int n = Math.max(k, j);
        i = Math.round(paramPixmap.height * f1);
        int i1 = Math.max(k, i);
        Pixmap pixmap2 = new Pixmap();
        this(n, i1);
        int i2 = paramPixmap.width;
        int i3 = paramPixmap.height;
        Pixmap pixmap3 = paramPixmap;
        pixmap2.draw(paramPixmap, 0, 0, i2, i3, 0, 0, n, i1);
        byte[] arrayOfByte = PixmapIO.writePngBytes(pixmap2);
        pixmap2.dispose();
        i = arrayOfByte.length;
        j = 59392;
        f2 = 8.3226E-41F;
        if (i <= j)
          return arrayOfByte; 
        m++;
        Pixmap pixmap1 = pixmap3;
        continue;
      } 
      return null;
    } 
  }
  
  static void sync() {
    boolean bool = HubClient.consented();
    if (bool) {
      String str = HubClient.myId();
      bool = str.isEmpty();
      if (!bool) {
        bool = HubClient.blocked;
        if (!bool) {
          StringBuilder stringBuilder = new StringBuilder();
          this("/api/plus/sync?gafter=");
          int i = Core.settings.getInt("lunarians-hub-gchat-after", 0);
          stringBuilder.append(i);
          String str1 = stringBuilder.toString();
          Plus$19 plus$19 = new Plus$19();
          this();
          Plus$20 plus$20 = new Plus$20();
          this();
          String str2 = "GET";
          HubClient.call(str2, str1, null, plus$19, plus$20);
        } 
      } 
    } 
  }
  
  static String tr(String paramString1, String paramString2) {
    return AudioUi.tr(paramString1, paramString2);
  }
  
  private static float width() {
    return Math.min(Core.scene.getWidth() - 40.0F, 520.0F);
  }
  
  private static Label wrap(String paramString) {
    Label label = new Label();
    this(paramString);
    label.setWrap(true);
    return label;
  }
}
