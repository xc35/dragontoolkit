/*     */ package edu.drexel.cis.dragon.cptc;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import edu.drexel.cis.dragon.onlinedb.BasicCollectionReader;
/*     */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class ContentSelector
/*     */ {
/*  10 */   private String punc = " \r\n\t_-.;,?/\"'`:(){}!+[]><=%$#*@&^~|\\";
/*     */   private Lemmatiser lemmatiser;
/*     */   private int maxDist;
/*     */   private int leftExt;
/*     */   private int rightExt;
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*  20 */     ContentSelector selector = new ContentSelector();
/*  21 */     CollectionReader reader = new BasicCollectionReader("cptc/entity/trainingdoc.collection", "cptc/entity/trainingdoc.index");
/*  22 */     String key = "http://www.beaver.state.ut.us/";
/*  23 */     System.out.println(selector.select(reader.getArticleByKey(key).getBody(), "Beaver"));
/*     */   }
/*     */ 
/*     */   public ContentSelector() {
/*  27 */     this(null, 40, 100, 200);
/*     */   }
/*     */ 
/*     */   public ContentSelector(Lemmatiser lemmatiser, int maxDist, int leftExt, int rightExt) {
/*  31 */     this.maxDist = maxDist;
/*  32 */     this.leftExt = leftExt;
/*  33 */     this.rightExt = rightExt;
/*  34 */     this.lemmatiser = lemmatiser;
/*     */   }
/*     */ 
/*     */   public String select(String content, String word)
/*     */   {
/*  40 */     if (content == null)
/*  41 */       return null;
/*  42 */     content = content.replaceAll(" +", " ");
/*  43 */     SortedArray list = new SortedArray(new IndexComparator());
/*  44 */     mark(content.toLowerCase(), word.toLowerCase(), list);
/*  45 */     return generateContent(content, list);
/*     */   }
/*     */ 
/*     */   public String select(String content, String wordA, String wordB)
/*     */   {
/*  52 */     if (content == null)
/*  53 */       return null;
/*  54 */     content = content.replaceAll(" +", " ");
/*  55 */     String tmp = content.toLowerCase();
/*  56 */     SortedArray list = new SortedArray(new IndexComparator());
/*  57 */     mark(tmp, wordA.toLowerCase(), list);
/*  58 */     mark(tmp, wordB.toLowerCase(), list);
/*  59 */     list = merge(list);
/*  60 */     return generateContent(content, list);
/*     */   }
/*     */ 
/*     */   private void mark(String content, String word, SortedArray list)
/*     */   {
/*  67 */     int start = content.indexOf(word, 0);
/*  68 */     while (start >= 0) {
/*  69 */       int end = start + word.length();
/*  70 */       if ((start > 0) && (this.punc.indexOf(content.charAt(start - 1)) < 0)) {
/*  71 */         start = content.indexOf(word, end);
/*     */       }
/*     */       else {
/*  74 */         if ((end < content.length()) && (this.punc.indexOf(content.charAt(end)) < 0)) {
/*  75 */           end = content.indexOf(' ', end);
/*     */           String surface;
/*  76 */           if (end < 0)
/*  77 */             surface = content.substring(start);
/*     */           else
/*  79 */             surface = content.substring(start, end);
/*  80 */           if ((this.lemmatiser == null) || (!this.lemmatiser.lemmatize(surface).equals(word))) {
/*  81 */             start = content.indexOf(word, end);
/*  82 */             continue;
/*     */           }
/*     */         }
/*  85 */         list.add(new Token(word, start, 1));
/*  86 */         start = content.indexOf(word, end);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private SortedArray merge(SortedArray list)
/*     */   {
/*  95 */     if (list.size() < 2)
/*  96 */       return null;
/*  97 */     SortedArray mergedList = new SortedArray(new IndexComparator());
/*  98 */     Token prev = (Token)list.get(0);
/*  99 */     int i = 1;
/* 100 */     while (i < list.size()) {
/* 101 */       Token next = (Token)list.get(i);
/* 102 */       if ((!next.getValue().equals(prev.getValue())) && (Math.abs(next.getIndex() - prev.getIndex()) < this.maxDist)) {
/* 103 */         mergedList.add(prev);
/* 104 */         i++;
/* 105 */         if (i < list.size())
/* 106 */           prev = (Token)list.get(i);
/*     */       }
/*     */       else {
/* 109 */         prev = next;
/*     */       }
/* 111 */       i++;
/*     */     }
/* 113 */     return mergedList;
/*     */   }
/*     */ 
/*     */   private String generateContent(String content, SortedArray regionList)
/*     */   {
/* 121 */     if ((regionList == null) || (regionList.size() == 0)) {
/* 122 */       return content;
/*     */     }
/* 124 */     StringBuffer buf = new StringBuffer();
/* 125 */     int lastStart = -1;
/* 126 */     int lastEnd = -1;
/* 127 */     int i = 0;
/* 128 */     while (i < regionList.size()) {
/* 129 */       Token cur = (Token)regionList.get(i);
/* 130 */       int start = Math.max(0, cur.getIndex() - this.leftExt);
/* 131 */       if (this.punc.indexOf(content.charAt(start)) < 0) {
/* 132 */         start = content.lastIndexOf(' ', start);
/* 133 */         if (start < 0) {
/* 134 */           start = 0;
/*     */         }
/*     */       }
/* 137 */       int end = Math.min(content.length(), cur.getIndex() + this.rightExt);
/* 138 */       if (this.punc.indexOf(content.charAt(end - 1)) < 0) {
/* 139 */         end = content.indexOf(' ', end);
/* 140 */         if (end < 0) {
/* 141 */           end = content.length();
/*     */         }
/*     */       }
/* 144 */       if (start > lastEnd) {
/* 145 */         if (lastEnd != -1) {
/* 146 */           buf.append(' ');
/* 147 */           buf.append(content.substring(lastStart, lastEnd));
/*     */         }
/* 149 */         lastStart = start;
/* 150 */         lastEnd = end;
/*     */       }
/*     */       else {
/* 153 */         lastEnd = end;
/*     */       }
/* 155 */       i++;
/*     */     }
/* 157 */     if ((lastStart >= 0) && (lastEnd > 0)) {
/* 158 */       buf.append(' ');
/* 159 */       buf.append(content.substring(lastStart, lastEnd));
/*     */     }
/* 161 */     return buf.toString();
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.cptc.ContentSelector
 * JD-Core Version:    0.6.2
 */