package lunarians;

import arc.Core;
import arc.Events;
import arc.Settings;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.Element;
import arc.scene.ui.Button;
import arc.scene.ui.Label;
import arc.scene.ui.Slider;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Time;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import mindustry.Vars;
import mindustry.core.UI;
import mindustry.core.World;
import mindustry.game.EventType;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public class Formations {
  public static final int ARC = 2;
  
  private static final String K_APPR = "lunarians-form-approach";
  
  private static final String K_CENTER = "lunarians-form-center";
  
  private static final String K_FACE = "lunarians-form-face";
  
  private static final String K_GDELAY = "lunarians-form-grad-delay";
  
  private static final String K_GRAD = "lunarians-form-grad";
  
  private static final String K_LAYERS = "lunarians-form-layers";
  
  private static final String K_LOOP = "lunarians-form-loop";
  
  private static final String K_LWAIT = "lunarians-form-loop-wait";
  
  private static final String K_MODE = "lunarians-form-mode";
  
  private static final String K_PER = "lunarians-form-per";
  
  private static final String K_REFORM = "lunarians-form-reform";
  
  private static final String K_SPACE = "lunarians-form-spacing";
  
  public static final int LINE = 1;
  
  public static final int OFF = 0;
  
  public static final int RING = 3;
  
  private static final float TWO_PI = 6.2831855F;
  
  private static final HashMap curLegs;
  
  private static Object lastGroup;
  
  private static final HashMap loopA;
  
  private static boolean loopActive;
  
  private static final HashMap loopB;
  
  private static boolean loopToB;
  
  private static float loopWaitUntil;
  
  private static float nextCheck;
  
  private static float nextLoop;
  
  private static final ArrayList pending;
  
  private static boolean reformed;
  
  private static final HashMap retries;
  
  static {
    HashMap<Object, Object> hashMap1 = new HashMap<Object, Object>();
    this();
    curLegs = hashMap1;
    hashMap1 = new HashMap<Object, Object>();
    this();
    retries = hashMap1;
    nextCheck = 0.0F;
    reformed = false;
    ArrayList arrayList = new ArrayList();
    this();
    pending = arrayList;
    HashMap<Object, Object> hashMap2 = new HashMap<Object, Object>();
    this();
    loopA = hashMap2;
    hashMap2 = new HashMap<Object, Object>();
    this();
    loopB = hashMap2;
    loopActive = false;
    loopToB = true;
    loopWaitUntil = 0.0F;
    nextLoop = 0.0F;
  }
  
  private static void apply(Seq paramSeq, Vec2 paramVec2, int paramInt) {
    Vec2 vec2 = paramVec2;
    Seq seq = paramSeq;
    int i = paramInt;
    int j = paramSeq.size;
    Iterator<Unit> iterator = paramSeq.iterator();
    int k = 1090519040;
    float f1 = 8.0F;
    int m = 0;
    float f2 = 0.0F;
    Vec2[] arrayOfVec2 = null;
    float f3 = 0.0F;
    int n = 1090519040;
    float f4 = 8.0F;
    while (true) {
      boolean bool = iterator.hasNext();
      if (bool) {
        Unit unit = iterator.next();
        float f9 = unit.x;
        f2 += f9;
        f9 = unit.y;
        f3 += f9;
        float f10 = unit.hitSize;
        f4 = Math.max(f4, f10);
        continue;
      } 
      float f6 = j;
      f2 /= f6;
      f3 /= f6;
      f6 = spacing() * f1;
      f1 = 1.15F;
      f4 *= f1;
      f6 = Math.max(f6, f4);
      k = face();
      n = 1;
      f4 = Float.MIN_VALUE;
      if (k == n) {
        k = 1070141403;
        f1 = 1.5707964F;
      } else {
        f1 = vec2.y - f3;
        float f = vec2.x - f2;
        f1 = Mathf.atan2(f1, f);
      } 
      float[] arrayOfFloat = layout(i, j, f6, f1);
      float f5 = Mathf.cos(f1);
      float f7 = Mathf.sin(f1);
      float f8 = -f7;
      ArrayList<Integer> arrayList = new ArrayList();
      this();
      int i1 = 0;
      ArrayList<Unit> arrayList1 = null;
      while (i1 < j) {
        Integer integer = Integer.valueOf(i1);
        arrayList.add(integer);
        i1++;
      } 
      arrayList1 = new ArrayList();
      this();
      Iterator<Object> iterator1 = seq.iterator();
      while (true) {
        float f10;
        float f11;
        boolean bool1 = iterator1.hasNext();
        if (bool1) {
          Unit unit1 = (Unit)iterator1.next();
          Unit unit2 = unit1;
          unit2 = unit1;
          arrayList1.add(unit2);
          continue;
        } 
        n = 3;
        f4 = 4.2E-45F;
        int i2 = 1078530011;
        float f9 = 3.1415927F;
        if (i == n) {
          f4 = f1 + f9;
          int i4 = 1078530011;
          f10 = 3.1415927F;
          Formations$18 formations$18 = new Formations$18();
          this(arrayOfFloat, f4);
          Comparator<?> comparator1 = Comparator.comparingDouble(formations$18);
          arrayList.sort(comparator1);
          Formations$19 formations$19 = new Formations$19();
          this(f3, f2, f4);
          Comparator<?> comparator2 = Comparator.comparingDouble(formations$19);
          arrayList1.sort(comparator2);
        } else {
          int i4 = 1078530011;
          f10 = 3.1415927F;
          Formations$20 formations$20 = new Formations$20();
          this(arrayOfFloat, f8, f5);
          Comparator<?> comparator2 = Comparator.comparingDouble(formations$20);
          Formations$21 formations$21 = new Formations$21();
          this(arrayOfFloat, f5, f7);
          comparator2 = comparator2.thenComparingDouble(formations$21);
          arrayList.sort(comparator2);
          Formations$22 formations$22 = new Formations$22();
          this(f2, f8, f3, f5);
          Comparator<?> comparator1 = Comparator.comparingDouble(formations$22);
          Formations$23 formations$23 = new Formations$23();
          this(f2, f5, f3, f7);
          comparator1 = comparator1.thenComparingDouble(formations$23);
          arrayList1.sort(comparator1);
        } 
        n = center();
        i2 = 1;
        f9 = Float.MIN_VALUE;
        if (n == i2) {
          Unit unit = (Unit)seq.first();
          int i4 = arrayList1.indexOf(unit);
          n = 0;
          Object object = null;
          i4 = Math.max(0, i4);
          Integer integer1 = arrayList.get(i4);
          i4 = integer1.intValue();
          f4 = vec2.x;
          i4 <<= i2;
          float f13 = arrayOfFloat[i4];
          f4 -= f13;
          f11 = vec2.y;
          i4 += i2;
          float f14 = arrayOfFloat[i4];
          f11 -= f14;
        } else {
          byte b = 2;
          float f = 2.8E-45F;
          if (n == b) {
            f4 = Core.camera.position.x;
            vec2 = Core.camera.position;
            f11 = vec2.y;
          } else {
            f4 = vec2.x;
            f11 = vec2.y;
          } 
        } 
        curLegs.clear();
        retries.clear();
        loopA.clear();
        loopB.clear();
        int i3 = 0;
        float f12 = 0.0F;
        Integer integer = null;
        reformed = false;
        f1 += f10;
        float[] arrayOfFloat1 = layout(i, j, f6, f1);
        ArrayList<Integer> arrayList2 = new ArrayList();
        this();
        ArrayList<Integer> arrayList3 = new ArrayList();
        this();
        i2 = 0;
        f9 = 0.0F;
        iterator1 = null;
        while (true) {
          ArrayList<Integer> arrayList5;
          float f = f11;
          if (i2 < j) {
            Integer integer1 = Integer.valueOf(i2);
            arrayList3.add(integer1);
            i2++;
            continue;
          } 
          Formations$24 formations$24 = new Formations$24();
          this(arrayOfFloat, arrayList, f5, f7);
          Comparator<?> comparator = Comparator.comparingDouble(formations$24);
          arrayList3.sort(comparator);
          i2 = 0;
          f9 = 0.0F;
          iterator1 = null;
          while (i2 < j) {
            Unit unit = arrayList1.get(i2);
            integer = arrayList.get(i2);
            i3 = integer.intValue();
            float[] arrayOfFloat2 = arrayOfFloat1;
            HashMap<Integer, Vec2[]> hashMap = loopB;
            ArrayList<Integer> arrayList6 = arrayList3;
            Integer integer1 = Integer.valueOf(unit.id);
            float f13 = Float.MIN_VALUE;
            i3 <<= 1;
            float f14 = arrayOfFloat[i3];
            float f15 = f2;
            f2 = f4 + f14;
            int i4 = i3 + 1;
            float f16 = arrayOfFloat[i4];
            float f17 = f3;
            f3 = f + f16;
            arrayOfVec2 = makeLeg(unit, f2, f3, f5, f7);
            hashMap.put(integer1, arrayOfVec2);
            hashMap = loopA;
            k = unit.id;
            integer1 = Integer.valueOf(k);
            f2 = arrayOfFloat2[i3];
            f2 = f15 + f2;
            f3 = arrayOfFloat2[i4];
            f3 = f17 + f3;
            f12 = f4;
            f4 = -f5;
            Vec2[] arrayOfVec21 = makeLeg(unit, f2, f3, f4, f8);
            hashMap.put(integer1, arrayOfVec21);
            i2++;
            float[] arrayOfFloat3 = arrayOfFloat2;
            f4 = f12;
            arrayList5 = arrayList6;
            f2 = f15;
            f3 = f17;
          } 
          ArrayList<Integer> arrayList4 = arrayList5;
          HashMap hashMap1 = curLegs;
          HashMap hashMap2 = loopB;
          hashMap1.putAll(hashMap2);
          Iterator<Integer> iterator2 = arrayList5.iterator();
          while (true) {
            boolean bool2 = iterator2.hasNext();
            if (bool2) {
              int i5 = ((Integer)iterator2.next()).intValue();
              i5 = ((Unit)arrayList1.get(i5)).id;
              Integer integer1 = Integer.valueOf(i5);
              arrayList2.add(integer1);
              continue;
            } 
            HashMap hashMap = curLegs;
            i2 = 1;
            f9 = Float.MIN_VALUE;
            dispatch(arrayList2, hashMap, i2);
            loopActive = loop();
            loopToB = i2;
            loopWaitUntil = 0.0F;
            nextLoop = Time.time + 60.0F;
            boolean bool3 = false;
            lastGroup = null;
            f11 = Time.time;
            int i4 = 1106247680;
            float f13 = 30.0F;
            f11 += f13;
            nextCheck = f11;
            UI uI = Vars.ui;
            if (uI != null) {
              bool3 = loopActive;
              if (!bool3) {
                String str3;
                uI = Vars.ui;
                StringBuilder stringBuilder = new StringBuilder();
                this();
                String str2 = modeName(i);
                stringBuilder.append(str2);
                str2 = " x";
                stringBuilder.append(str2);
                stringBuilder.append(j);
                i2 = 1;
                f9 = Float.MIN_VALUE;
                if (j > i2) {
                  StringBuilder stringBuilder1 = new StringBuilder();
                  String str4 = " (";
                  this(str4);
                  i1 = perLayer();
                  k = 14;
                  f1 = 2.0E-44F;
                  if (i1 > 0) {
                    i1 = perLayer();
                    i2 = 1;
                    f9 = Float.MIN_VALUE;
                  } else {
                    i1 = layers();
                    if (i1 > 0) {
                      i1 = layers() + j;
                      i2 = 1;
                      f9 = Float.MIN_VALUE;
                      i1 -= i2;
                      m = layers();
                      i1 /= m;
                    } else {
                      i2 = 1;
                      f9 = Float.MIN_VALUE;
                      if (i == i2) {
                        i1 = 14;
                      } else {
                        i1 = j;
                      } 
                    } 
                  } 
                  i1 = Math.min(j, i1);
                  i1 = Math.max(i2, i1) + j - i2;
                  m = perLayer();
                  if (m > 0) {
                    k = perLayer();
                  } else {
                    m = layers();
                    if (m > 0) {
                      i = layers() + j - i2;
                      k = layers();
                      k = i / k;
                    } else if (i != i2) {
                      k = j;
                    } 
                  } 
                  i = Math.min(j, k);
                  i = Math.max(i2, i);
                  i1 /= i;
                  stringBuilder1.append(i1);
                  String str5 = " camadas)";
                  str3 = tr(" layers)", str5);
                  stringBuilder1.append(str3);
                  str3 = stringBuilder1.toString();
                } else {
                  str3 = "";
                } 
                stringBuilder.append(str3);
                String str1 = stringBuilder.toString();
                i = 1069547520;
                float f14 = 1.5F;
                uI.showInfoToast(str1, f14);
              } 
            } 
            return;
          } 
          break;
        } 
        break;
      } 
      break;
    } 
  }
  
  public static boolean approach() {
    return Core.settings.getBool("lunarians-form-approach", true);
  }
  
  public static int center() {
    return Core.settings.getInt("lunarians-form-center", 0);
  }
  
  private static void dispatch(ArrayList paramArrayList, HashMap paramHashMap, boolean paramBoolean) {
    // Byte code:
    //   0: getstatic lunarians/Formations.pending : Ljava/util/ArrayList;
    //   3: astore_3
    //   4: aload_3
    //   5: invokevirtual clear : ()V
    //   8: iload_2
    //   9: ifeq -> 40
    //   12: invokestatic gradual : ()Z
    //   15: istore_2
    //   16: iload_2
    //   17: ifeq -> 40
    //   20: invokestatic gradualDelay : ()F
    //   23: fstore #4
    //   25: ldc_w 60.0
    //   28: fstore #5
    //   30: fload #4
    //   32: fload #5
    //   34: fmul
    //   35: fstore #4
    //   37: goto -> 45
    //   40: iconst_0
    //   41: istore_2
    //   42: fconst_0
    //   43: fstore #4
    //   45: getstatic arc/util/Time.time : F
    //   48: fstore #5
    //   50: iconst_0
    //   51: istore #6
    //   53: aload_0
    //   54: invokevirtual size : ()I
    //   57: istore #7
    //   59: iload #6
    //   61: iload #7
    //   63: if_icmpge -> 278
    //   66: aload_0
    //   67: iload #6
    //   69: invokevirtual get : (I)Ljava/lang/Object;
    //   72: astore #8
    //   74: aload_1
    //   75: aload #8
    //   77: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   80: checkcast [Larc/math/geom/Vec2;
    //   83: astore #8
    //   85: aload #8
    //   87: ifnull -> 269
    //   90: iload #6
    //   92: i2f
    //   93: fload #4
    //   95: fmul
    //   96: fload #5
    //   98: fadd
    //   99: fstore #9
    //   101: iconst_1
    //   102: istore #10
    //   104: aload #8
    //   106: iload #10
    //   108: aaload
    //   109: astore #11
    //   111: getstatic lunarians/Formations.pending : Ljava/util/ArrayList;
    //   114: astore #12
    //   116: aload #11
    //   118: ifnull -> 220
    //   121: new lunarians/Formations$Pend
    //   124: astore #11
    //   126: aload_0
    //   127: iload #6
    //   129: invokevirtual get : (I)Ljava/lang/Object;
    //   132: checkcast java/lang/Integer
    //   135: invokevirtual intValue : ()I
    //   138: istore #13
    //   140: aload #8
    //   142: iload #10
    //   144: aaload
    //   145: astore #14
    //   147: aload #11
    //   149: iload #13
    //   151: aload #14
    //   153: iconst_0
    //   154: fload #9
    //   156: invokespecial <init> : (ILarc/math/geom/Vec2;ZF)V
    //   159: aload #12
    //   161: aload #11
    //   163: invokevirtual add : (Ljava/lang/Object;)Z
    //   166: pop
    //   167: new lunarians/Formations$Pend
    //   170: astore #11
    //   172: aload_0
    //   173: iload #6
    //   175: invokevirtual get : (I)Ljava/lang/Object;
    //   178: checkcast java/lang/Integer
    //   181: astore #15
    //   183: aload #15
    //   185: invokevirtual intValue : ()I
    //   188: istore #13
    //   190: aload #8
    //   192: iconst_0
    //   193: aaload
    //   194: astore #8
    //   196: aload #11
    //   198: iload #13
    //   200: aload #8
    //   202: iload #10
    //   204: fload #9
    //   206: invokespecial <init> : (ILarc/math/geom/Vec2;ZF)V
    //   209: aload #12
    //   211: aload #11
    //   213: invokevirtual add : (Ljava/lang/Object;)Z
    //   216: pop
    //   217: goto -> 269
    //   220: new lunarians/Formations$Pend
    //   223: astore #16
    //   225: aload_0
    //   226: iload #6
    //   228: invokevirtual get : (I)Ljava/lang/Object;
    //   231: checkcast java/lang/Integer
    //   234: astore #11
    //   236: aload #11
    //   238: invokevirtual intValue : ()I
    //   241: istore #17
    //   243: aload #8
    //   245: iconst_0
    //   246: aaload
    //   247: astore #8
    //   249: aload #16
    //   251: iload #17
    //   253: aload #8
    //   255: iconst_0
    //   256: fload #9
    //   258: invokespecial <init> : (ILarc/math/geom/Vec2;ZF)V
    //   261: aload #12
    //   263: aload #16
    //   265: invokevirtual add : (Ljava/lang/Object;)Z
    //   268: pop
    //   269: iload #6
    //   271: iconst_1
    //   272: iadd
    //   273: istore #6
    //   275: goto -> 53
    //   278: invokestatic pump : ()V
    //   281: return
  }
  
  public static int face() {
    return Core.settings.getInt("lunarians-form-face", 0);
  }
  
  public static boolean gradual() {
    return Core.settings.getBool("lunarians-form-grad", false);
  }
  
  public static float gradualDelay() {
    return Core.settings.getFloat("lunarians-form-grad-delay", 0.4F);
  }
  
  public static void init() {
    EventType.Trigger trigger = EventType.Trigger.update;
    Formations$27 formations$27 = new Formations$27();
    this();
    Events.run(trigger, formations$27);
    Formations$28 formations$28 = new Formations$28();
    this();
    Events.on(EventType.WorldLoadEvent.class, formations$28);
  }
  
  public static int layers() {
    return Core.settings.getInt("lunarians-form-layers", 0);
  }
  
  public static float[] layout(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2) {
    int i = paramInt2 << 1;
    float[] arrayOfFloat = new float[i];
    if (paramInt2 > 0) {
      float f1 = Mathf.cos(paramFloat2);
      float f2 = Mathf.sin(paramFloat2);
      float f3 = -f2;
      int j = perLayer();
      int k = layers();
      byte b = 1;
      if (j <= 0)
        if (k > 0) {
          j = (paramInt2 + k - b) / k;
        } else if (paramInt1 == b) {
          j = 14;
        } else {
          j = paramInt2;
        }  
      j = Math.min(j, paramInt2);
      j = Math.max(b, j);
      k = 1073741824;
      float f4 = 2.0F;
      int m = 0;
      if (paramInt1 == b) {
        while (m < paramInt2) {
          paramInt1 = m / j;
          int n = m % j;
          int i1 = paramInt1 * j;
          i1 = paramInt2 - i1;
          i1 = Math.min(j, i1);
          paramFloat2 = n;
          float f5 = (i1 - b) / f4;
          paramFloat2 = (paramFloat2 - f5) * paramFloat1;
          float f6 = paramInt1 * paramFloat1;
          i1 = m << 1;
          float f7 = f3 * paramFloat2;
          float f8 = f1 * f6;
          f7 -= f8;
          arrayOfFloat[i1] = f7;
          i1 += b;
          paramFloat2 *= f1;
          f6 *= f2;
          paramFloat2 -= f6;
          arrayOfFloat[i1] = paramFloat2;
          m++;
        } 
      } else {
        int i1 = 2;
        f3 = 2.8E-45F;
        int n = 1078530011;
        float f = 3.1415927F;
        if (paramInt1 == i1) {
          paramInt1 = 1068708659;
          float f5 = 1.4F * paramFloat1;
          if (j > b) {
            i1 = j + -1;
            f3 = i1 * paramFloat1;
            int i2 = 1077212331;
            float f6 = 2.8274333F;
            f3 /= f6;
          } else {
            f3 = paramFloat1;
          } 
          f5 = Math.max(f5, f3);
          paramFloat2 += f;
          f1 *= f5;
          f2 *= f5;
          while (m < paramInt2) {
            i1 = m / j;
            n = m % j;
            int i2 = i1 * j;
            i2 = paramInt2 - i2;
            i2 = Math.min(j, i2);
            f3 = i1 * paramFloat1 + f5;
            float f7 = paramFloat1 / f3;
            f = n;
            float f6 = (i2 - b) / f4;
            f = (f - f6) * f7;
            i2 = m << 1;
            f += paramFloat2;
            f7 = Mathf.cos(f) * f3 + f1;
            arrayOfFloat[i2] = f7;
            i2 += b;
            f = Mathf.sin(f) * f3 + f2;
            arrayOfFloat[i2] = f;
            m++;
          } 
        } else {
          byte b1 = 3;
          f1 = 4.2E-45F;
          if (paramInt1 == b1) {
            paramInt1 = 1086918619;
            float f5 = 6.2831855F;
            if (j > b) {
              f1 = j * paramFloat1 / f5;
              int i2 = 1064514355;
              f2 = 0.95F * paramFloat1;
              f1 = Math.max(f1, f2);
            } else {
              b1 = 0;
              f1 = 0.0F;
            } 
            paramFloat2 += f;
            while (m < paramInt2) {
              int i2 = m / j;
              i1 = m % j;
              k = i2 * j;
              k = paramInt2 - k;
              k = Math.min(j, k);
              f2 = i2 * paramFloat1 + f1;
              f3 = i1 * f5;
              f4 = k;
              f3 = f3 / f4 + paramFloat2;
              k = m << 1;
              f = Mathf.cos(f3) * f2;
              arrayOfFloat[k] = f;
              k += b;
              f3 = Mathf.sin(f3) * f2;
              arrayOfFloat[k] = f3;
              m++;
            } 
          } 
        } 
      } 
    } 
    return arrayOfFloat;
  }
  
  public static boolean loop() {
    return Core.settings.getBool("lunarians-form-loop", false);
  }
  
  private static void loopTick() {
    // Byte code:
    //   0: getstatic lunarians/Formations.loopActive : Z
    //   3: istore_0
    //   4: iload_0
    //   5: ifeq -> 633
    //   8: invokestatic loop : ()Z
    //   11: istore_0
    //   12: iconst_0
    //   13: istore_1
    //   14: fconst_0
    //   15: fstore_2
    //   16: aconst_null
    //   17: astore_3
    //   18: iload_0
    //   19: ifeq -> 634
    //   22: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   25: astore #4
    //   27: aload #4
    //   29: ifnull -> 634
    //   32: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   35: astore #4
    //   37: aload #4
    //   39: ifnull -> 634
    //   42: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   45: astore #4
    //   47: aload #4
    //   49: invokevirtual isGame : ()Z
    //   52: istore_0
    //   53: iload_0
    //   54: ifeq -> 634
    //   57: getstatic lunarians/Formations.pending : Ljava/util/ArrayList;
    //   60: astore #4
    //   62: aload #4
    //   64: invokevirtual isEmpty : ()Z
    //   67: istore_0
    //   68: iload_0
    //   69: ifeq -> 633
    //   72: getstatic arc/util/Time.time : F
    //   75: fstore #5
    //   77: getstatic lunarians/Formations.nextLoop : F
    //   80: fstore #6
    //   82: fload #5
    //   84: fload #6
    //   86: fcmpg
    //   87: istore_0
    //   88: iload_0
    //   89: iflt -> 633
    //   92: getstatic arc/util/Time.time : F
    //   95: fstore #5
    //   97: ldc_w 20.0
    //   100: fstore #6
    //   102: fload #5
    //   104: fload #6
    //   106: fadd
    //   107: fstore #5
    //   109: fload #5
    //   111: putstatic lunarians/Formations.nextLoop : F
    //   114: getstatic lunarians/Formations.loopToB : Z
    //   117: istore_0
    //   118: iload_0
    //   119: ifeq -> 130
    //   122: getstatic lunarians/Formations.loopB : Ljava/util/HashMap;
    //   125: astore #4
    //   127: goto -> 135
    //   130: getstatic lunarians/Formations.loopA : Ljava/util/HashMap;
    //   133: astore #4
    //   135: new java/util/ArrayList
    //   138: astore #7
    //   140: aload #7
    //   142: invokespecial <init> : ()V
    //   145: new java/util/ArrayList
    //   148: astore #8
    //   150: aload #8
    //   152: invokespecial <init> : ()V
    //   155: getstatic lunarians/Formations.loopB : Ljava/util/HashMap;
    //   158: invokevirtual keySet : ()Ljava/util/Set;
    //   161: invokeinterface iterator : ()Ljava/util/Iterator;
    //   166: astore #9
    //   168: iconst_1
    //   169: istore #10
    //   171: iconst_1
    //   172: istore #11
    //   174: aload #9
    //   176: invokeinterface hasNext : ()Z
    //   181: istore #12
    //   183: iload #12
    //   185: ifeq -> 427
    //   188: aload #9
    //   190: invokeinterface next : ()Ljava/lang/Object;
    //   195: checkcast java/lang/Integer
    //   198: astore #13
    //   200: getstatic mindustry/gen/Groups.unit : Lmindustry/entities/EntityGroup;
    //   203: astore #14
    //   205: aload #13
    //   207: invokevirtual intValue : ()I
    //   210: istore #15
    //   212: aload #14
    //   214: iload #15
    //   216: invokevirtual getByID : (I)Lmindustry/gen/Entityc;
    //   219: checkcast mindustry/gen/Unit
    //   222: astore #14
    //   224: aload #14
    //   226: ifnull -> 416
    //   229: aload #14
    //   231: invokevirtual isValid : ()Z
    //   234: istore #15
    //   236: iload #15
    //   238: ifeq -> 416
    //   241: aload #14
    //   243: invokevirtual controller : ()Lmindustry/entities/units/UnitController;
    //   246: astore #16
    //   248: aload #16
    //   250: instanceof mindustry/ai/types/CommandAI
    //   253: istore #15
    //   255: iload #15
    //   257: ifeq -> 416
    //   260: aload #14
    //   262: invokevirtual controller : ()Lmindustry/entities/units/UnitController;
    //   265: checkcast mindustry/ai/types/CommandAI
    //   268: astore #14
    //   270: aload #4
    //   272: aload #13
    //   274: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   277: checkcast [Larc/math/geom/Vec2;
    //   280: astore #16
    //   282: aload #14
    //   284: getfield targetPos : Larc/math/geom/Vec2;
    //   287: astore #17
    //   289: aload #17
    //   291: ifnull -> 390
    //   294: aload #16
    //   296: ifnull -> 390
    //   299: aload #14
    //   301: getfield targetPos : Larc/math/geom/Vec2;
    //   304: astore #17
    //   306: aload #16
    //   308: iconst_0
    //   309: aaload
    //   310: astore #18
    //   312: aload #17
    //   314: aload #18
    //   316: invokevirtual dst : (Larc/math/geom/Vec2;)F
    //   319: fstore #19
    //   321: ldc_w 16.0
    //   324: fstore #20
    //   326: fload #19
    //   328: fload #20
    //   330: fcmpl
    //   331: istore #21
    //   333: iload #21
    //   335: ifle -> 390
    //   338: aload #16
    //   340: iload #10
    //   342: aaload
    //   343: astore #17
    //   345: aload #17
    //   347: ifnull -> 385
    //   350: aload #14
    //   352: getfield targetPos : Larc/math/geom/Vec2;
    //   355: astore #17
    //   357: aload #16
    //   359: iload #10
    //   361: aaload
    //   362: astore #16
    //   364: aload #17
    //   366: aload #16
    //   368: invokevirtual dst : (Larc/math/geom/Vec2;)F
    //   371: fstore #22
    //   373: fload #22
    //   375: fload #20
    //   377: fcmpl
    //   378: istore #15
    //   380: iload #15
    //   382: ifle -> 390
    //   385: iconst_0
    //   386: putstatic lunarians/Formations.loopActive : Z
    //   389: return
    //   390: aload #14
    //   392: getfield targetPos : Larc/math/geom/Vec2;
    //   395: astore #14
    //   397: aload #14
    //   399: ifnull -> 405
    //   402: iconst_0
    //   403: istore #11
    //   405: aload #7
    //   407: aload #13
    //   409: invokevirtual add : (Ljava/lang/Object;)Z
    //   412: pop
    //   413: goto -> 174
    //   416: aload #8
    //   418: aload #13
    //   420: invokevirtual add : (Ljava/lang/Object;)Z
    //   423: pop
    //   424: goto -> 174
    //   427: aload #8
    //   429: invokevirtual iterator : ()Ljava/util/Iterator;
    //   432: astore #4
    //   434: aload #4
    //   436: invokeinterface hasNext : ()Z
    //   441: istore #23
    //   443: iload #23
    //   445: ifeq -> 485
    //   448: aload #4
    //   450: invokeinterface next : ()Ljava/lang/Object;
    //   455: checkcast java/lang/Integer
    //   458: astore #8
    //   460: getstatic lunarians/Formations.loopA : Ljava/util/HashMap;
    //   463: aload #8
    //   465: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   468: pop
    //   469: getstatic lunarians/Formations.loopB : Ljava/util/HashMap;
    //   472: astore #9
    //   474: aload #9
    //   476: aload #8
    //   478: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   481: pop
    //   482: goto -> 434
    //   485: aload #7
    //   487: invokevirtual isEmpty : ()Z
    //   490: istore_0
    //   491: iload_0
    //   492: ifeq -> 500
    //   495: iconst_0
    //   496: putstatic lunarians/Formations.loopActive : Z
    //   499: return
    //   500: iconst_0
    //   501: istore_0
    //   502: fconst_0
    //   503: fstore #5
    //   505: aconst_null
    //   506: astore #4
    //   508: iload #11
    //   510: ifne -> 518
    //   513: fconst_0
    //   514: putstatic lunarians/Formations.loopWaitUntil : F
    //   517: return
    //   518: getstatic lunarians/Formations.loopWaitUntil : F
    //   521: fstore_2
    //   522: fload_2
    //   523: fconst_0
    //   524: fcmpl
    //   525: istore_1
    //   526: iload_1
    //   527: ifne -> 551
    //   530: getstatic arc/util/Time.time : F
    //   533: fstore #5
    //   535: invokestatic loopWait : ()F
    //   538: ldc_w 60.0
    //   541: fmul
    //   542: fstore_2
    //   543: fload #5
    //   545: fload_2
    //   546: fadd
    //   547: putstatic lunarians/Formations.loopWaitUntil : F
    //   550: return
    //   551: getstatic arc/util/Time.time : F
    //   554: fstore_2
    //   555: getstatic lunarians/Formations.loopWaitUntil : F
    //   558: fstore #24
    //   560: fload_2
    //   561: fload #24
    //   563: fcmpg
    //   564: istore_1
    //   565: iload_1
    //   566: iflt -> 633
    //   569: fconst_0
    //   570: putstatic lunarians/Formations.loopWaitUntil : F
    //   573: getstatic lunarians/Formations.loopToB : Z
    //   576: istore_0
    //   577: iload_0
    //   578: iconst_1
    //   579: ixor
    //   580: istore_1
    //   581: iload_1
    //   582: putstatic lunarians/Formations.loopToB : Z
    //   585: iload_0
    //   586: ifne -> 597
    //   589: getstatic lunarians/Formations.loopB : Ljava/util/HashMap;
    //   592: astore #4
    //   594: goto -> 602
    //   597: getstatic lunarians/Formations.loopA : Ljava/util/HashMap;
    //   600: astore #4
    //   602: getstatic lunarians/Formations.curLegs : Ljava/util/HashMap;
    //   605: astore_3
    //   606: aload_3
    //   607: invokevirtual clear : ()V
    //   610: aload_3
    //   611: aload #4
    //   613: invokevirtual putAll : (Ljava/util/Map;)V
    //   616: getstatic lunarians/Formations.retries : Ljava/util/HashMap;
    //   619: astore_3
    //   620: aload_3
    //   621: invokevirtual clear : ()V
    //   624: aload #7
    //   626: aload #4
    //   628: iload #10
    //   630: invokestatic dispatch : (Ljava/util/ArrayList;Ljava/util/HashMap;Z)V
    //   633: return
    //   634: iconst_0
    //   635: putstatic lunarians/Formations.loopActive : Z
    //   638: return
  }
  
  public static float loopWait() {
    return Core.settings.getFloat("lunarians-form-loop-wait", 1.0F);
  }
  
  private static Vec2[] makeLeg(Unit paramUnit, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    float f1 = paramUnit.hitSize / 2.0F;
    f1 = Math.max(5.0F, f1);
    World world1 = Vars.world;
    int i = world1.unitWidth();
    float f2 = i;
    World world2 = Vars.world;
    int j = world2.unitHeight();
    float f3 = j;
    Vec2 vec21 = new Vec2();
    float f4 = Mathf.clamp(paramFloat1, 0.0F, f2);
    float f5 = Mathf.clamp(paramFloat2, 0.0F, f3);
    this(f4, f5);
    boolean bool = approach();
    byte b1 = 2;
    f5 = 2.8E-45F;
    boolean bool1 = true;
    byte b2 = 3;
    if (!bool) {
      arrayOfVec2 = new Vec2[b2];
      arrayOfVec2[0] = vec21;
      arrayOfVec2[bool1] = null;
      arrayOfVec2[b1] = vec21;
      return arrayOfVec2;
    } 
    float f6 = ((Unit)arrayOfVec2).hitSize + 24.0F;
    Vec2 vec22 = new Vec2();
    float f7 = Mathf.clamp(paramFloat3 * f1 + paramFloat1, 0.0F, f2);
    f1 = Mathf.clamp(f1 * paramFloat4 + paramFloat2, 0.0F, f3);
    this(f7, f1);
    Vec2 vec23 = new Vec2();
    paramFloat3 *= f6;
    paramFloat1 = Mathf.clamp(paramFloat1 - paramFloat3, 0.0F, f2);
    paramFloat4 *= f6;
    f6 = Mathf.clamp(paramFloat2 - paramFloat4, 0.0F, f3);
    this(paramFloat1, f6);
    Vec2[] arrayOfVec2 = new Vec2[b2];
    arrayOfVec2[0] = vec22;
    arrayOfVec2[bool1] = vec23;
    arrayOfVec2[b1] = vec21;
    return arrayOfVec2;
  }
  
  public static int mode() {
    return Core.settings.getInt("lunarians-form-mode", 0);
  }
  
  public static String modeName(int paramInt) {
    // Byte code:
    //   0: getstatic lunarians/LunariansSettings.language : Ljava/lang/String;
    //   3: astore_1
    //   4: iconst_1
    //   5: istore_2
    //   6: aload_1
    //   7: ifnull -> 36
    //   10: getstatic lunarians/LunariansSettings.language : Ljava/lang/String;
    //   13: astore_1
    //   14: ldc_w 'pt'
    //   17: astore_3
    //   18: aload_1
    //   19: aload_3
    //   20: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   23: istore #4
    //   25: iload #4
    //   27: ifeq -> 36
    //   30: iconst_1
    //   31: istore #4
    //   33: goto -> 41
    //   36: iconst_0
    //   37: istore #4
    //   39: aconst_null
    //   40: astore_1
    //   41: iload_0
    //   42: iload_2
    //   43: if_icmpeq -> 99
    //   46: iconst_2
    //   47: istore_2
    //   48: iload_0
    //   49: iload_2
    //   50: if_icmpeq -> 86
    //   53: iconst_3
    //   54: istore_2
    //   55: iload_0
    //   56: iload_2
    //   57: if_icmpeq -> 73
    //   60: iload #4
    //   62: ifeq -> 69
    //   65: ldc_w 'Desligado'
    //   68: areturn
    //   69: ldc_w 'Off'
    //   72: areturn
    //   73: iload #4
    //   75: ifeq -> 82
    //   78: ldc_w 'Anel'
    //   81: areturn
    //   82: ldc_w 'Ring'
    //   85: areturn
    //   86: iload #4
    //   88: ifeq -> 95
    //   91: ldc_w 'Arco'
    //   94: areturn
    //   95: ldc_w 'Arc'
    //   98: areturn
    //   99: iload #4
    //   101: ifeq -> 108
    //   104: ldc_w 'Linha'
    //   107: areturn
    //   108: ldc_w 'Line'
    //   111: areturn
  }
  
  public static void openDialog() {
    BaseDialog baseDialog = new BaseDialog();
    String str = tr("Formations", "Formacoes");
    this(str);
    UiStyle.register(baseDialog);
    Table table = new Table();
    this();
    Runnable[] arrayOfRunnable = new Runnable[1];
    Formations$12 formations$12 = new Formations$12();
    this(table, arrayOfRunnable);
    arrayOfRunnable[0] = formations$12;
    arrayOfRunnable[0].run();
    baseDialog.cont.pane((Element)table).grow().minWidth(440.0F);
    baseDialog.addCloseButton();
    baseDialog.show();
  }
  
  public static int perLayer() {
    return Core.settings.getInt("lunarians-form-per", 0);
  }
  
  private static void pump() {
    ArrayList<Formations$Pend> arrayList = pending;
    boolean bool = arrayList.isEmpty();
    if (!bool) {
      int i = 0;
      while (true) {
        arrayList = pending;
        int j = arrayList.size();
        if (i < j) {
          Formations$Pend formations$Pend = arrayList.get(i);
          float f1 = Time.time;
          float f2 = formations$Pend.at;
          int k = f1 cmp f2;
          if (k >= 0) {
            arrayList.remove(i);
            try {
              Player player = Vars.player;
              int m = formations$Pend.id;
              int[] arrayOfInt = { m };
              Vec2 vec2 = formations$Pend.p;
              boolean bool1 = formations$Pend.queue;
            } finally {
              arrayList = null;
              String str = "[Lunarians] Formations send";
            } 
            continue;
          } 
          i++;
          continue;
        } 
        break;
      } 
    } 
  }
  
  public static boolean reform() {
    return Core.settings.getBool("lunarians-form-reform", false);
  }
  
  private static void reformCheck() {
    // Byte code:
    //   0: invokestatic reform : ()Z
    //   3: istore_0
    //   4: iload_0
    //   5: ifeq -> 586
    //   8: getstatic lunarians/Formations.loopActive : Z
    //   11: istore_0
    //   12: iload_0
    //   13: ifne -> 586
    //   16: getstatic lunarians/Formations.pending : Ljava/util/ArrayList;
    //   19: astore_1
    //   20: aload_1
    //   21: invokevirtual isEmpty : ()Z
    //   24: istore_0
    //   25: iload_0
    //   26: ifeq -> 586
    //   29: getstatic lunarians/Formations.curLegs : Ljava/util/HashMap;
    //   32: astore_1
    //   33: aload_1
    //   34: invokevirtual isEmpty : ()Z
    //   37: istore_2
    //   38: iload_2
    //   39: ifne -> 586
    //   42: getstatic arc/util/Time.time : F
    //   45: fstore_3
    //   46: getstatic lunarians/Formations.nextCheck : F
    //   49: fstore #4
    //   51: fload_3
    //   52: fload #4
    //   54: fcmpg
    //   55: istore_2
    //   56: iload_2
    //   57: iflt -> 586
    //   60: getstatic arc/util/Time.time : F
    //   63: fstore_3
    //   64: ldc_w 30.0
    //   67: fstore #4
    //   69: fload_3
    //   70: fload #4
    //   72: fadd
    //   73: fstore_3
    //   74: fload_3
    //   75: putstatic lunarians/Formations.nextCheck : F
    //   78: new java/util/ArrayList
    //   81: astore #5
    //   83: aload #5
    //   85: invokespecial <init> : ()V
    //   88: new java/util/ArrayList
    //   91: astore #6
    //   93: aload #6
    //   95: invokespecial <init> : ()V
    //   98: aload_1
    //   99: invokevirtual entrySet : ()Ljava/util/Set;
    //   102: invokeinterface iterator : ()Ljava/util/Iterator;
    //   107: astore_1
    //   108: iconst_1
    //   109: istore #7
    //   111: iconst_1
    //   112: istore #8
    //   114: aload_1
    //   115: invokeinterface hasNext : ()Z
    //   120: istore #9
    //   122: iconst_0
    //   123: istore #10
    //   125: fconst_0
    //   126: fstore #11
    //   128: iload #9
    //   130: ifeq -> 414
    //   133: aload_1
    //   134: invokeinterface next : ()Ljava/lang/Object;
    //   139: checkcast java/util/Map$Entry
    //   142: astore #12
    //   144: getstatic mindustry/gen/Groups.unit : Lmindustry/entities/EntityGroup;
    //   147: astore #13
    //   149: aload #12
    //   151: invokeinterface getKey : ()Ljava/lang/Object;
    //   156: checkcast java/lang/Integer
    //   159: astore #14
    //   161: aload #14
    //   163: invokevirtual intValue : ()I
    //   166: istore #15
    //   168: aload #13
    //   170: iload #15
    //   172: invokevirtual getByID : (I)Lmindustry/gen/Entityc;
    //   175: checkcast mindustry/gen/Unit
    //   178: astore #13
    //   180: aload #13
    //   182: ifnull -> 391
    //   185: aload #13
    //   187: invokevirtual isValid : ()Z
    //   190: istore #15
    //   192: iload #15
    //   194: ifeq -> 391
    //   197: aload #13
    //   199: invokevirtual controller : ()Lmindustry/entities/units/UnitController;
    //   202: astore #14
    //   204: aload #14
    //   206: instanceof mindustry/ai/types/CommandAI
    //   209: istore #15
    //   211: iload #15
    //   213: ifeq -> 391
    //   216: aload #13
    //   218: invokevirtual controller : ()Lmindustry/entities/units/UnitController;
    //   221: checkcast mindustry/ai/types/CommandAI
    //   224: getfield targetPos : Larc/math/geom/Vec2;
    //   227: astore #14
    //   229: aload #14
    //   231: ifnull -> 243
    //   234: iconst_0
    //   235: istore #8
    //   237: aconst_null
    //   238: astore #16
    //   240: goto -> 114
    //   243: aload #13
    //   245: getfield hitSize : F
    //   248: ldc_w 0.45
    //   251: fmul
    //   252: fstore #11
    //   254: ldc_w 1086324736
    //   257: istore #15
    //   259: ldc_w 6.0
    //   262: fstore #17
    //   264: fload #17
    //   266: fload #11
    //   268: invokestatic max : (FF)F
    //   271: fstore #11
    //   273: getstatic lunarians/Formations.retries : Ljava/util/HashMap;
    //   276: astore #14
    //   278: aload #12
    //   280: invokeinterface getKey : ()Ljava/lang/Object;
    //   285: astore #18
    //   287: aload #14
    //   289: aload #18
    //   291: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   294: checkcast java/lang/Integer
    //   297: astore #14
    //   299: aload #12
    //   301: invokeinterface getValue : ()Ljava/lang/Object;
    //   306: checkcast [Larc/math/geom/Vec2;
    //   309: astore #18
    //   311: iconst_2
    //   312: istore #19
    //   314: aload #18
    //   316: iload #19
    //   318: aaload
    //   319: astore #18
    //   321: aload #13
    //   323: aload #18
    //   325: invokevirtual dst : (Larc/math/geom/Position;)F
    //   328: fstore #20
    //   330: fload #20
    //   332: fload #11
    //   334: fcmpl
    //   335: istore #10
    //   337: iload #10
    //   339: ifle -> 114
    //   342: aload #14
    //   344: ifnull -> 368
    //   347: aload #14
    //   349: invokevirtual intValue : ()I
    //   352: istore #10
    //   354: iconst_3
    //   355: istore #21
    //   357: ldc 4.2E-45
    //   359: fstore #20
    //   361: iload #10
    //   363: iload #21
    //   365: if_icmpge -> 114
    //   368: aload #12
    //   370: invokeinterface getKey : ()Ljava/lang/Object;
    //   375: checkcast java/lang/Integer
    //   378: astore #12
    //   380: aload #6
    //   382: aload #12
    //   384: invokevirtual add : (Ljava/lang/Object;)Z
    //   387: pop
    //   388: goto -> 114
    //   391: aload #12
    //   393: invokeinterface getKey : ()Ljava/lang/Object;
    //   398: checkcast java/lang/Integer
    //   401: astore #12
    //   403: aload #5
    //   405: aload #12
    //   407: invokevirtual add : (Ljava/lang/Object;)Z
    //   410: pop
    //   411: goto -> 114
    //   414: aload #5
    //   416: invokevirtual iterator : ()Ljava/util/Iterator;
    //   419: astore_1
    //   420: aload_1
    //   421: invokeinterface hasNext : ()Z
    //   426: istore_2
    //   427: iload_2
    //   428: ifeq -> 458
    //   431: aload_1
    //   432: invokeinterface next : ()Ljava/lang/Object;
    //   437: checkcast java/lang/Integer
    //   440: astore #5
    //   442: getstatic lunarians/Formations.curLegs : Ljava/util/HashMap;
    //   445: astore #12
    //   447: aload #12
    //   449: aload #5
    //   451: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   454: pop
    //   455: goto -> 420
    //   458: iload #8
    //   460: ifeq -> 586
    //   463: aload #6
    //   465: invokevirtual isEmpty : ()Z
    //   468: istore_0
    //   469: iload_0
    //   470: ifeq -> 480
    //   473: getstatic lunarians/Formations.curLegs : Ljava/util/HashMap;
    //   476: invokevirtual clear : ()V
    //   479: return
    //   480: aload #6
    //   482: invokevirtual iterator : ()Ljava/util/Iterator;
    //   485: astore_1
    //   486: aload_1
    //   487: invokeinterface hasNext : ()Z
    //   492: istore_2
    //   493: iload_2
    //   494: ifeq -> 575
    //   497: aload_1
    //   498: invokeinterface next : ()Ljava/lang/Object;
    //   503: checkcast java/lang/Integer
    //   506: astore #5
    //   508: getstatic lunarians/Formations.retries : Ljava/util/HashMap;
    //   511: astore #16
    //   513: aload #16
    //   515: aload #5
    //   517: invokevirtual containsKey : (Ljava/lang/Object;)Z
    //   520: istore #9
    //   522: iload #9
    //   524: ifeq -> 552
    //   527: aload #16
    //   529: aload #5
    //   531: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   534: checkcast java/lang/Integer
    //   537: astore #12
    //   539: aload #12
    //   541: invokevirtual intValue : ()I
    //   544: iload #7
    //   546: iadd
    //   547: istore #9
    //   549: goto -> 555
    //   552: iconst_1
    //   553: istore #9
    //   555: iload #9
    //   557: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   560: astore #12
    //   562: aload #16
    //   564: aload #5
    //   566: aload #12
    //   568: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   571: pop
    //   572: goto -> 486
    //   575: getstatic lunarians/Formations.curLegs : Ljava/util/HashMap;
    //   578: astore_1
    //   579: aload #6
    //   581: aload_1
    //   582: iconst_0
    //   583: invokestatic dispatch : (Ljava/util/ArrayList;Ljava/util/HashMap;Z)V
    //   586: return
  }
  
  public static void setMode(int paramInt) {
    Settings settings = Core.settings;
    Integer integer = Integer.valueOf(paramInt);
    settings.put("lunarians-form-mode", integer);
  }
  
  public static float spacing() {
    return Core.settings.getFloat("lunarians-form-spacing", 2.0F);
  }
  
  private static String tr(String paramString1, String paramString2) {
    String str = LunariansSettings.language;
    if (str != null) {
      str = LunariansSettings.language;
      String str1 = "pt";
      boolean bool = str.startsWith(str1);
      if (bool)
        return paramString2; 
    } 
    return paramString1;
  }
  
  private static void update() {
    // Byte code:
    //   0: invokestatic pump : ()V
    //   3: invokestatic loopTick : ()V
    //   6: invokestatic mode : ()I
    //   9: istore_0
    //   10: iload_0
    //   11: ifeq -> 406
    //   14: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   17: astore_1
    //   18: aload_1
    //   19: ifnull -> 406
    //   22: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   25: astore_1
    //   26: aload_1
    //   27: invokevirtual isGame : ()Z
    //   30: istore_2
    //   31: iload_2
    //   32: ifeq -> 406
    //   35: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   38: astore_1
    //   39: aload_1
    //   40: ifnull -> 406
    //   43: getstatic mindustry/Vars.control : Lmindustry/core/Control;
    //   46: astore_1
    //   47: aload_1
    //   48: ifnull -> 406
    //   51: getstatic mindustry/Vars.control : Lmindustry/core/Control;
    //   54: astore_1
    //   55: aload_1
    //   56: getfield input : Lmindustry/input/InputHandler;
    //   59: astore_1
    //   60: aload_1
    //   61: getfield selectedUnits : Larc/struct/Seq;
    //   64: astore_1
    //   65: aconst_null
    //   66: astore_3
    //   67: aload_1
    //   68: ifnull -> 386
    //   71: aload_1
    //   72: getfield size : I
    //   75: istore #4
    //   77: iconst_2
    //   78: istore #5
    //   80: iload #4
    //   82: iload #5
    //   84: if_icmplt -> 386
    //   87: new arc/struct/Seq
    //   90: astore #6
    //   92: aload #6
    //   94: invokespecial <init> : ()V
    //   97: aload_1
    //   98: invokevirtual iterator : ()Ljava/util/Iterator;
    //   101: astore_1
    //   102: aconst_null
    //   103: astore #7
    //   105: aload_1
    //   106: invokeinterface hasNext : ()Z
    //   111: istore #8
    //   113: iload #8
    //   115: ifeq -> 346
    //   118: aload_1
    //   119: invokeinterface next : ()Ljava/lang/Object;
    //   124: astore #9
    //   126: aload #9
    //   128: checkcast mindustry/gen/Unit
    //   131: astore #9
    //   133: aload #9
    //   135: ifnull -> 105
    //   138: aload #9
    //   140: invokevirtual isValid : ()Z
    //   143: istore #10
    //   145: iload #10
    //   147: ifeq -> 105
    //   150: aload #9
    //   152: invokevirtual controller : ()Lmindustry/entities/units/UnitController;
    //   155: astore #11
    //   157: aload #11
    //   159: instanceof mindustry/ai/types/CommandAI
    //   162: istore #10
    //   164: iload #10
    //   166: ifeq -> 105
    //   169: aload #9
    //   171: invokevirtual controller : ()Lmindustry/entities/units/UnitController;
    //   174: astore #11
    //   176: aload #11
    //   178: checkcast mindustry/ai/types/CommandAI
    //   181: astore #11
    //   183: aload #11
    //   185: getfield group : Lmindustry/ai/UnitGroup;
    //   188: astore #12
    //   190: aload #12
    //   192: ifnull -> 406
    //   195: aload_3
    //   196: ifnonnull -> 208
    //   199: aload #11
    //   201: getfield group : Lmindustry/ai/UnitGroup;
    //   204: astore_3
    //   205: goto -> 221
    //   208: aload #11
    //   210: getfield group : Lmindustry/ai/UnitGroup;
    //   213: astore #12
    //   215: aload_3
    //   216: aload #12
    //   218: if_acmpne -> 406
    //   221: aload #11
    //   223: getfield attackTarget : Lmindustry/gen/Teamc;
    //   226: astore #12
    //   228: aload #12
    //   230: ifnull -> 282
    //   233: new arc/math/geom/Vec2
    //   236: astore #12
    //   238: aload #11
    //   240: getfield attackTarget : Lmindustry/gen/Teamc;
    //   243: astore #13
    //   245: aload #13
    //   247: invokeinterface x : ()F
    //   252: fstore #14
    //   254: aload #11
    //   256: getfield attackTarget : Lmindustry/gen/Teamc;
    //   259: astore #11
    //   261: aload #11
    //   263: invokeinterface y : ()F
    //   268: fstore #15
    //   270: aload #12
    //   272: fload #14
    //   274: fload #15
    //   276: invokespecial <init> : (FF)V
    //   279: goto -> 289
    //   282: aload #11
    //   284: getfield targetPos : Larc/math/geom/Vec2;
    //   287: astore #12
    //   289: aload #12
    //   291: ifnull -> 406
    //   294: aload #7
    //   296: ifnonnull -> 309
    //   299: aload #12
    //   301: invokevirtual cpy : ()Larc/math/geom/Vec2;
    //   304: astore #7
    //   306: goto -> 335
    //   309: aload #7
    //   311: aload #12
    //   313: invokevirtual dst : (Larc/math/geom/Vec2;)F
    //   316: fstore #15
    //   318: ldc_w 2.0
    //   321: fstore #16
    //   323: fload #15
    //   325: fload #16
    //   327: fcmpl
    //   328: istore #10
    //   330: iload #10
    //   332: ifgt -> 406
    //   335: aload #6
    //   337: aload #9
    //   339: invokevirtual add : (Ljava/lang/Object;)Larc/struct/Seq;
    //   342: pop
    //   343: goto -> 105
    //   346: aload_3
    //   347: ifnull -> 406
    //   350: aload #6
    //   352: getfield size : I
    //   355: istore_2
    //   356: iload_2
    //   357: iload #5
    //   359: if_icmplt -> 406
    //   362: getstatic lunarians/Formations.lastGroup : Ljava/lang/Object;
    //   365: astore_1
    //   366: aload_3
    //   367: aload_1
    //   368: if_acmpeq -> 403
    //   371: aload_3
    //   372: putstatic lunarians/Formations.lastGroup : Ljava/lang/Object;
    //   375: aload #6
    //   377: aload #7
    //   379: iload_0
    //   380: invokestatic apply : (Larc/struct/Seq;Larc/math/geom/Vec2;I)V
    //   383: goto -> 403
    //   386: aconst_null
    //   387: putstatic lunarians/Formations.lastGroup : Ljava/lang/Object;
    //   390: return
    //   391: astore #17
    //   393: ldc_w '[Lunarians] Formations'
    //   396: astore_1
    //   397: aload_1
    //   398: aload #17
    //   400: invokestatic err : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   403: invokestatic reformCheck : ()V
    //   406: return
    // Exception table:
    //   from	to	target	type
    //   51	54	391	finally
    //   55	59	391	finally
    //   60	64	391	finally
    //   71	75	391	finally
    //   87	90	391	finally
    //   92	97	391	finally
    //   97	101	391	finally
    //   105	111	391	finally
    //   118	124	391	finally
    //   126	131	391	finally
    //   138	143	391	finally
    //   150	155	391	finally
    //   169	174	391	finally
    //   176	181	391	finally
    //   183	188	391	finally
    //   199	204	391	finally
    //   208	213	391	finally
    //   221	226	391	finally
    //   233	236	391	finally
    //   238	243	391	finally
    //   245	252	391	finally
    //   254	259	391	finally
    //   261	268	391	finally
    //   274	279	391	finally
    //   282	287	391	finally
    //   299	304	391	finally
    //   311	316	391	finally
    //   337	343	391	finally
    //   350	355	391	finally
    //   362	365	391	finally
    //   371	375	391	finally
    //   379	383	391	finally
    //   386	390	391	finally
  }
}
