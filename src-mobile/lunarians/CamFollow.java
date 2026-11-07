package lunarians;

import arc.Core;
import arc.Events;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.IntSeq;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.Vars;
import mindustry.core.Control;
import mindustry.core.GameState;
import mindustry.core.World;
import mindustry.entities.EntityGroup;
import mindustry.entities.Units;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Entityc;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;
import mindustry.input.InputHandler;

public class CamFollow {
  private static final float AIM_RADIUS_TILES = 5.0F;
  
  private static final float INTERVAL = 0.4F;
  
  private static final float MOVE_REISSUE = 16.0F;
  
  private static final float TIME_REISSUE = 5.0F;
  
  private static float clock;
  
  private static boolean errorLogged;
  
  private static boolean hasLast;
  
  private static final Vec2 lastPos;
  
  private static int lastSig;
  
  private static int lastTargetId;
  
  private static float lastTime;
  
  private static boolean lastWasAttack;
  
  private static final IntSeq selected;
  
  private static long selectedHash;
  
  private static float timer;
  
  private static final IntSeq tmpIds;
  
  static {
    IntSeq intSeq = new IntSeq();
    this();
    selected = intSeq;
    intSeq = new IntSeq();
    this();
    tmpIds = intSeq;
    lastSig = -1 << -1;
    lastTargetId = -1;
    Vec2 vec2 = new Vec2();
    this();
    lastPos = vec2;
    lastTime = -999.0F;
  }
  
  private static void captureSelection() {
    Control control = Vars.control;
    if (control != null) {
      InputHandler inputHandler = Vars.control.input;
      if (inputHandler != null) {
        Seq seq = Vars.control.input.selectedUnits;
        if (seq != null) {
          boolean bool = seq.isEmpty();
          if (!bool) {
            bool = false;
            long l = 0L;
            int i = 0;
            int j = 0;
            while (true) {
              int k = seq.size;
              if (i < k) {
                Unit unit = (Unit)seq.get(i);
                if (unit != null) {
                  boolean bool1 = unit.isValid();
                  if (bool1) {
                    bool1 = unit.isCommandable();
                    if (bool1) {
                      bool1 = unit.isPlayer();
                      if (!bool1) {
                        k = unit.id;
                        long l1 = k + 1L;
                        long l2 = 2654435761L;
                        l1 *= l2;
                        l += l1;
                        j++;
                      } 
                    } 
                  } 
                } 
                i++;
                continue;
              } 
              if (j != 0) {
                long l1 = selectedHash;
                k = l cmp l1;
                if (k != 0) {
                  selectedHash = l;
                  IntSeq intSeq = selected;
                  intSeq.clear();
                  while (true) {
                    int m = seq.size;
                    if (bool < m) {
                      Unit unit = (Unit)seq.get(bool);
                      if (unit != null) {
                        boolean bool1 = unit.isValid();
                        if (bool1) {
                          bool1 = unit.isCommandable();
                          if (bool1) {
                            bool1 = unit.isPlayer();
                            if (!bool1) {
                              IntSeq intSeq1 = selected;
                              m = unit.id;
                              intSeq1.add(m);
                            } 
                          } 
                        } 
                      } 
                      int n = bool + 1;
                      continue;
                    } 
                    resetOrder();
                    break;
                  } 
                } 
              } 
              break;
            } 
          } 
        } 
      } 
    } 
  }
  
  public static int count() {
    return selected.size;
  }
  
  public static void init() {
    boolean bool = Vars.headless;
    if (bool)
      return; 
    EventType.Trigger trigger = EventType.Trigger.update;
    CamFollow$1 camFollow$1 = new CamFollow$1();
    this();
    Events.run(trigger, camFollow$1);
    CamFollow$2 camFollow$2 = new CamFollow$2();
    this();
    Events.on(EventType.WorldLoadEvent.class, camFollow$2);
  }
  
  private static void resetOrder() {
    hasLast = false;
    lastSig = -1 << -1;
    lastTargetId = -1;
  }
  
