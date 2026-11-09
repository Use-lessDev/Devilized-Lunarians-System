/*
 * Decompiled with CFR 0.152.
 */
package lunarians.data;

public class UnitHealSlot {
    public String unit = "";
    public int maxUnits = 0;

    public boolean ready() {
        return !this.unit.isEmpty();
    }

    public UnitHealSlot copy() {
        UnitHealSlot unitHealSlot = new UnitHealSlot();
        unitHealSlot.unit = this.unit;
        unitHealSlot.maxUnits = this.maxUnits;
        return unitHealSlot;
    }

    public String serialize() {
        return this.unit + "|" + this.maxUnits;
    }

    public static UnitHealSlot deserialize(String string) {
        UnitHealSlot unitHealSlot = new UnitHealSlot();
        if (string == null || string.isEmpty()) {
            return unitHealSlot;
        }
        String[] stringArray = string.split("\\|", -1);
        try {
            if (stringArray.length > 0) {
                unitHealSlot.unit = stringArray[0];
            }
            if (stringArray.length > 1 && !stringArray[1].isEmpty()) {
                unitHealSlot.maxUnits = Math.max(0, Integer.parseInt(stringArray[1]));
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return unitHealSlot;
    }
}

