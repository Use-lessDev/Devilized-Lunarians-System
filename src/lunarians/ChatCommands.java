/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.Events
 *  arc.graphics.Color
 *  arc.scene.Element
 *  arc.scene.ui.TextField
 *  arc.scene.ui.layout.Table
 *  arc.struct.IntFloatMap
 *  arc.struct.ObjectMap
 *  arc.struct.ObjectMap$Entry
 *  arc.struct.Seq
 *  arc.util.Log
 *  arc.util.Strings
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.EventType$WorldLoadEvent
 *  mindustry.gen.Call
 *  mindustry.gen.Groups
 *  mindustry.gen.Player
 *  mindustry.gen.Unit
 *  mindustry.ui.Styles
 *  mindustry.ui.dialogs.BaseDialog
 */
package lunarians;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.scene.Element;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import arc.struct.IntFloatMap;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Strings;
import arc.util.Time;
import lunarians.LunariansPresence;
import lunarians.LunariansSettings;
import lunarians.UiStyle;
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
    private static float lastHelp = -99999.0f;
    private static float lastFollow;
    private static boolean errorLogged;
    public static final int P_ATTACK = 1;
    public static final int P_MOON = 2;
    private static boolean picking;
    private static float pickUntil;
    private static int tab;
    private static final IntFloatMap lastHelpBy;
    private static BaseDialog panelDialog;
    private static Table pickOverlay;

    private ChatCommands() {
    }

    public static boolean on() {
        return Core.settings.getBool(KEY_ON, false);
    }

    public static String prefix() {
        String string = Core.settings.getString(KEY_PREFIX, "Ron");
        return string == null || string.trim().isEmpty() ? "Ron" : string.trim();
    }

    private static boolean pt() {
        return LunariansSettings.language != null && LunariansSettings.language.startsWith("pt");
    }

    private static String tr(String string, String string2) {
        return ChatCommands.pt() ? string2 : string;
    }

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run((Object)EventType.Trigger.update, ChatCommands::poll);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            prevText.clear();
            prevFade.clear();
            registry.clear();
        });
    }

    private static String plain(Player player) {
        String string = player.plainName();
        if (string != null) {
            string = LunariansPresence.stripMark(string);
        }
        return string == null ? "" : string.trim().toLowerCase();
    }

    private static ObjectMap<Integer, Object[]> perms() {
        ObjectMap objectMap = new ObjectMap();
        for (String string : Core.settings.getString(KEY_ALLOW, "").split(",")) {
            String[] stringArray = string.split(":");
            if (stringArray.length < 2) continue;
            try {
                int n = stringArray.length > 2 ? Integer.parseInt(stringArray[2].trim()) : 3;
                objectMap.put((Object)Integer.parseInt(stringArray[0].trim()), (Object)new Object[]{stringArray[1].trim(), n});
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        return objectMap;
    }

    private static void savePerms(ObjectMap<Integer, Object[]> objectMap) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ObjectMap.Entry entry : objectMap) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(',');
            }
            stringBuilder.append(entry.key).append(':').append(((String)((Object[])entry.value)[0]).replace(",", " ").replace(":", " ")).append(':').append(((Object[])entry.value)[1]);
        }
        Core.settings.put(KEY_ALLOW, (Object)stringBuilder.toString());
    }

    private static boolean allowed(Player player) {
        return player == Vars.player || ChatCommands.perms().containsKey((Object)player.id());
    }

    private static boolean may(Player player, int n) {
        if (player == Vars.player) {
            return true;
        }
        Object[] objectArray = (Object[])ChatCommands.perms().get((Object)player.id());
        return objectArray != null && ((Integer)objectArray[1] & n) != 0;
    }

    public static void addAllowed(Player player) {
        ObjectMap<Integer, Object[]> objectMap = ChatCommands.perms();
        if (!objectMap.containsKey((Object)player.id())) {
            objectMap.put((Object)player.id(), (Object)new Object[]{player.plainName() == null ? "?" : player.plainName(), 3});
            ChatCommands.savePerms(objectMap);
        }
    }

    private static Player playerByName(String string) {
        Player player = null;
        for (int i = 0; i < Groups.player.size(); ++i) {
            Player player2 = (Player)Groups.player.index(i);
            if (player2 == null || player2 == Vars.player || !ChatCommands.plain(player2).equals(string.trim().toLowerCase())) continue;
            if (player != null) {
                return null;
            }
            player = player2;
        }
        return player;
    }

    private static void toast(String string) {
        if (Vars.ui != null) {
            Vars.ui.showInfoToast(string, 3.0f);
        }
    }

    private static void poll() {
        block6: {
            if (Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
                return;
            }
            try {
                boolean bl = ChatCommands.on();
                for (int i = 0; i < Groups.player.size(); ++i) {
                    Player player = (Player)Groups.player.index(i);
                    if (player == null) continue;
                    String string = player.lastText;
                    float f = player.textFadeTime;
                    boolean bl2 = prevText.containsKey((Object)player.id());
                    String string2 = (String)prevText.get((Object)player.id());
                    float f2 = prevFade.get(player.id(), 0.0f);
                    prevText.put((Object)player.id(), (Object)(string == null ? "" : string));
                    prevFade.put(player.id(), f);
                    if (!bl || !bl2 || string == null || string.isEmpty() || string.equals(string2) && !(f > f2 + 0.2f)) continue;
                    ChatCommands.handle(player, string);
                }
                if (picking) {
                    ChatCommands.pickTick();
                }
                if (Time.time - lastFollow > 30.0f) {
                    lastFollow = Time.time;
                    ChatCommands.refreshFollowers();
                }
            }
            catch (Throwable throwable) {
                if (errorLogged) break block6;
                errorLogged = true;
                Log.err((String)"[Lunarians] ChatCommands", (Throwable)throwable);
            }
        }
    }

    private static void refreshFollowers() {
        for (int i = ChatCommands.registry.size - 1; i >= 0; --i) {
            Object[] objectArray = (Object[])registry.get(i);
            if (!"moon".equals(objectArray[0])) continue;
            String string = (String)objectArray[2];
            for (int j = 0; j < Groups.player.size(); ++j) {
                Player player = (Player)Groups.player.index(j);
                if (player == null || !ChatCommands.plain(player).equals(string) || player.unit() == null || player.unit().dead()) continue;
                ((MoonSlot)objectArray[1]).followId = player.unit().id;
            }
        }
    }

    private static void pickTick() {
        if (Time.time > pickUntil) {
            ChatCommands.endPick(true);
            return;
        }
        if (!Core.input.justTouched() || Core.scene.hasMouse()) {
            return;
        }
        float f = Core.input.mouseWorldX();
        float f2 = Core.input.mouseWorldY();
        Player player = null;
        float f3 = Float.MAX_VALUE;
        for (int i = 0; i < Groups.player.size(); ++i) {
            float f4;
            Player player2 = (Player)Groups.player.index(i);
            if (player2 == null || player2.unit() == null || player2.unit().dead()) continue;
            float f5 = player2.unit().dst(f, f2);
            if (!(f4 < Math.max(player2.unit().hitSize, 16.0f) + 8.0f) || !(f5 < f3)) continue;
            f3 = f5;
            player = player2;
        }
        if (player == null) {
            return;
        }
        if (player == Vars.player) {
            ChatCommands.toast(ChatCommands.tr("That is you.", "Essa unidade e voce."));
        } else {
            ChatCommands.addAllowed(player);
            ChatCommands.toast(ChatCommands.tr("Allowed: ", "Permitido: ") + player.plainName() + " (id " + player.id() + ")");
        }
        ChatCommands.endPick(true);
    }

    private static void startPick() {
        picking = true;
        pickUntil = Time.time + 1800.0f;
        if (panelDialog != null) {
            panelDialog.hide();
        }
        try {
            if (Vars.ui != null && Vars.ui.settings != null) {
                Vars.ui.settings.hide();
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        if (pickOverlay != null) {
            pickOverlay.remove();
        }
        pickOverlay = new Table();
        pickOverlay.setFillParent(true);
        pickOverlay.align(4);
        pickOverlay.marginBottom(70.0f);
        pickOverlay.table(Styles.black6, table -> {
            table.add((CharSequence)ChatCommands.tr("Tap a player's unit on the map", "Toque na unidade de um jogador no mapa")).pad(8.0f);
            table.button(ChatCommands.tr("Cancel", "Cancelar"), () -> ChatCommands.endPick(true)).size(110.0f, 40.0f).pad(6.0f);
        });
        Core.scene.add((Element)pickOverlay);
        ChatCommands.toast(ChatCommands.tr("Tap a player's unit on the map.", "Toque na unidade de um jogador no mapa."));
    }

    private static void endPick(boolean bl) {
        picking = false;
        if (pickOverlay != null) {
            pickOverlay.remove();
            pickOverlay = null;
        }
        if (bl) {
            ChatCommands.openPanel();
        }
    }

    public static void openPanel() {
        Runnable[] runnableArray;
        BaseDialog baseDialog;
        if (panelDialog != null) {
            panelDialog.hide();
        }
        panelDialog = baseDialog = new BaseDialog(ChatCommands.tr("Chat commands", "Comandos por chat"));
        UiStyle.register(baseDialog);
        Table table = new Table();
        runnableArray = new Runnable[]{() -> {
            table.clear();
            table.defaults().pad(3.0f);
            table.table(table -> {
                table.check(ChatCommands.tr("Enabled", "Ligado"), ChatCommands.on(), bl -> Core.settings.put(KEY_ON, (Object)bl)).left();
                table.button(ChatCommands.tr("Allowed people", "Pessoas permitidas"), Styles.togglet, () -> {
                    tab = 0;
                    runnableArray[0].run();
                }).checked(textButton -> tab == 0).height(36.0f).padLeft(10.0f);
                table.button(ChatCommands.tr("Chat slots", "Slots do chat"), Styles.togglet, () -> {
                    tab = 1;
                    runnableArray[0].run();
                }).checked(textButton -> tab == 1).height(36.0f);
            }).growX().row();
            if (tab == 0) {
                table.table(table -> {
                    table.add((CharSequence)ChatCommands.tr("Prefix: ", "Prefixo: ")).color(Color.lightGray).padRight(6.0f);
                    TextField textField = (TextField)table.field(ChatCommands.prefix(), string -> Core.settings.put(KEY_PREFIX, (Object)string.trim())).growX().height(40.0f).get();
                    table.button(ChatCommands.tr("Save", "Salvar"), () -> {
                        String string = textField.getText().trim();
                        if (string.isEmpty()) {
                            ChatCommands.toast(ChatCommands.tr("Prefix cannot be empty.", "O prefixo nao pode ficar vazio."));
                            return;
                        }
                        Core.settings.put(KEY_PREFIX, (Object)string);
                        ChatCommands.toast(ChatCommands.tr("Prefix saved: ", "Prefixo salvo: ") + string + ChatCommands.tr("  (say: ", "  (diga: ") + string + " help)");
                    }).size(90.0f, 40.0f).padLeft(6.0f);
                }).growX().row();
                table.add((CharSequence)(ChatCommands.tr("Current prefix: ", "Prefixo atual: ") + ChatCommands.prefix())).color(Color.lightGray).row();
                table.button(ChatCommands.tr("Choose user (tap their unit)", "Escolher usuario (toque na unidade)"), ChatCommands::startPick).growX().height(44.0f).row();
                ObjectMap<Integer, Object[]> objectMap = ChatCommands.perms();
                if (objectMap.isEmpty()) {
                    table.add((CharSequence)ChatCommands.tr("Nobody allowed yet.", "Ninguem permitido ainda.")).color(Color.lightGray).row();
                }
                for (ObjectMap.Entry entry : objectMap) {
                    int n = (Integer)entry.key;
                    String string = (String)((Object[])entry.value)[0];
                    boolean bl = Groups.player.getByID(n) != null;
                    table.table(table -> {
                        table.add((CharSequence)(string + "  (id " + n + (bl ? "" : ", offline") + ")")).left().growX().color(bl ? Color.white : Color.gray);
                        table.button("attack", Styles.togglet, () -> {
                            ObjectMap<Integer, Object[]> objectMap = ChatCommands.perms();
                            Object[] objectArray = (Object[])objectMap.get((Object)n);
                            if (objectArray != null) {
                                objectArray[1] = (Integer)objectArray[1] ^ 1;
                                ChatCommands.savePerms(objectMap);
                            }
                        }).checked(textButton -> {
                            Object[] objectArray = (Object[])ChatCommands.perms().get((Object)n);
                            return objectArray != null && ((Integer)objectArray[1] & 1) != 0;
                        }).size(72.0f, 36.0f);
                        table.button("moon", Styles.togglet, () -> {
                            ObjectMap<Integer, Object[]> objectMap = ChatCommands.perms();
                            Object[] objectArray = (Object[])objectMap.get((Object)n);
                            if (objectArray != null) {
                                objectArray[1] = (Integer)objectArray[1] ^ 2;
                                ChatCommands.savePerms(objectMap);
                            }
                        }).checked(textButton -> {
                            Object[] objectArray = (Object[])ChatCommands.perms().get((Object)n);
                            return objectArray != null && ((Integer)objectArray[1] & 2) != 0;
                        }).size(72.0f, 36.0f);
                        table.button("x", () -> {
                            ObjectMap<Integer, Object[]> objectMap = ChatCommands.perms();
                            objectMap.remove((Object)n);
                            ChatCommands.savePerms(objectMap);
                            runnableArray[0].run();
                        }).size(36.0f);
                    }).growX().row();
                }
            } else {
                if (registry.isEmpty()) {
                    table.add((CharSequence)ChatCommands.tr("No slots created by chat in this session.", "Nenhum slot criado por chat nesta sessao.")).color(Color.lightGray).row();
                }
                for (int i = 0; i < ChatCommands.registry.size; ++i) {
                    Object[] objectArray = (Object[])registry.get(i);
                    boolean bl = "atk".equals(objectArray[0]);
                    int n = bl ? ((AttackSlot)objectArray[1]).maxUnits : ((MoonSlot)objectArray[1]).maxUnits;
                    table.table(table -> {
                        table.add((CharSequence)(String.valueOf(objectArray[2]) + ": " + (bl ? "attack " : "moon ") + String.valueOf(objectArray[3]) + " x" + n)).left().growX();
                        table.button("x", () -> {
                            if (bl) {
                                LunariansSettings.attackSlots.remove((Object)((AttackSlot)objectArray[1]));
                            } else {
                                LunariansSettings.moonSlots.remove((Object)((MoonSlot)objectArray[1]));
                            }
                            registry.remove((Object)objectArray);
                            LunariansSettings.save();
                            runnableArray[0].run();
                        }).size(36.0f);
                    }).growX().row();
                }
            }
        }};
        runnableArray[0].run();
        baseDialog.cont.pane((Element)table).grow().minWidth(320.0f);
        baseDialog.addCloseButton();
        baseDialog.show();
    }

    private static int num(String string, int n, int n2, int n3) {
        try {
            return Math.max(n2, Math.min(n3, Integer.parseInt(string)));
        }
        catch (Throwable throwable) {
            return n;
        }
    }

    private static void say(String ... stringArray) {
        for (int i = 0; i < stringArray.length; ++i) {
            String string = stringArray[i].length() > 140 ? stringArray[i].substring(0, 140) : stringArray[i];
            Time.run((float)((float)i * 100.0f), () -> {
                if (Vars.state != null && Vars.state.isGame()) {
                    Call.sendChatMessage((String)string);
                }
            });
        }
    }

    private static void help(Player player, String string) {
        if (Time.time - lastHelpBy.get(player.id(), -99999.0f) < 600.0f || Time.time - lastHelp < 120.0f) {
            return;
        }
        lastHelpBy.put(player.id(), Time.time);
        lastHelp = Time.time;
        String string2 = ChatCommands.prefix();
        boolean bl = ChatCommands.may(player, 1);
        boolean bl2 = ChatCommands.may(player, 2);
        if (!bl && !bl2) {
            ChatCommands.say(ChatCommands.tr("You have no commands available. Ask the owner for permission.", "Voce nao tem comandos disponiveis. Peca permissao ao dono."));
            return;
        }
        if (string.equals("attack") && bl) {
            ChatCommands.say(string2 + " attack <unit> [amount]   e.g. " + string2 + " attack atrax 5", string2 + " stopattack <unit> [amount]   (no amount = remove all)");
        } else if (string.equals("moon") && bl2) {
            ChatCommands.say(string2 + " moon <unit> [amount] [distance 3-30] [speed 0.2-4]   e.g. " + string2 + " moon flare 5 4 1", string2 + " stopmoon <unit> [amount]   (units orbit you)");
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            if (bl) {
                stringBuilder.append("attack");
            }
            if (bl2) {
                stringBuilder.append(bl ? ", moon" : "moon");
            }
            ChatCommands.say(ChatCommands.tr("Your commands: ", "Seus comandos: ") + String.valueOf(stringBuilder) + ". " + ChatCommands.tr("Say \"", "Diga \"") + string2 + ChatCommands.tr(" help <command>\" to see how to use it, e.g. \"", " help <comando>\" para ver como usar, ex: \"") + string2 + " help " + (bl ? "attack" : "moon") + "\"");
        }
    }

    private static Object[] find(String string, String string2, String string3, boolean bl) {
        for (int i = 0; i < ChatCommands.registry.size; ++i) {
            Object[] objectArray = (Object[])registry.get(i);
            if (!string.equals(objectArray[0]) || !string2.equals(objectArray[3]) || !bl && !string3.equals(objectArray[2])) continue;
            return objectArray;
        }
        return null;
    }

    private static void handle(Player player, String string) {
        String string2 = Strings.stripColors((CharSequence)string).trim();
        String string3 = ChatCommands.prefix();
        if (string2.length() <= string3.length() || !string2.regionMatches(true, 0, string3, 0, string3.length()) || !Character.isWhitespace(string2.charAt(string3.length())) && string2.charAt(string3.length()) != ':' && string2.charAt(string3.length()) != ',') {
            return;
        }
        String[] stringArray = string2.substring(string3.length() + 1).trim().split("\\s+");
        if (stringArray.length == 0 || stringArray[0].isEmpty()) {
            return;
        }
        String string4 = stringArray[0].toLowerCase();
        boolean bl = player == Vars.player;
        boolean bl2 = bl;
        if (string4.equals("help") || string4.equals("commands") && stringArray.length > 1 && stringArray[1].equalsIgnoreCase("help")) {
            String string5 = string4.equals("help") && stringArray.length > 1 ? stringArray[1].toLowerCase() : "";
            String string6 = string5;
            if (string5.equals("stopattack")) {
                string5 = "attack";
            }
            if (string5.equals("stopmoon")) {
                string5 = "moon";
            }
            ChatCommands.help(player, string5);
            return;
        }
        if (!ChatCommands.allowed(player)) {
            return;
        }
        String string7 = ChatCommands.plain(player);
        if (string4.equals("allow") || string4.equals("deny")) {
            if (!bl || stringArray.length < 2) {
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 1; i < stringArray.length; ++i) {
                if (i > 1) {
                    stringBuilder.append(' ');
                }
                stringBuilder.append(stringArray[i]);
            }
            Player player2 = ChatCommands.playerByName(stringBuilder.toString());
            if (player2 == null) {
                ChatCommands.toast(ChatCommands.tr("Player not found (or 2 with that name). Use the panel to pick by unit.", "Jogador nao encontrado (ou 2 com esse nome). Use o painel para escolher pela unidade."));
                return;
            }
            if (string4.equals("allow")) {
                ChatCommands.addAllowed(player2);
            } else {
                ObjectMap<Integer, Object[]> objectMap = ChatCommands.perms();
                objectMap.remove((Object)player2.id());
                ChatCommands.savePerms(objectMap);
            }
            ChatCommands.toast((string4.equals("allow") ? "+ " : "- ") + String.valueOf(stringBuilder));
            return;
        }
        if (string4.equals("who")) {
            if (bl) {
                ChatCommands.toast(ChatCommands.tr("Allowed: ", "Permitidos: ") + ChatCommands.perms().size);
            }
            return;
        }
        boolean bl3 = string4.equals("attack") || string4.equals("stopattack");
        boolean bl4 = string4.equals("moon") || string4.equals("stopmoon");
        boolean bl5 = bl4;
        if (!bl3 && !bl4 || !ChatCommands.may(player, bl3 ? 1 : 2)) {
            return;
        }
        if (stringArray.length < 2) {
            return;
        }
        String string8 = stringArray[1].toLowerCase();
        if (Vars.content.unit(string8) == null) {
            ChatCommands.toast(ChatCommands.tr("Unknown unit: ", "Unidade desconhecida: ") + string8);
            return;
        }
        int n = stringArray.length > 2 ? ChatCommands.num(stringArray[2], 1, 1, 30) : -1;
        int n2 = n;
        if (string4.equals("attack")) {
            ChatCommands.addAttack(string7, string8, n < 0 ? 1 : n);
        } else if (string4.equals("stopattack")) {
            ChatCommands.stop("atk", LunariansSettings.attackSlots, string7, string8, n, bl);
        } else if (string4.equals("moon")) {
            int n3 = stringArray.length > 3 ? ChatCommands.num(stringArray[3], 8, 3, 30) : 8;
            float f = 1.0f;
            if (stringArray.length > 4) {
                try {
                    f = Math.max(0.2f, Math.min(4.0f, Float.parseFloat(stringArray[4].replace(',', '.'))));
                }
                catch (Throwable throwable) {
                    f = 1.0f;
                }
            }
            ChatCommands.addMoon(player, string7, string8, n < 0 ? 1 : n, n3, f);
        } else if (string4.equals("stopmoon")) {
            ChatCommands.stop("moon", LunariansSettings.moonSlots, string7, string8, n, bl);
        }
    }

    private static int chatSlots(String string) {
        int n = 0;
        for (int i = 0; i < ChatCommands.registry.size; ++i) {
            if (!string.equals(((Object[])registry.get(i))[2])) continue;
            ++n;
        }
        return n;
    }

    private static void addAttack(String string, String string2, int n) {
        Object[] objectArray = ChatCommands.find("atk", string2, string, false);
        if (objectArray != null) {
            AttackSlot attackSlot = (AttackSlot)objectArray[1];
            attackSlot.maxUnits = Math.min(30, attackSlot.maxUnits + n);
        } else {
            if (ChatCommands.chatSlots(string) >= 6 || LunariansSettings.attackSlots.size >= 10) {
                ChatCommands.toast(ChatCommands.tr("Too many slots.", "Slots demais."));
                return;
            }
            AttackSlot attackSlot = new AttackSlot();
            attackSlot.unit = string2;
            attackSlot.maxUnits = n;
            LunariansSettings.attackSlots.add((Object)attackSlot);
            registry.add((Object)new Object[]{"atk", attackSlot, string, string2});
        }
        LunariansSettings.enableAutoAttack = true;
        LunariansSettings.save();
        ChatCommands.toast(string + ": attack " + string2 + " " + n);
    }

    private static void addMoon(Player player, String string, String string2, int n, int n2, float f) {
        Unit unit = player.unit();
        if (unit == null || unit.dead()) {
            return;
        }
        Object[] objectArray = ChatCommands.find("moon", string2, string, false);
        if (objectArray != null) {
            MoonSlot moonSlot = (MoonSlot)objectArray[1];
            moonSlot.maxUnits = Math.min(30, moonSlot.maxUnits + n);
            moonSlot.radius = n2;
            moonSlot.speed = f;
            moonSlot.followId = unit.id;
        } else {
            if (ChatCommands.chatSlots(string) >= 6 || LunariansSettings.moonSlots.size >= 8) {
                ChatCommands.toast(ChatCommands.tr("Too many slots.", "Slots demais."));
                return;
            }
            MoonSlot moonSlot = new MoonSlot();
            moonSlot.unit = string2;
            moonSlot.maxUnits = n;
            moonSlot.radius = n2;
            moonSlot.speed = f;
            moonSlot.followId = unit.id;
            moonSlot.followLabel = player.plainName();
            LunariansSettings.moonSlots.add((Object)moonSlot);
            registry.add((Object)new Object[]{"moon", moonSlot, string, string2});
        }
        LunariansSettings.enableAutoMoon = true;
        LunariansSettings.save();
        ChatCommands.toast(string + ": moon " + string2 + " " + n + " " + n2 + " " + f);
    }

    private static <T> void stop(String string, Seq<T> seq, String string2, String string3, int n, boolean bl) {
        Object[] objectArray = ChatCommands.find(string, string3, string2, bl);
        if (objectArray == null) {
            return;
        }
        Object object = objectArray[1];
        int n2 = object instanceof AttackSlot ? ((AttackSlot)object).maxUnits : ((MoonSlot)object).maxUnits;
        int n3 = n2;
        if (n > 0 && n2 > n) {
            if (object instanceof AttackSlot) {
                ((AttackSlot)object).maxUnits = n2 - n;
            } else {
                ((MoonSlot)object).maxUnits = n2 - n;
            }
        } else {
            seq.remove(object);
            registry.remove((Object)objectArray);
        }
        LunariansSettings.save();
        ChatCommands.toast(string2 + ": stop " + string3 + (String)(n > 0 ? " " + n : ""));
    }

    static {
        lastHelpBy = new IntFloatMap();
    }
}

