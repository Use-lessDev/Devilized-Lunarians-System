package lunarians.hub;

import arc.Core;
import arc.Events;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import arc.util.Timer;
import arc.util.serialization.Jval;
import java.lang.reflect.Field;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.ui.Styles;

public class LunariansHub {
  private static Timer.Task task;
  
  private static Field betaField;
  
  private static Field panelField;
  
  private static final String BTN = "lunarians-hub-chat";
  
  private static boolean started = false;
  
  private static boolean blockedShown = false;
  
  private static int lastReq = 0;
  
  private static int lastChatN = 0;
  
  private static boolean betaFailed = false;
  
  public static void init() {
    if (started)
      return; 
    started = true;
    if (Vars.headless)
      return; 
    if (!HubClient.decided())
      Core.app.post(() -> Access.showConsent(null)); 
    startLoop();
    hookBetaHud();
    Skins.init();
    Access.initAll();
    try {
      Vars.ui.menufrag.addButton(HubText.get("hub"), (Drawable)Icon.players, HubUi::open);
    } catch (Throwable throwable) {}
  }
  
  private static void startLoop() {
    if (task != null)
      task.cancel(); 
    task = Timer.schedule(() -> {
          if (!HubClient.consented())
            return; 
          if (HubClient.myId().isEmpty()) {
            HubClient.ensureRegistered(null);
            return;
          } 
          HubClient.heartbeat(());
          if (HubClient.blocked && !blockedShown) {
            blockedShown = true;
            HubUi.showBlocked();
          } 
          if (!HubClient.blocked)
            blockedShown = false; 
          if (HubClient.pendingRequests > lastReq)
            Vars.ui.showInfoToast(HubText.get("newreq"), 3.0F); 
          if (HubClient.newChat > lastChatN)
            Vars.ui.showInfoToast(HubText.get("newchat"), 3.0F); 
          lastReq = HubClient.pendingRequests;
          lastChatN = HubClient.newChat;
        }5.0F, 45.0F);
  }
  
  private static void hookBetaHud() {
    Events.run(EventType.Trigger.update, () -> {
          if (betaFailed || Core.graphics.getFrameId() % 30L != 0L)
            return; 
          try {
            if (betaField == null)
              betaField = Class.forName("lunarians.LunariansMod").getField("betaHud"); 
            Object object = betaField.get(null);
            if (object == null)
              return; 
            if (panelField == null) {
              panelField = object.getClass().getDeclaredField("panel");
              panelField.setAccessible(true);
            } 
            Table table = (Table)panelField.get(object);
            if (table == null)
              return; 
            String str = HubText.get("beta.chat") + ((!HubClient.consented() || HubClient.newChat <= 0) ? "" : (" (" + HubClient.newChat + ")"));
            TextButton textButton1 = (TextButton)table.find("lunarians-hub-chat");
            if (textButton1 instanceof TextButton) {
              textButton1.setText(str);
              return;
            } 
            TextButton textButton2 = new TextButton(str, Styles.defaultt);
            textButton2.name = "lunarians-hub-chat";
            textButton2.clicked(HubUi::openChats);
            table.row();
            table.add((Element)textButton2).growX().height(44.0F).pad(2.0F);
          } catch (Throwable throwable) {
            betaFailed = true;
            Log.warn("[Lunarians Hub] Beta UI hook off: @", new Object[] { throwable.toString() });
          } 
        });
  }
  
  public static void stop() {
    if (task != null)
      task.cancel(); 
  }
}
