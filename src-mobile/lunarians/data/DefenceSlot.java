package lunarians.data;

public class DefenceSlot {
  public static final int UNSET = -2147483648;
  
  public int maxUnits = 0;
  
  public float radius;
  
  public String unit = "";
  
  public int x;
  
  public int y;
  
  public DefenceSlot() {
    int i = -1 << -1;
    this.x = i;
    this.y = i;
    this.radius = 12.0F;
  }
  
  public static DefenceSlot deserialize(String paramString) {
    DefenceSlot defenceSlot = new DefenceSlot();
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
            defenceSlot.unit = str;
          } 
          i = arrayOfString.length;
          byte b1 = 1;
          if (i > b1) {
            str = arrayOfString[b1];
            boolean bool1 = str.isEmpty();
            if (!bool1) {
              str = arrayOfString[b1];
              int j = Integer.parseInt(str);
              j = Math.max(0, j);
              defenceSlot.maxUnits = j;
            } 
          } 
          i = arrayOfString.length;
          b = 3;
          if (i > b) {
            i = 2;
            float f = 2.8E-45F;
            str = arrayOfString[i];
            i = Integer.parseInt(str);
            defenceSlot.x = i;
            str = arrayOfString[b];
            i = Integer.parseInt(str);
            defenceSlot.y = i;
          } 
          i = arrayOfString.length;
          b = 4;
          if (i > b) {
            str = arrayOfString[b];
            boolean bool1 = str.isEmpty();
            if (!bool1) {
              String str1 = arrayOfString[b];
              float f2 = Float.parseFloat(str1);
              int j = 1065353216;
              float f1 = 1.0F;
              f2 = Math.max(f1, f2);
              defenceSlot.radius = f2;
            } 
          } 
        } catch (Exception exception) {}
      } 
    } 
    return defenceSlot;
  }
  
  public DefenceSlot copy() {
    DefenceSlot defenceSlot = new DefenceSlot();
    this();
    String str = this.unit;
    defenceSlot.unit = str;
    int i = this.maxUnits;
    defenceSlot.maxUnits = i;
    i = this.x;
    defenceSlot.x = i;
    i = this.y;
    defenceSlot.y = i;
    float f = this.radius;
    defenceSlot.radius = f;
    return defenceSlot;
  }
  
  public boolean ready() {
    String str = this.unit;
    boolean bool = str.isEmpty();
    if (!bool) {
      int i = this.x;
      int j = -1 << -1;
      if (i != j) {
        i = this.y;
        if (i != j) {
          float f = this.radius;
          j = 0;
          i = f cmp 0.0F;
          if (i > 0)
            return true; 
        } 
      } 
    } 
    return false;
  }
  
  public String serialize() {
    String str = this.unit;
    int i = this.maxUnits;
    int j = this.x;
    int k = this.y;
    float f = this.radius;
    StringBuilder stringBuilder = new StringBuilder();
    this();
    stringBuilder.append(str);
    str = "|";
    stringBuilder.append(str);
    stringBuilder.append(i);
    stringBuilder.append(str);
    stringBuilder.append(j);
    stringBuilder.append(str);
    stringBuilder.append(k);
    stringBuilder.append(str);
    stringBuilder.append(f);
    return stringBuilder.toString();
  }
}
