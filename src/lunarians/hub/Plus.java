/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.Events
 *  arc.files.Fi
 *  arc.func.Cons
 *  arc.graphics.Pixmap
 *  arc.graphics.PixmapIO
 *  arc.scene.Element
 *  arc.scene.style.Drawable
 *  arc.scene.style.TextureRegionDrawable
 *  arc.scene.ui.Image
 *  arc.scene.ui.Label
 *  arc.scene.ui.TextButton
 *  arc.scene.ui.TextField
 *  arc.scene.ui.layout.Table
 *  arc.struct.ObjectMap
 *  arc.struct.ObjectMap$Entry
 *  arc.util.Log
 *  arc.util.Scaling
 *  arc.util.Timer
 *  arc.util.Timer$Task
 *  arc.util.serialization.Base64Coder
 *  arc.util.serialization.Jval
 *  mindustry.Vars
 *  mindustry.game.EventType$Trigger
 *  mindustry.gen.Icon
 *  mindustry.ui.FileChooser$FileChooserParams
 *  mindustry.ui.Styles
 *  mindustry.ui.dialogs.BaseDialog
 */
package lunarians.hub;

import arc.Core;
import arc.Events;
import arc.files.Fi;
import arc.func.Cons;
import arc.graphics.Pixmap;
import arc.graphics.PixmapIO;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Image;
import arc.scene.ui.Label;
import arc.scene.ui.TextButton;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectMap;
import arc.util.Log;
import arc.util.Scaling;
import arc.util.Timer;
import arc.util.serialization.Base64Coder;
import arc.util.serialization.Jval;
import java.lang.reflect.Field;
import lunarians.LunariansSettings;
import lunarians.UiStyle;
import lunarians.hub.Access;
import lunarians.hub.AudioUi;
import lunarians.hub.Avatar;
import lunarians.hub.HubClient;
import lunarians.hub.HubText;
import lunarians.hub.Room;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.ui.FileChooser;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public final class Plus {
    private static final String K_G = "lunarians-hub-gchat-after";
    private static final String[] FLAGS = new String[]{"enableEnemyCoreAlert", "enableAutoFill", "enableFreeCam", "enableProductionAlert", "enableCamFollow", "enableLossWarn", "enableAutoUnit", "enableAutoCargo", "enableAutoContainer", "enableAutoDefence", "enableAutoShield", "enableAutoHeal", "enableAutoAttack", "enableAutoUnitHeal", "enableAutoMoon", "enableAutoConnectConveyor", "enableAutoDrill", "enableAutoDrillConveyor", "dockAuto", "pvpMode"};
    public static volatile int gnew = 0;
    private static int lastGnew = 0;
    private static boolean inited;
    private static boolean globalOpen;
    private static final ObjectMap<String, Boolean> orig;
    private static final ObjectMap<String, Boolean> forced;
    private static final ObjectMap<Integer, TextureRegionDrawable> imgCache;
    private static Field betaField;
    private static Field panelField;
    private static boolean betaFailed;

    static String tr(String string, String string2) {
        return AudioUi.tr(string, string2);
    }

    private static float width() {
        return Math.min(Core.scene.getWidth() - 40.0f, 520.0f);
    }

    public static void init() {
        if (inited || Vars.headless) {
            return;
        }
        inited = true;
        Room.init();
        try {
            Vars.ui.menufrag.addButton(Plus.tr("Chat global", "Global chat"), (Drawable)Icon.chat, Plus::openGlobal);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        Plus.hookBeta();
        Timer.schedule(Plus::sync, (float)8.0f, (float)30.0f);
        Timer.schedule(Plus::enforce, (float)3.0f, (float)2.0f);
    }

    public static void openChatId(String string, String string2) {
        Plus.openChat(string2, string);
    }

    public static void openGlobal() {
        if (!HubClient.decided() || !HubClient.consented()) {
            Access.showConsent(Plus::openGlobal);
            return;
        }
        Plus.openChat(null, null);
    }

    private static void hookBeta() {
        Events.run((Object)EventType.Trigger.update, () -> {
            if (betaFailed || Core.graphics.getFrameId() % 30L != 0L) {
                return;
            }
            try {
                Table table;
                Object object;
                if (betaField == null) {
                    betaField = Class.forName("lunarians.LunariansMod").getField("betaHud");
                }
                if ((object = betaField.get(null)) == null) {
                    return;
                }
                if (panelField == null) {
                    panelField = object.getClass().getDeclaredField("panel");
                    panelField.setAccessible(true);
                }
                if ((table = (Table)panelField.get(object)) == null) {
                    return;
                }
                String string = Plus.tr("Chat global", "Global chat") + (gnew > 0 && HubClient.consented() ? " (" + gnew + ")" : "");
                Element element = table.find("lunarians-hub-gchat");
                if (element instanceof TextButton) {
                    ((TextButton)element).setText(string);
                    return;
                }
                TextButton textButton = new TextButton(string, Styles.defaultt);
                textButton.name = "lunarians-hub-gchat";
                textButton.clicked(Plus::openGlobal);
                table.row();
                table.add((Element)textButton).growX().height(44.0f).pad(2.0f);
            }
            catch (Throwable throwable) {
                betaFailed = true;
                Log.warn((String)"[Lunarians Hub] Global chat hook off: @", (Object[])new Object[]{throwable.toString()});
            }
        });
    }

    private static Boolean getFlag(String string) {
        try {
            return LunariansSettings.class.getField(string).getBoolean(null);
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    private static void setFlag(String string, boolean bl) {
        try {
            LunariansSettings.class.getField(string).setBoolean(null, bl);
        }
        catch (Throwable throwable) {
            Log.debug((String)"[LunariansHub] flag @ indisponivel", (Object[])new Object[]{string});
        }
    }

    static void sync() {
        if (!HubClient.consented() || HubClient.myId().isEmpty() || HubClient.blocked) {
            return;
        }
        HubClient.call("GET", "/api/plus/sync?gafter=" + Core.settings.getInt(K_G, 0), null, (Cons<Jval>)((Cons)jval -> {
            try {
                Jval jval2 = jval.get("features");
                boolean bl = false;
                for (String string : FLAGS) {
                    Boolean bl2;
                    boolean bl3;
                    boolean bl4;
                    boolean bl5 = jval2.getBool(string, true);
                    boolean bl6 = bl4 = bl5 == (bl3 = jval2.getBool(string, false));
                    if (bl4) {
                        if (!forced.containsKey((Object)string) && (bl2 = Plus.getFlag(string)) != null) {
                            orig.put((Object)string, (Object)bl2);
                        }
                        forced.put((Object)string, (Object)bl5);
                        Plus.setFlag(string, bl5);
                        continue;
                    }
                    if (!forced.containsKey((Object)string)) continue;
                    bl2 = (Boolean)orig.get((Object)string);
                    if (bl2 != null) {
                        Plus.setFlag(string, bl2);
                        bl = true;
                    }
                    forced.remove((Object)string);
                    orig.remove((Object)string);
                }
                if (bl) {
                    LunariansSettings.save();
                }
                if ((gnew = jval.getInt("gnew", 0)) > lastGnew && !globalOpen) {
                    Vars.ui.showInfoToast(Plus.tr("Novas mensagens no chat global", "New global chat messages"), 3.0f);
                }
                lastGnew = gnew;
            }
            catch (Throwable throwable) {
                Log.debug((String)"[LunariansHub] sync: @", (Object[])new Object[]{throwable.toString()});
            }
        }), (Cons<String>)((Cons)string -> {}));
    }

    private static void enforce() {
        if (Plus.forced.size == 0) {
            return;
        }
        for (ObjectMap.Entry entry : forced.entries()) {
            Boolean bl = Plus.getFlag((String)entry.key);
            if (bl == null || bl.booleanValue() == ((Boolean)entry.value).booleanValue()) continue;
            Plus.setFlag((String)entry.key, (Boolean)entry.value);
        }
    }

    private static void loadInto(Image image, int n) {
        TextureRegionDrawable textureRegionDrawable = (TextureRegionDrawable)imgCache.get((Object)n);
        if (textureRegionDrawable != null) {
            image.setDrawable((Drawable)textureRegionDrawable);
            return;
        }
        HubClient.call("GET", "/api/chat/img?id=" + n, null, (Cons<Jval>)((Cons)jval -> {
            try {
                TextureRegionDrawable textureRegionDrawable = Avatar.fromPng(Base64Coder.decode((String)jval.getString("png", "")));
                imgCache.put((Object)n, (Object)textureRegionDrawable);
                image.setDrawable((Drawable)textureRegionDrawable);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }), (Cons<String>)((Cons)string -> {}));
    }

    private static void showFull(int n) {
        BaseDialog baseDialog = new BaseDialog("");
        UiStyle.register(baseDialog);
        Image image = new Image();
        image.setScaling(Scaling.fit);
        Plus.loadInto(image, n);
        baseDialog.cont.add((Element)image).size(Math.min(Core.scene.getWidth() - 40.0f, 480.0f), Core.scene.getHeight() * 0.6f).row();
        baseDialog.addCloseButton();
        baseDialog.show();
    }

    private static byte[] shrink(Pixmap pixmap) {
        int[] nArray;
        for (int n : nArray = new int[]{384, 256, 192, 128, 96}) {
            float f = Math.min(1.0f, (float)n / (float)Math.max(pixmap.width, pixmap.height));
            int n2 = Math.max(1, Math.round((float)pixmap.width * f));
            int n3 = Math.max(1, Math.round((float)pixmap.height * f));
            Pixmap pixmap2 = new Pixmap(n2, n3);
            pixmap2.draw(pixmap, 0, 0, pixmap.width, pixmap.height, 0, 0, n2, n3);
            byte[] byArray = PixmapIO.writePngBytes((Pixmap)pixmap2);
            pixmap2.dispose();
            if (byArray.length > 59392) continue;
            return byArray;
        }
        return null;
    }

    private static void pickImage(Cons<byte[]> cons) {
        new FileChooser.FileChooserParams().open(true).title(Plus.tr("Enviar imagem", "Send image")).extensions(new String[]{"png", "jpg", "jpeg"}).submit(object -> {
            Fi fi = (Fi)object;
            try {
                if (fi.length() > 8000000L) {
                    Vars.ui.showInfoToast(Plus.tr("Imagem muito grande", "Image too large"), 3.0f);
                    return;
                }
                Pixmap pixmap = new Pixmap(fi);
                byte[] byArray = Plus.shrink(pixmap);
                pixmap.dispose();
                if (byArray == null) {
                    Vars.ui.showInfoToast(Plus.tr("Nao foi possivel reduzir a imagem", "Could not shrink the image"), 3.0f);
                    return;
                }
                cons.get((Object)byArray);
            }
            catch (Throwable throwable) {
                Vars.ui.showInfoToast(Plus.tr("Imagem invalida", "Invalid image"), 3.0f);
            }
        });
    }

    private static Label wrap(String string) {
        Label label = new Label((CharSequence)string);
        label.setWrap(true);
        return label;
    }

    private static void addMsg(Table table, Jval jval, boolean bl, String string) {
        String string2 = jval.getBool("mine", false) ? Plus.tr("Voce", "You") : (bl ? jval.getString("name", "?") : string);
        String string3 = jval.getString("text", "");
        table.add((Element)Plus.wrap(string2 + ": " + string3)).width(Plus.width() - 20.0f).left().row();
        int n = jval.getInt("img", 0);
        if (n > 0) {
            Image image = new Image();
            image.setScaling(Scaling.fit);
            image.clicked(() -> Plus.showFull(n));
            Plus.loadInto(image, n);
            table.add((Element)image).size(160.0f).left().row();
        }
    }

    public static void openChat(String string, String string2) {
        boolean bl = string2 == null;
        BaseDialog baseDialog = new BaseDialog(bl ? Plus.tr("Chat global", "Global chat") : HubText.get("chat") + ": " + string);
        UiStyle.register(baseDialog);
        Table table = new Table();
        int[] nArray = new int[]{0};
        boolean[] blArray = new boolean[]{false};
        Runnable[] runnableArray = new Runnable[]{() -> {
            if (blArray[0]) {
                return;
            }
            blArray[0] = true;
            String string4 = bl ? "/api/gchat?after=" + nArray[0] : "/api/chat?with=" + string2 + "&after=" + nArray[0];
            HubClient.call("GET", string4, null, (Cons<Jval>)((Cons)jval -> {
                blArray[0] = false;
                for (Object e : jval.get("messages").asArray()) {
                    Jval jval2 = (Jval)e;
                    int n = jval2.getInt("id", 0);
                    if (n <= nArray[0]) continue;
                    nArray[0] = n;
                    Plus.addMsg(table, jval2, bl, string);
                    if (bl) {
                        Core.settings.put(K_G, (Object)Math.max(Core.settings.getInt(K_G, 0), n));
                        continue;
                    }
                    HubClient.setLastChat(Math.max(HubClient.lastChat(), n));
                }
                if (bl) {
                    gnew = 0;
                    lastGnew = 0;
                }
            }), (Cons<String>)((Cons)string -> {
                blArray[0] = false;
            }));
        }};
        baseDialog.cont.pane((Element)table).width(Plus.width()).height(Core.scene.getHeight() * 0.4f).row();
        TextField textField = new TextField();
        textField.setMessageText(HubText.get("msg.hint"));
        textField.setMaxLength(300);
        Cons cons = byArray -> {
            String string3 = textField.getText().trim();
            if (string3.isEmpty() && byArray == null) {
                return;
            }
            Jval jval2 = Jval.newObject();
            if (bl) {
                jval2.add("text", string3);
            } else {
                jval2.add("to", string2);
                jval2.add("text", string3);
            }
            if (byArray != null) {
                jval2.add("png", new String(Base64Coder.encode((byte[])byArray)));
            }
            textField.setText("");
            HubClient.call("POST", bl ? "/api/gchat/send" : "/api/chat/send", jval2, (Cons<Jval>)((Cons)jval -> runnableArray[0].run()), (Cons<String>)((Cons)string2 -> {
                textField.setText(string3);
                String string3 = "muted".equals(string2) ? Plus.tr("Voce foi silenciado pelo admin", "You were muted by the admin") : ("rate_limited".equals(string2) ? Plus.tr("Devagar! Aguarde um instante", "Slow down, wait a moment") : HubText.get("sent.fail"));
                Vars.ui.showInfoToast(string3, 2.5f);
            }));
        };
        baseDialog.cont.table(object -> {
            Table table = (Table)object;
            table.add((Element)textField).growX().height(46.0f);
            table.button("Img", () -> Plus.pickImage((Cons<byte[]>)((Cons)byArray -> cons.get(byArray)))).size(60.0f, 46.0f);
            table.button(HubText.get("send"), () -> cons.get(null)).size(90.0f, 46.0f);
        }).width(Plus.width());
        baseDialog.addCloseButton();
        baseDialog.shown(() -> {
            if (bl) {
                globalOpen = true;
            }
            runnableArray[0].run();
        });
        Timer.Task task = Timer.schedule(() -> {
            if (baseDialog.isShown()) {
                runnableArray[0].run();
            }
        }, (float)4.0f, (float)4.0f);
        baseDialog.hidden(() -> {
            task.cancel();
            if (bl) {
                globalOpen = false;
            }
        });
        baseDialog.show();
    }

    static {
        orig = new ObjectMap();
        forced = new ObjectMap();
        imgCache = new ObjectMap();
    }
}

