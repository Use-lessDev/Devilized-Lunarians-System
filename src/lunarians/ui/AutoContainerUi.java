package lunarians.ui;

import arc.func.Prov;
import arc.graphics.Color;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Table;
import arc.struct.IntSeq;
import lunarians.AutoContainer;
import lunarians.LunariansSettings;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

public class AutoContainerUi {
  private final Table box = new Table();
  
  public static BuilderDialog make() {
    return new BuilderDialog(title(), paramTable -> new AutoContainerUi(paramTable), AutoContainer::reset);
  }
  
  private static void reopen() {
    make().after(() -> (new ConfigDialog()).show()).show();
  }
  
  public static String tr(String paramString1, String paramString2, String paramString3, String paramString4) {
    String str = LunariansSettings.language;
    if ("pt".equals(str))
      return paramString2; 
    if ("es".equals(str))
      return paramString3; 
    if ("ru".equals(str))
      return paramString4; 
    return paramString1;
  }
  
  public static String title() {
    return tr("Auto container (Beta)", "Auto container (Beta)", "Auto contenedor (Beta)", "Авто-контейнер (Бета)");
  }
  
  private static boolean canCarryBlocks(UnitType paramUnitType) {
    return (paramUnitType.payloadCapacity > 0.0F && paramUnitType.commands != null && paramUnitType.commands
      .contains(UnitCommand.loadBlocksCommand) && paramUnitType.commands
      .contains(UnitCommand.unloadPayloadCommand));
  }
  
  public AutoContainerUi(Table paramTable) {
    paramTable.defaults().growX();
    paramTable.add(tr("The carrier unit picks up the container at point A, places it at point B, waits, picks it up again, returns it to A and waits again. Then it repeats. Point A must already have the container. Needs the unit in command mode.", "A unidade de carga pega o container no ponto A, coloca no ponto B, espera, pega de novo, devolve em A e espera mais um pouco. Depois repete. O ponto A precisa já ter o container. Precisa da unidade em modo comando.", "La unidad de carga recoge el contenedor en el punto A, lo coloca en el punto B, espera, lo recoge de nuevo, lo devuelve a A y espera otra vez. Luego repite. El punto A ya debe tener el contenedor. Requiere la unidad en modo comando.", "Юнит берёт контейнер в точке A, ставит в точке B, ждёт, берёт снова, возвращает в A и снова ждёт. Затем повтор. В точке A уже должен стоять контейнер. Нужен режим командования."))



      
      .wrap().growX().color(Color.lightGray).padBottom(6.0F).row();
    paramTable.add((Element)this.box).growX().row();
    rebuild();
  }
  
