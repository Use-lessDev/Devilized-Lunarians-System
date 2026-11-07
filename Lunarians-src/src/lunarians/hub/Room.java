package lunarians.hub;

import arc.Events;
import arc.func.Cons;
import arc.struct.IntSeq;
import arc.struct.ObjectFloatMap;
import arc.util.Log;
import arc.util.Strings;
import arc.util.Time;
import arc.util.serialization.Jval;
import java.lang.reflect.Field;
import java.util.Iterator;
import mindustry.Vars;
import mindustry.game.EventType;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/hub/Room.class */
public class Room {
    private static boolean inited;
    private static boolean inRoom;
    private static boolean busy;
    private static float next;

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Room$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Cons {
        public final void get(Object obj) {
            Room.lambda$send$1((Jval) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Room$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass1 implements Cons {
        public final void get(Object obj) {
            Room.busy = false;
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Room$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass2 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            Room.$r8$lambda$AjsRolnsXE2PB-Ej8EgAsGvxeJk();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Room$3, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass3 implements Cons {
        public final void get(Object obj) {
            Room.lambda$init$0((EventType.WorldLoadEvent) obj);
        }
    }

    public static void init() {
        if (inited || Vars.headless) {
            return;
        }
        inited = true;
        AudioUi.init();
        Events.run(EventType.Trigger.update, Room::tick);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            next = Time.time + 120.0f;
        });
    }

    static void sendSoon() {
        next = Math.min(next, Time.time + 60.0f);
    }

    private static void tick() {
        if (Time.time < next || busy) {
            return;
        }
        next = Time.time + 900.0f;
        try {
            if (!HubClient.consented() || HubClient.myId().isEmpty() || Vars.player == null) {
                return;
            }
            boolean z = Vars.net != null && Vars.net.client() && Vars.state != null && Vars.state.isGame();
            String[] currentServer = HubClient.currentServer();
            int parseInt = Strings.parseInt(currentServer[1], 0);
            if (!z || currentServer[0].isEmpty() || parseInt <= 0) {
                if (inRoom) {
                    inRoom = false;
                    send("", 0);
                    return;
                }
                return;
            }
            inRoom = true;
            send(currentServer[0], parseInt);
        } catch (Throwable th) {
        }
    }

    private static void send(String str, int i) {
        busy = true;
        Jval newObject = Jval.newObject();
        newObject.add("server_name", str);
        newObject.add("server_port", Jval.valueOf(i));
        newObject.add("pid", Jval.valueOf(Vars.player.id));
        Jval newArray = Jval.newArray();
        IntSeq equippedApproved = Skins.equippedApproved();
        for (int i2 = 0; i2 < equippedApproved.size; i2++) {
            newArray.asArray().add(Jval.valueOf(equippedApproved.get(i2)));
        }
        newObject.add("skins", newArray);
        newObject.add("presence", Jval.valueOf(presenceOn()));
        HubClient.call("POST", "/api/presence", newObject, jval -> {
            busy = false;
            try {
                Iterator it = jval.get("peers").asArray().iterator();
                while (it.hasNext()) {
                    Jval jval = (Jval) it.next();
                    int i3 = jval.getInt("pid", -1);
                    if (i3 >= 0 && i3 != Vars.player.id) {
                        if (jval.getInt("presence", 0) == 1) {
                            feedPresence(i3);
                        }
                        Jval jval2 = jval.get("skins");
                        if (jval2 != null && jval2.isArray()) {
                            Skins.feedPeer(i3, jval2.asArray());
                        }
                    }
                }
            } catch (Throwable th) {
                Log.debug("[Lunarians Room] resposta invalida: @", new Object[]{th.toString()});
            }
        }, str2 -> {
            busy = false;
        });
    }

    private static boolean presenceOn() {
        try {
            return ((Boolean) Class.forName("lunarians.LunariansPresence").getMethod("enabled", new Class[0]).invoke(null, new Object[0])).booleanValue();
        } catch (Throwable th) {
            return false;
        }
    }

    private static void feedPresence(int i) {
        try {
            if (presenceOn()) {
                Field declaredField = Class.forName("lunarians.LunariansPresence").getDeclaredField("seen");
                declaredField.setAccessible(true);
                ((ObjectFloatMap) declaredField.get(null)).put(String.valueOf(i), Time.time);
            }
        } catch (Throwable th) {
        }
    }
}
