package lunarians.ui;

import arc.graphics.Color;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.Label;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import lunarians.data.UnitSlot;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;

public class AutoUnitUi {
  private static UnitSlot clipboard;
  
  private final Table list;
  
  public AutoUnitUi(Table paramTable) {
    Table table = new Table();
    this();
    this.list = table;
    String str = LunariansI18n.get("autounit");
    boolean bool = LunariansSettings.enableAutoUnit;
    AutoUnitUi$1 autoUnitUi$1 = new AutoUnitUi$1();
    this(this);
    UiText.check(paramTable, str, bool, autoUnitUi$1).left().row();
    str = LunariansI18n.get("autounit_hint");
    Cell cell = paramTable.add(str).wrap().growX();
    Color color = Color.lightGray;
    cell.color(color).padBottom(6.0F).row();
    paramTable.add((Element)table).growX().row();
    rebuild();
  }
  
  private Table card(UnitSlot paramUnitSlot) {
    // Byte code:
    //   0: new arc/scene/ui/layout/Table
    //   3: astore_2
    //   4: getstatic mindustry/ui/Styles.black6 : Larc/scene/style/Drawable;
    //   7: astore_3
    //   8: aload_2
    //   9: aload_3
    //   10: invokespecial <init> : (Larc/scene/style/Drawable;)V
    //   13: aload_2
    //   14: invokevirtual defaults : ()Larc/scene/ui/layout/Cell;
    //   17: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   20: pop
    //   21: new arc/scene/ui/layout/Table
    //   24: astore_3
    //   25: aload_3
    //   26: invokespecial <init> : ()V
    //   29: aload_1
    //   30: getfield unit : Ljava/lang/String;
    //   33: astore #4
    //   35: aload #4
    //   37: invokevirtual isEmpty : ()Z
    //   40: istore #5
    //   42: iload #5
    //   44: ifeq -> 56
    //   47: iconst_0
    //   48: istore #5
    //   50: aconst_null
    //   51: astore #4
    //   53: goto -> 76
    //   56: getstatic mindustry/Vars.content : Lmindustry/core/ContentLoader;
    //   59: astore #4
    //   61: aload_1
    //   62: getfield unit : Ljava/lang/String;
    //   65: astore #6
    //   67: aload #4
    //   69: aload #6
    //   71: invokevirtual unit : (Ljava/lang/String;)Lmindustry/type/UnitType;
    //   74: astore #4
    //   76: new arc/scene/ui/ImageButton
    //   79: astore #6
    //   81: aload #4
    //   83: ifnull -> 108
    //   86: new arc/scene/style/TextureRegionDrawable
    //   89: astore #7
    //   91: aload #4
    //   93: getfield uiIcon : Larc/graphics/g2d/TextureRegion;
    //   96: astore #8
    //   98: aload #7
    //   100: aload #8
    //   102: invokespecial <init> : (Larc/graphics/g2d/TextureRegion;)V
    //   105: goto -> 113
    //   108: getstatic mindustry/gen/Icon.add : Larc/scene/style/TextureRegionDrawable;
    //   111: astore #7
    //   113: getstatic mindustry/ui/Styles.defaulti : Larc/scene/ui/ImageButton$ImageButtonStyle;
    //   116: astore #8
    //   118: aload #6
    //   120: aload #7
    //   122: aload #8
    //   124: invokespecial <init> : (Larc/scene/style/Drawable;Larc/scene/ui/ImageButton$ImageButtonStyle;)V
    //   127: new lunarians/ui/AutoUnitUi$4
    //   130: astore #7
    //   132: aload #7
    //   134: aload_0
    //   135: aload_1
    //   136: invokespecial <init> : (Llunarians/ui/AutoUnitUi;Llunarians/data/UnitSlot;)V
    //   139: aload #6
    //   141: aload #7
    //   143: invokevirtual clicked : (Ljava/lang/Runnable;)Larc/scene/event/ClickListener;
    //   146: pop
    //   147: aload_3
    //   148: aload #6
    //   150: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   153: ldc 56.0
    //   155: invokevirtual size : (F)Larc/scene/ui/layout/Cell;
    //   158: astore #6
    //   160: ldc 1090519040
    //   162: istore #9
    //   164: ldc 8.0
    //   166: fstore #10
    //   168: aload #6
    //   170: fload #10
    //   172: invokevirtual padRight : (F)Larc/scene/ui/layout/Cell;
    //   175: pop
    //   176: aload #4
    //   178: ifnull -> 191
    //   181: aload #4
    //   183: getfield localizedName : Ljava/lang/String;
    //   186: astore #6
    //   188: goto -> 198
    //   191: ldc 'au_pick_unit'
    //   193: invokestatic get : (Ljava/lang/String;)Ljava/lang/String;
    //   196: astore #6
    //   198: aload_3
    //   199: aload #6
    //   201: invokevirtual add : (Ljava/lang/CharSequence;)Larc/scene/ui/layout/Cell;
    //   204: invokevirtual wrap : ()Larc/scene/ui/layout/Cell;
    //   207: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   210: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   213: pop
    //   214: invokestatic copyLabel : ()Ljava/lang/String;
    //   217: astore #6
    //   219: new lunarians/ui/AutoUnitUi$5
    //   222: astore #7
    //   224: aload #7
    //   226: aload_0
    //   227: aload_1
    //   228: invokespecial <init> : (Llunarians/ui/AutoUnitUi;Llunarians/data/UnitSlot;)V
    //   231: aload_3
    //   232: aload #6
    //   234: aload #7
    //   236: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   239: ldc 64.0
    //   241: invokevirtual width : (F)Larc/scene/ui/layout/Cell;
    //   244: astore #6
    //   246: ldc 1107296256
    //   248: istore #9
    //   250: ldc 32.0
    //   252: fstore #10
    //   254: aload #6
    //   256: fload #10
    //   258: invokevirtual height : (F)Larc/scene/ui/layout/Cell;
    //   261: astore #6
    //   263: ldc 4.0
    //   265: fstore #11
    //   267: aload #6
    //   269: fload #11
    //   271: invokevirtual padLeft : (F)Larc/scene/ui/layout/Cell;
    //   274: invokevirtual get : ()Larc/scene/Element;
    //   277: checkcast arc/scene/ui/TextButton
    //   280: invokevirtual getLabel : ()Larc/scene/ui/Label;
    //   283: ldc 0.8
    //   285: invokevirtual setFontScale : (F)V
    //   288: getstatic mindustry/gen/Icon.trash : Larc/scene/style/TextureRegionDrawable;
    //   291: astore #6
    //   293: getstatic mindustry/ui/Styles.defaulti : Larc/scene/ui/ImageButton$ImageButtonStyle;
    //   296: astore #12
    //   298: new lunarians/ui/AutoUnitUi$6
    //   301: astore #13
    //   303: aload #13
    //   305: aload_0
    //   306: aload_1
    //   307: invokespecial <init> : (Llunarians/ui/AutoUnitUi;Llunarians/data/UnitSlot;)V
    //   310: aload_3
    //   311: aload #6
    //   313: aload #12
    //   315: aload #13
    //   317: invokevirtual button : (Larc/scene/style/Drawable;Larc/scene/ui/ImageButton$ImageButtonStyle;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   320: astore #6
    //   322: ldc 1111490560
    //   324: istore #14
    //   326: ldc 48.0
    //   328: fstore #15
    //   330: aload #6
    //   332: fload #15
    //   334: invokevirtual size : (F)Larc/scene/ui/layout/Cell;
    //   337: pop
    //   338: aload_2
    //   339: aload_3
    //   340: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   343: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   346: invokevirtual row : ()V
    //   349: new arc/scene/ui/layout/Table
    //   352: astore_3
    //   353: aload_3
    //   354: invokespecial <init> : ()V
    //   357: ldc 'pt'
    //   359: astore #6
    //   361: getstatic lunarians/LunariansSettings.language : Ljava/lang/String;
    //   364: astore #12
    //   366: aload #6
    //   368: aload #12
    //   370: invokevirtual equals : (Ljava/lang/Object;)Z
    //   373: istore #16
    //   375: iload #16
    //   377: ifeq -> 387
    //   380: ldc 'Máx:'
    //   382: astore #6
    //   384: goto -> 391
    //   387: ldc 'Max:'
    //   389: astore #6
    //   391: aload_3
    //   392: aload #6
    //   394: invokevirtual add : (Ljava/lang/CharSequence;)Larc/scene/ui/layout/Cell;
    //   397: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   400: fload #11
    //   402: invokevirtual padRight : (F)Larc/scene/ui/layout/Cell;
    //   405: ldc 0.85
    //   407: invokevirtual fontScale : (F)Larc/scene/ui/layout/Cell;
    //   410: pop
    //   411: new lunarians/ui/AutoUnitUi$7
    //   414: astore #6
    //   416: aload #6
    //   418: aload_0
    //   419: aload_1
    //   420: invokespecial <init> : (Llunarians/ui/AutoUnitUi;Llunarians/data/UnitSlot;)V
    //   423: ldc_w '-'
    //   426: astore #8
    //   428: aload_3
    //   429: aload #8
    //   431: aload #6
    //   433: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   436: astore #6
    //   438: ldc_w 28.0
    //   441: fstore #11
    //   443: aload #6
    //   445: fload #10
    //   447: fload #11
    //   449: invokevirtual size : (FF)Larc/scene/ui/layout/Cell;
    //   452: pop
    //   453: new lunarians/ui/AutoUnitUi$8
    //   456: astore #6
    //   458: aload #6
    //   460: aload_0
    //   461: aload_1
    //   462: invokespecial <init> : (Llunarians/ui/AutoUnitUi;Llunarians/data/UnitSlot;)V
    //   465: aload_3
    //   466: aload #6
    //   468: invokevirtual label : (Larc/func/Prov;)Larc/scene/ui/layout/Cell;
    //   471: astore #6
    //   473: ldc_w 1109393408
    //   476: istore #14
    //   478: ldc_w 40.0
    //   481: fstore #15
    //   483: aload #6
    //   485: fload #15
    //   487: invokevirtual width : (F)Larc/scene/ui/layout/Cell;
    //   490: invokevirtual center : ()Larc/scene/ui/layout/Cell;
    //   493: pop
    //   494: new lunarians/ui/AutoUnitUi$9
    //   497: astore #6
    //   499: aload #6
    //   501: aload_0
    //   502: aload_1
    //   503: invokespecial <init> : (Llunarians/ui/AutoUnitUi;Llunarians/data/UnitSlot;)V
    //   506: ldc_w '+'
    //   509: astore #12
    //   511: aload_3
    //   512: aload #12
    //   514: aload #6
    //   516: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   519: fload #10
    //   521: fload #11
    //   523: invokevirtual size : (FF)Larc/scene/ui/layout/Cell;
    //   526: pop
    //   527: aload_2
    //   528: aload_3
    //   529: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   532: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   535: astore_3
    //   536: aload_3
    //   537: invokevirtual row : ()V
    //   540: ldc_w 2.0
    //   543: fstore #17
    //   545: iconst_0
    //   546: istore #16
    //   548: aconst_null
    //   549: astore #6
    //   551: aload #4
    //   553: ifnull -> 776
    //   556: aload #4
    //   558: getfield commands : Larc/struct/Seq;
    //   561: astore #7
    //   563: aload #7
    //   565: ifnull -> 776
    //   568: ldc_w 'au_command'
    //   571: invokestatic get : (Ljava/lang/String;)Ljava/lang/String;
    //   574: astore #7
    //   576: aload_2
    //   577: aload #7
    //   579: invokevirtual add : (Ljava/lang/CharSequence;)Larc/scene/ui/layout/Cell;
    //   582: astore #7
    //   584: getstatic mindustry/graphics/Pal.accent : Larc/graphics/Color;
    //   587: astore #12
    //   589: aload #7
    //   591: aload #12
    //   593: invokevirtual color : (Larc/graphics/Color;)Larc/scene/ui/layout/Cell;
    //   596: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   599: invokevirtual row : ()V
    //   602: new arc/scene/ui/layout/Table
    //   605: astore #7
    //   607: aload #7
    //   609: invokespecial <init> : ()V
    //   612: iconst_0
    //   613: istore #14
    //   615: fconst_0
    //   616: fstore #15
    //   618: aconst_null
    //   619: astore #12
    //   621: aload #4
    //   623: getfield commands : Larc/struct/Seq;
    //   626: astore #13
    //   628: aload #13
    //   630: getfield size : I
    //   633: istore #18
    //   635: iload #14
    //   637: iload #18
    //   639: if_icmpge -> 757
    //   642: aload #4
    //   644: getfield commands : Larc/struct/Seq;
    //   647: iload #14
    //   649: invokevirtual get : (I)Ljava/lang/Object;
    //   652: checkcast mindustry/ai/UnitCommand
    //   655: astore #13
    //   657: aload #13
    //   659: invokevirtual localized : ()Ljava/lang/String;
    //   662: astore #19
    //   664: new lunarians/ui/AutoUnitUi$10
    //   667: astore #20
    //   669: aload #20
    //   671: aload_0
    //   672: aload_1
    //   673: aload #13
    //   675: invokespecial <init> : (Llunarians/ui/AutoUnitUi;Llunarians/data/UnitSlot;Lmindustry/ai/UnitCommand;)V
    //   678: aload #7
    //   680: aload #19
    //   682: aload #20
    //   684: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   687: astore #19
    //   689: ldc_w 1108344832
    //   692: istore #21
    //   694: ldc_w 36.0
    //   697: fstore #22
    //   699: aload #19
    //   701: fload #22
    //   703: invokevirtual height : (F)Larc/scene/ui/layout/Cell;
    //   706: fload #17
    //   708: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   711: invokevirtual get : ()Larc/scene/Element;
    //   714: checkcast arc/scene/ui/TextButton
    //   717: astore #19
    //   719: aload #13
    //   721: getfield name : Ljava/lang/String;
    //   724: astore #13
    //   726: aload_1
    //   727: getfield command : Ljava/lang/String;
    //   730: astore #20
    //   732: aload #13
    //   734: aload #20
    //   736: invokevirtual equals : (Ljava/lang/Object;)Z
    //   739: istore #18
    //   741: aload #19
    //   743: iload #18
    //   745: invokevirtual setChecked : (Z)V
    //   748: iload #14
    //   750: iconst_1
    //   751: iadd
    //   752: istore #14
    //   754: goto -> 621
    //   757: aload_2
    //   758: aload #7
    //   760: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   763: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   766: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   769: astore #7
    //   771: aload #7
    //   773: invokevirtual row : ()V
    //   776: aload #4
    //   778: ifnull -> 1174
    //   781: getstatic mindustry/ai/UnitCommand.mineCommand : Lmindustry/ai/UnitCommand;
    //   784: getfield name : Ljava/lang/String;
    //   787: astore #7
    //   789: aload_1
    //   790: getfield command : Ljava/lang/String;
    //   793: astore #12
    //   795: aload #7
    //   797: aload #12
    //   799: invokevirtual equals : (Ljava/lang/Object;)Z
    //   802: istore #9
    //   804: iload #9
    //   806: ifeq -> 1174
    //   809: ldc_w 'au_items'
    //   812: invokestatic get : (Ljava/lang/String;)Ljava/lang/String;
    //   815: astore #7
    //   817: aload_2
    //   818: aload #7
    //   820: invokevirtual add : (Ljava/lang/CharSequence;)Larc/scene/ui/layout/Cell;
    //   823: astore #7
    //   825: getstatic mindustry/graphics/Pal.accent : Larc/graphics/Color;
    //   828: astore #12
    //   830: aload #7
    //   832: aload #12
    //   834: invokevirtual color : (Larc/graphics/Color;)Larc/scene/ui/layout/Cell;
    //   837: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   840: invokevirtual row : ()V
    //   843: new arc/scene/ui/layout/Table
    //   846: astore #7
    //   848: aload #7
    //   850: invokespecial <init> : ()V
    //   853: getstatic mindustry/Vars.content : Lmindustry/core/ContentLoader;
    //   856: invokevirtual items : ()Larc/struct/Seq;
    //   859: astore #12
    //   861: iconst_0
    //   862: istore #18
    //   864: aconst_null
    //   865: astore #13
    //   867: iconst_0
    //   868: istore #23
    //   870: aconst_null
    //   871: astore #19
    //   873: aload #12
    //   875: getfield size : I
    //   878: istore #21
    //   880: iload #18
    //   882: iload #21
    //   884: if_icmpge -> 1160
    //   887: aload #12
    //   889: iload #18
    //   891: invokevirtual get : (I)Ljava/lang/Object;
    //   894: checkcast mindustry/type/Item
    //   897: astore #20
    //   899: aload #20
    //   901: ifnull -> 1151
    //   904: aload #4
    //   906: getfield mineTier : I
    //   909: istore #24
    //   911: iload #24
    //   913: iflt -> 937
    //   916: aload #20
    //   918: getfield hardness : I
    //   921: istore #24
    //   923: aload #4
    //   925: getfield mineTier : I
    //   928: istore #25
    //   930: iload #24
    //   932: iload #25
    //   934: if_icmpgt -> 1151
    //   937: new arc/scene/ui/Button
    //   940: astore #26
    //   942: getstatic mindustry/ui/Styles.togglet : Larc/scene/ui/TextButton$TextButtonStyle;
    //   945: astore #27
    //   947: aload #26
    //   949: aload #27
    //   951: invokespecial <init> : (Larc/scene/ui/Button$ButtonStyle;)V
    //   954: aload #26
    //   956: iconst_0
    //   957: invokevirtual setProgrammaticChangeEvents : (Z)V
    //   960: new arc/scene/style/TextureRegionDrawable
    //   963: astore #27
    //   965: aload #20
    //   967: getfield uiIcon : Larc/graphics/g2d/TextureRegion;
    //   970: astore #28
    //   972: aload #27
    //   974: aload #28
    //   976: invokespecial <init> : (Larc/graphics/g2d/TextureRegion;)V
    //   979: aload #26
    //   981: aload #27
    //   983: invokevirtual image : (Larc/scene/style/Drawable;)Larc/scene/ui/layout/Cell;
    //   986: fload #11
    //   988: invokevirtual size : (F)Larc/scene/ui/layout/Cell;
    //   991: invokevirtual row : ()V
    //   994: aload #20
    //   996: getfield localizedName : Ljava/lang/String;
    //   999: astore #27
    //   1001: aload #26
    //   1003: aload #27
    //   1005: invokevirtual add : (Ljava/lang/CharSequence;)Larc/scene/ui/layout/Cell;
    //   1008: ldc_w 0.7
    //   1011: invokevirtual fontScale : (F)Larc/scene/ui/layout/Cell;
    //   1014: astore #27
    //   1016: ldc_w 70.0
    //   1019: fstore #29
    //   1021: aload #27
    //   1023: fload #29
    //   1025: invokevirtual width : (F)Larc/scene/ui/layout/Cell;
    //   1028: pop
    //   1029: aload_1
    //   1030: getfield items : Larc/struct/Seq;
    //   1033: astore #27
    //   1035: aload #20
    //   1037: getfield name : Ljava/lang/String;
    //   1040: astore #28
    //   1042: aload #27
    //   1044: aload #28
    //   1046: invokevirtual contains : (Ljava/lang/Object;)Z
    //   1049: istore #25
    //   1051: aload #26
    //   1053: iload #25
    //   1055: invokevirtual setChecked : (Z)V
    //   1058: new lunarians/ui/AutoUnitUi$11
    //   1061: astore #27
    //   1063: aload #27
    //   1065: aload_0
    //   1066: aload #26
    //   1068: aload_1
    //   1069: aload #20
    //   1071: invokespecial <init> : (Llunarians/ui/AutoUnitUi;Larc/scene/ui/Button;Llunarians/data/UnitSlot;Lmindustry/type/Item;)V
    //   1074: aload #26
    //   1076: aload #27
    //   1078: invokevirtual clicked : (Ljava/lang/Runnable;)Larc/scene/event/ClickListener;
    //   1081: pop
    //   1082: aload #7
    //   1084: aload #26
    //   1086: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   1089: astore #20
    //   1091: ldc_w 1117782016
    //   1094: istore #24
    //   1096: ldc_w 80.0
    //   1099: fstore #30
    //   1101: aload #20
    //   1103: fload #30
    //   1105: invokevirtual size : (F)Larc/scene/ui/layout/Cell;
    //   1108: astore #20
    //   1110: aload #20
    //   1112: fload #17
    //   1114: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   1117: pop
    //   1118: iload #23
    //   1120: iconst_1
    //   1121: iadd
    //   1122: istore #23
    //   1124: iconst_4
    //   1125: istore #21
    //   1127: ldc_w 5.6E-45
    //   1130: fstore #22
    //   1132: iload #23
    //   1134: iload #21
    //   1136: if_icmplt -> 1151
    //   1139: aload #7
    //   1141: invokevirtual row : ()Larc/scene/ui/layout/Table;
    //   1144: pop
    //   1145: iconst_0
    //   1146: istore #23
    //   1148: aconst_null
    //   1149: astore #19
    //   1151: iload #18
    //   1153: iconst_1
    //   1154: iadd
    //   1155: istore #18
    //   1157: goto -> 873
    //   1160: aload_2
    //   1161: aload #7
    //   1163: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   1166: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   1169: astore_1
    //   1170: aload_1
    //   1171: invokevirtual row : ()V
    //   1174: aload_2
    //   1175: areturn
  }
  
