package lunarians;

import arc.Events;
import arc.math.geom.Position;
import arc.struct.Seq;
import arc.util.Time;
import java.util.Objects;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;
import mindustry.type.Item;
import mindustry.type.ItemStack;

public class AutoFill {
  private static float timer;
  
  private static boolean pending;
  
  private static float pendingTimer;
  
  private static Item pendingItem;
  
  private static int previousAmount;
  
  public static void init() {
    if (Vars.headless)
      return; 
    Events.run(EventType.Trigger.update, AutoFill::update);
    Events.on(EventType.WorldLoadEvent.class, paramWorldLoadEvent -> {
          timer = 0.0F;
          pending = false;
          pendingItem = null;
          pendingTimer = 0.0F;
          previousAmount = 0;
        });
  }
  
  public static void update() {
    if (!LunariansSettings.enableAutoFill || Vars.state == null || !Vars.state.isGame() || Vars.player == null || Vars.player
      .unit() == null || Vars.player.dead())
      return; 
    Unit unit = Vars.player.unit();
    Player player = Vars.player;
    ItemStack itemStack = unit.stack;
    if (pending) {
      pendingTimer += Time.delta / 60.0F;
      boolean bool1 = (itemStack != null && itemStack.amount != previousAmount) ? true : false;
      boolean bool2 = (itemStack != null && itemStack.item == pendingItem && itemStack.amount > 0) ? true : false;
      if (bool1 || bool2 || pendingTimer >= 1.25F) {
        pending = false;
        pendingItem = null;
        pendingTimer = 0.0F;
      } else {
        timer = 0.0F;
        return;
      } 
    } 
    timer += Time.delta / 60.0F;
    if (timer < LunariansSettings.actionInterval(Math.max(0.05F, LunariansSettings.autoFillInterval)))
      return; 
    timer = 0.0F;
    float f1 = Math.min(Math.max(1.0F, LunariansSettings.autoFillRange) * 8.0F, 220.0F);
    if (f1 <= 0.0F)
      return; 
    if (itemStack != null && itemStack.item != null && itemStack.amount > 0) {
      Building building1 = findDeliveryTarget(player, unit, itemStack.item, itemStack.amount, f1);
      if (building1 != null) {
        int j = building1.acceptStack(itemStack.item, itemStack.amount, (Teamc)unit);
        if (j > 0) {
          previousAmount = itemStack.amount;
          pendingItem = itemStack.item;
          pending = true;
          pendingTimer = 0.0F;
          Call.transferItemTo(unit, itemStack.item, j, unit.x, unit.y, building1);
          return;
        } 
      } 
      return;
    } 
    Building building = null;
    Item item = null;
    int i = 0;
    float f2 = Float.MAX_VALUE;
    Seq seq = new Seq();
    Objects.requireNonNull(seq);
    Vars.indexer.eachBlock(player.team(), unit.x, unit.y, f1, paramBuilding -> (paramBuilding != null && paramBuilding.items != null), seq::add);
    for (Building building1 : seq) {
      if (building1.items == null || building1.items.empty())
        continue; 
      boolean bool = building1.block instanceof mindustry.world.blocks.storage.CoreBlock;
      boolean bool1 = (building1.block != null && (building1.block.name.contains("container") || building1.block.name.contains("vault") || building1.block.name.contains("core") || building1.block.name.contains("storage"))) ? true : false;
      if (!bool && !bool1)
        continue; 
      for (Item item1 : Vars.content.items()) {
        if (item1 == null)
          continue; 
        int j = building1.items.get(item1);
        if (j <= 0)
          continue; 
        float f = unit.dst((Position)building1);
        if (f < f2) {
          f2 = f;
          building = building1;
          item = item1;
          i = Math.min(j, unit.itemCapacity() - ((unit.stack.amount > 0) ? unit.stack.amount : 0));
          if (i <= 0)
            i = Math.min(j, unit.itemCapacity()); 
        } 
      } 
    } 
    if (building != null && item != null && i > 0) {
      previousAmount = 0;
      pendingItem = item;
      pending = true;
      pendingTimer = 0.0F;
      Call.takeItems(building, item, i, unit);
    } 
  }
  
  private static Building findDeliveryTarget(Player paramPlayer, Unit paramUnit, Item paramItem, int paramInt, float paramFloat) {
    Building building = null;
    float f = Float.MAX_VALUE;
    Seq seq = new Seq();
    Objects.requireNonNull(seq);
    Vars.indexer.eachBlock(paramPlayer.team(), paramUnit.x, paramUnit.y, paramFloat, paramBuilding -> (paramBuilding != null), seq::add);
    for (Building building1 : seq) {
      if (building1.block instanceof mindustry.world.blocks.storage.CoreBlock)
        continue; 
      String str = (building1.block != null) ? building1.block.name : "";
      boolean bool1 = (str.contains("turret") || str.contains("duo") || str.contains("scatter") || str.contains("salvo") || str.contains("fuse") || str.contains("ripple") || str.contains("cyclone") || str.contains("swarmer") || str.contains("spectre") || str.contains("meltdown") || str.contains("foreshadow") || str.contains("lancer") || str.contains("arc") || str.contains("parallax") || str.contains("segment")) ? true : false;
      boolean bool2 = (str.contains("factory") || str.contains("smelter") || str.contains("kiln") || str.contains("compressor") || str.contains("pulverizer") || str.contains("weaver") || str.contains("multi") || str.contains("cultivator") || str.contains("mixer") || str.contains("cryofluid") || str.contains("separator") || str.contains("disassembler") || str.contains("surge") || str.contains("phase") || str.contains("silicon") || str.contains("graphite")) ? true : false;
      if ((LunariansSettings.autoFillTurrets && bool1) || (LunariansSettings.autoFillFactories && bool2) || (
        !str.contains("container") && !str.contains("vault") && !str.contains("core") && building1.acceptStack(paramItem, 1, (Teamc)paramUnit) > 0)) {
        int i = building1.acceptStack(paramItem, paramInt, (Teamc)paramUnit);
        if (i <= 0)
          continue; 
        float f1 = paramUnit.dst((Position)building1);
        if (f1 < f) {
          f = f1;
          building = building1;
        } 
      } 
    } 
    return building;
  }
}
