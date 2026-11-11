/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.Events
 *  arc.graphics.Color
 *  arc.graphics.g2d.Draw
 *  arc.graphics.g2d.Fill
 *  arc.struct.ObjectFloatMap
 *  arc.struct.Seq
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.game.EventType$PlayerJoin
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.EventType$WorldLoadEvent
 *  mindustry.gen.Call
 *  mindustry.gen.Groups
 *  mindustry.gen.Player
 *  mindustry.gen.Unit
 *  mindustry.ui.Fonts
 */
package lunarians;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.struct.ObjectFloatMap;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.ui.Fonts;

public final class LunariansPresence {
    public static final String PACKET = "lunarians-user";
    public static final String KEY = "lunarians-presence";
    private static final float PING_EVERY = 720.0f;
    private static final float STALE = 1500.0f;
    private static final Color AURA = Color.valueOf((String)"9b30ff");
    private static final ObjectFloatMap<String> seen = new ObjectFloatMap();
    private static float nextPing;
    private static boolean inited;
    private static boolean sentThisMap;
    public static final String MARK = "\u200b\u2060\u200c";

    private LunariansPresence() {
    }

    public static boolean enabled() {
        return Core.settings.getBool(KEY, false);
    }

    public static String stripMark(String string) {
        return string == null ? null : string.replace(MARK, "");
    }

    public static void applyName() {
        try {
            String string = LunariansPresence.stripMark(Core.settings.getString("name", ""));
            if (string == null || string.isEmpty()) {
                return;
            }
            Object object = string;
            if (LunariansPresence.enabled()) {
                int n = Math.max(1, 40 - MARK.length());
                object = (string.length() > n ? string.substring(0, n) : string) + MARK;
            }
            Core.settings.put("name", object);
            if (Vars.player != null) {
                Vars.player.name = object;
            }
        }
        catch (Throwable throwable) {
            Log.err((String)"[Lunarians] presence applyName", (Throwable)throwable);
        }
    }

    public static void setEnabled(boolean bl) {
        Core.settings.put(KEY, (Object)bl);
        LunariansPresence.applyName();
        if (bl) {
            LunariansPresence.registerHostRelay();
            nextPing = 0.0f;
        }
        if (!bl) {
            seen.clear();
            sentThisMap = false;
            nextPing = 0.0f;
        }
    }

