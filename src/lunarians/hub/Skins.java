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
  
  private static final float PEER_TTL = 6000.0F;
  
  private static final int MAX_EQUIPPED = 8;
  
  static class Entry {
    int id;
    
    String unit;
    
    String part;
    
    int music;
    
    int radius;
    
    int owner = -1;
  }
  
  private static class Peer {
    private Peer() {}
    
    Seq<Skins.Entry> list = new Seq();
    
    float seen;
    
    String sig = "";
  }
  
  private static class UnitPool {
    private UnitPool() {}
    
    final Seq<Integer> owners = new Seq();
    
    final IntMap<ObjectMap<String, Skins.Entry>> byOwner = new IntMap();
  }
  
  private static final Seq<Entry> mine = new Seq();
  
  private static boolean mineLoaded;
  
  private static final IntMap<Peer> peers = new IntMap();
  
  private static final ObjectMap<String, UnitPool> pools = new ObjectMap();
  
  private static final IntMap<TextureRegion> tex = new IntMap();
  
  private static final IntMap<Seq<Cons<TextureRegion>>> waiting = new IntMap();
  
  private static final IntFloatMap failedAt = new IntFloatMap();
  
  private static final ObjectMap<UnitType, Seq<String>> weaponNames = new ObjectMap();
  
  private static final Vec2 tv1 = new Vec2();
  
  private static final Vec2 tv2 = new Vec2();
  
  private static boolean inited;
  
  private static boolean dirty = true;
  
  private static float nextPool;
  
  private static float nextMine;
  
  private static float nextMusic;
  
  private static final IntMap<Music> playing = new IntMap();
  
  private static final IntMap<Fi> files = new IntMap();
  
  private static final IntFloatMap audioFailed = new IntFloatMap();
  
  private static final IntSet audioLoading = new IntSet();
  
  static boolean okUnit(String paramString) {
    return (paramString != null && paramString.matches("[a-z0-9-]{1,40}"));
  }
  
  static boolean okPart(String paramString) {
    return (paramString != null && paramString.matches("body|legs|joint|legbase|base|weapon[0-9]{1,2}"));
  }
  
  public static boolean showOthers() {
    return Core.settings.getBool("lunarians-hub-skins-show", true);
  }
  
  private static IntSeq equipped() {
    IntSeq intSeq = new IntSeq();
    for (String str : Core.settings.getString("lunarians-hub-skins-eq", "").split(",")) {
      if (str.matches("\\d{1,9}"))
        intSeq.add(Strings.parseInt(str, 0)); 
    } 
    return intSeq;
  }
  
  private static void saveEquipped(IntSeq paramIntSeq) {
    StringBuilder stringBuilder = new StringBuilder();
    for (byte b = 0; b < paramIntSeq.size; b++) {
      if (b > 0)
        stringBuilder.append(','); 
      stringBuilder.append(paramIntSeq.get(b));
    } 
    Core.settings.put("lunarians-hub-skins-eq", stringBuilder.toString());
    dirty = true;
    Room.sendSoon();
  }
  
  public static boolean using(int paramInt) {
    return equipped().contains(paramInt);
  }
  
  private static Entry mineById(int paramInt) {
    for (Entry entry : mine) {
      if (entry.id == paramInt)
        return entry; 
    } 
    return null;
  }
  
  public static void equip(int paramInt) {
    Entry entry = mineById(paramInt);
    if (entry == null)
      return; 
    IntSeq intSeq1 = equipped();
    IntSeq intSeq2 = new IntSeq();
    for (byte b = 0; b < intSeq1.size; b++) {
      Entry entry1 = mineById(intSeq1.get(b));
      if (entry1 != null && (!entry1.unit.equals(entry.unit) || !entry1.part.equals(entry.part)))
        intSeq2.add(entry1.id); 
    } 
    if (intSeq2.size >= 8) {
      Vars.ui.showInfoToast(HubText.get("skin.max"), 2.5F);
      return;
    } 
    intSeq2.add(paramInt);
    saveEquipped(intSeq2);
  }
  
  public static void unequip(int paramInt) {
    IntSeq intSeq = equipped();
    intSeq.removeValue(paramInt);
    saveEquipped(intSeq);
  }
  
  public static IntSeq equippedApproved() {
    IntSeq intSeq1 = new IntSeq();
    IntSeq intSeq2 = equipped();
    for (byte b = 0; b < intSeq2.size && intSeq1.size < 8; b++) {
      if (mineById(intSeq2.get(b)) != null)
        intSeq1.add(intSeq2.get(b)); 
    } 
    return intSeq1;
  }
  
  public static void wipeLocal() {
    Core.settings.remove("lunarians-hub-skins-eq");
    mine.clear();
    mineLoaded = false;
    peers.clear();
    pools.clear();
    stopAllMusic();
    dirty = true;
  }
  
  private static Entry parse(Jval paramJval, int paramInt) {
    try {
      Entry entry = new Entry();
      entry.id = paramJval.getInt("i", paramJval.getInt("id", 0));
      entry.unit = paramJval.getString("u", paramJval.getString("unit", ""));
      entry.part = paramJval.getString("p", paramJval.getString("part", "body"));
      entry.music = paramJval.getInt("m", paramJval.getInt("music", 0));
      entry.radius = Math.max(0, Math.min(30, paramJval.getInt("r", paramJval.getInt("music_radius", 0))));
      entry.owner = paramInt;
      if (entry.id <= 0 || !okUnit(entry.unit) || !okPart(entry.part))
        return null; 
      if (entry.music < 0)
        entry.music = 0; 
      return entry;
    } catch (Throwable throwable) {
      return null;
    } 
  }
  
  private static void updateMine(Jval.JsonArray paramJsonArray) {
    mine.clear();
    for (Jval jval : paramJsonArray) {
      if (!"approved".equals(jval.getString("status", "")))
        continue; 
      Entry entry = parse(jval, -1);
      if (entry != null)
        mine.add(entry); 
    } 
    mineLoaded = true;
    IntSeq intSeq1 = equipped();
    IntSeq intSeq2 = new IntSeq();
    for (byte b = 0; b < intSeq1.size; ) {
      if (mineById(intSeq1.get(b)) != null)
        intSeq2.add(intSeq1.get(b)); 
      b++;
    } 
    if (intSeq2.size != intSeq1.size)
      saveEquipped(intSeq2); 
    dirty = true;
  }
  
  static void refreshMine() {
    if (!HubClient.consented() || HubClient.myId().isEmpty())
      return; 
    HubClient.call("GET", "/api/skins/mine", null, paramJval -> updateMine(paramJval.get("skins").asArray()), paramString -> {
        
        });
  }
  
  static void feedPeer(int paramInt, Jval.JsonArray paramJsonArray) {
    if (paramInt < 0 || paramJsonArray == null)
      return; 
    if (peers.size > 200 && !peers.containsKey(paramInt))
      return; 
    Peer peer = (Peer)peers.get(paramInt);
    if (peer == null) {
      peer = new Peer();
      peers.put(paramInt, peer);
    } 
    Seq<Entry> seq = new Seq();
    StringBuilder stringBuilder = new StringBuilder();
    byte b = 0;
    for (Jval jval : paramJsonArray) {
      if (b++ >= 8)
        break; 
      Entry entry = parse(jval, paramInt);
      if (entry == null)
        continue; 
      seq.add(entry);
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
    IntSeq intSeq = equipped();
    for (Entry entry : mine) {
      if (intSeq.contains(entry.id))
        add(entry); 
    } 
    if (showOthers()) {
      Seq seq = new Seq();
      for (IntMap.Entry entry : peers) {
        if (Time.time - ((Peer)entry.value).seen > 6000.0F) {
          seq.add(Integer.valueOf(entry.key));
          continue;
        } 
        for (Entry entry1 : ((Peer)entry.value).list)
          add(entry1); 
      } 
      for (Iterator<Integer> iterator = seq.iterator(); iterator.hasNext(); ) {
        int i = ((Integer)iterator.next()).intValue();
        peers.remove(i);
      } 
    } 
    for (ObjectMap.Entries<ObjectMap.Entry> entries = pools.iterator(); entries.hasNext(); ) {
      ObjectMap.Entry entry = entries.next();
      ((UnitPool)entry.value).owners.sort();
    } 
    dirty = false;
  }
  
  private static void add(Entry paramEntry) {
    UnitPool unitPool = (UnitPool)pools.get(paramEntry.unit);
    if (unitPool == null) {
      unitPool = new UnitPool();
      pools.put(paramEntry.unit, unitPool);
    } 
    ObjectMap objectMap = (ObjectMap)unitPool.byOwner.get(paramEntry.owner);
    if (objectMap == null) {
      objectMap = new ObjectMap();
      unitPool.byOwner.put(paramEntry.owner, objectMap);
      unitPool.owners.add(Integer.valueOf(paramEntry.owner));
    } 
    if (!objectMap.containsKey(paramEntry.part))
      objectMap.put(paramEntry.part, paramEntry); 
  }
  
  private static int pick(int paramInt1, int paramInt2) {
    if (paramInt2 <= 1)
      return 0; 
    int i = paramInt1 * -1640531535;
    i ^= i >>> 15;
    i *= -2048144789;
    i ^= i >>> 13;
    return Math.floorMod(i, paramInt2);
  }
  
  private static ObjectMap<String, Entry> skinsFor(Unit paramUnit, UnitPool paramUnitPool) {
    if (paramUnitPool == null || paramUnitPool.owners.size == 0)
      return null; 
    int i = ((Integer)paramUnitPool.owners.get(pick(paramUnit.id, paramUnitPool.owners.size))).intValue();
    return (ObjectMap<String, Entry>)paramUnitPool.byOwner.get(i);
  }
  
  public static void init() {
    if (inited || Vars.headless)
      return; 
    inited = true;
    Events.run(EventType.Trigger.drawOver, Skins::draw);
    Events.run(EventType.Trigger.update, Skins::tick);
    Events.on(EventType.WorldLoadEvent.class, paramWorldLoadEvent -> {
          peers.clear();
          dirty = true;
          stopAllMusic();
        });
  }
  
  private static void tick() {
    try {
      if (!HubClient.consented()) {
        if (pools.size > 0) {
          pools.clear();
          stopAllMusic();
        } 
        return;
      } 
      if (Time.time >= nextMine) {
        nextMine = Time.time + (mineLoaded ? 7200.0F : 600.0F);
        refreshMine();
      } 
      if (dirty || Time.time >= nextPool) {
        nextPool = Time.time + 300.0F;
        rebuildPools();
      } 
      if (Time.time >= nextMusic) {
        nextMusic = Time.time + 20.0F;
        musicTick();
      } 
    } catch (Throwable throwable) {
      Log.debug("[Lunarians Skins] tick: @", new Object[] { throwable.toString() });
    } 
  }
  
  static void fetch(int paramInt, Cons<TextureRegion> paramCons) {
    TextureRegion textureRegion = (TextureRegion)tex.get(paramInt);
    if (textureRegion != null) {
      paramCons.get(textureRegion);
      return;
    } 
    Seq seq = (Seq)waiting.get(paramInt);
    boolean bool = (seq == null) ? true : false;
    if (bool) {
      seq = new Seq();
      waiting.put(paramInt, seq);
    } 
    seq.add(paramCons);
    if (bool)
      HubClient.call("GET", "/api/skins/img?id=" + paramInt, null, paramJval -> {
            Seq seq = (Seq)waiting.remove(paramInt);
            try {
              Pixmap pixmap = new Pixmap(Base64Coder.decode(paramJval.getString("png", "")));
              Texture texture = new Texture(pixmap);
              texture.setFilter(Texture.TextureFilter.nearest);
              pixmap.dispose();
              TextureRegion textureRegion = new TextureRegion(texture);
              tex.put(paramInt, textureRegion);
              if (seq != null)
                for (Cons cons : seq)
                  cons.get(textureRegion);  
            } catch (Throwable throwable) {
              failedAt.put(paramInt, Time.time);
            } 
          }paramString -> {
            waiting.remove(paramInt);
            failedAt.put(paramInt, Time.time);
          }); 
  }
  
  private static TextureRegion region(int paramInt) {
    TextureRegion textureRegion = (TextureRegion)tex.get(paramInt);
    if (textureRegion != null)
      return textureRegion; 
    if (waiting.containsKey(paramInt) || failedAt.get(paramInt, -100000.0F) + 3600.0F >= Time.time)
      return null; 
    fetch(paramInt, paramTextureRegion -> {
        
        });
    return null;
  }
  
  private static TextureRegion skin(ObjectMap<String, Entry> paramObjectMap, String paramString) {
    if (paramObjectMap == null)
      return null; 
    Entry entry = (Entry)paramObjectMap.get(paramString);
    return (entry == null) ? null : region(entry.id);
  }
  
  private static void draw() {
    try {
      if (pools.size == 0 || !HubClient.consented() || Vars.state == null || !Vars.state.isGame() || Vars.player == null)
        return; 
      Core.camera.bounds(Tmp.r1);
      Team team = Vars.player.team();
      float f = Draw.z();
      for (Unit unit : Groups.unit) {
        if (unit == null || !unit.isAdded() || unit.dead || unit.type == null)
          continue; 
        UnitPool unitPool = (UnitPool)pools.get(unit.type.name);
        if (unitPool == null)
          continue; 
        Tmp.r2.set(Tmp.r1).grow(unit.hitSize * 2.0F + 16.0F);
        if (!Tmp.r2.contains(unit.x, unit.y) || unit.inFogTo(team))
          continue; 
        ObjectMap<String, Entry> objectMap = skinsFor(unit, unitPool);
        if (objectMap == null)
          continue; 
        try {
          drawUnit(unit, objectMap);
        } catch (Throwable throwable) {}
      } 
      Draw.z(f);
      Draw.reset();
    } catch (Throwable throwable) {}
  }
  
  private static void drawUnit(Unit paramUnit, ObjectMap<String, Entry> paramObjectMap) {
    UnitType unitType = paramUnit.type;
    float f = (paramUnit.elevation > 0.5F || (unitType.flying && paramUnit.dead)) ? unitType.flyingLayer : (unitType.groundLayer + Mathf.clamp(unitType.hitSize / 4000.0F, 0.0F, 0.01F));
    if (paramUnit instanceof Legsc) {
      TextureRegion textureRegion1 = skin(paramObjectMap, "legs"), textureRegion2 = skin(paramObjectMap, "legbase"), textureRegion3 = skin(paramObjectMap, "joint"), textureRegion4 = skin(paramObjectMap, "base");
      if (textureRegion1 != null || textureRegion2 != null || textureRegion3 != null || textureRegion4 != null)
        drawLegSkin(paramUnit, (Legsc)paramUnit, unitType, textureRegion1, textureRegion2, textureRegion3, textureRegion4, f - 0.01F); 
    } else if (paramUnit instanceof Mechc) {
      TextureRegion textureRegion1 = skin(paramObjectMap, "legs"), textureRegion2 = skin(paramObjectMap, "base");
      if (textureRegion1 != null || textureRegion2 != null)
        drawMechSkin(paramUnit, (Mechc)paramUnit, unitType, textureRegion1, textureRegion2, f - 0.01F); 
    } 
    TextureRegion textureRegion = skin(paramObjectMap, "body");
    if (textureRegion != null) {
      Draw.z(f + 0.0105F);
      unitType.applyColor(paramUnit);
      Draw.rect(textureRegion, paramUnit.x, paramUnit.y, paramUnit.rotation - 90.0F);
      Draw.reset();
    } 
    if (paramUnit.mounts != null) {
      Seq<String> seq = weaponNames(unitType);
      for (WeaponMount weaponMount : paramUnit.mounts) {
        Weapon weapon = weaponMount.weapon;
        if (weapon != null && weapon.name != null && !weapon.name.isEmpty()) {
          int i = seq.indexOf(weapon.name);
          TextureRegion textureRegion1 = (i >= 0) ? skin(paramObjectMap, "weapon" + i) : null;
          if (textureRegion1 == null && textureRegion != null && weapon.region != null && weapon.region.found())
            textureRegion1 = weapon.region; 
          if (textureRegion1 != null)
            drawMount(paramUnit, weaponMount, weapon, textureRegion1, f); 
        } 
      } 
    } 
    Draw.reset();
  }
  
  static Seq<String> weaponNames(UnitType paramUnitType) {
    Seq<String> seq = (Seq)weaponNames.get(paramUnitType);
    if (seq == null) {
      seq = new Seq();
      for (Weapon weapon : paramUnitType.weapons) {
        if (weapon.name != null && !weapon.name.isEmpty() && weapon.region != null && weapon.region.found() && !seq.contains(weapon.name))
          seq.add(weapon.name); 
      } 
      weaponNames.put(paramUnitType, seq);
    } 
    return seq;
  }
  
  private static void drawMount(Unit paramUnit, WeaponMount paramWeaponMount, Weapon paramWeapon, TextureRegion paramTextureRegion, float paramFloat) {
    Draw.z(paramFloat + 0.0106F + paramWeapon.layerOffset);
    paramUnit.type.applyColor(paramUnit);
    float f1 = paramUnit.rotation - 90.0F;
    float f2 = Mathf.pow(paramWeaponMount.recoil, paramWeapon.recoilPow) * paramWeapon.recoil;
    float f3 = f1 + (paramWeapon.rotate ? paramWeaponMount.rotation : paramWeapon.baseRotation);
    float f4 = paramUnit.x + Angles.trnsx(f1, paramWeapon.x, paramWeapon.y) + Angles.trnsx(f3, 0.0F, -f2);
    float f5 = paramUnit.y + Angles.trnsy(f1, paramWeapon.x, paramWeapon.y) + Angles.trnsy(f3, 0.0F, -f2);
    float f6 = Draw.xscl;
    Draw.xscl *= -Mathf.sign(paramWeapon.flipSprite);
    Draw.rect(paramTextureRegion, f4, f5, f3);
    Draw.xscl = f6;
    Draw.reset();
  }
  
  private static void drawLegSkin(Unit paramUnit, Legsc paramLegsc, UnitType paramUnitType, TextureRegion paramTextureRegion1, TextureRegion paramTextureRegion2, TextureRegion paramTextureRegion3, TextureRegion paramTextureRegion4, float paramFloat) {
    Draw.z(paramFloat);
    paramUnitType.applyColor(paramUnit);
    TextureRegion textureRegion1 = (paramTextureRegion1 != null) ? paramTextureRegion1 : paramUnitType.legRegion;
    TextureRegion textureRegion2 = (paramTextureRegion2 != null) ? paramTextureRegion2 : paramUnitType.legBaseRegion;
    TextureRegion textureRegion3 = (paramTextureRegion3 != null) ? paramTextureRegion3 : paramUnitType.jointRegion;
    Leg[] arrayOfLeg = paramLegsc.legs();
    for (int i = arrayOfLeg.length - 1; i >= 0; i--) {
      int j = (i % 2 == 0) ? (i / 2) : (arrayOfLeg.length - 1 - i / 2);
      Leg leg = arrayOfLeg[j];
      boolean bool = (j >= arrayOfLeg.length / 2.0F) ? true : false;
      int k = Mathf.sign(bool);
      Vec2 vec2 = paramLegsc.legOffset(tv2, j).add(paramUnit.x, paramUnit.y);
      tv1.set(leg.base).sub(leg.joint).inv().setLength(paramUnitType.legExtension);
      if (paramUnitType.legBaseUnder) {
        Lines.stroke(textureRegion2.height * paramUnitType.legRegion.scl() * k);
        Lines.line(textureRegion2, leg.joint.x + tv1.x, leg.joint.y + tv1.y, leg.base.x, leg.base.y, false);
        Lines.stroke(textureRegion1.height * paramUnitType.legRegion.scl() * k);
        Lines.line(textureRegion1, vec2.x, vec2.y, leg.joint.x, leg.joint.y, false);
      } else {
        Lines.stroke(textureRegion1.height * paramUnitType.legRegion.scl() * k);
        Lines.line(textureRegion1, vec2.x, vec2.y, leg.joint.x, leg.joint.y, false);
        Lines.stroke(textureRegion2.height * paramUnitType.legRegion.scl() * k);
        Lines.line(textureRegion2, leg.joint.x + tv1.x, leg.joint.y + tv1.y, leg.base.x, leg.base.y, false);
      } 
      if (textureRegion3 != null && textureRegion3.found())
        Draw.rect(textureRegion3, leg.joint.x, leg.joint.y); 
    } 
    if (paramTextureRegion4 != null)
      Draw.rect(paramTextureRegion4, paramUnit.x, paramUnit.y, paramLegsc.baseRotation() - 90.0F); 
    Draw.reset();
  }
  
  private static void drawMechSkin(Unit paramUnit, Mechc paramMechc, UnitType paramUnitType, TextureRegion paramTextureRegion1, TextureRegion paramTextureRegion2, float paramFloat) {
    Draw.z(paramFloat);
    paramUnitType.applyColor(paramUnit);
    float f1 = paramUnit.elevation;
    float f2 = Mathf.lerp(Mathf.sin(paramMechc.walkExtend(true), 0.63661975F, 1.0F), 0.0F, f1);
    float f3 = Mathf.lerp(paramMechc.walkExtend(false), 0.0F, f1);
    float f4 = f1 * 2.0F;
    if (paramTextureRegion1 != null)
      for (int i : Mathf.signs)
        Draw.rect(paramTextureRegion1, paramUnit.x + Angles.trnsx(paramMechc.baseRotation(), f3 * i - f4, -f4 * i), paramUnit.y + Angles.trnsy(paramMechc.baseRotation(), f3 * i - f4, -f4 * i), paramTextureRegion1.width * paramUnitType.legRegion
            .scl() * i, paramTextureRegion1.height * paramUnitType.legRegion.scl() * (1.0F - Math.max(-f2 * i, 0.0F) * 0.5F), paramMechc.baseRotation() - 90.0F + 35.0F * i * f1);  
    if (paramTextureRegion2 != null)
      Draw.rect(paramTextureRegion2, paramUnit.x, paramUnit.y, paramMechc.baseRotation() - 90.0F); 
    Draw.reset();
  }
  
  private static void stopAllMusic() {
    try {
      for (IntMap.Entry entry : playing) {
        try {
          if (((Music)entry.value).isPlaying())
            ((Music)entry.value).stop(); 
        } catch (Throwable throwable) {}
      } 
    } catch (Throwable throwable) {}
  }
  
  private static void musicTick() {
    if (!HubClient.consented() || Vars.state == null || !Vars.state.isGame() || Vars.player == null || pools.size == 0) {
      stopAllMusic();
      return;
    } 
    IntFloatMap intFloatMap = new IntFloatMap();
    float f1 = Core.camera.position.x, f2 = Core.camera.position.y;
    for (Unit unit : Groups.unit) {
      if (unit == null || !unit.isAdded() || unit.dead || unit.type == null)
        continue; 
      UnitPool unitPool = (UnitPool)pools.get(unit.type.name);
      if (unitPool == null)
        continue; 
      ObjectMap<String, Entry> objectMap = skinsFor(unit, unitPool);
      if (objectMap == null)
        continue; 
      for (ObjectMap.Entries<ObjectMap.Entry> entries = objectMap.iterator(); entries.hasNext(); ) {
        ObjectMap.Entry entry = entries.next();
        Entry entry1 = (Entry)entry.value;
        if (entry1.music <= 0 || entry1.radius <= 0)
          continue; 
        float f4 = entry1.radius * 8.0F;
        float f5 = unit.dst(f1, f2);
        if (f5 >= f4)
          continue; 
        float f6 = 1.0F - f5 / f4;
        if (f6 > intFloatMap.get(entry1.music, 0.0F))
          intFloatMap.put(entry1.music, f6); 
      } 
    } 
    IntSeq intSeq = new IntSeq();
    for (IntFloatMap.Entry entry : intFloatMap)
      intSeq.add(entry.key); 
    intSeq.sort();
    int i;
    for (i = 0; i < intSeq.size; i++) {
      for (int j = i + 1; j < intSeq.size; j++) {
        if (intFloatMap.get(intSeq.get(j), 0.0F) > intFloatMap.get(intSeq.get(i), 0.0F)) {
          int k = intSeq.get(i);
          intSeq.set(i, intSeq.get(j));
          intSeq.set(j, k);
        } 
      } 
    } 
    for (i = intSeq.size - 1; i >= 2; ) {
      intSeq.removeIndex(i);
      i--;
    } 
    float f3 = Core.settings.getInt("musicvol", 100) / 100.0F;
    for (byte b = 0; b < intSeq.size; b++) {
      int j = intSeq.get(b);
      Music music = (Music)playing.get(j);
      if (music == null) {
        loadAudio(j);
      } else {
        try {
          music.setVolume(Mathf.clamp(intFloatMap.get(j, 0.0F) * f3));
          if (!music.isPlaying())
            music.play(); 
        } catch (Throwable throwable) {}
      } 
    } 
    for (IntMap.Entry entry : playing) {
      if (intSeq.contains(entry.key))
        continue; 
      try {
        if (((Music)entry.value).isPlaying())
          ((Music)entry.value).stop(); 
      } catch (Throwable throwable) {}
    } 
  }
  
  private static Fi cached(int paramInt) {
    Fi fi = (Fi)files.get(paramInt);
    if (fi != null && fi.exists())
      return fi; 
    for (String str : new String[] { "ogg", "mp3", "wav" }) {
      Fi fi1 = AudioUi.dir().child(paramInt + "." + str);
      if (fi1.exists() && fi1.length() > 0L) {
        files.put(paramInt, fi1);
        return fi1;
      } 
    } 
    return null;
  }
  
  private static void startMusic(int paramInt, Fi paramFi) {
    try {
      Music music = new Music(paramFi);
      music.setLooping(true);
      music.setVolume(0.0F);
      playing.put(paramInt, music);
    } catch (Throwable throwable) {
      audioFailed.put(paramInt, Time.time);
      Log.warn("[Lunarians Skins] musica @: @", new Object[] { Integer.valueOf(paramInt), throwable.toString() });
    } 
  }
  
  private static void loadAudio(int paramInt) {
    if (audioLoading.contains(paramInt) || audioFailed.get(paramInt, -100000.0F) + 3600.0F >= Time.time)
      return; 
    Fi fi = cached(paramInt);
    if (fi != null) {
      startMusic(paramInt, fi);
      return;
    } 
    audioLoading.add(paramInt);
    HubClient.call("GET", "/api/audio/get?id=" + paramInt, null, paramJval -> {
          audioLoading.remove(paramInt);
          try {
            byte[] arrayOfByte = Base64Coder.decode(paramJval.getString("data", ""));
            String str = paramJval.getString("fmt", "ogg");
            if (arrayOfByte.length < 64 || !str.matches("ogg|mp3|wav"))
              throw new RuntimeException("invalido"); 
            Fi fi = AudioUi.dir().child(paramInt + "." + str);
            fi.writeBytes(arrayOfByte, false);
            files.put(paramInt, fi);
            startMusic(paramInt, fi);
          } catch (Throwable throwable) {
            audioFailed.put(paramInt, Time.time);
          } 
        }paramString -> {
          audioLoading.remove(paramInt);
          audioFailed.put(paramInt, Time.time);
        });
  }
  
  private static Label wrap(String paramString) {
    Label label = new Label(paramString);
    label.setWrap(true);
    return label;
  }
  
  private static boolean isPng(byte[] paramArrayOfbyte) {
    return (paramArrayOfbyte.length > 8 && (paramArrayOfbyte[0] & 0xFF) == 137 && paramArrayOfbyte[1] == 80 && paramArrayOfbyte[2] == 78 && paramArrayOfbyte[3] == 71);
  }
  
  static String partLabel(String paramString) {
    if (paramString.startsWith("weapon"))
      return HubText.get("skin.part.weapon") + " " + (Strings.parseInt(paramString.substring(6), 0) + 1); 
    return HubText.get("skin.part." + paramString);
  }
  
  private static UnitType unitType(String paramString) {
    return (UnitType)Vars.content.units().find(paramUnitType -> paramUnitType.name.equals(paramString));
  }
  
  static TextureRegion originalRegion(UnitType paramUnitType, String paramString) {
    if (paramUnitType == null)
      return null; 
    switch (paramString) {
      case "body":
        return paramUnitType.region;
      case "legs":
        return paramUnitType.legRegion;
      case "legbase":
        return paramUnitType.legBaseRegion;
      case "joint":
        return paramUnitType.jointRegion;
      case "base":
        return paramUnitType.baseRegion;
    } 
    if (paramString.startsWith("weapon")) {
      Seq<String> seq = weaponNames(paramUnitType);
      int i = Strings.parseInt(paramString.substring(6), -1);
      if (i >= 0 && i < seq.size)
        for (Weapon weapon : paramUnitType.weapons) {
          if (weapon.name.equals(seq.get(i)))
            return weapon.region; 
        }  
    } 
    return null;
  }
  
  static Seq<String> partsOf(UnitType paramUnitType) {
    Seq<String> seq1 = new Seq();
    seq1.add("body");
    for (String str : new String[] { "legs", "legbase", "joint", "base" }) {
      TextureRegion textureRegion = originalRegion(paramUnitType, str);
      if (textureRegion != null && textureRegion.found())
        seq1.add(str); 
    } 
    Seq<String> seq2 = weaponNames(paramUnitType);
    for (byte b = 0; b < seq2.size && b < 30; ) {
      seq1.add("weapon" + b);
      b++;
    } 
    return seq1;
  }
  
  public static void buildTab(Table paramTable, Runnable paramRunnable, float paramFloat) {
    paramTable.clear();
    paramTable.add((Element)wrap(HubText.get("skin.help"))).width(paramFloat - 10.0F).left().padBottom(6.0F).row();
    paramTable.check(HubText.get("skin.show"), showOthers(), paramBoolean -> {
          Core.settings.put("lunarians-hub-skins-show", Boolean.valueOf(paramBoolean));
          dirty = true;
        }).left().padBottom(6.0F).row();
    paramTable.button(HubText.get("skin.new"), (Drawable)Icon.add, () -> requestDialog(paramRunnable)).size(Math.min(paramFloat, 420.0F), 52.0F).padBottom(8.0F).row();
    Table table = new Table();
    paramTable.add((Element)table).width(paramFloat).row();
    table.add((Element)wrap("...")).row();
    HubClient.call("GET", "/api/skins/mine", null, paramJval -> {
          paramTable.clear();
          Jval.JsonArray jsonArray = paramJval.get("skins").asArray();
          updateMine(jsonArray);
          if (jsonArray.size == 0) {
            paramTable.add((Element)wrap(HubText.get("skin.none"))).width(paramFloat - 20.0F).pad(6.0F).row();
            return;
          } 
          UiStyle.section(paramTable, HubText.get("skin.mine"));
          for (Jval jval : jsonArray) {
            int i = jval.getInt("id", 0);
            String str1 = jval.getString("unit", "");
            String str2 = jval.getString("part", "body");
            String str3 = jval.getString("status", "pending");
            int j = jval.getInt("music", 0);
            int k = jval.getInt("music_radius", 0);
            paramTable.table(Styles.grayPanel, ()).width(paramFloat - 10.0F).pad(2.0F).row();
          } 
        }paramString -> {
          paramTable.clear();
          paramTable.add((Element)wrap(HubText.get("sent.fail"))).row();
        });
  }
  
  private static void musicDialog(int paramInt1, int paramInt2, int paramInt3, Runnable paramRunnable) {
    BaseDialog baseDialog = new BaseDialog(HubText.get("skin.music.title"));
    UiStyle.register(baseDialog);
    float f = Math.min(Core.scene.getWidth() - 40.0F, 520.0F);
    int[] arrayOfInt = { paramInt2 };
    baseDialog.cont.add((Element)wrap(HubText.get("skin.music.help"))).width(f).padBottom(6.0F).row();
    Table table = new Table();
    baseDialog.cont.pane((Element)table).width(f).maxHeight(Core.scene.getHeight() * 0.4F).row();
    table.add((Element)wrap("...")).row();
    Label label = new Label(String.valueOf((paramInt3 > 0) ? paramInt3 : 12));
    Slider slider = new Slider(1.0F, 30.0F, 1.0F, false);
    slider.setValue((paramInt3 > 0) ? paramInt3 : 12.0F);
    slider.moved(paramFloat -> paramLabel.setText(String.valueOf((int)paramFloat)));
    baseDialog.cont.table(paramTable -> {
          paramTable.add(HubText.get("skin.music.radius")).padRight(8.0F);
          paramTable.add((Element)paramSlider).width(Math.min(paramFloat - 150.0F, 260.0F));
          paramTable.add((Element)paramLabel).padLeft(8.0F).width(36.0F);
        }).padTop(6.0F).row();
    HubClient.call("GET", "/api/audio/mine", null, paramJval -> {
          paramTable.clear();
          byte b = 0;
          for (Jval jval : paramJval.get("audio").asArray()) {
            if (!"music".equals(jval.getString("kind", "")) || !"approved".equals(jval.getString("status", "")))
              continue; 
            int i = jval.getInt("id", 0);
            paramTable.button(jval.getString("name", "?"), Styles.togglet, ()).checked(()).growX().height(44.0F).pad(2.0F).row();
            b++;
          } 
          if (b == 0)
            paramTable.add((Element)wrap(HubText.get("skin.music.none"))).width(paramFloat - 20.0F).pad(6.0F).row(); 
        }paramString -> {
          paramTable.clear();
          paramTable.add((Element)wrap(HubText.get("sent.fail"))).row();
        });
    Runnable runnable = () -> {
        Jval jval = Jval.newObject();
        jval.add("id", Jval.valueOf(paramInt));
        jval.add("audio", Jval.valueOf(paramArrayOfint[0]));
        jval.add("radius", Jval.valueOf((int)paramSlider.getValue()));
        HubClient.call("POST", "/api/skins/music", jval, (), ());
      };
    baseDialog.cont.table(paramTable -> {
          paramTable.defaults().height(48.0F).pad(3.0F);
          paramTable.button(HubText.get("skin.music.save"), (Drawable)Icon.ok, ()).minWidth(150.0F);
          paramTable.button(HubText.get("skin.music.remove"), (Drawable)Icon.trash, ()).minWidth(150.0F);
        }).padTop(8.0F).row();
    baseDialog.addCloseButton();
    baseDialog.show();
  }
  
  static void pickUnit(Cons<String> paramCons) {
    BaseDialog baseDialog = new BaseDialog(HubText.get("skin.pickunit"));
    UiStyle.register(baseDialog);
    Table table = new Table();
    float f = Math.min(Core.scene.getWidth() - 40.0F, 520.0F);
    int i = Math.max(3, (int)(f / 76.0F));
    byte b = 0;
    for (UnitType unitType : Vars.content.units()) {
      if (unitType.isVanilla() && !unitType.internal && unitType.uiIcon != null && okUnit(unitType.name)) {
        Table table1 = new Table();
        table1.button((Drawable)new TextureRegionDrawable(unitType.uiIcon), Styles.cleari, 44.0F, () -> {
              paramCons.get(paramUnitType.name);
              paramBaseDialog.hide();
            }).size(56.0F);
        table1.row();
        table1.add(unitType.name).fontScale(0.55F);
        table.add((Element)table1).pad(2.0F);
        if (++b % i == 0)
          table.row(); 
      } 
    } 
    baseDialog.cont.pane((Element)table).width(f).maxHeight(Core.scene.getHeight() * 0.65F).row();
    baseDialog.addCloseButton();
    baseDialog.show();
  }
  
  private static void pickPart(UnitType paramUnitType, Cons<String> paramCons) {
    BaseDialog baseDialog = new BaseDialog(HubText.get("skin.pickpart"));
    UiStyle.register(baseDialog);
    float f = Math.min(Core.scene.getWidth() - 40.0F, 420.0F);
    Table table = new Table();
    for (String str1 : partsOf(paramUnitType)) {
      TextureRegion textureRegion = originalRegion(paramUnitType, str1);
      String str2 = (textureRegion != null && textureRegion.found()) ? ("  (" + textureRegion.width + "x" + textureRegion.height + ")") : "";
      table.button(partLabel(str1) + str2, () -> {
            paramCons.get(paramString);
            paramBaseDialog.hide();
          }).growX().height(46.0F).pad(2.0F).row();
    } 
    baseDialog.cont.pane((Element)table).width(f).maxHeight(Core.scene.getHeight() * 0.65F).row();
    baseDialog.addCloseButton();
    baseDialog.show();
  }
  
  public static void requestDialog(Runnable paramRunnable) {
    BaseDialog baseDialog = new BaseDialog(HubText.get("skin.new"));
    UiStyle.register(baseDialog);
    float f = Math.min(Core.scene.getWidth() - 40.0F, 520.0F);
    String[] arrayOfString1 = { "" };
    String[] arrayOfString2 = { "body" };
    byte[][] arrayOfByte = { null };
    String[] arrayOfString3 = { "" };
    TextField textField = new TextField();
    textField.setMessageText(HubText.get("skin.name"));
    textField.setMaxLength(32);
    Label label = wrap("");
    TextButton textButton1 = new TextButton(HubText.get("skin.pickunit"), Styles.defaultt);
    TextButton textButton2 = new TextButton(HubText.get("skin.pickpart"), Styles.defaultt);
    TextButton textButton3 = new TextButton(HubText.get("skin.pickfile"), Styles.defaultt);
    Runnable runnable = () -> {
        paramTextButton1.setText(paramArrayOfString1[0].isEmpty() ? HubText.get("skin.pickunit") : (HubText.get("skin.unit") + ": " + paramArrayOfString1[0]));
        paramTextButton2.setText(paramArrayOfString1[0].isEmpty() ? HubText.get("skin.pickpart") : (HubText.get("skin.part") + ": " + partLabel(paramArrayOfString2[0])));
        paramTextButton3.setText((paramArrayOfbyte[0] == null) ? HubText.get("skin.pickfile") : ("PNG " + paramArrayOfString3[0]));
        String str = "";
        UnitType unitType = paramArrayOfString1[0].isEmpty() ? null : unitType(paramArrayOfString1[0]);
        TextureRegion textureRegion = originalRegion(unitType, paramArrayOfString2[0]);
        if (textureRegion != null && textureRegion.found())
          str = HubText.get("skin.sprite") + ": " + textureRegion.width + "x" + textureRegion.height; 
        paramLabel.setText(str);
      };
    textButton1.clicked(() -> pickUnit(()));
    textButton2.clicked(() -> {
          UnitType unitType = paramArrayOfString1[0].isEmpty() ? null : unitType(paramArrayOfString1[0]);
          if (unitType == null) {
            Vars.ui.showInfoToast(HubText.get("skin.fill"), 2.0F);
            return;
          } 
          pickPart(unitType, ());
        });
    textButton3.clicked(() -> (new FileChooser.FileChooserParams()).open(true).title(HubText.get("skin.pickfile")).extensions(new String[] { "png" }).submit(()));
    baseDialog.cont.add((Element)wrap(HubText.get("skin.help"))).width(f).padBottom(6.0F).row();
    baseDialog.cont.add((Element)textField).width(f).height(46.0F).row();
    baseDialog.cont.add((Element)textButton1).width(f).height(48.0F).padTop(6.0F).row();
    baseDialog.cont.add((Element)textButton2).width(f).height(48.0F).padTop(6.0F).row();
    baseDialog.cont.add((Element)textButton3).width(f).height(48.0F).padTop(6.0F).row();
    baseDialog.cont.add((Element)label).width(f).padTop(6.0F).row();
    baseDialog.cont.button(HubText.get("skin.send"), (Drawable)Icon.upload, () -> {
          String str = paramTextField.getText().trim();
          if (str.isEmpty() || paramArrayOfString1[0].isEmpty() || paramArrayOfbyte[0] == null) {
            Vars.ui.showInfoToast(HubText.get("skin.fill"), 2.5F);
            return;
          } 
          Jval jval = Jval.newObject();
          jval.add("name", str);
          jval.add("unit", paramArrayOfString1[0]);
          jval.add("part", paramArrayOfString2[0]);
          jval.add("png", new String(Base64Coder.encode(paramArrayOfbyte[0])));
          HubClient.call("POST", "/api/skins/request", jval, (), ());
        }).size(Math.min(f, 320.0F), 52.0F).padTop(10.0F).row();
    baseDialog.addCloseButton();
    baseDialog.shown(runnable);
    baseDialog.show();
  }
}
