package lunarians;

import arc.Core;
import arc.Events;
import arc.scene.Element;
import arc.scene.ui.layout.Table;
import arc.struct.IntSeq;
import arc.util.Log;
import arc.util.Time;
import java.util.HashSet;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.UnitStance;
import mindustry.ai.types.CommandAI;
import mindustry.content.Items;
import mindustry.entities.units.UnitController;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.blocks.units.UnitFactory;

public class MinerUnits {
  public static final String[] TYPES = new String[] { "mono", "poly", "mega", "quasar" };
  
  private static float next;
  
  private static final HashSet<Integer> mine = new HashSet<>();
  
  private static boolean registered;
  
  private static boolean poor;
  
  public static void init() {
    if (registered)
      return; 
    registered = true;
    Events.run(EventType.Trigger.update, MinerUnits::update);
    Events.on(EventType.WorldLoadEvent.class, paramWorldLoadEvent -> mine.clear());
  }
  
  private static String tr(String paramString1, String paramString2, String paramString3, String paramString4) {
    String str = LunariansI18n.getLanguage();
    if ("pt".equals(str))
      return paramString1; 
    if ("es".equals(str))
      return paramString3; 
    if ("ru".equals(str))
      return paramString4; 
    return paramString2;
  }
  
  public static String title() {
    return tr("Mineradores", "Miner units", "Unidades mineras", "Шахтёры");
  }
  
  public static boolean enabled() {
    return Core.settings.getBool("lunarians-mu-on", false);
  }
  
  public static void setEnabled(boolean paramBoolean) {
    Core.settings.put("lunarians-mu-on", Boolean.valueOf(paramBoolean));
  }
  
  public static boolean typeOn(int paramInt) {
    return Core.settings.getBool("lunarians-mu-t-" + TYPES[paramInt], true);
  }
  
  public static void setTypeOn(int paramInt, boolean paramBoolean) {
    Core.settings.put("lunarians-mu-t-" + TYPES[paramInt], Boolean.valueOf(paramBoolean));
  }
  
  public static boolean produce() {
    return Core.settings.getBool("lunarians-mu-prod", true);
  }
  
  public static void setProduce(boolean paramBoolean) {
    Core.settings.put("lunarians-mu-prod", Boolean.valueOf(paramBoolean));
  }
  
  public static int cap() {
    return Math.max(1, Math.min(40, Core.settings.getInt("lunarians-mu-cap", 8)));
  }
  
  public static void setCap(int paramInt) {
    Core.settings.put("lunarians-mu-cap", Integer.valueOf(Math.max(1, Math.min(40, paramInt))));
  }
  
  private static int typeIndex(UnitType paramUnitType) {
    for (byte b = 0; b < TYPES.length; b++) {
      if (paramUnitType != null && TYPES[b].equals(paramUnitType.name))
        return b; 
    } 
    return -1;
  }
  
  private static void update() {
    if (!enabled() || Vars.state == null || !Vars.state.isGame() || Vars.player == null || Time.time < next)
      return; 
    next = Time.time + 120.0F;
    try {
      if (Vars.player.closestCore() == null)
        return; 
      IntSeq intSeq1 = new IntSeq(), intSeq2 = new IntSeq();
      int[] arrayOfInt = { 0 };
      Groups.unit.each(paramUnit -> {
            if (paramUnit.team != Vars.player.team() || paramUnit.isPlayer() || paramUnit.dead)
              return; 
            int i = typeIndex(paramUnit.type);
            if (i < 0 || !typeOn(i))
              return; 
            paramArrayOfint[0] = paramArrayOfint[0] + 1;
            UnitController unitController = paramUnit.controller();
            if (!(unitController instanceof CommandAI))
              return; 
            CommandAI commandAI = (CommandAI)unitController;
            if (commandAI.currentCommand() != UnitCommand.mineCommand) {
              paramIntSeq1.add(paramUnit.id);
            } else if (!commandAI.hasStance(UnitStance.mineAuto)) {
              paramIntSeq2.add(paramUnit.id);
            } 
          });
      if (intSeq1.size > 0)
        Call.setUnitCommand(Vars.player, intSeq1.toArray(), UnitCommand.mineCommand); 
      if (intSeq2.size > 0)
        Call.setUnitStance(Vars.player, intSeq2.toArray(), UnitStance.mineAuto, true); 
      if (produce())
        factories(arrayOfInt[0]); 
    } catch (Throwable throwable) {
      Log.err("[Lunarians] MinerUnits", throwable);
    } 
  }
  
