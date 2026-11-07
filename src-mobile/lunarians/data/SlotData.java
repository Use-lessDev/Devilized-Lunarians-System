package lunarians.data;

public class SlotData {
  public String iconName = null;
  
  public String schematicBase64 = null;
  
  public static SlotData deserialize(String paramString) {
    SlotData slotData = new SlotData();
    this();
    if (paramString != null) {
      boolean bool = paramString.isEmpty();
      if (!bool) {
        String str = "\\|\\|\\|";
        byte b = 2;
        String[] arrayOfString = paramString.split(str, b);
        int i = arrayOfString.length;
        if (i > 0) {
          i = 0;
          str = null;
          String str1 = arrayOfString[0];
          boolean bool1 = str1.isEmpty();
          if (!bool1) {
            str = arrayOfString[0];
            slotData.schematicBase64 = str;
          } 
        } 
        i = arrayOfString.length;
        b = 1;
        if (i > b) {
          str = arrayOfString[b];
          boolean bool1 = str.isEmpty();
          if (!bool1) {
            String str1 = arrayOfString[b];
            slotData.iconName = str1;
          } 
        } 
      } 
    } 
    return slotData;
  }
  
  public void clear() {
    this.schematicBase64 = null;
    this.iconName = null;
  }
  
  public boolean hasSchematic() {
    String str = this.schematicBase64;
    if (str != null) {
      boolean bool = str.isEmpty();
      if (!bool)
        return true; 
    } 
    return false;
  }
  
  public String serialize() {
    StringBuilder stringBuilder = new StringBuilder();
    this();
    String str1 = this.schematicBase64;
    String str2 = "";
    if (str1 == null)
      str1 = str2; 
    stringBuilder.append(str1);
    stringBuilder.append("|||");
    str1 = this.iconName;
    if (str1 != null)
      str2 = str1; 
    stringBuilder.append(str2);
    return stringBuilder.toString();
  }
  
  public void set(String paramString1, String paramString2) {
    this.schematicBase64 = paramString1;
    this.iconName = paramString2;
  }
}
