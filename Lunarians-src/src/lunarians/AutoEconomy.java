package lunarians;

import arc.Core;
import arc.Events;
import arc.func.Boolf;
import arc.func.Boolp;
import arc.func.Cons;
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

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoEconomy.class */
public class AutoEconomy {
    public static final String KEY = "lunarians-econ-order";
    private static int areaX;
    private static int areaY;
    private static float areaR;
    private static boolean finished;
    private static float autoAt;
    private static boolean scanning;
    private static int markCount;
    private static boolean[] marked;
    private static float scanAt;
    private static float stillSince;
    private static float lastX;
    private static float lastY;
    private static float scanStart;
    private static boolean drawReg;
    private static int sandMode;
    private static boolean startRequested;
    private static boolean mapWanted;
    private static boolean[] sandMarked;
    private static boolean panelBuilt;
    private static Object[] snap;
    public static boolean active = false;
    private static final ArrayList<Object[]> stages = new ArrayList<>();
    private static int idx = 0;
    private static boolean adopted = false;
    private static int[] markIdx = new int[0];
    private static int[] markColor = new int[0];
    private static final Color tmpC = new Color();
    private static final ArrayList<Integer> sandList = new ArrayList<>();
    private static int lastStep = 0;
    private static int lastTotal = 0;
    private static boolean sawRunning = false;
    private static float unitsDeadline = 0.0f;
    private static boolean inUnits = false;

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoEconomy$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Cons {
        public final void get(Object obj) {
            AutoEconomy.lambda$buildPanel$8((Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoEconomy$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass1 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoEconomy.lambda$buildPanel$1();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoEconomy$10, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass10 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoEconomy.$r8$lambda$RlXOXlbWI_8bIbop9qbRbkBhs5E();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoEconomy$11, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass11 implements Cons {
        public final void get(Object obj) {
            AutoEconomy.lambda$init$9((EventType.WorldLoadEvent) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoEconomy$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass2 implements Boolf {
        public final boolean get(Object obj) {
            return AutoEconomy.lambda$buildPanel$2((TextButton) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoEconomy$3, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass3 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoEconomy.lambda$buildPanel$3();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoEconomy$4, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass4 implements Boolf {
        public final boolean get(Object obj) {
            return AutoEconomy.lambda$buildPanel$4((TextButton) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoEconomy$5, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass5 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoEconomy.startRequested = true;
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoEconomy$6, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass6 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoEconomy.lambda$buildPanel$6();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoEconomy$7, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass7 implements Boolp {
        public final boolean get() {
            return AutoEconomy.lambda$buildPanel$0();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoEconomy$8, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass8 implements Cons {
        public final void get(Object obj) {
            AutoEconomy.lambda$buildPanel$7((Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoEconomy$9, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass9 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoEconomy.$r8$lambda$x3iDRh6blLMu5PgXgFszwfSsACI();
        }
    }

    private static String tr4(String str, String str2, String str3, String str4) {
        String language = LunariansI18n.getLanguage();
        if ("pt".equals(language)) {
            return str;
        }
        if ("es".equals(language)) {
            return str3;
        }
        if ("ru".equals(language)) {
            return str4;
        }
        return str2;
    }

    private static void buildPanel() {
        if (panelBuilt || Vars.ui == null || Vars.ui.hudGroup == null) {
            return;
        }
        panelBuilt = true;
        Vars.ui.hudGroup.fill(table -> {
            table.top();
            table.visible(() -> {
                return scanning && Vars.state != null && Vars.state.isGame();
            });
            table.table(Styles.black6, table -> {
                table.defaults().pad(3.0f).height(44.0f).minWidth(96.0f);
                table.button(tr4("Marcar areia", "Mark sand", "Marcar arena", "Песок"), Styles.togglet, () -> {
                    sandMode = sandMode == 1 ? 0 : 1;
                }).checked(textButton -> {
                    return sandMode == 1;
                });
                table.button(tr4("Apagar", "Erase", "Borrar", "Стереть"), Styles.togglet, () -> {
                    sandMode = sandMode == 2 ? 0 : 2;
                }).checked(textButton2 -> {
                    return sandMode == 2;
                });
                table.button(tr4("COMECAR", "START", "EMPEZAR", "СТАРТ"), () -> {
                    startRequested = true;
                });
                table.button(tr4("Cancelar", "Cancel", "Cancelar", "Отмена"), () -> {
                    finished = true;
                    abort(true);
                    toast(tr4("Auto Dock: cancelado.", "Auto Dock: cancelled.", "Auto Dock: cancelado.", "Auto Dock: отменено."));
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
                String str = tile.floor().name;
                if (str != null) {
                    String lowerCase = str.toLowerCase();
                    if (lowerCase.contains("water") || lowerCase.contains("deep") || lowerCase.contains("pool")) {
                        return false;
                    }
                    if (lowerCase.contains("darksand") || lowerCase.equals("sand") || lowerCase.contains("sand-floor")) {
                        return true;
                    }
                    if (lowerCase.contains("sand")) {
                        if (!lowerCase.contains("sandstone")) {
                            return true;
                        }
                    }
                }
            }
        } catch (Throwable th) {
        }
        try {
            if (tile.drop() == Items.sand) {
                return true;
            }
        } catch (Throwable th2) {
        }
        try {
            if (tile.overlay() != null) {
                return tile.overlay().itemDrop == Items.sand;
            }
            return false;
        } catch (Throwable th3) {
            return false;
        }
    }

    private static void input(Unit unit) {
        buildPanel();
        if (sandMode == 0 || !Core.input.justTouched() || Core.scene.hasMouse()) {
            return;
        }
        int width = Vars.world.width();
        if (sandMarked == null || sandMarked.length != width * Vars.world.height()) {
            sandMarked = new boolean[width * Vars.world.height()];
            sandList.clear();
        }
        int tile = World.toTile(Core.input.mouseWorldX());
        int tile2 = World.toTile(Core.input.mouseWorldY());
        for (int i = tile2 - 3; i <= tile2 + 3; i++) {
            for (int i2 = tile - 3; i2 <= tile + 3; i2++) {
                if (Vars.world.tiles.in(i2, i) && Mathf.dst(i2, i, tile, tile2) <= 3 + 0.5f) {
                    int i3 = (i * width) + i2;
                    if (sandMode == 1) {
                        Tile tile3 = Vars.world.tile(i2, i);
                        if (!sandMarked[i3] && tile3 != null && isSandTile(tile3)) {
                            sandMarked[i3] = true;
                            sandList.add(Integer.valueOf(i3));
                        }
                    } else if (sandMarked[i3]) {
                        sandMarked[i3] = false;
                        sandList.remove(Integer.valueOf(i3));
                    }
                }
            }
        }
    }

    private static void applySand(boolean[] zArr) {
        Tile tile;
        if (sandList.isEmpty() || sandMarked == null || zArr == null) {
            return;
        }
        int width = Vars.world.width();
        for (int max = Math.max(0, (areaY - ((int) areaR)) - 1); max <= Math.min(Vars.world.height() - 1, areaY + ((int) areaR) + 1); max++) {
            for (int max2 = Math.max(0, (areaX - ((int) areaR)) - 1); max2 <= Math.min(width - 1, areaX + ((int) areaR) + 1); max2++) {
                int i = (max * width) + max2;
                if (!sandMarked[i] && i < zArr.length && (tile = Vars.world.tile(max2, max)) != null && isSandTile(tile)) {
                    zArr[i] = true;
                }
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
        Tile tile;
        int width = Vars.world.width();
        if (!drawReg) {
            drawReg = true;
            Events.run(EventType.Trigger.draw, AutoEconomy::drawMarks);
        }
        if (!scanning || marked == null || marked.length != width * Vars.world.height()) {
            scanning = true;
            marked = new boolean[width * Vars.world.height()];
            markIdx = new int[2048];
            markColor = new int[2048];
            markCount = 0;
            scanStart = Time.time;
            stillSince = Time.time;
            lastX = unit.x;
            lastY = unit.y;
            toast(tr4("Auto Dock: va ate onde ha minerios (ficam marcados), marque areia se quiser e aperte COMECAR.", "Auto Dock: fly to the ore (it gets marked), mark sand if you want, then press START.", "Auto Dock: ve donde hay mineral (se marca), marca arena si quieres y pulsa EMPEZAR.", "Auto Dock: летите к руде, отметьте песок и нажмите СТАРТ."));
            return false;
        }
        if (Mathf.dst(unit.x, unit.y, lastX, lastY) > 16.0f) {
            lastX = unit.x;
            lastY = unit.y;
            stillSince = Time.time;
        }
        if (Time.time >= scanAt) {
            scanAt = Time.time + 20.0f;
            int tileX = unit.tileX();
            int tileY = unit.tileY();
            for (int i = tileY - 30; i <= tileY + 30; i++) {
                for (int i2 = tileX - 30; i2 <= tileX + 30; i2++) {
                    if (Vars.world.tiles.in(i2, i) && !marked[(i * width) + i2] && (tile = Vars.world.tile(i2, i)) != null && tile.overlay() != null && tile.overlay().itemDrop != null && Mathf.dst(i2, i, tileX, tileY) <= 30) {
                        marked[(i * width) + i2] = true;
                        if (markCount >= markIdx.length) {
                            if (markCount < 20000) {
                                markIdx = Arrays.copyOf(markIdx, markCount * 2);
                                markColor = Arrays.copyOf(markColor, markCount * 2);
                            }
                        }
                        markIdx[markCount] = (i * width) + i2;
                        markColor[markCount] = tile.overlay().itemDrop.color.rgba();
                        markCount++;
                    }
                }
            }
        }
        input(unit);
        if (startRequested) {
            startRequested = false;
            return true;
        }
        return false;
    }

    private static void drawMarks() {
        if (marked == null || Vars.state == null || !Vars.state.isGame()) {
            return;
        }
        int width = Vars.world.width();
        Draw.z(120.0f);
        float f = Core.camera.position.x;
        float f2 = Core.camera.position.y;
        float f3 = (Core.camera.width / 2.0f) + 16.0f;
        float f4 = (Core.camera.height / 2.0f) + 16.0f;
        for (int i = 0; i < markCount; i++) {
            int i2 = markIdx[i] % width;
            int i3 = markIdx[i] / width;
            if (Math.abs((i2 * 8) - f) <= f3 && Math.abs((i3 * 8) - f2) <= f4) {
                tmpC.rgba8888(markColor[i]);
                Draw.color(tmpC, 0.55f);
                Fill.rect(i2 * 8, i3 * 8, 7.0f, 7.0f);
            }
        }
        Draw.color(Items.sand.color, 0.6f);
        for (int i4 = 0; i4 < sandList.size(); i4++) {
            int intValue = sandList.get(i4).intValue() % width;
            int intValue2 = sandList.get(i4).intValue() / width;
            if (Math.abs((intValue * 8) - f) <= f3 && Math.abs((intValue2 * 8) - f2) <= f4) {
                Fill.rect(intValue * 8, intValue2 * 8, 7.0f, 7.0f);
            }
        }
        Draw.reset();
    }

    public static int[] markedTiles(Item item) {
        if (item != null) {
            try {
                if (marked != null) {
                    ArrayList arrayList = new ArrayList();
                    if (item == Items.sand) {
                        arrayList.addAll(sandList);
                    } else {
                        int width = Vars.world.width();
                        for (int i = 0; i < markCount; i++) {
                            Tile tile = Vars.world.tile(markIdx[i] % width, markIdx[i] / width);
                            if (tile != null && tile.overlay() != null && tile.overlay().itemDrop == item) {
                                arrayList.add(Integer.valueOf(markIdx[i]));
                            }
                        }
                    }
                    int[] iArr = new int[arrayList.size()];
                    for (int i2 = 0; i2 < iArr.length; i2++) {
                        iArr[i2] = ((Integer) arrayList.get(i2)).intValue();
                    }
                    return iArr;
                }
            } catch (Throwable th) {
                return new int[0];
            }
        }
        return new int[0];
    }

    public static boolean enabled() {
        return Core.settings.getBool(KEY, false);
    }

    public static void setEnabled(boolean z) {
        Core.settings.put(KEY, Boolean.valueOf(z));
        if (!z) {
            abort(true);
        }
    }

    public static void init() {
        recoverProfile();
        Events.run(EventType.Trigger.update, AutoEconomy::update);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            finished = false;
            autoAt = 0.0f;
            abort(true);
        });
    }

    public static void abort() {
        abort(false);
    }

    public static void abort(boolean z) {
        if (z) {
            clearMarks();
        }
        restoreProfile();
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
        snap = new Object[]{LunariansSettings.drillBlock, Integer.valueOf(LunariansSettings.drillMaxTiles), Integer.valueOf(LunariansSettings.drillMinOres), Integer.valueOf(LunariansSettings.drillOptQuality), Boolean.valueOf(LunariansSettings.drillPlaceWaterPower), Integer.valueOf(LunariansSettings.drillExitDir), Integer.valueOf(LunariansSettings.drillLayoutMode), Boolean.valueOf(LunariansSettings.drillPerfect)};
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
        if (snap == null) {
            return;
        }
        LunariansSettings.drillBlock = (String) snap[0];
        LunariansSettings.drillMaxTiles = ((Integer) snap[1]).intValue();
        LunariansSettings.drillMinOres = ((Integer) snap[2]).intValue();
        LunariansSettings.drillOptQuality = ((Integer) snap[3]).intValue();
        LunariansSettings.drillPlaceWaterPower = ((Boolean) snap[4]).booleanValue();
        LunariansSettings.drillExitDir = ((Integer) snap[5]).intValue();
        LunariansSettings.drillLayoutMode = ((Integer) snap[6]).intValue();
        LunariansSettings.drillPerfect = ((Boolean) snap[7]).booleanValue();
        snap = null;
        Core.settings.remove("lunarians-profile-backup");
    }

    private static void recoverProfile() {
        try {
            String string = Core.settings.getString("lunarians-profile-backup", "");
            if (string == null || string.isEmpty()) {
                return;
            }
            String[] split = string.split("\\|");
            Core.settings.put("lunarians-drill-block", split[0]);
            Core.settings.put("lunarians-drill-max-tiles", Integer.valueOf(Integer.parseInt(split[1])));
            Core.settings.put("lunarians-drill-min-ores", Integer.valueOf(Integer.parseInt(split[2])));
            Core.settings.put("lunarians-drill-opt", Integer.valueOf(Integer.parseInt(split[3])));
            Core.settings.put("lunarians-drill-water-power", Boolean.valueOf(Boolean.parseBoolean(split[4])));
            Core.settings.put("lunarians-drill-exit-dir", Integer.valueOf(Integer.parseInt(split[5])));
            Core.settings.put("lunarians-drill-layout", Integer.valueOf(Integer.parseInt(split[6])));
            Core.settings.put("lunarians-drill-perfect", Boolean.valueOf(Boolean.parseBoolean(split[7])));
            LunariansSettings.drillBlock = split[0];
            LunariansSettings.drillMaxTiles = Integer.parseInt(split[1]);
            LunariansSettings.drillMinOres = Integer.parseInt(split[2]);
            LunariansSettings.drillOptQuality = Integer.parseInt(split[3]);
            LunariansSettings.drillPlaceWaterPower = Boolean.parseBoolean(split[4]);
            LunariansSettings.drillExitDir = Integer.parseInt(split[5]);
            LunariansSettings.drillLayoutMode = Integer.parseInt(split[6]);
            LunariansSettings.drillPerfect = Boolean.parseBoolean(split[7]);
            Core.settings.remove("lunarians-profile-backup");
        } catch (Throwable th) {
            Core.settings.remove("lunarians-profile-backup");
        }
    }

    private static Field fld(String str) throws Exception {
        Field declaredField = AutoSet.class.getDeclaredField(str);
        declaredField.setAccessible(true);
        return declaredField;
    }

    private static int geti(String str) throws Exception {
        return fld(str).getInt(null);
    }

    private static void seti(String str, int i) throws Exception {
        fld(str).setInt(null, i);
    }

    private static void setf(String str, float f) throws Exception {
        fld(str).setFloat(null, f);
    }

    private static void setb(String str, boolean z) throws Exception {
        fld(str).setBoolean(null, z);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static void buildStages() {
        stages.clear();
        int max = Math.max(1, AutoSetConfig.oreCount(0));
        int max2 = Math.max(1, AutoSetConfig.oreCount(1));
        int max3 = Math.max(1, AutoSetConfig.oreCount(2));
        int max4 = Math.max(1, AutoSetConfig.oreCount(3));
        int max5 = Math.max(2, AutoSetConfig.oreCount(4));
        int max6 = Math.max(1, AutoSetConfig.oreCount(5));
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < max; i++) {
            arrayList.add("copper");
        }
        for (int i2 = 0; i2 < max2; i2++) {
            arrayList.add("lead");
        }
        stages.add(new Object[]{"Cobre+Chumbo", "drill", arrayList.toArray(new String[0])});
        ArrayList arrayList2 = new ArrayList();
        for (int i3 = 0; i3 < Math.max(max3, max4); i3++) {
            if (i3 < max3) {
                arrayList2.add("coal");
            }
            if (i3 < max4) {
                arrayList2.add("sand");
            }
        }
        stages.add(new Object[]{"Carvao+Areia", "drill", arrayList2.toArray(new String[0])});
        for (Object[] objArr : new String[]{new String[]{"graphite", "0", "Grafite"}, new String[]{"metaglass", "2", "Metavidro"}, new String[]{"silicon", "1", "Silicio"}}) {
            int max7 = Math.max(1, AutoSetConfig.facCount(Integer.parseInt(objArr[1])));
            ArrayList arrayList3 = new ArrayList();
            for (int i4 = 0; i4 < max7; i4++) {
                arrayList3.add(objArr[0]);
            }
            stages.add(new Object[]{objArr[2], "fac", arrayList3.toArray(new String[0])});
        }
        ArrayList arrayList4 = new ArrayList();
        for (int i5 = 0; i5 < max5; i5++) {
            arrayList4.add("titanium");
        }
        stages.add(new Object[]{"Titanio", "drill", arrayList4.toArray(new String[0])});
        stages.add(new Object[]{"Unidades", "units", new String[0]});
        ArrayList arrayList5 = new ArrayList();
        for (int i6 = 0; i6 < max6; i6++) {
            arrayList5.add("thorium");
        }
        stages.add(new Object[]{"Torio", "drill", arrayList5.toArray(new String[0])});
    }

    private static void load(int i, boolean z) throws Exception {
        Object[] objArr = stages.get(i);
        String str = (String) objArr[1];
        String[] strArr = (String[]) objArr[2];
        seti("runMode", str.equals("fac") ? 1 : 0);
        fld("oreOrder").set(null, str.equals("drill") ? strArr : new String[0]);
        fld("factoryQueue").set(null, str.equals("fac") ? strArr : new String[0]);
        seti("stepIndex", 0);
        ((ArrayList) fld("feedTargets").get(null)).clear();
        ((ArrayList) fld("feedItemNames").get(null)).clear();
        ((ArrayList) fld("feedReserved").get(null)).clear();
        ((HashMap) fld("feedLines").get(null)).clear();
        seti("feedIndex", 0);
        ((Seq) fld("pending").get(null)).clear();
        if (z) {
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
        setf("nextStepAt", Time.time + 10.0f);
        lastStep = 0;
        lastTotal = strArr.length;
        sawRunning = true;
        toast("Economia: etapa " + (i + 1) + "/" + stages.size() + " - " + String.valueOf(objArr[0]));
    }

    private static void update() {
        if ((!enabled() && !LunariansSettings.dockAuto && AutoSetConfig.mode() != 2 && !scanning) || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
            return;
        }
        try {
            boolean isRunning = AutoSet.isRunning();
            if (!adopted && !mapWanted && AutoSetConfig.mode() == 2 && geti("phase") == 2) {
                seti("phase", 0);
                mapWanted = true;
                finished = false;
                return;
            }
            if (!adopted && !isRunning && mapWanted && Vars.player.closestCore() != null && Vars.player.unit() != null && Vars.player.unit().canBuild()) {
                if (scan(Vars.player.unit())) {
                    areaX = Vars.player.unit().tileX();
                    areaY = Vars.player.unit().tileY();
                    float f = 0.0f;
                    int width = Vars.world.width();
                    for (int i = 0; i < markCount; i++) {
                        f = Math.max(f, Mathf.dst(markIdx[i] % width, markIdx[i] / width, areaX, areaY));
                    }
                    areaR = Math.max(AutoSetConfig.allRadius(), Math.min(80.0f, f + 4.0f));
                    boolean[] zArr = new boolean[Vars.world.width() * Vars.world.height()];
                    applySand(zArr);
                    fld("used").set(null, zArr);
                    applyProfile();
                    buildStages();
                    idx = 0;
                    adopted = true;
                    active = true;
                    scanning = false;
                    mapWanted = false;
                    load(0, true);
                    toast(tr4("Mapeamento pronto: Auto Set na sua posicao, depois schematics.", "Mapping done: Auto Set at your position, then schematics.", "Mapeo listo: Auto Set en tu posicion, luego esquemas.", "Карта готова: Auto Set в вашей точке, потом схемы."));
                    return;
                }
                return;
            }
            if (!adopted) {
                if (isRunning) {
                    areaX = geti("cx");
                    areaY = geti("cy");
                    areaR = fld("radius").getFloat(null);
                    buildStages();
                    idx = 0;
                    adopted = true;
                    active = true;
                    load(0, false);
                    return;
                }
                return;
            }
            if (inUnits) {
                if (Time.time > unitsDeadline || AutoDock.unitsDone()) {
                    AutoDock.stopUnits();
                    inUnits = false;
                    advance();
                    return;
                }
                return;
            }
            if (isRunning) {
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
        } catch (Throwable th) {
            Log.err("[Lunarians] AutoEconomy", th);
            toast("Auto All erro: " + th);
            abort(false);
        }
    }

    private static void advance() throws Exception {
        int i = idx + 1;
        idx = i;
        if (i >= stages.size()) {
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
        if ("units".equals(stages.get(idx)[1])) {
            toast("Economia: etapa " + (idx + 1) + "/" + stages.size() + " - Unidades (Auto Dock)");
            LunariansSettings.dockAuto = true;
            AutoDock.startUnits();
            inUnits = true;
            unitsDeadline = Time.time + 14400.0f;
            return;
        }
        load(idx, true);
    }

    private static void toast(String str) {
        Log.info("[Lunarians] " + str);
        if (Vars.ui != null) {
            Vars.ui.showInfoToast(str, 4.0f);
        }
    }
}
