package lunarians;

import arc.Core;
import arc.Events;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.struct.IntFloatMap;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import lunarians.data.AttackSlot;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.dialogs.SettingsMenuDialog;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BlockGroup;

public class AutoAttack {
  private static final float SCAN = 0.7F;
  
  private static float timer;
  
  private static int count;
  
  private static boolean errorLogged;
  
  public static int standoffBlocks() {
    return Math.max(1, Math.min(40, Core.settings.getInt("lunarians-attack-standoff-blocks", 3)));
  }
  
  public static void setStandoffBlocks(int paramInt) {
    Core.settings.put("lunarians-attack-standoff-blocks", Integer.valueOf(Math.max(1, Math.min(40, paramInt))));
  }
  
  public static int maxStandoffTiles(Unit paramUnit) {
    if (paramUnit == null || paramUnit.type == null)
      return 4; 
    return maxStandoffTiles(paramUnit.type);
  }
  
  public static int maxStandoffTiles(UnitType paramUnitType) {
    if (paramUnitType == null)
      return 4; 
    float f = paramUnitType.maxRange / 8.0F;
    return Math.max(1, Mathf.ceil(f) + 2);
  }
  
  public static boolean regroupEnabled() {
    return Core.settings.getBool("lunarians-attack-regroup", false);
  }
  
  public static void setRegroupEnabled(boolean paramBoolean) {
    Core.settings.put("lunarians-attack-regroup", Boolean.valueOf(paramBoolean));
  }
  
  public static int regroupSec() {
    return Math.max(5, Math.min(120, Core.settings.getInt("lunarians-attack-regroup-sec", 5)));
  }
  
  public static void setRegroupSec(int paramInt) {
    Core.settings.put("lunarians-attack-regroup-sec", Integer.valueOf(Math.max(5, Math.min(120, paramInt))));
  }
  
  public static float standoffDist(Unit paramUnit, AttackSlot paramAttackSlot) {
    int i = maxStandoffTiles(paramUnit);
    boolean bool = (paramAttackSlot != null) ? paramAttackSlot.standoffTiles : false;
    int j = bool ? i : Mathf.clamp(bool, 1, i);
    return (j * 8);
  }
  
  public static float standoffDist(Unit paramUnit) {
    return standoffDist(paramUnit, null);
  }
  
  public static void init() {
    if (Vars.headless)
      return; 
    Events.run(EventType.Trigger.update, AutoAttack::update);
    if (!drawReg) {
      drawReg = true;
      Events.run(EventType.Trigger.draw, AutoAttack::drawOverlay);
    } 
    ChatCommands.init();
    Events.on(EventType.WorldLoadEvent.class, paramWorldLoadEvent -> reset());
    try {
      if (Vars.ui != null && Vars.ui.settings != null)
        Vars.ui.settings.addCategory("Lunarians Attack", paramSettingsTable -> {
              label("lunarians-attack-standoff-blocks", "Attack distance", "Distancia de ataque");
              label("lunarians-attack-regroup", "Regroup periodically", "Reagrupar periodicamente");
              label("lunarians-attack-regroup-sec", "Regroup interval", "Intervalo de reagrupar");
              label("lunarians-attack-smart", "Smart targets (turrets first)", "Alvos inteligentes (torres primeiro)");
              label("lunarians-attack-flee", "Retreat below HP %", "Recuar abaixo de % de vida");
              label("lunarians-attack-formation", "Formation + gather before advancing", "Formacao + juntar antes de avancar");
              label("lunarians-heal-escort", "Healer escort (open lane / shield)", "Escolta do curandeiro (abre caminho / protege)");
              label("lunarians-attack-overlay", "Show zones and targets on map", "Mostrar zonas e alvos no mapa");
              paramSettingsTable.sliderPref("lunarians-attack-standoff-blocks", 3, 1, 40, 1, ());
              paramSettingsTable.checkPref("lunarians-attack-regroup", false);
              paramSettingsTable.sliderPref("lunarians-attack-regroup-sec", 5, 5, 60, 1, ());
              paramSettingsTable.checkPref("lunarians-attack-smart", true);
              paramSettingsTable.sliderPref("lunarians-attack-flee", 0, 0, 90, 5, ());
              paramSettingsTable.checkPref("lunarians-attack-formation", true);
              paramSettingsTable.checkPref("lunarians-heal-escort", true);
              paramSettingsTable.checkPref("lunarians-attack-overlay", true);
              paramSettingsTable.row();
              paramSettingsTable.button(tr("Profile: Defend (retreat 40%)", "Perfil: Defender (recua 40%)"), ()).growX().height(40.0F).row();
              paramSettingsTable.button(tr("Profile: Balanced (retreat 25%)", "Perfil: Equilibrado (recua 25%)"), ()).growX().height(40.0F).row();
              paramSettingsTable.button(tr("Profile: Rush (no retreat)", "Perfil: Rush (sem recuo)"), ()).growX().height(40.0F).row();
            }); 
    } catch (Throwable throwable) {}
  }
  
