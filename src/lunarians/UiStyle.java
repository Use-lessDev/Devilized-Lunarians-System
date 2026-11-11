/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.Events
 *  arc.graphics.Color
 *  arc.graphics.g2d.Draw
 *  arc.graphics.g2d.Fill
 *  arc.graphics.g2d.Lines
 *  arc.scene.Element
 *  arc.scene.Group
 *  arc.scene.style.BaseDrawable
 *  arc.scene.style.Drawable
 *  arc.scene.ui.Button
 *  arc.scene.ui.Button$ButtonStyle
 *  arc.scene.ui.CheckBox
 *  arc.scene.ui.CheckBox$CheckBoxStyle
 *  arc.scene.ui.ImageButton
 *  arc.scene.ui.ScrollPane
 *  arc.scene.ui.Slider
 *  arc.scene.ui.TextButton
 *  arc.scene.ui.TextButton$TextButtonStyle
 *  arc.scene.ui.TextField
 *  arc.scene.ui.layout.Table
 *  arc.struct.Seq
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.game.EventType$Trigger
 *  mindustry.gen.Tex
 *  mindustry.graphics.Pal
 *  mindustry.ui.Styles
 *  mindustry.ui.dialogs.BaseDialog
 */
package lunarians;

import arc.Core;
import arc.Events;
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
import java.util.WeakHashMap;
import mindustry.game.EventType;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public class UiStyle {
    public static final Color BG = Color.valueOf((String)"11141a");
    public static final Color CARD = Color.valueOf((String)"1b2028");
    public static final Color BTN = Color.valueOf((String)"262d39");
    public static final Color BTN_OVER = Color.valueOf((String)"323b4b");
    public static final Color DIM = Color.valueOf((String)"5d6676");
    public static final Color TEXT_DIM = Color.valueOf((String)"98a2b3");
    private static final WeakHashMap<Object, Boolean> styled = new WeakHashMap();
    private static final Seq<BaseDialog> registered = new Seq();
    private static float nextTick = 0.0f;
    private static CheckBox.CheckBoxStyle checkStyle;
    private static CheckBox.CheckBoxStyle checkStyleBare;
    private static TextButton.TextButtonStyle btnStyle;
    private static TextButton.TextButtonStyle toggleStyle;

    private static float alpha() {
        return Draw.getColor().a;
    }

    private static void ensureStyles() {
        if (checkStyle != null) {
            return;
        }
        TextButton.TextButtonStyle textButtonStyle = Styles.defaultt;
        checkStyle = new CheckBox.CheckBoxStyle();
        UiStyle.copyText(textButtonStyle, (TextButton.TextButtonStyle)checkStyle);
        UiStyle.checkStyle.up = new Pad(CARD, 0.94f, 8.0f, 5.0f, false);
        UiStyle.checkStyle.down = UiStyle.checkStyle.over = new Pad(BTN_OVER, 0.94f, 8.0f, 5.0f, false);
        UiStyle.checkStyle.checked = UiStyle.checkStyle.up;
        UiStyle.checkStyle.checkedOver = UiStyle.checkStyle.over;
        UiStyle.checkStyle.checkboxOn = new ToggleBox(true, false);
        UiStyle.checkStyle.checkboxOnOver = new ToggleBox(true, true);
        UiStyle.checkStyle.checkboxOff = new ToggleBox(false, false);
        UiStyle.checkStyle.checkboxOver = new ToggleBox(false, true);
        UiStyle.checkStyle.checkboxOnDisabled = UiStyle.checkStyle.checkboxOn;
        UiStyle.checkStyle.checkboxOffDisabled = UiStyle.checkStyle.checkboxOff;
        checkStyleBare = new CheckBox.CheckBoxStyle();
        UiStyle.copyText(textButtonStyle, (TextButton.TextButtonStyle)checkStyleBare);
        UiStyle.checkStyleBare.up = null;
        UiStyle.checkStyleBare.over = null;
        UiStyle.checkStyleBare.down = null;
        UiStyle.checkStyleBare.checked = null;
        UiStyle.checkStyleBare.checkedOver = null;
        UiStyle.checkStyleBare.checkboxOn = UiStyle.checkStyle.checkboxOn;
        UiStyle.checkStyleBare.checkboxOnOver = UiStyle.checkStyle.checkboxOnOver;
        UiStyle.checkStyleBare.checkboxOff = UiStyle.checkStyle.checkboxOff;
        UiStyle.checkStyleBare.checkboxOver = UiStyle.checkStyle.checkboxOver;
        UiStyle.checkStyleBare.checkboxOnDisabled = UiStyle.checkStyle.checkboxOn;
        UiStyle.checkStyleBare.checkboxOffDisabled = UiStyle.checkStyle.checkboxOff;
        btnStyle = new TextButton.TextButtonStyle(textButtonStyle);
        UiStyle.btnStyle.up = new Pad(BTN, 1.0f, 10.0f, 4.0f, false);
        UiStyle.btnStyle.over = new Pad(BTN_OVER, 1.0f, 10.0f, 4.0f, false);
        UiStyle.btnStyle.down = new Pad(Pal.accent, 0.6f, 10.0f, 4.0f, false);
        UiStyle.btnStyle.disabled = new Pad(CARD, 0.7f, 10.0f, 4.0f, false);
        UiStyle.btnStyle.checked = new Pad(Pal.accent, 0.55f, 10.0f, 4.0f, false);
        UiStyle.btnStyle.checkedOver = new Pad(Pal.accent, 0.7f, 10.0f, 4.0f, false);
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

    public static void section(Table table2, String string) {
        table2.table(table -> {
            table.image(Tex.whiteui).color(Pal.accent).size(4.0f, 15.0f).padRight(7.0f);
            table.add((CharSequence)string.toUpperCase()).color(TEXT_DIM).fontScale(0.82f).left();
            table.image(Tex.whiteui).color(DIM.cpy().a(0.45f)).height(1.0f).growX().padLeft(9.0f);
        }).growX().padTop(11.0f).padBottom(3.0f).padLeft(2.0f).padRight(2.0f).row();
    }

    public static void banner(Table table2, String string, String string2) {
        table2.table((Drawable)new Pad(Pal.accent.cpy().mul(0.3f), 1.0f, 12.0f, 8.0f, false), table -> {
            table.left();
            table.add((CharSequence)string).color(Color.white).fontScale(1.15f).left().row();
            table.add((CharSequence)string2).color(TEXT_DIM).fontScale(0.78f).left();
        }).growX().padBottom(2.0f).row();
    }

    public static void register(BaseDialog baseDialog) {
        if (baseDialog != null && !registered.contains((Object)baseDialog, true)) {
            registered.add((Object)baseDialog);
        }
    }

    public static void install() {
        Events.run((Object)EventType.Trigger.update, UiStyle::tick);
    }

    private static void tick() {
        if (Time.time < nextTick || Core.scene == null) {
            return;
        }
        nextTick = Time.time + 12.0f;
        try {
            for (Element element : Core.scene.root.getChildren()) {
                if (!(element instanceof BaseDialog) || !element.getClass().getName().startsWith("lunarians") && !registered.contains((Object)((BaseDialog)element), true)) continue;
                UiStyle.decorateDialog((BaseDialog)element);
            }
            registered.removeAll(baseDialog -> !baseDialog.isShown());
        }
        catch (Throwable throwable) {
            Log.err((String)"[Lunarians] UiStyle tick", (Throwable)throwable);
        }
    }

    private static void decorateDialog(BaseDialog baseDialog) {
        if (!styled.containsKey(baseDialog)) {
            styled.put(baseDialog, true);
            try {
                baseDialog.title.setColor(Pal.accent);
                baseDialog.title.setFontScale(1.1f);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        UiStyle.decorate((Element)baseDialog.cont);
    }

    public static void decorate(Element element) {
        try {
            UiStyle.ensureStyles();
            UiStyle.walk(element, false);
        }
        catch (Throwable throwable) {
            Log.err((String)"[Lunarians] UiStyle decorate", (Throwable)throwable);
        }
    }

    private static boolean hasControls(Table table) {
        for (Element element : table.getChildren()) {
            if (!(element instanceof TextButton) && !(element instanceof ImageButton) && !(element instanceof Slider) && !(element instanceof TextField)) continue;
            return true;
        }
        return false;
    }

    private static void walk(Element element, boolean bl) {
        if (element == null) {
            return;
        }
        if (element instanceof CheckBox) {
            CheckBox checkBox = (CheckBox)element;
            if (!styled.containsKey(checkBox)) {
                styled.put(checkBox, true);
                checkBox.setStyle((Button.ButtonStyle)(bl ? checkStyleBare : checkStyle));
            }
            return;
        }
        if (element instanceof TextButton) {
            TextButton textButton = (TextButton)element;
            if (!styled.containsKey(textButton)) {
                styled.put(textButton, true);
                TextButton.TextButtonStyle textButtonStyle = textButton.getStyle();
                if (textButtonStyle != null && textButtonStyle.up != null) {
                    textButton.setStyle((Button.ButtonStyle)(textButtonStyle.checked != null ? toggleStyle : btnStyle));
                }
            }
            return;
        }
        if (element instanceof ScrollPane) {
            UiStyle.walk(((ScrollPane)element).getWidget(), bl);
            return;
        }
        if (element instanceof Table) {
            boolean bl2;
            Table table = (Table)element;
            boolean bl3 = bl2 = !bl && table.getBackground() == null && !(table instanceof Button) && (UiStyle.hasControls(table) || UiStyle.hasCheck(table));
            if (bl2 && !styled.containsKey(table)) {
                styled.put(table, true);
                table.background((Drawable)new CardDrawable(table));
            }
            boolean bl4 = bl || bl2 || styled.containsKey(table) && table.getBackground() instanceof CardDrawable;
            for (Element element2 : table.getChildren()) {
                UiStyle.walk(element2, bl4);
            }
            return;
        }
        if (element instanceof Group) {
            for (Element element3 : ((Group)element).getChildren()) {
                UiStyle.walk(element3, bl);
            }
        }
    }

    private static boolean hasCheck(Table table) {
        for (Element element : table.getChildren()) {
            if (!(element instanceof CheckBox)) continue;
            return true;
        }
        return false;
    }

    public static class Pad
    extends BaseDrawable {
        final Color color;
        final float a;
        final boolean topLine;

        public Pad(Color color, float f, float f2, float f3, boolean bl) {
            this.color = color;
            this.a = f;
            this.topLine = bl;
            this.setLeftWidth(f2);
            this.setRightWidth(f2);
            this.setTopHeight(f3);
            this.setBottomHeight(f3);
        }

        public void draw(float f, float f2, float f3, float f4) {
            float f5 = UiStyle.alpha();
            Draw.color((float)this.color.r, (float)this.color.g, (float)this.color.b, (float)(this.a * f5));
            Fill.rect((float)(f + f3 / 2.0f), (float)(f2 + f4 / 2.0f), (float)f3, (float)f4);
            if (this.topLine) {
                Draw.color((float)Pal.accent.r, (float)Pal.accent.g, (float)Pal.accent.b, (float)f5);
                Fill.rect((float)(f + f3 / 2.0f), (float)(f2 + f4 - 1.5f), (float)f3, (float)3.0f);
            }
            Draw.color();
        }
    }

    public static class ToggleBox
    extends BaseDrawable {
        final boolean on;
        final boolean over;

        public ToggleBox(boolean bl, boolean bl2) {
            this.on = bl;
            this.over = bl2;
            this.setMinWidth(26.0f);
            this.setMinHeight(26.0f);
        }

        public void draw(float f, float f2, float f3, float f4) {
            float f5 = UiStyle.alpha();
            float f6 = Math.min(Math.min(f3, f4), 22.0f);
            float f7 = f + f3 / 2.0f;
            float f8 = f2 + f4 / 2.0f;
            Color color = this.on ? Pal.accent : (this.over ? TEXT_DIM : DIM);
            Draw.color((float)color.r, (float)color.g, (float)color.b, (float)f5);
            Fill.rect((float)f7, (float)f8, (float)f6, (float)f6);
            if (this.on) {
                Draw.color((float)(Pal.accent.r * 0.9f), (float)(Pal.accent.g * 0.9f), (float)(Pal.accent.b * 0.9f), (float)f5);
                Fill.rect((float)f7, (float)f8, (float)(f6 - 4.0f), (float)(f6 - 4.0f));
                Draw.color((float)0.07f, (float)0.08f, (float)0.1f, (float)f5);
                Lines.stroke((float)2.6f);
                Lines.line((float)(f7 - f6 * 0.24f), (float)(f8 - f6 * 0.02f), (float)(f7 - f6 * 0.06f), (float)(f8 - f6 * 0.22f));
                Lines.line((float)(f7 - f6 * 0.06f), (float)(f8 - f6 * 0.22f), (float)(f7 + f6 * 0.27f), (float)(f8 + f6 * 0.2f));
            } else {
                Draw.color((float)UiStyle.BG.r, (float)UiStyle.BG.g, (float)UiStyle.BG.b, (float)f5);
                Fill.rect((float)f7, (float)f8, (float)(f6 - 4.0f), (float)(f6 - 4.0f));
            }
            Draw.color();
        }
    }

    public static class CardDrawable
    extends BaseDrawable {
        final Table owner;

        public CardDrawable(Table table) {
            this.owner = table;
            this.setLeftWidth(12.0f);
            this.setRightWidth(8.0f);
            this.setTopHeight(6.0f);
            this.setBottomHeight(6.0f);
        }

        private boolean on() {
            for (Element element : this.owner.getChildren()) {
                if (element instanceof CheckBox && ((CheckBox)element).isChecked()) {
                    return true;
                }
                if (!(element instanceof Table)) continue;
                for (Element element2 : ((Table)element).getChildren()) {
                    if (!(element2 instanceof CheckBox) || !((CheckBox)element2).isChecked()) continue;
                    return true;
                }
            }
            return false;
        }

        public void draw(float f, float f2, float f3, float f4) {
            float f5 = UiStyle.alpha();
            Draw.color((float)UiStyle.CARD.r, (float)UiStyle.CARD.g, (float)UiStyle.CARD.b, (float)(0.94f * f5));
            Fill.rect((float)(f + f3 / 2.0f), (float)(f2 + f4 / 2.0f), (float)f3, (float)f4);
            Color color = this.on() ? Pal.accent : DIM;
            Draw.color((float)color.r, (float)color.g, (float)color.b, (float)f5);
            Fill.rect((float)(f + 2.0f), (float)(f2 + f4 / 2.0f), (float)4.0f, (float)f4);
            Draw.color((float)1.0f, (float)1.0f, (float)1.0f, (float)(0.05f * f5));
            Fill.rect((float)(f + f3 / 2.0f), (float)(f2 + f4 - 0.5f), (float)f3, (float)1.0f);
            Draw.color();
        }
    }
}

