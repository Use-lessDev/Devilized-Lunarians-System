package lunarians;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.core.World;
import mindustry.game.EventType;
import mindustry.gen.Unit;
import mindustry.type.Item;
import mindustry.ui.Styles;
import mindustry.world.Tile;

public class AutoEconomy {
  public static final String KEY = "lunarians-econ-order";
  
  public static boolean active = false;
  
  private static final ArrayList<Object[]> stages = new ArrayList();
  
  private static int idx = 0;
  
  private static boolean adopted = false;
  
  private static int areaX;
  
  private static int areaY;
  
  private static float areaR;
  
  private static int lastStep;
  
  private static int lastTotal;
  
  private static boolean sawRunning;
  
  private static float unitsDeadline;
  
  private static boolean inUnits;
  
  private static boolean finished;
  
  private static float autoAt;
  
  private static boolean scanning;
  
  private static String tr4(String paramString1, String paramString2, String paramString3, String paramString4) {
    String str = LunariansI18n.getLanguage();
    if ("pt".equals(str))
      return paramString1; 
    if ("es".equals(str))
      return paramString3; 
    if ("ru".equals(str))
      return paramString4; 
    return paramString2;
  }
  
  private static void buildPanel() {
    if (panelBuilt || Vars.ui == null || Vars.ui.hudGroup == null)
      return; 
    panelBuilt = true;
    Vars.ui.hudGroup.fill(paramTable -> {
          paramTable.top();
          paramTable.visible(());
          paramTable.table(Styles.black6, ()).padTop(110.0F);
        });
  }
  
  public static boolean isSandTile(Tile paramTile) {
    if (paramTile == null)
      return false; 
    try {
      if (paramTile.floor() != null) {
        if ((paramTile.floor()).isLiquid || (paramTile.floor()).liquidDrop != null)
          return false; 
        if ((paramTile.floor()).itemDrop == Items.sand)
          return true; 
        String str = (paramTile.floor()).name;
        if (str != null) {
          String str1 = str.toLowerCase();
          if (str1.contains("water") || str1.contains("deep") || str1.contains("pool"))
            return false; 
          if (str1.contains("darksand") || str1.equals("sand") || str1.contains("sand-floor") || (str1
            .contains("sand") && !str1.contains("sandstone")))
            return true; 
        } 
      } 
    } catch (Throwable throwable) {}
    try {
      if (paramTile.drop() == Items.sand)
        return true; 
    } catch (Throwable throwable) {}
    try {
      if (paramTile.overlay() != null && (paramTile.overlay()).itemDrop == Items.sand)
        return true; 
    } catch (Throwable throwable) {}
    return false;
  }
  
  private static void input(Unit paramUnit) {
    buildPanel();
    if (sandMode == 0 || !Core.input.justTouched() || Core.scene.hasMouse())
      return; 
    int i = Vars.world.width();
    if (sandMarked == null || sandMarked.length != i * Vars.world.height()) {
      sandMarked = new boolean[i * Vars.world.height()];
      sandList.clear();
    } 
    int j = World.toTile(Core.input.mouseWorldX());
    int k = World.toTile(Core.input.mouseWorldY());
    byte b = 3;
    for (int m = k - b; m <= k + b; m++) {
      for (int n = j - b; n <= j + b; n++) {
        if (Vars.world.tiles.in(n, m) && Mathf.dst(n, m, j, k) <= b + 0.5F) {
          int i1 = m * i + n;
          if (sandMode == 1) {
            Tile tile = Vars.world.tile(n, m);
            if (!sandMarked[i1] && tile != null && isSandTile(tile)) {
              sandMarked[i1] = true;
              sandList.add(Integer.valueOf(i1));
            } 
          } else if (sandMarked[i1]) {
            sandMarked[i1] = false;
            sandList.remove(Integer.valueOf(i1));
          } 
        } 
      } 
    } 
  }
  
