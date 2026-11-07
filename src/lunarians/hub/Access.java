package lunarians.hub;

import arc.Core;
import arc.Events;
import arc.func.Boolp;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.ui.Label;
import arc.scene.ui.TextArea;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectMap;
import arc.util.Log;
import arc.util.Timer;
import arc.util.serialization.Jval;
import java.lang.reflect.Field;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import lunarians.UiStyle;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.ui.dialogs.BaseDialog;

public class Access {
  private static final String K_STATUS = "lunarians-access";
  
  private static boolean inited;
  
  private static boolean askedThisSession;
  
  private static boolean fetchedOnce;
  
  private static boolean warned;
  
  private static Field betaField;
  
  private static Boolp wrapper;
  
  private static Boolean schemForced;
  
  private static Boolean schemOrig;
  
  public static String status() {
    return Core.settings.getString("lunarians-access", "none");
  }
  
  public static boolean approved() {
    return (HubClient.consented() && "approved".equals(status()));
  }
  
  public static void initAll() {
    Plus.init();
    init();
  }
  
  private static void init() {
    if (inited || Vars.headless)
      return; 
    inited = true;
    Events.run(EventType.Trigger.update, Access::gate);
    Timer.schedule(Access::poll, 4.0F, 30.0F);
    Timer.schedule(Access::enforce, 3.0F, 2.0F);
    try {
      Vars.ui.menufrag.addButton(tr("access.btn"), (Drawable)Icon.lock, Access::menuClick);
    } catch (Throwable throwable) {}
  }
  
  private static void gate() {
    if (Core.graphics.getFrameId() % 20L != 0L)
      return; 
    try {
      if (betaField == null)
        betaField = Class.forName("lunarians.LunariansMod").getField("betaHud"); 
      Element element = (Element)betaField.get(null);
      if (element == null)
        return; 
      if (element.visibility != wrapper) {
        Boolp boolp = element.visibility;
        wrapper = (() -> (approved() && (paramBoolp == null || paramBoolp.get())));
        element.visible(wrapper);
      } 
      if (!approved())
        element.visible = false; 
    } catch (Throwable throwable) {
      if (!warned) {
        warned = true;
        Log.warn("[Lunarians Hub] Beta UI gate: @", new Object[] { throwable.toString() });
      } 
    } 
  }
  
  private static void poll() {
    if (!HubClient.consented()) {
      if (!"none".equals(status()))
        Core.settings.put("lunarians-access", "none"); 
      applyFeatures(null);
      return;
    } 
    if (HubClient.myId().isEmpty() || HubClient.blocked)
      return; 
    HubClient.call("GET", "/api/access/status", null, paramJval -> {
          apply(paramJval.getString("status", "none"), paramJval.getString("note", ""));
          applyFeatures(paramJval.has("features") ? paramJval.get("features") : null);
        }paramString -> {
        
        });
  }
  
  private static void apply(String paramString1, String paramString2) {
    String str = status();
    Core.settings.put("lunarians-access", paramString1);
    if (fetchedOnce && !paramString1.equals(str))
      if (paramString1.equals("approved")) {
        Vars.ui.showInfoToast(tr("access.approved"), 5.0F);
      } else if (paramString1.equals("rejected")) {
        Vars.ui.showInfoToast(tr("access.rejected") + (paramString2.isEmpty() ? "" : (": " + paramString2)), 6.0F);
      }  
    fetchedOnce = true;
    if (paramString1.equals("none") && !askedThisSession) {
      askedThisSession = true;
      build(false, null);
    } 
  }
  
  private static void applyFeatures(Jval paramJval) {
    try {
      if (paramJval != null && paramJval.has("showSchematicPanel")) {
        boolean bool = paramJval.getBool("showSchematicPanel", true);
        if (schemForced == null)
          schemOrig = Boolean.valueOf(LunariansSettings.showSchematicPanel); 
        schemForced = Boolean.valueOf(bool);
        setSchem(bool);
      } else if (schemForced != null) {
        if (schemOrig != null)
          setSchem(schemOrig.booleanValue()); 
        schemForced = null;
        schemOrig = null;
      } 
    } catch (Throwable throwable) {}
  }
  
  private static void setSchem(boolean paramBoolean) {
    if (LunariansSettings.showSchematicPanel != paramBoolean)
      LunariansSettings.showSchematicPanel = paramBoolean; 
  }
  
  private static void enforce() {
    if (schemForced != null)
      setSchem(schemForced.booleanValue()); 
  }
  
  private static void menuClick() {
    if (!HubClient.consented()) {
      showConsent(null);
      return;
    } 
    String str = status();
    if (str.equals("approved")) {
      Vars.ui.showInfoToast(tr("access.ok"), 3.0F);
    } else if (str.equals("pending")) {
      Vars.ui.showInfoToast(tr("access.waiting"), 3.0F);
    } else {
      build(false, null);
    } 
  }
  
  public static void showConsent(Runnable paramRunnable) {
    build(true, paramRunnable);
  }
  
  private static Label wrap(String paramString) {
    Label label = new Label(paramString);
    label.setWrap(true);
    return label;
  }
  