  private static String copied() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Slot copiado" : "Slot copied";
  }
  
  private static String copyLabel() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Copiar" : "Copy";
  }
  
  private static String noClip() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Nada copiado" : "Nothing copied";
  }
  
  private static String pasteLabel() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Colar" : "Paste";
  }
  
  private static String pasted() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Slot colado" : "Slot pasted";
  }
  
  private void rebuild() {
    this.list.clear();
    this.list.defaults().growX();
    Seq seq = LunariansSettings.unitSlots;
    boolean bool = seq.isEmpty();
    int i = 1086324736;
    float f1 = 6.0F;
    if (bool) {
      Table table = this.list;
      String str = LunariansI18n.get("au_empty");
      Cell cell = table.add(str).wrap().growX().padBottom(f1);
      cell.row();
    } 
    bool = false;
    Object object = null;
    int j = 0;
    float f2 = 0.0F;
    TextureRegionDrawable textureRegionDrawable = null;
    while (true) {
      int k = seq.size;
      if (j < k) {
        Table table3 = this.list;
        UnitSlot unitSlot1 = (UnitSlot)seq.get(j);
        Table table4 = card(unitSlot1);
        Cell cell = table3.add((Element)table4).growX().padBottom(f1);
        cell.row();
        j++;
        continue;
      } 
      i = seq.size;
      j = 10;
      f2 = 1.4E-44F;
      if (i < j) {
        Table table = this.list;
        textureRegionDrawable = Icon.add;
        ImageButton.ImageButtonStyle imageButtonStyle = Styles.defaulti;
        AutoUnitUi$2 autoUnitUi$2 = new AutoUnitUi$2();
        this(this);
        Cell cell = table.button((Drawable)textureRegionDrawable, imageButtonStyle, autoUnitUi$2).size(48.0F);
        j = 1082130432;
        f2 = 4.0F;
        cell = cell.padTop(f2);
        cell.row();
      } 
      Table table2 = this.list;
      int m = seq.size;
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append(m);
      stringBuilder.append("/10");
      String str1 = stringBuilder.toString();
      Cell cell1 = table2.add(str1);
      Color color = Pal.accent;
      cell1.color(color).row();
      Table table1 = new Table();
      this();
      String str2 = pasteLabel();
      AutoUnitUi$3 autoUnitUi$3 = new AutoUnitUi$3();
      this(this);
      Cell cell2 = table1.button(str2, autoUnitUi$3).width(90.0F).height(36.0F);
      j = 1073741824;
      f2 = 2.0F;
      TextButton textButton = (TextButton)cell2.pad(f2).get();
      Label label = textButton.getLabel();
      float f = 0.85F;
      label.setFontScale(f);
      UnitSlot unitSlot = clipboard;
      if (unitSlot == null)
        bool = true; 
      textButton.setDisabled(bool);
      this.list.add((Element)table1).left().padTop(f2).row();
      return;
    } 
  }
}
