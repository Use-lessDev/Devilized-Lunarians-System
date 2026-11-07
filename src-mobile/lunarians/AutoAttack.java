package lunarians;

import arc.Core;
import arc.Events;
import arc.Settings;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Cell;
import arc.struct.IntFloatMap;
import arc.struct.IntSet;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.I18NBundle;
import arc.util.Time;
import java.util.Iterator;
import lunarians.data.AttackSlot;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.core.GameState;
import mindustry.core.UI;
import mindustry.entities.EntityGroup;
import mindustry.entities.units.UnitController;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.dialogs.SettingsMenuDialog;
import mindustry.world.Block;

public class AutoAttack {
  public static final String KEY_REGROUP = "lunarians-attack-regroup";
  
  public static final String KEY_REGROUP_SEC = "lunarians-attack-regroup-sec";
  
  public static final String KEY_STANDOFF = "lunarians-attack-standoff-blocks";
  
  private static final float SCAN = 0.7F;
  
  private static final IntSet claimed;
  
  private static int count;
  
  private static final IntFloatMap dpsEst;
  
  private static boolean drawReg;
  
  private static final Seq enemyB;
  
  private static boolean errorLogged;
  
  private static final IntFloatMap fleeStart;
  
  private static final IntSet fleeing;
  
  private static final IntFloatMap gatherSince;
  
  private static final IntFloatMap lastHp;
  
  private static float lastRegroupAt;
  
  private static float lastScanTime;
  
  private static final Seq lines;
  
  private static final Seq shown;
  
  private static float timer;
  
  static {
    IntSet intSet2 = new IntSet();
    this();
    claimed = intSet2;
    IntFloatMap intFloatMap2 = new IntFloatMap();
    this();
    lastHp = intFloatMap2;
    intFloatMap2 = new IntFloatMap();
    this();
    dpsEst = intFloatMap2;
    lastScanTime = -1.0F;
    IntSet intSet1 = new IntSet();
    this();
    fleeing = intSet1;
    IntFloatMap intFloatMap1 = new IntFloatMap();
    this();
    fleeStart = intFloatMap1;
    intFloatMap1 = new IntFloatMap();
    this();
    gatherSince = intFloatMap1;
    Seq seq = new Seq();
    this();
    enemyB = seq;
    seq = new Seq();
    this();
    lines = seq;
    seq = new Seq();
    this();
    shown = seq;
  }
  
  private static void commandBuild(Unit paramUnit, Building paramBuilding, float paramFloat, boolean paramBoolean) {
    ensureMove(paramUnit);
    float f1 = paramBuilding.angleTo((Position)paramUnit) * 0.017453292F;
    float f2 = 0.9F;
    paramFloat *= f2;
    if (paramBoolean) {
      paramBoolean = isCore(paramBuilding);
      if (paramBoolean) {
        float f = coreSafeDist(paramBuilding);
        f2 = 0.95F;
        f *= f2;
        paramFloat = Math.max(paramFloat, f);
      } 
    } 
    float f3 = paramBuilding.x;
    f2 = Mathf.cos(f1) * paramFloat;
    f3 += f2;
    f2 = paramBuilding.y;
    f1 = Mathf.sin(f1) * paramFloat;
    f2 += f1;
    Vec2 vec2 = PathSafe.safeNear(paramUnit, f3, f2, 64.0F);
    UnitCoord.cmd(paramUnit, 1, 90.0F, paramBuilding, null, vec2);
  }
  
