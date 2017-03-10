/*     */ package edu.drexel.cis.dragon.nlp;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class SimpleElementList
/*     */ {
/*     */   private SortedArray keyList;
/*     */   private SortedArray indexList;
/*     */   private boolean writingMode;
/*     */   private boolean continuousIndex;
/*     */   private String keylistFilename;
/*     */ 
/*     */   public SimpleElementList()
/*     */   {
/*  24 */     this.keylistFilename = null;
/*  25 */     this.keyList = new SortedArray();
/*  26 */     this.writingMode = true;
/*     */   }
/*     */ 
/*     */   public SimpleElementList(String filename, boolean writingMode) {
/*  30 */     this.writingMode = writingMode;
/*  31 */     this.keylistFilename = filename;
/*  32 */     this.keyList = loadKeyList(filename);
/*  33 */     if (!writingMode) {
/*  34 */       this.indexList = this.keyList.copy(new IndexComparator());
/*  35 */       if ((this.indexList.size() == 0) || (this.indexList.size() == ((SimpleElement)this.indexList.get(this.indexList.size() - 1)).getIndex() + 1))
/*  36 */         this.continuousIndex = true;
/*     */       else
/*  38 */         this.continuousIndex = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isWritingMode() {
/*  43 */     return this.writingMode;
/*     */   }
/*     */ 
/*     */   public int add(String key)
/*     */   {
/*  49 */     if (!this.writingMode)
/*  50 */       return -1;
/*  51 */     SimpleElement term = new SimpleElement(new String(key), this.keyList.size());
/*  52 */     if (!this.keyList.add(term))
/*  53 */       term = (SimpleElement)this.keyList.get(this.keyList.insertedPos());
/*  54 */     return term.getIndex();
/*     */   }
/*     */ 
/*     */   public boolean add(SimpleElement element) {
/*  58 */     if (!this.writingMode)
/*  59 */       return false;
/*  60 */     return this.keyList.add(element);
/*     */   }
/*     */ 
/*     */   public int search(String key)
/*     */   {
/*  66 */     int pos = this.keyList.binarySearch(new SimpleElement(key, -1));
/*  67 */     if (pos >= 0) {
/*  68 */       return ((SimpleElement)this.keyList.get(pos)).getIndex();
/*     */     }
/*  70 */     return -1;
/*     */   }
/*     */ 
/*     */   public String search(int index) {
/*  74 */     if (this.indexList == null)
/*     */     {
/*  76 */       if (this.keylistFilename == null) {
/*  77 */         this.writingMode = false;
/*  78 */         this.indexList = this.keyList.copy(new IndexComparator());
/*  79 */         if (this.indexList.size() == ((SimpleElement)this.indexList.get(this.indexList.size() - 1)).getIndex() + 1)
/*  80 */           this.continuousIndex = true;
/*     */         else
/*  82 */           this.continuousIndex = false;
/*     */       }
/*     */       else {
/*  85 */         return null;
/*     */       }
/*     */     }
/*  88 */     if (this.continuousIndex) {
/*  89 */       return ((SimpleElement)this.indexList.get(index)).getKey();
/*     */     }
/*  91 */     int pos = this.indexList.binarySearch(new SimpleElement(null, index));
/*  92 */     if (pos >= 0) {
/*  93 */       return ((SimpleElement)this.indexList.get(pos)).getKey();
/*     */     }
/*  95 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean contains(String key)
/*     */   {
/* 100 */     return this.keyList.contains(new SimpleElement(key, -1));
/*     */   }
/*     */ 
/*     */   public int size() {
/* 104 */     return this.keyList.size();
/*     */   }
/*     */ 
/*     */   public void close() {
/* 108 */     if ((this.writingMode) && (this.keylistFilename != null))
/* 109 */       saveKeyList(this.keylistFilename, this.keyList);
/* 110 */     if (this.keyList != null) this.keyList.clear();
/* 111 */     if (this.indexList != null) this.indexList.clear();
/*     */   }
/*     */ 
/*     */   private void saveKeyList(String filename, ArrayList list)
/*     */   {
/*     */     try
/*     */     {
/* 120 */       if (list == null) return;
/*     */ 
/* 122 */       System.out.println(new Date() + " Saving Element List...");
/* 123 */       BufferedWriter bw = FileUtil.getTextWriter(filename);
/* 124 */       bw.write(list.size());
/* 125 */       bw.write("\n");
/* 126 */       for (int i = 0; i < list.size(); i++) {
/* 127 */         SimpleElement cur = (SimpleElement)list.get(i);
/* 128 */         bw.write(cur.getKey() + "\t" + cur.getIndex() + "\n");
/* 129 */         bw.flush();
/*     */       }
/* 131 */       bw.close();
/*     */     }
/*     */     catch (Exception ex) {
/* 134 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private SortedArray loadKeyList(String filename)
/*     */   {
/*     */     try
/*     */     {
/* 148 */       File file = new File(filename);
/* 149 */       if (!file.exists()) {
/* 150 */         return new SortedArray();
/*     */       }
/* 152 */       System.out.println(new Date() + " Loading Element List...");
/* 153 */       BufferedReader br = FileUtil.getTextReader(filename);
/* 154 */       String line = br.readLine();
/* 155 */       String[] arrField = line.split("\t");
/* 156 */       int total = Integer.parseInt(arrField[0]);
/* 157 */       ArrayList list = new ArrayList(total);
/*     */ 
/* 159 */       for (int i = 0; i < total; i++) {
/* 160 */         line = br.readLine();
/* 161 */         arrField = line.split("\t");
/* 162 */         list.add(new SimpleElement(arrField[0], Integer.parseInt(arrField[(arrField.length - 1)])));
/*     */       }
/* 164 */       br.close();
/* 165 */       Collections.sort(list);
/* 166 */       SortedArray termList = new SortedArray();
/* 167 */       termList.addAll(list);
/* 168 */       return termList;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 172 */       e.printStackTrace();
/* 173 */     }return null;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.SimpleElementList
 * JD-Core Version:    0.6.2
 */