  public static void reset() {
    timer = 0.0F;
    count = 0;
    lastHp.clear();
    dpsEst.clear();
    claimed.clear();
    lastScanTime = -1.0F;
    lastRegroupAt = 0.0F;
    fleeing.clear();
    enemyB.clear();
    fleeStart.clear();
    gatherSince.clear();
    lines.clear();
    shown.clear();
  }
  
  private static void update() {
    if (!LunariansSettings.enableAutoAttack || Vars.state == null || !Vars.state.isGame() || Vars.player == null)
      return; 
    timer += Time.delta / 60.0F;
    if (timer < LunariansSettings.actionInterval(0.7F))
      return; 
    float f = timer;
    timer = 0.0F;
    try {
      scan(f);
    } catch (Throwable throwable) {
      if (!errorLogged) {
        errorLogged = true;
        Log.err("[Lunarians] AutoAttack error", throwable);
      } 
    } 
  }
  
  private static Unit nearestHealer(Unit paramUnit) {
    Unit unit = null;
    float f = Float.MAX_VALUE;
    for (byte b = 0; b < Groups.unit.size(); b++) {
      Unit unit1 = (Unit)Groups.unit.index(b);
      if (unit1 != null && unit1.isValid() && unit1.team == Vars.player.team() && AutoUnitHeal.isActiveHealer(unit1.id)) {
        float f1 = paramUnit.dst((Position)unit1);
        if (f1 < f) {
          f = f1;
          unit = unit1;
        } 
      } 
    } 
    return unit;
  }
  
  private static boolean isCore(Building paramBuilding) {
    return (paramBuilding != null && paramBuilding.block instanceof CoreBlock);
  }
  
  private static float coreSafeDist(Building paramBuilding) {
    return (paramBuilding.block.size * 8) * 0.7F + 48.0F + 24.0F;
  }
  
  private static void trackDamage(Unit paramUnit, float paramFloat) {
    float f1 = lastHp.get(paramUnit.id, paramUnit.health);
    float f2 = Math.max(0.0F, f1 - paramUnit.health);
    float f3 = (paramFloat > 0.05F) ? (f2 / paramFloat) : 0.0F;
    float f4 = dpsEst.get(paramUnit.id, 0.0F);
    dpsEst.put(paramUnit.id, f4 * 0.55F + f3 * 0.45F);
    lastHp.put(paramUnit.id, paramUnit.health);
  }
  
  private static boolean isHighDamage(Unit paramUnit) {
    float f = dpsEst.get(paramUnit.id, 0.0F);
    return (f > Math.max(4.0F, paramUnit.maxHealth * 0.08F));
  }
  
  private static boolean groupLowDamage(Seq<Unit> paramSeq) {
    if (paramSeq.isEmpty())
      return true; 
    byte b1 = 0;
    for (byte b2 = 0; b2 < paramSeq.size; b2++) {
      if (isHighDamage((Unit)paramSeq.get(b2)))
        b1++; 
    } 
    return (b1 * 2 <= paramSeq.size);
  }
  
  private static void ensureMove(Unit paramUnit) {
    if (!(paramUnit.controller() instanceof CommandAI))
      return; 
    CommandAI commandAI = (CommandAI)paramUnit.controller();
    if (commandAI.command != UnitCommand.moveCommand)
      Call.setUnitCommand(Vars.player, new int[] { paramUnit.id }, UnitCommand.moveCommand); 
  }
  
