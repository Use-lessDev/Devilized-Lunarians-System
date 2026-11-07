package lunarians.hub;

import arc.Core;
import arc.Events;
import arc.files.Fi;
import arc.func.Cons;
import arc.graphics.Pixmap;
import arc.graphics.PixmapIO;
import arc.scene.Element;
import arc.scene.style.Drawable;
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
import lunarians.LunariansSettings;
import lunarians.UiStyle;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.ui.FileChooser;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public final class Plus {
  private static final String K_G = "lunarians-hub-gchat-after";
  
  private static final String[] FLAGS = new String[] { 
      "enableEnemyCoreAlert", "enableAutoFill", "enableFreeCam", "enableProductionAlert", "enableCamFollow", "enableLossWarn", "enableAutoUnit", "enableAutoCargo", "enableAutoContainer", "enableAutoDefence", 
      "enableAutoShield", "enableAutoHeal", "enableAutoAttack", "enableAutoUnitHeal", "enableAutoMoon", "enableAutoConnectConveyor", "enableAutoDrill", "enableAutoDrillConveyor", "dockAuto", "pvpMode" };
  
  public static volatile int gnew = 0;
  
  private static int lastGnew = 0;
  
  private static boolean inited;
  
  private static boolean globalOpen;
  
  private static final ObjectMap<String, Boolean> orig = new ObjectMap();
  
  private static final ObjectMap<String, Boolean> forced = new ObjectMap();
  
  private static final ObjectMap<Integer, TextureRegionDrawable> imgCache = new ObjectMap();
  
  private static Field betaField;
  
  private static Field panelField;
  
  private static boolean betaFailed;
  
  static String tr(String paramString1, String paramString2) {
    return AudioUi.tr(paramString1, paramString2);
  }
  
  private static float width() {
    return Math.min(Core.scene.getWidth() - 40.0F, 520.0F);
  }
  
  public static void init() {
    if (inited || Vars.headless)
      return; 
    inited = true;
    Room.init();
    try {
      Vars.ui.menufrag.addButton(tr("Chat global", "Global chat"), (Drawable)Icon.chat, Plus::openGlobal);
    } catch (Throwable throwable) {}
    hookBeta();
    Timer.schedule(Plus::sync, 8.0F, 30.0F);
    Timer.schedule(Plus::enforce, 3.0F, 2.0F);
  }
  
  public static void openChatId(String paramString1, String paramString2) {
    openChat(paramString2, paramString1);
  }
  
  public static void openGlobal() {
    if (!HubClient.decided() || !HubClient.consented()) {
      Access.showConsent(Plus::openGlobal);
      return;
    } 
    openChat(null, null);
  }
  
  private static void hookBeta() {
    Events.run(EventType.Trigger.update, () -> {
          if (betaFailed || Core.graphics.getFrameId() % 30L != 0L)
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
            if (table == null)
              return; 
            String str = tr("Chat global", "Global chat") + ((gnew > 0 && HubClient.consented()) ? (" (" + gnew + ")") : "");
            Element element = table.find("lunarians-hub-gchat");
            if (element instanceof TextButton) {
              ((TextButton)element).setText(str);
              return;
            } 
            TextButton textButton = new TextButton(str, Styles.defaultt);
            textButton.name = "lunarians-hub-gchat";
            textButton.clicked(Plus::openGlobal);
            table.row();
            table.add((Element)textButton).growX().height(44.0F).pad(2.0F);
          } catch (Throwable throwable) {
            betaFailed = true;
            Log.warn("[Lunarians Hub] Global chat hook off: @", new Object[] { throwable.toString() });
          } 
        });
  }
  
  private static Boolean getFlag(String paramString) {
    try {
      return Boolean.valueOf(LunariansSettings.class.getField(paramString).getBoolean(null));
    } catch (Throwable throwable) {
      return null;
    } 
  }
  
  private static void setFlag(String paramString, boolean paramBoolean) {
    try {
      LunariansSettings.class.getField(paramString).setBoolean(null, paramBoolean);
    } catch (Throwable throwable) {
      Log.debug("[LunariansHub] flag @ indisponivel", new Object[] { paramString });
    } 
  }
  
  static void sync() {
    if (!HubClient.consented() || HubClient.myId().isEmpty() || HubClient.blocked)
      return; 
    HubClient.call("GET", "/api/plus/sync?gafter=" + Core.settings.getInt("lunarians-hub-gchat-after", 0), null, paramJval -> {
          try {
            Jval jval = paramJval.get("features");
            boolean bool = false;
            for (String str : FLAGS) {
              boolean bool1 = jval.getBool(str, true);
              boolean bool2 = jval.getBool(str, false);
              boolean bool3 = (bool1 == bool2) ? true : false;
              if (bool3) {
                if (!forced.containsKey(str)) {
                  Boolean bool4 = getFlag(str);
                  if (bool4 != null)
                    orig.put(str, bool4); 
                } 
                forced.put(str, Boolean.valueOf(bool1));
                setFlag(str, bool1);
              } else if (forced.containsKey(str)) {
                Boolean bool4 = (Boolean)orig.get(str);
                if (bool4 != null) {
                  setFlag(str, bool4.booleanValue());
                  bool = true;
                } 
                forced.remove(str);
                orig.remove(str);
              } 
            } 
            if (bool)
              LunariansSettings.save(); 
            gnew = paramJval.getInt("gnew", 0);
            if (gnew > lastGnew && !globalOpen)
              Vars.ui.showInfoToast(tr("Novas mensagens no chat global", "New global chat messages"), 3.0F); 
            lastGnew = gnew;
          } catch (Throwable throwable) {
            Log.debug("[LunariansHub] sync: @", new Object[] { throwable.toString() });
          } 
        }paramString -> {
        
        });
  }
  
  private static void enforce() {
    if (forced.size == 0)
      return; 
    for (ObjectMap.Entry entry : forced.entries()) {
      Boolean bool = getFlag((String)entry.key);
      if (bool != null && bool.booleanValue() != ((Boolean)entry.value).booleanValue())
        setFlag((String)entry.key, ((Boolean)entry.value).booleanValue()); 
    } 
  }
  
  private static void loadInto(Image paramImage, int paramInt) {
    TextureRegionDrawable textureRegionDrawable = (TextureRegionDrawable)imgCache.get(Integer.valueOf(paramInt));
    if (textureRegionDrawable != null) {
      paramImage.setDrawable((Drawable)textureRegionDrawable);
      return;
    } 
    HubClient.call("GET", "/api/chat/img?id=" + paramInt, null, paramJval -> {
          try {
            TextureRegionDrawable textureRegionDrawable = Avatar.fromPng(Base64Coder.decode(paramJval.getString("png", "")));
            imgCache.put(Integer.valueOf(paramInt), textureRegionDrawable);
            paramImage.setDrawable((Drawable)textureRegionDrawable);
          } catch (Throwable throwable) {}
        }paramString -> {
        
        });
  }
  
  private static void showFull(int paramInt) {
    BaseDialog baseDialog = new BaseDialog("");
    UiStyle.register(baseDialog);
    Image image = new Image();
    image.setScaling(Scaling.fit);
    loadInto(image, paramInt);
    baseDialog.cont.add((Element)image).size(Math.min(Core.scene.getWidth() - 40.0F, 480.0F), Core.scene.getHeight() * 0.6F).row();
    baseDialog.addCloseButton();
    baseDialog.show();
  }
  
  private static byte[] shrink(Pixmap paramPixmap) {
    int[] arrayOfInt = { 384, 256, 192, 128, 96 };
    for (int i : arrayOfInt) {
      float f = Math.min(1.0F, i / Math.max(paramPixmap.width, paramPixmap.height));
      int j = Math.max(1, Math.round(paramPixmap.width * f));
      int k = Math.max(1, Math.round(paramPixmap.height * f));
      Pixmap pixmap = new Pixmap(j, k);
      pixmap.draw(paramPixmap, 0, 0, paramPixmap.width, paramPixmap.height, 0, 0, j, k);
      byte[] arrayOfByte = PixmapIO.writePngBytes(pixmap);
      pixmap.dispose();
      if (arrayOfByte.length <= 59392)
        return arrayOfByte; 
    } 
    return null;
  }
  
  private static void pickImage(Cons<byte[]> paramCons) {
    (new FileChooser.FileChooserParams()).open(true).title(tr("Enviar imagem", "Send image")).extensions(new String[] { "png", "jpg", "jpeg" }).submit(paramObject -> {
          Fi fi = (Fi)paramObject;
          try {
            if (fi.length() > 8000000L) {
              Vars.ui.showInfoToast(tr("Imagem muito grande", "Image too large"), 3.0F);
              return;
            } 
            Pixmap pixmap = new Pixmap(fi);
            byte[] arrayOfByte = shrink(pixmap);
            pixmap.dispose();
            if (arrayOfByte == null) {
              Vars.ui.showInfoToast(tr("Nao foi possivel reduzir a imagem", "Could not shrink the image"), 3.0F);
              return;
            } 
            paramCons.get(arrayOfByte);
          } catch (Throwable throwable) {
            Vars.ui.showInfoToast(tr("Imagem invalida", "Invalid image"), 3.0F);
          } 
        });
  }
  
  private static Label wrap(String paramString) {
    Label label = new Label(paramString);
    label.setWrap(true);
    return label;
  }
  
  private static void addMsg(Table paramTable, Jval paramJval, boolean paramBoolean, String paramString) {
    String str1 = paramJval.getBool("mine", false) ? tr("Voce", "You") : (paramBoolean ? paramJval.getString("name", "?") : paramString);
    String str2 = paramJval.getString("text", "");
    paramTable.add((Element)wrap(str1 + ": " + str2)).width(width() - 20.0F).left().row();
    int i = paramJval.getInt("img", 0);
    if (i > 0) {
      Image image = new Image();
      image.setScaling(Scaling.fit);
      image.clicked(() -> showFull(paramInt));
      loadInto(image, i);
      paramTable.add((Element)image).size(160.0F).left().row();
    } 
  }
  
  public static void openChat(String paramString1, String paramString2) {
    boolean bool = (paramString2 == null) ? true : false;
    BaseDialog baseDialog = new BaseDialog(bool ? tr("Chat global", "Global chat") : (HubText.get("chat") + ": " + paramString1));
    UiStyle.register(baseDialog);
    Table table = new Table();
    int[] arrayOfInt = { 0 };
    boolean[] arrayOfBoolean = { false };
    Runnable[] arrayOfRunnable = new Runnable[1];
    arrayOfRunnable[0] = (() -> {
        if (paramArrayOfboolean[0])
          return; 
        paramArrayOfboolean[0] = true;
        String str = paramBoolean ? ("/api/gchat?after=" + paramArrayOfint[0]) : ("/api/chat?with=" + paramString1 + "&after=" + paramArrayOfint[0]);
        HubClient.call("GET", str, null, (), ());
      });
    baseDialog.cont.pane((Element)table).width(width()).height(Core.scene.getHeight() * 0.4F).row();
    TextField textField = new TextField();
    textField.setMessageText(HubText.get("msg.hint"));
    textField.setMaxLength(300);
    Cons cons = paramArrayOfbyte -> {
        String str = paramTextField.getText().trim();
        if (str.isEmpty() && paramArrayOfbyte == null)
          return; 
        Jval jval = Jval.newObject();
        if (paramBoolean) {
          jval.add("text", str);
        } else {
          jval.add("to", paramString);
          jval.add("text", str);
        } 
        if (paramArrayOfbyte != null)
          jval.add("png", new String(Base64Coder.encode(paramArrayOfbyte))); 
        paramTextField.setText("");
        HubClient.call("POST", paramBoolean ? "/api/gchat/send" : "/api/chat/send", jval, (), ());
      };
    baseDialog.cont.table(paramObject -> {
          Table table = (Table)paramObject;
          table.add((Element)paramTextField).growX().height(46.0F);
          table.button("Img", ()).size(60.0F, 46.0F);
          table.button(HubText.get("send"), ()).size(90.0F, 46.0F);
        }).width(width());
    baseDialog.addCloseButton();
    baseDialog.shown(() -> {
          if (paramBoolean)
            globalOpen = true; 
          paramArrayOfRunnable[0].run();
        });
    Timer.Task task = Timer.schedule(() -> {
          if (paramBaseDialog.isShown())
            paramArrayOfRunnable[0].run(); 
        }4.0F, 4.0F);
    baseDialog.hidden(() -> {
          paramTask.cancel();
          if (paramBoolean)
            globalOpen = false; 
        });
    baseDialog.show();
  }
}