  private static void commandBuildFormation(Unit paramUnit, Building paramBuilding, float paramFloat1, float paramFloat2, int paramInt) {
    ensureMove(paramUnit);
    float f1 = paramBuilding.angleTo((Position)paramUnit) * 0.017453292F;
    int i = 1;
    float f2 = Float.MIN_VALUE;
    if (paramInt <= i) {
      paramFloat2 = 0.0F;
    } else {
      float f = (paramInt - i);
      i = 1056964608;
      f2 = 0.5F;
      f *= f2;
      paramFloat2 -= f;
      paramInt = 1051931443;
      f = 0.35F;
      paramFloat2 *= f;
    } 
    f1 += paramFloat2;
    paramFloat1 *= 0.9F;
    paramFloat2 = paramBuilding.x;
    float f3 = Mathf.cos(f1) * paramFloat1;
    paramFloat2 += f3;
    f3 = paramBuilding.y;
    f1 = Mathf.sin(f1) * paramFloat1;
    f3 += f1;
    Vec2 vec2 = PathSafe.safeNear(paramUnit, paramFloat2, f3, 64.0F);
    UnitCoord.cmd(paramUnit, 1, 90.0F, paramBuilding, null, vec2);
  }
  
  private static float coreSafeDist(Building paramBuilding) {
    return (paramBuilding.block.size << 3) * 0.7F + 48.0F + 24.0F;
  }
  
  public static int count() {
    return count;
  }
  
  private static void drawOverlay() {
    GameState gameState = Vars.state;
    if (gameState != null) {
      gameState = Vars.state;
      boolean bool = gameState.isGame();
      if (bool) {
        bool = LunariansSettings.enableAutoAttack;
        if (bool) {
          bool = overlayOn();
          if (bool) {
            Draw.z(120.0F);
            bool = false;
            gameState = null;
            int i = 0;
            while (true) {
              Seq seq = shown;
              int j = seq.size;
              if (i < j) {
                float[] arrayOfFloat = (float[])seq.get(i);
                j = 4;
                float f1 = arrayOfFloat[j];
                float f2 = 2.0F;
                float f3 = 0.8F;
                byte b1 = 2;
                byte b2 = 1;
                int k = f1 cmp f2;
                if (k == 0) {
                  Color color = Pal.accent;
                  Draw.color(color, f3);
                  j = 1069547520;
                  Lines.stroke(1.5F);
                  f1 = arrayOfFloat[0];
                  f2 = arrayOfFloat[b2];
                  float f = arrayOfFloat[b1];
                  Lines.circle(f1, f2, f);
                } else {
                  Color color;
                  k = 1065353216;
                  f2 = 1.0F;
                  j = f1 cmp f2;
                  if (j == 0) {
                    color = Pal.remove;
                  } else {
                    color = Pal.lightishOrange;
                  } 
                  Draw.color(color, f3);
                  j = 1067030938;
                  Lines.stroke(1.2F);
                  f1 = arrayOfFloat[0];
                  f2 = arrayOfFloat[b2];
                  f3 = arrayOfFloat[b1];
                  b2 = 3;
                  float f5 = arrayOfFloat[b2];
                  Lines.line(f1, f2, f3, f5);
                  f1 = arrayOfFloat[b1];
                  float f4 = arrayOfFloat[b2];
                  k = 1086324736;
                  f2 = 6.0F;
                  Lines.circle(f1, f4, f2);
                } 
                i++;
                continue;
              } 
              Draw.reset();
              break;
            } 
          } 
        } 
      } 
    } 
  }
  
  private static void ensureMove(Unit paramUnit) {
    UnitController unitController = paramUnit.controller();
    boolean bool = unitController instanceof CommandAI;
    if (bool) {
      UnitCommand unitCommand1 = ((CommandAI)paramUnit.controller()).command;
      UnitCommand unitCommand2 = UnitCommand.moveCommand;
      if (unitCommand1 != unitCommand2) {
        Player player = Vars.player;
        int i = paramUnit.id;
        int[] arrayOfInt = { i };
        unitCommand2 = UnitCommand.moveCommand;
        Call.setUnitCommand(player, arrayOfInt, unitCommand2);
      } 
    } 
  }
  
  public static int fleePercent() {
    int i = Core.settings.getInt("lunarians-attack-flee", 0);
    i = Math.min(90, i);
    return Math.max(0, i);
  }
  
