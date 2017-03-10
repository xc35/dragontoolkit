/*     */ package edu.drexel.cis.dragon.ml.seqmodel.feature;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.MathUtil;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public class FeatureDictionaryNum extends AbstractFeatureDictionary
/*     */   implements FeatureDictionary
/*     */ {
/*     */   private Hashtable dictionary;
/*     */ 
/*     */   public FeatureDictionaryNum(int stateNum, int capacity)
/*     */   {
/*  22 */     super(stateNum);
/*  23 */     this.dictionary = new Hashtable(capacity);
/*  24 */     this.finalized = false;
/*     */   }
/*     */ 
/*     */   public int getIndex(Object feature)
/*     */   {
/*  30 */     HEntry entry = (HEntry)this.dictionary.get(feature);
/*  31 */     if (entry == null) {
/*  32 */       return -1;
/*     */     }
/*  34 */     return entry.getIndex();
/*     */   }
/*     */ 
/*     */   public boolean contain(Object feature) {
/*  38 */     return this.dictionary.get(feature) != null;
/*     */   }
/*     */ 
/*     */   public int size() {
/*  42 */     return this.dictionary.size();
/*     */   }
/*     */ 
/*     */   public int addFeature(Object feature, int label)
/*     */   {
/*  48 */     if ((label < 0) || (this.finalized)) return -1;
/*     */ 
/*  50 */     HEntry index = (HEntry)this.dictionary.get(feature);
/*  51 */     if (index == null) {
/*  52 */       index = new HEntry(this.dictionary.size(), this.stateNum);
/*  53 */       this.dictionary.put(feature, index);
/*     */     }
/*  55 */     index.addFrequency(label, 1);
/*  56 */     return index.getIndex();
/*     */   }
/*     */ 
/*     */   public void finalize()
/*     */   {
/*  65 */     this.cntsOverAllFeature = new int[this.stateNum];
/*  66 */     this.cntsArray = new int[this.dictionary.size()][];
/*  67 */     this.cntsOverAllState = new int[this.dictionary.size()];
/*  68 */     for (Enumeration e = this.dictionary.keys(); e.hasMoreElements(); ) {
/*  69 */       Integer key = (Integer)e.nextElement();
/*  70 */       HEntry entry = (HEntry)this.dictionary.get(key);
/*  71 */       this.cntsArray[entry.index] = entry.stateArray;
/*  72 */       this.cntsOverAllState[entry.index] = ((int)MathUtil.sumArray(entry.stateArray));
/*     */     }
/*     */ 
/*  75 */     for (int i = 0; i < this.stateNum; i++) {
/*  76 */       this.cntsOverAllFeature[i] = 0;
/*  77 */       for (int m = 0; m < this.cntsArray.length; m++) {
/*  78 */         this.cntsOverAllFeature[i] += this.cntsArray[m][i];
/*     */       }
/*  80 */       this.allTotal += this.cntsOverAllFeature[i];
/*     */     }
/*  82 */     this.finalized = true;
/*     */   }
/*     */ 
/*     */   public boolean read(String filename)
/*     */   {
/*     */     try
/*     */     {
/*  94 */       BufferedReader in = new BufferedReader(new FileReader(new File(filename)));
/*  95 */       int dictLen = Integer.parseInt(in.readLine());
/*  96 */       this.cntsOverAllFeature = new int[this.stateNum];
/*  97 */       this.cntsArray = new int[dictLen][this.stateNum];
/*  98 */       this.cntsOverAllState = new int[dictLen];
/*     */       String line;
/*  99 */       for (int l = 0; (l < dictLen) && ((line = in.readLine()) != null); l++)
/*     */       {
/* 100 */         StringTokenizer entry = new StringTokenizer(line, " ");
/* 101 */         Integer key = Integer.getInteger(entry.nextToken());
/* 102 */         int pos = Integer.parseInt(entry.nextToken());
/* 103 */         HEntry hEntry = new HEntry(pos);
/* 104 */         this.dictionary.put(key, hEntry);
/* 105 */         while (entry.hasMoreTokens()) {
/* 106 */           StringTokenizer scp = new StringTokenizer(entry.nextToken(), ":");
/* 107 */           int state = Integer.parseInt(scp.nextToken());
/* 108 */           int cnt = Integer.parseInt(scp.nextToken());
/* 109 */           this.cntsArray[pos][state] = cnt;
/*     */         }
/* 111 */         this.cntsOverAllState[pos] = ((int)MathUtil.sumArray(this.cntsArray[pos]));
/*     */       }
/* 113 */       for (int i = 0; i < this.stateNum; i++) {
/* 114 */         this.cntsOverAllFeature[i] = 0;
/* 115 */         for (int m = 0; m < this.cntsArray.length; m++) {
/* 116 */           this.cntsOverAllFeature[i] += this.cntsArray[m][i];
/*     */         }
/* 118 */         this.allTotal += this.cntsOverAllFeature[i];
/*     */       }
/* 120 */       this.finalized = true;
/* 121 */       return true;
/*     */     }
/*     */     catch (Exception e) {
/* 124 */       e.printStackTrace();
/* 125 */     }return false;
/*     */   }
/*     */ 
/*     */   public boolean write(String filename)
/*     */   {
/*     */     try
/*     */     {
/* 136 */       PrintWriter out = new PrintWriter(new FileOutputStream(new File(filename)));
/* 137 */       out.println(this.dictionary.size());
/* 138 */       for (Enumeration element = this.dictionary.keys(); element.hasMoreElements(); ) {
/* 139 */         Integer key = (Integer)element.nextElement();
/* 140 */         int pos = getIndex(key);
/* 141 */         out.print(key.toString() + " " + pos);
/* 142 */         for (int s = getNextStateWithFeature(pos, -1); s != -1; s = getNextStateWithFeature(pos, s)) {
/* 143 */           out.print(" " + s + ":" + this.cntsArray[pos][s]);
/*     */         }
/* 145 */         out.println("");
/*     */       }
/* 147 */       out.close();
/* 148 */       return true;
/*     */     }
/*     */     catch (Exception e) {
/* 151 */       e.printStackTrace();
/* 152 */     }return false;
/*     */   }
/*     */ 
/*     */   private class HEntry {
/*     */     private int index;
/*     */     private int[] stateArray;
/*     */ 
/*     */     public HEntry(int v) {
/* 161 */       this.index = v;
/* 162 */       this.stateArray = null;
/*     */     }
/*     */ 
/*     */     public HEntry(int v, int numStates) {
/* 166 */       this.index = v;
/* 167 */       this.stateArray = new int[numStates];
/*     */     }
/*     */ 
/*     */     public void addFrequency(int state, int inc) {
/* 171 */       this.stateArray[state] += inc;
/*     */     }
/*     */ 
/*     */     public int getIndex() {
/* 175 */       return this.index;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.FeatureDictionaryNum
 * JD-Core Version:    0.6.2
 */