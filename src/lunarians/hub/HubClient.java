package lunarians.hub;

import arc.Core;
import arc.func.Cons;
import arc.util.Http;
import arc.util.Log;
import arc.util.Reflect;
import arc.util.Strings;
import arc.util.serialization.Base64Coder;
import arc.util.serialization.Jval;
import java.security.SecureRandom;
import mindustry.Vars;

public class HubClient {
  public static final String BASE_URL = "https://lunarians-hub.morajoetorajo2.workers.dev";
  
  private static final String K_CONSENT = "lunarians-hub-consent";
  
  private static final String K_ID = "lunarians-hub-id";
  
  private static final String K_TOKEN = "lunarians-hub-token";
  
  private static final String K_GLOBAL = "lunarians-hub-global-after";
  
  private static final String K_CHAT = "lunarians-hub-chat-after";
  
  private static final String K_AVATAR = "lunarians-hub-avatar";
  
  private static final String K_AVATAR_IMG = "lunarians-hub-avatar-img";
  
  public static volatile boolean blocked = false;
  
  public static volatile int pendingRequests = 0, newChat = 0;
  
  public static boolean consented() {
    return "yes".equals(Core.settings.getString("lunarians-hub-consent", ""));
  }
  
  public static boolean decided() {
    return !Core.settings.getString("lunarians-hub-consent", "").isEmpty();
  }
  
  public static void setConsent(boolean paramBoolean) {
    Core.settings.put("lunarians-hub-consent", paramBoolean ? "yes" : "no");
  }
  
  public static String myId() {
    return Core.settings.getString("lunarians-hub-id", "");
  }
  
  private static String token() {
    return Core.settings.getString("lunarians-hub-token", "");
  }
  
  public static int lastGlobal() {
    return Core.settings.getInt("lunarians-hub-global-after", 0);
  }
  
  public static void setLastGlobal(int paramInt) {
    Core.settings.put("lunarians-hub-global-after", Integer.valueOf(paramInt));
  }
  
  public static String avatar() {
    return Core.settings.getString("lunarians-hub-avatar", "");
  }
  
  public static String avatarImg() {
    return Core.settings.getString("lunarians-hub-avatar-img", "");
  }
  
  public static void uploadAvatar(byte[] paramArrayOfbyte, Cons<Boolean> paramCons) {
    String str = new String(Base64Coder.encode(paramArrayOfbyte));
    Jval jval = Jval.newObject();
    jval.add("png", str);
    call("POST", "/api/avatar/upload", jval, paramJval -> {
          Core.settings.put("lunarians-hub-avatar", "img");
          Core.settings.put("lunarians-hub-avatar-img", paramString);
          paramCons.get(Boolean.valueOf(true));
        }paramString -> paramCons.get(Boolean.valueOf(false)));
  }
  
  public static void setAvatar(String paramString) {
    Core.settings.remove("lunarians-hub-avatar-img");
    Core.settings.put("lunarians-hub-avatar", (paramString == null) ? "" : paramString);
    if (consented() && !myId().isEmpty())
      heartbeat(paramJval -> {
          
          }); 
  }
  
  public static int lastChat() {
    return Core.settings.getInt("lunarians-hub-chat-after", 0);
  }
  
  public static void setLastChat(int paramInt) {
    Core.settings.put("lunarians-hub-chat-after", Integer.valueOf(paramInt));
  }
  
  private static String randomHex(int paramInt) {
    byte[] arrayOfByte = new byte[paramInt];
    (new SecureRandom()).nextBytes(arrayOfByte);
    StringBuilder stringBuilder = new StringBuilder();
    for (byte b : arrayOfByte) {
      stringBuilder.append(String.format("%02x", new Object[] { Byte.valueOf(b) }));
    } 
    return stringBuilder.toString();
  }
  
  public static void wipeLocal() {
    Core.settings.remove("lunarians-hub-id");
    Core.settings.remove("lunarians-hub-token");
    Core.settings.remove("lunarians-hub-global-after");
    Core.settings.remove("lunarians-hub-chat-after");
    Core.settings.remove("lunarians-hub-avatar");
    Core.settings.remove("lunarians-hub-avatar-img");
    try {
      Skins.wipeLocal();
    } catch (Throwable throwable) {}
    blocked = false;
    pendingRequests = 0;
    newChat = 0;
  }
  
