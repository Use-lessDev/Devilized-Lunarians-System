/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Events
 *  arc.func.Cons
 *  arc.struct.IntSeq
 *  arc.struct.ObjectFloatMap
 *  arc.util.Log
 *  arc.util.Strings
 *  arc.util.Time
 *  arc.util.serialization.Jval
 *  mindustry.Vars
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.EventType$WorldLoadEvent
 */
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
import lunarians.hub.AudioUi;
import lunarians.hub.HubClient;
import lunarians.hub.Skins;
import mindustry.Vars;
import mindustry.game.EventType;

public class Room {
    private static boolean inited;
    private static boolean inRoom;
    private static boolean busy;
    private static float next;

    public static void init() {
        if (inited || Vars.headless) {
            return;
        }
        inited = true;
        AudioUi.init();
        Events.run((Object)EventType.Trigger.update, Room::tick);
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
            boolean bl = Vars.net != null && Vars.net.client() && Vars.state != null && Vars.state.isGame();
            String[] stringArray = HubClient.currentServer();
            int n = Strings.parseInt((String)stringArray[1], (int)0);
            if (!bl || stringArray[0].isEmpty() || n <= 0) {
                if (inRoom) {
                    inRoom = false;
                    Room.send("", 0);
                }
                return;
            }
            inRoom = true;
            Room.send(stringArray[0], n);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static void send(String string2, int n) {
        busy = true;
        Jval jval2 = Jval.newObject();
        jval2.add("server_name", string2);
        jval2.add("server_port", Jval.valueOf((int)n));
        jval2.add("pid", Jval.valueOf((int)Vars.player.id));
        Jval jval3 = Jval.newArray();
        IntSeq intSeq = Skins.equippedApproved();
        for (int i = 0; i < intSeq.size; ++i) {
            jval3.asArray().add((Object)Jval.valueOf((int)intSeq.get(i)));
        }
        jval2.add("skins", jval3);
        jval2.add("presence", Jval.valueOf((boolean)Room.presenceOn()));
        HubClient.call("POST", "/api/presence", jval2, (Cons<Jval>)((Cons)jval -> {
            busy = false;
            try {
                for (Jval jval2 : jval.get("peers").asArray()) {
                    Jval jval3;
                    int n = jval2.getInt("pid", -1);
                    if (n < 0 || n == Vars.player.id) continue;
                    if (jval2.getInt("presence", 0) == 1) {
                        Room.feedPresence(n);
                    }
                    if ((jval3 = jval2.get("skins")) == null || !jval3.isArray()) continue;
                    Skins.feedPeer(n, jval3.asArray());
                }
            }
            catch (Throwable throwable) {
                Log.debug((String)"[Lunarians Room] resposta invalida: @", (Object[])new Object[]{throwable.toString()});
            }
        }), (Cons<String>)((Cons)string -> {
            busy = false;
        }));
    }

    private static boolean presenceOn() {
        try {
            return (Boolean)Class.forName("lunarians.LunariansPresence").getMethod("enabled", new Class[0]).invoke(null, new Object[0]);
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    private static void feedPresence(int n) {
        try {
            if (Room.presenceOn()) {
                Field field = Class.forName("lunarians.LunariansPresence").getDeclaredField("seen");
                field.setAccessible(true);
                ((ObjectFloatMap)field.get(null)).put((Object)String.valueOf(n), Time.time);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }
}

