/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.Events
 *  arc.audio.Music
 *  arc.files.Fi
 *  arc.func.Cons
 *  arc.graphics.Pixmap
 *  arc.graphics.Texture
 *  arc.graphics.Texture$TextureFilter
 *  arc.graphics.g2d.Draw
 *  arc.graphics.g2d.Lines
 *  arc.graphics.g2d.TextureRegion
 *  arc.math.Angles
 *  arc.math.Mathf
 *  arc.math.geom.Vec2
 *  arc.scene.Element
 *  arc.scene.style.Drawable
 *  arc.scene.style.TextureRegionDrawable
 *  arc.scene.ui.Image
 *  arc.scene.ui.Label
 *  arc.scene.ui.Slider
 *  arc.scene.ui.TextButton
 *  arc.scene.ui.TextField
 *  arc.scene.ui.layout.Table
 *  arc.struct.IntFloatMap
 *  arc.struct.IntMap
 *  arc.struct.IntMap$Entry
 *  arc.struct.IntSeq
 *  arc.struct.IntSet
 *  arc.struct.ObjectMap
 *  arc.struct.ObjectMap$Entries
 *  arc.struct.ObjectMap$Entry
 *  arc.struct.Seq
 *  arc.util.Log
 *  arc.util.Scaling
 *  arc.util.Strings
 *  arc.util.Time
 *  arc.util.Tmp
 *  arc.util.serialization.Base64Coder
 *  arc.util.serialization.Jval
 *  arc.util.serialization.Jval$JsonArray
 *  mindustry.Vars
 *  mindustry.entities.Leg
 *  mindustry.entities.units.WeaponMount
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.EventType$WorldLoadEvent
 *  mindustry.game.Team
 *  mindustry.gen.Groups
 *  mindustry.gen.Icon
 *  mindustry.gen.Legsc
 *  mindustry.gen.Mechc
 *  mindustry.gen.Unit
 *  mindustry.type.UnitType
 *  mindustry.type.Weapon
 *  mindustry.ui.FileChooser$FileChooserParams
 *  mindustry.ui.Styles
 *  mindustry.ui.dialogs.BaseDialog
 */
package lunarians.hub;

