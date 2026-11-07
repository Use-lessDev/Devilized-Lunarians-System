/*
 * Unit-to-unit conversations (client-only).
 * T1-T3: lots of personality. T4-T5: more serious and interested.
 * 5 conversations per unit type (generated/cached), proximity rules, toggleable.
 */
package lunarians;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Font;
import arc.math.Mathf;
import arc.struct.IntFloatMap;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.ui.Fonts;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.type.UnitType;

public class UnitConversations {
    private static final String KEY = "lunarians-unit-conversations";
    private static final float CHECK_INTERVAL = 60f * 7f; // ~7s
    private static final float LINE_TIME = 60f * 3.2f;
    private static final float UNIT_COOLDOWN = 60f * 28f;
    private static final float PLAYER_MIN_DST = 18f * 8f;
    private static final float PAIR_MIN_DST = 3.5f * 8f;
    private static final float PAIR_MAX_DST = 14f * 8f;
    private static final int MAX_NEARBY = 5;
    private static final float NEARBY_RAD = 10f * 8f;

    private static float timer = 0f;
    private static final IntFloatMap cooldowns = new IntFloatMap();
    private static final ObjectMap<String, String[][]> convCache = new ObjectMap<>();
    private static Active active = null;

    private static class Active {
        final Unit a, b;
        final String[] lines; // alternating a,b,a,b...
        float t;
        Active(Unit a, Unit b, String[] lines) { this.a = a; this.b = b; this.lines = lines; this.t = 0f; }
    }

    public static boolean enabled() {
        return Core.settings.getBool(KEY, false);
    }

    public static void setEnabled(boolean on) {
        Core.settings.put(KEY, (Object)on);
    }

    public static void init() {
        if (Vars.headless) return;
        Events.run(EventType.Trigger.update, UnitConversations::update);
        try { Events.run(EventType.Trigger.draw, UnitConversations::draw); } catch (Throwable ignored) {}
    }

    private static void update() {
        try {
            if (!enabled() || Vars.state == null || !Vars.state.isGame() || Vars.player == null) return;
            if (active != null) {
                active.t += Time.delta;
                if (!validUnit(active.a) || !validUnit(active.b) || active.t > LINE_TIME * active.lines.length + 30f) active = null;
            }
            timer += Time.delta;
            if (timer < CHECK_INTERVAL || active != null) return;
            timer = Mathf.random(0f, 60f * 2f);
            tryStart();
        } catch (Throwable ignored) {}
    }

    private static boolean validUnit(Unit u) {
        return u != null && u.isValid() && !u.dead() && u.team == (Vars.player != null ? Vars.player.team() : null);
    }

    private static void tryStart() {
        try {
            Seq<Unit> pool = new Seq<>();
            Groups.unit.each(u -> {
                if (pool.size >= 40) return;
                if (!validUnit(u) || u == Vars.player.unit()) return;
                if (Mathf.dst(u.x, u.y, Vars.player.x, Vars.player.y) < PLAYER_MIN_DST) return;
                if (cooldowns.get(u.id, -9999f) > Time.time - UNIT_COOLDOWN) return;
                pool.add(u);
            });
            if (pool.size < 2) return;
            for (int tries = 0; tries < 14; tries++) {
                Unit a = pool.random();
                if (a == null) continue;
                Unit b = findPartner(a, pool);
                if (b == null) continue;
                String[][] convs = getConversations(a.type);
                if (convs == null || convs.length == 0) continue;
                int idx = Mathf.random(0, convs.length - 1);
                String[] base = convs[idx];
                // make a short exchange: 2 lines from the set, attributed alternating
                String la = base.length > 0 ? personalize(base[0], a, b) : "...";
                String lb = base.length > 1 ? personalize(base[1], b, a) : "...";
                active = new Active(a, b, new String[]{la, lb});
                cooldowns.put(a.id, Time.time);
                cooldowns.put(b.id, Time.time);
                return;
            }
        } catch (Throwable ignored) {}
    }

    private static Unit findPartner(Unit a, Seq<Unit> pool) {
        Unit best = null;
        for (int i = 0; i < pool.size; i++) {
            Unit b = pool.get(i);
            if (b == null || b == a || b.type == null) continue;
            float d = Mathf.dst(a.x, a.y, b.x, b.y);
            if (d < PAIR_MIN_DST || d > PAIR_MAX_DST) continue;
            if (Mathf.dst(b.x, b.y, Vars.player.x, Vars.player.y) < PLAYER_MIN_DST) continue;
            int nearby = 0;
            for (int j = 0; j < pool.size && nearby <= MAX_NEARBY; j++) {
                Unit o = pool.get(j);
                if (o != null && o != a && o != b && Mathf.dst(a.x, a.y, o.x, o.y) < NEARBY_RAD) nearby++;
            }
            if (nearby > MAX_NEARBY) continue;
            if (cooldowns.get(b.id, -9999f) > Time.time - UNIT_COOLDOWN) continue;
            best = b; break;
        }
        return best;
    }

