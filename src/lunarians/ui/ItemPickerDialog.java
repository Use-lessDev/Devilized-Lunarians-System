package lunarians.ui;

import arc.Core;
import arc.func.Cons;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Button;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import java.util.Iterator;
import lunarians.LunariansI18n;
import mindustry.Vars;
import mindustry.type.Item;
import mindustry.ui.dialogs.BaseDialog;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/ItemPickerDialog.class */
public class ItemPickerDialog extends BaseDialog {
    private final Cons<Item> callback;

    public ItemPickerDialog(Cons<Item> cons) {
        super(LunariansI18n.get("loss_choose_title"));
        this.callback = cons;
        addCloseButton();
        build();
    }

    private void build() {
        Table table = new Table();
        int max = Math.max(3, Math.min(6, (int) (Core.graphics.getWidth() / Scl.scl(96.0f))));
        int i = 0;
        Iterator it = Vars.content.items().iterator();
        while (it.hasNext()) {
            final Item item = (Item) it.next();
            if (!item.isHidden() && item.unlockedNow()) {
                table.button(new Cons<Button>() { // from class: lunarians.ui.ItemPickerDialog.1
                    public void get(Button button) {
                        button.image(new TextureRegionDrawable(item.uiIcon)).size(40.0f);
                        button.row();
                        Label label = button.add(item.localizedName).wrap().width(80.0f).get();
                        label.setAlignment(1);
                        label.setFontScale(0.8f);
                    }
                }, new Runnable() { // from class: lunarians.ui.ItemPickerDialog.2
                    @Override // java.lang.Runnable
                    public void run() {
                        ItemPickerDialog.this.hide();
                        ItemPickerDialog.this.callback.get(item);
                    }
                }).size(92.0f, 92.0f).pad(3.0f);
                i++;
                if (i % max == 0) {
                    table.row();
                }
            }
        }
        this.cont.pane(table).grow();
    }
}
