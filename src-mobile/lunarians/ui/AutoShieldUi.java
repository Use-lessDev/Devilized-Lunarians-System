package lunarians.ui;

import arc.graphics.Color;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.LunariansSettings;
import lunarians.data.ShieldSlot;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;

public class AutoShieldUi {
  private static ShieldSlot clipboard;
  
  private final Table list;
  
  public AutoShieldUi(Table paramTable) {
    Table table = new Table();
    this();
    this.list = table;
    paramTable.defaults().growX();
    String str = hint();
    Cell cell = paramTable.add(str).wrap().growX();
    Color color = Color.lightGray;
    cell.color(color).padBottom(4.0F).row();
    paramTable.add((Element)table).growX().row();
    rebuild();
  }
  
  private static String all() {
    String str1 = LunariansSettings.language;
    String str2 = "pt";
    boolean bool = str2.equals(str1);
    if (!bool) {
      str2 = "es";
      boolean bool1 = str2.equals(str1);
      if (!bool1)
        return "All"; 
    } 
    return "Todas";
  }
  
  private Table card(ShieldSlot paramShieldSlot) {
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
    //   21: aload_2
    //   22: invokevirtual defaults : ()Larc/scene/ui/layout/Cell;
    //   25: astore_3
    //   26: ldc 4.0
    //   28: fstore #4
    //   30: aload_3
    //   31: fload #4
    //   33: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   36: pop
    //   37: new arc/scene/ui/layout/Table
    //   40: astore_3
    //   41: aload_3
    //   42: invokespecial <init> : ()V
    //   45: aload_1
    //   46: getfield unit : Ljava/lang/String;
    //   49: astore #5
    //   51: aload #5
    //   53: invokevirtual isEmpty : ()Z
    //   56: istore #6
    //   58: iconst_0
    //   59: istore #7
    //   61: aconst_null
    //   62: astore #8
    //   64: iload #6
    //   66: ifeq -> 78
    //   69: iconst_0
    //   70: istore #6
    //   72: aconst_null
    //   73: astore #5
    //   75: goto -> 98
    //   78: getstatic mindustry/Vars.content : Lmindustry/core/ContentLoader;
    //   81: astore #5
    //   83: aload_1
    //   84: getfield unit : Ljava/lang/String;
    //   87: astore #9
    //   89: aload #5
    //   91: aload #9
    //   93: invokevirtual unit : (Ljava/lang/String;)Lmindustry/type/UnitType;
    //   96: astore #5
    //   98: new arc/scene/ui/ImageButton
    //   101: astore #9
    //   103: aload #5
    //   105: ifnull -> 130
    //   108: new arc/scene/style/TextureRegionDrawable
    //   111: astore #10
    //   113: aload #5
    //   115: getfield uiIcon : Larc/graphics/g2d/TextureRegion;
    //   118: astore #11
    //   120: aload #10
    //   122: aload #11
    //   124: invokespecial <init> : (Larc/graphics/g2d/TextureRegion;)V
    //   127: goto -> 135
    //   130: getstatic mindustry/gen/Icon.add : Larc/scene/style/TextureRegionDrawable;
    //   133: astore #10
    //   135: getstatic mindustry/ui/Styles.defaulti : Larc/scene/ui/ImageButton$ImageButtonStyle;
    //   138: astore #11
    //   140: aload #9
    //   142: aload #10
    //   144: aload #11
    //   146: invokespecial <init> : (Larc/scene/style/Drawable;Larc/scene/ui/ImageButton$ImageButtonStyle;)V
    //   149: new lunarians/ui/AutoShieldUi$5
    //   152: astore #10
    //   154: aload #10
    //   156: aload_0
    //   157: aload_1
    //   158: invokespecial <init> : (Llunarians/ui/AutoShieldUi;Llunarians/data/ShieldSlot;)V
    //   161: aload #9
    //   163: aload #10
    //   165: invokevirtual clicked : (Ljava/lang/Runnable;)Larc/scene/event/ClickListener;
    //   168: pop
    //   169: aload_3
    //   170: aload #9
    //   172: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   175: astore #9
    //   177: ldc 1109393408
    //   179: istore #12
    //   181: ldc 40.0
    //   183: fstore #13
    //   185: aload #9
    //   187: fload #13
    //   189: invokevirtual size : (F)Larc/scene/ui/layout/Cell;
    //   192: astore #9
    //   194: ldc 6.0
    //   196: fstore #14
    //   198: aload #9
    //   200: fload #14
    //   202: invokevirtual padRight : (F)Larc/scene/ui/layout/Cell;
    //   205: pop
    //   206: aload #5
    //   208: ifnull -> 221
    //   211: aload #5
    //   213: getfield localizedName : Ljava/lang/String;
    //   216: astore #9
    //   218: goto -> 226
    //   221: invokestatic pickShield : ()Ljava/lang/String;
    //   224: astore #9
    //   226: aload_3
    //   227: aload #9
    //   229: invokevirtual add : (Ljava/lang/CharSequence;)Larc/scene/ui/layout/Cell;
    //   232: invokevirtual wrap : ()Larc/scene/ui/layout/Cell;
    //   235: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   238: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   241: astore #9
    //   243: ldc 0.9
    //   245: fstore #14
    //   247: aload #9
    //   249: fload #14
    //   251: invokevirtual fontScale : (F)Larc/scene/ui/layout/Cell;
    //   254: astore #9
    //   256: aload #5
    //   258: ifnull -> 269
    //   261: getstatic arc/graphics/Color.white : Larc/graphics/Color;
    //   264: astore #5
    //   266: goto -> 274
    //   269: getstatic arc/graphics/Color.lightGray : Larc/graphics/Color;
    //   272: astore #5
    //   274: aload #9
    //   276: aload #5
    //   278: invokevirtual color : (Larc/graphics/Color;)Larc/scene/ui/layout/Cell;
    //   281: pop
    //   282: invokestatic copyLabel : ()Ljava/lang/String;
    //   285: astore #5
    //   287: new lunarians/ui/AutoShieldUi$6
    //   290: astore #9
    //   292: aload #9
    //   294: aload_0
    //   295: aload_1
    //   296: invokespecial <init> : (Llunarians/ui/AutoShieldUi;Llunarians/data/ShieldSlot;)V
    //   299: aload_3
    //   300: aload #5
    //   302: aload #9
    //   304: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   307: ldc 64.0
    //   309: invokevirtual width : (F)Larc/scene/ui/layout/Cell;
    //   312: astore #5
    //   314: ldc 32.0
    //   316: fstore #15
    //   318: aload #5
    //   320: fload #15
    //   322: invokevirtual height : (F)Larc/scene/ui/layout/Cell;
    //   325: fload #4
    //   327: invokevirtual padLeft : (F)Larc/scene/ui/layout/Cell;
    //   330: invokevirtual get : ()Larc/scene/Element;
    //   333: checkcast arc/scene/ui/TextButton
    //   336: invokevirtual getLabel : ()Larc/scene/ui/Label;
    //   339: ldc 0.8
    //   341: invokevirtual setFontScale : (F)V
    //   344: getstatic mindustry/gen/Icon.trash : Larc/scene/style/TextureRegionDrawable;
    //   347: astore #5
    //   349: getstatic mindustry/ui/Styles.defaulti : Larc/scene/ui/ImageButton$ImageButtonStyle;
    //   352: astore #16
    //   354: new lunarians/ui/AutoShieldUi$7
    //   357: astore #17
    //   359: aload #17
    //   361: aload_0
    //   362: aload_1
    //   363: invokespecial <init> : (Llunarians/ui/AutoShieldUi;Llunarians/data/ShieldSlot;)V
    //   366: aload_3
    //   367: aload #5
    //   369: aload #16
    //   371: aload #17
    //   373: invokevirtual button : (Larc/scene/style/Drawable;Larc/scene/ui/ImageButton$ImageButtonStyle;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   376: astore #5
    //   378: ldc 36.0
    //   380: fstore #18
    //   382: aload #5
    //   384: fload #18
    //   386: invokevirtual size : (F)Larc/scene/ui/layout/Cell;
    //   389: fload #4
    //   391: invokevirtual padLeft : (F)Larc/scene/ui/layout/Cell;
    //   394: pop
    //   395: aload_2
    //   396: aload_3
    //   397: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   400: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   403: invokevirtual row : ()V
    //   406: new arc/scene/ui/layout/Table
    //   409: astore_3
    //   410: aload_3
    //   411: invokespecial <init> : ()V
    //   414: invokestatic pasteLabel : ()Ljava/lang/String;
    //   417: astore #5
    //   419: new lunarians/ui/AutoShieldUi$8
    //   422: astore #17
    //   424: aload #17
    //   426: aload_0
    //   427: invokespecial <init> : (Llunarians/ui/AutoShieldUi;)V
    //   430: aload_3
    //   431: aload #5
    //   433: aload #17
    //   435: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   438: astore #5
    //   440: ldc_w 1119092736
    //   443: istore #19
    //   445: ldc_w 90.0
    //   448: fstore #20
    //   450: aload #5
    //   452: fload #20
    //   454: invokevirtual width : (F)Larc/scene/ui/layout/Cell;
    //   457: fload #18
    //   459: invokevirtual height : (F)Larc/scene/ui/layout/Cell;
    //   462: astore #5
    //   464: ldc_w 2.0
    //   467: fstore #18
    //   469: aload #5
    //   471: fload #18
    //   473: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   476: invokevirtual get : ()Larc/scene/Element;
    //   479: checkcast arc/scene/ui/TextButton
    //   482: astore #5
    //   484: aload #5
    //   486: invokevirtual getLabel : ()Larc/scene/ui/Label;
    //   489: astore #17
    //   491: ldc_w 0.85
    //   494: fstore #21
    //   496: aload #17
    //   498: fload #21
    //   500: invokevirtual setFontScale : (F)V
    //   503: getstatic lunarians/ui/AutoShieldUi.clipboard : Llunarians/data/ShieldSlot;
    //   506: astore #17
    //   508: aload #17
    //   510: ifnonnull -> 524
    //   513: iconst_1
    //   514: istore #19
    //   516: ldc_w 1.4E-45
    //   519: fstore #20
    //   521: goto -> 533
    //   524: iconst_0
    //   525: istore #19
    //   527: fconst_0
    //   528: fstore #20
    //   530: aconst_null
    //   531: astore #17
    //   533: aload #5
    //   535: iload #19
    //   537: invokevirtual setDisabled : (Z)V
    //   540: aload_0
    //   541: getfield list : Larc/scene/ui/layout/Table;
    //   544: aload_3
    //   545: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   548: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   551: fload #18
    //   553: invokevirtual padTop : (F)Larc/scene/ui/layout/Cell;
    //   556: invokevirtual row : ()V
    //   559: new arc/scene/ui/layout/Table
    //   562: astore_3
    //   563: aload_3
    //   564: invokespecial <init> : ()V
    //   567: invokestatic maxLabel : ()Ljava/lang/String;
    //   570: astore #5
    //   572: aload_3
    //   573: aload #5
    //   575: invokevirtual add : (Ljava/lang/CharSequence;)Larc/scene/ui/layout/Cell;
    //   578: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   581: fload #4
    //   583: invokevirtual padRight : (F)Larc/scene/ui/layout/Cell;
    //   586: fload #21
    //   588: invokevirtual fontScale : (F)Larc/scene/ui/layout/Cell;
    //   591: pop
    //   592: new lunarians/ui/AutoShieldUi$9
    //   595: astore #5
    //   597: aload #5
    //   599: aload_0
    //   600: aload_1
    //   601: invokespecial <init> : (Llunarians/ui/AutoShieldUi;Llunarians/data/ShieldSlot;)V
    //   604: ldc_w '-'
    //   607: astore #17
    //   609: aload_3
    //   610: aload #17
    //   612: aload #5
    //   614: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   617: astore #5
    //   619: ldc_w 1105199104
    //   622: istore #19
    //   624: ldc_w 28.0
    //   627: fstore #20
    //   629: aload #5
    //   631: fload #15
    //   633: fload #20
    //   635: invokevirtual size : (FF)Larc/scene/ui/layout/Cell;
    //   638: pop
    //   639: new lunarians/ui/AutoShieldUi$10
    //   642: astore #5
    //   644: aload #5
    //   646: aload_0
    //   647: aload_1
    //   648: invokespecial <init> : (Llunarians/ui/AutoShieldUi;Llunarians/data/ShieldSlot;)V
    //   651: aload_3
    //   652: aload #5
    //   654: invokevirtual label : (Larc/func/Prov;)Larc/scene/ui/layout/Cell;
    //   657: fload #13
    //   659: invokevirtual width : (F)Larc/scene/ui/layout/Cell;
    //   662: invokevirtual center : ()Larc/scene/ui/layout/Cell;
    //   665: fload #18
    //   667: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   670: pop
    //   671: new lunarians/ui/AutoShieldUi$11
    //   674: astore #5
    //   676: aload #5
    //   678: aload_0
    //   679: aload_1
    //   680: invokespecial <init> : (Llunarians/ui/AutoShieldUi;Llunarians/data/ShieldSlot;)V
    //   683: ldc_w '+'
    //   686: astore #10
    //   688: aload_3
    //   689: aload #10
    //   691: aload #5
    //   693: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   696: fload #15
    //   698: fload #20
    //   700: invokevirtual size : (FF)Larc/scene/ui/layout/Cell;
    //   703: pop
    //   704: aload_2
    //   705: aload_3
    //   706: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   709: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   712: fload #18
    //   714: invokevirtual padTop : (F)Larc/scene/ui/layout/Cell;
    //   717: invokevirtual row : ()V
    //   720: new arc/scene/ui/layout/Table
    //   723: astore_3
    //   724: aload_3
    //   725: invokespecial <init> : ()V
    //   728: invokestatic followLabel : ()Ljava/lang/String;
    //   731: astore #5
    //   733: aload_3
    //   734: aload #5
    //   736: invokevirtual add : (Ljava/lang/CharSequence;)Larc/scene/ui/layout/Cell;
    //   739: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   742: fload #4
    //   744: invokevirtual padRight : (F)Larc/scene/ui/layout/Cell;
    //   747: astore #5
    //   749: aload #5
    //   751: fload #21
    //   753: invokevirtual fontScale : (F)Larc/scene/ui/layout/Cell;
    //   756: pop
    //   757: aload_1
    //   758: getfield followId : I
    //   761: istore #6
    //   763: iload #6
    //   765: iflt -> 799
    //   768: getstatic mindustry/gen/Groups.unit : Lmindustry/entities/EntityGroup;
    //   771: astore #5
    //   773: aload_1
    //   774: getfield followId : I
    //   777: istore #7
    //   779: aload #5
    //   781: iload #7
    //   783: invokevirtual getByID : (I)Lmindustry/gen/Entityc;
    //   786: astore #5
    //   788: aload #5
    //   790: astore #8
    //   792: aload #5
    //   794: checkcast mindustry/gen/Unit
    //   797: astore #8
    //   799: ldc_w '  #'
    //   802: astore #5
    //   804: aload #8
    //   806: ifnull -> 959
    //   809: aload #8
    //   811: invokevirtual isValid : ()Z
    //   814: istore #12
    //   816: iload #12
    //   818: ifeq -> 959
    //   821: new arc/scene/ui/Image
    //   824: astore #10
    //   826: aload #8
    //   828: getfield type : Lmindustry/type/UnitType;
    //   831: getfield uiIcon : Larc/graphics/g2d/TextureRegion;
    //   834: astore #22
    //   836: aload #10
    //   838: aload #22
    //   840: invokespecial <init> : (Larc/graphics/g2d/TextureRegion;)V
    //   843: aload_3
    //   844: aload #10
    //   846: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   849: fload #20
    //   851: invokevirtual size : (F)Larc/scene/ui/layout/Cell;
    //   854: astore #10
    //   856: aload #10
    //   858: fload #4
    //   860: invokevirtual padRight : (F)Larc/scene/ui/layout/Cell;
    //   863: pop
    //   864: aload #8
    //   866: getfield type : Lmindustry/type/UnitType;
    //   869: getfield localizedName : Ljava/lang/String;
    //   872: astore #8
    //   874: aload_1
    //   875: getfield followId : I
    //   878: istore #12
    //   880: new java/lang/StringBuilder
    //   883: astore #17
    //   885: aload #17
    //   887: invokespecial <init> : ()V
    //   890: aload #17
    //   892: aload #8
    //   894: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   897: pop
    //   898: aload #17
    //   900: aload #5
    //   902: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   905: pop
    //   906: aload #17
    //   908: iload #12
    //   910: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   913: pop
    //   914: aload #17
    //   916: invokevirtual toString : ()Ljava/lang/String;
    //   919: astore #5
    //   921: aload_3
    //   922: aload #5
    //   924: invokevirtual add : (Ljava/lang/CharSequence;)Larc/scene/ui/layout/Cell;
    //   927: invokevirtual wrap : ()Larc/scene/ui/layout/Cell;
    //   930: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   933: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   936: fload #14
    //   938: invokevirtual fontScale : (F)Larc/scene/ui/layout/Cell;
    //   941: astore #5
    //   943: getstatic mindustry/graphics/Pal.accent : Larc/graphics/Color;
    //   946: astore #8
    //   948: aload #5
    //   950: aload #8
    //   952: invokevirtual color : (Larc/graphics/Color;)Larc/scene/ui/layout/Cell;
    //   955: pop
    //   956: goto -> 1180
    //   959: aload_1
    //   960: getfield followId : I
    //   963: istore #7
    //   965: iload #7
    //   967: iflt -> 1140
    //   970: aload_1
    //   971: getfield followLabel : Ljava/lang/String;
    //   974: astore #8
    //   976: aload #8
    //   978: ifnull -> 1008
    //   981: aload_1
    //   982: getfield followLabel : Ljava/lang/String;
    //   985: astore #8
    //   987: aload #8
    //   989: invokevirtual isEmpty : ()Z
    //   992: istore #7
    //   994: iload #7
    //   996: ifne -> 1008
    //   999: aload_1
    //   1000: getfield followLabel : Ljava/lang/String;
    //   1003: astore #8
    //   1005: goto -> 1046
    //   1008: aload_1
    //   1009: getfield followId : I
    //   1012: istore #7
    //   1014: new java/lang/StringBuilder
    //   1017: astore #10
    //   1019: ldc_w '#'
    //   1022: astore #11
    //   1024: aload #10
    //   1026: aload #11
    //   1028: invokespecial <init> : (Ljava/lang/String;)V
    //   1031: aload #10
    //   1033: iload #7
    //   1035: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1038: pop
    //   1039: aload #10
    //   1041: invokevirtual toString : ()Ljava/lang/String;
    //   1044: astore #8
    //   1046: aload_1
    //   1047: getfield followId : I
    //   1050: istore #12
    //   1052: new java/lang/StringBuilder
    //   1055: astore #11
    //   1057: aload #11
    //   1059: invokespecial <init> : ()V
    //   1062: aload #11
    //   1064: aload #8
    //   1066: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1069: pop
    //   1070: aload #11
    //   1072: aload #5
    //   1074: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1077: pop
    //   1078: aload #11
    //   1080: iload #12
    //   1082: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1085: pop
    //   1086: aload #11
    //   1088: ldc_w ' [dead?]'
    //   1091: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1094: pop
    //   1095: aload #11
    //   1097: invokevirtual toString : ()Ljava/lang/String;
    //   1100: astore #5
    //   1102: aload_3
    //   1103: aload #5
    //   1105: invokevirtual add : (Ljava/lang/CharSequence;)Larc/scene/ui/layout/Cell;
    //   1108: invokevirtual wrap : ()Larc/scene/ui/layout/Cell;
    //   1111: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   1114: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   1117: fload #21
    //   1119: invokevirtual fontScale : (F)Larc/scene/ui/layout/Cell;
    //   1122: astore #5
    //   1124: getstatic arc/graphics/Color.orange : Larc/graphics/Color;
    //   1127: astore #8
    //   1129: aload #5
    //   1131: aload #8
    //   1133: invokevirtual color : (Larc/graphics/Color;)Larc/scene/ui/layout/Cell;
    //   1136: pop
    //   1137: goto -> 1180
    //   1140: invokestatic pickFollow : ()Ljava/lang/String;
    //   1143: astore #5
    //   1145: aload_3
    //   1146: aload #5
    //   1148: invokevirtual add : (Ljava/lang/CharSequence;)Larc/scene/ui/layout/Cell;
    //   1151: invokevirtual wrap : ()Larc/scene/ui/layout/Cell;
    //   1154: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   1157: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   1160: fload #21
    //   1162: invokevirtual fontScale : (F)Larc/scene/ui/layout/Cell;
    //   1165: astore #5
    //   1167: getstatic arc/graphics/Color.lightGray : Larc/graphics/Color;
    //   1170: astore #8
    //   1172: aload #5
    //   1174: aload #8
    //   1176: invokevirtual color : (Larc/graphics/Color;)Larc/scene/ui/layout/Cell;
    //   1179: pop
    //   1180: invokestatic mapBtn : ()Ljava/lang/String;
    //   1183: astore #5
    //   1185: new lunarians/ui/AutoShieldUi$12
    //   1188: astore #8
    //   1190: aload #8
    //   1192: aload_0
    //   1193: aload_1
    //   1194: invokespecial <init> : (Llunarians/ui/AutoShieldUi;Llunarians/data/ShieldSlot;)V
    //   1197: aload_3
    //   1198: aload #5
    //   1200: aload #8
    //   1202: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   1205: ldc_w 100.0
    //   1208: invokevirtual width : (F)Larc/scene/ui/layout/Cell;
    //   1211: fload #15
    //   1213: invokevirtual height : (F)Larc/scene/ui/layout/Cell;
    //   1216: fload #4
    //   1218: invokevirtual padLeft : (F)Larc/scene/ui/layout/Cell;
    //   1221: invokevirtual get : ()Larc/scene/Element;
    //   1224: checkcast arc/scene/ui/TextButton
    //   1227: invokevirtual getLabel : ()Larc/scene/ui/Label;
    //   1230: fload #21
    //   1232: invokevirtual setFontScale : (F)V
    //   1235: aload_2
    //   1236: aload_3
    //   1237: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   1240: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   1243: fload #18
    //   1245: invokevirtual padTop : (F)Larc/scene/ui/layout/Cell;
    //   1248: invokevirtual row : ()V
    //   1251: aload_2
    //   1252: areturn
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
  
  private static String emptyMsg() {
    String str1 = LunariansSettings.language;
    String str2 = "pt";
    boolean bool = str2.equals(str1);
    return bool ? "Nenhum slot. Aperte +" : "No slots. Press +";
  }
  
  private static String followLabel() {
    String str1 = LunariansSettings.language;
    String str2 = "pt";
    boolean bool = str2.equals(str1);
    if (!bool) {
      str2 = "es";
      boolean bool1 = str2.equals(str1);
      if (!bool1)
        return "Follow:"; 
    } 
    return "Seguir:";
  }
  
  private static String hint() {
    String str1 = LunariansSettings.language;
    String str2 = "pt";
    boolean bool = str2.equals(str1);
    return bool ? "1) Unidade shield  2) Máx  3) Mapa = toque na unidade a seguir. Movimento por comando." : "1) Shield unit  2) Max  3) Map = tap unit to follow. Uses unit commands.";
  }
  
  public static BuilderDialog make() {
    BuilderDialog builderDialog = new BuilderDialog();
    String str = title();
    AutoShieldUi$1 autoShieldUi$1 = new AutoShieldUi$1();
    this();
    AutoShieldUi$2 autoShieldUi$2 = new AutoShieldUi$2();
    this();
    this(str, autoShieldUi$1, autoShieldUi$2);
    return builderDialog;
  }
  
  private static String mapBtn() {
    String str1 = LunariansSettings.language;
    String str2 = "pt";
    boolean bool = str2.equals(str1);
    if (!bool) {
      str2 = "es";
      boolean bool1 = str2.equals(str1);
      if (!bool1)
        return "Map"; 
    } 
    return "Mapa";
  }
  
  private static String maxLabel() {
    String str1 = LunariansSettings.language;
    String str2 = "pt";
    boolean bool = str2.equals(str1);
    if (!bool) {
      str2 = "es";
      boolean bool1 = str2.equals(str1);
      if (!bool1)
        return "Max:"; 
    } 
    return "Máx:";
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
  
  private static String pickFollow() {
    String str1 = LunariansSettings.language;
    String str2 = "pt";
    boolean bool = str2.equals(str1);
    return bool ? "Nenhuma — toque Mapa" : "None — tap Map";
  }
  
  private static String pickShield() {
    String str1 = LunariansSettings.language;
    String str2 = "pt";
    boolean bool = str2.equals(str1);
    return bool ? "Escolher shield" : "Pick shield unit";
  }
  
  private void rebuild() {
    this.list.clearChildren();
    Seq seq = LunariansSettings.shieldSlots;
    Table table = this.list;
    int i = seq.size;
    StringBuilder stringBuilder = new StringBuilder();
    this();
    stringBuilder.append(i);
    stringBuilder.append("/8");
    String str = stringBuilder.toString();
    Cell cell = table.add(str);
    Color color = Pal.accent;
    cell = cell.color(color);
    i = 1082130432;
    float f1 = 4.0F;
    cell = cell.padBottom(f1);
    cell.row();
    boolean bool = seq.isEmpty();
    if (bool) {
      Table table1 = this.list;
      String str1 = emptyMsg();
      Cell cell1 = table1.add(str1).wrap().growX().padBottom(f1);
      cell1.row();
    } else {
      bool = false;
      float f = 0.0F;
      cell = null;
      while (true) {
        i = seq.size;
        if (bool < i) {
          Table table2 = this.list;
          ShieldSlot shieldSlot = (ShieldSlot)seq.get(bool);
          Table table1 = card(shieldSlot);
          Cell cell1 = table2.add((Element)table1).growX();
          float f3 = 3.0F;
          cell1 = cell1.padBottom(f3);
          cell1.row();
          int m = bool + 1;
          continue;
        } 
        break;
      } 
    } 
    int k = seq.size;
    int j = 8;
    float f2 = 1.1E-44F;
    if (k < j) {
      Table table1 = this.list;
      TextureRegionDrawable textureRegionDrawable = Icon.add;
      ImageButton.ImageButtonStyle imageButtonStyle = Styles.defaulti;
      AutoShieldUi$4 autoShieldUi$4 = new AutoShieldUi$4();
      this(this);
      Cell cell1 = table1.button((Drawable)textureRegionDrawable, imageButtonStyle, autoShieldUi$4).size(40.0F);
      j = 1073741824;
      f2 = 2.0F;
      cell1 = cell1.padTop(f2);
      cell1.row();
    } 
  }
  
  private static void reopen() {
    BuilderDialog builderDialog = make();
    AutoShieldUi$3 autoShieldUi$3 = new AutoShieldUi$3();
    this();
    builderDialog.after(autoShieldUi$3).show();
  }
  
  private static String title() {
    String str = LunariansSettings.language;
    "pt".equals(str);
    return "Auto shield";
  }
}
