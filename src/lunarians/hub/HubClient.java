package lunarians.hub;

import arc.Core;
import arc.func.Cons;
import arc.func.ConsT;
import arc.util.Http;
import arc.util.Log;
import arc.util.Reflect;
import arc.util.Strings;
import arc.util.serialization.Base64Coder;
import arc.util.serialization.Jval;
import java.security.SecureRandom;
import mindustry.Vars;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/hub/HubClient.class */
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

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubClient$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Cons {
        public final /* synthetic */ String f$0;
        public final /* synthetic */ Cons f$1;

        public /* synthetic */ AnonymousClass0(String str, Cons cons) {
            this.f$0 = str;
            this.f$1 = cons;
        }

        public final void get(Object obj) {
            HubClient.lambda$uploadAvatar$0(this.f$0, this.f$1, (Jval) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubClient$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass1 implements Cons {
        public final /* synthetic */ Cons f$0;

        public final void get(Object obj) {
            HubClient.lambda$uploadAvatar$1(this.f$0, (String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubClient$10, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass10 implements Runnable {
        public final /* synthetic */ Cons f$0;

        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.get("bad_response");
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubClient$11, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass11 implements Runnable {
        public final /* synthetic */ Cons f$0;

        @Override // java.lang.Runnable
        public final void run() {
            HubClient.lambda$leaveAndDelete$15(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubClient$12, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass12 implements Runnable {
        public final /* synthetic */ Cons f$0;

        @Override // java.lang.Runnable
        public final void run() {
            HubClient.lambda$leaveAndDelete$13(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubClient$13, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass13 implements Runnable {
        public final /* synthetic */ Cons f$0;
        public final /* synthetic */ String f$1;

        public /* synthetic */ AnonymousClass13(Cons cons, String str) {
            this.f$0 = cons;
            this.f$1 = str;
        }

        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.get(this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubClient$14, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass14 implements Cons {
        public final /* synthetic */ Cons f$0;

        public final void get(Object obj) {
            HubClient.lambda$leaveAndDelete$14(this.f$0, (Throwable) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubClient$15, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass15 implements ConsT {
        public final /* synthetic */ Cons f$0;

        public final void get(Object obj) {
            HubClient.lambda$leaveAndDelete$16(this.f$0, (Http.HttpResponse) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubClient$16, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass16 implements Cons {
        public final void get(Object obj) {
            HubClient.lambda$setAvatar$2((Jval) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubClient$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass2 implements Cons {
        public final /* synthetic */ Cons f$0;

        public final void get(Object obj) {
            HubClient.lambda$heartbeat$11(this.f$0, (Jval) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubClient$3, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass3 implements Cons {
        public final void get(Object obj) {
            HubClient.lambda$heartbeat$12((String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubClient$4, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass4 implements Cons {
        public final void get(Object obj) {
            HubClient.lambda$ensureRegistered$8((Throwable) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubClient$5, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass5 implements ConsT {
        public final /* synthetic */ String f$0;
        public final /* synthetic */ Runnable f$1;

        public /* synthetic */ AnonymousClass5(String str, Runnable runnable) {
            this.f$0 = str;
            this.f$1 = runnable;
        }

        public final void get(Object obj) {
            HubClient.lambda$ensureRegistered$10(this.f$0, this.f$1, (Http.HttpResponse) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubClient$6, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass6 implements Cons {
        public final /* synthetic */ String f$0;
        public final /* synthetic */ String f$1;
        public final /* synthetic */ Cons f$2;

        public /* synthetic */ AnonymousClass6(String str, String str2, Cons cons) {
            this.f$0 = str;
            this.f$1 = str2;
            this.f$2 = cons;
        }

        public final void get(Object obj) {
            HubClient.lambda$call$4(this.f$0, this.f$1, this.f$2, (Throwable) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubClient$7, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass7 implements ConsT {
        public final /* synthetic */ Cons f$0;
        public final /* synthetic */ Cons f$1;

        public /* synthetic */ AnonymousClass7(Cons cons, Cons cons2) {
            this.f$0 = cons;
            this.f$1 = cons2;
        }

        public final void get(Object obj) {
            HubClient.lambda$call$7(this.f$0, this.f$1, (Http.HttpResponse) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubClient$8, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass8 implements Runnable {
        public final /* synthetic */ String f$0;
        public final /* synthetic */ Jval f$1;
        public final /* synthetic */ Runnable f$2;

        public /* synthetic */ AnonymousClass8(String str, Jval jval, Runnable runnable) {
            this.f$0 = str;
            this.f$1 = jval;
            this.f$2 = runnable;
        }

        @Override // java.lang.Runnable
        public final void run() {
            HubClient.lambda$ensureRegistered$9(this.f$0, this.f$1, this.f$2);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubClient$9, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass9 implements Runnable {
        public final /* synthetic */ Cons f$0;
        public final /* synthetic */ Jval f$1;

        public /* synthetic */ AnonymousClass9(Cons cons, Jval jval) {
            this.f$0 = cons;
            this.f$1 = jval;
        }

        @Override // java.lang.Runnable
        public final void run() {
            HubClient.lambda$call$5(this.f$0, this.f$1);
        }
    }

    public static boolean consented() {
        return "yes".equals(Core.settings.getString(K_CONSENT, ""));
    }

    public static boolean decided() {
        return !Core.settings.getString(K_CONSENT, "").isEmpty();
    }

    public static void setConsent(boolean z) {
        Core.settings.put(K_CONSENT, z ? "yes" : "no");
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

    public static void setLastGlobal(int i) {
        Core.settings.put(K_GLOBAL, Integer.valueOf(i));
    }

    public static String avatar() {
        return Core.settings.getString(K_AVATAR, "");
    }

    public static String avatarImg() {
        return Core.settings.getString(K_AVATAR_IMG, "");
    }

    public static void uploadAvatar(byte[] bArr, Cons<Boolean> cons) {
        String str = new String(Base64Coder.encode(bArr));
        Jval newObject = Jval.newObject();
        newObject.add("png", str);
        call("POST", "/api/avatar/upload", newObject, jval -> {
            Core.settings.put(K_AVATAR, "img");
            Core.settings.put(K_AVATAR_IMG, str);
            cons.get(true);
        }, str2 -> {
            cons.get(false);
        });
    }

    public static void setAvatar(String str) {
        Core.settings.remove(K_AVATAR_IMG);
        Core.settings.put(K_AVATAR, str == null ? "" : str);
        if (!consented() || myId().isEmpty()) {
            return;
        }
        heartbeat(jval -> {
        });
    }

    public static int lastChat() {
        return Core.settings.getInt(K_CHAT, 0);
    }

    public static void setLastChat(int i) {
        Core.settings.put(K_CHAT, Integer.valueOf(i));
    }

    private static String randomHex(int i) {
        byte[] bArr = new byte[i];
        new SecureRandom().nextBytes(bArr);
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            sb.append(String.format("%02x", Byte.valueOf(b)));
        }
        return sb.toString();
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
        } catch (Throwable th) {
        }
        blocked = false;
        pendingRequests = 0;
        newChat = 0;
    }

    private static Jval profile() {
        Jval newObject = Jval.newObject();
        newObject.add("name", Strings.stripColors(Vars.player == null ? "" : Vars.player.name()));
        newObject.add("avatar", avatar());
        newObject.add("server_name", currentServer()[0]);
        newObject.add("server_port", Jval.valueOf(Strings.parseInt(r0[1], 0)));
        return newObject;
    }

    public static String[] currentServer() {
        try {
            if (Vars.net != null && Vars.net.client() && Vars.ui != null && Vars.ui.join != null) {
                String str = (String) Reflect.get(Vars.ui.join, "lastIp");
                Integer num = (Integer) Reflect.get(Vars.ui.join, "lastPort");
                if (str != null && !str.isEmpty()) {
                    String[] strArr = new String[2];
                    strArr[0] = str;
                    strArr[1] = String.valueOf(num == null ? 6567 : num.intValue());
                    return strArr;
                }
            }
        } catch (Throwable th) {
        }
        return new String[]{"", "0"};
    }

    private static Http.HttpRequest req(String str, String str2, Jval jval) {
        Http.HttpRequest request = Http.request(str.equals("POST") ? Http.HttpMethod.POST : Http.HttpMethod.GET, BASE_URL + str2);
        request.timeout((jval == null || jval.toString().length() <= 20000) ? 8000 : 30000);
        if (!token().isEmpty()) {
            request.header("Authorization", "Bearer " + myId() + "." + token());
        }
        if (jval != null) {
            request.header("Content-Type", "application/json");
            request.content(jval.toString());
        }
        return request;
    }

    public static void call(String str, String str2, Jval jval, Cons<Jval> cons, Cons<String> cons2) {
        if (consented()) {
            req(str, str2, jval).error(th -> {
                String str3 = "";
                try {
                    if (th instanceof Http.HttpStatusException) {
                        Jval read = Jval.read(((Http.HttpStatusException) th).response.getResultAsString());
                        str3 = read.getString("error", "");
                        if (read.getBool("blocked", false)) {
                            blocked = true;
                        }
                    }
                } catch (Throwable th) {
                }
                String str4 = str3;
                Log.debug("[LunariansHub] @ @ falhou: @", new Object[]{str, str2, th.toString()});
                if (cons2 != null) {
                    Core.app.post(() -> {
                        cons2.get(str4);
                    });
                }
            }).submit(httpResponse -> {
                try {
                    Jval read = Jval.read(httpResponse.getResultAsString());
                    Core.app.post(() -> {
                        if (cons != null) {
                            cons.get(read);
                        }
                    });
                } catch (Throwable th2) {
                    if (cons2 != null) {
                        Core.app.post(() -> {
                            cons2.get("bad_response");
                        });
                    }
                }
            });
        }
    }

    public static void ensureRegistered(Runnable runnable) {
        if (consented()) {
            if (!myId().isEmpty() && !token().isEmpty()) {
                if (runnable != null) {
                    runnable.run();
                }
            } else {
                String randomHex = randomHex(16);
                Jval profile = profile();
                profile.add("id", randomHex);
                req("POST", "/api/register", profile).error(th -> {
                    Log.debug("[LunariansHub] registro falhou: @", new Object[]{th.toString()});
                }).submit(httpResponse -> {
                    Jval read = Jval.read(httpResponse.getResultAsString());
                    if (read.getBool("ok", false)) {
                        Core.app.post(() -> {
                            Core.settings.put(K_ID, randomHex);
                            Core.settings.put(K_TOKEN, read.getString("token", ""));
                            if (runnable != null) {
                                runnable.run();
                            }
                        });
                    }
                });
            }
        }
    }

    public static void heartbeat(Cons<Jval> cons) {
        if (!consented() || myId().isEmpty()) {
            return;
        }
        Jval profile = profile();
        profile.add("global_after", Jval.valueOf(lastGlobal()));
        profile.add("chat_after", Jval.valueOf(lastChat()));
        call("POST", "/api/heartbeat", profile, jval -> {
            blocked = false;
            pendingRequests = jval.getInt("pending_requests", 0);
            newChat = jval.getInt("new_chat", 0);
            if (cons != null) {
                cons.get(jval);
            }
        }, str -> {
        });
    }

    public static void leaveAndDelete(Cons<Boolean> cons) {
        if ((myId().isEmpty() || token().isEmpty()) ? false : true) {
            req("POST", "/api/delete", Jval.newObject()).error(th -> {
                Core.app.post(() -> {
                    cons.get(false);
                });
            }).submit(httpResponse -> {
                Core.app.post(() -> {
                    setConsent(false);
                    wipeLocal();
                    cons.get(true);
                });
            });
            return;
        }
        setConsent(false);
        wipeLocal();
        cons.get(true);
    }
}
