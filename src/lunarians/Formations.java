package lunarians;

import arc.Core;
import arc.Events;
import arc.func.Boolc;
import arc.func.Boolf;
import arc.func.Cons;
import arc.func.Floatc;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.Element;
import arc.scene.ui.Button;
import arc.scene.ui.Label;
import arc.scene.ui.Slider;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.ToDoubleFunction;
import mindustry.Vars;
import mindustry.ai.UnitGroup;
import mindustry.ai.types.CommandAI;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/Formations.class */
public class Formations {
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
    private static Object lastGroup = null;
    private static final HashMap<Integer, Vec2[]> curLegs = new HashMap<>();
    private static final HashMap<Integer, Integer> retries = new HashMap<>();
    private static float nextCheck = 0.0f;
    private static boolean reformed = false;
    private static final ArrayList<Pend> pending = new ArrayList<>();
    private static final HashMap<Integer, Vec2[]> loopA = new HashMap<>();
    private static final HashMap<Integer, Vec2[]> loopB = new HashMap<>();
    private static boolean loopActive = false;
    private static boolean loopToB = true;
    private static float loopWaitUntil = 0.0f;
    private static float nextLoop = 0.0f;

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Cons {
        public final /* synthetic */ Runnable[] f$0;

        public final void get(Object obj) {
            Formations.lambda$openDialog$11(this.f$0, (Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass1 implements Cons {
        public final void get(Object obj) {
            Formations.lambda$openDialog$33((Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$10, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass10 implements Cons {
        public final void get(Object obj) {
            Formations.lambda$openDialog$28((Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$11, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass11 implements Cons {
        public final void get(Object obj) {
            Formations.lambda$openDialog$31((Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$12, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass12 implements Runnable {
        public final /* synthetic */ Table f$0;
        public final /* synthetic */ Runnable[] f$1;

        public /* synthetic */ AnonymousClass12(Table table, Runnable[] runnableArr) {
            this.f$0 = table;
            this.f$1 = runnableArr;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Formations.lambda$openDialog$36(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$13, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass13 implements Floatc {
        public final /* synthetic */ Label f$0;

        public final void get(float f) {
            Formations.lambda$openDialog$14(this.f$0, f);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$14, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass14 implements Floatc {
        public final /* synthetic */ Label f$0;

        public final void get(float f) {
            Formations.lambda$openDialog$12(this.f$0, f);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$15, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass15 implements Cons {
        public final /* synthetic */ int f$0;

        public final void get(Object obj) {
            Formations.lambda$openDialog$8(this.f$0, (Button) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$16, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass16 implements Runnable {
        public final /* synthetic */ int f$0;
        public final /* synthetic */ Runnable[] f$1;

        public /* synthetic */ AnonymousClass16(int i, Runnable[] runnableArr) {
            this.f$0 = i;
            this.f$1 = runnableArr;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Formations.lambda$openDialog$9(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$17, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass17 implements Boolf {
        public final /* synthetic */ int f$0;

        public final boolean get(Object obj) {
            return Formations.lambda$openDialog$10(this.f$0, (Button) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$18, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass18 implements ToDoubleFunction {
        public final /* synthetic */ float[] f$0;
        public final /* synthetic */ float f$1;

        public /* synthetic */ AnonymousClass18(float[] fArr, float f) {
            this.f$0 = fArr;
            this.f$1 = f;
        }

        @Override // java.util.function.ToDoubleFunction
        public final double applyAsDouble(Object obj) {
            return Formations.lambda$apply$1(this.f$0, this.f$1, (Integer) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$19, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass19 implements ToDoubleFunction {
        public final /* synthetic */ float f$0;
        public final /* synthetic */ float f$1;
        public final /* synthetic */ float f$2;

        public /* synthetic */ AnonymousClass19(float f, float f2, float f3) {
            this.f$0 = f;
            this.f$1 = f2;
            this.f$2 = f3;
        }

        @Override // java.util.function.ToDoubleFunction
        public final double applyAsDouble(Object obj) {
            return Formations.lambda$apply$2(this.f$0, this.f$1, this.f$2, (Unit) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass2 implements Cons {
        public final void get(Object obj) {
            Formations.lambda$openDialog$35((Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$20, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass20 implements ToDoubleFunction {
        public final /* synthetic */ float[] f$0;
        public final /* synthetic */ float f$1;
        public final /* synthetic */ float f$2;

        public /* synthetic */ AnonymousClass20(float[] fArr, float f, float f2) {
            this.f$0 = fArr;
            this.f$1 = f;
            this.f$2 = f2;
        }

        @Override // java.util.function.ToDoubleFunction
        public final double applyAsDouble(Object obj) {
            return Formations.lambda$apply$3(this.f$0, this.f$1, this.f$2, (Integer) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$21, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass21 implements ToDoubleFunction {
        public final /* synthetic */ float[] f$0;
        public final /* synthetic */ float f$1;
        public final /* synthetic */ float f$2;

        public /* synthetic */ AnonymousClass21(float[] fArr, float f, float f2) {
            this.f$0 = fArr;
            this.f$1 = f;
            this.f$2 = f2;
        }

        @Override // java.util.function.ToDoubleFunction
        public final double applyAsDouble(Object obj) {
            return Formations.lambda$apply$4(this.f$0, this.f$1, this.f$2, (Integer) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$22, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass22 implements ToDoubleFunction {
        public final /* synthetic */ float f$0;
        public final /* synthetic */ float f$1;
        public final /* synthetic */ float f$2;
        public final /* synthetic */ float f$3;

        public /* synthetic */ AnonymousClass22(float f, float f2, float f3, float f4) {
            this.f$0 = f;
            this.f$1 = f2;
            this.f$2 = f3;
            this.f$3 = f4;
        }

        @Override // java.util.function.ToDoubleFunction
        public final double applyAsDouble(Object obj) {
            return Formations.lambda$apply$5(this.f$0, this.f$1, this.f$2, this.f$3, (Unit) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$23, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass23 implements ToDoubleFunction {
        public final /* synthetic */ float f$0;
        public final /* synthetic */ float f$1;
        public final /* synthetic */ float f$2;
        public final /* synthetic */ float f$3;

        public /* synthetic */ AnonymousClass23(float f, float f2, float f3, float f4) {
            this.f$0 = f;
            this.f$1 = f2;
            this.f$2 = f3;
            this.f$3 = f4;
        }

        @Override // java.util.function.ToDoubleFunction
        public final double applyAsDouble(Object obj) {
            return Formations.lambda$apply$6(this.f$0, this.f$1, this.f$2, this.f$3, (Unit) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$24, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass24 implements ToDoubleFunction {
        public final /* synthetic */ float[] f$0;
        public final /* synthetic */ ArrayList f$1;
        public final /* synthetic */ float f$2;
        public final /* synthetic */ float f$3;

        public /* synthetic */ AnonymousClass24(float[] fArr, ArrayList arrayList, float f, float f2) {
            this.f$0 = fArr;
            this.f$1 = arrayList;
            this.f$2 = f;
            this.f$3 = f2;
        }

        @Override // java.util.function.ToDoubleFunction
        public final double applyAsDouble(Object obj) {
            return Formations.lambda$apply$7(this.f$0, this.f$1, this.f$2, this.f$3, (Integer) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$25, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass25 implements Boolc {
        public final void get(boolean z) {
            Formations.lambda$openDialog$22(z);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$26, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass26 implements Boolc {
        public final void get(boolean z) {
            Formations.lambda$openDialog$34(z);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$27, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass27 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            Formations.$r8$lambda$ZcLRKmccIs055yTvzZsQWyRcP-k();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$28, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass28 implements Cons {
        public final void get(Object obj) {
            Formations.lambda$init$0((EventType.WorldLoadEvent) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$29, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass29 implements Floatc {
        public final /* synthetic */ Label f$0;

        public final void get(float f) {
            Formations.lambda$openDialog$16(this.f$0, f);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$3, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass3 implements Cons {
        public final void get(Object obj) {
            Formations.lambda$openDialog$13((Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$30, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass30 implements Runnable {
        public final /* synthetic */ int f$0;

        @Override // java.lang.Runnable
        public final void run() {
            Formations.lambda$openDialog$26(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$31, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass31 implements Boolf {
        public final /* synthetic */ int f$0;

        public final boolean get(Object obj) {
            return Formations.lambda$openDialog$27(this.f$0, (TextButton) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$32, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass32 implements Floatc {
        public final /* synthetic */ Label f$0;

        public final void get(float f) {
            Formations.lambda$openDialog$24(this.f$0, f);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$33, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass33 implements Boolc {
        public final void get(boolean z) {
            Formations.lambda$openDialog$32(z);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$34, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass34 implements Floatc {
        public final /* synthetic */ Label f$0;

        public final void get(float f) {
            Formations.lambda$openDialog$20(this.f$0, f);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$35, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass35 implements Boolc {
        public final void get(boolean z) {
            Formations.lambda$openDialog$18(z);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$36, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass36 implements Runnable {
        public final /* synthetic */ int f$0;

        @Override // java.lang.Runnable
        public final void run() {
            Formations.lambda$openDialog$29(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$37, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass37 implements Boolf {
        public final /* synthetic */ int f$0;

        public final boolean get(Object obj) {
            return Formations.lambda$openDialog$30(this.f$0, (TextButton) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$4, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass4 implements Cons {
        public final void get(Object obj) {
            Formations.lambda$openDialog$15((Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$5, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass5 implements Cons {
        public final void get(Object obj) {
            Formations.lambda$openDialog$17((Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$6, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass6 implements Cons {
        public final void get(Object obj) {
            Formations.lambda$openDialog$19((Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$7, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass7 implements Cons {
        public final void get(Object obj) {
            Formations.lambda$openDialog$21((Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$8, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass8 implements Cons {
        public final void get(Object obj) {
            Formations.lambda$openDialog$23((Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.Formations$9, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass9 implements Cons {
        public final void get(Object obj) {
            Formations.lambda$openDialog$25((Table) obj);
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      classes.dex
     */
    /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/Formations$Pend.class */
    private static class Pend {
        int id;
        Vec2 p;
        boolean queue;
        float at;

        Pend(int i, Vec2 vec2, boolean z, float f) {
            this.id = i;
            this.p = vec2;
            this.queue = z;
            this.at = f;
        }
    }

    private static void pump() {
        if (pending.isEmpty()) {
            return;
        }
        int i = 0;
        while (i < pending.size()) {
            Pend pend = pending.get(i);
            if (Time.time >= pend.at) {
                pending.remove(i);
                try {
                    Call.commandUnits(Vars.player, new int[]{pend.id}, (Building) null, (Unit) null, pend.p, pend.queue, true);
                } catch (Throwable th) {
                    Log.err("[Lunarians] Formations send", th);
                }
            } else {
                i++;
            }
        }
    }

    private static void dispatch(ArrayList<Integer> arrayList, HashMap<Integer, Vec2[]> hashMap, boolean z) {
        pending.clear();
        float gradualDelay = (z && gradual()) ? gradualDelay() * 60.0f : 0.0f;
        float f = Time.time;
        for (int i = 0; i < arrayList.size(); i++) {
            Vec2[] vec2Arr = hashMap.get(arrayList.get(i));
            if (vec2Arr != null) {
                float f2 = f + (gradualDelay * i);
                if (vec2Arr[1] != null) {
                    pending.add(new Pend(arrayList.get(i).intValue(), vec2Arr[1], false, f2));
                    pending.add(new Pend(arrayList.get(i).intValue(), vec2Arr[0], true, f2));
                } else {
                    pending.add(new Pend(arrayList.get(i).intValue(), vec2Arr[0], false, f2));
                }
            }
        }
        pump();
    }

    private static Vec2[] makeLeg(Unit unit, float f, float f2, float f3, float f4) {
        float max = Math.max(5.0f, unit.hitSize / 2.0f);
        float unitWidth = Vars.world.unitWidth();
        float unitHeight = Vars.world.unitHeight();
        Vec2 vec2 = new Vec2(Mathf.clamp(f, 0.0f, unitWidth), Mathf.clamp(f2, 0.0f, unitHeight));
        if (!approach()) {
            return new Vec2[]{vec2, null, vec2};
        }
        float f5 = 24.0f + unit.hitSize;
        return new Vec2[]{new Vec2(Mathf.clamp(f + (f3 * max), 0.0f, unitWidth), Mathf.clamp(f2 + (f4 * max), 0.0f, unitHeight)), new Vec2(Mathf.clamp(f - (f3 * f5), 0.0f, unitWidth), Mathf.clamp(f2 - (f4 * f5), 0.0f, unitHeight)), vec2};
    }

    private static void loopTick() {
        if (loopActive) {
            if (!loop() || Vars.player == null || Vars.state == null || !Vars.state.isGame()) {
                loopActive = false;
                return;
            }
            if (!pending.isEmpty() || Time.time < nextLoop) {
                return;
            }
            nextLoop = Time.time + 20.0f;
            HashMap<Integer, Vec2[]> hashMap = loopToB ? loopB : loopA;
            boolean z = true;
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            for (Integer num : loopB.keySet()) {
                Unit byID = Groups.unit.getByID(num.intValue());
                if (byID != null && byID.isValid() && (byID.controller() instanceof CommandAI)) {
                    CommandAI controller = byID.controller();
                    Vec2[] vec2Arr = hashMap.get(num);
                    if (controller.targetPos != null && vec2Arr != null && controller.targetPos.dst(vec2Arr[0]) > 16.0f && (vec2Arr[1] == null || controller.targetPos.dst(vec2Arr[1]) > 16.0f)) {
                        loopActive = false;
                        return;
                    } else {
                        if (controller.targetPos != null) {
                            z = false;
                        }
                        arrayList.add(num);
                    }
                } else {
                    arrayList2.add(num);
                }
            }
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                Integer num2 = (Integer) it.next();
                loopA.remove(num2);
                loopB.remove(num2);
            }
            if (arrayList.isEmpty()) {
                loopActive = false;
                return;
            }
            if (!z) {
                loopWaitUntil = 0.0f;
                return;
            }
            if (loopWaitUntil == 0.0f) {
                loopWaitUntil = Time.time + (loopWait() * 60.0f);
                return;
            }
            if (Time.time < loopWaitUntil) {
                return;
            }
            loopWaitUntil = 0.0f;
            loopToB = !loopToB;
            HashMap<Integer, Vec2[]> hashMap2 = loopToB ? loopB : loopA;
            curLegs.clear();
            curLegs.putAll(hashMap2);
            retries.clear();
            dispatch(arrayList, hashMap2, true);
        }
    }

    public static int mode() {
        return Core.settings.getInt(K_MODE, 0);
    }

    public static void setMode(int i) {
        Core.settings.put(K_MODE, Integer.valueOf(i));
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

    public static String modeName(int i) {
        boolean z = LunariansSettings.language != null && LunariansSettings.language.startsWith("pt");
        switch (i) {
            case 1:
                return z ? "Linha" : "Line";
            case 2:
                return z ? "Arco" : "Arc";
            case 3:
                return z ? "Anel" : "Ring";
            default:
                return z ? "Desligado" : "Off";
        }
    }

    public static void init() {
        Events.run(EventType.Trigger.update, Formations::update);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            lastGroup = null;
            curLegs.clear();
        });
    }

    public static float[] layout(int i, int i2, float f, float f2) {
        int i3;
        float[] fArr = new float[i2 * 2];
        if (i2 <= 0) {
            return fArr;
        }
        float cos = Mathf.cos(f2);
        float sin = Mathf.sin(f2);
        float f3 = -sin;
        int perLayer = perLayer();
        int layers = layers();
        if (perLayer > 0) {
            i3 = perLayer;
        } else if (layers > 0) {
            i3 = ((i2 + layers) - 1) / layers;
        } else {
            i3 = i == 1 ? 14 : i2;
        }
        int max = Math.max(1, Math.min(i3, i2));
        if (i == 1) {
            for (int i4 = 0; i4 < i2; i4++) {
                int i5 = i4 / max;
                float min = ((i4 % max) - ((Math.min(max, i2 - (i5 * max)) - 1) / 2.0f)) * f;
                float f4 = i5 * f;
                fArr[i4 * 2] = (f3 * min) - (cos * f4);
                fArr[(i4 * 2) + 1] = (cos * min) - (sin * f4);
            }
        } else if (i == 2) {
            float max2 = Math.max(f * 1.4f, max > 1 ? (f * (max - 1)) / 2.8274333f : f);
            float f5 = f2 + 3.1415927f;
            float f6 = cos * max2;
            float f7 = sin * max2;
            for (int i6 = 0; i6 < i2; i6++) {
                int i7 = i6 / max;
                int i8 = i6 % max;
                int min2 = Math.min(max, i2 - (i7 * max));
                float f8 = max2 + (i7 * f);
                float f9 = (i8 - ((min2 - 1) / 2.0f)) * (f / f8);
                fArr[i6 * 2] = f6 + (Mathf.cos(f5 + f9) * f8);
                fArr[(i6 * 2) + 1] = f7 + (Mathf.sin(f5 + f9) * f8);
            }
        } else if (i == 3) {
            float max3 = max > 1 ? Math.max((f * max) / TWO_PI, f * 0.95f) : 0.0f;
            float f10 = f2 + 3.1415927f;
            for (int i9 = 0; i9 < i2; i9++) {
                int i10 = i9 / max;
                int i11 = i9 % max;
                int min3 = Math.min(max, i2 - (i10 * max));
                float f11 = max3 + (i10 * f);
                float f12 = f10 + ((TWO_PI * i11) / min3);
                fArr[i9 * 2] = Mathf.cos(f12) * f11;
                fArr[(i9 * 2) + 1] = Mathf.sin(f12) * f11;
            }
        }
        return fArr;
    }

    private static void update() {
        Seq seq;
        pump();
        loopTick();
        int mode = mode();
        if (mode == 0 || Vars.state == null || !Vars.state.isGame() || Vars.player == null || Vars.control == null) {
            return;
        }
        try {
            seq = Vars.control.input.selectedUnits;
        } catch (Throwable th) {
            Log.err("[Lunarians] Formations", th);
        }
        if (seq == null || seq.size < 2) {
            lastGroup = null;
            return;
        }
        UnitGroup unitGroup = null;
        Vec2 vec2 = null;
        Seq seq2 = new Seq();
        Iterator it = seq.iterator();
        while (it.hasNext()) {
            Unit unit = (Unit) it.next();
            if (unit != null && unit.isValid() && (unit.controller() instanceof CommandAI)) {
                CommandAI controller = unit.controller();
                if (controller.group == null) {
                    return;
                }
                if (unitGroup == null) {
                    unitGroup = controller.group;
                } else if (unitGroup != controller.group) {
                    return;
                }
                Vec2 vec22 = controller.attackTarget != null ? new Vec2(controller.attackTarget.x(), controller.attackTarget.y()) : controller.targetPos;
                if (vec22 == null) {
                    return;
                }
                if (vec2 == null) {
                    vec2 = vec22.cpy();
                } else if (vec2.dst(vec22) > 2.0f) {
                    return;
                }
                seq2.add(unit);
            }
        }
        if (unitGroup == null || seq2.size < 2) {
            return;
        }
        if (unitGroup != lastGroup) {
            lastGroup = unitGroup;
            apply(seq2, vec2, mode);
        }
        reformCheck();
    }

    private static void apply(Seq<Unit> seq, Vec2 vec2, int i) {
        float f;
        float f2;
        int i2 = seq.size;
        float f3 = 0.0f;
        float f4 = 0.0f;
        float f5 = 8.0f;
        Iterator it = seq.iterator();
        while (it.hasNext()) {
            Unit unit = (Unit) it.next();
            f3 += unit.x;
            f4 += unit.y;
            f5 = Math.max(f5, unit.hitSize);
        }
        float f6 = f3 / i2;
        float f7 = f4 / i2;
        float max = Math.max(spacing() * 8.0f, f5 * 1.15f);
        float atan2 = face() == 1 ? 1.5707964f : Mathf.atan2(vec2.y - f7, vec2.x - f6);
        float[] layout = layout(i, i2, max, atan2);
        float cos = Mathf.cos(atan2);
        float sin = Mathf.sin(atan2);
        float f8 = -sin;
        ArrayList arrayList = new ArrayList();
        for (int i3 = 0; i3 < i2; i3++) {
            arrayList.add(Integer.valueOf(i3));
        }
        ArrayList arrayList2 = new ArrayList();
        Iterator it2 = seq.iterator();
        while (it2.hasNext()) {
            arrayList2.add((Unit) it2.next());
        }
        if (i == 3) {
            float f9 = atan2 + 3.1415927f;
            arrayList.sort(Comparator.comparingDouble(num -> {
                return Mathf.mod(Mathf.atan2(layout[(num.intValue() * 2) + 1], layout[num.intValue() * 2]) - f9, TWO_PI);
            }));
            arrayList2.sort(Comparator.comparingDouble(unit2 -> {
                return Mathf.mod(Mathf.atan2(unit2.y - f7, unit2.x - f6) - f9, TWO_PI);
            }));
        } else {
            arrayList.sort(Comparator.comparingDouble(num2 -> {
                return (layout[num2.intValue() * 2] * f8) + (layout[(num2.intValue() * 2) + 1] * cos);
            }).thenComparingDouble(num3 -> {
                return -((layout[num3.intValue() * 2] * cos) + (layout[(num3.intValue() * 2) + 1] * sin));
            }));
            arrayList2.sort(Comparator.comparingDouble(unit3 -> {
                return ((unit3.x - f6) * f8) + ((unit3.y - f7) * cos);
            }).thenComparingDouble(unit4 -> {
                return -(((unit4.x - f6) * cos) + ((unit4.y - f7) * sin));
            }));
        }
        int center = center();
        if (center == 1) {
            int intValue = ((Integer) arrayList.get(Math.max(0, arrayList2.indexOf((Unit) seq.first())))).intValue();
            f = vec2.x - layout[intValue * 2];
            f2 = vec2.y - layout[(intValue * 2) + 1];
        } else if (center == 2) {
            f = Core.camera.position.x;
            f2 = Core.camera.position.y;
        } else {
            f = vec2.x;
            f2 = vec2.y;
        }
        curLegs.clear();
        retries.clear();
        loopA.clear();
        loopB.clear();
        reformed = false;
        float[] layout2 = layout(i, i2, max, atan2 + 3.1415927f);
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        for (int i4 = 0; i4 < i2; i4++) {
            arrayList4.add(Integer.valueOf(i4));
        }
        arrayList4.sort(Comparator.comparingDouble(num4 -> {
            return -((layout[((Integer) arrayList.get(num4.intValue())).intValue() * 2] * cos) + (layout[(((Integer) arrayList.get(num4.intValue())).intValue() * 2) + 1] * sin));
        }));
        for (int i5 = 0; i5 < i2; i5++) {
            Unit unit5 = (Unit) arrayList2.get(i5);
            int intValue2 = ((Integer) arrayList.get(i5)).intValue();
            loopB.put(Integer.valueOf(unit5.id), makeLeg(unit5, f + layout[intValue2 * 2], f2 + layout[(intValue2 * 2) + 1], cos, sin));
            loopA.put(Integer.valueOf(unit5.id), makeLeg(unit5, f6 + layout2[intValue2 * 2], f7 + layout2[(intValue2 * 2) + 1], -cos, -sin));
        }
        curLegs.putAll(loopB);
        Iterator it3 = arrayList4.iterator();
        while (it3.hasNext()) {
            arrayList3.add(Integer.valueOf(((Unit) arrayList2.get(((Integer) it3.next()).intValue())).id));
        }
        dispatch(arrayList3, curLegs, true);
        loopActive = loop();
        loopToB = true;
        loopWaitUntil = 0.0f;
        nextLoop = Time.time + 60.0f;
        lastGroup = null;
        nextCheck = Time.time + 30.0f;
        if (Vars.ui == null || loopActive) {
            return;
        }
        Vars.ui.showInfoToast(new StringBuilder().append(modeName(i)).append(" x").append(i2).append(i2 > 1 ? " (" + (((i2 + Math.max(1, Math.min(i2, perLayer() > 0 ? perLayer() : layers() > 0 ? ((i2 + layers()) - 1) / layers() : i == 1 ? 14 : i2))) - 1) / Math.max(1, Math.min(i2, perLayer() > 0 ? perLayer() : layers() > 0 ? ((i2 + layers()) - 1) / layers() : i == 1 ? 14 : i2))) + tr(" layers)", " camadas)") : "").toString(), 1.5f);
    }

    private static void reformCheck() {
        if (!reform() || loopActive || !pending.isEmpty() || curLegs.isEmpty() || Time.time < nextCheck) {
            return;
        }
        nextCheck = Time.time + 30.0f;
        boolean z = true;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (Map.Entry<Integer, Vec2[]> entry : curLegs.entrySet()) {
            Unit byID = Groups.unit.getByID(entry.getKey().intValue());
            if (byID == null || !byID.isValid() || !(byID.controller() instanceof CommandAI)) {
                arrayList.add(entry.getKey());
            } else if (byID.controller().targetPos != null) {
                z = false;
            } else {
                float max = Math.max(6.0f, byID.hitSize * 0.45f);
                Integer num = retries.get(entry.getKey());
                if (byID.dst(((Vec2[]) entry.getValue())[2]) > max && (num == null || num.intValue() < 3)) {
                    arrayList2.add(entry.getKey());
                }
            }
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            curLegs.remove((Integer) it.next());
        }
        if (z) {
            if (arrayList2.isEmpty()) {
                curLegs.clear();
                return;
            }
            Iterator it2 = arrayList2.iterator();
            while (it2.hasNext()) {
                Integer num2 = (Integer) it2.next();
                retries.put(num2, Integer.valueOf(retries.containsKey(num2) ? retries.get(num2).intValue() + 1 : 1));
            }
            dispatch(arrayList2, curLegs, false);
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      classes.dex
     */
    /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/Formations$Preview.class */
    static class Preview extends Element {
        final int mode;
        final float w;
        final float h;

        Preview(int i, float f, float f2) {
            this.mode = i;
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
            Draw.color(0.07f, 0.08f, 0.1f, 0.9f * f);
            Fill.rect(this.x + (this.width / 2.0f), this.y + (this.height / 2.0f), this.width, this.height);
            if (this.mode == 0) {
                Draw.color(UiStyle.DIM.r, UiStyle.DIM.g, UiStyle.DIM.b, f);
                Fill.rect(this.x + (this.width / 2.0f), this.y + (this.height / 2.0f), 18.0f, 3.0f);
                Draw.color();
                return;
            }
            int min = (Formations.perLayer() > 0 || Formations.layers() > 0) ? Math.min(30, Math.max(1, (Formations.perLayer() > 0 ? Formations.perLayer() : 5) * (Formations.layers() > 0 ? Formations.layers() : 2))) : this.mode == 3 ? 9 : 7;
            float[] layout = Formations.layout(this.mode, min, 1.0f, 1.5707964f);
            float f2 = 1.0E9f;
            float f3 = -1.0E9f;
            float f4 = 1.0E9f;
            float f5 = -1.0E9f;
            for (int i = 0; i < min; i++) {
                f2 = Math.min(f2, layout[i * 2]);
                f3 = Math.max(f3, layout[i * 2]);
                f4 = Math.min(f4, layout[(i * 2) + 1]);
                f5 = Math.max(f5, layout[(i * 2) + 1]);
            }
            float min2 = Math.min(Math.min((this.width - 26.0f) / Math.max(0.01f, f3 - f2), (this.height - 22.0f) / Math.max(0.01f, f5 - f4)), 16.0f);
            float f6 = (f2 + f3) / 2.0f;
            float f7 = (f4 + f5) / 2.0f;
            for (int i2 = 0; i2 < min; i2++) {
                Draw.color(Pal.accent.r, Pal.accent.g, Pal.accent.b, f);
                Fill.circle(this.x + (this.width / 2.0f) + ((layout[i2 * 2] - f6) * min2), this.y + (this.height / 2.0f) + ((layout[(i2 * 2) + 1] - f7) * min2), 3.6f);
            }
            Draw.color(1.0f, 1.0f, 1.0f, 0.5f * f);
            Fill.tri((this.x + this.width) - 11.0f, (this.y + this.height) - 8.0f, (this.x + this.width) - 5.0f, (this.y + this.height) - 8.0f, (this.x + this.width) - 8.0f, (this.y + this.height) - 3.0f);
            Draw.color();
        }
    }

    public static void openDialog() {
        BaseDialog baseDialog = new BaseDialog(tr("Formations", "Formacoes"));
        UiStyle.register(baseDialog);
        Table table = new Table();
        Runnable[] runnableArr = new Runnable[1];
        runnableArr[0] = () -> {
            table.clear();
            table.defaults().pad(4.0f);
            table.add(tr("Select units in command mode, choose a shape and send them to move or attack. The mod sends one position per unit.", "Selecione unidades no modo comando, escolha o formato e mande mover ou atacar. O mod envia uma posicao por unidade.")).color(UiStyle.TEXT_DIM).wrap().width(420.0f).left().row();
            UiStyle.section(table, tr("Shape", "Formato"));
            table.table(table2 -> {
                for (int i = 0; i <= 3; i++) {
                    int i2 = i;
                    table2.button(button -> {
                        button.add(new Preview(i2, 92.0f, 62.0f)).row();
                        button.add(modeName(i2)).padTop(3.0f);
                    }, Styles.togglet, () -> {
                        setMode(i2);
                        runnableArr[0].run();
                    }).checked(button2 -> {
                        return mode() == i2;
                    }).size(104.0f, 100.0f).pad(3.0f);
                }
            }).row();
            UiStyle.section(table, tr("Spacing", "Espacamento"));
            table.table(table3 -> {
                Slider slider = new Slider(1.0f, 8.0f, 0.5f, false);
                slider.setValue(spacing());
                Label label = new Label(String.format("%.1f", Float.valueOf(spacing())) + tr(" blocks", " blocos"));
                slider.moved(f -> {
                    Core.settings.put(K_SPACE, Float.valueOf(f));
                    label.setText(String.format("%.1f", Float.valueOf(f)) + tr(" blocks", " blocos"));
                });
                table3.add(slider).width(240.0f);
                table3.add(label).width(90.0f).padLeft(8.0f);
            }).row();
            table.add(tr("Never closer than the unit size.", "Nunca fica mais perto que o tamanho da unidade.")).color(UiStyle.TEXT_DIM).fontScale(0.8f).left().row();
            UiStyle.section(table, tr("Layers", "Camadas"));
            table.add(tr("Add more layers behind the first one. 0 = automatic.", "Adicione mais camadas atras da primeira. 0 = automatico.")).color(UiStyle.TEXT_DIM).wrap().width(420.0f).left().row();
            table.table(table4 -> {
                table4.add(tr("Layers", "Camadas")).width(110.0f).left();
                Slider slider = new Slider(0.0f, 10.0f, 1.0f, false);
                slider.setValue(layers());
                Label label = new Label(layers() == 0 ? tr("Auto", "Auto") : String.valueOf(layers()));
                slider.moved(f -> {
                    Core.settings.put(K_LAYERS, Integer.valueOf((int) f));
                    label.setText(((int) f) == 0 ? tr("Auto", "Auto") : String.valueOf((int) f));
                });
                table4.add(slider).width(200.0f);
                table4.add(label).width(60.0f).padLeft(8.0f);
            }).row();
            table.table(table5 -> {
                table5.add(tr("Units per layer", "Unidades por camada")).width(110.0f).left().wrap();
                Slider slider = new Slider(0.0f, 50.0f, 1.0f, false);
                slider.setValue(perLayer());
                Label label = new Label(perLayer() == 0 ? tr("Auto", "Auto") : String.valueOf(perLayer()));
                slider.moved(f -> {
                    Core.settings.put(K_PER, Integer.valueOf((int) f));
                    label.setText(((int) f) == 0 ? tr("Auto", "Auto") : String.valueOf((int) f));
                });
                table5.add(slider).width(200.0f);
                table5.add(label).width(60.0f).padLeft(8.0f);
            }).row();
            UiStyle.section(table, tr("Gradual and loop", "Gradual e loop"));
            table.table(table6 -> {
                table6.check(tr("Gradual (send units one by one, front first)", "Gradual (envia as unidades uma a uma, frente primeiro)"), gradual(), z -> {
                    Core.settings.put(K_GRAD, Boolean.valueOf(z));
                }).left();
            }).left().row();
            table.table(table7 -> {
                table7.add(tr("Delay between units", "Intervalo entre unidades")).width(110.0f).left().wrap();
                Slider slider = new Slider(0.1f, 3.0f, 0.1f, false);
                slider.setValue(gradualDelay());
                Label label = new Label(String.format("%.1fs", Float.valueOf(gradualDelay())));
                slider.moved(f -> {
                    Core.settings.put(K_GDELAY, Float.valueOf(f));
                    label.setText(String.format("%.1fs", Float.valueOf(f)));
                });
                table7.add(slider).width(200.0f);
                table7.add(label).width(60.0f).padLeft(8.0f);
            }).row();
            table.table(table8 -> {
                table8.check(tr("Loop (go back and forth keeping the formation)", "Loop (vai e volta mantendo a formacao)"), loop(), z -> {
                    Core.settings.put(K_LOOP, Boolean.valueOf(z));
                    if (z) {
                        return;
                    }
                    loopActive = false;
                }).left();
            }).left().row();
            table.table(table9 -> {
                table9.add(tr("Wait at each end", "Espera em cada ponta")).width(110.0f).left().wrap();
                Slider slider = new Slider(0.0f, 10.0f, 0.5f, false);
                slider.setValue(loopWait());
                Label label = new Label(String.format("%.1fs", Float.valueOf(loopWait())));
                slider.moved(f -> {
                    Core.settings.put(K_LWAIT, Float.valueOf(f));
                    label.setText(String.format("%.1fs", Float.valueOf(f)));
                });
                table9.add(slider).width(200.0f);
                table9.add(label).width(60.0f).padLeft(8.0f);
            }).row();
            table.add(tr("Loop stops when you give the units a new command or turn it off.", "O loop para quando voce da um novo comando as unidades ou desliga.")).color(UiStyle.TEXT_DIM).wrap().width(420.0f).left().row();
            UiStyle.section(table, tr("Center", "Centro"));
            table.table(table10 -> {
                String[] strArr = {tr("Command target", "Alvo do comando"), tr("First selected", "Primeiro da selecao"), "Camera"};
                for (int i = 0; i < 3; i++) {
                    int i2 = i;
                    table10.button(strArr[i], Styles.togglet, () -> {
                        Core.settings.put(K_CENTER, Integer.valueOf(i2));
                    }).checked(textButton -> {
                        return center() == i2;
                    }).size(138.0f, 42.0f).pad(2.0f);
                }
            }).row();
            UiStyle.section(table, tr("Facing", "Orientacao"));
            table.table(table11 -> {
                String[] strArr = {tr("Toward target / movement", "Para o alvo / movimento"), tr("Fixed (north)", "Fixa (norte)")};
                for (int i = 0; i < 2; i++) {
                    int i2 = i;
                    table11.button(strArr[i], Styles.togglet, () -> {
                        Core.settings.put(K_FACE, Integer.valueOf(i2));
                    }).checked(textButton -> {
                        return face() == i2;
                    }).size(210.0f, 42.0f).pad(2.0f);
                }
            }).row();
            table.table(table12 -> {
                table12.check(tr("Aligned approach (all units arrive from the same direction)", "Aproximacao alinhada (todas chegam pela mesma direcao)"), approach(), z -> {
                    Core.settings.put(K_APPR, Boolean.valueOf(z));
                }).left();
            }).left().row();
            table.table(table13 -> {
                table13.check(tr("Reform on stop (re-send slots when they stop out of place)", "Reformar ao parar (reenvia os slots se pararem fora do lugar)"), reform(), z -> {
                    Core.settings.put(K_REFORM, Boolean.valueOf(z));
                }).left();
            }).left().row();
        };
        runnableArr[0].run();
        baseDialog.cont.pane(table).grow().minWidth(440.0f);
        baseDialog.addCloseButton();
        baseDialog.show();
    }

    private static String tr(String str, String str2) {
        return (LunariansSettings.language == null || !LunariansSettings.language.startsWith("pt")) ? str : str2;
    }
}
