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
                int len = Math.min(base.length, 6);
                if (len < 2) len = base.length;
                String[] lines = new String[Math.max(2, len)];
                for (int li = 0; li < lines.length; li++) {
                    Unit speaker = (li % 2 == 0) ? a : b;
                    Unit other = (li % 2 == 0) ? b : a;
                    String raw = li < base.length ? base[li] : (li % 2 == 0 ? "{me}: ..." : "{you}: ...");
                    lines[li] = fmt(raw, speaker, other);
                }
                active = new Active(a, b, lines);
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
                {name + ": Maintaining formation. Status, {you}?", "{you}: Operational. No hostile contacts nearby.", name + ": Good. I am tracking a safer route to the objective.", "{you}: Acknowledged. I will cover your approach and report threats.", name + ": Keep spacing tight and conserve resources.", "{you}: Copy. Sensors steady and ready."},
                {name + ": Route to the objective looks exposed, {you}.", "{you}: I see the same risk. Turret coverage is possible ahead.", name + ": We should take a wider approach and move in stages.", "{you}: Agreed. I can scout the flank if you hold this line.", name + ": Proceed carefully and signal before we commit."},
                {name + ": I am tracking unusual terrain features ahead.", "{you}: Noted. That ground could slow a direct push.", name + ": We need to balance speed with safety.", "{you}: Understood. I will watch for ambush points and changes.", name + ": Report anything abnormal immediately.", "{you}: You will have my report in real time."},
                {name + ": Resource timing and position are within plan.", "{you}: Confirmed. Our current pace is sustainable.", name + ": The objective is closer, but threats may increase.", "{you}: Then we should keep formation and avoid overextending.", name + ": Ready to move when you signal."},
                {name + ": Keep communications clear and concise, {you}.", "{you}: Copy. Focus stays on the mission.", name + ": I am interested in how this sector responds to pressure.", "{you}: So am I. The patterns here are worth observing.", name + ": Good. We learn, adapt, and advance safely."}
            };
        }
        return new String[][]{
            {name + ": Hey {you}! Race you to that spot?", "{you}: You're on! ...wait, which spot?", name + ": The shiny one near the conveyors, obviously!", "{you}: Oh, that spot. I thought you meant the dangerous one.", name + ": Dangerous can still be fun if we're careful.", "{you}: Fine, but I'm blaming you if a turret complains."},
            {name + ": I swear that conveyor just winked at me.", "{you}: Ha! Maybe it's happy to see us working.", name + ": Or maybe it's planning something sneaky.", "{you}: A sneaky conveyor? Now I've heard everything.", name + ": If it steals my snacks, we riot.", "{you}: Deal. No one messes with imaginary snacks."},
            {name + ": If I spin fast enough, do I become a drill?", "{you}: Please don't. Last time it got messy.", name + ": But think of the efficiency!", "{you}: I am thinking about the cleanup.", name + ": Okay, okay... tiny spin. Just for science.", "{you}: That's still a spin! ...You know what, I'm watching."},
            {name + ": *dramatic pose* The battlefield chose me today!", "{you}: Sure, hero. Just watch for turrets.", name + ": A true hero fears no warning labels!", "{you}: Warning labels exist for a reason, you know.", name + ": Fine, I'll be a careful hero.", "{you}: That's the best kind. Lead the way, carefully."},
            {name + ": I brought snacks! Imaginary snacks, but still.", "{you}: Best kind. No crumbs in the core.", name + ": I even packed extra for a long mission.", "{you}: Wow, premium service from {me}.", name + ": Only because you're good company, {you}.", "{you}: Aww. Now let's not get blasted before snack time."}
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
        int v = Math.abs((n1 + ":" + n2).hashCode());

        String[][] playful = new String[][]{
            {n1 + ": Hey " + n2 + "! Two " + shortRole(r1,r2) + " on one mission?", n2 + ": Yeah! Teamwork makes the chaos prettier.", n1 + ": I call the dramatic entrance.", n2 + ": Only if I get the victory pose after.", n1 + ": Deal. Let's make the core proud."},
            {n1 + ": " + n2 + ", do you think the core watches us like a sitcom?", n2 + ": If it does, we are definitely the funny episode.", n1 + ": I hope it likes plot twists.", n2 + ": With us, every patrol is a plot twist.", n1 + ": Then stay close so we get a good ending."},
            {n1 + ": Psst, " + n2 + "... I found a totally safe shortcut. Maybe.", n2 + ": With you, 'maybe' means adventure! I'm in.", n1 + ": Great! I think it passes near fewer turrets.", n2 + ": 'Think' is doing a lot of work there.", n1 + ": Okay, we'll peek first. Team safety!"},
            {n1 + ": " + n2 + ", bet you can't make a cooler entrance than me.", n2 + ": Challenge accepted. Try not to trip on a router.", n1 + ": I never trip. I perform surprise landings.", n2 + ": Sure, sure. I'll bring style and balance.", n1 + ": Perfect combo. Let's go."},
            {n1 + ": I like working with you, " + n2 + ". You're fun under fire.", n2 + ": Same here, " + n1 + "! You keep things lively.", n1 + ": Even when things get rough, we figure it out.", n2 + ": That's the best part of being a team.", n1 + ": Ready for the next move?", n2 + ": Always."}
        };
        String[][] mixed = new String[][]{
            {n1 + ": " + n2 + ", I have an idea. A loud one.", n2 + ": I prefer effective. But explain it quickly.", n1 + ": We swing around that ridge and avoid the turret line.", n2 + ": That could work if we keep distance and timing.", n1 + ": See? Loud *and* smart.", n2 + ": We'll test that. Stay coordinated."},
            {n1 + ": You look serious, " + n2 + ". Everything clear?", n2 + ": Clear enough. Stay close and follow the plan.", n1 + ": Got it. I'll keep an eye on the fun... I mean, threats.", n2 + ": Good. Report anything that moves the wrong way.", n1 + ": You can count on me.", n2 + ": I know. Let's proceed."},
            {n1 + ": " + n2 + ", can a " + r1 + " and a " + r2 + " pull this off?", n2 + ": With coordination, yes. Keep your focus sharp.", n1 + ": I can handle my part if you guide the route.", n2 + ": I'll call the safer path. You respond on signal.", n1 + ": Sounds like a solid team.", n2 + ": It is. Move when ready."},
            {n1 + ": I spotted something interesting over there, " + n2 + "!", n2 + ": Interesting can mean dangerous. I will verify first.", n1 + ": Want me to come with you?", n2 + ": Yes, but keep a safe distance and stay alert.", n1 + ": Okay, careful curiosity mode activated.", n2 + ": That's the right mode."},
            {n1 + ": " + n2 + ", you handle the calm part, I'll handle the style.", n2 + ": Fine. Just make sure style doesn't cost us the objective.", n1 + ": No promises on style, but the objective is safe with us.", n2 + ": Good. I am interested to see this route choice.", n1 + ": Then let's give it a smart try.", n2 + ": Agreed. Forward, carefully."}
        };
        String[][] serious = new String[][]{
            {n1 + ": " + n2 + ", confirm current status and heading.", n2 + ": Status green. Holding position and monitoring threats.", n1 + ": I recommend a route with less turret exposure.", n2 + ": I agree. We should prioritize a safer approach.", n1 + ": Objective distance is decreasing. Keep formation.", n2 + ": Copy. Advancing with caution."},
            {n1 + ": Path ahead may have turret coverage. Recommendations?", n2 + ": Suggest a wider approach and staggered movement.", n1 + ": That should reduce direct risk to the core push.", n2 + ": Yes. I will mark danger zones as we move.", n1 + ": Maintain comms and report contacts.", n2 + ": Understood. Proceeding."},
            {n1 + ": " + n2 + ", as a " + r2 + " you have a better angle here.", n2 + ": Acknowledged. I will scout while you cover the approach.", n1 + ": Watch for crossfire and unstable terrain.", n2 + ": I am tracking both. The path is narrow but manageable.", n1 + ": Signal before we commit to the final line.", n2 + ": I will."},
            {n1 + ": Objective proximity is improving. Keep spacing tight.", n2 + ": Understood. Maintaining formation and comms discipline.", n1 + ": Enemy defenses may react as we close in.", n2 + ": Then we adjust route choice, not just speed.", n1 + ": Agreed. Safety and progress together.", n2 + ": Ready on your mark."},
            {n1 + ": Any anomalies in the last sector, " + n2 + "?", n2 + ": Minor resistance only. Data logged. Proceed when ready.", n1 + ": I am interested in the pattern of those defenses.", n2 + ": It suggests a predictable coverage gap.", n1 + ": We can use that gap on approach.", n2 + ": Confirmed. Let's move carefully."}
        };

        boolean s1 = t1 >= 4, s2 = t2 >= 4;
        String[][] base = (s1 && s2) ? serious : (!s1 && !s2 ? playful : mixed);
        String[][] out = new String[5][];
        for(int i=0;i<5;i++){
            String[] bl = base[i];
            String[] r = new String[bl.length];
            for(int j=0;j<bl.length;j++) r[j] = vary(bl[j], (j%2==0?n1:n2), (j%2==0?n2:n1), (j%2==0?t1:t2), (j%2==0?t2:t1), v, i*11 + j);
            out[i]=r;
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
