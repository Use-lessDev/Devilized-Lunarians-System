package lunarians.hub;

import arc.Application;
import arc.Core;
import arc.Settings;
import arc.func.Cons;
import arc.util.Http;
import arc.util.Log;
import arc.util.Reflect;
import arc.util.Strings;
import arc.util.serialization.Base64Coder;
import arc.util.serialization.Jval;
import java.security.SecureRandom;
import mindustry.Vars;
import mindustry.core.UI;
import mindustry.gen.Player;
import mindustry.net.Net;
import mindustry.ui.dialogs.JoinDialog;

public class HubClient {
  public static final String BASE_URL = "https://lunarians-hub.morajoetorajo2.workers.dev";
  
  private static final String K_AVATAR = "lunarians-hub-avatar";
  
  private static final String K_AVATAR_IMG = "lunarians-hub-avatar-img";
  
  private static final String K_CHAT = "lunarians-hub-chat-after";
  
  private static final String K_CONSENT = "lunarians-hub-consent";
  
  private static final String K_GLOBAL = "lunarians-hub-global-after";
  
  private static final String K_ID = "lunarians-hub-id";
  
  private static final String K_TOKEN = "lunarians-hub-token";
  
  public static volatile boolean blocked = false;
  
  public static volatile int newChat;
  
  public static volatile int pendingRequests;
  
  public static String avatar() {
    return Core.settings.getString("lunarians-hub-avatar", "");
  }
  
  public static String avatarImg() {
    return Core.settings.getString("lunarians-hub-avatar-img", "");
  }
  
  public static void call(String paramString1, String paramString2, Jval paramJval, Cons paramCons1, Cons paramCons2) {
    boolean bool = consented();
    if (!bool)
      return; 
    Http.HttpRequest httpRequest2 = req(paramString1, paramString2, paramJval);
    HubClient$6 hubClient$6 = new HubClient$6();
    this(paramString1, paramString2, paramCons2);
    Http.HttpRequest httpRequest1 = httpRequest2.error(hubClient$6);
    HubClient$7 hubClient$7 = new HubClient$7();
    this(paramCons1, paramCons2);
    httpRequest1.submit(hubClient$7);
  }
  
  public static boolean consented() {
    String str = Core.settings.getString("lunarians-hub-consent", "");
    return "yes".equals(str);
  }
  
  public static String[] currentServer() {
    try {
      Net net = Vars.net;
      if (net != null) {
        net = Vars.net;
        boolean bool = net.client();
        if (bool) {
          UI uI = Vars.ui;
          if (uI != null) {
            uI = Vars.ui;
            JoinDialog joinDialog = uI.join;
            if (joinDialog != null) {
              UI uI1 = Vars.ui;
              JoinDialog joinDialog1 = uI1.join;
              String str1 = "lastIp";
              Object object1 = Reflect.get(joinDialog1, str1);
              object1 = object1;
              UI uI2 = Vars.ui;
              JoinDialog joinDialog2 = uI2.join;
              String str2 = "lastPort";
              Object object2 = Reflect.get(joinDialog2, str2);
              object2 = object2;
              if (object1 != null) {
                boolean bool1 = object1.isEmpty();
                if (!bool1) {
                  int i;
                  if (object2 == null) {
                    i = 6567;
                  } else {
                    i = object2.intValue();
                  } 
                  object2 = String.valueOf(i);
                  return new String[] { (String)object1, (String)object2 };
                } 
              } 
            } 
          } 
        } 
      } 
    } finally {}
    return new String[] { "", "0" };
  }
  
  public static boolean decided() {
    return Core.settings.getString("lunarians-hub-consent", "").isEmpty() ^ true;
  }
  
  public static void ensureRegistered(Runnable paramRunnable) {
    boolean bool = consented();
    if (bool) {
      String str1 = myId();
      bool = str1.isEmpty();
      if (!bool) {
        str1 = token();
        bool = str1.isEmpty();
        if (!bool) {
          if (paramRunnable != null)
            paramRunnable.run(); 
          return;
        } 
      } 
    } else {
      return;
    } 
    String str = randomHex(16);
    Jval jval = profile();
    jval.add("id", str);
    Http.HttpRequest httpRequest = req("POST", "/api/register", jval);
    HubClient$4 hubClient$4 = new HubClient$4();
    this();
    httpRequest = httpRequest.error(hubClient$4);
    HubClient$5 hubClient$5 = new HubClient$5();
    this(str, paramRunnable);
    httpRequest.submit(hubClient$5);
  }
  
