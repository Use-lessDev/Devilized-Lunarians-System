// 
// Decompiled by Procyon v0.6.0
// 

package lunarians;

import mindustry.graphics.Pal;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Draw;
import arc.scene.ui.Button;
import arc.scene.ui.Label;
import arc.scene.ui.Slider;
import arc.scene.ui.TextButton;
import mindustry.ui.Styles;
import arc.scene.Element;
import arc.scene.ui.layout.Table;
import mindustry.ui.dialogs.BaseDialog;
import arc.math.geom.Position;
import java.util.Comparator;
import mindustry.ai.UnitGroup;
import arc.struct.Seq;
import arc.Events;
import mindustry.game.EventType;
import arc.Core;
import java.util.Iterator;
import java.util.Map;
import mindustry.ai.types.CommandAI;
import mindustry.gen.Groups;
import arc.math.Mathf;
import arc.util.Log;
import mindustry.gen.Unit;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.Vars;
import arc.util.Time;
import java.util.ArrayList;
import arc.math.geom.Vec2;
import java.util.HashMap;

public class Formations
{
    private static final float TWO_PI = 6.2831855f;
    public static final int OFF = 0;
    public static final int LINE = 1;
    public static final int ARC = 2;
    public static final int RING = 3;
    private static final String K_MODE = "lunarians-form-mode";
    private static final String K_SPACE = "lunarians-form-spacing";
    private static final String K_CENTER = "lunarians-form-center";
    private static final String K_PER = "lunarians-form-per";
    private static final String K_APPR = "lunarians-form-approach";
    private static final String K_GRAD = "lunarians-form-grad";
    private static final String K_GDELAY = "lunarians-form-grad-delay";
    private static final String K_LOOP = "lunarians-form-loop";
    private static final String K_LWAIT = "lunarians-form-loop-wait";
    private static final String K_LAYERS = "lunarians-form-layers";
    private static final String K_FACE = "lunarians-form-face";
    private static final String K_REFORM = "lunarians-form-reform";
    private static final String K_LAYERBY = "lunarians-form-layer-by-layer";
    private static Object lastGroup;
    private static final HashMap<Integer, Vec2[]> curLegs;
    private static final HashMap<Integer, Integer> retries;
    private static float nextCheck;
    private static boolean reformed;
    private static final ArrayList<Pend> pending;
    private static final HashMap<Integer, Vec2[]> loopA;
    private static final HashMap<Integer, Vec2[]> loopB;
    private static boolean loopActive;
    private static boolean loopToB;
    private static float loopWaitUntil;
    private static float nextLoop;
    private static boolean layerModeActive = false;
    private static int currentLayer = 0;
    private static int totalFormLayers = 0;
    private static final HashMap<Integer, ArrayList<Integer>> layerUnits = new HashMap<Integer, ArrayList<Integer>>();
    private static float nextLayerCheck = 0.0f;
    
    private static void pump() {
        if (Formations.pending.isEmpty()) {
            return;
        }
        int i = 0;
        while (i < Formations.pending.size()) {
            final Pend pend = Formations.pending.get(i);
            if (Time.time >= pend.at) {
                Formations.pending.remove(i);
                try {
                    Call.commandUnits(Vars.player, new int[] { pend.id }, (Building)null, (Unit)null, pend.p, pend.queue, true);
                }
                catch (final Throwable t) {
                    Log.err("[Lunarians] Formations send", t);
                }
            }
            else {
                ++i;
            }
        }
    }
    
    private static void dispatch(final ArrayList<Integer> list, final HashMap<Integer, Vec2[]> hashMap, final boolean b) {
        Formations.pending.clear();
        final float n = (b && gradual()) ? (gradualDelay() * 60.0f) : 0.0f;
        final float time = Time.time;
        for (int i = 0; i < list.size(); ++i) {
            final Vec2[] array = hashMap.get(list.get(i));
            if (array != null) {
                final float n2 = time + n * i;
                if (array[1] != null) {
                    Formations.pending.add(new Pend(list.get(i), array[1], false, n2));
                    Formations.pending.add(new Pend(list.get(i), array[0], true, n2));
                }
                else {
                    Formations.pending.add(new Pend(list.get(i), array[0], false, n2));
                }
            }
        }
        pump();
    }
    