  public static boolean formationOn() {
    return Core.settings.getBool("lunarians-attack-formation", true);
  }
  
  private static boolean groupLowDamage(Seq paramSeq) {
    boolean bool = paramSeq.isEmpty();
    byte b = 1;
    if (bool)
      return b; 
    bool = false;
    int i = 0;
    int j = 0;
    while (true) {
      int k = paramSeq.size;
      if (i < k) {
        Unit unit = (Unit)paramSeq.get(i);
        boolean bool1 = isHighDamage(unit);
        if (bool1)
          j++; 
        i++;
        continue;
      } 
      j <<= b;
      int m = paramSeq.size;
      return (j <= m) ? b : false;
    } 
  }
  
  private static void holdAwayFromCore(Unit paramUnit, Building paramBuilding, float paramFloat) {
    ensureMove(paramUnit);
    paramFloat *= 0.95F;
    float f1 = coreSafeDist(paramBuilding);
    paramFloat = Math.max(paramFloat, f1);
    f1 = paramBuilding.angleTo((Position)paramUnit) * 0.017453292F;
    float f2 = paramBuilding.x;
    float f3 = Mathf.cos(f1) * paramFloat;
    f2 += f3;
    float f4 = paramBuilding.y;
    f1 = Mathf.sin(f1) * paramFloat;
    f4 += f1;
    Vec2 vec2 = PathSafe.safeNear(paramUnit, f2, f4, 80.0F);
    UnitCoord.cmd(paramUnit, 1, 90.0F, null, null, vec2);
  }
  
  public static void init() {
    boolean bool = Vars.headless;
    if (!bool) {
      EventType.Trigger trigger = EventType.Trigger.update;
      AutoAttack$7 autoAttack$7 = new AutoAttack$7();
      this();
      Events.run(trigger, autoAttack$7);
      bool = drawReg;
      if (!bool) {
        bool = true;
        drawReg = bool;
        trigger = EventType.Trigger.draw;
        AutoAttack$8 autoAttack$8 = new AutoAttack$8();
        this();
        Events.run(trigger, autoAttack$8);
      } 
      ChatCommands.init();
      Class<EventType.WorldLoadEvent> clazz = EventType.WorldLoadEvent.class;
      AutoAttack$9 autoAttack$9 = new AutoAttack$9();
      this();
      Events.on(clazz, autoAttack$9);
      try {
        UI uI = Vars.ui;
        if (uI != null) {
          uI = Vars.ui;
          SettingsMenuDialog settingsMenuDialog = uI.settings;
          if (settingsMenuDialog != null) {
            UI uI1 = Vars.ui;
            SettingsMenuDialog settingsMenuDialog1 = uI1.settings;
            String str = "Lunarians Attack";
            AutoAttack$10 autoAttack$10 = new AutoAttack$10();
            this();
            settingsMenuDialog1.addCategory(str, autoAttack$10);
          } 
        } 
      } finally {}
    } 
  }
  
  private static boolean isCore(Building paramBuilding) {
    if (paramBuilding != null) {
      Block block = paramBuilding.block;
      boolean bool = block instanceof mindustry.world.blocks.storage.CoreBlock;
      if (bool)
        return true; 
    } 
    return false;
  }
  
  private static boolean isHighDamage(Unit paramUnit) {
    IntFloatMap intFloatMap = dpsEst;
    int i = paramUnit.id;
    float f1 = intFloatMap.get(i, 0.0F);
    float f2 = paramUnit.maxHealth * 0.08F;
    i = 1082130432;
    float f3 = 4.0F;
    f2 = Math.max(f3, f2);
    int j = f1 cmp f2;
    return (j > 0);
  }
  
