package lunarians;

import arc.Core;
import arc.Events;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.scene.Element;
import arc.scene.ui.Button;
import arc.scene.ui.Label;
import arc.scene.ui.Slider;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import mindustry.Vars;
import mindustry.ai.UnitGroup;
import mindustry.ai.types.CommandAI;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public class Formations {
  private static final float TWO_PI = 6.2831855F;
  
  public static final int OFF = 0;
  
  public static final int LINE = 1;
  
  public static final int ARC = 2;
  
  public static final int RING = 3;
  
  private static final String K_MODE = "lunarians-form-mode";
  
  private static final String K_SPACE = "lunarians-form-spacing";
  
  private static final String K_CENTER = "lunarians-form-center";
  
  private static final String K_PER = "lunarians-form-per";
  
  private static final String K_APPR = "lunarians-form-approach";
  
  private static final String K_GRAD = "lunarians-form-grad";
  
  private static final String K_GDELAY = "lunarians-form-grad-delay";
  
  private static final String K_LOOP = "lunarians-form-loop";
  
  private static final String K_LWAIT = "lunarians-form-loop-wait";
  
  private static final String K_LAYERS = "lunarians-form-layers";
  
  private static final String K_FACE = "lunarians-form-face";
  
  private static final String K_REFORM = "lunarians-form-reform";
  
  private static Object lastGroup = null;
  
  private static final HashMap<Integer, Vec2[]> curLegs = (HashMap)new HashMap<>();
  
  private static final HashMap<Integer, Integer> retries = new HashMap<>();
  
  private static float nextCheck = 0.0F;
  
  private static boolean reformed = false;
  
  private static class Pend {
    int id;
    
    Vec2 p;
    
    boolean queue;
    
    float at;
    
    Pend(int param1Int, Vec2 param1Vec2, boolean param1Boolean, float param1Float) {
      this.id = param1Int;
      this.p = param1Vec2;
      this.queue = param1Boolean;
      this.at = param1Float;
    }
  }
  
  private static final ArrayList<Pend> pending = new ArrayList<>();
  
  private static final HashMap<Integer, Vec2[]> loopA = (HashMap)new HashMap<>();
  
  private static final HashMap<Integer, Vec2[]> loopB = (HashMap)new HashMap<>();
  
  private static boolean loopActive = false;
  
  private static boolean loopToB = true;
  
  private static float loopWaitUntil = 0.0F, nextLoop = 0.0F;
  
  private static void pump() {
    if (pending.isEmpty())
      return; 
    for (byte b = 0; b < pending.size(); ) {
      Pend pend = pending.get(b);
      if (Time.time >= pend.at) {
        pending.remove(b);
        try {
          Call.commandUnits(Vars.player, new int[] { pend.id }, null, null, pend.p, pend.queue, true);
        } catch (Throwable throwable) {
          Log.err("[Lunarians] Formations send", throwable);
        } 
        continue;
      } 
      b++;
    } 
  }
  
  private static void dispatch(ArrayList<Integer> paramArrayList, HashMap<Integer, Vec2[]> paramHashMap, boolean paramBoolean) {
    pending.clear();
    float f1 = (paramBoolean && gradual()) ? (gradualDelay() * 60.0F) : 0.0F;
    float f2 = Time.time;
    for (byte b = 0; b < paramArrayList.size(); b++) {
      Vec2[] arrayOfVec2 = paramHashMap.get(paramArrayList.get(b));
      if (arrayOfVec2 != null) {
        float f = f2 + f1 * b;
        if (arrayOfVec2[1] != null) {
          pending.add(new Pend(((Integer)paramArrayList.get(b)).intValue(), arrayOfVec2[1], false, f));
          pending.add(new Pend(((Integer)paramArrayList.get(b)).intValue(), arrayOfVec2[0], true, f));
        } else {
          pending.add(new Pend(((Integer)paramArrayList.get(b)).intValue(), arrayOfVec2[0], false, f));
        } 
      } 
    } 
    pump();
  }
  
  private static Vec2[] makeLeg(Unit paramUnit, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    float f1 = Math.max(5.0F, paramUnit.hitSize / 2.0F);
    float f2 = Vars.world.unitWidth(), f3 = Vars.world.unitHeight();
    Vec2 vec21 = new Vec2(Mathf.clamp(paramFloat1, 0.0F, f2), Mathf.clamp(paramFloat2, 0.0F, f3));
    if (!approach())
      return new Vec2[] { vec21, null, vec21 }; 
    float f4 = 24.0F + paramUnit.hitSize;
    Vec2 vec22 = new Vec2(Mathf.clamp(paramFloat1 + paramFloat3 * f1, 0.0F, f2), Mathf.clamp(paramFloat2 + paramFloat4 * f1, 0.0F, f3));
    Vec2 vec23 = new Vec2(Mathf.clamp(paramFloat1 - paramFloat3 * f4, 0.0F, f2), Mathf.clamp(paramFloat2 - paramFloat4 * f4, 0.0F, f3));
    return new Vec2[] { vec22, vec23, vec21 };
  }
  
  private static void loopTick() {
    if (!loopActive)
      return; 
    if (!loop() || Vars.player == null || Vars.state == null || !Vars.state.isGame()) {
      loopActive = false;
      return;
    } 
    if (!pending.isEmpty() || Time.time < nextLoop)
      return; 
    nextLoop = Time.time + 20.0F;
    HashMap<Integer, Vec2[]> hashMap1 = loopToB ? loopB : loopA;
    boolean bool = true;
    ArrayList<Integer> arrayList1 = new ArrayList();
    ArrayList<Integer> arrayList2 = new ArrayList();
    for (Integer integer : loopB.keySet()) {
      Unit unit = (Unit)Groups.unit.getByID(integer.intValue());
      if (unit == null || !unit.isValid() || !(unit.controller() instanceof CommandAI)) {
        arrayList2.add(integer);
        continue;
      } 
      CommandAI commandAI = (CommandAI)unit.controller();
      Vec2[] arrayOfVec2 = hashMap1.get(integer);
      if (commandAI.targetPos != null && arrayOfVec2 != null && commandAI.targetPos.dst(arrayOfVec2[0]) > 16.0F && (arrayOfVec2[1] == null || commandAI.targetPos.dst(arrayOfVec2[1]) > 16.0F)) {
        loopActive = false;
        return;
      } 
      if (commandAI.targetPos != null)
        bool = false; 
      arrayList1.add(integer);
    } 
    for (Integer integer : arrayList2) {
      loopA.remove(integer);
      loopB.remove(integer);
    } 
    if (arrayList1.isEmpty()) {
      loopActive = false;
      return;
    } 
    if (!bool) {
      loopWaitUntil = 0.0F;
      return;
    } 
    if (loopWaitUntil == 0.0F) {
      loopWaitUntil = Time.time + loopWait() * 60.0F;
      return;
    } 
    if (Time.time < loopWaitUntil)
      return; 
    loopWaitUntil = 0.0F;
    loopToB = !loopToB;
    HashMap<Integer, Vec2[]> hashMap2 = loopToB ? loopB : loopA;
    curLegs.clear();
    curLegs.putAll(hashMap2);
    retries.clear();
    dispatch(arrayList1, hashMap2, true);
  }
  
  public static int mode() {
    return Core.settings.getInt("lunarians-form-mode", 0);
  }
  
  public static void setMode(int paramInt) {
    Core.settings.put("lunarians-form-mode", Integer.valueOf(paramInt));
  }
  
  public static float spacing() {
    return Core.settings.getFloat("lunarians-form-spacing", 2.0F);
  }
  
  public static int center() {
    return Core.settings.getInt("lunarians-form-center", 0);
  }
  
  public static int perLayer() {
    return Core.settings.getInt("lunarians-form-per", 0);
  }
  
  public static int layers() {
    return Core.settings.getInt("lunarians-form-layers", 0);
  }
  
  public static boolean gradual() {
    return Core.settings.getBool("lunarians-form-grad", false);
  }
  
  public static float gradualDelay() {
    return Core.settings.getFloat("lunarians-form-grad-delay", 0.4F);
  }
  
  public static boolean loop() {
    return Core.settings.getBool("lunarians-form-loop", false);
  }
  
  public static float loopWait() {
    return Core.settings.getFloat("lunarians-form-loop-wait", 1.0F);
  }
  
  public static boolean approach() {
    return Core.settings.getBool("lunarians-form-approach", true);
  }
  
  public static int face() {
    return Core.settings.getInt("lunarians-form-face", 0);
  }
  
  public static boolean reform() {
    return Core.settings.getBool("lunarians-form-reform", false);
  }
  
  public static String modeName(int paramInt) {
    boolean bool = (LunariansSettings.language != null && LunariansSettings.language.startsWith("pt")) ? true : false;
    switch (paramInt) {
      case 1:
        return bool ? "Linha" : "Line";
      case 2:
        return bool ? "Arco" : "Arc";
      case 3:
        return bool ? "Anel" : "Ring";
    } 
    return bool ? "Desligado" : "Off";
  }
  
  public static void init() {
    Events.run(EventType.Trigger.update, Formations::update);
    Events.on(EventType.WorldLoadEvent.class, paramWorldLoadEvent -> {
          lastGroup = null;
          curLegs.clear();
        });
  }
  
  public static float[] layout(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2) {
    float[] arrayOfFloat = new float[paramInt2 * 2];
    if (paramInt2 <= 0)
      return arrayOfFloat; 
    float f1 = Mathf.cos(paramFloat2), f2 = Mathf.sin(paramFloat2);
    float f3 = -f2, f4 = f1;
    int i = perLayer(), j = layers();
    if (i > 0) {
      k = i;
    } else if (j > 0) {
      k = (paramInt2 + j - 1) / j;
    } else {
      k = (paramInt1 == 1) ? 14 : paramInt2;
    } 
    int k = Math.max(1, Math.min(k, paramInt2));
    if (paramInt1 == 1) {
      for (byte b = 0; b < paramInt2; b++) {
        int m = b / k, n = b % k;
        int i1 = Math.min(k, paramInt2 - m * k);
        float f5 = (n - (i1 - 1) / 2.0F) * paramFloat1;
        float f6 = m * paramFloat1;
        arrayOfFloat[b * 2] = f3 * f5 - f1 * f6;
        arrayOfFloat[b * 2 + 1] = f4 * f5 - f2 * f6;
      } 
    } else if (paramInt1 == 2) {
      float f5 = Math.max(paramFloat1 * 1.4F, (k > 1) ? (paramFloat1 * (k - 1) / 2.8274333F) : paramFloat1);
      float f6 = paramFloat2 + 3.1415927F;
      float f7 = f1 * f5, f8 = f2 * f5;
      for (byte b = 0; b < paramInt2; b++) {
        int m = b / k, n = b % k;
        int i1 = Math.min(k, paramInt2 - m * k);
        float f9 = f5 + m * paramFloat1;
        float f10 = paramFloat1 / f9;
        float f11 = (n - (i1 - 1) / 2.0F) * f10;
        arrayOfFloat[b * 2] = f7 + Mathf.cos(f6 + f11) * f9;
        arrayOfFloat[b * 2 + 1] = f8 + Mathf.sin(f6 + f11) * f9;
      } 
    } else if (paramInt1 == 3) {
      float f5 = (k > 1) ? Math.max(paramFloat1 * k / 6.2831855F, paramFloat1 * 0.95F) : 0.0F;
      float f6 = paramFloat2 + 3.1415927F;
      for (byte b = 0; b < paramInt2; b++) {
        int m = b / k, n = b % k;
        int i1 = Math.min(k, paramInt2 - m * k);
        float f7 = f5 + m * paramFloat1;
        float f8 = f6 + 6.2831855F * n / i1;
        arrayOfFloat[b * 2] = Mathf.cos(f8) * f7;
        arrayOfFloat[b * 2 + 1] = Mathf.sin(f8) * f7;
      } 
    } 
    return arrayOfFloat;
  }
  
  private static void update() {
    pump();
    loopTick();
    int i = mode();
    if (i == 0 || Vars.state == null || !Vars.state.isGame() || Vars.player == null || Vars.control == null)
      return; 
    try {
      Seq seq = Vars.control.input.selectedUnits;
      if (seq == null || seq.size < 2) {
        lastGroup = null;
        return;
      } 
      UnitGroup unitGroup = null;
      Vec2 vec2 = null;
      Seq<Unit> seq1 = new Seq();
      for (Unit unit : seq) {
        if (unit == null || !unit.isValid() || !(unit.controller() instanceof CommandAI))
          continue; 
        CommandAI commandAI = (CommandAI)unit.controller();
        if (commandAI.group == null)
          return; 
        if (unitGroup == null) {
          unitGroup = commandAI.group;
        } else if (unitGroup != commandAI.group) {
          return;
        } 
        Vec2 vec21 = (commandAI.attackTarget != null) ? new Vec2(commandAI.attackTarget.x(), commandAI.attackTarget.y()) : commandAI.targetPos;
        if (vec21 == null)
          return; 
        if (vec2 == null) {
          vec2 = vec21.cpy();
        } else if (vec2.dst(vec21) > 2.0F) {
          return;
        } 
        seq1.add(unit);
      } 
      if (unitGroup == null || seq1.size < 2)
        return; 
      if (unitGroup != lastGroup) {
        lastGroup = unitGroup;
        apply(seq1, vec2, i);
      } 
    } catch (Throwable throwable) {
      Log.err("[Lunarians] Formations", throwable);
    } 
    reformCheck();
  }
  
  private static void apply(Seq<Unit> paramSeq, Vec2 paramVec2, int paramInt) {
    float f10, f11;
    int i = paramSeq.size;
    float f1 = 0.0F, f2 = 0.0F, f3 = 8.0F;
    for (Unit unit : paramSeq) {
      f1 += unit.x;
      f2 += unit.y;
      f3 = Math.max(f3, unit.hitSize);
    } 
    f1 /= i;
    f2 /= i;
    float f4 = Math.max(spacing() * 8.0F, f3 * 1.15F);
    float f5 = (face() == 1) ? 1.5707964F : Mathf.atan2(paramVec2.y - f2, paramVec2.x - f1);
    float[] arrayOfFloat1 = layout(paramInt, i, f4, f5);
    float f6 = Mathf.cos(f5), f7 = Mathf.sin(f5), f8 = -f7, f9 = f6;
    ArrayList<Integer> arrayList1 = new ArrayList();
    for (byte b1 = 0; b1 < i; ) {
      arrayList1.add(Integer.valueOf(b1));
      b1++;
    } 
    ArrayList<Unit> arrayList = new ArrayList();
    for (Unit unit : paramSeq)
      arrayList.add(unit); 
    if (paramInt == 3) {
      f10 = f5 + 3.1415927F;
      arrayList1.sort(Comparator.comparingDouble(paramInteger -> Mathf.mod(Mathf.atan2(paramArrayOffloat[paramInteger.intValue() * 2 + 1], paramArrayOffloat[paramInteger.intValue() * 2]) - paramFloat, 6.2831855F)));
      f11 = f1;
      float f = f2;
      arrayList.sort(Comparator.comparingDouble(paramUnit -> Mathf.mod(Mathf.atan2(paramUnit.y - paramFloat1, paramUnit.x - paramFloat2) - paramFloat3, 6.2831855F)));
    } else {
      arrayList1.sort(Comparator.<Integer>comparingDouble(paramInteger -> (paramArrayOffloat[paramInteger.intValue() * 2] * paramFloat1 + paramArrayOffloat[paramInteger.intValue() * 2 + 1] * paramFloat2)).thenComparingDouble(paramInteger -> -(paramArrayOffloat[paramInteger.intValue() * 2] * paramFloat1 + paramArrayOffloat[paramInteger.intValue() * 2 + 1] * paramFloat2)));
      f10 = f1;
      f11 = f2;
      arrayList.sort(Comparator.<Unit>comparingDouble(paramUnit -> ((paramUnit.x - paramFloat1) * paramFloat2 + (paramUnit.y - paramFloat3) * paramFloat4)).thenComparingDouble(paramUnit -> -((paramUnit.x - paramFloat1) * paramFloat2 + (paramUnit.y - paramFloat3) * paramFloat4)));
    } 
    int j = center();
    if (j == 1) {
      Unit unit = (Unit)paramSeq.first();
      int k = arrayList.indexOf(unit);
      int m = ((Integer)arrayList1.get(Math.max(0, k))).intValue();
      f10 = paramVec2.x - arrayOfFloat1[m * 2];
      f11 = paramVec2.y - arrayOfFloat1[m * 2 + 1];
    } else if (j == 2) {
      f10 = Core.camera.position.x;
      f11 = Core.camera.position.y;
    } else {
      f10 = paramVec2.x;
      f11 = paramVec2.y;
    } 
    curLegs.clear();
    retries.clear();
    loopA.clear();
    loopB.clear();
    reformed = false;
    float[] arrayOfFloat2 = layout(paramInt, i, f4, f5 + 3.1415927F);
    ArrayList<Integer> arrayList2 = new ArrayList();
    float f12 = f6, f13 = f7;
    ArrayList<Integer> arrayList3 = new ArrayList();
    byte b2;
    for (b2 = 0; b2 < i; ) {
      arrayList3.add(Integer.valueOf(b2));
      b2++;
    } 
    arrayList3.sort(Comparator.comparingDouble(paramInteger -> -(paramArrayOffloat[((Integer)paramArrayList.get(paramInteger.intValue())).intValue() * 2] * paramFloat1 + paramArrayOffloat[((Integer)paramArrayList.get(paramInteger.intValue())).intValue() * 2 + 1] * paramFloat2)));
    for (b2 = 0; b2 < i; b2++) {
      Unit unit = arrayList.get(b2);
      int k = ((Integer)arrayList1.get(b2)).intValue();
      loopB.put(Integer.valueOf(unit.id), makeLeg(unit, f10 + arrayOfFloat1[k * 2], f11 + arrayOfFloat1[k * 2 + 1], f6, f7));
      loopA.put(Integer.valueOf(unit.id), makeLeg(unit, f1 + arrayOfFloat2[k * 2], f2 + arrayOfFloat2[k * 2 + 1], -f6, -f7));
    } 
    curLegs.putAll(loopB);
    for (Iterator<Integer> iterator = arrayList3.iterator(); iterator.hasNext(); ) {
      int k = ((Integer)iterator.next()).intValue();
      arrayList2.add(Integer.valueOf(((Unit)arrayList.get(k)).id));
    } 
    dispatch(arrayList2, curLegs, true);
    loopActive = loop();
    loopToB = true;
    loopWaitUntil = 0.0F;
    nextLoop = Time.time + 60.0F;
    lastGroup = null;
    nextCheck = Time.time + 30.0F;
    if (Vars.ui != null && !loopActive)
      Vars.ui.showInfoToast(modeName(paramInt) + " x" + i + ((i > 1) ? (" (" + ((i + Math.max(1, Math.min(i, (perLayer() > 0) ? perLayer() : ((layers() > 0) ? ((i + layers() - 1) / layers()) : ((paramInt == 1) ? 14 : i)))) - 1) / Math.max(1, Math.min(i, (perLayer() > 0) ? perLayer() : ((layers() > 0) ? ((i + layers() - 1) / layers()) : ((paramInt == 1) ? 14 : i))))) + tr(" layers)", " camadas)")) : ""), 1.5F); 
  }
  
  private static void reformCheck() {
    if (!reform() || loopActive || !pending.isEmpty() || curLegs.isEmpty() || Time.time < nextCheck)
      return; 
    nextCheck = Time.time + 30.0F;
    boolean bool = true;
    ArrayList<Integer> arrayList1 = new ArrayList();
    ArrayList<Integer> arrayList2 = new ArrayList();
    for (Map.Entry<Integer, Vec2> entry : curLegs.entrySet()) {
      Unit unit = (Unit)Groups.unit.getByID(((Integer)entry.getKey()).intValue());
      if (unit == null || !unit.isValid() || !(unit.controller() instanceof CommandAI)) {
        arrayList1.add((Integer)entry.getKey());
        continue;
      } 
      CommandAI commandAI = (CommandAI)unit.controller();
      if (commandAI.targetPos != null) {
        bool = false;
        continue;
      } 
      float f = Math.max(6.0F, unit.hitSize * 0.45F);
      Integer integer = retries.get(entry.getKey());
      if (unit.dst((Position)((Vec2[])entry.getValue())[2]) > f && (integer == null || integer.intValue() < 3))
        arrayList2.add((Integer)entry.getKey()); 
    } 
    for (Integer integer : arrayList1)
      curLegs.remove(integer); 
    if (!bool)
      return; 
    if (arrayList2.isEmpty()) {
      curLegs.clear();
      return;
    } 
    for (Integer integer : arrayList2)
      retries.put(integer, Integer.valueOf(retries.containsKey(integer) ? (((Integer)retries.get(integer)).intValue() + 1) : 1)); 
    dispatch(arrayList2, curLegs, false);
  }
  
  static class Preview extends Element {
    final int mode;
    
    final float w;
    
    final float h;
    
    Preview(int param1Int, float param1Float1, float param1Float2) {
      this.mode = param1Int;
      this.w = param1Float1;
      this.h = param1Float2;
    }
    
    public float getPrefWidth() {
      return this.w;
    }
    
    public float getPrefHeight() {
      return this.h;
    }
    
    public void draw() {
      float f1 = this.parentAlpha;
      Draw.color(0.07F, 0.08F, 0.1F, 0.9F * f1);
      Fill.rect(this.x + this.width / 2.0F, this.y + this.height / 2.0F, this.width, this.height);
      if (this.mode == 0) {
        Draw.color(UiStyle.DIM.r, UiStyle.DIM.g, UiStyle.DIM.b, f1);
        Fill.rect(this.x + this.width / 2.0F, this.y + this.height / 2.0F, 18.0F, 3.0F);
        Draw.color();
        return;
      } 
      byte b1 = (Formations.perLayer() > 0 || Formations.layers() > 0) ? Math.min(30, Math.max(1, ((Formations.perLayer() > 0) ? Formations.perLayer() : 5) * ((Formations.layers() > 0) ? Formations.layers() : 2))) : ((this.mode == 3) ? 9 : 7);
      float[] arrayOfFloat = Formations.layout(this.mode, b1, 1.0F, 1.5707964F);
      float f2 = 1.0E9F, f3 = -1.0E9F, f4 = 1.0E9F, f5 = -1.0E9F;
      for (byte b2 = 0; b2 < b1; b2++) {
        f2 = Math.min(f2, arrayOfFloat[b2 * 2]);
        f3 = Math.max(f3, arrayOfFloat[b2 * 2]);
        f4 = Math.min(f4, arrayOfFloat[b2 * 2 + 1]);
        f5 = Math.max(f5, arrayOfFloat[b2 * 2 + 1]);
      } 
      float f6 = Math.min((this.width - 26.0F) / Math.max(0.01F, f3 - f2), (this.height - 22.0F) / Math.max(0.01F, f5 - f4));
      f6 = Math.min(f6, 16.0F);
      float f7 = (f2 + f3) / 2.0F, f8 = (f4 + f5) / 2.0F;
      for (byte b3 = 0; b3 < b1; b3++) {
        Draw.color(Pal.accent.r, Pal.accent.g, Pal.accent.b, f1);
        Fill.circle(this.x + this.width / 2.0F + (arrayOfFloat[b3 * 2] - f7) * f6, this.y + this.height / 2.0F + (arrayOfFloat[b3 * 2 + 1] - f8) * f6, 3.6F);
      } 
      Draw.color(1.0F, 1.0F, 1.0F, 0.5F * f1);
      Fill.tri(this.x + this.width - 11.0F, this.y + this.height - 8.0F, this.x + this.width - 5.0F, this.y + this.height - 8.0F, this.x + this.width - 8.0F, this.y + this.height - 3.0F);
      Draw.color();
    }
  }
  
  public static void openDialog() {
    BaseDialog baseDialog = new BaseDialog(tr("Formations", "Formacoes"));
    UiStyle.register(baseDialog);
    Table table = new Table();
    Runnable[] arrayOfRunnable = new Runnable[1];
    arrayOfRunnable[0] = (() -> {
        paramTable.clear();
        paramTable.defaults().pad(4.0F);
        paramTable.add(tr("Select units in command mode, choose a shape and send them to move or attack. The mod sends one position per unit.", "Selecione unidades no modo comando, escolha o formato e mande mover ou atacar. O mod envia uma posicao por unidade.")).color(UiStyle.TEXT_DIM).wrap().width(420.0F).left().row();
        UiStyle.section(paramTable, tr("Shape", "Formato"));
        paramTable.table(()).row();
        UiStyle.section(paramTable, tr("Spacing", "Espacamento"));
        paramTable.table(()).row();
        paramTable.add(tr("Never closer than the unit size.", "Nunca fica mais perto que o tamanho da unidade.")).color(UiStyle.TEXT_DIM).fontScale(0.8F).left().row();
        UiStyle.section(paramTable, tr("Layers", "Camadas"));
        paramTable.add(tr("Add more layers behind the first one. 0 = automatic.", "Adicione mais camadas atras da primeira. 0 = automatico.")).color(UiStyle.TEXT_DIM).wrap().width(420.0F).left().row();
        paramTable.table(()).row();
        paramTable.table(()).row();
        UiStyle.section(paramTable, tr("Gradual and loop", "Gradual e loop"));
        paramTable.table(()).left().row();
        paramTable.table(()).row();
        paramTable.table(()).left().row();
        paramTable.table(()).row();
        paramTable.add(tr("Loop stops when you give the units a new command or turn it off.", "O loop para quando voce da um novo comando as unidades ou desliga.")).color(UiStyle.TEXT_DIM).wrap().width(420.0F).left().row();
        UiStyle.section(paramTable, tr("Center", "Centro"));
        paramTable.table(()).row();
        UiStyle.section(paramTable, tr("Facing", "Orientacao"));
        paramTable.table(()).row();
        paramTable.table(()).left().row();
        paramTable.table(()).left().row();
      });
    arrayOfRunnable[0].run();
    baseDialog.cont.pane((Element)table).grow().minWidth(440.0F);
    baseDialog.addCloseButton();
    baseDialog.show();
  }
  
  private static String tr(String paramString1, String paramString2) {
    return (LunariansSettings.language != null && LunariansSettings.language.startsWith("pt")) ? paramString2 : paramString1;
  }
}
