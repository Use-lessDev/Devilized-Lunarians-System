/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.scene.Action
 *  arc.scene.Element
 *  arc.scene.actions.Actions
 *  arc.scene.event.Touchable
 *  arc.scene.ui.Label
 *  arc.scene.ui.layout.Table
 *  mindustry.gen.Tex
 */
package lunarians.hub;

import arc.Core;
import arc.scene.Action;
import arc.scene.Element;
import arc.scene.actions.Actions;
import arc.scene.event.Touchable;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Table;
import java.util.ArrayDeque;
import lunarians.hub.HubText;
import mindustry.gen.Tex;

public class GlobalBanner {
    private static final ArrayDeque<String> queue = new ArrayDeque();
    private static boolean showing;

    public static void show(String string) {
        if (string == null) {
            return;
        }
        if ((string = string.trim()).isEmpty()) {
            return;
        }
        if (queue.size() < 5) {
            queue.add(string);
        }
        GlobalBanner.next();
    }

    private static void next() {
        if (showing || queue.isEmpty() || Core.scene == null) {
            return;
        }
        showing = true;
        String string = queue.poll();
        Table table = new Table();
        table.setFillParent(true);
        table.touchable = Touchable.childrenOnly;
        table.top();
        Table table2 = new Table(Tex.buttonEdge3);
        table2.margin(8.0f, 14.0f, 8.0f, 14.0f);
        Label label = new Label((CharSequence)("[accent]" + HubText.get("global.title")));
        label.setFontScale(0.8f);
        table2.add((Element)label).left().row();
        Label label2 = new Label((CharSequence)string);
        label2.setWrap(true);
        label2.setAlignment(8);
        float f = Math.min(Core.scene.getWidth() - 40.0f, 560.0f);
        table2.add((Element)label2).width(f).left();
        table.add((Element)table2).padTop(54.0f);
        table.color.a = 0.0f;
        float f2 = Math.min(14.0f, 6.0f + (float)string.length() / 25.0f);
        table.actions(new Action[]{Actions.fadeIn((float)0.35f), Actions.delay((float)f2), Actions.fadeOut((float)0.6f), Actions.run(() -> GlobalBanner.close(table))});
        table2.clicked(() -> GlobalBanner.close(table));
        Core.scene.add((Element)table);
    }

    private static void close(Table table) {
        if (table.getScene() == null && !showing) {
            return;
        }
        table.remove();
        showing = false;
        GlobalBanner.next();
    }
}

