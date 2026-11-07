package lunarians;

import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.geom.Vec2;
import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Time;
import lunarians.data.DefenceSlot;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.entities.EntityGroup;
import mindustry.entities.units.UnitController;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class AutoDefence {
  private static final float SCAN = 1.2F;
  
  private static final IntSet claimed;
  
  private static int count;
  
  private static boolean errorLogged;
  
  private static float timer;
  
  static {
    IntSet intSet = new IntSet();
    this();
    claimed = intSet;
  }
  
  public static int count() {
    return count;
  }
  
  private static void draw() {
    boolean bool = LunariansSettings.enableAutoDefence;
    if (bool) {
      GameState gameState = Vars.state;
      if (gameState != null) {
        gameState = Vars.state;
        bool = gameState.isGame();
        if (bool) {
          Seq seq = LunariansSettings.defenceSlots;
          boolean bool1 = seq.isEmpty();
          if (!bool1) {
            Draw.z(120.0F);
            bool1 = false;
            while (true) {
              int i = seq.size;
              if (bool1 < i) {
                DefenceSlot defenceSlot = (DefenceSlot)seq.get(bool1);
                boolean bool2 = defenceSlot.ready();
                if (bool2) {
                  int k = defenceSlot.x;
                  float f1 = k;
                  float f2 = 8.0F;
                  f1 *= f2;
                  float f3 = 4.0F;
                  f1 += f3;
                  int m = defenceSlot.y;
                  float f4 = m * f2 + f3;
                  float f5 = defenceSlot.radius * f2;
                  Draw.color(Color.scarlet.cpy().a(0.08F));
                  Fill.circle(f1, f4, f5);
                  Color color2 = Color.scarlet.cpy();
                  f3 = 0.55F;
                  color2 = color2.a(f3);
                  Draw.color(color2);
                  Lines.stroke(1.4F);
                  Lines.circle(f1, f4, f5);
                  Color color1 = Color.orange.cpy();
                  f2 = 0.8F;
                  color1 = color1.a(f2);
                  Draw.color(color1);
                  i = 1077936128;
                  f5 = 3.0F;
                  Fill.circle(f1, f4, f5);
                } 
                int j = bool1 + 1;
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
  
  public static void init() {
    boolean bool = Vars.headless;
    if (bool)
      return; 
    EventType.Trigger trigger = EventType.Trigger.update;
    AutoDefence$1 autoDefence$1 = new AutoDefence$1();
    this();
    Events.run(trigger, autoDefence$1);
    trigger = EventType.Trigger.draw;
    AutoDefence$2 autoDefence$2 = new AutoDefence$2();
    this();
    Events.run(trigger, autoDefence$2);
    AutoDefence$3 autoDefence$3 = new AutoDefence$3();
    this();
    Events.on(EventType.WorldLoadEvent.class, autoDefence$3);
  }
  
  public static void reset() {
    timer = 0.0F;
    count = 0;
    claimed.clear();
  }
  
  private static void scan() {
    Seq seq = LunariansSettings.defenceSlots;
    boolean bool = seq.isEmpty();
    if (!bool) {
      bool = false;
      count = 0;
      claimed.clear();
      int i = 0;
      while (true) {
        int j = seq.size;
        if (i < j) {
          DefenceSlot defenceSlot = (DefenceSlot)seq.get(i);
          boolean bool1 = defenceSlot.ready();
          if (bool1) {
            int k = defenceSlot.x;
            float f1 = k;
            float f2 = 8.0F;
            f1 *= f2;
            int m = 1082130432;
            float f3 = 4.0F;
            f1 += f3;
            int n = defenceSlot.y;
            float f4 = n * f2 + f3;
            f3 = defenceSlot.radius * f2;
            int i1 = defenceSlot.maxUnits;
            if (i1 > 0) {
              i1 = defenceSlot.maxUnits;
            } else {
              i1 = -1 >>> 1;
              f2 = 0.0F / 0.0F;
            } 
            Seq seq2 = new Seq();
            this();
            EntityGroup entityGroup = Groups.unit;
            int i2 = entityGroup.size();
            int i3 = 0;
            float f5 = 0.0F;
            Unit unit = null;
            while (i3 < i2) {
              Unit unit1 = (Unit)Groups.unit.index(i3);
              if (unit1 != null) {
                boolean bool3 = unit1.isValid();
                if (bool3) {
                  Team team1 = unit1.team;
                  Team team2 = Vars.player.team();
                  if (team1 == team2) {
                    String str1 = defenceSlot.unit;
                    String str2 = unit1.type.name;
                    bool3 = str1.equals(str2);
                    if (bool3) {
                      UnitController unitController = unit1.controller();
                      bool3 = unitController instanceof mindustry.ai.types.CommandAI;
                      if (bool3) {
                        IntSet intSet = claimed;
                        int i4 = unit1.id;
                        bool3 = intSet.contains(i4);
                        if (!bool3) {
                          bool3 = AutoUnitHeal.isActiveHealer(unit1.id);
                          if (!bool3) {
                            UnitType unitType = unit1.type;
                            bool3 = AutoUnitHeal.isHealerType(unitType);
                            if (!bool3)
                              seq2.add(unit1); 
                          } 
                        } 
                      } 
                    } 
                  } 
                } 
              } 
              i3++;
            } 
            AutoDefence$4 autoDefence$4 = new AutoDefence$4();
            this(f1, f4);
            seq2.sort(autoDefence$4);
            Seq seq1 = new Seq();
            this();
            i3 = 0;
            f5 = 0.0F;
            unit = null;
            while (true) {
              int i4 = seq2.size;
              if (i3 < i4) {
                i4 = seq1.size;
                if (i4 < i1) {
                  Unit unit1 = (Unit)seq2.get(i3);
                  seq1.add(unit1);
                  IntSet intSet = claimed;
                  i4 = unit1.id;
                  intSet.add(i4);
                  i3++;
                  continue;
                } 
              } 
              break;
            } 
            boolean bool2 = seq1.isEmpty();
            if (!bool2) {
              int i4 = count;
              int i5 = seq1.size;
              count = i4 + i5;
              i5 = 2139095039;
              float f = Float.MAX_VALUE;
              boolean bool3 = false;
              Unit unit1 = null;
              i4 = 0;
              f2 = 0.0F;
              while (i4 < i2) {
                unit = (Unit)Groups.unit.index(i4);
                if (unit != null) {
                  boolean bool4 = unit.isValid();
                  if (bool4) {
                    Team team1 = unit.team;
                    Team team2 = Vars.player.team();
                    if (team1 != team2) {
                      float f6 = unit.dst(f1, f4);
                      int i6 = f6 cmp f3;
                      if (i6 <= 0) {
                        i6 = f6 cmp f;
                        if (i6 < 0) {
                          unit1 = unit;
                          f = f6;
                        } 
                      } 
                    } 
                  } 
                } 
                i4++;
              } 
              if (unit1 != null) {
                IntSeq intSeq = new IntSeq();
                this();
                i4 = 0;
                f2 = 0.0F;
                while (true) {
                  m = seq1.size;
                  if (i4 < m) {
                    Unit unit2 = (Unit)seq1.get(i4);
                    m = unit2.id;
                    intSeq.add(m);
                    i4++;
                    continue;
                  } 
                  Player player = Vars.player;
                  int[] arrayOfInt = intSeq.toArray();
                  Object object2 = null;
                  boolean bool4 = false;
                  Object object1 = null;
                  Call.commandUnits(player, arrayOfInt, null, unit1, null, false, false);
                  break;
                } 
              } else {
                i4 = 0;
                f2 = 0.0F;
                while (true) {
                  m = seq1.size;
                  if (i4 < m) {
                    m = seq1.size;
                    i5 = 1;
                    f = Float.MIN_VALUE;
                    if (m <= i5) {
                      m = 0;
                      f3 = 0.0F;
                      Object object = null;
                    } else {
                      f3 = i4 * 360.0F;
                      f = seq1.size;
                      f3 /= f;
                      i5 = 1016003125;
                      f = 0.017453292F;
                      f3 *= f;
                    } 
                    double d1 = f3;
                    double d2 = Math.cos(d1);
                    f3 = (float)d2;
                    f5 = 10.0F;
                    f3 *= f5;
                    d1 = Math.sin(d1);
                    f = (float)d1 * f5;
                    Unit unit2 = (Unit)seq1.get(i4);
                    f3 += f1;
                    f += f4;
                    i3 = 1115684864;
                    f5 = 64.0F;
                    Vec2 vec2 = PathSafe.safeNear(unit2, f3, f, f5);
                    Player player = Vars.player;
                    m = unit2.id;
                    int[] arrayOfInt = { m };
                    bool3 = false;
                    unit1 = null;
                    Call.commandUnits(player, arrayOfInt, null, null, vec2, false, false);
                    i4++;
                    continue;
                  } 
                  break;
                } 
              } 
            } 
          } 
          i++;
          continue;
        } 
        break;
      } 
    } 
  }
  
  private static void update() {
    boolean bool = LunariansSettings.enableAutoDefence;
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
            int j = 1067030938;
            f2 = LunariansSettings.actionInterval(1.2F);
            int i = f1 cmp f2;
            if (i >= 0) {
              i = 0;
              f1 = 0.0F;
              player = null;
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