  private static void applySand(boolean[] paramArrayOfboolean) {
    if (sandList.isEmpty() || sandMarked == null || paramArrayOfboolean == null)
      return; 
    int i = Vars.world.width();
    for (int j = Math.max(0, areaY - (int)areaR - 1); j <= Math.min(Vars.world.height() - 1, areaY + (int)areaR + 1); j++) {
      for (int k = Math.max(0, areaX - (int)areaR - 1); k <= Math.min(i - 1, areaX + (int)areaR + 1); k++) {
        int m = j * i + k;
        Tile tile;
        if (!sandMarked[m] && m < paramArrayOfboolean.length && (tile = Vars.world.tile(k, j)) != null && isSandTile(tile))
          paramArrayOfboolean[m] = true; 
      } 
    } 
  }
  
  private static void clearMarks() {
    sandList.clear();
    sandMarked = null;
    sandMode = 0;
    startRequested = false;
    mapWanted = false;
    markCount = 0;
    marked = null;
    scanning = false;
  }
  
  private static boolean scan(Unit paramUnit) {
    int i = Vars.world.width();
    if (!drawReg) {
      drawReg = true;
      Events.run(EventType.Trigger.draw, AutoEconomy::drawMarks);
    } 
    if (!scanning || marked == null || marked.length != i * Vars.world.height()) {
      scanning = true;
      marked = new boolean[i * Vars.world.height()];
      markIdx = new int[2048];
      markColor = new int[2048];
      markCount = 0;
      scanStart = Time.time;
      stillSince = Time.time;
      lastX = paramUnit.x;
      lastY = paramUnit.y;
      toast(tr4("Auto Dock: va ate onde ha minerios (ficam marcados), marque areia se quiser e aperte COMECAR.", "Auto Dock: fly to the ore (it gets marked), mark sand if you want, then press START.", "Auto Dock: ve donde hay mineral (se marca), marca arena si quieres y pulsa EMPEZAR.", "Auto Dock: летите к руде, отметьте песок и нажмите СТАРТ."));
      return false;
    } 
    if (Mathf.dst(paramUnit.x, paramUnit.y, lastX, lastY) > 16.0F) {
      lastX = paramUnit.x;
      lastY = paramUnit.y;
      stillSince = Time.time;
    } 
    if (Time.time >= scanAt) {
      scanAt = Time.time + 20.0F;
      int j = paramUnit.tileX();
      int k = paramUnit.tileY();
      byte b = 30;
      for (int m = k - b; m <= k + b; m++) {
        for (int n = j - b; n <= j + b; n++) {
          Tile tile;
          if (!Vars.world.tiles.in(n, m) || marked[m * i + n] || (tile = Vars.world.tile(n, m)) == null || tile.overlay() == null || (tile.overlay()).itemDrop == null || Mathf.dst(n, m, j, k) > b)
            continue; 
          marked[m * i + n] = true;
          if (markCount >= markIdx.length) {
            if (markCount >= 20000)
              continue; 
            markIdx = Arrays.copyOf(markIdx, markCount * 2);
            markColor = Arrays.copyOf(markColor, markCount * 2);
          } 
          markIdx[markCount] = m * i + n;
          markColor[markCount] = (tile.overlay()).itemDrop.color.rgba();
          markCount++;
          continue;
        } 
      } 
    } 
    input(paramUnit);
    if (startRequested) {
      startRequested = false;
      return true;
    } 
    return false;
  }
  
  private static void drawMarks() {
    if (marked == null || Vars.state == null || !Vars.state.isGame())
      return; 
    int i = Vars.world.width();
    Draw.z(120.0F);
    float f1 = Core.camera.position.x;
    float f2 = Core.camera.position.y;
    float f3 = Core.camera.width / 2.0F + 16.0F;
    float f4 = Core.camera.height / 2.0F + 16.0F;
    byte b;
    for (b = 0; b < markCount; b++) {
      int k = markIdx[b] % i;
      int j = markIdx[b] / i;
      if (Math.abs((k * 8) - f1) <= f3 && Math.abs((j * 8) - f2) <= f4) {
        tmpC.rgba8888(markColor[b]);
        Draw.color(tmpC, 0.55F);
        Fill.rect((k * 8), (j * 8), 7.0F, 7.0F);
      } 
    } 
    Draw.color(Items.sand.color, 0.6F);
    for (b = 0; b < sandList.size(); b++) {
      int k = ((Integer)sandList.get(b)).intValue() % i;
      int j = ((Integer)sandList.get(b)).intValue() / i;
      if (Math.abs((k * 8) - f1) <= f3 && Math.abs((j * 8) - f2) <= f4)
        Fill.rect((k * 8), (j * 8), 7.0F, 7.0F); 
    } 
    Draw.reset();
  }
  
