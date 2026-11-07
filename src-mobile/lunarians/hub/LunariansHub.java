package lunarians.hub;

import arc.Application;
import arc.Core;
import arc.Events;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.util.Timer;
import arc.util.serialization.Jval;
import java.lang.reflect.Field;
import java.util.Iterator;
import mindustry.Vars;
import mindustry.core.UI;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.ui.fragments.MenuFragment;

public class LunariansHub {
  private static final String BTN = "lunarians-hub-chat";
  
  private static boolean betaFailed = false;
  
  private static Field betaField;
  
  private static boolean blockedShown = false;
  
  private static int lastChatN = 0;
  
  private static int lastReq = 0;
  
  private static Field panelField;
  
  private static boolean started = false;
  
  private static Timer.Task task;
  
  private static void hookBetaHud() {
    EventType.Trigger trigger = EventType.Trigger.update;
    LunariansHub$2 lunariansHub$2 = new LunariansHub$2();
    this();
    Events.run(trigger, lunariansHub$2);
  }
  
  public static void init() {
    boolean bool = started;
    if (!bool) {
      started = true;
      bool = Vars.headless;
      if (!bool) {
        bool = HubClient.decided();
        if (!bool) {
          Application application = Core.app;
          LunariansHub$1 lunariansHub$1 = new LunariansHub$1();
          this();
          application.post(lunariansHub$1);
        } 
        startLoop();
        hookBetaHud();
        Skins.init();
        Access.initAll();
        try {
          UI uI = Vars.ui;
          MenuFragment menuFragment = uI.menufrag;
          String str = "hub";
          str = HubText.get(str);
          TextureRegionDrawable textureRegionDrawable = Icon.players;
          HubUi$3 hubUi$3 = new HubUi$3();
          this();
          menuFragment.addButton(str, (Drawable)textureRegionDrawable, hubUi$3);
        } finally {}
      } 
    } 
  }
  
  private static void startLoop() {
    Timer.Task task = task;
    if (task != null)
      task.cancel(); 
    LunariansHub$0 lunariansHub$0 = new LunariansHub$0();
    this();
    task = Timer.schedule(lunariansHub$0, 5.0F, 45.0F);
  }
  
  public static void stop() {
    Timer.Task task = task;
    if (task != null)
      task.cancel(); 
  }
}
