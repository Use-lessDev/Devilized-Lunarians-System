package lunarians.data;

public class MoonSlot {
  public static final int UNSET = -2147483648;
  
  public int followId;
  
  public String followLabel;
  
  public int maxUnits;
  
  public float radius;
  
  public float speed;
  
  public String unit;
  
  public int x;
  
  public int y;
  
  public MoonSlot() {
    String str = "";
    this.unit = str;
    this.maxUnits = 0;
    this.radius = 8.0F;
    this.speed = 1.0F;
    int i = -1 << -1;
    this.x = i;
    this.y = i;
    this.followId = -1;
    this.followLabel = str;
  }
  
  public static MoonSlot deserialize(String paramString) {
    MoonSlot moonSlot = new MoonSlot();
    this();
    if (paramString != null) {
      boolean bool = paramString.isEmpty();
      if (!bool) {
        String str = "\\|";
        byte b = -1;
        String[] arrayOfString = paramString.split(str, b);
        try {
          int i = arrayOfString.length;
          int j = 0;
          float f = 0.0F;
          if (i > 0) {
            str = arrayOfString[0];
            moonSlot.unit = str;
          } 
          i = arrayOfString.length;
          byte b1 = 1;
          if (i > b1) {
            str = arrayOfString[b1];
            boolean bool1 = str.isEmpty();
            if (!bool1) {
              str = arrayOfString[b1];
              int k = Integer.parseInt(str);
              k = Math.max(0, k);
              moonSlot.maxUnits = k;
            } 
          } 
          i = arrayOfString.length;
          j = 2;
          f = 2.8E-45F;
          if (i > j) {
            str = arrayOfString[j];
            boolean bool1 = str.isEmpty();
            if (!bool1) {
              str = arrayOfString[j];
              float f1 = Float.parseFloat(str);
              j = 1065353216;
              f = 1.0F;
              f1 = Math.max(f, f1);
              moonSlot.radius = f1;
            } 
          } 
          i = arrayOfString.length;
          j = 4;
          f = 5.6E-45F;
          if (i > j) {
            i = 3;
            float f1 = 4.2E-45F;
            str = arrayOfString[i];
            i = Integer.parseInt(str);
            moonSlot.x = i;
            str = arrayOfString[j];
            i = Integer.parseInt(str);
            moonSlot.y = i;
          } 
          i = arrayOfString.length;
          j = 5;
          f = 7.0E-45F;
          if (i > j) {
            str = arrayOfString[j];
            boolean bool1 = str.isEmpty();
            if (!bool1)
              try {
                str = arrayOfString[j];
                int k = Integer.parseInt(str);
                moonSlot.followId = k;
              } catch (NumberFormatException numberFormatException) {
                moonSlot.followId = b;
              }  
          } 
          i = arrayOfString.length;
          b = 6;
          if (i > b) {
            str = arrayOfString[b];
            moonSlot.followLabel = str;
          } 
          i = arrayOfString.length;
          b = 7;
          if (i > b) {
            str = arrayOfString[b];
            boolean bool1 = str.isEmpty();
            if (!bool1) {
              String str1 = arrayOfString[b];
              float f2 = Float.parseFloat(str1);
              int k = 1082130432;
              float f1 = 4.0F;
              f2 = Math.min(f1, f2);
              k = 1048576000;
              f1 = 0.25F;
              f2 = Math.max(f1, f2);
              moonSlot.speed = f2;
            } 
          } 
        } catch (Exception exception) {}
      } 
    } 
    return moonSlot;
  }
  
  public MoonSlot copy() {
    MoonSlot moonSlot = new MoonSlot();
    this();
    String str = this.unit;
    moonSlot.unit = str;
    int i = this.maxUnits;
    moonSlot.maxUnits = i;
    float f = this.radius;
    moonSlot.radius = f;
    f = this.speed;
    moonSlot.speed = f;
    i = this.x;
    moonSlot.x = i;
    i = this.y;
    moonSlot.y = i;
    i = this.followId;
    moonSlot.followId = i;
    str = this.followLabel;
    moonSlot.followLabel = str;
    return moonSlot;
  }
  
  public boolean hasTileCenter() {
    int i = this.x;
    int j = -1 << -1;
    if (i != j) {
      i = this.y;
      if (i != j)
        return true; 
    } 
    return false;
  }
  
  public boolean hasUnitCenter() {
    int i = this.followId;
    return (i >= 0);
  }
  
  public boolean ready() {
    String str = this.unit;
    boolean bool = str.isEmpty();
    if (!bool) {
      float f = this.radius;
      int i = f cmp 0.0F;
      if (i > 0)
        return true; 
    } 
    return false;
  }
  
  public String serialize() {
    String str1 = this.unit;
    int i = this.maxUnits;
    float f1 = this.radius;
    int j = this.x;
    int k = this.y;
    int m = this.followId;
    String str2 = this.followLabel;
    float f2 = this.speed;
    StringBuilder stringBuilder = new StringBuilder();
    this();
    stringBuilder.append(str1);
    str1 = "|";
    stringBuilder.append(str1);
    stringBuilder.append(i);
    stringBuilder.append(str1);
    stringBuilder.append(f1);
    stringBuilder.append(str1);
    stringBuilder.append(j);
    stringBuilder.append(str1);
    stringBuilder.append(k);
    stringBuilder.append(str1);
    stringBuilder.append(m);
    stringBuilder.append(str1);
    stringBuilder.append(str2);
    stringBuilder.append(str1);
    stringBuilder.append(f2);
    return stringBuilder.toString();
  }
}
