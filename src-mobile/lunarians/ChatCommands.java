package lunarians;

import arc.Core;
import arc.Events;
import arc.Input;
import arc.Settings;
import arc.graphics.Color;
import arc.scene.Element;
import arc.scene.Scene;
import arc.scene.style.Drawable;
import arc.scene.ui.TextButton;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.struct.IntFloatMap;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Time;
import lunarians.data.AttackSlot;
import lunarians.data.MoonSlot;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.core.UI;
import mindustry.entities.EntityGroup;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Entityc;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.ui.dialogs.SettingsMenuDialog;

public final class ChatCommands {
  public static final String KEY_ALLOW = "lunarians-chat-allow";
  
  public static final String KEY_ON = "lunarians-chat-on";
  
  public static final String KEY_PREFIX = "lunarians-chat-prefix";
  
  private static final int MAX_CHAT_SLOTS = 6;
  
  private static final int MAX_UNITS = 30;
  
  public static final int P_ATTACK = 1;
  
  public static final int P_MOON = 2;
  
  private static boolean errorLogged;
  
  private static float lastFollow;
  
  private static float lastHelp;
  
  private static final IntFloatMap lastHelpBy;
  
  private static BaseDialog panelDialog;
  
  private static Table pickOverlay;
  
  private static float pickUntil;
  
  private static boolean picking;
  
  private static final IntFloatMap prevFade;
  
  private static final ObjectMap prevText;
  
  private static final Seq registry;
  
  private static int tab;
  
  static {
    ObjectMap objectMap = new ObjectMap();
    this();
    prevText = objectMap;
    IntFloatMap intFloatMap2 = new IntFloatMap();
    this();
    prevFade = intFloatMap2;
    Seq seq = new Seq();
    this();
    registry = seq;
    lastHelp = -99999.0F;
    IntFloatMap intFloatMap1 = new IntFloatMap();
    this();
    lastHelpBy = intFloatMap1;
  }
  
  public static void addAllowed(Player paramPlayer) {
    ObjectMap objectMap = perms();
    Integer integer = Integer.valueOf(paramPlayer.id());
    boolean bool = objectMap.containsKey(integer);
    if (!bool) {
      String str1;
      int i = paramPlayer.id();
      integer = Integer.valueOf(i);
      String str2 = paramPlayer.plainName();
      if (str2 == null) {
        str1 = "?";
      } else {
        str1 = str1.plainName();
      } 
      byte b1 = 3;
      Integer integer1 = Integer.valueOf(b1);
      byte b2 = 2;
      Object[] arrayOfObject = new Object[b2];
      arrayOfObject[0] = str1;
      boolean bool1 = true;
      arrayOfObject[bool1] = integer1;
      objectMap.put(integer, arrayOfObject);
      savePerms(objectMap);
    } 
  }
  
  private static void addAttack(String paramString1, String paramString2, int paramInt) {
    // Byte code:
    //   0: ldc 'atk'
    //   2: astore_3
    //   3: iconst_0
    //   4: istore #4
    //   6: aload_3
    //   7: aload_1
    //   8: aload_0
    //   9: iconst_0
    //   10: invokestatic find : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)[Ljava/lang/Object;
    //   13: astore #5
    //   15: iconst_1
    //   16: istore #6
    //   18: aload #5
    //   20: ifnull -> 62
    //   23: aload #5
    //   25: iload #6
    //   27: aaload
    //   28: checkcast lunarians/data/AttackSlot
    //   31: astore_3
    //   32: aload_3
    //   33: getfield maxUnits : I
    //   36: iload_2
    //   37: iadd
    //   38: istore #4
    //   40: bipush #30
    //   42: istore #7
    //   44: iload #7
    //   46: iload #4
    //   48: invokestatic min : (II)I
    //   51: istore #4
    //   53: aload_3
    //   54: iload #4
    //   56: putfield maxUnits : I
    //   59: goto -> 182
    //   62: aload_0
    //   63: invokestatic chatSlots : (Ljava/lang/String;)I
    //   66: istore #7
    //   68: bipush #6
    //   70: istore #8
    //   72: iload #7
    //   74: iload #8
    //   76: if_icmpge -> 238
    //   79: getstatic lunarians/LunariansSettings.attackSlots : Larc/struct/Seq;
    //   82: astore #5
    //   84: aload #5
    //   86: getfield size : I
    //   89: istore #7
    //   91: bipush #10
    //   93: istore #8
    //   95: iload #7
    //   97: iload #8
    //   99: if_icmpge -> 238
    //   102: new lunarians/data/AttackSlot
    //   105: astore #5
    //   107: aload #5
    //   109: invokespecial <init> : ()V
    //   112: aload #5
    //   114: aload_1
    //   115: putfield unit : Ljava/lang/String;
    //   118: aload #5
    //   120: iload_2
    //   121: putfield maxUnits : I
    //   124: getstatic lunarians/LunariansSettings.attackSlots : Larc/struct/Seq;
    //   127: aload #5
    //   129: invokevirtual add : (Ljava/lang/Object;)Larc/struct/Seq;
    //   132: pop
    //   133: getstatic lunarians/ChatCommands.registry : Larc/struct/Seq;
    //   136: astore #9
    //   138: iconst_4
    //   139: istore #10
    //   141: iload #10
    //   143: anewarray java/lang/Object
    //   146: astore #11
    //   148: aload #11
    //   150: iconst_0
    //   151: aload_3
    //   152: aastore
    //   153: aload #11
    //   155: iload #6
    //   157: aload #5
    //   159: aastore
    //   160: aload #11
    //   162: iconst_2
    //   163: aload_0
    //   164: aastore
    //   165: iconst_3
    //   166: istore #12
    //   168: aload #11
    //   170: iload #12
    //   172: aload_1
    //   173: aastore
    //   174: aload #9
    //   176: aload #11
    //   178: invokevirtual add : (Ljava/lang/Object;)Larc/struct/Seq;
    //   181: pop
    //   182: iload #6
    //   184: putstatic lunarians/LunariansSettings.enableAutoAttack : Z
    //   187: invokestatic save : ()V
    //   190: new java/lang/StringBuilder
    //   193: astore_3
    //   194: aload_3
    //   195: invokespecial <init> : ()V
    //   198: aload_3
    //   199: aload_0
    //   200: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   203: pop
    //   204: aload_3
    //   205: ldc ': attack '
    //   207: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   210: pop
    //   211: aload_3
    //   212: aload_1
    //   213: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   216: pop
    //   217: aload_3
    //   218: ldc ' '
    //   220: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   223: pop
    //   224: aload_3
    //   225: iload_2
    //   226: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   229: pop
    //   230: aload_3
    //   231: invokevirtual toString : ()Ljava/lang/String;
    //   234: invokestatic toast : (Ljava/lang/String;)V
    //   237: return
    //   238: ldc 'Too many slots.'
    //   240: ldc 'Slots demais.'
    //   242: invokestatic tr : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   245: invokestatic toast : (Ljava/lang/String;)V
    //   248: return
  }
  
