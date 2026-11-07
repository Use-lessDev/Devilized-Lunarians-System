/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.Events
 *  arc.graphics.Color
 *  arc.graphics.g2d.Draw
 *  arc.graphics.g2d.Fill
 *  arc.math.Mathf
 *  arc.struct.Seq
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.content.Items
 *  mindustry.core.World
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.EventType$WorldLoadEvent
 *  mindustry.gen.Unit
 *  mindustry.type.Item
 *  mindustry.ui.Styles
 *  mindustry.world.Tile
 */
package lunarians;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import lunarians.AutoDock;
import lunarians.AutoSet;
import lunarians.AutoSetConfig;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
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
    private static int[] markIdx;
    private static int markCount;
    private static int[] markColor;
    private static boolean[] marked;
    private static float scanAt;
    private static float stillSince;
    private static float lastX;
    private static float lastY;
    private static float scanStart;
    private static boolean drawReg;
    private static final Color tmpC;
    private static int sandMode;
    private static boolean startRequested;
    private static boolean mapWanted;
    private static boolean[] sandMarked;
    private static final ArrayList<Integer> sandList;
    private static boolean panelBuilt;
    private static Object[] snap;

    private static String tr4(String string, String string2, String string3, String string4) {
        String string5 = LunariansI18n.getLanguage();
        if ("pt".equals(string5)) {
            return string;
        }
        if ("es".equals(string5)) {
            return string3;
        }
        if ("ru".equals(string5)) {
            return string4;
        }
        return string2;
    }

    private static void buildPanel() {
        if (panelBuilt || Vars.ui == null || Vars.ui.hudGroup == null) {
            return;
        }
        panelBuilt = true;
        Vars.ui.hudGroup.fill(table2 -> {
            table2.top();
            table2.visible(() -> scanning && Vars.state != null && Vars.state.isGame());
            table2.table(Styles.black6, table -> {
                table.defaults().pad(3.0f).height(44.0f).minWidth(96.0f);
                table.button(AutoEconomy.tr4("Marcar areia", "Mark sand", "Marcar arena", "\u041f\u0435\u0441\u043e\u043a"), Styles.togglet, () -> {
                    sandMode = sandMode == 1 ? 0 : 1;
                }).checked(textButton -> sandMode == 1);
                table.button(AutoEconomy.tr4("Apagar", "Erase", "Borrar", "\u0421\u0442\u0435\u0440\u0435\u0442\u044c"), Styles.togglet, () -> {
                    sandMode = sandMode == 2 ? 0 : 2;
                }).checked(textButton -> sandMode == 2);
                table.button(AutoEconomy.tr4("COMECAR", "START", "EMPEZAR", "\u0421\u0422\u0410\u0420\u0422"), () -> {
                    startRequested = true;
                });
                table.button(AutoEconomy.tr4("Cancelar", "Cancel", "Cancelar", "\u041e\u0442\u043c\u0435\u043d\u0430"), () -> {
                    finished = true;
                    AutoEconomy.abort(true);
                    AutoEconomy.toast(AutoEconomy.tr4("Auto Dock: cancelado.", "Auto Dock: cancelled.", "Auto Dock: cancelado.", "Auto Dock: \u043e\u0442\u043c\u0435\u043d\u0435\u043d\u043e."));
                });
            }).padTop(110.0f);
        });
    }

    public static boolean isSandTile(Tile tile) {
        if (tile == null) {
            return false;
        }
        try {
            if (tile.floor() != null) {
                if (tile.floor().isLiquid || tile.floor().liquidDrop != null) {
                    return false;
                }
                if (tile.floor().itemDrop == Items.sand) {
                    return true;
                }
                String string = tile.floor().name;
                if (string != null) {
                    String string2 = string.toLowerCase();
                    if (string2.contains("water") || string2.contains("deep") || string2.contains("pool")) {
                        return false;
                    }
                    if (string2.contains("darksand") || string2.equals("sand") || string2.contains("sand-floor") || string2.contains("sand") && !string2.contains("sandstone")) {
                        return true;
                    }
                }
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
            if (tile.drop() == Items.sand) {
                return true;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
            if (tile.overlay() != null && tile.overlay().itemDrop == Items.sand) {
                return true;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return false;
    }

    private static void input(Unit unit) {
        AutoEconomy.buildPanel();
        if (sandMode == 0 || !Core.input.justTouched() || Core.scene.hasMouse()) {
            return;
        }
        int n = Vars.world.width();
        if (sandMarked == null || sandMarked.length != n * Vars.world.height()) {
            sandMarked = new boolean[n * Vars.world.height()];
            sandList.clear();
        }
        int n2 = World.toTile((float)Core.input.mouseWorldX());
        int n3 = World.toTile((float)Core.input.mouseWorldY());
        int n4 = 3;
        for (int i = n3 - n4; i <= n3 + n4; ++i) {
            for (int j = n2 - n4; j <= n2 + n4; ++j) {
                if (!Vars.world.tiles.in(j, i) || Mathf.dst((float)j, (float)i, (float)n2, (float)n3) > (float)n4 + 0.5f) continue;
                int n5 = i * n + j;
                if (sandMode == 1) {
                    Tile tile = Vars.world.tile(j, i);
                    if (sandMarked[n5] || tile == null || !AutoEconomy.isSandTile(tile)) continue;
                    AutoEconomy.sandMarked[n5] = true;
                    sandList.add(n5);
                    continue;
                }
                if (!sandMarked[n5]) continue;
                AutoEconomy.sandMarked[n5] = false;
                sandList.remove((Object)n5);
            }
        }
    }

    private static void applySand(boolean[] blArray) {
        if (sandList.isEmpty() || sandMarked == null || blArray == null) {
            return;
        }
        int n = Vars.world.width();
        for (int i = Math.max(0, areaY - (int)areaR - 1); i <= Math.min(Vars.world.height() - 1, areaY + (int)areaR + 1); ++i) {
            for (int j = Math.max(0, areaX - (int)areaR - 1); j <= Math.min(n - 1, areaX + (int)areaR + 1); ++j) {
                Tile tile;
                int n2 = i * n + j;
                if (sandMarked[n2] || n2 >= blArray.length || (tile = Vars.world.tile(j, i)) == null || !AutoEconomy.isSandTile(tile)) continue;
                blArray[n2] = true;
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

    private static boolean scan(Unit unit) {
        int n = Vars.world.width();
        if (!drawReg) {
            drawReg = true;
            Events.run((Object)EventType.Trigger.draw, AutoEconomy::drawMarks);
        }
        if (!scanning || marked == null || marked.length != n * Vars.world.height()) {
            scanning = true;
            marked = new boolean[n * Vars.world.height()];
            markIdx = new int[2048];
            markColor = new int[2048];
            markCount = 0;
            scanStart = Time.time;
            stillSince = Time.time;
            lastX = unit.x;
            lastY = unit.y;
            AutoEconomy.toast(AutoEconomy.tr4("Auto Dock: va ate onde ha minerios (ficam marcados), marque areia se quiser e aperte COMECAR.", "Auto Dock: fly to the ore (it gets marked), mark sand if you want, then press START.", "Auto Dock: ve donde hay mineral (se marca), marca arena si quieres y pulsa EMPEZAR.", "Auto Dock: \u043b\u0435\u0442\u0438\u0442\u0435 \u043a \u0440\u0443\u0434\u0435, \u043e\u0442\u043c\u0435\u0442\u044c\u0442\u0435 \u043f\u0435\u0441\u043e\u043a \u0438 \u043d\u0430\u0436\u043c\u0438\u0442\u0435 \u0421\u0422\u0410\u0420\u0422."));
            return false;
        }
        if (Mathf.dst((float)unit.x, (float)unit.y, (float)lastX, (float)lastY) > 16.0f) {
            lastX = unit.x;
            lastY = unit.y;
            stillSince = Time.time;
        }
        if (Time.time >= scanAt) {
            scanAt = Time.time + 20.0f;
            int n2 = unit.tileX();
            int n3 = unit.tileY();
            int n4 = 30;
            for (int i = n3 - n4; i <= n3 + n4; ++i) {
                for (int j = n2 - n4; j <= n2 + n4; ++j) {
                    Tile tile;
                    if (!Vars.world.tiles.in(j, i) || marked[i * n + j] || (tile = Vars.world.tile(j, i)) == null || tile.overlay() == null || tile.overlay().itemDrop == null || Mathf.dst((float)j, (float)i, (float)n2, (float)n3) > (float)n4) continue;
                    AutoEconomy.marked[i * n + j] = true;
                    if (markCount >= markIdx.length) {
                        if (markCount >= 20000) continue;
                        markIdx = Arrays.copyOf(markIdx, markCount * 2);
                        markColor = Arrays.copyOf(markColor, markCount * 2);
                    }
                    AutoEconomy.markIdx[AutoEconomy.markCount] = i * n + j;
                    AutoEconomy.markColor[AutoEconomy.markCount] = tile.overlay().itemDrop.color.rgba();
                    ++markCount;
                }
            }
        }
        AutoEconomy.input(unit);
        if (startRequested) {
            startRequested = false;
            return true;
        }
        return false;
    }

    private static void drawMarks() {
        int n;
        int n2;
        int n3;
        if (marked == null || Vars.state == null || !Vars.state.isGame()) {
            return;
        }
        int n4 = Vars.world.width();
        Draw.z((float)120.0f);
        float f = Core.camera.position.x;
        float f2 = Core.camera.position.y;
        float f3 = Core.camera.width / 2.0f + 16.0f;
        float f4 = Core.camera.height / 2.0f + 16.0f;
        for (n3 = 0; n3 < markCount; ++n3) {
            n2 = markIdx[n3] % n4;
            n = markIdx[n3] / n4;
            if (Math.abs((float)(n2 * 8) - f) > f3 || Math.abs((float)(n * 8) - f2) > f4) continue;
            tmpC.rgba8888(markColor[n3]);
            Draw.color((Color)tmpC, (float)0.55f);
            Fill.rect((float)(n2 * 8), (float)(n * 8), (float)7.0f, (float)7.0f);
        }
        Draw.color((Color)Items.sand.color, (float)0.6f);
        for (n3 = 0; n3 < sandList.size(); ++n3) {
            n2 = sandList.get(n3) % n4;
            n = sandList.get(n3) / n4;
            if (Math.abs((float)(n2 * 8) - f) > f3 || Math.abs((float)(n * 8) - f2) > f4) continue;
            Fill.rect((float)(n2 * 8), (float)(n * 8), (float)7.0f, (float)7.0f);
        }
        Draw.reset();
    }

    public static int[] markedTiles(Item item) {
        try {
            int n;
            if (item == null || marked == null) {
                return new int[0];
            }
            ArrayList<Integer> arrayList = new ArrayList<Integer>();
            if (item == Items.sand) {
                arrayList.addAll(sandList);
            } else {
                int n2 = Vars.world.width();
                for (n = 0; n < markCount; ++n) {
                    Tile tile = Vars.world.tile(markIdx[n] % n2, markIdx[n] / n2);
                    if (tile == null || tile.overlay() == null || tile.overlay().itemDrop != item) continue;
                    arrayList.add(markIdx[n]);
                }
            }
            int[] nArray = new int[arrayList.size()];
            for (n = 0; n < nArray.length; ++n) {
                nArray[n] = (Integer)arrayList.get(n);
            }
            return nArray;
        }
        catch (Throwable throwable) {
            return new int[0];
        }
    }

    public static boolean enabled() {
        return Core.settings.getBool(KEY, false);
    }

    public static void setEnabled(boolean bl) {
        Core.settings.put(KEY, (Object)bl);
        if (!bl) {
            AutoEconomy.abort(true);
        }
    }

    public static void init() {
        AutoEconomy.recoverProfile();
        Events.run((Object)EventType.Trigger.update, AutoEconomy::update);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            finished = false;
            autoAt = 0.0f;
            AutoEconomy.abort(true);
        });
    }

    public static void abort() {
        AutoEconomy.abort(false);
    }

    public static void abort(boolean bl) {
        if (bl) {
            AutoEconomy.clearMarks();
        }
        AutoEconomy.restoreProfile();
        active = false;
        adopted = false;
        inUnits = false;
        sawRunning = false;
        stages.clear();
        if (AutoDock.isUnitsMode()) {
            AutoDock.stopUnits();
        }
    }

    public static void applyProfile() {
        if (snap != null) {
            return;
        }
        snap = new Object[]{LunariansSettings.drillBlock, LunariansSettings.drillMaxTiles, LunariansSettings.drillMinOres, LunariansSettings.drillOptQuality, LunariansSettings.drillPlaceWaterPower, LunariansSettings.drillExitDir, LunariansSettings.drillLayoutMode, LunariansSettings.drillPerfect};
        Core.settings.put("lunarians-profile-backup", (Object)(snap[0] + "|" + snap[1] + "|" + snap[2] + "|" + snap[3] + "|" + snap[4] + "|" + snap[5] + "|" + snap[6] + "|" + snap[7]));
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
        if (snap == null) {
            return;
        }
        LunariansSettings.drillBlock = (String)snap[0];
        LunariansSettings.drillMaxTiles = (Integer)snap[1];
        LunariansSettings.drillMinOres = (Integer)snap[2];
        LunariansSettings.drillOptQuality = (Integer)snap[3];
        LunariansSettings.drillPlaceWaterPower = (Boolean)snap[4];
        LunariansSettings.drillExitDir = (Integer)snap[5];
        LunariansSettings.drillLayoutMode = (Integer)snap[6];
        LunariansSettings.drillPerfect = (Boolean)snap[7];
        snap = null;
        Core.settings.remove("lunarians-profile-backup");
    }

    private static void recoverProfile() {
        try {
            String string = Core.settings.getString("lunarians-profile-backup", "");
            if (string == null || string.isEmpty()) {
                return;
            }
            String[] stringArray = string.split("\\|");
            Core.settings.put("lunarians-drill-block", (Object)stringArray[0]);
            Core.settings.put("lunarians-drill-max-tiles", (Object)Integer.parseInt(stringArray[1]));
            Core.settings.put("lunarians-drill-min-ores", (Object)Integer.parseInt(stringArray[2]));
            Core.settings.put("lunarians-drill-opt", (Object)Integer.parseInt(stringArray[3]));
            Core.settings.put("lunarians-drill-water-power", (Object)Boolean.parseBoolean(stringArray[4]));
            Core.settings.put("lunarians-drill-exit-dir", (Object)Integer.parseInt(stringArray[5]));
            Core.settings.put("lunarians-drill-layout", (Object)Integer.parseInt(stringArray[6]));
            Core.settings.put("lunarians-drill-perfect", (Object)Boolean.parseBoolean(stringArray[7]));
            LunariansSettings.drillBlock = stringArray[0];
            LunariansSettings.drillMaxTiles = Integer.parseInt(stringArray[1]);
            LunariansSettings.drillMinOres = Integer.parseInt(stringArray[2]);
            LunariansSettings.drillOptQuality = Integer.parseInt(stringArray[3]);
            LunariansSettings.drillPlaceWaterPower = Boolean.parseBoolean(stringArray[4]);
            LunariansSettings.drillExitDir = Integer.parseInt(stringArray[5]);
            LunariansSettings.drillLayoutMode = Integer.parseInt(stringArray[6]);
            LunariansSettings.drillPerfect = Boolean.parseBoolean(stringArray[7]);
            Core.settings.remove("lunarians-profile-backup");
        }
        catch (Throwable throwable) {
            Core.settings.remove("lunarians-profile-backup");
        }
    }

    private static Field fld(String string) throws Exception {
        Field field = AutoSet.class.getDeclaredField(string);
        field.setAccessible(true);
        return field;
    }

    private static int geti(String string) throws Exception {
        return AutoEconomy.fld(string).getInt(null);
    }

    private static void seti(String string, int n) throws Exception {
        AutoEconomy.fld(string).setInt(null, n);
    }

    private static void setf(String string, float f) throws Exception {
        AutoEconomy.fld(string).setFloat(null, f);
    }

    private static void setb(String string, boolean bl) throws Exception {
        AutoEconomy.fld(string).setBoolean(null, bl);
    }

    private static void buildStages() {
        int n;
        int n2;
        stages.clear();
        int n3 = Math.max(1, AutoSetConfig.oreCount(0));
        int n4 = Math.max(1, AutoSetConfig.oreCount(1));
        int n5 = Math.max(1, AutoSetConfig.oreCount(2));
        int n6 = Math.max(1, AutoSetConfig.oreCount(3));
        int n7 = Math.max(2, AutoSetConfig.oreCount(4));
        int n8 = Math.max(1, AutoSetConfig.oreCount(5));
        ArrayList<String> arrayList = new ArrayList<String>();
        for (n2 = 0; n2 < n3; ++n2) {
            arrayList.add("copper");
        }
        for (n2 = 0; n2 < n4; ++n2) {
            arrayList.add("lead");
        }
        stages.add(new Object[]{"Cobre+Chumbo", "drill", arrayList.toArray(new String[0])});
        arrayList = new ArrayList();
        for (n2 = 0; n2 < Math.max(n5, n6); ++n2) {
            if (n2 < n5) {
                arrayList.add("coal");
            }
            if (n2 >= n6) continue;
            arrayList.add("sand");
        }
        stages.add(new Object[]{"Carvao+Areia", "drill", arrayList.toArray(new String[0])});
        String[][] stringArrayArray = new String[][]{{"graphite", "0", "Grafite"}, {"metaglass", "2", "Metavidro"}, {"silicon", "1", "Silicio"}};
        for (String[] stringArray : stringArrayArray) {
            int n9 = Math.max(1, AutoSetConfig.facCount(Integer.parseInt(stringArray[1])));
            arrayList = new ArrayList();
            for (int i = 0; i < n9; ++i) {
                arrayList.add(stringArray[0]);
            }
            stages.add(new Object[]{stringArray[2], "fac", arrayList.toArray(new String[0])});
        }
        arrayList = new ArrayList();
        for (n = 0; n < n7; ++n) {
            arrayList.add("titanium");
        }
        stages.add(new Object[]{"Titanio", "drill", arrayList.toArray(new String[0])});
        stages.add(new Object[]{"Unidades", "units", new String[0]});
        arrayList = new ArrayList();
        for (n = 0; n < n8; ++n) {
            arrayList.add("thorium");
        }
        stages.add(new Object[]{"Torio", "drill", arrayList.toArray(new String[0])});
    }

    private static void load(int n, boolean bl) throws Exception {
        Object[] objectArray = stages.get(n);
        String string = (String)objectArray[1];
        String[] stringArray = (String[])objectArray[2];
        AutoEconomy.seti("runMode", string.equals("fac") ? 1 : 0);
        AutoEconomy.fld("oreOrder").set(null, string.equals("drill") ? stringArray : new String[]{});
        AutoEconomy.fld("factoryQueue").set(null, string.equals("fac") ? stringArray : new String[]{});
        AutoEconomy.seti("stepIndex", 0);
        ((ArrayList)AutoEconomy.fld("feedTargets").get(null)).clear();
        ((ArrayList)AutoEconomy.fld("feedItemNames").get(null)).clear();
        ((ArrayList)AutoEconomy.fld("feedReserved").get(null)).clear();
        ((HashMap)AutoEconomy.fld("feedLines").get(null)).clear();
        AutoEconomy.seti("feedIndex", 0);
        ((Seq)AutoEconomy.fld("pending").get(null)).clear();
        if (bl) {
            AutoEconomy.seti("phase", 3);
            AutoEconomy.setb("hasArea", true);
            AutoEconomy.setb("centerChosen", true);
            AutoEconomy.seti("cx", areaX);
            AutoEconomy.seti("cy", areaY);
            AutoEconomy.setf("radius", areaR);
            AutoEconomy.seti("steamDone", 0x100000);
            AutoEconomy.setf("lastProgress", Time.time);
            AutoEconomy.seti("lastPending", 0);
        }
        AutoEconomy.setf("nextStepAt", Time.time + 10.0f);
        lastStep = 0;
        lastTotal = stringArray.length;
        sawRunning = true;
        AutoEconomy.toast("Economia: etapa " + (n + 1) + "/" + stages.size() + " - " + String.valueOf(objectArray[0]));
    }

    private static void update() {
        if (!AutoEconomy.enabled() && !LunariansSettings.dockAuto && AutoSetConfig.mode() != 2 && !scanning || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
            return;
        }
        try {
            boolean bl = AutoSet.isRunning();
            if (!adopted && !mapWanted && AutoSetConfig.mode() == 2 && AutoEconomy.geti("phase") == 2) {
                AutoEconomy.seti("phase", 0);
                mapWanted = true;
                finished = false;
                return;
            }
            if (!adopted && !bl && mapWanted && Vars.player.closestCore() != null && Vars.player.unit() != null && Vars.player.unit().canBuild()) {
                if (AutoEconomy.scan(Vars.player.unit())) {
                    areaX = Vars.player.unit().tileX();
                    areaY = Vars.player.unit().tileY();
                    float f = 0.0f;
                    int n = Vars.world.width();
                    for (int i = 0; i < markCount; ++i) {
                        f = Math.max(f, Mathf.dst((float)(markIdx[i] % n), (float)(markIdx[i] / n), (float)areaX, (float)areaY));
                    }
                    areaR = Math.max((float)AutoSetConfig.allRadius(), Math.min(80.0f, f + 4.0f));
                    boolean[] blArray = new boolean[Vars.world.width() * Vars.world.height()];
                    AutoEconomy.applySand(blArray);
                    AutoEconomy.fld("used").set(null, blArray);
                    AutoEconomy.applyProfile();
                    AutoEconomy.buildStages();
                    idx = 0;
                    adopted = true;
                    active = true;
                    scanning = false;
                    mapWanted = false;
                    AutoEconomy.load(0, true);
                    AutoEconomy.toast(AutoEconomy.tr4("Mapeamento pronto: Auto Set na sua posicao, depois schematics.", "Mapping done: Auto Set at your position, then schematics.", "Mapeo listo: Auto Set en tu posicion, luego esquemas.", "\u041a\u0430\u0440\u0442\u0430 \u0433\u043e\u0442\u043e\u0432\u0430: Auto Set \u0432 \u0432\u0430\u0448\u0435\u0439 \u0442\u043e\u0447\u043a\u0435, \u043f\u043e\u0442\u043e\u043c \u0441\u0445\u0435\u043c\u044b."));
                }
                return;
            }
            if (!adopted) {
                if (bl) {
                    areaX = AutoEconomy.geti("cx");
                    areaY = AutoEconomy.geti("cy");
                    areaR = AutoEconomy.fld("radius").getFloat(null);
                    AutoEconomy.buildStages();
                    idx = 0;
                    adopted = true;
                    active = true;
                    AutoEconomy.load(0, false);
                }
                return;
            }
            if (inUnits) {
                if (Time.time > unitsDeadline || AutoDock.unitsDone()) {
                    AutoDock.stopUnits();
                    inUnits = false;
                    AutoEconomy.advance();
                }
                return;
            }
            if (bl) {
                lastStep = AutoEconomy.geti("stepIndex");
                return;
            }
            if (sawRunning) {
                sawRunning = false;
                if (lastStep >= lastTotal) {
                    AutoEconomy.advance();
                } else {
                    AutoEconomy.toast("Economia em ordem cancelada.");
                    finished = true;
                    AutoEconomy.abort();
                }
            }
        }
        catch (Throwable throwable) {
            Log.err((String)"[Lunarians] AutoEconomy", (Throwable)throwable);
            AutoEconomy.toast("Auto All erro: " + throwable);
            AutoEconomy.abort(false);
        }
    }

    private static void advance() throws Exception {
        if (++idx >= stages.size()) {
            AutoEconomy.toast("Economia completa: Auto Dock assume os schematics.");
            active = false;
            adopted = false;
            finished = true;
            AutoEconomy.restoreProfile();
            LunariansSettings.dockAuto = true;
            LunariansSettings.save();
            AutoDock.finishEconomy();
            return;
        }
        Object[] objectArray = stages.get(idx);
        if ("units".equals(objectArray[1])) {
            AutoEconomy.toast("Economia: etapa " + (idx + 1) + "/" + stages.size() + " - Unidades (Auto Dock)");
            LunariansSettings.dockAuto = true;
            AutoDock.startUnits();
            inUnits = true;
            unitsDeadline = Time.time + 14400.0f;
            return;
        }
        AutoEconomy.load(idx, true);
    }

    private static void toast(String string) {
        Log.info((Object)("[Lunarians] " + string));
        if (Vars.ui != null) {
            Vars.ui.showInfoToast(string, 4.0f);
        }
    }

    static {
        markIdx = new int[0];
        markColor = new int[0];
        tmpC = new Color();
        sandList = new ArrayList();
        lastStep = 0;
        lastTotal = 0;
        sawRunning = false;
        unitsDeadline = 0.0f;
        inUnits = false;
    }
}

