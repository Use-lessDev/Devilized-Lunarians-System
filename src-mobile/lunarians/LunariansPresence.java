package lunarians;

import arc.Core;
import arc.Events;
import arc.Settings;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Font;
import arc.struct.ObjectFloatMap;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import java.util.Iterator;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.core.NetServer;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.net.Net;
import mindustry.ui.Fonts;

public final class LunariansPresence {
  private static final Color AURA = Color.valueOf("9b30ff");
  
  public static final String KEY = "lunarians-presence";
  
  public static final String MARK = "​⁠‌";
  
  public static final String PACKET = "lunarians-user";
  
  private static final float PING_EVERY = 720.0F;
  
  private static final float STALE = 1500.0F;
  
  private static boolean inited;
  
  private static float nextPing;
  
  private static final ObjectFloatMap seen;
  
  private static boolean sentThisMap;
  
  static {
    ObjectFloatMap objectFloatMap = new ObjectFloatMap();
    this();
    seen = objectFloatMap;
  }
  
  public static void applyName() {
    String str = "name";
    try {
      Settings settings = Core.settings;
      String str2 = "";
      String str1 = settings.getString(str, str2);
      return;
    } finally {
      str = null;
      Log.err("[Lunarians] presence applyName", (Throwable)str);
    } 
  }
  
  private static void draw() {
    boolean bool = enabled();
    if (bool) {
      GameState gameState = Vars.state;
      if (gameState != null) {
        gameState = Vars.state;
        bool = gameState.isGame();
        if (bool) {
          Player player = Vars.player;
          if (player != null) {
            Net net = Vars.net;
            bool = net.active();
            if (bool) {
              float f = Draw.z();
              Iterator<Player> iterator = Groups.player.iterator();
              while (true) {
                boolean bool1 = iterator.hasNext();
                if (bool1) {
                  Player player1 = iterator.next();
                  if (player1 != null) {
                    Player player2 = Vars.player;
                    if (player1 != player2) {
                      boolean bool2 = isMarked(player1);
                      if (bool2) {
                        Unit unit = player1.unit();
                        if (unit != null) {
                          bool2 = unit.isValid();
                          if (bool2) {
                            float f1 = unit.hitSize * 0.9F;
                            float f2 = 6.0F;
                            f1 += f2;
                            f1 = Math.max(12.0F, f1);
                            Draw.z(109.99F);
                            Color color = AURA;
                            Draw.color(color, 0.18F);
                            float f3 = unit.x;
                            float f4 = unit.y;
                            Fill.circle(f3, f4, f1);
                            Draw.color(color, 0.1F);
                            f3 = unit.x;
                            f4 = unit.y;
                            float f5 = 1.35F * f1;
                            Fill.circle(f3, f4, f5);
                            f3 = 120.0F;
                            Draw.z(f3);
                            try {
                              Font font = Fonts.outline;
                              String str = "Lunarians Team User";
                              float f6 = unit.x;
                              float f7 = unit.y + f1;
                              float f8 = f7 + f2;
                              boolean bool3 = true;
                              float f9 = 0.22F;
                              font.draw(str, f6, f8, color, f9, false, bool3);
                            } finally {}
                          } 
                        } 
                      } 
                    } 
                  } 
                  continue;
                } 
                Draw.color();
                Draw.z(f);
                break;
              } 
            } 
          } 
        } 
      } 
    } 
  }
  
  public static boolean enabled() {
    return Core.settings.getBool("lunarians-presence", false);
  }
  
  public static void init() {
    boolean bool = inited;
    if (bool)
      return; 
    bool = true;
    inited = bool;
    try {
    
    } finally {
      Exception exception = null;
      String str = "[Lunarians] presence packet handler";
    } 
    Class<EventType.PlayerJoin> clazz = EventType.PlayerJoin.class;
    try {
      LunariansPresence$3 lunariansPresence$3 = new LunariansPresence$3();
      this();
      Events.on(clazz, lunariansPresence$3);
      registerHostRelay();
    } finally {}
    applyName();
    EventType.Trigger trigger = EventType.Trigger.update;
    LunariansPresence$4 lunariansPresence$4 = new LunariansPresence$4();
    this();
    Events.run(trigger, lunariansPresence$4);
    trigger = EventType.Trigger.draw;
    LunariansPresence$5 lunariansPresence$5 = new LunariansPresence$5();
    this();
    Events.run(trigger, lunariansPresence$5);
    LunariansPresence$6 lunariansPresence$6 = new LunariansPresence$6();
    this();
    Events.on(EventType.WorldLoadEvent.class, lunariansPresence$6);
    Log.info("[Lunarians] Presence ready (default OFF).");
  }
  