  private static void label(String paramString1, String paramString2, String paramString3) {
    String str = "setting.";
    try {
      I18NBundle i18NBundle = Core.bundle;
      ObjectMap objectMap = i18NBundle.getProperties();
      StringBuilder stringBuilder = new StringBuilder();
      this(str);
      stringBuilder.append(paramString1);
      paramString1 = ".name";
      stringBuilder.append(paramString1);
      paramString1 = stringBuilder.toString();
      paramString2 = tr(paramString2, paramString3);
      objectMap.put(paramString1, paramString2);
    } finally {}
  }
  
  public static int maxStandoffTiles(Unit paramUnit) {
    if (paramUnit != null) {
      UnitType unitType = paramUnit.type;
      if (unitType != null)
        return maxStandoffTiles(paramUnit.type); 
    } 
    return 4;
  }
  
  public static int maxStandoffTiles(UnitType paramUnitType) {
    if (paramUnitType == null)
      return 4; 
    int i = Mathf.ceil(paramUnitType.maxRange / 8.0F) + 2;
    return Math.max(1, i);
  }
  
  private static Unit nearestHealer(Unit paramUnit) {
    Unit unit = null;
    float f = Float.MAX_VALUE;
    int i = 0;
    while (true) {
      EntityGroup entityGroup = Groups.unit;
      int j = entityGroup.size();
      if (i < j) {
        Unit unit1 = (Unit)Groups.unit.index(i);
        if (unit1 != null) {
          boolean bool = unit1.isValid();
          if (bool) {
            Team team1 = unit1.team;
            Team team2 = Vars.player.team();
            if (team1 == team2) {
              bool = AutoUnitHeal.isActiveHealer(unit1.id);
              if (bool) {
                float f1 = paramUnit.dst((Position)unit1);
                int k = f1 cmp f;
                if (k < 0) {
                  unit = unit1;
                  f = f1;
                } 
              } 
            } 
          } 
        } 
        i++;
        continue;
      } 
      return unit;
    } 
  }
  
  public static boolean overlayOn() {
    return Core.settings.getBool("lunarians-attack-overlay", true);
  }
  
  private static Unit pickCoreBreaker(Seq paramSeq, Building paramBuilding) {
    Unit unit = null;
    float f = Float.MAX_VALUE;
    int i = 0;
    while (true) {
      int j = paramSeq.size;
      if (i < j) {
        Unit unit1 = (Unit)paramSeq.get(i);
        boolean bool = isHighDamage(unit1);
        if (!bool) {
          float f1 = unit1.dst((Position)paramBuilding);
          float f2 = unit1.health;
          float f3 = unit1.maxHealth;
          float f4 = Math.max(1.0F, f3);
          f2 /= f4;
          f4 = 40.0F;
          f2 *= f4;
          f1 -= f2;
          int k = f1 cmp f;
          if (k < 0) {
            unit = unit1;
            f = f1;
          } 
        } 
        i++;
        continue;
      } 
      return (unit != null) ? unit : (Unit)paramSeq.first();
    } 
  }
  
