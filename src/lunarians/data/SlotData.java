/*
 * Decompiled with CFR 0.152.
 */
package lunarians.data;

public class SlotData {
    public String schematicBase64 = null;
    public String iconName = null;

    public boolean hasSchematic() {
        return this.schematicBase64 != null && !this.schematicBase64.isEmpty();
    }

    public void clear() {
        this.schematicBase64 = null;
        this.iconName = null;
    }

    public void set(String string, String string2) {
        this.schematicBase64 = string;
        this.iconName = string2;
    }

    public String serialize() {
        return (this.schematicBase64 == null ? "" : this.schematicBase64) + "|||" + (this.iconName != null ? this.iconName : "");
    }

    public static SlotData deserialize(String string) {
        SlotData slotData = new SlotData();
        if (string == null || string.isEmpty()) {
            return slotData;
        }
        String[] stringArray = string.split("\\|\\|\\|", 2);
        if (stringArray.length > 0 && !stringArray[0].isEmpty()) {
            slotData.schematicBase64 = stringArray[0];
        }
        if (stringArray.length > 1 && !stringArray[1].isEmpty()) {
            slotData.iconName = stringArray[1];
        }
        return slotData;
    }
}

