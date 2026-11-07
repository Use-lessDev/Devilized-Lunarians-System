package lunarians.hub;

import arc.Application;
import arc.Core;
import arc.scene.Element;
import arc.scene.Scene;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Button;
import arc.scene.ui.Image;
import arc.scene.ui.ImageButton;
import arc.scene.ui.Label;
import arc.scene.ui.TextButton;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Timer;
import arc.util.serialization.Jval;
import java.util.Iterator;
import java.util.Objects;
import lunarians.UiStyle;
import mindustry.Vars;
import mindustry.core.UI;
import mindustry.gen.Icon;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public class HubUi {
  private static Table body;
  
  private static BaseDialog hub;
  
  private static int tab;
  
  private static void act(String paramString1, String paramString2, String paramString3) {
    Jval jval = Jval.newObject();
    jval.add(paramString2, paramString3);
    HubUi$1 hubUi$1 = new HubUi$1();
    this();
    HubUi$2 hubUi$2 = new HubUi$2();
    this();
    HubClient.call("POST", paramString1, jval, hubUi$1, hubUi$2);
  }
  
  private static void buildFriends() {
    body.clear();
    Table table = body;
    Label label = wrapLabel("...");
    table.add((Element)label).row();
    HubUi$12 hubUi$12 = new HubUi$12();
    this();
    HubUi$13 hubUi$13 = new HubUi$13();
    this();
    HubClient.call("GET", "/api/friends", null, hubUi$12, hubUi$13);
  }
  
  private static void buildSettings() {
    body.clear();
    Table table2 = body;
    Label label = wrapLabel(HubText.get("consent.body"));
    Cell cell2 = table2.add((Element)label);
    float f = width() - 10.0F;
    cell2.width(f).left().row();
    Table table1 = body;
    String str = HubText.get("leave");
    TextureRegionDrawable textureRegionDrawable = Icon.trash;
    HubUi$25 hubUi$25 = new HubUi$25();
    this();
    Cell cell1 = table1.button(str, (Drawable)textureRegionDrawable, hubUi$25);
    f = Math.min(width(), 420.0F);
    cell1.size(f, 56.0F).padTop(12.0F);
  }
  
  private static void buildSkins() {
    Table table = body;
    HubUi$11 hubUi$11 = new HubUi$11();
    this();
    float f = width();
    Skins.buildTab(table, hubUi$11, f);
  }
  
  public static void open() {
    boolean bool = HubClient.decided();
    if (bool) {
      bool = HubClient.consented();
      if (bool) {
        bool = HubClient.blocked;
        if (bool) {
          showBlocked();
          return;
        } 
        BaseDialog baseDialog = hub;
        if (baseDialog == null) {
          baseDialog = new BaseDialog();
          String str = HubText.get("title");
          this(str);
          hub = baseDialog;
          UiStyle.register(baseDialog);
          hub.addCloseButton();
          baseDialog = hub;
          HubUi$4 hubUi$4 = new HubUi$4();
          this();
          baseDialog.shown(hubUi$4);
        } 
        hub.show();
        return;
      } 
    } 
    HubUi$3 hubUi$3 = new HubUi$3();
    this();
    Access.showConsent(hubUi$3);
  }
  
  static void openChat(String paramString1, String paramString2) {
    BaseDialog baseDialog = new BaseDialog();
    StringBuilder stringBuilder = new StringBuilder();
    this();
    String str2 = HubText.get("chat");
    stringBuilder.append(str2);
    stringBuilder.append(": ");
    stringBuilder.append(paramString2);
    String str1 = stringBuilder.toString();
    this(str1);
    UiStyle.register(baseDialog);
    Table table2 = new Table();
    this();
    int[] arrayOfInt = { 0 };
    boolean bool = true;
    boolean[] arrayOfBoolean1 = new boolean[bool];
    arrayOfBoolean1[0] = false;
    boolean[] arrayOfBoolean2 = new boolean[bool];
    arrayOfBoolean2[0] = false;
    boolean[] arrayOfBoolean3 = new boolean[bool];
    arrayOfBoolean3[0] = false;
    Runnable[] arrayOfRunnable = new Runnable[bool];
    HubUi$52 hubUi$52 = new HubUi$52();
    String str3 = paramString1;
    this(arrayOfBoolean1, arrayOfBoolean2, paramString1, arrayOfInt, paramString2, table2, arrayOfRunnable);
    arrayOfRunnable[0] = hubUi$52;
    Cell cell2 = baseDialog.cont.pane((Element)table2);
    float f = width();
    cell2 = cell2.width(f);
    f = Core.scene.getHeight() * 0.4F;
    cell2.height(f).row();
    TextField textField = new TextField();
    this();
    paramString2 = HubText.get("msg.hint");
    textField.setMessageText(paramString2);
    textField.setMaxLength(300);
    Table table1 = baseDialog.cont;
    HubUi$53 hubUi$53 = new HubUi$53();
    this(textField, arrayOfBoolean3, str3, arrayOfRunnable);
    Cell cell1 = table1.table(hubUi$53);
    f = width();
    cell1.width(f);
    baseDialog.addCloseButton();
    HubUi$54 hubUi$54 = new HubUi$54();
    this(arrayOfRunnable);
    baseDialog.shown(hubUi$54);
    HubUi$55 hubUi$55 = new HubUi$55();
    this(baseDialog, arrayOfRunnable);
    f = 4.0F;
    Timer.Task task = Timer.schedule(hubUi$55, f, f);
    Objects.requireNonNull(task);
    HubUi$56 hubUi$56 = new HubUi$56();
    this(task);
    baseDialog.hidden(hubUi$56);
    baseDialog.show();
  }
  
  public static void openChats() {
    boolean bool = HubClient.decided();
    if (bool) {
      bool = HubClient.consented();
      if (bool) {
        bool = HubClient.blocked;
        if (bool) {
          showBlocked();
          return;
        } 
        BaseDialog baseDialog = new BaseDialog();
        String str1 = HubText.get("chats.title");
        this(str1);
        UiStyle.register(baseDialog);
        Table table = new Table();
        this();
        Cell cell = baseDialog.cont.pane((Element)table);
        float f = width();
        cell = cell.width(f);
        f = Core.scene.getHeight() * 0.65F;
        cell.maxHeight(f).row();
        Label label = wrapLabel("...");
        table.add((Element)label).row();
        HubUi$42 hubUi$42 = new HubUi$42();
        this(table);
        HubUi$43 hubUi$43 = new HubUi$43();
        this(table);
        HubClient.call("GET", "/api/friends", null, hubUi$42, hubUi$43);
        table = baseDialog.cont;
        String str2 = HubText.get("open.hub");
        TextureRegionDrawable textureRegionDrawable = Icon.players;
        HubUi$44 hubUi$44 = new HubUi$44();
        this(baseDialog);
        table.button(str2, (Drawable)textureRegionDrawable, hubUi$44).size(220.0F, 46.0F).padTop(8.0F);
        baseDialog.addCloseButton();
        baseDialog.show();
        return;
      } 
    } 
    HubUi$41 hubUi$41 = new HubUi$41();
    this();
    Access.showConsent(hubUi$41);
  }
  
  private static void rebuild() {
    hub.cont.clear();
    Table table2 = hub.cont;
    HubUi$51 hubUi$51 = new HubUi$51();
    this();
    Cell cell2 = table2.table(hubUi$51);
    float f1 = width();
    cell2.width(f1).row();
    Table table1 = new Table();
    this();
    body = table1;
    table1 = hub.cont;
    Table table3 = body;
    Cell cell1 = table1.pane((Element)table3);
    f1 = width();
    cell1 = cell1.width(f1);
    Scene scene = Core.scene;
    f1 = scene.getHeight();
    float f2 = 0.65F;
    f1 *= f2;
    cell1 = cell1.maxHeight(f1);
    cell1.row();
    int i = tab;
    if (i == 0) {
      buildFriends();
      return;
    } 
    byte b = 2;
    f1 = 2.8E-45F;
    if (i == b) {
      buildSkins();
      return;
    } 
    buildSettings();
  }
  
  private static void renderFriendLists(Jval paramJval) {
    body.clear();
    Table table3 = body;
    String str4 = HubText.get("myid");
    UiStyle.section(table3, str4);
    table3 = body;
    Label label = wrapLabel(HubClient.myId());
    Cell cell4 = table3.add((Element)label);
    float f1 = width();
    cell4.width(f1).left().row();
    Table table2 = body;
    String str3 = HubText.get("copy");
    HubUi$35 hubUi$35 = new HubUi$35();
    this();
    Cell cell3 = table2.button(str3, hubUi$35).size(140.0F, 40.0F).left();
    f1 = 6.0F;
    cell3.padBottom(f1).row();
    Table table1 = body;
    HubUi$36 hubUi$36 = new HubUi$36();
    this();
    Cell cell2 = table1.table(hubUi$36);
    float f2 = width();
    cell2.width(f2).padBottom(f1).row();
    TextField textField = new TextField();
    this();
    str3 = HubText.get("addfriend");
    textField.setMessageText(str3);
    textField.setMaxLength(32);
    Table table4 = body;
    HubUi$37 hubUi$37 = new HubUi$37();
    this(textField);
    Cell cell1 = table4.table(hubUi$37);
    f1 = width();
    cell1.width(f1).row();
    String str1 = HubText.get("requests");
    Jval.JsonArray jsonArray2 = paramJval.get("incoming").asArray();
    HubUi$38 hubUi$38 = new HubUi$38();
    this();
    section(str1, (Seq)jsonArray2, hubUi$38);
    str1 = HubText.get("pending");
    jsonArray2 = paramJval.get("outgoing").asArray();
    HubUi$39 hubUi$39 = new HubUi$39();
    this();
    section(str1, (Seq)jsonArray2, hubUi$39);
    str1 = "friends";
    String str2 = HubText.get(str1);
    Jval.JsonArray jsonArray1 = paramJval.get(str1).asArray();
    HubUi$40 hubUi$40 = new HubUi$40();
    this();
    section(str2, (Seq)jsonArray1, hubUi$40);
  }
  
  private static void report(String paramString) {
    UI uI = Vars.ui;
    String str = HubText.get("report");
    HubUi$8 hubUi$8 = new HubUi$8();
    this(paramString);
    uI.showTextInput(str, "", 200, "", hubUi$8);
  }
  
  private static void section(String paramString, Seq paramSeq, HubUi$RowFill paramHubUi$RowFill) {
    int i = paramSeq.size;
    if (i != 0) {
      Table table = body;
      UiStyle.section(table, paramString);
      Iterator<Jval> iterator = paramSeq.iterator();
      while (true) {
        boolean bool = iterator.hasNext();
        if (bool) {
          Jval jval = iterator.next();
          table = body;
          Drawable drawable = Styles.grayPanel;
          HubUi$23 hubUi$23 = new HubUi$23();
          this(jval, paramHubUi$RowFill);
          Cell cell = table.table(drawable, hubUi$23);
          float f1 = width();
          float f2 = 10.0F;
          f1 -= f2;
          cell = cell.width(f1);
          i = 1073741824;
          f1 = 2.0F;
          cell = cell.pad(f1);
          cell.row();
          continue;
        } 
        break;
      } 
    } 
  }
  
  public static void showBlocked() {
    BaseDialog baseDialog = new BaseDialog();
    String str1 = HubText.get("title");
    this(str1);
    UiStyle.register(baseDialog);
    Label label = new Label();
    String str2 = HubText.get("blocked");
    this(str2);
    label.setWrap(true);
    Cell cell = baseDialog.cont.add((Element)label);
    float f = width();
    cell.width(f).pad(8.0F);
    baseDialog.addCloseButton();
    baseDialog.show();
  }
  
  public static void showConsent(Runnable paramRunnable) {
    BaseDialog baseDialog = new BaseDialog();
    String str1 = HubText.get("consent.title");
    this(str1);
    UiStyle.register(baseDialog);
    Table table2 = baseDialog.cont;
    HubUi$45 hubUi$45 = new HubUi$45();
    this();
    Cell cell = table2.pane(hubUi$45);
    float f = Core.scene.getHeight() * 0.6F;
    cell.maxHeight(f).row();
    cell = baseDialog.buttons.defaults();
    f = Math.min(width() / 2.0F - 6.0F, 220.0F);
    cell.size(f, 56.0F).pad(4.0F);
    Table table1 = baseDialog.buttons;
    String str2 = HubText.get("decline");
    TextureRegionDrawable textureRegionDrawable = Icon.cancel;
    HubUi$46 hubUi$46 = new HubUi$46();
    this(baseDialog);
    table1.button(str2, (Drawable)textureRegionDrawable, hubUi$46);
    table1 = baseDialog.buttons;
    str2 = HubText.get("accept");
    textureRegionDrawable = Icon.ok;
    HubUi$47 hubUi$47 = new HubUi$47();
    this(baseDialog, paramRunnable);
    table1.button(str2, (Drawable)textureRegionDrawable, hubUi$47);
    paramRunnable = new HubUi$48();
    super();
    baseDialog.closeOnBack(paramRunnable);
    baseDialog.show();
  }
  
  public static void showGlobal(String paramString) {
    BaseDialog baseDialog = new BaseDialog();
    String str = HubText.get("global.title");
    this(str);
    UiStyle.register(baseDialog);
    Table table = baseDialog.cont;
    HubUi$26 hubUi$26 = new HubUi$26();
    this(paramString);
    Cell cell = table.pane(hubUi$26);
    float f = Core.scene.getHeight() * 0.6F;
    cell.maxHeight(f);
    baseDialog.addCloseButton();
    baseDialog.show();
  }
  
  private static float width() {
    return Math.min(Core.scene.getWidth() - 40.0F, 520.0F);
  }
  
  private static Label wrapLabel(String paramString) {
    Label label = new Label();
    this(paramString);
    label.setWrap(true);
    return label;
  }
}