  private static void commandBuild(Unit paramUnit, Building paramBuilding, float paramFloat, boolean paramBoolean) {
    ensureMove(paramUnit);
    float f1 = paramBuilding.angleTo((Position)paramUnit) * 0.017453292F;
    float f2 = paramFloat * 0.9F;
    if (paramBoolean && isCore(paramBuilding))
      f2 = Math.max(f2, coreSafeDist(paramBuilding) * 0.95F); 
    float f3 = paramBuilding.x + Mathf.cos(f1) * f2;
    float f4 = paramBuilding.y + Mathf.sin(f1) * f2;
    Vec2 vec2 = PathSafe.safeNear(paramUnit, f3, f4, 64.0F);
    UnitCoord.cmd(paramUnit, 1, 90.0F, paramBuilding, null, vec2);
  }
  
  private static void holdAwayFromCore(Unit paramUnit, Building paramBuilding, float paramFloat) {
    ensureMove(paramUnit);
    float f1 = Math.max(paramFloat * 0.95F, coreSafeDist(paramBuilding));
    float f2 = paramBuilding.angleTo((Position)paramUnit) * 0.017453292F;
    float f3 = paramBuilding.x + Mathf.cos(f2) * f1;
    float f4 = paramBuilding.y + Mathf.sin(f2) * f1;
    Vec2 vec2 = PathSafe.safeNear(paramUnit, f3, f4, 80.0F);
    UnitCoord.cmd(paramUnit, 1, 90.0F, null, null, vec2);
  }
  
  private static void commandBuildFormation(Unit paramUnit, Building paramBuilding, float paramFloat1, float paramFloat2, int paramInt) {
    ensureMove(paramUnit);
    float f1 = paramBuilding.angleTo((Position)paramUnit) * 0.017453292F;
    float f2 = (paramInt <= 1) ? 0.0F : ((paramFloat2 - (paramInt - 1) * 0.5F) * 0.35F);
    float f3 = f1 + f2;
    float f4 = paramFloat1 * 0.9F;
    float f5 = paramBuilding.x + Mathf.cos(f3) * f4;
    float f6 = paramBuilding.y + Mathf.sin(f3) * f4;
    Vec2 vec2 = PathSafe.safeNear(paramUnit, f5, f6, 64.0F);
    UnitCoord.cmd(paramUnit, 1, 90.0F, paramBuilding, null, vec2);
  }
  
  private static void regroupSlot(Seq<Unit> paramSeq) {
    if (paramSeq.size < 2)
      return; 
    float f1 = 0.0F;
    float f2 = 0.0F;
    byte b;
    for (b = 0; b < paramSeq.size; b++) {
      f1 += ((Unit)paramSeq.get(b)).x;
      f2 += ((Unit)paramSeq.get(b)).y;
    } 
    f1 /= paramSeq.size;
    f2 /= paramSeq.size;
    for (b = 0; b < paramSeq.size; b++) {
      Unit unit = (Unit)paramSeq.get(b);
      ensureMove(unit);
      float f3 = b * 360.0F / paramSeq.size * 0.017453292F;
      float f4 = Mathf.cos(f3) * 16.0F;
      float f5 = Mathf.sin(f3) * 16.0F;
      Vec2 vec2 = PathSafe.safeNear(unit, f1 + f4, f2 + f5, 48.0F);
      UnitCoord.cmd(unit, 1, 90.0F, null, null, vec2);
      count++;
    } 
  }
  
