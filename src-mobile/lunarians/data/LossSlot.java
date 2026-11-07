package lunarians.data;

public class LossSlot {
  public float delay = 1.0F;
  
  public String item = "";
  
  public float perSec = 5.0F;
  
  public static LossSlot deserialize(String paramString) {
    LossSlot lossSlot = new LossSlot();
    this();
    if (paramString != null) {
      boolean bool = paramString.isEmpty();
      if (!bool) {
        String str = "\\|";
        int i = -1;
        float f = 0.0F / 0.0F;
        String[] arrayOfString = paramString.split(str, i);
        try {
          int j = arrayOfString.length;
          if (j > 0) {
            j = 0;
            float f1 = 0.0F;
            str = null;
            str = arrayOfString[0];
            lossSlot.item = str;
          } 
          j = arrayOfString.length;
          i = 1;
          f = Float.MIN_VALUE;
          if (j > i) {
            str = arrayOfString[i];
            float f1 = Float.parseFloat(str);
            i = 1133903872;
            f = 300.0F;
            f1 = Math.min(f, f1);
            i = 1056964608;
            f = 0.5F;
            f1 = Math.max(f, f1);
            lossSlot.perSec = f1;
          } 
          j = arrayOfString.length;
          i = 2;
          f = 2.8E-45F;
          if (j > i) {
            String str1 = arrayOfString[i];
            float f2 = Float.parseFloat(str1);
            j = 1114636288;
            float f1 = 60.0F;
            f2 = Math.min(f1, f2);
            j = 1065353216;
            f1 = 1.0F;
            f2 = Math.max(f1, f2);
            lossSlot.delay = f2;
          } 
        } catch (NumberFormatException numberFormatException) {}
      } 
    } 
    return lossSlot;
  }
  
  public String serialize() {
    StringBuilder stringBuilder = new StringBuilder();
    this();
    String str = this.item;
    stringBuilder.append(str);
    str = "|";
    stringBuilder.append(str);
    float f1 = this.perSec;
    stringBuilder.append(f1);
    stringBuilder.append(str);
    float f2 = this.delay;
    stringBuilder.append(f2);
    return stringBuilder.toString();
  }
}