  public static void heartbeat(Cons paramCons) {
    boolean bool = consented();
    if (bool) {
      String str = myId();
      bool = str.isEmpty();
      if (!bool) {
        Jval jval1 = profile();
        Jval jval2 = Jval.valueOf(lastGlobal());
        jval1.add("global_after", jval2);
        int i = lastChat();
        long l = i;
        jval2 = Jval.valueOf(l);
        jval1.add("chat_after", jval2);
        HubClient$2 hubClient$2 = new HubClient$2();
        this(paramCons);
        paramCons = new HubClient$3();
        super();
        String str1 = "POST";
        String str2 = "/api/heartbeat";
        call(str1, str2, jval1, hubClient$2, paramCons);
      } 
    } 
  }
  
  public static int lastChat() {
    return Core.settings.getInt("lunarians-hub-chat-after", 0);
  }
  
  public static int lastGlobal() {
    return Core.settings.getInt("lunarians-hub-global-after", 0);
  }
  
  public static void leaveAndDelete(Cons paramCons) {
    String str = myId();
    boolean bool1 = str.isEmpty();
    if (!bool1) {
      str = token();
      bool1 = str.isEmpty();
      if (!bool1) {
        Jval jval = Jval.newObject();
        Http.HttpRequest httpRequest = req("POST", "/api/delete", jval);
        HubClient$14 hubClient$14 = new HubClient$14();
        this(paramCons);
        httpRequest = httpRequest.error(hubClient$14);
        HubClient$15 hubClient$15 = new HubClient$15();
        this(paramCons);
        httpRequest.submit(hubClient$15);
        return;
      } 
    } 
    setConsent(false);
    wipeLocal();
    Boolean bool = Boolean.TRUE;
    paramCons.get(bool);
  }
  
  public static String myId() {
    return Core.settings.getString("lunarians-hub-id", "");
  }
  
  private static Jval profile() {
    Jval jval1 = Jval.newObject();
    Player player = Vars.player;
    if (player == null) {
      str1 = "";
    } else {
      str1 = Vars.player.name();
    } 
    String str1 = Strings.stripColors(str1);
    jval1.add("name", str1);
    String str2 = avatar();
    jval1.add("avatar", str2);
    String[] arrayOfString = currentServer();
    String str3 = arrayOfString[0];
    jval1.add("server_name", str3);
    Jval jval2 = Jval.valueOf(Strings.parseInt(arrayOfString[1], 0));
    jval1.add("server_port", jval2);
    return jval1;
  }
  
  private static String randomHex(int paramInt) {
    byte[] arrayOfByte = new byte[paramInt];
    SecureRandom secureRandom = new SecureRandom();
    this();
    secureRandom.nextBytes(arrayOfByte);
    StringBuilder stringBuilder = new StringBuilder();
    this();
    for (int i = 0; i < paramInt; i++) {
      byte b = arrayOfByte[i];
      Byte byte_ = Byte.valueOf(b);
      boolean bool = true;
      Object[] arrayOfObject = new Object[bool];
      arrayOfObject[0] = byte_;
      String str = String.format("%02x", arrayOfObject);
      stringBuilder.append(str);
    } 
    return stringBuilder.toString();
  }
  
