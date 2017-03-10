/*     */ package edu.drexel.cis.dragon.nlp.ontology;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*     */ 
/*     */ public abstract class AbstractOntology
/*     */   implements Ontology
/*     */ {
/*     */   private Lemmatiser lemmatiser;
/*     */   private boolean enable_tsd_option;
/*     */   private boolean enable_npp_option;
/*     */   private boolean enable_coordinate_option;
/*     */   private boolean enable_adjterm_option;
/*     */   private boolean enable_lemma_option;
/*     */   private String nonboundaryPunctuations;
/*     */ 
/*     */   public AbstractOntology(Lemmatiser lemmatiser)
/*     */   {
/*  24 */     this.lemmatiser = lemmatiser;
/*  25 */     this.enable_tsd_option = false;
/*  26 */     this.enable_npp_option = false;
/*  27 */     this.enable_coordinate_option = false;
/*  28 */     this.enable_adjterm_option = false;
/*  29 */     this.enable_lemma_option = true;
/*  30 */     this.nonboundaryPunctuations = ".-'";
/*     */   }
/*     */ 
/*     */   public SemanticNet getSemanticNet() {
/*  34 */     return null;
/*     */   }
/*     */ 
/*     */   public SimilarityMetric getSimilarityMetric() {
/*  38 */     return null;
/*     */   }
/*     */ 
/*     */   public void setNonBoundaryPunctuation(String punctuations) {
/*  42 */     this.nonboundaryPunctuations = punctuations;
/*     */   }
/*     */ 
/*     */   public String getNonBoundaryPunctuation() {
/*  46 */     return this.nonboundaryPunctuations;
/*     */   }
/*     */ 
/*     */   public void setLemmaOption(boolean enabled) {
/*  50 */     this.enable_lemma_option = enabled;
/*     */   }
/*     */ 
/*     */   public boolean getLemmaOption() {
/*  54 */     return this.enable_lemma_option;
/*     */   }
/*     */ 
/*     */   public void setSenseDisambiguationOption(boolean enabled) {
/*  58 */     this.enable_tsd_option = enabled;
/*     */   }
/*     */ 
/*     */   public boolean getSenseDisambiguationOption() {
/*  62 */     return this.enable_tsd_option;
/*     */   }
/*     */ 
/*     */   public void setAdjectiveTermOption(boolean enabled) {
/*  66 */     this.enable_adjterm_option = enabled;
/*     */   }
/*     */ 
/*     */   public boolean getAdjectiveTermOption() {
/*  70 */     return this.enable_adjterm_option;
/*     */   }
/*     */ 
/*     */   public void setNPPOption(boolean enabled) {
/*  74 */     this.enable_npp_option = enabled;
/*     */   }
/*     */ 
/*     */   public boolean getNPPOption() {
/*  78 */     return this.enable_npp_option;
/*     */   }
/*     */ 
/*     */   public void setCoordinateOption(boolean enabled) {
/*  82 */     this.enable_coordinate_option = enabled;
/*     */   }
/*     */ 
/*     */   public boolean getCoordinateOption() {
/*  86 */     return this.enable_coordinate_option;
/*     */   }
/*     */ 
/*     */   public boolean isStartingWord(Word cur)
/*     */   {
/*  92 */     int posIndex = cur.getPOSIndex();
/*  93 */     if ((posIndex == 1) || (posIndex == 3)) {
/*  94 */       return true;
/*     */     }
/*     */ 
/*  97 */     if ((posIndex == 9) && (cur.next != null) && (cur.next.getContent().equals("-"))) {
/*  98 */       return true;
/*     */     }
/* 100 */     return false;
/*     */   }
/*     */ 
/*     */   protected boolean isBoundaryWord(Word curWord)
/*     */   {
/* 106 */     if ((curWord.isPunctuation()) && (this.nonboundaryPunctuations.indexOf(curWord.getContent()) < 0)) {
/* 107 */       return true;
/*     */     }
/* 109 */     if ((curWord.prev != null) && (curWord.prev.getType() == 4)) {
/* 110 */       return false;
/*     */     }
/* 112 */     int posIndex = curWord.getPOSIndex();
/* 113 */     if (posIndex == 2) {
/* 114 */       return true;
/*     */     }
/* 116 */     if (posIndex == 5) {
/* 117 */       if (!this.enable_npp_option)
/* 118 */         return true;
/* 119 */       if ("in for of".indexOf(curWord.getContent()) < 0) {
/* 120 */         return true;
/*     */       }
/*     */     }
/* 123 */     if (posIndex == 8) {
/* 124 */       if (!this.enable_coordinate_option)
/* 125 */         return true;
/* 126 */       if ("and or".indexOf(curWord.getContent()) < 0) {
/* 127 */         return true;
/*     */       }
/*     */     }
/* 130 */     return false;
/*     */   }
/*     */ 
/*     */   protected String getLemma(Word word)
/*     */   {
/* 136 */     String lemma = word.getLemma();
/* 137 */     if (lemma == null) {
/* 138 */       if (word.getPOSIndex() >= 0)
/* 139 */         lemma = this.lemmatiser.lemmatize(word.getContent(), word.getPOSIndex());
/*     */       else
/* 141 */         lemma = this.lemmatiser.lemmatize(word.getContent());
/* 142 */       word.setLemma(lemma);
/*     */     }
/* 144 */     return lemma;
/*     */   }
/*     */ 
/*     */   protected String buildString(Word start, Word end, boolean useLemma)
/*     */   {
/* 151 */     Word next = start;
/*     */     String term;
/* 152 */     if (useLemma) {
/* 153 */        term = getLemma(next);
/* 154 */       while (!next.equals(end)) {
/* 155 */         next = next.next;
/* 156 */         if (isUsefulForTerm(next))
/* 157 */           term = term + " " + getLemma(next);
/*     */       }
/*     */     }
/*     */     else {
/* 161 */       term = next.getContent().toLowerCase();
/* 162 */       while (!next.equals(end)) {
/* 163 */         next = next.next;
/* 164 */         if (isUsefulForTerm(next))
/* 165 */           term = term + " " + next.getContent().toLowerCase();
/*     */       }
/*     */     }
/* 168 */     return term;
/*     */   }
/*     */ 
/*     */   protected boolean isUsefulForTerm(Word word)
/*     */   {
/* 174 */     if ((word.prev != null) && (word.prev.isPunctuation())) {
/* 175 */       if (word.prev.getContent().charAt(0) == '\'')
/*     */       {
/* 177 */         word.setIgnore(true);
/* 178 */         return false;
/*     */       }
/*     */ 
/* 181 */       if (word.isPunctuation()) {
/* 182 */         word.setIgnore(true);
/* 183 */         return false;
/*     */       }
/*     */ 
/* 186 */       word.setIgnore(false);
/* 187 */       return true;
/*     */     }
/*     */ 
/* 192 */     int posIndex = word.getPOSIndex();
/* 193 */     if ((posIndex == 3) || (posIndex == 1) || (posIndex == 9))
/*     */     {
/* 195 */       word.setIgnore(false);
/* 196 */       return true;
/*     */     }
/*     */ 
/* 199 */     word.setIgnore(true);
/* 200 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.ontology.AbstractOntology
 * JD-Core Version:    0.6.2
 */