  private static void factories(int paramInt) {
    boolean bool1 = (typeOn(0) || typeOn(1) || typeOn(2)) ? true : false;
    boolean bool = typeOn(3);
    int i = (Vars.player.closestCore()).items.get(Items.silicon);
    int j = (Vars.player.closestCore()).items.get(Items.lead);
    if (i < 30 || j < 20) {
      poor = true;
    } else if (i >= 100 && j >= 60) {
      poor = false;
    } 
    boolean bool2 = (paramInt >= cap() || poor) ? true : false;
    for (Building building : Groups.build) {
      if (building.team != Vars.player.team() || !(building instanceof UnitFactory.UnitFactoryBuild))
        continue; 
      UnitFactory.UnitFactoryBuild unitFactoryBuild = (UnitFactory.UnitFactoryBuild)building;
      UnitFactory unitFactory = (UnitFactory)unitFactoryBuild.block;
      String str = unitFactory.name.equals("air-factory") ? (bool1 ? "mono" : null) : (unitFactory.name.equals("ground-factory") ? (bool ? "nova" : null) : null);
      if (str == null)
        continue; 
      int k = unitFactoryBuild.pos();
      if (bool2) {
        if (mine.remove(Integer.valueOf(k)) && unitFactoryBuild.currentPlan != -1)
          Call.tileConfig(Vars.player, (Building)unitFactoryBuild, Integer.valueOf(-1)); 
        continue;
      } 
      if (unitFactoryBuild.currentPlan != -1 && !mine.contains(Integer.valueOf(k)))
        continue; 
      byte b = -1;
      for (byte b1 = 0; b1 < unitFactory.plans.size; b1++) {
        if (((UnitFactory.UnitPlan)unitFactory.plans.get(b1)).unit != null && str.equals(((UnitFactory.UnitPlan)unitFactory.plans.get(b1)).unit.name))
          b = b1; 
      } 
      if (b >= 0 && unitFactoryBuild.currentPlan != b) {
        Call.tileConfig(Vars.player, (Building)unitFactoryBuild, Integer.valueOf(b));
        mine.add(Integer.valueOf(k));
        continue;
      } 
      if (b >= 0)
        mine.add(Integer.valueOf(k)); 
    } 
  }
  
  public static BaseDialog dialog() {
    BaseDialog baseDialog = new BaseDialog(title());
    Runnable[] arrayOfRunnable = new Runnable[1];
    arrayOfRunnable[0] = (() -> {
        Table table1 = paramBaseDialog.cont;
        table1.clear();
        table1.defaults().left().pad(4.0F);
        table1.check(tr("Ativar mineradores", "Enable miner units", "Activar unidades mineras", "Включить шахтёров"), enabled(), ()).row();
        table1.add(tr("Manda as unidades minerarem sempre o recurso que mais falta no núcleo.", "Sends the units to mine whatever the core lacks most.", "Envía las unidades a minar lo que más falta en el núcleo.", "Отправляет юнитов добывать то, чего в ядре меньше всего.")).wrap().width(380.0F).row();
        for (byte b = 0; b < TYPES.length; b++) {
          byte b1 = b;
          UnitType unitType = Vars.content.unit(TYPES[b]);
          table1.check((unitType != null) ? unitType.localizedName : TYPES[b], typeOn(b), ()).row();
        } 
        table1.check(tr("Produzir unidades (configura air/ground factory)", "Produce units (configures air/ground factory)", "Producir unidades (configura air/ground factory)", "Производить юнитов (настраивает air/ground factory)"), produce(), ()).row();
        Table table2 = new Table();
        table2.add(tr("Quantidade máxima", "Max amount", "Cantidad máxima", "Макс. количество")).width(190.0F);
        table2.button("-", ()).size(46.0F, 40.0F).pad(2.0F);
        table2.add(String.valueOf(cap())).width(48.0F).center();
        table2.button("+", ()).size(46.0F, 40.0F).pad(2.0F);
        table1.add((Element)table2).row();
        table1.add(tr("Mono→Poly→Mega vem das reconstructors; Quasar: ground factory (Nova→Pulsar→Quasar).", "Mono→Poly→Mega come from the reconstructors; Quasar: ground factory (Nova→Pulsar→Quasar).", "Mono→Poly→Mega vienen de los reconstructores; Quasar: ground factory (Nova→Pulsar→Quasar).", "Mono→Poly→Mega — из реконструкторов; Quasar — ground factory (Nova→Pulsar→Quasar).")).wrap().width(380.0F).row();
      });
    arrayOfRunnable[0].run();
    baseDialog.addCloseButton();
    baseDialog.hidden(LunariansSettings::save);
    return baseDialog;
  }
}