  private static void profile(int paramInt1, int paramInt2, int paramInt3) {
    Settings settings1 = Core.settings;
    boolean bool = false;
    byte b = 1;
    if (paramInt1 == b) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
      Object object = null;
    } 
    Boolean bool1 = Boolean.valueOf(paramInt1);
    String str2 = "lunarians-attack-smart";
    settings1.put(str2, bool1);
    Settings settings2 = Core.settings;
    String str1 = "lunarians-attack-flee";
    Integer integer = Integer.valueOf(paramInt2);
    settings2.put(str1, integer);
    settings2 = Core.settings;
    if (paramInt3 == b)
      bool = true; 
    Boolean bool2 = Boolean.valueOf(bool);
    String str3 = "lunarians-attack-formation";
    settings2.put(str3, bool2);
    UI uI = Vars.ui;
    if (uI != null) {
      uI = Vars.ui;
      str3 = "Perfil aplicado. Reabra o menu para ver os valores.";
      String str = tr("Profile applied. Reopen this menu to see the values.", str3);
      paramInt3 = 1077936128;
      float f = 3.0F;
      uI.showInfoToast(str, f);
    } 
  }
  
  private static boolean pt() {
    String str = LunariansSettings.language;
    if (str != null) {
      str = LunariansSettings.language;
      String str1 = "pt";
      boolean bool = str.startsWith(str1);
      if (bool)
        return true; 
    } 
    return false;
  }
  
  public static boolean regroupEnabled() {
    return Core.settings.getBool("lunarians-attack-regroup", false);
  }
  
  public static int regroupSec() {
    Settings settings = Core.settings;
    byte b = 5;
    int i = settings.getInt("lunarians-attack-regroup-sec", b);
    i = Math.min(120, i);
    return Math.max(b, i);
  }
  
  private static void regroupSlot(Seq paramSeq) {
    int i = paramSeq.size;
    byte b = 2;
    float f = 2.8E-45F;
    if (i >= b) {
      i = 0;
      b = 0;
      f = 0.0F;
      float f1 = 0.0F;
      int j = 0;
      float f2 = 0.0F;
      Object object = null;
      while (true) {
        int k = paramSeq.size;
        if (j < k) {
          float f3 = ((Unit)paramSeq.get(j)).x;
          f += f3;
          Unit unit = (Unit)paramSeq.get(j);
          f3 = unit.y;
          f1 += f3;
          j++;
          continue;
        } 
        f2 = paramSeq.size;
        f /= f2;
        j = paramSeq.size;
        f2 = j;
        f1 /= f2;
        while (true) {
          j = paramSeq.size;
          if (i < j) {
            object = paramSeq.get(i);
            Object object1 = object;
            object1 = object;
            ensureMove((Unit)object1);
            f2 = i * 360.0F;
            float f3 = paramSeq.size;
            f2 = f2 / f3 * 0.017453292F;
            f3 = Mathf.cos(f2);
            float f4 = 16.0F;
            f3 *= f4;
            f2 = Mathf.sin(f2) * f4;
            f3 += f;
            f2 += f1;
            Vec2 vec2 = PathSafe.safeNear((Unit)object1, f3, f2, 48.0F);
            boolean bool = true;
            f3 = Float.MIN_VALUE;
            f4 = 90.0F;
            UnitCoord.cmd((Unit)object1, bool, f4, null, null, vec2);
            j = count + 1;
            count = j;
            i++;
            continue;
          } 
          break;
        } 
        break;
      } 
    } 
  }
  
  public static void reset() {
    timer = 0.0F;
    count = 0;
    lastHp.clear();
    dpsEst.clear();
    claimed.clear();
    lastScanTime = -1.0F;
    lastRegroupAt = 0.0F;
    fleeing.clear();
    enemyB.clear();
    fleeStart.clear();
    gatherSince.clear();
    lines.clear();
    shown.clear();
  }
  
  private static void scan(float paramFloat) {
    Seq seq = LunariansSettings.attackSlots;
    boolean bool = seq.isEmpty();
    if (bool)
      return; 
    bool = false;
    float f = 0.0F;
    Object object = null;
    count = 0;
    lines.clear();
    claimed.clear();
    EntityGroup entityGroup = Groups.unit;
    int i = entityGroup.size();
    enemyB.clear();
    Iterator iterator = Groups.build.iterator();
    while (true) {
      while (true) {
        boolean bool1 = iterator.hasNext();
        break;
      } 
      if (SYNTHETIC_LOCAL_VARIABLE_9 != null) {
        boolean bool1 = SYNTHETIC_LOCAL_VARIABLE_9.isValid();
        if (bool1) {
          Team team1 = ((Building)SYNTHETIC_LOCAL_VARIABLE_9).team;
          Team team2 = Vars.player.team();
          if (team1 != team2) {
            team1 = ((Building)SYNTHETIC_LOCAL_VARIABLE_9).team;
            team2 = Team.derelict;
            if (team1 != team2) {
              Seq seq1 = enemyB;
              seq1.add(SYNTHETIC_LOCAL_VARIABLE_9);
            } 
          } 
        } 
      } 
    } 
  }
  
  public static void setRegroupEnabled(boolean paramBoolean) {
    Settings settings = Core.settings;
    Boolean bool = Boolean.valueOf(paramBoolean);
    settings.put("lunarians-attack-regroup", bool);
  }
  
  public static void setRegroupSec(int paramInt) {
    Settings settings = Core.settings;
    paramInt = Math.min(120, paramInt);
    Integer integer = Integer.valueOf(Math.max(5, paramInt));
    settings.put("lunarians-attack-regroup-sec", integer);
  }
  
  public static void setStandoffBlocks(int paramInt) {
    Settings settings = Core.settings;
    paramInt = Math.min(40, paramInt);
    Integer integer = Integer.valueOf(Math.max(1, paramInt));
    settings.put("lunarians-attack-standoff-blocks", integer);
  }
  
  public static boolean smartTargets() {
    return Core.settings.getBool("lunarians-attack-smart", true);
  }
  
  public static int standoffBlocks() {
    int i = Core.settings.getInt("lunarians-attack-standoff-blocks", 3);
    i = Math.min(40, i);
    return Math.max(1, i);
  }
  
  public static float standoffDist(Unit paramUnit) {
    return standoffDist(paramUnit, null);
  }
  
  public static float standoffDist(Unit paramUnit, AttackSlot paramAttackSlot) {
    boolean bool;
    int i = maxStandoffTiles(paramUnit);
    if (paramAttackSlot != null) {
      bool = paramAttackSlot.standoffTiles;
    } else {
      bool = false;
      paramAttackSlot = null;
    } 
    if (bool) {
      boolean bool1 = true;
      i = Mathf.clamp(bool, bool1, i);
    } 
    return (i << 3);
  }
  
  private static String tr(String paramString1, String paramString2) {
    boolean bool = pt();
    return bool ? paramString2 : paramString1;
  }
  
  private static void trackDamage(Unit paramUnit, float paramFloat) {
    IntFloatMap intFloatMap1 = lastHp;
    int i = paramUnit.id;
    float f1 = paramUnit.health;
    float f2 = intFloatMap1.get(i, f1);
    f1 = paramUnit.health;
    f2 -= f1;
    f1 = 0.0F;
    f2 = Math.max(0.0F, f2);
    float f3 = 0.05F;
    int j = paramFloat cmp f3;
    if (j > 0) {
      f2 /= paramFloat;
    } else {
      i = 0;
      f2 = 0.0F;
    } 
    IntFloatMap intFloatMap2 = dpsEst;
    j = paramUnit.id;
    f1 = intFloatMap2.get(j, 0.0F);
    j = paramUnit.id;
    f1 *= 0.55F;
    f2 *= 0.45F;
    f1 += f2;
    intFloatMap2.put(j, f1);
    int k = paramUnit.id;
    float f4 = paramUnit.health;
    intFloatMap1.put(k, f4);
  }
  
  private static void update() {
    boolean bool = LunariansSettings.enableAutoAttack;
    if (bool) {
      GameState gameState = Vars.state;
      if (gameState != null) {
        gameState = Vars.state;
        bool = gameState.isGame();
        if (bool) {
          Player player = Vars.player;
          if (player != null) {
            float f1 = timer;
            float f2 = Time.delta;
            float f3 = 60.0F;
            f2 /= f3;
            f1 += f2;
            timer = f1;
            int j = 1060320051;
            f2 = LunariansSettings.actionInterval(0.7F);
            int i = f1 cmp f2;
            if (i >= 0) {
              f1 = timer;
              j = 0;
              f2 = 0.0F;
              String str = null;
              timer = 0.0F;
              try {
                return;
              } finally {
                player = null;
                boolean bool1 = errorLogged;
              } 
            } 
          } 
        } 
      } 
    } 
  }
}
