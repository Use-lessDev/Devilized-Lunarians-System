package lunarians;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.scene.Element;
import arc.scene.ui.TextButton;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import arc.struct.IntFloatMap;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Strings;
import arc.util.Time;
import lunarians.data.AttackSlot;
import lunarians.data.MoonSlot;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public final class ChatCommands {
  public static final String KEY_ON = "lunarians-chat-on";
  
  public static final String KEY_PREFIX = "lunarians-chat-prefix";
  
  public static final String KEY_ALLOW = "lunarians-chat-allow";
  
  private static final int MAX_UNITS = 30;
  
  private static final int MAX_CHAT_SLOTS = 6;
  
  private static final ObjectMap<Integer, String> prevText = new ObjectMap();
  
  private static final IntFloatMap prevFade = new IntFloatMap();
  
  private static final Seq<Object[]> registry = new Seq();
  
  private static float lastHelp = -99999.0F;
  
  private static float lastFollow;
  
  private static boolean errorLogged;
  
  public static final int P_ATTACK = 1;
  
  public static final int P_MOON = 2;
  
  private static boolean picking;
  
  private static float pickUntil;
  
  private static int tab;
  
  public static boolean on() {
    return Core.settings.getBool("lunarians-chat-on", false);
  }
  
  public static String prefix() {
    String str = Core.settings.getString("lunarians-chat-prefix", "Ron");
    return (str == null || str.trim().isEmpty()) ? "Ron" : str.trim();
  }
  
  private static boolean pt() {
    return (LunariansSettings.language != null && LunariansSettings.language.startsWith("pt"));
  }
  
  private static String tr(String paramString1, String paramString2) {
    return pt() ? paramString2 : paramString1;
  }
  
  public static void init() {
    if (Vars.headless)
      return; 
    Events.run(EventType.Trigger.update, ChatCommands::poll);
    Events.on(EventType.WorldLoadEvent.class, paramWorldLoadEvent -> {
          prevText.clear();
          prevFade.clear();
          registry.clear();
        });
  }
  
  private static String plain(Player paramPlayer) {
    String str = paramPlayer.plainName();
    if (str != null)
      str = LunariansPresence.stripMark(str); 
    return (str == null) ? "" : str.trim().toLowerCase();
  }
  
  private static ObjectMap<Integer, Object[]> perms() {
    ObjectMap<Integer, Object[]> objectMap = new ObjectMap();
    for (String str : Core.settings.getString("lunarians-chat-allow", "").split(",")) {
      String[] arrayOfString = str.split(":");
      if (arrayOfString.length >= 2)
        try {
          boolean bool = (arrayOfString.length > 2) ? Integer.parseInt(arrayOfString[2].trim()) : true;
          objectMap.put(Integer.valueOf(Integer.parseInt(arrayOfString[0].trim())), new Object[] { arrayOfString[1].trim(), Integer.valueOf(bool) });
        } catch (Throwable throwable) {} 
    } 
    return objectMap;
  }
  
  private static void savePerms(ObjectMap<Integer, Object[]> paramObjectMap) {
    StringBuilder stringBuilder = new StringBuilder();
    for (ObjectMap.Entries<ObjectMap.Entry> entries = paramObjectMap.iterator(); entries.hasNext(); ) {
      ObjectMap.Entry entry = entries.next();
      if (stringBuilder.length() > 0)
        stringBuilder.append(','); 
      stringBuilder.append(entry.key).append(':').append(((String)((Object[])entry.value)[0]).replace(",", " ").replace(":", " ")).append(':').append(((Object[])entry.value)[1]);
    } 
    Core.settings.put("lunarians-chat-allow", stringBuilder.toString());
  }
  
  private static boolean allowed(Player paramPlayer) {
    return (paramPlayer == Vars.player || perms().containsKey(Integer.valueOf(paramPlayer.id())));
  }
  
  private static boolean may(Player paramPlayer, int paramInt) {
    if (paramPlayer == Vars.player)
      return true; 
    Object[] arrayOfObject = (Object[])perms().get(Integer.valueOf(paramPlayer.id()));
    return (arrayOfObject != null && (((Integer)arrayOfObject[1]).intValue() & paramInt) != 0);
  }
  
  public static void addAllowed(Player paramPlayer) {
    ObjectMap<Integer, Object[]> objectMap = perms();
    if (!objectMap.containsKey(Integer.valueOf(paramPlayer.id()))) {
      objectMap.put(Integer.valueOf(paramPlayer.id()), new Object[] { (paramPlayer.plainName() == null) ? "?" : paramPlayer.plainName(), Integer.valueOf(3) });
      savePerms(objectMap);
    } 
  }
  
  private static Player playerByName(String paramString) {
    Player player = null;
    for (byte b = 0; b < Groups.player.size(); b++) {
      Player player1 = (Player)Groups.player.index(b);
      if (player1 != null && player1 != Vars.player && plain(player1).equals(paramString.trim().toLowerCase())) {
        if (player != null)
          return null; 
        player = player1;
      } 
    } 
    return player;
  }
  
  private static void toast(String paramString) {
    if (Vars.ui != null)
      Vars.ui.showInfoToast(paramString, 3.0F); 
  }
  
  private static void poll() {
    if (Vars.state == null || !Vars.state.isGame() || Vars.player == null)
      return; 
    try {
      boolean bool = on();
      for (byte b = 0; b < Groups.player.size(); b++) {
        Player player = (Player)Groups.player.index(b);
        if (player != null) {
          String str1 = player.lastText;
          float f1 = player.textFadeTime;
          boolean bool1 = prevText.containsKey(Integer.valueOf(player.id()));
          String str2 = (String)prevText.get(Integer.valueOf(player.id()));
          float f2 = prevFade.get(player.id(), 0.0F);
          prevText.put(Integer.valueOf(player.id()), (str1 == null) ? "" : str1);
          prevFade.put(player.id(), f1);
          if (bool && bool1 && str1 != null && !str1.isEmpty() && (!str1.equals(str2) || f1 > f2 + 0.2F))
            handle(player, str1); 
        } 
      } 
      if (picking)
        pickTick(); 
      if (Time.time - lastFollow > 30.0F) {
        lastFollow = Time.time;
        refreshFollowers();
      } 
    } catch (Throwable throwable) {
      if (!errorLogged) {
        errorLogged = true;
        Log.err("[Lunarians] ChatCommands", throwable);
      } 
    } 
  }
  
  private static void refreshFollowers() {
    for (int i = registry.size - 1; i >= 0; i--) {
      Object[] arrayOfObject = (Object[])registry.get(i);
      if ("moon".equals(arrayOfObject[0])) {
        String str = (String)arrayOfObject[2];
        for (byte b = 0; b < Groups.player.size(); b++) {
          Player player = (Player)Groups.player.index(b);
          if (player != null && plain(player).equals(str) && player.unit() != null && !player.unit().dead())
            ((MoonSlot)arrayOfObject[1]).followId = (player.unit()).id; 
        } 
      } 
    } 
  }
  
  private static void pickTick() {
    if (Time.time > pickUntil) {
      endPick(true);
      return;
    } 
    if (!Core.input.justTouched() || Core.scene.hasMouse())
      return; 
    float f1 = Core.input.mouseWorldX();
    float f2 = Core.input.mouseWorldY();
    Player player = null;
    float f3 = Float.MAX_VALUE;
    for (byte b = 0; b < Groups.player.size(); b++) {
      Player player1 = (Player)Groups.player.index(b);
      float f;
      if (player1 != null && player1.unit() != null && !player1.unit().dead() && (f = player1.unit().dst(f1, f2)) < Math.max((player1.unit()).hitSize, 16.0F) + 8.0F && f < f3) {
        f3 = f;
        player = player1;
      } 
    } 
    if (player == null)
      return; 
    if (player == Vars.player) {
      toast(tr("That is you.", "Essa unidade e voce."));
    } else {
      addAllowed(player);
      toast(tr("Allowed: ", "Permitido: ") + tr("Allowed: ", "Permitido: ") + " (id " + player.plainName() + ")");
    } 
    endPick(true);
  }
  
  private static void startPick() {
    picking = true;
    pickUntil = Time.time + 1800.0F;
    if (panelDialog != null)
      panelDialog.hide(); 
    try {
      if (Vars.ui != null && Vars.ui.settings != null)
        Vars.ui.settings.hide(); 
    } catch (Throwable throwable) {}
    if (pickOverlay != null)
      pickOverlay.remove(); 
    pickOverlay = new Table();
    pickOverlay.setFillParent(true);
    pickOverlay.align(4);
    pickOverlay.marginBottom(70.0F);
    pickOverlay.table(Styles.black6, paramTable -> {
          paramTable.add(tr("Tap a player's unit on the map", "Toque na unidade de um jogador no mapa")).pad(8.0F);
          paramTable.button(tr("Cancel", "Cancelar"), ()).size(110.0F, 40.0F).pad(6.0F);
        });
    Core.scene.add((Element)pickOverlay);
    toast(tr("Tap a player's unit on the map.", "Toque na unidade de um jogador no mapa."));
  }
  
  private static void endPick(boolean paramBoolean) {
    picking = false;
    if (pickOverlay != null) {
      pickOverlay.remove();
      pickOverlay = null;
    } 
    if (paramBoolean)
      openPanel(); 
  }
  
  public static void openPanel() {
    if (panelDialog != null)
      panelDialog.hide(); 
    BaseDialog baseDialog = new BaseDialog(tr("Chat commands", "Comandos por chat"));
    panelDialog = baseDialog;
    UiStyle.register(baseDialog);
    Table table = new Table();
    Runnable[] arrayOfRunnable = new Runnable[1];
    arrayOfRunnable[0] = (() -> {
        paramTable.clear();
        paramTable.defaults().pad(3.0F);
        paramTable.table(()).growX().row();
        if (tab == 0) {
          paramTable.table(()).growX().row();
          paramTable.add(tr("Current prefix: ", "Prefixo atual: ") + tr("Current prefix: ", "Prefixo atual: ")).color(Color.lightGray).row();
          paramTable.button(tr("Choose user (tap their unit)", "Escolher usuario (toque na unidade)"), ChatCommands::startPick).growX().height(44.0F).row();
          ObjectMap<Integer, Object[]> objectMap = perms();
          if (objectMap.isEmpty())
            paramTable.add(tr("Nobody allowed yet.", "Ninguem permitido ainda.")).color(Color.lightGray).row(); 
          ObjectMap.Entries<ObjectMap.Entry> entries = objectMap.iterator();
          while (entries.hasNext()) {
            ObjectMap.Entry entry = entries.next();
            int i = ((Integer)entry.key).intValue();
            String str = (String)((Object[])entry.value)[0];
            boolean bool = (Groups.player.getByID(i) != null) ? true : false;
            paramTable.table(()).growX().row();
          } 
        } else {
          if (registry.isEmpty())
            paramTable.add(tr("No slots created by chat in this session.", "Nenhum slot criado por chat nesta sessao.")).color(Color.lightGray).row(); 
          for (byte b = 0; b < registry.size; b++) {
            Object[] arrayOfObject = (Object[])registry.get(b);
            boolean bool = "atk".equals(arrayOfObject[0]);
            int i = bool ? ((AttackSlot)arrayOfObject[1]).maxUnits : ((MoonSlot)arrayOfObject[1]).maxUnits;
            paramTable.table(()).growX().row();
          } 
        } 
      });
    arrayOfRunnable[0].run();
    baseDialog.cont.pane((Element)table).grow().minWidth(320.0F);
    baseDialog.addCloseButton();
    baseDialog.show();
  }
  
  private static int num(String paramString, int paramInt1, int paramInt2, int paramInt3) {
    try {
      return Math.max(paramInt2, Math.min(paramInt3, Integer.parseInt(paramString)));
    } catch (Throwable throwable) {
      return paramInt1;
    } 
  }
  
  private static void say(String... paramVarArgs) {
    for (byte b = 0; b < paramVarArgs.length; b++) {
      String str = (paramVarArgs[b].length() > 140) ? paramVarArgs[b].substring(0, 140) : paramVarArgs[b];
      Time.run(b * 100.0F, () -> {
            if (Vars.state != null && Vars.state.isGame())
              Call.sendChatMessage(paramString); 
          });
    } 
  }
  
  private static void help(Player paramPlayer, String paramString) {
    if (Time.time - lastHelpBy.get(paramPlayer.id(), -99999.0F) < 600.0F || Time.time - lastHelp < 120.0F)
      return; 
    lastHelpBy.put(paramPlayer.id(), Time.time);
    lastHelp = Time.time;
    String str = prefix();
    boolean bool1 = may(paramPlayer, 1);
    boolean bool2 = may(paramPlayer, 2);
    if (!bool1 && !bool2) {
      say(new String[] { tr("You have no commands available. Ask the owner for permission.", "Voce nao tem comandos disponiveis. Peca permissao ao dono.") });
      return;
    } 
    if (paramString.equals("attack") && bool1) {
      say(new String[] { str + " attack <unit> [amount]   e.g. " + str + " attack atrax 5", str + " stopattack <unit> [amount]   (no amount = remove all)" });
    } else if (paramString.equals("moon") && bool2) {
      say(new String[] { str + " moon <unit> [amount] [distance 3-30] [speed 0.2-4]   e.g. " + str + " moon flare 5 4 1", str + " stopmoon <unit> [amount]   (units orbit you)" });
    } else {
      StringBuilder stringBuilder = new StringBuilder();
      if (bool1)
        stringBuilder.append("attack"); 
      if (bool2)
        stringBuilder.append(bool1 ? ", moon" : "moon"); 
      say(new String[] { tr("Your commands: ", "Seus comandos: ") + tr("Your commands: ", "Seus comandos: ") + ". " + String.valueOf(stringBuilder) + tr("Say \"", "Diga \"") + str + tr(" help <command>\" to see how to use it, e.g. \"", " help <comando>\" para ver como usar, ex: \"") + " help " + str + "\"" });
    } 
  }
  
  private static Object[] find(String paramString1, String paramString2, String paramString3, boolean paramBoolean) {
    for (byte b = 0; b < registry.size; ) {
      Object[] arrayOfObject = (Object[])registry.get(b);
      if (!paramString1.equals(arrayOfObject[0]) || !paramString2.equals(arrayOfObject[3]) || (!paramBoolean && !paramString3.equals(arrayOfObject[2]))) {
        b++;
        continue;
      } 
      return arrayOfObject;
    } 
    return null;
  }
  
  private static void handle(Player paramPlayer, String paramString) {
    String str1 = Strings.stripColors(paramString).trim();
    String str2 = prefix();
    if (str1.length() <= str2.length() || !str1.regionMatches(true, 0, str2, 0, str2.length()) || (!Character.isWhitespace(str1.charAt(str2.length())) && str1.charAt(str2.length()) != ':' && str1.charAt(str2.length()) != ','))
      return; 
    String[] arrayOfString = str1.substring(str2.length() + 1).trim().split("\\s+");
    if (arrayOfString.length == 0 || arrayOfString[0].isEmpty())
      return; 
    String str3 = arrayOfString[0].toLowerCase();
    boolean bool3 = (paramPlayer == Vars.player) ? true : false, bool4 = bool3;
    if (str3.equals("help") || (str3.equals("commands") && arrayOfString.length > 1 && arrayOfString[1].equalsIgnoreCase("help"))) {
      String str6 = (str3.equals("help") && arrayOfString.length > 1) ? arrayOfString[1].toLowerCase() : "", str7 = str6;
      if (str6.equals("stopattack"))
        str6 = "attack"; 
      if (str6.equals("stopmoon"))
        str6 = "moon"; 
      help(paramPlayer, str6);
      return;
    } 
    if (!allowed(paramPlayer))
      return; 
    String str4 = plain(paramPlayer);
    if (str3.equals("allow") || str3.equals("deny")) {
      if (!bool3 || arrayOfString.length < 2)
        return; 
      StringBuilder stringBuilder = new StringBuilder();
      for (byte b = 1; b < arrayOfString.length; b++) {
        if (b > 1)
          stringBuilder.append(' '); 
        stringBuilder.append(arrayOfString[b]);
      } 
      Player player = playerByName(stringBuilder.toString());
      if (player == null) {
        toast(tr("Player not found (or 2 with that name). Use the panel to pick by unit.", "Jogador nao encontrado (ou 2 com esse nome). Use o painel para escolher pela unidade."));
        return;
      } 
      if (str3.equals("allow")) {
        addAllowed(player);
      } else {
        ObjectMap<Integer, Object[]> objectMap = perms();
        objectMap.remove(Integer.valueOf(player.id()));
        savePerms(objectMap);
      } 
      toast((str3.equals("allow") ? "+ " : "- ") + (str3.equals("allow") ? "+ " : "- "));
      return;
    } 
    if (str3.equals("who")) {
      if (bool3)
        toast(tr("Allowed: ", "Permitidos: ") + tr("Allowed: ", "Permitidos: ")); 
      return;
    } 
    boolean bool5 = (str3.equals("attack") || str3.equals("stopattack")) ? true : false;
    boolean bool2 = (str3.equals("moon") || str3.equals("stopmoon")) ? true : false, bool6 = bool2;
    if ((!bool5 && !bool2) || !may(paramPlayer, bool5 ? 1 : 2))
      return; 
    if (arrayOfString.length < 2)
      return; 
    String str5 = arrayOfString[1].toLowerCase();
    if (Vars.content.unit(str5) == null) {
      toast(tr("Unknown unit: ", "Unidade desconhecida: ") + tr("Unknown unit: ", "Unidade desconhecida: "));
      return;
    } 
    boolean bool1 = (arrayOfString.length > 2) ? num(arrayOfString[2], 1, 1, 30) : true, bool7 = bool1;
    if (str3.equals("attack")) {
      addAttack(str4, str5, bool1 ? 1 : bool1);
    } else if (str3.equals("stopattack")) {
      stop("atk", LunariansSettings.attackSlots, str4, str5, bool1, bool3);
    } else if (str3.equals("moon")) {
      boolean bool = (arrayOfString.length > 3) ? num(arrayOfString[3], 8, 3, 30) : true;
      float f = 1.0F;
      if (arrayOfString.length > 4)
        try {
          f = Math.max(0.2F, Math.min(4.0F, Float.parseFloat(arrayOfString[4].replace(',', '.'))));
        } catch (Throwable throwable) {
          f = 1.0F;
        }  
      addMoon(paramPlayer, str4, str5, bool1 ? 1 : bool1, bool, f);
    } else if (str3.equals("stopmoon")) {
      stop("moon", LunariansSettings.moonSlots, str4, str5, bool1, bool3);
    } 
  }
  
  private static int chatSlots(String paramString) {
    byte b1 = 0;
    for (byte b2 = 0; b2 < registry.size; b2++) {
      if (paramString.equals(((Object[])registry.get(b2))[2]))
        b1++; 
    } 
    return b1;
  }
  
  private static void addAttack(String paramString1, String paramString2, int paramInt) {
    Object[] arrayOfObject = find("atk", paramString2, paramString1, false);
    if (arrayOfObject != null) {
      AttackSlot attackSlot = (AttackSlot)arrayOfObject[1];
      attackSlot.maxUnits = Math.min(30, attackSlot.maxUnits + paramInt);
    } else {
      if (chatSlots(paramString1) >= 6 || LunariansSettings.attackSlots.size >= 10) {
        toast(tr("Too many slots.", "Slots demais."));
        return;
      } 
      AttackSlot attackSlot = new AttackSlot();
      attackSlot.unit = paramString2;
      attackSlot.maxUnits = paramInt;
      LunariansSettings.attackSlots.add(attackSlot);
      registry.add(new Object[] { "atk", attackSlot, paramString1, paramString2 });
    } 
    LunariansSettings.enableAutoAttack = true;
    LunariansSettings.save();
    toast(paramString1 + ": attack " + paramString1 + " " + paramString2);
  }
  
  private static void addMoon(Player paramPlayer, String paramString1, String paramString2, int paramInt1, int paramInt2, float paramFloat) {
    Unit unit = paramPlayer.unit();
    if (unit == null || unit.dead())
      return; 
    Object[] arrayOfObject = find("moon", paramString2, paramString1, false);
    if (arrayOfObject != null) {
      MoonSlot moonSlot = (MoonSlot)arrayOfObject[1];
      moonSlot.maxUnits = Math.min(30, moonSlot.maxUnits + paramInt1);
      moonSlot.radius = paramInt2;
      moonSlot.speed = paramFloat;
      moonSlot.followId = unit.id;
    } else {
      if (chatSlots(paramString1) >= 6 || LunariansSettings.moonSlots.size >= 8) {
        toast(tr("Too many slots.", "Slots demais."));
        return;
      } 
      MoonSlot moonSlot = new MoonSlot();
      moonSlot.unit = paramString2;
      moonSlot.maxUnits = paramInt1;
      moonSlot.radius = paramInt2;
      moonSlot.speed = paramFloat;
      moonSlot.followId = unit.id;
      moonSlot.followLabel = paramPlayer.plainName();
      LunariansSettings.moonSlots.add(moonSlot);
      registry.add(new Object[] { "moon", moonSlot, paramString1, paramString2 });
    } 
    LunariansSettings.enableAutoMoon = true;
    LunariansSettings.save();
    toast(paramString1 + ": moon " + paramString1 + " " + paramString2 + " " + paramInt1 + " " + paramInt2);
  }
  
  private static <T> void stop(String paramString1, Seq<T> paramSeq, String paramString2, String paramString3, int paramInt, boolean paramBoolean) {
    Object[] arrayOfObject = find(paramString1, paramString3, paramString2, paramBoolean);
    if (arrayOfObject == null)
      return; 
    Object object = arrayOfObject[1];
    int i = (object instanceof AttackSlot) ? ((AttackSlot)object).maxUnits : ((MoonSlot)object).maxUnits, j = i;
    if (paramInt > 0 && i > paramInt) {
      if (object instanceof AttackSlot) {
        ((AttackSlot)object).maxUnits = i - paramInt;
      } else {
        ((MoonSlot)object).maxUnits = i - paramInt;
      } 
    } else {
      paramSeq.remove(object);
      registry.remove(arrayOfObject);
    } 
    LunariansSettings.save();
    toast(paramString2 + ": stop " + paramString2 + paramString3);
  }
  
  private static final IntFloatMap lastHelpBy = new IntFloatMap();
  
  private static BaseDialog panelDialog;
  
  private static Table pickOverlay;
}
