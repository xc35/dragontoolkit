/*     */ package edu.drexel.cis.dragon.ml.seqmodel.feature;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.MathUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public class FeatureDictionaryChar extends AbstractFeatureDictionary
/*     */   implements FeatureDictionary
/*     */ {
/*     */   private SortedArray dictionary;
/*     */ 
/*     */   public FeatureDictionaryChar(int stateNum, int capacity)
/*     */   {
/*  22 */     super(stateNum);
/*  23 */     this.dictionary = new SortedArray(capacity);
/*  24 */     this.finalized = false;
/*     */   }
/*     */ 
/*     */   public int getIndex(Object feature)
/*     */   {
/*  30 */     int pos = this.dictionary.binarySearch(new HEntry((String)feature, -1));
/*  31 */     if (pos < 0) {
/*  32 */       return -1;
/*     */     }
/*  34 */     return ((HEntry)this.dictionary.get(pos)).getIndex();
/*     */   }
/*     */ 
/*     */   public int getStateNum() {
/*  38 */     return this.stateNum;
/*     */   }
/*     */ 
/*     */   public boolean contain(Object feature) {
/*  42 */     return this.dictionary.contains(feature);
/*     */   }
/*     */ 
/*     */   public int size() {
/*  46 */     return this.dictionary.size();
/*     */   }
/*     */ 
/*     */   public int addFeature(Object feature, int label)
/*     */   {
/*  52 */     if ((label < 0) || (this.finalized)) {
/*  53 */       return -1;
/*     */     }
/*  55 */     HEntry index = new HEntry((String)feature, this.dictionary.size(), this.stateNum);
/*  56 */     if (!this.dictionary.add(index))
/*  57 */       index = (HEntry)this.dictionary.get(this.dictionary.insertedPos());
/*  58 */     index.addFrequency(label, 1);
/*  59 */     return index.getIndex();
/*     */   }
/*     */ 
/*     */   public void finalize()
/*     */   {
/*  66 */     this.cntsOverAllFeature = new int[this.stateNum];
/*  67 */     this.cntsArray = new int[this.dictionary.size()][];
/*  68 */     this.cntsOverAllState = new int[this.dictionary.size()];
/*  69 */     for (int i = 0; i < this.dictionary.size(); i++) {
/*  70 */       HEntry entry = (HEntry)this.dictionary.get(i);
/*  71 */       this.cntsArray[entry.index] = entry.stateArray;
/*  72 */       this.cntsOverAllState[entry.index] = ((int)MathUtil.sumArray(entry.stateArray));
/*     */     }
/*     */       int i;
/*  75 */     for (i = 0; i < this.stateNum; i++) {
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
/*  93 */       BufferedReader in = new BufferedReader(new FileReader(new File(filename)));
/*  94 */       int dictLen = Integer.parseInt(in.readLine());
/*  95 */       this.cntsOverAllFeature = new int[this.stateNum];
/*  96 */       this.cntsArray = new int[dictLen][this.stateNum];
/*  97 */       this.cntsOverAllState = new int[dictLen];
/*     */       String line;
/*  98 */       for (int l = 0; (l < dictLen) && ((line = in.readLine()) != null); l++)
/*     */       {
/*  99 */         StringTokenizer entry = new StringTokenizer(line, " ");
/* 100 */         String key = entry.nextToken();
/* 101 */         int pos = Integer.parseInt(entry.nextToken());
/* 102 */         HEntry hEntry = new HEntry(key, pos);
/* 103 */         this.dictionary.add(hEntry);
/* 104 */         while (entry.hasMoreTokens()) {
/* 105 */           StringTokenizer scp = new StringTokenizer(entry.nextToken(), ":");
/* 106 */           int state = Integer.parseInt(scp.nextToken());
/* 107 */           int cnt = Integer.parseInt(scp.nextToken());
/* 108 */           this.cntsArray[pos][state] = cnt;
/*     */         }
/* 110 */         this.cntsOverAllState[pos] = ((int)MathUtil.sumArray(this.cntsArray[pos]));
/*     */       }
/* 112 */       for (int i = 0; i < this.stateNum; i++) {
/* 113 */         this.cntsOverAllFeature[i] = 0;
/* 114 */         for (int m = 0; m < this.cntsArray.length; m++) {
/* 115 */           this.cntsOverAllFeature[i] += this.cntsArray[m][i];
/*     */         }
/* 117 */         this.allTotal += this.cntsOverAllFeature[i];
/*     */       }
/* 119 */       this.finalized = true;
/* 120 */       return true;
/*     */     }
/*     */     catch (Exception e) {
/* 123 */       e.printStackTrace();
/* 124 */     }return false;
/*     */   }
/*     */ 
/*     */   public boolean write(String filename)
/*     */   {
/*     */     try
/*     */     {
/* 135 */       PrintWriter out = new PrintWriter(new FileOutputStream(new File(filename)));
/* 136 */       out.println(this.dictionary.size());
/* 137 */       for (int i = 0; i < this.dictionary.size(); i++) {
/* 138 */         HEntry entry = (HEntry)this.dictionary.get(i);
/* 139 */         String key = entry.getFeature();
/* 140 */         int pos = entry.getIndex();
/* 141 */         out.print(key + " " + pos);
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
/*     */   private class HEntry implements Comparable {
/*     */     private String feature;
/*     */     private int index;
/*     */     private int[] stateArray;
/*     */ 
/*     */     public HEntry(String feature, int v) {
/* 162 */       this.feature = feature;
/* 163 */       this.index = v;
/* 164 */       this.stateArray = null;
/*     */     }
/*     */ 
/*     */     public HEntry(String feature, int v, int numStates) {
/* 168 */       this.feature = feature;
/* 169 */       this.index = v;
/* 170 */       this.stateArray = new int[numStates];
/*     */     }
/*     */ 
/*     */     public void addFrequency(int state, int inc) {
/* 174 */       this.stateArray[state] += inc;
/*     */     }
/*     */ 
/*     */     public int getIndex() {
/* 178 */       return this.index;
/*     */     }
/*     */ 
/*     */     public String getFeature() {
/* 182 */       return this.feature;
/*     */     }
/*     */ 
/*     */     public int compareTo(Object o) {
/* 186 */       return this.feature.compareTo(((HEntry)o).getFeature());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.FeatureDictionaryChar
 * JD-Core Version:    0.6.2
 */