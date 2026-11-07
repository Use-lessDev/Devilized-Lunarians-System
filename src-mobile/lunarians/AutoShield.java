package lunarians;

import arc.Events;
import arc.math.geom.Vec2;
import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Time;
import lunarians.data.ShieldSlot;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.core.GameState;
import mindustry.entities.EntityGroup;
import mindustry.entities.abilities.Ability;
import mindustry.entities.units.UnitController;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class AutoShield {
  private static final float SCAN = 0.6F;
  
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
  
  public static boolean hasShieldAbility(UnitType paramUnitType) {
    if (paramUnitType != null) {
      Seq seq = paramUnitType.abilities;
      if (seq != null) {
        int i = 0;
        seq = null;
        while (true) {
          Seq seq1 = paramUnitType.abilities;
          int j = seq1.size;
          if (i < j) {
            Ability ability = (Ability)paramUnitType.abilities.get(i);
            boolean bool2 = ability instanceof mindustry.entities.abilities.ForceFieldAbility;
            boolean bool = true;
            if (bool2)
              return bool; 
            bool2 = ability instanceof mindustry.entities.abilities.ShieldArcAbility;
            if (bool2)
              return bool; 
            bool2 = ability instanceof mindustry.entities.abilities.ShieldRegenFieldAbility;
            if (bool2)
              return bool; 
            String str1 = ability.getClass().getSimpleName().toLowerCase();
            String str2 = "shield";
            boolean bool1 = str1.contains(str2);
            if (bool1)
              return bool; 
            i++;
            continue;
          } 
          break;
        } 
      } 
    } 
    return false;
  }
  
  public static void init() {
    boolean bool = Vars.headless;
    if (bool)
      return; 
    EventType.Trigger trigger = EventType.Trigger.update;
    AutoShield$1 autoShield$1 = new AutoShield$1();
    this();
    Events.run(trigger, autoShield$1);
    AutoShield$2 autoShield$2 = new AutoShield$2();
    this();
    Events.on(EventType.WorldLoadEvent.class, autoShield$2);
  }
  
  public static void reset() {
    timer = 0.0F;
    count = 0;
  }
  
  private static void scan() {
    Seq seq = LunariansSettings.shieldSlots;
    boolean bool = seq.isEmpty();
    if (!bool) {
      bool = false;
      count = 0;
      claimed.clear();
      EntityGroup entityGroup = Groups.unit;
      int i = entityGroup.size();
      int j = 0;
      while (true) {
        int k = seq.size;
        if (j < k) {
          ShieldSlot shieldSlot = (ShieldSlot)seq.get(j);
          boolean bool1 = shieldSlot.ready();
          if (bool1) {
            EntityGroup entityGroup1 = Groups.unit;
            int m = shieldSlot.followId;
            Unit unit = (Unit)entityGroup1.getByID(m);
            if (unit != null) {
              boolean bool2 = unit.isValid();
              if (bool2) {
                String str = unit.type.localizedName;
                shieldSlot.followLabel = str;
                Seq seq1 = new Seq();
                this();
                IntSeq intSeq = new IntSeq();
                this();
                int n;
                for (n = 0; n < i; n++) {
                  Unit unit1 = (Unit)Groups.unit.index(n);
                  if (unit1 != null) {
                    boolean bool4 = unit1.isValid();
                    if (bool4) {
                      Team team1 = unit1.team;
                      Team team2 = Vars.player.team();
                      if (team1 == team2) {
                        String str1 = shieldSlot.unit;
                        String str2 = unit1.type.name;
                        bool4 = str1.equals(str2);
                        if (bool4) {
                          UnitController unitController = unit1.controller();
                          bool4 = unitController instanceof CommandAI;
                          if (bool4) {
                            int i1 = unit1.id;
                            int i2 = unit.id;
                            if (i1 != i2) {
                              seq1.add(unit1);
                              UnitCommand unitCommand1 = ((CommandAI)unit1.controller()).command;
                              UnitCommand unitCommand2 = UnitCommand.moveCommand;
                              if (unitCommand1 != unitCommand2) {
                                int i3 = unit1.id;
                                intSeq.add(i3);
                              } 
                            } 
                          } 
                        } 
                      } 
                    } 
                  } 
                } 
                n = shieldSlot.maxUnits;
                if (n > 0) {
                  n = seq1.size;
                  int i1 = shieldSlot.maxUnits;
                  if (n > i1) {
                    k = shieldSlot.maxUnits;
                    seq1.truncate(k);
                  } 
                } 
                k = seq1.size;
                n = 1;
                for (k -= n; k >= 0; k--) {
                  IntSet intSet = claimed;
                  Unit unit1 = (Unit)seq1.get(k);
                  int i1 = unit1.id;
                  boolean bool4 = intSet.contains(i1);
                  if (bool4)
                    seq1.remove(k); 
                } 
                boolean bool3 = seq1.isEmpty();
                if (!bool3) {
                  bool3 = false;
                  shieldSlot = null;
                  while (true) {
                    int i2 = seq1.size;
                    if (bool3 < i2) {
                      IntSet intSet = claimed;
                      Unit unit1 = (Unit)seq1.get(bool3);
                      int i4 = unit1.id;
                      intSet.add(i4);
                      int i3 = bool3 + 1;
                      continue;
                    } 
                    int i1 = intSeq.size;
                    if (i1 > 0) {
                      Player player = Vars.player;
                      int[] arrayOfInt = intSeq.toArray();
                      UnitCommand unitCommand = UnitCommand.moveCommand;
                      Call.setUnitCommand(player, arrayOfInt, unitCommand);
                    } 
                    i1 = 0;
                    shieldSlot = null;
                    while (true) {
                      int i4 = seq1.size;
                      if (i1 < i4) {
                        Unit unit1 = (Unit)seq1.get(i1);
                        i2 = seq1.size;
                        if (i2 <= n) {
                          i2 = 0;
                          f1 = 0.0F;
                          Object object = null;
                        } else {
                          f1 = i1 * 360.0F;
                          float f = seq1.size;
                          f1 /= f;
                          int i5 = 1016003125;
                          f = 0.017453292F;
                          f1 *= f;
                        } 
                        double d1 = f1;
                        double d2 = Math.cos(d1);
                        float f3 = (float)d2;
                        float f4 = 18.0F;
                        f3 *= f4;
                        d1 = Math.sin(d1);
                        float f1 = (float)d1 * f4;
                        float f2 = unit.x + f3;
                        f3 = unit.y + f1;
                        i2 = 1111490560;
                        f1 = 48.0F;
                        Vec2 vec2 = PathSafe.safeNear(unit1, f2, f3, f1);
                        Player player = Vars.player;
                        i4 = unit1.id;
                        int[] arrayOfInt = { i4 };
                        boolean bool4 = true;
                        Call.commandUnits(player, arrayOfInt, null, null, vec2, false, bool4);
                        i1++;
                        continue;
                      } 
                      i1 = count;
                      int i3 = seq1.size;
                      i1 += i3;
                      count = i1;
                      break;
                    } 
                    break;
                  } 
                } 
              } 
            } 
          } 
          j++;
          continue;
        } 
        break;
      } 
    } 
  }
  
  private static void update() {
    boolean bool = LunariansSettings.enableAutoShield;
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
            int j = 1058642330;
            f2 = LunariansSettings.actionInterval(0.6F);
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
