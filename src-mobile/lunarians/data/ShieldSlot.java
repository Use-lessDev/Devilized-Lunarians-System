package lunarians.data;

public class ShieldSlot {
  public int followId;
  
  public String followLabel;
  
  public int maxUnits;
  
  public String unit;
  
  public ShieldSlot() {
    String str = "";
    this.unit = str;
    this.maxUnits = 0;
    this.followId = -1;
    this.followLabel = str;
  }
  
  public static ShieldSlot deserialize(String paramString) {
    ShieldSlot shieldSlot = new ShieldSlot();
    this();
    if (paramString != null) {
      boolean bool = paramString.isEmpty();
      if (!bool) {
        String str = "\\|";
        byte b = -1;
        String[] arrayOfString = paramString.split(str, b);
        try {
          int i = arrayOfString.length;
          byte b1 = 0;
          if (i > 0) {
            str = arrayOfString[0];
            shieldSlot.unit = str;
          } 
          i = arrayOfString.length;
          byte b2 = 1;
          if (i > b2) {
            str = arrayOfString[b2];
            boolean bool1 = str.isEmpty();
            if (!bool1) {
              str = arrayOfString[b2];
              int j = Integer.parseInt(str);
              j = Math.max(0, j);
              shieldSlot.maxUnits = j;
            } 
          } 
          i = arrayOfString.length;
          b1 = 2;
          if (i > b1) {
            str = arrayOfString[b1];
            boolean bool1 = str.isEmpty();
            if (!bool1)
              try {
                str = arrayOfString[b1];
                int j = Integer.parseInt(str);
                shieldSlot.followId = j;
              } catch (NumberFormatException numberFormatException) {
                shieldSlot.followId = b;
                str = arrayOfString[b1];
                shieldSlot.followLabel = str;
              }  
          } 
          i = arrayOfString.length;
          b = 3;
          if (i > b) {
            String str1 = arrayOfString[b];
            shieldSlot.followLabel = str1;
          } 
        } catch (Exception exception) {}
      } 
    } 
    return shieldSlot;
  }
  
  public ShieldSlot copy() {
    ShieldSlot shieldSlot = new ShieldSlot();
    this();
    String str = this.unit;
    shieldSlot.unit = str;
    int i = this.maxUnits;
    shieldSlot.maxUnits = i;
    i = this.followId;
    shieldSlot.followId = i;
    str = this.followLabel;
    shieldSlot.followLabel = str;
    return shieldSlot;
  }
  
  public boolean ready() {
    String str = this.unit;
    boolean bool = str.isEmpty();
    if (!bool) {
      int i = this.followId;
      if (i >= 0)
        return true; 
    } 
    return false;
  }
  
  public String serialize() {
    String str1 = this.unit;
    int i = this.maxUnits;
    int j = this.followId;
    String str2 = this.followLabel;
    StringBuilder stringBuilder = new StringBuilder();
    this();
    stringBuilder.append(str1);
    str1 = "|";
    stringBuilder.append(str1);
    stringBuilder.append(i);
    stringBuilder.append(str1);
    stringBuilder.append(j);
    stringBuilder.append(str1);
    stringBuilder.append(str2);
    return stringBuilder.toString();
  }
}
