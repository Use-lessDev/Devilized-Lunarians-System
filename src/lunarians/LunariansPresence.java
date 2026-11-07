package lunarians;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.struct.ObjectFloatMap;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.ui.Fonts;

public final class LunariansPresence {
  public static final String PACKET = "lunarians-user";
  
  public static final String KEY = "lunarians-presence";
  
  private static final float PING_EVERY = 720.0F;
  
  private static final float STALE = 1500.0F;
  
  private static final Color AURA = Color.valueOf("9b30ff");
  
  private static final ObjectFloatMap<String> seen = new ObjectFloatMap();
  
  private static float nextPing;
  
  private static boolean inited;
  
  private static boolean sentThisMap;
  
  public static final String MARK = "​⁠‌";
  
  public static boolean enabled() {
    return Core.settings.getBool("lunarians-presence", false);
  }
  
  public static String stripMark(String paramString) {
    return (paramString == null) ? null : paramString.replace("​⁠‌", "");
  }
  
  public static void applyName() {
    try {
      String str1 = stripMark(Core.settings.getString("name", ""));
      if (str1 == null || str1.isEmpty())
        return; 
      String str2 = str1;
      if (enabled()) {
        int i = Math.max(1, 40 - "​⁠‌".length());
        str2 = ((str1.length() > i) ? str1.substring(0, i) : str1) + "​⁠‌";
      } 
      Core.settings.put("name", str2);
      if (Vars.player != null)
        Vars.player.name = str2; 
    } catch (Throwable throwable) {
      Log.err("[Lunarians] presence applyName", throwable);
    } 
  }
  
  public static void setEnabled(boolean paramBoolean) {
    Core.settings.put("lunarians-presence", Boolean.valueOf(paramBoolean));
    applyName();
    if (paramBoolean) {
      registerHostRelay();
      nextPing = 0.0F;
    } 
    if (!paramBoolean) {
      seen.clear();
      sentThisMap = false;
      nextPing = 0.0F;
    } 
  }
  
  public static void init() {
    if (inited)
      return; 
    inited = true;
    try {
      if (Vars.netClient != null)
        Vars.netClient.addPacketHandler("lunarians-user", LunariansPresence::onPacket); 
    } catch (Throwable throwable) {
      Log.err("[Lunarians] presence packet handler", throwable);
    } 
    try {
      Events.on(EventType.PlayerJoin.class, paramPlayerJoin -> registerHostRelay());
      registerHostRelay();
    } catch (Throwable throwable) {}
    applyName();
    Events.run(EventType.Trigger.update, LunariansPresence::update);
    Events.run(EventType.Trigger.draw, LunariansPresence::draw);
    Events.on(EventType.WorldLoadEvent.class, paramWorldLoadEvent -> {
          seen.clear();
          sentThisMap = false;
          nextPing = Time.time + 180.0F;
          applyName();
          registerHostRelay();
        });
    Log.info("[Lunarians] Presence ready (default OFF).");
  }
  
  private static void registerHostRelay() {
    if (!enabled())
      return; 
    try {
      if (Vars.netServer == null)
        return; 
      Vars.netServer.addPacketHandler("lunarians-user", (paramPlayer, paramString) -> {
            if (paramPlayer == null || !enabled())
              return; 
            String str1 = (paramString == null) ? "" : paramString;
            String str2 = str1;
            if (!str1.contains("|"))
              str1 = "" + paramPlayer.id + "|" + paramPlayer.id + "|" + safe(paramPlayer.name); 
            try {
              Call.clientPacketReliable("lunarians-user", str1);
            } catch (Throwable throwable) {
              try {
                Call.clientPacketUnreliable("lunarians-user", str1);
              } catch (Throwable throwable1) {}
            } 
          });
    } catch (Throwable throwable) {}
  }
  
  private static void onPacket(String paramString) {
    if (!enabled())
      return; 
    if (paramString == null || paramString.isEmpty())
      return; 
    try {
      String str = paramString;
      int i = paramString.indexOf('|');
      if (i > 0)
        str = paramString.substring(0, i); 
      if (Vars.player != null && str.equals(String.valueOf(Vars.player.id)))
        return; 
      try {
        if (Vars.player != null && Vars.player.uuid() != null && paramString.contains(Vars.player.uuid()))
          return; 
      } catch (Throwable throwable) {}
      seen.put(str, Time.time);
      String[] arrayOfString = paramString.split("\\|");
      if (arrayOfString.length >= 1 && arrayOfString[0].length() > 0)
        seen.put(arrayOfString[0], Time.time); 
      if (arrayOfString.length >= 3 && arrayOfString[2].length() > 2)
        seen.put(arrayOfString[2], Time.time); 
    } catch (Throwable throwable) {}
  }
  
  private static void update() {
    if (!enabled())
      return; 
    if (Vars.state == null || !Vars.state.isGame() || Vars.player == null)
      return; 
    if (!Vars.net.active())
      return; 
    if (Time.time < nextPing)
      return; 
    nextPing = Time.time + 720.0F;
    String str = "" + Vars.player.id + "|" + Vars.player.id + "|" + safe(Vars.player.name);
    try {
      Call.serverPacketReliable("lunarians-user", str);
    } catch (Throwable throwable) {
      try {
        Call.serverPacketUnreliable("lunarians-user", str);
      } catch (Throwable throwable1) {}
    } 
    try {
      if (Vars.netServer != null)
        Call.clientPacketReliable("lunarians-user", str); 
    } catch (Throwable throwable) {}
    sentThisMap = true;
    Seq seq = new Seq();
    seen.keys().toSeq().each(paramString -> {
          if (Time.time - seen.get(paramString, 0.0F) > 1500.0F)
            paramSeq.add(paramString); 
        });
    seq.each(paramObject -> seen.remove(paramObject, 0.0F));
  }
  
  private static boolean isMarked(Player paramPlayer) {
    if (paramPlayer == null)
      return false; 
    if (paramPlayer.name != null && paramPlayer.name.contains("​⁠‌"))
      return true; 
    if (seen.containsKey(String.valueOf(paramPlayer.id)))
      return true; 
    try {
      if (paramPlayer.uuid() != null && seen.containsKey(paramPlayer.uuid()))
        return true; 
    } catch (Throwable throwable) {}
    return false;
  }
  
  private static void draw() {
    if (!enabled())
      return; 
    if (Vars.state == null || !Vars.state.isGame() || Vars.player == null)
      return; 
    if (!Vars.net.active())
      return; 
    float f = Draw.z();
    for (Player player : Groups.player) {
      Unit unit;
      if (player == null || player == Vars.player || !isMarked(player) || (unit = player.unit()) == null || !unit.isValid())
        continue; 
      float f1 = Math.max(12.0F, unit.hitSize * 0.9F + 6.0F);
      Draw.z(109.99F);
      Draw.color(AURA, 0.18F);
      Fill.circle(unit.x, unit.y, f1);
      Draw.color(AURA, 0.1F);
      Fill.circle(unit.x, unit.y, f1 * 1.35F);
      Draw.z(120.0F);
      try {
        Fonts.outline.draw("Lunarians Team User", unit.x, unit.y + f1 + 6.0F, AURA, 0.22F, false, 1);
      } catch (Throwable throwable) {}
    } 
    Draw.color();
    Draw.z(f);
  }
  
  private static String safe(String paramString) {
    if (paramString == null)
      return ""; 
    return paramString.replace("|", "").replace("\n", "");
  }
}
