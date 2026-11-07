package lunarians.data;

public class HealSlot {
  public int maxUnits = 0;
  
  public String unit = "";
  
  public static HealSlot deserialize(String paramString) {
    HealSlot healSlot = new HealSlot();
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
            healSlot.unit = str;
          } 
          i = arrayOfString.length;
          byte b1 = 1;
          if (i > b1) {
            str = arrayOfString[b1];
            boolean bool1 = str.isEmpty();
            if (!bool1) {
              String str1 = arrayOfString[b1];
              int j = Integer.parseInt(str1);
              j = Math.max(0, j);
              healSlot.maxUnits = j;
            } 
          } 
        } catch (Exception exception) {}
      } 
    } 
    return healSlot;
  }
  
  public HealSlot copy() {
    HealSlot healSlot = new HealSlot();
    this();
    String str = this.unit;
    healSlot.unit = str;
    int i = this.maxUnits;
    healSlot.maxUnits = i;
    return healSlot;
  }
  
  public boolean ready() {
    return this.unit.isEmpty() ^ true;
  }
  
  public String serialize() {
    String str = this.unit;
    int i = this.maxUnits;
    StringBuilder stringBuilder = new StringBuilder();
    this();
    stringBuilder.append(str);
    stringBuilder.append("|");
    stringBuilder.append(i);
    return stringBuilder.toString();
  }
}