    private static Vec2[] makeLeg(final Unit unit, final float n, final float n2, final float n3, final float n4) {
        final float max = Math.max(5.0f, unit.hitSize / 2.0f);
        final float n5 = (float)Vars.world.unitWidth();
        final float n6 = (float)Vars.world.unitHeight();
        final Vec2 vec2 = new Vec2(Mathf.clamp(n, 0.0f, n5), Mathf.clamp(n2, 0.0f, n6));
        if (!approach()) {
            return new Vec2[] { vec2, null, vec2 };
        }
        final float n7 = 24.0f + unit.hitSize;
        return new Vec2[] { new Vec2(Mathf.clamp(n + n3 * max, 0.0f, n5), Mathf.clamp(n2 + n4 * max, 0.0f, n6)), new Vec2(Mathf.clamp(n - n3 * n7, 0.0f, n5), Mathf.clamp(n2 - n4 * n7, 0.0f, n6)), vec2 };
    }
    

    private static void layerTick() {
        if (!Formations.layerModeActive) return;
        if (Formations.loopActive || Vars.player == null || Vars.state == null || !Vars.state.isGame()) {
            Formations.layerModeActive = false;
            return;
        }
        if (!Formations.pending.isEmpty() || Time.time < Formations.nextLayerCheck) return;
        Formations.nextLayerCheck = Time.time + 20.0f;
        ArrayList<Integer> ids = (ArrayList<Integer>)Formations.layerUnits.get(Formations.currentLayer);
        if (ids == null || ids.isEmpty()) {
            Formations.currentLayer++;
            if (Formations.currentLayer >= Formations.totalFormLayers) { Formations.layerModeActive = false; return; }
            ArrayList<Integer> nxt = (ArrayList<Integer>)Formations.layerUnits.get(Formations.currentLayer);
            if (nxt != null && !nxt.isEmpty()) dispatch(nxt, Formations.curLegs, true);
            return;
        }
        boolean allArrived = true;
        for (int i = 0; i < ids.size(); i++) {
            int id = ids.get(i);
            Unit unit = (Unit)Groups.unit.getByID(id);
            if (unit == null || !unit.isValid() || !(unit.controller() instanceof CommandAI)) continue;
            CommandAI commandAI = (CommandAI)unit.controller();
            if (commandAI.targetPos != null) {
                Vec2[] legs = (Vec2[])Formations.curLegs.get(id);
                Vec2 dest = (legs != null) ? ((legs[1] != null) ? legs[1] : legs[0]) : null;
                if (dest != null && unit.dst(dest) > Math.max(12.0f, unit.hitSize * 0.8f)) { allArrived = false; break; }
            }
        }
        if (!allArrived) return;
        Formations.currentLayer++;
        if (Formations.currentLayer >= Formations.totalFormLayers) { Formations.layerModeActive = false; return; }
        ArrayList<Integer> nxt2 = (ArrayList<Integer>)Formations.layerUnits.get(Formations.currentLayer);
        if (nxt2 != null && !nxt2.isEmpty()) dispatch(nxt2, Formations.curLegs, true);
        else Formations.nextLayerCheck = 0.0f;
    }

    private static void loopTick() {
        if (!Formations.loopActive) {
            return;
        }
        if (!loop() || Vars.player == null || Vars.state == null || !Vars.state.isGame()) {
            Formations.loopActive = false;
            return;
        }
        if (!Formations.pending.isEmpty() || Time.time < Formations.nextLoop) {
            return;
        }
        Formations.nextLoop = Time.time + 20.0f;
        final HashMap<Integer, Vec2[]> hashMap = Formations.loopToB ? Formations.loopB : Formations.loopA;
        boolean b = true;
        final ArrayList list = new ArrayList();
        final ArrayList list2 = new ArrayList();
        for (final Integer e : Formations.loopB.keySet()) {
            final Unit unit = (Unit)Groups.unit.getByID((int)e);
            if (unit == null || !unit.isValid() || !(unit.controller() instanceof CommandAI)) {
                list2.add(e);
            }
            else {
                final CommandAI commandAI = (CommandAI)unit.controller();
                final Vec2[] array = hashMap.get(e);
                if (commandAI.targetPos != null && array != null && commandAI.targetPos.dst(array[0]) > 16.0f && (array[1] == null || commandAI.targetPos.dst(array[1]) > 16.0f)) {
                    Formations.loopActive = false;
                    return;
                }
                if (commandAI.targetPos != null) {
                    b = false;
                }
                list.add(e);
            }
        }
        for (final Integer n : list2) {
            Formations.loopA.remove(n);
            Formations.loopB.remove(n);
        }
        if (list.isEmpty()) {
            Formations.loopActive = false;
            return;
        }
        if (!b) {
            Formations.loopWaitUntil = 0.0f;
            return;
        }
        if (Formations.loopWaitUntil == 0.0f) {
            Formations.loopWaitUntil = Time.time + loopWait() * 60.0f;
            return;
        }
        if (Time.time < Formations.loopWaitUntil) {
            return;
        }
        Formations.loopWaitUntil = 0.0f;
        Formations.loopToB = !Formations.loopToB;
        final HashMap<Integer, Vec2[]> m = Formations.loopToB ? Formations.loopB : Formations.loopA;
        Formations.curLegs.clear();
        Formations.curLegs.putAll(m);
        Formations.retries.clear();
        dispatch(list, m, true);
    }
    