  private static void scan(float paramFloat) {
    Seq<AttackSlot> seq = LunariansSettings.attackSlots;
    if (seq.isEmpty())
      return; 
    count = 0;
    lines.clear();
    claimed.clear();
    int i = Groups.unit.size();
    enemyB.clear();
    for (Building building : Groups.build) {
      if (building == null || !building.isValid() || building.team == Vars.player.team() || building.team == Team.derelict)
        continue; 
      enemyB.add(building);
    } 
    boolean bool1 = (regroupEnabled() && Time.time - lastRegroupAt >= regroupSec() * 60.0F) ? true : false, bool2 = bool1;
    if (bool1)
      lastRegroupAt = Time.time; 
    for (byte b = 0; b < seq.size; b++) {
      AttackSlot attackSlot = (AttackSlot)seq.get(b);
      if (!attackSlot.ready())
        continue; 
      Seq<Unit> seq1 = new Seq();
      byte b1;
      for (b1 = 0; b1 < i; b1++) {
        Unit unit1 = (Unit)Groups.unit.index(b1);
        if (unit1 != null && unit1.isValid() && unit1.team == Vars.player.team() && !claimed.contains(unit1.id) && UnitCoord.holder(unit1.id) != 2 && UnitCoord.holder(unit1.id) != 3 && !AutoUnitHeal.isActiveHealer(unit1.id) && !AutoUnitHeal.isHealerType(unit1.type) && attackSlot.unit.equals(unit1.type.name) && unit1.controller() instanceof CommandAI)
          seq1.add(unit1); 
      } 
      if (attackSlot.maxUnits > 0 && seq1.size > attackSlot.maxUnits)
        seq1.truncate(attackSlot.maxUnits); 
      if (seq1.isEmpty())
        continue; 
      for (b1 = 0; b1 < seq1.size; b1++) {
        claimed.add(((Unit)seq1.get(b1)).id);
        trackDamage((Unit)seq1.get(b1), paramFloat);
      } 
      int j = fleePercent();
      if (j > 0) {
        CoreBlock.CoreBuild coreBuild = Vars.player.closestCore();
        for (int k = seq1.size - 1; k >= 0; k--) {
          Unit unit1 = (Unit)seq1.get(k);
          float f = unit1.health / Math.max(1.0F, unit1.maxHealth);
          boolean bool5 = fleeing.contains(unit1.id);
          if (!bool5 && f * 100.0F < j) {
            fleeing.add(unit1.id);
            fleeStart.put(unit1.id, Time.time);
            bool5 = true;
          } else if (bool5 && (f >= 0.8F || Time.time - fleeStart.get(unit1.id, 0.0F) > 3600.0F)) {
            fleeing.remove(unit1.id);
            bool5 = false;
          } 
          if (bool5) {
            Unit unit2 = LunariansSettings.autoAttackHeal ? nearestHealer(unit1) : null;
            if (unit2 != null) {
              Vec2 vec2 = PathSafe.safeNear(unit1, unit2.x, unit2.y, 48.0F);
              if (UnitCoord.cmd(unit1, 4, 90.0F, null, null, vec2))
                count++; 
            } else if (coreBuild != null && unit1.dst((Position)coreBuild) > 96.0F) {
              Vec2 vec2 = PathSafe.safeNear(unit1, ((Building)coreBuild).x + 32.0F, ((Building)coreBuild).y + 32.0F, 64.0F);
              if (UnitCoord.cmd(unit1, 4, 90.0F, null, null, vec2))
                count++; 
            } 
            seq1.remove(k);
          } 
        } 
        if (seq1.isEmpty())
          continue; 
      } 
      if (bool1) {
        regroupSlot(seq1);
        continue;
      } 
      float f2 = attackSlot.hasZone() ? ((attackSlot.x * 8) + 4.0F) : ((Unit)seq1.first()).x;
      float f1 = attackSlot.hasZone() ? ((attackSlot.y * 8) + 4.0F) : ((Unit)seq1.first()).y, f3 = f1;
      if (!attackSlot.hasZone()) {
        f2 = 0.0F;
        f1 = 0.0F;
        for (byte b4 = 0; b4 < seq1.size; b4++) {
          f2 += ((Unit)seq1.get(b4)).x;
          f1 += ((Unit)seq1.get(b4)).y;
        } 
        f2 /= seq1.size;
        f1 /= seq1.size;
      } 
      Seq seq2 = new Seq();
      boolean bool = false;
      for (Building building : enemyB) {
        if (attackSlot.hasZone() && Mathf.dst(f2, f1, building.x, building.y) > attackSlot.zoneRadius * 8.0F)
          continue; 
        seq2.add(building);
        if (building.block instanceof mindustry.world.blocks.defense.turrets.Turret)
          bool = true; 
      } 
      boolean bool3 = smartTargets();
      Building building2 = null;
      Building building1 = null;
      float f4 = Float.MAX_VALUE;
      float f5 = Float.MAX_VALUE;
      float f6 = Float.MAX_VALUE;
      for (byte b2 = 0; b2 < seq2.size; b2++) {
        Building building = (Building)seq2.get(b2);
        float f = 0.0F;
        for (byte b4 = 0; b4 < seq1.size; b4++)
          f += ((Unit)seq1.get(b4)).dst((Position)building); 
        f /= Math.max(1, seq1.size);
        if (isCore(building)) {
          if (f < f4) {
            f4 = f;
            building2 = building;
          } 
        } else {
          float f8 = f;
          if (bool3) {
            boolean bool5 = (building.block instanceof mindustry.world.blocks.defense.Wall || building.block.group == BlockGroup.transportation) ? true : false;
            if (building.block instanceof mindustry.world.blocks.defense.turrets.Turret) {
              if (f < 640.0F)
                f8 -= 240.0F; 
            } else if (building.block instanceof mindustry.world.blocks.power.PowerGenerator) {
              f8 -= 96.0F;
            } else if (building.block instanceof mindustry.world.blocks.units.UnitFactory || building.block instanceof mindustry.world.blocks.production.GenericCrafter) {
              f8 -= 64.0F;
            } else if (bool5) {
              f8 += bool ? 800.0F : 160.0F;
            } 
          } 
          if (f8 < f5) {
            f5 = f8;
            f6 = f;
            building1 = building;
          } 
        } 
      } 
      float f7 = Math.min(f4, f6);
      if (formationOn() && !attackSlot.hasZone() && seq1.size > 1 && f7 > 240.0F) {
        float f = 0.0F;
        for (byte b4 = 0; b4 < seq1.size; b4++)
          f = Math.max(f, ((Unit)seq1.get(b4)).dst(f2, f1)); 
        if (f > 144.0F) {
          float f8 = gatherSince.get(b, -1.0F);
          if (f8 < 0.0F) {
            gatherSince.put(b, Time.time);
            f8 = Time.time;
          } 
          if (Time.time - f8 < 600.0F) {
            regroupSlot(seq1);
            continue;
          } 
        } else {
          gatherSince.put(b, -1.0F);
        } 
      } 
      if (overlayOn()) {
        if (building1 != null)
          lines.add(new float[] { f2, f1, building1.x, building1.y, 0.0F }); 
        if (building2 != null)
          lines.add(new float[] { f2, f1, building2.x, building2.y, 1.0F }); 
        if (attackSlot.hasZone())
          lines.add(new float[] { (attackSlot.x * 8) + 4.0F, (attackSlot.y * 8) + 4.0F, attackSlot.zoneRadius * 8.0F, 0.0F, 2.0F }); 
      } 
      seq1.sort(paramUnit -> -paramUnit.maxHealth);
      Unit unit = (building2 != null) ? pickCoreBreaker(seq1, building2) : null;
      boolean bool4 = groupLowDamage(seq1);
      for (byte b3 = 0; b3 < seq1.size; b3++) {
        Unit unit1 = (Unit)seq1.get(b3);
        float f = standoffDist(unit1, attackSlot);
        if (building2 != null && isHighDamage(unit1) && !bool4) {
          holdAwayFromCore(unit1, building2, f);
          count++;
        } else if (unit1 == unit && building2 != null && bool4) {
          commandBuild(unit1, building2, f, true);
          count++;
        } else if (building1 != null) {
          float f9 = unit1.dst(f2, f1);
          float f8;
          if (f9 > (f8 = 40.0F) * 1.35F + 40.0F) {
            ensureMove(unit1);
            float f10 = b3 * 360.0F / seq1.size * 0.017453292F;
            UnitCoord.cmd(unit1, 1, 90.0F, null, null, new Vec2(f2 + Mathf.cos(f10) * 18.0F, f1 + Mathf.sin(f10) * 18.0F));
            count++;
          } else {
            commandBuildFormation(unit1, building1, f, formationOn() ? (((b3 == 0) ? false : (((b3 & 0x1) == 1) ? ((b3 + 1) / 2) : (-b3 / 2))) + (seq1.size - 1) * 0.5F) : b3, seq1.size);
            count++;
          } 
        } else if (building2 != null) {
          holdAwayFromCore(unit1, building2, f);
          count++;
        } 
      } 
      continue;
    } 
    shown.clear();
    shown.addAll(lines);
  }
  
