package lunarians.data;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/data/SlotData.class */
public class SlotData {
    public String schematicBase64 = null;
    public String iconName = null;

    public boolean hasSchematic() {
        return (this.schematicBase64 == null || this.schematicBase64.isEmpty()) ? false : true;
    }

    public void clear() {
        this.schematicBase64 = null;
        this.iconName = null;
    }

    public void set(String str, String str2) {
        this.schematicBase64 = str;
        this.iconName = str2;
    }

    public String serialize() {
        return (this.schematicBase64 == null ? "" : this.schematicBase64) + "|||" + (this.iconName != null ? this.iconName : "");
    }

    public static SlotData deserialize(String str) {
        SlotData slotData = new SlotData();
        if (str == null || str.isEmpty()) {
            return slotData;
        }
        String[] split = str.split("\\|\\|\\|", 2);
        if (split.length > 0 && !split[0].isEmpty()) {
            slotData.schematicBase64 = split[0];
        }
        if (split.length > 1 && !split[1].isEmpty()) {
            slotData.iconName = split[1];
        }
        return slotData;
    }
}