    public static int mode() {
        return Core.settings.getInt("lunarians-form-mode", 0);
    }
    
    public static void setMode(final int i) {
        Core.settings.put("lunarians-form-mode", (Object)i);
    }
    
    public static float spacing() {
        return Core.settings.getFloat("lunarians-form-spacing", 2.0f);
    }
    
    public static int center() {
        return Core.settings.getInt("lunarians-form-center", 0);
    }
    
    public static int perLayer() {
        return Core.settings.getInt("lunarians-form-per", 0);
    }
    
    public static int layers() {
        return Core.settings.getInt("lunarians-form-layers", 0);
    }
    
    public static boolean gradual() {
        return Core.settings.getBool("lunarians-form-grad", false);
    }
    
    public static float gradualDelay() {
        return Core.settings.getFloat("lunarians-form-grad-delay", 0.4f);
    }
    
    public static boolean loop() {
        return Core.settings.getBool("lunarians-form-loop", false);
    }
    
    public static float loopWait() {
        return Core.settings.getFloat("lunarians-form-loop-wait", 1.0f);
    }
    
    public static boolean approach() {
        return Core.settings.getBool("lunarians-form-approach", true);
    }
    
    public static int face() {
        return Core.settings.getInt("lunarians-form-face", 0);
    }
    
    public static boolean reform() {
        return Core.settings.getBool("lunarians-form-reform", false);
    }

    public static boolean layerByLayer() {
        return Core.settings.getBool("lunarians-form-layer-by-layer", true);
    }
    
    public static String modeName(final int n) {
        final boolean b = LunariansSettings.language != null && LunariansSettings.language.startsWith("pt");
        switch (n) {
            case 1: {
                return b ? "Linha" : "Line";
            }
            case 2: {
                return b ? "Arco" : "Arc";
            }
            case 3: {
                return b ? "Anel" : "Ring";
            }
            default: {
                return b ? "Desligado" : "Off";
            }
        }
    }
    
    public static void init() {
        Events.run((Object)EventType.Trigger.update, Formations::update);
        Events.on((Class)EventType.WorldLoadEvent.class, worldLoadEvent -> {
            Formations.lastGroup = null;
            Formations.curLegs.clear();
            Formations.layerModeActive = false;
            Formations.layerUnits.clear();
        });
    }
    
