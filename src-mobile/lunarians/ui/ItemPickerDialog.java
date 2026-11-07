package lunarians.ui;

import arc.Core;
import arc.Graphics;
import arc.func.Cons;
import arc.scene.Element;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import java.util.Iterator;
import mindustry.Vars;
import mindustry.type.Item;
import mindustry.ui.dialogs.BaseDialog;

public class ItemPickerDialog extends BaseDialog {
  private final Cons callback;
  
  public ItemPickerDialog(Cons paramCons) {
    super(str);
    this.callback = paramCons;
    addCloseButton();
    build();
  }
  
  private void build() {
    Table table = new Table();
    this();
    Graphics graphics = Core.graphics;
    float f1 = graphics.getWidth();
    float f2 = Scl.scl(96.0F);
    f1 /= f2;
    int i = (int)f1;
    i = Math.min(6, i);
    byte b = 3;
    f2 = 4.2E-45F;
    i = Math.max(b, i);
    Iterator<Item> iterator = Vars.content.items().iterator();
    int j = 0;
    while (true) {
      boolean bool = iterator.hasNext();
      if (bool) {
        Item item = iterator.next();
        boolean bool1 = item.isHidden();
        if (!bool1) {
          bool1 = item.unlockedNow();
          if (bool1) {
            ItemPickerDialog$1 itemPickerDialog$1 = new ItemPickerDialog$1();
            this(this, item);
            ItemPickerDialog$2 itemPickerDialog$2 = new ItemPickerDialog$2();
            this(this, item);
            Cell cell = table.button(itemPickerDialog$1, itemPickerDialog$2);
            float f = 92.0F;
            cell = cell.size(f, f);
            int m = 1077936128;
            f = 3.0F;
            cell.pad(f);
            int k = ++j % i;
            if (k == 0)
              table.row(); 
          } 
        } 
        continue;
      } 
      this.cont.pane((Element)table).grow();
      return;
    } 
  }
}
