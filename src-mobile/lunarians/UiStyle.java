package lunarians;

import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Font;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.ui.CheckBox;
import arc.scene.ui.Label;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Log;
import java.util.Iterator;
import java.util.WeakHashMap;
import mindustry.game.EventType;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public class UiStyle {
  public static final Color BG = Color.valueOf("11141a");
  
  public static final Color BTN;
  
  public static final Color BTN_OVER;
  
  public static final Color CARD = Color.valueOf("1b2028");
  
  public static final Color DIM;
  
  public static final Color TEXT_DIM;
  
  private static TextButton.TextButtonStyle btnStyle;
  
  private static CheckBox.CheckBoxStyle checkStyle;
  
  private static CheckBox.CheckBoxStyle checkStyleBare;
  
  private static float nextTick;
  
  private static final Seq registered;
  
  private static final WeakHashMap styled;
  
  private static TextButton.TextButtonStyle toggleStyle;
  
  static {
    BTN = Color.valueOf("262d39");
    BTN_OVER = Color.valueOf("323b4b");
    DIM = Color.valueOf("5d6676");
    TEXT_DIM = Color.valueOf("98a2b3");
    WeakHashMap<Object, Object> weakHashMap = new WeakHashMap<Object, Object>();
    this();
    styled = weakHashMap;
    Seq seq = new Seq();
    this();
    registered = seq;
    nextTick = 0.0F;
  }
  
  private static float alpha() {
    return (Draw.getColor()).a;
  }
  
  public static void banner(Table paramTable, String paramString1, String paramString2) {
    UiStyle$Pad uiStyle$Pad = new UiStyle$Pad();
    Color color = Pal.accent.cpy().mul(0.3F);
    this(color, 1.0F, 12.0F, 8.0F, false);
    UiStyle$3 uiStyle$3 = new UiStyle$3();
    this(paramString1, paramString2);
    paramTable.table((Drawable)uiStyle$Pad, uiStyle$3).growX().padBottom(2.0F).row();
  }
  
  private static void copyText(TextButton.TextButtonStyle paramTextButtonStyle1, TextButton.TextButtonStyle paramTextButtonStyle2) {
    Font font = paramTextButtonStyle1.font;
    paramTextButtonStyle2.font = font;
    Color color2 = paramTextButtonStyle1.fontColor;
    paramTextButtonStyle2.fontColor = color2;
    color2 = paramTextButtonStyle1.downFontColor;
    paramTextButtonStyle2.downFontColor = color2;
    color2 = paramTextButtonStyle1.overFontColor;
    paramTextButtonStyle2.overFontColor = color2;
    color2 = paramTextButtonStyle1.checkedFontColor;
    paramTextButtonStyle2.checkedFontColor = color2;
    color2 = paramTextButtonStyle1.checkedOverFontColor;
    paramTextButtonStyle2.checkedOverFontColor = color2;
    Color color1 = paramTextButtonStyle1.disabledFontColor;
    paramTextButtonStyle2.disabledFontColor = color1;
  }
  
  public static void decorate(Element paramElement) {
    try {
      return;
    } finally {
      paramElement = null;
      Log.err("[Lunarians] UiStyle decorate", (Throwable)paramElement);
    } 
  }
  
  private static void decorateDialog(BaseDialog paramBaseDialog) {
    WeakHashMap<BaseDialog, Boolean> weakHashMap = styled;
    boolean bool = weakHashMap.containsKey(paramBaseDialog);
    if (!bool) {
      Boolean bool1 = Boolean.TRUE;
      weakHashMap.put(paramBaseDialog, bool1);
      try {
        Label label = paramBaseDialog.title;
        Color color = Pal.accent;
        label.setColor(color);
        label = paramBaseDialog.title;
        int i = 1066192077;
        float f = 1.1F;
        label.setFontScale(f);
      } finally {}
    } 
    decorate((Element)paramBaseDialog.cont);
  }
  
  private static void ensureStyles() {
    CheckBox.CheckBoxStyle checkBoxStyle1 = checkStyle;
    if (checkBoxStyle1 != null)
      return; 
    TextButton.TextButtonStyle textButtonStyle1 = Styles.defaultt;
    CheckBox.CheckBoxStyle checkBoxStyle2 = new CheckBox.CheckBoxStyle();
    this();
    checkStyle = checkBoxStyle2;
    copyText(textButtonStyle1, (TextButton.TextButtonStyle)checkBoxStyle2);
    checkBoxStyle2 = checkStyle;
    UiStyle$Pad uiStyle$Pad2 = new UiStyle$Pad();
    Color color2 = CARD;
    this(color2, 0.94F, 8.0F, 5.0F, false);
    checkBoxStyle2.up = (Drawable)uiStyle$Pad2;
    checkBoxStyle2 = checkStyle;
    UiStyle$Pad uiStyle$Pad4 = new UiStyle$Pad();
    Color color5 = BTN_OVER;
    this(color5, 0.94F, 8.0F, 5.0F, false);
    checkBoxStyle2.over = (Drawable)uiStyle$Pad4;
    checkBoxStyle2 = checkStyle;
    Drawable drawable2 = checkBoxStyle2.over;
    checkBoxStyle2.down = drawable2;
    checkBoxStyle2 = checkStyle;
    drawable2 = checkBoxStyle2.up;
    checkBoxStyle2.checked = drawable2;
    checkBoxStyle2 = checkStyle;
    drawable2 = checkBoxStyle2.over;
    checkBoxStyle2.checkedOver = drawable2;
    checkBoxStyle2 = checkStyle;
    UiStyle$ToggleBox uiStyle$ToggleBox = new UiStyle$ToggleBox();
    boolean bool = true;
    this(bool, false);
    checkBoxStyle2.checkboxOn = (Drawable)uiStyle$ToggleBox;
    checkBoxStyle2 = checkStyle;
    uiStyle$ToggleBox = new UiStyle$ToggleBox();
    this(bool, bool);
    checkBoxStyle2.checkboxOnOver = (Drawable)uiStyle$ToggleBox;
    checkBoxStyle2 = checkStyle;
    uiStyle$ToggleBox = new UiStyle$ToggleBox();
    this(false, false);
    checkBoxStyle2.checkboxOff = (Drawable)uiStyle$ToggleBox;
    checkBoxStyle2 = checkStyle;
    uiStyle$ToggleBox = new UiStyle$ToggleBox();
    this(false, bool);
    checkBoxStyle2.checkboxOver = (Drawable)uiStyle$ToggleBox;
    checkBoxStyle2 = checkStyle;
    Drawable drawable1 = checkBoxStyle2.checkboxOn;
    checkBoxStyle2.checkboxOnDisabled = drawable1;
    checkBoxStyle2 = checkStyle;
    drawable1 = checkBoxStyle2.checkboxOff;
    checkBoxStyle2.checkboxOffDisabled = drawable1;
    checkBoxStyle2 = new CheckBox.CheckBoxStyle();
    this();
    checkStyleBare = checkBoxStyle2;
    copyText(textButtonStyle1, (TextButton.TextButtonStyle)checkBoxStyle2);
    checkStyleBare.up = null;
    checkStyleBare.over = null;
    checkStyleBare.down = null;
    checkStyleBare.checked = null;
    checkStyleBare.checkedOver = null;
    checkBoxStyle2 = checkStyleBare;
    drawable1 = checkStyle.checkboxOn;
    checkBoxStyle2.checkboxOn = drawable1;
    checkBoxStyle2 = checkStyleBare;
    drawable1 = checkStyle.checkboxOnOver;
    checkBoxStyle2.checkboxOnOver = drawable1;
    checkBoxStyle2 = checkStyleBare;
    drawable1 = checkStyle.checkboxOff;
    checkBoxStyle2.checkboxOff = drawable1;
    checkBoxStyle2 = checkStyleBare;
    drawable1 = checkStyle.checkboxOver;
    checkBoxStyle2.checkboxOver = drawable1;
    checkBoxStyle2 = checkStyleBare;
    drawable1 = checkStyle.checkboxOn;
    checkBoxStyle2.checkboxOnDisabled = drawable1;
    checkBoxStyle2 = checkStyleBare;
    drawable1 = checkStyle.checkboxOff;
    checkBoxStyle2.checkboxOffDisabled = drawable1;
    TextButton.TextButtonStyle textButtonStyle2 = new TextButton.TextButtonStyle();
    this(textButtonStyle1);
    btnStyle = textButtonStyle2;
    UiStyle$Pad uiStyle$Pad6 = new UiStyle$Pad();
    Color color7 = BTN;
    this(color7, 1.0F, 10.0F, 4.0F, false);
    textButtonStyle2.up = (Drawable)uiStyle$Pad6;
    textButtonStyle1 = btnStyle;
    Color color6 = color5;
    UiStyle$Pad uiStyle$Pad5 = new UiStyle$Pad();
    this(color6, 1.0F, 10.0F, 4.0F, false);
    textButtonStyle1.over = (Drawable)uiStyle$Pad5;
    textButtonStyle1 = btnStyle;
    uiStyle$Pad4 = new UiStyle$Pad();
    Color color4 = Pal.accent;
    this(color4, 0.6F, 10.0F, 4.0F, false);
    textButtonStyle1.down = (Drawable)uiStyle$Pad4;
    textButtonStyle1 = btnStyle;
    Color color3 = color2;
    UiStyle$Pad uiStyle$Pad3 = new UiStyle$Pad();
    this(color3, 0.7F, 10.0F, 4.0F, false);
    textButtonStyle1.disabled = (Drawable)uiStyle$Pad3;
    textButtonStyle1 = btnStyle;
    UiStyle$Pad uiStyle$Pad1 = new UiStyle$Pad();
    Color color1 = Pal.accent;
    float f1 = 4.0F;
    float f2 = 10.0F;
    this(color1, 0.55F, f2, f1, false);
    textButtonStyle1.checked = (Drawable)uiStyle$Pad1;
    textButtonStyle1 = btnStyle;
    uiStyle$Pad1 = new UiStyle$Pad();
    color1 = Pal.accent;
    this(color1, 0.7F, f2, f1, false);
    textButtonStyle1.checkedOver = (Drawable)uiStyle$Pad1;
    toggleStyle = btnStyle;
  }
  
  private static boolean hasCheck(Table paramTable) {
    Iterator<Element> iterator = paramTable.getChildren().iterator();
    while (true) {
      boolean bool = iterator.hasNext();
      if (bool) {
        Element element = iterator.next();
        bool = element instanceof CheckBox;
        if (bool)
          return true; 
        continue;
      } 
      return false;
    } 
  }
  
  private static boolean hasControls(Table paramTable) {
    Iterator<Element> iterator = paramTable.getChildren().iterator();
    while (true) {
      boolean bool = iterator.hasNext();
      if (bool) {
        Element element = iterator.next();
        boolean bool1 = element instanceof TextButton;
        if (!bool1) {
          bool1 = element instanceof arc.scene.ui.ImageButton;
          if (!bool1) {
            bool1 = element instanceof arc.scene.ui.Slider;
            if (!bool1) {
              bool = element instanceof arc.scene.ui.TextField;
              if (bool)
                continue; 
              continue;
            } 
            continue;
          } 
          continue;
        } 
        return true;
      } 
      return false;
    } 
  }
  
  public static void install() {
    EventType.Trigger trigger = EventType.Trigger.update;
    UiStyle$0 uiStyle$0 = new UiStyle$0();
    this();
    Events.run(trigger, uiStyle$0);
  }
  
  public static Drawable panelBg() {
    UiStyle$Pad uiStyle$Pad = new UiStyle$Pad();
    Color color = BG;
    this(color, 0.95F, 6.0F, 6.0F, true);
    return (Drawable)uiStyle$Pad;
  }
  
  public static void register(BaseDialog paramBaseDialog) {
    if (paramBaseDialog != null) {
      Seq seq = registered;
      boolean bool = seq.contains(paramBaseDialog, true);
      if (!bool)
        seq.add(paramBaseDialog); 
    } 
  }
  
  public static void section(Table paramTable, String paramString) {
    UiStyle$2 uiStyle$2 = new UiStyle$2();
    this(paramString);
    Cell cell = paramTable.table(uiStyle$2).growX().padTop(11.0F).padBottom(3.0F);
    float f = 2.0F;
    cell.padLeft(f).padRight(f).row();
  }
  
  private static void tick() {
    // Byte code:
    //   0: getstatic arc/util/Time.time : F
    //   3: fstore_0
    //   4: getstatic lunarians/UiStyle.nextTick : F
    //   7: fstore_1
    //   8: fload_0
    //   9: fload_1
    //   10: fcmpg
    //   11: istore_2
    //   12: iload_2
    //   13: iflt -> 220
    //   16: getstatic arc/Core.scene : Larc/scene/Scene;
    //   19: astore_3
    //   20: aload_3
    //   21: ifnull -> 220
    //   24: getstatic arc/util/Time.time : F
    //   27: fstore_0
    //   28: ldc_w 1094713344
    //   31: istore #4
    //   33: ldc 12.0
    //   35: fstore_1
    //   36: fload_0
    //   37: fload_1
    //   38: fadd
    //   39: fstore_0
    //   40: fload_0
    //   41: putstatic lunarians/UiStyle.nextTick : F
    //   44: getstatic arc/Core.scene : Larc/scene/Scene;
    //   47: astore_3
    //   48: aload_3
    //   49: getfield root : Larc/scene/Group;
    //   52: astore_3
    //   53: aload_3
    //   54: invokevirtual getChildren : ()Larc/struct/SnapshotSeq;
    //   57: astore_3
    //   58: aload_3
    //   59: invokevirtual iterator : ()Ljava/util/Iterator;
    //   62: astore_3
    //   63: aload_3
    //   64: invokeinterface hasNext : ()Z
    //   69: istore #4
    //   71: iload #4
    //   73: ifeq -> 186
    //   76: aload_3
    //   77: invokeinterface next : ()Ljava/lang/Object;
    //   82: astore #5
    //   84: aload #5
    //   86: checkcast arc/scene/Element
    //   89: astore #5
    //   91: aload #5
    //   93: instanceof mindustry/ui/dialogs/BaseDialog
    //   96: istore #6
    //   98: iload #6
    //   100: ifeq -> 63
    //   103: aload #5
    //   105: invokevirtual getClass : ()Ljava/lang/Class;
    //   108: astore #7
    //   110: aload #7
    //   112: invokevirtual getName : ()Ljava/lang/String;
    //   115: astore #7
    //   117: ldc_w 'lunarians'
    //   120: astore #8
    //   122: aload #7
    //   124: aload #8
    //   126: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   129: istore #6
    //   131: iload #6
    //   133: ifne -> 171
    //   136: getstatic lunarians/UiStyle.registered : Larc/struct/Seq;
    //   139: astore #7
    //   141: aload #5
    //   143: astore #8
    //   145: aload #5
    //   147: checkcast mindustry/ui/dialogs/BaseDialog
    //   150: astore #8
    //   152: iconst_1
    //   153: istore #9
    //   155: aload #7
    //   157: aload #8
    //   159: iload #9
    //   161: invokevirtual contains : (Ljava/lang/Object;Z)Z
    //   164: istore #6
    //   166: iload #6
    //   168: ifeq -> 63
    //   171: aload #5
    //   173: checkcast mindustry/ui/dialogs/BaseDialog
    //   176: astore #5
    //   178: aload #5
    //   180: invokestatic decorateDialog : (Lmindustry/ui/dialogs/BaseDialog;)V
    //   183: goto -> 63
    //   186: getstatic lunarians/UiStyle.registered : Larc/struct/Seq;
    //   189: astore_3
    //   190: new lunarians/UiStyle$1
    //   193: astore #5
    //   195: aload #5
    //   197: invokespecial <init> : ()V
    //   200: aload_3
    //   201: aload #5
    //   203: invokevirtual removeAll : (Larc/func/Boolf;)Larc/struct/Seq;
    //   206: pop
    //   207: return
    //   208: astore_3
    //   209: ldc_w '[Lunarians] UiStyle tick'
    //   212: astore #5
    //   214: aload #5
    //   216: aload_3
    //   217: invokestatic err : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   220: return
    // Exception table:
    //   from	to	target	type
    //   44	47	208	finally
    //   48	52	208	finally
    //   53	57	208	finally
    //   58	62	208	finally
    //   63	69	208	finally
    //   76	82	208	finally
    //   84	89	208	finally
    //   103	108	208	finally
    //   110	115	208	finally
    //   124	129	208	finally
    //   136	139	208	finally
    //   145	150	208	finally
    //   159	164	208	finally
    //   171	176	208	finally
    //   178	183	208	finally
    //   186	189	208	finally
    //   190	193	208	finally
    //   195	200	208	finally
    //   201	207	208	finally
  }
  
  private static void walk(Element paramElement, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: ifnull -> 463
    //   4: aload_0
    //   5: instanceof arc/scene/ui/CheckBox
    //   8: istore_2
    //   9: iload_2
    //   10: ifeq -> 71
    //   13: aload_0
    //   14: checkcast arc/scene/ui/CheckBox
    //   17: astore_0
    //   18: getstatic lunarians/UiStyle.styled : Ljava/util/WeakHashMap;
    //   21: astore_3
    //   22: aload_3
    //   23: aload_0
    //   24: invokevirtual containsKey : (Ljava/lang/Object;)Z
    //   27: istore #4
    //   29: iload #4
    //   31: ifne -> 463
    //   34: getstatic java/lang/Boolean.TRUE : Ljava/lang/Boolean;
    //   37: astore #5
    //   39: aload_3
    //   40: aload_0
    //   41: aload #5
    //   43: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   46: pop
    //   47: iload_1
    //   48: ifeq -> 59
    //   51: getstatic lunarians/UiStyle.checkStyleBare : Larc/scene/ui/CheckBox$CheckBoxStyle;
    //   54: astore #6
    //   56: goto -> 64
    //   59: getstatic lunarians/UiStyle.checkStyle : Larc/scene/ui/CheckBox$CheckBoxStyle;
    //   62: astore #6
    //   64: aload_0
    //   65: aload #6
    //   67: invokevirtual setStyle : (Larc/scene/ui/Button$ButtonStyle;)V
    //   70: return
    //   71: aload_0
    //   72: instanceof arc/scene/ui/TextButton
    //   75: istore_2
    //   76: iload_2
    //   77: ifeq -> 166
    //   80: aload_0
    //   81: checkcast arc/scene/ui/TextButton
    //   84: astore_0
    //   85: getstatic lunarians/UiStyle.styled : Ljava/util/WeakHashMap;
    //   88: astore #6
    //   90: aload #6
    //   92: aload_0
    //   93: invokevirtual containsKey : (Ljava/lang/Object;)Z
    //   96: istore_2
    //   97: iload_2
    //   98: ifne -> 463
    //   101: getstatic java/lang/Boolean.TRUE : Ljava/lang/Boolean;
    //   104: astore_3
    //   105: aload #6
    //   107: aload_0
    //   108: aload_3
    //   109: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   112: pop
    //   113: aload_0
    //   114: invokevirtual getStyle : ()Larc/scene/ui/TextButton$TextButtonStyle;
    //   117: astore #6
    //   119: aload #6
    //   121: ifnull -> 463
    //   124: aload #6
    //   126: getfield up : Larc/scene/style/Drawable;
    //   129: astore_3
    //   130: aload_3
    //   131: ifnull -> 463
    //   134: aload #6
    //   136: getfield checked : Larc/scene/style/Drawable;
    //   139: astore #6
    //   141: aload #6
    //   143: ifnull -> 154
    //   146: getstatic lunarians/UiStyle.toggleStyle : Larc/scene/ui/TextButton$TextButtonStyle;
    //   149: astore #6
    //   151: goto -> 159
    //   154: getstatic lunarians/UiStyle.btnStyle : Larc/scene/ui/TextButton$TextButtonStyle;
    //   157: astore #6
    //   159: aload_0
    //   160: aload #6
    //   162: invokevirtual setStyle : (Larc/scene/ui/Button$ButtonStyle;)V
    //   165: return
    //   166: aload_0
    //   167: instanceof arc/scene/ui/ScrollPane
    //   170: istore_2
    //   171: iload_2
    //   172: ifeq -> 187
    //   175: aload_0
    //   176: checkcast arc/scene/ui/ScrollPane
    //   179: invokevirtual getWidget : ()Larc/scene/Element;
    //   182: iload_1
    //   183: invokestatic walk : (Larc/scene/Element;Z)V
    //   186: return
    //   187: aload_0
    //   188: instanceof arc/scene/ui/layout/Table
    //   191: istore_2
    //   192: iload_2
    //   193: ifeq -> 414
    //   196: aload_0
    //   197: checkcast arc/scene/ui/layout/Table
    //   200: astore_0
    //   201: iconst_1
    //   202: istore_2
    //   203: iload_1
    //   204: ifne -> 257
    //   207: aload_0
    //   208: invokevirtual getBackground : ()Larc/scene/style/Drawable;
    //   211: astore #7
    //   213: aload #7
    //   215: ifnonnull -> 257
    //   218: aload_0
    //   219: instanceof arc/scene/ui/Button
    //   222: istore #8
    //   224: iload #8
    //   226: ifne -> 257
    //   229: aload_0
    //   230: invokestatic hasControls : (Larc/scene/ui/layout/Table;)Z
    //   233: istore #8
    //   235: iload #8
    //   237: ifne -> 251
    //   240: aload_0
    //   241: invokestatic hasCheck : (Larc/scene/ui/layout/Table;)Z
    //   244: istore #8
    //   246: iload #8
    //   248: ifeq -> 257
    //   251: iconst_1
    //   252: istore #8
    //   254: goto -> 263
    //   257: iconst_0
    //   258: istore #8
    //   260: aconst_null
    //   261: astore #7
    //   263: iload #8
    //   265: ifeq -> 324
    //   268: getstatic lunarians/UiStyle.styled : Ljava/util/WeakHashMap;
    //   271: astore #9
    //   273: aload #9
    //   275: aload_0
    //   276: invokevirtual containsKey : (Ljava/lang/Object;)Z
    //   279: istore #10
    //   281: iload #10
    //   283: ifne -> 324
    //   286: getstatic java/lang/Boolean.TRUE : Ljava/lang/Boolean;
    //   289: astore #11
    //   291: aload #9
    //   293: aload_0
    //   294: aload #11
    //   296: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   299: pop
    //   300: new lunarians/UiStyle$CardDrawable
    //   303: astore #9
    //   305: aload #9
    //   307: aload_0
    //   308: checkcast arc/scene/ui/layout/Table
    //   311: invokespecial <init> : (Larc/scene/ui/layout/Table;)V
    //   314: aload_0
    //   315: checkcast arc/scene/ui/layout/Table
    //   318: aload #9
    //   320: invokevirtual background : (Larc/scene/style/Drawable;)Larc/scene/ui/layout/Table;
    //   323: pop
    //   324: iload_1
    //   325: ifne -> 372
    //   328: iload #8
    //   330: ifne -> 372
    //   333: getstatic lunarians/UiStyle.styled : Ljava/util/WeakHashMap;
    //   336: astore #6
    //   338: aload #6
    //   340: aload_0
    //   341: invokevirtual containsKey : (Ljava/lang/Object;)Z
    //   344: istore_1
    //   345: iload_1
    //   346: ifeq -> 368
    //   349: aload_0
    //   350: checkcast arc/scene/ui/layout/Table
    //   353: invokevirtual getBackground : ()Larc/scene/style/Drawable;
    //   356: astore #6
    //   358: aload #6
    //   360: instanceof lunarians/UiStyle$CardDrawable
    //   363: istore_1
    //   364: iload_1
    //   365: ifne -> 372
    //   368: iconst_0
    //   369: istore_2
    //   370: aconst_null
    //   371: astore_3
    //   372: aload_0
    //   373: checkcast arc/scene/ui/layout/Table
    //   376: invokevirtual getChildren : ()Larc/struct/SnapshotSeq;
    //   379: invokevirtual iterator : ()Ljava/util/Iterator;
    //   382: astore_0
    //   383: aload_0
    //   384: invokeinterface hasNext : ()Z
    //   389: istore_1
    //   390: iload_1
    //   391: ifeq -> 463
    //   394: aload_0
    //   395: invokeinterface next : ()Ljava/lang/Object;
    //   400: checkcast arc/scene/Element
    //   403: astore #6
    //   405: aload #6
    //   407: iload_2
    //   408: invokestatic walk : (Larc/scene/Element;Z)V
    //   411: goto -> 383
    //   414: aload_0
    //   415: instanceof arc/scene/Group
    //   418: istore_2
    //   419: iload_2
    //   420: ifeq -> 463
    //   423: aload_0
    //   424: checkcast arc/scene/Group
    //   427: invokevirtual getChildren : ()Larc/struct/SnapshotSeq;
    //   430: invokevirtual iterator : ()Ljava/util/Iterator;
    //   433: astore_0
    //   434: aload_0
    //   435: invokeinterface hasNext : ()Z
    //   440: istore_2
    //   441: iload_2
    //   442: ifeq -> 463
    //   445: aload_0
    //   446: invokeinterface next : ()Ljava/lang/Object;
    //   451: checkcast arc/scene/Element
    //   454: astore_3
    //   455: aload_3
    //   456: iload_1
    //   457: invokestatic walk : (Larc/scene/Element;Z)V
    //   460: goto -> 434
    //   463: return
  }
}