  private static boolean isMarked(Player paramPlayer) {
    if (paramPlayer == null)
      return false; 
    String str1 = paramPlayer.name;
    boolean bool = true;
    if (str1 != null) {
      str1 = paramPlayer.name;
      String str = "​⁠‌";
      boolean bool2 = str1.contains(str);
      if (bool2)
        return bool; 
    } 
    ObjectFloatMap objectFloatMap = seen;
    String str2 = String.valueOf(paramPlayer.id);
    boolean bool1 = objectFloatMap.containsKey(str2);
    if (bool1)
      return bool; 
    try {
      str2 = paramPlayer.uuid();
      if (str2 != null) {
        String str = paramPlayer.uuid();
        boolean bool2 = objectFloatMap.containsKey(str);
        if (bool2)
          return bool; 
      } 
    } finally {}
    return false;
  }
  
  private static void onPacket(String paramString) {
    // Byte code:
    //   0: invokestatic enabled : ()Z
    //   3: istore_1
    //   4: iload_1
    //   5: ifeq -> 289
    //   8: aload_0
    //   9: ifnull -> 289
    //   12: aload_0
    //   13: invokevirtual isEmpty : ()Z
    //   16: istore_1
    //   17: iload_1
    //   18: ifne -> 289
    //   21: bipush #124
    //   23: istore_1
    //   24: ldc_w 1.74E-43
    //   27: fstore_2
    //   28: aload_0
    //   29: iload_1
    //   30: invokevirtual indexOf : (I)I
    //   33: istore_1
    //   34: iconst_0
    //   35: istore_3
    //   36: fconst_0
    //   37: fstore #4
    //   39: aconst_null
    //   40: astore #5
    //   42: iload_1
    //   43: ifle -> 57
    //   46: aload_0
    //   47: iconst_0
    //   48: iload_1
    //   49: invokevirtual substring : (II)Ljava/lang/String;
    //   52: astore #6
    //   54: goto -> 60
    //   57: aload_0
    //   58: astore #6
    //   60: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   63: astore #7
    //   65: aload #7
    //   67: ifnull -> 103
    //   70: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   73: astore #7
    //   75: aload #7
    //   77: getfield id : I
    //   80: istore #8
    //   82: iload #8
    //   84: invokestatic valueOf : (I)Ljava/lang/String;
    //   87: astore #7
    //   89: aload #6
    //   91: aload #7
    //   93: invokevirtual equals : (Ljava/lang/Object;)Z
    //   96: istore #8
    //   98: iload #8
    //   100: ifne -> 289
    //   103: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   106: astore #7
    //   108: aload #7
    //   110: ifnull -> 155
    //   113: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   116: astore #7
    //   118: aload #7
    //   120: invokevirtual uuid : ()Ljava/lang/String;
    //   123: astore #7
    //   125: aload #7
    //   127: ifnull -> 155
    //   130: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   133: astore #7
    //   135: aload #7
    //   137: invokevirtual uuid : ()Ljava/lang/String;
    //   140: astore #7
    //   142: aload_0
    //   143: aload #7
    //   145: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   148: istore #8
    //   150: iload #8
    //   152: ifne -> 289
    //   155: getstatic lunarians/LunariansPresence.seen : Larc/struct/ObjectFloatMap;
    //   158: astore #7
    //   160: getstatic arc/util/Time.time : F
    //   163: fstore #9
    //   165: aload #7
    //   167: aload #6
    //   169: fload #9
    //   171: invokevirtual put : (Ljava/lang/Object;F)V
    //   174: ldc_w '\|'
    //   177: astore #6
    //   179: aload_0
    //   180: aload #6
    //   182: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
    //   185: astore_0
    //   186: aload_0
    //   187: arraylength
    //   188: istore_1
    //   189: iconst_1
    //   190: istore #10
    //   192: ldc_w 1.4E-45
    //   195: fstore #9
    //   197: iload_1
    //   198: iload #10
    //   200: if_icmplt -> 237
    //   203: aload_0
    //   204: iconst_0
    //   205: aaload
    //   206: astore #6
    //   208: aload #6
    //   210: invokevirtual length : ()I
    //   213: istore_1
    //   214: iload_1
    //   215: ifle -> 237
    //   218: aload_0
    //   219: iconst_0
    //   220: aaload
    //   221: astore #6
    //   223: getstatic arc/util/Time.time : F
    //   226: fstore #4
    //   228: aload #7
    //   230: aload #6
    //   232: fload #4
    //   234: invokevirtual put : (Ljava/lang/Object;F)V
    //   237: aload_0
    //   238: arraylength
    //   239: istore_1
    //   240: iconst_3
    //   241: istore_3
    //   242: ldc_w 4.2E-45
    //   245: fstore #4
    //   247: iload_1
    //   248: iload_3
    //   249: if_icmplt -> 289
    //   252: iconst_2
    //   253: istore_1
    //   254: ldc_w 2.8E-45
    //   257: fstore_2
    //   258: aload_0
    //   259: iload_1
    //   260: aaload
    //   261: astore #5
    //   263: aload #5
    //   265: invokevirtual length : ()I
    //   268: istore_3
    //   269: iload_3
    //   270: iload_1
    //   271: if_icmple -> 289
    //   274: aload_0
    //   275: iload_1
    //   276: aaload
    //   277: astore_0
    //   278: getstatic arc/util/Time.time : F
    //   281: fstore_2
    //   282: aload #7
    //   284: aload_0
    //   285: fload_2
    //   286: invokevirtual put : (Ljava/lang/Object;F)V
    //   289: return
    //   290: pop
    //   291: goto -> 155
    //   294: pop
    //   295: goto -> 289
    // Exception table:
    //   from	to	target	type
    //   29	33	294	finally
    //   48	52	294	finally
    //   60	63	294	finally
    //   70	73	294	finally
    //   75	80	294	finally
    //   82	87	294	finally
    //   91	96	294	finally
    //   103	106	290	finally
    //   113	116	290	finally
    //   118	123	290	finally
    //   130	133	290	finally
    //   135	140	290	finally
    //   143	148	290	finally
    //   155	158	294	finally
    //   160	163	294	finally
    //   169	174	294	finally
    //   180	185	294	finally
    //   186	188	294	finally
    //   204	206	294	finally
    //   208	213	294	finally
    //   219	221	294	finally
    //   223	226	294	finally
    //   232	237	294	finally
    //   237	239	294	finally
    //   259	261	294	finally
    //   263	268	294	finally
    //   275	277	294	finally
    //   278	281	294	finally
    //   285	289	294	finally
  }
  
