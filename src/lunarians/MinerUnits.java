/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.Events
 *  arc.scene.Element
 *  arc.scene.ui.layout.Table
 *  arc.struct.IntSeq
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.ai.UnitCommand
 *  mindustry.ai.UnitStance
 *  mindustry.ai.types.CommandAI
 *  mindustry.content.Items
 *  mindustry.entities.units.UnitController
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.EventType$WorldLoadEvent
 *  mindustry.gen.Building
 *  mindustry.gen.Call
 *  mindustry.gen.Groups
 *  mindustry.gen.Player
 *  mindustry.type.UnitType
 *  mindustry.ui.dialogs.BaseDialog
 *  mindustry.world.blocks.units.UnitFactory
 *  mindustry.world.blocks.units.UnitFactory$UnitFactoryBuild
 *  mindustry.world.blocks.units.UnitFactory$UnitPlan
 */
package lunarians;

import arc.Core;
import arc.Events;
import arc.scene.Element;
import arc.scene.ui.layout.Table;
import arc.struct.IntSeq;
import arc.util.Log;
import arc.util.Time;
import java.util.HashSet;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.UnitStance;
import mindustry.ai.types.CommandAI;
import mindustry.content.Items;
import mindustry.entities.units.UnitController;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.type.UnitType;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.blocks.units.UnitFactory;

public class MinerUnits {
    public static final String[] TYPES = new String[]{"mono", "poly", "mega", "quasar"};
    private static float next;
    private static final HashSet<Integer> mine;
    private static boolean registered;
    private static boolean poor;

