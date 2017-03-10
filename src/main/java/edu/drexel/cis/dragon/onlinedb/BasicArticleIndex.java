/*     */ package edu.drexel.cis.dragon.onlinedb;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElement;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ 
/*     */ public class BasicArticleIndex
/*     */ {
/*     */   private long collectionSize;
/*     */   private SortedArray list;
/*     */   private SimpleElementList fileList;
/*     */   private boolean writingMode;
/*     */   private String indexFilename;
/*     */ 
/*     */   public BasicArticleIndex(String indexFilename, boolean writingMode)
/*     */   {
/*  23 */     this.writingMode = writingMode;
/*  24 */     this.indexFilename = indexFilename;
/*  25 */     loadKeyList(indexFilename);
/*     */   }
/*     */ 
/*     */   public boolean isWritingMode() {
/*  29 */     return this.writingMode;
/*     */   }
/*     */ 
/*     */   public boolean add(String key, long offset)
/*     */   {
/*  35 */     if (!this.writingMode)
/*  36 */       return false;
/*  37 */     BasicArticleKey basicKey = new BasicArticleKey(new String(key));
/*  38 */     basicKey.setOffset(offset);
/*  39 */     if (this.list.add(basicKey)) {
/*  40 */       return true;
/*     */     }
/*  42 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean add(String key, String filename, long offset, int length)
/*     */   {
/*  48 */     if (!this.writingMode)
/*  49 */       return false;
/*  50 */     BasicArticleKey basicKey = new BasicArticleKey(new String(key));
/*  51 */     basicKey.setOffset(offset);
/*  52 */     basicKey.setFileIndex(this.fileList.add(filename));
/*  53 */     basicKey.setLength(length);
/*  54 */     if (this.list.add(basicKey)) {
/*  55 */       return true;
/*     */     }
/*  57 */     return false;
/*     */   }
/*     */ 
/*     */   public String getFilename(int fileIndex) {
/*  61 */     return this.fileList.search(fileIndex);
/*     */   }
/*     */ 
/*     */   public boolean contains(String key) {
/*  65 */     return this.list.contains(new BasicArticleKey(key));
/*     */   }
/*     */ 
/*     */   public BasicArticleKey search(String key)
/*     */   {
/*  71 */     int pos = this.list.binarySearch(new BasicArticleKey(key));
/*  72 */     if (pos >= 0) {
/*  73 */       return (BasicArticleKey)this.list.get(pos);
/*     */     }
/*  75 */     return null;
/*     */   }
/*     */ 
/*     */   public void setCollectionFileSize(long size) {
/*  79 */     this.collectionSize = size;
/*     */   }
/*     */ 
/*     */   public long getCollectionFileSize() {
/*  83 */     return this.collectionSize;
/*     */   }
/*     */ 
/*     */   public void close() {
/*  87 */     if (this.writingMode)
/*  88 */       saveKeyList(this.indexFilename, this.list, this.fileList);
/*  89 */     this.list.clear();
/*     */   }
/*     */ 
/*     */   private void loadKeyList(String indexFilename)
/*     */   {
/*     */     try
/*     */     {
/* 101 */       File file = new File(indexFilename);
/* 102 */       if (!file.exists()) {
/* 103 */         this.list = new SortedArray();
/* 104 */         this.fileList = new SimpleElementList();
/* 105 */         return;
/*     */       }
/*     */ 
/* 108 */       BufferedReader br = FileUtil.getTextReader(file);
/* 109 */       String[] arrField = br.readLine().split("\t");
/* 110 */       int total = Integer.parseInt(arrField[0]);
/* 111 */       this.list = new SortedArray(total);
/* 112 */       this.collectionSize = Long.parseLong(arrField[1]);
/* 113 */       this.fileList = new SimpleElementList();
/*     */ 
/* 115 */       for (int i = 0; i < total; i++) {
/* 116 */         String line = br.readLine();
/* 117 */         arrField = line.split("\t");
/* 118 */         BasicArticleKey cur = new BasicArticleKey(arrField[0]);
/* 119 */         cur.setOffset(Long.parseLong(arrField[1]));
/* 120 */         if (arrField.length >= 4) {
/* 121 */           cur.setLength(Integer.parseInt(arrField[2]));
/* 122 */           cur.setFileIndex(Integer.parseInt(arrField[3]));
/*     */         }
/* 124 */         this.list.add(cur);
/*     */       }
/*     */       String line;
/* 127 */       while ((line = br.readLine()) != null)
/*     */       {
/* 128 */         arrField = line.split("\t");
/* 129 */         this.fileList.add(new SimpleElement(arrField[0], Integer.parseInt(arrField[1])));
/*     */       }
/* 131 */       br.close();
/*     */     }
/*     */     catch (Exception e) {
/* 134 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void saveKeyList(String docKeyListFilename, SortedArray list, SimpleElementList fileList)
/*     */   {
/*     */     try
/*     */     {
/* 144 */       if (list == null) {
/* 145 */         return;
/*     */       }
/* 147 */       BufferedWriter bw = FileUtil.getTextWriter(docKeyListFilename);
/* 148 */       bw.write(list.size() + "\t" + this.collectionSize + "\t" + fileList.size() + "\n");
/*     */ 
/* 150 */       for (int i = 0; i < list.size(); i++) {
/* 151 */         BasicArticleKey cur = (BasicArticleKey)list.get(i);
/* 152 */         bw.write(cur.getKey() + "\t" + cur.getOffset());
/* 153 */         if (cur.getLength() > 0)
/* 154 */           bw.write("\t" + cur.getLength() + "\t" + cur.getFileIndex());
/* 155 */         bw.write("\n");
/* 156 */         bw.flush();
/*     */       }
/*     */ 
/* 159 */       for (int i = 0; i < fileList.size(); i++) {
/* 160 */         bw.write(fileList.search(i) + "\t" + i + "\n");
/* 161 */         bw.flush();
/*     */       }
/* 163 */       bw.close();
/*     */     }
/*     */     catch (Exception ex) {
/* 166 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.BasicArticleIndex
 * JD-Core Version:    0.6.2
 */