package lunarians;

import arc.Events;
import arc.struct.ObjectSet;
import arc.util.Time;
import arc.util.Timer;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.world.blocks.storage.CoreBlock;

public class EnemyCoreAlert {
  private static float cooldown = 0.0F;
  
  private static float scanTimer = 0.0F;
  
  private static final ObjectSet<Integer> recentlyAlerted = new ObjectSet();
  
  public static void init() {
    if (Vars.headless)
      return; 
    Events.run(EventType.Trigger.update, new Runnable() {
          public final void run() {
            EnemyCoreAlert.lam_init$1();
          }
        });
  }
  
  static void lam_init$1() {
    if (!LunariansSettings.enableEnemyCoreAlert || !Vars.state.isGame() || Vars.player == null)
      return; 
    float f1 = Time.delta / 60.0F;
    cooldown -= f1;
    scanTimer += f1;
    if (scanTimer < LunariansSettings.actionInterval(0.5F))
      return; 
    scanTimer = 0.0F;
    Team team;
    if (cooldown > 0.0F || (team = Vars.player.team()) == null || team.cores() == null || team.cores().isEmpty())
      return; 
    float f2 = LunariansSettings.enemyAlertRange * 8.0F;
    for (CoreBlock.CoreBuild coreBuild : team.cores()) {
      if (coreBuild == null || !coreBuild.isValid())
        continue; 
      int i = Groups.unit.size();
      for (byte b = 0; b < i; ) {
        Unit unit = (Unit)Groups.unit.index(b);
        final int n2;
        if (unit == null || !unit.isValid() || unit.team == team || !unit.within(coreBuild.x, coreBuild.y, f2) || recentlyAlerted.contains(Integer.valueOf(j = unit.id))) {
          b++;
          continue;
        } 
        Vars.ui.chatfrag.addMessage(LunariansI18n.get("enemy_alert", (unit.type != null) ? unit.type.localizedName : "Unknown").replace("{1}", (int)(coreBuild.x / 8.0F) + ", " + (int)(coreBuild.y / 8.0F)));
        recentlyAlerted.add(Integer.valueOf(j));
        cooldown = LunariansSettings.attackMessageDelay;
        Timer.schedule(new Runnable() {
              public final void run() {
                EnemyCoreAlert.lam_init$0(n2);
              }
            },  LunariansSettings.attackMessageDelay + 2.0F);
        return;
      } 
    } 
  }
  
  static void lam_init$0(int paramInt) {
    recentlyAlerted.remove(Integer.valueOf(paramInt));
  }
}
