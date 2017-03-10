/*     */ package edu.drexel.cis.dragon.nlp;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*     */ import edu.drexel.cis.dragon.util.FastBinaryReader;
/*     */ import edu.drexel.cis.dragon.util.FastBinaryWriter;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class SimplePairList
/*     */ {
/*     */   private SortedArray keyList;
/*     */   private SortedArray indexList;
/*     */   private boolean writingMode;
/*     */   private String keylistFilename;
/*     */ 
/*     */   public SimplePairList()
/*     */   {
/*  23 */     this.keyList = new SortedArray();
/*  24 */     this.keylistFilename = null;
/*  25 */     this.writingMode = true;
/*     */   }
/*     */ 
/*     */   public SimplePairList(String filename, boolean writingMode) {
/*  29 */     this.writingMode = writingMode;
/*  30 */     this.keylistFilename = filename;
/*  31 */     this.keyList = loadRelationKeyList(filename);
/*  32 */     if (!writingMode)
/*  33 */       this.indexList = this.keyList.copy(new IndexComparator());
/*     */   }
/*     */ 
/*     */   public int add(int firstElement, int secondElement)
/*     */   {
/*  39 */     if (!this.writingMode) return -1;
/*  40 */     SimplePair pair = new SimplePair(this.keyList.size(), firstElement, secondElement);
/*  41 */     if (!this.keyList.add(pair))
/*  42 */       pair = (SimplePair)this.keyList.get(this.keyList.insertedPos());
/*  43 */     return pair.getIndex();
/*     */   }
/*     */ 
/*     */   public int search(int firstElement, int secondElement)
/*     */   {
/*  49 */     int pos = this.keyList.binarySearch(new SimplePair(0, firstElement, secondElement));
/*  50 */     if (pos >= 0) {
/*  51 */       return ((SimplePair)this.keyList.get(pos)).getIndex();
/*     */     }
/*  53 */     return -1;
/*     */   }
/*     */ 
/*     */   public SimplePair get(int index) {
/*  57 */     if (this.indexList == null)
/*     */     {
/*  59 */       if (this.keylistFilename == null) {
/*  60 */         this.writingMode = false;
/*  61 */         this.indexList = this.keyList.copy(new IndexComparator());
/*     */       }
/*     */       else {
/*  64 */         return null;
/*     */       }
/*     */     }
/*  66 */     return (SimplePair)this.indexList.get(index);
/*     */   }
/*     */ 
/*     */   public int size() {
/*  70 */     return this.keyList.size();
/*     */   }
/*     */ 
/*     */   public void close() {
/*  74 */     if (this.writingMode)
/*  75 */       saveRelationKeyList(this.keylistFilename, this.keyList);
/*  76 */     this.keyList.clear();
/*     */   }
/*     */ 
/*     */   private SortedArray loadRelationKeyList(String filename)
/*     */   {
/*     */     try
/*     */     {
/*  88 */       File file = new File(filename);
/*  89 */       if (!file.exists()) {
/*  90 */         return new SortedArray();
/*     */       }
/*  92 */       System.out.println(new Date() + " Loading Pair List...");
/*  93 */       FastBinaryReader br = new FastBinaryReader(filename);
/*  94 */       int total = br.readInt();
/*  95 */       ArrayList list = new ArrayList(total);
/*  96 */       for (int i = 0; i < total; i++) {
/*  97 */         SimplePair cur = new SimplePair(br.readInt(), br.readInt(), br.readInt());
/*  98 */         list.add(cur);
/*     */       }
/* 100 */       br.close();
/*     */ 
/* 102 */       SortedArray relationList = new SortedArray();
/* 103 */       relationList.addAll(list);
/* 104 */       return relationList;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 108 */       e.printStackTrace();
/* 109 */     }return null;
/*     */   }
/*     */ 
/*     */   private void saveRelationKeyList(String filename, ArrayList list)
/*     */   {
/*     */     try
/*     */     {
/* 119 */       if (list == null) return;
/*     */ 
/* 121 */       System.out.println(new Date() + " Saving Pair List...");
/* 122 */       FastBinaryWriter fbw = new FastBinaryWriter(filename);
/* 123 */       fbw.writeInt(list.size());
/* 124 */       for (int i = 0; i < list.size(); i++) {
/* 125 */         SimplePair cur = (SimplePair)list.get(i);
/* 126 */         fbw.writeInt(cur.getIndex());
/* 127 */         fbw.writeInt(cur.getFirstElement());
/* 128 */         fbw.writeInt(cur.getSecondElement());
/* 129 */         if (i % 100000 == 0)
/* 130 */           fbw.flush();
/*     */       }
/* 132 */       fbw.close();
/*     */     }
/*     */     catch (Exception ex) {
/* 135 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.SimplePairList
 * JD-Core Version:    0.6.2
 */