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
import mindustry.ui.Styles;

public final class SimpleFeatureDialogs {
  public static final class Language {
    public static void build(Table param1Table, final Runnable runnable) {
      for (byte b = 0; b < LunariansI18n.LANG_CODES.length; b++) {
        final String string = LunariansI18n.LANG_CODES[b];
        TextButton textButton = new TextButton(LunariansI18n.LANG_NAMES[b], Styles.togglet);
        textButton.setChecked(str.equals(LunariansSettings.language));
        textButton.clicked(new Runnable() {
              public void run() {
                LunariansSettings.language = string;
                LunariansI18n.setLanguage(string);
                LunariansSettings.save();
                runnable.run();
              }
            });
        param1Table.add((Element)textButton).height(46.0F).growX().row();
      } 
    }
  }
  
  public static final class ProdAlert {
    public static void build(Table param1Table) {
      UiText.check(param1Table, LunariansI18n.get("prod_alert"), LunariansSettings.enableProductionAlert, new Boolc() {
            public void get(boolean param2Boolean) {
              LunariansSettings.enableProductionAlert = param2Boolean;
              if (param2Boolean)
                ProductionAlert.reset(); 
            }
          }).left().row();
      UiText.check(param1Table, LunariansI18n.get("prod_ignore_own"), LunariansSettings.productionIgnoreOwn, new Boolc() {
            public void get(boolean param2Boolean) {
              LunariansSettings.productionIgnoreOwn = param2Boolean;
              ProductionAlert.reset();
            }
          }).left().row();
    }
  }
  
  public static final class EnemyAlert {
    public static void build(Table param1Table) {
      UiText.check(param1Table, LunariansI18n.get("enemy_core_alert"), LunariansSettings.enableEnemyCoreAlert, new Boolc() {
            public void get(boolean param2Boolean) {
              LunariansSettings.enableEnemyCoreAlert = param2Boolean;
            }
          }).left().row();
      UiText.slider(param1Table, LunariansI18n.get("attack_msg_delay"), 1.0F, 60.0F, 1.0F, LunariansSettings.attackMessageDelay, new Floatc() {
            public void get(float param2Float) {
              LunariansSettings.attackMessageDelay = param2Float;
            }
          },  new Prov<CharSequence>() {
            public CharSequence get() {
              return (int)LunariansSettings.attackMessageDelay + "s";
            }
          });
      UiText.slider(param1Table, LunariansI18n.get("enemy_alert_range"), 5.0F, 50.0F, 1.0F, LunariansSettings.enemyAlertRange, new Floatc() {
            public void get(float param2Float) {
              LunariansSettings.enemyAlertRange = param2Float;
            }
          },  new Prov<CharSequence>() {
            public CharSequence get() {
              return (int)LunariansSettings.enemyAlertRange + "t";
            }
          });
    }
  }
}
