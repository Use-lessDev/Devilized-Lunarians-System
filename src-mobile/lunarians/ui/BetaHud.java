package lunarians.ui;

import arc.Core;
import arc.Graphics;
import arc.func.Boolc;
import arc.func.Prov;
import arc.scene.Action;
import arc.scene.Element;
import arc.scene.actions.Actions;
import arc.scene.actions.AlphaAction;
import arc.scene.actions.RunnableAction;
import arc.scene.actions.SequenceAction;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import lunarians.Formations;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import lunarians.UiStyle;
import mindustry.gen.Icon;
import mindustry.ui.Styles;

public class BetaHud extends Table {
  private boolean open;
  
  private final Table panel;
  
  private final ScrollPane scroll;
  
  private Cell scrollCell;
  
  private final ImageButton toggleBtn;
  
  public BetaHud() {
    Cell cell1;
    Table table1 = null;
    this.open = false;
    int i = 1;
    float f1 = Float.MIN_VALUE;
    setFillParent(i);
    right().top();
    defaults().right();
    Table table2 = new Table();
    Drawable drawable = UiStyle.panelBg();
    this(drawable);
    this.panel = table2;
    table2.visible = false;
    table2.defaults().growX().pad(4.0F);
    ImageButton imageButton1 = new ImageButton();
    TextureRegionDrawable textureRegionDrawable = Icon.rightOpen;
    ImageButton.ImageButtonStyle imageButtonStyle = Styles.clearNonei;
    this((Drawable)textureRegionDrawable, imageButtonStyle);
    this.toggleBtn = imageButton1;
    BetaHud$1 betaHud$1 = new BetaHud$1();
    this(this);
    imageButton1.clicked(betaHud$1);
    float f2 = 28.0F;
    try {
      imageButton1.resizeImage(f2);
    } finally {}
    ScrollPane scrollPane = new ScrollPane();
    Table table3 = this.panel;
    ScrollPane.ScrollPaneStyle scrollPaneStyle = Styles.smallPane;
    this((Element)table3, scrollPaneStyle);
    this.scroll = scrollPane;
    scrollPane.setScrollingDisabled(i, false);
    scrollPane.setFadeScrollBars(false);
    scrollPane.visible = false;
    table1 = new Table();
    this();
    Cell cell3 = table1.add((Element)scrollPane).top();
    this.scrollCell = cell3;
    applySize();
    ImageButton imageButton2 = this.toggleBtn;
    Cell cell2 = table1.add((Element)imageButton2);
    float f3 = 40.0F;
    cell2 = cell2.size(f3, f3).top();
    f3 = 6.0F;
    cell2.pad(f3);
  }
  
  private void addCheck(String paramString, boolean paramBoolean, Boolc paramBoolc) {
    float f = 4.0F;
    try {
      Table table = this.panel;
      Cell cell = UiText.checkSmall(table, paramString, paramBoolean, paramBoolc);
      cell = cell.left();
      return;
    } finally {
      null;
      this.panel.check(paramString, paramBoolean, paramBoolc).left().pad(f).row();
    } 
  }
  
  private void addCheckCount(String paramString, boolean paramBoolean, Boolc paramBoolc, Prov paramProv) {
    addCheck(paramString, paramBoolean, paramBoolc);
    try {
      Table table = this.panel;
      Cell cell = table.label(paramProv);
      cell = cell.left();
      int i = 1092616192;
      float f = 10.0F;
      cell = cell.padLeft(f);
      cell.row();
    } finally {}
  }
  
