package lunarians.hub;

import arc.Core;
import arc.Events;
import arc.Graphics;
import arc.Settings;
import arc.func.Boolp;
import arc.scene.Element;
import arc.scene.Scene;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Label;
import arc.scene.ui.TextArea;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectMap;
import arc.util.Timer;
import arc.util.serialization.Jval;
import java.lang.reflect.Field;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import lunarians.UiStyle;
import mindustry.Vars;
import mindustry.core.UI;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.ui.fragments.MenuFragment;

public class Access {
  private static final String K_STATUS = "lunarians-access";
  
  private static final ObjectMap T;
  
  private static boolean askedThisSession;
  
  private static Field betaField;
  
  private static boolean fetchedOnce;
  
  private static boolean inited;
  
  private static Boolean schemForced;
  
  private static Boolean schemOrig;
  
  private static boolean warned;
  
  private static Boolp wrapper;
  
  static {
    ObjectMap objectMap = new ObjectMap();
    this();
    T = objectMap;
    a("access.title", "Request Beta UI access", "Pedir acesso ao Beta UI", "Pedir acceso a la Beta UI", "Запросить доступ к Beta UI");
    a("access.intro", "The Beta UI is unlocked by the developer. Tell him why you want to use the mod and send the request.", "O Beta UI é liberado pelo desenvolvedor. Conte por que você quer usar o mod e envie o pedido.", "La Beta UI la desbloquea el desarrollador. Cuéntale por qué quieres usar el mod y envía la solicitud.", "Beta UI открывает разработчик. Напишите, зачем вам мод, и отправьте заявку.");
    a("access.why", "Why do you want to use the mod?", "Por que você quer usar o mod?", "¿Por qué quieres usar el mod?", "Зачем вы хотите использовать мод?");
    a("access.hint", "Write your reason here...", "Escreva o motivo aqui...", "Escribe el motivo aquí...", "Напишите причину здесь...");
    String str = "Enviar";
    a("access.send", "Send", str, str, "Отправить");
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
  
  private static void a(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5) {
    ObjectMap objectMap = T;
    String[] arrayOfString = { paramString2, paramString3, paramString4, paramString5 };
    objectMap.put(paramString1, arrayOfString);
  }
  
  private static void apply(String paramString1, String paramString2) {
    String str1 = status();
    Settings settings = Core.settings;
    String str2 = "lunarians-access";
    settings.put(str2, paramString1);
    boolean bool1 = fetchedOnce;
    if (bool1) {
      boolean bool3 = paramString1.equals(str1);
      if (!bool3) {
        UI uI;
        str1 = "approved";
        bool3 = paramString1.equals(str1);
        if (bool3) {
          uI = Vars.ui;
          str1 = tr("access.approved");
          int i = 1084227584;
          float f = 5.0F;
          uI.showInfoToast(str1, f);
        } else {
          str1 = "rejected";
          bool3 = paramString1.equals(str1);
          if (bool3) {
            UI uI1 = Vars.ui;
            StringBuilder stringBuilder = new StringBuilder();
            this();
            str2 = tr("access.rejected");
            stringBuilder.append(str2);
            boolean bool4 = uI.isEmpty();
            if (bool4) {
              str = "";
            } else {
              StringBuilder stringBuilder1 = new StringBuilder();
              String str3 = ": ";
              this(str3);
              stringBuilder1.append(str);
              str = stringBuilder1.toString();
            } 
            stringBuilder.append(str);
            String str = stringBuilder.toString();
            int i = 1086324736;
            float f = 6.0F;
            uI1.showInfoToast(str, f);
          } 
        } 
      } 
    } 
    boolean bool = true;
    fetchedOnce = bool;
    str1 = "none";
    boolean bool2 = paramString1.equals(str1);
    if (bool2) {
      bool2 = askedThisSession;
      if (!bool2) {
        askedThisSession = bool;
        bool2 = false;
        paramString1 = null;
        bool = false;
        paramString2 = null;
        build(false, null);
      } 
    } 
  }
  
  private static void applyFeatures(Jval paramJval) {
    // Byte code:
    //   0: ldc_w 'showSchematicPanel'
    //   3: astore_1
    //   4: aload_0
    //   5: ifnull -> 64
    //   8: aload_0
    //   9: aload_1
    //   10: invokevirtual has : (Ljava/lang/String;)Z
    //   13: istore_2
    //   14: iload_2
    //   15: ifeq -> 64
    //   18: iconst_1
    //   19: istore_2
    //   20: aload_0
    //   21: aload_1
    //   22: iload_2
    //   23: invokevirtual getBool : (Ljava/lang/String;Z)Z
    //   26: istore_3
    //   27: getstatic lunarians/hub/Access.schemForced : Ljava/lang/Boolean;
    //   30: astore_1
    //   31: aload_1
    //   32: ifnonnull -> 50
    //   35: getstatic lunarians/LunariansSettings.showSchematicPanel : Z
    //   38: istore #4
    //   40: iload #4
    //   42: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   45: astore_1
    //   46: aload_1
    //   47: putstatic lunarians/hub/Access.schemOrig : Ljava/lang/Boolean;
    //   50: iload_3
    //   51: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   54: astore_1
    //   55: aload_1
    //   56: putstatic lunarians/hub/Access.schemForced : Ljava/lang/Boolean;
    //   59: iload_3
    //   60: invokestatic setSchem : (Z)V
    //   63: return
    //   64: getstatic lunarians/hub/Access.schemForced : Ljava/lang/Boolean;
    //   67: astore_0
    //   68: aload_0
    //   69: ifnull -> 101
    //   72: getstatic lunarians/hub/Access.schemOrig : Ljava/lang/Boolean;
    //   75: astore_0
    //   76: aload_0
    //   77: ifnull -> 89
    //   80: aload_0
    //   81: invokevirtual booleanValue : ()Z
    //   84: istore_3
    //   85: iload_3
    //   86: invokestatic setSchem : (Z)V
    //   89: iconst_0
    //   90: istore_3
    //   91: aconst_null
    //   92: astore_0
    //   93: aconst_null
    //   94: putstatic lunarians/hub/Access.schemForced : Ljava/lang/Boolean;
    //   97: aconst_null
    //   98: putstatic lunarians/hub/Access.schemOrig : Ljava/lang/Boolean;
    //   101: return
    //   102: pop
    //   103: goto -> 101
    // Exception table:
    //   from	to	target	type
    //   9	13	102	finally
    //   22	26	102	finally
    //   27	30	102	finally
    //   35	38	102	finally
    //   40	45	102	finally
    //   46	50	102	finally
    //   50	54	102	finally
    //   55	59	102	finally
    //   59	63	102	finally
    //   64	67	102	finally
    //   72	75	102	finally
    //   80	84	102	finally
    //   85	89	102	finally
    //   93	97	102	finally
    //   97	101	102	finally
  }
  
  public static boolean approved() {
    boolean bool = HubClient.consented();
    if (bool) {
      String str1 = "approved";
      String str2 = status();
      bool = str1.equals(str2);
      if (bool)
        return true; 
    } 
    return false;
  }
  
  private static void build(boolean paramBoolean, Runnable paramRunnable) {
    BaseDialog baseDialog = new BaseDialog();
    if (paramBoolean) {
      str1 = HubText.get("consent.title");
    } else {
      str1 = tr("access.title");
    } 
    this(str1);
    String str1;
    UiStyle.register(baseDialog);
    float f1 = Core.scene.getWidth() - 40.0F;
    float f2 = 520.0F;
    f1 = Math.min(f1, f2);
    TextArea textArea = new TextArea();
    this("");
    String str3 = tr("access.hint");
    textArea.setMessageText(str3);
    textArea.setMaxLength(300);
    Table table2 = baseDialog.cont;
    Access$3 access$3 = new Access$3();
    this(paramBoolean, f1, textArea);
    Cell cell2 = table2.pane(access$3);
    Scene scene = Core.scene;
    float f3 = scene.getHeight();
    float f4 = 0.7F;
    f3 *= f4;
    cell2.maxHeight(f3).row();
    cell2 = baseDialog.buttons.defaults();
    f1 = Math.min(f1 / 2.0F - 6.0F, 220.0F);
    f3 = 56.0F;
    Cell cell1 = cell2.size(f1, f3);
    float f5 = 4.0F;
    cell1.pad(f5);
    Table table1 = baseDialog.buttons;
    if (paramBoolean) {
      str2 = HubText.get("decline");
    } else {
      str2 = tr("access.later");
    } 
    TextureRegionDrawable textureRegionDrawable = Icon.cancel;
    Access$4 access$4 = new Access$4();
    this(paramBoolean, baseDialog);
    table1.button(str2, (Drawable)textureRegionDrawable, access$4);
    table1 = baseDialog.buttons;
    String str2 = tr("access.send");
    textureRegionDrawable = Icon.upload;
    Access$5 access$5 = new Access$5();
    this(textArea, baseDialog, paramBoolean, paramRunnable);
    table1.button(str2, (Drawable)textureRegionDrawable, access$5);
    paramRunnable = new Access$6();
    super(paramBoolean);
    baseDialog.closeOnBack(paramRunnable);
    baseDialog.show();
  }
  
  private static void enforce() {
    Boolean bool = schemForced;
    if (bool != null) {
      boolean bool1 = bool.booleanValue();
      setSchem(bool1);
    } 
  }
  
  private static void gate() {
    Graphics graphics = Core.graphics;
    long l1 = graphics.getFrameId() % 20L;
    long l2 = 0L;
    int i = l1 cmp l2;
    if (i == 0) {
      graphics = null;
      try {
        Field field = betaField;
        if (field == null) {
          String str1 = "lunarians.LunariansMod";
          Class<?> clazz = Class.forName(str1);
          String str2 = "betaHud";
          field = clazz.getField(str2);
          betaField = field;
        } 
        boolean bool = false;
        Boolp boolp = null;
        Object object = field.get(null);
      } finally {
        Exception exception = null;
        boolean bool = warned;
      } 
    } 
  }
  
  private static void init() {
    boolean bool = inited;
    if (!bool) {
      bool = Vars.headless;
      if (!bool) {
        bool = true;
        inited = bool;
        EventType.Trigger trigger = EventType.Trigger.update;
        Access$7 access$7 = new Access$7();
        this();
        Events.run(trigger, access$7);
        Access$8 access$8 = new Access$8();
        this();
        Timer.schedule(access$8, 4.0F, 30.0F);
        Access$9 access$9 = new Access$9();
        this();
        float f1 = 3.0F;
        float f2 = 2.0F;
        Timer.schedule(access$9, f1, f2);
        try {
          UI uI = Vars.ui;
          MenuFragment menuFragment = uI.menufrag;
          String str = "access.btn";
          str = tr(str);
          TextureRegionDrawable textureRegionDrawable = Icon.lock;
          Access$10 access$10 = new Access$10();
          this();
          menuFragment.addButton(str, (Drawable)textureRegionDrawable, access$10);
        } finally {}
      } 
    } 
  }
  
  public static void initAll() {
    Plus.init();
    init();
  }
  
  private static void menuClick() {
    UI uI;
    boolean bool1 = HubClient.consented();
    String str1 = null;
    if (!bool1) {
      showConsent(null);
      return;
    } 
    String str2 = status();
    String str3 = "approved";
    boolean bool2 = str2.equals(str3);
    float f = 3.0F;
    if (bool2) {
      uI = Vars.ui;
      str1 = tr("access.ok");
      uI.showInfoToast(str1, f);
      return;
    } 
    str3 = "pending";
    bool1 = uI.equals(str3);
    if (bool1) {
      uI = Vars.ui;
      str1 = tr("access.waiting");
      uI.showInfoToast(str1, f);
      return;
    } 
    build(false, null);
  }
  
  private static void poll() {
    boolean bool = HubClient.consented();
    if (!bool) {
      String str1 = status();
      String str2 = "none";
      bool = str2.equals(str1);
      if (!bool) {
        Settings settings = Core.settings;
        String str3 = "lunarians-access";
        settings.put(str3, str2);
      } 
      applyFeatures(null);
      return;
    } 
    String str = HubClient.myId();
    bool = str.isEmpty();
    if (!bool) {
      bool = HubClient.blocked;
      if (!bool) {
        Access$12 access$12 = new Access$12();
        this();
        Access$13 access$13 = new Access$13();
        this();
        String str1 = "GET";
        String str2 = "/api/access/status";
        HubClient.call(str1, str2, null, access$12, access$13);
      } 
    } 
  }
  
  private static void send(String paramString) {
    Jval jval = Jval.newObject();
    jval.add("reason", paramString);
    Access$0 access$0 = new Access$0();
    this();
    Access$1 access$1 = new Access$1();
    this();
    HubClient.call("POST", "/api/access/request", jval, access$0, access$1);
  }
  
  private static void setSchem(boolean paramBoolean) {
    boolean bool = LunariansSettings.showSchematicPanel;
    if (bool != paramBoolean)
      LunariansSettings.showSchematicPanel = paramBoolean; 
  }
  
  public static void showConsent(Runnable paramRunnable) {
    build(true, paramRunnable);
  }
  
  public static String status() {
    return Core.settings.getString("lunarians-access", "none");
  }
  
  private static String tr(String paramString) {
    boolean bool2;
    String[] arrayOfString = (String[])T.get(paramString);
    if (arrayOfString == null)
      return paramString; 
    paramString = LunariansI18n.getLanguage();
    String str = "pt";
    boolean bool1 = paramString.equals(str);
    if (bool1) {
      bool2 = true;
    } else {
      str = "es";
      bool1 = paramString.equals(str);
      if (bool1) {
        byte b = 2;
      } else {
        str = "ru";
        bool2 = paramString.equals(str);
        if (bool2) {
          byte b = 3;
        } else {
          bool2 = false;
          paramString = null;
        } 
      } 
    } 
    return arrayOfString[bool2];
  }
  
  private static Label wrap(String paramString) {
    Label label = new Label();
    this(paramString);
    label.setWrap(true);
    return label;
  }
}