  private static void run() {
    IntSeq intSeq = tmpIds;
    intSeq.clear();
    boolean bool = false;
    Teamc teamc = null;
    int i = 0;
    while (true) {
      Unit unit;
      int[] arrayOfInt1;
      boolean bool2;
      IntSeq intSeq1 = selected;
      int j = intSeq1.size;
      if (i < j) {
        EntityGroup entityGroup = Groups.unit;
        int n = intSeq1.get(i);
        Entityc entityc = entityGroup.getByID(n);
        if (entityc != null) {
          unit = (Unit)entityc;
          boolean bool3 = unit.isValid();
          if (bool3) {
            Team team1 = unit.team;
            Team team2 = Vars.player.team();
            if (team1 == team2) {
              bool3 = unit.isCommandable();
              if (bool3) {
                n = unit.id;
                intSeq.add(n);
              } 
            } 
          } 
        } 
        i++;
        continue;
      } 
      i = intSeq.size;
      j = ((IntSeq)unit).size;
      if (i != j) {
        unit.clear();
        unit.addAll(intSeq);
      } 
      i = intSeq.size;
      if (i != 0) {
        Unit unit1;
        i = signature(intSeq);
        arrayOfInt1 = intSeq.toArray();
        Player player1 = Vars.player;
        boolean bool4 = player1.dead();
        boolean bool3 = false;
        float f = 0.0F;
        unit = null;
        if (bool4) {
          bool4 = false;
          player1 = null;
          float f6 = 0.0F;
        } else {
          unit1 = Vars.player.unit();
        } 
        bool2 = true;
        if (unit1 != null) {
          boolean bool5 = unit1.isValid();
          if (bool5) {
            bool5 = unit1.isShooting();
            if (bool5) {
              Team team = Vars.player.team();
              float f7 = unit1.aimX();
              float f6 = unit1.aimY();
              float f8 = 40.0F;
              Teamc teamc1 = Units.closestTarget(team, f7, f6, f8);
              if (teamc1 != null) {
                Building building1;
                int n;
                Unit unit2;
                bool = teamc1 instanceof Unit;
                if (bool) {
                  Teamc teamc2 = teamc1;
                  Unit unit3 = (Unit)teamc1;
                  n = unit3.id;
                } else {
                  Teamc teamc2 = teamc1;
                  Building building = (Building)teamc1;
                  n = building.pos();
                } 
                int i1 = n;
                boolean bool6 = hasLast;
                if (bool6) {
                  bool6 = lastWasAttack;
                  if (bool6) {
                    int i2 = lastTargetId;
                    if (i2 == i1) {
                      i2 = lastSig;
                      if (i2 == i) {
                        float f9 = clock;
                        f7 = lastTime;
                        f9 -= f7;
                        int i3 = 1073741824;
                        f7 = 2.0F;
                        i2 = f9 cmp f7;
                        if (i2 <= 0)
                          return; 
                      } 
                    } 
                  } 
                } 
                if (bool) {
                  teamc = teamc1;
                  Unit unit3 = (Unit)teamc1;
                  unit2 = unit3;
                } else {
                  boolean bool7 = false;
                  unit2 = null;
                  f7 = 0.0F;
                } 
                bool = teamc1 instanceof Building;
                if (bool) {
                  Teamc teamc2 = teamc1;
                  building1 = (Building)teamc1;
                } 
                Building building2 = building1;
                Player player2 = Vars.player;
                int[] arrayOfInt = arrayOfInt1;
                arrayOfInt = arrayOfInt1;
                Call.commandUnits(player2, arrayOfInt1, building2, unit2, null, false, true);
                hasLast = bool2;
                lastWasAttack = bool2;
                lastTargetId = i1;
                lastSig = i;
                lastTime = clock;
                return;
              } 
            } 
          } 
        } 
      } else {
        return;
      } 
      Vec2 vec21 = Core.camera.position;
      float f2 = vec21.x;
      float f1 = Vars.world.unitWidth();
      int k = 0;
      float f4 = 0.0F;
      int[] arrayOfInt2 = null;
      f2 = Mathf.clamp(f2, 0.0F, f1);
      Vec2 vec22 = Core.camera.position;
      f1 = vec22.y;
      World world = Vars.world;
      int m = world.unitHeight();
      float f3 = m;
      float f5 = Mathf.clamp(f1, 0.0F, f3);
      boolean bool1 = hasLast;
      if (bool1) {
        bool1 = lastWasAttack;
        if (!bool1) {
          int n = lastSig;
          if (n == i) {
            vec22 = lastPos;
            f1 = vec22.dst(f2, f5);
            k = 1098907648;
            f4 = 16.0F;
            n = f1 cmp f4;
            if (n <= 0) {
              f1 = clock;
              f4 = lastTime;
              f1 -= f4;
              k = 1084227584;
              f4 = 5.0F;
              n = f1 cmp f4;
              if (n <= 0)
                return; 
            } 
          } 
        } 
      } 
      Player player = Vars.player;
      arrayOfInt2 = arrayOfInt1;
      arrayOfInt2 = arrayOfInt1;
      Vec2 vec23 = new Vec2();
      this(f2, f5);
      Call.commandUnits(player, arrayOfInt1, null, null, vec23, false, true);
      hasLast = bool2;
      lastWasAttack = false;
      lastTargetId = -1;
      lastSig = i;
      lastPos.set(f2, f5);
      lastTime = clock;
      return;
    } 
  }
  
  public static void setEnabled(boolean paramBoolean) {
    LunariansSettings.enableCamFollow = paramBoolean;
    LunariansSettings.save();
    resetOrder();
    if (paramBoolean) {
      captureSelection();
      return;
    } 
    selected.clear();
    selectedHash = 0L;
  }
  
  private static int signature(IntSeq paramIntSeq) {
    int i = paramIntSeq.size;
    int j = 0;
    while (true) {
      int k = paramIntSeq.size;
      if (j < k) {
        i *= 31;
        k = paramIntSeq.get(j);
        i += k;
        j++;
        continue;
      } 
      return i;
    } 
  }
  
  private static void update() {
    boolean bool = LunariansSettings.enableCamFollow;
    if (bool) {
      GameState gameState = Vars.state;
      if (gameState != null) {
        gameState = Vars.state;
        bool = gameState.isGame();
        if (bool) {
          gameState = Vars.state;
          bool = gameState.isPaused();
          if (!bool) {
            Player player = Vars.player;
            if (player != null) {
              float f1 = clock;
              float f2 = Time.delta;
              float f3 = 60.0F;
              f2 /= f3;
              clock = f1 + f2;
              captureSelection();
              f1 = timer;
              f2 = Time.delta / f3;
              f1 += f2;
              timer = f1;
              int j = 1053609165;
              f2 = 0.4F;
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
}
