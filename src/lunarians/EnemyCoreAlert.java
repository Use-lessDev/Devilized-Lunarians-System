package lunarians;

import arc.Events;
import arc.struct.ObjectSet;
import arc.util.Time;
import arc.util.Timer;
import java.util.Iterator;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.world.blocks.storage.CoreBlock;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/EnemyCoreAlert.class */
public class EnemyCoreAlert {
    private static float cooldown = 0.0f;
    private static float scanTimer = 0.0f;
    private static final ObjectSet<Integer> recentlyAlerted = new ObjectSet<>();

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run(EventType.Trigger.update, new Runnable() { // from class: lunarians.EnemyCoreAlert.1
            @Override // java.lang.Runnable
            public final void run() {
                EnemyCoreAlert.lam_init$1();
            }
        });
    }

    static void lam_init$1() {
        Team team;
        if (!LunariansSettings.enableEnemyCoreAlert || !Vars.state.isGame() || Vars.player == null) {
            return;
        }
        float f = Time.delta / 60.0f;
        cooldown -= f;
        scanTimer += f;
        if (scanTimer < LunariansSettings.actionInterval(0.5f)) {
            return;
        }
        scanTimer = 0.0f;
        if (cooldown > 0.0f || (team = Vars.player.team()) == null || team.cores() == null || team.cores().isEmpty()) {
            return;
        }
        float f2 = LunariansSettings.enemyAlertRange * 8.0f;
        Iterator it = team.cores().iterator();
        while (it.hasNext()) {
            CoreBlock.CoreBuild coreBuild = (CoreBlock.CoreBuild) it.next();
            if (coreBuild != null && coreBuild.isValid()) {
                int size = Groups.unit.size();
                for (int i = 0; i < size; i++) {
                    Unit index = Groups.unit.index(i);
                    if (index != null && index.isValid() && index.team != team && index.within(coreBuild.x, coreBuild.y, f2)) {
                        ObjectSet<Integer> objectSet = recentlyAlerted;
                        final int i2 = index.id;
                        if (!objectSet.contains(Integer.valueOf(i2))) {
                            Vars.ui.chatfrag.addMessage(LunariansI18n.get("enemy_alert", index.type != null ? index.type.localizedName : "Unknown").replace("{1}", ((int) (coreBuild.x / 8.0f)) + ", " + ((int) (coreBuild.y / 8.0f))));
                            recentlyAlerted.add(Integer.valueOf(i2));
                            cooldown = LunariansSettings.attackMessageDelay;
                            Timer.schedule(new Runnable() { // from class: lunarians.EnemyCoreAlert.2
                                @Override // java.lang.Runnable
                                public final void run() {
                                    EnemyCoreAlert.lam_init$0(i2);
                                }
                            }, LunariansSettings.attackMessageDelay + 2.0f);
                            return;
                        }
                    }
                }
            }
        }
    }

    static void lam_init$0(int i) {
        recentlyAlerted.remove(Integer.valueOf(i));
    }
}