  public static int[] markedTiles(Item paramItem) {
    try {
      if (paramItem == null || marked == null)
        return new int[0]; 
      ArrayList<Integer> arrayList = new ArrayList();
      if (paramItem == Items.sand) {
        arrayList.addAll(sandList);
      } else {
        int i = Vars.world.width();
        for (byte b1 = 0; b1 < markCount; b1++) {
          Tile tile = Vars.world.tile(markIdx[b1] % i, markIdx[b1] / i);
          if (tile != null && tile.overlay() != null && (tile.overlay()).itemDrop == paramItem)
            arrayList.add(Integer.valueOf(markIdx[b1])); 
        } 
      } 
      int[] arrayOfInt = new int[arrayList.size()];
      for (byte b = 0; b < arrayOfInt.length; b++)
        arrayOfInt[b] = ((Integer)arrayList.get(b)).intValue(); 
      return arrayOfInt;
    } catch (Throwable throwable) {
      return new int[0];
    } 
  }
  
  public static boolean enabled() {
    return Core.settings.getBool("lunarians-econ-order", false);
  }
  
  public static void setEnabled(boolean paramBoolean) {
    Core.settings.put("lunarians-econ-order", Boolean.valueOf(paramBoolean));
    if (!paramBoolean)
      abort(true); 
  }
  
  public static void init() {
    recoverProfile();
    Events.run(EventType.Trigger.update, AutoEconomy::update);
    Events.on(EventType.WorldLoadEvent.class, paramWorldLoadEvent -> {
          finished = false;
          autoAt = 0.0F;
          abort(true);
        });
  }
  
  public static void abort() {
    abort(false);
  }
  
  public static void abort(boolean paramBoolean) {
    if (paramBoolean)
      clearMarks(); 
    restoreProfile();
    active = false;
    adopted = false;
    inUnits = false;
    sawRunning = false;
    stages.clear();
    if (AutoDock.isUnitsMode())
      AutoDock.stopUnits(); 
  }
  
  public static void applyProfile() {
    if (snap != null)
      return; 
    snap = new Object[] { LunariansSettings.drillBlock, Integer.valueOf(LunariansSettings.drillMaxTiles), Integer.valueOf(LunariansSettings.drillMinOres), Integer.valueOf(LunariansSettings.drillOptQuality), Boolean.valueOf(LunariansSettings.drillPlaceWaterPower), Integer.valueOf(LunariansSettings.drillExitDir), Integer.valueOf(LunariansSettings.drillLayoutMode), Boolean.valueOf(LunariansSettings.drillPerfect) };
    Core.settings.put("lunarians-profile-backup", snap[0] + "|" + snap[1] + "|" + snap[2] + "|" + snap[3] + "|" + snap[4] + "|" + snap[5] + "|" + snap[6] + "|" + snap[7]);
    LunariansSettings.drillBlock = "mechanical-drill";
    LunariansSettings.drillMaxTiles = 400;
    LunariansSettings.drillMinOres = 1;
    LunariansSettings.drillOptQuality = 10;
    LunariansSettings.drillPlaceWaterPower = false;
    LunariansSettings.drillExitDir = 0;
    LunariansSettings.drillLayoutMode = 0;
    LunariansSettings.drillPerfect = false;
  }
  
