/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.struct.Seq
 */
package lunarians.data;

import arc.struct.Seq;

public class UnitSlot {
    public String unit = "";
    public String command = "";
    public final Seq<String> items = new Seq();
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
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.items.size; ++i) {
            if (i > 0) {
                stringBuilder.append(',');
            }
            stringBuilder.append((String)this.items.get(i));
        }
        return this.unit + "|" + this.command + "|" + String.valueOf(stringBuilder) + "|" + this.maxUnits;
    }

    public static UnitSlot deserialize(String string) {
        UnitSlot unitSlot = new UnitSlot();
        if (string == null || string.isEmpty()) {
            return unitSlot;
        }
        String[] stringArray = string.split("\\|", -1);
        try {
            if (stringArray.length > 0) {
                unitSlot.unit = stringArray[0];
            }
            if (stringArray.length > 1) {
                unitSlot.command = stringArray[1];
            }
            if (stringArray.length > 2 && !stringArray[2].isEmpty()) {
                for (String string2 : stringArray[2].split(",")) {
                    if (string2.isEmpty()) continue;
                    unitSlot.items.add((Object)string2);
                }
            }
            if (stringArray.length > 3 && !stringArray[3].isEmpty()) {
                unitSlot.maxUnits = Math.max(0, Integer.parseInt(stringArray[3]));
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return unitSlot;
    }
}

