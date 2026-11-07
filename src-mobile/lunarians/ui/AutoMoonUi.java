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
import lunarians.data.MoonSlot;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;

public class AutoMoonUi {
  private static MoonSlot clipboard;
  
  private final Table list;
  
  public AutoMoonUi(Table paramTable) {
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
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Todas" : "All";
  }
  
  private Table card(MoonSlot paramMoonSlot) {
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
    //   17: astore_3
    //   18: ldc 4.0
    //   20: fstore #4
    //   22: aload_3
    //   23: fload #4
    //   25: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   28: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   31: pop
    //   32: new arc/scene/ui/layout/Table
    //   35: astore_3
    //   36: aload_3
    //   37: invokespecial <init> : ()V
    //   40: aload_1
    //   41: getfield unit : Ljava/lang/String;
    //   44: astore #5
    //   46: aload #5
    //   48: invokevirtual isEmpty : ()Z
    //   51: istore #6
    //   53: iload #6
    //   55: ifeq -> 67
    //   58: iconst_0
    //   59: istore #6
    //   61: aconst_null
    //   62: astore #5
    //   64: goto -> 87
    //   67: getstatic mindustry/Vars.content : Lmindustry/core/ContentLoader;
    //   70: astore #5
    //   72: aload_1
    //   73: getfield unit : Ljava/lang/String;
    //   76: astore #7
    //   78: aload #5
    //   80: aload #7
    //   82: invokevirtual unit : (Ljava/lang/String;)Lmindustry/type/UnitType;
    //   85: astore #5
    //   87: new arc/scene/ui/ImageButton
    //   90: astore #7
    //   92: aload #5
    //   94: ifnull -> 119
    //   97: new arc/scene/style/TextureRegionDrawable
    //   100: astore #8
    //   102: aload #5
    //   104: getfield uiIcon : Larc/graphics/g2d/TextureRegion;
    //   107: astore #9
    //   109: aload #8
    //   111: aload #9
    //   113: invokespecial <init> : (Larc/graphics/g2d/TextureRegion;)V
    //   116: goto -> 124
    //   119: getstatic mindustry/gen/Icon.add : Larc/scene/style/TextureRegionDrawable;
    //   122: astore #8
    //   124: getstatic mindustry/ui/Styles.defaulti : Larc/scene/ui/ImageButton$ImageButtonStyle;
    //   127: astore #9
    //   129: aload #7
    //   131: aload #8
    //   133: aload #9
    //   135: invokespecial <init> : (Larc/scene/style/Drawable;Larc/scene/ui/ImageButton$ImageButtonStyle;)V
    //   138: new lunarians/ui/AutoMoonUi$5
    //   141: astore #8
    //   143: aload #8
    //   145: aload_0
    //   146: aload_1
    //   147: invokespecial <init> : (Llunarians/ui/AutoMoonUi;Llunarians/data/MoonSlot;)V
    //   150: aload #7
    //   152: aload #8
    //   154: invokevirtual clicked : (Ljava/lang/Runnable;)Larc/scene/event/ClickListener;
    //   157: pop
    //   158: aload_3
    //   159: aload #7
    //   161: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   164: ldc 40.0
    //   166: invokevirtual size : (F)Larc/scene/ui/layout/Cell;
    //   169: astore #7
    //   171: ldc 1086324736
    //   173: istore #10
    //   175: ldc 6.0
    //   177: fstore #11
    //   179: aload #7
    //   181: fload #11
    //   183: invokevirtual padRight : (F)Larc/scene/ui/layout/Cell;
    //   186: pop
    //   187: aload #5
    //   189: ifnull -> 202
    //   192: aload #5
    //   194: getfield localizedName : Ljava/lang/String;
    //   197: astore #5
    //   199: goto -> 207
    //   202: invokestatic pick : ()Ljava/lang/String;
    //   205: astore #5
    //   207: aload_3
    //   208: aload #5
    //   210: invokevirtual add : (Ljava/lang/CharSequence;)Larc/scene/ui/layout/Cell;
    //   213: invokevirtual wrap : ()Larc/scene/ui/layout/Cell;
    //   216: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   219: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   222: ldc 0.9
    //   224: invokevirtual fontScale : (F)Larc/scene/ui/layout/Cell;
    //   227: pop
    //   228: invokestatic copyLabel : ()Ljava/lang/String;
    //   231: astore #5
    //   233: new lunarians/ui/AutoMoonUi$6
    //   236: astore #7
    //   238: aload #7
    //   240: aload_0
    //   241: aload_1
    //   242: invokespecial <init> : (Llunarians/ui/AutoMoonUi;Llunarians/data/MoonSlot;)V
    //   245: aload_3
    //   246: aload #5
    //   248: aload #7
    //   250: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   253: ldc 64.0
    //   255: invokevirtual width : (F)Larc/scene/ui/layout/Cell;
    //   258: astore #5
    //   260: ldc 32.0
    //   262: fstore #12
    //   264: aload #5
    //   266: fload #12
    //   268: invokevirtual height : (F)Larc/scene/ui/layout/Cell;
    //   271: fload #4
    //   273: invokevirtual padLeft : (F)Larc/scene/ui/layout/Cell;
    //   276: invokevirtual get : ()Larc/scene/Element;
    //   279: checkcast arc/scene/ui/TextButton
    //   282: invokevirtual getLabel : ()Larc/scene/ui/Label;
    //   285: astore #5
    //   287: ldc 0.8
    //   289: fstore #13
    //   291: aload #5
    //   293: fload #13
    //   295: invokevirtual setFontScale : (F)V
    //   298: getstatic mindustry/gen/Icon.trash : Larc/scene/style/TextureRegionDrawable;
    //   301: astore #5
    //   303: getstatic mindustry/ui/Styles.defaulti : Larc/scene/ui/ImageButton$ImageButtonStyle;
    //   306: astore #7
    //   308: new lunarians/ui/AutoMoonUi$7
    //   311: astore #8
    //   313: aload #8
    //   315: aload_0
    //   316: aload_1
    //   317: invokespecial <init> : (Llunarians/ui/AutoMoonUi;Llunarians/data/MoonSlot;)V
    //   320: aload_3
    //   321: aload #5
    //   323: aload #7
    //   325: aload #8
    //   327: invokevirtual button : (Larc/scene/style/Drawable;Larc/scene/ui/ImageButton$ImageButtonStyle;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   330: astore #5
    //   332: ldc 36.0
    //   334: fstore #14
    //   336: aload #5
    //   338: fload #14
    //   340: invokevirtual size : (F)Larc/scene/ui/layout/Cell;
    //   343: pop
    //   344: aload_2
    //   345: aload_3
    //   346: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   349: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   352: invokevirtual row : ()V
    //   355: new arc/scene/ui/layout/Table
    //   358: astore_3
    //   359: aload_3
    //   360: invokespecial <init> : ()V
    //   363: invokestatic pasteLabel : ()Ljava/lang/String;
    //   366: astore #5
    //   368: new lunarians/ui/AutoMoonUi$8
    //   371: astore #8
    //   373: aload #8
    //   375: aload_0
    //   376: invokespecial <init> : (Llunarians/ui/AutoMoonUi;)V
    //   379: aload_3
    //   380: aload #5
    //   382: aload #8
    //   384: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   387: astore #5
    //   389: ldc 1119092736
    //   391: istore #10
    //   393: ldc 90.0
    //   395: fstore #11
    //   397: aload #5
    //   399: fload #11
    //   401: invokevirtual width : (F)Larc/scene/ui/layout/Cell;
    //   404: fload #14
    //   406: invokevirtual height : (F)Larc/scene/ui/layout/Cell;
    //   409: astore #5
    //   411: ldc 1073741824
    //   413: istore #15
    //   415: ldc 2.0
    //   417: fstore #14
    //   419: aload #5
    //   421: fload #14
    //   423: invokevirtual pad : (F)Larc/scene/ui/layout/Cell;
    //   426: invokevirtual get : ()Larc/scene/Element;
    //   429: checkcast arc/scene/ui/TextButton
    //   432: astore #5
    //   434: aload #5
    //   436: invokevirtual getLabel : ()Larc/scene/ui/Label;
    //   439: astore #8
    //   441: ldc_w 0.85
    //   444: fstore #16
    //   446: aload #8
    //   448: fload #16
    //   450: invokevirtual setFontScale : (F)V
    //   453: getstatic lunarians/ui/AutoMoonUi.clipboard : Llunarians/data/MoonSlot;
    //   456: astore #8
    //   458: iconst_1
    //   459: istore #17
    //   461: aload #8
    //   463: ifnonnull -> 477
    //   466: iconst_1
    //   467: istore #10
    //   469: ldc_w 1.4E-45
    //   472: fstore #11
    //   474: goto -> 486
    //   477: iconst_0
    //   478: istore #10
    //   480: fconst_0
    //   481: fstore #11
    //   483: aconst_null
    //   484: astore #8
    //   486: aload #5
    //   488: iload #10
    //   490: invokevirtual setDisabled : (Z)V
    //   493: aload_0
    //   494: getfield list : Larc/scene/ui/layout/Table;
    //   497: aload_3
    //   498: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   501: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   504: fload #14
    //   506: invokevirtual padTop : (F)Larc/scene/ui/layout/Cell;
    //   509: astore_3
    //   510: aload_3
    //   511: invokevirtual row : ()V
    //   514: invokestatic maxL : ()Ljava/lang/String;
    //   517: astore #5
    //   519: aload_1
    //   520: getfield maxUnits : I
    //   523: istore #18
    //   525: iload #18
    //   527: ifgt -> 537
    //   530: invokestatic all : ()Ljava/lang/String;
    //   533: astore_3
    //   534: goto -> 549
    //   537: aload_1
    //   538: getfield maxUnits : I
    //   541: istore #18
    //   543: iload #18
    //   545: invokestatic valueOf : (I)Ljava/lang/String;
    //   548: astore_3
    //   549: aload_3
    //   550: astore #7
    //   552: new lunarians/ui/AutoMoonUi$9
    //   555: astore #8
    //   557: aload #8
    //   559: aload_0
    //   560: aload_1
    //   561: invokespecial <init> : (Llunarians/ui/AutoMoonUi;Llunarians/data/MoonSlot;)V
    //   564: new lunarians/ui/AutoMoonUi$10
    //   567: astore #9
    //   569: aload #9
    //   571: aload_0
    //   572: aload_1
    //   573: invokespecial <init> : (Llunarians/ui/AutoMoonUi;Llunarians/data/MoonSlot;)V
    //   576: aload_0
    //   577: astore_3
    //   578: aload_0
    //   579: aload_2
    //   580: aload #5
    //   582: aload #7
    //   584: aload #8
    //   586: aload #9
    //   588: invokespecial rowStepper : (Larc/scene/ui/layout/Table;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Runnable;Ljava/lang/Runnable;)V
    //   591: invokestatic radL : ()Ljava/lang/String;
    //   594: astore #5
    //   596: aload_1
    //   597: getfield radius : F
    //   600: f2i
    //   601: istore #18
    //   603: new java/lang/StringBuilder
    //   606: astore #7
    //   608: aload #7
    //   610: invokespecial <init> : ()V
    //   613: aload #7
    //   615: iload #18
    //   617: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   620: pop
    //   621: aload #7
    //   623: ldc_w 'b'
    //   626: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   629: pop
    //   630: aload #7
    //   632: invokevirtual toString : ()Ljava/lang/String;
    //   635: astore #7
    //   637: new lunarians/ui/AutoMoonUi$11
    //   640: astore #8
    //   642: aload #8
    //   644: aload_0
    //   645: aload_1
    //   646: invokespecial <init> : (Llunarians/ui/AutoMoonUi;Llunarians/data/MoonSlot;)V
    //   649: new lunarians/ui/AutoMoonUi$12
    //   652: astore #9
    //   654: aload #9
    //   656: aload_0
    //   657: aload_1
    //   658: invokespecial <init> : (Llunarians/ui/AutoMoonUi;Llunarians/data/MoonSlot;)V
    //   661: aload_0
    //   662: aload_2
    //   663: aload #5
    //   665: aload #7
    //   667: aload #8
    //   669: aload #9
    //   671: invokespecial rowStepper : (Larc/scene/ui/layout/Table;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Runnable;Ljava/lang/Runnable;)V
    //   674: invokestatic speedL : ()Ljava/lang/String;
    //   677: astore #5
    //   679: aload_1
    //   680: getfield speed : F
    //   683: fstore #19
    //   685: fload #19
    //   687: invokestatic valueOf : (F)Ljava/lang/Float;
    //   690: astore_3
    //   691: iload #17
    //   693: anewarray java/lang/Object
    //   696: astore #7
    //   698: aload #7
    //   700: iconst_0
    //   701: aload_3
    //   702: aastore
    //   703: ldc_w '%.2fx'
    //   706: aload #7
    //   708: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   711: astore #7
    //   713: new lunarians/ui/AutoMoonUi$13
    //   716: astore #8
    //   718: aload #8
    //   720: aload_0
    //   721: aload_1
    //   722: invokespecial <init> : (Llunarians/ui/AutoMoonUi;Llunarians/data/MoonSlot;)V
    //   725: new lunarians/ui/AutoMoonUi$14
    //   728: astore #9
    //   730: aload #9
    //   732: aload_0
    //   733: aload_1
    //   734: invokespecial <init> : (Llunarians/ui/AutoMoonUi;Llunarians/data/MoonSlot;)V
    //   737: aload_0
    //   738: astore_3
    //   739: aload_0
    //   740: aload_2
    //   741: aload #5
    //   743: aload #7
    //   745: aload #8
    //   747: aload #9
    //   749: invokespecial rowStepper : (Larc/scene/ui/layout/Table;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Runnable;Ljava/lang/Runnable;)V
    //   752: new arc/scene/ui/layout/Table
    //   755: astore_3
    //   756: aload_3
    //   757: invokespecial <init> : ()V
    //   760: aload_1
    //   761: invokevirtual hasUnitCenter : ()Z
    //   764: istore #6
    //   766: iload #6
    //   768: ifeq -> 1002
    //   771: getstatic mindustry/gen/Groups.unit : Lmindustry/entities/EntityGroup;
    //   774: astore #5
    //   776: aload_1
    //   777: getfield followId : I
    //   780: istore #15
    //   782: aload #5
    //   784: iload #15
    //   786: invokevirtual getByID : (I)Lmindustry/gen/Entityc;
    //   789: checkcast mindustry/gen/Unit
    //   792: astore #5
    //   794: ldc_w ' #'
    //   797: astore #7
    //   799: aload #5
    //   801: ifnull -> 876
    //   804: aload #5
    //   806: invokevirtual isValid : ()Z
    //   809: istore #10
    //   811: iload #10
    //   813: ifeq -> 876
    //   816: aload #5
    //   818: getfield type : Lmindustry/type/UnitType;
    //   821: getfield localizedName : Ljava/lang/String;
    //   824: astore #5
    //   826: aload_1
    //   827: getfield followId : I
    //   830: istore #10
    //   832: new java/lang/StringBuilder
    //   835: astore #9
    //   837: aload #9
    //   839: invokespecial <init> : ()V
    //   842: aload #9
    //   844: aload #5
    //   846: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   849: pop
    //   850: aload #9
    //   852: aload #7
    //   854: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   857: pop
    //   858: aload #9
    //   860: iload #10
    //   862: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   865: pop
    //   866: aload #9
    //   868: invokevirtual toString : ()Ljava/lang/String;
    //   871: astore #5
    //   873: goto -> 1082
    //   876: aload_1
    //   877: getfield followLabel : Ljava/lang/String;
    //   880: astore #5
    //   882: aload #5
    //   884: ifnull -> 961
    //   887: aload_1
    //   888: getfield followLabel : Ljava/lang/String;
    //   891: astore #5
    //   893: aload #5
    //   895: invokevirtual isEmpty : ()Z
    //   898: istore #6
    //   900: iload #6
    //   902: ifne -> 961
    //   905: aload_1
    //   906: getfield followLabel : Ljava/lang/String;
    //   909: astore #5
    //   911: aload_1
    //   912: getfield followId : I
    //   915: istore #10
    //   917: new java/lang/StringBuilder
    //   920: astore #9
    //   922: aload #9
    //   924: invokespecial <init> : ()V
    //   927: aload #9
    //   929: aload #5
    //   931: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   934: pop
    //   935: aload #9
    //   937: aload #7
    //   939: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   942: pop
    //   943: aload #9
    //   945: iload #10
    //   947: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   950: pop
    //   951: aload #9
    //   953: invokevirtual toString : ()Ljava/lang/String;
    //   956: astore #5
    //   958: goto -> 1082
    //   961: aload_1
    //   962: getfield followId : I
    //   965: istore #6
    //   967: new java/lang/StringBuilder
    //   970: astore #7
    //   972: ldc_w '#'
    //   975: astore #8
    //   977: aload #7
    //   979: aload #8
    //   981: invokespecial <init> : (Ljava/lang/String;)V
    //   984: aload #7
    //   986: iload #6
    //   988: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   991: pop
    //   992: aload #7
    //   994: invokevirtual toString : ()Ljava/lang/String;
    //   997: astore #5
    //   999: goto -> 1082
    //   1002: aload_1
    //   1003: invokevirtual hasTileCenter : ()Z
    //   1006: istore #6
    //   1008: iload #6
    //   1010: ifeq -> 1077
    //   1013: aload_1
    //   1014: getfield x : I
    //   1017: istore #6
    //   1019: aload_1
    //   1020: getfield y : I
    //   1023: istore #15
    //   1025: new java/lang/StringBuilder
    //   1028: astore #8
    //   1030: ldc_w 'tile '
    //   1033: astore #9
    //   1035: aload #8
    //   1037: aload #9
    //   1039: invokespecial <init> : (Ljava/lang/String;)V
    //   1042: aload #8
    //   1044: iload #6
    //   1046: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1049: pop
    //   1050: aload #8
    //   1052: ldc_w ','
    //   1055: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1058: pop
    //   1059: aload #8
    //   1061: iload #15
    //   1063: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1066: pop
    //   1067: aload #8
    //   1069: invokevirtual toString : ()Ljava/lang/String;
    //   1072: astore #5
    //   1074: goto -> 1082
    //   1077: invokestatic followPlayer : ()Ljava/lang/String;
    //   1080: astore #5
    //   1082: invokestatic centerL : ()Ljava/lang/String;
    //   1085: astore #7
    //   1087: new java/lang/StringBuilder
    //   1090: astore #8
    //   1092: aload #8
    //   1094: invokespecial <init> : ()V
    //   1097: aload #8
    //   1099: aload #7
    //   1101: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1104: pop
    //   1105: ldc_w ' '
    //   1108: astore #7
    //   1110: aload #8
    //   1112: aload #7
    //   1114: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1117: pop
    //   1118: aload #8
    //   1120: aload #5
    //   1122: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1125: pop
    //   1126: aload #8
    //   1128: invokevirtual toString : ()Ljava/lang/String;
    //   1131: astore #5
    //   1133: aload_3
    //   1134: aload #5
    //   1136: invokevirtual add : (Ljava/lang/CharSequence;)Larc/scene/ui/layout/Cell;
    //   1139: invokevirtual wrap : ()Larc/scene/ui/layout/Cell;
    //   1142: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   1145: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   1148: fload #16
    //   1150: invokevirtual fontScale : (F)Larc/scene/ui/layout/Cell;
    //   1153: astore #5
    //   1155: aload_1
    //   1156: invokevirtual hasUnitCenter : ()Z
    //   1159: istore #15
    //   1161: iload #15
    //   1163: ifne -> 1185
    //   1166: aload_1
    //   1167: invokevirtual hasTileCenter : ()Z
    //   1170: istore #15
    //   1172: iload #15
    //   1174: ifne -> 1185
    //   1177: getstatic arc/graphics/Color.lightGray : Larc/graphics/Color;
    //   1180: astore #7
    //   1182: goto -> 1190
    //   1185: getstatic arc/graphics/Color.white : Larc/graphics/Color;
    //   1188: astore #7
    //   1190: aload #5
    //   1192: aload #7
    //   1194: invokevirtual color : (Larc/graphics/Color;)Larc/scene/ui/layout/Cell;
    //   1197: pop
    //   1198: aload_2
    //   1199: aload_3
    //   1200: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   1203: invokevirtual growX : ()Larc/scene/ui/layout/Cell;
    //   1206: invokevirtual row : ()V
    //   1209: new arc/scene/ui/layout/Table
    //   1212: astore_3
    //   1213: aload_3
    //   1214: invokespecial <init> : ()V
    //   1217: invokestatic unitMapBtn : ()Ljava/lang/String;
    //   1220: astore #5
    //   1222: new lunarians/ui/AutoMoonUi$15
    //   1225: astore #7
    //   1227: aload #7
    //   1229: aload_0
    //   1230: aload_1
    //   1231: invokespecial <init> : (Llunarians/ui/AutoMoonUi;Llunarians/data/MoonSlot;)V
    //   1234: aload_3
    //   1235: aload #5
    //   1237: aload #7
    //   1239: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   1242: ldc_w 110.0
    //   1245: invokevirtual width : (F)Larc/scene/ui/layout/Cell;
    //   1248: fload #12
    //   1250: invokevirtual height : (F)Larc/scene/ui/layout/Cell;
    //   1253: invokevirtual get : ()Larc/scene/Element;
    //   1256: checkcast arc/scene/ui/TextButton
    //   1259: invokevirtual getLabel : ()Larc/scene/ui/Label;
    //   1262: fload #13
    //   1264: invokevirtual setFontScale : (F)V
    //   1267: invokestatic mapBtn : ()Ljava/lang/String;
    //   1270: astore #5
    //   1272: new lunarians/ui/AutoMoonUi$16
    //   1275: astore #7
    //   1277: aload #7
    //   1279: aload_0
    //   1280: aload_1
    //   1281: invokespecial <init> : (Llunarians/ui/AutoMoonUi;Llunarians/data/MoonSlot;)V
    //   1284: aload_3
    //   1285: aload #5
    //   1287: aload #7
    //   1289: invokevirtual button : (Ljava/lang/String;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   1292: ldc_w 70.0
    //   1295: invokevirtual width : (F)Larc/scene/ui/layout/Cell;
    //   1298: fload #12
    //   1300: invokevirtual height : (F)Larc/scene/ui/layout/Cell;
    //   1303: fload #4
    //   1305: invokevirtual padLeft : (F)Larc/scene/ui/layout/Cell;
    //   1308: invokevirtual get : ()Larc/scene/Element;
    //   1311: checkcast arc/scene/ui/TextButton
    //   1314: invokevirtual getLabel : ()Larc/scene/ui/Label;
    //   1317: fload #13
    //   1319: invokevirtual setFontScale : (F)V
    //   1322: getstatic mindustry/gen/Icon.trash : Larc/scene/style/TextureRegionDrawable;
    //   1325: astore #5
    //   1327: getstatic mindustry/ui/Styles.defaulti : Larc/scene/ui/ImageButton$ImageButtonStyle;
    //   1330: astore #7
    //   1332: new lunarians/ui/AutoMoonUi$17
    //   1335: astore #8
    //   1337: aload #8
    //   1339: aload_0
    //   1340: aload_1
    //   1341: invokespecial <init> : (Llunarians/ui/AutoMoonUi;Llunarians/data/MoonSlot;)V
    //   1344: aload_3
    //   1345: aload #5
    //   1347: aload #7
    //   1349: aload #8
    //   1351: invokevirtual button : (Larc/scene/style/Drawable;Larc/scene/ui/ImageButton$ImageButtonStyle;Ljava/lang/Runnable;)Larc/scene/ui/layout/Cell;
    //   1354: fload #12
    //   1356: invokevirtual size : (F)Larc/scene/ui/layout/Cell;
    //   1359: fload #4
    //   1361: invokevirtual padLeft : (F)Larc/scene/ui/layout/Cell;
    //   1364: pop
    //   1365: aload_2
    //   1366: aload_3
    //   1367: invokevirtual add : (Larc/scene/Element;)Larc/scene/ui/layout/Cell;
    //   1370: invokevirtual left : ()Larc/scene/ui/layout/Cell;
    //   1373: invokevirtual row : ()V
    //   1376: aload_2
    //   1377: areturn
  }
  