  private static Unit pickCoreBreaker(Seq<Unit> paramSeq, Building paramBuilding) {
    Unit unit = null;
    float f = Float.MAX_VALUE;
    for (byte b = 0; b < paramSeq.size; b++) {
      Unit unit1 = (Unit)paramSeq.get(b);
      float f1;
      if (!isHighDamage(unit1) && (f1 = unit1.dst((Position)paramBuilding) - unit1.health / Math.max(1.0F, unit1.maxHealth) * 40.0F) < f) {
        f = f1;
        unit = unit1;
      } 
    } 
    return (unit != null) ? unit : (Unit)paramSeq.first();
  }
  
  private static final IntSet claimed = new IntSet();
  
  private static final IntFloatMap lastHp = new IntFloatMap();
  
  private static final IntFloatMap dpsEst = new IntFloatMap();
  
  private static float lastScanTime = -1.0F;
  
  private static float lastRegroupAt;
  
  public static final String KEY_STANDOFF = "lunarians-attack-standoff-blocks";
  
  public static final String KEY_REGROUP = "lunarians-attack-regroup";
  
  public static final String KEY_REGROUP_SEC = "lunarians-attack-regroup-sec";
  
  private static final IntSet fleeing = new IntSet();
  
  private static final IntFloatMap fleeStart = new IntFloatMap();
  
