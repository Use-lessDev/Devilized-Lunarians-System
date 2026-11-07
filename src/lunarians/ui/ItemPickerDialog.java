package lunarians.ui;

import arc.Core;
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
import mindustry.type.Item;
import mindustry.ui.dialogs.BaseDialog;

public class ItemPickerDialog extends BaseDialog {
  private final Cons<Item> callback;
  
  public ItemPickerDialog(Cons<Item> paramCons) {
    super(LunariansI18n.get("loss_choose_title"));
    this.callback = paramCons;
    addCloseButton();
    build();
  }
  
  private void build() {
    Table table = new Table();
    int i = Math.max(3, Math.min(6, (int)(Core.graphics.getWidth() / Scl.scl(96.0F))));
    byte b = 0;
    for (Item item : Vars.content.items()) {
      if (item.isHidden() || !item.unlockedNow())
        continue; 
      table.button(new Cons<Button>() {
            public void get(Button param1Button) {
              param1Button.image((Drawable)new TextureRegionDrawable(item.uiIcon)).size(40.0F);
              param1Button.row();
              Label label = (Label)param1Button.add(item.localizedName).wrap().width(80.0F).get();
              label.setAlignment(1);
              label.setFontScale(0.8F);
            }
          },  new Runnable() {
            public void run() {
              ItemPickerDialog.this.hide();
              ItemPickerDialog.this.callback.get(item);
            }
          }).size(92.0F, 92.0F).pad(3.0F);
      if (++b % i != 0)
        continue; 
      table.row();
    } 
    this.cont.pane((Element)table).grow();
  }
}
