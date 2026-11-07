package lunarians;

import arc.Core;
import arc.Events;
import arc.func.Boolf;
import arc.func.Cons;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.scene.Element;
import arc.scene.Group;
import arc.scene.style.BaseDrawable;
import arc.scene.style.Drawable;
import arc.scene.ui.Button;
import arc.scene.ui.CheckBox;
import arc.scene.ui.ImageButton;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.Slider;
import arc.scene.ui.TextButton;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import java.util.Iterator;
import java.util.WeakHashMap;
import mindustry.game.EventType;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/UiStyle.class */
public class UiStyle {
    public static final Color BG = Color.valueOf("11141a");
    public static final Color CARD = Color.valueOf("1b2028");
    public static final Color BTN = Color.valueOf("262d39");
    public static final Color BTN_OVER = Color.valueOf("323b4b");
    public static final Color DIM = Color.valueOf("5d6676");
    public static final Color TEXT_DIM = Color.valueOf("98a2b3");
    private static final WeakHashMap<Object, Boolean> styled = new WeakHashMap<>();
    private static final Seq<BaseDialog> registered = new Seq<>();
    private static float nextTick = 0.0f;
    private static CheckBox.CheckBoxStyle checkStyle;
    private static CheckBox.CheckBoxStyle checkStyleBare;
    private static TextButton.TextButtonStyle btnStyle;
    private static TextButton.TextButtonStyle toggleStyle;

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.UiStyle$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            UiStyle.$r8$lambda$r_FIJsz_yATdIa5qxqyxr-MilXQ();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.UiStyle$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass1 implements Boolf {
        public final boolean get(Object obj) {
            return UiStyle.lambda$tick$2((BaseDialog) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.UiStyle$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass2 implements Cons {
        public final /* synthetic */ String f$0;

        public final void get(Object obj) {
            UiStyle.lambda$section$0(this.f$0, (Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.UiStyle$3, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass3 implements Cons {
        public final /* synthetic */ String f$0;
        public final /* synthetic */ String f$1;

        public /* synthetic */ AnonymousClass3(String str, String str2) {
            this.f$0 = str;
            this.f$1 = str2;
        }

        public final void get(Object obj) {
            UiStyle.lambda$banner$1(this.f$0, this.f$1, (Table) obj);
        }
    }

    private static float alpha() {
        return Draw.getColor().a;
    }

    /* JADX WARN: Classes with same name are omitted:
      classes.dex
     */
    /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/UiStyle$Pad.class */
    public static class Pad extends BaseDrawable {
        final Color color;
        final float a;
        final boolean topLine;

        public Pad(Color color, float f, float f2, float f3, boolean z) {
            this.color = color;
            this.a = f;
            this.topLine = z;
            setLeftWidth(f2);
            setRightWidth(f2);
            setTopHeight(f3);
            setBottomHeight(f3);
        }

        public void draw(float f, float f2, float f3, float f4) {
            float alpha = UiStyle.alpha();
            Draw.color(this.color.r, this.color.g, this.color.b, this.a * alpha);
            Fill.rect(f + (f3 / 2.0f), f2 + (f4 / 2.0f), f3, f4);
            if (this.topLine) {
                Draw.color(Pal.accent.r, Pal.accent.g, Pal.accent.b, alpha);
                Fill.rect(f + (f3 / 2.0f), (f2 + f4) - 1.5f, f3, 3.0f);
            }
            Draw.color();
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      classes.dex
     */
    /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/UiStyle$CardDrawable.class */
    public static class CardDrawable extends BaseDrawable {
        final Table owner;

        public CardDrawable(Table table) {
            this.owner = table;
            setLeftWidth(12.0f);
            setRightWidth(8.0f);
            setTopHeight(6.0f);
            setBottomHeight(6.0f);
        }

        private boolean on() {
            Iterator it = this.owner.getChildren().iterator();
            while (it.hasNext()) {
                CheckBox checkBox = (Element) it.next();
                if ((checkBox instanceof CheckBox) && checkBox.isChecked()) {
                    return true;
                }
                if (checkBox instanceof Table) {
                    Iterator it2 = ((Table) checkBox).getChildren().iterator();
                    while (it2.hasNext()) {
                        CheckBox checkBox2 = (Element) it2.next();
                        if ((checkBox2 instanceof CheckBox) && checkBox2.isChecked()) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        public void draw(float f, float f2, float f3, float f4) {
            float alpha = UiStyle.alpha();
            Draw.color(UiStyle.CARD.r, UiStyle.CARD.g, UiStyle.CARD.b, 0.94f * alpha);
            Fill.rect(f + (f3 / 2.0f), f2 + (f4 / 2.0f), f3, f4);
            Color color = on() ? Pal.accent : UiStyle.DIM;
            Draw.color(color.r, color.g, color.b, alpha);
            Fill.rect(f + 2.0f, f2 + (f4 / 2.0f), 4.0f, f4);
            Draw.color(1.0f, 1.0f, 1.0f, 0.05f * alpha);
            Fill.rect(f + (f3 / 2.0f), (f2 + f4) - 0.5f, f3, 1.0f);
            Draw.color();
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      classes.dex
     */
    /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/UiStyle$ToggleBox.class */
    public static class ToggleBox extends BaseDrawable {
        final boolean on;
        final boolean over;

        public ToggleBox(boolean z, boolean z2) {
            this.on = z;
            this.over = z2;
            setMinWidth(26.0f);
            setMinHeight(26.0f);
        }

        public void draw(float f, float f2, float f3, float f4) {
            float alpha = UiStyle.alpha();
            float min = Math.min(Math.min(f3, f4), 22.0f);
            float f5 = f + (f3 / 2.0f);
            float f6 = f2 + (f4 / 2.0f);
            Color color = this.on ? Pal.accent : this.over ? UiStyle.TEXT_DIM : UiStyle.DIM;
            Draw.color(color.r, color.g, color.b, alpha);
            Fill.rect(f5, f6, min, min);
            if (this.on) {
                Draw.color(Pal.accent.r * 0.9f, Pal.accent.g * 0.9f, Pal.accent.b * 0.9f, alpha);
                Fill.rect(f5, f6, min - 4.0f, min - 4.0f);
                Draw.color(0.07f, 0.08f, 0.1f, alpha);
                Lines.stroke(2.6f);
                Lines.line(f5 - (min * 0.24f), f6 - (min * 0.02f), f5 - (min * 0.06f), f6 - (min * 0.22f));
                Lines.line(f5 - (min * 0.06f), f6 - (min * 0.22f), f5 + (min * 0.27f), f6 + (min * 0.2f));
            } else {
                Draw.color(UiStyle.BG.r, UiStyle.BG.g, UiStyle.BG.b, alpha);
                Fill.rect(f5, f6, min - 4.0f, min - 4.0f);
            }
            Draw.color();
        }
    }

    private static void ensureStyles() {
        if (checkStyle != null) {
            return;
        }
        TextButton.TextButtonStyle textButtonStyle = Styles.defaultt;
        checkStyle = new CheckBox.CheckBoxStyle();
        copyText(textButtonStyle, checkStyle);
        checkStyle.up = new Pad(CARD, 0.94f, 8.0f, 5.0f, false);
        checkStyle.over = new Pad(BTN_OVER, 0.94f, 8.0f, 5.0f, false);
        checkStyle.down = checkStyle.over;
        checkStyle.checked = checkStyle.up;
        checkStyle.checkedOver = checkStyle.over;
        checkStyle.checkboxOn = new ToggleBox(true, false);
        checkStyle.checkboxOnOver = new ToggleBox(true, true);
        checkStyle.checkboxOff = new ToggleBox(false, false);
        checkStyle.checkboxOver = new ToggleBox(false, true);
        checkStyle.checkboxOnDisabled = checkStyle.checkboxOn;
        checkStyle.checkboxOffDisabled = checkStyle.checkboxOff;
        checkStyleBare = new CheckBox.CheckBoxStyle();
        copyText(textButtonStyle, checkStyleBare);
        checkStyleBare.up = null;
        checkStyleBare.over = null;
        checkStyleBare.down = null;
        checkStyleBare.checked = null;
        checkStyleBare.checkedOver = null;
        checkStyleBare.checkboxOn = checkStyle.checkboxOn;
        checkStyleBare.checkboxOnOver = checkStyle.checkboxOnOver;
        checkStyleBare.checkboxOff = checkStyle.checkboxOff;
        checkStyleBare.checkboxOver = checkStyle.checkboxOver;
        checkStyleBare.checkboxOnDisabled = checkStyle.checkboxOn;
        checkStyleBare.checkboxOffDisabled = checkStyle.checkboxOff;
        btnStyle = new TextButton.TextButtonStyle(textButtonStyle);
        btnStyle.up = new Pad(BTN, 1.0f, 10.0f, 4.0f, false);
        btnStyle.over = new Pad(BTN_OVER, 1.0f, 10.0f, 4.0f, false);
        btnStyle.down = new Pad(Pal.accent, 0.6f, 10.0f, 4.0f, false);
        btnStyle.disabled = new Pad(CARD, 0.7f, 10.0f, 4.0f, false);
        btnStyle.checked = new Pad(Pal.accent, 0.55f, 10.0f, 4.0f, false);
        btnStyle.checkedOver = new Pad(Pal.accent, 0.7f, 10.0f, 4.0f, false);
        toggleStyle = btnStyle;
    }

    private static void copyText(TextButton.TextButtonStyle textButtonStyle, TextButton.TextButtonStyle textButtonStyle2) {
        textButtonStyle2.font = textButtonStyle.font;
        textButtonStyle2.fontColor = textButtonStyle.fontColor;
        textButtonStyle2.downFontColor = textButtonStyle.downFontColor;
        textButtonStyle2.overFontColor = textButtonStyle.overFontColor;
        textButtonStyle2.checkedFontColor = textButtonStyle.checkedFontColor;
        textButtonStyle2.checkedOverFontColor = textButtonStyle.checkedOverFontColor;
        textButtonStyle2.disabledFontColor = textButtonStyle.disabledFontColor;
    }

    public static Drawable panelBg() {
        return new Pad(BG, 0.95f, 6.0f, 6.0f, true);
    }

    public static void section(Table table, String str) {
        table.table(table2 -> {
            table2.image(Tex.whiteui).color(Pal.accent).size(4.0f, 15.0f).padRight(7.0f);
            table2.add(str.toUpperCase()).color(TEXT_DIM).fontScale(0.82f).left();
            table2.image(Tex.whiteui).color(DIM.cpy().a(0.45f)).height(1.0f).growX().padLeft(9.0f);
        }).growX().padTop(11.0f).padBottom(3.0f).padLeft(2.0f).padRight(2.0f).row();
    }

    public static void banner(Table table, String str, String str2) {
        table.table(new Pad(Pal.accent.cpy().mul(0.3f), 1.0f, 12.0f, 8.0f, false), table2 -> {
            table2.left();
            table2.add(str).color(Color.white).fontScale(1.15f).left().row();
            table2.add(str2).color(TEXT_DIM).fontScale(0.78f).left();
        }).growX().padBottom(2.0f).row();
    }

    public static void register(BaseDialog baseDialog) {
        if (baseDialog == null || registered.contains(baseDialog, true)) {
            return;
        }
        registered.add(baseDialog);
    }

    public static void install() {
        Events.run(EventType.Trigger.update, UiStyle::tick);
    }

    private static void tick() {
        if (Time.time < nextTick || Core.scene == null) {
            return;
        }
        nextTick = Time.time + 12.0f;
        try {
            Iterator it = Core.scene.root.getChildren().iterator();
            while (it.hasNext()) {
                BaseDialog baseDialog = (Element) it.next();
                if ((baseDialog instanceof BaseDialog) && (baseDialog.getClass().getName().startsWith("lunarians") || registered.contains(baseDialog, true))) {
                    decorateDialog(baseDialog);
                }
            }
            registered.removeAll(baseDialog2 -> {
                return !baseDialog2.isShown();
            });
        } catch (Throwable th) {
            Log.err("[Lunarians] UiStyle tick", th);
        }
    }

    private static void decorateDialog(BaseDialog baseDialog) {
        if (!styled.containsKey(baseDialog)) {
            styled.put(baseDialog, true);
            try {
                baseDialog.title.setColor(Pal.accent);
                baseDialog.title.setFontScale(1.1f);
            } catch (Throwable th) {
            }
        }
        decorate(baseDialog.cont);
    }

    public static void decorate(Element element) {
        try {
            ensureStyles();
            walk(element, false);
        } catch (Throwable th) {
            Log.err("[Lunarians] UiStyle decorate", th);
        }
    }

    private static boolean hasControls(Table table) {
        Iterator it = table.getChildren().iterator();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            if ((element instanceof TextButton) || (element instanceof ImageButton) || (element instanceof Slider) || (element instanceof TextField)) {
                return true;
            }
        }
        return false;
    }

    private static void walk(Element element, boolean z) {
        if (element == null) {
            return;
        }
        if (element instanceof CheckBox) {
            CheckBox checkBox = (CheckBox) element;
            if (!styled.containsKey(checkBox)) {
                styled.put(checkBox, true);
                checkBox.setStyle(z ? checkStyleBare : checkStyle);
                return;
            }
            return;
        }
        if (element instanceof TextButton) {
            TextButton textButton = (TextButton) element;
            if (!styled.containsKey(textButton)) {
                styled.put(textButton, true);
                TextButton.TextButtonStyle style = textButton.getStyle();
                if (style == null || style.up == null) {
                    return;
                }
                textButton.setStyle(style.checked != null ? toggleStyle : btnStyle);
                return;
            }
            return;
        }
        if (element instanceof ScrollPane) {
            walk(((ScrollPane) element).getWidget(), z);
            return;
        }
        if (element instanceof Table) {
            Table table = (Table) element;
            boolean z2 = !z && table.getBackground() == null && !(table instanceof Button) && (hasControls(table) || hasCheck(table));
            if (z2 && !styled.containsKey(table)) {
                styled.put(table, true);
                table.background(new CardDrawable(table));
            }
            boolean z3 = z || z2 || (styled.containsKey(table) && (table.getBackground() instanceof CardDrawable));
            Iterator it = table.getChildren().iterator();
            while (it.hasNext()) {
                walk((Element) it.next(), z3);
            }
            return;
        }
        if (element instanceof Group) {
            Iterator it2 = ((Group) element).getChildren().iterator();
            while (it2.hasNext()) {
                walk((Element) it2.next(), z);
            }
        }
    }

    private static boolean hasCheck(Table table) {
        Iterator it = table.getChildren().iterator();
        while (it.hasNext()) {
            if (((Element) it.next()) instanceof CheckBox) {
                return true;
            }
        }
        return false;
    }
}
