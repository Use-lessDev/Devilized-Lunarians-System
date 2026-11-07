package lunarians.ui;

import arc.graphics.Color;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.LunariansSettings;
import mindustry.Vars;
import mindustry.core.ContentLoader;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;

public class AutoConnectConveyorUi {
  public AutoConnectConveyorUi(Table paramTable) {
    String str3;
    paramTable.defaults().growX();
    String str2 = hint();
    Cell cell2 = paramTable.add(str2).wrap().growX();
    Color color1 = Color.lightGray;
    cell2 = cell2.color(color1);
    float f1 = 6.0F;
    cell2.padBottom(f1).row();
    Table table2 = new Table();
    this();
    String str4 = convL();
    table2.add(str4).left().padRight(f1);
    ContentLoader contentLoader = Vars.content;
    String str6 = LunariansSettings.conveyorBlock;
    Block block = contentLoader.block(str6);
    if (block != null) {
      str3 = block.localizedName;
    } else {
      str3 = LunariansSettings.conveyorBlock;
    } 
    Cell cell4 = table2.add(str3).wrap().growX().left();
    Color color2 = Pal.accent;
    cell4.color(color2);
    TextureRegionDrawable textureRegionDrawable = Icon.pencil;
    ImageButton.ImageButtonStyle imageButtonStyle = Styles.defaulti;
    AutoConnectConveyorUi$3 autoConnectConveyorUi$3 = new AutoConnectConveyorUi$3();
    this(this);
    Cell cell3 = table2.button((Drawable)textureRegionDrawable, imageButtonStyle, autoConnectConveyorUi$3);
    float f2 = 36.0F;
    cell3.size(f2);
    Cell cell1 = paramTable.add((Element)table2).growX();
    float f3 = 4.0F;
    cell1.pad(f3).row();
    Table table1 = new Table();
    this();
    int i = LunariansSettings.conveyorStartX;
    int j = -1 << -1;
    if (i == j) {
      str5 = unset();
    } else {
      i = LunariansSettings.conveyorStartX;
      j = LunariansSettings.conveyorStartY;
      StringBuilder stringBuilder1 = new StringBuilder();
      this();
      stringBuilder1.append(i);
      stringBuilder1.append(",");
      stringBuilder1.append(j);
      str5 = stringBuilder1.toString();
    } 
    String str7 = startL();
    StringBuilder stringBuilder = new StringBuilder();
    this();
    stringBuilder.append(str7);
    stringBuilder.append(" ");
    stringBuilder.append(str5);
    String str5 = stringBuilder.toString();
    table1.add(str5).wrap().growX().left();
    str5 = mapBtn();
    AutoConnectConveyorUi$4 autoConnectConveyorUi$4 = new AutoConnectConveyorUi$4();
    this(this);
    table1.button(str5, autoConnectConveyorUi$4).width(90.0F).height(32.0F);
    paramTable.add((Element)table1).growX().pad(f3).row();
    String str1 = runL();
    AutoConnectConveyorUi$5 autoConnectConveyorUi$5 = new AutoConnectConveyorUi$5();
    this(this);
    paramTable.button(str1, autoConnectConveyorUi$5).height(44.0F).growX().pad(f1).row();
  }
  
  private static String convL() {
    String str = LunariansSettings.language;
    "pt".equals(str);
    return "Conveyor:";
  }
  
  private static String hint() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Pré-build (fantasma) até o core/vault. Não constrói sozinho. Escolha conveyor + início." : "Pre-build ghosts to core/vault. Does not auto-build. Pick conveyor + start.";
  }
  
  public static BuilderDialog make() {
    BuilderDialog builderDialog = new BuilderDialog();
    String str = title();
    AutoConnectConveyorUi$1 autoConnectConveyorUi$1 = new AutoConnectConveyorUi$1();
    this();
    AutoConnectConveyorUi$2 autoConnectConveyorUi$2 = new AutoConnectConveyorUi$2();
    this();
    this(str, autoConnectConveyorUi$1, autoConnectConveyorUi$2);
    return builderDialog;
  }
  
  private static String mapBtn() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Mapa" : "Map";
  }
  
  private void pickConveyor() {
    BaseDialog baseDialog = new BaseDialog();
    String str = convL();
    this(str);
    Table table = new Table();
    this();
    Seq seq = Vars.content.blocks();
    int i = 0;
    ScrollPane scrollPane = null;
    while (true) {
      int j = seq.size;
      if (i < j) {
        Block block = (Block)seq.get(i);
        boolean bool = block instanceof mindustry.world.blocks.distribution.Conveyor;
        if (bool) {
          bool = block.isHidden();
          if (!bool) {
            String str1 = block.localizedName;
            AutoConnectConveyorUi$6 autoConnectConveyorUi$6 = new AutoConnectConveyorUi$6();
            this(this, block, baseDialog);
            Cell cell = table.button(str1, autoConnectConveyorUi$6).growX().height(40.0F);
            int k = 1073741824;
            float f = 2.0F;
            cell = cell.pad(f);
            cell.row();
          } 
        } 
        i++;
        continue;
      } 
      Table table1 = baseDialog.cont;
      scrollPane = new ScrollPane();
      this((Element)table);
      table1.add((Element)scrollPane).grow();
      baseDialog.addCloseButton();
      baseDialog.show();
      return;
    } 
  }
  
  private static String runL() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Gerar pré-build" : "Generate pre-build";
  }
  
  private static String startL() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Início:" : "Start:";
  }
  
  private static String title() {
    String str = LunariansSettings.language;
    "pt".equals(str);
    return "Auto connect conveyor";
  }
  
  private static String unset() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "não definido" : "unset";
  }
}
