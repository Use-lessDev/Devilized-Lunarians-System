/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Events
 *  arc.math.geom.Position
 *  arc.struct.Seq
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.EventType$WorldLoadEvent
 *  mindustry.gen.Building
 *  mindustry.gen.Call
 *  mindustry.gen.Player
 *  mindustry.gen.Teamc
 *  mindustry.gen.Unit
 *  mindustry.type.Item
 *  mindustry.type.ItemStack
 *  mindustry.world.blocks.storage.CoreBlock
 */
package lunarians;

import arc.Events;
import arc.math.geom.Position;
import arc.struct.Seq;
import arc.util.Time;
import lunarians.LunariansSettings;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.world.blocks.storage.CoreBlock;

public class AutoFill {
    private static float timer;
    private static boolean pending;
    private static float pendingTimer;
    private static Item pendingItem;
    private static int previousAmount;

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run((Object)EventType.Trigger.update, AutoFill::update);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            timer = 0.0f;
            pending = false;
            pendingItem = null;
            pendingTimer = 0.0f;
            previousAmount = 0;
        });
    }

    public static void update() {
        if (!LunariansSettings.enableAutoFill || Vars.state == null || !Vars.state.isGame() || Vars.player == null || Vars.player.unit() == null || Vars.player.dead()) {
            return;
        }
        Unit unit = Vars.player.unit();
        Player player = Vars.player;
        ItemStack itemStack = unit.stack;
        if (pending) {
            boolean bl;
            pendingTimer += Time.delta / 60.0f;
            boolean bl2 = itemStack != null && itemStack.amount != previousAmount;
            boolean bl3 = bl = itemStack != null && itemStack.item == pendingItem && itemStack.amount > 0;
            if (bl2 || bl || pendingTimer >= 1.25f) {
                pending = false;
                pendingItem = null;
                pendingTimer = 0.0f;
            } else {
                timer = 0.0f;
                return;
            }
        }
        if ((timer += Time.delta / 60.0f) < LunariansSettings.actionInterval(Math.max(0.05f, LunariansSettings.autoFillInterval))) {
            return;
        }
        timer = 0.0f;
        float f = Math.min(Math.max(1.0f, LunariansSettings.autoFillRange) * 8.0f, 220.0f);
        if (f <= 0.0f) {
            return;
        }
        if (itemStack != null && itemStack.item != null && itemStack.amount > 0) {
            int n;
            Building building2 = AutoFill.findDeliveryTarget(player, unit, itemStack.item, itemStack.amount, f);
            if (building2 != null && (n = building2.acceptStack(itemStack.item, itemStack.amount, (Teamc)unit)) > 0) {
                previousAmount = itemStack.amount;
                pendingItem = itemStack.item;
                pending = true;
                pendingTimer = 0.0f;
                Call.transferItemTo((Unit)unit, (Item)itemStack.item, (int)n, (float)unit.x, (float)unit.y, (Building)building2);
                return;
            }
            return;
        }
        Building building3 = null;
        Item item = null;
        int n = 0;
        float f2 = Float.MAX_VALUE;
        Seq seq = new Seq();
        Vars.indexer.eachBlock(player.team(), unit.x, unit.y, f, building -> building != null && building.items != null, arg_0 -> ((Seq)seq).add(arg_0));
        for (Building building4 : seq) {
            boolean bl;
            if (building4.items == null || building4.items.empty()) continue;
            boolean bl4 = building4.block instanceof CoreBlock;
            boolean bl5 = bl = building4.block != null && (building4.block.name.contains("container") || building4.block.name.contains("vault") || building4.block.name.contains("core") || building4.block.name.contains("storage"));
            if (!bl4 && !bl) continue;
            for (Item item2 : Vars.content.items()) {
                float f3;
                int n2;
                if (item2 == null || (n2 = building4.items.get(item2)) <= 0 || !((f3 = unit.dst((Position)building4)) < f2)) continue;
                f2 = f3;
                building3 = building4;
                item = item2;
                n = Math.min(n2, unit.itemCapacity() - (unit.stack.amount > 0 ? unit.stack.amount : 0));
                if (n > 0) continue;
                n = Math.min(n2, unit.itemCapacity());
            }
        }
        if (building3 != null && item != null && n > 0) {
            previousAmount = 0;
            pendingItem = item;
            pending = true;
            pendingTimer = 0.0f;
            Call.takeItems(building3, item, (int)n, (Unit)unit);
        }
    }

    private static Building findDeliveryTarget(Player player, Unit unit, Item item, int n, float f) {
        Building building2 = null;
        float f2 = Float.MAX_VALUE;
        Seq seq = new Seq();
        Vars.indexer.eachBlock(player.team(), unit.x, unit.y, f, building -> building != null, arg_0 -> ((Seq)seq).add(arg_0));
        for (Building building3 : seq) {
            float f3;
            int n2;
            boolean bl;
            if (building3.block instanceof CoreBlock) continue;
            String string = building3.block != null ? building3.block.name : "";
            boolean bl2 = string.contains("turret") || string.contains("duo") || string.contains("scatter") || string.contains("salvo") || string.contains("fuse") || string.contains("ripple") || string.contains("cyclone") || string.contains("swarmer") || string.contains("spectre") || string.contains("meltdown") || string.contains("foreshadow") || string.contains("lancer") || string.contains("arc") || string.contains("parallax") || string.contains("segment");
            boolean bl3 = bl = string.contains("factory") || string.contains("smelter") || string.contains("kiln") || string.contains("compressor") || string.contains("pulverizer") || string.contains("weaver") || string.contains("multi") || string.contains("cultivator") || string.contains("mixer") || string.contains("cryofluid") || string.contains("separator") || string.contains("disassembler") || string.contains("surge") || string.contains("phase") || string.contains("silicon") || string.contains("graphite");
            if (!(LunariansSettings.autoFillTurrets && bl2 || LunariansSettings.autoFillFactories && bl) && (string.contains("container") || string.contains("vault") || string.contains("core") || building3.acceptStack(item, 1, (Teamc)unit) <= 0) || (n2 = building3.acceptStack(item, n, (Teamc)unit)) <= 0 || !((f3 = unit.dst((Position)building3)) < f2)) continue;
            f2 = f3;
            building2 = building3;
        }
        return building2;
    }
}

