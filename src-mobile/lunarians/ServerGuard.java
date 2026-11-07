package lunarians;

import arc.Events;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.core.UI;
import mindustry.game.EventType;

public class ServerGuard {
  private static boolean forced;
  
  private static float lastCheck;
  
  private static boolean registered;
  
  private static boolean TimeOk() {
    float f1 = Time.time;
    float f2 = lastCheck;
    f2 = f1 - f2;
    float f3 = 180.0F;
    int i = f2 cmp f3;
    if (i < 0)
      return false; 
    lastCheck = f1;
    return true;
  }
  
  public static void check() {
    try {
      return;
    } finally {
      Exception exception = null;
      Log.err("[Lunarians] ServerGuard", exception);
    } 
  }
  
  private static void checkLight() {
    try {
      boolean bool = isRestrictedServer();
      if (bool) {
        bool = LunariansSettings.pvpMode;
        if (!bool) {
          bool = true;
          LunariansSettings.pvpMode = bool;
          LunariansSettings.save();
          forced = bool;
          UI uI = Vars.ui;
          if (uI != null) {
            uI = Vars.ui;
            String str1 = "pt";
            String str2 = LunariansSettings.language;
            boolean bool1 = str1.equals(str2);
            if (bool1) {
              str1 = "Servidor restrito → Balanced ON";
            } else {
              str1 = "Restricted server → Balanced ON";
            } 
            float f = 4.0F;
            uI.showInfoToast(str1, f);
          } 
        } 
      } 
    } finally {}
  }
  
  public static void init() {
    boolean bool = registered;
    if (bool)
      return; 
    registered = true;
    ServerGuard$0 serverGuard$0 = new ServerGuard$0();
    this();
    Events.on(EventType.ClientLoadEvent.class, serverGuard$0);
    ServerGuard$1 serverGuard$1 = new ServerGuard$1();
    this();
    Events.on(EventType.WorldLoadEvent.class, serverGuard$1);
    EventType.Trigger trigger = EventType.Trigger.update;
    ServerGuard$2 serverGuard$2 = new ServerGuard$2();
    this();
    Events.run(trigger, serverGuard$2);
  }
  
