package lunarians.hub;

import arc.Core;
import arc.func.Cons;
import arc.scene.ui.Button;
import arc.scene.ui.Label;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Timer;
import arc.util.serialization.Jval;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import lunarians.UiStyle;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/hub/HubUi.class */
public class HubUi {
    private static BaseDialog hub;
    private static Table body;
    private static int tab = 0;

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Runnable {
        public final /* synthetic */ TextField f$0;
        public final /* synthetic */ boolean[] f$1;
        public final /* synthetic */ String f$2;
        public final /* synthetic */ Runnable[] f$3;

        public /* synthetic */ AnonymousClass0(TextField textField, boolean[] zArr, String str, Runnable[] runnableArr) {
            this.f$0 = textField;
            this.f$1 = zArr;
            this.f$2 = str;
            this.f$3 = runnableArr;
        }

        @Override // java.lang.Runnable
        public final void run() {
            HubUi.lambda$openChat$40(this.f$0, this.f$1, this.f$2, this.f$3);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass1 implements Cons {
        public final void get(Object obj) {
            HubUi.buildFriends();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$10, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass10 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            HubUi.lambda$renderFriendLists$13();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$11, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass11 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            HubUi.$r8$lambda$PbWNmrMNdPde9hkVGNOjZTLQP5U();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$12, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass12 implements Cons {
        public final void get(Object obj) {
            HubUi.$r8$lambda$0lb5X82YeLn6Q3avh1mED3N3Q7g((Jval) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$13, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass13 implements Cons {
        public final void get(Object obj) {
            HubUi.lambda$buildFriends$10((String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$14, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass14 implements Runnable {
        public final /* synthetic */ Runnable f$0;

        @Override // java.lang.Runnable
        public final void run() {
            HubUi.lambda$showConsent$2(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$15, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass15 implements Runnable {
        public final /* synthetic */ Jval f$0;

        @Override // java.lang.Runnable
        public final void run() {
            HubUi.lambda$renderFriendLists$20(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$16, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass16 implements Runnable {
        public final /* synthetic */ Jval f$0;

        @Override // java.lang.Runnable
        public final void run() {
            HubUi.lambda$renderFriendLists$21(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$17, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass17 implements Cons {
        public final /* synthetic */ boolean[] f$0;
        public final /* synthetic */ Runnable[] f$1;

        public /* synthetic */ AnonymousClass17(boolean[] zArr, Runnable[] runnableArr) {
            this.f$0 = zArr;
            this.f$1 = runnableArr;
        }

        public final void get(Object obj) {
            HubUi.lambda$openChat$38(this.f$0, this.f$1, (Jval) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$18, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass18 implements Cons {
        public final /* synthetic */ boolean[] f$0;
        public final /* synthetic */ TextField f$1;
        public final /* synthetic */ String f$2;

        public /* synthetic */ AnonymousClass18(boolean[] zArr, TextField textField, String str) {
            this.f$0 = zArr;
            this.f$1 = textField;
            this.f$2 = str;
        }

        public final void get(Object obj) {
            HubUi.lambda$openChat$39(this.f$0, this.f$1, this.f$2, (String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$19, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass19 implements Runnable {
        public final /* synthetic */ String f$0;
        public final /* synthetic */ Jval f$1;

        public /* synthetic */ AnonymousClass19(String str, Jval jval) {
            this.f$0 = str;
            this.f$1 = jval;
        }

        @Override // java.lang.Runnable
        public final void run() {
            HubUi.lambda$renderFriendLists$25(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass2 implements Cons {
        public final void get(Object obj) {
            HubUi.lambda$act$31((String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$20, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass20 implements Runnable {
        public final /* synthetic */ String f$0;

        @Override // java.lang.Runnable
        public final void run() {
            HubUi.report(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$21, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass21 implements Runnable {
        public final /* synthetic */ String f$0;

        @Override // java.lang.Runnable
        public final void run() {
            HubUi.act("/api/friends/remove", "id", this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$22, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass22 implements Cons {
        public final void get(Object obj) {
            HubUi.lambda$renderFriendLists$14((Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$23, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass23 implements Cons {
        public final /* synthetic */ Jval f$0;
        public final /* synthetic */ RowFill f$1;

        public /* synthetic */ AnonymousClass23(Jval jval, RowFill rowFill) {
            this.f$0 = jval;
            this.f$1 = rowFill;
        }

        public final void get(Object obj) {
            HubUi.lambda$section$29(this.f$0, this.f$1, (Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$24, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass24 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            HubUi.lambda$buildSettings$51();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$25, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass25 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            HubUi.lambda$buildSettings$52();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$26, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass26 implements Cons {
        public final /* synthetic */ String f$0;

        public final void get(Object obj) {
            HubUi.lambda$showGlobal$5(this.f$0, (Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$27, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass27 implements Comparator {
        @Override // java.util.Comparator
        public final int compare(Object obj, Object obj2) {
            return HubUi.lambda$openChats$44((Jval) obj, (Jval) obj2);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$28, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass28 implements Cons {
        public final /* synthetic */ Jval f$0;
        public final /* synthetic */ String f$1;
        public final /* synthetic */ boolean f$2;

        public /* synthetic */ AnonymousClass28(Jval jval, String str, boolean z) {
            this.f$0 = jval;
            this.f$1 = str;
            this.f$2 = z;
        }

        public final void get(Object obj) {
            HubUi.lambda$openChats$45(this.f$0, this.f$1, this.f$2, (Button) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$29, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass29 implements Runnable {
        public final /* synthetic */ String f$0;
        public final /* synthetic */ String f$1;

        public /* synthetic */ AnonymousClass29(String str, String str2) {
            this.f$0 = str;
            this.f$1 = str2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Plus.openChatId(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$3, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass3 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            HubUi.open();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$30, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass30 implements Runnable {
        public final /* synthetic */ TextField f$0;

        @Override // java.lang.Runnable
        public final void run() {
            HubUi.lambda$renderFriendLists$18(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$31, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass31 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            HubUi.lambda$rebuild$6();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$32, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass32 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            HubUi.lambda$rebuild$7();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$33, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass33 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            HubUi.lambda$rebuild$8();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$34, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass34 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            HubUi.$r8$lambda$MqYb1HAyEim3rjDbH8L962Gu0po();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$35, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass35 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            HubUi.lambda$renderFriendLists$11();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$36, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass36 implements Cons {
        public final void get(Object obj) {
            HubUi.lambda$renderFriendLists$15((Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$37, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass37 implements Cons {
        public final /* synthetic */ TextField f$0;

        public final void get(Object obj) {
            HubUi.lambda$renderFriendLists$19(this.f$0, (Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$38, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass38 implements RowFill {
        @Override // lunarians.hub.HubUi.RowFill
        public final void fill(Table table, Jval jval) {
            HubUi.lambda$renderFriendLists$22(table, jval);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$39, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass39 implements RowFill {
        @Override // lunarians.hub.HubUi.RowFill
        public final void fill(Table table, Jval jval) {
            HubUi.lambda$renderFriendLists$24(table, jval);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$4, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass4 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            HubUi.$r8$lambda$17l_Y_wW1aleKAO6LGsC9jXc0JM();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$40, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass40 implements RowFill {
        @Override // lunarians.hub.HubUi.RowFill
        public final void fill(Table table, Jval jval) {
            HubUi.lambda$renderFriendLists$28(table, jval);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$41, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass41 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            HubUi.openChats();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$42, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass42 implements Cons {
        public final /* synthetic */ Table f$0;

        public final void get(Object obj) {
            HubUi.lambda$openChats$47(this.f$0, (Jval) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$43, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass43 implements Cons {
        public final /* synthetic */ Table f$0;

        public final void get(Object obj) {
            HubUi.lambda$openChats$48(this.f$0, (String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$44, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass44 implements Runnable {
        public final /* synthetic */ BaseDialog f$0;

        @Override // java.lang.Runnable
        public final void run() {
            HubUi.lambda$openChats$49(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$45, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass45 implements Cons {
        public final void get(Object obj) {
            HubUi.lambda$showConsent$0((Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$46, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass46 implements Runnable {
        public final /* synthetic */ BaseDialog f$0;

        @Override // java.lang.Runnable
        public final void run() {
            HubUi.lambda$showConsent$1(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$47, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass47 implements Runnable {
        public final /* synthetic */ BaseDialog f$0;
        public final /* synthetic */ Runnable f$1;

        public /* synthetic */ AnonymousClass47(BaseDialog baseDialog, Runnable runnable) {
            this.f$0 = baseDialog;
            this.f$1 = runnable;
        }

        @Override // java.lang.Runnable
        public final void run() {
            HubUi.lambda$showConsent$3(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$48, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass48 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            HubUi.lambda$showConsent$4();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$49, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass49 implements Cons {
        public final void get(Object obj) {
            HubUi.lambda$report$32((Jval) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$5, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass5 implements Cons {
        public final void get(Object obj) {
            HubUi.buildFriends();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$50, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass50 implements Cons {
        public final void get(Object obj) {
            HubUi.lambda$report$33((String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$51, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass51 implements Cons {
        public final void get(Object obj) {
            HubUi.lambda$rebuild$9((Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$52, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass52 implements Runnable {
        public final /* synthetic */ boolean[] f$0;
        public final /* synthetic */ boolean[] f$1;
        public final /* synthetic */ String f$2;
        public final /* synthetic */ int[] f$3;
        public final /* synthetic */ String f$4;
        public final /* synthetic */ Table f$5;
        public final /* synthetic */ Runnable[] f$6;

        public /* synthetic */ AnonymousClass52(boolean[] zArr, boolean[] zArr2, String str, int[] iArr, String str2, Table table, Runnable[] runnableArr) {
            this.f$0 = zArr;
            this.f$1 = zArr2;
            this.f$2 = str;
            this.f$3 = iArr;
            this.f$4 = str2;
            this.f$5 = table;
            this.f$6 = runnableArr;
        }

        @Override // java.lang.Runnable
        public final void run() {
            HubUi.lambda$openChat$37(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$53, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass53 implements Cons {
        public final /* synthetic */ TextField f$0;
        public final /* synthetic */ boolean[] f$1;
        public final /* synthetic */ String f$2;
        public final /* synthetic */ Runnable[] f$3;

        public /* synthetic */ AnonymousClass53(TextField textField, boolean[] zArr, String str, Runnable[] runnableArr) {
            this.f$0 = textField;
            this.f$1 = zArr;
            this.f$2 = str;
            this.f$3 = runnableArr;
        }

        public final void get(Object obj) {
            HubUi.lambda$openChat$41(this.f$0, this.f$1, this.f$2, this.f$3, (Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$54, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass54 implements Runnable {
        public final /* synthetic */ Runnable[] f$0;

        @Override // java.lang.Runnable
        public final void run() {
            HubUi.lambda$openChat$42(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$55, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass55 implements Runnable {
        public final /* synthetic */ BaseDialog f$0;
        public final /* synthetic */ Runnable[] f$1;

        public /* synthetic */ AnonymousClass55(BaseDialog baseDialog, Runnable[] runnableArr) {
            this.f$0 = baseDialog;
            this.f$1 = runnableArr;
        }

        @Override // java.lang.Runnable
        public final void run() {
            HubUi.lambda$openChat$43(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$56, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass56 implements Runnable {
        public final /* synthetic */ Timer.Task f$0;

        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.cancel();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$57, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass57 implements Cons {
        public final void get(Object obj) {
            HubUi.lambda$buildSettings$50((Boolean) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$58, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass58 implements Cons {
        public final /* synthetic */ boolean[] f$0;
        public final /* synthetic */ int[] f$1;
        public final /* synthetic */ String f$2;
        public final /* synthetic */ Table f$3;
        public final /* synthetic */ boolean[] f$4;
        public final /* synthetic */ Runnable[] f$5;

        public /* synthetic */ AnonymousClass58(boolean[] zArr, int[] iArr, String str, Table table, boolean[] zArr2, Runnable[] runnableArr) {
            this.f$0 = zArr;
            this.f$1 = iArr;
            this.f$2 = str;
            this.f$3 = table;
            this.f$4 = zArr2;
            this.f$5 = runnableArr;
        }

        public final void get(Object obj) {
            HubUi.lambda$openChat$35(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, (Jval) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$59, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass59 implements Cons {
        public final /* synthetic */ boolean[] f$0;
        public final /* synthetic */ boolean[] f$1;

        public /* synthetic */ AnonymousClass59(boolean[] zArr, boolean[] zArr2) {
            this.f$0 = zArr;
            this.f$1 = zArr2;
        }

        public final void get(Object obj) {
            HubUi.lambda$openChat$36(this.f$0, this.f$1, (String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$6, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass6 implements Cons {
        public final void get(Object obj) {
            HubUi.lambda$renderFriendLists$17((String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$7, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass7 implements Runnable {
        public final /* synthetic */ Jval f$0;

        @Override // java.lang.Runnable
        public final void run() {
            HubUi.lambda$renderFriendLists$23(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$8, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass8 implements Cons {
        public final /* synthetic */ String f$0;

        public final void get(Object obj) {
            HubUi.lambda$report$34(this.f$0, (String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.HubUi$9, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass9 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            HubUi.lambda$renderFriendLists$12();
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      classes.dex
     */
    /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/hub/HubUi$RowFill.class */
    private interface RowFill {
        void fill(Table table, Jval jval);
    }

    private static float width() {
        return Math.min(Core.scene.getWidth() - 40.0f, 520.0f);
    }

    public static void showConsent(Runnable runnable) {
        BaseDialog baseDialog = new BaseDialog(HubText.get("consent.title"));
        UiStyle.register(baseDialog);
        baseDialog.cont.pane(table -> {
            Label label = new Label(HubText.get("consent.body"));
            label.setWrap(true);
            table.add(label).width(width()).left().pad(8.0f);
        }).maxHeight(Core.scene.getHeight() * 0.6f).row();
        baseDialog.buttons.defaults().size(Math.min((width() / 2.0f) - 6.0f, 220.0f), 56.0f).pad(4.0f);
        baseDialog.buttons.button(HubText.get("decline"), Icon.cancel, () -> {
            HubClient.setConsent(false);
            baseDialog.hide();
        });
        baseDialog.buttons.button(HubText.get("accept"), Icon.ok, () -> {
            HubClient.setConsent(true);
            baseDialog.hide();
            HubClient.ensureRegistered(() -> {
                HubClient.heartbeat(null);
                if (runnable != null) {
                    runnable.run();
                }
            });
        });
        baseDialog.closeOnBack(() -> {
            if (HubClient.decided()) {
                return;
            }
            HubClient.setConsent(false);
        });
        baseDialog.show();
    }

    public static void showGlobal(String str) {
        BaseDialog baseDialog = new BaseDialog(HubText.get("global.title"));
        UiStyle.register(baseDialog);
        baseDialog.cont.pane(table -> {
            Label label = new Label(str);
            label.setWrap(true);
            table.add(label).width(width()).pad(8.0f);
        }).maxHeight(Core.scene.getHeight() * 0.6f);
        baseDialog.addCloseButton();
        baseDialog.show();
    }

    public static void showBlocked() {
        BaseDialog baseDialog = new BaseDialog(HubText.get("title"));
        UiStyle.register(baseDialog);
        Label label = new Label(HubText.get("blocked"));
        label.setWrap(true);
        baseDialog.cont.add(label).width(width()).pad(8.0f);
        baseDialog.addCloseButton();
        baseDialog.show();
    }

    public static void open() {
        if (!HubClient.decided() || !HubClient.consented()) {
            Access.showConsent(HubUi::open);
            return;
        }
        if (HubClient.blocked) {
            showBlocked();
            return;
        }
        if (hub == null) {
            hub = new BaseDialog(HubText.get("title"));
            UiStyle.register(hub);
            hub.addCloseButton();
            hub.shown(HubUi::rebuild);
        }
        hub.show();
    }

    private static void rebuild() {
        hub.cont.clear();
        hub.cont.table(table -> {
            table.defaults().height(48.0f).growX().pad(2.0f);
            table.button(HubText.get("tab.friends"), Styles.togglet, () -> {
                tab = 0;
                rebuild();
            }).checked(tab == 0);
            table.button(HubText.get("tab.skins"), Styles.togglet, () -> {
                tab = 2;
                rebuild();
            }).checked(tab == 2);
            table.button(HubText.get("tab.settings"), Styles.togglet, () -> {
                tab = 1;
                rebuild();
            }).checked(tab == 1);
        }).width(width()).row();
        body = new Table();
        hub.cont.pane(body).width(width()).maxHeight(Core.scene.getHeight() * 0.65f).row();
        if (tab == 0) {
            buildFriends();
        } else if (tab == 2) {
            buildSkins();
        } else {
            buildSettings();
        }
    }

    private static Label wrapLabel(String str) {
        Label label = new Label(str);
        label.setWrap(true);
        return label;
    }

    private static void buildSkins() {
        Skins.buildTab(body, HubUi::buildSkins, width());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void buildFriends() {
        body.clear();
        body.add(wrapLabel("...")).row();
        HubClient.call("GET", "/api/friends", null, HubUi::renderFriendLists, str -> {
        });
    }

    private static void renderFriendLists(Jval jval) {
        body.clear();
        UiStyle.section(body, HubText.get("myid"));
        body.add(wrapLabel(HubClient.myId())).width(width()).left().row();
        body.button(HubText.get("copy"), () -> {
            Core.app.setClipboardText(HubClient.myId());
            Vars.ui.showInfoToast(HubText.get("copied"), 1.5f);
        }).size(140.0f, 40.0f).left().padBottom(6.0f).row();
        body.table(table -> {
            table.add(Avatar.mine(48.0f)).size(48.0f).padRight(8.0f);
            table.table(table -> {
                table.defaults().height(40.0f).growX().pad(1.0f);
                table.button(HubText.get("avatar.pick"), () -> {
                    Avatar.pick(HubUi::buildFriends);
                }).row();
                table.button(HubText.get("avatar.upload"), () -> {
                    Avatar.pickImage(HubUi::buildFriends);
                }).row();
            }).growX();
        }).width(width()).padBottom(6.0f).row();
        TextField textField = new TextField();
        textField.setMessageText(HubText.get("addfriend"));
        textField.setMaxLength(32);
        body.table(table2 -> {
            table2.add(textField).growX().height(44.0f);
            table2.button(HubText.get("send"), () -> {
                String lowerCase = textField.getText().trim().toLowerCase();
                if (lowerCase.length() != 32) {
                    return;
                }
                Jval newObject = Jval.newObject();
                newObject.add("to", lowerCase);
                HubClient.call("POST", "/api/friends/request", newObject, jval2 -> {
                    buildFriends();
                }, str -> {
                    Vars.ui.showInfoToast(HubText.get("sent.fail"), 2.0f);
                });
            }).size(90.0f, 44.0f);
        }).width(width()).row();
        section(HubText.get("requests"), jval.get("incoming").asArray(), (table3, jval2) -> {
            table3.button(HubText.get("accept.req"), () -> {
                act("/api/friends/accept", "from", jval2.getString("id", ""));
            }).size(90.0f, 38.0f);
            table3.button(Icon.cancel, Styles.cleari, () -> {
                act("/api/friends/remove", "id", jval2.getString("id", ""));
            }).size(38.0f);
        });
        section(HubText.get("pending"), jval.get("outgoing").asArray(), (table4, jval3) -> {
            table4.button(Icon.cancel, Styles.cleari, () -> {
                act("/api/friends/remove", "id", jval3.getString("id", ""));
            }).size(38.0f);
        });
        section(HubText.get("friends"), jval.get("friends").asArray(), (table5, jval4) -> {
            String string = jval4.getString("id", "");
            table5.button(Icon.chat, Styles.cleari, () -> {
                Plus.openChatId(string, jval4.getString("name", "?"));
            }).size(38.0f);
            table5.button(Icon.warning, Styles.cleari, () -> {
                report(string);
            }).size(38.0f);
            table5.button(Icon.trash, Styles.cleari, () -> {
                act("/api/friends/remove", "id", string);
            }).size(38.0f);
        });
    }

    private static void section(String str, Seq<Jval> seq, RowFill rowFill) {
        if (seq.size == 0) {
            return;
        }
        UiStyle.section(body, str);
        Iterator it = seq.iterator();
        while (it.hasNext()) {
            Jval jval = (Jval) it.next();
            body.table(Styles.grayPanel, table -> {
                boolean has = jval.has("online");
                String string = jval.getString("name", "?");
                if (has) {
                    string = string + "  [" + (jval.getBool("online", false) ? HubText.get("online") : HubText.get("offline")) + "]";
                    String string2 = jval.getString("server_name", "");
                    if (!string2.isEmpty() && jval.getBool("online", false)) {
                        string = string + "\n" + string2 + ":" + jval.getInt("server_port", 0);
                    }
                }
                table.add(Avatar.imageFor(jval, 40.0f)).size(40.0f).pad(4.0f);
                Label wrapLabel = wrapLabel(string);
                wrapLabel.setAlignment(8);
                table.add(wrapLabel).growX().pad(6.0f);
                rowFill.fill(table, jval);
            }).width(width() - 10.0f).pad(2.0f).row();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void act(String str, String str2, String str3) {
        Jval newObject = Jval.newObject();
        newObject.add(str2, str3);
        HubClient.call("POST", str, newObject, jval -> {
            buildFriends();
        }, str4 -> {
            Vars.ui.showInfoToast(HubText.get("sent.fail"), 2.0f);
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void report(String str) {
        Vars.ui.showTextInput(HubText.get("report"), "", 200, "", str2 -> {
            Jval newObject = Jval.newObject();
            newObject.add("target", str);
            newObject.add("reason", str2);
            HubClient.call("POST", "/api/report", newObject, jval -> {
                Vars.ui.showInfoToast(HubText.get("reported"), 2.0f);
            }, str2 -> {
                Vars.ui.showInfoToast(HubText.get("sent.fail"), 2.0f);
            });
        });
    }

    static void openChat(String str, String str2) {
        BaseDialog baseDialog = new BaseDialog(HubText.get("chat") + ": " + str2);
        UiStyle.register(baseDialog);
        Table table = new Table();
        int[] iArr = {0};
        boolean[] zArr = {false};
        boolean[] zArr2 = {false};
        boolean[] zArr3 = {false};
        Runnable[] runnableArr = new Runnable[1];
        runnableArr[0] = () -> {
            if (zArr[0]) {
                zArr2[0] = true;
            } else {
                zArr[0] = true;
                HubClient.call("GET", "/api/chat?with=" + str + "&after=" + iArr[0], null, jval -> {
                    zArr[0] = false;
                    Iterator it = jval.get("messages").asArray().iterator();
                    while (it.hasNext()) {
                        Jval jval = (Jval) it.next();
                        int i = jval.getInt("id", 0);
                        if (i > iArr[0]) {
                            iArr[0] = i;
                            table.add(wrapLabel((jval.getBool("mine", false) ? "> " : str2 + ": ") + jval.getString("text", ""))).width(width() - 20.0f).left().row();
                            HubClient.setLastChat(Math.max(HubClient.lastChat(), i));
                        }
                    }
                    if (zArr2[0]) {
                        zArr2[0] = false;
                        runnableArr[0].run();
                    }
                }, str3 -> {
                    zArr[0] = false;
                    zArr2[0] = false;
                });
            }
        };
        baseDialog.cont.pane(table).width(width()).height(Core.scene.getHeight() * 0.4f).row();
        TextField textField = new TextField();
        textField.setMessageText(HubText.get("msg.hint"));
        textField.setMaxLength(300);
        baseDialog.cont.table(table2 -> {
            table2.add(textField).growX().height(46.0f);
            table2.button(HubText.get("send"), () -> {
                String trim = textField.getText().trim();
                if (trim.isEmpty() || zArr3[0]) {
                    return;
                }
                zArr3[0] = true;
                textField.setText("");
                Jval newObject = Jval.newObject();
                newObject.add("to", str);
                newObject.add("text", trim);
                HubClient.call("POST", "/api/chat/send", newObject, jval -> {
                    zArr3[0] = false;
                    runnableArr[0].run();
                }, str3 -> {
                    zArr3[0] = false;
                    textField.setText(trim);
                    Vars.ui.showInfoToast(HubText.get("sent.fail"), 2.0f);
                });
            }).size(90.0f, 46.0f);
        }).width(width());
        baseDialog.addCloseButton();
        baseDialog.shown(() -> {
            runnableArr[0].run();
        });
        Timer.Task schedule = Timer.schedule(() -> {
            if (baseDialog.isShown()) {
                runnableArr[0].run();
            }
        }, 4.0f, 4.0f);
        Objects.requireNonNull(schedule);
        baseDialog.hidden(schedule::cancel);
        baseDialog.show();
    }

    public static void openChats() {
        if (!HubClient.decided() || !HubClient.consented()) {
            Access.showConsent(HubUi::openChats);
            return;
        }
        if (HubClient.blocked) {
            showBlocked();
            return;
        }
        BaseDialog baseDialog = new BaseDialog(HubText.get("chats.title"));
        UiStyle.register(baseDialog);
        Table table = new Table();
        baseDialog.cont.pane(table).width(width()).maxHeight(Core.scene.getHeight() * 0.65f).row();
        table.add(wrapLabel("...")).row();
        HubClient.call("GET", "/api/friends", null, jval -> {
            table.clear();
            Jval.JsonArray asArray = jval.get("friends").asArray();
            if (((Seq) asArray).size == 0) {
                table.add(wrapLabel(HubText.get("chats.empty"))).width(width() - 20.0f).pad(8.0f).row();
                return;
            }
            asArray.sort((jval, jval2) -> {
                return Boolean.compare(jval2.getBool("online", false), jval.getBool("online", false));
            });
            Iterator it = asArray.iterator();
            while (it.hasNext()) {
                Jval jval3 = (Jval) it.next();
                String string = jval3.getString("id", "");
                String string2 = jval3.getString("name", "?");
                boolean bool = jval3.getBool("online", false);
                table.button(button -> {
                    button.add(Avatar.imageFor(jval3, 40.0f)).size(40.0f).pad(4.0f);
                    Label wrapLabel = wrapLabel(string2 + "  [" + (bool ? HubText.get("online") : HubText.get("offline")) + "]");
                    wrapLabel.setAlignment(8);
                    button.add(wrapLabel).growX().pad(4.0f);
                    button.image(Icon.chat).size(28.0f).pad(4.0f);
                }, () -> {
                    Plus.openChatId(string, string2);
                }).width(width() - 10.0f).minHeight(52.0f).pad(2.0f).row();
            }
        }, str -> {
            table.clear();
            table.add(wrapLabel(HubText.get("sent.fail"))).row();
        });
        baseDialog.cont.button(HubText.get("open.hub"), Icon.players, () -> {
            baseDialog.hide();
            open();
        }).size(220.0f, 46.0f).padTop(8.0f);
        baseDialog.addCloseButton();
        baseDialog.show();
    }

    private static void buildSettings() {
        body.clear();
        body.add(wrapLabel(HubText.get("consent.body"))).width(width() - 10.0f).left().row();
        body.button(HubText.get("leave"), Icon.trash, () -> {
            Vars.ui.showConfirm(HubText.get("title"), HubText.get("leave.confirm"), () -> {
                HubClient.leaveAndDelete(bool -> {
                    Vars.ui.showInfoToast(HubText.get(bool.booleanValue() ? "leave.done" : "leave.offline"), 3.0f);
                    if (!bool.booleanValue() || hub == null) {
                        return;
                    }
                    hub.hide();
                });
            });
        }).size(Math.min(width(), 420.0f), 56.0f).padTop(12.0f);
    }
}
