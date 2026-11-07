package lunarians.hub;

import arc.Events;
import arc.struct.IntSeq;
import arc.util.Log;
import arc.util.Time;
import arc.util.serialization.Jval;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import mindustry.Vars;
import mindustry.game.EventType;

public class Room {
  private static boolean busy;
  
  private static boolean inRoom;
  
  private static boolean inited;
  
  private static float next;
  
  private static void feedPresence(int paramInt) {
    try {
      boolean bool = presenceOn();
      if (bool) {
        String str1 = "lunarians.LunariansPresence";
        Class<?> clazz = Class.forName(str1);
        String str2 = "seen";
        Field field = clazz.getDeclaredField(str2);
        boolean bool1 = true;
        float f = Float.MIN_VALUE;
        field.setAccessible(bool1);
        bool1 = false;
        f = 0.0F;
        str2 = null;
        Object object = field.get(null);
        object = object;
        String str3 = String.valueOf(paramInt);
        f = Time.time;
        object.put(str3, f);
      } 
    } finally {}
  }
  
  public static void init() {
    boolean bool = inited;
    if (!bool) {
      bool = Vars.headless;
      if (!bool) {
        bool = true;
        inited = bool;
        AudioUi.init();
        EventType.Trigger trigger = EventType.Trigger.update;
        Room$2 room$2 = new Room$2();
        this();
        Events.run(trigger, room$2);
        Class<EventType.WorldLoadEvent> clazz = EventType.WorldLoadEvent.class;
        Room$3 room$3 = new Room$3();
        this();
        Events.on(clazz, room$3);
      } 
    } 
  }
  
  private static boolean presenceOn() {
    boolean bool = false;
    String str = "lunarians.LunariansPresence";
    try {
      Class<?> clazz = Class.forName(str);
      String str1 = "enabled";
      new Class[0];
      Method method = clazz.getMethod(str1, null);
      Object object = method.invoke(null, null);
      object = object;
      bool = object.booleanValue();
    } finally {}
    return bool;
  }
  
  private static void send(String paramString, int paramInt) {
    boolean bool = true;
    busy = bool;
    Jval jval2 = Jval.newObject();
    jval2.add("server_name", paramString);
    Jval jval3 = Jval.valueOf(paramInt);
    jval2.add("server_port", jval3);
    int i = Vars.player.id;
    Jval jval1 = Jval.valueOf(i);
    jval2.add("pid", jval1);
    jval1 = Jval.newArray();
    IntSeq intSeq = Skins.equippedApproved();
    int j = 0;
    while (true) {
      int k = intSeq.size;
      if (j < k) {
        Jval.JsonArray jsonArray = jval1.asArray();
        int m = intSeq.get(j);
        Jval jval = Jval.valueOf(m);
        jsonArray.add(jval);
        j++;
        continue;
      } 
      jval2.add("skins", jval1);
      jval1 = Jval.valueOf(presenceOn());
      jval2.add("presence", jval1);
      Room$0 room$0 = new Room$0();
      this();
      Room$1 room$1 = new Room$1();
      this();
      HubClient.call("POST", "/api/presence", jval2, room$0, room$1);
      return;
    } 
  }
  
  static void sendSoon() {
    float f1 = next;
    float f2 = Time.time + 60.0F;
    next = Math.min(f1, f2);
  }
  
