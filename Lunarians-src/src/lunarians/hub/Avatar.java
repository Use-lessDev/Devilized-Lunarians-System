package lunarians.hub;

import arc.Core;
import arc.files.Fi;
import arc.func.Cons;
import arc.graphics.Pixmap;
import arc.graphics.PixmapIO;
import arc.graphics.Texture;
import arc.graphics.g2d.TextureRegion;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Scaling;
import arc.util.serialization.Base64Coder;
import arc.util.serialization.Jval;
import java.util.Iterator;
import lunarians.UiStyle;
import mindustry.Vars;
import mindustry.ctype.ContentType;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Icon;
import mindustry.type.Item;
import mindustry.type.UnitType;
import mindustry.ui.FileChooser;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/hub/Avatar.class */
public class Avatar {
    private static Seq<String> cache;
    private static final ObjectMap<String, TextureRegionDrawable> imgCache = new ObjectMap<>();
    private static final ObjectMap<String, Seq<Image>> waiting = new ObjectMap<>();

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Avatar$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Cons {
        public final /* synthetic */ Runnable f$0;

        public final void get(Object obj) {
            Avatar.lambda$pickImage$7(this.f$0, (Fi) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Avatar$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass1 implements Cons {
        public final /* synthetic */ String f$0;

        public final void get(Object obj) {
            Avatar.lambda$imageFor$2(this.f$0, (Jval) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Avatar$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass2 implements Cons {
        public final /* synthetic */ String f$0;

        public final void get(Object obj) {
            Avatar.lambda$imageFor$3(this.f$0, (String) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Avatar$3, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass3 implements Cons {
        public final /* synthetic */ Seq f$0;

        public final void get(Object obj) {
            Avatar.lambda$choices$0(this.f$0, (Item) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Avatar$4, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass4 implements Cons {
        public final /* synthetic */ Seq f$0;

        public final void get(Object obj) {
            Avatar.lambda$choices$1(this.f$0, (UnitType) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Avatar$5, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass5 implements Runnable {
        public final /* synthetic */ String f$0;
        public final /* synthetic */ Runnable f$1;
        public final /* synthetic */ BaseDialog f$2;

        public /* synthetic */ AnonymousClass5(String str, Runnable runnable, BaseDialog baseDialog) {
            this.f$0 = str;
            this.f$1 = runnable;
            this.f$2 = baseDialog;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Avatar.lambda$pick$4(this.f$0, this.f$1, this.f$2);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Avatar$6, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass6 implements Runnable {
        public final /* synthetic */ Runnable f$0;
        public final /* synthetic */ BaseDialog f$1;

        public /* synthetic */ AnonymousClass6(Runnable runnable, BaseDialog baseDialog) {
            this.f$0 = runnable;
            this.f$1 = baseDialog;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Avatar.lambda$pick$5(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.hub.Avatar$7, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass7 implements Cons {
        public final /* synthetic */ Runnable f$0;

        public final void get(Object obj) {
            Avatar.lambda$pickImage$6(this.f$0, (Boolean) obj);
        }
    }

    private static boolean okName(String str) {
        return str != null && str.matches("[a-z0-9-]{1,40}");
    }

    public static Seq<String> choices() {
        if (cache != null) {
            return cache;
        }
        Seq<String> seq = new Seq<>();
        try {
            Vars.content.items().each(item -> {
                if (item.isVanilla() && okName(item.name) && item.uiIcon != null) {
                    seq.add("item:" + item.name);
                }
            });
            Vars.content.units().each(unitType -> {
                if (!unitType.isVanilla() || unitType.internal || !okName(unitType.name) || unitType.uiIcon == null) {
                    return;
                }
                seq.add("unit:" + unitType.name);
            });
        } catch (Throwable th) {
        }
        cache = seq;
        return seq;
    }

    private static UnlockableContent find(String str) {
        if (str == null) {
            return null;
        }
        try {
            int indexOf = str.indexOf(58);
            if (indexOf <= 0) {
                return null;
            }
            String substring = str.substring(0, indexOf);
            String substring2 = str.substring(indexOf + 1);
            if (!okName(substring2)) {
                return null;
            }
            ContentType contentType = substring.equals("unit") ? ContentType.unit : substring.equals("item") ? ContentType.item : null;
            if (contentType == null) {
                return null;
            }
            UnlockableContent byName = Vars.content.getByName(contentType, substring2);
            if (byName instanceof UnlockableContent) {
                return byName;
            }
            return null;
        } catch (Throwable th) {
            return null;
        }
    }

    public static Drawable drawable(String str) {
        UnlockableContent find = find(str);
        return (find == null || find.uiIcon == null) ? Icon.players : new TextureRegionDrawable(find.uiIcon);
    }

    public static Image image(String str, float f) {
        Image image = new Image(drawable(str));
        image.setScaling(Scaling.fit);
        return image;
    }

    static TextureRegionDrawable fromPng(byte[] bArr) {
        Pixmap pixmap = new Pixmap(bArr);
        Texture texture = new Texture(pixmap);
        texture.setFilter(Texture.TextureFilter.linear);
        pixmap.dispose();
        return new TextureRegionDrawable(new TextureRegion(texture));
    }

    public static Image imageFor(Jval jval, float f) {
        String string = jval.getString("avatar", "");
        if (!"img".equals(string)) {
            return image(string, f);
        }
        String string2 = jval.getString("id", "");
        String str = string2 + ":" + jval.getLong("avatar_ver", 0L);
        Image image = new Image(Icon.players);
        image.setScaling(Scaling.fit);
        TextureRegionDrawable textureRegionDrawable = (TextureRegionDrawable) imgCache.get(str);
        if (textureRegionDrawable != null) {
            image.setDrawable(textureRegionDrawable);
            return image;
        }
        Seq seq = (Seq) waiting.get(str);
        if (seq != null) {
            seq.add(image);
            return image;
        }
        Seq seq2 = new Seq();
        seq2.add(image);
        waiting.put(str, seq2);
        HubClient.call("GET", "/api/avatar?id=" + string2, null, jval2 -> {
            Seq seq3 = (Seq) waiting.remove(str);
            try {
                TextureRegionDrawable fromPng = fromPng(Base64Coder.decode(jval2.getString("png", "")));
                imgCache.put(str, fromPng);
                if (seq3 != null) {
                    Iterator it = seq3.iterator();
                    while (it.hasNext()) {
                        ((Image) it.next()).setDrawable(fromPng);
                    }
                }
            } catch (Throwable th) {
            }
        }, str2 -> {
            waiting.remove(str);
        });
        return image;
    }

    public static Image mine(float f) {
        String avatar = HubClient.avatar();
        if ("img".equals(avatar) && !HubClient.avatarImg().isEmpty()) {
            String avatarImg = HubClient.avatarImg();
            String str = "me:" + avatarImg.length() + ":" + avatarImg.hashCode();
            try {
                TextureRegionDrawable textureRegionDrawable = (TextureRegionDrawable) imgCache.get(str);
                if (textureRegionDrawable == null) {
                    textureRegionDrawable = fromPng(Base64Coder.decode(avatarImg));
                    imgCache.put(str, textureRegionDrawable);
                }
                Image image = new Image(textureRegionDrawable);
                image.setScaling(Scaling.fit);
                return image;
            } catch (Throwable th) {
            }
        }
        return image("img".equals(avatar) ? "" : avatar, f);
    }

    public static void pick(Runnable runnable) {
        BaseDialog baseDialog = new BaseDialog(HubText.get("avatar.pick"));
        UiStyle.register(baseDialog);
        Table table = new Table();
        int max = Math.max(3, (int) (Math.min(Core.scene.getWidth() - 60.0f, 520.0f) / 64.0f));
        int i = 0;
        Iterator it = choices().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            UnlockableContent find = find(str);
            if (find != null && find.uiIcon != null) {
                table.button(new TextureRegionDrawable(find.uiIcon), Styles.cleari, 44.0f, () -> {
                    HubClient.setAvatar(str);
                    if (runnable != null) {
                        runnable.run();
                    }
                    baseDialog.hide();
                }).size(58.0f).pad(2.0f);
                i++;
                if (i % max == 0) {
                    table.row();
                }
            }
        }
        baseDialog.cont.pane(table).width(Math.min(Core.scene.getWidth() - 40.0f, 520.0f)).maxHeight(Core.scene.getHeight() * 0.6f).row();
        baseDialog.cont.button(HubText.get("avatar.none"), () -> {
            HubClient.setAvatar("");
            if (runnable != null) {
                runnable.run();
            }
            baseDialog.hide();
        }).size(220.0f, 46.0f).padTop(8.0f);
        baseDialog.addCloseButton();
        baseDialog.show();
    }

    public static void pickImage(Runnable runnable) {
        new FileChooser.FileChooserParams().open(true).title(HubText.get("avatar.upload")).extensions(new String[]{"png", "jpg", "jpeg"}).submit(fi -> {
            try {
                if (fi.length() > 8000000) {
                    Vars.ui.showInfoToast(HubText.get("avatar.fail"), 3.0f);
                    return;
                }
                Pixmap pixmap = new Pixmap(fi);
                int min = Math.min(pixmap.width, pixmap.height);
                Pixmap pixmap2 = new Pixmap(64, 64);
                pixmap2.draw(pixmap, (pixmap.width - min) / 2, (pixmap.height - min) / 2, min, min, 0, 0, 64, 64);
                byte[] writePngBytes = PixmapIO.writePngBytes(pixmap2);
                pixmap.dispose();
                pixmap2.dispose();
                HubClient.uploadAvatar(writePngBytes, bool -> {
                    Vars.ui.showInfoToast(HubText.get(bool.booleanValue() ? "avatar.done" : "avatar.fail"), 3.0f);
                    if (!bool.booleanValue() || runnable == null) {
                        return;
                    }
                    runnable.run();
                });
            } catch (Throwable th) {
                Vars.ui.showInfoToast(HubText.get("avatar.fail"), 3.0f);
            }
        });
    }
}