  public static boolean isRestrictedServer() {
    // Byte code:
    //   0: ldc ''
    //   2: astore_0
    //   3: ldc 'fish'
    //   5: astore_1
    //   6: getstatic mindustry/Vars.net : Lmindustry/net/Net;
    //   9: astore_2
    //   10: iconst_0
    //   11: istore_3
    //   12: aload_2
    //   13: ifnull -> 436
    //   16: getstatic mindustry/Vars.net : Lmindustry/net/Net;
    //   19: astore_2
    //   20: aload_2
    //   21: invokevirtual client : ()Z
    //   24: istore #4
    //   26: iload #4
    //   28: ifeq -> 436
    //   31: getstatic mindustry/Vars.ui : Lmindustry/core/UI;
    //   34: astore_2
    //   35: aload_2
    //   36: ifnull -> 48
    //   39: getstatic mindustry/Vars.ui : Lmindustry/core/UI;
    //   42: astore_2
    //   43: aload_2
    //   44: getfield join : Lmindustry/ui/dialogs/JoinDialog;
    //   47: astore_2
    //   48: getstatic mindustry/Vars.platform : Lmindustry/core/Platform;
    //   51: astore_2
    //   52: getstatic arc/Core.settings : Larc/Settings;
    //   55: astore_2
    //   56: ldc 'last-server-ip'
    //   58: astore #5
    //   60: aload_2
    //   61: aload #5
    //   63: aload_0
    //   64: invokevirtual getString : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   67: astore_2
    //   68: getstatic arc/Core.settings : Larc/Settings;
    //   71: astore #5
    //   73: ldc 'last-server-name'
    //   75: astore #6
    //   77: aload #5
    //   79: aload #6
    //   81: aload_0
    //   82: invokevirtual getString : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   85: astore #5
    //   87: new java/lang/StringBuilder
    //   90: astore #6
    //   92: aload #6
    //   94: invokespecial <init> : ()V
    //   97: aload #6
    //   99: aload_2
    //   100: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   103: pop
    //   104: ldc ' '
    //   106: astore_2
    //   107: aload #6
    //   109: aload_2
    //   110: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   113: pop
    //   114: aload #6
    //   116: aload #5
    //   118: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   121: pop
    //   122: aload #6
    //   124: invokevirtual toString : ()Ljava/lang/String;
    //   127: astore_2
    //   128: aload_2
    //   129: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   132: astore_2
    //   133: aload_2
    //   134: aload_1
    //   135: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   138: istore #7
    //   140: iconst_1
    //   141: istore #8
    //   143: iload #7
    //   145: ifne -> 432
    //   148: ldc 'xpdustry'
    //   150: astore #5
    //   152: aload_2
    //   153: aload #5
    //   155: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   158: istore #7
    //   160: iload #7
    //   162: ifne -> 432
    //   165: ldc 'darkdustry'
    //   167: astore #5
    //   169: aload_2
    //   170: aload #5
    //   172: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   175: istore #7
    //   177: iload #7
    //   179: ifne -> 432
    //   182: ldc 'mindustry.ddns'
    //   184: astore #5
    //   186: aload_2
    //   187: aload #5
    //   189: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   192: istore #7
    //   194: iload #7
    //   196: ifne -> 432
    //   199: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   202: astore #5
    //   204: aload #5
    //   206: ifnull -> 221
    //   209: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   212: astore #5
    //   214: aload #5
    //   216: getfield name : Ljava/lang/String;
    //   219: astore #5
    //   221: getstatic mindustry/Vars.net : Lmindustry/net/Net;
    //   224: astore #5
    //   226: aload #5
    //   228: ifnull -> 267
    //   231: getstatic mindustry/Vars.net : Lmindustry/net/Net;
    //   234: astore #5
    //   236: aload #5
    //   238: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   241: astore #5
    //   243: aload #5
    //   245: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   248: astore #5
    //   250: aload #5
    //   252: aload_1
    //   253: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   256: istore #7
    //   258: iload #7
    //   260: ifeq -> 267
    //   263: iload #8
    //   265: ireturn
    //   266: pop
    //   267: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   270: astore #5
    //   272: aload #5
    //   274: ifnull -> 426
    //   277: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   280: astore #5
    //   282: aload #5
    //   284: getfield rules : Lmindustry/game/Rules;
    //   287: astore #5
    //   289: aload #5
    //   291: ifnull -> 426
    //   294: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   297: astore #5
    //   299: aload #5
    //   301: getfield rules : Lmindustry/game/Rules;
    //   304: astore #5
    //   306: aload #5
    //   308: getfield modeName : Ljava/lang/String;
    //   311: astore #5
    //   313: aload #5
    //   315: ifnull -> 332
    //   318: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   321: astore_0
    //   322: aload_0
    //   323: getfield rules : Lmindustry/game/Rules;
    //   326: astore_0
    //   327: aload_0
    //   328: getfield modeName : Ljava/lang/String;
    //   331: astore_0
    //   332: aload_0
    //   333: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   336: astore #5
    //   338: aload #5
    //   340: aload_1
    //   341: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   344: istore #7
    //   346: iload #7
    //   348: ifne -> 375
    //   351: aload_0
    //   352: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   355: astore #5
    //   357: ldc 'pvp'
    //   359: astore #9
    //   361: aload #5
    //   363: aload #9
    //   365: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   368: istore #7
    //   370: iload #7
    //   372: ifeq -> 426
    //   375: getstatic mindustry/Vars.net : Lmindustry/net/Net;
    //   378: astore #5
    //   380: aload #5
    //   382: invokevirtual client : ()Z
    //   385: istore #7
    //   387: iload #7
    //   389: ifeq -> 426
    //   392: aload_2
    //   393: aload_1
    //   394: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   397: istore #7
    //   399: iload #7
    //   401: ifne -> 421
    //   404: aload_0
    //   405: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   408: astore_0
    //   409: aload_0
    //   410: aload_1
    //   411: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   414: istore #10
    //   416: iload #10
    //   418: ifeq -> 423
    //   421: iconst_1
    //   422: istore_3
    //   423: iload_3
    //   424: ireturn
    //   425: pop
    //   426: aload_2
    //   427: aload_1
    //   428: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   431: ireturn
    //   432: iload #8
    //   434: ireturn
    //   435: pop
    //   436: iconst_0
    //   437: ireturn
    //   438: pop
    //   439: goto -> 48
    //   442: pop
    //   443: goto -> 52
    //   446: pop
    //   447: goto -> 221
    // Exception table:
    //   from	to	target	type
    //   31	34	438	finally
    //   39	42	438	finally
    //   43	47	438	finally
    //   48	51	442	finally
    //   52	55	435	finally
    //   63	67	435	finally
    //   68	71	435	finally
    //   81	85	435	finally
    //   87	90	435	finally
    //   92	97	435	finally
    //   99	104	435	finally
    //   109	114	435	finally
    //   116	122	435	finally
    //   122	127	435	finally
    //   128	132	435	finally
    //   134	138	435	finally
    //   153	158	435	finally
    //   170	175	435	finally
    //   187	192	435	finally
    //   199	202	446	finally
    //   209	212	446	finally
    //   214	219	446	finally
    //   221	224	266	finally
    //   231	234	266	finally
    //   236	241	266	finally
    //   243	248	266	finally
    //   252	256	266	finally
    //   267	270	425	finally
    //   277	280	425	finally
    //   282	287	425	finally
    //   294	297	425	finally
    //   299	304	425	finally
    //   306	311	425	finally
    //   318	321	425	finally
    //   322	326	425	finally
    //   327	331	425	finally
    //   332	336	425	finally
    //   340	344	425	finally
    //   351	355	425	finally
    //   363	368	425	finally
    //   375	378	425	finally
    //   380	385	425	finally
    //   393	397	425	finally
    //   404	408	425	finally
    //   410	414	425	finally
    //   427	431	435	finally
  }
}
