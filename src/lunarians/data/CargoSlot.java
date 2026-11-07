package lunarians.data;

import arc.struct.IntSeq;

public class CargoSlot {
  public static final int UNSET = -2147483648;
  
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
    return (!this.unit.isEmpty() && this.points.size >= 4);
  }
  
  public int pointCount() {
    return this.points.size / 2;
  }
  
  public int px(int paramInt) {
    return this.points.get(paramInt * 2);
  }
  
  public int py(int paramInt) {
    return this.points.get(paramInt * 2 + 1);
  }
  
  public void clearPoints() {
    this.points.clear();
  }
  
  public void addPoint(int paramInt1, int paramInt2) {
    this.points.add(paramInt1);
    this.points.add(paramInt2);
  }
  
  public void removeLastPoint() {
    if (this.points.size >= 2) {
      this.points.removeIndex(this.points.size - 1);
      this.points.removeIndex(this.points.size - 1);
    } 
  }
  
  public String serialize() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.unit).append('|').append(this.maxUnits).append('|');
    int i;
    for (i = 0; i < this.points.size; i++) {
      if (i > 0)
        stringBuilder.append((i % 2 == 0) ? 59 : 44); 
      if (i % 2 != 0 || i > 0);
      stringBuilder.append(this.points.get(i));
    } 
    stringBuilder = new StringBuilder();
    stringBuilder.append(this.unit).append('|').append(this.maxUnits).append('|');
    i = pointCount();
    for (byte b = 0; b < i; b++) {
      if (b > 0)
        stringBuilder.append(';'); 
      stringBuilder.append(px(b)).append(',').append(py(b));
    } 
    return stringBuilder.toString();
  }
  
  public static CargoSlot deserialize(String paramString) {
    CargoSlot cargoSlot = new CargoSlot();
    if (paramString == null || paramString.isEmpty())
      return cargoSlot; 
    String[] arrayOfString = paramString.split("\\|", -1);
    try {
      if (arrayOfString.length > 0)
        cargoSlot.unit = arrayOfString[0]; 
      if (arrayOfString.length >= 3 && arrayOfString[2].indexOf(',') >= 0) {
        if (arrayOfString.length > 1 && !arrayOfString[1].isEmpty())
          cargoSlot.maxUnits = Math.max(0, Integer.parseInt(arrayOfString[1])); 
        String[] arrayOfString1 = arrayOfString[2].split(";");
        for (String str : arrayOfString1) {
          if (!str.isEmpty()) {
            String[] arrayOfString2 = str.split(",");
            if (arrayOfString2.length >= 2)
              cargoSlot.addPoint(Integer.parseInt(arrayOfString2[0].trim()), Integer.parseInt(arrayOfString2[1].trim())); 
          } 
        } 
      } else if (arrayOfString.length > 4) {
        cargoSlot.addPoint(Integer.parseInt(arrayOfString[1]), Integer.parseInt(arrayOfString[2]));
        cargoSlot.addPoint(Integer.parseInt(arrayOfString[3]), Integer.parseInt(arrayOfString[4]));
        if (arrayOfString.length > 5 && !arrayOfString[5].isEmpty())
          cargoSlot.maxUnits = Math.max(0, Integer.parseInt(arrayOfString[5])); 
      } 
    } catch (Exception exception) {}
    return cargoSlot;
  }
  
  public boolean hasA() {
    return (pointCount() >= 1);
  }
  
  public boolean hasB() {
    return (pointCount() >= 2);
  }
  
  public int getAx() {
    return hasA() ? px(0) : Integer.MIN_VALUE;
  }
  
  public int getAy() {
    return hasA() ? py(0) : Integer.MIN_VALUE;
  }
  
  public int getBx() {
    return hasB() ? px(pointCount() - 1) : Integer.MIN_VALUE;
  }
  
  public int getBy() {
    return hasB() ? py(pointCount() - 1) : Integer.MIN_VALUE;
  }
}
