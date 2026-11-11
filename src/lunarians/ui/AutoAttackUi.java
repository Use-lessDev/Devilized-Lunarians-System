/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.func.Cons
 *  arc.graphics.Color
 *  arc.math.Mathf
 *  arc.scene.Element
 *  arc.scene.style.Drawable
 *  arc.scene.style.TextureRegionDrawable
 *  arc.scene.ui.Dialog
 *  arc.scene.ui.ImageButton
 *  arc.scene.ui.layout.Table
 *  arc.struct.Seq
 *  mindustry.Vars
 *  mindustry.gen.Icon
 *  mindustry.graphics.Pal
 *  mindustry.type.UnitType
 *  mindustry.ui.Styles
 *  mindustry.ui.dialogs.BaseDialog
 */
package lunarians.ui;

import arc.Core;
import arc.func.Cons;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Dialog;
import arc.scene.ui.ImageButton;
import arc.scene.ui.Label;
import arc.scene.ui.Slider;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.AutoAttack;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import lunarians.data.AttackSlot;
import lunarians.ui.BuilderDialog;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public class AutoAttackUi {
    private final Table list = new Table();
    private static AttackSlot clipboard = null;
    private static BuilderDialog currentDlg;

    public static BuilderDialog make() {
        currentDlg = AutoAttackUi.make0();
        return currentDlg;
    }

    public static BuilderDialog make0() {
        return new BuilderDialog(AutoAttackUi.title(), (Cons<Table>)((Cons)table -> new AutoAttackUi((Table)table)), () -> {});
    }

    private static void reopen() {
        if (currentDlg != null) {
            currentDlg.hide();
        }
        currentDlg = AutoAttackUi.make();
        Dialog dialog = currentDlg.show();
    }

    public AutoAttackUi(Table table) {
        table.defaults().growX();
        table.add((CharSequence)AutoAttackUi.hint()).wrap().growX().color(Color.lightGray).padBottom(4.0f).row();
        Table table2 = new Table();
        table2.check(AutoAttackUi.tr("Regroup", "Reagrupar"), AutoAttack.regroupEnabled(), bl -> AutoAttack.setRegroupEnabled(bl)).left();
        table2.add((CharSequence)(" " + AutoAttackUi.tr("every", "a cada") + " ")).fontScale(0.85f);
        table2.button("-", () -> {
            AutoAttack.setRegroupSec(AutoAttack.regroupSec() - 1);
            AutoAttackUi.reopen();
        }).size(32.0f, 28.0f);
        table2.label(() -> AutoAttack.regroupSec() + "s").width(40.0f).center();
        table2.button("+", () -> {
            AutoAttack.setRegroupSec(AutoAttack.regroupSec() + 1);
            AutoAttackUi.reopen();
        }).size(32.0f, 28.0f);
        table2.add((CharSequence)(" " + AutoAttackUi.tr("(min 5)", "(min 5)"))).color(Color.lightGray).fontScale(0.75f);
        table.add((Element)table2).left().padBottom(6.0f).row();
        Table table3 = new Table();
        table3.check(LunariansI18n.get("atk_heal"), LunariansSettings.autoAttackHeal, bl -> {
            LunariansSettings.autoAttackHeal = bl;
            LunariansSettings.save();
        }).left();
        table.add((Element)table3).left().padBottom(2.0f).row();
        Table table3b = new Table();
        table3b.check(AutoAttackUi.tr("Smart path (fewer turrets, safer route to core)", "Rota inteligente (menos torres, rota segura ate o nucleo)"), AutoAttack.smartPath(), bl -> {
            LunariansSettings.smartPath = bl;
            LunariansSettings.save();
        }).left();
        table.add((Element)table3b).left().padBottom(4.0f).row();
        Table table3c = new Table();
        table3c.check(AutoAttackUi.tr("Enable projectile dodge", "Ativar desvio de projeteis"), AutoAttack.enableAutoDodge(), bl -> {
            LunariansSettings.enableAutoDodge = bl;
            LunariansSettings.save();
        }).left();
        table.add((Element)table3c).left().padBottom(2.0f).row();
        Table table3d = new Table();
        table3d.add(AutoAttackUi.tr("Dodge sensitivity", "Sensibilidade do desvio")).width(120.0f).left().wrap();
        Slider dodgeSlider = new Slider(0.1f, 3.0f, 0.1f, false);
        dodgeSlider.setValue(AutoAttack.autoDodgeSensitivity());
        Label dodgeLabel = new Label(() -> String.format("%.1f", AutoAttack.autoDodgeSensitivity()));
        dodgeSlider.moved(f -> {
            LunariansSettings.autoDodgeSensitivity = f * 10f;
            LunariansSettings.save();
            dodgeLabel.setText(String.format("%.1f", f));
        });
        table3d.add((Element)dodgeSlider).width(160.0f);
        table3d.add((Element)dodgeLabel).width(50.0f).padLeft(8.0f);
        table.add((Element)table3d).left().padBottom(2.0f).row();
        Table table3e = new Table();
        table3e.check(AutoAttackUi.tr("Enable focus fire (coordinated target)", "Ativar foco coordenado (alvo compartilhado)"), AutoAttack.enableFocusFire(), bl -> {
            LunariansSettings.enableFocusFire = bl;
            LunariansSettings.save();
        }).left();
        table.add((Element)table3e).left().padBottom(2.0f).row();
        Table table3f = new Table();
        table3f.add(AutoAttackUi.tr("Focus fire range", "Alcance do foco coordenado")).width(120.0f).left().wrap();
        Slider focusSlider = new Slider(200.0f, 1500.0f, 50.0f, false);
        focusSlider.setValue(AutoAttack.focusFireRange());
        Label focusLabel = new Label(() -> String.format("%.0f", AutoAttack.focusFireRange()));
        focusSlider.moved(f -> {
            LunariansSettings.focusFireRange = f;
            LunariansSettings.save();
            focusLabel.setText(String.format("%.0f", f));
        });
        table3f.add((Element)focusSlider).width(160.0f);
        table3f.add((Element)focusLabel).width(60.0f).padLeft(8.0f);
        table.add((Element)table3f).left().padBottom(2.0f).row();
        Table table3g = new Table();
        table3g.check(AutoAttackUi.tr("Debug visual (colored unit squares)", "Visual debug (quadrados coloridos nas unidades)"), AutoAttack.enableDebugVisual(), bl -> {
            LunariansSettings.enableDebugVisual = bl;
            LunariansSettings.save();
        }).left();
        table.add((Element)table3g).left().padBottom(4.0f).row();
        table.add((Element)this.list).left().growX().row();
        this.rebuild();
    }

    private void rebuild() {
        this.list.clearChildren();
        Seq<AttackSlot> seq = LunariansSettings.attackSlots;
        this.list.add((CharSequence)(seq.size + "/10")).color(Pal.accent).padBottom(4.0f).row();
        if (seq.isEmpty()) {
            this.list.add((CharSequence)AutoAttackUi.emptyMsg()).wrap().growX().padBottom(4.0f).row();
        } else {
            for (int i = 0; i < seq.size; ++i) {
                this.list.add((Element)this.card((AttackSlot)seq.get(i), i)).growX().padBottom(3.0f).row();
            }
        }
        Table table = new Table();
        if (seq.size < 10) {
            table.button(AutoAttackUi.tr("+ Add Unit Slot", "+ Adicionar Slot"), () -> {
                seq.add(new AttackSlot());
                LunariansSettings.save();
                AutoAttackUi.reopen();
            }).height(44.0f).pad(4.0f).fillX().uniformX();
        }
        table.button(AutoAttackUi.pasteLabel(), () -> {
            if (clipboard == null) {
                if (Vars.ui != null) {
                    Vars.ui.showInfoToast(AutoAttackUi.noClip(), 2.0f);
                }
                return;
            }
            if (seq.size >= 10) {
                return;
            }
            seq.add(clipboard.copy());
            LunariansSettings.save();
            if (Vars.ui != null) {
                Vars.ui.showInfoToast(AutoAttackUi.pasted(), 2.0f);
            }
            AutoAttackUi.reopen();
        }).width(90.0f).height(36.0f).pad(2.0f);
        this.list.add((Element)table).left().padTop(2.0f).row();
    }

    private Table card(AttackSlot attackSlot, int n) {
        Table table;
        int n2;
        Table table2 = new Table();
        table2.setBackground(Styles.black6);
        table2.defaults().growX().pad(4.0f);
        UnitType unitType = attackSlot.unit == null || attackSlot.unit.isEmpty() ? null : Vars.content.unit(attackSlot.unit);
        Table table3 = new Table();
        ImageButton imageButton = new ImageButton((Drawable)(unitType != null ? new TextureRegionDrawable(unitType.uiIcon) : Icon.add), Styles.clearNonei);
        imageButton.resizeImage(32.0f);
        imageButton.clicked(() -> this.openUnitPicker(attackSlot));
        table3.add((Element)imageButton).size(40.0f).padRight(6.0f);
        table3.add((CharSequence)(unitType != null ? unitType.localizedName : AutoAttackUi.pickUnit())).wrap().growX().left().fontScale(0.9f);
        table3.button(AutoAttackUi.copyLabel(), () -> {
            clipboard = attackSlot.copy();
            if (Vars.ui != null) {
                Vars.ui.showInfoToast(AutoAttackUi.copied(), 2.0f);
            }
        }).width(64.0f).height(32.0f).padLeft(4.0f);
        table3.button((Drawable)Icon.trash, Styles.defaulti, () -> {
            LunariansSettings.attackSlots.remove(n);
            LunariansSettings.save();
            AutoAttackUi.reopen();
        }).size(36.0f).padLeft(4.0f);
        table2.add((Element)table3).growX().row();
        Table table4 = new Table();
        table4.add((CharSequence)AutoAttackUi.maxLabel()).left().padRight(4.0f).fontScale(0.85f);
        table4.button("-", () -> {
            attackSlot.maxUnits = Math.max(0, attackSlot.maxUnits - 1);
            LunariansSettings.save();
            AutoAttackUi.reopen();
        }).size(32.0f, 28.0f);
        table4.label(() -> attackSlot.maxUnits <= 0 ? AutoAttackUi.all() : String.valueOf(attackSlot.maxUnits)).width(40.0f).center().pad(2.0f);
        table4.button("+", () -> {
            attackSlot.maxUnits = Math.min(99, attackSlot.maxUnits + 1);
            LunariansSettings.save();
            AutoAttackUi.reopen();
        }).size(32.0f, 28.0f);
        table2.add((Element)table4).left().padTop(2.0f).row();
        if (unitType != null) {
            final int maxStandoff = AutoAttack.maxStandoffTilesType(unitType);
            table = new Table();
            table.add((CharSequence)AutoAttackUi.tr("Attack distance", "Dist. ataque")).left().padRight(4.0f).fontScale(0.85f);
            table.button("-", () -> {
                attackSlot.standoffTiles = attackSlot.standoffTiles <= 0 ? maxStandoff : (attackSlot.standoffTiles <= 1 ? 0 : attackSlot.standoffTiles - 1);
                LunariansSettings.save();
                AutoAttackUi.reopen();
            }).size(32.0f, 28.0f);
            table.label(() -> {
                if (attackSlot.standoffTiles <= 0) {
                    return AutoAttackUi.tr("normal", "normal");
                }
                return String.valueOf(Mathf.clamp((int)attackSlot.standoffTiles, (int)1, (int)maxStandoff));
            }).width(56.0f).center().pad(2.0f);
            table.button("+", () -> {
                attackSlot.standoffTiles = attackSlot.standoffTiles <= 0 ? 1 : Math.min(maxStandoff, attackSlot.standoffTiles + 1);
                LunariansSettings.save();
                AutoAttackUi.reopen();
            }).size(32.0f, 28.0f);
            float f = unitType.maxRange / 8.0f;
            table.add((CharSequence)(" " + AutoAttackUi.tr("1-" + maxStandoff + " (range " + (int)f + "+2)", "1-" + maxStandoff + " (alcance " + (int)f + "+2)"))).color(Color.lightGray).fontScale(0.7f).padLeft(4.0f);
            table2.add((Element)table).left().padTop(2.0f).row();
        }
        // Retreat percent per slot
        Table tableRetreat = new Table();
        tableRetreat.add((CharSequence)AutoAttackUi.tr("Retreat % HP (0=global)", "Recuar % HP (0=global)")).left().padRight(4.0f).fontScale(0.85f);
        tableRetreat.button("-", () -> {
            attackSlot.retreatPercent = Math.max(0, attackSlot.retreatPercent - 5);
            LunariansSettings.save();
            AutoAttackUi.reopen();
        }).size(32.0f, 28.0f);
        tableRetreat.label(() -> attackSlot.retreatPercent <= 0 ? AutoAttackUi.tr("global", "global") : attackSlot.retreatPercent + "%").width(48.0f).center().pad(2.0f);
        tableRetreat.button("+", () -> {
            attackSlot.retreatPercent = Math.min(90, attackSlot.retreatPercent + 5);
            LunariansSettings.save();
            AutoAttackUi.reopen();
        }).size(32.0f, 28.0f);
        table2.add((Element)tableRetreat).left().padTop(2.0f).row();
        final boolean hasZone = attackSlot.hasZone();
        table = new Table();
        table.add((CharSequence)LunariansI18n.get("atk_zone", hasZone ? attackSlot.x + "," + attackSlot.y + " r" + (int)attackSlot.zoneRadius : LunariansI18n.get("atk_whole_map"))).wrap().growX().left().fontScale(0.85f).color(hasZone ? Color.white : Color.lightGray);
        table.button(AutoAttackUi.mapBtn(), () -> AutoAttackUi.lambda$card$18(hasZone, attackSlot)).width(90.0f).height(32.0f).padLeft(4.0f);
        table2.add((Element)table).growX().padTop(2.0f).row();
        if (hasZone) {
            Table table5 = new Table();
            table5.add((CharSequence)LunariansI18n.get("atk_zone_radius")).left().padRight(4.0f).fontScale(0.85f);
            table5.button("-", () -> {
                attackSlot.zoneRadius = Math.max(1.0f, attackSlot.zoneRadius - 5.0f);
                LunariansSettings.save();
                AutoAttackUi.reopen();
            }).size(32.0f, 28.0f);
            table5.label(() -> String.valueOf((int)attackSlot.zoneRadius)).width(40.0f).center().pad(2.0f);
            table5.button("+", () -> {
                attackSlot.zoneRadius = Math.min(200.0f, attackSlot.zoneRadius + 5.0f);
                LunariansSettings.save();
                AutoAttackUi.reopen();
            }).size(32.0f, 28.0f);
            table2.add((Element)table5).left().row();
        }
        return table2;
    }

    private void openUnitPicker(AttackSlot attackSlot) {
        BaseDialog baseDialog = new BaseDialog(AutoAttackUi.pickUnit());
        baseDialog.cont.pane(table -> {
            table.defaults().size(48.0f).pad(3.0f);
            int n = 0;
            for (UnitType unitType : Vars.content.units()) {
                if (unitType == null || unitType.isHidden() || unitType.uiIcon == null) continue;
                ImageButton imageButton = new ImageButton((Drawable)new TextureRegionDrawable(unitType.uiIcon), Styles.clearNonei);
                imageButton.resizeImage(40.0f);
                imageButton.clicked(() -> {
                    attackSlot.unit = unitType.name;
                    attackSlot.standoffTiles = 0;
                    LunariansSettings.save();
                    baseDialog.hide();
                    AutoAttackUi.reopen();
                });
                table.add((Element)imageButton).size(48.0f);
                if (++n % 6 != 0) continue;
                table.row();
            }
        }).maxHeight(360.0f).growX();
        baseDialog.addCloseButton();
        baseDialog.show();
    }

    private static String tr(String string, String string2) {
        String string3 = LunariansI18n.getLanguage();
        if ("pt".equals(string3)) {
            return string2;
        }
        if ("es".equals(string3)) {
            return string2;
        }
        if ("ru".equals(string3)) {
            return string;
        }
        return string;
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

    private static /* synthetic */ void lambda$card$18(boolean bl, AttackSlot attackSlot) {
        if (bl) {
            attackSlot.x = Integer.MIN_VALUE;
            attackSlot.y = Integer.MIN_VALUE;
            attackSlot.zoneRadius = 0.0f;
        } else if (Vars.player != null && Vars.player.unit() != null) {
            attackSlot.x = Vars.player.unit().tileX();
            attackSlot.y = Vars.player.unit().tileY();
            attackSlot.zoneRadius = Math.max(10.0f, attackSlot.zoneRadius);
        }
        LunariansSettings.save();
        AutoAttackUi.reopen();
    }
}

