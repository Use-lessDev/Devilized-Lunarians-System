package lunarians.hub;

import arc.Events;
import arc.struct.IntSeq;
import arc.struct.ObjectFloatMap;
import arc.util.Log;
import arc.util.Strings;
import arc.util.Time;
import arc.util.serialization.Jval;
import java.lang.reflect.Field;
import mindustry.Vars;
import mindustry.game.EventType;

public class Room {
  private static boolean inited;
  
  private static boolean inRoom;
  
  private static boolean busy;
  
  private static float next;
  
  public static void init() {
    if (inited || Vars.headless)
      return; 
    inited = true;
    AudioUi.init();
    Events.run(EventType.Trigger.update, Room::tick);
    Events.on(EventType.WorldLoadEvent.class, paramWorldLoadEvent -> next = Time.time + 120.0F);
  }
  
  static void sendSoon() {
    next = Math.min(next, Time.time + 60.0F);
  }
  
  private static void tick() {
    if (Time.time < next || busy)
      return; 
    next = Time.time + 900.0F;
    try {
      if (!HubClient.consented() || HubClient.myId().isEmpty() || Vars.player == null)
        return; 
      boolean bool = (Vars.net != null && Vars.net.client() && Vars.state != null && Vars.state.isGame()) ? true : false;
      String[] arrayOfString = HubClient.currentServer();
      int i = Strings.parseInt(arrayOfString[1], 0);
      if (!bool || arrayOfString[0].isEmpty() || i <= 0) {
        if (inRoom) {
          inRoom = false;
          send("", 0);
        } 
        return;
      } 
      inRoom = true;
      send(arrayOfString[0], i);
    } catch (Throwable throwable) {}
  }
  
  private static void send(String paramString, int paramInt) {
    busy = true;
    Jval jval1 = Jval.newObject();
    jval1.add("server_name", paramString);
    jval1.add("server_port", Jval.valueOf(paramInt));
    jval1.add("pid", Jval.valueOf(Vars.player.id));
    Jval jval2 = Jval.newArray();
    IntSeq intSeq = Skins.equippedApproved();
    for (byte b = 0; b < intSeq.size; ) {
      jval2.asArray().add(Jval.valueOf(intSeq.get(b)));
      b++;
    } 
    jval1.add("skins", jval2);
    jval1.add("presence", Jval.valueOf(presenceOn()));
    HubClient.call("POST", "/api/presence", jval1, paramJval -> {
          busy = false;
          try {
            for (Jval jval1 : paramJval.get("peers").asArray()) {
              int i = jval1.getInt("pid", -1);
              if (i < 0 || i == Vars.player.id)
                continue; 
              if (jval1.getInt("presence", 0) == 1)
                feedPresence(i); 
              Jval jval2 = jval1.get("skins");
              if (jval2 != null && jval2.isArray())
                Skins.feedPeer(i, jval2.asArray()); 
            } 
          } catch (Throwable throwable) {
            Log.debug("[Lunarians Room] resposta invalida: @", new Object[] { throwable.toString() });
          } 
        }paramString -> busy = false);
  }
  
  private static boolean presenceOn() {
    try {
      return ((Boolean)Class.forName("lunarians.LunariansPresence").getMethod("enabled", new Class[0]).invoke(null, new Object[0])).booleanValue();
    } catch (Throwable throwable) {
      return false;
    } 
  }
  
  private static void feedPresence(int paramInt) {
    try {
      if (presenceOn()) {
        Field field = Class.forName("lunarians.LunariansPresence").getDeclaredField("seen");
        field.setAccessible(true);
        ((ObjectFloatMap)field.get(null)).put(String.valueOf(paramInt), Time.time);
      } 
    } catch (Throwable throwable) {}
  }
}