  private static String centerL() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Centro:" : "Center:";
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
  
  private static String empty() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Nenhum slot. +" : "No slots. +";
  }
  
  private static String followPlayer() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "jogador" : "player";
  }
  
  private static String hint() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Voadoras orbitam jogador / tile / unidade no mapa. Velocidade ajustável. Atacam perto." : "Flyers orbit player / tile / map unit. Adjustable speed. Attack nearby.";
  }
  
  public static BuilderDialog make() {
    BuilderDialog builderDialog = new BuilderDialog();
    String str = title();
    AutoMoonUi$1 autoMoonUi$1 = new AutoMoonUi$1();
    this();
    AutoMoonUi$2 autoMoonUi$2 = new AutoMoonUi$2();
    this();
    this(str, autoMoonUi$1, autoMoonUi$2);
    return builderDialog;
  }
  
  private static String mapBtn() {
    String str = LunariansSettings.language;
    "pt".equals(str);
    return "Tile";
  }
  
  private static String maxL() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Máx:" : "Max:";
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
  
  private static String pick() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Unidade voadora" : "Flying unit";
  }
  
  private static String radL() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Raio:" : "Radius:";
  }
  
  private void rebuild() {
    this.list.clearChildren();
    Seq seq = LunariansSettings.moonSlots;
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
      String str1 = empty();
      Cell cell1 = table1.add(str1).wrap().growX();
      cell1.row();
    } else {
      bool = false;
      float f = 0.0F;
      cell = null;
      while (true) {
        i = seq.size;
        if (bool < i) {
          Table table2 = this.list;
          MoonSlot moonSlot = (MoonSlot)seq.get(bool);
          Table table1 = card(moonSlot);
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
      AutoMoonUi$4 autoMoonUi$4 = new AutoMoonUi$4();
      this(this);
      Cell cell1 = table1.button((Drawable)textureRegionDrawable, imageButtonStyle, autoMoonUi$4).size(40.0F);
      j = 1073741824;
      f2 = 2.0F;
      cell1 = cell1.padTop(f2);
      cell1.row();
    } 
  }
  
  private static void reopen() {
    BuilderDialog builderDialog = make();
    AutoMoonUi$3 autoMoonUi$3 = new AutoMoonUi$3();
    this();
    builderDialog.after(autoMoonUi$3).show();
  }
  
  private void rowStepper(Table paramTable, String paramString1, String paramString2, Runnable paramRunnable1, Runnable paramRunnable2) {
    Table table = new Table();
    this();
    table.add(paramString1).left().padRight(4.0F).fontScale(0.85F);
    Cell cell = table.button("-", paramRunnable1);
    float f1 = 32.0F;
    float f2 = 28.0F;
    cell.size(f1, f2);
    table.add(paramString2).width(48.0F).center().pad(2.0F);
    table.button("+", paramRunnable2).size(f1, f2);
    paramTable.add((Element)table).left().row();
  }
  
  private static String speedL() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Vel:" : "Speed:";
  }
  
  private static String title() {
    return "Auto Moon";
  }
  
  private static String unitMapBtn() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Unidade mapa" : "Map unit";
  }
}
