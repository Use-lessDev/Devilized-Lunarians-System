/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.func.Boolc
 *  arc.func.Floatc
 *  arc.func.Prov
 *  arc.scene.Element
 *  arc.scene.ui.TextButton
 *  arc.scene.ui.layout.Table
 *  mindustry.ui.Styles
 */
package lunarians.ui;

import arc.func.Boolc;
import arc.func.Floatc;
import arc.func.Prov;
import arc.scene.Element;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import lunarians.ProductionAlert;
import lunarians.ui.UiText;
import mindustry.ui.Styles;

public final class SimpleFeatureDialogs {
    private SimpleFeatureDialogs() {
    }

    public static final class EnemyAlert {
        public static void build(Table table) {
            UiText.check(table, LunariansI18n.get("enemy_core_alert"), LunariansSettings.enableEnemyCoreAlert, new Boolc(){

                public void get(boolean bl) {
                    LunariansSettings.enableEnemyCoreAlert = bl;
                }
            }).left().row();
            UiText.slider(table, LunariansI18n.get("attack_msg_delay"), 1.0f, 60.0f, 1.0f, LunariansSettings.attackMessageDelay, new Floatc(){

                public void get(float f) {
                    LunariansSettings.attackMessageDelay = f;
                }
            }, new Prov<CharSequence>(){

                public CharSequence get() {
                    return (int)LunariansSettings.attackMessageDelay + "s";
                }
            });
            UiText.slider(table, LunariansI18n.get("enemy_alert_range"), 5.0f, 50.0f, 1.0f, LunariansSettings.enemyAlertRange, new Floatc(){

                public void get(float f) {
                    LunariansSettings.enemyAlertRange = f;
                }
            }, new Prov<CharSequence>(){

                public CharSequence get() {
                    return (int)LunariansSettings.enemyAlertRange + "t";
                }
            });
        }
    }

    public static final class ProdAlert {
        public static void build(Table table) {
            UiText.check(table, LunariansI18n.get("prod_alert"), LunariansSettings.enableProductionAlert, new Boolc(){

                public void get(boolean bl) {
                    LunariansSettings.enableProductionAlert = bl;
                    if (bl) {
                        ProductionAlert.reset();
                    }
                }
            }).left().row();
            UiText.check(table, LunariansI18n.get("prod_ignore_own"), LunariansSettings.productionIgnoreOwn, new Boolc(){

                public void get(boolean bl) {
                    LunariansSettings.productionIgnoreOwn = bl;
                    ProductionAlert.reset();
                }
            }).left().row();
        }
    }

    public static final class Language {
        public static void build(Table table, final Runnable runnable) {
            for (int i = 0; i < LunariansI18n.LANG_CODES.length; ++i) {
                final String string = LunariansI18n.LANG_CODES[i];
                TextButton textButton = new TextButton(LunariansI18n.LANG_NAMES[i], Styles.togglet);
                textButton.setChecked(string.equals(LunariansSettings.language));
                textButton.clicked(new Runnable(){

                    @Override
                    public void run() {
                        LunariansSettings.language = string;
                        LunariansI18n.setLanguage(string);
                        LunariansSettings.save();
                        runnable.run();
                    }
                });
                table.add((Element)textButton).height(46.0f).growX().row();
            }
        }
    }
}

