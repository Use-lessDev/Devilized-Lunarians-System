package lunarians.hub;

import arc.Core;
import arc.Events;
import arc.audio.Music;
import arc.files.Fi;
import arc.func.Boolc;
import arc.func.Boolf;
import arc.func.Cons;
import arc.func.Floatc;
import arc.graphics.Pixmap;
import arc.graphics.Texture;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Image;
import arc.scene.ui.Label;
import arc.scene.ui.Slider;
import arc.scene.ui.TextButton;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import arc.struct.IntFloatMap;
import arc.struct.IntMap;
import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Scaling;
import arc.util.Strings;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.serialization.Base64Coder;
import arc.util.serialization.Jval;
import java.util.Iterator;
import lunarians.UiStyle;
import mindustry.Vars;
import mindustry.entities.Leg;
import mindustry.entities.units.WeaponMount;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Groups;
import mindustry.gen.Icon;
import mindustry.gen.Legsc;
import mindustry.gen.Mechc;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.ui.FileChooser;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/hub/Skins.class */
public class Skins {
    private static final String K_EQ = "lunarians-hub-skins-eq";
    private static final String K_SHOW = "lunarians-hub-skins-show";
    private static final float PEER_TTL = 6000.0f;
    private static final int MAX_EQUIPPED = 8;
    private static boolean mineLoaded;
    private static boolean inited;
    private static float nextPool;
    private static float nextMine;
    private static float nextMusic;
    private static final Seq<Entry> mine = new Seq<>();
    private static final IntMap<Peer> peers = new IntMap<>();
    private static final ObjectMap<String, UnitPool> pools = new ObjectMap<>();
    private static final IntMap<TextureRegion> tex = new IntMap<>();
    private static final IntMap<Seq<Cons<TextureRegion>>> waiting = new IntMap<>();
    private static final IntFloatMap failedAt = new IntFloatMap();
    private static final ObjectMap<UnitType, Seq<String>> weaponNames = new ObjectMap<>();
    private static final Vec2 tv1 = new Vec2();
    private static final Vec2 tv2 = new Vec2();
    private static boolean dirty = true;
    private static final IntMap<Music> playing = new IntMap<>();
    private static final IntMap<Fi> files = new IntMap<>();
    private static final IntFloatMap audioFailed = new IntFloatMap();
    private static final IntSet audioLoading = new IntSet();

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 {
        public static /* synthetic */ int m(int i, int i2) {
            int i3 = i % i2;
            if (i3 == 0) {
                return 0;
            }
            return (((i ^ i2) >> 31) | 1) > 0 ? i3 : i3 + i2;
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$10, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass10 implements Cons {
        public final /* synthetic */ Image f$0;

        public final void get(Object obj) {
            Skins.lambda$buildTab$11(this.f$0, (TextureRegion) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$11, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass11 implements Boolf {
        public final /* synthetic */ String f$0;

        public final boolean get(Object obj) {
            return Skins.lambda$unitType$8(this.f$0, (UnitType) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$12, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass12 implements Cons {
        public final /* synthetic */ BaseDialog f$0;
        public final /* synthetic */ Runnable f$1;

        public /* synthetic */ AnonymousClass12(BaseDialog baseDialog, Runnable runnable) {
            this.f$0 = baseDialog;
            this.f$1 = runnable;
        }

        public final void get(Object obj) {
            Skins.lambda$requestDialog$45(this.f$0, this.f$1, (Jval) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$13, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass13 implements Cons {
        public final void get(Object obj) {
            Skins.lambda$requestDialog$46((String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$14, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass14 implements Runnable {
        public final /* synthetic */ int[] f$0;
        public final /* synthetic */ int f$1;

        public /* synthetic */ AnonymousClass14(int[] iArr, int i) {
            this.f$0 = iArr;
            this.f$1 = i;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Skins.lambda$musicDialog$26(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$15, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass15 implements Boolf {
        public final /* synthetic */ int[] f$0;
        public final /* synthetic */ int f$1;

        public /* synthetic */ AnonymousClass15(int[] iArr, int i) {
            this.f$0 = iArr;
            this.f$1 = i;
        }

        public final boolean get(Object obj) {
            return Skins.lambda$musicDialog$27(this.f$0, this.f$1, (TextButton) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$16, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass16 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            Skins.$r8$lambda$5089Sc770q-ZBNsggjVM4_2d3DA();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$17, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass17 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            Skins.$r8$lambda$EjsHG6Yh0XoswPGXsIrYP9ofq7s();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$18, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass18 implements Cons {
        public final void get(Object obj) {
            Skins.lambda$init$2((EventType.WorldLoadEvent) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$19, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass19 implements Cons {
        public final /* synthetic */ int f$0;
        public final /* synthetic */ Jval f$1;
        public final /* synthetic */ String f$2;
        public final /* synthetic */ String f$3;
        public final /* synthetic */ String f$4;
        public final /* synthetic */ int f$5;
        public final /* synthetic */ int f$6;

        public /* synthetic */ AnonymousClass19(int i, Jval jval, String str, String str2, String str3, int i2, int i3) {
            this.f$0 = i;
            this.f$1 = jval;
            this.f$2 = str;
            this.f$3 = str2;
            this.f$4 = str3;
            this.f$5 = i2;
            this.f$6 = i3;
        }

        public final void get(Object obj) {
            Skins.lambda$buildTab$12(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, (Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass2 implements Cons {
        public final /* synthetic */ String[] f$0;
        public final /* synthetic */ String[] f$1;
        public final /* synthetic */ Runnable f$2;

        public /* synthetic */ AnonymousClass2(String[] strArr, String[] strArr2, Runnable runnable) {
            this.f$0 = strArr;
            this.f$1 = strArr2;
            this.f$2 = runnable;
        }

        public final void get(Object obj) {
            Skins.lambda$requestDialog$39(this.f$0, this.f$1, this.f$2, (String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$20, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass20 implements Cons {
        public final /* synthetic */ String f$0;
        public final /* synthetic */ int f$1;
        public final /* synthetic */ Runnable f$2;
        public final /* synthetic */ int f$3;
        public final /* synthetic */ int f$4;

        public /* synthetic */ AnonymousClass20(String str, int i, Runnable runnable, int i2, int i3) {
            this.f$0 = str;
            this.f$1 = i;
            this.f$2 = runnable;
            this.f$3 = i2;
            this.f$4 = i3;
        }

        public final void get(Object obj) {
            Skins.lambda$buildTab$20(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, (Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$21, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass21 implements Boolc {
        public final void get(boolean z) {
            Skins.lambda$buildTab$9(z);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$22, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass22 implements Runnable {
        public final /* synthetic */ Runnable f$0;

        @Override // java.lang.Runnable
        public final void run() {
            Skins.requestDialog(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$23, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass23 implements Cons {
        public final /* synthetic */ Table f$0;
        public final /* synthetic */ float f$1;
        public final /* synthetic */ Runnable f$2;

        public /* synthetic */ AnonymousClass23(Table table, float f, Runnable runnable) {
            this.f$0 = table;
            this.f$1 = f;
            this.f$2 = runnable;
        }

        public final void get(Object obj) {
            Skins.lambda$buildTab$22(this.f$0, this.f$1, this.f$2, (Jval) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$24, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass24 implements Cons {
        public final /* synthetic */ Table f$0;

        public final void get(Object obj) {
            Skins.lambda$buildTab$23(this.f$0, (String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$25, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass25 implements Runnable {
        public final /* synthetic */ Cons f$0;
        public final /* synthetic */ UnitType f$1;
        public final /* synthetic */ BaseDialog f$2;

        public /* synthetic */ AnonymousClass25(Cons cons, UnitType unitType, BaseDialog baseDialog) {
            this.f$0 = cons;
            this.f$1 = unitType;
            this.f$2 = baseDialog;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Skins.lambda$pickUnit$36(this.f$0, this.f$1, this.f$2);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$26, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass26 implements Cons {
        public final /* synthetic */ String[] f$0;
        public final /* synthetic */ byte[][] f$1;
        public final /* synthetic */ Runnable f$2;

        public /* synthetic */ AnonymousClass26(String[] strArr, byte[][] bArr, Runnable runnable) {
            this.f$0 = strArr;
            this.f$1 = bArr;
            this.f$2 = runnable;
        }

        public final void get(Object obj) {
            Skins.lambda$requestDialog$43(this.f$0, this.f$1, this.f$2, (Fi) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$27, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass27 implements Cons {
        public final void get(Object obj) {
            Skins.lambda$refreshMine$0((Jval) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$28, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass28 implements Cons {
        public final void get(Object obj) {
            Skins.lambda$refreshMine$1((String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$29, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass29 implements Cons {
        public final /* synthetic */ int f$0;

        public final void get(Object obj) {
            Skins.lambda$fetch$3(this.f$0, (Jval) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$3, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass3 implements Runnable {
        public final /* synthetic */ int[] f$0;
        public final /* synthetic */ Runnable f$1;

        public /* synthetic */ AnonymousClass3(int[] iArr, Runnable runnable) {
            this.f$0 = iArr;
            this.f$1 = runnable;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Skins.lambda$musicDialog$33(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$30, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass30 implements Cons {
        public final /* synthetic */ int f$0;

        public final void get(Object obj) {
            Skins.lambda$fetch$4(this.f$0, (String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$31, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass31 implements Runnable {
        public final /* synthetic */ int f$0;
        public final /* synthetic */ Runnable f$1;

        public /* synthetic */ AnonymousClass31(int i, Runnable runnable) {
            this.f$0 = i;
            this.f$1 = runnable;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Skins.lambda$buildTab$13(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$32, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass32 implements Runnable {
        public final /* synthetic */ int f$0;
        public final /* synthetic */ Runnable f$1;

        public /* synthetic */ AnonymousClass32(int i, Runnable runnable) {
            this.f$0 = i;
            this.f$1 = runnable;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Skins.lambda$buildTab$14(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$33, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass33 implements Runnable {
        public final /* synthetic */ int f$0;
        public final /* synthetic */ int f$1;
        public final /* synthetic */ int f$2;
        public final /* synthetic */ Runnable f$3;

        public /* synthetic */ AnonymousClass33(int i, int i2, int i3, Runnable runnable) {
            this.f$0 = i;
            this.f$1 = i2;
            this.f$2 = i3;
            this.f$3 = runnable;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Skins.musicDialog(this.f$0, this.f$1, this.f$2, this.f$3);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$34, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass34 implements Runnable {
        public final /* synthetic */ int f$0;
        public final /* synthetic */ Runnable f$1;

        public /* synthetic */ AnonymousClass34(int i, Runnable runnable) {
            this.f$0 = i;
            this.f$1 = runnable;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Skins.lambda$buildTab$19(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$35, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass35 implements Cons {
        public final /* synthetic */ String[] f$0;
        public final /* synthetic */ Runnable f$1;

        public /* synthetic */ AnonymousClass35(String[] strArr, Runnable runnable) {
            this.f$0 = strArr;
            this.f$1 = runnable;
        }

        public final void get(Object obj) {
            Skins.lambda$requestDialog$41(this.f$0, this.f$1, (String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$36, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass36 implements Floatc {
        public final /* synthetic */ Label f$0;

        public final void get(float f) {
            Skins.lambda$musicDialog$24(this.f$0, f);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$37, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass37 implements Cons {
        public final /* synthetic */ Slider f$0;
        public final /* synthetic */ float f$1;
        public final /* synthetic */ Label f$2;

        public /* synthetic */ AnonymousClass37(Slider slider, float f, Label label) {
            this.f$0 = slider;
            this.f$1 = f;
            this.f$2 = label;
        }

        public final void get(Object obj) {
            Skins.lambda$musicDialog$25(this.f$0, this.f$1, this.f$2, (Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$38, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass38 implements Cons {
        public final /* synthetic */ Table f$0;
        public final /* synthetic */ int[] f$1;
        public final /* synthetic */ float f$2;

        public /* synthetic */ AnonymousClass38(Table table, int[] iArr, float f) {
            this.f$0 = table;
            this.f$1 = iArr;
            this.f$2 = f;
        }

        public final void get(Object obj) {
            Skins.lambda$musicDialog$28(this.f$0, this.f$1, this.f$2, (Jval) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$39, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass39 implements Cons {
        public final /* synthetic */ Table f$0;

        public final void get(Object obj) {
            Skins.lambda$musicDialog$29(this.f$0, (String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$4, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass4 implements Runnable {
        public final /* synthetic */ int[] f$0;
        public final /* synthetic */ Runnable f$1;

        public /* synthetic */ AnonymousClass4(int[] iArr, Runnable runnable) {
            this.f$0 = iArr;
            this.f$1 = runnable;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Skins.lambda$musicDialog$34(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$40, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass40 implements Runnable {
        public final /* synthetic */ int f$0;
        public final /* synthetic */ int[] f$1;
        public final /* synthetic */ Slider f$2;
        public final /* synthetic */ BaseDialog f$3;
        public final /* synthetic */ Runnable f$4;

        public /* synthetic */ AnonymousClass40(int i, int[] iArr, Slider slider, BaseDialog baseDialog, Runnable runnable) {
            this.f$0 = i;
            this.f$1 = iArr;
            this.f$2 = slider;
            this.f$3 = baseDialog;
            this.f$4 = runnable;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Skins.lambda$musicDialog$32(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$41, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass41 implements Cons {
        public final /* synthetic */ int[] f$0;
        public final /* synthetic */ Runnable f$1;

        public /* synthetic */ AnonymousClass41(int[] iArr, Runnable runnable) {
            this.f$0 = iArr;
            this.f$1 = runnable;
        }

        public final void get(Object obj) {
            Skins.lambda$musicDialog$35(this.f$0, this.f$1, (Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$42, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass42 implements Cons {
        public final void get(Object obj) {
            Skins.lambda$region$5((TextureRegion) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$43, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass43 implements Runnable {
        public final /* synthetic */ Cons f$0;
        public final /* synthetic */ String f$1;
        public final /* synthetic */ BaseDialog f$2;

        public /* synthetic */ AnonymousClass43(Cons cons, String str, BaseDialog baseDialog) {
            this.f$0 = cons;
            this.f$1 = str;
            this.f$2 = baseDialog;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Skins.lambda$pickPart$37(this.f$0, this.f$1, this.f$2);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$44, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass44 implements Runnable {
        public final /* synthetic */ TextButton f$0;
        public final /* synthetic */ String[] f$1;
        public final /* synthetic */ TextButton f$2;
        public final /* synthetic */ String[] f$3;
        public final /* synthetic */ TextButton f$4;
        public final /* synthetic */ byte[][] f$5;
        public final /* synthetic */ String[] f$6;
        public final /* synthetic */ Label f$7;

        public /* synthetic */ AnonymousClass44(TextButton textButton, String[] strArr, TextButton textButton2, String[] strArr2, TextButton textButton3, byte[][] bArr, String[] strArr3, Label label) {
            this.f$0 = textButton;
            this.f$1 = strArr;
            this.f$2 = textButton2;
            this.f$3 = strArr2;
            this.f$4 = textButton3;
            this.f$5 = bArr;
            this.f$6 = strArr3;
            this.f$7 = label;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Skins.lambda$requestDialog$38(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$45, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass45 implements Runnable {
        public final /* synthetic */ String[] f$0;
        public final /* synthetic */ String[] f$1;
        public final /* synthetic */ Runnable f$2;

        public /* synthetic */ AnonymousClass45(String[] strArr, String[] strArr2, Runnable runnable) {
            this.f$0 = strArr;
            this.f$1 = strArr2;
            this.f$2 = runnable;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Skins.lambda$requestDialog$40(this.f$0, this.f$1, this.f$2);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$46, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass46 implements Runnable {
        public final /* synthetic */ String[] f$0;
        public final /* synthetic */ String[] f$1;
        public final /* synthetic */ Runnable f$2;

        public /* synthetic */ AnonymousClass46(String[] strArr, String[] strArr2, Runnable runnable) {
            this.f$0 = strArr;
            this.f$1 = strArr2;
            this.f$2 = runnable;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Skins.lambda$requestDialog$42(this.f$0, this.f$1, this.f$2);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$47, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass47 implements Runnable {
        public final /* synthetic */ String[] f$0;
        public final /* synthetic */ byte[][] f$1;
        public final /* synthetic */ Runnable f$2;

        public /* synthetic */ AnonymousClass47(String[] strArr, byte[][] bArr, Runnable runnable) {
            this.f$0 = strArr;
            this.f$1 = bArr;
            this.f$2 = runnable;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Skins.lambda$requestDialog$44(this.f$0, this.f$1, this.f$2);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$48, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass48 implements Runnable {
        public final /* synthetic */ TextField f$0;
        public final /* synthetic */ String[] f$1;
        public final /* synthetic */ byte[][] f$2;
        public final /* synthetic */ String[] f$3;
        public final /* synthetic */ BaseDialog f$4;
        public final /* synthetic */ Runnable f$5;

        public /* synthetic */ AnonymousClass48(TextField textField, String[] strArr, byte[][] bArr, String[] strArr2, BaseDialog baseDialog, Runnable runnable) {
            this.f$0 = textField;
            this.f$1 = strArr;
            this.f$2 = bArr;
            this.f$3 = strArr2;
            this.f$4 = baseDialog;
            this.f$5 = runnable;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Skins.lambda$requestDialog$47(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$49, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass49 implements Cons {
        public final /* synthetic */ BaseDialog f$0;
        public final /* synthetic */ Runnable f$1;

        public /* synthetic */ AnonymousClass49(BaseDialog baseDialog, Runnable runnable) {
            this.f$0 = baseDialog;
            this.f$1 = runnable;
        }

        public final void get(Object obj) {
            Skins.lambda$musicDialog$30(this.f$0, this.f$1, (Jval) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$5, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass5 implements Cons {
        public final /* synthetic */ int f$0;

        public final void get(Object obj) {
            Skins.lambda$loadAudio$6(this.f$0, (Jval) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$50, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass50 implements Cons {
        public final void get(Object obj) {
            Skins.lambda$musicDialog$31((String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$51, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass51 implements Cons {
        public final /* synthetic */ int f$0;
        public final /* synthetic */ Jval f$1;
        public final /* synthetic */ String f$2;
        public final /* synthetic */ String f$3;
        public final /* synthetic */ String f$4;
        public final /* synthetic */ int f$5;
        public final /* synthetic */ int f$6;
        public final /* synthetic */ Runnable f$7;

        public /* synthetic */ AnonymousClass51(int i, Jval jval, String str, String str2, String str3, int i2, int i3, Runnable runnable) {
            this.f$0 = i;
            this.f$1 = jval;
            this.f$2 = str;
            this.f$3 = str2;
            this.f$4 = str3;
            this.f$5 = i2;
            this.f$6 = i3;
            this.f$7 = runnable;
        }

        public final void get(Object obj) {
            Skins.lambda$buildTab$21(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, (Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$6, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass6 implements Cons {
        public final /* synthetic */ int f$0;

        public final void get(Object obj) {
            Skins.lambda$loadAudio$7(this.f$0, (String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$7, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass7 implements Cons {
        public final /* synthetic */ Runnable f$0;

        public final void get(Object obj) {
            this.f$0.run();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$8, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass8 implements Cons {
        public final void get(Object obj) {
            Skins.lambda$buildTab$17((String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Skins$9, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass9 implements Runnable {
        public final /* synthetic */ int f$0;
        public final /* synthetic */ Runnable f$1;

        public /* synthetic */ AnonymousClass9(int i, Runnable runnable) {
            this.f$0 = i;
            this.f$1 = runnable;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Skins.lambda$buildTab$18(this.f$0, this.f$1);
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      classes.dex
     */
    /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/hub/Skins$Entry.class */
    static class Entry {
        int id;
        String unit;
        String part;
        int music;
        int radius;
        int owner = -1;

        Entry() {
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      classes.dex
     */
    /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/hub/Skins$Peer.class */
    private static class Peer {
        Seq<Entry> list;
        float seen;
        String sig;

        private Peer() {
            this.list = new Seq<>();
            this.sig = "";
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      classes.dex
     */
    /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/hub/Skins$UnitPool.class */
    private static class UnitPool {
        final Seq<Integer> owners;
        final IntMap<ObjectMap<String, Entry>> byOwner;

        private UnitPool() {
            this.owners = new Seq<>();
            this.byOwner = new IntMap<>();
        }
    }

    static boolean okUnit(String str) {
        return str != null && str.matches("[a-z0-9-]{1,40}");
    }

    static boolean okPart(String str) {
        return str != null && str.matches("body|legs|joint|legbase|base|weapon[0-9]{1,2}");
    }

    public static boolean showOthers() {
        return Core.settings.getBool(K_SHOW, true);
    }

    private static IntSeq equipped() {
        IntSeq intSeq = new IntSeq();
        for (String str : Core.settings.getString(K_EQ, "").split(",")) {
            if (str.matches("\\d{1,9}")) {
                intSeq.add(Strings.parseInt(str, 0));
            }
        }
        return intSeq;
    }

    private static void saveEquipped(IntSeq intSeq) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < intSeq.size; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(intSeq.get(i));
        }
        Core.settings.put(K_EQ, sb.toString());
        dirty = true;
        Room.sendSoon();
    }

    public static boolean using(int i) {
        return equipped().contains(i);
    }

    private static Entry mineById(int i) {
        Iterator it = mine.iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            if (entry.id == i) {
                return entry;
            }
        }
        return null;
    }

    public static void equip(int i) {
        Entry mineById = mineById(i);
        if (mineById == null) {
            return;
        }
        IntSeq equipped = equipped();
        IntSeq intSeq = new IntSeq();
        for (int i2 = 0; i2 < equipped.size; i2++) {
            Entry mineById2 = mineById(equipped.get(i2));
            if (mineById2 != null && (!mineById2.unit.equals(mineById.unit) || !mineById2.part.equals(mineById.part))) {
                intSeq.add(mineById2.id);
            }
        }
        if (intSeq.size >= 8) {
            Vars.ui.showInfoToast(HubText.get("skin.max"), 2.5f);
        } else {
            intSeq.add(i);
            saveEquipped(intSeq);
        }
    }

    public static void unequip(int i) {
        IntSeq equipped = equipped();
        equipped.removeValue(i);
        saveEquipped(equipped);
    }

    public static IntSeq equippedApproved() {
        IntSeq intSeq = new IntSeq();
        IntSeq equipped = equipped();
        for (int i = 0; i < equipped.size && intSeq.size < 8; i++) {
            if (mineById(equipped.get(i)) != null) {
                intSeq.add(equipped.get(i));
            }
        }
        return intSeq;
    }

    public static void wipeLocal() {
        Core.settings.remove(K_EQ);
        mine.clear();
        mineLoaded = false;
        peers.clear();
        pools.clear();
        stopAllMusic();
        dirty = true;
    }

    private static Entry parse(Jval jval, int i) {
        try {
            Entry entry = new Entry();
            entry.id = jval.getInt("i", jval.getInt("id", 0));
            entry.unit = jval.getString("u", jval.getString("unit", ""));
            entry.part = jval.getString("p", jval.getString("part", "body"));
            entry.music = jval.getInt("m", jval.getInt("music", 0));
            entry.radius = Math.max(0, Math.min(30, jval.getInt("r", jval.getInt("music_radius", 0))));
            entry.owner = i;
            if (entry.id <= 0 || !okUnit(entry.unit) || !okPart(entry.part)) {
                return null;
            }
            if (entry.music < 0) {
                entry.music = 0;
            }
            return entry;
        } catch (Throwable th) {
            return null;
        }
    }

    private static void updateMine(Jval.JsonArray jsonArray) {
        Entry parse;
        mine.clear();
        Iterator it = jsonArray.iterator();
        while (it.hasNext()) {
            Jval jval = (Jval) it.next();
            if ("approved".equals(jval.getString("status", "")) && (parse = parse(jval, -1)) != null) {
                mine.add(parse);
            }
        }
        mineLoaded = true;
        IntSeq equipped = equipped();
        IntSeq intSeq = new IntSeq();
        for (int i = 0; i < equipped.size; i++) {
            if (mineById(equipped.get(i)) != null) {
                intSeq.add(equipped.get(i));
            }
        }
        if (intSeq.size != equipped.size) {
            saveEquipped(intSeq);
        }
        dirty = true;
    }

    static void refreshMine() {
        if (!HubClient.consented() || HubClient.myId().isEmpty()) {
            return;
        }
        HubClient.call("GET", "/api/skins/mine", null, jval -> {
            updateMine(jval.get("skins").asArray());
        }, str -> {
        });
    }

    static void feedPeer(int i, Jval.JsonArray jsonArray) {
        if (i < 0 || jsonArray == null) {
            return;
        }
        if (peers.size <= 200 || peers.containsKey(i)) {
            Peer peer = (Peer) peers.get(i);
            if (peer == null) {
                peer = new Peer();
                peers.put(i, peer);
            }
            Seq<Entry> seq = new Seq<>();
            StringBuilder sb = new StringBuilder();
            int i2 = 0;
            Iterator it = jsonArray.iterator();
            while (it.hasNext()) {
                Jval jval = (Jval) it.next();
                int i3 = i2;
                i2++;
                if (i3 >= 8) {
                    break;
                }
                Entry parse = parse(jval, i);
                if (parse != null) {
                    seq.add(parse);
                    sb.append(parse.id).append(':').append(parse.music).append(':').append(parse.radius).append(',');
                }
            }
            peer.seen = Time.time;
            if (!sb.toString().equals(peer.sig)) {
                peer.sig = sb.toString();
                peer.list = seq;
                dirty = true;
            }
        }
    }

    private static void rebuildPools() {
        pools.clear();
        IntSeq equipped = equipped();
        Iterator it = mine.iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            if (equipped.contains(entry.id)) {
                add(entry);
            }
        }
        if (showOthers()) {
            Seq seq = new Seq();
            Iterator it2 = peers.iterator();
            while (it2.hasNext()) {
                IntMap.Entry entry2 = (IntMap.Entry) it2.next();
                if (Time.time - ((Peer) entry2.value).seen > PEER_TTL) {
                    seq.add(Integer.valueOf(entry2.key));
                } else {
                    Iterator it3 = ((Peer) entry2.value).list.iterator();
                    while (it3.hasNext()) {
                        add((Entry) it3.next());
                    }
                }
            }
            Iterator it4 = seq.iterator();
            while (it4.hasNext()) {
                peers.remove(((Integer) it4.next()).intValue());
            }
        }
        ObjectMap.Entries it5 = pools.iterator();
        while (it5.hasNext()) {
            ((UnitPool) ((ObjectMap.Entry) it5.next()).value).owners.sort();
        }
        dirty = false;
    }

    private static void add(Entry entry) {
        UnitPool unitPool = (UnitPool) pools.get(entry.unit);
        if (unitPool == null) {
            unitPool = new UnitPool();
            pools.put(entry.unit, unitPool);
        }
        ObjectMap objectMap = (ObjectMap) unitPool.byOwner.get(entry.owner);
        if (objectMap == null) {
            objectMap = new ObjectMap();
            unitPool.byOwner.put(entry.owner, objectMap);
            unitPool.owners.add(Integer.valueOf(entry.owner));
        }
        if (!objectMap.containsKey(entry.part)) {
            objectMap.put(entry.part, entry);
        }
    }

    private static int pick(int i, int i2) {
        if (i2 <= 1) {
            return 0;
        }
        int i3 = i * (-1640531535);
        int i4 = (i3 ^ (i3 >>> 15)) * (-2048144789);
        return Math.floorMod(i4 ^ (i4 >>> 13), i2);
    }

    private static ObjectMap<String, Entry> skinsFor(Unit unit, UnitPool unitPool) {
        if (unitPool == null || unitPool.owners.size == 0) {
            return null;
        }
        return (ObjectMap) unitPool.byOwner.get(((Integer) unitPool.owners.get(pick(unit.id, unitPool.owners.size))).intValue());
    }

    public static void init() {
        if (inited || Vars.headless) {
            return;
        }
        inited = true;
        Events.run(EventType.Trigger.drawOver, Skins::draw);
        Events.run(EventType.Trigger.update, Skins::tick);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            peers.clear();
            dirty = true;
            stopAllMusic();
        });
    }

    private static void tick() {
        try {
            if (!HubClient.consented()) {
                if (pools.size > 0) {
                    pools.clear();
                    stopAllMusic();
                    return;
                }
                return;
            }
            if (Time.time >= nextMine) {
                nextMine = Time.time + (mineLoaded ? 7200.0f : 600.0f);
                refreshMine();
            }
            if (dirty || Time.time >= nextPool) {
                nextPool = Time.time + 300.0f;
                rebuildPools();
            }
            if (Time.time >= nextMusic) {
                nextMusic = Time.time + 20.0f;
                musicTick();
            }
        } catch (Throwable th) {
            Log.debug("[Lunarians Skins] tick: @", new Object[]{th.toString()});
        }
    }

    static void fetch(int i, Cons<TextureRegion> cons) {
        TextureRegion textureRegion = (TextureRegion) tex.get(i);
        if (textureRegion != null) {
            cons.get(textureRegion);
            return;
        }
        Seq seq = (Seq) waiting.get(i);
        boolean z = seq == null;
        if (z) {
            seq = new Seq();
            waiting.put(i, seq);
        }
        seq.add(cons);
        if (z) {
            HubClient.call("GET", "/api/skins/img?id=" + i, null, jval -> {
                Seq seq2 = (Seq) waiting.remove(i);
                try {
                    Pixmap pixmap = new Pixmap(Base64Coder.decode(jval.getString("png", "")));
                    Texture texture = new Texture(pixmap);
                    texture.setFilter(Texture.TextureFilter.nearest);
                    pixmap.dispose();
                    TextureRegion textureRegion2 = new TextureRegion(texture);
                    tex.put(i, textureRegion2);
                    if (seq2 != null) {
                        Iterator it = seq2.iterator();
                        while (it.hasNext()) {
                            ((Cons) it.next()).get(textureRegion2);
                        }
                    }
                } catch (Throwable th) {
                    failedAt.put(i, Time.time);
                }
            }, str -> {
                waiting.remove(i);
                failedAt.put(i, Time.time);
            });
        }
    }

    private static TextureRegion region(int i) {
        TextureRegion textureRegion = (TextureRegion) tex.get(i);
        if (textureRegion != null) {
            return textureRegion;
        }
        if (waiting.containsKey(i) || failedAt.get(i, -100000.0f) + 3600.0f >= Time.time) {
            return null;
        }
        fetch(i, textureRegion2 -> {
        });
        return null;
    }

    private static TextureRegion skin(ObjectMap<String, Entry> objectMap, String str) {
        Entry entry;
        if (objectMap == null || (entry = (Entry) objectMap.get(str)) == null) {
            return null;
        }
        return region(entry.id);
    }

    private static void draw() {
        UnitPool unitPool;
        ObjectMap<String, Entry> skinsFor;
        try {
            if (pools.size == 0 || !HubClient.consented() || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
                return;
            }
            Core.camera.bounds(Tmp.r1);
            Team team = Vars.player.team();
            float z = Draw.z();
            Iterator it = Groups.unit.iterator();
            while (it.hasNext()) {
                Unit unit = (Unit) it.next();
                if (unit != null && unit.isAdded() && !unit.dead && unit.type != null && (unitPool = (UnitPool) pools.get(unit.type.name)) != null) {
                    Tmp.r2.set(Tmp.r1).grow((unit.hitSize * 2.0f) + 16.0f);
                    if (Tmp.r2.contains(unit.x, unit.y) && !unit.inFogTo(team) && (skinsFor = skinsFor(unit, unitPool)) != null) {
                        try {
                            drawUnit(unit, skinsFor);
                        } catch (Throwable th) {
                        }
                    }
                }
            }
            Draw.z(z);
            Draw.reset();
        } catch (Throwable th2) {
        }
    }

    private static void drawUnit(Unit unit, ObjectMap<String, Entry> objectMap) {
        UnitType unitType = unit.type;
        float clamp = (unit.elevation > 0.5f || (unitType.flying && unit.dead)) ? unitType.flyingLayer : unitType.groundLayer + Mathf.clamp(unitType.hitSize / 4000.0f, 0.0f, 0.01f);
        if (unit instanceof Legsc) {
            TextureRegion skin = skin(objectMap, "legs");
            TextureRegion skin2 = skin(objectMap, "legbase");
            TextureRegion skin3 = skin(objectMap, "joint");
            TextureRegion skin4 = skin(objectMap, "base");
            if (skin != null || skin2 != null || skin3 != null || skin4 != null) {
                drawLegSkin(unit, (Legsc) unit, unitType, skin, skin2, skin3, skin4, clamp - 0.01f);
            }
        } else if (unit instanceof Mechc) {
            TextureRegion skin5 = skin(objectMap, "legs");
            TextureRegion skin6 = skin(objectMap, "base");
            if (skin5 != null || skin6 != null) {
                drawMechSkin(unit, (Mechc) unit, unitType, skin5, skin6, clamp - 0.01f);
            }
        }
        TextureRegion skin7 = skin(objectMap, "body");
        if (skin7 != null) {
            Draw.z(clamp + 0.0105f);
            unitType.applyColor(unit);
            Draw.rect(skin7, unit.x, unit.y, unit.rotation - 90.0f);
            Draw.reset();
        }
        if (unit.mounts != null) {
            Seq<String> weaponNames2 = weaponNames(unitType);
            for (WeaponMount weaponMount : unit.mounts) {
                Weapon weapon = weaponMount.weapon;
                if (weapon != null && weapon.name != null && !weapon.name.isEmpty()) {
                    int indexOf = weaponNames2.indexOf(weapon.name);
                    TextureRegion skin8 = indexOf >= 0 ? skin(objectMap, "weapon" + indexOf) : null;
                    if (skin8 == null && skin7 != null && weapon.region != null && weapon.region.found()) {
                        skin8 = weapon.region;
                    }
                    if (skin8 != null) {
                        drawMount(unit, weaponMount, weapon, skin8, clamp);
                    }
                }
            }
        }
        Draw.reset();
    }

    static Seq<String> weaponNames(UnitType unitType) {
        Seq<String> seq = (Seq) weaponNames.get(unitType);
        if (seq == null) {
            seq = new Seq<>();
            Iterator it = unitType.weapons.iterator();
            while (it.hasNext()) {
                Weapon weapon = (Weapon) it.next();
                if (weapon.name != null && !weapon.name.isEmpty() && weapon.region != null && weapon.region.found() && !seq.contains(weapon.name)) {
                    seq.add(weapon.name);
                }
            }
            weaponNames.put(unitType, seq);
        }
        return seq;
    }

    private static void drawMount(Unit unit, WeaponMount weaponMount, Weapon weapon, TextureRegion textureRegion, float f) {
        Draw.z(f + 0.0106f + weapon.layerOffset);
        unit.type.applyColor(unit);
        float f2 = unit.rotation - 90.0f;
        float pow = Mathf.pow(weaponMount.recoil, weapon.recoilPow) * weapon.recoil;
        float f3 = f2 + (weapon.rotate ? weaponMount.rotation : weapon.baseRotation);
        float trnsx = unit.x + Angles.trnsx(f2, weapon.x, weapon.y) + Angles.trnsx(f3, 0.0f, -pow);
        float trnsy = unit.y + Angles.trnsy(f2, weapon.x, weapon.y) + Angles.trnsy(f3, 0.0f, -pow);
        float f4 = Draw.xscl;
        Draw.xscl *= -Mathf.sign(weapon.flipSprite);
        Draw.rect(textureRegion, trnsx, trnsy, f3);
        Draw.xscl = f4;
        Draw.reset();
    }

    private static void drawLegSkin(Unit unit, Legsc legsc, UnitType unitType, TextureRegion textureRegion, TextureRegion textureRegion2, TextureRegion textureRegion3, TextureRegion textureRegion4, float f) {
        Draw.z(f);
        unitType.applyColor(unit);
        TextureRegion textureRegion5 = textureRegion != null ? textureRegion : unitType.legRegion;
        TextureRegion textureRegion6 = textureRegion2 != null ? textureRegion2 : unitType.legBaseRegion;
        TextureRegion textureRegion7 = textureRegion3 != null ? textureRegion3 : unitType.jointRegion;
        Leg[] legs = legsc.legs();
        for (int length = legs.length - 1; length >= 0; length--) {
            int length2 = length % 2 == 0 ? length / 2 : (legs.length - 1) - (length / 2);
            Leg leg = legs[length2];
            int sign = Mathf.sign(((float) length2) >= ((float) legs.length) / 2.0f);
            Vec2 add = legsc.legOffset(tv2, length2).add(unit.x, unit.y);
            tv1.set(leg.base).sub(leg.joint).inv().setLength(unitType.legExtension);
            if (unitType.legBaseUnder) {
                Lines.stroke(textureRegion6.height * unitType.legRegion.scl() * sign);
                Lines.line(textureRegion6, leg.joint.x + tv1.x, leg.joint.y + tv1.y, leg.base.x, leg.base.y, false);
                Lines.stroke(textureRegion5.height * unitType.legRegion.scl() * sign);
                Lines.line(textureRegion5, add.x, add.y, leg.joint.x, leg.joint.y, false);
            } else {
                Lines.stroke(textureRegion5.height * unitType.legRegion.scl() * sign);
                Lines.line(textureRegion5, add.x, add.y, leg.joint.x, leg.joint.y, false);
                Lines.stroke(textureRegion6.height * unitType.legRegion.scl() * sign);
                Lines.line(textureRegion6, leg.joint.x + tv1.x, leg.joint.y + tv1.y, leg.base.x, leg.base.y, false);
            }
            if (textureRegion7 != null && textureRegion7.found()) {
                Draw.rect(textureRegion7, leg.joint.x, leg.joint.y);
            }
        }
        if (textureRegion4 != null) {
            Draw.rect(textureRegion4, unit.x, unit.y, legsc.baseRotation() - 90.0f);
        }
        Draw.reset();
    }

    private static void drawMechSkin(Unit unit, Mechc mechc, UnitType unitType, TextureRegion textureRegion, TextureRegion textureRegion2, float f) {
        Draw.z(f);
        unitType.applyColor(unit);
        float f2 = unit.elevation;
        float lerp = Mathf.lerp(Mathf.sin(mechc.walkExtend(true), 0.63661975f, 1.0f), 0.0f, f2);
        float lerp2 = Mathf.lerp(mechc.walkExtend(false), 0.0f, f2);
        float f3 = f2 * 2.0f;
        if (textureRegion != null) {
            for (int i : Mathf.signs) {
                Draw.rect(textureRegion, unit.x + Angles.trnsx(mechc.baseRotation(), (lerp2 * i) - f3, (-f3) * i), unit.y + Angles.trnsy(mechc.baseRotation(), (lerp2 * i) - f3, (-f3) * i), textureRegion.width * unitType.legRegion.scl() * i, textureRegion.height * unitType.legRegion.scl() * (1.0f - (Math.max((-lerp) * i, 0.0f) * 0.5f)), (mechc.baseRotation() - 90.0f) + (35.0f * i * f2));
            }
        }
        if (textureRegion2 != null) {
            Draw.rect(textureRegion2, unit.x, unit.y, mechc.baseRotation() - 90.0f);
        }
        Draw.reset();
    }

    private static void stopAllMusic() {
        try {
            Iterator it = playing.iterator();
            while (it.hasNext()) {
                IntMap.Entry entry = (IntMap.Entry) it.next();
                try {
                    if (((Music) entry.value).isPlaying()) {
                        ((Music) entry.value).stop();
                    }
                } catch (Throwable th) {
                }
            }
        } catch (Throwable th2) {
        }
    }

    private static void musicTick() {
        UnitPool unitPool;
        ObjectMap<String, Entry> skinsFor;
        if (!HubClient.consented() || Vars.state == null || !Vars.state.isGame() || Vars.player == null || pools.size == 0) {
            stopAllMusic();
            return;
        }
        IntFloatMap intFloatMap = new IntFloatMap();
        float f = Core.camera.position.x;
        float f2 = Core.camera.position.y;
        Iterator it = Groups.unit.iterator();
        while (it.hasNext()) {
            Unit unit = (Unit) it.next();
            if (unit != null && unit.isAdded() && !unit.dead && unit.type != null && (unitPool = (UnitPool) pools.get(unit.type.name)) != null && (skinsFor = skinsFor(unit, unitPool)) != null) {
                ObjectMap.Entries it2 = skinsFor.iterator();
                while (it2.hasNext()) {
                    Entry entry = (Entry) ((ObjectMap.Entry) it2.next()).value;
                    if (entry.music > 0 && entry.radius > 0) {
                        float f3 = entry.radius * 8.0f;
                        float dst = unit.dst(f, f2);
                        if (dst < f3) {
                            float f4 = 1.0f - (dst / f3);
                            if (f4 > intFloatMap.get(entry.music, 0.0f)) {
                                intFloatMap.put(entry.music, f4);
                            }
                        }
                    }
                }
            }
        }
        IntSeq intSeq = new IntSeq();
        Iterator it3 = intFloatMap.iterator();
        while (it3.hasNext()) {
            intSeq.add(((IntFloatMap.Entry) it3.next()).key);
        }
        intSeq.sort();
        for (int i = 0; i < intSeq.size; i++) {
            for (int i2 = i + 1; i2 < intSeq.size; i2++) {
                if (intFloatMap.get(intSeq.get(i2), 0.0f) > intFloatMap.get(intSeq.get(i), 0.0f)) {
                    int i3 = intSeq.get(i);
                    intSeq.set(i, intSeq.get(i2));
                    intSeq.set(i2, i3);
                }
            }
        }
        for (int i4 = intSeq.size - 1; i4 >= 2; i4--) {
            intSeq.removeIndex(i4);
        }
        float f5 = Core.settings.getInt("musicvol", 100) / 100.0f;
        for (int i5 = 0; i5 < intSeq.size; i5++) {
            int i6 = intSeq.get(i5);
            Music music = (Music) playing.get(i6);
            if (music == null) {
                loadAudio(i6);
            } else {
                try {
                    music.setVolume(Mathf.clamp(intFloatMap.get(i6, 0.0f) * f5));
                    if (!music.isPlaying()) {
                        music.play();
                    }
                } catch (Throwable th) {
                }
            }
        }
        Iterator it4 = playing.iterator();
        while (it4.hasNext()) {
            IntMap.Entry entry2 = (IntMap.Entry) it4.next();
            if (!intSeq.contains(entry2.key)) {
                try {
                    if (((Music) entry2.value).isPlaying()) {
                        ((Music) entry2.value).stop();
                    }
                } catch (Throwable th2) {
                }
            }
        }
    }

    private static Fi cached(int i) {
        Fi fi = (Fi) files.get(i);
        if (fi != null && fi.exists()) {
            return fi;
        }
        for (String str : new String[]{"ogg", "mp3", "wav"}) {
            Fi child = AudioUi.dir().child(i + "." + str);
            if (child.exists() && child.length() > 0) {
                files.put(i, child);
                return child;
            }
        }
        return null;
    }

    private static void startMusic(int i, Fi fi) {
        try {
            Music music = new Music(fi);
            music.setLooping(true);
            music.setVolume(0.0f);
            playing.put(i, music);
        } catch (Throwable th) {
            audioFailed.put(i, Time.time);
            Log.warn("[Lunarians Skins] musica @: @", new Object[]{Integer.valueOf(i), th.toString()});
        }
    }

    private static void loadAudio(int i) {
        if (audioLoading.contains(i) || audioFailed.get(i, -100000.0f) + 3600.0f >= Time.time) {
            return;
        }
        Fi cached = cached(i);
        if (cached != null) {
            startMusic(i, cached);
        } else {
            audioLoading.add(i);
            HubClient.call("GET", "/api/audio/get?id=" + i, null, jval -> {
                audioLoading.remove(i);
                try {
                    byte[] decode = Base64Coder.decode(jval.getString("data", ""));
                    String string = jval.getString("fmt", "ogg");
                    if (decode.length < 64 || !string.matches("ogg|mp3|wav")) {
                        throw new RuntimeException("invalido");
                    }
                    Fi child = AudioUi.dir().child(i + "." + string);
                    child.writeBytes(decode, false);
                    files.put(i, child);
                    startMusic(i, child);
                } catch (Throwable th) {
                    audioFailed.put(i, Time.time);
                }
            }, str -> {
                audioLoading.remove(i);
                audioFailed.put(i, Time.time);
            });
        }
    }

    private static Label wrap(String str) {
        Label label = new Label(str);
        label.setWrap(true);
        return label;
    }

    private static boolean isPng(byte[] bArr) {
        return bArr.length > 8 && (bArr[0] & 255) == 137 && bArr[1] == 80 && bArr[2] == 78 && bArr[3] == 71;
    }

    static String partLabel(String str) {
        return str.startsWith("weapon") ? HubText.get("skin.part.weapon") + " " + (Strings.parseInt(str.substring(6), 0) + 1) : HubText.get("skin.part." + str);
    }

    private static UnitType unitType(String str) {
        return (UnitType) Vars.content.units().find(unitType -> {
            return unitType.name.equals(str);
        });
    }

    static TextureRegion originalRegion(UnitType unitType, String str) {
        if (unitType == null) {
            return null;
        }
        switch (str) {
            case "body":
                return unitType.region;
            case "legs":
                return unitType.legRegion;
            case "legbase":
                return unitType.legBaseRegion;
            case "joint":
                return unitType.jointRegion;
            case "base":
                return unitType.baseRegion;
            default:
                if (str.startsWith("weapon")) {
                    Seq<String> weaponNames2 = weaponNames(unitType);
                    int parseInt = Strings.parseInt(str.substring(6), -1);
                    if (parseInt >= 0 && parseInt < weaponNames2.size) {
                        Iterator it = unitType.weapons.iterator();
                        while (it.hasNext()) {
                            Weapon weapon = (Weapon) it.next();
                            if (weapon.name.equals(weaponNames2.get(parseInt))) {
                                return weapon.region;
                            }
                        }
                        return null;
                    }
                    return null;
                }
                return null;
        }
    }

    static Seq<String> partsOf(UnitType unitType) {
        Seq<String> seq = new Seq<>();
        seq.add("body");
        for (String str : new String[]{"legs", "legbase", "joint", "base"}) {
            TextureRegion originalRegion = originalRegion(unitType, str);
            if (originalRegion != null && originalRegion.found()) {
                seq.add(str);
            }
        }
        Seq<String> weaponNames2 = weaponNames(unitType);
        for (int i = 0; i < weaponNames2.size && i < 30; i++) {
            seq.add("weapon" + i);
        }
        return seq;
    }

    public static void buildTab(Table table, Runnable runnable, float f) {
        table.clear();
        table.add(wrap(HubText.get("skin.help"))).width(f - 10.0f).left().padBottom(6.0f).row();
        table.check(HubText.get("skin.show"), showOthers(), z -> {
            Core.settings.put(K_SHOW, Boolean.valueOf(z));
            dirty = true;
        }).left().padBottom(6.0f).row();
        table.button(HubText.get("skin.new"), Icon.add, () -> {
            requestDialog(runnable);
        }).size(Math.min(f, 420.0f), 52.0f).padBottom(8.0f).row();
        Table table2 = new Table();
        table.add(table2).width(f).row();
        table2.add(wrap("...")).row();
        HubClient.call("GET", "/api/skins/mine", null, jval -> {
            table2.clear();
            Jval.JsonArray asArray = jval.get("skins").asArray();
            updateMine(asArray);
            if (asArray.size == 0) {
                table2.add(wrap(HubText.get("skin.none"))).width(f - 20.0f).pad(6.0f).row();
                return;
            }
            UiStyle.section(table2, HubText.get("skin.mine"));
            Iterator it = asArray.iterator();
            while (it.hasNext()) {
                Jval jval = (Jval) it.next();
                int i = jval.getInt("id", 0);
                String string = jval.getString("unit", "");
                String string2 = jval.getString("part", "body");
                String string3 = jval.getString("status", "pending");
                int i2 = jval.getInt("music", 0);
                int i3 = jval.getInt("music_radius", 0);
                table2.table(Styles.grayPanel, table3 -> {
                    table3.table(table3 -> {
                        Image image = new Image(Icon.players);
                        image.setScaling(Scaling.fit);
                        fetch(i, textureRegion -> {
                            image.setDrawable(new TextureRegionDrawable(textureRegion));
                        });
                        table3.add(image).size(56.0f).pad(4.0f);
                        String str = jval.getString("name", "?") + "\n" + HubText.get("skin.unit") + ": " + string + "  •  " + partLabel(string2) + "\n" + HubText.get("skin.status." + string3);
                        if (string3.equals("rejected") && !jval.getString("note", "").isEmpty()) {
                            str = str + " - " + jval.getString("note", "");
                        }
                        if (i2 > 0) {
                            str = str + "\n♪ " + HubText.get("skin.music.radius") + ": " + i3;
                        }
                        if (using(i)) {
                            str = str + "  [" + HubText.get("skin.using") + "]";
                        }
                        Label wrap = wrap(str);
                        wrap.setAlignment(8);
                        table3.add(wrap).growX().pad(6.0f);
                    }).growX().row();
                    table3.table(table4 -> {
                        table4.defaults().height(40.0f).pad(2.0f);
                        if (string3.equals("approved")) {
                            if (using(i)) {
                                table4.button(HubText.get("skin.stop"), () -> {
                                    unequip(i);
                                    runnable.run();
                                }).minWidth(96.0f);
                            } else {
                                table4.button(HubText.get("skin.use"), () -> {
                                    equip(i);
                                    runnable.run();
                                }).minWidth(96.0f);
                            }
                            table4.button("♪", () -> {
                                musicDialog(i, i2, i3, runnable);
                            }).size(44.0f, 40.0f);
                        }
                        table4.button(Icon.trash, Styles.cleari, () -> {
                            Vars.ui.showConfirm(HubText.get("skin.delete"), HubText.get("skin.delete.confirm"), () -> {
                                Jval newObject = Jval.newObject();
                                newObject.add("id", Jval.valueOf(i));
                                if (using(i)) {
                                    unequip(i);
                                }
                                HubClient.call("POST", "/api/skins/delete", newObject, jval2 -> {
                                    runnable.run();
                                }, str -> {
                                    Vars.ui.showInfoToast(HubText.get("sent.fail"), 2.0f);
                                });
                            });
                        }).size(40.0f);
                    }).left().row();
                }).width(f - 10.0f).pad(2.0f).row();
            }
        }, str -> {
            table2.clear();
            table2.add(wrap(HubText.get("sent.fail"))).row();
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void musicDialog(int i, int i2, int i3, Runnable runnable) {
        BaseDialog baseDialog = new BaseDialog(HubText.get("skin.music.title"));
        UiStyle.register(baseDialog);
        float min = Math.min(Core.scene.getWidth() - 40.0f, 520.0f);
        int[] iArr = {i2};
        baseDialog.cont.add(wrap(HubText.get("skin.music.help"))).width(min).padBottom(6.0f).row();
        Table table = new Table();
        baseDialog.cont.pane(table).width(min).maxHeight(Core.scene.getHeight() * 0.4f).row();
        table.add(wrap("...")).row();
        Label label = new Label(String.valueOf(i3 > 0 ? i3 : 12));
        Slider slider = new Slider(1.0f, 30.0f, 1.0f, false);
        slider.setValue(i3 > 0 ? i3 : 12.0f);
        slider.moved(f -> {
            label.setText(String.valueOf((int) f));
        });
        baseDialog.cont.table(table2 -> {
            table2.add(HubText.get("skin.music.radius")).padRight(8.0f);
            table2.add(slider).width(Math.min(min - 150.0f, 260.0f));
            table2.add(label).padLeft(8.0f).width(36.0f);
        }).padTop(6.0f).row();
        HubClient.call("GET", "/api/audio/mine", null, jval -> {
            table.clear();
            int i4 = 0;
            Iterator it = jval.get("audio").asArray().iterator();
            while (it.hasNext()) {
                Jval jval = (Jval) it.next();
                if ("music".equals(jval.getString("kind", "")) && "approved".equals(jval.getString("status", ""))) {
                    int i5 = jval.getInt("id", 0);
                    table.button(jval.getString("name", "?"), Styles.togglet, () -> {
                        iArr[0] = i5;
                    }).checked(textButton -> {
                        return iArr[0] == i5;
                    }).growX().height(44.0f).pad(2.0f).row();
                    i4++;
                }
            }
            if (i4 == 0) {
                table.add(wrap(HubText.get("skin.music.none"))).width(min - 20.0f).pad(6.0f).row();
            }
        }, str -> {
            table.clear();
            table.add(wrap(HubText.get("sent.fail"))).row();
        });
        Runnable runnable2 = () -> {
            Jval newObject = Jval.newObject();
            newObject.add("id", Jval.valueOf(i));
            newObject.add("audio", Jval.valueOf(iArr[0]));
            newObject.add("radius", Jval.valueOf((int) slider.getValue()));
            HubClient.call("POST", "/api/skins/music", newObject, jval2 -> {
                Vars.ui.showInfoToast(HubText.get("skin.music.saved"), 2.0f);
                refreshMine();
                baseDialog.hide();
                runnable.run();
            }, str2 -> {
                Vars.ui.showInfoToast(str2.equals("audio_not_approved") ? HubText.get("skin.music.none") : HubText.get("sent.fail"), 3.0f);
            });
        };
        baseDialog.cont.table(table3 -> {
            table3.defaults().height(48.0f).pad(3.0f);
            table3.button(HubText.get("skin.music.save"), Icon.ok, () -> {
                if (iArr[0] <= 0) {
                    Vars.ui.showInfoToast(HubText.get("skin.music.none"), 2.5f);
                } else {
                    runnable2.run();
                }
            }).minWidth(150.0f);
            table3.button(HubText.get("skin.music.remove"), Icon.trash, () -> {
                iArr[0] = 0;
                runnable2.run();
            }).minWidth(150.0f);
        }).padTop(8.0f).row();
        baseDialog.addCloseButton();
        baseDialog.show();
    }

    static void pickUnit(Cons<String> cons) {
        BaseDialog baseDialog = new BaseDialog(HubText.get("skin.pickunit"));
        UiStyle.register(baseDialog);
        Table table = new Table();
        float min = Math.min(Core.scene.getWidth() - 40.0f, 520.0f);
        int max = Math.max(3, (int) (min / 76.0f));
        int i = 0;
        Iterator it = Vars.content.units().iterator();
        while (it.hasNext()) {
            UnitType unitType = (UnitType) it.next();
            if (unitType.isVanilla() && !unitType.internal && unitType.uiIcon != null && okUnit(unitType.name)) {
                Table table2 = new Table();
                table2.button(new TextureRegionDrawable(unitType.uiIcon), Styles.cleari, 44.0f, () -> {
                    cons.get(unitType.name);
                    baseDialog.hide();
                }).size(56.0f);
                table2.row();
                table2.add(unitType.name).fontScale(0.55f);
                table.add(table2).pad(2.0f);
                i++;
                if (i % max == 0) {
                    table.row();
                }
            }
        }
        baseDialog.cont.pane(table).width(min).maxHeight(Core.scene.getHeight() * 0.65f).row();
        baseDialog.addCloseButton();
        baseDialog.show();
    }

    private static void pickPart(UnitType unitType, Cons<String> cons) {
        BaseDialog baseDialog = new BaseDialog(HubText.get("skin.pickpart"));
        UiStyle.register(baseDialog);
        float min = Math.min(Core.scene.getWidth() - 40.0f, 420.0f);
        Table table = new Table();
        Iterator it = partsOf(unitType).iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            TextureRegion originalRegion = originalRegion(unitType, str);
            table.button(partLabel(str) + ((originalRegion == null || !originalRegion.found()) ? "" : "  (" + originalRegion.width + "x" + originalRegion.height + ")"), () -> {
                cons.get(str);
                baseDialog.hide();
            }).growX().height(46.0f).pad(2.0f).row();
        }
        baseDialog.cont.pane(table).width(min).maxHeight(Core.scene.getHeight() * 0.65f).row();
        baseDialog.addCloseButton();
        baseDialog.show();
    }

    /* JADX WARN: Type inference failed for: r0v11, types: [byte[], byte[][]] */
    public static void requestDialog(Runnable runnable) {
        BaseDialog baseDialog = new BaseDialog(HubText.get("skin.new"));
        UiStyle.register(baseDialog);
        float min = Math.min(Core.scene.getWidth() - 40.0f, 520.0f);
        String[] strArr = {""};
        String[] strArr2 = {"body"};
        ?? r0 = {0};
        String[] strArr3 = {""};
        TextField textField = new TextField();
        textField.setMessageText(HubText.get("skin.name"));
        textField.setMaxLength(32);
        Label wrap = wrap("");
        TextButton textButton = new TextButton(HubText.get("skin.pickunit"), Styles.defaultt);
        TextButton textButton2 = new TextButton(HubText.get("skin.pickpart"), Styles.defaultt);
        TextButton textButton3 = new TextButton(HubText.get("skin.pickfile"), Styles.defaultt);
        Runnable runnable2 = () -> {
            textButton.setText(strArr[0].isEmpty() ? HubText.get("skin.pickunit") : HubText.get("skin.unit") + ": " + strArr[0]);
            textButton2.setText(strArr[0].isEmpty() ? HubText.get("skin.pickpart") : HubText.get("skin.part") + ": " + partLabel(strArr2[0]));
            textButton3.setText(r0[0] == null ? HubText.get("skin.pickfile") : "PNG " + strArr3[0]);
            String str = "";
            TextureRegion originalRegion = originalRegion(strArr[0].isEmpty() ? null : unitType(strArr[0]), strArr2[0]);
            if (originalRegion != null && originalRegion.found()) {
                str = HubText.get("skin.sprite") + ": " + originalRegion.width + "x" + originalRegion.height;
            }
            wrap.setText(str);
        };
        textButton.clicked(() -> {
            pickUnit(str -> {
                strArr[0] = str;
                strArr2[0] = "body";
                runnable2.run();
            });
        });
        textButton2.clicked(() -> {
            UnitType unitType = strArr[0].isEmpty() ? null : unitType(strArr[0]);
            if (unitType == null) {
                Vars.ui.showInfoToast(HubText.get("skin.fill"), 2.0f);
            } else {
                pickPart(unitType, str -> {
                    strArr2[0] = str;
                    runnable2.run();
                });
            }
        });
        textButton3.clicked(() -> {
            new FileChooser.FileChooserParams().open(true).title(HubText.get("skin.pickfile")).extensions(new String[]{"png"}).submit(fi -> {
                try {
                    byte[] readBytes = fi.readBytes();
                    Pixmap pixmap = (!isPng(readBytes) || readBytes.length > 153600) ? null : new Pixmap(readBytes);
                    if (pixmap == null || pixmap.width > 512 || pixmap.height > 512) {
                        if (pixmap != null) {
                            pixmap.dispose();
                        }
                        Vars.ui.showInfoToast(HubText.get("skin.invalid"), 3.0f);
                    } else {
                        strArr3[0] = pixmap.width + "x" + pixmap.height;
                        pixmap.dispose();
                        r0[0] = readBytes;
                        runnable2.run();
                    }
                } catch (Throwable th) {
                    Vars.ui.showInfoToast(HubText.get("skin.invalid"), 3.0f);
                }
            });
        });
        baseDialog.cont.add(wrap(HubText.get("skin.help"))).width(min).padBottom(6.0f).row();
        baseDialog.cont.add(textField).width(min).height(46.0f).row();
        baseDialog.cont.add(textButton).width(min).height(48.0f).padTop(6.0f).row();
        baseDialog.cont.add(textButton2).width(min).height(48.0f).padTop(6.0f).row();
        baseDialog.cont.add(textButton3).width(min).height(48.0f).padTop(6.0f).row();
        baseDialog.cont.add(wrap).width(min).padTop(6.0f).row();
        baseDialog.cont.button(HubText.get("skin.send"), Icon.upload, () -> {
            String trim = textField.getText().trim();
            if (trim.isEmpty() || strArr[0].isEmpty() || r0[0] == null) {
                Vars.ui.showInfoToast(HubText.get("skin.fill"), 2.5f);
                return;
            }
            Jval newObject = Jval.newObject();
            newObject.add("name", trim);
            newObject.add("unit", strArr[0]);
            newObject.add("part", strArr2[0]);
            newObject.add("png", new String(Base64Coder.encode(r0[0])));
            HubClient.call("POST", "/api/skins/request", newObject, jval -> {
                Vars.ui.showInfoToast(HubText.get("skin.sent"), 3.5f);
                baseDialog.hide();
                if (runnable != null) {
                    runnable.run();
                }
            }, str -> {
                Vars.ui.showInfoToast(str.equals("too_many_pending") ? HubText.get("skin.limit") : HubText.get("sent.fail"), 3.0f);
            });
        }).size(Math.min(min, 320.0f), 52.0f).padTop(10.0f).row();
        baseDialog.addCloseButton();
        baseDialog.shown(runnable2);
        baseDialog.show();
    }
}
