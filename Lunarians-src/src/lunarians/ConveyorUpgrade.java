package lunarians;

import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.util.Log;
import arc.util.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.entities.units.BuildPlan;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Unit;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ConveyorUpgrade.class */
public class ConveyorUpgrade {
    private static boolean registered;
    private static boolean running;
    private static float next;

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ConveyorUpgrade$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            ConveyorUpgrade.$r8$lambda$lph53A4JaiFXFW_CJrOPRYQMDc8();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ConveyorUpgrade$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass2 implements Cons {
        public final void get(Object obj) {
            ConveyorUpgrade.running = false;
        }
    }

    public static boolean enabled() {
        return Core.settings.getBool("lunarians-ti-conv", true);
    }

    public static void setEnabled(boolean z) {
        Core.settings.put("lunarians-ti-conv", Boolean.valueOf(z));
        if (z) {
            return;
        }
        running = false;
    }

    public static void setThreshold(int i) {
        Core.settings.put("lunarians-ti-conv-min", Integer.valueOf(Math.max(200, Math.min(10000, i))));
    }

    public static int threshold() {
        return Math.max(100, Core.settings.getInt("lunarians-ti-conv-min", 2000));
    }

    public static String title() {
        String language = LunariansI18n.getLanguage();
        return "pt".equals(language) ? "Esteira de titanio automatica (2k)" : "es".equals(language) ? "Cinta de titanio automatica (2k)" : "ru".equals(language) ? "Авто-титановый конвейер (2k)" : "Auto titanium conveyor (2k)";
    }

    public static void init() {
        if (registered) {
            return;
        }
        registered = true;
        Events.run(EventType.Trigger.update, ConveyorUpgrade::update);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            running = false;
        });
    }

    private static void update() {
        if (!enabled() || Vars.state == null || !Vars.state.isGame() || Vars.player == null || Time.time < next) {
            return;
        }
        next = Time.time + 180.0f;
        try {
            final Unit unit = Vars.player.unit();
            if (unit == null || unit.dead || !unit.canBuild() || Vars.player.closestCore() == null || AutoSet.isActive() || AutoEconomy.active) {
                return;
            }
            int i = Vars.player.closestCore().items.get(Items.titanium);
            if (!running && i >= threshold()) {
                running = true;
            }
            if (running && i < threshold() / 4) {
                running = false;
            }
            int i2 = Vars.player.closestCore().items.get(Items.copper);
            int i3 = Vars.player.closestCore().items.get(Items.lead);
            if (!running || unit.plans().size > 1 || i2 < 300 || i3 < 300) {
                return;
            }
            ArrayList arrayList = new ArrayList();
            float f = unit.type.buildRange * 0.9f;
            Iterator it = Groups.build.iterator();
            while (it.hasNext()) {
                Building building = (Building) it.next();
                if (building.team == Vars.player.team() && building.block == Blocks.conveyor && building.within(unit, f)) {
                    arrayList.add(building);
                }
            }
            if (arrayList.isEmpty()) {
                return;
            }
            Collections.sort(arrayList, new Comparator<Building>() { // from class: lunarians.ConveyorUpgrade.1
                @Override // java.util.Comparator
                public int compare(Building building2, Building building3) {
                    return Float.compare(building2.dst2(unit), building3.dst2(unit));
                }
            });
            int min = Math.min(8, Math.min(arrayList.size(), (Math.min(i2, i3) - 200) / 2));
            for (int i4 = 0; i4 < min; i4++) {
                Building building2 = (Building) arrayList.get(i4);
                unit.addBuild(new BuildPlan(building2.tileX(), building2.tileY(), building2.rotation, Blocks.titaniumConveyor), true);
            }
        } catch (Throwable th) {
            Log.err("[Lunarians] ConveyorUpgrade", th);
        }
    }
}
