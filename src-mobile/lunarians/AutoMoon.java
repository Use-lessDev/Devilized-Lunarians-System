package lunarians;

import arc.Events;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.struct.IntFloatMap;
import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.struct.Queue;
import arc.struct.Seq;
import arc.util.Time;
import lunarians.data.MoonSlot;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.core.GameState;
import mindustry.entities.EntityGroup;
import mindustry.entities.units.BuildPlan;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class AutoMoon {
  private static final float ATTACK_RANGE_PAD = 1.15F;
  
  private static final float SCAN = 0.55F;
  
  private static final IntFloatMap angles;
  
  private static final IntSet claimed;
  
  private static int count;
  
  private static boolean errorLogged;
  
  private static float timer;
  
  static {
    IntSet intSet = new IntSet();
    this();
    claimed = intSet;
    IntFloatMap intFloatMap = new IntFloatMap();
    this();
    angles = intFloatMap;
  }
  
  private static void assistBuild(Seq paramSeq, Unit paramUnit) {
    BuildPlan buildPlan = currentPlan(paramUnit);
    float f1 = paramUnit.x;
    float f2 = paramUnit.y;
    if (buildPlan != null)
      try {
        int k = buildPlan.x;
        f1 = k;
        f2 = 8.0F;
        f1 *= f2;
        int m = 1082130432;
        float f5 = 4.0F;
        f1 += f5;
        int n = buildPlan.y;
      } finally {
        null;
        f1 = paramUnit.x;
      }  
    IntSeq intSeq1 = new IntSeq();
    this();
    IntSeq intSeq2 = new IntSeq();
    this();
    int i = 0;
    float f3 = 0.0F;
    int j = 0;
    float f4 = 0.0F;
    Player player = null;
    while (true) {
      int k = paramSeq.size;
      if (j < k) {
        Unit unit = (Unit)paramSeq.get(j);
        UnitType unitType = unit.type;
        if (unitType != null) {
          unitType = unit.type;
          float f5 = unitType.buildSpeed;
          float f6 = 0.0F;
          Object object = null;
          int n = f5 cmp 0.0F;
          if (n > 0) {
            k = unit.id;
            intSeq1.add(k);
            continue;
          } 
        } 
        k = unit.id;
        intSeq2.add(k);
        continue;
      } 
      int m = intSeq1.size;
      if (m > 0) {
        UnitCommand unitCommand = UnitCommand.assistCommand;
        player = Vars.player;
        int[] arrayOfInt = intSeq1.toArray();
        Call.setUnitCommand(player, arrayOfInt, unitCommand);
      } 
      while (true) {
        int n = paramSeq.size;
        if (i < n) {
          Unit unit = (Unit)paramSeq.get(i);
          UnitType unitType = unit.type;
          if (unitType != null) {
            unitType = unit.type;
            f7 = unitType.buildRange;
          } else {
            m = 1109393408;
            f7 = 40.0F;
          } 
          float f7 = Math.max(24.0F, f7);
          j = 1060320051;
          f7 *= 0.7F;
          f4 = i * 360.0F;
          k = paramSeq.size;
          float f8 = Math.max(1, k);
          f4 /= f8;
          k = 1016003125;
          f4 *= 0.017453292F;
          f8 = Mathf.cos(f4);
          f7 *= 0.3F;
          int i1 = 1099956224;
          float f5 = 18.0F;
          float f6 = Math.min(f5, f7);
          f8 *= f6;
          f4 = Mathf.sin(f4);
          f7 = Math.min(f5, f7);
          f4 *= f7;
          Player player1 = Vars.player;
          n = unit.id;
          int[] arrayOfInt = { n };
          Vec2 vec2 = new Vec2();
          f8 += f1;
          f4 += f2;
          this(f8, f4);
          boolean bool = true;
          Call.commandUnits(player1, arrayOfInt, null, null, vec2, false, bool);
          i++;
          continue;
        } 
        break;
      } 
      return;
      j++;
    } 
  }
  
  public static int count() {
    return count;
  }
  
  private static BuildPlan currentPlan(Unit paramUnit) {
    if (paramUnit == null)
      return null; 
    try {
      BuildPlan buildPlan = paramUnit.buildPlan();
      if (buildPlan != null)
        return buildPlan; 
      Queue queue = paramUnit.plans;
      if (queue != null) {
        queue = paramUnit.plans;
        int i = queue.size;
        if (i > 0) {
          Queue queue1 = paramUnit.plans;
          Object object = queue1.first();
          return (BuildPlan)object;
        } 
      } 
    } finally {}
    return null;
  }
  
  private static Unit findBuildingHost(MoonSlot paramMoonSlot) {
    boolean bool = paramMoonSlot.hasUnitCenter();
    if (bool) {
      EntityGroup entityGroup = Groups.unit;
      int i = paramMoonSlot.followId;
      Unit unit1 = (Unit)entityGroup.getByID(i);
      bool = isConstructing(unit1);
      if (bool)
        return unit1; 
    } 
    Unit unit = Vars.player.unit();
    bool = isConstructing(unit);
    return bool ? unit : null;
  }
  
  public static void init() {
    boolean bool = Vars.headless;
    if (bool)
      return; 
    EventType.Trigger trigger = EventType.Trigger.update;
    AutoMoon$1 autoMoon$1 = new AutoMoon$1();
    this();
    Events.run(trigger, autoMoon$1);
    AutoMoon$2 autoMoon$2 = new AutoMoon$2();
    this();
    Events.on(EventType.WorldLoadEvent.class, autoMoon$2);
  }
  
  private static boolean isConstructing(Unit paramUnit) {
    if (paramUnit != null) {
      boolean bool = paramUnit.isValid();
      if (bool)
        try {
          bool = paramUnit.activelyBuilding();
          boolean bool1 = true;
          if (bool)
            return bool1; 
          bool = paramUnit.isBuilding();
          if (bool)
            return bool1; 
          Queue queue = paramUnit.plans;
          if (queue != null) {
            Queue queue1 = paramUnit.plans;
            int i = queue1.size;
            if (i > 0)
              return bool1; 
          } 
        } finally {} 
    } 
    return false;
  }
  
  private static Unit nearestEnemy(Unit paramUnit, float paramFloat1, float paramFloat2, float paramFloat3) {
    EntityGroup entityGroup = Groups.unit;
    int i = entityGroup.size();
    Unit unit = null;
    int j = 0;
    float f = paramFloat3;
    while (j < i) {
      Unit unit1 = (Unit)Groups.unit.index(j);
      if (unit1 != null) {
        boolean bool = unit1.isValid();
        if (bool) {
          Team team1 = unit1.team;
          Team team2 = paramUnit.team;
          if (team1 != team2) {
            float f1 = unit1.x;
            float f2 = unit1.y;
            f1 = Mathf.dst(paramFloat1, paramFloat2, f1, f2);
            int k = f1 cmp paramFloat3;
            if (k <= 0) {
              f1 = paramUnit.dst((Position)unit1);
              int m = f1 cmp f;
              if (m < 0) {
                unit = unit1;
                f = f1;
              } 
            } 
          } 
        } 
      } 
      j++;
    } 
    return unit;
  }
  
  private static Building nearestEnemyBuild(Unit paramUnit, float paramFloat1, float paramFloat2, float paramFloat3) {
    EntityGroup entityGroup = Groups.build;
    int i = entityGroup.size();
    Building building = null;
    int j = 0;
    float f = paramFloat3;
    while (j < i) {
      Building building1 = (Building)Groups.build.index(j);
      if (building1 != null) {
        boolean bool = building1.isValid();
        if (bool) {
          Team team1 = building1.team;
          Team team2 = paramUnit.team;
          if (team1 != team2) {
            float f1 = building1.x;
            float f2 = building1.y;
            f1 = Mathf.dst(paramFloat1, paramFloat2, f1, f2);
            int k = f1 cmp paramFloat3;
            if (k <= 0) {
              f1 = paramUnit.dst((Position)building1);
              int m = f1 cmp f;
              if (m < 0) {
                building = building1;
                f = f1;
              } 
            } 
          } 
        } 
      } 
      j++;
    } 
    return building;
  }
  
  public static void reset() {
    timer = 0.0F;
    count = 0;
    claimed.clear();
    angles.clear();
  }
  
  private static void resolveCenter(MoonSlot paramMoonSlot, float[] paramArrayOffloat) {
    boolean bool = paramMoonSlot.hasUnitCenter();
    boolean bool1 = true;
    if (bool) {
      EntityGroup entityGroup = Groups.unit;
      int i = paramMoonSlot.followId;
      Unit unit1 = (Unit)entityGroup.getByID(i);
      if (unit1 != null) {
        boolean bool2 = unit1.isValid();
        if (bool2) {
          float f1 = unit1.x;
          paramArrayOffloat[0] = f1;
          f1 = unit1.y;
          paramArrayOffloat[bool1] = f1;
          return;
        } 
      } 
    } 
    bool = paramMoonSlot.hasTileCenter();
    if (bool) {
      float f2 = paramMoonSlot.x;
      float f3 = 8.0F;
      f2 *= f3;
      float f4 = 4.0F;
      f2 += f4;
      paramArrayOffloat[0] = f2;
      float f1 = paramMoonSlot.y * f3 + f4;
      paramArrayOffloat[bool1] = f1;
      return;
    } 
    Unit unit = Vars.player.unit();
    if (unit != null) {
      unit = Vars.player.unit();
      boolean bool2 = unit.isValid();
      if (bool2) {
        float f1 = (Vars.player.unit()).x;
        paramArrayOffloat[0] = f1;
        f1 = (Vars.player.unit()).y;
        paramArrayOffloat[bool1] = f1;
        return;
      } 
    } 
    float f = Vars.player.x;
    paramArrayOffloat[0] = f;
    f = Vars.player.y;
    paramArrayOffloat[bool1] = f;
  }
  
  private static void scan(float paramFloat) {
    Seq seq = LunariansSettings.moonSlots;
    boolean bool = seq.isEmpty();
    if (!bool) {
      claimed.clear();
      bool = false;
      float f = 0.0F;
      Unit unit = null;
      count = 0;
      EntityGroup entityGroup = Groups.unit;
      int i = entityGroup.size();
      byte b = 2;
      float[] arrayOfFloat = new float[b];
      int j = 0;
      while (true) {
        int k = seq.size;
        if (j < k) {
          MoonSlot moonSlot = (MoonSlot)seq.get(j);
          boolean bool1 = moonSlot.ready();
          if (bool1) {
            resolveCenter(moonSlot, arrayOfFloat);
            float f1 = arrayOfFloat[0];
            int n = 1;
            float f2 = Float.MIN_VALUE;
            float f3 = arrayOfFloat[n];
            float f4 = moonSlot.radius * 8.0F;
            int i1 = 1082130432;
            float f5 = moonSlot.speed;
            float f6 = Math.min(4.0F, f5);
            int i2 = 1048576000;
            f5 = 0.25F;
            f6 = Math.max(f5, f6);
            Seq seq1 = new Seq();
            this();
            int i3 = 0;
            float f7 = 0.0F;
            IntSet intSet = null;
            while (true)
              i3++; 
            int m = moonSlot.maxUnits;
            if (m > 0) {
              m = seq1.size;
              i3 = moonSlot.maxUnits;
              if (m > i3) {
                m = moonSlot.maxUnits;
                seq1.truncate(m);
              } 
            } 
            m = 0;
            f = 0.0F;
            unit = null;
            while (true) {
              i3 = seq1.size;
              if (m < i3) {
                intSet = claimed;
                Unit unit1 = (Unit)seq1.get(m);
                int i4 = unit1.id;
                intSet.add(i4);
                m++;
                continue;
              } 
              boolean bool2 = seq1.isEmpty();
              if (!bool2) {
                unit = findBuildingHost(moonSlot);
                if (unit != null) {
                  assistBuild(seq1, unit);
                  int i5 = count;
                  k = seq1.size;
                  i5 += k;
                  count = i5;
                  break;
                } 
                int i4 = 1065353216;
                float f8 = moonSlot.radius;
                f = Math.max(1.0F, f8);
                f8 = 2.2F / f;
                f = Math.max(f5, f8) * paramFloat * f6;
                k = 0;
                f8 = 0.0F;
                moonSlot = null;
                while (true) {
                  i1 = seq1.size;
                  if (k < i1) {
                    Unit unit2 = (Unit)seq1.get(k);
                    UnitType unitType = unit2.type;
                    f5 = unitType.maxRange;
                    i3 = 1103101952;
                    f7 = 24.0F;
                    f5 = Math.max(f7, f5);
                    int i5 = 1066611507;
                    float f9 = 1.15F;
                    f5 = f5 * f9 + f4;
                    Unit unit1 = nearestEnemy(unit2, f1, f3, f5);
                    if (unit1 == null) {
                      Building building = nearestEnemyBuild(unit2, f1, f3, f5);
                    } else {
                      i2 = 0;
                      f5 = 0.0F;
                      unitType = null;
                    } 
                    UnitCommand unitCommand1 = ((CommandAI)unit2.controller()).command;
                    UnitCommand unitCommand2 = UnitCommand.moveCommand;
                    if (unitCommand1 != unitCommand2) {
                      Player player = Vars.player;
                      int i6 = unit2.id;
                      int[] arrayOfInt = { i6 };
                      UnitCommand unitCommand = UnitCommand.moveCommand;
                      Call.setUnitCommand(player, arrayOfInt, unitCommand);
                    } 
                    n = 1016003125;
                    f2 = 0.017453292F;
                    i3 = 1063675494;
                    f7 = 0.9F;
                    if (unit1 != null) {
                      unitType = unit2.type;
                      f5 = unitType.maxRange;
                      int i6 = 1103101952;
                      f5 = Math.max(24.0F, f5) * f7;
                      f7 = unit1.angleTo((Position)unit2) * f2;
                      Player player = Vars.player;
                      n = unit2.id;
                      int[] arrayOfInt = { n };
                      Vec2 vec2 = new Vec2();
                      f6 = unit1.x;
                      float f10 = Mathf.cos(f7) * f5;
                      f6 += f10;
                      f10 = unit1.y;
                      f7 = Mathf.sin(f7) * f5;
                      f10 += f7;
                      this(f6, f10);
                      boolean bool3 = true;
                      Call.commandUnits(player, arrayOfInt, null, unit1, vec2, false, bool3);
                      n = count + 1;
                      count = n;
                    } else if (unitType != null) {
                      UnitType unitType1 = unit2.type;
                      f9 = unitType1.maxRange;
                      int i6 = 1103101952;
                      f9 = Math.max(24.0F, f9) * f7;
                      f7 = unitType.angleTo((Position)unit2) * f2;
                      Player player = Vars.player;
                      n = unit2.id;
                      int[] arrayOfInt = { n };
                      Vec2 vec2 = new Vec2();
                      f6 = ((Building)unitType).x;
                      float f10 = Mathf.cos(f7) * f9;
                      f6 += f10;
                      f10 = ((Building)unitType).y;
                      f7 = Mathf.sin(f7) * f9;
                      f10 += f7;
                      this(f6, f10);
                      boolean bool3 = true;
                      Call.commandUnits(player, arrayOfInt, (Building)unitType, null, vec2, false, bool3);
                      n = count + 1;
                      count = n;
                    } else {
                      IntFloatMap intFloatMap = angles;
                      i2 = unit2.id;
                      f7 = k;
                      i5 = seq1.size;
                      i5 = Math.max(1, i5);
                      f9 = i5;
                      int i6 = 1086918619;
                      float f10 = 6.2831855F;
                      f9 = f10 / f9;
                      f7 *= f9;
                      f5 = intFloatMap.get(i2, f7) + f;
                      i3 = f5 cmp f10;
                      if (i3 > 0)
                        f5 -= f10; 
                      i3 = unit2.id;
                      intFloatMap.put(i3, f5);
                      Player player = Vars.player;
                      n = unit2.id;
                      int[] arrayOfInt = { n };
                      Vec2 vec2 = new Vec2();
                      f6 = Mathf.cos(f5) * f4 + f1;
                      f5 = Mathf.sin(f5) * f4 + f3;
                      this(f6, f5);
                      boolean bool3 = true;
                      Call.commandUnits(player, arrayOfInt, null, null, vec2, false, bool3);
                      n = count + 1;
                      count = n;
                    } 
                    k++;
                    continue;
                  } 
                  break;
                } 
              } else {
                break;
              } 
            } 
          } 
          continue;
        } 
        break;
        j++;
        bool = false;
        f = 0.0F;
        unit = null;
      } 
    } 
  }
  
  private static void update() {
    boolean bool = LunariansSettings.enableAutoMoon;
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
            int j = 1057803469;
            f2 = LunariansSettings.actionInterval(0.55F);
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