  public static void restoreProfile() {
    if (snap == null)
      return; 
    LunariansSettings.drillBlock = (String)snap[0];
    LunariansSettings.drillMaxTiles = ((Integer)snap[1]).intValue();
    LunariansSettings.drillMinOres = ((Integer)snap[2]).intValue();
    LunariansSettings.drillOptQuality = ((Integer)snap[3]).intValue();
    LunariansSettings.drillPlaceWaterPower = ((Boolean)snap[4]).booleanValue();
    LunariansSettings.drillExitDir = ((Integer)snap[5]).intValue();
    LunariansSettings.drillLayoutMode = ((Integer)snap[6]).intValue();
    LunariansSettings.drillPerfect = ((Boolean)snap[7]).booleanValue();
    snap = null;
    Core.settings.remove("lunarians-profile-backup");
  }
  
  private static void recoverProfile() {
    try {
      String str = Core.settings.getString("lunarians-profile-backup", "");
      if (str == null || str.isEmpty())
        return; 
      String[] arrayOfString = str.split("\\|");
      Core.settings.put("lunarians-drill-block", arrayOfString[0]);
      Core.settings.put("lunarians-drill-max-tiles", Integer.valueOf(Integer.parseInt(arrayOfString[1])));
      Core.settings.put("lunarians-drill-min-ores", Integer.valueOf(Integer.parseInt(arrayOfString[2])));
      Core.settings.put("lunarians-drill-opt", Integer.valueOf(Integer.parseInt(arrayOfString[3])));
      Core.settings.put("lunarians-drill-water-power", Boolean.valueOf(Boolean.parseBoolean(arrayOfString[4])));
      Core.settings.put("lunarians-drill-exit-dir", Integer.valueOf(Integer.parseInt(arrayOfString[5])));
      Core.settings.put("lunarians-drill-layout", Integer.valueOf(Integer.parseInt(arrayOfString[6])));
      Core.settings.put("lunarians-drill-perfect", Boolean.valueOf(Boolean.parseBoolean(arrayOfString[7])));
      LunariansSettings.drillBlock = arrayOfString[0];
      LunariansSettings.drillMaxTiles = Integer.parseInt(arrayOfString[1]);
      LunariansSettings.drillMinOres = Integer.parseInt(arrayOfString[2]);
      LunariansSettings.drillOptQuality = Integer.parseInt(arrayOfString[3]);
      LunariansSettings.drillPlaceWaterPower = Boolean.parseBoolean(arrayOfString[4]);
      LunariansSettings.drillExitDir = Integer.parseInt(arrayOfString[5]);
      LunariansSettings.drillLayoutMode = Integer.parseInt(arrayOfString[6]);
      LunariansSettings.drillPerfect = Boolean.parseBoolean(arrayOfString[7]);
      Core.settings.remove("lunarians-profile-backup");
    } catch (Throwable throwable) {
      Core.settings.remove("lunarians-profile-backup");
    } 
  }
  
  private static Field fld(String paramString) throws Exception {
    Field field = AutoSet.class.getDeclaredField(paramString);
    field.setAccessible(true);
    return field;
  }
  
  private static int geti(String paramString) throws Exception {
    return fld(paramString).getInt(null);
  }
  
  private static void seti(String paramString, int paramInt) throws Exception {
    fld(paramString).setInt(null, paramInt);
  }
  
  private static void setf(String paramString, float paramFloat) throws Exception {
    fld(paramString).setFloat(null, paramFloat);
  }
  
  private static void setb(String paramString, boolean paramBoolean) throws Exception {
    fld(paramString).setBoolean(null, paramBoolean);
  }
  