  private static Jval profile() {
    Jval jval = Jval.newObject();
    jval.add("name", Strings.stripColors((Vars.player == null) ? "" : Vars.player.name()));
    jval.add("avatar", avatar());
    String[] arrayOfString = currentServer();
    jval.add("server_name", arrayOfString[0]);
    jval.add("server_port", Jval.valueOf(Strings.parseInt(arrayOfString[1], 0)));
    return jval;
  }
  
  public static String[] currentServer() {
    try {
      if (Vars.net != null && Vars.net.client() && Vars.ui != null && Vars.ui.join != null) {
        String str = (String)Reflect.get(Vars.ui.join, "lastIp");
        Integer integer = (Integer)Reflect.get(Vars.ui.join, "lastPort");
        if (str != null && !str.isEmpty())
          return new String[] { str, String.valueOf((integer == null) ? 6567 : integer.intValue()) }; 
      } 
    } catch (Throwable throwable) {}
    return new String[] { "", "0" };
  }
  
  private static Http.HttpRequest req(String paramString1, String paramString2, Jval paramJval) {
    Http.HttpRequest httpRequest = Http.request(paramString1.equals("POST") ? Http.HttpMethod.POST : Http.HttpMethod.GET, "https://lunarians-hub.morajoetorajo2.workers.dev" + paramString2);
    httpRequest.timeout((paramJval != null && paramJval.toString().length() > 20000) ? 30000 : 8000);
    if (!token().isEmpty())
      httpRequest.header("Authorization", "Bearer " + myId() + "." + token()); 
    if (paramJval != null) {
      httpRequest.header("Content-Type", "application/json");
      httpRequest.content(paramJval.toString());
    } 
    return httpRequest;
  }
  
  public static void call(String paramString1, String paramString2, Jval paramJval, Cons<Jval> paramCons, Cons<String> paramCons1) {
    if (!consented())
      return; 
    req(paramString1, paramString2, paramJval).error(paramThrowable -> {
          String str1 = "";
          try {
            if (paramThrowable instanceof Http.HttpStatusException) {
              Http.HttpStatusException httpStatusException = (Http.HttpStatusException)paramThrowable;
              Jval jval = Jval.read(httpStatusException.response.getResultAsString());
              str1 = jval.getString("error", "");
              if (jval.getBool("blocked", false))
                blocked = true; 
            } 
          } catch (Throwable throwable) {}
          String str2 = str1;
          Log.debug("[LunariansHub] @ @ falhou: @", new Object[] { paramString1, paramString2, paramThrowable.toString() });
          if (paramCons != null)
            Core.app.post(()); 
        }).submit(paramHttpResponse -> {
          try {
            Jval jval = Jval.read(paramHttpResponse.getResultAsString());
            Core.app.post(());
          } catch (Throwable throwable) {
            if (paramCons2 != null)
              Core.app.post(()); 
          } 
        });
  }
  
  public static void ensureRegistered(Runnable paramRunnable) {
    if (!consented())
      return; 
    if (!myId().isEmpty() && !token().isEmpty()) {
      if (paramRunnable != null)
        paramRunnable.run(); 
      return;
    } 
    String str = randomHex(16);
    Jval jval = profile();
    jval.add("id", str);
    req("POST", "/api/register", jval).error(paramThrowable -> Log.debug("[LunariansHub] registro falhou: @", new Object[] { paramThrowable.toString() })).submit(paramHttpResponse -> {
          Jval jval = Jval.read(paramHttpResponse.getResultAsString());
          if (jval.getBool("ok", false))
            Core.app.post(()); 
        });
  }
  
  public static void heartbeat(Cons<Jval> paramCons) {
    if (!consented() || myId().isEmpty())
      return; 
    Jval jval = profile();
    jval.add("global_after", Jval.valueOf(lastGlobal()));
    jval.add("chat_after", Jval.valueOf(lastChat()));
    call("POST", "/api/heartbeat", jval, paramJval -> {
          blocked = false;
          pendingRequests = paramJval.getInt("pending_requests", 0);
          newChat = paramJval.getInt("new_chat", 0);
          if (paramCons != null)
            paramCons.get(paramJval); 
        }paramString -> {
        
        });
  }
  
  public static void leaveAndDelete(Cons<Boolean> paramCons) {
    boolean bool = (!myId().isEmpty() && !token().isEmpty()) ? true : false;
    if (!bool) {
      setConsent(false);
      wipeLocal();
      paramCons.get(Boolean.valueOf(true));
      return;
    } 
    req("POST", "/api/delete", Jval.newObject())
      .error(paramThrowable -> Core.app.post(()))
      .submit(paramHttpResponse -> Core.app.post(()));
  }
}