    private static String personalize(String s, Unit speaker, Unit other) {
        if (s == null) return "...";
        String sn = speaker.type != null ? speaker.type.localizedName : "Unit";
        String on = other != null && other.type != null ? other.type.localizedName : "friend";
        return s.replace("{me}", sn).replace("{you}", on);
    }

    private static void draw() {
        try {
            if (active == null || !enabled()) return;
            if (!validUnit(active.a) || !validUnit(active.b)) { active = null; return; }
            int idx = (int)(active.t / LINE_TIME);
            if (idx < 0) idx = 0;
            if (idx >= active.lines.length) return;
            Unit speaker = (idx % 2 == 0) ? active.a : active.b;
            if (!validUnit(speaker)) speaker = active.a;
            String text = active.lines[idx];
            float alpha = 1f;
            float into = active.t - idx * LINE_TIME;
            if (into < 12f) alpha = into / 12f;
            if (into > LINE_TIME - 12f) alpha = Math.max(0f, (LINE_TIME - into) / 12f);
            Draw.z(Layer.playerName);
            Color c = tierOf(speaker.type) >= 4 ? Color.valueOf("cfe8ff") : Color.valueOf("fff4c2");
            Fonts.outline.draw((CharSequence)text, speaker.x, speaker.y + speaker.hitSize / 2f + 10f, c, 0.22f * alpha, false, 1);
            Draw.reset();
        } catch (Throwable ignored) {}
    }

    private static int tierOf(UnitType t) {
        if (t == null) return 1;
        String n = t.name == null ? "" : t.name.toLowerCase();
        // T5
        if (n.contains("reign") || n.contains("corvus") || n.contains("navanax") || n.contains("omura") || n.contains("toxopid") || n.contains("eclipse") || n.contains("oct") || n.contains("disrupt") || n.contains("conquer") || n.contains("collaris")) return 5;
        // T4
        if (n.contains("scepter") || n.contains("vela") || n.contains("arkyid") || n.contains("quad") || n.contains("antumbra") || n.contains("sei") || n.contains("aegires") || n.contains("quell") || n.contains("anthicus") || n.contains("tecta")) return 4;
        // T3
        if (n.contains("fortress") || n.contains("spiroct") || n.contains("zenith") || n.contains("mega") || n.contains("bryde") || n.contains("cyerce") || n.contains("quasar") || n.contains("precept") || n.contains("obviate") || n.contains("cleroi")) return 3;
        // T2
        if (n.contains("mace") || n.contains("atrax") || n.contains("horizon") || n.contains("poly") || n.contains("minke") || n.contains("pulsar") || n.contains("oxynoe") || n.contains("locus") || n.contains("avert") || n.contains("stell")) return 2;
        return 1;
    }

    private static String[][] getConversations(UnitType t) {
        if (t == null) return new String[0][];
        String key = t.name;
        if (convCache.containsKey(key)) return convCache.get(key);
        String[][] c = generate(t);
        convCache.put(key, c);
        return c;
    }

    private static String[][] generate(UnitType t) {
        String name = t.localizedName != null ? t.localizedName : (t.name != null ? t.name : "Unit");
        int tier = tierOf(t);
        boolean serious = tier >= 4;
        if (serious) {
            return new String[][]{
                {name + ": Maintaining formation. Status?", "{you}: Operational. No contacts nearby."},
                {name + ": Objective distance noted. We proceed carefully.", "{you}: Understood. Watching for threats."},
                {name + ": Interesting terrain ahead.", "{you}: Agreed. Adjusting path for safety."},
                {name + ": Resources and timing look acceptable.", "{you}: Confirmed. Ready when you are."},
                {name + ": Keep comms clear. Report anomalies.", "{you}: Copy. Sensors steady."}
            };
        } else {
            return new String[][]{
                {name + ": Hey {you}! Race you to that spot? :D", "{you}: You're on! ...wait, which spot?"},
                {name + ": I swear that conveyor winked at me.", "{you}: Haha, maybe it's happy to see us!"},
                {name + ": If I spin fast enough, do I become a drill?", "{you}: Please don't. Last time was messy."},
                {name + ": *dramatic pose* The battlefield chose me!", "{you}: Sure it did, hero. Watch the turrets."},
                {name + ": I brought snacks! ...imaginary snacks.", "{you}: Best kind. No crumbs in the core."}
            };
        }
    }
}