  private void rebuild() {
    String str;
    this.box.clearChildren();
    this.box.defaults().growX();
    UnitType unitType = LunariansSettings.containerUnit.isEmpty() ? null : Vars.content.unit(LunariansSettings.containerUnit);
    Table table1 = new Table(Styles.black6);
    table1.defaults().pad(4.0F);
    ImageButton imageButton = new ImageButton((unitType != null) ? (Drawable)new TextureRegionDrawable(unitType.uiIcon) : (Drawable)Icon.add, Styles.defaulti);
    imageButton.clicked(() -> (new UnitPickerDialog((), AutoContainerUi::canCarryBlocks)).show());
    table1.add((Element)imageButton).size(40.0F).padRight(6.0F);
    table1.add((unitType != null) ? unitType.localizedName : tr("Choose carrier unit", "Escolher unidade de carga", "Elegir unidad de carga", "Выбрать юнит"))
      .wrap().growX().left();
    table1.button((Drawable)Icon.trash, Styles.defaulti, () -> {
          LunariansSettings.containerUnit = "";
          LunariansSettings.save();
          AutoContainer.reset();
          rebuild();
        }).size(36.0F);
    this.box.add((Element)table1).growX().padBottom(4.0F).row();
    stepper(tr("Max units:", "Máx unidades:", "Máx unidades:", "Макс. юнитов:"), () -> (LunariansSettings.containerMaxUnits <= 0) ? tr("All", "Todas", "Todas", "Все") : String.valueOf(LunariansSettings.containerMaxUnits), () -> {
          if (LunariansSettings.containerMaxUnits > 0)
            LunariansSettings.containerMaxUnits--; 
        }() -> {
          if (LunariansSettings.containerMaxUnits < 10)
            LunariansSettings.containerMaxUnits++; 
        });
    boolean bool = AutoContainer.hasPoints();
    if (!bool) {
      str = tr("Points not set. Press \"Map points\": 1st = where the container is (A), 2nd = where it will stay (B).", "Pontos não definidos. Aperte \"Pontos mapa\": 1º = onde está o container (A), 2º = onde ele vai ficar (B).", "Puntos sin definir. Pulsa \"Puntos mapa\": 1.º = donde está el contenedor (A), 2.º = donde se quedará (B).", "Точки не заданы. Нажмите «Точки»: 1-я = где контейнер (A), 2-я = где он останется (B).");
    } else {
      str = "A (" + LunariansSettings.containerPickX + "," + LunariansSettings.containerPickY + ")  →  B (" + LunariansSettings.containerDropX + "," + LunariansSettings.containerDropY + ")";
    } 
    this.box.add(str).wrap().growX().left().color(bool ? Color.white : Color.lightGray).padTop(4.0F).row();
    Table table2 = new Table();
    table2.button(tr("Map points", "Pontos mapa", "Puntos mapa", "Точки"), () -> {
          FeatureDialog featureDialog = FeatureDialog.top;
          if (featureDialog != null) {
            featureDialog.after((Runnable)null);
            featureDialog.hide();
          } 
          PathPicker.pick((), AutoContainerUi::reopen);
        }).width(130.0F).height(36.0F);
    if (bool)
      table2.button((Drawable)Icon.trash, Styles.defaulti, () -> {
            AutoContainer.clearPoints();
            rebuild();
          }).size(36.0F).padLeft(4.0F); 
    this.box.add((Element)table2).left().padTop(2.0F).padBottom(4.0F).row();
    stepper(tr("Wait after placing (s):", "Espera após colocar (s):", "Espera tras colocar (s):", "Ожидание после установки (с):"), () -> fmt(AutoContainer.waitAfterPlace()), () -> LunariansSettings.containerWaitSec = Math.max(0.0F, LunariansSettings.containerWaitSec - 1.0F), () -> LunariansSettings.containerWaitSec = Math.min(300.0F, LunariansSettings.containerWaitSec + 1.0F));
    stepper(tr("Wait after returning (s):", "Espera após devolver (s):", "Espera tras devolver (s):", "Ожидание после возврата (с):"), () -> fmt(AutoContainer.waitAfterReturn()), () -> AutoContainer.setWaitAfterReturn(Math.max(0.0F, AutoContainer.waitAfterReturn() - 1.0F)), () -> AutoContainer.setWaitAfterReturn(Math.min(300.0F, AutoContainer.waitAfterReturn() + 1.0F)));
  }
  
  private static String fmt(float paramFloat) {
    return (paramFloat == (int)paramFloat) ? String.valueOf((int)paramFloat) : String.valueOf(paramFloat);
  }
  
  private void stepper(String paramString, Prov<String> paramProv, Runnable paramRunnable1, Runnable paramRunnable2) {
    Table table = new Table();
    table.add(paramString).left().padRight(6.0F).wrap().width(190.0F).color(Pal.accent).fontScale(0.9F);
    table.button("-", () -> {
          paramRunnable.run();
          LunariansSettings.save();
          rebuild();
        }).size(36.0F, 32.0F);
    table.add((CharSequence)paramProv.get()).width(50.0F).center().pad(2.0F);
    table.button("+", () -> {
          paramRunnable.run();
          LunariansSettings.save();
          rebuild();
        }).size(36.0F, 32.0F);
    this.box.add((Element)table).left().padTop(3.0F).row();
  }
}