  private static void build(boolean paramBoolean, Runnable paramRunnable) {
    BaseDialog baseDialog = new BaseDialog(paramBoolean ? HubText.get("consent.title") : tr("access.title"));
    UiStyle.register(baseDialog);
    float f = Math.min(Core.scene.getWidth() - 40.0F, 520.0F);
    TextArea textArea = new TextArea("");
    textArea.setMessageText(tr("access.hint"));
    textArea.setMaxLength(300);
    baseDialog.cont.pane(paramTable -> {
          if (paramBoolean) {
            paramTable.add((Element)wrap(HubText.get("consent.body"))).width(paramFloat).left().pad(8.0F).row();
          } else {
            paramTable.add((Element)wrap(tr("access.intro"))).width(paramFloat).left().pad(8.0F).row();
          } 
          paramTable.add((Element)wrap(tr("access.why"))).width(paramFloat).left().padTop(8.0F).padLeft(8.0F).row();
          paramTable.add((Element)paramTextArea).width(paramFloat).height(120.0F).pad(8.0F).row();
        }).maxHeight(Core.scene.getHeight() * 0.7F).row();
    baseDialog.buttons.defaults().size(Math.min(f / 2.0F - 6.0F, 220.0F), 56.0F).pad(4.0F);
    baseDialog.buttons.button(paramBoolean ? HubText.get("decline") : tr("access.later"), (Drawable)Icon.cancel, () -> {
          if (paramBoolean)
            HubClient.setConsent(false); 
          paramBaseDialog.hide();
        });
    baseDialog.buttons.button(tr("access.send"), (Drawable)Icon.upload, () -> {
          String str = paramTextArea.getText().trim();
          if (str.length() < 5) {
            Vars.ui.showInfoToast(tr("access.short"), 3.0F);
            return;
          } 
          paramBaseDialog.hide();
          if (paramBoolean) {
            HubClient.setConsent(true);
            HubClient.ensureRegistered(());
          } else {
            send(str);
          } 
        });
    baseDialog.closeOnBack(() -> {
          if (paramBoolean && !HubClient.decided())
            HubClient.setConsent(false); 
        });
    baseDialog.show();
  }
  
  private static void send(String paramString) {
    Jval jval = Jval.newObject();
    jval.add("reason", paramString);
    HubClient.call("POST", "/api/access/request", jval, paramJval -> {
          Core.settings.put("lunarians-access", paramJval.getString("status", "pending"));
          Vars.ui.showInfoToast(tr("access.sent"), 5.0F);
        }paramString -> Vars.ui.showInfoToast(tr("access.fail"), 4.0F));
  }
  
  private static final ObjectMap<String, String[]> T = new ObjectMap();
  
  private static void a(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5) {
    T.put(paramString1, new String[] { paramString2, paramString3, paramString4, paramString5 });
  }
  
  private static String tr(String paramString) {
    String[] arrayOfString = (String[])T.get(paramString);
    if (arrayOfString == null)
      return paramString; 
    String str = LunariansI18n.getLanguage();
    return arrayOfString[str.equals("pt") ? 1 : (str.equals("es") ? 2 : (str.equals("ru") ? 3 : 0))];
  }
  
  static {
    a("access.title", "Request Beta UI access", "Pedir acesso ao Beta UI", "Pedir acceso a la Beta UI", "Запросить доступ к Beta UI");
    a("access.intro", "The Beta UI is unlocked by the developer. Tell him why you want to use the mod and send the request.", "O Beta UI é liberado pelo desenvolvedor. Conte por que você quer usar o mod e envie o pedido.", "La Beta UI la desbloquea el desarrollador. Cuéntale por qué quieres usar el mod y envía la solicitud.", "Beta UI открывает разработчик. Напишите, зачем вам мод, и отправьте заявку.");
    a("access.why", "Why do you want to use the mod?", "Por que você quer usar o mod?", "¿Por qué quieres usar el mod?", "Зачем вы хотите использовать мод?");
    a("access.hint", "Write your reason here...", "Escreva o motivo aqui...", "Escribe el motivo aquí...", "Напишите причину здесь...");
    a("access.send", "Send", "Enviar", "Enviar", "Отправить");
    a("access.later", "Later", "Depois", "Después", "Позже");
    a("access.short", "Write at least a few words", "Escreva pelo menos algumas palavras", "Escribe al menos unas palabras", "Напишите хотя бы пару слов");
    a("access.sent", "Request sent. You will be notified when it is approved.", "Pedido enviado. Você será avisado quando for aprovado.", "Solicitud enviada. Se te avisará cuando sea aprobada.", "Заявка отправлена. Вы получите уведомление после одобрения.");
    a("access.fail", "Could not send. Try again.", "Não foi possível enviar. Tente de novo.", "No se pudo enviar. Inténtalo de nuevo.", "Не удалось отправить. Попробуйте ещё раз.");
    a("access.approved", "Beta UI unlocked!", "Beta UI liberado!", "¡Beta UI desbloqueada!", "Beta UI разблокирован!");
    a("access.rejected", "Beta UI request rejected", "Pedido do Beta UI recusado", "Solicitud de Beta UI rechazada", "Заявка на Beta UI отклонена");
    a("access.waiting", "Your request is waiting for the developer.", "Seu pedido está aguardando o desenvolvedor.", "Tu solicitud espera al desarrollador.", "Ваша заявка ждёт разработчика.");
    a("access.ok", "Beta UI is already unlocked.", "O Beta UI já está liberado.", "La Beta UI ya está desbloqueada.", "Beta UI уже разблокирован.");
    a("access.btn", "Beta UI access", "Acesso ao Beta UI", "Acceso a la Beta UI", "Доступ к Beta UI");
  }
}
