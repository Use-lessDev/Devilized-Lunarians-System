package lunarians;

import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.func.Cons2;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.struct.ObjectFloatMap;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import java.util.Iterator;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.ui.Fonts;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/LunariansPresence.class */
public final class LunariansPresence {
    public static final String PACKET = "lunarians-user";
    public static final String KEY = "lunarians-presence";
    private static final float PING_EVERY = 720.0f;
    private static final float STALE = 1500.0f;
    private static final Color AURA = Color.valueOf("9b30ff");
    private static final ObjectFloatMap<String> seen = new ObjectFloatMap<>();
    private static float nextPing;
    private static boolean inited;
    private static boolean sentThisMap;
    public static final String MARK = "\u200b\u2060\u200c";

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.LunariansPresence$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Cons {
        public final /* synthetic */ Seq f$0;

        public final void get(Object obj) {
            LunariansPresence.lambda$update$3(this.f$0, (String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.LunariansPresence$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass1 implements Cons {
        public final void get(Object obj) {
            LunariansPresence.lambda$update$4(obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.LunariansPresence$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass2 implements Cons {
        public final void get(Object obj) {
            LunariansPresence.$r8$lambda$FHJYouvmj7D8T9zeEuSyoysOwm8((String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.LunariansPresence$3, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass3 implements Cons {
        public final void get(Object obj) {
            LunariansPresence.registerHostRelay();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.LunariansPresence$4, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass4 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            LunariansPresence.$r8$lambda$JHVUr_vN5yMxpmXBSiwh5ngrZ8g();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.LunariansPresence$5, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass5 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            LunariansPresence.$r8$lambda$IqCF51e73y7hOcdne2g92IJtYUY();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.LunariansPresence$6, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass6 implements Cons {
        public final void get(Object obj) {
            LunariansPresence.lambda$init$1((EventType.WorldLoadEvent) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.LunariansPresence$7, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass7 implements Cons2 {
        public final void get(Object obj, Object obj2) {
            LunariansPresence.lambda$registerHostRelay$2((Player) obj, (String) obj2);
        }
    }

    private LunariansPresence() {
    }

    public static boolean enabled() {
        return Core.settings.getBool(KEY, false);
    }

    public static String stripMark(String str) {
        if (str == null) {
            return null;
        }
        return str.replace(MARK, "");
    }

    public static void applyName() {
        try {
            String stripMark = stripMark(Core.settings.getString("name", ""));
            if (stripMark == null || stripMark.isEmpty()) {
                return;
            }
            String str = stripMark;
            if (enabled()) {
                int max = Math.max(1, 40 - MARK.length());
                str = (stripMark.length() > max ? stripMark.substring(0, max) : stripMark) + "\u200b\u2060\u200c";
            }
            Core.settings.put("name", str);
            if (Vars.player != null) {
                Vars.player.name = str;
            }
        } catch (Throwable th) {
            Log.err("[Lunarians] presence applyName", th);
        }
    }

    public static void setEnabled(boolean z) {
        Core.settings.put(KEY, Boolean.valueOf(z));
        applyName();
        if (z) {
            registerHostRelay();
            nextPing = 0.0f;
        }
        if (!z) {
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
        } catch (Throwable th) {
            Log.err("[Lunarians] presence packet handler", th);
        }
        try {
            Events.on(EventType.PlayerJoin.class, playerJoin -> {
                registerHostRelay();
            });
            registerHostRelay();
        } catch (Throwable th2) {
        }
        applyName();
        Events.run(EventType.Trigger.update, LunariansPresence::update);
        Events.run(EventType.Trigger.draw, LunariansPresence::draw);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            seen.clear();
            sentThisMap = false;
            nextPing = Time.time + 180.0f;
            applyName();
            registerHostRelay();
        });
        Log.info("[Lunarians] Presence ready (default OFF).");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void registerHostRelay() {
        if (!enabled()) {
            return;
        }
        try {
            if (Vars.netServer == null) {
                return;
            }
            Vars.netServer.addPacketHandler(PACKET, (player, str) -> {
                if (player == null || !enabled()) {
                    return;
                }
                String str = str == null ? "" : str;
                if (!str.contains("|")) {
                    str = player.id + "|" + safe(player.name) + "|" + safe(player.uuid());
                }
                try {
                    Call.clientPacketReliable(PACKET, str);
                } catch (Throwable th) {
                    try {
                        Call.clientPacketUnreliable(PACKET, str);
                    } catch (Throwable th2) {
                    }
                }
            });
        } catch (Throwable th) {
        }
    }

    private static void onPacket(String str) {
        if (!enabled() || str == null || str.isEmpty()) {
            return;
        }
        try {
            String str2 = str;
            int indexOf = str.indexOf(124);
            if (indexOf > 0) {
                str2 = str.substring(0, indexOf);
            }
            if (Vars.player != null && str2.equals(String.valueOf(Vars.player.id))) {
                return;
            }
            try {
                if (Vars.player != null && Vars.player.uuid() != null) {
                    if (str.contains(Vars.player.uuid())) {
                        return;
                    }
                }
            } catch (Throwable th) {
            }
            seen.put(str2, Time.time);
            String[] split = str.split("\\|");
            if (split.length >= 1 && split[0].length() > 0) {
                seen.put(split[0], Time.time);
            }
            if (split.length >= 3 && split[2].length() > 2) {
                seen.put(split[2], Time.time);
            }
        } catch (Throwable th2) {
        }
    }

    private static void update() {
        if (!enabled() || Vars.state == null || !Vars.state.isGame() || Vars.player == null || !Vars.net.active() || Time.time < nextPing) {
            return;
        }
        nextPing = Time.time + PING_EVERY;
        String str = Vars.player.id + "|" + safe(Vars.player.name) + "|" + safe(Vars.player.uuid());
        try {
            Call.serverPacketReliable(PACKET, str);
        } catch (Throwable th) {
            try {
                Call.serverPacketUnreliable(PACKET, str);
            } catch (Throwable th2) {
            }
        }
        try {
            if (Vars.netServer != null) {
                Call.clientPacketReliable(PACKET, str);
            }
        } catch (Throwable th3) {
        }
        sentThisMap = true;
        Seq seq = new Seq();
        seen.keys().toSeq().each(str2 -> {
            if (Time.time - seen.get(str2, 0.0f) > STALE) {
                seq.add(str2);
            }
        });
        seq.each(obj -> {
            seen.remove((String) obj, 0.0f);
        });
    }

    private static boolean isMarked(Player player) {
        if (player == null) {
            return false;
        }
        if ((player.name != null && player.name.contains(MARK)) || seen.containsKey(String.valueOf(player.id))) {
            return true;
        }
        try {
            if (player.uuid() == null) {
                return false;
            }
            if (seen.containsKey(player.uuid())) {
                return true;
            }
            return false;
        } catch (Throwable th) {
            return false;
        }
    }

    private static void draw() {
        Unit unit;
        if (!enabled() || Vars.state == null || !Vars.state.isGame() || Vars.player == null || !Vars.net.active()) {
            return;
        }
        float z = Draw.z();
        Iterator it = Groups.player.iterator();
        while (it.hasNext()) {
            Player player = (Player) it.next();
            if (player != null && player != Vars.player && isMarked(player) && (unit = player.unit()) != null && unit.isValid()) {
                float max = Math.max(12.0f, (unit.hitSize * 0.9f) + 6.0f);
                Draw.z(109.99f);
                Draw.color(AURA, 0.18f);
                Fill.circle(unit.x, unit.y, max);
                Draw.color(AURA, 0.1f);
                Fill.circle(unit.x, unit.y, max * 1.35f);
                Draw.z(120.0f);
                try {
                    Fonts.outline.draw("Lunarians Team User", unit.x, unit.y + max + 6.0f, AURA, 0.22f, false, 1);
                } catch (Throwable th) {
                }
            }
        }
        Draw.color();
        Draw.z(z);
    }

    private static String safe(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("|", "").replace("\n", "");
    }
}
