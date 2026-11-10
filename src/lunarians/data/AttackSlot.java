/*
 * Decompiled with CFR 0.152.
 */
package lunarians.data;

public class AttackSlot {
    public static final int UNSET = Integer.MIN_VALUE;
    public String unit = "";
    public int maxUnits = 0;
    public int x = Integer.MIN_VALUE;
    public int y = Integer.MIN_VALUE;
    public float zoneRadius = 0.0f;
    public int standoffTiles = 0;
    public int retreatPercent = 0;

    public boolean ready() {
        return !this.unit.isEmpty();
    }

    public boolean hasZone() {
        return this.x != Integer.MIN_VALUE && this.y != Integer.MIN_VALUE && this.zoneRadius > 0.0f;
    }

    public AttackSlot copy() {
        AttackSlot attackSlot = new AttackSlot();
        attackSlot.unit = this.unit;
        attackSlot.maxUnits = this.maxUnits;
        attackSlot.x = this.x;
        attackSlot.y = this.y;
        attackSlot.zoneRadius = this.zoneRadius;
        attackSlot.standoffTiles = this.standoffTiles;
        attackSlot.retreatPercent = this.retreatPercent;
        return attackSlot;
    }

    public String serialize() {
        return this.unit + "|" + this.maxUnits + "|" + this.x + "|" + this.y + "|" + this.zoneRadius + "|" + this.standoffTiles + "|" + this.retreatPercent;
    }

    public static AttackSlot deserialize(String string) {
        AttackSlot attackSlot = new AttackSlot();
        if (string == null || string.isEmpty()) {
            return attackSlot;
        }
        String[] stringArray = string.split("\\|", -1);
        try {
            if (stringArray.length > 0) {
                attackSlot.unit = stringArray[0];
            }
            if (stringArray.length > 1 && !stringArray[1].isEmpty()) {
                attackSlot.maxUnits = Math.max(0, Integer.parseInt(stringArray[1]));
            }
            if (stringArray.length > 3) {
                attackSlot.x = Integer.parseInt(stringArray[2]);
                attackSlot.y = Integer.parseInt(stringArray[3]);
            }
            if (stringArray.length > 4 && !stringArray[4].isEmpty()) {
                attackSlot.zoneRadius = Math.max(0.0f, Float.parseFloat(stringArray[4]));
            }
            if (stringArray.length > 5 && !stringArray[5].isEmpty()) {
                attackSlot.standoffTiles = Math.max(0, Integer.parseInt(stringArray[5]));
            }
            if (stringArray.length > 6 && !stringArray[6].isEmpty()) {
                attackSlot.retreatPercent = Math.max(0, Math.min(90, Integer.parseInt(stringArray[6])));
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return attackSlot;
    }
}

