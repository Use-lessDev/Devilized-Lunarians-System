package lunarians.hub;

import arc.Core;
import arc.scene.Action;
import arc.scene.actions.Actions;
import arc.scene.event.Touchable;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Table;
import java.util.ArrayDeque;
import mindustry.gen.Tex;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/hub/GlobalBanner.class */
public class GlobalBanner {
    private static final ArrayDeque<String> queue = new ArrayDeque<>();
    private static boolean showing;

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.GlobalBanner$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Runnable {
        public final /* synthetic */ Table f$0;

        @Override // java.lang.Runnable
        public final void run() {
            GlobalBanner.close(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.GlobalBanner$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass1 implements Runnable {
        public final /* synthetic */ Table f$0;

        @Override // java.lang.Runnable
        public final void run() {
            GlobalBanner.close(this.f$0);
        }
    }

    public static void show(String str) {
        if (str == null) {
            return;
        }
        String trim = str.trim();
        if (trim.isEmpty()) {
            return;
        }
        if (queue.size() < 5) {
            queue.add(trim);
        }
        next();
    }

    private static void next() {
        if (showing || queue.isEmpty() || Core.scene == null) {
            return;
        }
        showing = true;
        String poll = queue.poll();
        Table table = new Table();
        table.setFillParent(true);
        table.touchable = Touchable.childrenOnly;
        table.top();
        Table table2 = new Table(Tex.buttonEdge3);
        table2.margin(8.0f, 14.0f, 8.0f, 14.0f);
        Label label = new Label("[accent]" + HubText.get("global.title"));
        label.setFontScale(0.8f);
        table2.add(label).left().row();
        Label label2 = new Label(poll);
        label2.setWrap(true);
        label2.setAlignment(8);
        table2.add(label2).width(Math.min(Core.scene.getWidth() - 40.0f, 560.0f)).left();
        table.add(table2).padTop(54.0f);
        table.color.a = 0.0f;
        table.actions(new Action[]{Actions.fadeIn(0.35f), Actions.delay(Math.min(14.0f, 6.0f + (poll.length() / 25.0f))), Actions.fadeOut(0.6f), Actions.run(() -> {
            close(table);
        })});
        table2.clicked(() -> {
            close(table);
        });
        Core.scene.add(table);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void close(Table table) {
        if (table.getScene() != null || showing) {
            table.remove();
            showing = false;
            next();
        }
    }
}
