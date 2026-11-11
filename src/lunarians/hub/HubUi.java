/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.func.Cons
 *  arc.scene.Element
 *  arc.scene.style.Drawable
 *  arc.scene.ui.Label
 *  arc.scene.ui.TextField
 *  arc.scene.ui.layout.Table
 *  arc.struct.Seq
 *  arc.util.Timer
 *  arc.util.Timer$Task
 *  arc.util.serialization.Jval
 *  arc.util.serialization.Jval$JsonArray
 *  mindustry.Vars
 *  mindustry.gen.Icon
 *  mindustry.ui.Styles
 *  mindustry.ui.dialogs.BaseDialog
 */
package lunarians.hub;

import arc.Core;
import arc.func.Cons;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.ui.Label;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Timer;
import arc.util.serialization.Jval;
import lunarians.UiStyle;
import lunarians.hub.Access;
import lunarians.hub.Avatar;
import lunarians.hub.HubClient;
import lunarians.hub.HubText;
import lunarians.hub.Plus;
import lunarians.hub.Skins;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public class HubUi {
    private static BaseDialog hub;
    private static Table body;
    private static int tab;

    private static float width() {
        return Math.min(Core.scene.getWidth() - 40.0f, 520.0f);
    }

    public static void showConsent(Runnable runnable) {
        BaseDialog baseDialog = new BaseDialog(HubText.get("consent.title"));
        UiStyle.register(baseDialog);
        baseDialog.cont.pane(table -> {
            Label label = new Label((CharSequence)HubText.get("consent.body"));
            label.setWrap(true);
            table.add((Element)label).width(HubUi.width()).left().pad(8.0f);
        }).maxHeight(Core.scene.getHeight() * 0.6f).row();
        baseDialog.buttons.defaults().size(Math.min(HubUi.width() / 2.0f - 6.0f, 220.0f), 56.0f).pad(4.0f);
        baseDialog.buttons.button(HubText.get("decline"), (Drawable)Icon.cancel, () -> {
            HubClient.setConsent(false);
            baseDialog.hide();
        });
        baseDialog.buttons.button(HubText.get("accept"), (Drawable)Icon.ok, () -> {
            HubClient.setConsent(true);
            baseDialog.hide();
            HubClient.ensureRegistered(() -> {
                HubClient.heartbeat(null);
                if (runnable != null) {
                    runnable.run();
                }
            });
        });
        baseDialog.closeOnBack(() -> {
            if (!HubClient.decided()) {
                HubClient.setConsent(false);
            }
        });
        baseDialog.show();
    }

    public static void showGlobal(String string) {
        BaseDialog baseDialog = new BaseDialog(HubText.get("global.title"));
        UiStyle.register(baseDialog);
        baseDialog.cont.pane(table -> {
            Label label = new Label((CharSequence)string);
            label.setWrap(true);
            table.add((Element)label).width(HubUi.width()).pad(8.0f);
        }).maxHeight(Core.scene.getHeight() * 0.6f);
        baseDialog.addCloseButton();
        baseDialog.show();
    }

    public static void showBlocked() {
        BaseDialog baseDialog = new BaseDialog(HubText.get("title"));
        UiStyle.register(baseDialog);
        Label label = new Label((CharSequence)HubText.get("blocked"));
        label.setWrap(true);
        baseDialog.cont.add((Element)label).width(HubUi.width()).pad(8.0f);
        baseDialog.addCloseButton();
        baseDialog.show();
    }

    public static void open() {
        if (!HubClient.decided() || !HubClient.consented()) {
            Access.showConsent(HubUi::open);
            return;
        }
        if (HubClient.blocked) {
            HubUi.showBlocked();
            return;
        }
        if (hub == null) {
            hub = new BaseDialog(HubText.get("title"));
            UiStyle.register(hub);
            hub.addCloseButton();
            hub.shown(HubUi::rebuild);
        }
        hub.show();
    }

    private static void rebuild() {
        HubUi.hub.cont.clear();
        HubUi.hub.cont.table(table -> {
            table.defaults().height(48.0f).growX().pad(2.0f);
            table.button(HubText.get("tab.friends"), Styles.togglet, () -> {
                tab = 0;
                HubUi.rebuild();
            }).checked(tab == 0);
            table.button(HubText.get("tab.skins"), Styles.togglet, () -> {
                tab = 2;
                HubUi.rebuild();
            }).checked(tab == 2);
            table.button(HubText.get("tab.settings"), Styles.togglet, () -> {
                tab = 1;
                HubUi.rebuild();
            }).checked(tab == 1);
        }).width(HubUi.width()).row();
        body = new Table();
        HubUi.hub.cont.pane((Element)body).width(HubUi.width()).maxHeight(Core.scene.getHeight() * 0.65f).row();
        if (tab == 0) {
            HubUi.buildFriends();
        } else if (tab == 2) {
            HubUi.buildSkins();
        } else {
            HubUi.buildSettings();
        }
    }

    private static Label wrapLabel(String string) {
        Label label = new Label((CharSequence)string);
        label.setWrap(true);
        return label;
    }

    private static void buildSkins() {
        Skins.buildTab(body, HubUi::buildSkins, HubUi.width());
    }

    private static void buildFriends() {
        body.clear();
        body.add((Element)HubUi.wrapLabel("...")).row();
        HubClient.call("GET", "/api/friends", null, (Cons<Jval>)((Cons)HubUi::renderFriendLists), (Cons<String>)((Cons)string -> {}));
    }

    private static void renderFriendLists(Jval jval2) {
        body.clear();
        UiStyle.section(body, HubText.get("myid"));
        body.add((Element)HubUi.wrapLabel(HubClient.myId())).width(HubUi.width()).left().row();
        body.button(HubText.get("copy"), () -> {
            Core.app.setClipboardText(HubClient.myId());
            Vars.ui.showInfoToast(HubText.get("copied"), 1.5f);
        }).size(140.0f, 40.0f).left().padBottom(6.0f).row();
        body.table(table2 -> {
            table2.add((Element)Avatar.mine(48.0f)).size(48.0f).padRight(8.0f);
            table2.table(table -> {
                table.defaults().height(40.0f).growX().pad(1.0f);
                table.button(HubText.get("avatar.pick"), () -> Avatar.pick(HubUi::buildFriends)).row();
                table.button(HubText.get("avatar.upload"), () -> Avatar.pickImage(HubUi::buildFriends)).row();
            }).growX();
        }).width(HubUi.width()).padBottom(6.0f).row();
        TextField textField = new TextField();
        textField.setMessageText(HubText.get("addfriend"));
        textField.setMaxLength(32);
        body.table(table -> {
            table.add((Element)textField).growX().height(44.0f);
            table.button(HubText.get("send"), () -> {
                String string2 = textField.getText().trim().toLowerCase();
                if (string2.length() != 32) {
                    return;
                }
                Jval jval2 = Jval.newObject();
                jval2.add("to", string2);
                HubClient.call("POST", "/api/friends/request", jval2, (Cons<Jval>)((Cons)jval -> HubUi.buildFriends()), (Cons<String>)((Cons)string -> Vars.ui.showInfoToast(HubText.get("sent.fail"), 2.0f)));
            }).size(90.0f, 44.0f);
        }).width(HubUi.width()).row();
        HubUi.section(HubText.get("requests"), (Seq<Jval>)jval2.get("incoming").asArray(), (table, jval) -> {
            table.button(HubText.get("accept.req"), () -> HubUi.act("/api/friends/accept", "from", jval.getString("id", ""))).size(90.0f, 38.0f);
            table.button((Drawable)Icon.cancel, Styles.cleari, () -> HubUi.act("/api/friends/remove", "id", jval.getString("id", ""))).size(38.0f);
        });
        HubUi.section(HubText.get("pending"), (Seq<Jval>)jval2.get("outgoing").asArray(), (table, jval) -> table.button((Drawable)Icon.cancel, Styles.cleari, () -> HubUi.act("/api/friends/remove", "id", jval.getString("id", ""))).size(38.0f));
        HubUi.section(HubText.get("friends"), (Seq<Jval>)jval2.get("friends").asArray(), (table, jval) -> {
            String string = jval.getString("id", "");
            table.button((Drawable)Icon.chat, Styles.cleari, () -> Plus.openChatId(string, jval.getString("name", "?"))).size(38.0f);
            table.button((Drawable)Icon.warning, Styles.cleari, () -> HubUi.report(string)).size(38.0f);
            table.button((Drawable)Icon.trash, Styles.cleari, () -> HubUi.act("/api/friends/remove", "id", string)).size(38.0f);
        });
    }

    private static void section(String string, Seq<Jval> seq, RowFill rowFill) {
        if (seq.size == 0) {
            return;
        }
        UiStyle.section(body, string);
        for (Jval jval : seq) {
            body.table(Styles.grayPanel, table -> {
                Object object;
                boolean bl = jval.has("online");
                String string = jval.getString("name", "?");
                if (bl) {
                    string = string + "  [" + (jval.getBool("online", false) ? HubText.get("online") : HubText.get("offline")) + "]";
                    object = jval.getString("server_name", "");
                    if (!((String)object).isEmpty() && jval.getBool("online", false)) {
                        string = string + "\n" + (String)object + ":" + jval.getInt("server_port", 0);
                    }
                }
                table.add((Element)Avatar.imageFor(jval, 40.0f)).size(40.0f).pad(4.0f);
                object = HubUi.wrapLabel(string);
                object.setAlignment(8);
                table.add((Element)object).growX().pad(6.0f);
                rowFill.fill((Table)table, jval);
            }).width(HubUi.width() - 10.0f).pad(2.0f).row();
        }
    }

    private static void act(String string2, String string3, String string4) {
        Jval jval2 = Jval.newObject();
        jval2.add(string3, string4);
        HubClient.call("POST", string2, jval2, (Cons<Jval>)((Cons)jval -> HubUi.buildFriends()), (Cons<String>)((Cons)string -> Vars.ui.showInfoToast(HubText.get("sent.fail"), 2.0f)));
    }

    private static void report(String string) {
        Vars.ui.showTextInput(HubText.get("report"), "", 200, "", string3 -> {
            Jval jval2 = Jval.newObject();
            jval2.add("target", string);
            jval2.add("reason", string3);
            HubClient.call("POST", "/api/report", jval2, (Cons<Jval>)((Cons)jval -> Vars.ui.showInfoToast(HubText.get("reported"), 2.0f)), (Cons<String>)((Cons)string -> Vars.ui.showInfoToast(HubText.get("sent.fail"), 2.0f)));
        });
    }

    static void openChat(String string, String string2) {
        Runnable[] runnableArray;
        BaseDialog baseDialog = new BaseDialog(HubText.get("chat") + ": " + string2);
        UiStyle.register(baseDialog);
        Table table2 = new Table();
        int[] nArray = new int[]{0};
        boolean[] blArray = new boolean[]{false};
        boolean[] blArray2 = new boolean[]{false};
        boolean[] blArray3 = new boolean[]{false};
        runnableArray = new Runnable[]{() -> {
            if (blArray[0]) {
                blArray2[0] = true;
                return;
            }
            blArray[0] = true;
            HubClient.call("GET", "/api/chat?with=" + string + "&after=" + nArray[0], null, (Cons<Jval>)((Cons)jval -> {
                blArray[0] = false;
                for (Jval jval2 : jval.get("messages").asArray()) {
                    int n = jval2.getInt("id", 0);
                    if (n <= nArray[0]) continue;
                    nArray[0] = n;
                    Label label = HubUi.wrapLabel((jval2.getBool("mine", false) ? "> " : string2 + ": ") + jval2.getString("text", ""));
                    table2.add((Element)label).width(HubUi.width() - 20.0f).left().row();
                    HubClient.setLastChat(Math.max(HubClient.lastChat(), n));
                }
                if (blArray2[0]) {
                    blArray2[0] = false;
                    runnableArray[0].run();
                }
            }), (Cons<String>)((Cons)string -> {
                blArray[0] = false;
                blArray2[0] = false;
            }));
        }};
        baseDialog.cont.pane((Element)table2).width(HubUi.width()).height(Core.scene.getHeight() * 0.4f).row();
        TextField textField = new TextField();
        textField.setMessageText(HubText.get("msg.hint"));
        textField.setMaxLength(300);
        baseDialog.cont.table(table -> {
            table.add((Element)textField).growX().height(46.0f);
            table.button(HubText.get("send"), () -> {
                String string3 = textField.getText().trim();
                if (string3.isEmpty() || blArray3[0]) {
                    return;
                }
                blArray[0] = true;
                textField.setText("");
                Jval jval2 = Jval.newObject();
                jval2.add("to", string);
                jval2.add("text", string3);
                HubClient.call("POST", "/api/chat/send", jval2, (Cons<Jval>)((Cons)jval -> {
                    blArray[0] = false;
                    runnableArray[0].run();
                }), (Cons<String>)((Cons)string2 -> {
                    blArray[0] = false;
                    textField.setText(string3);
                    Vars.ui.showInfoToast(HubText.get("sent.fail"), 2.0f);
                }));
            }).size(90.0f, 46.0f);
        }).width(HubUi.width());
        baseDialog.addCloseButton();
        baseDialog.shown(() -> runnableArray[0].run());
        Timer.Task task = Timer.schedule(() -> {
            if (baseDialog.isShown()) {
                runnableArray[0].run();
            }
        }, (float)4.0f, (float)4.0f);
        baseDialog.hidden(() -> ((Timer.Task)task).cancel());
        baseDialog.show();
    }

    public static void openChats() {
        if (!HubClient.decided() || !HubClient.consented()) {
            Access.showConsent(HubUi::openChats);
            return;
        }
        if (HubClient.blocked) {
            HubUi.showBlocked();
            return;
        }
        BaseDialog baseDialog = new BaseDialog(HubText.get("chats.title"));
        UiStyle.register(baseDialog);
        Table table = new Table();
        baseDialog.cont.pane((Element)table).width(HubUi.width()).maxHeight(Core.scene.getHeight() * 0.65f).row();
        table.add((Element)HubUi.wrapLabel("...")).row();
        HubClient.call("GET", "/api/friends", null, (Cons<Jval>)((Cons)jval3 -> {
            table.clear();
            Jval.JsonArray jsonArray = jval3.get("friends").asArray();
            if (jsonArray.size == 0) {
                table.add((Element)HubUi.wrapLabel(HubText.get("chats.empty"))).width(HubUi.width() - 20.0f).pad(8.0f).row();
                return;
            }
            jsonArray.sort((jval, jval2) -> Boolean.compare(jval2.getBool("online", false), jval.getBool("online", false)));
            for (Jval jval4 : jsonArray) {
                String string = jval4.getString("id", "");
                String string2 = jval4.getString("name", "?");
                boolean bl = jval4.getBool("online", false);
                table.button(button -> {
                    button.add((Element)Avatar.imageFor(jval4, 40.0f)).size(40.0f).pad(4.0f);
                    Label label = HubUi.wrapLabel(string2 + "  [" + (bl ? HubText.get("online") : HubText.get("offline")) + "]");
                    label.setAlignment(8);
                    button.add((Element)label).growX().pad(4.0f);
                    button.image((Drawable)Icon.chat).size(28.0f).pad(4.0f);
                }, () -> Plus.openChatId(string, string2)).width(HubUi.width() - 10.0f).minHeight(52.0f).pad(2.0f).row();
            }
        }), (Cons<String>)((Cons)string -> {
            table.clear();
            table.add((Element)HubUi.wrapLabel(HubText.get("sent.fail"))).row();
        }));
        baseDialog.cont.button(HubText.get("open.hub"), (Drawable)Icon.players, () -> {
            baseDialog.hide();
            HubUi.open();
        }).size(220.0f, 46.0f).padTop(8.0f);
        baseDialog.addCloseButton();
        baseDialog.show();
    }

    private static void buildSettings() {
        body.clear();
        body.add((Element)HubUi.wrapLabel(HubText.get("consent.body"))).width(HubUi.width() - 10.0f).left().row();
        body.button(HubText.get("leave"), (Drawable)Icon.trash, () -> Vars.ui.showConfirm(HubText.get("title"), HubText.get("leave.confirm"), () -> HubClient.leaveAndDelete((Cons<Boolean>)((Cons)bl -> {
            Vars.ui.showInfoToast(HubText.get(bl != false ? "leave.done" : "leave.offline"), 3.0f);
            if (bl.booleanValue() && hub != null) {
                hub.hide();
            }
        })))).size(Math.min(HubUi.width(), 420.0f), 56.0f).padTop(12.0f);
    }

    static {
        tab = 0;
    }

    private static interface RowFill {
        public void fill(Table var1, Jval var2);
    }
}

