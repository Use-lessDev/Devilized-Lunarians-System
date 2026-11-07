package lunarians;

import arc.Events;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.IntIntMap;
import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Time;
import lunarians.data.CargoSlot;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
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

public class AutoCargo {
  private static final IntSet applied;
  
  private static int count;
  
  private static boolean errorLogged;
  
  private static float timer;
  
  private static final IntIntMap wpIndex;
  
  static {
    IntSet intSet = new IntSet();
    this();
    applied = intSet;
    IntIntMap intIntMap = new IntIntMap();
    this();
    wpIndex = intIntMap;
  }
  
  private static void apply(CargoSlot paramCargoSlot, Seq paramSeq) {
    CargoSlot cargoSlot = paramCargoSlot;
    Seq seq = paramSeq;
    IntSeq intSeq = new IntSeq();
    this();
    int i = 0;
    float f = 0.0F;
    int j = 0;
    Player player = null;
    while (true) {
      int k = seq.size;
      if (j < k) {
        Unit unit = (Unit)seq.get(j);
        CommandAI commandAI = (CommandAI)unit.controller();
        try {
          UnitCommand unitCommand = UnitCommand.loopPayloadCommand;
          if (unitCommand != null) {
            UnitCommand unitCommand1 = commandAI.command;
            unitCommand = UnitCommand.loopPayloadCommand;
            if (unitCommand1 != unitCommand) {
              k = unit.id;
              intSeq.add(k);
            } 
          } 
        } finally {}
        j++;
        continue;
      } 
      j = intSeq.size;
      if (j > 0)
        try {
          player = Vars.player;
          int[] arrayOfInt = intSeq.toArray();
          UnitCommand unitCommand = UnitCommand.loopPayloadCommand;
        } finally {
          null;
        }  
      int m = cargoSlot.pointCount();
      j = 2;
      if (m >= j) {
        j = seq.size;
        k = 0;
        Object object = null;
        while (k < j) {
          Unit unit = (Unit)seq.get(k);
          IntIntMap intIntMap = wpIndex;
          int n = unit.id;
          n = intIntMap.get(n, 0);
          if (n < 0 || n >= m)
            n = 0; 
          byte b = 1;
          float f1 = Float.MIN_VALUE;
          if (j <= b) {
            b = 0;
            f1 = 0.0F;
          } else {
            f1 = k * 360.0F;
            float f8 = j;
            f1 /= f8;
            f8 = 0.017453292F;
            f1 *= f8;
          } 
          float f2 = Mathf.cos(f1);
          float f3 = 14.0F;
          f2 *= f3;
          float f4 = (unit.id % 7) * 1.5F;
          f2 += f4;
          f1 = Mathf.sin(f1) * f3;
          f3 = (unit.id % 5) * 1.2F;
          f1 += f3;
          int i1 = cargoSlot.px(n);
          f3 = i1;
          f4 = 8.0F;
          f3 *= f4;
          float f5 = 4.0F;
          f3 = f3 + f5 + f2;
          float f6 = cargoSlot.py(n) * f4 + f5 + f1;
          float f7 = 48.0F;
          Vec2 vec2 = PathSafe.safeNear(unit, f3, f6, f7);
          f6 = vec2.x;
          f = vec2.y;
          f = unit.dst(f6, f);
          f6 = 36.0F;
          i = f cmp f6;
          if (i <= 0) {
            n = (n + 1) % m;
            i = unit.id;
            intIntMap.put(i, n);
            i = cargoSlot.px(n);
            f = i * f4 + f5 + f2;
            int i2 = cargoSlot.py(n);
            float f8 = i2 * f4 + f5 + f1;
            vec2 = PathSafe.safeNear(unit, f, f8, f7);
          } else {
            i = unit.id;
            intIntMap.put(i, n);
          } 
          Player player1 = Vars.player;
          i = unit.id;
          int[] arrayOfInt = { i };
          boolean bool = true;
          Call.commandUnits(player1, arrayOfInt, null, null, vec2, false, bool);
          k++;
          i = 0;
          f = 0.0F;
        } 
      } 
      return;
    } 
  }
  
  public static int count() {
    return count;
  }
  
  public static void init() {
    boolean bool = Vars.headless;
    if (bool)
      return; 
    EventType.Trigger trigger = EventType.Trigger.update;
    AutoCargo$0 autoCargo$0 = new AutoCargo$0();
    this();
    Events.run(trigger, autoCargo$0);
    AutoCargo$3 autoCargo$3 = new AutoCargo$3();
    this();
    Events.on(EventType.WorldLoadEvent.class, autoCargo$3);
  }
  
  public static void reset() {
    timer = 0.0F;
    count = 0;
    applied.clear();
    wpIndex.clear();
  }
  
  private static void scan() {
    Seq seq = LunariansSettings.cargoSlots;
    if (seq != null) {
      boolean bool = seq.isEmpty();
      if (!bool) {
        applied.clear();
        bool = false;
        count = 0;
        int i = 0;
        while (true) {
          int j = seq.size;
          if (i < j) {
            CargoSlot cargoSlot = (CargoSlot)seq.get(i);
            if (cargoSlot != null) {
              boolean bool1 = cargoSlot.ready();
              if (bool1) {
                Seq seq1 = new Seq();
                this();
                EntityGroup entityGroup = Groups.unit;
                int k = entityGroup.size();
                int m = 0;
                IntSet intSet = null;
                while (m < k) {
                  Unit unit = (Unit)Groups.unit.index(m);
                  if (unit != null) {
                    boolean bool2 = unit.isValid();
                    if (bool2) {
                      Team team1 = unit.team;
                      Team team2 = Vars.player.team();
                      if (team1 == team2) {
                        IntSet intSet1 = applied;
                        int n = unit.id;
                        bool2 = intSet1.contains(n);
                        if (!bool2) {
                          bool2 = unit instanceof mindustry.gen.Payloadc;
                          if (bool2) {
                            UnitController unitController = unit.controller();
                            bool2 = unitController instanceof CommandAI;
                            if (bool2) {
                              UnitType unitType = unit.type;
                              if (unitType != null) {
                                String str1 = cargoSlot.unit;
                                String str2 = unit.type.name;
                                bool2 = str1.equals(str2);
                                if (bool2)
                                  seq1.add(unit); 
                              } 
                            } 
                          } 
                        } 
                      } 
                    } 
                  } 
                  m++;
                } 
                k = cargoSlot.maxUnits;
                if (k > 0) {
                  k = seq1.size;
                  m = cargoSlot.maxUnits;
                  if (k > m) {
                    k = cargoSlot.maxUnits;
                    seq1.truncate(k);
                  } 
                } 
                k = 0;
                entityGroup = null;
                while (true) {
                  m = seq1.size;
                  if (k < m) {
                    intSet = applied;
                    Unit unit = (Unit)seq1.get(k);
                    int n = unit.id;
                    intSet.add(n);
                    k++;
                    continue;
                  } 
                  boolean bool2 = seq1.isEmpty();
                  if (!bool2) {
                    apply(cargoSlot, seq1);
                    j = count;
                    int n = seq1.size;
                    j += n;
                    count = j;
                  } 
                  break;
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
  }
  
  private static void update() {
    boolean bool = LunariansSettings.enableAutoCargo;
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