    public static float[] layout(final int n, final int b, final float n2, final float n3) {
        final float[] array = new float[b * 2];
        if (b <= 0) {
            return array;
        }
        final float cos = Mathf.cos(n3);
        final float sin = Mathf.sin(n3);
        final float n4 = -sin;
        final float n5 = cos;
        final int perLayer = perLayer();
        final int layers = layers();
        int a;
        if (perLayer > 0) {
            a = perLayer;
        }
        else if (layers > 0) {
            a = (b + layers - 1) / layers;
        }
        else {
            a = ((n == 1) ? 14 : b);
        }
        final int max = Math.max(1, Math.min(a, b));
        if (n == 1) {
            for (int i = 0; i < b; ++i) {
                final int n6 = i / max;
                final float n7 = (i % max - (Math.min(max, b - n6 * max) - 1) / 2.0f) * n2;
                final float n8 = n6 * n2;
                array[i * 2] = n4 * n7 - cos * n8;
                array[i * 2 + 1] = n5 * n7 - sin * n8;
            }
        }
        else if (n == 2) {
            final float max2 = Math.max(n2 * 1.4f, (max > 1) ? (n2 * (max - 1) / 2.8274333f) : n2);
            final float n9 = n3 + 3.1415927f;
            final float n10 = cos * max2;
            final float n11 = sin * max2;
            for (int j = 0; j < b; ++j) {
                final int n12 = j / max;
                final int n13 = j % max;
                final int min = Math.min(max, b - n12 * max);
                final float n14 = max2 + n12 * n2;
                final float n15 = (n13 - (min - 1) / 2.0f) * (n2 / n14);
                array[j * 2] = n10 + Mathf.cos(n9 + n15) * n14;
                array[j * 2 + 1] = n11 + Mathf.sin(n9 + n15) * n14;
            }
        }
        else if (n == 3) {
            final float n16 = (max > 1) ? Math.max(n2 * max / 6.2831855f, n2 * 0.95f) : 0.0f;
            final float n17 = n3 + 3.1415927f;
            for (int k = 0; k < b; ++k) {
                final int n18 = k / max;
                final int n19 = k % max;
                final int min2 = Math.min(max, b - n18 * max);
                final float n20 = n16 + n18 * n2;
                final float n21 = n17 + 6.2831855f * n19 / min2;
                array[k * 2] = Mathf.cos(n21) * n20;
                array[k * 2 + 1] = Mathf.sin(n21) * n20;
            }
        }
        return array;
    }
    
    private static void update() {
        pump();
        layerTick();
        loopTick();
        final int mode = mode();
        if (mode == 0 || Vars.state == null || !Vars.state.isGame() || Vars.player == null || Vars.control == null) {
            return;
        }
        try {
            final Seq selectedUnits = Vars.control.input.selectedUnits;
            if (selectedUnits == null || selectedUnits.size < 2) {
                Formations.lastGroup = null;
                return;
            }
            UnitGroup group = null;
            Vec2 cpy = null;
            final Seq seq = new Seq();
            for (final Unit unit : selectedUnits) {
                if (unit != null && unit.isValid()) {
                    if (!(unit.controller() instanceof CommandAI)) {
                        continue;
                    }
                    final CommandAI commandAI = (CommandAI)unit.controller();
                    if (commandAI.group == null) {
                        return;
                    }
                    if (group == null) {
                        group = commandAI.group;
                    }
                    else if (group != commandAI.group) {
                        return;
                    }
                    final Vec2 vec2 = (commandAI.attackTarget != null) ? new Vec2(commandAI.attackTarget.x(), commandAI.attackTarget.y()) : commandAI.targetPos;
                    if (vec2 == null) {
                        return;
                    }
                    if (cpy == null) {
                        cpy = vec2.cpy();
                    }
                    else if (cpy.dst(vec2) > 2.0f) {
                        return;
                    }
                    seq.add((Object)unit);
                }
            }
            if (group == null || seq.size < 2) {
                return;
            }
            if (group != Formations.lastGroup) {
                Formations.lastGroup = group;
                apply((Seq<Unit>)seq, cpy, mode);
            }
        }
        catch (final Throwable t) {
            Log.err("[Lunarians] Formations", t);
        }
        reformCheck();
    }
    