    public static void init() {
        if (inited) {
            return;
        }
        inited = true;
        try {
            if (Vars.netClient != null) {
                Vars.netClient.addPacketHandler(PACKET, LunariansPresence::onPacket);
            }
        }
        catch (Throwable throwable) {
            Log.err((String)"[Lunarians] presence packet handler", (Throwable)throwable);
        }
        try {
            Events.on(EventType.PlayerJoin.class, playerJoin -> LunariansPresence.registerHostRelay());
            LunariansPresence.registerHostRelay();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        LunariansPresence.applyName();
        Events.run((Object)EventType.Trigger.update, LunariansPresence::update);
        Events.run((Object)EventType.Trigger.draw, LunariansPresence::draw);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            seen.clear();
            sentThisMap = false;
            nextPing = Time.time + 180.0f;
            LunariansPresence.applyName();
            LunariansPresence.registerHostRelay();
        });
        Log.info((Object)"[Lunarians] Presence ready (default OFF).");
    }

    private static void registerHostRelay() {
        if (!LunariansPresence.enabled()) {
            return;
        }
        try {
            if (Vars.netServer == null) {
                return;
            }
            Vars.netServer.addPacketHandler(PACKET, (player, string) -> {
                if (player == null || !LunariansPresence.enabled()) {
                    return;
                }
                Object object = string == null ? "" : string;
                String string2 = object;
                if (!((String)object).contains("|")) {
                    object = player.id + "|" + LunariansPresence.safe(player.name) + "|" + LunariansPresence.safe(player.uuid());
                }
                try {
                    Call.clientPacketReliable((String)PACKET, (String)object);
                }
                catch (Throwable throwable) {
                    try {
                        Call.clientPacketUnreliable((String)PACKET, (String)object);
                    }
                    catch (Throwable throwable2) {
                        // empty catch block
                    }
                }
            });
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static void onPacket(String string) {
        if (!LunariansPresence.enabled()) {
            return;
        }
        if (string == null || string.isEmpty()) {
            return;
        }
        try {
            String string2 = string;
            int n = string.indexOf(124);
            if (n > 0) {
                string2 = string.substring(0, n);
            }
            if (Vars.player != null && string2.equals(String.valueOf(Vars.player.id))) {
                return;
            }
            try {
                if (Vars.player != null && Vars.player.uuid() != null && string.contains(Vars.player.uuid())) {
                    return;
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            seen.put((Object)string2, Time.time);
            String[] stringArray = string.split("\\|");
            if (stringArray.length >= 1 && stringArray[0].length() > 0) {
                seen.put((Object)stringArray[0], Time.time);
            }
            if (stringArray.length >= 3 && stringArray[2].length() > 2) {
                seen.put((Object)stringArray[2], Time.time);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static void update() {
        if (!LunariansPresence.enabled()) {
            return;
        }
        if (Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
            return;
        }
        if (!Vars.net.active()) {
            return;
        }
        if (Time.time < nextPing) {
            return;
        }
        nextPing = Time.time + 720.0f;
        String string2 = Vars.player.id + "|" + LunariansPresence.safe(Vars.player.name) + "|" + LunariansPresence.safe(Vars.player.uuid());
        try {
            Call.serverPacketReliable((String)PACKET, (String)string2);
        }
        catch (Throwable throwable) {
            try {
                Call.serverPacketUnreliable((String)PACKET, (String)string2);
            }
            catch (Throwable throwable2) {
                // empty catch block
            }
        }
        try {
            if (Vars.netServer != null) {
                Call.clientPacketReliable((String)PACKET, (String)string2);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        sentThisMap = true;
        Seq seq = new Seq();
        seen.keys().toSeq().each(string -> {
            if (Time.time - seen.get(string, 0.0f) > 1500.0f) {
                seq.add(string);
            }
        });
        seq.each(object -> seen.remove((Object)((String)object), 0.0f));
    }

    private static boolean isMarked(Player player) {
        if (player == null) {
            return false;
        }
        if (player.name != null && player.name.contains(MARK)) {
            return true;
        }
        if (seen.containsKey((Object)String.valueOf(player.id))) {
            return true;
        }
        try {
            if (player.uuid() != null && seen.containsKey((Object)player.uuid())) {
                return true;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return false;
    }

    private static void draw() {
        if (!LunariansPresence.enabled()) {
            return;
        }
        if (Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
            return;
        }
        if (!Vars.net.active()) {
            return;
        }
        float f = Draw.z();
        for (Player player : Groups.player) {
            Unit unit;
            if (player == null || player == Vars.player || !LunariansPresence.isMarked(player) || (unit = player.unit()) == null || !unit.isValid()) continue;
            float f2 = Math.max(12.0f, unit.hitSize * 0.9f + 6.0f);
            Draw.z((float)109.99f);
            Draw.color((Color)AURA, (float)0.18f);
            Fill.circle((float)unit.x, (float)unit.y, (float)f2);
            Draw.color((Color)AURA, (float)0.1f);
            Fill.circle((float)unit.x, (float)unit.y, (float)(f2 * 1.35f));
            Draw.z((float)120.0f);
            try {
                Fonts.outline.draw((CharSequence)"Lunarians Team User", unit.x, unit.y + f2 + 6.0f, AURA, 0.22f, false, 1);
            }
            catch (Throwable throwable) {}
        }
        Draw.color();
        Draw.z((float)f);
    }

    private static String safe(String string) {
        if (string == null) {
            return "";
        }
        return string.replace("|", "").replace("\n", "");
    }
}

