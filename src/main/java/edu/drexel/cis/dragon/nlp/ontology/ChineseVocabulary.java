/*     */ package edu.drexel.cis.dragon.nlp.ontology;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.BufferedReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ 
/*     */ public class ChineseVocabulary
/*     */   implements Vocabulary
/*     */ {
/*     */   private SortedArray list;
/*     */   private String[] arrPhrase;
/*     */ 
/*     */   public ChineseVocabulary(String vobFile)
/*     */   {
/*  22 */     load(vobFile);
/*     */   }
/*     */ 
/*     */   public int getPhraseIndex(long phraseUID)
/*     */   {
/*  28 */     int pos = this.list.binarySearch(new ChinesePhrase(phraseUID, -1));
/*  29 */     if (pos < 0) {
/*  30 */       return -1;
/*     */     }
/*  32 */     return ((ChinesePhrase)this.list.get(pos)).getIndex();
/*     */   }
/*     */ 
/*     */   public boolean isPhrase(String term) {
/*  36 */     return this.list.binarySearch(new ChinesePhrase(getUID(term), -1)) >= 0;
/*     */   }
/*     */ 
/*     */   public boolean isPhrase(Word start, Word end) {
/*  40 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isStartingWord(Word cur) {
/*  44 */     return true;
/*     */   }
/*     */ 
/*     */   public Word findPhrase(Word start) {
/*  48 */     return null;
/*     */   }
/*     */ 
/*     */   public int getPhraseNum()
/*     */   {
/*  53 */     return this.arrPhrase.length;
/*     */   }
/*     */ 
/*     */   public String getPhrase(int index) {
/*  57 */     return this.arrPhrase[index];
/*     */   }
/*     */ 
/*     */   public int maxPhraseLength()
/*     */   {
/*  62 */     return 4;
/*     */   }
/*     */ 
/*     */   public int minPhraseLength() {
/*  66 */     return 2;
/*     */   }
/*     */ 
/*     */   public void setAdjectivePhraseOption(boolean enabled)
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean getAdjectivePhraseOption() {
/*  74 */     return false;
/*     */   }
/*     */ 
/*     */   public void setNPPOption(boolean enabled)
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean getNPPOption() {
/*  82 */     return false;
/*     */   }
/*     */ 
/*     */   public void setCoordinateOption(boolean enabled)
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean getCoordinateOption() {
/*  90 */     return false;
/*     */   }
/*     */ 
/*     */   public void setLemmaOption(boolean enabled)
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean getLemmaOption() {
/*  98 */     return false;
/*     */   }
/*     */ 
/*     */   public long getUID(String phrase)
/*     */   {
/* 105 */     if ((phrase == null) || (phrase.length() > 4) || (phrase.length() == 0))
/* 106 */       return -1L;
/* 107 */     long uid = phrase.charAt(0);
/* 108 */     int i = 1;
/* 109 */     while (i < phrase.length()) {
/* 110 */       uid = (uid << 16) + phrase.charAt(i);
/* 111 */       i++;
/*     */     }
/* 113 */     return uid;
/*     */   }
/*     */ 
/*     */   private void load(String vobFile)
/*     */   {
/*     */     try
/*     */     {
/* 123 */       BufferedReader br = FileUtil.getTextReader(vobFile, "GBK");
/* 124 */       String line = br.readLine();
/* 125 */       String[] arrField = line.split("\t");
/* 126 */       int total = Integer.parseInt(arrField[0]);
/* 127 */       this.arrPhrase = new String[total];
/* 128 */       ArrayList phraseList = new ArrayList(total);
/*     */ 
/* 130 */       for (int i = 0; i < total; i++) {
/* 131 */         line = br.readLine();
/* 132 */         arrField = line.split("\t");
/* 133 */         this.arrPhrase[i] = arrField[0];
/* 134 */         if (this.arrPhrase[i].length() <= 4)
/*     */         {
/* 136 */           phraseList.add(new ChinesePhrase(getUID(this.arrPhrase[i]), i));
/*     */         }
/*     */       }
/* 138 */       Collections.sort(phraseList);
/* 139 */       this.list = new SortedArray(total);
/* 140 */       this.list.addAll(phraseList);
/* 141 */       phraseList.clear();
/*     */     }
/*     */     catch (Exception e) {
/* 144 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ChinesePhrase implements Comparable {
/*     */     private long id;
/*     */     private int index;
/*     */ 
/* 153 */     public ChinesePhrase(long id, int index) { this.id = id;
/* 154 */       this.index = index;
/*     */     }
/*     */ 
/*     */     public int compareTo(Object obj)
/*     */     {
/* 160 */       long objId = ((ChinesePhrase)obj).getID();
/* 161 */       if (this.id > objId)
/* 162 */         return 1;
/* 163 */       if (this.id < objId) {
/* 164 */         return -1;
/*     */       }
/* 166 */       return 0;
/*     */     }
/*     */ 
/*     */     public long getID() {
/* 170 */       return this.id;
/*     */     }
/*     */ 
/*     */     public int getIndex() {
/* 174 */       return this.index;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.ontology.ChineseVocabulary
 * JD-Core Version:    0.6.2
 */