package lunarians.ui;

import arc.Core;
import arc.func.Boolf;
import arc.func.Cons;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Button;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import java.util.Iterator;
import lunarians.LunariansI18n;
import mindustry.Vars;
import mindustry.type.UnitType;
import mindustry.ui.dialogs.BaseDialog;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/UnitPickerDialog.class */
public class UnitPickerDialog extends BaseDialog {
    private final Cons<UnitType> callback;

    public UnitPickerDialog(Cons<UnitType> cons) {
        this(cons, null);
    }

    public UnitPickerDialog(Cons<UnitType> cons, Boolf<UnitType> boolf) {
        super(LunariansI18n.get("au_choose_title"));
        this.callback = cons;
        addCloseButton();
        Table table = new Table();
        int max = Math.max(3, Math.min(6, (int) (Core.graphics.getWidth() / Scl.scl(96.0f))));
        int i = 0;
        Iterator it = Vars.content.units().iterator();
        while (it.hasNext()) {
            final UnitType unitType = (UnitType) it.next();
            if (!unitType.isHidden() && unitType.unlockedNow() && unitType.commands != null && unitType.commands.size > 1 && (boolf == null || boolf.get(unitType))) {
                table.button(new Cons<Button>() { // from class: lunarians.ui.UnitPickerDialog.1
                    public void get(Button button) {
                        button.image(new TextureRegionDrawable(unitType.uiIcon)).size(40.0f);
                        button.row();
                        Label label = button.add(unitType.localizedName).wrap().width(80.0f).get();
                        label.setAlignment(1);
                        label.setFontScale(0.8f);
                    }
                }, new Runnable() { // from class: lunarians.ui.UnitPickerDialog.2
                    @Override // java.lang.Runnable
                    public void run() {
                        UnitPickerDialog.this.hide();
                        UnitPickerDialog.this.callback.get(unitType);
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
