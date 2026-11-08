/*
 * Unit-to-unit conversations (client-only, toggleable).
 * - Keeps 5 tier-based conversations per unit type.
 * - Adds 5 unique per-pair conversations for every unit pair (generated/cached).
 * - T1-T3: lots of personality. T4-T5: more serious and interested.
 * - Variation by partner/unit; texts in English.
 * Proximity rules: units near each other, not crowded, away from player.
 */
package lunarians;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.struct.IntFloatMap;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.type.UnitType;
import mindustry.ui.Fonts;

public class UnitConversations {
    private static final String KEY = "lunarians-unit-conversations";
    private static final float CHECK_INTERVAL = 60f * 7f;
    private static final float LINE_TIME = 60f * 3.2f;
    private static final float UNIT_COOLDOWN = 60f * 28f;
    private static final float PLAYER_MIN_DST = 18f * 8f;
    private static final float PAIR_MIN_DST = 3.5f * 8f;
    private static final float PAIR_MAX_DST = 14f * 8f;
    private static final int MAX_NEARBY = 5;
    private static final float NEARBY_RAD = 10f * 8f;

    private static float timer = 0f;
    private static final IntFloatMap cooldowns = new IntFloatMap();
    private static final ObjectMap<String, String[][]> typeCache = new ObjectMap<>();
    private static final ObjectMap<String, String[][]> pairCache = new ObjectMap<>();
    private static Active active = null;

