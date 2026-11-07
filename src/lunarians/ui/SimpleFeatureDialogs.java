package lunarians.ui;

import arc.func.Boolc;
import arc.func.Floatc;
import arc.func.Prov;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import lunarians.ProductionAlert;
import mindustry.ui.Styles;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/SimpleFeatureDialogs.class */
public final class SimpleFeatureDialogs {
    private SimpleFeatureDialogs() {
    }

    /* JADX WARN: Classes with same name are omitted:
      classes.dex
     */
    /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/SimpleFeatureDialogs$Language.class */
    public static final class Language {
        public static void build(Table table, final Runnable runnable) {
            for (int i = 0; i < LunariansI18n.LANG_CODES.length; i++) {
                final String str = LunariansI18n.LANG_CODES[i];
                TextButton textButton = new TextButton(LunariansI18n.LANG_NAMES[i], Styles.togglet);
                textButton.setChecked(str.equals(LunariansSettings.language));
                textButton.clicked(new Runnable() { // from class: lunarians.ui.SimpleFeatureDialogs.Language.1
                    @Override // java.lang.Runnable
                    public void run() {
                        LunariansSettings.language = str;
                        LunariansI18n.setLanguage(str);
                        LunariansSettings.save();
                        runnable.run();
                    }
                });
                table.add(textButton).height(46.0f).growX().row();
            }
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      classes.dex
     */
    /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/SimpleFeatureDialogs$ProdAlert.class */
    public static final class ProdAlert {
        public static void build(Table table) {
            UiText.check(table, LunariansI18n.get("prod_alert"), LunariansSettings.enableProductionAlert, new Boolc() { // from class: lunarians.ui.SimpleFeatureDialogs.ProdAlert.1
                public void get(boolean z) {
                    LunariansSettings.enableProductionAlert = z;
                    if (z) {
                        ProductionAlert.reset();
                    }
                }
            }).left().row();
            UiText.check(table, LunariansI18n.get("prod_ignore_own"), LunariansSettings.productionIgnoreOwn, new Boolc() { // from class: lunarians.ui.SimpleFeatureDialogs.ProdAlert.2
                public void get(boolean z) {
                    LunariansSettings.productionIgnoreOwn = z;
                    ProductionAlert.reset();
                }
            }).left().row();
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      classes.dex
     */
    /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/SimpleFeatureDialogs$EnemyAlert.class */
    public static final class EnemyAlert {
        public static void build(Table table) {
            UiText.check(table, LunariansI18n.get("enemy_core_alert"), LunariansSettings.enableEnemyCoreAlert, new Boolc() { // from class: lunarians.ui.SimpleFeatureDialogs.EnemyAlert.1
                public void get(boolean z) {
                    LunariansSettings.enableEnemyCoreAlert = z;
                }
            }).left().row();
            UiText.slider(table, LunariansI18n.get("attack_msg_delay"), 1.0f, 60.0f, 1.0f, LunariansSettings.attackMessageDelay, new Floatc() { // from class: lunarians.ui.SimpleFeatureDialogs.EnemyAlert.2
                public void get(float f) {
                    LunariansSettings.attackMessageDelay = f;
                }
            }, new Prov<CharSequence>() { // from class: lunarians.ui.SimpleFeatureDialogs.EnemyAlert.3
                /* renamed from: get, reason: merged with bridge method [inline-methods] */
                public CharSequence m86get() {
                    return ((int) LunariansSettings.attackMessageDelay) + "s";
                }
            });
            UiText.slider(table, LunariansI18n.get("enemy_alert_range"), 5.0f, 50.0f, 1.0f, LunariansSettings.enemyAlertRange, new Floatc() { // from class: lunarians.ui.SimpleFeatureDialogs.EnemyAlert.4
                public void get(float f) {
                    LunariansSettings.enemyAlertRange = f;
                }
            }, new Prov<CharSequence>() { // from class: lunarians.ui.SimpleFeatureDialogs.EnemyAlert.5
                /* renamed from: get, reason: merged with bridge method [inline-methods] */
                public CharSequence m87get() {
                    return ((int) LunariansSettings.enemyAlertRange) + "t";
                }
            });
        }
    }
}