  private void rebuildPanel() {
    // Byte code:
    //   0: aload_0
    //   1: astore_1
    //   2: ldc 'Conveyor'
    //   4: astore_2
    //   5: ldc 'D+C'
    //   7: astore_3
    //   8: ldc 'Drill'
    //   10: astore #4
    //   12: aload_0
    //   13: getfield panel : Larc/scene/ui/layout/Table;
    //   16: invokevirtual clear : ()V
    //   19: aload_0
    //   20: getfield panel : Larc/scene/ui/layout/Table;
    //   23: astore #5
    //   25: ldc 'beta_features'
    //   27: ldc 'Beta features'
    //   29: invokestatic safeI18n : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   32: astore #6
    //   34: aload #5
    //   36: ldc 'LUNARIANS'
    //   38: aload #6
    //   40: invokestatic banner : (Larc/scene/ui/layout/Table;Ljava/lang/String;Ljava/lang/String;)V
    //   43: aload_0
    //   44: getfield panel : Larc/scene/ui/layout/Table;
    //   47: astore #5
    //   49: ldc 'Modo'
    //   51: astore #7
    //   53: ldc 'Mode'
    //   55: aload #7
    //   57: invokestatic t : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   60: astore #6
    //   62: aload #5
    //   64: aload #6
    //   66: invokestatic section : (Larc/scene/ui/layout/Table;Ljava/lang/String;)V
    //   69: getstatic lunarians/LunariansSettings.pvpMode : Z
    //   72: istore #8
    //   74: iload #8
    //   76: ifeq -> 86
    //   79: ldc 'Balanced ON'
    //   81: astore #5
    //   83: goto -> 90
    //   86: ldc 'Normal'
    //   88: astore #5
    //   90: aload_1
    //   91: getfield panel : Larc/scene/ui/layout/Table;
    //   94: astore #6
    //   96: new lunarians/ui/BetaHud$3
    //   99: astore #7
    //   101: aload #7
    //   103: aload_1
    //   104: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   107: aload #6
    //   109: aload #5
    //   111: aload #7
    //   113: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   116: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   119: ldc_w 42.0
    //   122: invokevirtual height : (F)Larc/scene/ui/layout/Cell;
    //   125: astore #5
    //   127: ldc 4.0
    //   129: fstore #9
    //   131: aload #5
    //   133: fload #9
    //   135: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   138: invokevirtual get : ()Larc/scene/Element;
    //   141: checkcast arc/scene/ui/TextButton
    //   144: astore #5
    //   146: getstatic lunarians/LunariansSettings.pvpMode : Z
    //   149: istore #10
    //   151: iload #10
    //   153: ifeq -> 164
    //   156: getstatic arc/graphics/Color.scarlet : Larc/graphics/Color;
    //   159: astore #7
    //   161: goto -> 169
    //   164: getstatic mindustry/graphics/Pal.accent : Larc/graphics/Color;
    //   167: astore #7
    //   169: aload #5
    //   171: aload #7
    //   173: invokevirtual setColor : (Larc/graphics/Color;)V
    //   176: iconst_1
    //   177: istore #10
    //   179: ldc 1.4E-45
    //   181: fstore #11
    //   183: aload #5
    //   185: invokevirtual getLabel : ()Larc/scene/ui/Label;
    //   188: astore #5
    //   190: aload #5
    //   192: iload #10
    //   194: invokevirtual setWrap : (Z)V
    //   197: goto -> 201
    //   200: pop
    //   201: aload_1
    //   202: getfield panel : Larc/scene/ui/layout/Table;
    //   205: astore #5
    //   207: aload #5
    //   209: invokevirtual row : ()Larc/scene/ui/layout/Table;
    //   212: pop
    //   213: getstatic lunarians/LunariansSettings.pvpMode : Z
    //   216: istore #8
    //   218: iconst_0
    //   219: istore #12
    //   221: aconst_null
    //   222: astore #13
    //   224: ldc_w 1108344832
    //   227: istore #14
    //   229: ldc_w 36.0
    //   232: fstore #15
    //   234: ldc_w 2.0
    //   237: fstore #16
    //   239: ldc_w 52.0
    //   242: fstore #17
    //   244: iload #8
    //   246: ifeq -> 440
    //   249: new arc/scene/ui/layout/Table
    //   252: astore #5
    //   254: aload #5
    //   256: invokespecial <init> : ()V
    //   259: aload #5
    //   261: ldc_w 'Delay:'
    //   264: invokevirtual add : (Ljava/lang/CharSequence;)Larc/scene/ui/layout/Cell;
    //   267: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   270: fload #9
    //   272: invokevirtual padRight : (F)Larc/scene/ui/layout/Cell;
    //   275: pop
    //   276: new lunarians/ui/BetaHud$4
    //   279: astore #18
    //   281: aload #18
    //   283: aload_1
    //   284: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   287: ldc_w '-'
    //   290: astore #19
    //   292: aload #5
    //   294: aload #19
    //   296: aload #18
    //   298: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   301: astore #18
    //   303: ldc_w 1107296256
    //   306: istore #20
    //   308: ldc_w 32.0
    //   311: fstore #21
    //   313: aload #18
    //   315: fload #15
    //   317: fload #21
    //   319: invokevirtual size : (FF)Larc/scene/ui/layout/Cell;
    //   322: pop
    //   323: getstatic lunarians/LunariansSettings.pvpDelay : F
    //   326: fstore #22
    //   328: fload #22
    //   330: invokestatic valueOf : (F)Ljava/lang/Float;
    //   333: astore #18
    //   335: iload #10
    //   337: anewarray java/lang/Object
    //   340: astore #23
    //   342: aload #23
    //   344: iconst_0
    //   345: aload #18
    //   347: aastore
    //   348: ldc_w '%.1fs'
    //   351: aload #23
    //   353: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   356: astore #18
    //   358: aload #5
    //   360: aload #18
    //   362: invokevirtual add : (Ljava/lang/CharSequence;)Larc/scene/ui/layout/Cell;
    //   365: fload #17
    //   367: invokevirtual width : (F)Larc/scene/ui/layout/Cell;
    //   370: invokevirtual center : ()Larc/scene/ui/layout/Cell;
    //   373: fload #16
    //   375: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   378: pop
    //   379: new lunarians/ui/BetaHud$5
    //   382: astore #18
    //   384: aload #18
    //   386: aload_1
    //   387: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   390: ldc_w '+'
    //   393: astore #23
    //   395: aload #5
    //   397: aload #23
    //   399: aload #18
    //   401: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   404: fload #15
    //   406: fload #21
    //   408: invokevirtual size : (FF)Larc/scene/ui/layout/Cell;
    //   411: pop
    //   412: aload_1
    //   413: getfield panel : Larc/scene/ui/layout/Table;
    //   416: astore #18
    //   418: aload #18
    //   420: aload #5
    //   422: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   425: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   428: fload #9
    //   430: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   433: astore #5
    //   435: aload #5
    //   437: invokevirtual row : ()V
    //   440: aload_1
    //   441: getfield panel : Larc/scene/ui/layout/Table;
    //   444: astore #5
    //   446: ldc_w 'Configuration'
    //   449: ldc_w 'Configuração'
    //   452: invokestatic t : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   455: astore #18
    //   457: aload #5
    //   459: aload #18
    //   461: invokestatic section : (Larc/scene/ui/layout/Table;Ljava/lang/String;)V
    //   464: aload_1
    //   465: getfield panel : Larc/scene/ui/layout/Table;
    //   468: astore #5
    //   470: ldc_w 'settings_btn'
    //   473: ldc_w 'Settings'
    //   476: invokestatic safeI18n : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   479: astore #18
    //   481: new lunarians/ui/BetaHud$6
    //   484: astore #19
    //   486: aload #19
    //   488: aload_1
    //   489: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   492: aload #5
    //   494: aload #18
    //   496: aload #19
    //   498: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   501: astore #5
    //   503: ldc 40.0
    //   505: fstore #22
    //   507: aload #5
    //   509: fload #22
    //   511: invokevirtual height : (F)Larc/scene/ui/layout/Cell;
    //   514: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   517: fload #9
    //   519: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   522: invokevirtual get : ()Larc/scene/Element;
    //   525: checkcast arc/scene/ui/TextButton
    //   528: astore #5
    //   530: aload_1
    //   531: getfield panel : Larc/scene/ui/layout/Table;
    //   534: invokevirtual row : ()Larc/scene/ui/layout/Table;
    //   537: pop
    //   538: aload_1
    //   539: getfield panel : Larc/scene/ui/layout/Table;
    //   542: astore #5
    //   544: ldc_w 'Auto Set e economia'
    //   547: astore #23
    //   549: ldc_w 'Auto Set and economy'
    //   552: aload #23
    //   554: invokestatic t : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   557: astore #19
    //   559: aload #5
    //   561: aload #19
    //   563: invokestatic section : (Larc/scene/ui/layout/Table;Ljava/lang/String;)V
    //   566: new arc/scene/ui/layout/Table
    //   569: astore #5
    //   571: aload #5
    //   573: invokespecial <init> : ()V
    //   576: aload #5
    //   578: ldc_w 'Auto Set'
    //   581: invokevirtual add : (Ljava/lang/CharSequence;)Larc/scene/ui/layout/Cell;
    //   584: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   587: astore #19
    //   589: ldc 6.0
    //   591: fstore #24
    //   593: aload #19
    //   595: fload #24
    //   597: invokevirtual padRight : (F)Larc/scene/ui/layout/Cell;
    //   600: pop
    //   601: new lunarians/ui/BetaHud$7
    //   604: astore #19
    //   606: aload #19
    //   608: aload_1
    //   609: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   612: aload #5
    //   614: ldc_w 'GO'
    //   617: aload #19
    //   619: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   622: fload #17
    //   624: fload #22
    //   626: invokevirtual size : (FF)Larc/scene/ui/layout/Cell;
    //   629: fload #16
    //   631: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   634: pop
    //   635: new lunarians/ui/BetaHud$8
    //   638: astore #19
    //   640: aload #19
    //   642: aload_1
    //   643: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   646: ldc_w 'X'
    //   649: astore #25
    //   651: aload #5
    //   653: aload #25
    //   655: aload #19
    //   657: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   660: fload #22
    //   662: fload #22
    //   664: invokevirtual size : (FF)Larc/scene/ui/layout/Cell;
    //   667: astore #19
    //   669: aload #19
    //   671: fload #16
    //   673: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   676: pop
    //   677: new lunarians/ui/BetaHud$9
    //   680: astore #19
    //   682: aload #19
    //   684: aload_1
    //   685: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   688: aload #5
    //   690: aload #19
    //   692: invokevirtual label : (Larc/func/Prov;)Larc/scene/ui/layout/Cell;
    //   695: astore #19
    //   697: aload #19
    //   699: fload #9
    //   701: invokevirtual padLeft : (F)Larc/scene/ui/layout/Cell;
    //   704: pop
    //   705: aload_1
    //   706: getfield panel : Larc/scene/ui/layout/Table;
    //   709: astore #19
    //   711: aload #19
    //   713: aload #5
    //   715: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   718: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   721: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   724: fload #9
    //   726: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   729: invokevirtual row : ()V
    //   732: new arc/scene/ui/layout/Table
    //   735: astore #5
    //   737: aload #5
    //   739: invokespecial <init> : ()V
    //   742: getstatic lunarians/LunariansSettings.enableAutoDrill : Z
    //   745: istore #20
    //   747: new lunarians/ui/BetaHud$10
    //   750: astore #25
    //   752: aload #25
    //   754: aload_1
    //   755: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   758: aload #5
    //   760: aload #4
    //   762: iload #20
    //   764: aload #25
    //   766: invokestatic checkSmall : (Larc/scene/ui/layout/Table;Ljava/lang/String;ZLarc/func/Boolc;)Larc/scene/ui/layout/Cell;
    //   769: astore #19
    //   771: aload #19
    //   773: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   776: pop
    //   777: goto -> 816
    //   780: pop
    //   781: getstatic lunarians/LunariansSettings.enableAutoDrill : Z
    //   784: istore #20
    //   786: new lunarians/ui/BetaHud$11
    //   789: astore #25
    //   791: aload #25
    //   793: aload_1
    //   794: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   797: aload #5
    //   799: aload #4
    //   801: iload #20
    //   803: aload #25
    //   805: invokevirtual check : (Ljava/lang/String;ZLarc/func/Boolc;)Larc/scene/ui/layout/Cell;
    //   808: astore #4
    //   810: aload #4
    //   812: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   815: pop
    //   816: new lunarians/ui/BetaHud$12
    //   819: astore #4
    //   821: aload #4
    //   823: aload_1
    //   824: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   827: ldc_w 'Set'
    //   830: astore #19
    //   832: aload #5
    //   834: aload #19
    //   836: aload #4
    //   838: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   841: fload #17
    //   843: invokevirtual width : (F)Larc/scene/ui/layout/Cell;
    //   846: fload #22
    //   848: invokevirtual height : (F)Larc/scene/ui/layout/Cell;
    //   851: fload #9
    //   853: invokevirtual padLeft : (F)Larc/scene/ui/layout/Cell;
    //   856: pop
    //   857: new lunarians/ui/BetaHud$13
    //   860: astore #4
    //   862: aload #4
    //   864: aload_1
    //   865: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   868: ldc_w 'Gen'
    //   871: astore #25
    //   873: aload #5
    //   875: aload #25
    //   877: aload #4
    //   879: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   882: fload #17
    //   884: invokevirtual width : (F)Larc/scene/ui/layout/Cell;
    //   887: fload #22
    //   889: invokevirtual height : (F)Larc/scene/ui/layout/Cell;
    //   892: fload #16
    //   894: invokevirtual padLeft : (F)Larc/scene/ui/layout/Cell;
    //   897: pop
    //   898: aload_1
    //   899: getfield panel : Larc/scene/ui/layout/Table;
    //   902: aload #5
    //   904: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   907: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   910: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   913: fload #9
    //   915: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   918: invokevirtual row : ()V
    //   921: new arc/scene/ui/layout/Table
    //   924: astore #4
    //   926: aload #4
    //   928: invokespecial <init> : ()V
    //   931: aload #4
    //   933: ldc_w 'Exit'
    //   936: invokevirtual add : (Ljava/lang/CharSequence;)Larc/scene/ui/layout/Cell;
    //   939: fload #24
    //   941: invokevirtual padRight : (F)Larc/scene/ui/layout/Cell;
    //   944: pop
    //   945: ldc_w 'v'
    //   948: astore #26
    //   950: ldc_w '^'
    //   953: astore #27
    //   955: iconst_4
    //   956: anewarray java/lang/String
    //   959: dup
    //   960: dup2
    //   961: iconst_0
    //   962: ldc_w '>'
    //   965: aastore
    //   966: iconst_1
    //   967: aload #27
    //   969: aastore
    //   970: iconst_2
    //   971: ldc_w '<'
    //   974: aastore
    //   975: dup
    //   976: iconst_3
    //   977: aload #26
    //   979: aastore
    //   980: astore #5
    //   982: iconst_0
    //   983: istore #12
    //   985: aconst_null
    //   986: astore #13
    //   988: iconst_4
    //   989: istore #14
    //   991: ldc_w 5.6E-45
    //   994: fstore #15
    //   996: iload #12
    //   998: iload #14
    //   1000: if_icmpge -> 1114
    //   1003: aload #5
    //   1005: iload #12
    //   1007: aaload
    //   1008: astore #27
    //   1010: new lunarians/ui/BetaHud$14
    //   1013: astore #26
    //   1015: aload #26
    //   1017: aload_1
    //   1018: iload #12
    //   1020: invokespecial <init> : (Llunarians/ui/BetaHud;I)V
    //   1023: aload #4
    //   1025: aload #27
    //   1027: aload #26
    //   1029: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   1032: astore #27
    //   1034: ldc_w 1110441984
    //   1037: istore #28
    //   1039: ldc_w 44.0
    //   1042: fstore #29
    //   1044: aload #27
    //   1046: fload #29
    //   1048: fload #22
    //   1050: invokevirtual size : (FF)Larc/scene/ui/layout/Cell;
    //   1053: fload #16
    //   1055: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   1058: invokevirtual get : ()Larc/scene/Element;
    //   1061: checkcast arc/scene/ui/TextButton
    //   1064: astore #27
    //   1066: getstatic lunarians/LunariansSettings.drillExitDir : I
    //   1069: istore #28
    //   1071: iload #28
    //   1073: iload #12
    //   1075: if_icmpne -> 1093
    //   1078: getstatic mindustry/graphics/Pal.accent : Larc/graphics/Color;
    //   1081: astore #26
    //   1083: aload #27
    //   1085: aload #26
    //   1087: invokevirtual setColor : (Larc/graphics/Color;)V
    //   1090: goto -> 1105
    //   1093: getstatic arc/graphics/Color.lightGray : Larc/graphics/Color;
    //   1096: astore #26
    //   1098: aload #27
    //   1100: aload #26
    //   1102: invokevirtual setColor : (Larc/graphics/Color;)V
    //   1105: iload #12
    //   1107: iconst_1
    //   1108: iadd
    //   1109: istore #12
    //   1111: goto -> 988
    //   1114: getstatic lunarians/LunariansSettings.drillLayoutMode : I
    //   1117: istore #8
    //   1119: iload #8
    //   1121: iload #10
    //   1123: if_icmpne -> 1134
    //   1126: ldc_w 'Full'
    //   1129: astore #5
    //   1131: goto -> 1139
    //   1134: ldc_w 'Str'
    //   1137: astore #5
    //   1139: new lunarians/ui/BetaHud$15
    //   1142: astore #7
    //   1144: aload #7
    //   1146: aload_1
    //   1147: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   1150: aload #4
    //   1152: aload #5
    //   1154: aload #7
    //   1156: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   1159: astore #5
    //   1161: ldc_w 1113587712
    //   1164: istore #10
    //   1166: ldc_w 56.0
    //   1169: fstore #11
    //   1171: aload #5
    //   1173: fload #11
    //   1175: fload #22
    //   1177: invokevirtual size : (FF)Larc/scene/ui/layout/Cell;
    //   1180: fload #24
    //   1182: invokevirtual padLeft : (F)Larc/scene/ui/layout/Cell;
    //   1185: pop
    //   1186: aload_1
    //   1187: getfield panel : Larc/scene/ui/layout/Table;
    //   1190: astore #5
    //   1192: aload #5
    //   1194: aload #4
    //   1196: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   1199: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   1202: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   1205: fload #9
    //   1207: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   1210: invokevirtual row : ()V
    //   1213: new arc/scene/ui/layout/Table
    //   1216: astore #4
    //   1218: aload #4
    //   1220: invokespecial <init> : ()V
    //   1223: getstatic lunarians/LunariansSettings.enableAutoDrillConveyor : Z
    //   1226: istore #8
    //   1228: new lunarians/ui/BetaHud$16
    //   1231: astore #7
    //   1233: aload #7
    //   1235: aload_1
    //   1236: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   1239: aload #4
    //   1241: aload_3
    //   1242: iload #8
    //   1244: aload #7
    //   1246: invokestatic checkSmall : (Larc/scene/ui/layout/Table;Ljava/lang/String;ZLarc/func/Boolc;)Larc/scene/ui/layout/Cell;
    //   1249: astore #5
    //   1251: aload #5
    //   1253: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   1256: pop
    //   1257: goto -> 1293
    //   1260: pop
    //   1261: getstatic lunarians/LunariansSettings.enableAutoDrillConveyor : Z
    //   1264: istore #8
    //   1266: new lunarians/ui/BetaHud$17
    //   1269: astore #7
    //   1271: aload #7
    //   1273: aload_1
    //   1274: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   1277: aload #4
    //   1279: aload_3
    //   1280: iload #8
    //   1282: aload #7
    //   1284: invokevirtual check : (Ljava/lang/String;ZLarc/func/Boolc;)Larc/scene/ui/layout/Cell;
    //   1287: astore_3
    //   1288: aload_3
    //   1289: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   1292: pop
    //   1293: new lunarians/ui/BetaHud$18
    //   1296: astore_3
    //   1297: aload_3
    //   1298: aload_1
    //   1299: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   1302: aload #4
    //   1304: aload #25
    //   1306: aload_3
    //   1307: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   1310: fload #17
    //   1312: invokevirtual width : (F)Larc/scene/ui/layout/Cell;
    //   1315: fload #22
    //   1317: invokevirtual height : (F)Larc/scene/ui/layout/Cell;
    //   1320: fload #9
    //   1322: invokevirtual padLeft : (F)Larc/scene/ui/layout/Cell;
    //   1325: pop
    //   1326: aload_1
    //   1327: getfield panel : Larc/scene/ui/layout/Table;
    //   1330: aload #4
    //   1332: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   1335: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   1338: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   1341: fload #9
    //   1343: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   1346: invokevirtual row : ()V
    //   1349: new arc/scene/ui/layout/Table
    //   1352: astore_3
    //   1353: aload_3
    //   1354: invokespecial <init> : ()V
    //   1357: getstatic lunarians/LunariansSettings.language : Ljava/lang/String;
    //   1360: astore #4
    //   1362: ldc_w 'pt'
    //   1365: astore #5
    //   1367: aload #5
    //   1369: aload #4
    //   1371: invokevirtual equals : (Ljava/lang/Object;)Z
    //   1374: istore #30
    //   1376: iload #30
    //   1378: ifeq -> 1389
    //   1381: ldc_w 'Drills perfeitos (Beta)'
    //   1384: astore #4
    //   1386: goto -> 1394
    //   1389: ldc_w 'Perfect drills (Beta)'
    //   1392: astore #4
    //   1394: getstatic lunarians/LunariansSettings.drillPerfect : Z
    //   1397: istore #10
    //   1399: new lunarians/ui/BetaHud$19
    //   1402: astore #13
    //   1404: aload #13
    //   1406: aload_1
    //   1407: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   1410: aload_3
    //   1411: aload #4
    //   1413: iload #10
    //   1415: aload #13
    //   1417: invokevirtual check : (Ljava/lang/String;ZLarc/func/Boolc;)Larc/scene/ui/layout/Cell;
    //   1420: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   1423: pop
    //   1424: aload_1
    //   1425: getfield panel : Larc/scene/ui/layout/Table;
    //   1428: aload_3
    //   1429: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   1432: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   1435: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   1438: fload #9
    //   1440: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   1443: invokevirtual row : ()V
    //   1446: new arc/scene/ui/layout/Table
    //   1449: astore_3
    //   1450: aload_3
    //   1451: invokespecial <init> : ()V
    //   1454: getstatic lunarians/LunariansSettings.language : Ljava/lang/String;
    //   1457: astore #4
    //   1459: aload #5
    //   1461: aload #4
    //   1463: invokevirtual equals : (Ljava/lang/Object;)Z
    //   1466: istore #30
    //   1468: iload #30
    //   1470: ifeq -> 1481
    //   1473: ldc_w 'Auto Dock + Energia (Beta)'
    //   1476: astore #4
    //   1478: goto -> 1486
    //   1481: ldc_w 'Auto Dock + Energy (Beta)'
    //   1484: astore #4
    //   1486: getstatic lunarians/LunariansSettings.dockAuto : Z
    //   1489: istore #10
    //   1491: new lunarians/ui/BetaHud$20
    //   1494: astore #13
    //   1496: aload #13
    //   1498: aload_1
    //   1499: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   1502: aload_3
    //   1503: aload #4
    //   1505: iload #10
    //   1507: aload #13
    //   1509: invokevirtual check : (Ljava/lang/String;ZLarc/func/Boolc;)Larc/scene/ui/layout/Cell;
    //   1512: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   1515: pop
    //   1516: aload_1
    //   1517: getfield panel : Larc/scene/ui/layout/Table;
    //   1520: aload_3
    //   1521: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   1524: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   1527: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   1530: fload #9
    //   1532: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   1535: invokevirtual row : ()V
    //   1538: new arc/scene/ui/layout/Table
    //   1541: astore_3
    //   1542: aload_3
    //   1543: invokespecial <init> : ()V
    //   1546: getstatic lunarians/LunariansSettings.language : Ljava/lang/String;
    //   1549: astore #4
    //   1551: aload #5
    //   1553: aload #4
    //   1555: invokevirtual equals : (Ljava/lang/Object;)Z
    //   1558: istore #30
    //   1560: iload #30
    //   1562: ifeq -> 1573
    //   1565: ldc_w 'Economia em ordem (Beta)'
    //   1568: astore #4
    //   1570: goto -> 1578
    //   1573: ldc_w 'Ordered economy (Beta)'
    //   1576: astore #4
    //   1578: invokestatic enabled : ()Z
    //   1581: istore #10
    //   1583: new lunarians/ui/BetaHud$21
    //   1586: astore #13
    //   1588: aload #13
    //   1590: aload_1
    //   1591: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   1594: aload_3
    //   1595: aload #4
    //   1597: iload #10
    //   1599: aload #13
    //   1601: invokevirtual check : (Ljava/lang/String;ZLarc/func/Boolc;)Larc/scene/ui/layout/Cell;
    //   1604: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   1607: pop
    //   1608: aload_1
    //   1609: getfield panel : Larc/scene/ui/layout/Table;
    //   1612: aload_3
    //   1613: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   1616: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   1619: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   1622: fload #9
    //   1624: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   1627: invokevirtual row : ()V
    //   1630: aload_1
    //   1631: getfield panel : Larc/scene/ui/layout/Table;
    //   1634: astore_3
    //   1635: ldc_w 'Equipe e chat'
    //   1638: astore #7
    //   1640: ldc_w 'Team and chat'
    //   1643: aload #7
    //   1645: invokestatic t : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1648: astore #4
    //   1650: aload_3
    //   1651: aload #4
    //   1653: invokestatic section : (Larc/scene/ui/layout/Table;Ljava/lang/String;)V
    //   1656: new arc/scene/ui/layout/Table
    //   1659: astore_3
    //   1660: aload_3
    //   1661: invokespecial <init> : ()V
    //   1664: getstatic lunarians/LunariansSettings.language : Ljava/lang/String;
    //   1667: astore #4
    //   1669: aload #5
    //   1671: aload #4
    //   1673: invokevirtual equals : (Ljava/lang/Object;)Z
    //   1676: istore #30
    //   1678: iload #30
    //   1680: ifeq -> 1691
    //   1683: ldc_w 'Presença Lunarians (aura roxa)'
    //   1686: astore #4
    //   1688: goto -> 1696
    //   1691: ldc_w 'Lunarians presence (purple aura)'
    //   1694: astore #4
    //   1696: invokestatic enabled : ()Z
    //   1699: istore #10
    //   1701: new lunarians/ui/BetaHud$22
    //   1704: astore #13
    //   1706: aload #13
    //   1708: aload_1
    //   1709: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   1712: aload_3
    //   1713: aload #4
    //   1715: iload #10
    //   1717: aload #13
    //   1719: invokevirtual check : (Ljava/lang/String;ZLarc/func/Boolc;)Larc/scene/ui/layout/Cell;
    //   1722: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   1725: pop
    //   1726: aload_1
    //   1727: getfield panel : Larc/scene/ui/layout/Table;
    //   1730: aload_3
    //   1731: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   1734: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   1737: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   1740: fload #9
    //   1742: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   1745: invokevirtual row : ()V
    //   1748: new arc/scene/ui/layout/Table
    //   1751: astore_3
    //   1752: aload_3
    //   1753: invokespecial <init> : ()V
    //   1756: getstatic lunarians/LunariansSettings.language : Ljava/lang/String;
    //   1759: astore #4
    //   1761: aload #5
    //   1763: aload #4
    //   1765: invokevirtual equals : (Ljava/lang/Object;)Z
    //   1768: istore #30
    //   1770: iload #30
    //   1772: ifeq -> 1783
    //   1775: ldc_w 'Comandos por chat'
    //   1778: astore #4
    //   1780: goto -> 1788
    //   1783: ldc_w 'Chat commands'
    //   1786: astore #4
    //   1788: invokestatic on : ()Z
    //   1791: istore #8
    //   1793: new lunarians/ui/BetaHud$23
    //   1796: astore #7
    //   1798: aload #7
    //   1800: aload_1
    //   1801: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   1804: aload_3
    //   1805: aload #4
    //   1807: iload #8
    //   1809: aload #7
    //   1811: invokevirtual check : (Ljava/lang/String;ZLarc/func/Boolc;)Larc/scene/ui/layout/Cell;
    //   1814: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   1817: pop
    //   1818: new lunarians/ui/BetaHud$24
    //   1821: astore #4
    //   1823: aload #4
    //   1825: aload_1
    //   1826: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   1829: ldc_w '...'
    //   1832: astore #5
    //   1834: aload_3
    //   1835: aload #5
    //   1837: aload #4
    //   1839: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   1842: fload #17
    //   1844: invokevirtual width : (F)Larc/scene/ui/layout/Cell;
    //   1847: fload #22
    //   1849: invokevirtual height : (F)Larc/scene/ui/layout/Cell;
    //   1852: fload #9
    //   1854: invokevirtual padLeft : (F)Larc/scene/ui/layout/Cell;
    //   1857: pop
    //   1858: aload_1
    //   1859: getfield panel : Larc/scene/ui/layout/Table;
    //   1862: aload_3
    //   1863: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   1866: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   1869: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   1872: fload #9
    //   1874: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   1877: invokevirtual row : ()V
    //   1880: aload_1
    //   1881: getfield panel : Larc/scene/ui/layout/Table;
    //   1884: astore_3
    //   1885: ldc_w 'Units'
    //   1888: ldc_w 'Unidades'
    //   1891: invokestatic t : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1894: astore #4
    //   1896: aload_3
    //   1897: aload #4
    //   1899: invokestatic section : (Larc/scene/ui/layout/Table;Ljava/lang/String;)V
    //   1902: new arc/scene/ui/layout/Table
    //   1905: astore_3
    //   1906: aload_3
    //   1907: invokespecial <init> : ()V
    //   1910: invokestatic title : ()Ljava/lang/String;
    //   1913: astore #4
    //   1915: new java/lang/StringBuilder
    //   1918: astore #7
    //   1920: aload #7
    //   1922: invokespecial <init> : ()V
    //   1925: aload #7
    //   1927: aload #4
    //   1929: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1932: pop
    //   1933: aload #7
    //   1935: ldc_w ' (Beta)'
    //   1938: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1941: pop
    //   1942: aload #7
    //   1944: invokevirtual toString : ()Ljava/lang/String;
    //   1947: astore #4
    //   1949: invokestatic enabled : ()Z
    //   1952: istore #10
    //   1954: new lunarians/ui/BetaHud$25
    //   1957: astore #13
    //   1959: aload #13
    //   1961: aload_1
    //   1962: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   1965: aload_3
    //   1966: aload #4
    //   1968: iload #10
    //   1970: aload #13
    //   1972: invokevirtual check : (Ljava/lang/String;ZLarc/func/Boolc;)Larc/scene/ui/layout/Cell;
    //   1975: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   1978: pop
    //   1979: new lunarians/ui/BetaHud$26
    //   1982: astore #4
    //   1984: aload #4
    //   1986: aload_1
    //   1987: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   1990: aload_3
    //   1991: aload #5
    //   1993: aload #4
    //   1995: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   1998: fload #17
    //   2000: invokevirtual width : (F)Larc/scene/ui/layout/Cell;
    //   2003: fload #22
    //   2005: invokevirtual height : (F)Larc/scene/ui/layout/Cell;
    //   2008: fload #9
    //   2010: invokevirtual padLeft : (F)Larc/scene/ui/layout/Cell;
    //   2013: pop
    //   2014: aload_1
    //   2015: getfield panel : Larc/scene/ui/layout/Table;
    //   2018: aload_3
    //   2019: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   2022: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   2025: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   2028: fload #9
    //   2030: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   2033: invokevirtual row : ()V
    //   2036: new arc/scene/ui/layout/Table
    //   2039: astore_3
    //   2040: aload_3
    //   2041: invokespecial <init> : ()V
    //   2044: ldc_w 'Formation'
    //   2047: ldc_w 'Formação'
    //   2050: invokestatic t : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   2053: astore #4
    //   2055: aload_3
    //   2056: aload #4
    //   2058: invokevirtual add : (Ljava/lang/CharSequence;)Larc/scene/ui/layout/Cell;
    //   2061: astore #4
    //   2063: getstatic lunarians/UiStyle.TEXT_DIM : Larc/graphics/Color;
    //   2066: astore #7
    //   2068: aload #4
    //   2070: aload #7
    //   2072: invokevirtual color : (Larc/graphics/Color;)Larc/scene/ui/layout/Cell;
    //   2075: astore #4
    //   2077: ldc_w 1090519040
    //   2080: istore #10
    //   2082: ldc_w 8.0
    //   2085: fstore #11
    //   2087: aload #4
    //   2089: fload #11
    //   2091: invokevirtual padRight : (F)Larc/scene/ui/layout/Cell;
    //   2094: pop
    //   2095: iconst_0
    //   2096: istore #12
    //   2098: aconst_null
    //   2099: astore #13
    //   2101: iconst_3
    //   2102: istore #30
    //   2104: iload #12
    //   2106: iload #30
    //   2108: if_icmpgt -> 2211
    //   2111: iload #12
    //   2113: invokestatic modeName : (I)Ljava/lang/String;
    //   2116: astore #4
    //   2118: getstatic mindustry/ui/Styles.togglet : Larc/scene/ui/TextButton$TextButtonStyle;
    //   2121: astore #7
    //   2123: new lunarians/ui/BetaHud$27
    //   2126: astore #27
    //   2128: aload #27
    //   2130: aload_1
    //   2131: iload #12
    //   2133: invokespecial <init> : (Llunarians/ui/BetaHud;I)V
    //   2136: aload_3
    //   2137: aload #4
    //   2139: aload #7
    //   2141: aload #27
    //   2143: invokevirtual button : (Ljava/lang/String;Larc/scene/ui/TextButton$TextButtonStyle;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   2146: astore #4
    //   2148: new lunarians/ui/BetaHud$0
    //   2151: astore #7
    //   2153: aload #7
    //   2155: iload #12
    //   2157: invokespecial <init> : (I)V
    //   2160: aload #4
    //   2162: aload #7
    //   2164: invokevirtual checked : (Larc/func/Boolf;)Larc/scene/ui/layout/Cell;
    //   2167: ldc_w 36.0
    //   2170: invokevirtual height : (F)Larc/scene/ui/layout/Cell;
    //   2173: astore #4
    //   2175: ldc_w 1113063424
    //   2178: istore #10
    //   2180: ldc_w 54.0
    //   2183: fstore #11
    //   2185: aload #4
    //   2187: fload #11
    //   2189: invokevirtual minWidth : (F)Larc/scene/ui/layout/Cell;
    //   2192: astore #4
    //   2194: aload #4
    //   2196: fload #16
    //   2198: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   2201: pop
    //   2202: iload #12
    //   2204: iconst_1
    //   2205: iadd
    //   2206: istore #12
    //   2208: goto -> 2101
    //   2211: new lunarians/ui/BetaHud$28
    //   2214: astore #4
    //   2216: aload #4
    //   2218: aload_1
    //   2219: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   2222: aload_3
    //   2223: aload #5
    //   2225: aload #4
    //   2227: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   2230: astore #4
    //   2232: ldc_w 1110966272
    //   2235: istore #8
    //   2237: ldc_w 46.0
    //   2240: fstore #31
    //   2242: aload #4
    //   2244: fload #31
    //   2246: invokevirtual width : (F)Larc/scene/ui/layout/Cell;
    //   2249: astore #4
    //   2251: ldc_w 1108344832
    //   2254: istore #10
    //   2256: ldc_w 36.0
    //   2259: fstore #11
    //   2261: aload #4
    //   2263: fload #11
    //   2265: invokevirtual height : (F)Larc/scene/ui/layout/Cell;
    //   2268: fload #9
    //   2270: invokevirtual padLeft : (F)Larc/scene/ui/layout/Cell;
    //   2273: pop
    //   2274: aload_1
    //   2275: getfield panel : Larc/scene/ui/layout/Table;
    //   2278: astore #4
    //   2280: aload #4
    //   2282: aload_3
    //   2283: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   2286: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   2289: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   2292: fload #9
    //   2294: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   2297: invokevirtual row : ()V
    //   2300: new arc/scene/ui/layout/Table
    //   2303: astore_3
    //   2304: aload_3
    //   2305: invokespecial <init> : ()V
    //   2308: getstatic lunarians/LunariansSettings.enableAutoConnectConveyor : Z
    //   2311: istore #30
    //   2313: new lunarians/ui/BetaHud$29
    //   2316: astore #5
    //   2318: aload #5
    //   2320: aload_1
    //   2321: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   2324: aload_3
    //   2325: aload_2
    //   2326: iload #30
    //   2328: aload #5
    //   2330: invokestatic checkSmall : (Larc/scene/ui/layout/Table;Ljava/lang/String;ZLarc/func/Boolc;)Larc/scene/ui/layout/Cell;
    //   2333: astore #4
    //   2335: aload #4
    //   2337: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   2340: pop
    //   2341: goto -> 2376
    //   2344: pop
    //   2345: getstatic lunarians/LunariansSettings.enableAutoConnectConveyor : Z
    //   2348: istore #30
    //   2350: new lunarians/ui/BetaHud$30
    //   2353: astore #5
    //   2355: aload #5
    //   2357: aload_1
    //   2358: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   2361: aload_3
    //   2362: aload_2
    //   2363: iload #30
    //   2365: aload #5
    //   2367: invokevirtual check : (Ljava/lang/String;ZLarc/func/Boolc;)Larc/scene/ui/layout/Cell;
    //   2370: astore_2
    //   2371: aload_2
    //   2372: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   2375: pop
    //   2376: new lunarians/ui/BetaHud$31
    //   2379: astore_2
    //   2380: aload_2
    //   2381: aload_1
    //   2382: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   2385: aload_3
    //   2386: aload #19
    //   2388: aload_2
    //   2389: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   2392: fload #17
    //   2394: invokevirtual width : (F)Larc/scene/ui/layout/Cell;
    //   2397: fload #22
    //   2399: invokevirtual height : (F)Larc/scene/ui/layout/Cell;
    //   2402: fload #9
    //   2404: invokevirtual padLeft : (F)Larc/scene/ui/layout/Cell;
    //   2407: pop
    //   2408: new lunarians/ui/BetaHud$32
    //   2411: astore_2
    //   2412: aload_2
    //   2413: aload_1
    //   2414: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   2417: aload_3
    //   2418: aload #25
    //   2420: aload_2
    //   2421: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   2424: fload #17
    //   2426: invokevirtual width : (F)Larc/scene/ui/layout/Cell;
    //   2429: fload #22
    //   2431: invokevirtual height : (F)Larc/scene/ui/layout/Cell;
    //   2434: fload #16
    //   2436: invokevirtual padLeft : (F)Larc/scene/ui/layout/Cell;
    //   2439: pop
    //   2440: aload_1
    //   2441: getfield panel : Larc/scene/ui/layout/Table;
    //   2444: aload_3
    //   2445: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   2448: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   2451: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   2454: fload #9
    //   2456: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   2459: invokevirtual row : ()V
    //   2462: aload_1
    //   2463: getfield panel : Larc/scene/ui/layout/Table;
    //   2466: astore_2
    //   2467: new lunarians/ui/BetaHud$33
    //   2470: astore_3
    //   2471: aload_3
    //   2472: aload_1
    //   2473: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   2476: aload_2
    //   2477: aload_3
    //   2478: invokevirtual label : (Larc/func/Prov;)Larc/scene/ui/layout/Cell;
    //   2481: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   2484: ldc 10.0
    //   2486: invokevirtual padLeft : (F)Larc/scene/ui/layout/Cell;
    //   2489: invokevirtual row : ()V
    //   2492: aload_1
    //   2493: getfield panel : Larc/scene/ui/layout/Table;
    //   2496: astore_2
    //   2497: ldc_w 'Alerts and camera'
    //   2500: ldc_w 'Alertas e câmera'
    //   2503: invokestatic t : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   2506: astore_3
    //   2507: aload_2
    //   2508: aload_3
    //   2509: invokestatic section : (Larc/scene/ui/layout/Table;Ljava/lang/String;)V
    //   2512: ldc_w 'enemy_core_alert'
    //   2515: ldc_w 'Enemy core alert'
    //   2518: invokestatic safeI18n : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   2521: astore_2
    //   2522: getstatic lunarians/LunariansSettings.enableEnemyCoreAlert : Z
    //   2525: istore #32
    //   2527: new lunarians/ui/BetaHud$34
    //   2530: astore #4
    //   2532: aload #4
    //   2534: aload_1
    //   2535: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   2538: aload_1
    //   2539: aload_2
    //   2540: iload #32
    //   2542: aload #4
    //   2544: invokespecial addCheck : (Ljava/lang/String;ZLarc/func/Boolc;)V
    //   2547: ldc_w 'autofill'
    //   2550: ldc_w 'Auto fill'
    //   2553: invokestatic safeI18n : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   2556: astore_2
    //   2557: getstatic lunarians/LunariansSettings.enableAutoFill : Z
    //   2560: istore #32
    //   2562: new lunarians/ui/BetaHud$35
    //   2565: astore #4
    //   2567: aload #4
    //   2569: aload_1
    //   2570: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   2573: aload_1
    //   2574: aload_2
    //   2575: iload #32
    //   2577: aload #4
    //   2579: invokespecial addCheck : (Ljava/lang/String;ZLarc/func/Boolc;)V
    //   2582: aload_1
    //   2583: getfield panel : Larc/scene/ui/layout/Table;
    //   2586: astore_2
    //   2587: ldc_w 'autofill_config'
    //   2590: ldc_w 'Auto fill config'
    //   2593: invokestatic safeI18n : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   2596: astore_3
    //   2597: new lunarians/ui/BetaHud$36
    //   2600: astore #4
    //   2602: aload #4
    //   2604: aload_1
    //   2605: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   2608: aload_2
    //   2609: aload_3
    //   2610: aload #4
    //   2612: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   2615: ldc_w 36.0
    //   2618: invokevirtual height : (F)Larc/scene/ui/layout/Cell;
    //   2621: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   2624: fload #9
    //   2626: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   2629: invokevirtual row : ()V
    //   2632: ldc_w 'freecam'
    //   2635: ldc_w 'Free cam'
    //   2638: invokestatic safeI18n : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   2641: astore_2
    //   2642: getstatic lunarians/LunariansSettings.enableFreeCam : Z
    //   2645: istore #32
    //   2647: new lunarians/ui/BetaHud$37
    //   2650: astore #4
    //   2652: aload #4
    //   2654: aload_1
    //   2655: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   2658: aload_1
    //   2659: aload_2
    //   2660: iload #32
    //   2662: aload #4
    //   2664: invokespecial addCheck : (Ljava/lang/String;ZLarc/func/Boolc;)V
    //   2667: ldc_w 'prod_alert'
    //   2670: ldc_w 'Production alert'
    //   2673: invokestatic safeI18n : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   2676: astore_2
    //   2677: getstatic lunarians/LunariansSettings.enableProductionAlert : Z
    //   2680: istore #32
    //   2682: new lunarians/ui/BetaHud$38
    //   2685: astore #4
    //   2687: aload #4
    //   2689: aload_1
    //   2690: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   2693: aload_1
    //   2694: aload_2
    //   2695: iload #32
    //   2697: aload #4
    //   2699: invokespecial addCheck : (Ljava/lang/String;ZLarc/func/Boolc;)V
    //   2702: ldc_w 'camfollow'
    //   2705: ldc_w 'Cam follow'
    //   2708: invokestatic safeI18n : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   2711: astore_2
    //   2712: getstatic lunarians/LunariansSettings.enableCamFollow : Z
    //   2715: istore #32
    //   2717: new lunarians/ui/BetaHud$39
    //   2720: astore #4
    //   2722: aload #4
    //   2724: aload_1
    //   2725: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   2728: aload_1
    //   2729: aload_2
    //   2730: iload #32
    //   2732: aload #4
    //   2734: invokespecial addCheck : (Ljava/lang/String;ZLarc/func/Boolc;)V
    //   2737: ldc_w 'loss'
    //   2740: ldc_w 'Resource loss'
    //   2743: invokestatic safeI18n : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   2746: astore_2
    //   2747: getstatic lunarians/LunariansSettings.enableLossWarn : Z
    //   2750: istore #32
    //   2752: new lunarians/ui/BetaHud$40
    //   2755: astore #4
    //   2757: aload #4
    //   2759: aload_1
    //   2760: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   2763: aload_1
    //   2764: aload_2
    //   2765: iload #32
    //   2767: aload #4
    //   2769: invokespecial addCheck : (Ljava/lang/String;ZLarc/func/Boolc;)V
    //   2772: ldc_w 'autounit'
    //   2775: ldc_w 'Auto unit'
    //   2778: invokestatic safeI18n : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   2781: astore_2
    //   2782: getstatic lunarians/LunariansSettings.enableAutoUnit : Z
    //   2785: istore #32
    //   2787: new lunarians/ui/BetaHud$41
    //   2790: astore #4
    //   2792: aload #4
    //   2794: aload_1
    //   2795: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   2798: new lunarians/ui/BetaHud$42
    //   2801: astore #5
    //   2803: aload #5
    //   2805: aload_1
    //   2806: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   2809: aload_1
    //   2810: aload_2
    //   2811: iload #32
    //   2813: aload #4
    //   2815: aload #5
    //   2817: invokespecial addCheckCount : (Ljava/lang/String;ZLarc/func/Boolc;Larc/func/Prov;)V
    //   2820: getstatic lunarians/LunariansSettings.enableAutoUnitHeal : Z
    //   2823: istore #33
    //   2825: new lunarians/ui/BetaHud$43
    //   2828: astore_3
    //   2829: aload_3
    //   2830: aload_1
    //   2831: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   2834: new lunarians/ui/BetaHud$44
    //   2837: astore #4
    //   2839: aload #4
    //   2841: aload_1
    //   2842: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   2845: aload_1
    //   2846: ldc_w 'Auto unit heal'
    //   2849: iload #33
    //   2851: aload_3
    //   2852: aload #4
    //   2854: invokespecial addCheckCount : (Ljava/lang/String;ZLarc/func/Boolc;Larc/func/Prov;)V
    //   2857: getstatic lunarians/LunariansSettings.enableAutoMoon : Z
    //   2860: istore #33
    //   2862: new lunarians/ui/BetaHud$45
    //   2865: astore_3
    //   2866: aload_3
    //   2867: aload_1
    //   2868: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   2871: new lunarians/ui/BetaHud$46
    //   2874: astore #4
    //   2876: aload #4
    //   2878: aload_1
    //   2879: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   2882: aload_1
    //   2883: ldc_w 'Auto Moon'
    //   2886: iload #33
    //   2888: aload_3
    //   2889: aload #4
    //   2891: invokespecial addCheckCount : (Ljava/lang/String;ZLarc/func/Boolc;Larc/func/Prov;)V
    //   2894: ldc_w 'autocargo'
    //   2897: ldc_w 'Auto cargo'
    //   2900: invokestatic safeI18n : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   2903: astore_2
    //   2904: getstatic lunarians/LunariansSettings.enableAutoCargo : Z
    //   2907: istore #32
    //   2909: new lunarians/ui/BetaHud$47
    //   2912: astore #4
    //   2914: aload #4
    //   2916: aload_1
    //   2917: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   2920: new lunarians/ui/BetaHud$48
    //   2923: astore #5
    //   2925: aload #5
    //   2927: aload_1
    //   2928: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   2931: aload_1
    //   2932: aload_2
    //   2933: iload #32
    //   2935: aload #4
    //   2937: aload #5
    //   2939: invokespecial addCheckCount : (Ljava/lang/String;ZLarc/func/Boolc;Larc/func/Prov;)V
    //   2942: getstatic lunarians/LunariansSettings.enableAutoContainer : Z
    //   2945: istore #33
    //   2947: new lunarians/ui/BetaHud$49
    //   2950: astore_3
    //   2951: aload_3
    //   2952: aload_1
    //   2953: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   2956: new lunarians/ui/BetaHud$50
    //   2959: astore #4
    //   2961: aload #4
    //   2963: aload_1
    //   2964: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   2967: aload_1
    //   2968: ldc_w 'Auto container'
    //   2971: iload #33
    //   2973: aload_3
    //   2974: aload #4
    //   2976: invokespecial addCheckCount : (Ljava/lang/String;ZLarc/func/Boolc;Larc/func/Prov;)V
    //   2979: getstatic lunarians/LunariansSettings.enableAutoDefence : Z
    //   2982: istore #33
    //   2984: new lunarians/ui/BetaHud$51
    //   2987: astore_3
    //   2988: aload_3
    //   2989: aload_1
    //   2990: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   2993: new lunarians/ui/BetaHud$52
    //   2996: astore #4
    //   2998: aload #4
    //   3000: aload_1
    //   3001: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   3004: aload_1
    //   3005: ldc_w 'Auto defesa'
    //   3008: iload #33
    //   3010: aload_3
    //   3011: aload #4
    //   3013: invokespecial addCheckCount : (Ljava/lang/String;ZLarc/func/Boolc;Larc/func/Prov;)V
    //   3016: getstatic lunarians/LunariansSettings.enableAutoShield : Z
    //   3019: istore #33
    //   3021: new lunarians/ui/BetaHud$53
    //   3024: astore_3
    //   3025: aload_3
    //   3026: aload_1
    //   3027: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   3030: new lunarians/ui/BetaHud$54
    //   3033: astore #4
    //   3035: aload #4
    //   3037: aload_1
    //   3038: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   3041: aload_1
    //   3042: ldc_w 'Auto shield'
    //   3045: iload #33
    //   3047: aload_3
    //   3048: aload #4
    //   3050: invokespecial addCheckCount : (Ljava/lang/String;ZLarc/func/Boolc;Larc/func/Prov;)V
    //   3053: getstatic lunarians/LunariansSettings.enableAutoHeal : Z
    //   3056: istore #33
    //   3058: new lunarians/ui/BetaHud$55
    //   3061: astore_3
    //   3062: aload_3
    //   3063: aload_1
    //   3064: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   3067: new lunarians/ui/BetaHud$56
    //   3070: astore #4
    //   3072: aload #4
    //   3074: aload_1
    //   3075: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   3078: aload_1
    //   3079: ldc_w 'Auto heal'
    //   3082: iload #33
    //   3084: aload_3
    //   3085: aload #4
    //   3087: invokespecial addCheckCount : (Ljava/lang/String;ZLarc/func/Boolc;Larc/func/Prov;)V
    //   3090: aload_1
    //   3091: getfield panel : Larc/scene/ui/layout/Table;
    //   3094: astore_2
    //   3095: ldc_w 'Combat and interface'
    //   3098: ldc_w 'Combate e interface'
    //   3101: invokestatic t : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   3104: astore_3
    //   3105: aload_2
    //   3106: aload_3
    //   3107: invokestatic section : (Larc/scene/ui/layout/Table;Ljava/lang/String;)V
    //   3110: getstatic lunarians/LunariansSettings.enableAutoAttack : Z
    //   3113: istore #33
    //   3115: new lunarians/ui/BetaHud$57
    //   3118: astore_3
    //   3119: aload_3
    //   3120: aload_1
    //   3121: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   3124: new lunarians/ui/BetaHud$58
    //   3127: astore #4
    //   3129: aload #4
    //   3131: aload_1
    //   3132: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   3135: aload_1
    //   3136: ldc_w 'Auto attack'
    //   3139: iload #33
    //   3141: aload_3
    //   3142: aload #4
    //   3144: invokespecial addCheckCount : (Ljava/lang/String;ZLarc/func/Boolc;Larc/func/Prov;)V
    //   3147: ldc_w 'schem_ui'
    //   3150: ldc_w 'Schematic panel'
    //   3153: invokestatic safeI18n : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   3156: astore_2
    //   3157: getstatic lunarians/LunariansSettings.showSchematicPanel : Z
    //   3160: istore #32
    //   3162: new lunarians/ui/BetaHud$59
    //   3165: astore #4
    //   3167: aload #4
    //   3169: aload_1
    //   3170: invokespecial <init> : (Llunarians/ui/BetaHud;)V
    //   3173: aload_1
    //   3174: aload_2
    //   3175: iload #32
    //   3177: aload #4
    //   3179: invokespecial addCheck : (Ljava/lang/String;ZLarc/func/Boolc;)V
    //   3182: aload_1
    //   3183: getfield panel : Larc/scene/ui/layout/Table;
    //   3186: invokestatic decorate : (Larc/scene/Element;)V
    //   3189: return
    //   3190: pop
    //   3191: goto -> 705
    //   3194: pop
    //   3195: goto -> 1105
    // Exception table:
    //   from	to	target	type
    //   183	188	200	finally
    //   192	197	200	finally
    //   677	680	3190	finally
    //   684	688	3190	finally
    //   690	695	3190	finally
    //   699	705	3190	finally
    //   742	745	780	finally
    //   747	750	780	finally
    //   754	758	780	finally
    //   764	769	780	finally
    //   771	777	780	finally
    //   1066	1069	3194	finally
    //   1078	1081	3194	finally
    //   1085	1090	3194	finally
    //   1093	1096	3194	finally
    //   1100	1105	3194	finally
    //   1223	1226	1260	finally
    //   1228	1231	1260	finally
    //   1235	1239	1260	finally
    //   1244	1249	1260	finally
    //   1251	1257	1260	finally
    //   2308	2311	2344	finally
    //   2313	2316	2344	finally
    //   2320	2324	2344	finally
    //   2328	2333	2344	finally
    //   2335	2341	2344	finally
  }
  
