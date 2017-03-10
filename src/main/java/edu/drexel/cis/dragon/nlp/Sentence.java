/*     */ package edu.drexel.cis.dragon.nlp;
/*     */ 
/*     */ public class Sentence
/*     */ {
/*     */   public Sentence next;
/*     */   public Sentence prev;
/*     */   private Paragraph parent;
/*     */   private Word start;
/*     */   private Word end;
/*     */   private int count;
/*     */   private int index;
/*     */   private char m_punctuation;
/*     */   private Word subjStart;
/*     */   private Word subjEnd;
/*     */ 
/*     */   public Sentence()
/*     */   {
/*  23 */     this.next = null;
/*  24 */     this.prev = null;
/*  25 */     this.start = null;
/*  26 */     this.subjStart = null;
/*  27 */     this.subjEnd = null;
/*  28 */     this.end = null;
/*  29 */     this.count = 0;
/*  30 */     this.parent = null;
/*  31 */     this.index = -1;
/*     */   }
/*     */ 
/*     */   public int getIndex() {
/*  35 */     return this.index;
/*     */   }
/*     */ 
/*     */   public void setIndex(int index) {
/*  39 */     this.index = index;
/*     */   }
/*     */ 
/*     */   public Paragraph getParent() {
/*  43 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public void setParent(Paragraph parent) {
/*  47 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */   public void setPunctuation(char punctuation) {
/*  51 */     this.m_punctuation = punctuation;
/*     */   }
/*     */   public char getPunctuation() {
/*  54 */     return this.m_punctuation;
/*     */   }
/*     */ 
/*     */   public int getWordNum()
/*     */   {
/*  59 */     return this.count;
/*     */   }
/*     */ 
/*     */   public Word getWord(int index)
/*     */   {
/*  67 */     if ((index < 0) || (index >= this.count)) return null;
/*     */ 
/*  69 */     int curPos = 0;
/*  70 */     Word cur = this.start;
/*  71 */     while (curPos < index) {
/*  72 */       cur = cur.next;
/*  73 */       curPos++;
/*     */     }
/*  75 */     return cur;
/*     */   }
/*     */ 
/*     */   public Word getFirstWord() {
/*  79 */     return this.start;
/*     */   }
/*     */ 
/*     */   public void resetBoundary(Word start, Word end) {
/*  83 */     this.start = start;
/*  84 */     start.prev = null;
/*  85 */     this.end = end;
/*  86 */     end.next = null;
/*  87 */     this.count = 0;
/*  88 */     while (start != null) {
/*  89 */       start.setParent(this);
/*  90 */       start.setPosInSentence(this.count);
/*  91 */       this.count += 1;
/*  92 */       start = start.next;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Word getLastWord() {
/*  97 */     return this.end;
/*     */   }
/*     */ 
/*     */   public boolean addWord(Word cur) {
/* 101 */     if (cur == null)
/* 102 */       return false;
/* 103 */     cur.setParent(this);
/* 104 */     if (this.end != null)
/* 105 */       this.end.next = cur;
/* 106 */     if (this.start == null)
/* 107 */       this.start = cur;
/* 108 */     cur.prev = this.end;
/* 109 */     cur.next = null;
/* 110 */     cur.setPosInSentence(this.count);
/* 111 */     this.end = cur;
/* 112 */     this.count += 1;
/* 113 */     return true;
/*     */   }
/*     */ 
/*     */   public String toLinkGrammarString() {
/* 117 */     StringBuffer str = new StringBuffer();
/* 118 */     Word word = getFirstWord();
/* 119 */     while (word != null)
/*     */     {
/* 121 */       str.append(word.getContent());
/* 122 */       str.append(' ');
/* 123 */       word = word.next;
/*     */     }
/*     */ 
/* 126 */     str.setCharAt(str.length() - 1, getPunctuation());
/* 127 */     return str.toString();
/*     */   }
/*     */ 
/*     */   public String toPOSTaggedString() {
/* 131 */     StringBuffer str = new StringBuffer();
/* 132 */     Word word = getFirstWord();
/* 133 */     while (word != null)
/*     */     {
/* 135 */       str.append(word.getContent());
/* 136 */       str.append('/');
/* 137 */       str.append(word.getPOSLabel());
/* 138 */       str.append(' ');
/* 139 */       word = word.next;
/*     */     }
/*     */ 
/* 142 */     str.setCharAt(str.length() - 1, getPunctuation());
/* 143 */     return str.toString();
/*     */   }
/*     */ 
/*     */   public String toBrillTaggerString() {
/* 147 */     StringBuffer str = new StringBuffer();
/* 148 */     Word word = getFirstWord();
/* 149 */     while (word != null) {
/* 150 */       str.append(word.getContent());
/* 151 */       str.append(' ');
/* 152 */       word = word.next;
/*     */     }
/* 154 */     str.append(getPunctuation());
/* 155 */     return str.toString();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 162 */     Word word = getFirstWord();
/* 163 */     if (word == null)
/* 164 */       return null;
/* 165 */     StringBuffer str = new StringBuffer(word.getContent());
/*     */ 
/* 167 */     Word last = word;
/* 168 */     word = word.next;
/* 169 */     while (word != null) {
/* 170 */       if (word.isPunctuation()) {
/* 171 */         str.append(word.getContent());
/* 172 */       } else if (last.isPunctuation()) {
/* 173 */         if ("-_".indexOf(last.getContent()) >= 0) {
/* 174 */           str.append(word.getContent());
/* 175 */         } else if ((".'".lastIndexOf(last.getContent()) >= 0) && (word.getContent().length() <= 2)) {
/* 176 */           str.append(word.getContent());
/*     */         } else {
/* 178 */           str.append(' ');
/* 179 */           str.append(word.getContent());
/*     */         }
/*     */       }
/*     */       else {
/* 183 */         str.append(' ');
/* 184 */         str.append(word.getContent());
/*     */       }
/* 186 */       last = word;
/* 187 */       word = word.next;
/*     */     }
/* 189 */     str.append(getPunctuation());
/* 190 */     return str.toString();
/*     */   }
/*     */ 
/*     */   public Word getFirstSubjectWord() {
/* 194 */     return this.subjStart;
/*     */   }
/*     */ 
/*     */   public Word getLastSubjectWord() {
/* 198 */     return this.subjEnd;
/*     */   }
/*     */ 
/*     */   public void setSubject(Word starting, Word ending) {
/* 202 */     this.subjStart = starting;
/* 203 */     this.subjEnd = ending;
/*     */   }
/*     */ 
/*     */   public Word indexOf(Word word) {
/* 207 */     return indexOf(word.getContent(), 0);
/*     */   }
/*     */ 
/*     */   public Word indexOf(Word word, int start) {
/* 211 */     return indexOf(word.getContent(), start);
/*     */   }
/*     */ 
/*     */   public Word indexOf(String word) {
/* 215 */     return indexOf(word, 0);
/*     */   }
/*     */ 
/*     */   public Word indexOf(String word, int start)
/*     */   {
/* 221 */     Word next = getWord(start);
/* 222 */     while ((next != null) && (!next.getContent().equalsIgnoreCase(word))) {
/* 223 */       next = next.next;
/*     */     }
/* 225 */     if (next != null) {
/* 226 */       return next;
/*     */     }
/* 228 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.Sentence
 * JD-Core Version:    0.6.2
 */