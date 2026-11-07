package lunarians;

import arc.Core;
import arc.Events;
import arc.func.Boolc;
import arc.func.Boolf;
import arc.func.Cons;
import arc.graphics.Color;
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

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ChatCommands.class */
public final class ChatCommands {
    public static final String KEY_ON = "lunarians-chat-on";
    public static final String KEY_PREFIX = "lunarians-chat-prefix";
    public static final String KEY_ALLOW = "lunarians-chat-allow";
    private static final int MAX_UNITS = 30;
    private static final int MAX_CHAT_SLOTS = 6;
    private static float lastFollow;
    private static boolean errorLogged;
    public static final int P_ATTACK = 1;
    public static final int P_MOON = 2;
    private static boolean picking;
    private static float pickUntil;
    private static int tab;
    private static BaseDialog panelDialog;
    private static Table pickOverlay;
    private static final ObjectMap<Integer, String> prevText = new ObjectMap<>();
    private static final IntFloatMap prevFade = new IntFloatMap();
    private static final Seq<Object[]> registry = new Seq<>();
    private static float lastHelp = -99999.0f;
    private static final IntFloatMap lastHelpBy = new IntFloatMap();

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ChatCommands$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Cons {
        public final void get(Object obj) {
            ChatCommands.lambda$openPanel$9((String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ChatCommands$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass1 implements Runnable {
        public final /* synthetic */ TextField f$0;

        @Override // java.lang.Runnable
        public final void run() {
            ChatCommands.lambda$openPanel$10(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ChatCommands$10, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass10 implements Cons {
        public final void get(Object obj) {
            ChatCommands.lambda$openPanel$11((Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ChatCommands$11, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass11 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            ChatCommands.$r8$lambda$W9Uc_iUOdhMRc6-qhDhDnnZ5HMM();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ChatCommands$12, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass12 implements Cons {
        public final /* synthetic */ String f$0;
        public final /* synthetic */ int f$1;
        public final /* synthetic */ boolean f$2;
        public final /* synthetic */ Runnable[] f$3;

        public /* synthetic */ AnonymousClass12(String str, int i, boolean z, Runnable[] runnableArr) {
            this.f$0 = str;
            this.f$1 = i;
            this.f$2 = z;
            this.f$3 = runnableArr;
        }

        public final void get(Object obj) {
            ChatCommands.lambda$openPanel$17(this.f$0, this.f$1, this.f$2, this.f$3, (Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ChatCommands$13, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass13 implements Cons {
        public final /* synthetic */ Object[] f$0;
        public final /* synthetic */ boolean f$1;
        public final /* synthetic */ int f$2;
        public final /* synthetic */ Runnable[] f$3;

        public /* synthetic */ AnonymousClass13(Object[] objArr, boolean z, int i, Runnable[] runnableArr) {
            this.f$0 = objArr;
            this.f$1 = z;
            this.f$2 = i;
            this.f$3 = runnableArr;
        }

        public final void get(Object obj) {
            ChatCommands.lambda$openPanel$19(this.f$0, this.f$1, this.f$2, this.f$3, (Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ChatCommands$14, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass14 implements Runnable {
        public final /* synthetic */ String f$0;

        @Override // java.lang.Runnable
        public final void run() {
            ChatCommands.lambda$say$21(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ChatCommands$15, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass15 implements Runnable {
        public final /* synthetic */ int f$0;

        @Override // java.lang.Runnable
        public final void run() {
            ChatCommands.lambda$openPanel$12(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ChatCommands$16, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass16 implements Boolf {
        public final /* synthetic */ int f$0;

        public final boolean get(Object obj) {
            return ChatCommands.lambda$openPanel$13(this.f$0, (TextButton) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ChatCommands$17, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass17 implements Runnable {
        public final /* synthetic */ int f$0;

        @Override // java.lang.Runnable
        public final void run() {
            ChatCommands.lambda$openPanel$14(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ChatCommands$18, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass18 implements Boolf {
        public final /* synthetic */ int f$0;

        public final boolean get(Object obj) {
            return ChatCommands.lambda$openPanel$15(this.f$0, (TextButton) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ChatCommands$19, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass19 implements Runnable {
        public final /* synthetic */ int f$0;
        public final /* synthetic */ Runnable[] f$1;

        public /* synthetic */ AnonymousClass19(int i, Runnable[] runnableArr) {
            this.f$0 = i;
            this.f$1 = runnableArr;
        }

        @Override // java.lang.Runnable
        public final void run() {
            ChatCommands.lambda$openPanel$16(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ChatCommands$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass2 implements Boolc {
        public final void get(boolean z) {
            ChatCommands.lambda$openPanel$3(z);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ChatCommands$20, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass20 implements Runnable {
        public final /* synthetic */ Table f$0;
        public final /* synthetic */ Runnable[] f$1;

        public /* synthetic */ AnonymousClass20(Table table, Runnable[] runnableArr) {
            this.f$0 = table;
            this.f$1 = runnableArr;
        }

        @Override // java.lang.Runnable
        public final void run() {
            ChatCommands.lambda$openPanel$20(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ChatCommands$21, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass21 implements Runnable {
        public final /* synthetic */ boolean f$0;
        public final /* synthetic */ Object[] f$1;
        public final /* synthetic */ Runnable[] f$2;

        public /* synthetic */ AnonymousClass21(boolean z, Object[] objArr, Runnable[] runnableArr) {
            this.f$0 = z;
            this.f$1 = objArr;
            this.f$2 = runnableArr;
        }

        @Override // java.lang.Runnable
        public final void run() {
            ChatCommands.lambda$openPanel$18(this.f$0, this.f$1, this.f$2);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ChatCommands$22, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass22 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            ChatCommands.$r8$lambda$CA72CLQh_5vGIuZst_mhAbtJW3o();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ChatCommands$23, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass23 implements Cons {
        public final void get(Object obj) {
            ChatCommands.lambda$init$0((EventType.WorldLoadEvent) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ChatCommands$3, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass3 implements Runnable {
        public final /* synthetic */ Runnable[] f$0;

        @Override // java.lang.Runnable
        public final void run() {
            ChatCommands.lambda$openPanel$4(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ChatCommands$4, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass4 implements Boolf {
        public final boolean get(Object obj) {
            return ChatCommands.lambda$openPanel$5((TextButton) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ChatCommands$5, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass5 implements Runnable {
        public final /* synthetic */ Runnable[] f$0;

        @Override // java.lang.Runnable
        public final void run() {
            ChatCommands.lambda$openPanel$6(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ChatCommands$6, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass6 implements Boolf {
        public final boolean get(Object obj) {
            return ChatCommands.lambda$openPanel$7((TextButton) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ChatCommands$7, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass7 implements Cons {
        public final void get(Object obj) {
            ChatCommands.lambda$startPick$2((Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ChatCommands$8, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass8 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            ChatCommands.endPick(true);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ChatCommands$9, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass9 implements Cons {
        public final /* synthetic */ Runnable[] f$0;

        public final void get(Object obj) {
            ChatCommands.lambda$openPanel$8(this.f$0, (Table) obj);
        }
    }

    private ChatCommands() {
    }

    public static boolean on() {
        return Core.settings.getBool(KEY_ON, false);
    }

    public static String prefix() {
        String string = Core.settings.getString(KEY_PREFIX, "Ron");
        return (string == null || string.trim().isEmpty()) ? "Ron" : string.trim();
    }

    private static boolean pt() {
        return LunariansSettings.language != null && LunariansSettings.language.startsWith("pt");
    }

    private static String tr(String str, String str2) {
        return pt() ? str2 : str;
    }

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run(EventType.Trigger.update, ChatCommands::poll);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            prevText.clear();
            prevFade.clear();
            registry.clear();
        });
    }

    private static String plain(Player player) {
        String plainName = player.plainName();
        if (plainName != null) {
            plainName = LunariansPresence.stripMark(plainName);
        }
        return plainName == null ? "" : plainName.trim().toLowerCase();
    }

    private static ObjectMap<Integer, Object[]> perms() {
        ObjectMap<Integer, Object[]> objectMap = new ObjectMap<>();
        for (String str : Core.settings.getString(KEY_ALLOW, "").split(",")) {
            String[] split = str.split(":");
            if (split.length >= 2) {
                try {
                    objectMap.put(Integer.valueOf(Integer.parseInt(split[0].trim())), new Object[]{split[1].trim(), Integer.valueOf(split.length > 2 ? Integer.parseInt(split[2].trim()) : 3)});
                } catch (Throwable th) {
                }
            }
        }
        return objectMap;
    }

    private static void savePerms(ObjectMap<Integer, Object[]> objectMap) {
        StringBuilder sb = new StringBuilder();
        ObjectMap.Entries it = objectMap.iterator();
        while (it.hasNext()) {
            ObjectMap.Entry entry = (ObjectMap.Entry) it.next();
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(entry.key).append(':').append(((String) ((Object[]) entry.value)[0]).replace(",", " ").replace(":", " ")).append(':').append(((Object[]) entry.value)[1]);
        }
        Core.settings.put(KEY_ALLOW, sb.toString());
    }

    private static boolean allowed(Player player) {
        return player == Vars.player || perms().containsKey(Integer.valueOf(player.id()));
    }

    private static boolean may(Player player, int i) {
        if (player == Vars.player) {
            return true;
        }
        Object[] objArr = (Object[]) perms().get(Integer.valueOf(player.id()));
        return (objArr == null || (((Integer) objArr[1]).intValue() & i) == 0) ? false : true;
    }

    public static void addAllowed(Player player) {
        ObjectMap<Integer, Object[]> perms = perms();
        if (!perms.containsKey(Integer.valueOf(player.id()))) {
            Integer valueOf = Integer.valueOf(player.id());
            Object[] objArr = new Object[2];
            objArr[0] = player.plainName() == null ? "?" : player.plainName();
            objArr[1] = 3;
            perms.put(valueOf, objArr);
            savePerms(perms);
        }
    }

    private static Player playerByName(String str) {
        Player player = null;
        for (int i = 0; i < Groups.player.size(); i++) {
            Player player2 = (Player) Groups.player.index(i);
            if (player2 != null && player2 != Vars.player && plain(player2).equals(str.trim().toLowerCase())) {
                if (player != null) {
                    return null;
                }
                player = player2;
            }
        }
        return player;
    }

    private static void toast(String str) {
        if (Vars.ui != null) {
            Vars.ui.showInfoToast(str, 3.0f);
        }
    }

    private static void poll() {
        if (Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
            return;
        }
        try {
            boolean on = on();
            for (int i = 0; i < Groups.player.size(); i++) {
                Player index = Groups.player.index(i);
                if (index != null) {
                    String str = index.lastText;
                    float f = index.textFadeTime;
                    boolean containsKey = prevText.containsKey(Integer.valueOf(index.id()));
                    String str2 = (String) prevText.get(Integer.valueOf(index.id()));
                    float f2 = prevFade.get(index.id(), 0.0f);
                    prevText.put(Integer.valueOf(index.id()), str == null ? "" : str);
                    prevFade.put(index.id(), f);
                    if (on && containsKey && str != null && !str.isEmpty() && (!str.equals(str2) || f > f2 + 0.2f)) {
                        handle(index, str);
                    }
                }
            }
            if (picking) {
                pickTick();
            }
            if (Time.time - lastFollow > 30.0f) {
                lastFollow = Time.time;
                refreshFollowers();
            }
        } catch (Throwable th) {
            if (!errorLogged) {
                errorLogged = true;
                Log.err("[Lunarians] ChatCommands", th);
            }
        }
    }

    private static void refreshFollowers() {
        for (int i = registry.size - 1; i >= 0; i--) {
            Object[] objArr = (Object[]) registry.get(i);
            if ("moon".equals(objArr[0])) {
                String str = (String) objArr[2];
                for (int i2 = 0; i2 < Groups.player.size(); i2++) {
                    Player index = Groups.player.index(i2);
                    if (index != null && plain(index).equals(str) && index.unit() != null && !index.unit().dead()) {
                        ((MoonSlot) objArr[1]).followId = index.unit().id;
                    }
                }
            }
        }
    }

    private static void pickTick() {
        if (Time.time > pickUntil) {
            endPick(true);
            return;
        }
        if (!Core.input.justTouched() || Core.scene.hasMouse()) {
            return;
        }
        float mouseWorldX = Core.input.mouseWorldX();
        float mouseWorldY = Core.input.mouseWorldY();
        Player player = null;
        float f = Float.MAX_VALUE;
        for (int i = 0; i < Groups.player.size(); i++) {
            Player player2 = (Player) Groups.player.index(i);
            if (player2 != null && player2.unit() != null && !player2.unit().dead()) {
                float dst = player2.unit().dst(mouseWorldX, mouseWorldY);
                if (dst < Math.max(player2.unit().hitSize, 16.0f) + 8.0f && dst < f) {
                    f = dst;
                    player = player2;
                }
            }
        }
        if (player == null) {
            return;
        }
        if (player == Vars.player) {
            toast(tr("That is you.", "Essa unidade e voce."));
        } else {
            addAllowed(player);
            toast(tr("Allowed: ", "Permitido: ") + player.plainName() + " (id " + player.id() + ")");
        }
        endPick(true);
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
        } catch (Throwable th) {
        }
        if (pickOverlay != null) {
            pickOverlay.remove();
        }
        pickOverlay = new Table();
        pickOverlay.setFillParent(true);
        pickOverlay.align(4);
        pickOverlay.marginBottom(70.0f);
        pickOverlay.table(Styles.black6, table -> {
            table.add(tr("Tap a player's unit on the map", "Toque na unidade de um jogador no mapa")).pad(8.0f);
            table.button(tr("Cancel", "Cancelar"), () -> {
                endPick(true);
            }).size(110.0f, 40.0f).pad(6.0f);
        });
        Core.scene.add(pickOverlay);
        toast(tr("Tap a player's unit on the map.", "Toque na unidade de um jogador no mapa."));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void endPick(boolean z) {
        picking = false;
        if (pickOverlay != null) {
            pickOverlay.remove();
            pickOverlay = null;
        }
        if (z) {
            openPanel();
        }
    }

    public static void openPanel() {
        if (panelDialog != null) {
            panelDialog.hide();
        }
        BaseDialog baseDialog = new BaseDialog(tr("Chat commands", "Comandos por chat"));
        panelDialog = baseDialog;
        UiStyle.register(baseDialog);
        Table table = new Table();
        Runnable[] runnableArr = new Runnable[1];
        runnableArr[0] = () -> {
            table.clear();
            table.defaults().pad(3.0f);
            table.table(table2 -> {
                table2.check(tr("Enabled", "Ligado"), on(), z -> {
                    Core.settings.put(KEY_ON, Boolean.valueOf(z));
                }).left();
                table2.button(tr("Allowed people", "Pessoas permitidas"), Styles.togglet, () -> {
                    tab = 0;
                    runnableArr[0].run();
                }).checked(textButton -> {
                    return tab == 0;
                }).height(36.0f).padLeft(10.0f);
                table2.button(tr("Chat slots", "Slots do chat"), Styles.togglet, () -> {
                    tab = 1;
                    runnableArr[0].run();
                }).checked(textButton2 -> {
                    return tab == 1;
                }).height(36.0f);
            }).growX().row();
            if (tab == 0) {
                table.table(table3 -> {
                    table3.add(tr("Prefix: ", "Prefixo: ")).color(Color.lightGray).padRight(6.0f);
                    TextField textField = table3.field(prefix(), str -> {
                        Core.settings.put(KEY_PREFIX, str.trim());
                    }).growX().height(40.0f).get();
                    table3.button(tr("Save", "Salvar"), () -> {
                        String trim = textField.getText().trim();
                        if (trim.isEmpty()) {
                            toast(tr("Prefix cannot be empty.", "O prefixo nao pode ficar vazio."));
                        } else {
                            Core.settings.put(KEY_PREFIX, trim);
                            toast(tr("Prefix saved: ", "Prefixo salvo: ") + trim + tr("  (say: ", "  (diga: ") + trim + " help)");
                        }
                    }).size(90.0f, 40.0f).padLeft(6.0f);
                }).growX().row();
                table.add(tr("Current prefix: ", "Prefixo atual: ") + prefix()).color(Color.lightGray).row();
                table.button(tr("Choose user (tap their unit)", "Escolher usuario (toque na unidade)"), ChatCommands::startPick).growX().height(44.0f).row();
                ObjectMap<Integer, Object[]> perms = perms();
                if (perms.isEmpty()) {
                    table.add(tr("Nobody allowed yet.", "Ninguem permitido ainda.")).color(Color.lightGray).row();
                }
                ObjectMap.Entries it = perms.iterator();
                while (it.hasNext()) {
                    ObjectMap.Entry entry = (ObjectMap.Entry) it.next();
                    int intValue = ((Integer) entry.key).intValue();
                    String str = (String) ((Object[]) entry.value)[0];
                    boolean z = Groups.player.getByID(intValue) != null;
                    table.table(table4 -> {
                        table4.add(str + "  (id " + intValue + (z ? "" : ", offline") + ")").left().growX().color(z ? Color.white : Color.gray);
                        table4.button("attack", Styles.togglet, () -> {
                            ObjectMap<Integer, Object[]> perms2 = perms();
                            Object[] objArr = (Object[]) perms2.get(Integer.valueOf(intValue));
                            if (objArr != null) {
                                objArr[1] = Integer.valueOf(((Integer) objArr[1]).intValue() ^ 1);
                                savePerms(perms2);
                            }
                        }).checked(textButton -> {
                            Object[] objArr = (Object[]) perms().get(Integer.valueOf(intValue));
                            return (objArr == null || (((Integer) objArr[1]).intValue() & 1) == 0) ? false : true;
                        }).size(72.0f, 36.0f);
                        table4.button("moon", Styles.togglet, () -> {
                            ObjectMap<Integer, Object[]> perms2 = perms();
                            Object[] objArr = (Object[]) perms2.get(Integer.valueOf(intValue));
                            if (objArr != null) {
                                objArr[1] = Integer.valueOf(((Integer) objArr[1]).intValue() ^ 2);
                                savePerms(perms2);
                            }
                        }).checked(textButton2 -> {
                            Object[] objArr = (Object[]) perms().get(Integer.valueOf(intValue));
                            return (objArr == null || (((Integer) objArr[1]).intValue() & 2) == 0) ? false : true;
                        }).size(72.0f, 36.0f);
                        table4.button("x", () -> {
                            ObjectMap<Integer, Object[]> perms2 = perms();
                            perms2.remove(Integer.valueOf(intValue));
                            savePerms(perms2);
                            runnableArr[0].run();
                        }).size(36.0f);
                    }).growX().row();
                }
                return;
            }
            if (registry.isEmpty()) {
                table.add(tr("No slots created by chat in this session.", "Nenhum slot criado por chat nesta sessao.")).color(Color.lightGray).row();
            }
            for (int i = 0; i < registry.size; i++) {
                Object[] objArr = (Object[]) registry.get(i);
                boolean equals = "atk".equals(objArr[0]);
                int i2 = equals ? ((AttackSlot) objArr[1]).maxUnits : ((MoonSlot) objArr[1]).maxUnits;
                table.table(table5 -> {
                    table5.add(String.valueOf(objArr[2]) + ": " + (equals ? "attack " : "moon ") + String.valueOf(objArr[3]) + " x" + i2).left().growX();
                    table5.button("x", () -> {
                        if (equals) {
                            LunariansSettings.attackSlots.remove((AttackSlot) objArr[1]);
                        } else {
                            LunariansSettings.moonSlots.remove((MoonSlot) objArr[1]);
                        }
                        registry.remove(objArr);
                        LunariansSettings.save();
                        runnableArr[0].run();
                    }).size(36.0f);
                }).growX().row();
            }
        };
        runnableArr[0].run();
        baseDialog.cont.pane(table).grow().minWidth(320.0f);
        baseDialog.addCloseButton();
        baseDialog.show();
    }

    private static int num(String str, int i, int i2, int i3) {
        try {
            return Math.max(i2, Math.min(i3, Integer.parseInt(str)));
        } catch (Throwable th) {
            return i;
        }
    }

    private static void say(String... strArr) {
        for (int i = 0; i < strArr.length; i++) {
            String substring = strArr[i].length() > 140 ? strArr[i].substring(0, 140) : strArr[i];
            Time.run(i * 100.0f, () -> {
                if (Vars.state != null && Vars.state.isGame()) {
                    Call.sendChatMessage(substring);
                }
            });
        }
    }

    private static void help(Player player, String str) {
        if (Time.time - lastHelpBy.get(player.id(), -99999.0f) < 600.0f || Time.time - lastHelp < 120.0f) {
            return;
        }
        lastHelpBy.put(player.id(), Time.time);
        lastHelp = Time.time;
        String prefix = prefix();
        boolean may = may(player, 1);
        boolean may2 = may(player, 2);
        if (!may && !may2) {
            say(tr("You have no commands available. Ask the owner for permission.", "Voce nao tem comandos disponiveis. Peca permissao ao dono."));
            return;
        }
        if (str.equals("attack") && may) {
            say(prefix + " attack <unit> [amount]   e.g. " + prefix + " attack atrax 5", prefix + " stopattack <unit> [amount]   (no amount = remove all)");
            return;
        }
        if (str.equals("moon") && may2) {
            say(prefix + " moon <unit> [amount] [distance 3-30] [speed 0.2-4]   e.g. " + prefix + " moon flare 5 4 1", prefix + " stopmoon <unit> [amount]   (units orbit you)");
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (may) {
            sb.append("attack");
        }
        if (may2) {
            sb.append(may ? ", moon" : "moon");
        }
        String[] strArr = new String[1];
        strArr[0] = tr("Your commands: ", "Seus comandos: ") + String.valueOf(sb) + ". " + tr("Say \"", "Diga \"") + prefix + tr(" help <command>\" to see how to use it, e.g. \"", " help <comando>\" para ver como usar, ex: \"") + prefix + " help " + (may ? "attack" : "moon") + "\"";
        say(strArr);
    }

    private static Object[] find(String str, String str2, String str3, boolean z) {
        for (int i = 0; i < registry.size; i++) {
            Object[] objArr = (Object[]) registry.get(i);
            if (str.equals(objArr[0]) && str2.equals(objArr[3]) && (z || str3.equals(objArr[2]))) {
                return objArr;
            }
        }
        return null;
    }

    private static void handle(Player player, String str) {
        String trim = Strings.stripColors(str).trim();
        String prefix = prefix();
        if (trim.length() <= prefix.length() || !trim.regionMatches(true, 0, prefix, 0, prefix.length())) {
            return;
        }
        if (!Character.isWhitespace(trim.charAt(prefix.length())) && trim.charAt(prefix.length()) != ':' && trim.charAt(prefix.length()) != ',') {
            return;
        }
        String[] split = trim.substring(prefix.length() + 1).trim().split("\\s+");
        if (split.length == 0 || split[0].isEmpty()) {
            return;
        }
        String lowerCase = split[0].toLowerCase();
        boolean z = player == Vars.player;
        if (lowerCase.equals("help") || (lowerCase.equals("commands") && split.length > 1 && split[1].equalsIgnoreCase("help"))) {
            String lowerCase2 = (!lowerCase.equals("help") || split.length <= 1) ? "" : split[1].toLowerCase();
            if (lowerCase2.equals("stopattack")) {
                lowerCase2 = "attack";
            }
            if (lowerCase2.equals("stopmoon")) {
                lowerCase2 = "moon";
            }
            help(player, lowerCase2);
            return;
        }
        if (!allowed(player)) {
            return;
        }
        String plain = plain(player);
        if (lowerCase.equals("allow") || lowerCase.equals("deny")) {
            if (!z || split.length < 2) {
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < split.length; i++) {
                if (i > 1) {
                    sb.append(' ');
                }
                sb.append(split[i]);
            }
            Player playerByName = playerByName(sb.toString());
            if (playerByName == null) {
                toast(tr("Player not found (or 2 with that name). Use the panel to pick by unit.", "Jogador nao encontrado (ou 2 com esse nome). Use o painel para escolher pela unidade."));
                return;
            }
            if (lowerCase.equals("allow")) {
                addAllowed(playerByName);
            } else {
                ObjectMap<Integer, Object[]> perms = perms();
                perms.remove(Integer.valueOf(playerByName.id()));
                savePerms(perms);
            }
            toast((lowerCase.equals("allow") ? "+ " : "- ") + String.valueOf(sb));
            return;
        }
        if (lowerCase.equals("who")) {
            if (z) {
                toast(tr("Allowed: ", "Permitidos: ") + perms().size);
                return;
            }
            return;
        }
        boolean z2 = lowerCase.equals("attack") || lowerCase.equals("stopattack");
        boolean z3 = lowerCase.equals("moon") || lowerCase.equals("stopmoon");
        if (!z2 && !z3) {
            return;
        }
        if (!may(player, z2 ? 1 : 2) || split.length < 2) {
            return;
        }
        String lowerCase3 = split[1].toLowerCase();
        if (Vars.content.unit(lowerCase3) == null) {
            toast(tr("Unknown unit: ", "Unidade desconhecida: ") + lowerCase3);
            return;
        }
        int num = split.length > 2 ? num(split[2], 1, 1, MAX_UNITS) : -1;
        if (lowerCase.equals("attack")) {
            addAttack(plain, lowerCase3, num < 0 ? 1 : num);
            return;
        }
        if (lowerCase.equals("stopattack")) {
            stop("atk", LunariansSettings.attackSlots, plain, lowerCase3, num, z);
            return;
        }
        if (!lowerCase.equals("moon")) {
            if (lowerCase.equals("stopmoon")) {
                stop("moon", LunariansSettings.moonSlots, plain, lowerCase3, num, z);
            }
        } else {
            int num2 = split.length > 3 ? num(split[3], 8, 3, MAX_UNITS) : 8;
            float f = 1.0f;
            if (split.length > 4) {
                try {
                    f = Math.max(0.2f, Math.min(4.0f, Float.parseFloat(split[4].replace(',', '.'))));
                } catch (Throwable th) {
                    f = 1.0f;
                }
            }
            addMoon(player, plain, lowerCase3, num < 0 ? 1 : num, num2, f);
        }
    }

    private static int chatSlots(String str) {
        int i = 0;
        for (int i2 = 0; i2 < registry.size; i2++) {
            if (str.equals(((Object[]) registry.get(i2))[2])) {
                i++;
            }
        }
        return i;
    }

    private static void addAttack(String str, String str2, int i) {
        Object[] find = find("atk", str2, str, false);
        if (find != null) {
            AttackSlot attackSlot = (AttackSlot) find[1];
            attackSlot.maxUnits = Math.min(MAX_UNITS, attackSlot.maxUnits + i);
        } else {
            if (chatSlots(str) >= MAX_CHAT_SLOTS || LunariansSettings.attackSlots.size >= 10) {
                toast(tr("Too many slots.", "Slots demais."));
                return;
            }
            AttackSlot attackSlot2 = new AttackSlot();
            attackSlot2.unit = str2;
            attackSlot2.maxUnits = i;
            LunariansSettings.attackSlots.add(attackSlot2);
            registry.add(new Object[]{"atk", attackSlot2, str, str2});
        }
        LunariansSettings.enableAutoAttack = true;
        LunariansSettings.save();
        toast(str + ": attack " + str2 + " " + i);
    }

    private static void addMoon(Player player, String str, String str2, int i, int i2, float f) {
        Unit unit = player.unit();
        if (unit == null || unit.dead()) {
            return;
        }
        Object[] find = find("moon", str2, str, false);
        if (find != null) {
            MoonSlot moonSlot = (MoonSlot) find[1];
            moonSlot.maxUnits = Math.min(MAX_UNITS, moonSlot.maxUnits + i);
            moonSlot.radius = i2;
            moonSlot.speed = f;
            moonSlot.followId = unit.id;
        } else {
            if (chatSlots(str) >= MAX_CHAT_SLOTS || LunariansSettings.moonSlots.size >= 8) {
                toast(tr("Too many slots.", "Slots demais."));
                return;
            }
            MoonSlot moonSlot2 = new MoonSlot();
            moonSlot2.unit = str2;
            moonSlot2.maxUnits = i;
            moonSlot2.radius = i2;
            moonSlot2.speed = f;
            moonSlot2.followId = unit.id;
            moonSlot2.followLabel = player.plainName();
            LunariansSettings.moonSlots.add(moonSlot2);
            registry.add(new Object[]{"moon", moonSlot2, str, str2});
        }
        LunariansSettings.enableAutoMoon = true;
        LunariansSettings.save();
        toast(str + ": moon " + str2 + " " + i + " " + i2 + " " + f);
    }

    private static <T> void stop(String str, Seq<T> seq, String str2, String str3, int i, boolean z) {
        Object[] find = find(str, str3, str2, z);
        if (find == null) {
            return;
        }
        Object obj = find[1];
        int i2 = obj instanceof AttackSlot ? ((AttackSlot) obj).maxUnits : ((MoonSlot) obj).maxUnits;
        if (i > 0 && i2 > i) {
            if (obj instanceof AttackSlot) {
                ((AttackSlot) obj).maxUnits = i2 - i;
            } else {
                ((MoonSlot) obj).maxUnits = i2 - i;
            }
        } else {
            seq.remove(obj);
            registry.remove(find);
        }
        LunariansSettings.save();
        toast(str2 + ": stop " + str3 + (i > 0 ? " " + i : ""));
    }
}