  private static String safeI18n(String paramString1, String paramString2) {
    try {
      String str = LunariansI18n.get(paramString1);
      if (str != null) {
        boolean bool = str.isEmpty();
        if (!bool) {
          boolean bool1 = str.equals(paramString1);
          if (!bool1)
            return str; 
        } 
      } 
    } finally {}
    return paramString2;
  }
  
  private static String t(String paramString1, String paramString2) {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? paramString2 : paramString1;
  }
  
  public void applySize() {
    try {
      Graphics graphics = Core.graphics;
      int i = graphics.getHeight();
      float f1 = i;
      float f2 = LunariansSettings.betaHeightPct;
      f1 *= f2;
      f2 = 100.0F;
      f1 /= f2;
      Cell cell = this.scrollCell;
      float f3 = LunariansSettings.betaWidth;
      cell = cell.width(f3);
      cell.maxHeight(f1);
      invalidateHierarchy();
    } finally {}
  }
  
  public void refresh() {
    applySize();
    boolean bool = this.open;
    if (bool)
      try {
        return;
      } finally {
        Exception exception = null;
        String str = "[Lunarians] BetaHud refresh failed";
      }  
  }
  
  public void toggle() {
    boolean bool = this.open;
    int i = bool ^ true;
    this.open = i;
    i = 0;
    TextureRegionDrawable textureRegionDrawable = null;
    boolean bool1 = true;
    if (!bool) {
      try {
        rebuildPanel();
      } finally {
        Exception exception = null;
        String str = "[Lunarians] BetaHud rebuild failed";
      } 
      Table table = this.panel;
      Action[] arrayOfAction = new Action[bool1];
      float f1 = 0.0F;
      AlphaAction alphaAction1 = Actions.alpha(0.0F);
      float f2 = 0.2F;
      AlphaAction alphaAction2 = Actions.fadeIn(f2);
      SequenceAction sequenceAction = Actions.sequence((Action)alphaAction1, (Action)alphaAction2);
      arrayOfAction[0] = (Action)sequenceAction;
      table.actions(arrayOfAction);
      try {
        ImageButton imageButton = this.toggleBtn;
        ImageButton.ImageButtonStyle imageButtonStyle = imageButton.getStyle();
        textureRegionDrawable = Icon.leftOpen;
        imageButtonStyle.imageUp = (Drawable)textureRegionDrawable;
      } finally {}
    } else {
      Table table = this.panel;
      Action[] arrayOfAction = new Action[bool1];
      float f = 0.15F;
      AlphaAction alphaAction = Actions.fadeOut(f);
      BetaHud$2 betaHud$2 = new BetaHud$2();
      this(this);
      RunnableAction runnableAction = Actions.run(betaHud$2);
      SequenceAction sequenceAction = Actions.sequence((Action)alphaAction, (Action)runnableAction);
      arrayOfAction[0] = (Action)sequenceAction;
      table.actions(arrayOfAction);
      ImageButton imageButton = this.toggleBtn;
      ImageButton.ImageButtonStyle imageButtonStyle = imageButton.getStyle();
      textureRegionDrawable = Icon.rightOpen;
      imageButtonStyle.imageUp = (Drawable)textureRegionDrawable;
    } 
  }
}
