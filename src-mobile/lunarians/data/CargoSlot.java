package lunarians.data;

import arc.struct.IntSeq;

public class CargoSlot {
  public static final int UNSET = -2147483648;
  
  public int maxUnits = 0;
  
  public final IntSeq points;
  
  public String unit = "";
  
  public CargoSlot() {
    IntSeq intSeq = new IntSeq();
    this();
    this.points = intSeq;
  }
  
  public static CargoSlot deserialize(String paramString) {
    CargoSlot cargoSlot = new CargoSlot();
    this();
    if (paramString != null) {
      boolean bool = paramString.isEmpty();
      if (!bool) {
        String str = "\\|";
        byte b = -1;
        String[] arrayOfString = paramString.split(str, b);
        try {
          String[] arrayOfString1;
          int i = arrayOfString.length;
          b = 0;
          if (i > 0) {
            str = arrayOfString[0];
            cargoSlot.unit = str;
          } 
          i = arrayOfString.length;
          int j = 3;
          int k = 2;
          byte b1 = 1;
          if (i >= j) {
            str = arrayOfString[k];
            int m = 44;
            i = str.indexOf(m);
            if (i >= 0) {
              i = arrayOfString.length;
              if (i > b1) {
                str = arrayOfString[b1];
                boolean bool1 = str.isEmpty();
                if (!bool1) {
                  str = arrayOfString[b1];
                  int n = Integer.parseInt(str);
                  n = Math.max(0, n);
                  cargoSlot.maxUnits = n;
                } 
              } 
              String str1 = arrayOfString[k];
              str = ";";
              arrayOfString1 = str1.split(str);
              i = arrayOfString1.length;
              j = 0;
              Object object = null;
              while (j < i) {
                String str2 = arrayOfString1[j];
                boolean bool1 = str2.isEmpty();
                if (!bool1) {
                  String str3 = ",";
                  String[] arrayOfString2 = str2.split(str3);
                  int n = arrayOfString2.length;
                  if (n >= k) {
                    str3 = arrayOfString2[0];
                    str3 = str3.trim();
                    n = Integer.parseInt(str3);
                    String str4 = arrayOfString2[b1];
                    str4 = str4.trim();
                    m = Integer.parseInt(str4);
                    cargoSlot.addPoint(n, m);
                  } 
                } 
                j++;
              } 
              return cargoSlot;
            } 
          } 
          i = arrayOfString1.length;
          byte b2 = 4;
          if (i > b2) {
            str = arrayOfString1[b1];
            i = Integer.parseInt(str);
            String str2 = arrayOfString1[k];
            k = Integer.parseInt(str2);
            cargoSlot.addPoint(i, k);
            str = arrayOfString1[j];
            i = Integer.parseInt(str);
            String str1 = arrayOfString1[b2];
            j = Integer.parseInt(str1);
            cargoSlot.addPoint(i, j);
            i = arrayOfString1.length;
            j = 5;
            if (i > j) {
              str = arrayOfString1[j];
              boolean bool1 = str.isEmpty();
              if (!bool1) {
                String str3 = arrayOfString1[j];
                int m = Integer.parseInt(str3);
                m = Math.max(0, m);
                cargoSlot.maxUnits = m;
              } 
            } 
          } 
        } catch (Exception exception) {}
      } 
    } 
    return cargoSlot;
  }
  
  public void addPoint(int paramInt1, int paramInt2) {
    this.points.add(paramInt1);
    this.points.add(paramInt2);
  }
  
  public void clearPoints() {
    this.points.clear();
  }
  
  public CargoSlot copy() {
    CargoSlot cargoSlot = new CargoSlot();
    this();
    String str = this.unit;
    cargoSlot.unit = str;
    int i = this.maxUnits;
    cargoSlot.maxUnits = i;
    IntSeq intSeq1 = cargoSlot.points;
    IntSeq intSeq2 = this.points;
    intSeq1.addAll(intSeq2);
    return cargoSlot;
  }
  
  public int getAx() {
    boolean bool = hasA();
    return bool ? px(0) : (-1 << -1);
  }
  
  public int getAy() {
    boolean bool = hasA();
    return bool ? py(0) : (-1 << -1);
  }
  
  public int getBx() {
    boolean bool = hasB();
    if (bool) {
      int i = pointCount() + -1;
      return px(i);
    } 
    return -1 << -1;
  }
  
  public int getBy() {
    boolean bool = hasB();
    if (bool) {
      int i = pointCount() + -1;
      return py(i);
    } 
    return -1 << -1;
  }
  
  public boolean hasA() {
    int i = pointCount();
    byte b = 1;
    return (i >= b) ? b : false;
  }
  
  public boolean hasB() {
    int i = pointCount();
    byte b = 2;
    return (i >= b);
  }
  
  public int pointCount() {
    return this.points.size / 2;
  }
  
  public int px(int paramInt) {
    IntSeq intSeq = this.points;
    paramInt <<= 1;
    return intSeq.get(paramInt);
  }
  
  public int py(int paramInt) {
    IntSeq intSeq = this.points;
    paramInt = (paramInt << 1) + 1;
    return intSeq.get(paramInt);
  }
  
  public boolean ready() {
    String str = this.unit;
    boolean bool = str.isEmpty();
    if (!bool) {
      IntSeq intSeq = this.points;
      int i = intSeq.size;
      byte b = 4;
      if (i >= b)
        return true; 
    } 
    return false;
  }
  
  public void removeLastPoint() {
    IntSeq intSeq = this.points;
    int i = intSeq.size;
    int j = 2;
    if (i >= j) {
      intSeq = this.points;
      j = intSeq.size + -1;
      intSeq.removeIndex(j);
      intSeq = this.points;
      j = intSeq.size + -1;
      intSeq.removeIndex(j);
    } 
  }
  
  public String serialize() {
    int i = 0;
    int j = 0;
    StringBuilder stringBuilder = null;
    while (true) {
      IntSeq intSeq = this.points;
      int k = intSeq.size;
      if (j < k) {
        if (j > 0)
          k = j % 2; 
        k = j % 2;
        intSeq = this.points;
        intSeq.get(j);
        j++;
        continue;
      } 
      stringBuilder = new StringBuilder();
      this();
      String str = this.unit;
      stringBuilder.append(str);
      k = 124;
      stringBuilder.append(k);
      int m = this.maxUnits;
      stringBuilder.append(m);
      stringBuilder.append(k);
      k = pointCount();
      while (i < k) {
        if (i > 0) {
          m = 59;
          stringBuilder.append(m);
        } 
        m = px(i);
        stringBuilder.append(m);
        stringBuilder.append(',');
        m = py(i);
        stringBuilder.append(m);
        i++;
      } 
      return stringBuilder.toString();
    } 
  }
}
