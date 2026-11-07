package lunarians.hub;

import arc.Core;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.ui.Button;
import arc.scene.ui.Label;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Timer;
import arc.util.serialization.Jval;
import java.util.Iterator;
import java.util.Objects;
import lunarians.UiStyle;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public class HubUi {
  private static BaseDialog hub;
  
  private static Table body;
  
  private static int tab = 0;
  
  private static float width() {
    return Math.min(Core.scene.getWidth() - 40.0F, 520.0F);
  }
  
  public static void showConsent(Runnable paramRunnable) {
    BaseDialog baseDialog = new BaseDialog(HubText.get("consent.title"));
    UiStyle.register(baseDialog);
    baseDialog.cont.pane(paramTable -> {
          Label label = new Label(HubText.get("consent.body"));
          label.setWrap(true);
          paramTable.add((Element)label).width(width()).left().pad(8.0F);
        }).maxHeight(Core.scene.getHeight() * 0.6F).row();
    baseDialog.buttons.defaults().size(Math.min(width() / 2.0F - 6.0F, 220.0F), 56.0F).pad(4.0F);
    baseDialog.buttons.button(HubText.get("decline"), (Drawable)Icon.cancel, () -> {
          HubClient.setConsent(false);
          paramBaseDialog.hide();
        });
    baseDialog.buttons.button(HubText.get("accept"), (Drawable)Icon.ok, () -> {
          HubClient.setConsent(true);
          paramBaseDialog.hide();
          HubClient.ensureRegistered(());
        });
    baseDialog.closeOnBack(() -> {
          if (!HubClient.decided())
            HubClient.setConsent(false); 
        });
    baseDialog.show();
  }
  
  public static void showGlobal(String paramString) {
    BaseDialog baseDialog = new BaseDialog(HubText.get("global.title"));
    UiStyle.register(baseDialog);
    baseDialog.cont.pane(paramTable -> {
          Label label = new Label(paramString);
          label.setWrap(true);
          paramTable.add((Element)label).width(width()).pad(8.0F);
        }).maxHeight(Core.scene.getHeight() * 0.6F);
    baseDialog.addCloseButton();
    baseDialog.show();
  }
  
  public static void showBlocked() {
    BaseDialog baseDialog = new BaseDialog(HubText.get("title"));
    UiStyle.register(baseDialog);
    Label label = new Label(HubText.get("blocked"));
    label.setWrap(true);
    baseDialog.cont.add((Element)label).width(width()).pad(8.0F);
    baseDialog.addCloseButton();
    baseDialog.show();
  }
  
  public static void open() {
    if (!HubClient.decided() || !HubClient.consented()) {
      Access.showConsent(HubUi::open);
      return;
    } 
    if (HubClient.blocked) {
      showBlocked();
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
    hub.cont.clear();
    hub.cont.table(paramTable -> {
          paramTable.defaults().height(48.0F).growX().pad(2.0F);
          paramTable.button(HubText.get("tab.friends"), Styles.togglet, ()).checked((tab == 0));
          paramTable.button(HubText.get("tab.skins"), Styles.togglet, ()).checked((tab == 2));
          paramTable.button(HubText.get("tab.settings"), Styles.togglet, ()).checked((tab == 1));
        }).width(width()).row();
    body = new Table();
    hub.cont.pane((Element)body).width(width()).maxHeight(Core.scene.getHeight() * 0.65F).row();
    if (tab == 0) {
      buildFriends();
    } else if (tab == 2) {
      buildSkins();
    } else {
      buildSettings();
    } 
  }
  
  private static Label wrapLabel(String paramString) {
    Label label = new Label(paramString);
    label.setWrap(true);
    return label;
  }
  
  private static void buildSkins() {
    Skins.buildTab(body, HubUi::buildSkins, width());
  }
  
  private static void buildFriends() {
    body.clear();
    body.add((Element)wrapLabel("...")).row();
    HubClient.call("GET", "/api/friends", null, HubUi::renderFriendLists, paramString -> {
        
        });
  }
  
  private static void renderFriendLists(Jval paramJval) {
    body.clear();
    UiStyle.section(body, HubText.get("myid"));
    body.add((Element)wrapLabel(HubClient.myId())).width(width()).left().row();
    body.button(HubText.get("copy"), () -> {
          Core.app.setClipboardText(HubClient.myId());
          Vars.ui.showInfoToast(HubText.get("copied"), 1.5F);
        }).size(140.0F, 40.0F).left().padBottom(6.0F).row();
    body.table(paramTable -> {
          paramTable.add((Element)Avatar.mine(48.0F)).size(48.0F).padRight(8.0F);
          paramTable.table(()).growX();
        }).width(width()).padBottom(6.0F).row();
    TextField textField = new TextField();
    textField.setMessageText(HubText.get("addfriend"));
    textField.setMaxLength(32);
    body.table(paramTable -> {
          paramTable.add((Element)paramTextField).growX().height(44.0F);
          paramTable.button(HubText.get("send"), ()).size(90.0F, 44.0F);
        }).width(width()).row();
    section(HubText.get("requests"), (Seq<Jval>)paramJval.get("incoming").asArray(), (paramTable, paramJval) -> {
          paramTable.button(HubText.get("accept.req"), ()).size(90.0F, 38.0F);
          paramTable.button((Drawable)Icon.cancel, Styles.cleari, ()).size(38.0F);
        });
    section(HubText.get("pending"), (Seq<Jval>)paramJval.get("outgoing").asArray(), (paramTable, paramJval) -> paramTable.button((Drawable)Icon.cancel, Styles.cleari, ()).size(38.0F));
    section(HubText.get("friends"), (Seq<Jval>)paramJval.get("friends").asArray(), (paramTable, paramJval) -> {
          String str = paramJval.getString("id", "");
          paramTable.button((Drawable)Icon.chat, Styles.cleari, ()).size(38.0F);
          paramTable.button((Drawable)Icon.warning, Styles.cleari, ()).size(38.0F);
          paramTable.button((Drawable)Icon.trash, Styles.cleari, ()).size(38.0F);
        });
  }
  
  private static void section(String paramString, Seq<Jval> paramSeq, RowFill paramRowFill) {
    if (paramSeq.size == 0)
      return; 
    UiStyle.section(body, paramString);
    for (Iterator<Jval> iterator = paramSeq.iterator(); iterator.hasNext(); ) {
      Jval jval = iterator.next();
      body.table(Styles.grayPanel, paramTable -> {
            boolean bool = paramJval.has("online");
            String str = paramJval.getString("name", "?");
            if (bool) {
              str = str + "  [" + (paramJval.getBool("online", false) ? HubText.get("online") : HubText.get("offline")) + "]";
              String str1 = paramJval.getString("server_name", "");
              if (!str1.isEmpty() && paramJval.getBool("online", false))
                str = str + "\n" + str1 + ":" + paramJval.getInt("server_port", 0); 
            } 
            paramTable.add((Element)Avatar.imageFor(paramJval, 40.0F)).size(40.0F).pad(4.0F);
            Label label = wrapLabel(str);
            label.setAlignment(8);
            paramTable.add((Element)label).growX().pad(6.0F);
            paramRowFill.fill(paramTable, paramJval);
          }).width(width() - 10.0F).pad(2.0F).row();
    } 
  }
  
  private static void act(String paramString1, String paramString2, String paramString3) {
    Jval jval = Jval.newObject();
    jval.add(paramString2, paramString3);
    HubClient.call("POST", paramString1, jval, paramJval -> buildFriends(), paramString -> Vars.ui.showInfoToast(HubText.get("sent.fail"), 2.0F));
  }
  
  private static void report(String paramString) {
    Vars.ui.showTextInput(HubText.get("report"), "", 200, "", paramString2 -> {
          Jval jval = Jval.newObject();
          jval.add("target", paramString1);
          jval.add("reason", paramString2);
          HubClient.call("POST", "/api/report", jval, (), ());
        });
  }
  
  static void openChat(String paramString1, String paramString2) {
    BaseDialog baseDialog = new BaseDialog(HubText.get("chat") + ": " + paramString2);
    UiStyle.register(baseDialog);
    Table table = new Table();
    int[] arrayOfInt = { 0 };
    boolean[] arrayOfBoolean1 = { false }, arrayOfBoolean2 = { false }, arrayOfBoolean3 = { false };
    Runnable[] arrayOfRunnable = new Runnable[1];
    arrayOfRunnable[0] = (() -> {
        if (paramArrayOfboolean1[0]) {
          paramArrayOfboolean2[0] = true;
          return;
        } 
        paramArrayOfboolean1[0] = true;
        HubClient.call("GET", "/api/chat?with=" + paramString1 + "&after=" + paramArrayOfint[0], null, (), ());
      });
    baseDialog.cont.pane((Element)table).width(width()).height(Core.scene.getHeight() * 0.4F).row();
    TextField textField = new TextField();
    textField.setMessageText(HubText.get("msg.hint"));
    textField.setMaxLength(300);
    baseDialog.cont.table(paramTable -> {
          paramTable.add((Element)paramTextField).growX().height(46.0F);
          paramTable.button(HubText.get("send"), ()).size(90.0F, 46.0F);
        }).width(width());
    baseDialog.addCloseButton();
    baseDialog.shown(() -> paramArrayOfRunnable[0].run());
    Timer.Task task = Timer.schedule(() -> {
          if (paramBaseDialog.isShown())
            paramArrayOfRunnable[0].run(); 
        }4.0F, 4.0F);
    Objects.requireNonNull(task);
    baseDialog.hidden(task::cancel);
    baseDialog.show();
  }
  
  public static void openChats() {
    if (!HubClient.decided() || !HubClient.consented()) {
      Access.showConsent(HubUi::openChats);
      return;
    } 
    if (HubClient.blocked) {
      showBlocked();
      return;
    } 
    BaseDialog baseDialog = new BaseDialog(HubText.get("chats.title"));
    UiStyle.register(baseDialog);
    Table table = new Table();
    baseDialog.cont.pane((Element)table).width(width()).maxHeight(Core.scene.getHeight() * 0.65F).row();
    table.add((Element)wrapLabel("...")).row();
    HubClient.call("GET", "/api/friends", null, paramJval -> {
          paramTable.clear();
          Jval.JsonArray jsonArray = paramJval.get("friends").asArray();
          if (((Seq)jsonArray).size == 0) {
            paramTable.add((Element)wrapLabel(HubText.get("chats.empty"))).width(width() - 20.0F).pad(8.0F).row();
            return;
          } 
          jsonArray.sort(());
          for (Jval jval : jsonArray) {
            String str1 = jval.getString("id", "");
            String str2 = jval.getString("name", "?");
            boolean bool = jval.getBool("online", false);
            paramTable.button((), ()).width(width() - 10.0F).minHeight(52.0F).pad(2.0F).row();
          } 
        }paramString -> {
          paramTable.clear();
          paramTable.add((Element)wrapLabel(HubText.get("sent.fail"))).row();
        });
    baseDialog.cont.button(HubText.get("open.hub"), (Drawable)Icon.players, () -> {
          paramBaseDialog.hide();
          open();
        }).size(220.0F, 46.0F).padTop(8.0F);
    baseDialog.addCloseButton();
    baseDialog.show();
  }
  
  private static void buildSettings() {
    body.clear();
    body.add((Element)wrapLabel(HubText.get("consent.body"))).width(width() - 10.0F).left().row();
    body.button(HubText.get("leave"), (Drawable)Icon.trash, () -> Vars.ui.showConfirm(HubText.get("title"), HubText.get("leave.confirm"), ()))



      
      .size(Math.min(width(), 420.0F), 56.0F).padTop(12.0F);
  }
  
  private static interface RowFill {
    void fill(Table param1Table, Jval param1Jval);
  }
}
