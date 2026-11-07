package lunarians.data;

import arc.struct.IntSeq;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/data/CargoSlot.class */
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

    public int px(int i) {
        return this.points.get(i * 2);
    }

    public int py(int i) {
        return this.points.get((i * 2) + 1);
    }

    public void clearPoints() {
        this.points.clear();
    }

    public void addPoint(int i, int i2) {
        this.points.add(i);
        this.points.add(i2);
    }

    public void removeLastPoint() {
        if (this.points.size >= 2) {
            this.points.removeIndex(this.points.size - 1);
            this.points.removeIndex(this.points.size - 1);
        }
    }

    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.unit).append('|').append(this.maxUnits).append('|');
        for (int i = 0; i < this.points.size; i++) {
            if (i > 0) {
                sb.append(i % 2 == 0 ? ';' : ',');
            }
            if (i % 2 != 0 || i > 0) {
                sb.append(this.points.get(i));
            } else {
                sb.append(this.points.get(i));
            }
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.unit).append('|').append(this.maxUnits).append('|');
        int pointCount = pointCount();
        for (int i2 = 0; i2 < pointCount; i2++) {
            if (i2 > 0) {
                sb2.append(';');
            }
            sb2.append(px(i2)).append(',').append(py(i2));
        }
        return sb2.toString();
    }

    public static CargoSlot deserialize(String str) {
        CargoSlot cargoSlot = new CargoSlot();
        if (str == null || str.isEmpty()) {
            return cargoSlot;
        }
        String[] split = str.split("\\|", -1);
        try {
            if (split.length > 0) {
                cargoSlot.unit = split[0];
            }
            if (split.length >= 3 && split[2].indexOf(44) >= 0) {
                if (split.length > 1 && !split[1].isEmpty()) {
                    cargoSlot.maxUnits = Math.max(0, Integer.parseInt(split[1]));
                }
                for (String str2 : split[2].split(";")) {
                    if (!str2.isEmpty()) {
                        String[] split2 = str2.split(",");
                        if (split2.length >= 2) {
                            cargoSlot.addPoint(Integer.parseInt(split2[0].trim()), Integer.parseInt(split2[1].trim()));
                        }
                    }
                }
            } else if (split.length > 4) {
                cargoSlot.addPoint(Integer.parseInt(split[1]), Integer.parseInt(split[2]));
                cargoSlot.addPoint(Integer.parseInt(split[3]), Integer.parseInt(split[4]));
                if (split.length > 5 && !split[5].isEmpty()) {
                    cargoSlot.maxUnits = Math.max(0, Integer.parseInt(split[5]));
                }
            }
        } catch (Exception e) {
        }
        return cargoSlot;
    }

    public boolean hasA() {
        return pointCount() >= 1;
    }

    public boolean hasB() {
        return pointCount() >= 2;
    }

    public int getAx() {
        if (hasA()) {
            return px(0);
        }
        return Integer.MIN_VALUE;
    }

    public int getAy() {
        if (hasA()) {
            return py(0);
        }
        return Integer.MIN_VALUE;
    }

    public int getBx() {
        if (hasB()) {
            return px(pointCount() - 1);
        }
        return Integer.MIN_VALUE;
    }

    public int getBy() {
        if (hasB()) {
            return py(pointCount() - 1);
        }
        return Integer.MIN_VALUE;
    }
}
