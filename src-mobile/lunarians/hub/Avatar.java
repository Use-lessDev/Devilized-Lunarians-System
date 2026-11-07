package lunarians.hub;

import arc.Core;
import arc.files.Fi;
import arc.graphics.Pixmap;
import arc.graphics.PixmapIO;
import arc.graphics.Texture;
import arc.graphics.g2d.TextureRegion;
import arc.scene.Element;
import arc.scene.Scene;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Image;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Scaling;
import arc.util.serialization.Base64Coder;
import arc.util.serialization.Jval;
import java.util.Iterator;
import lunarians.UiStyle;
import mindustry.Vars;
import mindustry.core.ContentLoader;
import mindustry.core.UI;
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
  private static Seq cache;
  
  private static final ObjectMap imgCache;
  
  private static final ObjectMap waiting;
  
  static {
    ObjectMap objectMap = new ObjectMap();
    this();
    imgCache = objectMap;
    objectMap = new ObjectMap();
    this();
    waiting = objectMap;
  }
  
  public static Seq choices() {
    Seq seq = cache;
    if (seq != null)
      return seq; 
    seq = new Seq();
    this();
    try {
      ContentLoader contentLoader2 = Vars.content;
      Seq seq2 = contentLoader2.items();
      Avatar$3 avatar$3 = new Avatar$3();
      this(seq);
      seq2.each(avatar$3);
      ContentLoader contentLoader1 = Vars.content;
      Seq seq1 = contentLoader1.units();
      Avatar$4 avatar$4 = new Avatar$4();
      this(seq);
      seq1.each(avatar$4);
    } finally {}
    cache = seq;
    return seq;
  }
  
  public static Drawable drawable(String paramString) {
    UnlockableContent unlockableContent = find(paramString);
    if (unlockableContent != null) {
      TextureRegion textureRegion = unlockableContent.uiIcon;
      if (textureRegion != null) {
        TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable();
        TextureRegion textureRegion1 = unlockableContent.uiIcon;
        this(textureRegion1);
        return (Drawable)textureRegionDrawable;
      } 
    } 
    return (Drawable)Icon.players;
  }
  
  private static UnlockableContent find(String paramString) {
    if (paramString == null)
      return null; 
    int i = 58;
    try {
      i = paramString.indexOf(i);
      if (i <= 0)
        return null; 
      String str1 = null;
      str1 = paramString.substring(0, i);
      paramString = paramString.substring(++i);
      boolean bool = okName(paramString);
      if (!bool)
        return null; 
      String str2 = "unit";
      bool = str1.equals(str2);
      if (bool) {
        ContentType contentType = ContentType.unit;
      } else {
        str2 = "item";
        bool = str1.equals(str2);
        if (bool) {
          ContentType contentType = ContentType.item;
        } else {
          bool = false;
          str2 = null;
        } 
      } 
      if (str2 == null)
        return null; 
      ContentLoader contentLoader = Vars.content;
      MappableContent mappableContent = contentLoader.getByName((ContentType)str2, paramString);
      bool = mappableContent instanceof UnlockableContent;
      if (bool)
        return (UnlockableContent)mappableContent; 
    } finally {}
    return null;
  }
  
  static TextureRegionDrawable fromPng(byte[] paramArrayOfbyte) {
    Pixmap pixmap = new Pixmap();
    this(paramArrayOfbyte);
    Texture texture = new Texture();
    this(pixmap);
    Texture.TextureFilter textureFilter = Texture.TextureFilter.linear;
    texture.setFilter(textureFilter);
    pixmap.dispose();
    TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable();
    TextureRegion textureRegion = new TextureRegion();
    this(texture);
    this(textureRegion);
    return textureRegionDrawable;
  }
  
  public static Image image(String paramString, float paramFloat) {
    Image image = new Image();
    Drawable drawable = drawable(paramString);
    this(drawable);
    Scaling scaling = Scaling.fit;
    image.setScaling(scaling);
    return image;
  }
  
  public static Image imageFor(Jval paramJval, float paramFloat) {
    String str2 = "";
    String str3 = paramJval.getString("avatar", str2);
    String str4 = "img";
    boolean bool = str4.equals(str3);
    if (!bool)
      return image(str3, paramFloat); 
    String str5 = paramJval.getString("id", str2);
    StringBuilder stringBuilder2 = new StringBuilder();
    this();
    stringBuilder2.append(str5);
    stringBuilder2.append(":");
    long l1 = 0L;
    long l2 = paramJval.getLong("avatar_ver", l1);
    stringBuilder2.append(l2);
    String str1 = stringBuilder2.toString();
    Image image = new Image();
    TextureRegionDrawable textureRegionDrawable2 = Icon.players;
    this((Drawable)textureRegionDrawable2);
    Scaling scaling = Scaling.fit;
    image.setScaling(scaling);
    TextureRegionDrawable textureRegionDrawable1 = (TextureRegionDrawable)imgCache.get(str1);
    if (textureRegionDrawable1 != null) {
      image.setDrawable((Drawable)textureRegionDrawable1);
      return image;
    } 
    ObjectMap objectMap = waiting;
    Seq seq = (Seq)objectMap.get(str1);
    if (seq != null) {
      seq.add(image);
      return image;
    } 
    seq = new Seq();
    this();
    seq.add(image);
    objectMap.put(str1, seq);
    StringBuilder stringBuilder1 = new StringBuilder();
    this("/api/avatar?id=");
    stringBuilder1.append(str5);
    str5 = stringBuilder1.toString();
    Avatar$1 avatar$1 = new Avatar$1();
    this(str1);
    Avatar$2 avatar$2 = new Avatar$2();
    this(str1);
    HubClient.call("GET", str5, null, avatar$1, avatar$2);
    return image;
  }
  
  public static Image mine(float paramFloat) {
    String str1 = HubClient.avatar();
    String str2 = "img";
    boolean bool1 = str2.equals(str1);
    if (bool1) {
      String str = HubClient.avatarImg();
      bool1 = str.isEmpty();
      if (!bool1) {
        str = HubClient.avatarImg();
        StringBuilder stringBuilder = new StringBuilder();
        this("me:");
        int i = str.length();
        stringBuilder.append(i);
        String str4 = ":";
        stringBuilder.append(str4);
        i = str.hashCode();
        stringBuilder.append(i);
        String str3 = stringBuilder.toString();
        try {
          ObjectMap objectMap = imgCache;
          Object object = objectMap.get(str3);
          object = object;
          if (object == null) {
            byte[] arrayOfByte = Base64Coder.decode(str);
            object = fromPng(arrayOfByte);
            objectMap.put(str3, object);
          } 
          Image image = new Image();
          this((Drawable)object);
          Scaling scaling = Scaling.fit;
          image.setScaling(scaling);
          return image;
        } finally {}
      } 
    } 
    boolean bool2 = str2.equals(str1);
    if (bool2)
      str1 = ""; 
    return image(str1, paramFloat);
  }
  
  private static boolean okName(String paramString) {
    if (paramString != null) {
      String str = "[a-z0-9-]{1,40}";
      boolean bool = paramString.matches(str);
      if (bool)
        return true; 
    } 
    return false;
  }
  
  public static void pick(Runnable paramRunnable) {
    BaseDialog baseDialog = new BaseDialog();
    String str = HubText.get("avatar.pick");
    this(str);
    UiStyle.register(baseDialog);
    Table table = new Table();
    this();
    Scene scene = Core.scene;
    float f1 = scene.getWidth() - 60.0F;
    float f2 = 520.0F;
    f1 = Math.min(f1, f2) / 64.0F;
    int i = (int)f1;
    byte b = 3;
    i = Math.max(b, i);
    Iterator<String> iterator = choices().iterator();
    int j = 0;
    while (true) {
      boolean bool = iterator.hasNext();
      if (bool) {
        String str2 = iterator.next();
        UnlockableContent unlockableContent = find(str2);
        if (unlockableContent != null) {
          TextureRegion textureRegion = unlockableContent.uiIcon;
          if (textureRegion != null) {
            TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable();
            TextureRegion textureRegion1 = unlockableContent.uiIcon;
            this(textureRegion1);
            ImageButton.ImageButtonStyle imageButtonStyle = Styles.cleari;
            Avatar$5 avatar$5 = new Avatar$5();
            this(str2, paramRunnable, baseDialog);
            float f3 = 44.0F;
            Cell cell1 = table.button((Drawable)textureRegionDrawable, imageButtonStyle, f3, avatar$5).size(58.0F);
            float f4 = 2.0F;
            cell1.pad(f4);
            int k = ++j % i;
            if (k == 0)
              table.row(); 
          } 
        } 
        continue;
      } 
      Cell cell = baseDialog.cont.pane((Element)table);
      f1 = Math.min(Core.scene.getWidth() - 40.0F, f2);
      cell = cell.width(f1);
      f1 = Core.scene.getHeight() * 0.6F;
      cell.maxHeight(f1).row();
      Table table1 = baseDialog.cont;
      String str1 = HubText.get("avatar.none");
      Avatar$6 avatar$6 = new Avatar$6();
      this(paramRunnable, baseDialog);
      table1.button(str1, avatar$6).size(220.0F, 46.0F).padTop(8.0F);
      baseDialog.addCloseButton();
      baseDialog.show();
      return;
    } 
  }
  
  public static void pickImage(Runnable paramRunnable) {
    FileChooser.FileChooserParams fileChooserParams = new FileChooser.FileChooserParams();
    this();
    fileChooserParams = fileChooserParams.open(true);
    String str = HubText.get("avatar.upload");
    fileChooserParams = fileChooserParams.title(str);
    String[] arrayOfString = { "png", "jpg", "jpeg" };
    fileChooserParams = fileChooserParams.extensions(arrayOfString);
    Avatar$0 avatar$0 = new Avatar$0();
    this(paramRunnable);
    fileChooserParams.submit(avatar$0);
  }
}