    private static void apply(final Seq<Unit> seq, final Vec2 vec2, final int n) {
        final int size = seq.size;
        float n2 = 0.0f;
        float n3 = 0.0f;
        float max = 8.0f;
        for (final Unit unit : seq) {
            n2 += unit.x;
            n3 += unit.y;
            max = Math.max(max, unit.hitSize);
        }
        final float n4 = n2 / size;
        final float n5 = n3 / size;
        final float max2 = Math.max(spacing() * 8.0f, max * 1.15f);
        final float n6 = (face() == 1) ? 1.5707964f : Mathf.atan2(vec2.y - n5, vec2.x - n4);
        final float[] layout = layout(n, size, max2, n6);
        final float cos = Mathf.cos(n6);
        final float sin = Mathf.sin(n6);
        final ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < size; ++i) {
            list.add(i);
        }
        final ArrayList list2 = new ArrayList();
        final Iterator iterator2 = seq.iterator();
        while (iterator2.hasNext()) {
            list2.add(iterator2.next());
        }
        if (n == 3) {
            list.sort(Comparator.comparingDouble(n10 -> Mathf.mod(Mathf.atan2(array[n10 * 2 + 1], array[n10 * 2]) - n9, 6.2831855f)));
            list2.sort(Comparator.comparingDouble(unit3 -> Mathf.mod(Mathf.atan2(unit3.y - n11, unit3.x - n12) - n13, 6.2831855f)));
        }
        else {
            list.sort(Comparator.comparingDouble(n16 -> array2[n16 * 2] * n14 + array2[n16 * 2 + 1] * n15).thenComparingDouble(n19 -> -(array3[n19 * 2] * n17 + array3[n19 * 2 + 1] * n18)));
            list2.sort(Comparator.comparingDouble(unit4 -> (unit4.x - n20) * n21 + (unit4.y - n22) * n23).thenComparingDouble(unit5 -> -((unit5.x - n24) * n25 + (unit5.y - n26) * n27)));
        }
        final int center = center();
        float n7;
        float n8;
        if (center == 1) {
            final int intValue = list.get(Math.max(0, list2.indexOf(seq.first())));
            n7 = vec2.x - layout[intValue * 2];
            n8 = vec2.y - layout[intValue * 2 + 1];
        }
        else if (center == 2) {
            n7 = Core.camera.position.x;
            n8 = Core.camera.position.y;
        }
        else {
            n7 = vec2.x;
            n8 = vec2.y;
        }
        Formations.curLegs.clear();
        Formations.retries.clear();
        Formations.loopA.clear();
        Formations.loopB.clear();
        Formations.reformed = false;
        final float[] layout2 = layout(n, size, max2, n6 + 3.1415927f);
        final ArrayList<Integer> list3 = new ArrayList<Integer>();
        final ArrayList<Integer> list4 = new ArrayList<Integer>();
        for (int j = 0; j < size; ++j) {
            list4.add(j);
        }
        list4.sort(Comparator.comparingDouble(n30 -> -(array4[list5.get(n30) * 2] * n28 + array4[list5.get(n30) * 2 + 1] * n29)));
        for (int k = 0; k < size; ++k) {
            final Unit unit2 = (Unit)list2.get(k);
            final int intValue2 = list.get(k);
            Formations.loopB.put(unit2.id, makeLeg(unit2, n7 + layout[intValue2 * 2], n8 + layout[intValue2 * 2 + 1], cos, sin));
            Formations.loopA.put(unit2.id, makeLeg(unit2, n4 + layout2[intValue2 * 2], n5 + layout2[intValue2 * 2 + 1], -cos, -sin));
        }
        Formations.curLegs.putAll(Formations.loopB);
        final Iterator<Integer> iterator3 = list4.iterator();
        while (iterator3.hasNext()) {
            list3.add(((Unit)list2.get(iterator3.next())).id);
        }
        dispatch(list3, Formations.curLegs, true);
        Formations.loopActive = loop();
        Formations.loopToB = true;
        Formations.loopWaitUntil = 0.0f;
        Formations.nextLoop = Time.time + 60.0f;
        Formations.lastGroup = null;
        Formations.nextCheck = Time.time + 30.0f;
        if (Vars.ui != null && !Formations.loopActive) {
            Vars.ui.showInfoToast(modeName(n) + " x" + size + ((size > 1) ? (" (" + (size + Math.max(1, Math.min(size, (perLayer() > 0) ? perLayer() : ((layers() > 0) ? ((size + layers() - 1) / layers()) : ((n == 1) ? 14 : size)))) - 1) / Math.max(1, Math.min(size, (perLayer() > 0) ? perLayer() : ((layers() > 0) ? ((size + layers() - 1) / layers()) : ((n == 1) ? 14 : size)))) + tr(" layers)", " camadas)")) : ""), 1.5f);
        }
    }
    
    private static void reformCheck() {
        if (!reform() || Formations.loopActive || !Formations.pending.isEmpty() || Formations.curLegs.isEmpty() || Time.time < Formations.nextCheck) {
            return;
        }
        Formations.nextCheck = Time.time + 30.0f;
        boolean b = true;
        final ArrayList list = new ArrayList();
        final ArrayList list2 = new ArrayList();
        for (final Map.Entry entry : Formations.curLegs.entrySet()) {
            final Unit unit = (Unit)Groups.unit.getByID((int)entry.getKey());
            if (unit == null || !unit.isValid() || !(unit.controller() instanceof CommandAI)) {
                list.add(entry.getKey());
            }
            else if (((CommandAI)unit.controller()).targetPos != null) {
                b = false;
            }
            else {
                final float max = Math.max(6.0f, unit.hitSize * 0.45f);
                final Integer n = Formations.retries.get(entry.getKey());
                if (unit.dst((Position)((Vec2[])entry.getValue())[2]) <= max || (n != null && n >= 3)) {
                    continue;
                }
                list2.add(entry.getKey());
            }
        }
        final Iterator iterator2 = list.iterator();
        while (iterator2.hasNext()) {
            Formations.curLegs.remove(iterator2.next());
        }
        if (!b) {
            return;
        }
        if (list2.isEmpty()) {
            Formations.curLegs.clear();
            return;
        }
        for (final Integer key : list2) {
            Formations.retries.put(key, Formations.retries.containsKey(key) ? (Formations.retries.get(key) + 1) : 1);
        }
        dispatch(list2, Formations.curLegs, false);
    }
    
    public static void openDialog() {
        final BaseDialog baseDialog = new BaseDialog(tr("Formations", "Formacoes"));
        UiStyle.register(baseDialog);
        final Table table = new Table();
        ((new Runnable[] { null })[0] = (() -> {
            table2.clear();
            table2.defaults().pad(4.0f);
            table2.add((CharSequence)tr("Select units in command mode, choose a shape and send them to move or attack. The mod sends one position per unit.", "Selecione unidades no modo comando, escolha o formato e mande mover ou atacar. O mod envia uma posicao por unidade.")).color(UiStyle.TEXT_DIM).wrap().width(420.0f).left().row();
            UiStyle.section(table2, tr("Shape", "Formato"));
            table2.table(table -> {
                for (int i = 0; i <= 3; ++i) {
                    final int n = i;
                    table.button(button -> {
                        button.add((Element)new Preview(n, 92.0f, 62.0f)).row();
                        button.add((CharSequence)modeName(n)).padTop(3.0f);
                    }, (Button.ButtonStyle)Styles.togglet, () -> {
                        setMode(mode);
                        array2[0].run();
                        return;
                    }).checked(button -> mode() == n).size(104.0f, 100.0f).pad(3.0f);
                }
            }).row();
            UiStyle.section(table2, tr("Spacing", "Espacamento"));
            table2.table(table -> {
                final Slider slider = new Slider(1.0f, 8.0f, 0.5f, false);
                slider.setValue(spacing());
                final Label label = new Label((CharSequence)(String.format("%.1f", spacing()) + tr(" blocks", " blocos")));
                slider.moved(n -> {
                    Core.settings.put("lunarians-form-spacing", (Object)n);
                    label.setText((CharSequence)(String.format("%.1f", n) + tr(" blocks", " blocos")));
                });
                table.add((Element)slider).width(240.0f);
                table.add((Element)label).width(90.0f).padLeft(8.0f);
            }).row();
            table2.add((CharSequence)tr("Never closer than the unit size.", "Nunca fica mais perto que o tamanho da unidade.")).color(UiStyle.TEXT_DIM).fontScale(0.8f).left().row();
            UiStyle.section(table2, tr("Layers", "Camadas"));
            table2.add((CharSequence)tr("Add more layers behind the first one. 0 = automatic.", "Adicione mais camadas atras da primeira. 0 = automatico.")).color(UiStyle.TEXT_DIM).wrap().width(420.0f).left().row();
            table2.table(table -> {
                table.add((CharSequence)tr("Layers", "Camadas")).width(110.0f).left();
                final Slider slider = new Slider(0.0f, 10.0f, 1.0f, false);
                slider.setValue((float)layers());
                final Label label = new Label((CharSequence)((layers() == 0) ? tr("Auto", "Auto") : String.valueOf(layers())));
                slider.moved(n -> {
                    Core.settings.put("lunarians-form-layers", (Object)(int)n);
                    label.setText((CharSequence)(((int)n == 0) ? tr("Auto", "Auto") : String.valueOf((int)n)));
                });
                table.add((Element)slider).width(200.0f);
                table.add((Element)label).width(60.0f).padLeft(8.0f);
            }).row();
            table2.table(table -> {
                table.add((CharSequence)tr("Units per layer", "Unidades por camada")).width(110.0f).left().wrap();
                final Slider slider = new Slider(0.0f, 50.0f, 1.0f, false);
                slider.setValue((float)perLayer());
                final Label label = new Label((CharSequence)((perLayer() == 0) ? tr("Auto", "Auto") : String.valueOf(perLayer())));
                slider.moved(n -> {
                    Core.settings.put("lunarians-form-per", (Object)(int)n);
                    label.setText((CharSequence)(((int)n == 0) ? tr("Auto", "Auto") : String.valueOf((int)n)));
                });
                table.add((Element)slider).width(200.0f);
                table.add((Element)label).width(60.0f).padLeft(8.0f);
            }).row();
            UiStyle.section(table2, tr("Gradual and loop", "Gradual e loop"));
            table2.table(tlb -> tlb.check(tr("Layer by layer (deploy one layer at a time)", "Camada por camada (envia uma camada por vez)"), layerByLayer(), b -> Core.settings.put("lunarians-form-layer-by-layer", (Object)b)).left().wrap()).left().row();
            table2.table(table -> table.check(tr("Gradual (send units one by one, front first)", "Gradual (envia as unidades uma a uma, frente primeiro)"), gradual(), b -> Core.settings.put("lunarians-form-grad", (Object)b)).left()).left().row();
            table2.table(table -> {
                table.add((CharSequence)tr("Delay between units", "Intervalo entre unidades")).width(110.0f).left().wrap();
                final Slider slider = new Slider(0.1f, 3.0f, 0.1f, false);
                slider.setValue(gradualDelay());
                final Label label = new Label((CharSequence)String.format("%.1fs", gradualDelay()));
                slider.moved(n -> {
                    Core.settings.put("lunarians-form-grad-delay", (Object)n);
                    label.setText((CharSequence)String.format("%.1fs", n));
                });
                table.add((Element)slider).width(200.0f);
                table.add((Element)label).width(60.0f).padLeft(8.0f);
            }).row();
            table2.table(table -> table.check(tr("Loop (go back and forth keeping the formation)", "Loop (vai e volta mantendo a formacao)"), loop(), b -> {
                Core.settings.put("lunarians-form-loop", (Object)b);
                if (!b) {
                    Formations.loopActive = false;
                }
            }).left()).left().row();
            table2.table(table -> {
                table.add((CharSequence)tr("Wait at each end", "Espera em cada ponta")).width(110.0f).left().wrap();
                final Slider slider = new Slider(0.0f, 10.0f, 0.5f, false);
                slider.setValue(loopWait());
                final Label label = new Label((CharSequence)String.format("%.1fs", loopWait()));
                slider.moved(n -> {
                    Core.settings.put("lunarians-form-loop-wait", (Object)n);
                    label.setText((CharSequence)String.format("%.1fs", n));
                });
                table.add((Element)slider).width(200.0f);
                table.add((Element)label).width(60.0f).padLeft(8.0f);
            }).row();
            table2.add((CharSequence)tr("Loop stops when you give the units a new command or turn it off.", "O loop para quando voce da um novo comando as unidades ou desliga.")).color(UiStyle.TEXT_DIM).wrap().width(420.0f).left().row();
            UiStyle.section(table2, tr("Center", "Centro"));
            table2.table(table -> {
                final String[] array = { tr("Command target", "Alvo do comando"), tr("First selected", "Primeiro da selecao"), "Camera" };
                for (int i = 0; i < 3; ++i) {
                    table.button(array[i], Styles.togglet, () -> Core.settings.put("lunarians-form-center", (Object)j)).checked(textButton -> center() == i).size(138.0f, 42.0f).pad(2.0f);
                }
            }).row();
            UiStyle.section(table2, tr("Facing", "Orientacao"));
            table2.table(table -> {
                final String[] array = { tr("Toward target / movement", "Para o alvo / movimento"), tr("Fixed (north)", "Fixa (norte)") };
                for (int i = 0; i < 2; ++i) {
                    table.button(array[i], Styles.togglet, () -> Core.settings.put("lunarians-form-face", (Object)j)).checked(textButton -> face() == i).size(210.0f, 42.0f).pad(2.0f);
                }
            }).row();
            table2.table(table -> table.check(tr("Aligned approach (all units arrive from the same direction)", "Aproximacao alinhada (todas chegam pela mesma direcao)"), approach(), b -> Core.settings.put("lunarians-form-approach", (Object)b)).left()).left().row();
            table2.table(table -> table.check(tr("Reform on stop (re-send slots when they stop out of place)", "Reformar ao parar (reenvia os slots se pararem fora do lugar)"), reform(), b -> Core.settings.put("lunarians-form-reform", (Object)b)).left()).left().row();
            return;
        })).run();
        baseDialog.cont.pane((Element)table).grow().minWidth(440.0f);
        baseDialog.addCloseButton();
        baseDialog.show();
    }
    
    private static String tr(final String s, final String s2) {
        return (LunariansSettings.language != null && LunariansSettings.language.startsWith("pt")) ? s2 : s;
    }
    
    static {
        Formations.lastGroup = null;
        curLegs = new HashMap<Integer, Vec2[]>();
        retries = new HashMap<Integer, Integer>();
        Formations.nextCheck = 0.0f;
        Formations.reformed = false;
        pending = new ArrayList<Pend>();
        loopA = new HashMap<Integer, Vec2[]>();
        loopB = new HashMap<Integer, Vec2[]>();
        Formations.loopActive = false;
        Formations.loopToB = true;
        Formations.loopWaitUntil = 0.0f;
        Formations.nextLoop = 0.0f;
    }
    
    private static class Pend
    {
        int id;
        Vec2 p;
        boolean queue;
        float at;
        
        Pend(final int id, final Vec2 p4, final boolean queue, final float at) {
            this.id = id;
            this.p = p4;
            this.queue = queue;
            this.at = at;
        }
    }
    
    static class Preview extends Element
    {
        final int mode;
        final float w;
        final float h;
        
        Preview(final int mode, final float w, final float h) {
            this.mode = mode;
            this.w = w;
            this.h = h;
        }
        
        public float getPrefWidth() {
            return this.w;
        }
        
        public float getPrefHeight() {
            return this.h;
        }
        
        public void draw() {
            final float parentAlpha = this.parentAlpha;
            Draw.color(0.07f, 0.08f, 0.1f, 0.9f * parentAlpha);
            Fill.rect(this.x + this.width / 2.0f, this.y + this.height / 2.0f, this.width, this.height);
            if (this.mode == 0) {
                Draw.color(UiStyle.DIM.r, UiStyle.DIM.g, UiStyle.DIM.b, parentAlpha);
                Fill.rect(this.x + this.width / 2.0f, this.y + this.height / 2.0f, 18.0f, 3.0f);
                Draw.color();
                return;
            }
            final int n = (Formations.perLayer() > 0 || Formations.layers() > 0) ? Math.min(30, Math.max(1, ((Formations.perLayer() > 0) ? Formations.perLayer() : 5) * ((Formations.layers() > 0) ? Formations.layers() : 2))) : ((this.mode == 3) ? 9 : 7);
            final float[] layout = Formations.layout(this.mode, n, 1.0f, 1.5707964f);
            float min = 1.0E9f;
            float max = -1.0E9f;
            float min2 = 1.0E9f;
            float max2 = -1.0E9f;
            for (int i = 0; i < n; ++i) {
                min = Math.min(min, layout[i * 2]);
                max = Math.max(max, layout[i * 2]);
                min2 = Math.min(min2, layout[i * 2 + 1]);
                max2 = Math.max(max2, layout[i * 2 + 1]);
            }
            final float min3 = Math.min(Math.min((this.width - 26.0f) / Math.max(0.01f, max - min), (this.height - 22.0f) / Math.max(0.01f, max2 - min2)), 16.0f);
            final float n2 = (min + max) / 2.0f;
            final float n3 = (min2 + max2) / 2.0f;
            for (int j = 0; j < n; ++j) {
                Draw.color(Pal.accent.r, Pal.accent.g, Pal.accent.b, parentAlpha);
                Fill.circle(this.x + this.width / 2.0f + (layout[j * 2] - n2) * min3, this.y + this.height / 2.0f + (layout[j * 2 + 1] - n3) * min3, 3.6f);
            }
            Draw.color(1.0f, 1.0f, 1.0f, 0.5f * parentAlpha);
            Fill.tri(this.x + this.width - 11.0f, this.y + this.height - 8.0f, this.x + this.width - 5.0f, this.y + this.height - 8.0f, this.x + this.width - 8.0f, this.y + this.height - 3.0f);
            Draw.color();
        }
    }
}