    public static void init() {
        if (registered) {
            return;
        }
        registered = true;
        Events.run((Object)EventType.Trigger.update, MinerUnits::update);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> mine.clear());
    }

    private static String tr(String string, String string2, String string3, String string4) {
        String string5 = LunariansI18n.getLanguage();
        if ("pt".equals(string5)) {
            return string;
        }
        if ("es".equals(string5)) {
            return string3;
        }
        if ("ru".equals(string5)) {
            return string4;
        }
        return string2;
    }

    public static String title() {
        return MinerUnits.tr("Mineradores", "Miner units", "Unidades mineras", "\u0428\u0430\u0445\u0442\u0451\u0440\u044b");
    }

    public static boolean enabled() {
        return Core.settings.getBool("lunarians-mu-on", false);
    }

    public static void setEnabled(boolean bl) {
        Core.settings.put("lunarians-mu-on", (Object)bl);
    }

    public static boolean typeOn(int n) {
        return Core.settings.getBool("lunarians-mu-t-" + TYPES[n], true);
    }

    public static void setTypeOn(int n, boolean bl) {
        Core.settings.put("lunarians-mu-t-" + TYPES[n], (Object)bl);
    }

    public static boolean produce() {
        return Core.settings.getBool("lunarians-mu-prod", true);
    }

    public static void setProduce(boolean bl) {
        Core.settings.put("lunarians-mu-prod", (Object)bl);
    }

    public static int cap() {
        return Math.max(1, Math.min(40, Core.settings.getInt("lunarians-mu-cap", 8)));
    }

    public static void setCap(int n) {
        Core.settings.put("lunarians-mu-cap", (Object)Math.max(1, Math.min(40, n)));
    }

    private static int typeIndex(UnitType unitType) {
        for (int i = 0; i < TYPES.length; ++i) {
            if (unitType == null || !TYPES[i].equals(unitType.name)) continue;
            return i;
        }
        return -1;
    }

    private static void update() {
        if (!MinerUnits.enabled() || Vars.state == null || !Vars.state.isGame() || Vars.player == null || Time.time < next) {
            return;
        }
        next = Time.time + 120.0f;
        try {
            if (Vars.player.closestCore() == null) {
                return;
            }
            IntSeq intSeq = new IntSeq();
            IntSeq intSeq2 = new IntSeq();
            int[] nArray = new int[]{0};
            Groups.unit.each(unit -> {
                if (unit.team != Vars.player.team() || unit.isPlayer() || unit.dead) {
                    return;
                }
                int n = MinerUnits.typeIndex(unit.type);
                if (n < 0 || !MinerUnits.typeOn(n)) {
                    return;
                }
                nArray[0] = nArray[0] + 1;
                UnitController unitController = unit.controller();
                if (!(unitController instanceof CommandAI)) {
                    return;
                }
                CommandAI commandAI = (CommandAI)unitController;
                if (commandAI.currentCommand() != UnitCommand.mineCommand) {
                    intSeq.add(unit.id);
                } else if (!commandAI.hasStance(UnitStance.mineAuto)) {
                    intSeq2.add(unit.id);
                }
            });
            if (intSeq.size > 0) {
                Call.setUnitCommand((Player)Vars.player, (int[])intSeq.toArray(), (UnitCommand)UnitCommand.mineCommand);
            }
            if (intSeq2.size > 0) {
                Call.setUnitStance((Player)Vars.player, (int[])intSeq2.toArray(), (UnitStance)UnitStance.mineAuto, (boolean)true);
            }
            if (MinerUnits.produce()) {
                MinerUnits.factories(nArray[0]);
            }
        }
        catch (Throwable throwable) {
            Log.err((String)"[Lunarians] MinerUnits", (Throwable)throwable);
        }
    }

    private static void factories(int n) {
        boolean bl = MinerUnits.typeOn(0) || MinerUnits.typeOn(1) || MinerUnits.typeOn(2);
        boolean bl2 = MinerUnits.typeOn(3);
        int n2 = Vars.player.closestCore().items.get(Items.silicon);
        int n3 = Vars.player.closestCore().items.get(Items.lead);
        if (n2 < 30 || n3 < 20) {
            poor = true;
        } else if (n2 >= 100 && n3 >= 60) {
            poor = false;
        }
        boolean bl3 = n >= MinerUnits.cap() || poor;
        for (Building building : Groups.build) {
            String string;
            if (building.team != Vars.player.team() || !(building instanceof UnitFactory.UnitFactoryBuild)) continue;
            UnitFactory.UnitFactoryBuild unitFactoryBuild = (UnitFactory.UnitFactoryBuild)building;
            UnitFactory unitFactory = (UnitFactory)unitFactoryBuild.block;
            String string2 = unitFactory.name.equals("air-factory") ? (bl ? "mono" : null) : (unitFactory.name.equals("ground-factory") ? (bl2 ? "nova" : null) : (string = null));
            if (string == null) continue;
            int n4 = unitFactoryBuild.pos();
            if (bl3) {
                if (!mine.remove(n4) || unitFactoryBuild.currentPlan == -1) continue;
                Call.tileConfig((Player)Vars.player, (Building)unitFactoryBuild, (Object)-1);
                continue;
            }
            if (unitFactoryBuild.currentPlan != -1 && !mine.contains(n4)) continue;
            int n5 = -1;
            for (int i = 0; i < unitFactory.plans.size; ++i) {
                if (((UnitFactory.UnitPlan)unitFactory.plans.get((int)i)).unit == null || !string.equals(((UnitFactory.UnitPlan)unitFactory.plans.get((int)i)).unit.name)) continue;
                n5 = i;
            }
            if (n5 >= 0 && unitFactoryBuild.currentPlan != n5) {
                Call.tileConfig((Player)Vars.player, (Building)unitFactoryBuild, (Object)n5);
                mine.add(n4);
                continue;
            }
            if (n5 < 0) continue;
            mine.add(n4);
        }
    }

    public static BaseDialog dialog() {
        Runnable[] runnableArray;
        BaseDialog baseDialog = new BaseDialog(MinerUnits.title());
        runnableArray = new Runnable[]{() -> {
            Table table = baseDialog.cont;
            table.clear();
            table.defaults().left().pad(4.0f);
            table.check(MinerUnits.tr("Ativar mineradores", "Enable miner units", "Activar unidades mineras", "\u0412\u043a\u043b\u044e\u0447\u0438\u0442\u044c \u0448\u0430\u0445\u0442\u0451\u0440\u043e\u0432"), MinerUnits.enabled(), bl -> {
                MinerUnits.setEnabled(bl);
                Vars.ui.showInfoToast(MinerUnits.title() + ": " + bl, 2.0f);
            }).row();
            table.add((CharSequence)MinerUnits.tr("Manda as unidades minerarem sempre o recurso que mais falta no n\u00facleo.", "Sends the units to mine whatever the core lacks most.", "Env\u00eda las unidades a minar lo que m\u00e1s falta en el n\u00facleo.", "\u041e\u0442\u043f\u0440\u0430\u0432\u043b\u044f\u0435\u0442 \u044e\u043d\u0438\u0442\u043e\u0432 \u0434\u043e\u0431\u044b\u0432\u0430\u0442\u044c \u0442\u043e, \u0447\u0435\u0433\u043e \u0432 \u044f\u0434\u0440\u0435 \u043c\u0435\u043d\u044c\u0448\u0435 \u0432\u0441\u0435\u0433\u043e.")).wrap().width(380.0f).row();
            for (int i = 0; i < TYPES.length; ++i) {
                int n = i;
                UnitType unitType = Vars.content.unit(TYPES[i]);
                table.check(unitType != null ? unitType.localizedName : TYPES[i], MinerUnits.typeOn(i), bl -> MinerUnits.setTypeOn(n, bl)).row();
            }
            table.check(MinerUnits.tr("Produzir unidades (configura air/ground factory)", "Produce units (configures air/ground factory)", "Producir unidades (configura air/ground factory)", "\u041f\u0440\u043e\u0438\u0437\u0432\u043e\u0434\u0438\u0442\u044c \u044e\u043d\u0438\u0442\u043e\u0432 (\u043d\u0430\u0441\u0442\u0440\u0430\u0438\u0432\u0430\u0435\u0442 air/ground factory)"), MinerUnits.produce(), bl -> MinerUnits.setProduce(bl)).row();
            Table table2 = new Table();
            table2.add((CharSequence)MinerUnits.tr("Quantidade m\u00e1xima", "Max amount", "Cantidad m\u00e1xima", "\u041c\u0430\u043a\u0441. \u043a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e")).width(190.0f);
            table2.button("-", () -> {
                MinerUnits.setCap(MinerUnits.cap() - 1);
                LunariansSettings.save();
                runnableArray[0].run();
            }).size(46.0f, 40.0f).pad(2.0f);
            table2.add((CharSequence)String.valueOf(MinerUnits.cap())).width(48.0f).center();
            table2.button("+", () -> {
                MinerUnits.setCap(MinerUnits.cap() + 1);
                LunariansSettings.save();
                runnableArray[0].run();
            }).size(46.0f, 40.0f).pad(2.0f);
            table.add((Element)table2).row();
            table.add((CharSequence)MinerUnits.tr("Mono\u2192Poly\u2192Mega vem das reconstructors; Quasar: ground factory (Nova\u2192Pulsar\u2192Quasar).", "Mono\u2192Poly\u2192Mega come from the reconstructors; Quasar: ground factory (Nova\u2192Pulsar\u2192Quasar).", "Mono\u2192Poly\u2192Mega vienen de los reconstructores; Quasar: ground factory (Nova\u2192Pulsar\u2192Quasar).", "Mono\u2192Poly\u2192Mega \u2014 \u0438\u0437 \u0440\u0435\u043a\u043e\u043d\u0441\u0442\u0440\u0443\u043a\u0442\u043e\u0440\u043e\u0432; Quasar \u2014 ground factory (Nova\u2192Pulsar\u2192Quasar).")).wrap().width(380.0f).row();
        }};
        runnableArray[0].run();
        baseDialog.addCloseButton();
        baseDialog.hidden(LunariansSettings::save);
        return baseDialog;
    }

    static {
        mine = new HashSet();
    }
}

