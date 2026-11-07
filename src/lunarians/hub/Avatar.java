package lunarians.hub;

import arc.Core;
import arc.files.Fi;
import arc.graphics.Pixmap;
import arc.graphics.PixmapIO;
import arc.graphics.Texture;
import arc.graphics.g2d.TextureRegion;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Scaling;
import arc.util.serialization.Base64Coder;
import arc.util.serialization.Jval;
import lunarians.UiStyle;
import mindustry.Vars;
import mindustry.ctype.ContentType;
import mindustry.ctype.MappableContent;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Icon;
import mindustry.type.Item;
import mindustry.type.UnitType;
import mindustry.ui.FileChooser;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public class Avatar {
  private static Seq<String> cache;
  
  private static final ObjectMap<String, TextureRegionDrawable> imgCache = new ObjectMap();
  
  private static final ObjectMap<String, Seq<Image>> waiting = new ObjectMap();
  
  private static boolean okName(String paramString) {
    return (paramString != null && paramString.matches("[a-z0-9-]{1,40}"));
  }
  
  public static Seq<String> choices() {
    if (cache != null)
      return cache; 
    Seq<String> seq = new Seq();
    try {
      Vars.content.items().each(paramItem -> {
            if (paramItem.isVanilla() && okName(paramItem.name) && paramItem.uiIcon != null)
              paramSeq.add("item:" + paramItem.name); 
          });
      Vars.content.units().each(paramUnitType -> {
            if (paramUnitType.isVanilla() && !paramUnitType.internal && okName(paramUnitType.name) && paramUnitType.uiIcon != null)
              paramSeq.add("unit:" + paramUnitType.name); 
          });
    } catch (Throwable throwable) {}
    cache = seq;
    return seq;
  }
  
  private static UnlockableContent find(String paramString) {
    try {
      if (paramString == null)
        return null; 
      int i = paramString.indexOf(':');
      if (i <= 0)
        return null; 
      String str1 = paramString.substring(0, i), str2 = paramString.substring(i + 1);
      if (!okName(str2))
        return null; 
      ContentType contentType = str1.equals("unit") ? ContentType.unit : (str1.equals("item") ? ContentType.item : null);
      if (contentType == null)
        return null; 
      MappableContent mappableContent = Vars.content.getByName(contentType, str2);
      return (mappableContent instanceof UnlockableContent) ? (UnlockableContent)mappableContent : null;
    } catch (Throwable throwable) {
      return null;
    } 
  }
  
  public static Drawable drawable(String paramString) {
    UnlockableContent unlockableContent = find(paramString);
    if (unlockableContent != null && unlockableContent.uiIcon != null)
      return (Drawable)new TextureRegionDrawable(unlockableContent.uiIcon); 
    return (Drawable)Icon.players;
  }
  
  public static Image image(String paramString, float paramFloat) {
    Image image = new Image(drawable(paramString));
    image.setScaling(Scaling.fit);
    return image;
  }
  
  static TextureRegionDrawable fromPng(byte[] paramArrayOfbyte) {
    Pixmap pixmap = new Pixmap(paramArrayOfbyte);
    Texture texture = new Texture(pixmap);
    texture.setFilter(Texture.TextureFilter.linear);
    pixmap.dispose();
    return new TextureRegionDrawable(new TextureRegion(texture));
  }
  
  public static Image imageFor(Jval paramJval, float paramFloat) {
    String str1 = paramJval.getString("avatar", "");
    if (!"img".equals(str1))
      return image(str1, paramFloat); 
    String str2 = paramJval.getString("id", "");
    String str3 = str2 + ":" + paramJval.getLong("avatar_ver", 0L);
    Image image = new Image((Drawable)Icon.players);
    image.setScaling(Scaling.fit);
    TextureRegionDrawable textureRegionDrawable = (TextureRegionDrawable)imgCache.get(str3);
    if (textureRegionDrawable != null) {
      image.setDrawable((Drawable)textureRegionDrawable);
      return image;
    } 
    Seq seq = (Seq)waiting.get(str3);
    if (seq != null) {
      seq.add(image);
      return image;
    } 
    seq = new Seq();
    seq.add(image);
    waiting.put(str3, seq);
    HubClient.call("GET", "/api/avatar?id=" + str2, null, paramJval -> {
          Seq seq = (Seq)waiting.remove(paramString);
          try {
            TextureRegionDrawable textureRegionDrawable = fromPng(Base64Coder.decode(paramJval.getString("png", "")));
            imgCache.put(paramString, textureRegionDrawable);
            if (seq != null)
              for (Image image : seq)
                image.setDrawable((Drawable)textureRegionDrawable);  
          } catch (Throwable throwable) {}
        }paramString2 -> waiting.remove(paramString1));
    return image;
  }
  
  public static Image mine(float paramFloat) {
    String str = HubClient.avatar();
    if ("img".equals(str) && !HubClient.avatarImg().isEmpty()) {
      String str1 = HubClient.avatarImg();
      String str2 = "me:" + str1.length() + ":" + str1.hashCode();
      try {
        TextureRegionDrawable textureRegionDrawable = (TextureRegionDrawable)imgCache.get(str2);
        if (textureRegionDrawable == null) {
          textureRegionDrawable = fromPng(Base64Coder.decode(str1));
          imgCache.put(str2, textureRegionDrawable);
        } 
        Image image = new Image((Drawable)textureRegionDrawable);
        image.setScaling(Scaling.fit);
        return image;
      } catch (Throwable throwable) {}
    } 
    return image("img".equals(str) ? "" : str, paramFloat);
  }
  
  public static void pick(Runnable paramRunnable) {
    BaseDialog baseDialog = new BaseDialog(HubText.get("avatar.pick"));
    UiStyle.register(baseDialog);
    Table table = new Table();
    int i = Math.max(3, (int)(Math.min(Core.scene.getWidth() - 60.0F, 520.0F) / 64.0F));
    byte b = 0;
    for (String str : choices()) {
      UnlockableContent unlockableContent = find(str);
      if (unlockableContent == null || unlockableContent.uiIcon == null)
        continue; 
      table.button((Drawable)new TextureRegionDrawable(unlockableContent.uiIcon), Styles.cleari, 44.0F, () -> {
            HubClient.setAvatar(paramString);
            if (paramRunnable != null)
              paramRunnable.run(); 
            paramBaseDialog.hide();
          }).size(58.0F).pad(2.0F);
      if (++b % i == 0)
        table.row(); 
    } 
    baseDialog.cont.pane((Element)table).width(Math.min(Core.scene.getWidth() - 40.0F, 520.0F)).maxHeight(Core.scene.getHeight() * 0.6F).row();
    baseDialog.cont.button(HubText.get("avatar.none"), () -> {
          HubClient.setAvatar("");
          if (paramRunnable != null)
            paramRunnable.run(); 
          paramBaseDialog.hide();
        }).size(220.0F, 46.0F).padTop(8.0F);
    baseDialog.addCloseButton();
    baseDialog.show();
  }
  
  public static void pickImage(Runnable paramRunnable) {
    (new FileChooser.FileChooserParams()).open(true).title(HubText.get("avatar.upload")).extensions(new String[] { "png", "jpg", "jpeg" }).submit(paramFi -> {
          try {
            if (paramFi.length() > 8000000L) {
              Vars.ui.showInfoToast(HubText.get("avatar.fail"), 3.0F);
              return;
            } 
            Pixmap pixmap1 = new Pixmap(paramFi);
            int i = Math.min(pixmap1.width, pixmap1.height);
            Pixmap pixmap2 = new Pixmap(64, 64);
            pixmap2.draw(pixmap1, (pixmap1.width - i) / 2, (pixmap1.height - i) / 2, i, i, 0, 0, 64, 64);
            byte[] arrayOfByte = PixmapIO.writePngBytes(pixmap2);
            pixmap1.dispose();
            pixmap2.dispose();
            HubClient.uploadAvatar(arrayOfByte, ());
          } catch (Throwable throwable) {
            Vars.ui.showInfoToast(HubText.get("avatar.fail"), 3.0F);
          } 
        });
  }
}
