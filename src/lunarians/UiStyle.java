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
import arc.scene.ui.ScrollPane;
import arc.scene.ui.TextButton;
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
  public static final Color BG = Color.valueOf("11141a");
  
  public static final Color CARD = Color.valueOf("1b2028");
  
  public static final Color BTN = Color.valueOf("262d39");
  
  public static final Color BTN_OVER = Color.valueOf("323b4b");
  
  public static final Color DIM = Color.valueOf("5d6676");
  
  public static final Color TEXT_DIM = Color.valueOf("98a2b3");
  
  private static final WeakHashMap<Object, Boolean> styled = new WeakHashMap<>();
  
  private static final Seq<BaseDialog> registered = new Seq();
  
  private static float nextTick = 0.0F;
  
  private static CheckBox.CheckBoxStyle checkStyle;
  
  private static CheckBox.CheckBoxStyle checkStyleBare;
  
  private static TextButton.TextButtonStyle btnStyle;
  
  private static TextButton.TextButtonStyle toggleStyle;
  
  private static float alpha() {
    return (Draw.getColor()).a;
  }
  
  public static class Pad extends BaseDrawable {
    final Color color;
    
    final float a;
    
    final boolean topLine;
    
    public Pad(Color param1Color, float param1Float1, float param1Float2, float param1Float3, boolean param1Boolean) {
      this.color = param1Color;
      this.a = param1Float1;
      this.topLine = param1Boolean;
      setLeftWidth(param1Float2);
      setRightWidth(param1Float2);
      setTopHeight(param1Float3);
      setBottomHeight(param1Float3);
    }
    
    public void draw(float param1Float1, float param1Float2, float param1Float3, float param1Float4) {
      float f = UiStyle.alpha();
      Draw.color(this.color.r, this.color.g, this.color.b, this.a * f);
      Fill.rect(param1Float1 + param1Float3 / 2.0F, param1Float2 + param1Float4 / 2.0F, param1Float3, param1Float4);
      if (this.topLine) {
        Draw.color(Pal.accent.r, Pal.accent.g, Pal.accent.b, f);
        Fill.rect(param1Float1 + param1Float3 / 2.0F, param1Float2 + param1Float4 - 1.5F, param1Float3, 3.0F);
      } 
      Draw.color();
    }
  }
  
  public static class CardDrawable extends BaseDrawable {
    final Table owner;
    
    public CardDrawable(Table param1Table) {
      this.owner = param1Table;
      setLeftWidth(12.0F);
      setRightWidth(8.0F);
      setTopHeight(6.0F);
      setBottomHeight(6.0F);
    }
    
    private boolean on() {
      for (Element element : this.owner.getChildren()) {
        if (element instanceof CheckBox && ((CheckBox)element).isChecked())
          return true; 
        if (element instanceof Table)
          for (Element element1 : ((Table)element).getChildren()) {
            if (element1 instanceof CheckBox && ((CheckBox)element1).isChecked())
              return true; 
          }  
      } 
      return false;
    }
    
    public void draw(float param1Float1, float param1Float2, float param1Float3, float param1Float4) {
      float f = UiStyle.alpha();
      Draw.color(UiStyle.CARD.r, UiStyle.CARD.g, UiStyle.CARD.b, 0.94F * f);
      Fill.rect(param1Float1 + param1Float3 / 2.0F, param1Float2 + param1Float4 / 2.0F, param1Float3, param1Float4);
      Color color = on() ? Pal.accent : UiStyle.DIM;
      Draw.color(color.r, color.g, color.b, f);
      Fill.rect(param1Float1 + 2.0F, param1Float2 + param1Float4 / 2.0F, 4.0F, param1Float4);
      Draw.color(1.0F, 1.0F, 1.0F, 0.05F * f);
      Fill.rect(param1Float1 + param1Float3 / 2.0F, param1Float2 + param1Float4 - 0.5F, param1Float3, 1.0F);
      Draw.color();
    }
  }
  
  public static class ToggleBox extends BaseDrawable {
    final boolean on;
    
    final boolean over;
    
    public ToggleBox(boolean param1Boolean1, boolean param1Boolean2) {
      this.on = param1Boolean1;
      this.over = param1Boolean2;
      setMinWidth(26.0F);
      setMinHeight(26.0F);
    }
    
    public void draw(float param1Float1, float param1Float2, float param1Float3, float param1Float4) {
      float f1 = UiStyle.alpha();
      float f2 = Math.min(Math.min(param1Float3, param1Float4), 22.0F);
      float f3 = param1Float1 + param1Float3 / 2.0F, f4 = param1Float2 + param1Float4 / 2.0F;
      Color color = this.on ? Pal.accent : (this.over ? UiStyle.TEXT_DIM : UiStyle.DIM);
      Draw.color(color.r, color.g, color.b, f1);
      Fill.rect(f3, f4, f2, f2);
      if (this.on) {
        Draw.color(Pal.accent.r * 0.9F, Pal.accent.g * 0.9F, Pal.accent.b * 0.9F, f1);
        Fill.rect(f3, f4, f2 - 4.0F, f2 - 4.0F);
        Draw.color(0.07F, 0.08F, 0.1F, f1);
        Lines.stroke(2.6F);
        Lines.line(f3 - f2 * 0.24F, f4 - f2 * 0.02F, f3 - f2 * 0.06F, f4 - f2 * 0.22F);
        Lines.line(f3 - f2 * 0.06F, f4 - f2 * 0.22F, f3 + f2 * 0.27F, f4 + f2 * 0.2F);
      } else {
        Draw.color(UiStyle.BG.r, UiStyle.BG.g, UiStyle.BG.b, f1);
        Fill.rect(f3, f4, f2 - 4.0F, f2 - 4.0F);
      } 
      Draw.color();
    }
  }
  
  private static void ensureStyles() {
    if (checkStyle != null)
      return; 
    TextButton.TextButtonStyle textButtonStyle = Styles.defaultt;
    checkStyle = new CheckBox.CheckBoxStyle();
    copyText(textButtonStyle, (TextButton.TextButtonStyle)checkStyle);
    checkStyle.up = (Drawable)new Pad(CARD, 0.94F, 8.0F, 5.0F, false);
    checkStyle.over = (Drawable)new Pad(BTN_OVER, 0.94F, 8.0F, 5.0F, false);
    checkStyle.down = checkStyle.over;
    checkStyle.checked = checkStyle.up;
    checkStyle.checkedOver = checkStyle.over;
    checkStyle.checkboxOn = (Drawable)new ToggleBox(true, false);
    checkStyle.checkboxOnOver = (Drawable)new ToggleBox(true, true);
    checkStyle.checkboxOff = (Drawable)new ToggleBox(false, false);
    checkStyle.checkboxOver = (Drawable)new ToggleBox(false, true);
    checkStyle.checkboxOnDisabled = checkStyle.checkboxOn;
    checkStyle.checkboxOffDisabled = checkStyle.checkboxOff;
    checkStyleBare = new CheckBox.CheckBoxStyle();
    copyText(textButtonStyle, (TextButton.TextButtonStyle)checkStyleBare);
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
    btnStyle.up = (Drawable)new Pad(BTN, 1.0F, 10.0F, 4.0F, false);
    btnStyle.over = (Drawable)new Pad(BTN_OVER, 1.0F, 10.0F, 4.0F, false);
    btnStyle.down = (Drawable)new Pad(Pal.accent, 0.6F, 10.0F, 4.0F, false);
    btnStyle.disabled = (Drawable)new Pad(CARD, 0.7F, 10.0F, 4.0F, false);
    btnStyle.checked = (Drawable)new Pad(Pal.accent, 0.55F, 10.0F, 4.0F, false);
    btnStyle.checkedOver = (Drawable)new Pad(Pal.accent, 0.7F, 10.0F, 4.0F, false);
    toggleStyle = btnStyle;
  }
  
  private static void copyText(TextButton.TextButtonStyle paramTextButtonStyle1, TextButton.TextButtonStyle paramTextButtonStyle2) {
    paramTextButtonStyle2.font = paramTextButtonStyle1.font;
    paramTextButtonStyle2.fontColor = paramTextButtonStyle1.fontColor;
    paramTextButtonStyle2.downFontColor = paramTextButtonStyle1.downFontColor;
    paramTextButtonStyle2.overFontColor = paramTextButtonStyle1.overFontColor;
    paramTextButtonStyle2.checkedFontColor = paramTextButtonStyle1.checkedFontColor;
    paramTextButtonStyle2.checkedOverFontColor = paramTextButtonStyle1.checkedOverFontColor;
    paramTextButtonStyle2.disabledFontColor = paramTextButtonStyle1.disabledFontColor;
  }
  
  public static Drawable panelBg() {
    return (Drawable)new Pad(BG, 0.95F, 6.0F, 6.0F, true);
  }
  
  public static void section(Table paramTable, String paramString) {
    paramTable.table(paramTable -> {
          paramTable.image(Tex.whiteui).color(Pal.accent).size(4.0F, 15.0F).padRight(7.0F);
          paramTable.add(paramString.toUpperCase()).color(TEXT_DIM).fontScale(0.82F).left();
          paramTable.image(Tex.whiteui).color(DIM.cpy().a(0.45F)).height(1.0F).growX().padLeft(9.0F);
        }).growX().padTop(11.0F).padBottom(3.0F).padLeft(2.0F).padRight(2.0F).row();
  }
  
  public static void banner(Table paramTable, String paramString1, String paramString2) {
    paramTable.table((Drawable)new Pad(Pal.accent.cpy().mul(0.3F), 1.0F, 12.0F, 8.0F, false), paramTable -> {
          paramTable.left();
          paramTable.add(paramString1).color(Color.white).fontScale(1.15F).left().row();
          paramTable.add(paramString2).color(TEXT_DIM).fontScale(0.78F).left();
        }).growX().padBottom(2.0F).row();
  }
  
  public static void register(BaseDialog paramBaseDialog) {
    if (paramBaseDialog != null && !registered.contains(paramBaseDialog, true))
      registered.add(paramBaseDialog); 
  }
  
  public static void install() {
    Events.run(EventType.Trigger.update, UiStyle::tick);
  }
  
  private static void tick() {
    if (Time.time < nextTick || Core.scene == null)
      return; 
    nextTick = Time.time + 12.0F;
    try {
      for (Element element : Core.scene.root.getChildren()) {
        if (element instanceof BaseDialog && (element.getClass().getName().startsWith("lunarians") || registered.contains(element, true)))
          decorateDialog((BaseDialog)element); 
      } 
      registered.removeAll(paramBaseDialog -> !paramBaseDialog.isShown());
    } catch (Throwable throwable) {
      Log.err("[Lunarians] UiStyle tick", throwable);
    } 
  }
  
  private static void decorateDialog(BaseDialog paramBaseDialog) {
    if (!styled.containsKey(paramBaseDialog)) {
      styled.put(paramBaseDialog, Boolean.valueOf(true));
      try {
        paramBaseDialog.title.setColor(Pal.accent);
        paramBaseDialog.title.setFontScale(1.1F);
      } catch (Throwable throwable) {}
    } 
    decorate((Element)paramBaseDialog.cont);
  }
  
  public static void decorate(Element paramElement) {
    try {
      ensureStyles();
      walk(paramElement, false);
    } catch (Throwable throwable) {
      Log.err("[Lunarians] UiStyle decorate", throwable);
    } 
  }
  
  private static boolean hasControls(Table paramTable) {
    for (Element element : paramTable.getChildren()) {
      if (element instanceof TextButton || element instanceof arc.scene.ui.ImageButton || element instanceof arc.scene.ui.Slider || element instanceof arc.scene.ui.TextField)
        return true; 
    } 
    return false;
  }
  
  private static void walk(Element paramElement, boolean paramBoolean) {
    if (paramElement == null)
      return; 
    if (paramElement instanceof CheckBox) {
      CheckBox checkBox = (CheckBox)paramElement;
      if (!styled.containsKey(checkBox)) {
        styled.put(checkBox, Boolean.valueOf(true));
        checkBox.setStyle(paramBoolean ? (Button.ButtonStyle)checkStyleBare : (Button.ButtonStyle)checkStyle);
      } 
      return;
    } 
    if (paramElement instanceof TextButton) {
      TextButton textButton = (TextButton)paramElement;
      if (!styled.containsKey(textButton)) {
        styled.put(textButton, Boolean.valueOf(true));
        TextButton.TextButtonStyle textButtonStyle = textButton.getStyle();
        if (textButtonStyle != null && textButtonStyle.up != null)
          textButton.setStyle((textButtonStyle.checked != null) ? (Button.ButtonStyle)toggleStyle : (Button.ButtonStyle)btnStyle); 
      } 
      return;
    } 
    if (paramElement instanceof ScrollPane) {
      walk(((ScrollPane)paramElement).getWidget(), paramBoolean);
      return;
    } 
    if (paramElement instanceof Table) {
      Table table = (Table)paramElement;
      boolean bool1 = (!paramBoolean && table.getBackground() == null && !(table instanceof Button) && (hasControls(table) || hasCheck(table))) ? true : false;
      if (bool1 && !styled.containsKey(table)) {
        styled.put(table, Boolean.valueOf(true));
        table.background((Drawable)new CardDrawable(table));
      } 
      boolean bool2 = (paramBoolean || bool1 || (styled.containsKey(table) && table.getBackground() instanceof CardDrawable)) ? true : false;
      for (Element element : table.getChildren())
        walk(element, bool2); 
      return;
    } 
    if (paramElement instanceof Group)
      for (Element element : ((Group)paramElement).getChildren())
        walk(element, paramBoolean);  
  }
  
  private static boolean hasCheck(Table paramTable) {
    for (Element element : paramTable.getChildren()) {
      if (element instanceof CheckBox)
        return true; 
    } 
    return false;
  }
}