  private static void registerHostRelay() {
    boolean bool = enabled();
    if (bool)
      try {
        NetServer netServer = Vars.netServer;
        if (netServer != null) {
          netServer = Vars.netServer;
          String str = "lunarians-user";
          LunariansPresence$7 lunariansPresence$7 = new LunariansPresence$7();
          this();
          netServer.addPacketHandler(str, lunariansPresence$7);
        } 
      } finally {} 
  }
  
  private static String safe(String paramString) {
    String str = "";
    return (paramString == null) ? str : paramString.replace("|", str).replace("\n", str);
  }
  
  public static void setEnabled(boolean paramBoolean) {
    Settings settings = Core.settings;
    String str = "lunarians-presence";
    Boolean bool = Boolean.valueOf(paramBoolean);
    settings.put(str, bool);
    applyName();
    settings = null;
    if (paramBoolean) {
      registerHostRelay();
      nextPing = 0.0F;
      return;
    } 
    seen.clear();
    sentThisMap = false;
    nextPing = 0.0F;
  }
  
  public static String stripMark(String paramString) {
    return (paramString == null) ? null : paramString.replace("​⁠‌", "");
  }
  
  private static void update() {
    String str = "lunarians-user";
    boolean bool = enabled();
    if (bool) {
      GameState gameState = Vars.state;
      if (gameState != null) {
        gameState = Vars.state;
        bool = gameState.isGame();
        if (bool) {
          Player player = Vars.player;
          if (player != null) {
            Net net = Vars.net;
            bool = net.active();
            if (bool) {
              float f1 = Time.time;
              float f2 = nextPing;
              int i = f1 cmp f2;
              if (i >= 0) {
                f1 = Time.time;
                f2 = 720.0F;
                f1 += f2;
                nextPing = f1;
                i = Vars.player.id;
                String str2 = safe(Vars.player.name);
                String str3 = safe(Vars.player.uuid());
                StringBuilder stringBuilder = new StringBuilder();
                this();
                stringBuilder.append(i);
                String str1 = "|";
                stringBuilder.append(str1);
                stringBuilder.append(str2);
                stringBuilder.append(str1);
                stringBuilder.append(str3);
                str1 = stringBuilder.toString();
                try {
                  Call.serverPacketReliable(str, str1);
                } finally {
                  null;
                } 
                boolean bool1 = true;
                sentThisMap = bool1;
                Seq seq1 = new Seq();
                this();
                Seq seq2 = seen.keys().toSeq();
                LunariansPresence$0 lunariansPresence$0 = new LunariansPresence$0();
                this(seq1);
                seq2.each(lunariansPresence$0);
                LunariansPresence$1 lunariansPresence$1 = new LunariansPresence$1();
                this();
                seq1.each(lunariansPresence$1);
              } 
            } 
          } 
        } 
      } 
    } 
  }
}
