package lunarians.ui;

import arc.Core;
import arc.func.Boolf;
import arc.func.Cons;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Button;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import lunarians.LunariansI18n;
import mindustry.Vars;
import mindustry.type.UnitType;
import mindustry.ui.dialogs.BaseDialog;

public class UnitPickerDialog extends BaseDialog {
  private final Cons<UnitType> callback;
  
  public UnitPickerDialog(Cons<UnitType> paramCons) {
    this(paramCons, (Boolf<UnitType>)null);
  }
  
  public UnitPickerDialog(Cons<UnitType> paramCons, Boolf<UnitType> paramBoolf) {
    super(LunariansI18n.get("au_choose_title"));
    this.callback = paramCons;
    addCloseButton();
    Table table = new Table();
    int i = Math.max(3, Math.min(6, (int)(Core.graphics.getWidth() / Scl.scl(96.0F))));
    byte b = 0;
    for (UnitType unitType : Vars.content.units()) {
      if (unitType.isHidden() || !unitType.unlockedNow() || unitType.commands == null || unitType.commands.size <= 1 || (paramBoolf != null && !paramBoolf.get(unitType)))
        continue; 
      table.button(new Cons<Button>() {
            public void get(Button param1Button) {
              param1Button.image((Drawable)new TextureRegionDrawable(unitType.uiIcon)).size(40.0F);
              param1Button.row();
              Label label = (Label)param1Button.add(unitType.localizedName).wrap().width(80.0F).get();
              label.setAlignment(1);
              label.setFontScale(0.8F);
            }
          },  new Runnable() {
            public void run() {
              UnitPickerDialog.this.hide();
              UnitPickerDialog.this.callback.get(unitType);
            }
          }).size(92.0F, 92.0F).pad(3.0F);
      if (++b % i != 0)
        continue; 
      table.row();
    } 
    this.cont.pane((Element)table).grow();
  }
}
