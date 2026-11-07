package lunarians.data;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/data/AttackSlot.class */
public class AttackSlot {
    public static final int UNSET = Integer.MIN_VALUE;
    public String unit = "";
    public int maxUnits = 0;
    public int x = Integer.MIN_VALUE;
    public int y = Integer.MIN_VALUE;
    public float zoneRadius = 0.0f;
    public int standoffTiles = 0;

    public boolean ready() {
        return !this.unit.isEmpty();
    }

    public boolean hasZone() {
        return (this.x == Integer.MIN_VALUE || this.y == Integer.MIN_VALUE || this.zoneRadius <= 0.0f) ? false : true;
    }

    public AttackSlot copy() {
        AttackSlot attackSlot = new AttackSlot();
        attackSlot.unit = this.unit;
        attackSlot.maxUnits = this.maxUnits;
        attackSlot.x = this.x;
        attackSlot.y = this.y;
        attackSlot.zoneRadius = this.zoneRadius;
        attackSlot.standoffTiles = this.standoffTiles;
        return attackSlot;
    }

    public String serialize() {
        return this.unit + "|" + this.maxUnits + "|" + this.x + "|" + this.y + "|" + this.zoneRadius + "|" + this.standoffTiles;
    }

    public static AttackSlot deserialize(String str) {
        AttackSlot attackSlot = new AttackSlot();
        if (str == null || str.isEmpty()) {
            return attackSlot;
        }
        String[] split = str.split("\\|", -1);
        try {
            if (split.length > 0) {
                attackSlot.unit = split[0];
            }
            if (split.length > 1 && !split[1].isEmpty()) {
                attackSlot.maxUnits = Math.max(0, Integer.parseInt(split[1]));
            }
            if (split.length > 3) {
                attackSlot.x = Integer.parseInt(split[2]);
                attackSlot.y = Integer.parseInt(split[3]);
            }
            if (split.length > 4 && !split[4].isEmpty()) {
                attackSlot.zoneRadius = Math.max(0.0f, Float.parseFloat(split[4]));
            }
            if (split.length > 5 && !split[5].isEmpty()) {
                attackSlot.standoffTiles = Math.max(0, Integer.parseInt(split[5]));
            }
        } catch (Exception e) {
        }
        return attackSlot;
    }
}
