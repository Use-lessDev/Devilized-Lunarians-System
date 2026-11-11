/*
 * Decompiled with CFR 0.152.
 */
package lunarians.data;

public class HealSlot {
    public String unit = "";
    public int maxUnits = 0;

    public boolean ready() {
        return !this.unit.isEmpty();
    }

    public HealSlot copy() {
        HealSlot healSlot = new HealSlot();
        healSlot.unit = this.unit;
        healSlot.maxUnits = this.maxUnits;
        return healSlot;
    }

    public String serialize() {
        return this.unit + "|" + this.maxUnits;
    }

    public static HealSlot deserialize(String string) {
        HealSlot healSlot = new HealSlot();
        if (string == null || string.isEmpty()) {
            return healSlot;
        }
        String[] stringArray = string.split("\\|", -1);
        try {
            if (stringArray.length > 0) {
                healSlot.unit = stringArray[0];
            }
            if (stringArray.length > 1 && !stringArray[1].isEmpty()) {
                healSlot.maxUnits = Math.max(0, Integer.parseInt(stringArray[1]));
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return healSlot;
    }
}

