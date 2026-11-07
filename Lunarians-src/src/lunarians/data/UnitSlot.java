package lunarians.data;

import arc.struct.Seq;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/data/UnitSlot.class */
public class UnitSlot {
    public String unit = "";
    public String command = "";
    public final Seq<String> items = new Seq<>();
    public int maxUnits = 0;

    public boolean ready() {
        return !this.unit.isEmpty();
    }

    public UnitSlot copy() {
        UnitSlot unitSlot = new UnitSlot();
        unitSlot.unit = this.unit;
        unitSlot.command = this.command;
        unitSlot.maxUnits = this.maxUnits;
        unitSlot.items.addAll(this.items);
        return unitSlot;
    }

    public String serialize() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.items.size; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append((String) this.items.get(i));
        }
        return this.unit + "|" + this.command + "|" + String.valueOf(sb) + "|" + this.maxUnits;
    }

    public static UnitSlot deserialize(String str) {
        UnitSlot unitSlot = new UnitSlot();
        if (str == null || str.isEmpty()) {
            return unitSlot;
        }
        String[] split = str.split("\\|", -1);
        try {
            if (split.length > 0) {
                unitSlot.unit = split[0];
            }
            if (split.length > 1) {
                unitSlot.command = split[1];
            }
            if (split.length > 2 && !split[2].isEmpty()) {
                for (String str2 : split[2].split(",")) {
                    if (!str2.isEmpty()) {
                        unitSlot.items.add(str2);
                    }
                }
            }
            if (split.length > 3 && !split[3].isEmpty()) {
                unitSlot.maxUnits = Math.max(0, Integer.parseInt(split[3]));
            }
        } catch (Exception e) {
        }
        return unitSlot;
    }
}