  private static void buildStages() {
    stages.clear();
    int i = Math.max(1, AutoSetConfig.oreCount(0));
    int j = Math.max(1, AutoSetConfig.oreCount(1));
    int k = Math.max(1, AutoSetConfig.oreCount(2));
    int m = Math.max(1, AutoSetConfig.oreCount(3));
    int n = Math.max(2, AutoSetConfig.oreCount(4));
    int i1 = Math.max(1, AutoSetConfig.oreCount(5));
    ArrayList<String> arrayList = new ArrayList();
    byte b2;
    for (b2 = 0; b2 < i; b2++)
      arrayList.add("copper"); 
    for (b2 = 0; b2 < j; b2++)
      arrayList.add("lead"); 
    stages.add(new Object[] { "Cobre+Chumbo", "drill", arrayList.toArray(new String[0]) });
    arrayList = new ArrayList<>();
    for (b2 = 0; b2 < Math.max(k, m); b2++) {
      if (b2 < k)
        arrayList.add("coal"); 
      if (b2 < m)
        arrayList.add("sand"); 
    } 
    stages.add(new Object[] { "Carvao+Areia", "drill", arrayList.toArray(new String[0]) });
    String[][] arrayOfString;
    for (String[] arrayOfString1 : arrayOfString = new String[][] { { "graphite", "0", "Grafite" }, { "metaglass", "2", "Metavidro" }, { "silicon", "1", "Silicio" } }) {
      int i2 = Math.max(1, AutoSetConfig.facCount(Integer.parseInt(arrayOfString1[1])));
      arrayList = new ArrayList<>();
      for (byte b = 0; b < i2; b++)
        arrayList.add(arrayOfString1[0]); 
      stages.add(new Object[] { arrayOfString1[2], "fac", arrayList.toArray(new String[0]) });
    } 
    arrayList = new ArrayList<>();
    byte b1;
    for (b1 = 0; b1 < n; b1++)
      arrayList.add("titanium"); 
    stages.add(new Object[] { "Titanio", "drill", arrayList.toArray(new String[0]) });
    stages.add(new Object[] { "Unidades", "units", new String[0] });
    arrayList = new ArrayList<>();
    for (b1 = 0; b1 < i1; b1++)
      arrayList.add("thorium"); 
    stages.add(new Object[] { "Torio", "drill", arrayList.toArray(new String[0]) });
  }
  
  private static void load(int paramInt, boolean paramBoolean) throws Exception {
    Object[] arrayOfObject = stages.get(paramInt);
    String str = (String)arrayOfObject[1];
    String[] arrayOfString = (String[])arrayOfObject[2];
    seti("runMode", str.equals("fac") ? 1 : 0);
    fld("oreOrder").set(null, str.equals("drill") ? arrayOfString : new String[0]);
    fld("factoryQueue").set(null, str.equals("fac") ? arrayOfString : new String[0]);
    seti("stepIndex", 0);
    ((ArrayList)fld("feedTargets").get(null)).clear();
    ((ArrayList)fld("feedItemNames").get(null)).clear();
    ((ArrayList)fld("feedReserved").get(null)).clear();
    ((HashMap)fld("feedLines").get(null)).clear();
    seti("feedIndex", 0);
    ((Seq)fld("pending").get(null)).clear();
    if (paramBoolean) {
      seti("phase", 3);
      setb("hasArea", true);
      setb("centerChosen", true);
      seti("cx", areaX);
      seti("cy", areaY);
      setf("radius", areaR);
      seti("steamDone", 1048576);
      setf("lastProgress", Time.time);
      seti("lastPending", 0);
    } 
    setf("nextStepAt", Time.time + 10.0F);
    lastStep = 0;
    lastTotal = arrayOfString.length;
    sawRunning = true;
    toast("Economia: etapa " + (paramInt + 1) + "/" + stages.size() + " - " + String.valueOf(arrayOfObject[0]));
  }
  
