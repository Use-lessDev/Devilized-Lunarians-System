/*
 * Decompiled with CFR 0.152.
 */
package lunarians.data;

public class MoonSlot {
    public static final int UNSET = Integer.MIN_VALUE;
    public String unit = "";
    public int maxUnits = 0;
    public float radius = 8.0f;
    public float speed = 1.0f;
    public int x = Integer.MIN_VALUE;
    public int y = Integer.MIN_VALUE;
    public int followId = -1;
    public String followLabel = "";

    public MoonSlot copy() {
        MoonSlot moonSlot = new MoonSlot();
        moonSlot.unit = this.unit;
        moonSlot.maxUnits = this.maxUnits;
        moonSlot.radius = this.radius;
        moonSlot.speed = this.speed;
        moonSlot.x = this.x;
        moonSlot.y = this.y;
        moonSlot.followId = this.followId;
        moonSlot.followLabel = this.followLabel;
        return moonSlot;
    }

    public boolean ready() {
        return !this.unit.isEmpty() && this.radius > 0.0f;
    }

    public boolean hasTileCenter() {
        return this.x != Integer.MIN_VALUE && this.y != Integer.MIN_VALUE;
    }

    public boolean hasUnitCenter() {
        return this.followId >= 0;
    }

    public String serialize() {
        return this.unit + "|" + this.maxUnits + "|" + this.radius + "|" + this.x + "|" + this.y + "|" + this.followId + "|" + this.followLabel + "|" + this.speed;
    }

    public static MoonSlot deserialize(String string) {
        MoonSlot moonSlot = new MoonSlot();
        if (string == null || string.isEmpty()) {
            return moonSlot;
        }
        String[] stringArray = string.split("\\|", -1);
        try {
            if (stringArray.length > 0) {
                moonSlot.unit = stringArray[0];
            }
            if (stringArray.length > 1 && !stringArray[1].isEmpty()) {
                moonSlot.maxUnits = Math.max(0, Integer.parseInt(stringArray[1]));
            }
            if (stringArray.length > 2 && !stringArray[2].isEmpty()) {
                moonSlot.radius = Math.max(1.0f, Float.parseFloat(stringArray[2]));
            }
            if (stringArray.length > 4) {
                moonSlot.x = Integer.parseInt(stringArray[3]);
                moonSlot.y = Integer.parseInt(stringArray[4]);
            }
            if (stringArray.length > 5 && !stringArray[5].isEmpty()) {
                try {
                    moonSlot.followId = Integer.parseInt(stringArray[5]);
                }
                catch (NumberFormatException numberFormatException) {
                    moonSlot.followId = -1;
                }
            }
            if (stringArray.length > 6) {
                moonSlot.followLabel = stringArray[6];
            }
            if (stringArray.length > 7 && !stringArray[7].isEmpty()) {
                moonSlot.speed = Math.max(0.25f, Math.min(4.0f, Float.parseFloat(stringArray[7])));
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return moonSlot;
    }
}

