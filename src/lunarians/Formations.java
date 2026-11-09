/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.Events
 *  arc.graphics.g2d.Draw
 *  arc.graphics.g2d.Fill
 *  arc.math.Mathf
 *  arc.math.geom.Position
 *  arc.math.geom.Vec2
 *  arc.scene.Element
 *  arc.scene.ui.Button$ButtonStyle
 *  arc.scene.ui.Label
 *  arc.scene.ui.Slider
 *  arc.scene.ui.layout.Table
 *  arc.struct.Seq
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.ai.UnitGroup
 *  mindustry.ai.types.CommandAI
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.EventType$WorldLoadEvent
 *  mindustry.gen.Call
 *  mindustry.gen.Groups
 *  mindustry.gen.Player
 *  mindustry.gen.Unit
 *  mindustry.graphics.Pal
 *  mindustry.ui.Styles
 *  mindustry.ui.dialogs.BaseDialog
 */
package lunarians;

import arc.Core;
import arc.Events;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.scene.Element;
import arc.scene.ui.Button;
import arc.scene.ui.Label;
import arc.scene.ui.Slider;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import lunarians.LunariansSettings;
import lunarians.UiStyle;
import mindustry.Vars;
import mindustry.ai.UnitGroup;
import mindustry.ai.types.CommandAI;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public class Formations {
    private static final float TWO_PI = (float)Math.PI * 2;
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
    private static final String K_LAYDELAY = "lunarians-form-layer-delay";
    private static Object lastGroup = null;
    private static final HashMap<Integer, Vec2[]> curLegs = new HashMap();
    private static final HashMap<Integer, Integer> retries = new HashMap();
    private static float nextCheck = 0.0f;
    private static boolean reformed = false;
    private static final ArrayList<Pend> pending = new ArrayList();
    private static final HashMap<Integer, Vec2[]> loopA = new HashMap();
    private static final HashMap<Integer, Vec2[]> loopB = new HashMap();
    private static boolean loopActive = false;
    private static boolean loopToB = true;
    private static float loopWaitUntil = 0.0f;
    private static float nextLoop = 0.0f;
    private static boolean layerModeActive = false;
    private static int currentLayer = 0;
    private static int totalFormLayers = 0;
    private static final HashMap<Integer, ArrayList<Integer>> layerUnits = new HashMap();
    private static float nextLayerCheck = 0.0f;

    private static void pump() {
        if (pending.isEmpty()) {
            return;
        }
        int n = 0;
        while (n < pending.size()) {
            Pend pend = pending.get(n);
            if (Time.time >= pend.at) {
                pending.remove(n);
                try {
                    Call.commandUnits((Player)Vars.player, (int[])new int[]{pend.id}, null, null, (Vec2)pend.p, (boolean)pend.queue, (boolean)true);
                }
                catch (Throwable throwable) {
                    Log.err((String)"[Lunarians] Formations send", (Throwable)throwable);
                }
                continue;
            }
            ++n;
        }
    }

    private static void dispatch(ArrayList<Integer> arrayList, HashMap<Integer, Vec2[]> hashMap, boolean bl) {
        pending.clear();
        float f = bl && Formations.gradual() ? Formations.gradualDelay() * 60.0f : 0.0f;
        float f2 = Time.time;
        for (int i = 0; i < arrayList.size(); ++i) {
            Vec2[] vec2Array = hashMap.get(arrayList.get(i));
            if (vec2Array == null) continue;
            float f3 = f2 + f * (float)i;
            if (vec2Array[1] != null) {
                pending.add(new Pend(arrayList.get(i), vec2Array[1], false, f3));
                pending.add(new Pend(arrayList.get(i), vec2Array[0], true, f3));
                continue;
            }
            pending.add(new Pend(arrayList.get(i), vec2Array[0], false, f3));
        }
        Formations.pump();
    }

    private static Vec2[] makeLeg(Unit unit, float f, float f2, float f3, float f4) {
        float f5 = Math.max(5.0f, unit.hitSize / 2.0f);
        float f6 = Vars.world.unitWidth();
        float f7 = Vars.world.unitHeight();
        Vec2 vec2 = new Vec2(Mathf.clamp((float)f, (float)0.0f, (float)f6), Mathf.clamp((float)f2, (float)0.0f, (float)f7));
        if (!Formations.approach()) {
            return new Vec2[]{vec2, null, vec2};
        }
        float f8 = 24.0f + unit.hitSize;
        Vec2 vec22 = new Vec2(Mathf.clamp((float)(f + f3 * f5), (float)0.0f, (float)f6), Mathf.clamp((float)(f2 + f4 * f5), (float)0.0f, (float)f7));
        Vec2 vec23 = new Vec2(Mathf.clamp((float)(f - f3 * f8), (float)0.0f, (float)f6), Mathf.clamp((float)(f2 - f4 * f8), (float)0.0f, (float)f7));
        return new Vec2[]{vec22, vec23, vec2};
    }


    private static void layerTick() {
        if (!layerModeActive) {
            return;
        }
        if (loopActive || Vars.player == null || Vars.state == null || !Vars.state.isGame()) {
            layerModeActive = false;
            return;
        }
        if (Time.time < nextLayerCheck) {
            return;
        }
        nextLayerCheck = 0.0f;
        ArrayList<Integer> ids = layerUnits.get(currentLayer);
        if (ids == null || ids.isEmpty()) {
            currentLayer++;
            if (currentLayer >= totalFormLayers) { layerModeActive = false; return; }
            ArrayList<Integer> next = layerUnits.get(currentLayer);
            if (next != null && !next.isEmpty()) {
                Formations.dispatch(next, curLegs, true);
                nextLayerCheck = Time.time + Formations.layerDelay() * 60.0f;
            }
            return;
        }
        boolean allArrived = true;
        for (Integer id : ids) {
            Unit unit = (Unit)Groups.unit.getByID(id.intValue());
            if (unit == null || !unit.isValid() || !(unit.controller() instanceof CommandAI)) continue;
            CommandAI commandAI = (CommandAI)unit.controller();
            if (commandAI.targetPos != null) {
                Vec2[] legs = curLegs.get(id);
                Vec2 dest = legs != null ? (legs[1] != null ? legs[1] : legs[0]) : null;
                if (dest != null && unit.dst(dest) > Math.max(12.0f, unit.hitSize * 0.8f)) {
                    allArrived = false;
                    break;
                }
            }
        }
        if (!allArrived) {
            nextLayerCheck = Time.time + 0.4f;
            return;
        }
        currentLayer++;
        if (currentLayer >= totalFormLayers) {
            layerModeActive = false;
            return;
        }
        ArrayList<Integer> next = layerUnits.get(currentLayer);
        if (next != null && !next.isEmpty()) {
            Formations.dispatch(next, curLegs, true);
            nextLayerCheck = Time.time + Formations.layerDelay() * 60.0f;
        } else {
            nextLayerCheck = 0.0f;
        }
    }

    private static void loopTick() {
        if (!loopActive) {
            return;
        }
        if (!Formations.loop() || Vars.player == null || Vars.state == null || !Vars.state.isGame()) {
            loopActive = false;
            return;
        }
        if (!pending.isEmpty() || Time.time < nextLoop) {
            return;
        }
        nextLoop = Time.time + 20.0f;
        HashMap<Integer, Vec2[]> hashMap = loopToB ? loopB : loopA;
        boolean bl = true;
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        ArrayList<Integer> arrayList2 = new ArrayList<Integer>();
        for (Integer n : loopB.keySet()) {
            Unit unit = (Unit)Groups.unit.getByID(n.intValue());
            if (unit == null || !unit.isValid() || !(unit.controller() instanceof CommandAI)) {
                arrayList2.add(n);
                continue;
            }
            CommandAI commandAI = (CommandAI)unit.controller();
            Vec2[] vec2Array = hashMap.get(n);
            if (commandAI.targetPos != null && vec2Array != null && commandAI.targetPos.dst(vec2Array[0]) > 16.0f && (vec2Array[1] == null || commandAI.targetPos.dst(vec2Array[1]) > 16.0f)) {
                loopActive = false;
                return;
            }
            if (commandAI.targetPos != null) {
                bl = false;
            }
            arrayList.add(n);
        }
        for (Integer n : arrayList2) {
            loopA.remove(n);
            loopB.remove(n);
        }
        if (arrayList.isEmpty()) {
            loopActive = false;
            return;
        }
        if (!bl) {
            loopWaitUntil = 0.0f;
            return;
        }
        if (loopWaitUntil == 0.0f) {
            loopWaitUntil = Time.time + Formations.loopWait() * 60.0f;
            return;
        }
        if (Time.time < loopWaitUntil) {
            return;
        }
        loopWaitUntil = 0.0f;
        loopToB = !loopToB;
        HashMap<Integer, Vec2[]> hashMap2 = loopToB ? loopB : loopA;
        curLegs.clear();
        curLegs.putAll((Map<Integer, Vec2[]>)hashMap2);
        retries.clear();
        Formations.dispatch(arrayList, hashMap2, true);
    }

    public static int mode() {
        return Core.settings.getInt(K_MODE, 0);
    }

    public static void setMode(int n) {
        Core.settings.put(K_MODE, (Object)n);
    }

    public static float spacing() {
        return Core.settings.getFloat(K_SPACE, 2.0f);
    }

    public static int center() {
        return Core.settings.getInt(K_CENTER, 0);
    }

    public static int perLayer() {
        return Core.settings.getInt(K_PER, 0);
    }

    public static int layers() {
        return Core.settings.getInt(K_LAYERS, 0);
    }

    public static boolean gradual() {
        return Core.settings.getBool(K_GRAD, false);
    }

    public static float gradualDelay() {
        return Core.settings.getFloat(K_GDELAY, 0.4f);
    }

    public static boolean loop() {
        return Core.settings.getBool(K_LOOP, false);
    }

    public static float loopWait() {
        return Core.settings.getFloat(K_LWAIT, 1.0f);
    }

    public static boolean approach() {
        return Core.settings.getBool(K_APPR, true);
    }

    public static int face() {
        return Core.settings.getInt(K_FACE, 0);
    }

    public static boolean reform() {
        return Core.settings.getBool(K_REFORM, false);
    }

    public static boolean layerByLayer() {
        return Core.settings.getBool(K_LAYERBY, true);
    }

    public static float layerDelay() {
        return Core.settings.getFloat(K_LAYDELAY, 0.5f);
    }

    public static String modeName(int n) {
        boolean bl = LunariansSettings.language != null && LunariansSettings.language.startsWith("pt");
        switch (n) {
            case 1: {
                return bl ? "Linha" : "Line";
            }
            case 2: {
                return bl ? "Arco" : "Arc";
            }
            case 3: {
                return bl ? "Anel" : "Ring";
            }
            case 4: {
                return bl ? "Triangulo" : "Triangle";
            }
            case 5: {
                return bl ? "Losango" : "Diamond";
            }
            case 6: {
                return bl ? "Quadrado" : "Box";
            }
            case 7: {
                return bl ? "V" : "Chevron";
            }
        }
        return bl ? "Desligado" : "Off";
    }

    public static void init() {
        Events.run((Object)EventType.Trigger.update, Formations::update);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            lastGroup = null;
            curLegs.clear();
            layerModeActive = false;
            layerUnits.clear();
        });
    }

    public static float[] layout(int n, int n2, float f, float f2) {
        float[] fArray;
        float f3 = 0f, f4 = 0f, f5 = 0f, f6 = 0f;
        block5: {
            int n3;
            block6: {
                block4: {
                    fArray = new float[n2 * 2];
                    if (n2 <= 0) {
                        return fArray;
                    }
                    f4 = Mathf.cos((float)f2);
                    f3 = Mathf.sin((float)f2);
                    f5 = -f3;
                    f6 = f4;
                    int n4 = Formations.perLayer();
                    int n5 = Formations.layers();
                    n3 = n4 > 0 ? n4 : (n5 > 0 ? (n2 + n5 - 1) / n5 : (n == 1 ? 14 : n2));
                    n3 = Math.max(1, Math.min(n3, n2));
                    if (n != 1) break block4;
                    for (int i = 0; i < n2; ++i) {
                        int n6 = i / n3;
                        int n7 = i % n3;
                        int n8 = Math.min(n3, n2 - n6 * n3);
                        float f7 = ((float)n7 - (float)(n8 - 1) / 2.0f) * f;
                        float f8 = (float)n6 * f;
                        fArray[i * 2] = f5 * f7 - f4 * f8;
                        fArray[i * 2 + 1] = f6 * f7 - f3 * f8;
                    }
                    break block5;
                }
                if (n != 2) break block6;
                float f9 = Math.max(f * 1.4f, n3 > 1 ? f * (float)(n3 - 1) / 2.8274333f : f);
                float f10 = f2 + (float)Math.PI;
                float f11 = f4 * f9;
                float f12 = f3 * f9;
                for (int i = 0; i < n2; ++i) {
                    int n9 = i / n3;
                    int n10 = i % n3;
                    int n11 = Math.min(n3, n2 - n9 * n3);
                    float f13 = f9 + (float)n9 * f;
                    float f14 = f / f13;
                    float f15 = ((float)n10 - (float)(n11 - 1) / 2.0f) * f14;
                    fArray[i * 2] = f11 + Mathf.cos((float)(f10 + f15)) * f13;
                    fArray[i * 2 + 1] = f12 + Mathf.sin((float)(f10 + f15)) * f13;
                }
                break block5;
            }
            if (n != 3) break block5;
            float f16 = n3 > 1 ? Math.max(f * (float)n3 / ((float)Math.PI * 2), f * 0.95f) : 0.0f;
            float f17 = f2 + (float)Math.PI;
            for (int i = 0; i < n2; ++i) {
                int n12 = i / n3;
                int n13 = i % n3;
                int n14 = Math.min(n3, n2 - n12 * n3);
                float f18 = f16 + (float)n12 * f;
                float f19 = f17 + (float)Math.PI * 2 * (float)n13 / (float)n14;
                fArray[i * 2] = Mathf.cos((float)f19) * f18;
                fArray[i * 2 + 1] = Mathf.sin((float)f19) * f18;
            }
            break block5;
        }
        if (n == 4) {
            // Triangle / wedge: front row = 1 unit, widening toward the back
            int rows = (int)Math.ceil(Math.sqrt((double)n2));
            rows = Math.max(1, Math.min(rows, n2));
            int idx = 0;
            for (int r = 0; r < rows && idx < n2; ++r) {
                int inRow = Math.min(n2 - idx, r + 1);
                for (int c = 0; c < inRow && idx < n2; ++c, ++idx) {
                    float off = ((float)c - (float)(inRow - 1) / 2.0f) * f;
                    float dep = (float)r * f;
                    fArray[idx * 2] = f4 * dep - f3 * off;
                    fArray[idx * 2 + 1] = f3 * dep + f4 * off;
                }
            }
            return fArray;
        }
        if (n == 5) {
            // Diamond: front row = 1 unit, widening then narrowing back
            int rows = (int)Math.ceil(Math.sqrt((double)n2));
            rows = Math.max(1, Math.min(rows, n2));
            int idx = 0;
            for (int r = 0; r < rows && idx < n2; ++r) {
                int inRow = Math.min(n2 - idx, r + 1);
                for (int c = 0; c < inRow && idx < n2; ++c, ++idx) {
                    float off = ((float)c - (float)(inRow - 1) / 2.0f) * f;
                    float dep = (float)r * f;
                    fArray[idx * 2] = f4 * dep - f3 * off;
                    fArray[idx * 2 + 1] = f3 * dep + f4 * off;
                }
            }
            for (int r = rows - 2; r >= 0 && idx < n2; --r) {
                int inRow = Math.min(n2 - idx, r + 1);
                for (int c = 0; c < inRow && idx < n2; ++c, ++idx) {
                    float off = ((float)c - (float)(inRow - 1) / 2.0f) * f;
                    float dep = (float)r * f;
                    fArray[idx * 2] = f4 * dep - f3 * off;
                    fArray[idx * 2 + 1] = f3 * dep + f4 * off;
                }
            }
            return fArray;
        }
        if (n == 6) {
            // Box / square block
            int side = (int)Math.sqrt((double)n2);
            side = Math.max(1, Math.min(side, n2));
            int idx = 0;
            for (int r = 0; r < side && idx < n2; ++r) {
                for (int c = 0; c < side && idx < n2; ++c, ++idx) {
                    float off = ((float)c - (float)(side - 1) / 2.0f) * f;
                    float dep = ((float)r - (float)(side - 1) / 2.0f) * f;
                    fArray[idx * 2] = f4 * dep - f3 * off;
                    fArray[idx * 2 + 1] = f3 * dep + f4 * off;
                }
            }
            return fArray;
        }
        if (n == 7) {
            // V / chevron: opening toward the target (front row = 1 unit, widening)
            int rows = (int)Math.ceil(Math.sqrt((double)n2));
            rows = Math.max(1, Math.min(rows, n2));
            int idx = 0;
            for (int r = 0; r < rows && idx < n2; ++r) {
                int inRow = Math.min(n2 - idx, r + 1);
                for (int c = 0; c < inRow && idx < n2; ++c, ++idx) {
                    float off = ((float)c - (float)(inRow - 1) / 2.0f) * f;
                    float dep = ((float)r * f) * (1.0f - Math.abs(off) / Math.max(0.01f, f * (float)inRow));
                    fArray[idx * 2] = f4 * dep - f3 * off;
                    fArray[idx * 2 + 1] = f3 * dep + f4 * off;
                }
            }
            return fArray;
        }
        return fArray;
    }

    private static void update() {
        Formations.pump();
        Formations.layerTick();
        Formations.loopTick();
        int n = Formations.mode();
        if (n == 0 || Vars.state == null || !Vars.state.isGame() || Vars.player == null || Vars.control == null) {
            return;
        }
        try {
            Seq seq = Vars.control.input.selectedUnits;
            if (seq == null || seq.size < 2) {
                lastGroup = null;
                return;
            }
            UnitGroup unitGroup = null;
            Vec2 vec2 = null;
            Seq seq2 = new Seq();
            for (Object _uo : (Seq)seq) {
                Unit unit = (Unit)_uo;
                Vec2 vec22;
                if (unit == null || !unit.isValid() || !(unit.controller() instanceof CommandAI)) continue;
                CommandAI commandAI = (CommandAI)unit.controller();
                if (commandAI.group == null) {
                    return;
                }
                if (unitGroup == null) {
                    unitGroup = commandAI.group;
                } else if (unitGroup != commandAI.group) {
                    return;
                }
                Vec2 vec23 = vec22 = commandAI.attackTarget != null ? new Vec2(commandAI.attackTarget.x(), commandAI.attackTarget.y()) : commandAI.targetPos;
                if (vec22 == null) {
                    return;
                }
                if (vec2 == null) {
                    vec2 = vec22.cpy();
                } else if (vec2.dst(vec22) > 2.0f) {
                    return;
                }
                seq2.add((Object)unit);
            }
            if (unitGroup == null || seq2.size < 2) {
                return;
            }
            if (unitGroup != lastGroup) {
                lastGroup = unitGroup;
                Formations.apply((Seq<Unit>)seq2, vec2, n);
            }
        }
        catch (Throwable throwable) {
            Log.err((String)"[Lunarians] Formations", (Throwable)throwable);
        }
        Formations.reformCheck();
    }

    private static void apply(Seq<Unit> seq, Vec2 vec2, int n2) {
        int n3;
        Object object;
        float f;
        float f2;
        int n4 = seq.size;
        float f3 = 0.0f;
        float f4 = 0.0f;
        float f5 = 8.0f;
        for (Unit unit2 : seq) {
            f3 += unit2.x;
            f4 += unit2.y;
            f5 = Math.max(f5, unit2.hitSize);
        }
        float f6 = Math.max(Formations.spacing() * 8.0f, f5 * 1.15f);
        float f7 = Formations.face() == 1 ? 1.5707964f : Mathf.atan2((float)(vec2.y - (f4 /= (float)n4)), (float)(vec2.x - (f3 /= (float)n4)));
        float[] fArray = Formations.layout(n2, n4, f6, f7);
        float f8 = Mathf.cos((float)f7);
        float f9 = Mathf.sin((float)f7);
        float f10 = -f9;
        float f11 = f8;
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (int i = 0; i < n4; ++i) {
            arrayList.add(i);
        }
        ArrayList<Unit> arrayList2 = new ArrayList<Unit>();
        for (Unit unit3 : seq) {
            arrayList2.add(unit3);
        }
        if (n2 == 3) {
            f2 = f7 + (float)Math.PI;
            final float ff2a = f2;
            arrayList.sort(Comparator.comparingDouble((Integer n) -> Mathf.mod((float)(Mathf.atan2((float)fArray[n * 2 + 1], (float)fArray[n * 2]) - ff2a), (float)((float)Math.PI * 2))));
            f = f3;
            final float ff12 = f4;
            final float ffa = f;
            final float ff2b = f2;
            arrayList2.sort(Comparator.comparingDouble((Unit unit) -> Mathf.mod((float)(Mathf.atan2((float)(unit.y - ff12), (float)(unit.x - ffa)) - ff2b), (float)((float)Math.PI * 2))));
        } else {
            final float ff10 = f10, ff11 = f11, ff8 = f8, ff9 = f9;
            arrayList.sort(Comparator.comparingDouble((Integer n) -> fArray[n * 2] * ff10 + fArray[n * 2 + 1] * ff11).thenComparingDouble((Integer n) -> -(fArray[n * 2] * ff8 + fArray[n * 2 + 1] * ff9)));
            f2 = f3;
            f = f4;
            final float ff2c = f2, ffc = f;
            arrayList2.sort(Comparator.comparingDouble((Unit unit) -> (unit.x - ff2c) * ff10 + (unit.y - ffc) * ff11).thenComparingDouble((Unit unit) -> -((unit.x - ff2c) * ff8 + (unit.y - ffc) * ff9)));
        }
        int n5 = Formations.center();
        if (n5 == 1) {
            object = (Unit)seq.first();
            int n6 = arrayList2.indexOf(object);
            int n7 = (Integer)arrayList.get(Math.max(0, n6));
            f2 = vec2.x - fArray[n7 * 2];
            f = vec2.y - fArray[n7 * 2 + 1];
        } else if (n5 == 2) {
            f2 = Core.camera.position.x;
            f = Core.camera.position.y;
        } else {
            f2 = vec2.x;
            f = vec2.y;
        }
        curLegs.clear();
        retries.clear();
        loopA.clear();
        loopB.clear();
        reformed = false;
        layerModeActive = false;
        layerUnits.clear();
        currentLayer = 0;
        totalFormLayers = 1;
        nextLayerCheck = 0.0f;
        object = Formations.layout(n2, n4, f6, f7 + (float)Math.PI);
        ArrayList<Integer> arrayList3 = new ArrayList<Integer>();
        float f13 = f8;
        float f14 = f9;
        ArrayList<Integer> arrayList4 = new ArrayList<Integer>();
        for (n3 = 0; n3 < n4; ++n3) {
            arrayList4.add(n3);
        }
        arrayList4.sort(Comparator.comparingDouble(n -> -(fArray[(Integer)arrayList.get((int)n) * 2] * f13 + fArray[(Integer)arrayList.get((int)n) * 2 + 1] * f14)));
        int unitsPerLayer = Math.max(1, Math.min(n4, Formations.perLayer() > 0 ? Formations.perLayer() : (Formations.layers() > 0 ? (n4 + Formations.layers() - 1) / Formations.layers() : (n2 == 1 ? 14 : n4))));
        for (n3 = 0; n3 < n4; ++n3) {
            Unit unit4 = (Unit)arrayList2.get(n3);
            int n8 = (Integer)arrayList.get(n3);
            loopB.put(unit4.id, Formations.makeLeg(unit4, f2 + fArray[n8 * 2], f + fArray[n8 * 2 + 1], f8, f9));
            loopA.put(unit4.id, Formations.makeLeg(unit4, f3 + ((float[])object)[n8 * 2], f4 + ((float[])object)[n8 * 2 + 1], -f8, -f9));
        }
        curLegs.putAll(loopB);
        for (n3 = 0; n3 < unitsPerLayer * 12; ++n3) { layerUnits.put(n3, new ArrayList<Integer>()); }
        Iterator iterator = arrayList4.iterator();
        while (iterator.hasNext()) {
            int n9 = (Integer)iterator.next();
            int uid = ((Unit)arrayList2.get((int)n9)).id;
            arrayList3.add(uid);
            int slotIdx = (Integer)arrayList.get(n9);
            int layerIdx = slotIdx / unitsPerLayer;
            ArrayList<Integer> lu = layerUnits.get(layerIdx);
            if (lu == null) { lu = new ArrayList<Integer>(); layerUnits.put(layerIdx, lu); }
            lu.add(uid);
            if (layerIdx + 1 > totalFormLayers) totalFormLayers = layerIdx + 1;
        }
        boolean doLayerBy = Formations.layerByLayer() && totalFormLayers > 1 && !Formations.loop();
        if (doLayerBy) {
            layerModeActive = true;
            currentLayer = 0;
            ArrayList<Integer> first = layerUnits.get(0);
            if (first != null && !first.isEmpty()) Formations.dispatch(first, curLegs, true);
        } else {
            Formations.dispatch(arrayList3, curLegs, true);
        }
        loopActive = Formations.loop() && !layerModeActive;
        loopToB = true;
        loopWaitUntil = 0.0f;
        nextLoop = Time.time + 60.0f;
        lastGroup = null;
        nextCheck = Time.time + 30.0f;
        if (Vars.ui != null) {
            String msg = Formations.modeName(n2) + " x" + n4 + (totalFormLayers > 1 ? " (" + totalFormLayers + Formations.tr(" layers)", " camadas)") : "");
            if (doLayerBy) msg = msg + Formations.tr(" layer by layer", " camada por camada");
            Vars.ui.showInfoToast(msg, 1.8f);
        }
    }

    private static void reformCheck() {
        if (!Formations.reform() || loopActive || !pending.isEmpty() || curLegs.isEmpty() || Time.time < nextCheck) {
            return;
        }
        nextCheck = Time.time + 30.0f;
        boolean bl = true;
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        ArrayList<Integer> arrayList2 = new ArrayList<Integer>();
        for (Map.Entry<Integer, Vec2[]> entry : curLegs.entrySet()) {
            Unit unit = (Unit)Groups.unit.getByID(entry.getKey().intValue());
            if (unit == null || !unit.isValid() || !(unit.controller() instanceof CommandAI)) {
                arrayList.add(entry.getKey());
                continue;
            }
            CommandAI commandAI = (CommandAI)unit.controller();
            if (commandAI.targetPos != null) {
                bl = false;
                continue;
            }
            float f = Math.max(6.0f, unit.hitSize * 0.45f);
            Integer n = retries.get(entry.getKey());
            if (!(unit.dst((Position)entry.getValue()[2]) > f) || n != null && n >= 3) continue;
            arrayList2.add(entry.getKey());
        }
        for (Integer n : arrayList) {
            curLegs.remove(n);
        }
        if (!bl) {
            return;
        }
        if (arrayList2.isEmpty()) {
            curLegs.clear();
            return;
        }
        Iterator iterator = arrayList2.iterator();
        while (iterator.hasNext()) {
            Integer nid = (Integer)iterator.next();
            retries.put(nid, retries.containsKey(nid) ? ((Integer)retries.get(nid)) + 1 : 1);
        }
        Formations.dispatch(arrayList2, curLegs, false);
    }

    public static void openDialog() {
        Runnable[] runnableArray = new Runnable[1];
        BaseDialog baseDialog = new BaseDialog(Formations.tr("Formations", "Formacoes"));
        UiStyle.register(baseDialog);
        Table table = new Table();
        runnableArray[0] = () -> {
            table.clear();
            table.defaults().pad(4.0f);
            table.add((CharSequence)Formations.tr("Select units in command mode, choose a shape and send them to move or attack. The mod sends one position per unit.", "Selecione unidades no modo comando, escolha o formato e mande mover ou atacar. O mod envia uma posicao por unidade.")).color(UiStyle.TEXT_DIM).wrap().width(420.0f).left().row();
            UiStyle.section(table, Formations.tr("Shape", "Formato"));
            table.table(tb -> {
                int n = 0;
                while (n <= 7) {
                    int n2 = n++;
                    tb.button(button -> {
                        button.add((Element)new Preview(n2, 92.0f, 62.0f)).row();
                        button.add((CharSequence)Formations.modeName(n2)).padTop(3.0f);
                    }, (Button.ButtonStyle)Styles.togglet, () -> {
                        Formations.setMode(n2);
                        runnableArray[0].run();
                    }).checked(button -> Formations.mode() == n2).size(104.0f, 100.0f).pad(3.0f);
                }
            }).row();
            UiStyle.section(table, Formations.tr("Spacing", "Espacamento"));
            table.table(tb -> {
                Slider slider = new Slider(1.0f, 8.0f, 0.5f, false);
                slider.setValue(Formations.spacing());
                Label label = new Label((CharSequence)(String.format("%.1f", Float.valueOf(Formations.spacing())) + Formations.tr(" blocks", " blocos")));
                slider.moved(f -> {
                    Core.settings.put(K_SPACE, (Object)Float.valueOf(f));
                    label.setText((CharSequence)(String.format("%.1f", Float.valueOf(f)) + Formations.tr(" blocks", " blocos")));
                });
                tb.add((Element)slider).width(240.0f);
                tb.add((Element)label).width(90.0f).padLeft(8.0f);
            }).row();
            table.add((CharSequence)Formations.tr("Never closer than the unit size.", "Nunca fica mais perto que o tamanho da unidade.")).color(UiStyle.TEXT_DIM).fontScale(0.8f).left().row();
            UiStyle.section(table, Formations.tr("Layers", "Camadas"));
            table.add((CharSequence)Formations.tr("Add more layers behind the first one. 0 = automatic.", "Adicione mais camadas atras da primeira. 0 = automatico.")).color(UiStyle.TEXT_DIM).wrap().width(420.0f).left().row();
            table.table(tb -> {
                tb.add((CharSequence)Formations.tr("Layers", "Camadas")).width(110.0f).left();
                Slider slider = new Slider(0.0f, 10.0f, 1.0f, false);
                slider.setValue((float)Formations.layers());
                Label label = new Label((CharSequence)(Formations.layers() == 0 ? Formations.tr("Auto", "Auto") : String.valueOf(Formations.layers())));
                slider.moved(f -> {
                    Core.settings.put(K_LAYERS, (Object)((int)f));
                    label.setText((CharSequence)((int)f == 0 ? Formations.tr("Auto", "Auto") : String.valueOf((int)f)));
                });
                tb.add((Element)slider).width(200.0f);
                tb.add((Element)label).width(60.0f).padLeft(8.0f);
            }).row();
            table.table(tb -> {
                tb.add((CharSequence)Formations.tr("Units per layer", "Unidades por camada")).width(110.0f).left().wrap();
                Slider slider = new Slider(0.0f, 50.0f, 1.0f, false);
                slider.setValue((float)Formations.perLayer());
                Label label = new Label((CharSequence)(Formations.perLayer() == 0 ? Formations.tr("Auto", "Auto") : String.valueOf(Formations.perLayer())));
                slider.moved(f -> {
                    Core.settings.put(K_PER, (Object)((int)f));
                    label.setText((CharSequence)((int)f == 0 ? Formations.tr("Auto", "Auto") : String.valueOf((int)f)));
                });
                tb.add((Element)slider).width(200.0f);
                tb.add((Element)label).width(60.0f).padLeft(8.0f);
            }).row();
            UiStyle.section(table, Formations.tr("Gradual and loop", "Gradual e loop"));
            table.table(t -> t.check(Formations.tr("Layer by layer (deploy one layer at a time)", "Camada por camada (envia uma camada por vez)"), Formations.layerByLayer(), bl -> Core.settings.put(K_LAYERBY, (Object)bl)).left().wrap()).left().row();
            table.table(tb -> {
                tb.add((CharSequence)Formations.tr("Delay between layers", "Atraso entre camadas")).width(140.0f).left().wrap();
                Slider slider = new Slider(0.0f, 3.0f, 0.1f, false);
                slider.setValue(Formations.layerDelay());
                Label label = new Label((CharSequence)(String.format("%.1fs", Float.valueOf(Formations.layerDelay()))));
                slider.moved(f -> {
                    Core.settings.put(K_LAYDELAY, (Object)Float.valueOf(f));
                    label.setText((CharSequence)(String.format("%.1fs", Float.valueOf(f))));
                });
                tb.add((Element)slider).width(160.0f);
                tb.add((Element)label).width(60.0f).padLeft(8.0f);
            }).left().wrap().row();
            table.table(tb -> tb.check(Formations.tr("Gradual (send units one by one, front first)", "Gradual (envia as unidades uma a uma, frente primeiro)"), Formations.gradual(), bl -> Core.settings.put(K_GRAD, (Object)bl)).left()).left().row();
            table.table(tb -> {
                tb.add((CharSequence)Formations.tr("Delay between units", "Intervalo entre unidades")).width(110.0f).left().wrap();
                Slider slider = new Slider(0.1f, 3.0f, 0.1f, false);
                slider.setValue(Formations.gradualDelay());
                Label label = new Label((CharSequence)String.format("%.1fs", Float.valueOf(Formations.gradualDelay())));
                slider.moved(f -> {
                    Core.settings.put(K_GDELAY, (Object)Float.valueOf(f));
                    label.setText((CharSequence)String.format("%.1fs", Float.valueOf(f)));
                });
                tb.add((Element)slider).width(200.0f);
                tb.add((Element)label).width(60.0f).padLeft(8.0f);
            }).row();
            table.table(tb -> tb.check(Formations.tr("Loop (go back and forth keeping the formation)", "Loop (vai e volta mantendo a formacao)"), Formations.loop(), bl -> {
                Core.settings.put(K_LOOP, (Object)bl);
                if (!bl) {
                    loopActive = false;
                }
            }).left()).left().row();
            table.table(tb -> {
                tb.add((CharSequence)Formations.tr("Wait at each end", "Espera em cada ponta")).width(110.0f).left().wrap();
                Slider slider = new Slider(0.0f, 10.0f, 0.5f, false);
                slider.setValue(Formations.loopWait());
                Label label = new Label((CharSequence)String.format("%.1fs", Float.valueOf(Formations.loopWait())));
                slider.moved(f -> {
                    Core.settings.put(K_LWAIT, (Object)Float.valueOf(f));
                    label.setText((CharSequence)String.format("%.1fs", Float.valueOf(f)));
                });
                tb.add((Element)slider).width(200.0f);
                tb.add((Element)label).width(60.0f).padLeft(8.0f);
            }).row();
            table.add((CharSequence)Formations.tr("Loop stops when you give the units a new command or turn it off.", "O loop para quando voce da um novo comando as unidades ou desliga.")).color(UiStyle.TEXT_DIM).wrap().width(420.0f).left().row();
            UiStyle.section(table, Formations.tr("Center", "Centro"));
            table.table(tb -> {
                String[] stringArray = new String[]{Formations.tr("Command target", "Alvo do comando"), Formations.tr("First selected", "Primeiro da selecao"), "Camera"};
                for (int i = 0; i < 3; ++i) {
                    int n = i;
                    tb.button(stringArray[i], Styles.togglet, () -> Core.settings.put(K_CENTER, (Object)n)).checked(textButton -> Formations.center() == n).size(138.0f, 42.0f).pad(2.0f);
                }
            }).row();
            UiStyle.section(table, Formations.tr("Facing", "Orientacao"));
            table.table(tb -> {
                String[] stringArray = new String[]{Formations.tr("Toward target / movement", "Para o alvo / movimento"), Formations.tr("Fixed (north)", "Fixa (norte)")};
                for (int i = 0; i < 2; ++i) {
                    int n = i;
                    tb.button(stringArray[i], Styles.togglet, () -> Core.settings.put(K_FACE, (Object)n)).checked(textButton -> Formations.face() == n).size(210.0f, 42.0f).pad(2.0f);
                }
            }).row();
            table.table(tb -> tb.check(Formations.tr("Aligned approach (all units arrive from the same direction)", "Aproximacao alinhada (todas chegam pela mesma direcao)"), Formations.approach(), bl -> Core.settings.put(K_APPR, (Object)bl)).left()).left().row();
            table.table(tb -> tb.check(Formations.tr("Reform on stop (re-send slots when they stop out of place)", "Reformar ao parar (reenvia os slots se pararem fora do lugar)"), Formations.reform(), bl -> Core.settings.put(K_REFORM, (Object)bl)).left()).left().row();
        };
        runnableArray[0].run();
        baseDialog.cont.pane((Element)table).grow().minWidth(440.0f);
        baseDialog.addCloseButton();
        baseDialog.show();
    }

    private static String tr(String string, String string2) {
        return LunariansSettings.language != null && LunariansSettings.language.startsWith("pt") ? string2 : string;
    }

    private static class Pend {
        int id;
        Vec2 p;
        boolean queue;
        float at;

        Pend(int n, Vec2 vec2, boolean bl, float f) {
            this.id = n;
            this.p = vec2;
            this.queue = bl;
            this.at = f;
        }
    }

    static class Preview
    extends Element {
        final int mode;
        final float w;
        final float h;

        Preview(int n, float f, float f2) {
            this.mode = n;
            this.w = f;
            this.h = f2;
        }

        public float getPrefWidth() {
            return this.w;
        }

        public float getPrefHeight() {
            return this.h;
        }

        public void draw() {
            float f = this.parentAlpha;
            Draw.color((float)0.07f, (float)0.08f, (float)0.1f, (float)(0.9f * f));
            Fill.rect((float)(this.x + this.width / 2.0f), (float)(this.y + this.height / 2.0f), (float)this.width, (float)this.height);
            if (this.mode == 0) {
                Draw.color((float)UiStyle.DIM.r, (float)UiStyle.DIM.g, (float)UiStyle.DIM.b, (float)f);
                Fill.rect((float)(this.x + this.width / 2.0f), (float)(this.y + this.height / 2.0f), (float)18.0f, (float)3.0f);
                Draw.color();
                return;
            }
            int n = Formations.perLayer() > 0 || Formations.layers() > 0 ? Math.min(30, Math.max(1, (Formations.perLayer() > 0 ? Formations.perLayer() : 5) * (Formations.layers() > 0 ? Formations.layers() : 2))) : (this.mode == 3 ? 9 : 7);
            float[] fArray = Formations.layout(this.mode, n, 1.0f, 1.5707964f);
            float f2 = 1.0E9f;
            float f3 = -1.0E9f;
            float f4 = 1.0E9f;
            float f5 = -1.0E9f;
            for (int i = 0; i < n; ++i) {
                f2 = Math.min(f2, fArray[i * 2]);
                f3 = Math.max(f3, fArray[i * 2]);
                f4 = Math.min(f4, fArray[i * 2 + 1]);
                f5 = Math.max(f5, fArray[i * 2 + 1]);
            }
            float f6 = Math.min((this.width - 26.0f) / Math.max(0.01f, f3 - f2), (this.height - 22.0f) / Math.max(0.01f, f5 - f4));
            f6 = Math.min(f6, 16.0f);
            float f7 = (f2 + f3) / 2.0f;
            float f8 = (f4 + f5) / 2.0f;
            for (int i = 0; i < n; ++i) {
                Draw.color((float)Pal.accent.r, (float)Pal.accent.g, (float)Pal.accent.b, (float)f);
                Fill.circle((float)(this.x + this.width / 2.0f + (fArray[i * 2] - f7) * f6), (float)(this.y + this.height / 2.0f + (fArray[i * 2 + 1] - f8) * f6), (float)3.6f);
            }
            Draw.color((float)1.0f, (float)1.0f, (float)1.0f, (float)(0.5f * f));
            Fill.tri((float)(this.x + this.width - 11.0f), (float)(this.y + this.height - 8.0f), (float)(this.x + this.width - 5.0f), (float)(this.y + this.height - 8.0f), (float)(this.x + this.width - 8.0f), (float)(this.y + this.height - 3.0f));
            Draw.color();
        }
    }
}