  private static void update() {
    if ((!enabled() && !LunariansSettings.dockAuto && AutoSetConfig.mode() != 2 && !scanning) || Vars.state == null || !Vars.state.isGame() || Vars.player == null)
      return; 
    try {
      boolean bool = AutoSet.isRunning();
      if (!adopted && !mapWanted && AutoSetConfig.mode() == 2 && geti("phase") == 2) {
        seti("phase", 0);
        mapWanted = true;
        finished = false;
        return;
      } 
      if (!adopted && !bool && mapWanted && Vars.player.closestCore() != null && Vars.player.unit() != null && Vars.player.unit().canBuild()) {
        if (scan(Vars.player.unit())) {
          areaX = Vars.player.unit().tileX();
          areaY = Vars.player.unit().tileY();
          float f = 0.0F;
          int i = Vars.world.width();
          for (byte b = 0; b < markCount; b++)
            f = Math.max(f, Mathf.dst((markIdx[b] % i), (markIdx[b] / i), areaX, areaY)); 
          areaR = Math.max(AutoSetConfig.allRadius(), Math.min(80.0F, f + 4.0F));
          boolean[] arrayOfBoolean = new boolean[Vars.world.width() * Vars.world.height()];
          applySand(arrayOfBoolean);
          fld("used").set(null, arrayOfBoolean);
          applyProfile();
          buildStages();
          idx = 0;
          adopted = true;
          active = true;
          scanning = false;
          mapWanted = false;
          load(0, true);
          toast(tr4("Mapeamento pronto: Auto Set na sua posicao, depois schematics.", "Mapping done: Auto Set at your position, then schematics.", "Mapeo listo: Auto Set en tu posicion, luego esquemas.", "Карта готова: Auto Set в вашей точке, потом схемы."));
        } 
        return;
      } 
      if (!adopted) {
        if (bool) {
          areaX = geti("cx");
          areaY = geti("cy");
          areaR = fld("radius").getFloat(null);
          buildStages();
          idx = 0;
          adopted = true;
          active = true;
          load(0, false);
        } 
        return;
      } 
      if (inUnits) {
        if (Time.time > unitsDeadline || AutoDock.unitsDone()) {
          AutoDock.stopUnits();
          inUnits = false;
          advance();
        } 
        return;
      } 
      if (bool) {
        lastStep = geti("stepIndex");
        return;
      } 
      if (sawRunning) {
        sawRunning = false;
        if (lastStep >= lastTotal) {
          advance();
        } else {
          toast("Economia em ordem cancelada.");
          finished = true;
          abort();
        } 
      } 
    } catch (Throwable throwable) {
      Log.err("[Lunarians] AutoEconomy", throwable);
      toast("Auto All erro: " + throwable);
      abort(false);
    } 
  }
  
  private static void advance() throws Exception {
    if (++idx >= stages.size()) {
      toast("Economia completa: Auto Dock assume os schematics.");
      active = false;
      adopted = false;
      finished = true;
      restoreProfile();
      LunariansSettings.dockAuto = true;
      LunariansSettings.save();
      AutoDock.finishEconomy();
      return;
    } 
    Object[] arrayOfObject = stages.get(idx);
    if ("units".equals(arrayOfObject[1])) {
      toast("Economia: etapa " + (idx + 1) + "/" + stages.size() + " - Unidades (Auto Dock)");
      LunariansSettings.dockAuto = true;
      AutoDock.startUnits();
      inUnits = true;
      unitsDeadline = Time.time + 14400.0F;
      return;
    } 
    load(idx, true);
  }
  
  private static void toast(String paramString) {
    Log.info("[Lunarians] " + paramString);
    if (Vars.ui != null)
      Vars.ui.showInfoToast(paramString, 4.0F); 
  }
  
  private static int[] markIdx = new int[0];
  
  private static int markCount;
  
  private static int[] markColor = new int[0];
  
  private static boolean[] marked;
  
  private static float scanAt;
  
  private static float stillSince;
  
  private static float lastX;
  
  private static float lastY;
  
  private static float scanStart;
  
  private static boolean drawReg;
  
  private static final Color tmpC = new Color();
  
  private static int sandMode;
  
  private static boolean startRequested;
  
  private static boolean mapWanted;
  
  private static boolean[] sandMarked;
  
  private static final ArrayList<Integer> sandList = new ArrayList<>();
  
  private static boolean panelBuilt;
  
  private static Object[] snap;
  
  static {
    lastStep = 0;
    lastTotal = 0;
    sawRunning = false;
    unitsDeadline = 0.0F;
    inUnits = false;
  }
}
