package lunarians.data;

import arc.struct.Seq;

public class UnitSlot {
  public String command;
  
  public final Seq items;
  
  public int maxUnits;
  
  public String unit;
  
  public UnitSlot() {
    String str = "";
    this.unit = str;
    this.command = str;
    Seq seq = new Seq();
    this();
    this.items = seq;
    this.maxUnits = 0;
  }
  
  public static UnitSlot deserialize(String paramString) {
    UnitSlot unitSlot = new UnitSlot();
    this();
    if (paramString != null) {
      boolean bool = paramString.isEmpty();
      if (!bool) {
        String str = "\\|";
        byte b = -1;
        String[] arrayOfString = paramString.split(str, b);
        try {
          int i = arrayOfString.length;
          b = 0;
          if (i > 0) {
            str = arrayOfString[0];
            unitSlot.unit = str;
          } 
          i = arrayOfString.length;
          int j = 1;
          if (i > j) {
            str = arrayOfString[j];
            unitSlot.command = str;
          } 
          i = arrayOfString.length;
          j = 2;
          if (i > j) {
            str = arrayOfString[j];
            boolean bool1 = str.isEmpty();
            if (!bool1) {
              str = arrayOfString[j];
              String str1 = ",";
              for (String str2 : str.split(str1)) {
                boolean bool2 = str2.isEmpty();
                if (!bool2) {
                  Seq seq = unitSlot.items;
                  seq.add(str2);
                } 
              } 
            } 
          } 
          i = arrayOfString.length;
          j = 3;
          if (i > j) {
            str = arrayOfString[j];
            boolean bool1 = str.isEmpty();
            if (!bool1) {
              String str1 = arrayOfString[j];
              int k = Integer.parseInt(str1);
              k = Math.max(0, k);
              unitSlot.maxUnits = k;
            } 
          } 
        } catch (Exception exception) {}
      } 
    } 
    return unitSlot;
  }
  
  public UnitSlot copy() {
    UnitSlot unitSlot = new UnitSlot();
    this();
    String str = this.unit;
    unitSlot.unit = str;
    str = this.command;
    unitSlot.command = str;
    int i = this.maxUnits;
    unitSlot.maxUnits = i;
    Seq seq1 = unitSlot.items;
    Seq seq2 = this.items;
    seq1.addAll(seq2);
    return unitSlot;
  }
  
  public boolean ready() {
    return this.unit.isEmpty() ^ true;
  }
  
  public String serialize() {
    StringBuilder stringBuilder = new StringBuilder();
    this();
    int i = 0;
    String str = null;
    while (true) {
      Seq seq = this.items;
      int j = seq.size;
      if (i < j) {
        if (i > 0) {
          j = 44;
          stringBuilder.append(j);
        } 
        String str3 = (String)this.items.get(i);
        stringBuilder.append(str3);
        i++;
        continue;
      } 
      str = this.unit;
      String str2 = this.command;
      String str1 = String.valueOf(stringBuilder);
      int k = this.maxUnits;
      StringBuilder stringBuilder1 = new StringBuilder();
      this();
      stringBuilder1.append(str);
      str = "|";
      stringBuilder1.append(str);
      stringBuilder1.append(str2);
      stringBuilder1.append(str);
      stringBuilder1.append(str1);
      stringBuilder1.append(str);
      stringBuilder1.append(k);
      return stringBuilder1.toString();
    } 
  }
}
