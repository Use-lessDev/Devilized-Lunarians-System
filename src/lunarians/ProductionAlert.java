package lunarians;

import arc.Events;
import arc.func.Cons;
import arc.struct.IntFloatMap;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.ctype.UnlockableContent;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.type.UnitType;
import mindustry.world.blocks.production.Drill;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.units.Reconstructor;
import mindustry.world.blocks.units.UnitFactory;

public class ProductionAlert {
  private static final float SCAN_INTERVAL = 1.5F;
  
  private static final float IDLE_RESET = 45.0F;
  
  private static final int MAX_PER_SCAN = 3;
  
  private static final IntFloatMap lastSeen = new IntFloatMap();
  
  private static float clock;
  
  private static float timer;
  
  private static int silentScans;
  
  private static int notifiedThisScan;
  
  private static boolean errorLogged;
  
  public static void init() {
    if (Vars.headless)
      return; 
    Events.run(EventType.Trigger.update, new Runnable() {
          public void run() {
            ProductionAlert.update();
          }
        });
    Events.on(EventType.WorldLoadEvent.class, new Cons<EventType.WorldLoadEvent>() {
          public void get(EventType.WorldLoadEvent param1WorldLoadEvent) {
            ProductionAlert.reset();
          }
        });
  }
  
  public static void reset() {
    lastSeen.clear();
    silentScans = 2;
    timer = 0.0F;
  }
  
  private static void update() {
    if (!LunariansSettings.enableProductionAlert || Vars.state == null || !Vars.state.isGame() || Vars.player == null)
      return; 
    float f = Time.delta / 60.0F;
    clock += f;
    if ((timer += f) < 1.5F)
      return; 
    timer = 0.0F;
    try {
      scan();
    } catch (Throwable throwable) {
      if (!errorLogged) {
        errorLogged = true;
        Log.err("[Lunarians] ProductionAlert error", throwable);
      } 
    } 
  }
  
  private static void scan() {
    notifiedThisScan = 0;
    boolean bool = (silentScans > 0) ? true : false;
    Team team = Vars.player.team();
    int i = Groups.build.size();
    for (byte b = 0; b < i; b++) {
      Building building = (Building)Groups.build.index(b);
      if (building != null && building.isValid() && building.team != null && building.team != Team.derelict && (!LunariansSettings.productionIgnoreOwn || building.team != team))
        inspect(building, bool); 
    } 
    if (silentScans > 0)
      silentScans--; 
  }
  
  private static void inspect(Building paramBuilding, boolean paramBoolean) {
    if (paramBuilding instanceof UnitFactory.UnitFactoryBuild) {
      UnitFactory.UnitFactoryBuild unitFactoryBuild = (UnitFactory.UnitFactoryBuild)paramBuilding;
      UnitFactory unitFactory = (UnitFactory)paramBuilding.block;
      if (unitFactoryBuild.currentPlan >= 0 && unitFactoryBuild.currentPlan < unitFactory.plans.size && unitFactoryBuild.efficiency > 0.0F && unitFactoryBuild.shouldConsume()) {
        UnitFactory.UnitPlan unitPlan = (UnitFactory.UnitPlan)unitFactory.plans.get(unitFactoryBuild.currentPlan);
        found(paramBuilding.team, (unitPlan == null) ? null : (UnlockableContent)unitPlan.unit, paramBoolean);
      } 
    } else if (paramBuilding instanceof Reconstructor.ReconstructorBuild) {
      Reconstructor.ReconstructorBuild reconstructorBuild = (Reconstructor.ReconstructorBuild)paramBuilding;
      if (reconstructorBuild.constructing() && reconstructorBuild.efficiency > 0.0F) {
        UnitType unitType = reconstructorBuild.unit();
        found(paramBuilding.team, (UnlockableContent)unitType, paramBoolean);
      } 
    } else if (paramBuilding instanceof GenericCrafter.GenericCrafterBuild) {
      if (paramBuilding.efficiency > 0.0F && paramBuilding.shouldConsume()) {
        GenericCrafter genericCrafter = (GenericCrafter)paramBuilding.block;
        if (genericCrafter.outputItems != null) {
          for (ItemStack itemStack : genericCrafter.outputItems) {
            if (itemStack != null)
              found(paramBuilding.team, (UnlockableContent)itemStack.item, paramBoolean); 
          } 
        } else if (genericCrafter.outputItem != null) {
          found(paramBuilding.team, (UnlockableContent)genericCrafter.outputItem.item, paramBoolean);
        } 
        if (genericCrafter.outputLiquids != null) {
          for (LiquidStack liquidStack : genericCrafter.outputLiquids) {
            if (liquidStack != null)
              found(paramBuilding.team, (UnlockableContent)liquidStack.liquid, paramBoolean); 
          } 
        } else if (genericCrafter.outputLiquid != null) {
          found(paramBuilding.team, (UnlockableContent)genericCrafter.outputLiquid.liquid, paramBoolean);
        } 
      } 
    } else if (paramBuilding instanceof Drill.DrillBuild) {
      Drill.DrillBuild drillBuild = (Drill.DrillBuild)paramBuilding;
      if (drillBuild.dominantItem != null && drillBuild.warmup > 0.3F)
        found(drillBuild.team, (UnlockableContent)drillBuild.dominantItem, paramBoolean); 
    } 
  }
  
  private static void found(Team paramTeam, UnlockableContent paramUnlockableContent, boolean paramBoolean) {
    if (paramTeam == null || paramUnlockableContent == null)
      return; 
    int i = paramTeam.id << 20 | paramUnlockableContent.getContentType().ordinal() << 14 | paramUnlockableContent.id & 0x3FFF;
    boolean bool = lastSeen.containsKey(i);
    float f = lastSeen.get(i, 0.0F);
    boolean bool1 = (!bool || clock - f > 45.0F) ? true : false, bool2 = bool1;
    if (!bool1) {
      lastSeen.put(i, clock);
      return;
    } 
    if (paramBoolean) {
      lastSeen.put(i, clock);
      return;
    } 
    if (notifiedThisScan >= 3)
      return; 
    notifiedThisScan++;
    lastSeen.put(i, clock);
    announce(paramTeam, paramUnlockableContent);
  }
  
  public static String teamName(Team paramTeam) {
    String str = "[#" + paramTeam.color + "]";
    if (paramTeam.name == null || paramTeam.name.startsWith("team#"))
      return str + "Team " + paramTeam.id + "[]"; 
    return str + paramTeam.localized() + "[]";
  }
  
  private static void announce(Team paramTeam, UnlockableContent paramUnlockableContent) {
    String str1 = paramUnlockableContent.emoji();
    String str2 = LunariansI18n.get("prod_started", teamName(paramTeam), paramUnlockableContent.localizedName);
    try {
      Vars.ui.chatfrag.addMessage(str2 + " " + str1);
    } catch (Throwable throwable) {}
  }
}
