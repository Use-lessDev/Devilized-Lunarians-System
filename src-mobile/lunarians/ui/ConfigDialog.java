package lunarians.ui;

import arc.Core;
import arc.Graphics;
import arc.func.Boolc;
import arc.func.Cons;
import arc.graphics.Color;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import lunarians.LunariansI18n;
import lunarians.LunariansMod;
import lunarians.LunariansSettings;
import mindustry.Vars;
import mindustry.core.UI;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public class ConfigDialog extends BaseDialog {
  private int col;
  
  private int cols;
  
  public ConfigDialog() {
    super(str1);
    Graphics graphics = Core.graphics;
    int i = graphics.getWidth();
    char c = 'ʀ';
    float f = 8.97E-43F;
    if (i >= c) {
      i = 2;
    } else {
      i = 1;
    } 
    this.cols = i;
    this.col = 0;
    Table table2 = this.cont;
    ConfigDialog$1 configDialog$1 = new ConfigDialog$1();
    this(this);
    Cell cell = table2.pane(configDialog$1).grow();
    f = Core.graphics.getHeight() * 0.78F;
    cell = cell.maxHeight(f);
    f = Math.min(Core.graphics.getWidth() * 0.96F, 900.0F);
    cell.width(f);
    this.buttons.defaults().size(150.0F, 48.0F).pad(4.0F);
    Table table1 = this.buttons;
    String str2 = LunariansI18n.get("reset_position");
    ConfigDialog$2 configDialog$2 = new ConfigDialog$2();
    this(this);
    table1.button(str2, configDialog$2);
    table1 = this.buttons;
    str2 = LunariansI18n.get("reset_all");
    ConfigDialog$3 configDialog$3 = new ConfigDialog$3();
    this(this);
    table1.button(str2, configDialog$3);
    table1 = this.buttons;
    str2 = LunariansI18n.get("back");
    ConfigDialog$4 configDialog$4 = new ConfigDialog$4();
    this(this);
    table1.button(str2, configDialog$4);
    closeOnBack();
    ConfigDialog$5 configDialog$5 = new ConfigDialog$5();
    this(this);
    hidden(configDialog$5);
  }
  
  private void autofillGear(Table paramTable) {
    String str = LunariansI18n.get("autofill_config");
    TextureRegionDrawable textureRegionDrawable = Icon.settingsSmall;
    TextButton.TextButtonStyle textButtonStyle = Styles.defaultt;
    ConfigDialog$51 configDialog$51 = new ConfigDialog$51();
    this(this);
    paramTable.button(str, (Drawable)textureRegionDrawable, textButtonStyle, configDialog$51).height(40.0F).growX().pad(2.0F);
    next(paramTable);
  }
  
  private static void betaSize(Table paramTable) {
    String str1 = LunariansI18n.get("beta_width");
    float f1 = LunariansSettings.betaWidth;
    ConfigDialog$46 configDialog$46 = new ConfigDialog$46();
    this();
    ConfigDialog$47 configDialog$47 = new ConfigDialog$47();
    this();
    UiText.slider(paramTable, str1, 180.0F, 450.0F, 10.0F, f1, configDialog$46, configDialog$47);
    String str2 = LunariansI18n.get("beta_height");
    float f2 = LunariansSettings.betaHeightPct;
    ConfigDialog$48 configDialog$48 = new ConfigDialog$48();
    this();
    ConfigDialog$49 configDialog$49 = new ConfigDialog$49();
    this();
    UiText.slider(paramTable, str2, 25.0F, 90.0F, 5.0F, f2, configDialog$48, configDialog$49);
  }
  
  private void build(Table paramTable) {
    String str2 = LunariansI18n.get("cfg_general");
    section(paramTable, str2);
    str2 = LunariansI18n.get("cfg_panel");
    ConfigDialog$6 configDialog$6 = new ConfigDialog$6();
    this(this);
    String str3;
    menu(paramTable, str2, configDialog$6);
    str2 = LunariansI18n.get("cfg_categories");
    ConfigDialog$7 configDialog$7 = new ConfigDialog$7();
    this(this);
    menu(paramTable, str2, configDialog$7);
    ConfigDialog$8 configDialog$8 = new ConfigDialog$8();
    this(this);
    menu(paramTable, "Auto Set", configDialog$8);
    String str1 = LunariansI18n.get("cfg_beta_panel");
    ConfigDialog$9 configDialog$9 = new ConfigDialog$9();
    this(this);
    menu(paramTable, str1, configDialog$9);
    str1 = LunariansI18n.get("language");
    ConfigDialog$10 configDialog$10 = new ConfigDialog$10();
    this(this);
    menu(paramTable, str1, configDialog$10);
    str1 = LunariansI18n.get("extras");
    section(paramTable, str1);
    Table table1 = new Table();
    this();
    Cell cell3 = table1.add("Modo:").left();
    float f1 = 8.0F;
    cell3.padRight(f1);
    boolean bool1 = LunariansSettings.pvpMode;
    if (bool1) {
      str3 = "Balanced ON";
    } else {
      str3 = "Normal";
    } 
    ConfigDialog$11 configDialog$11 = new ConfigDialog$11();
    this(this);
    Cell cell2 = table1.button(str3, configDialog$11).width(120.0F);
    f1 = 40.0F;
    cell2.height(f1);
    Cell cell1 = paramTable.add((Element)table1).growX();
    int i = 1082130432;
    float f2 = 4.0F;
    cell1.pad(f2);
    next(paramTable);
    boolean bool2 = LunariansSettings.pvpMode;
    if (bool2) {
      Table table = new Table();
      this();
      table.add("Balanced delay:").left().padRight(6.0F);
      ConfigDialog$12 configDialog$12 = new ConfigDialog$12();
      this(this);
      String str6 = "-";
      Cell cell7 = table.button(str6, configDialog$12);
      float f3 = 36.0F;
      cell7.size(f1, f3);
      float f4 = LunariansSettings.pvpDelay;
      Float float_ = Float.valueOf(f4);
      Object[] arrayOfObject = new Object[1];
      Object object = null;
      arrayOfObject[0] = float_;
      String str5 = String.format("%.1fs", arrayOfObject);
      Cell cell6 = table.add(str5);
      int j = 1113587712;
      float f5 = 56.0F;
      cell6.width(f5).center().pad(f2);
      ConfigDialog$13 configDialog$13 = new ConfigDialog$13();
      this(this);
      String str7 = "+";
      Cell cell5 = table.button(str7, configDialog$13);
      cell5.size(f1, f3);
      Cell cell4 = paramTable.add((Element)table).left();
      cell4.pad(f2);
      next(paramTable);
    } 
    boolean bool3 = LunariansSettings.enableAutoDrill;
    ConfigDialog$14 configDialog$14 = new ConfigDialog$14();
    this(this);
    ConfigDialog$15 configDialog$15 = new ConfigDialog$15();
    this(this);
    ConfigDialog configDialog = this;
    Table table2 = paramTable;
    feature(paramTable, "Auto drill", bool3, configDialog$14, configDialog$15);
    boolean bool4 = LunariansSettings.enableAutoConnectConveyor;
    ConfigDialog$16 configDialog$16 = new ConfigDialog$16();
    this(this);
    ConfigDialog$17 configDialog$17 = new ConfigDialog$17();
    this(this);
    feature(paramTable, "Auto connect conveyor", bool4, configDialog$16, configDialog$17);
    bool4 = LunariansSettings.enableAutoMoon;
    ConfigDialog$18 configDialog$18 = new ConfigDialog$18();
    this(this);
    ConfigDialog$19 configDialog$19 = new ConfigDialog$19();
    this(this);
    feature(paramTable, "Auto Moon", bool4, configDialog$18, configDialog$19);
    String str4 = LunariansI18n.get("enemy_core_alert");
    bool4 = LunariansSettings.enableEnemyCoreAlert;
    ConfigDialog$20 configDialog$20 = new ConfigDialog$20();
    this(this);
    ConfigDialog$21 configDialog$21 = new ConfigDialog$21();
    this(this);
    feature(paramTable, str4, bool4, configDialog$20, configDialog$21);
    str4 = LunariansI18n.get("autofill");
    bool4 = LunariansSettings.enableAutoFill;
    ConfigDialog$22 configDialog$22 = new ConfigDialog$22();
    this(this);
    feature(paramTable, str4, bool4, configDialog$22, (ConfigDialog$Maker)null);
    autofillGear(paramTable);
    str4 = LunariansI18n.get("freecam");
    bool4 = LunariansSettings.enableFreeCam;
    ConfigDialog$23 configDialog$23 = new ConfigDialog$23();
    this(this);
    feature(paramTable, str4, bool4, configDialog$23, (ConfigDialog$Maker)null);
    str4 = LunariansI18n.get("prod_alert");
    bool4 = LunariansSettings.enableProductionAlert;
    ConfigDialog$24 configDialog$24 = new ConfigDialog$24();
    this(this);
    ConfigDialog$25 configDialog$25 = new ConfigDialog$25();
    this(this);
    feature(paramTable, str4, bool4, configDialog$24, configDialog$25);
    str4 = LunariansI18n.get("camfollow");
    bool4 = LunariansSettings.enableCamFollow;
    ConfigDialog$26 configDialog$26 = new ConfigDialog$26();
    this(this);
    feature(paramTable, str4, bool4, configDialog$26, (ConfigDialog$Maker)null);
    str4 = LunariansI18n.get("loss");
    bool4 = LunariansSettings.enableLossWarn;
    ConfigDialog$27 configDialog$27 = new ConfigDialog$27();
    this(this);
    ConfigDialog$28 configDialog$28 = new ConfigDialog$28();
    this(this);
    feature(paramTable, str4, bool4, configDialog$27, configDialog$28);
    str4 = LunariansI18n.get("autounit");
    bool4 = LunariansSettings.enableAutoUnit;
    ConfigDialog$29 configDialog$29 = new ConfigDialog$29();
    this(this);
    ConfigDialog$30 configDialog$30 = new ConfigDialog$30();
    this(this);
    feature(paramTable, str4, bool4, configDialog$29, configDialog$30);
    bool4 = LunariansSettings.enableAutoUnitHeal;
    ConfigDialog$31 configDialog$31 = new ConfigDialog$31();
    this(this);
    ConfigDialog$32 configDialog$32 = new ConfigDialog$32();
    this(this);
    feature(paramTable, "Auto unit heal", bool4, configDialog$31, configDialog$32);
    str4 = LunariansI18n.get("autocargo");
    bool4 = LunariansSettings.enableAutoCargo;
    ConfigDialog$33 configDialog$33 = new ConfigDialog$33();
    this(this);
    ConfigDialog$34 configDialog$34 = new ConfigDialog$34();
    this(this);
    feature(paramTable, str4, bool4, configDialog$33, configDialog$34);
    str4 = AutoContainerUi.title();
    bool4 = LunariansSettings.enableAutoContainer;
    ConfigDialog$35 configDialog$35 = new ConfigDialog$35();
    this(this);
    ConfigDialog$36 configDialog$36 = new ConfigDialog$36();
    this(this);
    feature(paramTable, str4, bool4, configDialog$35, configDialog$36);
    bool4 = LunariansSettings.enableAutoDefence;
    ConfigDialog$37 configDialog$37 = new ConfigDialog$37();
    this(this);
    ConfigDialog$38 configDialog$38 = new ConfigDialog$38();
    this(this);
    feature(paramTable, "Auto defesa", bool4, configDialog$37, configDialog$38);
    bool4 = LunariansSettings.enableAutoShield;
    ConfigDialog$39 configDialog$39 = new ConfigDialog$39();
    this(this);
    ConfigDialog$40 configDialog$40 = new ConfigDialog$40();
    this(this);
    feature(paramTable, "Auto shield", bool4, configDialog$39, configDialog$40);
    bool4 = LunariansSettings.enableAutoHeal;
    ConfigDialog$41 configDialog$41 = new ConfigDialog$41();
    this(this);
    ConfigDialog$42 configDialog$42 = new ConfigDialog$42();
    this(this);
    feature(paramTable, "Auto heal", bool4, configDialog$41, configDialog$42);
    bool4 = LunariansSettings.enableAutoAttack;
    ConfigDialog$43 configDialog$43 = new ConfigDialog$43();
    this(this);
    ConfigDialog$44 configDialog$44 = new ConfigDialog$44();
    this(this);
    feature(paramTable, "Auto attack", bool4, configDialog$43, configDialog$44);
    str4 = LunariansI18n.get("schem_ui");
    bool4 = LunariansSettings.showSchematicPanel;
    ConfigDialog$45 configDialog$45 = new ConfigDialog$45();
    this(this);
    feature(paramTable, str4, bool4, configDialog$45, (ConfigDialog$Maker)null);
  }
  
  private void confirmResetAll() {
    UI uI = Vars.ui;
    StringBuilder stringBuilder = new StringBuilder();
    this();
    String str2 = LunariansI18n.get("reset_all");
    stringBuilder.append(str2);
    stringBuilder.append("?");
    String str1 = stringBuilder.toString();
    ConfigDialog$55 configDialog$55 = new ConfigDialog$55();
    this(this);
    uI.showConfirm(str1, configDialog$55);
  }
  
  private static BuilderDialog dlg(String paramString, Cons paramCons, Runnable paramRunnable) {
    BuilderDialog builderDialog = new BuilderDialog();
    this(paramString, paramCons, paramRunnable);
    return builderDialog;
  }
  
  private void feature(Table paramTable, String paramString, boolean paramBoolean, Boolc paramBoolc, ConfigDialog$Maker paramConfigDialog$Maker) {
    Table table = new Table();
    this();
    ConfigDialog$52 configDialog$52 = new ConfigDialog$52();
    this(this, paramBoolc);
    Cell cell = UiText.check(table, paramString, paramBoolean, configDialog$52).left();
    cell.growX();
    if (paramConfigDialog$Maker != null) {
      TextureRegionDrawable textureRegionDrawable = Icon.settingsSmall;
      ImageButton.ImageButtonStyle imageButtonStyle = Styles.defaulti;
      ConfigDialog$53 configDialog$53 = new ConfigDialog$53();
      this(this, paramConfigDialog$Maker);
      Cell cell1 = table.button((Drawable)textureRegionDrawable, imageButtonStyle, configDialog$53).size(44.0F);
      int i = 1086324736;
      float f = 6.0F;
      cell1.padLeft(f);
    } 
    paramTable.add((Element)table).growX().pad(2.0F);
    next(paramTable);
  }
  
  private void menu(Table paramTable, String paramString, ConfigDialog$Maker paramConfigDialog$Maker) {
    ConfigDialog$50 configDialog$50 = new ConfigDialog$50();
    this(this, paramConfigDialog$Maker);
    paramTable.button(paramString, configDialog$50).height(44.0F).growX().pad(2.0F);
    next(paramTable);
  }
  
  private void next(Table paramTable) {
    int i = this.col + 1;
    this.col = i;
    int j = this.cols;
    if (i >= j) {
      i = 0;
      this.col = 0;
      paramTable.row();
    } 
  }
  
  private void open(BuilderDialog paramBuilderDialog) {
    hide();
    ConfigDialog$54 configDialog$54 = new ConfigDialog$54();
    this(this);
    paramBuilderDialog.after(configDialog$54);
    paramBuilderDialog.show();
  }
  
  private static void resetPosition() {
    LunariansSettings.panelX = 20.0F;
    float f = 100.0F;
    LunariansSettings.panelY = f;
    LunariansSettings.save();
    SchematicPanel schematicPanel = LunariansMod.panel;
    if (schematicPanel != null) {
      schematicPanel = LunariansMod.panel;
      schematicPanel.applySettings();
    } 
    UI uI = Vars.ui;
    String str = LunariansI18n.get("position_reset");
    uI.showInfoToast(str, 2.0F);
  }
  
  private void section(Table paramTable, String paramString) {
    int i = this.col;
    if (i != 0) {
      paramTable.row();
      i = 0;
      Object object = null;
      this.col = 0;
    } 
    Cell cell2 = paramTable.add(paramString);
    Color color2 = Pal.accent;
    cell2 = cell2.color(color2).left().wrap().growX().padTop(8.0F).padBottom(4.0F);
    i = this.cols;
    cell2.colspan(i).row();
    Cell cell1 = paramTable.image();
    Color color1 = Pal.accent;
    cell1 = cell1.color(color1).height(2.0F).growX().padBottom(6.0F);
    int j = this.cols;
    cell1.colspan(j).row();
  }
}
