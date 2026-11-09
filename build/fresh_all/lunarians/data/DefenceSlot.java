/*
 * Decompiled with CFR 0.152.
 */
package lunarians.data;

public class DefenceSlot {
    public static final int UNSET = Integer.MIN_VALUE;
    public String unit = "";
    public int maxUnits = 0;
    public int x = Integer.MIN_VALUE;
    public int y = Integer.MIN_VALUE;
    public float radius = 12.0f;

    public boolean ready() {
        return !this.unit.isEmpty() && this.x != Integer.MIN_VALUE && this.y != Integer.MIN_VALUE && this.radius > 0.0f;
    }

    public DefenceSlot copy() {
        DefenceSlot defenceSlot = new DefenceSlot();
        defenceSlot.unit = this.unit;
        defenceSlot.maxUnits = this.maxUnits;
        defenceSlot.x = this.x;
        defenceSlot.y = this.y;
        defenceSlot.radius = this.radius;
        return defenceSlot;
    }

    public String serialize() {
        return this.unit + "|" + this.maxUnits + "|" + this.x + "|" + this.y + "|" + this.radius;
    }

    public static DefenceSlot deserialize(String string) {
        DefenceSlot defenceSlot = new DefenceSlot();
        if (string == null || string.isEmpty()) {
            return defenceSlot;
        }
        String[] stringArray = string.split("\\|", -1);
        try {
            if (stringArray.length > 0) {
                defenceSlot.unit = stringArray[0];
            }
            if (stringArray.length > 1 && !stringArray[1].isEmpty()) {
                defenceSlot.maxUnits = Math.max(0, Integer.parseInt(stringArray[1]));
            }
            if (stringArray.length > 3) {
                defenceSlot.x = Integer.parseInt(stringArray[2]);
                defenceSlot.y = Integer.parseInt(stringArray[3]);
            }
            if (stringArray.length > 4 && !stringArray[4].isEmpty()) {
                defenceSlot.radius = Math.max(1.0f, Float.parseFloat(stringArray[4]));
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return defenceSlot;
    }
}

