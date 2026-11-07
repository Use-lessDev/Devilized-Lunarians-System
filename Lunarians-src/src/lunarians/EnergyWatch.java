package lunarians;

import arc.Events;
import arc.util.Log;
import arc.util.Time;
import java.util.Iterator;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.world.blocks.power.PowerGenerator;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/EnergyWatch.class */
public class EnergyWatch {
    private static boolean registered;
    private static float next;
    private static float lastWarn = -99999.0f;

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.EnergyWatch$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            EnergyWatch.$r8$lambda$WdqWZ-BuOh9NwH2A0aLxYM4IJ40();
        }
    }

    public static void init() {
        if (registered) {
            return;
        }
        registered = true;
        Events.run(EventType.Trigger.update, EnergyWatch::update);
    }

    private static void update() {
        if (Vars.state == null || !Vars.state.isGame() || Vars.player == null || Time.time < next) {
            return;
        }
        next = Time.time + 600.0f;
        try {
            if (Vars.player.closestCore() == null || Vars.player.closestCore().items.get(Items.coal) >= 50) {
                return;
            }
            Iterator it = Groups.build.iterator();
            while (it.hasNext()) {
                PowerGenerator.GeneratorBuild generatorBuild = (Building) it.next();
                if (((Building) generatorBuild).team == Vars.player.team() && ((Building) generatorBuild).block == Blocks.combustionGenerator) {
                    if ((generatorBuild instanceof PowerGenerator.GeneratorBuild) && generatorBuild.productionEfficiency <= 0.0f) {
                        if (Time.time - lastWarn > 3600.0f && Vars.ui != null) {
                            lastWarn = Time.time;
                            String language = LunariansI18n.getLanguage();
                            Vars.ui.showInfoToast("pt".equals(language) ? "Energia: o núcleo está sem carvão, geradores parados." : "es".equals(language) ? "Energía: el núcleo sin carbón, generadores parados." : "ru".equals(language) ? "Энергия: в ядре нет угля, генераторы стоят." : "Power: core is out of coal, generators idle.", 5.0f);
                            return;
                        }
                        return;
                    }
                }
            }
        } catch (Throwable th) {
            Log.err("[Lunarians] EnergyWatch", th);
        }
    }
}
