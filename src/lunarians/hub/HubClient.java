/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.func.Cons
 *  arc.util.Http
 *  arc.util.Http$HttpMethod
 *  arc.util.Http$HttpRequest
 *  arc.util.Http$HttpStatusException
 *  arc.util.Log
 *  arc.util.Reflect
 *  arc.util.Strings
 *  arc.util.serialization.Base64Coder
 *  arc.util.serialization.Jval
 *  mindustry.Vars
 */
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
import lunarians.hub.Skins;
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
    public static volatile int pendingRequests = 0;
    public static volatile int newChat = 0;

    public static boolean consented() {
        return "yes".equals(Core.settings.getString(K_CONSENT, ""));
    }

    public static boolean decided() {
        return !Core.settings.getString(K_CONSENT, "").isEmpty();
    }

    public static void setConsent(boolean bl) {
        Core.settings.put(K_CONSENT, (Object)(bl ? "yes" : "no"));
    }

    public static String myId() {
        return Core.settings.getString(K_ID, "");
    }

    private static String token() {
        return Core.settings.getString(K_TOKEN, "");
    }

    public static int lastGlobal() {
        return Core.settings.getInt(K_GLOBAL, 0);
    }

    public static void setLastGlobal(int n) {
        Core.settings.put(K_GLOBAL, (Object)n);
    }

    public static String avatar() {
        return Core.settings.getString(K_AVATAR, "");
    }

    public static String avatarImg() {
        return Core.settings.getString(K_AVATAR_IMG, "");
    }

    public static void uploadAvatar(byte[] byArray, Cons<Boolean> cons) {
        String string2 = new String(Base64Coder.encode((byte[])byArray));
        Jval jval2 = Jval.newObject();
        jval2.add("png", string2);
        HubClient.call("POST", "/api/avatar/upload", jval2, (Cons<Jval>)((Cons)jval -> {
            Core.settings.put(K_AVATAR, (Object)"img");
            Core.settings.put(K_AVATAR_IMG, (Object)string2);
            cons.get((Object)true);
        }), (Cons<String>)((Cons)string -> cons.get((Object)false)));
    }

    public static void setAvatar(String string) {
        Core.settings.remove(K_AVATAR_IMG);
        Core.settings.put(K_AVATAR, (Object)(string == null ? "" : string));
        if (HubClient.consented() && !HubClient.myId().isEmpty()) {
            HubClient.heartbeat((Cons<Jval>)((Cons)jval -> {}));
        }
    }

    public static int lastChat() {
        return Core.settings.getInt(K_CHAT, 0);
    }

    public static void setLastChat(int n) {
        Core.settings.put(K_CHAT, (Object)n);
    }

    private static String randomHex(int n) {
        byte[] byArray = new byte[n];
        new SecureRandom().nextBytes(byArray);
        StringBuilder stringBuilder = new StringBuilder();
        for (byte by : byArray) {
            stringBuilder.append(String.format("%02x", by));
        }
        return stringBuilder.toString();
    }

    public static void wipeLocal() {
        Core.settings.remove(K_ID);
        Core.settings.remove(K_TOKEN);
        Core.settings.remove(K_GLOBAL);
        Core.settings.remove(K_CHAT);
        Core.settings.remove(K_AVATAR);
        Core.settings.remove(K_AVATAR_IMG);
        try {
            Skins.wipeLocal();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        blocked = false;
        pendingRequests = 0;
        newChat = 0;
    }

    private static Jval profile() {
        Jval jval = Jval.newObject();
        jval.add("name", Strings.stripColors((CharSequence)(Vars.player == null ? "" : Vars.player.name())));
        jval.add("avatar", HubClient.avatar());
        String[] stringArray = HubClient.currentServer();
        jval.add("server_name", stringArray[0]);
        jval.add("server_port", Jval.valueOf((long)Strings.parseInt((String)stringArray[1], (int)0)));
        return jval;
    }

    public static String[] currentServer() {
        try {
            if (Vars.net != null && Vars.net.client() && Vars.ui != null && Vars.ui.join != null) {
                String string = (String)Reflect.get((Object)Vars.ui.join, (String)"lastIp");
                Integer n = (Integer)Reflect.get((Object)Vars.ui.join, (String)"lastPort");
                if (string != null && !string.isEmpty()) {
                    return new String[]{string, String.valueOf(n == null ? 6567 : n)};
                }
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return new String[]{"", "0"};
    }

    private static Http.HttpRequest req(String string, String string2, Jval jval) {
        Http.HttpRequest httpRequest = Http.request((Http.HttpMethod)(string.equals("POST") ? Http.HttpMethod.POST : Http.HttpMethod.GET), (String)(BASE_URL + string2));
        httpRequest.timeout(jval != null && jval.toString().length() > 20000 ? 30000 : 8000);
        if (!HubClient.token().isEmpty()) {
            httpRequest.header("Authorization", "Bearer " + HubClient.myId() + "." + HubClient.token());
        }
        if (jval != null) {
            httpRequest.header("Content-Type", "application/json");
            httpRequest.content(jval.toString());
        }
        return httpRequest;
    }

    public static void call(String string, String string2, Jval jval, Cons<Jval> cons, Cons<String> cons2) {
        if (!HubClient.consented()) {
            return;
        }
        HubClient.req(string, string2, jval).error(throwable -> {
            String string3;
            String string4 = "";
            try {
                if (throwable instanceof Http.HttpStatusException) {
                    string3 = (Http.HttpStatusException)throwable;
                    Jval jval = Jval.read((String)((Http.HttpStatusException)string3).response.getResultAsString());
                    string4 = jval.getString("error", "");
                    if (jval.getBool("blocked", false)) {
                        blocked = true;
                    }
                }
            }
            catch (Throwable throwable2) {
                // empty catch block
            }
            string3 = string4;
            Log.debug((String)"[LunariansHub] @ @ falhou: @", (Object[])new Object[]{string, string2, throwable.toString()});
            if (cons2 != null) {
                Core.app.post(() -> cons2.get((Object)string3));
            }
        }).submit(httpResponse -> {
            block2: {
                try {
                    Jval jval = Jval.read((String)httpResponse.getResultAsString());
                    Core.app.post(() -> {
                        if (cons != null) {
                            cons.get((Object)jval);
                        }
                    });
                }
                catch (Throwable throwable) {
                    if (cons2 == null) break block2;
                    Core.app.post(() -> cons2.get((Object)"bad_response"));
                }
            }
        });
    }

    public static void ensureRegistered(Runnable runnable) {
        if (!HubClient.consented()) {
            return;
        }
        if (!HubClient.myId().isEmpty() && !HubClient.token().isEmpty()) {
            if (runnable != null) {
                runnable.run();
            }
            return;
        }
        String string = HubClient.randomHex(16);
        Jval jval = HubClient.profile();
        jval.add("id", string);
        HubClient.req("POST", "/api/register", jval).error(throwable -> Log.debug((String)"[LunariansHub] registro falhou: @", (Object[])new Object[]{throwable.toString()})).submit(httpResponse -> {
            Jval jval = Jval.read((String)httpResponse.getResultAsString());
            if (jval.getBool("ok", false)) {
                Core.app.post(() -> {
                    Core.settings.put(K_ID, (Object)string);
                    Core.settings.put(K_TOKEN, (Object)jval.getString("token", ""));
                    if (runnable != null) {
                        runnable.run();
                    }
                });
            }
        });
    }

    public static void heartbeat(Cons<Jval> cons) {
        if (!HubClient.consented() || HubClient.myId().isEmpty()) {
            return;
        }
        Jval jval2 = HubClient.profile();
        jval2.add("global_after", Jval.valueOf((long)HubClient.lastGlobal()));
        jval2.add("chat_after", Jval.valueOf((long)HubClient.lastChat()));
        HubClient.call("POST", "/api/heartbeat", jval2, (Cons<Jval>)((Cons)jval -> {
            blocked = false;
            pendingRequests = jval.getInt("pending_requests", 0);
            newChat = jval.getInt("new_chat", 0);
            if (cons != null) {
                cons.get(jval);
            }
        }), (Cons<String>)((Cons)string -> {}));
    }

    public static void leaveAndDelete(Cons<Boolean> cons) {
        boolean bl;
        boolean bl2 = bl = !HubClient.myId().isEmpty() && !HubClient.token().isEmpty();
        if (!bl) {
            HubClient.setConsent(false);
            HubClient.wipeLocal();
            cons.get((Object)true);
            return;
        }
        HubClient.req("POST", "/api/delete", Jval.newObject()).error(throwable -> Core.app.post(() -> cons.get((Object)false))).submit(httpResponse -> Core.app.post(() -> {
            HubClient.setConsent(false);
            HubClient.wipeLocal();
            cons.get((Object)true);
        }));
    }
}

