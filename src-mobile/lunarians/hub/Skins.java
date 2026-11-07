package lunarians.hub;

import arc.Core;
import arc.Events;
import arc.Settings;
import arc.audio.Music;
import arc.files.Fi;
import arc.func.Cons;
import arc.graphics.Camera;
import arc.graphics.Pixmap;
import arc.graphics.Texture;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.scene.Element;
import arc.scene.Scene;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Image;
import arc.scene.ui.ImageButton;
import arc.scene.ui.Label;
import arc.scene.ui.Slider;
import arc.scene.ui.TextButton;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Cell;
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
import mindustry.core.GameState;
import mindustry.core.UI;
import mindustry.entities.EntityGroup;
import mindustry.entities.Leg;
import mindustry.entities.units.WeaponMount;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Groups;
import mindustry.gen.Icon;
import mindustry.gen.Legsc;
import mindustry.gen.Mechc;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.ui.FileChooser;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public class Skins {
  private static final String K_EQ = "lunarians-hub-skins-eq";
  
  private static final String K_SHOW = "lunarians-hub-skins-show";
  
  private static final int MAX_EQUIPPED = 8;
  
  private static final float PEER_TTL = 6000.0F;
  
  private static final IntFloatMap audioFailed;
  
  private static final IntSet audioLoading;
  
  private static boolean dirty;
  
  private static final IntFloatMap failedAt;
  
  private static final IntMap files;
  
  private static boolean inited;
  
  private static final Seq mine;
  
  private static boolean mineLoaded;
  
  private static float nextMine;
  
  private static float nextMusic;
  
  private static float nextPool;
  
  private static final IntMap peers;
  
  private static final IntMap playing;
  
  private static final ObjectMap pools;
  
  private static final IntMap tex;
  
  private static final Vec2 tv1;
  
  private static final Vec2 tv2;
  
  private static final IntMap waiting;
  
  private static final ObjectMap weaponNames;
  
  static {
    Seq seq = new Seq();
    this();
    mine = seq;
    IntMap intMap3 = new IntMap();
    this();
    peers = intMap3;
    ObjectMap objectMap2 = new ObjectMap();
    this();
    pools = objectMap2;
    IntMap intMap2 = new IntMap();
    this();
    tex = intMap2;
    intMap2 = new IntMap();
    this();
    waiting = intMap2;
    IntFloatMap intFloatMap2 = new IntFloatMap();
    this();
    failedAt = intFloatMap2;
    ObjectMap objectMap1 = new ObjectMap();
    this();
    weaponNames = objectMap1;
    Vec2 vec2 = new Vec2();
    this();
    tv1 = vec2;
    vec2 = new Vec2();
    this();
    tv2 = vec2;
    dirty = true;
    IntMap intMap1 = new IntMap();
    this();
    playing = intMap1;
    intMap1 = new IntMap();
    this();
    files = intMap1;
    IntFloatMap intFloatMap1 = new IntFloatMap();
    this();
    audioFailed = intFloatMap1;
    IntSet intSet = new IntSet();
    this();
    audioLoading = intSet;
  }
  
  private static void add(Skins$Entry paramSkins$Entry) {
    ObjectMap objectMap2 = pools;
    String str2 = paramSkins$Entry.unit;
    Skins$UnitPool skins$UnitPool = (Skins$UnitPool)objectMap2.get(str2);
    if (skins$UnitPool == null) {
      skins$UnitPool = new Skins$UnitPool();
      boolean bool1 = false;
      this(null);
      String str = paramSkins$Entry.unit;
      objectMap2.put(str, skins$UnitPool);
    } 
    IntMap intMap = skins$UnitPool.byOwner;
    int i = paramSkins$Entry.owner;
    ObjectMap objectMap1 = (ObjectMap)intMap.get(i);
    if (objectMap1 == null) {
      objectMap1 = new ObjectMap();
      this();
      IntMap intMap1 = skins$UnitPool.byOwner;
      int j = paramSkins$Entry.owner;
      intMap1.put(j, objectMap1);
      Seq seq = skins$UnitPool.owners;
      i = paramSkins$Entry.owner;
      Integer integer = Integer.valueOf(i);
      seq.add(integer);
    } 
    String str1 = paramSkins$Entry.part;
    boolean bool = objectMap1.containsKey(str1);
    if (!bool) {
      str1 = paramSkins$Entry.part;
      objectMap1.put(str1, paramSkins$Entry);
    } 
  }
  
  public static void buildTab(Table paramTable, Runnable paramRunnable, float paramFloat) {
    paramTable.clear();
    Label label2 = wrap(HubText.get("skin.help"));
    Cell cell2 = paramTable.add((Element)label2);
    float f = paramFloat - 10.0F;
    cell2 = cell2.width(f).left();
    f = 6.0F;
    cell2.padBottom(f).row();
    String str = HubText.get("skin.show");
    boolean bool = showOthers();
    Skins$21 skins$21 = new Skins$21();
    this();
    paramTable.check(str, bool, skins$21).left().padBottom(f).row();
    str = HubText.get("skin.new");
    TextureRegionDrawable textureRegionDrawable = Icon.add;
    Skins$22 skins$22 = new Skins$22();
    this(paramRunnable);
    Cell cell1 = paramTable.button(str, (Drawable)textureRegionDrawable, skins$22);
    f = Math.min(paramFloat, 420.0F);
    cell1.size(f, 52.0F).padBottom(8.0F).row();
    Table table = new Table();
    this();
    paramTable.add((Element)table).width(paramFloat).row();
    Label label1 = wrap("...");
    table.add((Element)label1).row();
    Skins$23 skins$23 = new Skins$23();
    this(table, paramFloat, paramRunnable);
    Skins$24 skins$24 = new Skins$24();
    this(table);
    HubClient.call("GET", "/api/skins/mine", null, skins$23, skins$24);
  }
  
  private static Fi cached(int paramInt) {
    Fi fi = (Fi)files.get(paramInt);
    if (fi != null) {
      boolean bool = fi.exists();
      if (bool)
        return fi; 
    } 
    String str = "ogg";
    String[] arrayOfString = { str, "mp3", "wav" };
    int i = 0;
    while (true) {
      byte b = 3;
      if (i < b) {
        str = arrayOfString[i];
        Fi fi2 = AudioUi.dir();
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append(paramInt);
        String str1 = ".";
        stringBuilder.append(str1);
        stringBuilder.append(str);
        str = stringBuilder.toString();
        Fi fi1 = fi2.child(str);
        boolean bool = fi1.exists();
        if (bool) {
          long l1 = fi1.length();
          long l2 = 0L;
          int j = l1 cmp l2;
          if (j > 0) {
            files.put(paramInt, fi1);
            return fi1;
          } 
        } 
        i++;
        continue;
      } 
      return null;
    } 
  }
  
  private static void draw() {
    try {
      ObjectMap objectMap = pools;
      int i = objectMap.size;
      if (i != 0) {
        boolean bool = HubClient.consented();
        if (bool) {
          GameState gameState = Vars.state;
          if (gameState != null) {
            gameState = Vars.state;
            bool = gameState.isGame();
            if (bool) {
              Player player = Vars.player;
              if (player != null) {
                Camera camera = Core.camera;
                Rect rect = Tmp.r1;
                camera.bounds(rect);
                Player player1 = Vars.player;
                Team team = player1.team();
                float f = Draw.z();
                EntityGroup entityGroup = Groups.unit;
                Iterator<Object> iterator = entityGroup.iterator();
                while (true) {
                  boolean bool1 = iterator.hasNext();
                  if (bool1) {
                    Unit unit = (Unit)iterator.next();
                    unit = unit;
                    if (unit != null) {
                      boolean bool2 = unit.isAdded();
                      if (bool2) {
                        bool2 = unit.dead;
                        if (!bool2) {
                          UnitType unitType = unit.type;
                          if (unitType != null) {
                            ObjectMap objectMap1 = pools;
                            UnitType unitType1 = unit.type;
                            String str = unitType1.name;
                            Object object = objectMap1.get(str);
                            object = object;
                            if (object != null) {
                              Rect rect1 = Tmp.r2;
                              Rect rect2 = Tmp.r1;
                              rect1 = rect1.set(rect2);
                              float f1 = unit.hitSize * 2.0F;
                              float f2 = 16.0F;
                              f1 += f2;
                              rect1.grow(f1);
                              rect1 = Tmp.r2;
                              f1 = unit.x;
                              f2 = unit.y;
                              boolean bool3 = rect1.contains(f1, f2);
                              if (bool3) {
                                bool3 = unit.inFogTo(team);
                                if (!bool3) {
                                  object = skinsFor(unit, (Skins$UnitPool)object);
                                  if (object != null)
                                    try {
                                      drawUnit(unit, (ObjectMap)object);
                                    } finally {} 
                                } 
                              } 
                            } 
                          } 
                        } 
                      } 
                    } 
                    continue;
                  } 
                  Draw.z(f);
                  Draw.reset();
                  return;
                } 
              } 
            } 
          } 
        } 
      } 
    } finally {}
  }
  
  private static void drawLegSkin(Unit paramUnit, Legsc paramLegsc, UnitType paramUnitType, TextureRegion paramTextureRegion1, TextureRegion paramTextureRegion2, TextureRegion paramTextureRegion3, TextureRegion paramTextureRegion4, float paramFloat) {
    TextureRegion textureRegion2;
    TextureRegion textureRegion3;
    TextureRegion textureRegion4;
    Unit unit = paramUnit;
    UnitType unitType = paramUnitType;
    TextureRegion textureRegion1 = paramTextureRegion4;
    Draw.z(paramFloat);
    paramUnitType.applyColor(paramUnit);
    if (paramTextureRegion1 != null) {
      textureRegion2 = paramTextureRegion1;
    } else {
      textureRegion3 = paramUnitType.legRegion;
      textureRegion2 = textureRegion3;
    } 
    if (paramTextureRegion2 != null) {
      textureRegion4 = paramTextureRegion2;
    } else {
      textureRegion3 = unitType.legBaseRegion;
      textureRegion4 = textureRegion3;
    } 
    if (paramTextureRegion3 != null) {
      textureRegion3 = paramTextureRegion3;
    } else {
      textureRegion3 = unitType.jointRegion;
    } 
    Leg[] arrayOfLeg = paramLegsc.legs();
    int i = arrayOfLeg.length;
    byte b = 1;
    float f = Float.MIN_VALUE;
    i -= b;
    int j = i;
    while (j >= 0) {
      TextureRegion textureRegion5;
      TextureRegion textureRegion6;
      i = j % 2;
      if (i == 0) {
        i = j / 2;
      } else {
        i = arrayOfLeg.length - b;
        int n = j / 2;
        i -= n;
      } 
      Leg leg = arrayOfLeg[i];
      float f1 = i;
      int m = arrayOfLeg.length;
      float f2 = m;
      float f3 = 2.0F;
      f2 /= f3;
      int k = f1 cmp f2;
      if (k >= 0) {
        k = 1;
        f1 = Float.MIN_VALUE;
      } else {
        k = 0;
        f1 = 0.0F;
        Object object = null;
      } 
      k = Mathf.sign(k);
      Vec2 vec21 = tv2;
      Vec2 vec22 = paramLegsc.legOffset(vec21, i);
      f2 = unit.x;
      f3 = unit.y;
      vec22 = vec22.add(f2, f3);
      Vec2 vec23 = tv1;
      vec21 = leg.base;
      vec21 = vec23.set(vec21);
      Vec2 vec24 = leg.joint;
      vec21 = vec21.sub(vec24).inv();
      f3 = unitType.legExtension;
      vec21.setLength(f3);
      boolean bool = unitType.legBaseUnder;
      if (bool) {
        int n = textureRegion4.height;
        f2 = n;
        f3 = unitType.legRegion.scl();
        f2 *= f3;
        f1 = k;
        Lines.stroke(f2 * f1);
        f2 = leg.joint.x;
        f3 = vec23.x;
        f2 += f3;
        f3 = leg.joint.y;
        float f4 = vec23.y;
        f3 += f4;
        f4 = leg.base.x;
        f = leg.base.y;
        Vec2 vec26 = vec22;
        float f5 = f2;
        f2 = f4;
        f4 = 0.0F;
        float f6 = f1;
        f1 = f3;
        f3 = f;
        Vec2 vec27 = vec22;
        Lines.line(textureRegion4, f5, f1, f2, f, false);
        textureRegion6 = textureRegion4;
        int i1 = textureRegion2.height;
        float f7 = i1;
        textureRegion5 = unitType.legRegion;
        f5 = textureRegion5.scl();
        Lines.stroke(f7 * f5 * f6);
        f7 = vec26.x;
        f5 = vec26.y;
        Vec2 vec25 = leg.joint;
        f1 = vec25.x;
        vec21 = leg.joint;
        f2 = vec21.y;
        f3 = 0.0F;
        vec24 = null;
        Lines.line(textureRegion2, f7, f5, f1, f2, false);
      } else {
        TextureRegion textureRegion8 = textureRegion5;
        i = textureRegion2.height;
        float f5 = i;
        TextureRegion textureRegion7 = unitType.legRegion;
        f2 = textureRegion7.scl();
        f5 *= f2;
        f1 = k;
        Lines.stroke(f5 * f1);
        textureRegion5 = textureRegion4;
        float f7 = ((Vec2)textureRegion8).x;
        f2 = ((Vec2)textureRegion8).y;
        f3 = leg.joint.x;
        f = leg.joint.y;
        float f6 = f1;
        f1 = f3;
        vec24 = null;
        textureRegion8 = textureRegion4;
        f5 = f2;
        f2 = f;
        Lines.line(textureRegion2, f7, f5, f3, f, false);
        int n = textureRegion4.height;
        f7 = n;
        textureRegion5 = unitType.legRegion;
        f5 = textureRegion5.scl();
        Lines.stroke(f7 * f5 * f6);
        f7 = leg.joint.x;
        f5 = ((Vec2)textureRegion6).x + f7;
        f7 = leg.joint.y;
        f1 = ((Vec2)textureRegion6).y + f7;
        f2 = leg.base.x;
        f3 = leg.base.y;
        float f4 = 0.0F;
        textureRegion6 = null;
        textureRegion4 = textureRegion8;
        Lines.line(textureRegion8, f5, f1, f2, f3, false);
      } 
      if (textureRegion3 != null) {
        boolean bool1 = textureRegion3.found();
        if (bool1) {
          Vec2 vec26 = leg.joint;
          float f4 = vec26.x;
          Vec2 vec25 = leg.joint;
          f1 = vec25.y;
          Draw.rect(textureRegion3, f4, f1);
        } 
      } 
      j--;
      b = 1;
      f = Float.MIN_VALUE;
    } 
    if (textureRegion1 != null) {
      float f1 = unit.x;
      float f2 = unit.y;
      float f3 = paramLegsc.baseRotation();
      float f4 = 90.0F;
      f3 -= f4;
      Draw.rect(textureRegion1, f1, f2, f3);
    } 
    Draw.reset();
  }
  
  private static void drawMechSkin(Unit paramUnit, Mechc paramMechc, UnitType paramUnitType, TextureRegion paramTextureRegion1, TextureRegion paramTextureRegion2, float paramFloat) {
    Unit unit = paramUnit;
    Mechc mechc = paramMechc;
    UnitType unitType = paramUnitType;
    TextureRegion textureRegion1 = paramTextureRegion1;
    TextureRegion textureRegion2 = paramTextureRegion2;
    Draw.z(paramFloat);
    paramUnitType.applyColor(paramUnit);
    float f1 = paramUnit.elevation;
    float f2 = paramMechc.walkExtend(true);
    float f3 = 1.0F;
    f2 = Mathf.sin(f2, 0.63661975F, f3);
    float f4 = 0.0F;
    TextureRegion textureRegion3 = null;
    float f5 = Mathf.lerp(f2, 0.0F, f1);
    int i = 0;
    f2 = 0.0F;
    TextureRegion textureRegion4 = null;
    float f6 = Mathf.lerp(paramMechc.walkExtend(false), 0.0F, f1);
    float f7 = 2.0F;
    float f8 = f1 * f7;
    float f9 = 90.0F;
    if (paramTextureRegion1 != null) {
      int[] arrayOfInt = Mathf.signs;
      int j = arrayOfInt.length;
      while (i < j) {
        int k = arrayOfInt[i];
        float f10 = unit.x;
        paramFloat = 1.0F;
        f3 = mechc.baseRotation();
        float f11 = k;
        float f12 = f6 * f11;
        f4 = f12 - f8;
        float f13 = -f8 * f11;
        f3 = Angles.trnsx(f3, f4, f13);
        f10 += f3;
        f3 = unit.y;
        int m = i;
        f13 = Angles.trnsy(paramMechc.baseRotation(), f4, f13);
        f3 += f13;
        f13 = textureRegion1.width;
        textureRegion4 = unitType.legRegion;
        f2 = textureRegion4.scl();
        f13 = f13 * f2 * f11;
        f2 = textureRegion1.height;
        textureRegion3 = unitType.legRegion;
        f4 = textureRegion3.scl();
        f2 *= f4;
        f4 = -f5 * f11;
        float f14 = f13;
        f13 = 0.0F;
        f4 = Math.max(f4, 0.0F) * 0.5F;
        f4 = paramFloat - f4;
        f2 *= f4;
        f4 = paramMechc.baseRotation() - f9;
        float f15 = 35.0F;
        f11 = f11 * f15 * f1;
        f4 += f11;
        f11 = f3;
        f7 = f3;
        f11 = f2;
        f2 = f10;
        f10 = f4;
        Draw.rect(textureRegion1, f2, f3, f14, f11, f4);
        i = m + 1;
        mechc = paramMechc;
        textureRegion1 = paramTextureRegion1;
      } 
    } 
    if (textureRegion2 != null) {
      float f10 = unit.x;
      float f11 = unit.y;
      float f12 = paramMechc.baseRotation() - f9;
      Draw.rect(textureRegion2, f10, f11, f12);
    } 
    Draw.reset();
  }
  
  private static void drawMount(Unit paramUnit, WeaponMount paramWeaponMount, Weapon paramWeapon, TextureRegion paramTextureRegion, float paramFloat) {
    float f3;
    paramFloat += 0.0106F;
    float f1 = paramWeapon.layerOffset;
    Draw.z(paramFloat + f1);
    UnitType unitType = paramUnit.type;
    unitType.applyColor(paramUnit);
    paramFloat = paramUnit.rotation - 90.0F;
    f1 = paramWeaponMount.recoil;
    float f2 = paramWeapon.recoilPow;
    f1 = Mathf.pow(f1, f2);
    f2 = paramWeapon.recoil;
    f1 *= f2;
    boolean bool = paramWeapon.rotate;
    if (bool) {
      f3 = paramWeaponMount.rotation;
    } else {
      f3 = paramWeapon.baseRotation;
    } 
    f3 += paramFloat;
    f2 = paramUnit.x;
    float f4 = paramWeapon.x;
    float f5 = paramWeapon.y;
    f4 = Angles.trnsx(paramFloat, f4, f5);
    f2 += f4;
    f1 = -f1;
    f5 = Angles.trnsx(f3, 0.0F, f1);
    f2 += f5;
    float f6 = paramUnit.y;
    f5 = paramWeapon.x;
    float f7 = paramWeapon.y;
    paramFloat = Angles.trnsy(paramFloat, f5, f7);
    f6 += paramFloat;
    paramFloat = Angles.trnsy(f3, 0.0F, f1);
    f6 += paramFloat;
    paramFloat = Draw.xscl;
    f1 = Draw.xscl;
    float f8 = -Mathf.sign(paramWeapon.flipSprite);
    Draw.xscl = f1 * f8;
    Draw.rect(paramTextureRegion, f2, f6, f3);
    Draw.xscl = paramFloat;
    Draw.reset();
  }
  
  private static void drawUnit(Unit paramUnit, ObjectMap paramObjectMap) {
    // Byte code:
    //   0: aload_0
    //   1: getfield type : Lmindustry/type/UnitType;
    //   4: astore_2
    //   5: aload_0
    //   6: getfield elevation : F
    //   9: fstore_3
    //   10: ldc_w 0.5
    //   13: fstore #4
    //   15: ldc_w 1008981770
    //   18: istore #5
    //   20: ldc_w 0.01
    //   23: fstore #6
    //   25: fload_3
    //   26: fload #4
    //   28: fcmpl
    //   29: istore #7
    //   31: iload #7
    //   33: ifgt -> 100
    //   36: aload_2
    //   37: getfield flying : Z
    //   40: istore #7
    //   42: iload #7
    //   44: ifeq -> 58
    //   47: aload_0
    //   48: getfield dead : Z
    //   51: istore #7
    //   53: iload #7
    //   55: ifne -> 100
    //   58: aload_2
    //   59: getfield groundLayer : F
    //   62: fstore_3
    //   63: aload_2
    //   64: getfield hitSize : F
    //   67: ldc_w 4000.0
    //   70: fdiv
    //   71: fstore #4
    //   73: iconst_0
    //   74: istore #8
    //   76: fconst_0
    //   77: fstore #9
    //   79: aconst_null
    //   80: astore #10
    //   82: fload #4
    //   84: fconst_0
    //   85: fload #6
    //   87: invokestatic clamp : (FFF)F
    //   90: fstore #4
    //   92: fload_3
    //   93: fload #4
    //   95: fadd
    //   96: fstore_3
    //   97: goto -> 105
    //   100: aload_2
    //   101: getfield flyingLayer : F
    //   104: fstore_3
    //   105: fload_3
    //   106: fstore #11
    //   108: aload_0
    //   109: instanceof mindustry/gen/Legsc
    //   112: istore #7
    //   114: ldc_w 'base'
    //   117: astore #12
    //   119: ldc_w 'legs'
    //   122: astore #10
    //   124: iload #7
    //   126: ifeq -> 230
    //   129: ldc_w 1008981770
    //   132: istore #7
    //   134: ldc_w 0.01
    //   137: fstore_3
    //   138: aload_1
    //   139: aload #10
    //   141: invokestatic skin : (Larc/struct/ObjectMap;Ljava/lang/String;)Larc/graphics/g2d/TextureRegion;
    //   144: astore #13
    //   146: aload_1
    //   147: ldc_w 'legbase'
    //   150: invokestatic skin : (Larc/struct/ObjectMap;Ljava/lang/String;)Larc/graphics/g2d/TextureRegion;
    //   153: astore #10
    //   155: aload_1
    //   156: ldc_w 'joint'
    //   159: invokestatic skin : (Larc/struct/ObjectMap;Ljava/lang/String;)Larc/graphics/g2d/TextureRegion;
    //   162: astore #14
    //   164: aload_1
    //   165: aload #12
    //   167: invokestatic skin : (Larc/struct/ObjectMap;Ljava/lang/String;)Larc/graphics/g2d/TextureRegion;
    //   170: astore #15
    //   172: aload #13
    //   174: ifnonnull -> 192
    //   177: aload #10
    //   179: ifnonnull -> 192
    //   182: aload #14
    //   184: ifnonnull -> 192
    //   187: aload #15
    //   189: ifnull -> 313
    //   192: aload_0
    //   193: astore #12
    //   195: aload_0
    //   196: checkcast mindustry/gen/Legsc
    //   199: astore #12
    //   201: fload #11
    //   203: fload_3
    //   204: fsub
    //   205: fstore #16
    //   207: aload_0
    //   208: astore #17
    //   210: aload_0
    //   211: aload #12
    //   213: aload_2
    //   214: aload #13
    //   216: aload #10
    //   218: aload #14
    //   220: aload #15
    //   222: fload #16
    //   224: invokestatic drawLegSkin : (Lmindustry/gen/Unit;Lmindustry/gen/Legsc;Lmindustry/type/UnitType;Larc/graphics/g2d/TextureRegion;Larc/graphics/g2d/TextureRegion;Larc/graphics/g2d/TextureRegion;Larc/graphics/g2d/TextureRegion;F)V
    //   227: goto -> 313
    //   230: ldc_w 1008981770
    //   233: istore #7
    //   235: ldc_w 0.01
    //   238: fstore_3
    //   239: aload_0
    //   240: instanceof mindustry/gen/Mechc
    //   243: istore #5
    //   245: iload #5
    //   247: ifeq -> 313
    //   250: aload_1
    //   251: aload #10
    //   253: invokestatic skin : (Larc/struct/ObjectMap;Ljava/lang/String;)Larc/graphics/g2d/TextureRegion;
    //   256: astore #13
    //   258: aload_1
    //   259: aload #12
    //   261: invokestatic skin : (Larc/struct/ObjectMap;Ljava/lang/String;)Larc/graphics/g2d/TextureRegion;
    //   264: astore #10
    //   266: aload #13
    //   268: ifnonnull -> 276
    //   271: aload #10
    //   273: ifnull -> 313
    //   276: aload_0
    //   277: astore #12
    //   279: aload_0
    //   280: checkcast mindustry/gen/Mechc
    //   283: astore #12
    //   285: fload #11
    //   287: fload_3
    //   288: fsub
    //   289: fstore #18
    //   291: aload_0
    //   292: astore #17
    //   294: aload_0
    //   295: checkcast mindustry/gen/Unit
    //   298: aload #12
    //   300: aload_2
    //   301: aload #13
    //   303: aload #10
    //   305: fload #18
    //   307: invokestatic drawMechSkin : (Lmindustry/gen/Unit;Lmindustry/gen/Mechc;Lmindustry/type/UnitType;Larc/graphics/g2d/TextureRegion;Larc/graphics/g2d/TextureRegion;F)V
    //   310: goto -> 316
    //   313: aload_0
    //   314: astore #17
    //   316: aload_1
    //   317: ldc_w 'body'
    //   320: invokestatic skin : (Larc/struct/ObjectMap;Ljava/lang/String;)Larc/graphics/g2d/TextureRegion;
    //   323: astore_0
    //   324: aload_0
    //   325: ifnull -> 401
    //   328: ldc_w 0.0105
    //   331: fload #11
    //   333: fadd
    //   334: invokestatic z : (F)V
    //   337: aload_2
    //   338: aload #17
    //   340: checkcast mindustry/gen/Unit
    //   343: invokevirtual applyColor : (Lmindustry/gen/Unit;)V
    //   346: aload #17
    //   348: checkcast mindustry/gen/Unit
    //   351: getfield x : F
    //   354: fstore #4
    //   356: aload #17
    //   358: checkcast mindustry/gen/Unit
    //   361: getfield y : F
    //   364: fstore #6
    //   366: aload #17
    //   368: checkcast mindustry/gen/Unit
    //   371: getfield rotation : F
    //   374: fstore #9
    //   376: ldc_w 90.0
    //   379: fstore #18
    //   381: fload #9
    //   383: fload #18
    //   385: fsub
    //   386: fstore #9
    //   388: aload_0
    //   389: fload #4
    //   391: fload #6
    //   393: fload #9
    //   395: invokestatic rect : (Larc/graphics/g2d/TextureRegion;FFF)V
    //   398: invokestatic reset : ()V
    //   401: aload #17
    //   403: checkcast mindustry/gen/Unit
    //   406: getfield mounts : [Lmindustry/entities/units/WeaponMount;
    //   409: astore #12
    //   411: aload #12
    //   413: ifnull -> 650
    //   416: aload_2
    //   417: invokestatic weaponNames : (Lmindustry/type/UnitType;)Larc/struct/Seq;
    //   420: astore #12
    //   422: aload #17
    //   424: checkcast mindustry/gen/Unit
    //   427: getfield mounts : [Lmindustry/entities/units/WeaponMount;
    //   430: astore_2
    //   431: aload_2
    //   432: arraylength
    //   433: istore #5
    //   435: iconst_0
    //   436: istore #8
    //   438: fconst_0
    //   439: fstore #9
    //   441: aconst_null
    //   442: astore #10
    //   444: iload #8
    //   446: iload #5
    //   448: if_icmpge -> 650
    //   451: aload_2
    //   452: iload #8
    //   454: aaload
    //   455: astore #14
    //   457: aload #14
    //   459: getfield weapon : Lmindustry/type/Weapon;
    //   462: astore #15
    //   464: aload #15
    //   466: ifnull -> 641
    //   469: aload #15
    //   471: getfield name : Ljava/lang/String;
    //   474: astore #19
    //   476: aload #19
    //   478: ifnull -> 641
    //   481: aload #15
    //   483: getfield name : Ljava/lang/String;
    //   486: astore #19
    //   488: aload #19
    //   490: invokevirtual isEmpty : ()Z
    //   493: istore #20
    //   495: iload #20
    //   497: ifne -> 641
    //   500: aload #15
    //   502: getfield name : Ljava/lang/String;
    //   505: astore #19
    //   507: aload #12
    //   509: aload #19
    //   511: invokevirtual indexOf : (Ljava/lang/Object;)I
    //   514: istore #20
    //   516: iload #20
    //   518: iflt -> 564
    //   521: new java/lang/StringBuilder
    //   524: astore #21
    //   526: ldc_w 'weapon'
    //   529: astore #22
    //   531: aload #21
    //   533: aload #22
    //   535: invokespecial <init> : (Ljava/lang/String;)V
    //   538: aload #21
    //   540: iload #20
    //   542: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   545: pop
    //   546: aload #21
    //   548: invokevirtual toString : ()Ljava/lang/String;
    //   551: astore #19
    //   553: aload_1
    //   554: aload #19
    //   556: invokestatic skin : (Larc/struct/ObjectMap;Ljava/lang/String;)Larc/graphics/g2d/TextureRegion;
    //   559: astore #19
    //   561: goto -> 573
    //   564: iconst_0
    //   565: istore #20
    //   567: fconst_0
    //   568: fstore #16
    //   570: aconst_null
    //   571: astore #19
    //   573: aload #19
    //   575: ifnonnull -> 620
    //   578: aload_0
    //   579: ifnull -> 620
    //   582: aload #15
    //   584: getfield region : Larc/graphics/g2d/TextureRegion;
    //   587: astore #21
    //   589: aload #21
    //   591: ifnull -> 620
    //   594: aload #15
    //   596: getfield region : Larc/graphics/g2d/TextureRegion;
    //   599: astore #21
    //   601: aload #21
    //   603: invokevirtual found : ()Z
    //   606: istore #23
    //   608: iload #23
    //   610: ifeq -> 620
    //   613: aload #15
    //   615: getfield region : Larc/graphics/g2d/TextureRegion;
    //   618: astore #19
    //   620: aload #19
    //   622: ifnull -> 641
    //   625: aload #17
    //   627: checkcast mindustry/gen/Unit
    //   630: aload #14
    //   632: aload #15
    //   634: aload #19
    //   636: fload #11
    //   638: invokestatic drawMount : (Lmindustry/gen/Unit;Lmindustry/entities/units/WeaponMount;Lmindustry/type/Weapon;Larc/graphics/g2d/TextureRegion;F)V
    //   641: iload #8
    //   643: iconst_1
    //   644: iadd
    //   645: istore #8
    //   647: goto -> 444
    //   650: invokestatic reset : ()V
    //   653: return
  }
  
  public static void equip(int paramInt) {
    // Byte code:
    //   0: iload_0
    //   1: invokestatic mineById : (I)Llunarians/hub/Skins$Entry;
    //   4: astore_1
    //   5: aload_1
    //   6: ifnonnull -> 10
    //   9: return
    //   10: invokestatic equipped : ()Larc/struct/IntSeq;
    //   13: astore_2
    //   14: new arc/struct/IntSeq
    //   17: astore_3
    //   18: aload_3
    //   19: invokespecial <init> : ()V
    //   22: iconst_0
    //   23: istore #4
    //   25: aload_2
    //   26: getfield size : I
    //   29: istore #5
    //   31: iload #4
    //   33: iload #5
    //   35: if_icmpge -> 134
    //   38: aload_2
    //   39: iload #4
    //   41: invokevirtual get : (I)I
    //   44: istore #5
    //   46: iload #5
    //   48: invokestatic mineById : (I)Llunarians/hub/Skins$Entry;
    //   51: astore #6
    //   53: aload #6
    //   55: ifnull -> 125
    //   58: aload #6
    //   60: getfield unit : Ljava/lang/String;
    //   63: astore #7
    //   65: aload_1
    //   66: getfield unit : Ljava/lang/String;
    //   69: astore #8
    //   71: aload #7
    //   73: aload #8
    //   75: invokevirtual equals : (Ljava/lang/Object;)Z
    //   78: istore #9
    //   80: iload #9
    //   82: ifeq -> 112
    //   85: aload #6
    //   87: getfield part : Ljava/lang/String;
    //   90: astore #7
    //   92: aload_1
    //   93: getfield part : Ljava/lang/String;
    //   96: astore #8
    //   98: aload #7
    //   100: aload #8
    //   102: invokevirtual equals : (Ljava/lang/Object;)Z
    //   105: istore #9
    //   107: iload #9
    //   109: ifne -> 125
    //   112: aload #6
    //   114: getfield id : I
    //   117: istore #5
    //   119: aload_3
    //   120: iload #5
    //   122: invokevirtual add : (I)V
    //   125: iload #4
    //   127: iconst_1
    //   128: iadd
    //   129: istore #4
    //   131: goto -> 25
    //   134: aload_3
    //   135: getfield size : I
    //   138: istore #10
    //   140: bipush #8
    //   142: istore #11
    //   144: iload #10
    //   146: iload #11
    //   148: if_icmplt -> 173
    //   151: getstatic mindustry/Vars.ui : Lmindustry/core/UI;
    //   154: astore #12
    //   156: ldc_w 'skin.max'
    //   159: invokestatic get : (Ljava/lang/String;)Ljava/lang/String;
    //   162: astore_1
    //   163: aload #12
    //   165: aload_1
    //   166: ldc_w 2.5
    //   169: invokevirtual showInfoToast : (Ljava/lang/String;F)V
    //   172: return
    //   173: aload_3
    //   174: iload_0
    //   175: invokevirtual add : (I)V
    //   178: aload_3
    //   179: invokestatic saveEquipped : (Larc/struct/IntSeq;)V
    //   182: return
  }
  
  private static IntSeq equipped() {
    IntSeq intSeq = new IntSeq();
    this();
    String str1 = Core.settings.getString("lunarians-hub-skins-eq", "");
    String str2 = ",";
    for (String str3 : str1.split(str2)) {
      String str4 = "\\d{1,9}";
      boolean bool = str3.matches(str4);
      if (bool) {
        int i = Strings.parseInt(str3, 0);
        intSeq.add(i);
      } 
    } 
    return intSeq;
  }
  
  public static IntSeq equippedApproved() {
    IntSeq intSeq1 = new IntSeq();
    this();
    IntSeq intSeq2 = equipped();
    int i = 0;
    while (true) {
      int j = intSeq2.size;
      if (i < j) {
        j = intSeq1.size;
        byte b = 8;
        if (j < b) {
          j = intSeq2.get(i);
          Skins$Entry skins$Entry = mineById(j);
          if (skins$Entry != null) {
            j = intSeq2.get(i);
            intSeq1.add(j);
          } 
          i++;
          continue;
        } 
      } 
      break;
    } 
    return intSeq1;
  }
  
  static void feedPeer(int paramInt, Jval.JsonArray paramJsonArray) {
    // Byte code:
    //   0: iload_0
    //   1: iflt -> 319
    //   4: aload_1
    //   5: ifnull -> 319
    //   8: getstatic lunarians/hub/Skins.peers : Larc/struct/IntMap;
    //   11: astore_2
    //   12: aload_2
    //   13: getfield size : I
    //   16: istore_3
    //   17: sipush #200
    //   20: istore #4
    //   22: iload_3
    //   23: iload #4
    //   25: if_icmple -> 38
    //   28: aload_2
    //   29: iload_0
    //   30: invokevirtual containsKey : (I)Z
    //   33: istore_3
    //   34: iload_3
    //   35: ifeq -> 319
    //   38: aload_2
    //   39: iload_0
    //   40: invokevirtual get : (I)Ljava/lang/Object;
    //   43: checkcast lunarians/hub/Skins$Peer
    //   46: astore #5
    //   48: aload #5
    //   50: ifnonnull -> 78
    //   53: new lunarians/hub/Skins$Peer
    //   56: astore #5
    //   58: iconst_0
    //   59: istore #4
    //   61: aconst_null
    //   62: astore #6
    //   64: aload #5
    //   66: aconst_null
    //   67: invokespecial <init> : (Llunarians/hub/Skins$1;)V
    //   70: aload_2
    //   71: iload_0
    //   72: aload #5
    //   74: invokevirtual put : (ILjava/lang/Object;)Ljava/lang/Object;
    //   77: pop
    //   78: new arc/struct/Seq
    //   81: astore_2
    //   82: aload_2
    //   83: invokespecial <init> : ()V
    //   86: new java/lang/StringBuilder
    //   89: astore #6
    //   91: aload #6
    //   93: invokespecial <init> : ()V
    //   96: aload_1
    //   97: invokevirtual iterator : ()Ljava/util/Iterator;
    //   100: astore_1
    //   101: iconst_0
    //   102: istore #7
    //   104: aconst_null
    //   105: astore #8
    //   107: aload_1
    //   108: invokeinterface hasNext : ()Z
    //   113: istore #9
    //   115: iload #9
    //   117: ifeq -> 252
    //   120: aload_1
    //   121: invokeinterface next : ()Ljava/lang/Object;
    //   126: checkcast arc/util/serialization/Jval
    //   129: astore #10
    //   131: iload #7
    //   133: iconst_1
    //   134: iadd
    //   135: istore #11
    //   137: bipush #8
    //   139: istore #12
    //   141: iload #7
    //   143: iload #12
    //   145: if_icmpge -> 252
    //   148: aload #10
    //   150: iload_0
    //   151: invokestatic parse : (Larc/util/serialization/Jval;I)Llunarians/hub/Skins$Entry;
    //   154: astore #8
    //   156: aload #8
    //   158: ifnull -> 245
    //   161: aload_2
    //   162: aload #8
    //   164: invokevirtual add : (Ljava/lang/Object;)Larc/struct/Seq;
    //   167: pop
    //   168: aload #8
    //   170: getfield id : I
    //   173: istore #9
    //   175: aload #6
    //   177: iload #9
    //   179: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   182: pop
    //   183: bipush #58
    //   185: istore #9
    //   187: aload #6
    //   189: iload #9
    //   191: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   194: pop
    //   195: aload #8
    //   197: getfield music : I
    //   200: istore #12
    //   202: aload #6
    //   204: iload #12
    //   206: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   209: pop
    //   210: aload #6
    //   212: iload #9
    //   214: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   217: pop
    //   218: aload #8
    //   220: getfield radius : I
    //   223: istore #7
    //   225: aload #6
    //   227: iload #7
    //   229: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   232: pop
    //   233: bipush #44
    //   235: istore #7
    //   237: aload #6
    //   239: iload #7
    //   241: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   244: pop
    //   245: iload #11
    //   247: istore #7
    //   249: goto -> 107
    //   252: getstatic arc/util/Time.time : F
    //   255: fstore #13
    //   257: aload #5
    //   259: fload #13
    //   261: putfield seen : F
    //   264: aload #6
    //   266: invokevirtual toString : ()Ljava/lang/String;
    //   269: astore #14
    //   271: aload #5
    //   273: getfield sig : Ljava/lang/String;
    //   276: astore_1
    //   277: aload #14
    //   279: aload_1
    //   280: invokevirtual equals : (Ljava/lang/Object;)Z
    //   283: istore_0
    //   284: iload_0
    //   285: ifne -> 319
    //   288: aload #6
    //   290: invokevirtual toString : ()Ljava/lang/String;
    //   293: astore #14
    //   295: aload #5
    //   297: aload #14
    //   299: putfield sig : Ljava/lang/String;
    //   302: aload #5
    //   304: aload_2
    //   305: putfield list : Larc/struct/Seq;
    //   308: iconst_1
    //   309: istore_0
    //   310: ldc_w 1.4E-45
    //   313: fstore #13
    //   315: iload_0
    //   316: putstatic lunarians/hub/Skins.dirty : Z
    //   319: return
  }
  
  static void fetch(int paramInt, Cons paramCons) {
    boolean bool;
    TextureRegion textureRegion = (TextureRegion)tex.get(paramInt);
    if (textureRegion != null) {
      paramCons.get(textureRegion);
      return;
    } 
    IntMap intMap = waiting;
    Seq seq = (Seq)intMap.get(paramInt);
    if (seq == null) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool) {
      seq = new Seq();
      this();
      intMap.put(paramInt, seq);
    } 
    seq.add(paramCons);
    if (bool) {
      StringBuilder stringBuilder = new StringBuilder();
      this("/api/skins/img?id=");
      stringBuilder.append(paramInt);
      String str1 = stringBuilder.toString();
      Skins$29 skins$29 = new Skins$29();
      this(paramInt);
      Skins$30 skins$30 = new Skins$30();
      this(paramInt);
      String str2 = "GET";
      bool = false;
      HubClient.call(str2, str1, null, skins$29, skins$30);
    } 
  }
  
  public static void init() {
    boolean bool = inited;
    if (!bool) {
      bool = Vars.headless;
      if (!bool) {
        bool = true;
        inited = bool;
        EventType.Trigger trigger = EventType.Trigger.drawOver;
        Skins$16 skins$16 = new Skins$16();
        this();
        Events.run(trigger, skins$16);
        trigger = EventType.Trigger.update;
        Skins$17 skins$17 = new Skins$17();
        this();
        Events.run(trigger, skins$17);
        Class<EventType.WorldLoadEvent> clazz = EventType.WorldLoadEvent.class;
        Skins$18 skins$18 = new Skins$18();
        this();
        Events.on(clazz, skins$18);
      } 
    } 
  }
  
  private static boolean isPng(byte[] paramArrayOfbyte) {
    int i = paramArrayOfbyte.length;
    short s = 8;
    if (i > s) {
      i = paramArrayOfbyte[0] & 0xFF;
      s = 137;
      if (i == s) {
        i = 1;
        s = paramArrayOfbyte[i];
        byte b = 80;
        if (s == b) {
          s = paramArrayOfbyte[2];
          b = 78;
          if (s == b) {
            byte b1 = paramArrayOfbyte[3];
            s = 71;
            if (b1 == s)
              return i; 
          } 
        } 
      } 
    } 
    return false;
  }
  
  private static void loadAudio(int paramInt) {
    IntSet intSet = audioLoading;
    boolean bool = intSet.contains(paramInt);
    if (!bool) {
      IntFloatMap intFloatMap = audioFailed;
      float f1 = intFloatMap.get(paramInt, -100000.0F) + 3600.0F;
      float f2 = Time.time;
      int i = f1 cmp f2;
      if (i < 0) {
        Fi fi = cached(paramInt);
        if (fi != null) {
          startMusic(paramInt, fi);
          return;
        } 
        intSet.add(paramInt);
        StringBuilder stringBuilder = new StringBuilder();
        this("/api/audio/get?id=");
        stringBuilder.append(paramInt);
        String str1 = stringBuilder.toString();
        Skins$5 skins$5 = new Skins$5();
        this(paramInt);
        Skins$6 skins$6 = new Skins$6();
        this(paramInt);
        String str2 = "GET";
        HubClient.call(str2, str1, null, skins$5, skins$6);
      } 
    } 
  }
  
  private static Skins$Entry mineById(int paramInt) {
    Iterator<Skins$Entry> iterator = mine.iterator();
    while (true) {
      boolean bool = iterator.hasNext();
      if (bool) {
        Skins$Entry skins$Entry = iterator.next();
        int i = skins$Entry.id;
        if (i == paramInt)
          return skins$Entry; 
        continue;
      } 
      return null;
    } 
  }
  
  private static void musicDialog(int paramInt1, int paramInt2, int paramInt3, Runnable paramRunnable) {
    BaseDialog baseDialog = new BaseDialog();
    String str1 = HubText.get("skin.music.title");
    this(str1);
    byte b;
    float f6;
    UiStyle.register(baseDialog);
    Scene scene1 = Core.scene;
    float f1 = Math.min(scene1.getWidth() - 40.0F, 520.0F);
    int[] arrayOfInt = { paramInt2 };
    Table table2 = baseDialog.cont;
    Label label1 = wrap(HubText.get("skin.music.help"));
    Cell cell1 = table2.add((Element)label1).width(f1);
    float f2 = 6.0F;
    cell1.padBottom(f2).row();
    Table table1 = new Table();
    this();
    Cell cell2 = baseDialog.cont.pane((Element)table1).width(f1);
    Scene scene2 = Core.scene;
    float f3 = scene2.getHeight();
    float f4 = 0.4F;
    f3 *= f4;
    cell2.maxHeight(f3).row();
    Label label2 = wrap("...");
    table1.add((Element)label2).row();
    label2 = new Label();
    if (paramInt3 > 0) {
      b = paramInt3;
    } else {
      b = 12;
      f3 = 1.7E-44F;
    } 
    String str2 = String.valueOf(b);
    this(str2);
    Label label3 = label2;
    Slider slider = new Slider();
    f4 = 30.0F;
    float f5 = 1.0F;
    this(f5, f4, f5, false);
    if (paramInt3 > 0) {
      f6 = paramInt3;
    } else {
      paramInt3 = 1094713344;
      f6 = 12.0F;
    } 
    slider.setValue(f6);
    Skins$36 skins$36 = new Skins$36();
    this(label3);
    slider.moved(skins$36);
    Table table3 = baseDialog.cont;
    Skins$37 skins$37 = new Skins$37();
    this(slider, f1, label3);
    table3.table(skins$37).padTop(f2).row();
    Skins$38 skins$38 = new Skins$38();
    this(table1, arrayOfInt, f1);
    Skins$39 skins$39 = new Skins$39();
    this(table1);
    HubClient.call("GET", "/api/audio/mine", null, skins$38, skins$39);
    Skins$40 skins$40 = new Skins$40();
    Runnable runnable = paramRunnable;
    this(paramInt1, arrayOfInt, slider, baseDialog, paramRunnable);
    Table table4 = baseDialog.cont;
    Skins$41 skins$41 = new Skins$41();
    this(arrayOfInt, skins$40);
    table4.table(skins$41).padTop(8.0F).row();
    baseDialog.addCloseButton();
    baseDialog.show();
  }
  
  private static void musicTick() {
    // Byte code:
    //   0: invokestatic consented : ()Z
    //   3: istore_0
    //   4: iload_0
    //   5: ifeq -> 869
    //   8: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   11: astore_1
    //   12: aload_1
    //   13: ifnull -> 869
    //   16: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   19: astore_1
    //   20: aload_1
    //   21: invokevirtual isGame : ()Z
    //   24: istore_0
    //   25: iload_0
    //   26: ifeq -> 869
    //   29: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   32: astore_1
    //   33: aload_1
    //   34: ifnull -> 869
    //   37: getstatic lunarians/hub/Skins.pools : Larc/struct/ObjectMap;
    //   40: astore_1
    //   41: aload_1
    //   42: getfield size : I
    //   45: istore_0
    //   46: iload_0
    //   47: ifeq -> 869
    //   50: new arc/struct/IntFloatMap
    //   53: astore_1
    //   54: aload_1
    //   55: invokespecial <init> : ()V
    //   58: getstatic arc/Core.camera : Larc/graphics/Camera;
    //   61: getfield position : Larc/math/geom/Vec2;
    //   64: astore_2
    //   65: aload_2
    //   66: getfield x : F
    //   69: fstore_3
    //   70: getstatic arc/Core.camera : Larc/graphics/Camera;
    //   73: getfield position : Larc/math/geom/Vec2;
    //   76: astore #4
    //   78: aload #4
    //   80: getfield y : F
    //   83: fstore #5
    //   85: getstatic mindustry/gen/Groups.unit : Lmindustry/entities/EntityGroup;
    //   88: invokevirtual iterator : ()Ljava/util/Iterator;
    //   91: astore #6
    //   93: aload #6
    //   95: invokeinterface hasNext : ()Z
    //   100: istore #7
    //   102: iload #7
    //   104: ifeq -> 372
    //   107: aload #6
    //   109: invokeinterface next : ()Ljava/lang/Object;
    //   114: checkcast mindustry/gen/Unit
    //   117: astore #8
    //   119: aload #8
    //   121: ifnull -> 93
    //   124: aload #8
    //   126: invokevirtual isAdded : ()Z
    //   129: istore #9
    //   131: iload #9
    //   133: ifeq -> 93
    //   136: aload #8
    //   138: getfield dead : Z
    //   141: istore #9
    //   143: iload #9
    //   145: ifne -> 93
    //   148: aload #8
    //   150: getfield type : Lmindustry/type/UnitType;
    //   153: astore #10
    //   155: aload #10
    //   157: ifnull -> 93
    //   160: getstatic lunarians/hub/Skins.pools : Larc/struct/ObjectMap;
    //   163: astore #10
    //   165: aload #8
    //   167: getfield type : Lmindustry/type/UnitType;
    //   170: getfield name : Ljava/lang/String;
    //   173: astore #11
    //   175: aload #10
    //   177: aload #11
    //   179: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   182: checkcast lunarians/hub/Skins$UnitPool
    //   185: astore #10
    //   187: aload #10
    //   189: ifnull -> 93
    //   192: aload #8
    //   194: aload #10
    //   196: invokestatic skinsFor : (Lmindustry/gen/Unit;Llunarians/hub/Skins$UnitPool;)Larc/struct/ObjectMap;
    //   199: astore #10
    //   201: aload #10
    //   203: ifnull -> 93
    //   206: aload #10
    //   208: invokevirtual iterator : ()Larc/struct/ObjectMap$Entries;
    //   211: astore #10
    //   213: aload #10
    //   215: invokeinterface hasNext : ()Z
    //   220: istore #12
    //   222: iload #12
    //   224: ifeq -> 93
    //   227: aload #10
    //   229: invokeinterface next : ()Ljava/lang/Object;
    //   234: checkcast arc/struct/ObjectMap$Entry
    //   237: getfield value : Ljava/lang/Object;
    //   240: checkcast lunarians/hub/Skins$Entry
    //   243: astore #11
    //   245: aload #11
    //   247: getfield music : I
    //   250: istore #13
    //   252: iload #13
    //   254: ifle -> 213
    //   257: aload #11
    //   259: getfield radius : I
    //   262: istore #13
    //   264: iload #13
    //   266: ifle -> 213
    //   269: aload #11
    //   271: getfield radius : I
    //   274: istore #13
    //   276: iload #13
    //   278: i2f
    //   279: ldc 8.0
    //   281: fmul
    //   282: fstore #14
    //   284: aload #8
    //   286: fload_3
    //   287: fload #5
    //   289: invokevirtual dst : (FF)F
    //   292: fstore #15
    //   294: fload #15
    //   296: fload #14
    //   298: fcmpl
    //   299: istore #16
    //   301: iload #16
    //   303: ifge -> 213
    //   306: ldc_w 1065353216
    //   309: istore #16
    //   311: fload #15
    //   313: fload #14
    //   315: fdiv
    //   316: fstore #15
    //   318: ldc_w 1.0
    //   321: fload #15
    //   323: fsub
    //   324: fstore #17
    //   326: aload #11
    //   328: getfield music : I
    //   331: istore #13
    //   333: aload_1
    //   334: iload #13
    //   336: fconst_0
    //   337: invokevirtual get : (IF)F
    //   340: fstore #14
    //   342: fload #17
    //   344: fload #14
    //   346: fcmpl
    //   347: istore #13
    //   349: iload #13
    //   351: ifle -> 213
    //   354: aload #11
    //   356: getfield music : I
    //   359: istore #12
    //   361: aload_1
    //   362: iload #12
    //   364: fload #17
    //   366: invokevirtual put : (IF)V
    //   369: goto -> 213
    //   372: new arc/struct/IntSeq
    //   375: astore_2
    //   376: aload_2
    //   377: invokespecial <init> : ()V
    //   380: aload_1
    //   381: invokevirtual iterator : ()Ljava/util/Iterator;
    //   384: astore #4
    //   386: aload #4
    //   388: invokeinterface hasNext : ()Z
    //   393: istore #18
    //   395: iload #18
    //   397: ifeq -> 428
    //   400: aload #4
    //   402: invokeinterface next : ()Ljava/lang/Object;
    //   407: checkcast arc/struct/IntFloatMap$Entry
    //   410: astore #6
    //   412: aload #6
    //   414: getfield key : I
    //   417: istore #18
    //   419: aload_2
    //   420: iload #18
    //   422: invokevirtual add : (I)V
    //   425: goto -> 386
    //   428: aload_2
    //   429: invokevirtual sort : ()V
    //   432: iconst_0
    //   433: istore #19
    //   435: fconst_0
    //   436: fstore #5
    //   438: aconst_null
    //   439: astore #4
    //   441: iconst_0
    //   442: istore #18
    //   444: fconst_0
    //   445: fstore #20
    //   447: aconst_null
    //   448: astore #6
    //   450: aload_2
    //   451: getfield size : I
    //   454: istore #7
    //   456: iload #18
    //   458: iload #7
    //   460: if_icmpge -> 580
    //   463: iload #18
    //   465: iconst_1
    //   466: iadd
    //   467: istore #7
    //   469: iload #7
    //   471: istore #9
    //   473: aload_2
    //   474: getfield size : I
    //   477: istore #12
    //   479: iload #9
    //   481: iload #12
    //   483: if_icmpge -> 573
    //   486: aload_2
    //   487: iload #9
    //   489: invokevirtual get : (I)I
    //   492: istore #12
    //   494: aload_1
    //   495: iload #12
    //   497: fconst_0
    //   498: invokevirtual get : (IF)F
    //   501: fstore #21
    //   503: aload_2
    //   504: iload #18
    //   506: invokevirtual get : (I)I
    //   509: istore #13
    //   511: aload_1
    //   512: iload #13
    //   514: fconst_0
    //   515: invokevirtual get : (IF)F
    //   518: fstore #14
    //   520: fload #21
    //   522: fload #14
    //   524: fcmpl
    //   525: istore #12
    //   527: iload #12
    //   529: ifle -> 564
    //   532: aload_2
    //   533: iload #18
    //   535: invokevirtual get : (I)I
    //   538: istore #12
    //   540: aload_2
    //   541: iload #9
    //   543: invokevirtual get : (I)I
    //   546: istore #13
    //   548: aload_2
    //   549: iload #18
    //   551: iload #13
    //   553: invokevirtual set : (II)V
    //   556: aload_2
    //   557: iload #9
    //   559: iload #12
    //   561: invokevirtual set : (II)V
    //   564: iload #9
    //   566: iconst_1
    //   567: iadd
    //   568: istore #9
    //   570: goto -> 473
    //   573: iload #7
    //   575: istore #18
    //   577: goto -> 450
    //   580: aload_2
    //   581: getfield size : I
    //   584: iconst_m1
    //   585: iadd
    //   586: istore #18
    //   588: iconst_2
    //   589: istore #7
    //   591: ldc_w 2.8E-45
    //   594: fstore #22
    //   596: iload #18
    //   598: iload #7
    //   600: if_icmplt -> 619
    //   603: aload_2
    //   604: iload #18
    //   606: invokevirtual removeIndex : (I)I
    //   609: pop
    //   610: iload #18
    //   612: iconst_m1
    //   613: iadd
    //   614: istore #18
    //   616: goto -> 588
    //   619: getstatic arc/Core.settings : Larc/Settings;
    //   622: astore #6
    //   624: ldc_w 'musicvol'
    //   627: astore #8
    //   629: bipush #100
    //   631: istore #9
    //   633: aload #6
    //   635: aload #8
    //   637: iload #9
    //   639: invokevirtual getInt : (Ljava/lang/String;I)I
    //   642: istore #18
    //   644: iload #18
    //   646: i2f
    //   647: fstore #20
    //   649: ldc_w 1120403456
    //   652: istore #7
    //   654: ldc_w 100.0
    //   657: fstore #22
    //   659: fload #20
    //   661: fload #22
    //   663: fdiv
    //   664: fstore #20
    //   666: aload_2
    //   667: getfield size : I
    //   670: istore #7
    //   672: iload #19
    //   674: iload #7
    //   676: if_icmpge -> 765
    //   679: aload_2
    //   680: iload #19
    //   682: invokevirtual get : (I)I
    //   685: istore #7
    //   687: getstatic lunarians/hub/Skins.playing : Larc/struct/IntMap;
    //   690: iload #7
    //   692: invokevirtual get : (I)Ljava/lang/Object;
    //   695: checkcast arc/audio/Music
    //   698: astore #10
    //   700: aload #10
    //   702: ifnonnull -> 713
    //   705: iload #7
    //   707: invokestatic loadAudio : (I)V
    //   710: goto -> 756
    //   713: aload_1
    //   714: iload #7
    //   716: fconst_0
    //   717: invokevirtual get : (IF)F
    //   720: fload #20
    //   722: fmul
    //   723: fstore #22
    //   725: fload #22
    //   727: invokestatic clamp : (F)F
    //   730: fstore #22
    //   732: aload #10
    //   734: fload #22
    //   736: invokevirtual setVolume : (F)V
    //   739: aload #10
    //   741: invokevirtual isPlaying : ()Z
    //   744: istore #7
    //   746: iload #7
    //   748: ifne -> 756
    //   751: aload #10
    //   753: invokevirtual play : ()V
    //   756: iload #19
    //   758: iconst_1
    //   759: iadd
    //   760: istore #19
    //   762: goto -> 666
    //   765: getstatic lunarians/hub/Skins.playing : Larc/struct/IntMap;
    //   768: invokevirtual iterator : ()Ljava/util/Iterator;
    //   771: astore_1
    //   772: aload_1
    //   773: invokeinterface hasNext : ()Z
    //   778: istore #19
    //   780: iload #19
    //   782: ifeq -> 868
    //   785: aload_1
    //   786: invokeinterface next : ()Ljava/lang/Object;
    //   791: checkcast arc/struct/IntMap$Entry
    //   794: astore #4
    //   796: aload #4
    //   798: getfield key : I
    //   801: istore #18
    //   803: aload_2
    //   804: iload #18
    //   806: invokevirtual contains : (I)Z
    //   809: istore #18
    //   811: iload #18
    //   813: ifne -> 772
    //   816: aload #4
    //   818: getfield value : Ljava/lang/Object;
    //   821: astore #6
    //   823: aload #6
    //   825: checkcast arc/audio/Music
    //   828: astore #6
    //   830: aload #6
    //   832: invokevirtual isPlaying : ()Z
    //   835: istore #18
    //   837: iload #18
    //   839: ifeq -> 772
    //   842: aload #4
    //   844: getfield value : Ljava/lang/Object;
    //   847: astore #4
    //   849: aload #4
    //   851: checkcast arc/audio/Music
    //   854: astore #4
    //   856: aload #4
    //   858: invokevirtual stop : ()V
    //   861: goto -> 772
    //   864: pop
    //   865: goto -> 772
    //   868: return
    //   869: invokestatic stopAllMusic : ()V
    //   872: return
    //   873: pop
    //   874: goto -> 756
    // Exception table:
    //   from	to	target	type
    //   716	720	873	finally
    //   725	730	873	finally
    //   734	739	873	finally
    //   739	744	873	finally
    //   751	756	873	finally
    //   816	821	864	finally
    //   823	828	864	finally
    //   830	835	864	finally
    //   842	847	864	finally
    //   849	854	864	finally
    //   856	861	864	finally
  }
  
  static boolean okPart(String paramString) {
    if (paramString != null) {
      String str = "body|legs|joint|legbase|base|weapon[0-9]{1,2}";
      boolean bool = paramString.matches(str);
      if (bool)
        return true; 
    } 
    return false;
  }
  
  static boolean okUnit(String paramString) {
    if (paramString != null) {
      String str = "[a-z0-9-]{1,40}";
      boolean bool = paramString.matches(str);
      if (bool)
        return true; 
    } 
    return false;
  }
  
  static TextureRegion originalRegion(UnitType paramUnitType, String paramString) {
    Iterator<Weapon> iterator;
    boolean bool;
    String str;
    if (paramUnitType == null)
      return null; 
    paramString.hashCode();
    int i = paramString.hashCode();
    int j = -1;
    switch (i) {
      default:
        i = -1;
        break;
      case 101304458:
        str = "joint";
        bool = paramString.equals(str);
        if (bool) {
          byte b = 4;
          break;
        } 
      case 55833055:
        str = "legbase";
        bool = paramString.equals(str);
        if (bool) {
          byte b = 3;
          break;
        } 
      case 3317797:
        str = "legs";
        bool = paramString.equals(str);
        if (bool) {
          byte b = 2;
          break;
        } 
      case 3029410:
        str = "body";
        bool = paramString.equals(str);
        if (bool) {
          bool = true;
          break;
        } 
      case 3016401:
        str = "base";
        bool = paramString.equals(str);
        if (bool) {
          bool = false;
          str = null;
          break;
        } 
    } 
    switch (bool) {
      default:
        str = "weapon";
        bool = paramString.startsWith(str);
        if (bool) {
          Seq seq = weaponNames(paramUnitType);
          byte b = 6;
          paramString = paramString.substring(b);
          int k = Strings.parseInt(paramString, j);
          if (k >= 0) {
            j = seq.size;
            if (k < j) {
              iterator = paramUnitType.weapons.iterator();
              while (true) {
                boolean bool1 = iterator.hasNext();
                if (bool1) {
                  Weapon weapon = iterator.next();
                  String str1 = weapon.name;
                  Object object = seq.get(k);
                  boolean bool2 = str1.equals(object);
                  if (bool2)
                    return weapon.region; 
                  continue;
                } 
                break;
              } 
            } 
          } 
        } 
        return null;
      case true:
        return ((UnitType)iterator).jointRegion;
      case true:
        return ((UnitType)iterator).legBaseRegion;
      case true:
        return ((UnitType)iterator).legRegion;
      case true:
        return ((UnitType)iterator).region;
      case false:
        break;
    } 
    return ((UnitType)iterator).baseRegion;
  }
  
  private static Skins$Entry parse(Jval paramJval, int paramInt) {
    try {
      Skins$Entry skins$Entry = new Skins$Entry();
      this();
      String str1 = "i";
      String str2 = "id";
      int i = paramJval.getInt(str2, 0);
      int j = paramJval.getInt(str1, i);
      skins$Entry.id = j;
      str1 = "u";
      str2 = "unit";
      String str3 = "";
      str2 = paramJval.getString(str2, str3);
      str1 = paramJval.getString(str1, str2);
      skins$Entry.unit = str1;
      str1 = "p";
      str2 = "part";
      str3 = "body";
      str2 = paramJval.getString(str2, str3);
      str1 = paramJval.getString(str1, str2);
      skins$Entry.part = str1;
      str1 = "m";
      str2 = "music";
      i = paramJval.getInt(str2, 0);
      j = paramJval.getInt(str1, i);
      skins$Entry.music = j;
      str1 = "r";
      str2 = "music_radius";
      i = paramJval.getInt(str2, 0);
      int k = paramJval.getInt(str1, i);
      j = 30;
      k = Math.min(j, k);
      k = Math.max(0, k);
      skins$Entry.radius = k;
      skins$Entry.owner = paramInt;
      k = skins$Entry.id;
      if (k > 0) {
        String str = skins$Entry.unit;
        boolean bool = okUnit(str);
        if (bool) {
          str = skins$Entry.part;
          bool = okPart(str);
          if (bool) {
            int m = skins$Entry.music;
            if (m < 0)
              skins$Entry.music = 0; 
            return skins$Entry;
          } 
        } 
      } 
    } finally {}
    return null;
  }
  
  static String partLabel(String paramString) {
    String str = "weapon";
    boolean bool = paramString.startsWith(str);
    if (bool) {
      StringBuilder stringBuilder1 = new StringBuilder();
      this();
      String str1 = HubText.get("skin.part.weapon");
      stringBuilder1.append(str1);
      stringBuilder1.append(" ");
      int i = Strings.parseInt(paramString.substring(6), 0) + 1;
      stringBuilder1.append(i);
      return stringBuilder1.toString();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    this("skin.part.");
    stringBuilder.append(paramString);
    return HubText.get(stringBuilder.toString());
  }
  
  static Seq partsOf(UnitType paramUnitType) {
    Seq seq = new Seq();
    this();
    seq.add("body");
    String str1 = "legbase";
    String[] arrayOfString = { "legs", str1, "joint", "base" };
    int i = 0;
    int j = 0;
    String str2 = null;
    while (true) {
      byte b = 4;
      if (j < b) {
        str1 = arrayOfString[j];
        TextureRegion textureRegion = originalRegion(paramUnitType, str1);
        if (textureRegion != null) {
          boolean bool = textureRegion.found();
          if (bool)
            seq.add(str1); 
        } 
        j++;
        continue;
      } 
      Seq seq1 = weaponNames(paramUnitType);
      while (true) {
        int k = seq1.size;
        if (i < k) {
          k = 30;
          if (i < k) {
            StringBuilder stringBuilder = new StringBuilder();
            str2 = "weapon";
            this(str2);
            stringBuilder.append(i);
            String str = stringBuilder.toString();
            seq.add(str);
            i++;
            continue;
          } 
        } 
        break;
      } 
      return seq;
    } 
  }
  
  private static int pick(int paramInt1, int paramInt2) {
    int i = 1;
    if (paramInt2 <= i)
      return 0; 
    paramInt1 *= -1640531535;
    i = paramInt1 >>> 15;
    paramInt1 = (paramInt1 ^ i) * -2048144789;
    i = paramInt1 >>> 13;
    return Skins$0.m(paramInt1 ^ i, paramInt2);
  }
  
  private static void pickPart(UnitType paramUnitType, Cons paramCons) {
    BaseDialog baseDialog = new BaseDialog();
    String str = HubText.get("skin.pickpart");
    this(str);
    UiStyle.register(baseDialog);
    Scene scene = Core.scene;
    float f1 = scene.getWidth() - 40.0F;
    float f2 = 420.0F;
    f1 = Math.min(f1, f2);
    Table table = new Table();
    this();
    Iterator<String> iterator = partsOf(paramUnitType).iterator();
    while (true) {
      boolean bool = iterator.hasNext();
      if (bool) {
        String str1 = iterator.next();
        TextureRegion textureRegion = originalRegion(paramUnitType, str1);
        if (textureRegion != null) {
          boolean bool1 = textureRegion.found();
          if (bool1) {
            StringBuilder stringBuilder = new StringBuilder();
            this("  (");
            int i = textureRegion.width;
            stringBuilder.append(i);
            String str4 = "x";
            stringBuilder.append(str4);
            int j = textureRegion.height;
            stringBuilder.append(j);
            stringBuilder.append(")");
            String str3 = stringBuilder.toString();
            continue;
          } 
        } 
        String str2 = "";
        continue;
      } 
      Cell cell = baseDialog.cont.pane((Element)table).width(f1);
      float f = Core.scene.getHeight() * 0.65F;
      cell.maxHeight(f).row();
      baseDialog.addCloseButton();
      baseDialog.show();
      return;
      stringBuilder = new StringBuilder();
      this();
      str2 = partLabel((String)SYNTHETIC_LOCAL_VARIABLE_9);
      stringBuilder.append(str2);
      stringBuilder.append((String)SYNTHETIC_LOCAL_VARIABLE_10);
      str1 = stringBuilder.toString();
      skins$43 = new Skins$43();
      this(paramCons, (String)SYNTHETIC_LOCAL_VARIABLE_9, baseDialog);
      cell = table.button(str1, skins$43).growX().height(46.0F);
      i = 1073741824;
      f = 2.0F;
      cell = cell.pad(f);
      cell.row();
    } 
  }
  
  static void pickUnit(Cons paramCons) {
    BaseDialog baseDialog = new BaseDialog();
    String str = HubText.get("skin.pickunit");
    this(str);
    UiStyle.register(baseDialog);
    Table table = new Table();
    this();
    Scene scene = Core.scene;
    float f1 = Math.min(scene.getWidth() - 40.0F, 520.0F);
    float f2 = f1 / 76.0F;
    int i = (int)f2;
    byte b = 3;
    i = Math.max(b, i);
    Iterator<UnitType> iterator = Vars.content.units().iterator();
    int j = 0;
    while (true) {
      boolean bool = iterator.hasNext();
      if (bool) {
        UnitType unitType = iterator.next();
        boolean bool1 = unitType.isVanilla();
        if (bool1) {
          bool1 = unitType.internal;
          if (!bool1) {
            TextureRegion textureRegion = unitType.uiIcon;
            if (textureRegion != null) {
              String str1 = unitType.name;
              bool1 = okUnit(str1);
              if (bool1) {
                Table table1 = new Table();
                this();
                TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable();
                TextureRegion textureRegion1 = unitType.uiIcon;
                this(textureRegion1);
                ImageButton.ImageButtonStyle imageButtonStyle = Styles.cleari;
                Skins$25 skins$25 = new Skins$25();
                this(paramCons, unitType, baseDialog);
                float f3 = 44.0F;
                Cell cell2 = table1.button((Drawable)textureRegionDrawable, imageButtonStyle, f3, skins$25);
                float f4 = 56.0F;
                cell2.size(f4);
                table1.row();
                String str2 = unitType.name;
                Cell cell1 = table1.add(str2);
                float f5 = 0.55F;
                cell1.fontScale(f5);
                cell1 = table.add((Element)table1);
                int m = 1073741824;
                float f6 = 2.0F;
                cell1.pad(f6);
                int k = ++j % i;
                if (k == 0)
                  table.row(); 
              } 
            } 
          } 
        } 
        continue;
      } 
      Cell cell = baseDialog.cont.pane((Element)table).width(f1);
      float f = Core.scene.getHeight() * 0.65F;
      cell.maxHeight(f).row();
      baseDialog.addCloseButton();
      baseDialog.show();
      return;
    } 
  }
  
  private static void rebuildPools() {
    pools.clear();
    IntSeq intSeq = equipped();
    Iterator<Skins$Entry> iterator = mine.iterator();
    while (true) {
      boolean bool1 = iterator.hasNext();
      if (bool1) {
        Skins$Entry skins$Entry = iterator.next();
        int i = skins$Entry.id;
        boolean bool = intSeq.contains(i);
        if (bool)
          add(skins$Entry); 
        continue;
      } 
      boolean bool2 = showOthers();
      if (bool2) {
        Seq seq = new Seq();
        this();
        iterator = peers.iterator();
        label38: while (true) {
          bool1 = iterator.hasNext();
          if (bool1) {
            Integer integer;
            IntMap.Entry entry = (IntMap.Entry)iterator.next();
            float f1 = Time.time;
            Skins$Peer skins$Peer = (Skins$Peer)entry.value;
            float f2 = skins$Peer.seen;
            f1 -= f2;
            f2 = 6000.0F;
            int i = f1 cmp f2;
            if (i > 0) {
              int j = entry.key;
              integer = Integer.valueOf(j);
              seq.add(integer);
              continue;
            } 
            Iterator<Skins$Entry> iterator2 = ((Skins$Peer)((IntMap.Entry)integer).value).list.iterator();
            while (true) {
              boolean bool = iterator2.hasNext();
              if (bool) {
                Skins$Entry skins$Entry = iterator2.next();
                add(skins$Entry);
                continue;
              } 
              continue label38;
            } 
          } 
          Iterator<Integer> iterator1 = seq.iterator();
          while (true) {
            boolean bool = iterator1.hasNext();
            if (bool) {
              Integer integer = iterator1.next();
              int i = integer.intValue();
              IntMap intMap = peers;
              intMap.remove(i);
              continue;
            } 
            break;
          } 
          break;
        } 
      } 
      ObjectMap.Entries entries = pools.iterator();
      while (true) {
        boolean bool = entries.hasNext();
        if (bool) {
          Seq seq = ((Skins$UnitPool)((ObjectMap.Entry)entries.next()).value).owners;
          seq.sort();
          continue;
        } 
        dirty = false;
        return;
      } 
      break;
    } 
  }
  
  static void refreshMine() {
    boolean bool = HubClient.consented();
    if (bool) {
      String str = HubClient.myId();
      bool = str.isEmpty();
      if (!bool) {
        Skins$27 skins$27 = new Skins$27();
        this();
        Skins$28 skins$28 = new Skins$28();
        this();
        String str1 = "GET";
        String str2 = "/api/skins/mine";
        HubClient.call(str1, str2, null, skins$27, skins$28);
      } 
    } 
  }
  
  private static TextureRegion region(int paramInt) {
    TextureRegion textureRegion = (TextureRegion)tex.get(paramInt);
    if (textureRegion != null)
      return textureRegion; 
    IntMap intMap = waiting;
    boolean bool = intMap.containsKey(paramInt);
    if (!bool) {
      IntFloatMap intFloatMap = failedAt;
      float f1 = intFloatMap.get(paramInt, -100000.0F) + 3600.0F;
      float f2 = Time.time;
      int i = f1 cmp f2;
      if (i < 0) {
        Skins$42 skins$42 = new Skins$42();
        this();
        fetch(paramInt, skins$42);
      } 
    } 
    return null;
  }
  
  public static void requestDialog(Runnable paramRunnable) {
    BaseDialog baseDialog = new BaseDialog();
    String str1 = HubText.get("skin.new");
    this(str1);
    UiStyle.register(baseDialog);
    float f1 = Math.min(Core.scene.getWidth() - 40.0F, 520.0F);
    str1 = "";
    String[] arrayOfString1 = { str1 };
    String[] arrayOfString2 = { "body" };
    byte[][] arrayOfByte = new byte[1][];
    arrayOfByte[0] = null;
    String[] arrayOfString3 = { str1 };
    TextField textField = new TextField();
    this();
    String str2 = HubText.get("skin.name");
    textField.setMessageText(str2);
    textField.setMaxLength(32);
    Label label2 = wrap(str1);
    TextButton textButton1 = new TextButton();
    str1 = HubText.get("skin.pickunit");
    TextButton.TextButtonStyle textButtonStyle = Styles.defaultt;
    this(str1, textButtonStyle);
    TextButton textButton2 = new TextButton();
    str1 = HubText.get("skin.pickpart");
    textButtonStyle = Styles.defaultt;
    this(str1, textButtonStyle);
    TextButton textButton3 = new TextButton();
    str1 = HubText.get("skin.pickfile");
    textButtonStyle = Styles.defaultt;
    this(str1, textButtonStyle);
    Skins$44 skins$44 = new Skins$44();
    String[] arrayOfString4 = arrayOfString1;
    this(textButton1, arrayOfString1, textButton2, arrayOfString2, textButton3, arrayOfByte, arrayOfString3, label2);
    Label label1 = label2;
    Skins$45 skins$45 = new Skins$45();
    this(arrayOfString1, arrayOfString2, skins$44);
    textButton1.clicked(skins$45);
    Skins$46 skins$46 = new Skins$46();
    this(arrayOfString1, arrayOfString2, skins$44);
    textButton2.clicked(skins$46);
    Skins$47 skins$47 = new Skins$47();
    this(arrayOfString3, arrayOfByte, skins$44);
    textButton3.clicked(skins$47);
    Table table1 = baseDialog.cont;
    Label label3 = wrap(HubText.get("skin.help"));
    Cell cell2 = table1.add((Element)label3).width(f1);
    float f2 = 6.0F;
    cell2.padBottom(f2).row();
    baseDialog.cont.add((Element)textField).width(f1).height(46.0F).row();
    cell2 = baseDialog.cont.add((Element)textButton1).width(f1);
    float f3 = 48.0F;
    cell2.height(f3).padTop(f2).row();
    baseDialog.cont.add((Element)textButton2).width(f1).height(f3).padTop(f2).row();
    baseDialog.cont.add((Element)textButton3).width(f1).height(f3).padTop(f2).row();
    baseDialog.cont.add((Element)label2).width(f1).padTop(f2).row();
    Table table2 = baseDialog.cont;
    String str3 = HubText.get("skin.send");
    TextureRegionDrawable textureRegionDrawable = Icon.upload;
    Skins$48 skins$48 = new Skins$48();
    Runnable runnable = paramRunnable;
    this(textField, arrayOfString1, arrayOfByte, arrayOfString2, baseDialog, paramRunnable);
    Cell cell1 = table2.button(str3, (Drawable)textureRegionDrawable, skins$48);
    float f4 = Math.min(f1, 320.0F);
    cell1.size(f4, 52.0F).padTop(10.0F).row();
    baseDialog.addCloseButton();
    baseDialog.shown(skins$44);
    baseDialog.show();
  }
  
  private static void saveEquipped(IntSeq paramIntSeq) {
    StringBuilder stringBuilder = new StringBuilder();
    this();
    int i = 0;
    while (true) {
      int j = paramIntSeq.size;
      if (i < j) {
        if (i > 0) {
          j = 44;
          stringBuilder.append(j);
        } 
        j = paramIntSeq.get(i);
        stringBuilder.append(j);
        i++;
        continue;
      } 
      Settings settings = Core.settings;
      String str = stringBuilder.toString();
      settings.put("lunarians-hub-skins-eq", str);
      dirty = true;
      Room.sendSoon();
      return;
    } 
  }
  
  public static boolean showOthers() {
    return Core.settings.getBool("lunarians-hub-skins-show", true);
  }
  
  private static TextureRegion skin(ObjectMap paramObjectMap, String paramString) {
    if (paramObjectMap == null)
      return null; 
    Skins$Entry skins$Entry = (Skins$Entry)paramObjectMap.get(paramString);
    return (skins$Entry == null) ? null : region(skins$Entry.id);
  }
  
  private static ObjectMap skinsFor(Unit paramUnit, Skins$UnitPool paramSkins$UnitPool) {
    if (paramSkins$UnitPool != null) {
      Seq seq = paramSkins$UnitPool.owners;
      int i = seq.size;
      if (i != 0) {
        seq = paramSkins$UnitPool.owners;
        int j = paramUnit.id;
        int k = paramSkins$UnitPool.owners.size;
        j = pick(j, k);
        j = ((Integer)seq.get(j)).intValue();
        return (ObjectMap)paramSkins$UnitPool.byOwner.get(j);
      } 
    } 
    return null;
  }
  
  private static void startMusic(int paramInt, Fi paramFi) {
    boolean bool = true;
    try {
      Music music;
      return;
    } finally {
      paramFi = null;
      IntFloatMap intFloatMap = audioFailed;
      float f = Time.time;
      intFloatMap.put(paramInt, f);
      Integer integer = Integer.valueOf(paramInt);
      String str = paramFi.toString();
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = integer;
      arrayOfObject[bool] = str;
      Log.warn("[Lunarians Skins] musica @: @", arrayOfObject);
    } 
  }
  
  private static void stopAllMusic() {
    try {
      IntMap intMap = playing;
      Iterator<Object> iterator = intMap.iterator();
      while (true) {
        boolean bool = iterator.hasNext();
        if (bool) {
          IntMap.Entry entry = (IntMap.Entry)iterator.next();
          entry = entry;
          try {
            Object object = entry.value;
            object = object;
            boolean bool1 = object.isPlaying();
            if (bool1) {
              Object object1 = entry.value;
              object1 = object1;
              object1.stop();
            } 
          } finally {}
          continue;
        } 
        return;
      } 
    } finally {}
  }
  
  private static void tick() {
    // Byte code:
    //   0: invokestatic consented : ()Z
    //   3: istore_0
    //   4: iload_0
    //   5: ifne -> 29
    //   8: getstatic lunarians/hub/Skins.pools : Larc/struct/ObjectMap;
    //   11: astore_1
    //   12: aload_1
    //   13: getfield size : I
    //   16: istore_2
    //   17: iload_2
    //   18: ifle -> 186
    //   21: aload_1
    //   22: invokevirtual clear : ()V
    //   25: invokestatic stopAllMusic : ()V
    //   28: return
    //   29: getstatic arc/util/Time.time : F
    //   32: fstore_3
    //   33: getstatic lunarians/hub/Skins.nextMine : F
    //   36: fstore #4
    //   38: fload_3
    //   39: fload #4
    //   41: fcmpl
    //   42: istore_0
    //   43: iload_0
    //   44: iflt -> 92
    //   47: getstatic arc/util/Time.time : F
    //   50: fstore_3
    //   51: getstatic lunarians/hub/Skins.mineLoaded : Z
    //   54: istore_2
    //   55: iload_2
    //   56: ifeq -> 71
    //   59: ldc_w 1172373504
    //   62: istore_2
    //   63: ldc_w 7200.0
    //   66: fstore #4
    //   68: goto -> 80
    //   71: ldc_w 1142292480
    //   74: istore_2
    //   75: ldc_w 600.0
    //   78: fstore #4
    //   80: fload_3
    //   81: fload #4
    //   83: fadd
    //   84: fstore_3
    //   85: fload_3
    //   86: putstatic lunarians/hub/Skins.nextMine : F
    //   89: invokestatic refreshMine : ()V
    //   92: getstatic lunarians/hub/Skins.dirty : Z
    //   95: istore_0
    //   96: iload_0
    //   97: ifne -> 118
    //   100: getstatic arc/util/Time.time : F
    //   103: fstore_3
    //   104: getstatic lunarians/hub/Skins.nextPool : F
    //   107: fstore #4
    //   109: fload_3
    //   110: fload #4
    //   112: fcmpl
    //   113: istore_0
    //   114: iload_0
    //   115: iflt -> 143
    //   118: getstatic arc/util/Time.time : F
    //   121: fstore_3
    //   122: ldc_w 1133903872
    //   125: istore_2
    //   126: ldc_w 300.0
    //   129: fstore #4
    //   131: fload_3
    //   132: fload #4
    //   134: fadd
    //   135: fstore_3
    //   136: fload_3
    //   137: putstatic lunarians/hub/Skins.nextPool : F
    //   140: invokestatic rebuildPools : ()V
    //   143: getstatic arc/util/Time.time : F
    //   146: fstore_3
    //   147: getstatic lunarians/hub/Skins.nextMusic : F
    //   150: fstore #4
    //   152: fload_3
    //   153: fload #4
    //   155: fcmpl
    //   156: istore_0
    //   157: iload_0
    //   158: iflt -> 186
    //   161: getstatic arc/util/Time.time : F
    //   164: fstore_3
    //   165: ldc_w 1101004800
    //   168: istore_2
    //   169: ldc_w 20.0
    //   172: fstore #4
    //   174: fload_3
    //   175: fload #4
    //   177: fadd
    //   178: fstore_3
    //   179: fload_3
    //   180: putstatic lunarians/hub/Skins.nextMusic : F
    //   183: invokestatic musicTick : ()V
    //   186: return
    //   187: invokevirtual toString : ()Ljava/lang/String;
    //   190: astore_1
    //   191: iconst_1
    //   192: anewarray java/lang/Object
    //   195: astore #5
    //   197: aload #5
    //   199: iconst_0
    //   200: aload_1
    //   201: aastore
    //   202: ldc_w '[Lunarians Skins] tick: @'
    //   205: aload #5
    //   207: invokestatic debug : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   210: return
    // Exception table:
    //   from	to	target	type
    //   0	3	187	finally
    //   8	11	187	finally
    //   12	16	187	finally
    //   21	25	187	finally
    //   25	28	187	finally
    //   29	32	187	finally
    //   33	36	187	finally
    //   47	50	187	finally
    //   51	54	187	finally
    //   85	89	187	finally
    //   89	92	187	finally
    //   92	95	187	finally
    //   100	103	187	finally
    //   104	107	187	finally
    //   118	121	187	finally
    //   136	140	187	finally
    //   140	143	187	finally
    //   143	146	187	finally
    //   147	150	187	finally
    //   161	164	187	finally
    //   179	183	187	finally
    //   183	186	187	finally
  }
  
  public static void unequip(int paramInt) {
    IntSeq intSeq = equipped();
    intSeq.removeValue(paramInt);
    saveEquipped(intSeq);
  }
  
  private static UnitType unitType(String paramString) {
    Seq seq = Vars.content.units();
    Skins$11 skins$11 = new Skins$11();
    this(paramString);
    return (UnitType)seq.find(skins$11);
  }
  
  private static void updateMine(Jval.JsonArray paramJsonArray) {
    Seq seq = mine;
    seq.clear();
    Iterator<Jval> iterator = paramJsonArray.iterator();
    while (true) {
      boolean bool = iterator.hasNext();
      if (bool) {
        Jval jval = iterator.next();
        String str1 = jval.getString("status", "");
        String str2 = "approved";
        boolean bool2 = str2.equals(str1);
        if (bool2) {
          byte b = -1;
          Skins$Entry skins$Entry = parse(jval, b);
          if (skins$Entry != null) {
            Seq seq1 = mine;
            seq1.add(skins$Entry);
          } 
        } 
        continue;
      } 
      boolean bool1 = true;
      mineLoaded = bool1;
      IntSeq intSeq1 = equipped();
      IntSeq intSeq2 = new IntSeq();
      this();
      int i = 0;
      Object object = null;
      while (true) {
        int k = intSeq1.size;
        if (i < k) {
          k = intSeq1.get(i);
          Skins$Entry skins$Entry = mineById(k);
          if (skins$Entry != null) {
            k = intSeq1.get(i);
            intSeq2.add(k);
          } 
          i++;
          continue;
        } 
        i = intSeq2.size;
        int j = intSeq1.size;
        if (i != j)
          saveEquipped(intSeq2); 
        dirty = bool1;
        return;
      } 
      break;
    } 
  }
  
  public static boolean using(int paramInt) {
    return equipped().contains(paramInt);
  }
  
  static Seq weaponNames(UnitType paramUnitType) {
    Seq seq = (Seq)weaponNames.get(paramUnitType);
    if (seq == null) {
      seq = new Seq();
      this();
      Iterator<Weapon> iterator = paramUnitType.weapons.iterator();
      while (true) {
        boolean bool = iterator.hasNext();
        if (bool) {
          Weapon weapon = iterator.next();
          String str = weapon.name;
          if (str != null) {
            str = weapon.name;
            boolean bool1 = str.isEmpty();
            if (!bool1) {
              TextureRegion textureRegion = weapon.region;
              if (textureRegion != null) {
                textureRegion = weapon.region;
                bool1 = textureRegion.found();
                if (bool1) {
                  String str1 = weapon.name;
                  bool1 = seq.contains(str1);
                  if (!bool1) {
                    String str2 = weapon.name;
                    seq.add(str2);
                  } 
                } 
              } 
            } 
          } 
          continue;
        } 
        ObjectMap objectMap = weaponNames;
        objectMap.put(paramUnitType, seq);
        break;
      } 
    } 
    return seq;
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
  
  private static Label wrap(String paramString) {
    Label label = new Label();
    this(paramString);
    label.setWrap(true);
    return label;
  }
}
