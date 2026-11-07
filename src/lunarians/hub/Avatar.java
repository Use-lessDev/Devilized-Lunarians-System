/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.func.Cons
 *  arc.graphics.Pixmap
 *  arc.graphics.PixmapIO
 *  arc.graphics.Texture
 *  arc.graphics.Texture$TextureFilter
 *  arc.graphics.g2d.TextureRegion
 *  arc.scene.Element
 *  arc.scene.style.Drawable
 *  arc.scene.style.TextureRegionDrawable
 *  arc.scene.ui.Image
 *  arc.scene.ui.layout.Table
 *  arc.struct.ObjectMap
 *  arc.struct.Seq
 *  arc.util.Scaling
 *  arc.util.serialization.Base64Coder
 *  arc.util.serialization.Jval
 *  mindustry.Vars
 *  mindustry.ctype.ContentType
 *  mindustry.ctype.MappableContent
 *  mindustry.ctype.UnlockableContent
 *  mindustry.gen.Icon
 *  mindustry.ui.FileChooser$FileChooserParams
 *  mindustry.ui.Styles
 *  mindustry.ui.dialogs.BaseDialog
 */
package lunarians.hub;

import arc.Core;
import arc.func.Cons;
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
import lunarians.hub.HubClient;
import lunarians.hub.HubText;
import mindustry.Vars;
import mindustry.ctype.ContentType;
import mindustry.ctype.MappableContent;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Icon;
import mindustry.ui.FileChooser;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public class Avatar {
    private static Seq<String> cache;
    private static final ObjectMap<String, TextureRegionDrawable> imgCache;
    private static final ObjectMap<String, Seq<Image>> waiting;

    private static boolean okName(String string) {
        return string != null && string.matches("[a-z0-9-]{1,40}");
    }

    public static Seq<String> choices() {
        if (cache != null) {
            return cache;
        }
        Seq seq = new Seq();
        try {
            Vars.content.items().each(item -> {
                if (item.isVanilla() && Avatar.okName(item.name) && item.uiIcon != null) {
                    seq.add((Object)("item:" + item.name));
                }
            });
            Vars.content.units().each(unitType -> {
                if (unitType.isVanilla() && !unitType.internal && Avatar.okName(unitType.name) && unitType.uiIcon != null) {
                    seq.add((Object)("unit:" + unitType.name));
                }
            });
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        cache = seq;
        return seq;
    }

    private static UnlockableContent find(String string) {
        try {
            ContentType contentType;
            if (string == null) {
                return null;
            }
            int n = string.indexOf(58);
            if (n <= 0) {
                return null;
            }
            String string2 = string.substring(0, n);
            String string3 = string.substring(n + 1);
            if (!Avatar.okName(string3)) {
                return null;
            }
            Object object = string2.equals("unit") ? ContentType.unit : (contentType = string2.equals("item") ? ContentType.item : null);
            if (contentType == null) {
                return null;
            }
            MappableContent mappableContent = Vars.content.getByName(contentType, string3);
            return mappableContent instanceof UnlockableContent ? (UnlockableContent)mappableContent : null;
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    public static Drawable drawable(String string) {
        UnlockableContent unlockableContent = Avatar.find(string);
        if (unlockableContent != null && unlockableContent.uiIcon != null) {
            return new TextureRegionDrawable(unlockableContent.uiIcon);
        }
        return Icon.players;
    }

    public static Image image(String string, float f) {
        Image image = new Image(Avatar.drawable(string));
        image.setScaling(Scaling.fit);
        return image;
    }

    static TextureRegionDrawable fromPng(byte[] byArray) {
        Pixmap pixmap = new Pixmap(byArray);
        Texture texture = new Texture(pixmap);
        texture.setFilter(Texture.TextureFilter.linear);
        pixmap.dispose();
        return new TextureRegionDrawable(new TextureRegion(texture));
    }

    public static Image imageFor(Jval jval2, float f) {
        String string = jval2.getString("avatar", "");
        if (!"img".equals(string)) {
            return Avatar.image(string, f);
        }
        String string3 = jval2.getString("id", "");
        String string4 = string3 + ":" + jval2.getLong("avatar_ver", 0L);
        Image image = new Image((Drawable)Icon.players);
        image.setScaling(Scaling.fit);
        TextureRegionDrawable textureRegionDrawable = (TextureRegionDrawable)imgCache.get((Object)string4);
        if (textureRegionDrawable != null) {
            image.setDrawable((Drawable)textureRegionDrawable);
            return image;
        }
        Seq seq = (Seq)waiting.get((Object)string4);
        if (seq != null) {
            seq.add((Object)image);
            return image;
        }
        seq = new Seq();
        seq.add((Object)image);
        waiting.put((Object)string4, (Object)seq);
        HubClient.call("GET", "/api/avatar?id=" + string3, null, (Cons<Jval>)((Cons)jval -> {
            Seq seq = (Seq)waiting.remove((Object)string4);
            try {
                TextureRegionDrawable textureRegionDrawable = Avatar.fromPng(Base64Coder.decode((String)jval.getString("png", "")));
                imgCache.put((Object)string4, (Object)textureRegionDrawable);
                if (seq != null) {
                    for (Image image : seq) {
                        image.setDrawable((Drawable)textureRegionDrawable);
                    }
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }), (Cons<String>)((Cons)string2 -> waiting.remove((Object)string4)));
        return image;
    }

    public static Image mine(float f) {
        String string = HubClient.avatar();
        if ("img".equals(string) && !HubClient.avatarImg().isEmpty()) {
            String string2 = HubClient.avatarImg();
            String string3 = "me:" + string2.length() + ":" + string2.hashCode();
            try {
                TextureRegionDrawable textureRegionDrawable = (TextureRegionDrawable)imgCache.get((Object)string3);
                if (textureRegionDrawable == null) {
                    textureRegionDrawable = Avatar.fromPng(Base64Coder.decode((String)string2));
                    imgCache.put((Object)string3, (Object)textureRegionDrawable);
                }
                Image image = new Image((Drawable)textureRegionDrawable);
                image.setScaling(Scaling.fit);
                return image;
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        return Avatar.image("img".equals(string) ? "" : string, f);
    }

    public static void pick(Runnable runnable) {
        BaseDialog baseDialog = new BaseDialog(HubText.get("avatar.pick"));
        UiStyle.register(baseDialog);
        Table table = new Table();
        int n = Math.max(3, (int)(Math.min(Core.scene.getWidth() - 60.0f, 520.0f) / 64.0f));
        int n2 = 0;
        for (String string : Avatar.choices()) {
            UnlockableContent unlockableContent = Avatar.find(string);
            if (unlockableContent == null || unlockableContent.uiIcon == null) continue;
            table.button((Drawable)new TextureRegionDrawable(unlockableContent.uiIcon), Styles.cleari, 44.0f, () -> {
                HubClient.setAvatar(string);
                if (runnable != null) {
                    runnable.run();
                }
                baseDialog.hide();
            }).size(58.0f).pad(2.0f);
            if (++n2 % n != 0) continue;
            table.row();
        }
        baseDialog.cont.pane((Element)table).width(Math.min(Core.scene.getWidth() - 40.0f, 520.0f)).maxHeight(Core.scene.getHeight() * 0.6f).row();
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
                if (fi.length() > 8000000L) {
                    Vars.ui.showInfoToast(HubText.get("avatar.fail"), 3.0f);
                    return;
                }
                Pixmap pixmap = new Pixmap(fi);
                int n = Math.min(pixmap.width, pixmap.height);
                Pixmap pixmap2 = new Pixmap(64, 64);
                pixmap2.draw(pixmap, (pixmap.width - n) / 2, (pixmap.height - n) / 2, n, n, 0, 0, 64, 64);
                byte[] byArray = PixmapIO.writePngBytes((Pixmap)pixmap2);
                pixmap.dispose();
                pixmap2.dispose();
                HubClient.uploadAvatar(byArray, (Cons<Boolean>)((Cons)bl -> {
                    Vars.ui.showInfoToast(HubText.get(bl != false ? "avatar.done" : "avatar.fail"), 3.0f);
                    if (bl.booleanValue() && runnable != null) {
                        runnable.run();
                    }
                }));
            }
            catch (Throwable throwable) {
                Vars.ui.showInfoToast(HubText.get("avatar.fail"), 3.0f);
            }
        });
    }

    static {
        imgCache = new ObjectMap();
        waiting = new ObjectMap();
    }
}