  private static final IntFloatMap gatherSince = new IntFloatMap();
  
  private static final Seq<Building> enemyB = new Seq();
  
  private static final Seq<float[]> lines = new Seq();
  
  private static final Seq<float[]> shown = new Seq();
  
  private static boolean drawReg;
  
  public static boolean smartTargets() {
    return Core.settings.getBool("lunarians-attack-smart", true);
  }
  
  public static boolean formationOn() {
    return Core.settings.getBool("lunarians-attack-formation", true);
  }
  
  public static boolean overlayOn() {
    return Core.settings.getBool("lunarians-attack-overlay", true);
  }
  
  public static int fleePercent() {
    return Math.max(0, Math.min(90, Core.settings.getInt("lunarians-attack-flee", 0)));
  }
  
  private static boolean pt() {
    return (LunariansSettings.language != null && LunariansSettings.language.startsWith("pt"));
  }
  
  private static String tr(String paramString1, String paramString2) {
    return pt() ? paramString2 : paramString1;
  }
  
  private static void label(String paramString1, String paramString2, String paramString3) {
    try {
      Core.bundle.getProperties().put("setting." + paramString1 + ".name", tr(paramString2, paramString3));
    } catch (Throwable throwable) {}
  }
  
  private static void profile(int paramInt1, int paramInt2, int paramInt3) {
    Core.settings.put("lunarians-attack-smart", Boolean.valueOf((paramInt1 == 1)));
    Core.settings.put("lunarians-attack-flee", Integer.valueOf(paramInt2));
    Core.settings.put("lunarians-attack-formation", Boolean.valueOf((paramInt3 == 1)));
    if (Vars.ui != null)
      Vars.ui.showInfoToast(tr("Profile applied. Reopen this menu to see the values.", "Perfil aplicado. Reabra o menu para ver os valores."), 3.0F); 
  }
  
  private static void drawOverlay() {
    if (Vars.state == null || !Vars.state.isGame() || !LunariansSettings.enableAutoAttack || !overlayOn())
      return; 
    Draw.z(120.0F);
    for (byte b = 0; b < shown.size; b++) {
      float[] arrayOfFloat = (float[])shown.get(b);
      if (arrayOfFloat[4] == 2.0F) {
        Draw.color(Pal.accent, 0.8F);
        Lines.stroke(1.5F);
        Lines.circle(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2]);
      } else {
        Draw.color((arrayOfFloat[4] == 1.0F) ? Pal.remove : Pal.lightishOrange, 0.8F);
        Lines.stroke(1.2F);
        Lines.line(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2], arrayOfFloat[3]);
        Lines.circle(arrayOfFloat[2], arrayOfFloat[3], 6.0F);
      } 
    } 
    Draw.reset();
  }
  
  public static int count() {
    return count;
  }
}