    private static class Active {
        final Unit a, b;
        final String[] lines;
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
                if (pool.size >= 45) return;
                if (!validUnit(u) || u == Vars.player.unit()) return;
                if (Mathf.dst(u.x, u.y, Vars.player.x, Vars.player.y) < PLAYER_MIN_DST) return;
                if (cooldowns.get(u.id, -9999f) > Time.time - UNIT_COOLDOWN) return;
                pool.add(u);
            });
            if (pool.size < 2) return;
            for (int tries = 0; tries < 16; tries++) {
                Unit a = pool.random();
                if (a == null) continue;
                Unit b = findPartner(a, pool);
                if (b == null || a.type == null || b.type == null) continue;
                // keep ready per-type conversations and add per-pair conversations
                String[][] typeConvs = getTypeConversations(a.type);
                String[][] pairConvs = getPairConversations(a.type, b.type);
                String[][] poolConvs = Mathf.chance(0.65f) ? pairConvs : typeConvs;
                if (poolConvs == null || poolConvs.length == 0) poolConvs = (poolConvs == pairConvs ? typeConvs : pairConvs);
                if (poolConvs == null || poolConvs.length == 0) continue;
                int idx = Mathf.random(0, poolConvs.length - 1);
                String[] base = poolConvs[idx];
                String la = base.length > 0 ? fmt(base[0], a, b) : "...";
                String lb = base.length > 1 ? fmt(base[1], b, a) : (base.length > 0 ? fmt("{you}: Copy that.", b, a) : "...");
                active = new Active(a, b, new String[]{la, lb});
                cooldowns.put(a.id, Time.time);
                cooldowns.put(b.id, Time.time);
                return;
            }
        } catch (Throwable ignored) {}
    }

    private static Unit findPartner(Unit a, Seq<Unit> pool) {
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
            return b;
        }
        return null;
    }

    private static String uname(UnitType t) {
        if (t == null) return "Unit";
        try { if (t.localizedName != null && !t.localizedName.isEmpty()) return t.localizedName; } catch (Throwable ignored) {}
        return t.name != null ? t.name : "Unit";
    }

    private static String role(UnitType t) {
        if (t == null) return "unit";
        try { if (t.flying) return "air unit"; } catch (Throwable ignored) {}
        try { if (t.naval) return "naval unit"; } catch (Throwable ignored) {}
        try { if (t.constructor != null && t.constructor.get() != null && t.constructor.get().canDrown()) return "ground unit"; } catch (Throwable ignored) {}
        return "ground unit";
    }

    private static String fmt(String s, Unit speaker, Unit other) {
        if (s == null) return "...";
        String sn = speaker != null && speaker.type != null ? uname(speaker.type) : "Unit";
        String on = other != null && other.type != null ? uname(other.type) : "friend";
        String sr = speaker != null && speaker.type != null ? role(speaker.type) : "unit";
        String or = other != null && other.type != null ? role(other.type) : "unit";
        return s.replace("{me}", sn).replace("{you}", on).replace("{myrole}", sr).replace("{yourrole}", or);
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
            float cx = speaker.x;
            float cy = speaker.y + speaker.hitSize / 2f + 12f;
            float padX = 4f, padY = 2.5f;
            float bw = Math.max(18f, text.length() * 2.1f + padX * 2f);
            float bh = 8f + padY * 2f;
            // square conversation bubble
            Draw.color(0f, 0f, 0f, 0.55f * alpha);
            Fill.rect(cx, cy, bw, bh);
            Color border = tierOf(speaker.type) >= 4 ? Color.valueOf("cfe8ff") : Color.valueOf("fff4c2");
            Draw.color(border.r, border.g, border.b, 0.75f * alpha);
            Lines.stroke(1f);
            Lines.rect(cx - bw / 2f, cy - bh / 2f, bw, bh);
            Color c = border;
            Fonts.outline.draw((CharSequence)text, cx, cy + 1f, c, 0.22f * alpha, false, 1);
            Draw.reset();
        } catch (Throwable ignored) {}
    }

    private static int tierOf(UnitType t) {
        if (t == null) return 1;
        String n = t.name == null ? "" : t.name.toLowerCase();
        if (n.contains("reign") || n.contains("corvus") || n.contains("navanax") || n.contains("omura") || n.contains("toxopid") || n.contains("eclipse") || n.contains("oct") || n.contains("disrupt") || n.contains("conquer") || n.contains("collaris")) return 5;
        if (n.contains("scepter") || n.contains("vela") || n.contains("arkyid") || n.contains("quad") || n.contains("antumbra") || n.contains("sei") || n.contains("aegires") || n.contains("quell") || n.contains("anthicus") || n.contains("tecta")) return 4;
        if (n.contains("fortress") || n.contains("spiroct") || n.contains("zenith") || n.contains("mega") || n.contains("bryde") || n.contains("cyerce") || n.contains("quasar") || n.contains("precept") || n.contains("obviate") || n.contains("cleroi")) return 3;
        if (n.contains("mace") || n.contains("atrax") || n.contains("horizon") || n.contains("poly") || n.contains("minke") || n.contains("pulsar") || n.contains("oxynoe") || n.contains("locus") || n.contains("avert") || n.contains("stell")) return 2;
        return 1;
    }

    // --- 5 conversations per unit type (kept) ---
    private static String[][] getTypeConversations(UnitType t) {
        if (t == null) return new String[0][];
        String key = t.name;
        if (typeCache.containsKey(key)) return typeCache.get(key);
        String[][] c = generateType(t);
        typeCache.put(key, c);
        return c;
    }

    private static String[][] generateType(UnitType t) {
        String name = uname(t);
        int tier = tierOf(t);
        if (tier >= 4) {
            return new String[][]{
                {name + ": Maintaining formation. Status, {you}?", "{you}: Operational. No hostile contacts nearby."},
                {name + ": Route to the objective looks exposed.", "{you}: Understood. I can adjust and cover your flank."},
                {name + ": I am tracking unusual terrain features ahead.", "{you}: Noted. Proceed with caution and report changes."},
                {name + ": Resource timing and position are within plan.", "{you}: Confirmed. Ready to move when you signal."},
                {name + ": Keep communications clear and concise.", "{you}: Copy. Sensors steady and focused."}
            };
        }
        return new String[][]{
            {name + ": Hey {you}! Race you to that spot?", "{you}: You're on! ...wait, which spot?"},
            {name + ": I swear that conveyor just winked at me.", "{you}: Ha! Maybe it's happy to see us working."},
            {name + ": If I spin fast enough, do I become a drill?", "{you}: Please don't. Last time it got messy."},
            {name + ": *dramatic pose* The battlefield chose me today!", "{you}: Sure, hero. Just watch for turrets."},
            {name + ": I brought snacks! Imaginary snacks, but still.", "{you}: Best kind. No crumbs in the core."}
        };
    }

    // --- 5 unique per-pair conversations (added, for all pairs) ---
    private static String[][] getPairConversations(UnitType a, UnitType b) {
        if (a == null || b == null) return new String[0][];
        String k1 = a.name == null ? "?" : a.name;
        String k2 = b.name == null ? "?" : b.name;
        String key = k1.compareTo(k2) <= 0 ? (k1 + "|" + k2) : (k2 + "|" + k1);
        if (pairCache.containsKey(key)) return pairCache.get(key);
        String[][] c = generatePair(a, b);
        pairCache.put(key, c);
        return c;
    }

    private static String[][] generatePair(UnitType a, UnitType b) {
        UnitType first = (a.name != null && b.name != null && a.name.compareTo(b.name) <= 0) ? a : b;
        UnitType second = (first == a) ? b : a;
        String n1 = uname(first), n2 = uname(second);
        int t1 = tierOf(first), t2 = tierOf(second);
        String r1 = role(first), r2 = role(second);
        // Variation seed from pair
        int v = Math.abs((n1 + ":" + n2).hashCode());

        String[][] playful = new String[][]{
            {n1 + ": Hey " + n2 + "! Two " + shortRole(r1,r2) + " on one mission?", n2 + ": Yeah! Teamwork makes the chaos prettier."},
            {n1 + ": " + n2 + ", do you think the core watches us like a sitcom?", n2 + ": If it does, we are definitely the funny episode."},
            {n1 + ": Psst, " + n2 + "... I found a totally safe shortcut. Maybe.", n2 + ": With you, 'maybe' means adventure! I'm in."},
            {n1 + ": " + n2 + ", bet you can't make a cooler entrance than me.", n2 + ": Challenge accepted. Try not to trip on a router."},
            {n1 + ": I like working with you, " + n2 + ". You're fun under fire.", n2 + ": Same here, " + n1 + "! Let's make this look easy."}
        };
        String[][] mixed = new String[][]{
            {n1 + ": " + n2 + ", I have an idea. A loud one.", n2 + ": I prefer effective. But explain it quickly."},
            {n1 + ": You look serious, " + n2 + ". Everything clear?", n2 + ": Clear enough. Stay close and follow the plan."},
            {n1 + ": " + n2 + ", can a " + r1 + " and a " + r2 + " pull this off?", n2 + ": With coordination, yes. Keep your focus sharp."},
            {n1 + ": I spotted something interesting over there, " + n2 + "!", n2 + ": Interesting can mean dangerous. I will verify first."},
            {n1 + ": " + n2 + ", you handle the calm part, I'll handle the style.", n2 + ": Fine. Just make sure style doesn't cost us the objective."}
        };
        String[][] serious = new String[][]{
            {n1 + ": " + n2 + ", confirm current status and heading.", n2 + ": Status green. Holding position and monitoring threats."},
            {n1 + ": Path ahead may have turret coverage. Recommendations?", n2 + ": Suggest a wider approach and staggered movement."},
            {n1 + ": " + n2 + ", as a " + r2 + " you have a better angle here.", n2 + ": Acknowledged. I will scout while you cover the approach."},
            {n1 + ": Objective proximity is improving. Keep spacing tight.", n2 + ": Understood. Maintaining formation and comms discipline."},
            {n1 + ": Any anomalies in the last sector, " + n2 + "?", n2 + ": Minor resistance only. Data logged. Proceed when ready."}
        };

        boolean s1 = t1 >= 4, s2 = t2 >= 4;
        String[][] base = (s1 && s2) ? serious : (!s1 && !s2 ? playful : mixed);
        // add pair-specific variation (5 unique lines per pair kept, with small flavor shifts)
        String[][] out = new String[5][2];
        for(int i=0;i<5;i++){
            out[i][0] = vary(base[i][0], n1, n2, t1, t2, v, i);
            out[i][1] = vary(base[i][1], n2, n1, t2, t1, v, i + 7);
        }
        return out;
    }

    private static String shortRole(String r1, String r2){
        if(r1.equals(r2)) return r1 + "s";
        return "units";
    }

    private static String vary(String s, String me, String you, int myTier, int yourTier, int seed, int salt){
        // subtle variation per pair/conversation while keeping English text natural
        int pick = Math.abs((seed ^ (salt * 1103515245)) % 4);
        if(pick == 1 && myTier < 4) s = s.replace("!", "...");
        else if(pick == 2 && myTier >= 4) s = s.replace("Proceed", "Advance");
        else if(pick == 3) s = s.replace("you", you).replace(you + "!", you + ".");
        // ensure names present feel pair-unique context (already embedded)
        return s;
    }
}
