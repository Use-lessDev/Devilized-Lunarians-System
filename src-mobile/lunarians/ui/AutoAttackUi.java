package lunarians.ui;

import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import java.util.Iterator;
import lunarians.AutoAttack;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import lunarians.data.AttackSlot;
import mindustry.Vars;
import mindustry.core.UI;
import mindustry.gen.Icon;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public class AutoAttackUi {
  private static AttackSlot clipboard;
  
  private static BuilderDialog currentDlg;
  
  private final Table list;
  
  public AutoAttackUi(Table paramTable) {
    Table table1 = new Table();
    this();
    this.list = table1;
    paramTable.defaults().growX();
    String str1 = hint();
    Cell cell1 = paramTable.add(str1).wrap().growX();
    Color color1 = Color.lightGray;
    cell1 = cell1.color(color1);
    float f1 = 4.0F;
    cell1.padBottom(f1).row();
    Table table2 = new Table();
    this();
    String str5 = tr("Regroup", "Reagrupar");
    boolean bool = AutoAttack.regroupEnabled();
    AutoAttackUi$3 autoAttackUi$3 = new AutoAttackUi$3();
    this();
    table2.check(str5, bool, autoAttackUi$3).left();
    StringBuilder stringBuilder2 = new StringBuilder();
    String str7 = " ";
    this(str7);
    String str6 = tr("every", "a cada");
    stringBuilder2.append(str6);
    stringBuilder2.append(str7);
    String str4 = stringBuilder2.toString();
    table2.add(str4).fontScale(0.85F);
    AutoAttackUi$4 autoAttackUi$4 = new AutoAttackUi$4();
    this();
    Cell cell3 = table2.button("-", autoAttackUi$4);
    float f2 = 32.0F;
    float f3 = 28.0F;
    cell3.size(f2, f3);
    AutoAttackUi$5 autoAttackUi$5 = new AutoAttackUi$5();
    this();
    table2.label(autoAttackUi$5).width(40.0F).center();
    AutoAttackUi$6 autoAttackUi$6 = new AutoAttackUi$6();
    this();
    table2.button("+", autoAttackUi$6).size(f2, f3);
    StringBuilder stringBuilder1 = new StringBuilder();
    this(str7);
    str7 = "(min 5)";
    str7 = tr(str7, str7);
    stringBuilder1.append(str7);
    String str3 = stringBuilder1.toString();
    Cell cell2 = table2.add(str3);
    Color color2 = Color.lightGray;
    cell2.color(color2).fontScale(0.75F);
    paramTable.add((Element)table2).left().padBottom(6.0F).row();
    table2 = new Table();
    this();
    String str2 = LunariansI18n.get("atk_heal");
    bool = LunariansSettings.autoAttackHeal;
    AutoAttackUi$7 autoAttackUi$7 = new AutoAttackUi$7();
    this();
    table2.check(str2, bool, autoAttackUi$7).left();
    paramTable.add((Element)table2).left().padBottom(f1).row();
    paramTable.add((Element)table1).growX().row();
    rebuild();
  }
  
  private static String all() {
    return LunariansI18n.get("t_all");
  }
  
  private Table card(AttackSlot paramAttackSlot, int paramInt) {
    // Byte code:
    //   0: aload_1
    //   1: astore_3
    //   2: new arc/scene/ui/layout/Table
    //   5: astore #4
    //   7: aload #4
    //   9: invokespecial <init> : ()V
    //   12: getstatic mindustry/ui/Styles.black6 : Larc/scene/style/Drawable;
    //   15: astore #5
    //   17: aload #4
    //   19: aload #5
    //   21: invokevirtual setBackground : (Larc/scene/style/Drawable;)V
    //   24: aload #4
    //   26: invokevirtual defaults : ()Larc/scene/ui/layout/Cell;
    //   29: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   32: astore #5
    //   34: ldc 4.0
    //   36: fstore #6
    //   38: aload #5
    //   40: fload #6
    //   42: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   45: pop
    //   46: aload_1
    //   47: getfield unit : Ljava/lang/String;
    //   50: astore #5
    //   52: aload #5
    //   54: ifnull -> 98
    //   57: aload_1
    //   58: getfield unit : Ljava/lang/String;
    //   61: astore #5
    //   63: aload #5
    //   65: invokevirtual isEmpty : ()Z
    //   68: istore #7
    //   70: iload #7
    //   72: ifne -> 98
    //   75: getstatic mindustry/Vars.content : Lmindustry/core/ContentLoader;
    //   78: astore #5
    //   80: aload_1
    //   81: getfield unit : Ljava/lang/String;
    //   84: astore #8
    //   86: aload #5
    //   88: aload #8
    //   90: invokevirtual unit : (Ljava/lang/String;)Lmindustry/type/UnitType;
    //   93: astore #5
    //   95: goto -> 107
    //   98: iconst_0
    //   99: istore #7
    //   101: fconst_0
    //   102: fstore #9
    //   104: aconst_null
    //   105: astore #5
    //   107: new arc/scene/ui/layout/Table
    //   110: astore #8
    //   112: aload #8
    //   114: invokespecial <init> : ()V
    //   117: new arc/scene/ui/ImageButton
    //   120: astore #10
    //   122: aload #5
    //   124: ifnull -> 149
    //   127: new arc/scene/style/TextureRegionDrawable
    //   130: astore #11
    //   132: aload #5
    //   134: getfield uiIcon : Larc/graphics/g2d/TextureRegion;
    //   137: astore #12
    //   139: aload #11
    //   141: aload #12
    //   143: invokespecial <init> : (Larc/graphics/g2d/TextureRegion;)V
    //   146: goto -> 154
    //   149: getstatic mindustry/gen/Icon.add : Larc/scene/style/TextureRegionDrawable;
    //   152: astore #11
    //   154: getstatic mindustry/ui/Styles.clearNonei : Larc/scene/ui/ImageButton$ImageButtonStyle;
    //   157: astore #12
    //   159: aload #10
    //   161: aload #11
    //   163: aload #12
    //   165: invokespecial <init> : (Larc/scene/style/Drawable;Larc/scene/ui/ImageButton$ImageButtonStyle;)V
    //   168: ldc 1107296256
    //   170: istore #13
    //   172: ldc 32.0
    //   174: fstore #14
    //   176: aload #10
    //   178: fload #14
    //   180: invokevirtual resizeImage : (F)V
    //   183: new lunarians/ui/AutoAttackUi$8
    //   186: astore #12
    //   188: aload #12
    //   190: aload_0
    //   191: aload_3
    //   192: invokespecial <init> : (Llunarians/ui/AutoAttackUi;Llunarians/data/AttackSlot;)V
    //   195: aload #10
    //   197: aload #12
    //   199: invokevirtual clicked : (Ljava/lang/Runnable;)Larc/scene/event/ClickListener;
    //   202: pop
    //   203: aload #8
    //   205: aload #10
    //   207: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   210: astore #10
    //   212: ldc 40.0
    //   214: fstore #15
    //   216: aload #10
    //   218: fload #15
    //   220: invokevirtual size : (F)Larc/scene/ui/layout/Cell;
    //   223: astore #10
    //   225: ldc 6.0
    //   227: fstore #16
    //   229: aload #10
    //   231: fload #16
    //   233: invokevirtual padRight : (F)Larc/scene/ui/layout/Cell;
    //   236: pop
    //   237: aload #5
    //   239: ifnull -> 252
    //   242: aload #5
    //   244: getfield localizedName : Ljava/lang/String;
    //   247: astore #10
    //   249: goto -> 257
    //   252: invokestatic pickUnit : ()Ljava/lang/String;
    //   255: astore #10
    //   257: aload #8
    //   259: aload #10
    //   261: invokevirtual add : (Ljava/lang/CharSequence;)Larc/scene/ui/layout/Cell;
    //   264: invokevirtual wrap : ()Larc/scene/ui/layout/Cell;
    //   267: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   270: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   273: ldc_w 0.9
    //   276: invokevirtual fontScale : (F)Larc/scene/ui/layout/Cell;
    //   279: pop
    //   280: invokestatic copyLabel : ()Ljava/lang/String;
    //   283: astore #10
    //   285: new lunarians/ui/AutoAttackUi$12
    //   288: astore #17
    //   290: aload #17
    //   292: aload_3
    //   293: invokespecial <init> : (Llunarians/data/AttackSlot;)V
    //   296: aload #8
    //   298: aload #10
    //   300: aload #17
    //   302: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   305: ldc_w 64.0
    //   308: invokevirtual width : (F)Larc/scene/ui/layout/Cell;
    //   311: fload #14
    //   313: invokevirtual height : (F)Larc/scene/ui/layout/Cell;
    //   316: fload #6
    //   318: invokevirtual padLeft : (F)Larc/scene/ui/layout/Cell;
    //   321: pop
    //   322: getstatic mindustry/gen/Icon.trash : Larc/scene/style/TextureRegionDrawable;
    //   325: astore #10
    //   327: getstatic mindustry/ui/Styles.defaulti : Larc/scene/ui/ImageButton$ImageButtonStyle;
    //   330: astore #17
    //   332: new lunarians/ui/AutoAttackUi$13
    //   335: astore #18
    //   337: aload #18
    //   339: iload_2
    //   340: invokespecial <init> : (I)V
    //   343: aload #8
    //   345: aload #10
    //   347: aload #17
    //   349: aload #18
    //   351: invokevirtual button : (Larc/scene/style/Drawable;Larc/scene/ui/ImageButton$ImageButtonStyle;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   354: ldc_w 36.0
    //   357: invokevirtual size : (F)Larc/scene/ui/layout/Cell;
    //   360: fload #6
    //   362: invokevirtual padLeft : (F)Larc/scene/ui/layout/Cell;
    //   365: pop
    //   366: aload #4
    //   368: aload #8
    //   370: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   373: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   376: invokevirtual row : ()V
    //   379: new arc/scene/ui/layout/Table
    //   382: astore #8
    //   384: aload #8
    //   386: invokespecial <init> : ()V
    //   389: invokestatic maxLabel : ()Ljava/lang/String;
    //   392: astore #10
    //   394: aload #8
    //   396: aload #10
    //   398: invokevirtual add : (Ljava/lang/CharSequence;)Larc/scene/ui/layout/Cell;
    //   401: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   404: fload #6
    //   406: invokevirtual padRight : (F)Larc/scene/ui/layout/Cell;
    //   409: astore #10
    //   411: ldc 0.85
    //   413: fstore #16
    //   415: aload #10
    //   417: fload #16
    //   419: invokevirtual fontScale : (F)Larc/scene/ui/layout/Cell;
    //   422: pop
    //   423: new lunarians/ui/AutoAttackUi$14
    //   426: astore #10
    //   428: aload #10
    //   430: aload_3
    //   431: invokespecial <init> : (Llunarians/data/AttackSlot;)V
    //   434: ldc '-'
    //   436: astore #18
    //   438: aload #8
    //   440: aload #18
    //   442: aload #10
    //   444: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   447: astore #10
    //   449: ldc 28.0
    //   451: fstore #19
    //   453: aload #10
    //   455: fload #14
    //   457: fload #19
    //   459: invokevirtual size : (FF)Larc/scene/ui/layout/Cell;
    //   462: pop
    //   463: new lunarians/ui/AutoAttackUi$15
    //   466: astore #10
    //   468: aload #10
    //   470: aload_3
    //   471: invokespecial <init> : (Llunarians/data/AttackSlot;)V
    //   474: aload #8
    //   476: aload #10
    //   478: invokevirtual label : (Larc/func/Prov;)Larc/scene/ui/layout/Cell;
    //   481: fload #15
    //   483: invokevirtual width : (F)Larc/scene/ui/layout/Cell;
    //   486: invokevirtual center : ()Larc/scene/ui/layout/Cell;
    //   489: astore #10
    //   491: ldc_w 2.0
    //   494: fstore #20
    //   496: aload #10
    //   498: fload #20
    //   500: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   503: pop
    //   504: new lunarians/ui/AutoAttackUi$16
    //   507: astore #10
    //   509: aload #10
    //   511: aload_3
    //   512: invokespecial <init> : (Llunarians/data/AttackSlot;)V
    //   515: ldc '+'
    //   517: astore #21
    //   519: aload #8
    //   521: aload #21
    //   523: aload #10
    //   525: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   528: astore #10
    //   530: aload #10
    //   532: fload #14
    //   534: fload #19
    //   536: invokevirtual size : (FF)Larc/scene/ui/layout/Cell;
    //   539: pop
    //   540: aload #4
    //   542: aload #8
    //   544: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   547: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   550: fload #20
    //   552: invokevirtual padTop : (F)Larc/scene/ui/layout/Cell;
    //   555: astore #8
    //   557: aload #8
    //   559: invokevirtual row : ()V
    //   562: aload #5
    //   564: ifnull -> 961
    //   567: aload #5
    //   569: invokestatic maxStandoffTiles : (Lmindustry/type/UnitType;)I
    //   572: istore #22
    //   574: new arc/scene/ui/layout/Table
    //   577: astore #10
    //   579: aload #10
    //   581: invokespecial <init> : ()V
    //   584: ldc_w 'Attack distance'
    //   587: ldc_w 'Dist. ataque'
    //   590: invokestatic tr : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   593: astore #23
    //   595: aload #10
    //   597: aload #23
    //   599: invokevirtual add : (Ljava/lang/CharSequence;)Larc/scene/ui/layout/Cell;
    //   602: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   605: fload #6
    //   607: invokevirtual padRight : (F)Larc/scene/ui/layout/Cell;
    //   610: fload #16
    //   612: invokevirtual fontScale : (F)Larc/scene/ui/layout/Cell;
    //   615: pop
    //   616: new lunarians/ui/AutoAttackUi$17
    //   619: astore #23
    //   621: aload #23
    //   623: aload_3
    //   624: iload #22
    //   626: invokespecial <init> : (Llunarians/data/AttackSlot;I)V
    //   629: aload #10
    //   631: aload #18
    //   633: aload #23
    //   635: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   638: fload #14
    //   640: fload #19
    //   642: invokevirtual size : (FF)Larc/scene/ui/layout/Cell;
    //   645: pop
    //   646: new lunarians/ui/AutoAttackUi$18
    //   649: astore #23
    //   651: aload #23
    //   653: aload_3
    //   654: iload #22
    //   656: invokespecial <init> : (Llunarians/data/AttackSlot;I)V
    //   659: aload #10
    //   661: aload #23
    //   663: invokevirtual label : (Larc/func/Prov;)Larc/scene/ui/layout/Cell;
    //   666: astore #23
    //   668: ldc_w 56.0
    //   671: fstore #24
    //   673: aload #23
    //   675: fload #24
    //   677: invokevirtual width : (F)Larc/scene/ui/layout/Cell;
    //   680: invokevirtual center : ()Larc/scene/ui/layout/Cell;
    //   683: fload #20
    //   685: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   688: pop
    //   689: new lunarians/ui/AutoAttackUi$19
    //   692: astore #23
    //   694: aload #23
    //   696: aload_3
    //   697: iload #22
    //   699: invokespecial <init> : (Llunarians/data/AttackSlot;I)V
    //   702: aload #10
    //   704: aload #21
    //   706: aload #23
    //   708: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   711: fload #14
    //   713: fload #19
    //   715: invokevirtual size : (FF)Larc/scene/ui/layout/Cell;
    //   718: pop
    //   719: aload #5
    //   721: getfield maxRange : F
    //   724: fstore #9
    //   726: ldc_w 8.0
    //   729: fstore #25
    //   731: fload #9
    //   733: fload #25
    //   735: fdiv
    //   736: fstore #9
    //   738: new java/lang/StringBuilder
    //   741: astore #23
    //   743: aload #23
    //   745: ldc ' '
    //   747: invokespecial <init> : (Ljava/lang/String;)V
    //   750: new java/lang/StringBuilder
    //   753: astore #26
    //   755: ldc_w '1-'
    //   758: astore #12
    //   760: aload #26
    //   762: aload #12
    //   764: invokespecial <init> : (Ljava/lang/String;)V
    //   767: aload #26
    //   769: iload #22
    //   771: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   774: pop
    //   775: aload #26
    //   777: ldc_w ' (range '
    //   780: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   783: pop
    //   784: fload #9
    //   786: f2i
    //   787: istore #7
    //   789: aload #26
    //   791: iload #7
    //   793: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   796: pop
    //   797: ldc_w '+2)'
    //   800: astore #27
    //   802: aload #26
    //   804: aload #27
    //   806: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   809: pop
    //   810: aload #26
    //   812: invokevirtual toString : ()Ljava/lang/String;
    //   815: astore #26
    //   817: new java/lang/StringBuilder
    //   820: astore #11
    //   822: aload #11
    //   824: aload #12
    //   826: invokespecial <init> : (Ljava/lang/String;)V
    //   829: aload #11
    //   831: iload #22
    //   833: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   836: pop
    //   837: aload #11
    //   839: ldc_w ' (alcance '
    //   842: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   845: pop
    //   846: aload #11
    //   848: iload #7
    //   850: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   853: pop
    //   854: aload #11
    //   856: aload #27
    //   858: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   861: pop
    //   862: aload #11
    //   864: invokevirtual toString : ()Ljava/lang/String;
    //   867: astore #5
    //   869: aload #26
    //   871: aload #5
    //   873: invokestatic tr : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   876: astore #5
    //   878: aload #23
    //   880: aload #5
    //   882: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   885: pop
    //   886: aload #23
    //   888: invokevirtual toString : ()Ljava/lang/String;
    //   891: astore #5
    //   893: aload #10
    //   895: aload #5
    //   897: invokevirtual add : (Ljava/lang/CharSequence;)Larc/scene/ui/layout/Cell;
    //   900: astore #5
    //   902: getstatic arc/graphics/Color.lightGray : Larc/graphics/Color;
    //   905: astore #8
    //   907: aload #5
    //   909: aload #8
    //   911: invokevirtual color : (Larc/graphics/Color;)Larc/scene/ui/layout/Cell;
    //   914: astore #5
    //   916: ldc_w 1060320051
    //   919: istore #22
    //   921: ldc_w 0.7
    //   924: fstore #28
    //   926: aload #5
    //   928: fload #28
    //   930: invokevirtual fontScale : (F)Larc/scene/ui/layout/Cell;
    //   933: fload #6
    //   935: invokevirtual padLeft : (F)Larc/scene/ui/layout/Cell;
    //   938: pop
    //   939: aload #4
    //   941: aload #10
    //   943: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   946: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   949: fload #20
    //   951: invokevirtual padTop : (F)Larc/scene/ui/layout/Cell;
    //   954: astore #5
    //   956: aload #5
    //   958: invokevirtual row : ()V
    //   961: aload_3
    //   962: invokevirtual hasZone : ()Z
    //   965: istore #7
    //   967: new arc/scene/ui/layout/Table
    //   970: astore #8
    //   972: aload #8
    //   974: invokespecial <init> : ()V
    //   977: iload #7
    //   979: ifeq -> 1071
    //   982: new java/lang/StringBuilder
    //   985: astore #10
    //   987: aload #10
    //   989: invokespecial <init> : ()V
    //   992: aload_3
    //   993: getfield x : I
    //   996: istore #13
    //   998: aload #10
    //   1000: iload #13
    //   1002: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1005: pop
    //   1006: aload #10
    //   1008: ldc_w ','
    //   1011: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1014: pop
    //   1015: aload_3
    //   1016: getfield y : I
    //   1019: istore #13
    //   1021: aload #10
    //   1023: iload #13
    //   1025: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1028: pop
    //   1029: ldc_w ' r'
    //   1032: astore #11
    //   1034: aload #10
    //   1036: aload #11
    //   1038: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1041: pop
    //   1042: aload_3
    //   1043: getfield zoneRadius : F
    //   1046: fstore #14
    //   1048: fload #14
    //   1050: f2i
    //   1051: istore #13
    //   1053: aload #10
    //   1055: iload #13
    //   1057: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1060: pop
    //   1061: aload #10
    //   1063: invokevirtual toString : ()Ljava/lang/String;
    //   1066: astore #10
    //   1068: goto -> 1079
    //   1071: ldc_w 'atk_whole_map'
    //   1074: invokestatic get : (Ljava/lang/String;)Ljava/lang/String;
    //   1077: astore #10
    //   1079: ldc_w 'atk_zone'
    //   1082: astore #11
    //   1084: aload #11
    //   1086: aload #10
    //   1088: invokestatic get : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1091: astore #10
    //   1093: aload #8
    //   1095: aload #10
    //   1097: invokevirtual add : (Ljava/lang/CharSequence;)Larc/scene/ui/layout/Cell;
    //   1100: invokevirtual wrap : ()Larc/scene/ui/layout/Cell;
    //   1103: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   1106: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   1109: fload #16
    //   1111: invokevirtual fontScale : (F)Larc/scene/ui/layout/Cell;
    //   1114: astore #10
    //   1116: iload #7
    //   1118: ifeq -> 1129
    //   1121: getstatic arc/graphics/Color.white : Larc/graphics/Color;
    //   1124: astore #11
    //   1126: goto -> 1134
    //   1129: getstatic arc/graphics/Color.lightGray : Larc/graphics/Color;
    //   1132: astore #11
    //   1134: aload #10
    //   1136: aload #11
    //   1138: invokevirtual color : (Larc/graphics/Color;)Larc/scene/ui/layout/Cell;
    //   1141: pop
    //   1142: invokestatic mapBtn : ()Ljava/lang/String;
    //   1145: astore #10
    //   1147: new lunarians/ui/AutoAttackUi$20
    //   1150: astore #11
    //   1152: aload #11
    //   1154: iload #7
    //   1156: aload_3
    //   1157: invokespecial <init> : (ZLlunarians/data/AttackSlot;)V
    //   1160: aload #8
    //   1162: aload #10
    //   1164: aload #11
    //   1166: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   1169: ldc_w 90.0
    //   1172: invokevirtual width : (F)Larc/scene/ui/layout/Cell;
    //   1175: astore #10
    //   1177: ldc 1107296256
    //   1179: istore #13
    //   1181: ldc 32.0
    //   1183: fstore #14
    //   1185: aload #10
    //   1187: fload #14
    //   1189: invokevirtual height : (F)Larc/scene/ui/layout/Cell;
    //   1192: astore #10
    //   1194: aload #10
    //   1196: fload #6
    //   1198: invokevirtual padLeft : (F)Larc/scene/ui/layout/Cell;
    //   1201: pop
    //   1202: aload #4
    //   1204: aload #8
    //   1206: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   1209: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   1212: fload #20
    //   1214: invokevirtual padTop : (F)Larc/scene/ui/layout/Cell;
    //   1217: astore #8
    //   1219: aload #8
    //   1221: invokevirtual row : ()V
    //   1224: iload #7
    //   1226: ifeq -> 1404
    //   1229: new arc/scene/ui/layout/Table
    //   1232: astore #5
    //   1234: aload #5
    //   1236: invokespecial <init> : ()V
    //   1239: ldc_w 'atk_zone_radius'
    //   1242: invokestatic get : (Ljava/lang/String;)Ljava/lang/String;
    //   1245: astore #8
    //   1247: aload #5
    //   1249: aload #8
    //   1251: invokevirtual add : (Ljava/lang/CharSequence;)Larc/scene/ui/layout/Cell;
    //   1254: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   1257: astore #8
    //   1259: aload #8
    //   1261: fload #6
    //   1263: invokevirtual padRight : (F)Larc/scene/ui/layout/Cell;
    //   1266: fload #16
    //   1268: invokevirtual fontScale : (F)Larc/scene/ui/layout/Cell;
    //   1271: pop
    //   1272: new lunarians/ui/AutoAttackUi$9
    //   1275: astore #29
    //   1277: aload #29
    //   1279: aload_3
    //   1280: invokespecial <init> : (Llunarians/data/AttackSlot;)V
    //   1283: aload #5
    //   1285: aload #18
    //   1287: aload #29
    //   1289: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   1292: astore #29
    //   1294: ldc_w 1105199104
    //   1297: istore #22
    //   1299: ldc 28.0
    //   1301: fstore #28
    //   1303: ldc 1107296256
    //   1305: istore #13
    //   1307: ldc 32.0
    //   1309: fstore #14
    //   1311: aload #29
    //   1313: fload #14
    //   1315: fload #28
    //   1317: invokevirtual size : (FF)Larc/scene/ui/layout/Cell;
    //   1320: pop
    //   1321: new lunarians/ui/AutoAttackUi$10
    //   1324: astore #29
    //   1326: aload #29
    //   1328: aload_3
    //   1329: invokespecial <init> : (Llunarians/data/AttackSlot;)V
    //   1332: aload #5
    //   1334: aload #29
    //   1336: invokevirtual label : (Larc/func/Prov;)Larc/scene/ui/layout/Cell;
    //   1339: astore #29
    //   1341: ldc 40.0
    //   1343: fstore #30
    //   1345: aload #29
    //   1347: fload #30
    //   1349: invokevirtual width : (F)Larc/scene/ui/layout/Cell;
    //   1352: invokevirtual center : ()Larc/scene/ui/layout/Cell;
    //   1355: fload #20
    //   1357: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   1360: pop
    //   1361: new lunarians/ui/AutoAttackUi$11
    //   1364: astore #29
    //   1366: aload #29
    //   1368: aload_3
    //   1369: invokespecial <init> : (Llunarians/data/AttackSlot;)V
    //   1372: aload #5
    //   1374: aload #21
    //   1376: aload #29
    //   1378: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   1381: fload #14
    //   1383: fload #28
    //   1385: invokevirtual size : (FF)Larc/scene/ui/layout/Cell;
    //   1388: pop
    //   1389: aload #4
    //   1391: aload #5
    //   1393: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   1396: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   1399: astore_3
    //   1400: aload_3
    //   1401: invokevirtual row : ()V
    //   1404: aload #4
    //   1406: areturn
  }
  
  private static String copied() {
    return LunariansI18n.get("atk_copied");
  }
  
  private static String copyLabel() {
    return LunariansI18n.get("t_copy");
  }
  
  private static String emptyMsg() {
    return LunariansI18n.get("t_noslots");
  }
  
  private static String hint() {
    return LunariansI18n.get("atk_hint");
  }
  
  public static BuilderDialog make() {
    BuilderDialog builderDialog = make0();
    currentDlg = builderDialog;
    return builderDialog;
  }
  
  public static BuilderDialog make0() {
    BuilderDialog builderDialog = new BuilderDialog();
    String str = title();
    AutoAttackUi$1 autoAttackUi$1 = new AutoAttackUi$1();
    this();
    AutoAttackUi$2 autoAttackUi$2 = new AutoAttackUi$2();
    this();
    this(str, autoAttackUi$1, autoAttackUi$2);
    return builderDialog;
  }
  
  private static String mapBtn() {
    return LunariansI18n.get("t_map");
  }
  
  private static String maxLabel() {
    return LunariansI18n.get("t_max");
  }
  
  private static String noClip() {
    return LunariansI18n.get("atk_noclip");
  }
  
  private void openUnitPicker(AttackSlot paramAttackSlot) {
    BaseDialog baseDialog = new BaseDialog();
    String str = pickUnit();
    this(str);
    Table table = baseDialog.cont;
    AutoAttackUi$21 autoAttackUi$21 = new AutoAttackUi$21();
    this(paramAttackSlot, baseDialog);
    table.pane(autoAttackUi$21).maxHeight(360.0F).growX();
    baseDialog.addCloseButton();
    baseDialog.show();
  }
  
  private static String pasteLabel() {
    return LunariansI18n.get("t_paste");
  }
  
  private static String pasted() {
    return LunariansI18n.get("atk_pasted");
  }
  
  private static String pickUnit() {
    return LunariansI18n.get("t_choose");
  }
  
  private void rebuild() {
    this.list.clearChildren();
    Seq seq = LunariansSettings.attackSlots;
    Table table2 = this.list;
    StringBuilder stringBuilder = new StringBuilder();
    this();
    int i = seq.size;
    stringBuilder.append(i);
    String str3 = "/10";
    stringBuilder.append(str3);
    String str2 = stringBuilder.toString();
    Cell cell = table2.add(str2);
    Color color = Pal.accent;
    cell = cell.color(color);
    int j = 1082130432;
    float f1 = 4.0F;
    cell = cell.padBottom(f1);
    cell.row();
    boolean bool = seq.isEmpty();
    if (bool) {
      Table table = this.list;
      str3 = emptyMsg();
      Cell cell1 = table.add(str3).wrap().growX().padBottom(f1);
      cell1.row();
    } else {
      bool = false;
      cell = null;
      while (true) {
        j = seq.size;
        if (bool < j) {
          Table table3 = this.list;
          AttackSlot attackSlot = (AttackSlot)seq.get(bool);
          Table table4 = card(attackSlot, bool);
          Cell cell1 = table3.add((Element)table4).growX();
          i = 1077936128;
          float f = 3.0F;
          cell1 = cell1.padBottom(f);
          cell1.row();
          int k = bool + 1;
          continue;
        } 
        break;
      } 
    } 
    Table table1 = new Table();
    this();
    j = seq.size;
    i = 10;
    float f2 = 1.4E-44F;
    float f3 = 2.0F;
    if (j < i) {
      TextureRegionDrawable textureRegionDrawable = Icon.add;
      ImageButton.ImageButtonStyle imageButtonStyle = Styles.defaulti;
      AutoAttackUi$22 autoAttackUi$22 = new AutoAttackUi$22();
      this(seq);
      Cell cell1 = table1.button((Drawable)textureRegionDrawable, imageButtonStyle, autoAttackUi$22);
      i = 1109393408;
      f2 = 40.0F;
      cell1 = cell1.size(f2);
      cell1.pad(f3);
    } 
    String str1 = pasteLabel();
    AutoAttackUi$23 autoAttackUi$23 = new AutoAttackUi$23();
    this(seq);
    table1.button(str1, autoAttackUi$23).width(90.0F).height(36.0F).pad(f3);
    this.list.add((Element)table1).left().padTop(f3).row();
  }
  
  private static void reopen() {
    BuilderDialog builderDialog = currentDlg;
    if (builderDialog != null)
      builderDialog.hide(); 
    builderDialog = make();
    currentDlg = builderDialog;
    builderDialog.show();
  }
  
  private static String title() {
    return LunariansI18n.get("auto_attack");
  }
  
  private static String tr(String paramString1, String paramString2) {
    String str1 = LunariansI18n.getLanguage();
    String str2 = "pt";
    boolean bool = str2.equals(str1);
    if (!bool) {
      str2 = "es";
      bool = str2.equals(str1);
      if (!bool) {
        "ru".equals(str1);
        return paramString1;
      } 
    } 
    return paramString2;
  }
}
