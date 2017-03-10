/*     */ package edu.drexel.cis.dragon.nlp.ontology;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import java.io.BufferedReader;
/*     */ 
/*     */ public abstract class AbstractVocabulary
/*     */   implements Vocabulary
/*     */ {
/*     */   protected Lemmatiser lemmatiser;
/*     */   protected boolean enable_npp_option;
/*     */   protected boolean enable_coordinate_option;
/*     */   protected boolean enable_adjterm_option;
/*     */   protected boolean enable_lemma_option;
/*     */   protected String nonboundaryPunctuations;
/*     */   protected SimpleElementList list;
/*     */   protected int maxPhraseLength;
/*     */   protected int minPhraseLength;
/*     */ 
/*     */   public AbstractVocabulary(String termFilename)
/*     */   {
/*  28 */     this.lemmatiser = null;
/*  29 */     this.enable_npp_option = false;
/*  30 */     this.enable_coordinate_option = false;
/*  31 */     this.enable_adjterm_option = false;
/*  32 */     this.enable_lemma_option = false;
/*  33 */     this.nonboundaryPunctuations = "-";
/*  34 */     this.list = new SimpleElementList(termFilename, false);
/*  35 */     readVocabularyMeta(termFilename);
/*     */   }
/*     */ 
/*     */   public AbstractVocabulary(String termFilename, Lemmatiser lemmatiser) {
/*  39 */     this.lemmatiser = lemmatiser;
/*  40 */     this.enable_npp_option = false;
/*  41 */     this.enable_coordinate_option = false;
/*  42 */     this.enable_adjterm_option = false;
/*  43 */     this.enable_lemma_option = false;
/*  44 */     this.nonboundaryPunctuations = "-";
/*  45 */     this.list = new SimpleElementList(termFilename, false);
/*  46 */     readVocabularyMeta(termFilename);
/*     */   }
/*     */ 
/*     */   public int getPhraseNum() {
/*  50 */     return this.list.size();
/*     */   }
/*     */ 
/*     */   public String getPhrase(int index) {
/*  54 */     return this.list.search(index);
/*     */   }
/*     */ 
/*     */   public int maxPhraseLength() {
/*  58 */     return this.maxPhraseLength;
/*     */   }
/*     */ 
/*     */   public int minPhraseLength() {
/*  62 */     return this.minPhraseLength;
/*     */   }
/*     */ 
/*     */   public void setNonBoundaryPunctuation(String punctuations) {
/*  66 */     this.nonboundaryPunctuations = punctuations;
/*     */   }
/*     */ 
/*     */   public String getNonBoundaryPunctuation() {
/*  70 */     return this.nonboundaryPunctuations;
/*     */   }
/*     */ 
/*     */   public void setLemmaOption(boolean enabled) {
/*  74 */     this.enable_lemma_option = enabled;
/*     */   }
/*     */ 
/*     */   public boolean getLemmaOption() {
/*  78 */     return this.enable_lemma_option;
/*     */   }
/*     */ 
/*     */   public void setAdjectivePhraseOption(boolean enabled) {
/*  82 */     this.enable_adjterm_option = enabled;
/*     */   }
/*     */ 
/*     */   public boolean getAdjectivePhraseOption() {
/*  86 */     return this.enable_adjterm_option;
/*     */   }
/*     */ 
/*     */   public void setNPPOption(boolean enabled) {
/*  90 */     this.enable_npp_option = enabled;
/*     */   }
/*     */ 
/*     */   public boolean getNPPOption() {
/*  94 */     return this.enable_npp_option;
/*     */   }
/*     */ 
/*     */   public void setCoordinateOption(boolean enabled) {
/*  98 */     this.enable_coordinate_option = enabled;
/*     */   }
/*     */ 
/*     */   public boolean getCoordinateOption() {
/* 102 */     return this.enable_coordinate_option;
/*     */   }
/*     */ 
/*     */   public boolean isStartingWord(Word cur)
/*     */   {
/* 108 */     int posIndex = cur.getPOSIndex();
/* 109 */     if ((posIndex == 1) || (posIndex == 3)) {
/* 110 */       return true;
/*     */     }
/*     */ 
/* 113 */     if ((posIndex == 9) && (cur.next != null) && (cur.next.getContent().equals("-"))) {
/* 114 */       return true;
/*     */     }
/* 116 */     return false;
/*     */   }
/*     */ 
/*     */   protected boolean isBoundaryWord(Word curWord)
/*     */   {
/* 122 */     if ((curWord.isPunctuation()) && (this.nonboundaryPunctuations.indexOf(curWord.getContent()) < 0)) {
/* 123 */       return true;
/*     */     }
/* 125 */     if ((curWord.prev != null) && (curWord.prev.getType() == 4)) {
/* 126 */       return false;
/*     */     }
/* 128 */     int posIndex = curWord.getPOSIndex();
/* 129 */     if (posIndex == 2) {
/* 130 */       return true;
/*     */     }
/* 132 */     if (posIndex == 5) {
/* 133 */       if (!this.enable_npp_option)
/* 134 */         return true;
/* 135 */       if ("of".indexOf(curWord.getContent()) < 0) {
/* 136 */         return true;
/*     */       }
/*     */     }
/* 139 */     if (posIndex == 8) {
/* 140 */       if (!this.enable_coordinate_option)
/* 141 */         return true;
/* 142 */       if ("and or".indexOf(curWord.getContent()) < 0) {
/* 143 */         return true;
/*     */       }
/*     */     }
/* 146 */     return false;
/*     */   }
/*     */ 
/*     */   protected String getLemma(Word word)
/*     */   {
/* 152 */     String lemma = word.getLemma();
/* 153 */     if (lemma == null) {
/* 154 */       if (word.getPOSIndex() >= 0)
/* 155 */         lemma = this.lemmatiser.lemmatize(word.getContent(), word.getPOSIndex());
/*     */       else
/* 157 */         lemma = this.lemmatiser.lemmatize(word.getContent());
/* 158 */       word.setLemma(lemma);
/*     */     }
/* 160 */     return lemma;
/*     */   }
/*     */ 
/*     */   protected String buildString(Word start, Word end, boolean useLemma)
/*     */   {
/* 167 */     Word next = start;
/*     */     String term;
/* 168 */     if (useLemma) {
/* 169 */         term = next.getLemma();
/* 170 */       while (!next.equals(end)) {
/* 171 */         next = next.next;
/* 172 */         if (isUsefulForPhrase(next))
/* 173 */           term = term + " " + next.getLemma();
/*     */       }
/*     */     }
/*     */     else {
/* 177 */       term = next.getContent().toLowerCase();
/* 178 */       while (!next.equals(end)) {
/* 179 */         next = next.next;
/* 180 */         if (isUsefulForPhrase(next))
/* 181 */           term = term + " " + next.getContent().toLowerCase();
/*     */       }
/*     */     }
/* 184 */     return term;
/*     */   }
/*     */ 
/*     */   protected boolean isUsefulForPhrase(Word word)
/*     */   {
/* 190 */     int posIndex = word.getPOSIndex();
/* 191 */     if ((posIndex == 3) || (posIndex == 1) || (posIndex == 9))
/*     */     {
/* 193 */       word.setIgnore(false);
/* 194 */       return true;
/*     */     }
/*     */ 
/* 197 */     word.setIgnore(true);
/* 198 */     return false;
/*     */   }
/*     */ 
/*     */   protected void readVocabularyMeta(String termFilename)
/*     */   {
/*     */     try
/*     */     {
/* 207 */       BufferedReader br = FileUtil.getTextReader(termFilename);
/* 208 */       String[] arrField = br.readLine().split("\t");
/* 209 */       this.minPhraseLength = Integer.parseInt(arrField[1]);
/* 210 */       this.maxPhraseLength = Integer.parseInt(arrField[2]);
/* 211 */       br.close();
/*     */     }
/*     */     catch (Exception e) {
/* 214 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.ontology.AbstractVocabulary
 * JD-Core Version:    0.6.2
 */