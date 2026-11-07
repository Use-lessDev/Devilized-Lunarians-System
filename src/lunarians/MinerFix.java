package lunarians;

import arc.Events;
import arc.func.Cons;
import arc.util.Log;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.content.UnitTypes;
import mindustry.game.EventType;
import mindustry.type.Item;
import mindustry.type.UnitType;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/MinerFix.class */
public class MinerFix {
    private static Boolean savedAutoAttack = null;

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.MinerFix$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Cons {
        public final void get(Object obj) {
            MinerFix.lambda$init$0((EventType.WorldLoadEvent) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.MinerFix$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass1 implements Cons {
        public final void get(Object obj) {
            MinerFix.restoreAutoAttack();
        }
    }

    public static void init() {
        apply();
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            apply();
            pvpGuard();
        });
        Events.on(EventType.ResetEvent.class, resetEvent -> {
            restoreAutoAttack();
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void restoreAutoAttack() {
        if (savedAutoAttack != null) {
            LunariansSettings.enableAutoAttack = savedAutoAttack.booleanValue();
            savedAutoAttack = null;
        }
    }

    private static void pvpGuard() {
        try {
            if ((Vars.state == null || Vars.state.rules == null || !Vars.state.rules.pvp) ? false : true) {
                if (savedAutoAttack == null) {
                    savedAutoAttack = Boolean.valueOf(LunariansSettings.enableAutoAttack);
                }
                LunariansSettings.enableAutoAttack = false;
            } else if (savedAutoAttack != null) {
                LunariansSettings.enableAutoAttack = savedAutoAttack.booleanValue();
                savedAutoAttack = null;
            }
        } catch (Throwable th) {
            Log.err("[Lunarians] pvpGuard falhou", th);
        }
    }

    public static void apply() {
        allow(UnitTypes.mono, Items.sand);
        allow(UnitTypes.poly, Items.sand, Items.coal);
        allow(UnitTypes.mega, Items.sand, Items.coal, Items.titanium);
    }

    private static void allow(UnitType unitType, Item... itemArr) {
        if (unitType == null) {
            return;
        }
        try {
            if (unitType.mineItems == null) {
                return;
            }
            for (Item item : itemArr) {
                if (item.hardness <= unitType.mineTier && !unitType.mineItems.contains(item)) {
                    unitType.mineItems.add(item);
                }
            }
        } catch (Throwable th) {
            Log.err("[Lunarians] MinerFix falhou em " + unitType.name, th);
        }
    }
}
