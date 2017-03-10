/*     */ package edu.drexel.cis.dragon.nlp;
/*     */ 
/*     */ public class Word
/*     */ {
/*     */   public static final int SUBJ = 1;
/*     */   public static final int PREDICATE = 2;
/*     */   public static final int OBJ = 3;
/*     */   public static final int TYPE_WORD = 1;
/*     */   public static final int TYPE_NUMBER = 2;
/*     */   public static final int TYPE_PUNC = 4;
/*     */   public Word next;
/*     */   public Word prev;
/*     */   private Sentence parent;
/*     */   private String content;
/*     */   private String lemma;
/*     */   private boolean isBaseForm;
/*     */   private boolean ignored;
/*     */   private String posLabel;
/*     */   private int freq;
/*     */   private int index;
/*     */   private int offset;
/*     */   private byte posIndex;
/*     */   private byte types;
/*     */   private byte posInSentence;
/*     */   private byte roleInClause;
/*     */   private byte parallelGroup;
/*     */   private byte clauseID;
/*     */   private Concept associatedConcept;
/*     */ 
/*     */   public Word(String content)
/*     */   {
/*  42 */     this.prev = null;
/*  43 */     this.next = null;
/*  44 */     this.parent = null;
/*  45 */     this.freq = 1;
/*  46 */     this.offset = -1;
/*  47 */     this.posInSentence = -1;
/*  48 */     this.roleInClause = -1;
/*  49 */     this.associatedConcept = null;
/*  50 */     this.posLabel = null;
/*  51 */     this.posIndex = -1;
/*  52 */     this.content = content;
/*  53 */     this.lemma = null;
/*  54 */     this.ignored = false;
/*  55 */     this.isBaseForm = false;
/*  56 */     this.types = 1;
/*  57 */     this.parallelGroup = -1;
/*  58 */     this.parent = null;
/*  59 */     this.clauseID = -1;
/*  60 */     this.index = -2147483648;
/*     */   }
/*     */ 
/*     */   public Word copy()
/*     */   {
/*  67 */     Word newWord = new Word(this.content);
/*  68 */     newWord.setLemma(getLemma());
/*  69 */     newWord.setPOS(getPOSLabel(), getPOSIndex());
/*  70 */     newWord.setParallelGroup(getParallelGroup());
/*  71 */     newWord.setRoleInClause(getRoleInClause());
/*  72 */     newWord.setIndex(getIndex());
/*  73 */     newWord.setAssociatedConcept(getAssociatedConcept());
/*  74 */     return newWord;
/*     */   }
/*     */ 
/*     */   public String getName() {
/*  78 */     if (getLemma() != null) {
/*  79 */       return getLemma();
/*     */     }
/*  81 */     return this.content;
/*     */   }
/*     */ 
/*     */   public String getEntryID() {
/*  85 */     return null;
/*     */   }
/*     */ 
/*     */   public String getSemanticType() {
/*  89 */     return null;
/*     */   }
/*     */ 
/*     */   public void setIndex(int index) {
/*  93 */     this.index = index;
/*     */   }
/*     */ 
/*     */   public int getIndex() {
/*  97 */     return this.index;
/*     */   }
/*     */ 
/*     */   public int getOffset() {
/* 101 */     return this.offset;
/*     */   }
/*     */ 
/*     */   public void setOffset(int offset) {
/* 105 */     this.offset = offset;
/*     */   }
/*     */ 
/*     */   public void setLemma(String lemma) {
/* 109 */     if (lemma == null)
/*     */     {
/* 111 */       this.lemma = null;
/* 112 */       this.isBaseForm = false;
/* 113 */       return;
/*     */     }
/*     */ 
/* 116 */     lemma = lemma.toLowerCase();
/* 117 */     if (lemma.compareTo(this.content) == 0)
/*     */     {
/* 119 */       lemma = null;
/* 120 */       this.isBaseForm = true;
/*     */     }
/*     */     else
/*     */     {
/* 124 */       this.isBaseForm = false;
/* 125 */       this.lemma = lemma;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getLemma() {
/* 130 */     if (this.isBaseForm) {
/* 131 */       return this.content;
/*     */     }
/* 133 */     return this.lemma;
/*     */   }
/*     */ 
/*     */   public Sentence getParent() {
/* 137 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public void setParent(Sentence parent) {
/* 141 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */   public int getType() {
/* 145 */     return this.types;
/*     */   }
/*     */ 
/*     */   public void setType(int wordType) {
/* 149 */     if ((wordType == 2) || (wordType == 4)) {
/* 150 */       this.types = ((byte)wordType);
/* 151 */       this.isBaseForm = true;
/*     */     }
/*     */     else {
/* 154 */       this.types = 1;
/*     */     }
/*     */   }
/*     */ 
/* 158 */   public int getPosInSentence() { return this.posInSentence; }
/*     */ 
/*     */   public void setPosInSentence(int offset)
/*     */   {
/* 162 */     this.posInSentence = ((byte)offset);
/*     */   }
/*     */ 
/*     */   public boolean isNumber() {
/* 166 */     return this.types == 2;
/*     */   }
/*     */ 
/*     */   public boolean isWord() {
/* 170 */     return this.types == 1;
/*     */   }
/*     */ 
/*     */   public boolean isAllCapital() {
/* 174 */     if ((Character.isUpperCase(this.content.charAt(0))) && (Character.isUpperCase(this.content.charAt(this.content.length() - 1)))) {
/* 175 */       return true;
/*     */     }
/* 177 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isInitialCapital() {
/* 181 */     return Character.isUpperCase(this.content.charAt(0));
/*     */   }
/*     */ 
/*     */   public int getParallelGroup() {
/* 185 */     return this.parallelGroup;
/*     */   }
/*     */ 
/*     */   public void setParallelGroup(int groupNo) {
/* 189 */     this.parallelGroup = ((byte)groupNo);
/*     */   }
/*     */ 
/*     */   public int getClauseID() {
/* 193 */     return this.clauseID;
/*     */   }
/*     */ 
/*     */   public void setClauseID(int clauseID) {
/* 197 */     this.clauseID = ((byte)clauseID);
/*     */   }
/*     */ 
/*     */   public boolean isPunctuation()
/*     */   {
/* 202 */     return this.types == 4;
/*     */   }
/*     */ 
/*     */   public String getContent() {
/* 206 */     return this.content;
/*     */   }
/*     */ 
/*     */   public void setContent(String content) {
/* 210 */     this.content = content;
/*     */   }
/*     */ 
/*     */   public void setPOS(String posLabel, int posIndex) {
/* 214 */     this.posLabel = posLabel;
/* 215 */     this.posIndex = ((byte)posIndex);
/* 216 */     if ((posIndex <= 0) || (posIndex == 8) || (posIndex == 7) || (posIndex == 5))
/* 217 */       this.isBaseForm = true;
/*     */   }
/*     */ 
/*     */   public void setIgnore(boolean ignored)
/*     */   {
/* 222 */     this.ignored = ignored;
/*     */   }
/*     */ 
/*     */   public boolean canIgnore() {
/* 226 */     return this.ignored;
/*     */   }
/*     */ 
/*     */   public String getPOSLabel() {
/* 230 */     return this.posLabel;
/*     */   }
/*     */ 
/*     */   public int getPOSIndex() {
/* 234 */     return this.posIndex;
/*     */   }
/*     */ 
/*     */   public int getFrequency() {
/* 238 */     return this.freq;
/*     */   }
/*     */ 
/*     */   public void addFrequency(int inc) {
/* 242 */     this.freq += inc;
/*     */   }
/*     */ 
/*     */   public void setFrequency(int freq) {
/* 246 */     this.freq = freq;
/*     */   }
/*     */ 
/*     */   public void setAssociatedConcept(Concept concept) {
/* 250 */     this.associatedConcept = concept;
/*     */   }
/*     */ 
/*     */   public Concept getAssociatedConcept() {
/* 254 */     return this.associatedConcept;
/*     */   }
/*     */ 
/*     */   public void setRoleInClause(int role) {
/* 258 */     this.roleInClause = ((byte)role);
/*     */   }
/*     */ 
/*     */   public int getRoleInClause() {
/* 262 */     return this.roleInClause;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 266 */     if (this.index >= 0) {
/* 267 */       return this.index;
/*     */     }
/* 269 */     return super.hashCode();
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.Word
 * JD-Core Version:    0.6.2
 */