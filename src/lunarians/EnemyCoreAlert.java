/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Events
 *  arc.struct.ObjectSet
 *  arc.util.Time
 *  arc.util.Timer
 *  mindustry.Vars
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.Team
 *  mindustry.gen.Groups
 *  mindustry.gen.Unit
 *  mindustry.world.blocks.storage.CoreBlock$CoreBuild
 */
package lunarians;

import arc.Events;
import arc.struct.ObjectSet;
import arc.util.Time;
import arc.util.Timer;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.world.blocks.storage.CoreBlock;

public class EnemyCoreAlert {
    private static float cooldown = 0.0f;
    private static float scanTimer = 0.0f;
    private static final ObjectSet<Integer> recentlyAlerted = new ObjectSet();

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run((Object)EventType.Trigger.update, (Runnable)new Runnable(){

            @Override
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
        if ((scanTimer += f) < LunariansSettings.actionInterval(0.5f)) {
            return;
        }
        scanTimer = 0.0f;
        if (cooldown > 0.0f || (team = Vars.player.team()) == null || team.cores() == null || team.cores().isEmpty()) {
            return;
        }
        float f2 = LunariansSettings.enemyAlertRange * 8.0f;
        for (CoreBlock.CoreBuild coreBuild : team.cores()) {
            if (coreBuild == null || !coreBuild.isValid()) continue;
            int n = Groups.unit.size();
            for (int i = 0; i < n; ++i) {
                int n2;
                Unit unit = (Unit)Groups.unit.index(i);
                if (unit == null || !unit.isValid() || unit.team == team || !unit.within(coreBuild.x, coreBuild.y, f2) || recentlyAlerted.contains((Object)(n2 = unit.id))) continue;
                Vars.ui.chatfrag.addMessage(LunariansI18n.get("enemy_alert", unit.type != null ? unit.type.localizedName : "Unknown").replace("{1}", (int)(coreBuild.x / 8.0f) + ", " + (int)(coreBuild.y / 8.0f)));
                recentlyAlerted.add((Object)n2);
                cooldown = LunariansSettings.attackMessageDelay;
                Timer.schedule((Runnable)new Runnable(){

                    @Override
                    public final void run() {
                        EnemyCoreAlert.lam_init$0(n2);
                    }
                }, (float)(LunariansSettings.attackMessageDelay + 2.0f));
                return;
            }
        }
    }

    static void lam_init$0(int n) {
        recentlyAlerted.remove((Object)n);
    }
}

