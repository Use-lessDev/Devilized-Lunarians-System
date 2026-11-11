/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.struct.IntSeq
 */
package lunarians.data;

import arc.struct.IntSeq;

public class CargoSlot {
    public static final int UNSET = Integer.MIN_VALUE;
    public String unit = "";
    public int maxUnits = 0;
    public final IntSeq points = new IntSeq();

    public CargoSlot copy() {
        CargoSlot cargoSlot = new CargoSlot();
        cargoSlot.unit = this.unit;
        cargoSlot.maxUnits = this.maxUnits;
        cargoSlot.points.addAll(this.points);
        return cargoSlot;
    }

    public boolean ready() {
        return !this.unit.isEmpty() && this.points.size >= 4;
    }

    public int pointCount() {
        return this.points.size / 2;
    }

    public int px(int n) {
        return this.points.get(n * 2);
    }

    public int py(int n) {
        return this.points.get(n * 2 + 1);
    }

    public void clearPoints() {
        this.points.clear();
    }

    public void addPoint(int n, int n2) {
        this.points.add(n);
        this.points.add(n2);
    }

    public void removeLastPoint() {
        if (this.points.size >= 2) {
            this.points.removeIndex(this.points.size - 1);
            this.points.removeIndex(this.points.size - 1);
        }
    }

    public String serialize() {
        int n;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.unit).append('|').append(this.maxUnits).append('|');
        for (n = 0; n < this.points.size; ++n) {
            if (n > 0) {
                stringBuilder.append(n % 2 == 0 ? (char)';' : ',');
            }
            if (n % 2 != 0 || n > 0) {
                // empty if block
            }
            stringBuilder.append(this.points.get(n));
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(this.unit).append('|').append(this.maxUnits).append('|');
        n = this.pointCount();
        for (int i = 0; i < n; ++i) {
            if (i > 0) {
                stringBuilder.append(';');
            }
            stringBuilder.append(this.px(i)).append(',').append(this.py(i));
        }
        return stringBuilder.toString();
    }

    public static CargoSlot deserialize(String string) {
        CargoSlot cargoSlot = new CargoSlot();
        if (string == null || string.isEmpty()) {
            return cargoSlot;
        }
        String[] stringArray = string.split("\\|", -1);
        try {
            if (stringArray.length > 0) {
                cargoSlot.unit = stringArray[0];
            }
            if (stringArray.length >= 3 && stringArray[2].indexOf(44) >= 0) {
                String[] stringArray2;
                if (stringArray.length > 1 && !stringArray[1].isEmpty()) {
                    cargoSlot.maxUnits = Math.max(0, Integer.parseInt(stringArray[1]));
                }
                for (String string2 : stringArray2 = stringArray[2].split(";")) {
                    String[] stringArray3;
                    if (string2.isEmpty() || (stringArray3 = string2.split(",")).length < 2) continue;
                    cargoSlot.addPoint(Integer.parseInt(stringArray3[0].trim()), Integer.parseInt(stringArray3[1].trim()));
                }
            } else if (stringArray.length > 4) {
                cargoSlot.addPoint(Integer.parseInt(stringArray[1]), Integer.parseInt(stringArray[2]));
                cargoSlot.addPoint(Integer.parseInt(stringArray[3]), Integer.parseInt(stringArray[4]));
                if (stringArray.length > 5 && !stringArray[5].isEmpty()) {
                    cargoSlot.maxUnits = Math.max(0, Integer.parseInt(stringArray[5]));
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return cargoSlot;
    }

    public boolean hasA() {
        return this.pointCount() >= 1;
    }

    public boolean hasB() {
        return this.pointCount() >= 2;
    }

    public int getAx() {
        return this.hasA() ? this.px(0) : Integer.MIN_VALUE;
    }

    public int getAy() {
        return this.hasA() ? this.py(0) : Integer.MIN_VALUE;
    }

    public int getBx() {
        return this.hasB() ? this.px(this.pointCount() - 1) : Integer.MIN_VALUE;
    }

    public int getBy() {
        return this.hasB() ? this.py(this.pointCount() - 1) : Integer.MIN_VALUE;
    }
}

