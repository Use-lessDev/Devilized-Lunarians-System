package lunarians.ui;

import arc.func.Boolc;
import arc.func.Cons;
import arc.func.Prov;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import java.util.Iterator;
import lunarians.AutoAttack;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import lunarians.data.AttackSlot;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/AutoAttackUi.class */
public class AutoAttackUi {
    private final Table list = new Table();
    private static AttackSlot clipboard = null;
    private static BuilderDialog currentDlg;

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoAttackUi$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Runnable {
        public final /* synthetic */ AttackSlot f$0;
        public final /* synthetic */ UnitType f$1;
        public final /* synthetic */ BaseDialog f$2;

        public /* synthetic */ AnonymousClass0(AttackSlot attackSlot, UnitType unitType, BaseDialog baseDialog) {
            this.f$0 = attackSlot;
            this.f$1 = unitType;
            this.f$2 = baseDialog;
        }

        @Override // java.lang.Runnable
        public final void run() {
            AutoAttackUi.lambda$openUnitPicker$22(this.f$0, this.f$1, this.f$2);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoAttackUi$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass1 implements Cons {
        public final void get(Object obj) {
            AutoAttackUi.lambda$make$0((Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoAttackUi$10, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass10 implements Prov {
        public final /* synthetic */ AttackSlot f$0;

        public final Object get() {
            return AutoAttackUi.lambda$card$20(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoAttackUi$11, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass11 implements Runnable {
        public final /* synthetic */ AttackSlot f$0;

        @Override // java.lang.Runnable
        public final void run() {
            AutoAttackUi.lambda$card$21(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoAttackUi$12, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass12 implements Runnable {
        public final /* synthetic */ AttackSlot f$0;

        @Override // java.lang.Runnable
        public final void run() {
            AutoAttackUi.lambda$card$10(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoAttackUi$13, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass13 implements Runnable {
        public final /* synthetic */ int f$0;

        @Override // java.lang.Runnable
        public final void run() {
            AutoAttackUi.lambda$card$11(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoAttackUi$14, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass14 implements Runnable {
        public final /* synthetic */ AttackSlot f$0;

        @Override // java.lang.Runnable
        public final void run() {
            AutoAttackUi.lambda$card$12(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoAttackUi$15, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass15 implements Prov {
        public final /* synthetic */ AttackSlot f$0;

        public final Object get() {
            return AutoAttackUi.lambda$card$13(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoAttackUi$16, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass16 implements Runnable {
        public final /* synthetic */ AttackSlot f$0;

        @Override // java.lang.Runnable
        public final void run() {
            AutoAttackUi.lambda$card$14(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoAttackUi$17, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass17 implements Runnable {
        public final /* synthetic */ AttackSlot f$0;
        public final /* synthetic */ int f$1;

        public /* synthetic */ AnonymousClass17(AttackSlot attackSlot, int i) {
            this.f$0 = attackSlot;
            this.f$1 = i;
        }

        @Override // java.lang.Runnable
        public final void run() {
            AutoAttackUi.lambda$card$15(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoAttackUi$18, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass18 implements Prov {
        public final /* synthetic */ AttackSlot f$0;
        public final /* synthetic */ int f$1;

        public /* synthetic */ AnonymousClass18(AttackSlot attackSlot, int i) {
            this.f$0 = attackSlot;
            this.f$1 = i;
        }

        public final Object get() {
            return AutoAttackUi.lambda$card$16(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoAttackUi$19, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass19 implements Runnable {
        public final /* synthetic */ AttackSlot f$0;
        public final /* synthetic */ int f$1;

        public /* synthetic */ AnonymousClass19(AttackSlot attackSlot, int i) {
            this.f$0 = attackSlot;
            this.f$1 = i;
        }

        @Override // java.lang.Runnable
        public final void run() {
            AutoAttackUi.lambda$card$17(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoAttackUi$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass2 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoAttackUi.lambda$make$1();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoAttackUi$20, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass20 implements Runnable {
        public final /* synthetic */ boolean f$0;
        public final /* synthetic */ AttackSlot f$1;

        public /* synthetic */ AnonymousClass20(boolean z, AttackSlot attackSlot) {
            this.f$0 = z;
            this.f$1 = attackSlot;
        }

        @Override // java.lang.Runnable
        public final void run() {
            AutoAttackUi.lambda$card$18(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoAttackUi$21, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass21 implements Cons {
        public final /* synthetic */ AttackSlot f$0;
        public final /* synthetic */ BaseDialog f$1;

        public /* synthetic */ AnonymousClass21(AttackSlot attackSlot, BaseDialog baseDialog) {
            this.f$0 = attackSlot;
            this.f$1 = baseDialog;
        }

        public final void get(Object obj) {
            AutoAttackUi.lambda$openUnitPicker$23(this.f$0, this.f$1, (Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoAttackUi$22, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass22 implements Runnable {
        public final /* synthetic */ Seq f$0;

        @Override // java.lang.Runnable
        public final void run() {
            AutoAttackUi.lambda$rebuild$7(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoAttackUi$23, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass23 implements Runnable {
        public final /* synthetic */ Seq f$0;

        @Override // java.lang.Runnable
        public final void run() {
            AutoAttackUi.lambda$rebuild$8(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoAttackUi$3, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass3 implements Boolc {
        public final void get(boolean z) {
            AutoAttack.setRegroupEnabled(z);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoAttackUi$4, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass4 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoAttackUi.lambda$new$3();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoAttackUi$5, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass5 implements Prov {
        public final Object get() {
            return AutoAttackUi.lambda$new$4();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoAttackUi$6, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass6 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoAttackUi.lambda$new$5();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoAttackUi$7, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass7 implements Boolc {
        public final void get(boolean z) {
            AutoAttackUi.lambda$new$6(z);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoAttackUi$8, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass8 implements Runnable {
        public final /* synthetic */ AttackSlot f$1;

        @Override // java.lang.Runnable
        public final void run() {
            AutoAttackUi.this.lambda$card$9$lunarians-ui-AutoAttackUi(this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoAttackUi$9, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass9 implements Runnable {
        public final /* synthetic */ AttackSlot f$0;

        @Override // java.lang.Runnable
        public final void run() {
            AutoAttackUi.lambda$card$19(this.f$0);
        }
    }

    public static BuilderDialog make() {
        BuilderDialog make0 = make0();
        currentDlg = make0;
        return make0;
    }

    private static void reopen() {
        if (currentDlg != null) {
            currentDlg.hide();
        }
        currentDlg = make();
        currentDlg.show();
    }

    public static BuilderDialog make0() {
        return new BuilderDialog(title(), table -> {
            new AutoAttackUi(table);
        }, () -> {
        });
    }

    public AutoAttackUi(Table table) {
        table.defaults().growX();
        table.add(hint()).wrap().growX().color(Color.lightGray).padBottom(4.0f).row();
        Table table2 = new Table();
        table2.check(tr("Regroup", "Reagrupar"), AutoAttack.regroupEnabled(), z -> {
            AutoAttack.setRegroupEnabled(z);
        }).left();
        table2.add(" " + tr("every", "a cada") + " ").fontScale(0.85f);
        table2.button("-", () -> {
            AutoAttack.setRegroupSec(AutoAttack.regroupSec() - 1);
            reopen();
        }).size(32.0f, 28.0f);
        table2.label(() -> {
            return AutoAttack.regroupSec() + "s";
        }).width(40.0f).center();
        table2.button("+", () -> {
            AutoAttack.setRegroupSec(AutoAttack.regroupSec() + 1);
            reopen();
        }).size(32.0f, 28.0f);
        table2.add(" " + tr("(min 5)", "(min 5)")).color(Color.lightGray).fontScale(0.75f);
        table.add(table2).left().padBottom(6.0f).row();
        Table table3 = new Table();
        table3.check(LunariansI18n.get("atk_heal"), LunariansSettings.autoAttackHeal, z2 -> {
            LunariansSettings.autoAttackHeal = z2;
            LunariansSettings.save();
        }).left();
        table.add(table3).left().padBottom(4.0f).row();
        table.add(this.list).growX().row();
        rebuild();
    }

    private void rebuild() {
        this.list.clearChildren();
        Seq<AttackSlot> seq = LunariansSettings.attackSlots;
        this.list.add(seq.size + "/10").color(Pal.accent).padBottom(4.0f).row();
        if (seq.isEmpty()) {
            this.list.add(emptyMsg()).wrap().growX().padBottom(4.0f).row();
        } else {
            for (int i = 0; i < seq.size; i++) {
                this.list.add(card((AttackSlot) seq.get(i), i)).growX().padBottom(3.0f).row();
            }
        }
        Table table = new Table();
        if (seq.size < 10) {
            table.button(Icon.add, Styles.defaulti, () -> {
                seq.add(new AttackSlot());
                LunariansSettings.save();
                reopen();
            }).size(40.0f).pad(2.0f);
        }
        table.button(pasteLabel(), () -> {
            if (clipboard == null) {
                if (Vars.ui != null) {
                    Vars.ui.showInfoToast(noClip(), 2.0f);
                }
            } else {
                if (seq.size >= 10) {
                    return;
                }
                seq.add(clipboard.copy());
                LunariansSettings.save();
                if (Vars.ui != null) {
                    Vars.ui.showInfoToast(pasted(), 2.0f);
                }
                reopen();
            }
        }).width(90.0f).height(36.0f).pad(2.0f);
        this.list.add(table).left().padTop(2.0f).row();
    }

    private Table card(AttackSlot attackSlot, int i) {
        Table table = new Table();
        table.setBackground(Styles.black6);
        table.defaults().growX().pad(4.0f);
        UnitType unit = (attackSlot.unit == null || attackSlot.unit.isEmpty()) ? null : Vars.content.unit(attackSlot.unit);
        Table table2 = new Table();
        ImageButton imageButton = new ImageButton(unit != null ? new TextureRegionDrawable(unit.uiIcon) : Icon.add, Styles.clearNonei);
        imageButton.resizeImage(32.0f);
        imageButton.clicked(() -> {
            openUnitPicker(attackSlot);
        });
        table2.add(imageButton).size(40.0f).padRight(6.0f);
        table2.add(unit != null ? unit.localizedName : pickUnit()).wrap().growX().left().fontScale(0.9f);
        table2.button(copyLabel(), () -> {
            clipboard = attackSlot.copy();
            if (Vars.ui != null) {
                Vars.ui.showInfoToast(copied(), 2.0f);
            }
        }).width(64.0f).height(32.0f).padLeft(4.0f);
        table2.button(Icon.trash, Styles.defaulti, () -> {
            LunariansSettings.attackSlots.remove(i);
            LunariansSettings.save();
            reopen();
        }).size(36.0f).padLeft(4.0f);
        table.add(table2).growX().row();
        Table table3 = new Table();
        table3.add(maxLabel()).left().padRight(4.0f).fontScale(0.85f);
        table3.button("-", () -> {
            attackSlot.maxUnits = Math.max(0, attackSlot.maxUnits - 1);
            LunariansSettings.save();
            reopen();
        }).size(32.0f, 28.0f);
        table3.label(() -> {
            return attackSlot.maxUnits <= 0 ? all() : String.valueOf(attackSlot.maxUnits);
        }).width(40.0f).center().pad(2.0f);
        table3.button("+", () -> {
            attackSlot.maxUnits = Math.min(99, attackSlot.maxUnits + 1);
            LunariansSettings.save();
            reopen();
        }).size(32.0f, 28.0f);
        table.add(table3).left().padTop(2.0f).row();
        if (unit != null) {
            int maxStandoffTiles = AutoAttack.maxStandoffTiles(unit);
            Table table4 = new Table();
            table4.add(tr("Attack distance", "Dist. ataque")).left().padRight(4.0f).fontScale(0.85f);
            table4.button("-", () -> {
                if (attackSlot.standoffTiles <= 0) {
                    attackSlot.standoffTiles = maxStandoffTiles;
                } else if (attackSlot.standoffTiles <= 1) {
                    attackSlot.standoffTiles = 0;
                } else {
                    attackSlot.standoffTiles--;
                }
                LunariansSettings.save();
                reopen();
            }).size(32.0f, 28.0f);
            table4.label(() -> {
                return attackSlot.standoffTiles <= 0 ? tr("normal", "normal") : String.valueOf(Mathf.clamp(attackSlot.standoffTiles, 1, maxStandoffTiles));
            }).width(56.0f).center().pad(2.0f);
            table4.button("+", () -> {
                if (attackSlot.standoffTiles <= 0) {
                    attackSlot.standoffTiles = 1;
                } else {
                    attackSlot.standoffTiles = Math.min(maxStandoffTiles, attackSlot.standoffTiles + 1);
                }
                LunariansSettings.save();
                reopen();
            }).size(32.0f, 28.0f);
            float f = unit.maxRange / 8.0f;
            table4.add(" " + tr("1-" + maxStandoffTiles + " (range " + ((int) f) + "+2)", "1-" + maxStandoffTiles + " (alcance " + ((int) f) + "+2)")).color(Color.lightGray).fontScale(0.7f).padLeft(4.0f);
            table.add(table4).left().padTop(2.0f).row();
        }
        boolean hasZone = attackSlot.hasZone();
        Table table5 = new Table();
        table5.add(LunariansI18n.get("atk_zone", hasZone ? attackSlot.x + "," + attackSlot.y + " r" + ((int) attackSlot.zoneRadius) : LunariansI18n.get("atk_whole_map"))).wrap().growX().left().fontScale(0.85f).color(hasZone ? Color.white : Color.lightGray);
        table5.button(mapBtn(), () -> {
            if (hasZone) {
                attackSlot.x = Integer.MIN_VALUE;
                attackSlot.y = Integer.MIN_VALUE;
                attackSlot.zoneRadius = 0.0f;
            } else if (Vars.player != null && Vars.player.unit() != null) {
                attackSlot.x = Vars.player.unit().tileX();
                attackSlot.y = Vars.player.unit().tileY();
                attackSlot.zoneRadius = Math.max(10.0f, attackSlot.zoneRadius);
            }
            LunariansSettings.save();
            reopen();
        }).width(90.0f).height(32.0f).padLeft(4.0f);
        table.add(table5).growX().padTop(2.0f).row();
        if (hasZone) {
            Table table6 = new Table();
            table6.add(LunariansI18n.get("atk_zone_radius")).left().padRight(4.0f).fontScale(0.85f);
            table6.button("-", () -> {
                attackSlot.zoneRadius = Math.max(1.0f, attackSlot.zoneRadius - 5.0f);
                LunariansSettings.save();
                reopen();
            }).size(32.0f, 28.0f);
            table6.label(() -> {
                return String.valueOf((int) attackSlot.zoneRadius);
            }).width(40.0f).center().pad(2.0f);
            table6.button("+", () -> {
                attackSlot.zoneRadius = Math.min(200.0f, attackSlot.zoneRadius + 5.0f);
                LunariansSettings.save();
                reopen();
            }).size(32.0f, 28.0f);
            table.add(table6).left().row();
        }
        return table;
    }

    private void openUnitPicker(AttackSlot attackSlot) {
        BaseDialog baseDialog = new BaseDialog(pickUnit());
        baseDialog.cont.pane(table -> {
            table.defaults().size(48.0f).pad(3.0f);
            int i = 0;
            Iterator it = Vars.content.units().iterator();
            while (it.hasNext()) {
                UnitType unitType = (UnitType) it.next();
                if (unitType != null && !unitType.isHidden() && unitType.uiIcon != null) {
                    ImageButton imageButton = new ImageButton(new TextureRegionDrawable(unitType.uiIcon), Styles.clearNonei);
                    imageButton.resizeImage(40.0f);
                    imageButton.clicked(() -> {
                        attackSlot.unit = unitType.name;
                        attackSlot.standoffTiles = 0;
                        LunariansSettings.save();
                        baseDialog.hide();
                        reopen();
                    });
                    table.add(imageButton).size(48.0f);
                    i++;
                    if (i % 6 == 0) {
                        table.row();
                    }
                }
            }
        }).maxHeight(360.0f).growX();
        baseDialog.addCloseButton();
        baseDialog.show();
    }

    private static String tr(String str, String str2) {
        String language = LunariansI18n.getLanguage();
        if (!"pt".equals(language) && !"es".equals(language)) {
            return "ru".equals(language) ? str : str;
        }
        return str2;
    }

    private static String title() {
        return LunariansI18n.get("auto_attack");
    }

    private static String hint() {
        return LunariansI18n.get("atk_hint");
    }

    private static String emptyMsg() {
        return LunariansI18n.get("t_noslots");
    }

    private static String pickUnit() {
        return LunariansI18n.get("t_choose");
    }

    private static String maxLabel() {
        return LunariansI18n.get("t_max");
    }

    private static String all() {
        return LunariansI18n.get("t_all");
    }

    private static String mapBtn() {
        return LunariansI18n.get("t_map");
    }

    private static String copyLabel() {
        return LunariansI18n.get("t_copy");
    }

    private static String pasteLabel() {
        return LunariansI18n.get("t_paste");
    }

    private static String copied() {
        return LunariansI18n.get("atk_copied");
    }

    private static String pasted() {
        return LunariansI18n.get("atk_pasted");
    }

    private static String noClip() {
        return LunariansI18n.get("atk_noclip");
    }
}