  private static void addMoon(Player paramPlayer, String paramString1, String paramString2, int paramInt1, int paramInt2, float paramFloat) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual unit : ()Lmindustry/gen/Unit;
    //   4: astore #6
    //   6: aload #6
    //   8: ifnull -> 390
    //   11: aload #6
    //   13: invokevirtual dead : ()Z
    //   16: istore #7
    //   18: iload #7
    //   20: ifne -> 390
    //   23: ldc 'moon'
    //   25: astore #8
    //   27: iconst_0
    //   28: istore #9
    //   30: aload #8
    //   32: aload_2
    //   33: aload_1
    //   34: iconst_0
    //   35: invokestatic find : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)[Ljava/lang/Object;
    //   38: astore #10
    //   40: iconst_1
    //   41: istore #11
    //   43: aload #10
    //   45: ifnull -> 117
    //   48: aload #10
    //   50: iload #11
    //   52: aaload
    //   53: checkcast lunarians/data/MoonSlot
    //   56: astore_0
    //   57: aload_0
    //   58: getfield maxUnits : I
    //   61: iload_3
    //   62: iadd
    //   63: istore #7
    //   65: bipush #30
    //   67: istore #9
    //   69: iload #9
    //   71: iload #7
    //   73: invokestatic min : (II)I
    //   76: istore #7
    //   78: aload_0
    //   79: iload #7
    //   81: putfield maxUnits : I
    //   84: iload #4
    //   86: i2f
    //   87: fstore #12
    //   89: aload_0
    //   90: fload #12
    //   92: putfield radius : F
    //   95: aload_0
    //   96: fload #5
    //   98: putfield speed : F
    //   101: aload #6
    //   103: getfield id : I
    //   106: istore #13
    //   108: aload_0
    //   109: iload #13
    //   111: putfield followId : I
    //   114: goto -> 292
    //   117: aload_1
    //   118: invokestatic chatSlots : (Ljava/lang/String;)I
    //   121: istore #14
    //   123: bipush #6
    //   125: istore #15
    //   127: ldc 8.4E-45
    //   129: fstore #16
    //   131: iload #14
    //   133: iload #15
    //   135: if_icmpge -> 376
    //   138: getstatic lunarians/LunariansSettings.moonSlots : Larc/struct/Seq;
    //   141: astore #10
    //   143: aload #10
    //   145: getfield size : I
    //   148: istore #14
    //   150: bipush #8
    //   152: istore #15
    //   154: ldc 1.1E-44
    //   156: fstore #16
    //   158: iload #14
    //   160: iload #15
    //   162: if_icmpge -> 376
    //   165: new lunarians/data/MoonSlot
    //   168: astore #10
    //   170: aload #10
    //   172: invokespecial <init> : ()V
    //   175: aload #10
    //   177: aload_2
    //   178: putfield unit : Ljava/lang/String;
    //   181: aload #10
    //   183: iload_3
    //   184: putfield maxUnits : I
    //   187: iload #4
    //   189: i2f
    //   190: fstore #16
    //   192: aload #10
    //   194: fload #16
    //   196: putfield radius : F
    //   199: aload #10
    //   201: fload #5
    //   203: putfield speed : F
    //   206: aload #6
    //   208: getfield id : I
    //   211: istore #13
    //   213: aload #10
    //   215: iload #13
    //   217: putfield followId : I
    //   220: aload_0
    //   221: invokevirtual plainName : ()Ljava/lang/String;
    //   224: astore_0
    //   225: aload #10
    //   227: aload_0
    //   228: putfield followLabel : Ljava/lang/String;
    //   231: getstatic lunarians/LunariansSettings.moonSlots : Larc/struct/Seq;
    //   234: aload #10
    //   236: invokevirtual add : (Ljava/lang/Object;)Larc/struct/Seq;
    //   239: pop
    //   240: getstatic lunarians/ChatCommands.registry : Larc/struct/Seq;
    //   243: astore_0
    //   244: iconst_4
    //   245: istore #13
    //   247: iload #13
    //   249: anewarray java/lang/Object
    //   252: astore #6
    //   254: aload #6
    //   256: iconst_0
    //   257: aload #8
    //   259: aastore
    //   260: aload #6
    //   262: iload #11
    //   264: aload #10
    //   266: aastore
    //   267: aload #6
    //   269: iconst_2
    //   270: aload_1
    //   271: aastore
    //   272: iconst_3
    //   273: istore #7
    //   275: ldc 4.2E-45
    //   277: fstore #12
    //   279: aload #6
    //   281: iload #7
    //   283: aload_2
    //   284: aastore
    //   285: aload_0
    //   286: aload #6
    //   288: invokevirtual add : (Ljava/lang/Object;)Larc/struct/Seq;
    //   291: pop
    //   292: iload #11
    //   294: putstatic lunarians/LunariansSettings.enableAutoMoon : Z
    //   297: invokestatic save : ()V
    //   300: new java/lang/StringBuilder
    //   303: astore_0
    //   304: aload_0
    //   305: invokespecial <init> : ()V
    //   308: aload_0
    //   309: aload_1
    //   310: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   313: pop
    //   314: aload_0
    //   315: ldc ': moon '
    //   317: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   320: pop
    //   321: aload_0
    //   322: aload_2
    //   323: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   326: pop
    //   327: ldc ' '
    //   329: astore_1
    //   330: aload_0
    //   331: aload_1
    //   332: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   335: pop
    //   336: aload_0
    //   337: iload_3
    //   338: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   341: pop
    //   342: aload_0
    //   343: aload_1
    //   344: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   347: pop
    //   348: aload_0
    //   349: iload #4
    //   351: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   354: pop
    //   355: aload_0
    //   356: aload_1
    //   357: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   360: pop
    //   361: aload_0
    //   362: fload #5
    //   364: invokevirtual append : (F)Ljava/lang/StringBuilder;
    //   367: pop
    //   368: aload_0
    //   369: invokevirtual toString : ()Ljava/lang/String;
    //   372: invokestatic toast : (Ljava/lang/String;)V
    //   375: return
    //   376: ldc 'Slots demais.'
    //   378: astore_1
    //   379: ldc 'Too many slots.'
    //   381: aload_1
    //   382: invokestatic tr : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   385: astore_0
    //   386: aload_0
    //   387: invokestatic toast : (Ljava/lang/String;)V
    //   390: return
  }
  
  private static boolean allowed(Player paramPlayer) {
    Player player = Vars.player;
    if (paramPlayer != player) {
      ObjectMap objectMap = perms();
      Integer integer = Integer.valueOf(paramPlayer.id());
      boolean bool = objectMap.containsKey(integer);
      if (!bool)
        return false; 
    } 
    return true;
  }
  
  private static int chatSlots(String paramString) {
    int i = 0;
    int j = 0;
    while (true) {
      Seq seq = registry;
      int k = seq.size;
      if (i < k) {
        Object[] arrayOfObject = (Object[])seq.get(i);
        k = 2;
        Object object = arrayOfObject[k];
        boolean bool = paramString.equals(object);
        if (bool)
          j++; 
        i++;
        continue;
      } 
      return j;
    } 
  }
  
  private static void endPick(boolean paramBoolean) {
    picking = false;
    Table table = pickOverlay;
    if (table != null) {
      table.remove();
      table = null;
      pickOverlay = null;
    } 
    if (paramBoolean)
      openPanel(); 
  }
  
  private static Object[] find(String paramString1, String paramString2, String paramString3, boolean paramBoolean) {
    int i = 0;
    while (true) {
      Seq seq = registry;
      int j = seq.size;
      if (i < j) {
        Object[] arrayOfObject = (Object[])seq.get(i);
        Object object = arrayOfObject[0];
        boolean bool = paramString1.equals(object);
        if (bool) {
          object = arrayOfObject[3];
          bool = paramString2.equals(object);
          if (bool)
            if (!paramBoolean) {
              object = arrayOfObject[2];
              bool = paramString3.equals(object);
              if (bool)
                return arrayOfObject; 
            } else {
              return arrayOfObject;
            }  
        } 
        i++;
        continue;
      } 
      return null;
    } 
  }
  
  private static void handle(Player paramPlayer, String paramString) {
    // Byte code:
    //   0: aload_1
    //   1: invokestatic stripColors : (Ljava/lang/CharSequence;)Ljava/lang/String;
    //   4: astore_2
    //   5: aload_2
    //   6: invokevirtual trim : ()Ljava/lang/String;
    //   9: astore_3
    //   10: invokestatic prefix : ()Ljava/lang/String;
    //   13: astore #4
    //   15: aload_3
    //   16: invokevirtual length : ()I
    //   19: istore #5
    //   21: aload #4
    //   23: invokevirtual length : ()I
    //   26: istore #6
    //   28: iload #5
    //   30: iload #6
    //   32: if_icmple -> 1301
    //   35: iconst_0
    //   36: istore #7
    //   38: aconst_null
    //   39: astore #8
    //   41: aload #4
    //   43: invokevirtual length : ()I
    //   46: istore #9
    //   48: iconst_1
    //   49: istore #6
    //   51: ldc_w 1.4E-45
    //   54: fstore #10
    //   56: iconst_0
    //   57: istore #11
    //   59: aload_3
    //   60: iload #6
    //   62: iconst_0
    //   63: aload #4
    //   65: iconst_0
    //   66: iload #9
    //   68: invokevirtual regionMatches : (ZILjava/lang/String;II)Z
    //   71: istore #5
    //   73: iload #5
    //   75: ifeq -> 1301
    //   78: aload #4
    //   80: invokevirtual length : ()I
    //   83: istore #5
    //   85: aload_3
    //   86: iload #5
    //   88: invokevirtual charAt : (I)C
    //   91: invokestatic isWhitespace : (C)Z
    //   94: istore #5
    //   96: bipush #44
    //   98: istore #6
    //   100: ldc_w 6.2E-44
    //   103: fstore #10
    //   105: iload #5
    //   107: ifne -> 158
    //   110: aload #4
    //   112: invokevirtual length : ()I
    //   115: istore #5
    //   117: aload_3
    //   118: iload #5
    //   120: invokevirtual charAt : (I)C
    //   123: istore #5
    //   125: bipush #58
    //   127: istore #11
    //   129: iload #5
    //   131: iload #11
    //   133: if_icmpeq -> 158
    //   136: aload #4
    //   138: invokevirtual length : ()I
    //   141: istore #5
    //   143: aload_3
    //   144: iload #5
    //   146: invokevirtual charAt : (I)C
    //   149: istore #5
    //   151: iload #5
    //   153: iload #6
    //   155: if_icmpne -> 1301
    //   158: aload #4
    //   160: invokevirtual length : ()I
    //   163: istore #5
    //   165: iconst_1
    //   166: istore #11
    //   168: iload #5
    //   170: iload #11
    //   172: iadd
    //   173: istore #5
    //   175: aload_3
    //   176: iload #5
    //   178: invokevirtual substring : (I)Ljava/lang/String;
    //   181: invokevirtual trim : ()Ljava/lang/String;
    //   184: astore_2
    //   185: ldc_w '\s+'
    //   188: astore_3
    //   189: aload_2
    //   190: aload_3
    //   191: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
    //   194: astore_2
    //   195: aload_2
    //   196: arraylength
    //   197: istore #12
    //   199: iload #12
    //   201: ifeq -> 1301
    //   204: iconst_0
    //   205: istore #12
    //   207: aconst_null
    //   208: astore_3
    //   209: aload_2
    //   210: iconst_0
    //   211: aaload
    //   212: astore #4
    //   214: aload #4
    //   216: invokevirtual isEmpty : ()Z
    //   219: istore #13
    //   221: iload #13
    //   223: ifne -> 1301
    //   226: aload_2
    //   227: iconst_0
    //   228: aaload
    //   229: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   232: astore #4
    //   234: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   237: astore #8
    //   239: aload_0
    //   240: aload #8
    //   242: if_acmpne -> 251
    //   245: iconst_1
    //   246: istore #14
    //   248: goto -> 254
    //   251: iconst_0
    //   252: istore #14
    //   254: ldc_w 'help'
    //   257: astore #8
    //   259: aload #4
    //   261: aload #8
    //   263: invokevirtual equals : (Ljava/lang/Object;)Z
    //   266: istore #9
    //   268: ldc 'moon'
    //   270: astore #15
    //   272: ldc_w 'attack'
    //   275: astore #16
    //   277: ldc_w 'stopmoon'
    //   280: astore #17
    //   282: ldc_w 'stopattack'
    //   285: astore #18
    //   287: iload #9
    //   289: ifne -> 1221
    //   292: ldc_w 'commands'
    //   295: astore #19
    //   297: aload #4
    //   299: aload #19
    //   301: invokevirtual equals : (Ljava/lang/Object;)Z
    //   304: istore #9
    //   306: iload #9
    //   308: ifeq -> 342
    //   311: aload_2
    //   312: arraylength
    //   313: istore #9
    //   315: iload #9
    //   317: iload #11
    //   319: if_icmple -> 342
    //   322: aload_2
    //   323: iload #11
    //   325: aaload
    //   326: astore #19
    //   328: aload #19
    //   330: aload #8
    //   332: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   335: istore #9
    //   337: iload #9
    //   339: ifne -> 1221
    //   342: aload_0
    //   343: invokestatic allowed : (Lmindustry/gen/Player;)Z
    //   346: istore #7
    //   348: iload #7
    //   350: ifeq -> 1301
    //   353: aload_0
    //   354: invokestatic plain : (Lmindustry/gen/Player;)Ljava/lang/String;
    //   357: astore #8
    //   359: ldc_w 'allow'
    //   362: astore #19
    //   364: aload #4
    //   366: aload #19
    //   368: invokevirtual equals : (Ljava/lang/Object;)Z
    //   371: istore #20
    //   373: iconst_2
    //   374: istore #21
    //   376: iload #20
    //   378: ifne -> 1005
    //   381: ldc_w 'deny'
    //   384: astore #22
    //   386: aload #4
    //   388: aload #22
    //   390: invokevirtual equals : (Ljava/lang/Object;)Z
    //   393: istore #20
    //   395: iload #20
    //   397: ifne -> 1005
    //   400: ldc_w 'who'
    //   403: astore #19
    //   405: aload #4
    //   407: aload #19
    //   409: invokevirtual equals : (Ljava/lang/Object;)Z
    //   412: istore #9
    //   414: iload #9
    //   416: ifeq -> 473
    //   419: iload #14
    //   421: ifeq -> 1301
    //   424: ldc_w 'Allowed: '
    //   427: ldc_w 'Permitidos: '
    //   430: invokestatic tr : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   433: astore #23
    //   435: invokestatic perms : ()Larc/struct/ObjectMap;
    //   438: getfield size : I
    //   441: istore #5
    //   443: new java/lang/StringBuilder
    //   446: astore_3
    //   447: aload_3
    //   448: invokespecial <init> : ()V
    //   451: aload_3
    //   452: aload #23
    //   454: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   457: pop
    //   458: aload_3
    //   459: iload #5
    //   461: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   464: pop
    //   465: aload_3
    //   466: invokevirtual toString : ()Ljava/lang/String;
    //   469: invokestatic toast : (Ljava/lang/String;)V
    //   472: return
    //   473: aload #4
    //   475: aload #16
    //   477: invokevirtual equals : (Ljava/lang/Object;)Z
    //   480: istore #9
    //   482: iload #9
    //   484: ifne -> 510
    //   487: aload #4
    //   489: aload #18
    //   491: invokevirtual equals : (Ljava/lang/Object;)Z
    //   494: istore #9
    //   496: iload #9
    //   498: ifne -> 510
    //   501: iconst_0
    //   502: istore #9
    //   504: aconst_null
    //   505: astore #19
    //   507: goto -> 513
    //   510: iconst_1
    //   511: istore #9
    //   513: aload #4
    //   515: aload #15
    //   517: invokevirtual equals : (Ljava/lang/Object;)Z
    //   520: istore #20
    //   522: iload #20
    //   524: ifne -> 541
    //   527: aload #4
    //   529: aload #17
    //   531: invokevirtual equals : (Ljava/lang/Object;)Z
    //   534: istore #20
    //   536: iload #20
    //   538: ifeq -> 544
    //   541: iconst_1
    //   542: istore #12
    //   544: iload #9
    //   546: ifne -> 560
    //   549: iload #12
    //   551: ifeq -> 1301
    //   554: iconst_2
    //   555: istore #12
    //   557: goto -> 563
    //   560: iconst_1
    //   561: istore #12
    //   563: aload_0
    //   564: iload #12
    //   566: invokestatic may : (Lmindustry/gen/Player;I)Z
    //   569: istore #12
    //   571: iload #12
    //   573: ifeq -> 1301
    //   576: aload_2
    //   577: arraylength
    //   578: istore #12
    //   580: iload #12
    //   582: iload #21
    //   584: if_icmplt -> 1301
    //   587: aload_2
    //   588: iload #11
    //   590: aaload
    //   591: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   594: astore_3
    //   595: getstatic mindustry/Vars.content : Lmindustry/core/ContentLoader;
    //   598: aload_3
    //   599: invokevirtual unit : (Ljava/lang/String;)Lmindustry/type/UnitType;
    //   602: astore #19
    //   604: aload #19
    //   606: ifnonnull -> 649
    //   609: ldc_w 'Unknown unit: '
    //   612: ldc_w 'Unidade desconhecida: '
    //   615: invokestatic tr : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   618: astore #23
    //   620: new java/lang/StringBuilder
    //   623: astore_2
    //   624: aload_2
    //   625: invokespecial <init> : ()V
    //   628: aload_2
    //   629: aload #23
    //   631: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   634: pop
    //   635: aload_2
    //   636: aload_3
    //   637: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   640: pop
    //   641: aload_2
    //   642: invokevirtual toString : ()Ljava/lang/String;
    //   645: invokestatic toast : (Ljava/lang/String;)V
    //   648: return
    //   649: aload_2
    //   650: arraylength
    //   651: istore #9
    //   653: bipush #30
    //   655: istore #20
    //   657: iload #9
    //   659: iload #21
    //   661: if_icmple -> 686
    //   664: aload_2
    //   665: iload #21
    //   667: aaload
    //   668: astore #19
    //   670: aload #19
    //   672: iload #11
    //   674: iload #11
    //   676: iload #20
    //   678: invokestatic num : (Ljava/lang/String;III)I
    //   681: istore #9
    //   683: goto -> 689
    //   686: iconst_m1
    //   687: istore #9
    //   689: aload #4
    //   691: aload #16
    //   693: invokevirtual equals : (Ljava/lang/Object;)Z
    //   696: istore #24
    //   698: iload #24
    //   700: ifeq -> 721
    //   703: iload #9
    //   705: iflt -> 712
    //   708: iload #9
    //   710: istore #11
    //   712: aload #8
    //   714: aload_3
    //   715: iload #11
    //   717: invokestatic addAttack : (Ljava/lang/String;Ljava/lang/String;I)V
    //   720: return
    //   721: aload #4
    //   723: aload #18
    //   725: invokevirtual equals : (Ljava/lang/Object;)Z
    //   728: istore #24
    //   730: iload #24
    //   732: ifeq -> 766
    //   735: iload #9
    //   737: istore #25
    //   739: getstatic lunarians/LunariansSettings.attackSlots : Larc/struct/Seq;
    //   742: astore #15
    //   744: aload_3
    //   745: astore #17
    //   747: aload #8
    //   749: astore #16
    //   751: ldc 'atk'
    //   753: aload #15
    //   755: aload #8
    //   757: aload_3
    //   758: iload #9
    //   760: iload #14
    //   762: invokestatic stop : (Ljava/lang/String;Larc/struct/Seq;Ljava/lang/String;Ljava/lang/String;IZ)V
    //   765: return
    //   766: aload #8
    //   768: astore #16
    //   770: iload #9
    //   772: istore #25
    //   774: aload #4
    //   776: aload #15
    //   778: invokevirtual equals : (Ljava/lang/Object;)Z
    //   781: istore #7
    //   783: iload #7
    //   785: ifeq -> 968
    //   788: aload_2
    //   789: arraylength
    //   790: istore #13
    //   792: bipush #8
    //   794: istore #7
    //   796: iconst_3
    //   797: istore #9
    //   799: iload #13
    //   801: iload #9
    //   803: if_icmple -> 825
    //   806: aload_2
    //   807: iload #9
    //   809: aaload
    //   810: astore #4
    //   812: aload #4
    //   814: iload #7
    //   816: iload #9
    //   818: iload #20
    //   820: invokestatic num : (Ljava/lang/String;III)I
    //   823: istore #7
    //   825: aload_2
    //   826: arraylength
    //   827: istore #13
    //   829: iconst_4
    //   830: istore #9
    //   832: iload #13
    //   834: iload #9
    //   836: if_icmple -> 914
    //   839: aload_2
    //   840: iload #9
    //   842: aaload
    //   843: astore_2
    //   844: bipush #46
    //   846: istore #13
    //   848: ldc_w 6.4E-44
    //   851: fstore #26
    //   853: aload_2
    //   854: iload #6
    //   856: iload #13
    //   858: invokevirtual replace : (CC)Ljava/lang/String;
    //   861: astore_2
    //   862: aload_2
    //   863: invokestatic parseFloat : (Ljava/lang/String;)F
    //   866: fstore #27
    //   868: ldc_w 1082130432
    //   871: istore #6
    //   873: ldc_w 4.0
    //   876: fstore #10
    //   878: fload #10
    //   880: fload #27
    //   882: invokestatic min : (FF)F
    //   885: fstore #27
    //   887: ldc_w 1045220557
    //   890: istore #6
    //   892: ldc_w 0.2
    //   895: fstore #10
    //   897: fload #10
    //   899: fload #27
    //   901: invokestatic max : (FF)F
    //   904: fstore #28
    //   906: fload #28
    //   908: fstore #26
    //   910: goto -> 924
    //   913: pop
    //   914: ldc_w 1065353216
    //   917: istore #13
    //   919: ldc_w 1.0
    //   922: fstore #26
    //   924: iload #25
    //   926: ifge -> 940
    //   929: iconst_1
    //   930: istore #6
    //   932: ldc_w 1.4E-45
    //   935: fstore #10
    //   937: goto -> 944
    //   940: iload #25
    //   942: istore #6
    //   944: aload_0
    //   945: astore #23
    //   947: iload #7
    //   949: istore #11
    //   951: aload #16
    //   953: astore_2
    //   954: aload_0
    //   955: aload #16
    //   957: aload_3
    //   958: iload #6
    //   960: iload #7
    //   962: fload #26
    //   964: invokestatic addMoon : (Lmindustry/gen/Player;Ljava/lang/String;Ljava/lang/String;IIF)V
    //   967: return
    //   968: aload #4
    //   970: aload #17
    //   972: invokevirtual equals : (Ljava/lang/Object;)Z
    //   975: istore #29
    //   977: iload #29
    //   979: ifeq -> 1301
    //   982: getstatic lunarians/LunariansSettings.moonSlots : Larc/struct/Seq;
    //   985: astore #15
    //   987: aload_3
    //   988: astore #17
    //   990: ldc 'moon'
    //   992: aload #15
    //   994: aload #8
    //   996: aload_3
    //   997: iload #9
    //   999: iload #14
    //   1001: invokestatic stop : (Ljava/lang/String;Larc/struct/Seq;Ljava/lang/String;Ljava/lang/String;IZ)V
    //   1004: return
    //   1005: iload #14
    //   1007: ifeq -> 1301
    //   1010: aload_2
    //   1011: arraylength
    //   1012: istore #29
    //   1014: iload #29
    //   1016: iload #21
    //   1018: if_icmplt -> 1301
    //   1021: new java/lang/StringBuilder
    //   1024: astore #23
    //   1026: aload #23
    //   1028: invokespecial <init> : ()V
    //   1031: iconst_1
    //   1032: istore #12
    //   1034: aload_2
    //   1035: arraylength
    //   1036: istore #6
    //   1038: iload #12
    //   1040: iload #6
    //   1042: if_icmpge -> 1092
    //   1045: iload #12
    //   1047: iload #11
    //   1049: if_icmple -> 1069
    //   1052: bipush #32
    //   1054: istore #6
    //   1056: ldc_w 4.5E-44
    //   1059: fstore #10
    //   1061: aload #23
    //   1063: iload #6
    //   1065: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1068: pop
    //   1069: aload_2
    //   1070: iload #12
    //   1072: aaload
    //   1073: astore #30
    //   1075: aload #23
    //   1077: aload #30
    //   1079: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1082: pop
    //   1083: iload #12
    //   1085: iconst_1
    //   1086: iadd
    //   1087: istore #12
    //   1089: goto -> 1034
    //   1092: aload #23
    //   1094: invokevirtual toString : ()Ljava/lang/String;
    //   1097: invokestatic playerByName : (Ljava/lang/String;)Lmindustry/gen/Player;
    //   1100: astore_2
    //   1101: aload_2
    //   1102: ifnonnull -> 1118
    //   1105: ldc_w 'Player not found (or 2 with that name). Use the panel to pick by unit.'
    //   1108: ldc_w 'Jogador nao encontrado (ou 2 com esse nome). Use o painel para escolher pela unidade.'
    //   1111: invokestatic tr : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1114: invokestatic toast : (Ljava/lang/String;)V
    //   1117: return
    //   1118: aload #4
    //   1120: aload #19
    //   1122: invokevirtual equals : (Ljava/lang/Object;)Z
    //   1125: istore #12
    //   1127: iload #12
    //   1129: ifeq -> 1139
    //   1132: aload_2
    //   1133: invokestatic addAllowed : (Lmindustry/gen/Player;)V
    //   1136: goto -> 1165
    //   1139: invokestatic perms : ()Larc/struct/ObjectMap;
    //   1142: astore_3
    //   1143: aload_2
    //   1144: invokevirtual id : ()I
    //   1147: istore #5
    //   1149: iload #5
    //   1151: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1154: astore_2
    //   1155: aload_3
    //   1156: aload_2
    //   1157: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   1160: pop
    //   1161: aload_3
    //   1162: invokestatic savePerms : (Larc/struct/ObjectMap;)V
    //   1165: aload #4
    //   1167: aload #19
    //   1169: invokevirtual equals : (Ljava/lang/Object;)Z
    //   1172: istore #5
    //   1174: iload #5
    //   1176: ifeq -> 1186
    //   1179: ldc_w '+ '
    //   1182: astore_2
    //   1183: goto -> 1190
    //   1186: ldc_w '- '
    //   1189: astore_2
    //   1190: aload #23
    //   1192: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   1195: astore #23
    //   1197: new java/lang/StringBuilder
    //   1200: astore_3
    //   1201: aload_3
    //   1202: aload_2
    //   1203: invokespecial <init> : (Ljava/lang/String;)V
    //   1206: aload_3
    //   1207: aload #23
    //   1209: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1212: pop
    //   1213: aload_3
    //   1214: invokevirtual toString : ()Ljava/lang/String;
    //   1217: invokestatic toast : (Ljava/lang/String;)V
    //   1220: return
    //   1221: aload #4
    //   1223: aload #8
    //   1225: invokevirtual equals : (Ljava/lang/Object;)Z
    //   1228: istore #12
    //   1230: iload #12
    //   1232: ifeq -> 1257
    //   1235: aload_2
    //   1236: arraylength
    //   1237: istore #12
    //   1239: iload #12
    //   1241: iload #11
    //   1243: if_icmple -> 1257
    //   1246: aload_2
    //   1247: iload #11
    //   1249: aaload
    //   1250: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   1253: astore_2
    //   1254: goto -> 1261
    //   1257: ldc_w ''
    //   1260: astore_2
    //   1261: aload_2
    //   1262: aload #18
    //   1264: invokevirtual equals : (Ljava/lang/Object;)Z
    //   1267: istore #12
    //   1269: iload #12
    //   1271: ifne -> 1277
    //   1274: aload_2
    //   1275: astore #16
    //   1277: aload #16
    //   1279: aload #17
    //   1281: invokevirtual equals : (Ljava/lang/Object;)Z
    //   1284: istore #5
    //   1286: iload #5
    //   1288: ifne -> 1295
    //   1291: aload #16
    //   1293: astore #15
    //   1295: aload_0
    //   1296: aload #15
    //   1298: invokestatic help : (Lmindustry/gen/Player;Ljava/lang/String;)V
    //   1301: return
    // Exception table:
    //   from	to	target	type
    //   840	843	913	finally
    //   856	861	913	finally
    //   862	866	913	finally
    //   880	885	913	finally
    //   899	904	913	finally
  }
  
  private static void help(Player paramPlayer, String paramString) {
    float f1 = Time.time;
    IntFloatMap intFloatMap = lastHelpBy;
    int i = paramPlayer.id();
    int j = -943501440;
    float f2 = -99999.0F;
    float f3 = intFloatMap.get(i, f2);
    f1 -= f3;
    i = 1142292480;
    f3 = 600.0F;
    int k = f1 cmp f3;
    if (k >= 0) {
      f1 = Time.time;
      f3 = lastHelp;
      f1 -= f3;
      i = 1123024896;
      f3 = 120.0F;
      k = f1 cmp f3;
      if (k >= 0) {
        String str3;
        k = paramPlayer.id();
        f3 = Time.time;
        intFloatMap.put(k, f3);
        f1 = Time.time;
        lastHelp = f1;
        String str4 = prefix();
        boolean bool2 = may(paramPlayer, 1);
        i = 2;
        f3 = 2.8E-45F;
        boolean bool3 = may(paramPlayer, i);
        if (!bool2 && !bool3) {
          String str = tr("You have no commands available. Ask the owner for permission.", "Voce nao tem comandos disponiveis. Peca permissao ao dono.");
          say(new String[] { str });
          return;
        } 
        String str5 = "attack";
        boolean bool1 = paramString.equals(str5);
        if (bool1 && bool2) {
          StringBuilder stringBuilder3 = new StringBuilder();
          this();
          stringBuilder3.append(str4);
          stringBuilder3.append(" attack <unit> [amount]   e.g. ");
          stringBuilder3.append(str4);
          stringBuilder3.append(" attack atrax 5");
          String str = stringBuilder3.toString();
          StringBuilder stringBuilder4 = new StringBuilder();
          this();
          stringBuilder4.append(str4);
          stringBuilder4.append(" stopattack <unit> [amount]   (no amount = remove all)");
          str3 = stringBuilder4.toString();
          say(new String[] { str, str3 });
          return;
        } 
        String str6 = "moon";
        boolean bool4 = str3.equals(str6);
        if (bool4 && bool3) {
          StringBuilder stringBuilder3 = new StringBuilder();
          this();
          stringBuilder3.append(str4);
          stringBuilder3.append(" moon <unit> [amount] [distance 3-30] [speed 0.2-4]   e.g. ");
          stringBuilder3.append(str4);
          stringBuilder3.append(" moon flare 5 4 1");
          String str10 = stringBuilder3.toString();
          StringBuilder stringBuilder4 = new StringBuilder();
          this();
          stringBuilder4.append(str4);
          stringBuilder4.append(" stopmoon <unit> [amount]   (units orbit you)");
          String str11 = stringBuilder4.toString();
          say(new String[] { str10, str11 });
          return;
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        this();
        if (bool2)
          stringBuilder1.append(str5); 
        if (bool3) {
          String str;
          if (bool2) {
            str = ", moon";
          } else {
            str = str6;
          } 
          stringBuilder1.append(str);
        } 
        String str1 = tr("Your commands: ", "Seus comandos: ");
        String str2 = String.valueOf(stringBuilder1);
        String str7 = tr("Say \"", "Diga \"");
        String str8 = " help <comando>\" para ver como usar, ex: \"";
        String str9 = tr(" help <command>\" to see how to use it, e.g. \"", str8);
        if (!bool2)
          str5 = str6; 
        StringBuilder stringBuilder2 = new StringBuilder();
        this();
        stringBuilder2.append(str1);
        stringBuilder2.append(str2);
        stringBuilder2.append(". ");
        stringBuilder2.append(str7);
        stringBuilder2.append(str4);
        stringBuilder2.append(str9);
        stringBuilder2.append(str4);
        stringBuilder2.append(" help ");
        stringBuilder2.append(str5);
        stringBuilder2.append("\"");
        str1 = stringBuilder2.toString();
        String[] arrayOfString = { str1 };
        say(arrayOfString);
      } 
    } 
  }
  
  public static void init() {
    boolean bool = Vars.headless;
    if (bool)
      return; 
    EventType.Trigger trigger = EventType.Trigger.update;
    ChatCommands$22 chatCommands$22 = new ChatCommands$22();
    this();
    Events.run(trigger, chatCommands$22);
    ChatCommands$23 chatCommands$23 = new ChatCommands$23();
    this();
    Events.on(EventType.WorldLoadEvent.class, chatCommands$23);
  }
  
  private static boolean may(Player paramPlayer, int paramInt) {
    Player player = Vars.player;
    boolean bool = true;
    if (paramPlayer == player)
      return bool; 
    ObjectMap objectMap = perms();
    int i = paramPlayer.id();
    Integer integer = Integer.valueOf(i);
    Object[] arrayOfObject = (Object[])objectMap.get(integer);
    if (arrayOfObject != null) {
      Integer integer1 = (Integer)arrayOfObject[bool];
      i = integer1.intValue() & paramInt;
      if (i != 0)
        return bool; 
    } 
    return false;
  }
  
  private static int num(String paramString, int paramInt1, int paramInt2, int paramInt3) {
    try {
      int i = Integer.parseInt(paramString);
      return Math.max(paramInt2, i);
    } finally {
      null;
    } 
  }
  
  public static boolean on() {
    return Core.settings.getBool("lunarians-chat-on", false);
  }
  
  public static void openPanel() {
    BaseDialog baseDialog = panelDialog;
    if (baseDialog != null)
      baseDialog.hide(); 
    baseDialog = new BaseDialog();
    String str = tr("Chat commands", "Comandos por chat");
    this(str);
    panelDialog = baseDialog;
    UiStyle.register(baseDialog);
    Table table = new Table();
    this();
    Runnable[] arrayOfRunnable = new Runnable[1];
    ChatCommands$20 chatCommands$20 = new ChatCommands$20();
    this(table, arrayOfRunnable);
    arrayOfRunnable[0] = chatCommands$20;
    arrayOfRunnable[0].run();
    baseDialog.cont.pane((Element)table).grow().minWidth(320.0F);
    baseDialog.addCloseButton();
    baseDialog.show();
  }
  
  private static ObjectMap perms() {
    ObjectMap objectMap = new ObjectMap();
    this();
    String str1 = Core.settings.getString("lunarians-chat-allow", "");
    String str2 = ",";
    String[] arrayOfString = str1.split(str2);
    int i = arrayOfString.length;
    int j = 0;
    while (true) {
      if (j < i) {
        String str3 = arrayOfString[j];
        String str4 = ":";
        String[] arrayOfString1 = str3.split(str4);
        int k = arrayOfString1.length;
        byte b = 2;
        if (k >= b)
          try {
            k = arrayOfString1.length;
            if (k > b) {
              str4 = arrayOfString1[b];
              str4 = str4.trim();
              k = Integer.parseInt(str4);
            } else {
              k = 3;
            } 
            String str6 = arrayOfString1[0];
            str6 = str6.trim();
            int m = Integer.parseInt(str6);
            Integer integer2 = Integer.valueOf(m);
            boolean bool = true;
            String str5 = arrayOfString1[bool];
            str5 = str5.trim();
            Integer integer1 = Integer.valueOf(k);
            Object[] arrayOfObject = new Object[b];
            arrayOfObject[0] = str5;
            arrayOfObject[bool] = integer1;
            objectMap.put(integer2, arrayOfObject);
          } finally {} 
        j++;
        continue;
      } 
      return objectMap;
    } 
  }
  
  private static void pickTick() {
    float f1 = Time.time;
    float f2 = pickUntil;
    boolean bool = true;
    int i = f1 cmp f2;
    if (i > 0) {
      endPick(bool);
      return;
    } 
    Input input = Core.input;
    boolean bool1 = input.justTouched();
    if (bool1) {
      Scene scene = Core.scene;
      bool1 = scene.hasMouse();
      if (!bool1) {
        Input input1 = Core.input;
        f1 = input1.mouseWorldX();
        Input input2 = Core.input;
        f2 = input2.mouseWorldY();
        int j = 0;
        Player player = null;
        float f = Float.MAX_VALUE;
        int k = 0;
        while (true) {
          EntityGroup entityGroup = Groups.player;
          int m = entityGroup.size();
          if (k < m) {
            Player player1 = (Player)Groups.player.index(k);
            if (player1 != null) {
              Unit unit = player1.unit();
              if (unit != null) {
                unit = player1.unit();
                boolean bool2 = unit.dead();
                if (!bool2) {
                  unit = player1.unit();
                  float f3 = unit.dst(f1, f2);
                  Unit unit1 = player1.unit();
                  float f4 = Math.max(unit1.hitSize, 16.0F);
                  float f5 = 8.0F;
                  f4 += f5;
                  int n = f3 cmp f4;
                  if (n < 0) {
                    n = f3 cmp f;
                    if (n < 0) {
                      player = player1;
                      f = f3;
                    } 
                  } 
                } 
              } 
            } 
            k++;
            continue;
          } 
          if (player != null) {
            Player player1 = Vars.player;
            if (player == player1) {
              String str2 = "Essa unidade e voce.";
              String str1 = tr("That is you.", str2);
              toast(str1);
            } else {
              addAllowed(player);
              String str1 = tr("Allowed: ", "Permitido: ");
              String str2 = player.plainName();
              j = player.id();
              StringBuilder stringBuilder = new StringBuilder();
              this();
              stringBuilder.append(str1);
              stringBuilder.append(str2);
              stringBuilder.append(" (id ");
              stringBuilder.append(j);
              stringBuilder.append(")");
              str1 = stringBuilder.toString();
              toast(str1);
            } 
            endPick(bool);
          } 
          break;
        } 
      } 
    } 
  }
  
  private static String plain(Player paramPlayer) {
    String str = paramPlayer.plainName();
    if (str != null)
      str = LunariansPresence.stripMark(str); 
    return (str == null) ? "" : str.trim().toLowerCase();
  }
  
  private static Player playerByName(String paramString) {
    int i = 0;
    Player player = null;
    while (true) {
      EntityGroup entityGroup = Groups.player;
      int j = entityGroup.size();
      if (i < j) {
        Player player1 = (Player)Groups.player.index(i);
        if (player1 != null) {
          Player player2 = Vars.player;
          if (player1 != player2) {
            String str1 = plain(player1);
            String str2 = paramString.trim().toLowerCase();
            boolean bool = str1.equals(str2);
            if (bool) {
              if (player != null)
                return null; 
              player = player1;
            } 
          } 
        } 
        i++;
        continue;
      } 
      return player;
    } 
  }
  
  private static void poll() {
    // Byte code:
    //   0: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   3: astore_0
    //   4: aload_0
    //   5: ifnull -> 410
    //   8: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   11: astore_0
    //   12: aload_0
    //   13: invokevirtual isGame : ()Z
    //   16: istore_1
    //   17: iload_1
    //   18: ifeq -> 410
    //   21: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   24: astore_0
    //   25: aload_0
    //   26: ifnull -> 410
    //   29: invokestatic on : ()Z
    //   32: istore_1
    //   33: iconst_0
    //   34: istore_2
    //   35: fconst_0
    //   36: fstore_3
    //   37: aconst_null
    //   38: astore #4
    //   40: getstatic mindustry/gen/Groups.player : Lmindustry/entities/EntityGroup;
    //   43: astore #5
    //   45: aload #5
    //   47: invokevirtual size : ()I
    //   50: istore #6
    //   52: iload_2
    //   53: iload #6
    //   55: if_icmpge -> 323
    //   58: getstatic mindustry/gen/Groups.player : Lmindustry/entities/EntityGroup;
    //   61: astore #5
    //   63: aload #5
    //   65: iload_2
    //   66: invokevirtual index : (I)Lmindustry/gen/Entityc;
    //   69: astore #5
    //   71: aload #5
    //   73: checkcast mindustry/gen/Player
    //   76: astore #5
    //   78: aload #5
    //   80: ifnull -> 316
    //   83: aload #5
    //   85: getfield lastText : Ljava/lang/String;
    //   88: astore #7
    //   90: aload #5
    //   92: getfield textFadeTime : F
    //   95: fstore #8
    //   97: getstatic lunarians/ChatCommands.prevText : Larc/struct/ObjectMap;
    //   100: astore #9
    //   102: aload #5
    //   104: invokevirtual id : ()I
    //   107: istore #10
    //   109: iload #10
    //   111: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   114: astore #11
    //   116: aload #9
    //   118: aload #11
    //   120: invokevirtual containsKey : (Ljava/lang/Object;)Z
    //   123: istore #10
    //   125: aload #5
    //   127: invokevirtual id : ()I
    //   130: istore #12
    //   132: iload #12
    //   134: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   137: astore #13
    //   139: aload #9
    //   141: aload #13
    //   143: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   146: astore #13
    //   148: aload #13
    //   150: checkcast java/lang/String
    //   153: astore #13
    //   155: getstatic lunarians/ChatCommands.prevFade : Larc/struct/IntFloatMap;
    //   158: astore #14
    //   160: aload #5
    //   162: invokevirtual id : ()I
    //   165: istore #15
    //   167: iconst_0
    //   168: istore #16
    //   170: aconst_null
    //   171: astore #17
    //   173: aload #14
    //   175: iload #15
    //   177: fconst_0
    //   178: invokevirtual get : (IF)F
    //   181: fstore #18
    //   183: aload #5
    //   185: invokevirtual id : ()I
    //   188: istore #16
    //   190: iload #16
    //   192: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   195: astore #17
    //   197: aload #7
    //   199: ifnonnull -> 210
    //   202: ldc_w ''
    //   205: astore #19
    //   207: goto -> 214
    //   210: aload #7
    //   212: astore #19
    //   214: aload #9
    //   216: aload #17
    //   218: aload #19
    //   220: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   223: pop
    //   224: aload #5
    //   226: invokevirtual id : ()I
    //   229: istore #20
    //   231: aload #14
    //   233: iload #20
    //   235: fload #8
    //   237: invokevirtual put : (IF)V
    //   240: iload_1
    //   241: ifeq -> 316
    //   244: iload #10
    //   246: ifeq -> 316
    //   249: aload #7
    //   251: ifnull -> 316
    //   254: aload #7
    //   256: invokevirtual isEmpty : ()Z
    //   259: istore #20
    //   261: iload #20
    //   263: ifne -> 316
    //   266: aload #7
    //   268: aload #13
    //   270: invokevirtual equals : (Ljava/lang/Object;)Z
    //   273: istore #20
    //   275: iload #20
    //   277: ifeq -> 309
    //   280: ldc_w 1045220557
    //   283: istore #20
    //   285: ldc_w 0.2
    //   288: fstore #21
    //   290: fload #18
    //   292: fload #21
    //   294: fadd
    //   295: fstore #18
    //   297: fload #8
    //   299: fload #18
    //   301: fcmpl
    //   302: istore #22
    //   304: iload #22
    //   306: ifle -> 316
    //   309: aload #5
    //   311: aload #7
    //   313: invokestatic handle : (Lmindustry/gen/Player;Ljava/lang/String;)V
    //   316: iload_2
    //   317: iconst_1
    //   318: iadd
    //   319: istore_2
    //   320: goto -> 40
    //   323: getstatic lunarians/ChatCommands.picking : Z
    //   326: istore_1
    //   327: iload_1
    //   328: ifeq -> 334
    //   331: invokestatic pickTick : ()V
    //   334: getstatic arc/util/Time.time : F
    //   337: fstore #23
    //   339: getstatic lunarians/ChatCommands.lastFollow : F
    //   342: fstore_3
    //   343: fload #23
    //   345: fload_3
    //   346: fsub
    //   347: fstore #23
    //   349: ldc_w 1106247680
    //   352: istore_2
    //   353: ldc_w 30.0
    //   356: fstore_3
    //   357: fload #23
    //   359: fload_3
    //   360: fcmpl
    //   361: istore_1
    //   362: iload_1
    //   363: ifle -> 410
    //   366: getstatic arc/util/Time.time : F
    //   369: fstore #23
    //   371: fload #23
    //   373: putstatic lunarians/ChatCommands.lastFollow : F
    //   376: invokestatic refreshFollowers : ()V
    //   379: return
    //   380: astore_0
    //   381: getstatic lunarians/ChatCommands.errorLogged : Z
    //   384: istore_2
    //   385: iload_2
    //   386: ifne -> 410
    //   389: iconst_1
    //   390: istore_2
    //   391: ldc_w 1.4E-45
    //   394: fstore_3
    //   395: iload_2
    //   396: putstatic lunarians/ChatCommands.errorLogged : Z
    //   399: ldc_w '[Lunarians] ChatCommands'
    //   402: astore #4
    //   404: aload #4
    //   406: aload_0
    //   407: invokestatic err : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   410: return
    // Exception table:
    //   from	to	target	type
    //   29	32	380	finally
    //   40	43	380	finally
    //   45	50	380	finally
    //   58	61	380	finally
    //   65	69	380	finally
    //   71	76	380	finally
    //   83	88	380	finally
    //   90	95	380	finally
    //   97	100	380	finally
    //   102	107	380	finally
    //   109	114	380	finally
    //   118	123	380	finally
    //   125	130	380	finally
    //   132	137	380	finally
    //   141	146	380	finally
    //   148	153	380	finally
    //   155	158	380	finally
    //   160	165	380	finally
    //   177	181	380	finally
    //   183	188	380	finally
    //   190	195	380	finally
    //   218	224	380	finally
    //   224	229	380	finally
    //   235	240	380	finally
    //   254	259	380	finally
    //   268	273	380	finally
    //   311	316	380	finally
    //   323	326	380	finally
    //   331	334	380	finally
    //   334	337	380	finally
    //   339	342	380	finally
    //   366	369	380	finally
    //   371	376	380	finally
    //   376	379	380	finally
  }
  
  public static String prefix() {
    Settings settings = Core.settings;
    String str2 = "lunarians-chat-prefix";
    String str3 = "Ron";
    String str1 = settings.getString(str2, str3);
    if (str1 != null) {
      str2 = str1.trim();
      boolean bool = str2.isEmpty();
      if (!bool)
        return str1.trim(); 
    } 
    return str3;
  }
  
  private static boolean pt() {
    String str = LunariansSettings.language;
    if (str != null) {
      str = LunariansSettings.language;
      String str1 = "pt";
      boolean bool = str.startsWith(str1);
      if (bool)
        return true; 
    } 
    return false;
  }
  
  private static void refreshFollowers() {
    Seq seq = registry;
    int i = seq.size;
    byte b = 1;
    for (i -= b; i >= 0; i--) {
      Object[] arrayOfObject = (Object[])registry.get(i);
      String str = "moon";
      int j = 0;
      Object object = arrayOfObject[0];
      boolean bool = str.equals(object);
      if (bool) {
        byte b1 = 2;
        str = (String)arrayOfObject[b1];
        while (true) {
          object = Groups.player;
          int k = object.size();
          if (j < k) {
            object = Groups.player.index(j);
            if (object != null) {
              String str1 = plain((Player)object);
              boolean bool1 = str1.equals(str);
              if (bool1) {
                Unit unit = object.unit();
                if (unit != null) {
                  unit = object.unit();
                  bool1 = unit.dead();
                  if (!bool1) {
                    MoonSlot moonSlot = (MoonSlot)arrayOfObject[b];
                    object = object.unit();
                    k = ((Unit)object).id;
                    moonSlot.followId = k;
                  } 
                } 
              } 
            } 
            j++;
            continue;
          } 
          break;
        } 
      } 
    } 
  }
  
  private static void savePerms(ObjectMap paramObjectMap) {
    StringBuilder stringBuilder = new StringBuilder();
    this();
    ObjectMap.Entries<ObjectMap.Entry> entries = paramObjectMap.iterator();
    while (true) {
      boolean bool = entries.hasNext();
      if (bool) {
        ObjectMap.Entry entry = entries.next();
        int i = stringBuilder.length();
        if (i > 0) {
          i = 44;
          stringBuilder.append(i);
        } 
        Object object2 = entry.key;
        stringBuilder.append(object2);
        i = 58;
        stringBuilder.append(i);
        String str1 = (String)((Object[])entry.value)[0];
        String str2 = " ";
        str1 = str1.replace(",", str2);
        String str3 = ":";
        str1 = str1.replace(str3, str2);
        stringBuilder.append(str1);
        stringBuilder.append(i);
        Object[] arrayOfObject = (Object[])entry.value;
        i = 1;
        Object object1 = arrayOfObject[i];
        stringBuilder.append(object1);
        continue;
      } 
      Settings settings = Core.settings;
      String str = stringBuilder.toString();
      settings.put("lunarians-chat-allow", str);
      return;
    } 
  }
  
  private static void say(String... paramVarArgs) {
    int i = 0;
    while (true) {
      int j = paramVarArgs.length;
      if (i < j) {
        String str = paramVarArgs[i];
        j = str.length();
        char c = '';
        float f1 = 1.96E-43F;
        if (j > c) {
          str = paramVarArgs[i].substring(0, c);
        } else {
          str = paramVarArgs[i];
        } 
        f1 = i;
        float f2 = 100.0F;
        f1 *= f2;
        ChatCommands$14 chatCommands$14 = new ChatCommands$14();
        this(str);
        Time.run(f1, chatCommands$14);
        i++;
        continue;
      } 
      break;
    } 
  }
  
  private static void startPick() {
    boolean bool = true;
    picking = bool;
    float f1 = Time.time;
    float f2 = 1800.0F;
    f1 += f2;
    pickUntil = f1;
    BaseDialog baseDialog = panelDialog;
    if (baseDialog != null)
      baseDialog.hide(); 
    try {
      UI uI = Vars.ui;
      if (uI != null) {
        uI = Vars.ui;
        SettingsMenuDialog settingsMenuDialog = uI.settings;
        if (settingsMenuDialog != null) {
          UI uI1 = Vars.ui;
          SettingsMenuDialog settingsMenuDialog1 = uI1.settings;
          settingsMenuDialog1.hide();
        } 
      } 
    } finally {}
    Table table2 = pickOverlay;
    if (table2 != null)
      table2.remove(); 
    table2 = new Table();
    this();
    pickOverlay = table2;
    table2.setFillParent(bool);
    pickOverlay.align(4);
    pickOverlay.marginBottom(70.0F);
    Table table3 = pickOverlay;
    Drawable drawable = Styles.black6;
    ChatCommands$7 chatCommands$7 = new ChatCommands$7();
    this();
    table3.table(drawable, chatCommands$7);
    Scene scene = Core.scene;
    Table table1 = pickOverlay;
    scene.add((Element)table1);
    toast(tr("Tap a player's unit on the map.", "Toque na unidade de um jogador no mapa."));
  }
  
  private static void stop(String paramString1, Seq paramSeq, String paramString2, String paramString3, int paramInt, boolean paramBoolean) {
    Object[] arrayOfObject = find(paramString1, paramString3, paramString2, paramBoolean);
    if (arrayOfObject == null)
      return; 
    paramBoolean = true;
    Object object = arrayOfObject[paramBoolean];
    boolean bool = object instanceof AttackSlot;
    if (bool) {
      Object object1 = object;
      object1 = object;
      i = ((AttackSlot)object1).maxUnits;
    } else {
      Object object1 = object;
      object1 = object;
      i = ((MoonSlot)object1).maxUnits;
    } 
    if (paramInt > 0 && i > paramInt) {
      if (bool) {
        object = object;
        i -= paramInt;
        ((AttackSlot)object).maxUnits = i;
      } else {
        object = object;
        i -= paramInt;
        ((MoonSlot)object).maxUnits = i;
      } 
    } else {
      paramSeq.remove(object);
      paramSeq = registry;
      paramSeq.remove(arrayOfObject);
    } 
    LunariansSettings.save();
    if (paramInt > 0) {
      StringBuilder stringBuilder1 = new StringBuilder();
      String str1 = " ";
      this(str1);
      stringBuilder1.append(paramInt);
      str = stringBuilder1.toString();
    } else {
      str = "";
    } 
    StringBuilder stringBuilder = new StringBuilder();
    this();
    String str;
    int i;
    stringBuilder.append(paramString2);
    stringBuilder.append(": stop ");
    stringBuilder.append(paramString3);
    stringBuilder.append(str);
    toast(stringBuilder.toString());
  }
  
  private static void toast(String paramString) {
    UI uI = Vars.ui;
    if (uI != null) {
      uI = Vars.ui;
      float f = 3.0F;
      uI.showInfoToast(paramString, f);
    } 
  }
  
  private static String tr(String paramString1, String paramString2) {
    boolean bool = pt();
    return bool ? paramString2 : paramString1;
  }
}
