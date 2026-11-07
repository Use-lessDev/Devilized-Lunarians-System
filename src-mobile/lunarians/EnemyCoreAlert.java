package lunarians;

import arc.Events;
import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.Timer;
import java.util.Iterator;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.entities.EntityGroup;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.ui.fragments.ChatFragment;
import mindustry.world.blocks.storage.CoreBlock;

public class EnemyCoreAlert {
  private static float cooldown;
  
  private static final ObjectSet recentlyAlerted;
  
  private static float scanTimer;
  
  static {
    ObjectSet objectSet = new ObjectSet();
    this();
    recentlyAlerted = objectSet;
  }
  
  public static void init() {
    boolean bool = Vars.headless;
    if (bool)
      return; 
    EventType.Trigger trigger = EventType.Trigger.update;
    EnemyCoreAlert$1 enemyCoreAlert$1 = new EnemyCoreAlert$1();
    this();
    Events.run(trigger, enemyCoreAlert$1);
  }
  
  static void lam_init$0(int paramInt) {
    ObjectSet objectSet = recentlyAlerted;
    Integer integer = Integer.valueOf(paramInt);
    objectSet.remove(integer);
  }
  
  static void lam_init$1() {
    boolean bool = LunariansSettings.enableEnemyCoreAlert;
    if (bool) {
      GameState gameState = Vars.state;
      bool = gameState.isGame();
      if (bool) {
        Player player = Vars.player;
        if (player != null) {
          float f1 = Time.delta;
          int j = 1114636288;
          f1 /= 60.0F;
          cooldown -= f1;
          float f2 = scanTimer + f1;
          scanTimer = f2;
          f1 = LunariansSettings.actionInterval(0.5F);
          int i = f2 cmp f1;
          if (i >= 0) {
            f1 = 0.0F;
            player = null;
            scanTimer = 0.0F;
            f2 = cooldown;
            i = f2 cmp 0.0F;
            if (i <= 0) {
              Team team = Vars.player.team();
              if (team != null) {
                Seq seq = team.cores();
                if (seq != null) {
                  seq = team.cores();
                  boolean bool1 = seq.isEmpty();
                  if (!bool1) {
                    f2 = LunariansSettings.enemyAlertRange;
                    int k = 1090519040;
                    float f = 8.0F;
                    f2 *= f;
                    Iterator<CoreBlock.CoreBuild> iterator = team.cores().iterator();
                    while (true) {
                      boolean bool2 = iterator.hasNext();
                      if (bool2) {
                        CoreBlock.CoreBuild coreBuild = iterator.next();
                        if (coreBuild != null) {
                          boolean bool3 = coreBuild.isValid();
                          if (bool3) {
                            EntityGroup entityGroup = Groups.unit;
                            int m = entityGroup.size();
                            int n;
                            for (n = 0; n < m; n++) {
                              Unit unit = (Unit)Groups.unit.index(n);
                              if (unit != null) {
                                boolean bool4 = unit.isValid();
                                if (bool4) {
                                  Team team1 = unit.team;
                                  if (team1 != team) {
                                    float f3 = coreBuild.x;
                                    float f4 = coreBuild.y;
                                    bool4 = unit.within(f3, f4, f2);
                                    if (bool4) {
                                      ObjectSet objectSet = recentlyAlerted;
                                      int i1 = unit.id;
                                      Integer integer = Integer.valueOf(i1);
                                      boolean bool5 = objectSet.contains(integer);
                                      if (!bool5) {
                                        ChatFragment chatFragment = Vars.ui.chatfrag;
                                        UnitType unitType = unit.type;
                                        if (unitType != null) {
                                          str1 = unit.type.localizedName;
                                        } else {
                                          str1 = "Unknown";
                                        } 
                                        String str1 = LunariansI18n.get("enemy_alert", str1);
                                        StringBuilder stringBuilder = new StringBuilder();
                                        this();
                                        m = (int)(coreBuild.x / f);
                                        stringBuilder.append(m);
                                        stringBuilder.append(", ");
                                        k = (int)(coreBuild.y / f);
                                        stringBuilder.append(k);
                                        String str2 = stringBuilder.toString();
                                        str1 = str1.replace("{1}", str2);
                                        chatFragment.addMessage(str1);
                                        Integer integer1 = Integer.valueOf(i1);
                                        objectSet.add(integer1);
                                        cooldown = LunariansSettings.attackMessageDelay;
                                        EnemyCoreAlert$2 enemyCoreAlert$2 = new EnemyCoreAlert$2();
                                        this(i1);
                                        f2 = LunariansSettings.attackMessageDelay + 2.0F;
                                        Timer.schedule(enemyCoreAlert$2, f2);
                                        return;
                                      } 
                                    } 
                                  } 
                                } 
                              } 
                            } 
                          } 
                        } 
                        continue;
                      } 
                      break;
                    } 
                  } 
                } 
              } 
            } 
          } 
        } 
      } 
    } 
  }
}
