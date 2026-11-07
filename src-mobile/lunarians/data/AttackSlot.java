package lunarians.data;

public class AttackSlot {
  public static final int UNSET = -2147483648;
  
  public int maxUnits = 0;
  
  public int standoffTiles;
  
  public String unit = "";
  
  public int x;
  
  public int y;
  
  public float zoneRadius;
  
  public AttackSlot() {
    int i = -1 << -1;
    this.x = i;
    this.y = i;
    this.zoneRadius = 0.0F;
    this.standoffTiles = 0;
  }
  
  public static AttackSlot deserialize(String paramString) {
    AttackSlot attackSlot = new AttackSlot();
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
            attackSlot.unit = str;
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
              attackSlot.maxUnits = j;
            } 
          } 
          i = arrayOfString.length;
          b1 = 3;
          if (i > b1) {
            i = 2;
            float f = 2.8E-45F;
            str = arrayOfString[i];
            i = Integer.parseInt(str);
            attackSlot.x = i;
            str = arrayOfString[b1];
            i = Integer.parseInt(str);
            attackSlot.y = i;
          } 
          i = arrayOfString.length;
          b1 = 4;
          if (i > b1) {
            str = arrayOfString[b1];
            boolean bool1 = str.isEmpty();
            if (!bool1) {
              str = arrayOfString[b1];
              float f = Float.parseFloat(str);
              b1 = 0;
              f = Math.max(0.0F, f);
              attackSlot.zoneRadius = f;
            } 
          } 
          i = arrayOfString.length;
          b1 = 5;
          if (i > b1) {
            str = arrayOfString[b1];
            boolean bool1 = str.isEmpty();
            if (!bool1) {
              String str1 = arrayOfString[b1];
              int j = Integer.parseInt(str1);
              j = Math.max(0, j);
              attackSlot.standoffTiles = j;
            } 
          } 
        } catch (Exception exception) {}
      } 
    } 
    return attackSlot;
  }
  
  public AttackSlot copy() {
    AttackSlot attackSlot = new AttackSlot();
    this();
    String str = this.unit;
    attackSlot.unit = str;
    int i = this.maxUnits;
    attackSlot.maxUnits = i;
    i = this.x;
    attackSlot.x = i;
    i = this.y;
    attackSlot.y = i;
    float f = this.zoneRadius;
    attackSlot.zoneRadius = f;
    i = this.standoffTiles;
    attackSlot.standoffTiles = i;
    return attackSlot;
  }
  
  public boolean hasZone() {
    int i = this.x;
    int j = -1 << -1;
    if (i != j) {
      i = this.y;
      if (i != j) {
        float f = this.zoneRadius;
        j = 0;
        i = f cmp 0.0F;
        if (i > 0)
          return true; 
      } 
    } 
    return false;
  }
  
  public boolean ready() {
    return this.unit.isEmpty() ^ true;
  }
  
  public String serialize() {
    StringBuilder stringBuilder = new StringBuilder();
    this();
    String str = this.unit;
    stringBuilder.append(str);
    str = "|";
    stringBuilder.append(str);
    int i = this.maxUnits;
    stringBuilder.append(i);
    stringBuilder.append(str);
    i = this.x;
    stringBuilder.append(i);
    stringBuilder.append(str);
    i = this.y;
    stringBuilder.append(i);
    stringBuilder.append(str);
    float f = this.zoneRadius;
    stringBuilder.append(f);
    stringBuilder.append(str);
    int j = this.standoffTiles;
    stringBuilder.append(j);
    return stringBuilder.toString();
  }
}
