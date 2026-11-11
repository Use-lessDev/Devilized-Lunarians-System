/*
 * Decompiled with CFR 0.152.
 */
package lunarians.data;

public class ShieldSlot {
    public String unit = "";
    public int maxUnits = 0;
    public int followId = -1;
    public String followLabel = "";

    public boolean ready() {
        return !this.unit.isEmpty() && this.followId >= 0;
    }

    public ShieldSlot copy() {
        ShieldSlot shieldSlot = new ShieldSlot();
        shieldSlot.unit = this.unit;
        shieldSlot.maxUnits = this.maxUnits;
        shieldSlot.followId = this.followId;
        shieldSlot.followLabel = this.followLabel;
        return shieldSlot;
    }

    public String serialize() {
        return this.unit + "|" + this.maxUnits + "|" + this.followId + "|" + this.followLabel;
    }

    public static ShieldSlot deserialize(String string) {
        ShieldSlot shieldSlot = new ShieldSlot();
        if (string == null || string.isEmpty()) {
            return shieldSlot;
        }
        String[] stringArray = string.split("\\|", -1);
        try {
            if (stringArray.length > 0) {
                shieldSlot.unit = stringArray[0];
            }
            if (stringArray.length > 1 && !stringArray[1].isEmpty()) {
                shieldSlot.maxUnits = Math.max(0, Integer.parseInt(stringArray[1]));
            }
            if (stringArray.length > 2 && !stringArray[2].isEmpty()) {
                try {
                    shieldSlot.followId = Integer.parseInt(stringArray[2]);
                }
                catch (NumberFormatException numberFormatException) {
                    shieldSlot.followId = -1;
                    shieldSlot.followLabel = stringArray[2];
                }
            }
            if (stringArray.length > 3) {
                shieldSlot.followLabel = stringArray[3];
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return shieldSlot;
    }
}