  private static void tick() {
    // Byte code:
    //   0: getstatic arc/util/Time.time : F
    //   3: fstore_0
    //   4: getstatic lunarians/hub/Room.next : F
    //   7: fstore_1
    //   8: fload_0
    //   9: fload_1
    //   10: fcmpg
    //   11: istore_2
    //   12: iload_2
    //   13: iflt -> 232
    //   16: getstatic lunarians/hub/Room.busy : Z
    //   19: istore_2
    //   20: iload_2
    //   21: ifne -> 232
    //   24: getstatic arc/util/Time.time : F
    //   27: fstore_0
    //   28: ldc_w 1147207680
    //   31: istore_3
    //   32: ldc_w 900.0
    //   35: fstore_1
    //   36: fload_0
    //   37: fload_1
    //   38: fadd
    //   39: fstore_0
    //   40: fload_0
    //   41: putstatic lunarians/hub/Room.next : F
    //   44: invokestatic consented : ()Z
    //   47: istore_2
    //   48: iload_2
    //   49: ifeq -> 232
    //   52: invokestatic myId : ()Ljava/lang/String;
    //   55: astore #4
    //   57: aload #4
    //   59: invokevirtual isEmpty : ()Z
    //   62: istore_2
    //   63: iload_2
    //   64: ifne -> 232
    //   67: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   70: astore #4
    //   72: aload #4
    //   74: ifnull -> 232
    //   77: getstatic mindustry/Vars.net : Lmindustry/net/Net;
    //   80: astore #4
    //   82: iconst_1
    //   83: istore_3
    //   84: ldc 1.4E-45
    //   86: fstore_1
    //   87: aload #4
    //   89: ifnull -> 140
    //   92: getstatic mindustry/Vars.net : Lmindustry/net/Net;
    //   95: astore #4
    //   97: aload #4
    //   99: invokevirtual client : ()Z
    //   102: istore_2
    //   103: iload_2
    //   104: ifeq -> 140
    //   107: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   110: astore #4
    //   112: aload #4
    //   114: ifnull -> 140
    //   117: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   120: astore #4
    //   122: aload #4
    //   124: invokevirtual isGame : ()Z
    //   127: istore_2
    //   128: iload_2
    //   129: ifeq -> 140
    //   132: iconst_1
    //   133: istore_2
    //   134: ldc 1.4E-45
    //   136: fstore_0
    //   137: goto -> 147
    //   140: iconst_0
    //   141: istore_2
    //   142: fconst_0
    //   143: fstore_0
    //   144: aconst_null
    //   145: astore #4
    //   147: invokestatic currentServer : ()[Ljava/lang/String;
    //   150: astore #5
    //   152: aload #5
    //   154: iload_3
    //   155: aaload
    //   156: astore #6
    //   158: aload #6
    //   160: iconst_0
    //   161: invokestatic parseInt : (Ljava/lang/String;I)I
    //   164: istore #7
    //   166: iload_2
    //   167: ifeq -> 209
    //   170: aload #5
    //   172: iconst_0
    //   173: aaload
    //   174: astore #4
    //   176: aload #4
    //   178: invokevirtual isEmpty : ()Z
    //   181: istore_2
    //   182: iload_2
    //   183: ifne -> 209
    //   186: iload #7
    //   188: ifle -> 209
    //   191: iload_3
    //   192: putstatic lunarians/hub/Room.inRoom : Z
    //   195: aload #5
    //   197: iconst_0
    //   198: aaload
    //   199: astore #4
    //   201: aload #4
    //   203: iload #7
    //   205: invokestatic send : (Ljava/lang/String;I)V
    //   208: return
    //   209: getstatic lunarians/hub/Room.inRoom : Z
    //   212: istore_2
    //   213: iload_2
    //   214: ifeq -> 232
    //   217: iconst_0
    //   218: putstatic lunarians/hub/Room.inRoom : Z
    //   221: ldc_w ''
    //   224: astore #4
    //   226: aload #4
    //   228: iconst_0
    //   229: invokestatic send : (Ljava/lang/String;I)V
    //   232: return
    //   233: pop
    //   234: goto -> 232
    // Exception table:
    //   from	to	target	type
    //   44	47	233	finally
    //   52	55	233	finally
    //   57	62	233	finally
    //   67	70	233	finally
    //   77	80	233	finally
    //   92	95	233	finally
    //   97	102	233	finally
    //   107	110	233	finally
    //   117	120	233	finally
    //   122	127	233	finally
    //   147	150	233	finally
    //   154	156	233	finally
    //   160	164	233	finally
    //   172	174	233	finally
    //   176	181	233	finally
    //   191	195	233	finally
    //   197	199	233	finally
    //   203	208	233	finally
    //   209	212	233	finally
    //   217	221	233	finally
    //   228	232	233	finally
  }
}
