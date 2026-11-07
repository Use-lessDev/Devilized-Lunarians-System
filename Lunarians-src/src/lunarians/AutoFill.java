package lunarians;

import arc.Events;
import arc.func.Boolf;
import arc.func.Cons;
import arc.struct.Seq;
import arc.util.Time;
import java.util.Iterator;
import java.util.Objects;
import mindustry.Vars;
import mindustry.ai.BlockIndexer;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.world.blocks.storage.CoreBlock;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoFill.class */
public class AutoFill {
    private static float timer;
    private static boolean pending;
    private static float pendingTimer;
    private static Item pendingItem;
    private static int previousAmount;

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoFill$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Boolf {
        public final boolean get(Object obj) {
            return AutoFill.lambda$update$1((Building) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoFill$10, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass10 implements Cons {
        public final void get(Object obj) {
            AutoFill.lambda$init$0((EventType.WorldLoadEvent) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoFill$11, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass11 implements Boolf {
        public final boolean get(Object obj) {
            return AutoFill.lambda$findDeliveryTarget$2((Building) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoFill$8, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass8 implements Cons {
        public final /* synthetic */ Seq f$0;

        public final void get(Object obj) {
            this.f$0.add((Building) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoFill$9, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass9 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoFill.update();
        }
    }

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run(EventType.Trigger.update, AutoFill::update);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            timer = 0.0f;
            pending = false;
            pendingItem = null;
            pendingTimer = 0.0f;
            previousAmount = 0;
        });
    }

    /* JADX WARN: Classes with same name are omitted:
      classes.dex
     */
    /* renamed from: lunarians.AutoFill$1, reason: invalid class name */
    /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoFill$1.class */
    class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            AutoFill.update();
        }
    }

    public static void update() {
        int i;
        int acceptStack;
        if (!LunariansSettings.enableAutoFill || Vars.state == null || !Vars.state.isGame() || Vars.player == null || Vars.player.unit() == null || Vars.player.dead()) {
            return;
        }
        Unit unit = Vars.player.unit();
        Player player = Vars.player;
        ItemStack itemStack = unit.stack;
        if (pending) {
            pendingTimer += Time.delta / 60.0f;
            boolean z = (itemStack == null || itemStack.amount == previousAmount) ? false : true;
            boolean z2 = itemStack != null && itemStack.item == pendingItem && itemStack.amount > 0;
            if (z || z2 || pendingTimer >= 1.25f) {
                pending = false;
                pendingItem = null;
                pendingTimer = 0.0f;
            } else {
                timer = 0.0f;
                return;
            }
        }
        timer += Time.delta / 60.0f;
        if (timer < LunariansSettings.actionInterval(Math.max(0.05f, LunariansSettings.autoFillInterval))) {
            return;
        }
        timer = 0.0f;
        float min = Math.min(Math.max(1.0f, LunariansSettings.autoFillRange) * 8.0f, 220.0f);
        if (min <= 0.0f) {
            return;
        }
        if (itemStack != null && itemStack.item != null && itemStack.amount > 0) {
            Building findDeliveryTarget = findDeliveryTarget(player, unit, itemStack.item, itemStack.amount, min);
            if (findDeliveryTarget != null && (acceptStack = findDeliveryTarget.acceptStack(itemStack.item, itemStack.amount, unit)) > 0) {
                previousAmount = itemStack.amount;
                pendingItem = itemStack.item;
                pending = true;
                pendingTimer = 0.0f;
                Call.transferItemTo(unit, itemStack.item, acceptStack, unit.x, unit.y, findDeliveryTarget);
                return;
            }
            return;
        }
        Building building = null;
        Item item = null;
        int i2 = 0;
        float f = Float.MAX_VALUE;
        Seq seq = new Seq();
        BlockIndexer blockIndexer = Vars.indexer;
        Team team = player.team();
        float f2 = unit.x;
        float f3 = unit.y;
        Boolf boolf = building2 -> {
            return (building2 == null || building2.items == null) ? false : true;
        };
        Objects.requireNonNull(seq);
        blockIndexer.eachBlock(team, f2, f3, min, boolf, (v1) -> {
            r6.add(v1);
        });
        Iterator it = seq.iterator();
        while (it.hasNext()) {
            Building building3 = (Building) it.next();
            if (building3.items != null && !building3.items.empty()) {
                boolean z3 = building3.block instanceof CoreBlock;
                boolean z4 = building3.block != null && (building3.block.name.contains("container") || building3.block.name.contains("vault") || building3.block.name.contains("core") || building3.block.name.contains("storage"));
                if (z3 || z4) {
                    Iterator it2 = Vars.content.items().iterator();
                    while (it2.hasNext()) {
                        Item item2 = (Item) it2.next();
                        if (item2 != null && (i = building3.items.get(item2)) > 0) {
                            float dst = unit.dst(building3);
                            if (dst < f) {
                                f = dst;
                                building = building3;
                                item = item2;
                                i2 = Math.min(i, unit.itemCapacity() - (unit.stack.amount > 0 ? unit.stack.amount : 0));
                                if (i2 <= 0) {
                                    i2 = Math.min(i, unit.itemCapacity());
                                }
                            }
                        }
                    }
                }
            }
        }
        if (building != null && item != null && i2 > 0) {
            previousAmount = 0;
            pendingItem = item;
            pending = true;
            pendingTimer = 0.0f;
            Call.takeItems(building, item, i2, unit);
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      classes.dex
     */
    /* renamed from: lunarians.AutoFill$2, reason: invalid class name */
    /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoFill$2.class */
    class AnonymousClass2 implements Boolf {
        final /* synthetic */ Player val$player;

        AnonymousClass2(Player player) {
            this.val$player = player;
        }

        public final boolean get(Object obj) {
            return AutoFill.isPotentialTarget(this.val$player, (Building) obj);
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      classes.dex
     */
    /* renamed from: lunarians.AutoFill$3, reason: invalid class name */
    /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoFill$3.class */
    class AnonymousClass3 implements Cons {
        final /* synthetic */ Building[] val$buildingArray;
        final /* synthetic */ Unit val$unit;
        final /* synthetic */ Player val$player;
        final /* synthetic */ float val$f2;
        final /* synthetic */ Building[] val$buildingArray2;
        final /* synthetic */ Item[] val$itemArray;
        final /* synthetic */ int[] val$nArray;

        AnonymousClass3(Building[] buildingArr, Unit unit, Player player, float f, Building[] buildingArr2, Item[] itemArr, int[] iArr) {
            this.val$buildingArray = buildingArr;
            this.val$unit = unit;
            this.val$player = player;
            this.val$f2 = f;
            this.val$buildingArray2 = buildingArr2;
            this.val$itemArray = itemArr;
            this.val$nArray = iArr;
        }

        public final void get(Object obj) {
            AutoFill.lam_update$1(this.val$buildingArray, this.val$unit, this.val$player, this.val$f2, this.val$buildingArray2, this.val$itemArray, this.val$nArray, (Building) obj);
        }
    }

    private static Building findDeliveryTarget(Player player, Unit unit, Item item, int i, float f) {
        Building building = null;
        float f2 = Float.MAX_VALUE;
        Seq seq = new Seq();
        BlockIndexer blockIndexer = Vars.indexer;
        Team team = player.team();
        float f3 = unit.x;
        float f4 = unit.y;
        Boolf boolf = building2 -> {
            return building2 != null;
        };
        Objects.requireNonNull(seq);
        blockIndexer.eachBlock(team, f3, f4, f, boolf, (v1) -> {
            r6.add(v1);
        });
        Iterator it = seq.iterator();
        while (it.hasNext()) {
            Building building3 = (Building) it.next();
            if (!(building3.block instanceof CoreBlock)) {
                String str = building3.block != null ? building3.block.name : "";
                boolean z = str.contains("turret") || str.contains("duo") || str.contains("scatter") || str.contains("salvo") || str.contains("fuse") || str.contains("ripple") || str.contains("cyclone") || str.contains("swarmer") || str.contains("spectre") || str.contains("meltdown") || str.contains("foreshadow") || str.contains("lancer") || str.contains("arc") || str.contains("parallax") || str.contains("segment");
                boolean z2 = str.contains("factory") || str.contains("smelter") || str.contains("kiln") || str.contains("compressor") || str.contains("pulverizer") || str.contains("weaver") || str.contains("multi") || str.contains("cultivator") || str.contains("mixer") || str.contains("cryofluid") || str.contains("separator") || str.contains("disassembler") || str.contains("surge") || str.contains("phase") || str.contains("silicon") || str.contains("graphite");
                if ((LunariansSettings.autoFillTurrets && z) || ((LunariansSettings.autoFillFactories && z2) || (!str.contains("container") && !str.contains("vault") && !str.contains("core") && building3.acceptStack(item, 1, unit) > 0))) {
                    if (building3.acceptStack(item, i, unit) > 0) {
                        float dst = unit.dst(building3);
                        if (dst < f2) {
                            f2 = dst;
                            building = building3;
                        }
                    }
                }
            }
        }
        return building;
    }

    /* JADX WARN: Classes with same name are omitted:
      classes.dex
     */
    /* renamed from: lunarians.AutoFill$4, reason: invalid class name */
    /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoFill$4.class */
    class AnonymousClass4 implements Boolf {
        final /* synthetic */ Player val$player;
        final /* synthetic */ Item val$item;
        final /* synthetic */ Unit val$unit;
        final /* synthetic */ int val$n;

        AnonymousClass4(Player player, Item item, Unit unit, int i) {
            this.val$player = player;
            this.val$item = item;
            this.val$unit = unit;
            this.val$n = i;
        }

        public final boolean get(Object obj) {
            return AutoFill.lam_findDeliveryTarget$2(this.val$player, this.val$item, this.val$unit, this.val$n, (Building) obj);
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      classes.dex
     */
    /* renamed from: lunarians.AutoFill$5, reason: invalid class name */
    /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoFill$5.class */
    class AnonymousClass5 implements Cons {
        final /* synthetic */ Unit val$unit;
        final /* synthetic */ float[] val$fArray;
        final /* synthetic */ Building[] val$buildingArray;

        AnonymousClass5(Unit unit, float[] fArr, Building[] buildingArr) {
            this.val$unit = unit;
            this.val$fArray = fArr;
            this.val$buildingArray = buildingArr;
        }

        public final void get(Object obj) {
            AutoFill.lam_findDeliveryTarget$3(this.val$unit, this.val$fArray, this.val$buildingArray, (Building) obj);
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      classes.dex
     */
    /* renamed from: lunarians.AutoFill$6, reason: invalid class name */
    /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoFill$6.class */
    class AnonymousClass6 implements Boolf {
        final /* synthetic */ Player val$player;
        final /* synthetic */ Item val$item;

        AnonymousClass6(Player player, Item item) {
            this.val$player = player;
            this.val$item = item;
        }

        public final boolean get(Object obj) {
            return AutoFill.lam_findSource$4(this.val$player, this.val$item, (Building) obj);
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      classes.dex
     */
    /* renamed from: lunarians.AutoFill$7, reason: invalid class name */
    /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoFill$7.class */
    class AnonymousClass7 implements Cons {
        final /* synthetic */ Unit val$unit;
        final /* synthetic */ float[] val$fArray;
        final /* synthetic */ Building[] val$buildingArray;

        AnonymousClass7(Unit unit, float[] fArr, Building[] buildingArr) {
            this.val$unit = unit;
            this.val$fArray = fArr;
            this.val$buildingArray = buildingArr;
        }

        public final void get(Object obj) {
            AutoFill.lam_findSource$5(this.val$unit, this.val$fArray, this.val$buildingArray, (Building) obj);
        }
    }
}