import arc.Core;
import arc.Events;
import arc.audio.Music;
import arc.files.Fi;
import arc.func.Cons;
import arc.graphics.Pixmap;
import arc.graphics.Texture;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Image;
import arc.scene.ui.Label;
import arc.scene.ui.Slider;
import arc.scene.ui.TextButton;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import arc.struct.IntFloatMap;
import arc.struct.IntMap;
import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Scaling;
import arc.util.Strings;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.serialization.Base64Coder;
import arc.util.serialization.Jval;
import java.util.Iterator;
import lunarians.UiStyle;
import lunarians.hub.AudioUi;
import lunarians.hub.HubClient;
import lunarians.hub.HubText;
import lunarians.hub.Room;
import mindustry.Vars;
import mindustry.entities.Leg;
import mindustry.entities.units.WeaponMount;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Groups;
import mindustry.gen.Icon;
import mindustry.gen.Legsc;
import mindustry.gen.Mechc;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.ui.FileChooser;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public class Skins {
    private static final String K_EQ = "lunarians-hub-skins-eq";
    private static final String K_SHOW = "lunarians-hub-skins-show";
    private static final float PEER_TTL = 6000.0f;
    private static final int MAX_EQUIPPED = 8;
    private static final Seq<Entry> mine = new Seq();
    private static boolean mineLoaded;
    private static final IntMap<Peer> peers;
    private static final ObjectMap<String, UnitPool> pools;
    private static final IntMap<TextureRegion> tex;
    private static final IntMap<Seq<Cons<TextureRegion>>> waiting;
    private static final IntFloatMap failedAt;
    private static final ObjectMap<UnitType, Seq<String>> weaponNames;
    private static final Vec2 tv1;
    private static final Vec2 tv2;
    private static boolean inited;
    private static boolean dirty;
    private static float nextPool;
    private static float nextMine;
    private static float nextMusic;
    private static final IntMap<Music> playing;
    private static final IntMap<Fi> files;
    private static final IntFloatMap audioFailed;
    private static final IntSet audioLoading;

    static boolean okUnit(String string) {
        return string != null && string.matches("[a-z0-9-]{1,40}");
    }

    static boolean okPart(String string) {
        return string != null && string.matches("body|legs|joint|legbase|base|weapon[0-9]{1,2}");
    }

    public static boolean showOthers() {
        return Core.settings.getBool(K_SHOW, true);
    }

    private static IntSeq equipped() {
        IntSeq intSeq = new IntSeq();
        for (String string : Core.settings.getString(K_EQ, "").split(",")) {
            if (!string.matches("\\d{1,9}")) continue;
            intSeq.add(Strings.parseInt((String)string, (int)0));
        }
        return intSeq;
    }

    private static void saveEquipped(IntSeq intSeq) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < intSeq.size; ++i) {
            if (i > 0) {
                stringBuilder.append(',');
            }
            stringBuilder.append(intSeq.get(i));
        }
        Core.settings.put(K_EQ, (Object)stringBuilder.toString());
        dirty = true;
        Room.sendSoon();
    }

    public static boolean using(int n) {
        return Skins.equipped().contains(n);
    }

    private static Entry mineById(int n) {
        for (Entry entry : mine) {
            if (entry.id != n) continue;
            return entry;
        }
        return null;
    }

    public static void equip(int n) {
        Entry entry = Skins.mineById(n);
        if (entry == null) {
            return;
        }
        IntSeq intSeq = Skins.equipped();
        IntSeq intSeq2 = new IntSeq();
        for (int i = 0; i < intSeq.size; ++i) {
            Entry entry2 = Skins.mineById(intSeq.get(i));
            if (entry2 == null || entry2.unit.equals(entry.unit) && entry2.part.equals(entry.part)) continue;
            intSeq2.add(entry2.id);
        }
        if (intSeq2.size >= 8) {
            Vars.ui.showInfoToast(HubText.get("skin.max"), 2.5f);
            return;
        }
        intSeq2.add(n);
        Skins.saveEquipped(intSeq2);
    }

    public static void unequip(int n) {
        IntSeq intSeq = Skins.equipped();
        intSeq.removeValue(n);
        Skins.saveEquipped(intSeq);
    }

    public static IntSeq equippedApproved() {
        IntSeq intSeq = new IntSeq();
        IntSeq intSeq2 = Skins.equipped();
        for (int i = 0; i < intSeq2.size && intSeq.size < 8; ++i) {
            if (Skins.mineById(intSeq2.get(i)) == null) continue;
            intSeq.add(intSeq2.get(i));
        }
        return intSeq;
    }

    public static void wipeLocal() {
        Core.settings.remove(K_EQ);
        mine.clear();
        mineLoaded = false;
        peers.clear();
        pools.clear();
        Skins.stopAllMusic();
        dirty = true;
    }

    private static Entry parse(Jval jval, int n) {
        try {
            Entry entry = new Entry();
            entry.id = jval.getInt("i", jval.getInt("id", 0));
            entry.unit = jval.getString("u", jval.getString("unit", ""));
            entry.part = jval.getString("p", jval.getString("part", "body"));
            entry.music = jval.getInt("m", jval.getInt("music", 0));
            entry.radius = Math.max(0, Math.min(30, jval.getInt("r", jval.getInt("music_radius", 0))));
            entry.owner = n;
            if (entry.id <= 0 || !Skins.okUnit(entry.unit) || !Skins.okPart(entry.part)) {
                return null;
            }
            if (entry.music < 0) {
                entry.music = 0;
            }
            return entry;
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    private static void updateMine(Jval.JsonArray jsonArray) {
        Jval jval2;
        mine.clear();
        for (Jval jval2 : jsonArray) {
            Entry entry;
            if (!"approved".equals(jval2.getString("status", "")) || (entry = Skins.parse(jval2, -1)) == null) continue;
            mine.add((Object)entry);
        }
        mineLoaded = true;
        Iterator iterator = Skins.equipped();
        jval2 = new IntSeq();
        for (int i = 0; i < ((IntSeq)iterator).size; ++i) {
            if (Skins.mineById(iterator.get(i)) == null) continue;
            jval2.add(iterator.get(i));
        }
        if (jval2.size != ((IntSeq)iterator).size) {
            Skins.saveEquipped((IntSeq)jval2);
        }
        dirty = true;
    }

    static void refreshMine() {
        if (!HubClient.consented() || HubClient.myId().isEmpty()) {
            return;
        }
        HubClient.call("GET", "/api/skins/mine", null, (Cons<Jval>)((Cons)jval -> Skins.updateMine(jval.get("skins").asArray())), (Cons<String>)((Cons)string -> {}));
    }

    static void feedPeer(int n, Jval.JsonArray jsonArray) {
        if (n < 0 || jsonArray == null) {
            return;
        }
        if (Skins.peers.size > 200 && !peers.containsKey(n)) {
            return;
        }
        Peer peer = (Peer)peers.get(n);
        if (peer == null) {
            peer = new Peer();
            peers.put(n, (Object)peer);
        }
        Seq seq = new Seq();
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = 0;
        for (Jval jval : jsonArray) {
            if (n2++ >= 8) break;
            Entry entry = Skins.parse(jval, n);
            if (entry == null) continue;
            seq.add((Object)entry);
            stringBuilder.append(entry.id).append(':').append(entry.music).append(':').append(entry.radius).append(',');
        }
        peer.seen = Time.time;
        if (!stringBuilder.toString().equals(peer.sig)) {
            peer.sig = stringBuilder.toString();
            peer.list = seq;
            dirty = true;
        }
    }

    private static void rebuildPools() {
        pools.clear();
        IntSeq intSeq = Skins.equipped();
        for (Object object : mine) {
            if (!intSeq.contains(((Entry)object).id)) continue;
            Skins.add((Entry)object);
        }
        if (Skins.showOthers()) {
            Object object;
            Iterator iterator = new Seq();
            for (IntMap.Entry entry : peers) {
                if (Time.time - ((Peer)entry.value).seen > 6000.0f) {
                    iterator.add(entry.key);
                    continue;
                }
                for (Entry entry2 : ((Peer)entry.value).list) {
                    Skins.add(entry2);
                }
            }
            object = iterator.iterator();
            while (object.hasNext()) {
                int n = (Integer)object.next();
                peers.remove(n);
            }
        }
        for (Object object : pools) {
            ((UnitPool)((ObjectMap.Entry)object).value).owners.sort();
        }
        dirty = false;
    }

    private static void add(Entry entry) {
        ObjectMap objectMap;
        UnitPool unitPool = (UnitPool)pools.get((Object)entry.unit);
        if (unitPool == null) {
            unitPool = new UnitPool();
            pools.put((Object)entry.unit, (Object)unitPool);
        }
        if ((objectMap = (ObjectMap)unitPool.byOwner.get(entry.owner)) == null) {
            objectMap = new ObjectMap();
            unitPool.byOwner.put(entry.owner, (Object)objectMap);
            unitPool.owners.add((Object)entry.owner);
        }
        if (!objectMap.containsKey((Object)entry.part)) {
            objectMap.put((Object)entry.part, (Object)entry);
        }
    }

    private static int pick(int n, int n2) {
        if (n2 <= 1) {
            return 0;
        }
        int n3 = n * -1640531535;
        n3 ^= n3 >>> 15;
        n3 *= -2048144789;
        n3 ^= n3 >>> 13;
        return Math.floorMod(n3, n2);
    }

    private static ObjectMap<String, Entry> skinsFor(Unit unit, UnitPool unitPool) {
        if (unitPool == null || unitPool.owners.size == 0) {
            return null;
        }
        int n = (Integer)unitPool.owners.get(Skins.pick(unit.id, unitPool.owners.size));
        return (ObjectMap)unitPool.byOwner.get(n);
    }

    public static void init() {
        if (inited || Vars.headless) {
            return;
        }
        inited = true;
        Events.run((Object)EventType.Trigger.drawOver, Skins::draw);
        Events.run((Object)EventType.Trigger.update, Skins::tick);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            peers.clear();
            dirty = true;
            Skins.stopAllMusic();
        });
    }

    private static void tick() {
        try {
            if (!HubClient.consented()) {
                if (Skins.pools.size > 0) {
                    pools.clear();
                    Skins.stopAllMusic();
                }
                return;
            }
            if (Time.time >= nextMine) {
                nextMine = Time.time + (mineLoaded ? 7200.0f : 600.0f);
                Skins.refreshMine();
            }
            if (dirty || Time.time >= nextPool) {
                nextPool = Time.time + 300.0f;
                Skins.rebuildPools();
            }
            if (Time.time >= nextMusic) {
                nextMusic = Time.time + 20.0f;
                Skins.musicTick();
            }
        }
        catch (Throwable throwable) {
            Log.debug((String)"[Lunarians Skins] tick: @", (Object[])new Object[]{throwable.toString()});
        }
    }

    static void fetch(int n, Cons<TextureRegion> cons) {
        boolean bl;
        TextureRegion textureRegion = (TextureRegion)tex.get(n);
        if (textureRegion != null) {
            cons.get((Object)textureRegion);
            return;
        }
        Seq seq = (Seq)waiting.get(n);
        boolean bl2 = bl = seq == null;
        if (bl) {
            seq = new Seq();
            waiting.put(n, (Object)seq);
        }
        seq.add(cons);
        if (bl) {
            HubClient.call("GET", "/api/skins/img?id=" + n, null, (Cons<Jval>)((Cons)jval -> {
                Seq seq = (Seq)waiting.remove(n);
                try {
                    Pixmap pixmap = new Pixmap(Base64Coder.decode((String)jval.getString("png", "")));
                    Texture texture = new Texture(pixmap);
                    texture.setFilter(Texture.TextureFilter.nearest);
                    pixmap.dispose();
                    TextureRegion textureRegion = new TextureRegion(texture);
                    tex.put(n, (Object)textureRegion);
                    if (seq != null) {
                        for (Cons cons : seq) {
                            cons.get((Object)textureRegion);
                        }
                    }
                }
                catch (Throwable throwable) {
                    failedAt.put(n, Time.time);
                }
            }), (Cons<String>)((Cons)string -> {
                waiting.remove(n);
                failedAt.put(n, Time.time);
            }));
        }
    }

    private static TextureRegion region(int n) {
        TextureRegion textureRegion2 = (TextureRegion)tex.get(n);
        if (textureRegion2 != null) {
            return textureRegion2;
        }
        if (waiting.containsKey(n) || failedAt.get(n, -100000.0f) + 3600.0f >= Time.time) {
            return null;
        }
        Skins.fetch(n, (Cons<TextureRegion>)((Cons)textureRegion -> {}));
        return null;
    }

    private static TextureRegion skin(ObjectMap<String, Entry> objectMap, String string) {
        if (objectMap == null) {
            return null;
        }
        Entry entry = (Entry)objectMap.get((Object)string);
        return entry == null ? null : Skins.region(entry.id);
    }

    private static void draw() {
        try {
            if (Skins.pools.size == 0 || !HubClient.consented() || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
                return;
            }
            Core.camera.bounds(Tmp.r1);
            Team team = Vars.player.team();
            float f = Draw.z();
            for (Unit unit : Groups.unit) {
                ObjectMap<String, Entry> objectMap;
                UnitPool unitPool;
                if (unit == null || !unit.isAdded() || unit.dead || unit.type == null || (unitPool = (UnitPool)pools.get((Object)unit.type.name)) == null) continue;
                Tmp.r2.set(Tmp.r1).grow(unit.hitSize * 2.0f + 16.0f);
                if (!Tmp.r2.contains(unit.x, unit.y) || unit.inFogTo(team) || (objectMap = Skins.skinsFor(unit, unitPool)) == null) continue;
                try {
                    Skins.drawUnit(unit, objectMap);
                }
                catch (Throwable throwable) {}
            }
            Draw.z((float)f);
            Draw.reset();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static void drawUnit(Unit unit, ObjectMap<String, Entry> objectMap) {
        Seq<String> seq;
        TextureRegion textureRegion;
        float f;
        UnitType unitType = unit.type;
        float f2 = f = unit.elevation > 0.5f || unitType.flying && unit.dead ? unitType.flyingLayer : unitType.groundLayer + Mathf.clamp((float)(unitType.hitSize / 4000.0f), (float)0.0f, (float)0.01f);
        if (unit instanceof Legsc) {
            textureRegion = Skins.skin(objectMap, "legs");
            seq = Skins.skin(objectMap, "legbase");
            WeaponMount[] weaponMountArray = Skins.skin(objectMap, "joint");
            TextureRegion textureRegion2 = Skins.skin(objectMap, "base");
            if (textureRegion != null || seq != null || weaponMountArray != null || textureRegion2 != null) {
                Skins.drawLegSkin(unit, (Legsc)unit, unitType, textureRegion, seq, (TextureRegion)weaponMountArray, textureRegion2, f - 0.01f);
            }
        } else if (unit instanceof Mechc) {
            textureRegion = Skins.skin(objectMap, "legs");
            seq = Skins.skin(objectMap, "base");
            if (textureRegion != null || seq != null) {
                Skins.drawMechSkin(unit, (Mechc)unit, unitType, textureRegion, seq, f - 0.01f);
            }
        }
        textureRegion = Skins.skin(objectMap, "body");
        if (textureRegion != null) {
            Draw.z((float)(f + 0.0105f));
            unitType.applyColor(unit);
            Draw.rect((TextureRegion)textureRegion, (float)unit.x, (float)unit.y, (float)(unit.rotation - 90.0f));
            Draw.reset();
        }
        if (unit.mounts != null) {
            seq = Skins.weaponNames(unitType);
            for (WeaponMount weaponMount : unit.mounts) {
                TextureRegion textureRegion3;
                Weapon weapon = weaponMount.weapon;
                if (weapon == null || weapon.name == null || weapon.name.isEmpty()) continue;
                int n = seq.indexOf((Object)weapon.name);
                TextureRegion textureRegion4 = textureRegion3 = n >= 0 ? Skins.skin(objectMap, "weapon" + n) : null;
                if (textureRegion3 == null && textureRegion != null && weapon.region != null && weapon.region.found()) {
                    textureRegion3 = weapon.region;
                }
                if (textureRegion3 == null) continue;
                Skins.drawMount(unit, weaponMount, weapon, textureRegion3, f);
            }
        }
        Draw.reset();
    }

    static Seq<String> weaponNames(UnitType unitType) {
        Seq seq = (Seq)weaponNames.get((Object)unitType);
        if (seq == null) {
            seq = new Seq();
            for (Weapon weapon : unitType.weapons) {
                if (weapon.name == null || weapon.name.isEmpty() || weapon.region == null || !weapon.region.found() || seq.contains((Object)weapon.name)) continue;
                seq.add((Object)weapon.name);
            }
            weaponNames.put((Object)unitType, (Object)seq);
        }
        return seq;
    }

    private static void drawMount(Unit unit, WeaponMount weaponMount, Weapon weapon, TextureRegion textureRegion, float f) {
        Draw.z((float)(f + 0.0106f + weapon.layerOffset));
        unit.type.applyColor(unit);
        float f2 = unit.rotation - 90.0f;
        float f3 = Mathf.pow((float)weaponMount.recoil, (float)weapon.recoilPow) * weapon.recoil;
        float f4 = f2 + (weapon.rotate ? weaponMount.rotation : weapon.baseRotation);
        float f5 = unit.x + Angles.trnsx((float)f2, (float)weapon.x, (float)weapon.y) + Angles.trnsx((float)f4, (float)0.0f, (float)(-f3));
        float f6 = unit.y + Angles.trnsy((float)f2, (float)weapon.x, (float)weapon.y) + Angles.trnsy((float)f4, (float)0.0f, (float)(-f3));
        float f7 = Draw.xscl;
        Draw.xscl *= (float)(-Mathf.sign((boolean)weapon.flipSprite));
        Draw.rect((TextureRegion)textureRegion, (float)f5, (float)f6, (float)f4);
        Draw.xscl = f7;
        Draw.reset();
    }

    private static void drawLegSkin(Unit unit, Legsc legsc, UnitType unitType, TextureRegion textureRegion, TextureRegion textureRegion2, TextureRegion textureRegion3, TextureRegion textureRegion4, float f) {
        Draw.z((float)f);
        unitType.applyColor(unit);
        TextureRegion textureRegion5 = textureRegion != null ? textureRegion : unitType.legRegion;
        TextureRegion textureRegion6 = textureRegion2 != null ? textureRegion2 : unitType.legBaseRegion;
        TextureRegion textureRegion7 = textureRegion3 != null ? textureRegion3 : unitType.jointRegion;
        Leg[] legArray = legsc.legs();
        for (int i = legArray.length - 1; i >= 0; --i) {
            int n = i % 2 == 0 ? i / 2 : legArray.length - 1 - i / 2;
            Leg leg = legArray[n];
            boolean bl = (float)n >= (float)legArray.length / 2.0f;
            int n2 = Mathf.sign((boolean)bl);
            Vec2 vec2 = legsc.legOffset(tv2, n).add(unit.x, unit.y);
            tv1.set(leg.base).sub(leg.joint).inv().setLength(unitType.legExtension);
            if (unitType.legBaseUnder) {
                Lines.stroke((float)((float)textureRegion6.height * unitType.legRegion.scl() * (float)n2));
                Lines.line((TextureRegion)textureRegion6, (float)(leg.joint.x + Skins.tv1.x), (float)(leg.joint.y + Skins.tv1.y), (float)leg.base.x, (float)leg.base.y, (boolean)false);
                Lines.stroke((float)((float)textureRegion5.height * unitType.legRegion.scl() * (float)n2));
                Lines.line((TextureRegion)textureRegion5, (float)vec2.x, (float)vec2.y, (float)leg.joint.x, (float)leg.joint.y, (boolean)false);
            } else {
                Lines.stroke((float)((float)textureRegion5.height * unitType.legRegion.scl() * (float)n2));
                Lines.line((TextureRegion)textureRegion5, (float)vec2.x, (float)vec2.y, (float)leg.joint.x, (float)leg.joint.y, (boolean)false);
                Lines.stroke((float)((float)textureRegion6.height * unitType.legRegion.scl() * (float)n2));
                Lines.line((TextureRegion)textureRegion6, (float)(leg.joint.x + Skins.tv1.x), (float)(leg.joint.y + Skins.tv1.y), (float)leg.base.x, (float)leg.base.y, (boolean)false);
            }
            if (textureRegion7 == null || !textureRegion7.found()) continue;
            Draw.rect((TextureRegion)textureRegion7, (float)leg.joint.x, (float)leg.joint.y);
        }
        if (textureRegion4 != null) {
            Draw.rect((TextureRegion)textureRegion4, (float)unit.x, (float)unit.y, (float)(legsc.baseRotation() - 90.0f));
        }
        Draw.reset();
    }

    private static void drawMechSkin(Unit unit, Mechc mechc, UnitType unitType, TextureRegion textureRegion, TextureRegion textureRegion2, float f) {
        Draw.z((float)f);
        unitType.applyColor(unit);
        float f2 = unit.elevation;
        float f3 = Mathf.lerp((float)Mathf.sin((float)mechc.walkExtend(true), (float)0.63661975f, (float)1.0f), (float)0.0f, (float)f2);
        float f4 = Mathf.lerp((float)mechc.walkExtend(false), (float)0.0f, (float)f2);
        float f5 = f2 * 2.0f;
        if (textureRegion != null) {
            for (int n : Mathf.signs) {
                Draw.rect((TextureRegion)textureRegion, (float)(unit.x + Angles.trnsx((float)mechc.baseRotation(), (float)(f4 * (float)n - f5), (float)(-f5 * (float)n))), (float)(unit.y + Angles.trnsy((float)mechc.baseRotation(), (float)(f4 * (float)n - f5), (float)(-f5 * (float)n))), (float)((float)textureRegion.width * unitType.legRegion.scl() * (float)n), (float)((float)textureRegion.height * unitType.legRegion.scl() * (1.0f - Math.max(-f3 * (float)n, 0.0f) * 0.5f)), (float)(mechc.baseRotation() - 90.0f + 35.0f * (float)n * f2));
            }
        }
        if (textureRegion2 != null) {
            Draw.rect((TextureRegion)textureRegion2, (float)unit.x, (float)unit.y, (float)(mechc.baseRotation() - 90.0f));
        }
        Draw.reset();
    }

    private static void stopAllMusic() {
        try {
            for (IntMap.Entry entry : playing) {
                try {
                    if (!((Music)entry.value).isPlaying()) continue;
                    ((Music)entry.value).stop();
                }
                catch (Throwable throwable) {}
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static void musicTick() {
        int n;
        if (!HubClient.consented() || Vars.state == null || !Vars.state.isGame() || Vars.player == null || Skins.pools.size == 0) {
            Skins.stopAllMusic();
            return;
        }
        IntFloatMap intFloatMap = new IntFloatMap();
        float f = Core.camera.position.x;
        float f2 = Core.camera.position.y;
        for (Object object : Groups.unit) {
            ObjectMap<String, Entry> objectMap;
            Object object2;
            if (object == null || !object.isAdded() || ((Unit)object).dead || ((Unit)object).type == null || (object2 = (UnitPool)pools.get((Object)((Unit)object).type.name)) == null || (objectMap = Skins.skinsFor((Unit)object, (UnitPool)object2)) == null) continue;
            for (ObjectMap.Entry entry : objectMap) {
                float f3;
                Entry entry2 = (Entry)entry.value;
                if (entry2.music <= 0 || entry2.radius <= 0) continue;
                float f4 = (float)entry2.radius * 8.0f;
                float f5 = object.dst(f, f2);
                if (f5 >= f4 || !((f3 = 1.0f - f5 / f4) > intFloatMap.get(entry2.music, 0.0f))) continue;
                intFloatMap.put(entry2.music, f3);
            }
        }
        Iterator iterator = new IntSeq();
        for (Object object2 : intFloatMap) {
            iterator.add(object2.key);
        }
        iterator.sort();
        for (n = 0; n < ((IntSeq)iterator).size; ++n) {
            for (int i = n + 1; i < ((IntSeq)iterator).size; ++i) {
                if (!(intFloatMap.get(iterator.get(i), 0.0f) > intFloatMap.get(iterator.get(n), 0.0f))) continue;
                int n2 = iterator.get(n);
                iterator.set(n, iterator.get(i));
                iterator.set(i, n2);
            }
        }
        for (n = ((IntSeq)iterator).size - 1; n >= 2; --n) {
            iterator.removeIndex(n);
        }
        float f6 = (float)Core.settings.getInt("musicvol", 100) / 100.0f;
        for (int i = 0; i < ((IntSeq)iterator).size; ++i) {
            int n3 = iterator.get(i);
            ObjectMap.Entries entries = (Music)playing.get(n3);
            if (entries == null) {
                Skins.loadAudio(n3);
                continue;
            }
            try {
                entries.setVolume(Mathf.clamp((float)(intFloatMap.get(n3, 0.0f) * f6)));
                if (entries.isPlaying()) continue;
                entries.play();
                continue;
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        for (IntMap.Entry entry : playing) {
            if (iterator.contains(entry.key)) continue;
            try {
                if (!((Music)entry.value).isPlaying()) continue;
                ((Music)entry.value).stop();
            }
            catch (Throwable throwable) {}
        }
    }

    private static Fi cached(int n) {
        Fi fi = (Fi)files.get(n);
        if (fi != null && fi.exists()) {
            return fi;
        }
        for (String string : new String[]{"ogg", "mp3", "wav"}) {
            Fi fi2 = AudioUi.dir().child(n + "." + string);
            if (!fi2.exists() || fi2.length() <= 0L) continue;
            files.put(n, (Object)fi2);
            return fi2;
        }
        return null;
    }

    private static void startMusic(int n, Fi fi) {
        try {
            Music music = new Music(fi);
            music.setLooping(true);
            music.setVolume(0.0f);
            playing.put(n, (Object)music);
        }
        catch (Throwable throwable) {
            audioFailed.put(n, Time.time);
            Log.warn((String)"[Lunarians Skins] musica @: @", (Object[])new Object[]{n, throwable.toString()});
        }
    }

    private static void loadAudio(int n) {
        if (audioLoading.contains(n) || audioFailed.get(n, -100000.0f) + 3600.0f >= Time.time) {
            return;
        }
        Fi fi = Skins.cached(n);
        if (fi != null) {
            Skins.startMusic(n, fi);
            return;
        }
        audioLoading.add(n);
        HubClient.call("GET", "/api/audio/get?id=" + n, null, (Cons<Jval>)((Cons)jval -> {
            audioLoading.remove(n);
            try {
                byte[] byArray = Base64Coder.decode((String)jval.getString("data", ""));
                String string = jval.getString("fmt", "ogg");
                if (byArray.length < 64 || !string.matches("ogg|mp3|wav")) {
                    throw new RuntimeException("invalido");
                }
                Fi fi = AudioUi.dir().child(n + "." + string);
                fi.writeBytes(byArray, false);
                files.put(n, (Object)fi);
                Skins.startMusic(n, fi);
            }
            catch (Throwable throwable) {
                audioFailed.put(n, Time.time);
            }
        }), (Cons<String>)((Cons)string -> {
            audioLoading.remove(n);
            audioFailed.put(n, Time.time);
        }));
    }

    private static Label wrap(String string) {
        Label label = new Label((CharSequence)string);
        label.setWrap(true);
        return label;
    }

    private static boolean isPng(byte[] byArray) {
        return byArray.length > 8 && (byArray[0] & 0xFF) == 137 && byArray[1] == 80 && byArray[2] == 78 && byArray[3] == 71;
    }

    static String partLabel(String string) {
        if (string.startsWith("weapon")) {
            return HubText.get("skin.part.weapon") + " " + (Strings.parseInt((String)string.substring(6), (int)0) + 1);
        }
        return HubText.get("skin.part." + string);
    }

    private static UnitType unitType(String string) {
        return (UnitType)Vars.content.units().find(unitType -> unitType.name.equals(string));
    }

    static TextureRegion originalRegion(UnitType unitType, String string) {
        if (unitType == null) {
            return null;
        }
        switch (string) {
            case "body": {
                return unitType.region;
            }
            case "legs": {
                return unitType.legRegion;
            }
            case "legbase": {
                return unitType.legBaseRegion;
            }
            case "joint": {
                return unitType.jointRegion;
            }
            case "base": {
                return unitType.baseRegion;
            }
        }
        if (string.startsWith("weapon")) {
            Seq<String> seq = Skins.weaponNames(unitType);
            int n = Strings.parseInt((String)string.substring(6), (int)-1);
            if (n >= 0 && n < seq.size) {
                for (Weapon weapon : unitType.weapons) {
                    if (!weapon.name.equals(seq.get(n))) continue;
                    return weapon.region;
                }
            }
        }
        return null;
    }

    static Seq<String> partsOf(UnitType unitType) {
        Seq seq = new Seq();
        seq.add((Object)"body");
        for (String string : new Seq<String>[]{"legs", "legbase", "joint", "base"}) {
            TextureRegion textureRegion = Skins.originalRegion(unitType, string);
            if (textureRegion == null || !textureRegion.found()) continue;
            seq.add((Object)string);
        }
        Seq<String> seq2 = Skins.weaponNames(unitType);
        for (int i = 0; i < seq2.size && i < 30; ++i) {
            seq.add((Object)("weapon" + i));
        }
        return seq;
    }

    public static void buildTab(Table table, Runnable runnable, float f) {
        table.clear();
        table.add((Element)Skins.wrap(HubText.get("skin.help"))).width(f - 10.0f).left().padBottom(6.0f).row();
        table.check(HubText.get("skin.show"), Skins.showOthers(), bl -> {
            Core.settings.put(K_SHOW, (Object)bl);
            dirty = true;
        }).left().padBottom(6.0f).row();
        table.button(HubText.get("skin.new"), (Drawable)Icon.add, () -> Skins.requestDialog(runnable)).size(Math.min(f, 420.0f), 52.0f).padBottom(8.0f).row();
        Table table2 = new Table();
        table.add((Element)table2).width(f).row();
        table2.add((Element)Skins.wrap("...")).row();
        HubClient.call("GET", "/api/skins/mine", null, (Cons<Jval>)((Cons)jval -> {
            table2.clear();
            Jval.JsonArray jsonArray = jval.get("skins").asArray();
            Skins.updateMine(jsonArray);
            if (jsonArray.size == 0) {
                table2.add((Element)Skins.wrap(HubText.get("skin.none"))).width(f - 20.0f).pad(6.0f).row();
                return;
            }
            UiStyle.section(table2, HubText.get("skin.mine"));
            for (Jval jval2 : jsonArray) {
                int n = jval2.getInt("id", 0);
                String string = jval2.getString("unit", "");
                String string2 = jval2.getString("part", "body");
                String string3 = jval2.getString("status", "pending");
                int n2 = jval2.getInt("music", 0);
                int n3 = jval2.getInt("music_radius", 0);
                table2.table(Styles.grayPanel, table2 -> {
                    table2.table(table -> {
                        Image image = new Image((Drawable)Icon.players);
                        image.setScaling(Scaling.fit);
                        Skins.fetch(n, (Cons<TextureRegion>)((Cons)textureRegion -> image.setDrawable((Drawable)new TextureRegionDrawable(textureRegion))));
                        table.add((Element)image).size(56.0f).pad(4.0f);
                        String string4 = jval2.getString("name", "?") + "\n" + HubText.get("skin.unit") + ": " + string + "  \u2022  " + Skins.partLabel(string2) + "\n" + HubText.get("skin.status." + string3);
                        if (string3.equals("rejected") && !jval2.getString("note", "").isEmpty()) {
                            string4 = string4 + " - " + jval2.getString("note", "");
                        }
                        if (n2 > 0) {
                            string4 = string4 + "\n\u266a " + HubText.get("skin.music.radius") + ": " + n3;
                        }
                        if (Skins.using(n)) {
                            string4 = string4 + "  [" + HubText.get("skin.using") + "]";
                        }
                        Label label = Skins.wrap(string4);
                        label.setAlignment(8);
                        table.add((Element)label).growX().pad(6.0f);
                    }).growX().row();
                    table2.table(table -> {
                        table.defaults().height(40.0f).pad(2.0f);
                        if (string3.equals("approved")) {
                            if (Skins.using(n)) {
                                table.button(HubText.get("skin.stop"), () -> {
                                    Skins.unequip(n);
                                    runnable.run();
                                }).minWidth(96.0f);
                            } else {
                                table.button(HubText.get("skin.use"), () -> {
                                    Skins.equip(n);
                                    runnable.run();
                                }).minWidth(96.0f);
                            }
                            table.button("\u266a", () -> Skins.musicDialog(n, n2, n3, runnable)).size(44.0f, 40.0f);
                        }
                        table.button((Drawable)Icon.trash, Styles.cleari, () -> Vars.ui.showConfirm(HubText.get("skin.delete"), HubText.get("skin.delete.confirm"), () -> {
                            Jval jval2 = Jval.newObject();
                            jval2.add("id", Jval.valueOf((int)n));
                            if (Skins.using(n)) {
                                Skins.unequip(n);
                            }
                            HubClient.call("POST", "/api/skins/delete", jval2, (Cons<Jval>)((Cons)jval -> runnable.run()), (Cons<String>)((Cons)string -> Vars.ui.showInfoToast(HubText.get("sent.fail"), 2.0f)));
                        })).size(40.0f);
                    }).left().row();
                }).width(f - 10.0f).pad(2.0f).row();
            }
        }), (Cons<String>)((Cons)string -> {
            table2.clear();
            table2.add((Element)Skins.wrap(HubText.get("sent.fail"))).row();
        }));
    }

    private static void musicDialog(int n, int n2, int n3, Runnable runnable) {
        BaseDialog baseDialog = new BaseDialog(HubText.get("skin.music.title"));
        UiStyle.register(baseDialog);
        float f2 = Math.min(Core.scene.getWidth() - 40.0f, 520.0f);
        int[] nArray = new int[]{n2};
        baseDialog.cont.add((Element)Skins.wrap(HubText.get("skin.music.help"))).width(f2).padBottom(6.0f).row();
        Table table2 = new Table();
        baseDialog.cont.pane((Element)table2).width(f2).maxHeight(Core.scene.getHeight() * 0.4f).row();
        table2.add((Element)Skins.wrap("...")).row();
        Label label = new Label((CharSequence)String.valueOf(n3 > 0 ? n3 : 12));
        Slider slider = new Slider(1.0f, 30.0f, 1.0f, false);
        slider.setValue(n3 > 0 ? (float)n3 : 12.0f);
        slider.moved(f -> label.setText((CharSequence)String.valueOf((int)f)));
        baseDialog.cont.table(table -> {
            table.add((CharSequence)HubText.get("skin.music.radius")).padRight(8.0f);
            table.add((Element)slider).width(Math.min(f2 - 150.0f, 260.0f));
            table.add((Element)label).padLeft(8.0f).width(36.0f);
        }).padTop(6.0f).row();
        HubClient.call("GET", "/api/audio/mine", null, (Cons<Jval>)((Cons)jval -> {
            table2.clear();
            int n = 0;
            for (Jval jval2 : jval.get("audio").asArray()) {
                if (!"music".equals(jval2.getString("kind", "")) || !"approved".equals(jval2.getString("status", ""))) continue;
                int n2 = jval2.getInt("id", 0);
                table2.button(jval2.getString("name", "?"), Styles.togglet, () -> {
                    nArray[0] = n2;
                }).checked(textButton -> nArray[0] == n2).growX().height(44.0f).pad(2.0f).row();
                ++n;
            }
            if (n == 0) {
                table2.add((Element)Skins.wrap(HubText.get("skin.music.none"))).width(f2 - 20.0f).pad(6.0f).row();
            }
        }), (Cons<String>)((Cons)string -> {
            table2.clear();
            table2.add((Element)Skins.wrap(HubText.get("sent.fail"))).row();
        }));
        Runnable runnable2 = () -> {
            Jval jval2 = Jval.newObject();
            jval2.add("id", Jval.valueOf((int)n));
            jval2.add("audio", Jval.valueOf((int)nArray[0]));
            jval2.add("radius", Jval.valueOf((int)((int)slider.getValue())));
            HubClient.call("POST", "/api/skins/music", jval2, (Cons<Jval>)((Cons)jval -> {
                Vars.ui.showInfoToast(HubText.get("skin.music.saved"), 2.0f);
                Skins.refreshMine();
                baseDialog.hide();
                runnable.run();
            }), (Cons<String>)((Cons)string -> Vars.ui.showInfoToast(string.equals("audio_not_approved") ? HubText.get("skin.music.none") : HubText.get("sent.fail"), 3.0f)));
        };
        baseDialog.cont.table(table -> {
            table.defaults().height(48.0f).pad(3.0f);
            table.button(HubText.get("skin.music.save"), (Drawable)Icon.ok, () -> {
                if (nArray[0] <= 0) {
                    Vars.ui.showInfoToast(HubText.get("skin.music.none"), 2.5f);
                    return;
                }
                runnable2.run();
            }).minWidth(150.0f);
            table.button(HubText.get("skin.music.remove"), (Drawable)Icon.trash, () -> {
                nArray[0] = 0;
                runnable2.run();
            }).minWidth(150.0f);
        }).padTop(8.0f).row();
        baseDialog.addCloseButton();
        baseDialog.show();
    }

    static void pickUnit(Cons<String> cons) {
        BaseDialog baseDialog = new BaseDialog(HubText.get("skin.pickunit"));
        UiStyle.register(baseDialog);
        Table table = new Table();
        float f = Math.min(Core.scene.getWidth() - 40.0f, 520.0f);
        int n = Math.max(3, (int)(f / 76.0f));
        int n2 = 0;
        for (UnitType unitType : Vars.content.units()) {
            if (!unitType.isVanilla() || unitType.internal || unitType.uiIcon == null || !Skins.okUnit(unitType.name)) continue;
            Table table2 = new Table();
            table2.button((Drawable)new TextureRegionDrawable(unitType.uiIcon), Styles.cleari, 44.0f, () -> {
                cons.get((Object)unitType.name);
                baseDialog.hide();
            }).size(56.0f);
            table2.row();
            table2.add((CharSequence)unitType.name).fontScale(0.55f);
            table.add((Element)table2).pad(2.0f);
            if (++n2 % n != 0) continue;
            table.row();
        }
        baseDialog.cont.pane((Element)table).width(f).maxHeight(Core.scene.getHeight() * 0.65f).row();
        baseDialog.addCloseButton();
        baseDialog.show();
    }

    private static void pickPart(UnitType unitType, Cons<String> cons) {
        BaseDialog baseDialog = new BaseDialog(HubText.get("skin.pickpart"));
        UiStyle.register(baseDialog);
        float f = Math.min(Core.scene.getWidth() - 40.0f, 420.0f);
        Table table = new Table();
        for (String string : Skins.partsOf(unitType)) {
            TextureRegion textureRegion = Skins.originalRegion(unitType, string);
            String string2 = textureRegion != null && textureRegion.found() ? "  (" + textureRegion.width + "x" + textureRegion.height + ")" : "";
            table.button(Skins.partLabel(string) + string2, () -> {
                cons.get((Object)string);
                baseDialog.hide();
            }).growX().height(46.0f).pad(2.0f).row();
        }
        baseDialog.cont.pane((Element)table).width(f).maxHeight(Core.scene.getHeight() * 0.65f).row();
        baseDialog.addCloseButton();
        baseDialog.show();
    }

    public static void requestDialog(Runnable runnable) {
        BaseDialog baseDialog = new BaseDialog(HubText.get("skin.new"));
        UiStyle.register(baseDialog);
        float f = Math.min(Core.scene.getWidth() - 40.0f, 520.0f);
        String[] stringArray = new String[]{""};
        String[] stringArray2 = new String[]{"body"};
        byte[][] byArrayArray = new byte[][]{null};
        String[] stringArray3 = new String[]{""};
        TextField textField = new TextField();
        textField.setMessageText(HubText.get("skin.name"));
        textField.setMaxLength(32);
        Label label = Skins.wrap("");
        TextButton textButton = new TextButton(HubText.get("skin.pickunit"), Styles.defaultt);
        TextButton textButton2 = new TextButton(HubText.get("skin.pickpart"), Styles.defaultt);
        TextButton textButton3 = new TextButton(HubText.get("skin.pickfile"), Styles.defaultt);
        Runnable runnable2 = () -> {
            textButton.setText(stringArray[0].isEmpty() ? HubText.get("skin.pickunit") : HubText.get("skin.unit") + ": " + stringArray[0]);
            textButton2.setText(stringArray[0].isEmpty() ? HubText.get("skin.pickpart") : HubText.get("skin.part") + ": " + Skins.partLabel(stringArray2[0]));
            textButton3.setText(byArrayArray[0] == null ? HubText.get("skin.pickfile") : "PNG " + stringArray3[0]);
            String string = "";
            UnitType unitType = stringArray[0].isEmpty() ? null : Skins.unitType(stringArray[0]);
            TextureRegion textureRegion = Skins.originalRegion(unitType, stringArray2[0]);
            if (textureRegion != null && textureRegion.found()) {
                string = HubText.get("skin.sprite") + ": " + textureRegion.width + "x" + textureRegion.height;
            }
            label.setText((CharSequence)string);
        };
        textButton.clicked(() -> Skins.pickUnit((Cons<String>)((Cons)string -> {
            stringArray[0] = string;
            stringArray2[0] = "body";
            runnable2.run();
        })));
        textButton2.clicked(() -> {
            UnitType unitType;
            UnitType unitType2 = unitType = stringArray[0].isEmpty() ? null : Skins.unitType(stringArray[0]);
            if (unitType == null) {
                Vars.ui.showInfoToast(HubText.get("skin.fill"), 2.0f);
                return;
            }
            Skins.pickPart(unitType, (Cons<String>)((Cons)string -> {
                stringArray[0] = string;
                runnable2.run();
            }));
        });
        textButton3.clicked(() -> new FileChooser.FileChooserParams().open(true).title(HubText.get("skin.pickfile")).extensions(new String[]{"png"}).submit(fi -> {
            try {
                Pixmap pixmap;
                byte[] byArray2 = fi.readBytes();
                Pixmap pixmap2 = pixmap = !Skins.isPng(byArray2) || byArray2.length > 153600 ? null : new Pixmap(byArray2);
                if (pixmap == null || pixmap.width > 512 || pixmap.height > 512) {
                    if (pixmap != null) {
                        pixmap.dispose();
                    }
                    Vars.ui.showInfoToast(HubText.get("skin.invalid"), 3.0f);
                } else {
                    stringArray[0] = pixmap.width + "x" + pixmap.height;
                    pixmap.dispose();
                    byArray[0] = byArray2;
                    runnable2.run();
                }
            }
            catch (Throwable throwable) {
                Vars.ui.showInfoToast(HubText.get("skin.invalid"), 3.0f);
            }
        }));
        baseDialog.cont.add((Element)Skins.wrap(HubText.get("skin.help"))).width(f).padBottom(6.0f).row();
        baseDialog.cont.add((Element)textField).width(f).height(46.0f).row();
        baseDialog.cont.add((Element)textButton).width(f).height(48.0f).padTop(6.0f).row();
        baseDialog.cont.add((Element)textButton2).width(f).height(48.0f).padTop(6.0f).row();
        baseDialog.cont.add((Element)textButton3).width(f).height(48.0f).padTop(6.0f).row();
        baseDialog.cont.add((Element)label).width(f).padTop(6.0f).row();
        baseDialog.cont.button(HubText.get("skin.send"), (Drawable)Icon.upload, () -> {
            String string2 = textField.getText().trim();
            if (string2.isEmpty() || stringArray[0].isEmpty() || byArrayArray[0] == null) {
                Vars.ui.showInfoToast(HubText.get("skin.fill"), 2.5f);
                return;
            }
            Jval jval2 = Jval.newObject();
            jval2.add("name", string2);
            jval2.add("unit", stringArray[0]);
            jval2.add("part", stringArray2[0]);
            jval2.add("png", new String(Base64Coder.encode((byte[])byArrayArray[0])));
            HubClient.call("POST", "/api/skins/request", jval2, (Cons<Jval>)((Cons)jval -> {
                Vars.ui.showInfoToast(HubText.get("skin.sent"), 3.5f);
                baseDialog.hide();
                if (runnable != null) {
                    runnable.run();
                }
            }), (Cons<String>)((Cons)string -> Vars.ui.showInfoToast(string.equals("too_many_pending") ? HubText.get("skin.limit") : HubText.get("sent.fail"), 3.0f)));
        }).size(Math.min(f, 320.0f), 52.0f).padTop(10.0f).row();
        baseDialog.addCloseButton();
        baseDialog.shown(runnable2);
        baseDialog.show();
    }

    static {
        peers = new IntMap();
        pools = new ObjectMap();
        tex = new IntMap();
        waiting = new IntMap();
        failedAt = new IntFloatMap();
        weaponNames = new ObjectMap();
        tv1 = new Vec2();
        tv2 = new Vec2();
        dirty = true;
        playing = new IntMap();
        files = new IntMap();
        audioFailed = new IntFloatMap();
        audioLoading = new IntSet();
    }

    static class Entry {
        int id;
        String unit;
        String part;
        int music;
        int radius;
        int owner = -1;

        Entry() {
        }
    }

    private static class Peer {
        Seq<Entry> list = new Seq();
        float seen;
        String sig = "";

        private Peer() {
        }
    }

    private static class UnitPool {
        final Seq<Integer> owners = new Seq();
        final IntMap<ObjectMap<String, Entry>> byOwner = new IntMap();

        private UnitPool() {
        }
    }
}