  private static Http.HttpRequest req(String paramString1, String paramString2, Jval paramJval) {
    // Byte code:
    //   0: ldc 'POST'
    //   2: astore_3
    //   3: aload_0
    //   4: aload_3
    //   5: invokevirtual equals : (Ljava/lang/Object;)Z
    //   8: istore #4
    //   10: iload #4
    //   12: ifeq -> 22
    //   15: getstatic arc/util/Http$HttpMethod.POST : Larc/util/Http$HttpMethod;
    //   18: astore_0
    //   19: goto -> 26
    //   22: getstatic arc/util/Http$HttpMethod.GET : Larc/util/Http$HttpMethod;
    //   25: astore_0
    //   26: new java/lang/StringBuilder
    //   29: astore_3
    //   30: ldc 'https://lunarians-hub.morajoetorajo2.workers.dev'
    //   32: astore #5
    //   34: aload_3
    //   35: aload #5
    //   37: invokespecial <init> : (Ljava/lang/String;)V
    //   40: aload_3
    //   41: aload_1
    //   42: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   45: pop
    //   46: aload_3
    //   47: invokevirtual toString : ()Ljava/lang/String;
    //   50: astore_1
    //   51: aload_0
    //   52: aload_1
    //   53: invokestatic request : (Larc/util/Http$HttpMethod;Ljava/lang/String;)Larc/util/Http$HttpRequest;
    //   56: astore_0
    //   57: aload_2
    //   58: ifnull -> 92
    //   61: aload_2
    //   62: invokevirtual toString : ()Ljava/lang/String;
    //   65: astore_1
    //   66: aload_1
    //   67: invokevirtual length : ()I
    //   70: istore #6
    //   72: sipush #20000
    //   75: istore #7
    //   77: iload #6
    //   79: iload #7
    //   81: if_icmple -> 92
    //   84: sipush #30000
    //   87: istore #6
    //   89: goto -> 97
    //   92: sipush #8000
    //   95: istore #6
    //   97: aload_0
    //   98: iload #6
    //   100: invokevirtual timeout : (I)Larc/util/Http$HttpRequest;
    //   103: pop
    //   104: invokestatic token : ()Ljava/lang/String;
    //   107: astore_1
    //   108: aload_1
    //   109: invokevirtual isEmpty : ()Z
    //   112: istore #6
    //   114: iload #6
    //   116: ifne -> 174
    //   119: new java/lang/StringBuilder
    //   122: astore_1
    //   123: aload_1
    //   124: ldc_w 'Bearer '
    //   127: invokespecial <init> : (Ljava/lang/String;)V
    //   130: invokestatic myId : ()Ljava/lang/String;
    //   133: astore_3
    //   134: aload_1
    //   135: aload_3
    //   136: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   139: pop
    //   140: aload_1
    //   141: ldc_w '.'
    //   144: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   147: pop
    //   148: invokestatic token : ()Ljava/lang/String;
    //   151: astore_3
    //   152: aload_1
    //   153: aload_3
    //   154: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   157: pop
    //   158: aload_1
    //   159: invokevirtual toString : ()Ljava/lang/String;
    //   162: astore_1
    //   163: ldc_w 'Authorization'
    //   166: astore_3
    //   167: aload_0
    //   168: aload_3
    //   169: aload_1
    //   170: invokevirtual header : (Ljava/lang/String;Ljava/lang/String;)Larc/util/Http$HttpRequest;
    //   173: pop
    //   174: aload_2
    //   175: ifnull -> 202
    //   178: ldc_w 'application/json'
    //   181: astore_3
    //   182: aload_0
    //   183: ldc_w 'Content-Type'
    //   186: aload_3
    //   187: invokevirtual header : (Ljava/lang/String;Ljava/lang/String;)Larc/util/Http$HttpRequest;
    //   190: pop
    //   191: aload_2
    //   192: invokevirtual toString : ()Ljava/lang/String;
    //   195: astore_1
    //   196: aload_0
    //   197: aload_1
    //   198: invokevirtual content : (Ljava/lang/String;)Larc/util/Http$HttpRequest;
    //   201: pop
    //   202: aload_0
    //   203: areturn
  }
  
  public static void setAvatar(String paramString) {
    Settings settings = Core.settings;
    String str = "lunarians-hub-avatar-img";
    settings.remove(str);
    settings = Core.settings;
    if (paramString == null)
      paramString = ""; 
    str = "lunarians-hub-avatar";
    settings.put(str, paramString);
    boolean bool = consented();
    if (bool) {
      paramString = myId();
      bool = paramString.isEmpty();
      if (!bool) {
        HubClient$16 hubClient$16 = new HubClient$16();
        this();
        heartbeat(hubClient$16);
      } 
    } 
  }
  
  public static void setConsent(boolean paramBoolean) {
    String str;
    Settings settings = Core.settings;
    if (paramBoolean) {
      str = "yes";
    } else {
      str = "no";
    } 
    settings.put("lunarians-hub-consent", str);
  }
  
  public static void setLastChat(int paramInt) {
    Settings settings = Core.settings;
    Integer integer = Integer.valueOf(paramInt);
    settings.put("lunarians-hub-chat-after", integer);
  }
  
  public static void setLastGlobal(int paramInt) {
    Settings settings = Core.settings;
    Integer integer = Integer.valueOf(paramInt);
    settings.put("lunarians-hub-global-after", integer);
  }
  
  private static String token() {
    return Core.settings.getString("lunarians-hub-token", "");
  }
  
  public static void uploadAvatar(byte[] paramArrayOfbyte, Cons paramCons) {
    String str = new String();
    char[] arrayOfChar = Base64Coder.encode(paramArrayOfbyte);
    this(arrayOfChar);
    Jval jval = Jval.newObject();
    jval.add("png", str);
    HubClient$0 hubClient$0 = new HubClient$0();
    this(str, paramCons);
    HubClient$1 hubClient$1 = new HubClient$1();
    this(paramCons);
    call("POST", "/api/avatar/upload", jval, hubClient$0, hubClient$1);
  }
  
  public static void wipeLocal() {
    Core.settings.remove("lunarians-hub-id");
    Core.settings.remove("lunarians-hub-token");
    Core.settings.remove("lunarians-hub-global-after");
    Core.settings.remove("lunarians-hub-chat-after");
    Core.settings.remove("lunarians-hub-avatar");
    Settings settings = Core.settings;
    String str = "lunarians-hub-avatar-img";
    settings.remove(str);
    try {
      Skins.wipeLocal();
    } finally {}
    blocked = false;
    pendingRequests = 0;
    newChat = 0;
  }
}
