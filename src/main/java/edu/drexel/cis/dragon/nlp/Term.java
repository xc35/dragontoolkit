/*     */ package edu.drexel.cis.dragon.nlp;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.compare.SortedElement;
/*     */ import edu.drexel.cis.dragon.nlp.compare.TermLemmaComparator;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ 
/*     */ public class Term
/*     */   implements Concept, Comparable, SortedElement
/*     */ {
/*     */   public static final int NAME_ASIS = 0;
/*     */   public static final int NAME_LEMMA = 1;
/*     */   public static final int NAME_NORM = 2;
/*  22 */   private static int nameMode = 2;
/*     */   private Word starting;
/*     */   private Word ending;
/*     */   private Term referral;
/*     */   private Object memo;
/*     */   private SortedArray attributes;
/*     */   private String normalizedStr;
/*     */   private String[] can_cui;
/*     */   private String cui;
/*     */   private String[] can_tui;
/*     */   private String tui;
/*     */   private int freq;
/*     */   private double weight;
/*     */   private int len;
/*     */   private int index;
/*     */   private boolean predicted;
/*     */   private boolean expired;
/*     */   private boolean subterm;
/*     */ 
/*     */   public Term(Word starting, Word ending)
/*     */   {
/*  42 */     this.starting = starting;
/*  43 */     this.ending = ending;
/*  44 */     this.attributes = null;
/*  45 */     this.tui = null;
/*  46 */     this.cui = null;
/*  47 */     this.weight = 0.0D;
/*  48 */     this.predicted = false;
/*  49 */     this.freq = 1;
/*  50 */     this.index = 0;
/*  51 */     this.len = 1;
/*  52 */     this.memo = null;
/*  53 */     this.expired = false;
/*  54 */     this.normalizedStr = null;
/*  55 */     this.referral = null;
/*  56 */     this.subterm = false;
/*  57 */     Word next = starting;
/*  58 */     while (!next.equals(ending)) {
/*  59 */       this.len += 1;
/*  60 */       next = next.next;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Term(Word word) {
/*  65 */     this.starting = word;
/*  66 */     this.ending = word;
/*  67 */     this.attributes = null;
/*  68 */     this.tui = null;
/*  69 */     this.cui = null;
/*  70 */     this.predicted = false;
/*  71 */     this.freq = 1;
/*  72 */     this.index = 0;
/*  73 */     this.memo = null;
/*  74 */     this.expired = false;
/*  75 */     this.weight = 0.0D;
/*  76 */     this.normalizedStr = null;
/*  77 */     this.referral = null;
/*  78 */     this.len = 1;
/*     */   }
/*     */ 
/*     */   public Concept copy()
/*     */   {
/*  84 */     Term newTerm = new Term(getStartingWord(), getEndingWord());
/*  85 */     newTerm.setReferral(getReferral());
/*  86 */     newTerm.setFrequency(getFrequency());
/*  87 */     newTerm.setCUI(getCUI());
/*  88 */     newTerm.setCandidateCUI(getCandidateCUI());
/*  89 */     newTerm.setTUI(getTUI());
/*  90 */     newTerm.setCandidateTUI(getCandidateTUI());
/*  91 */     newTerm.setSubConcept(isSubConcept());
/*  92 */     return newTerm;
/*     */   }
/*     */ 
/*     */   public int getConceptType() {
/*  96 */     return 1;
/*     */   }
/*     */ 
/*     */   public static void setNameMode(int mode) {
/* 100 */     if ((mode < 0) || (mode > 2))
/* 101 */       return;
/* 102 */     nameMode = mode;
/*     */   }
/*     */ 
/*     */   public int compareTo(Object obj)
/*     */   {
/* 108 */     String objValue = ((Term)obj).toString();
/* 109 */     return toString().compareToIgnoreCase(objValue);
/*     */   }
/*     */ 
/*     */   public int compareTo(Term term) {
/* 113 */     return toString().compareToIgnoreCase(term.toString());
/*     */   }
/*     */ 
/*     */   public boolean isSubConcept() {
/* 117 */     return this.subterm;
/*     */   }
/*     */ 
/*     */   public void setSubConcept(boolean subterm) {
/* 121 */     this.subterm = subterm;
/*     */   }
/*     */ 
/*     */   public String getName() {
/* 125 */     if (nameMode == 0)
/* 126 */       return toString();
/* 127 */     if (nameMode == 1) {
/* 128 */       return toLemmaString();
/*     */     }
/* 130 */     return toNormalizedString();
/*     */   }
/*     */ 
/*     */   public String getEntryID() {
/* 134 */     if (this.cui != null)
/* 135 */       return this.cui;
/* 136 */     if (this.can_cui != null) {
/* 137 */       return this.can_cui[0];
/*     */     }
/* 139 */     return null;
/*     */   }
/*     */ 
/*     */   public String getSemanticType() {
/* 143 */     if (this.tui != null)
/* 144 */       return this.tui;
/* 145 */     if (this.can_tui != null) {
/* 146 */       return this.can_tui[0];
/*     */     }
/* 148 */     return null;
/*     */   }
/*     */ 
/*     */   public int getIndex() {
/* 152 */     return this.index;
/*     */   }
/*     */ 
/*     */   public void setIndex(int index) {
/* 156 */     this.index = index;
/*     */   }
/*     */ 
/*     */   public boolean isPredicted() {
/* 160 */     return this.predicted;
/*     */   }
/*     */ 
/*     */   public void setPredictedTerm(boolean predicted) {
/* 164 */     this.predicted = predicted;
/*     */   }
/*     */ 
/*     */   public boolean isExpired() {
/* 168 */     return this.expired;
/*     */   }
/*     */ 
/*     */   public void setExpired(boolean expired) {
/* 172 */     this.expired = expired;
/*     */   }
/*     */ 
/*     */   public void setWeight(double weight) {
/* 176 */     this.weight = weight;
/*     */   }
/*     */ 
/*     */   public double getWeight() {
/* 180 */     return this.weight;
/*     */   }
/*     */ 
/*     */   public String[] getCandidateCUI() {
/* 184 */     return this.can_cui;
/*     */   }
/*     */ 
/*     */   public String getCandidateCUI(int index) {
/* 188 */     return this.can_cui[index];
/*     */   }
/*     */ 
/*     */   public int getCandidateCUINum() {
/* 192 */     if (this.can_cui == null) {
/* 193 */       return 0;
/*     */     }
/*     */ 
/* 196 */     return this.can_cui.length;
/*     */   }
/*     */ 
/*     */   public void setCandidateCUI(String[] can_cui)
/*     */   {
/* 201 */     this.can_cui = can_cui;
/*     */   }
/*     */ 
/*     */   public String getCUI() {
/* 205 */     return this.cui;
/*     */   }
/*     */ 
/*     */   public void setCUI(String cui) {
/* 209 */     this.cui = cui;
/*     */   }
/*     */ 
/*     */   public int getSenseNum() {
/* 213 */     if (this.can_cui == null) {
/* 214 */       return 0;
/*     */     }
/*     */ 
/* 217 */     return this.can_cui.length;
/*     */   }
/*     */ 
/*     */   public String[] getCandidateTUI()
/*     */   {
/* 222 */     return this.can_tui;
/*     */   }
/*     */ 
/*     */   public String getCandidateTUI(int index) {
/* 226 */     return this.can_tui[index];
/*     */   }
/*     */ 
/*     */   public int getCandidateTUINum() {
/* 230 */     if (this.can_tui == null) {
/* 231 */       return 0;
/*     */     }
/*     */ 
/* 234 */     return this.can_tui.length;
/*     */   }
/*     */ 
/*     */   public void setCandidateTUI(String[] can_tui)
/*     */   {
/* 239 */     this.can_tui = can_tui;
/*     */   }
/*     */ 
/*     */   public String getTUI() {
/* 243 */     return this.tui;
/*     */   }
/*     */ 
/*     */   public void setTUI(String tui) {
/* 247 */     this.tui = tui;
/*     */   }
/*     */ 
/*     */   public Word getStartingWord() {
/* 251 */     return this.starting;
/*     */   }
/*     */ 
/*     */   public Word getEndingWord() {
/* 255 */     return this.ending;
/*     */   }
/*     */ 
/*     */   public Object getMemo() {
/* 259 */     return this.memo;
/*     */   }
/*     */ 
/*     */   public void setMemo(Object memo) {
/* 263 */     this.memo = memo;
/*     */   }
/*     */ 
/*     */   public Term getReferral() {
/* 267 */     return this.referral;
/*     */   }
/*     */ 
/*     */   public void setReferral(Term referral) {
/* 271 */     this.referral = referral;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 279 */     StringBuffer term = new StringBuffer(this.starting.getContent());
/* 280 */     Word next = this.starting;
/* 281 */     boolean lastPunctuation = false;
/* 282 */     while (!next.equals(this.ending)) {
/* 283 */       next = next.next;
/* 284 */       if (next.isPunctuation())
/*     */       {
/* 286 */         term.append(next.getContent());
/* 287 */         lastPunctuation = true;
/*     */       }
/*     */       else
/*     */       {
/* 291 */         if (lastPunctuation) {
/* 292 */           term.append(next.getContent());
/*     */         }
/*     */         else {
/* 295 */           term.append(' ');
/* 296 */           term.append(next.getContent());
/*     */         }
/* 298 */         lastPunctuation = false;
/*     */       }
/*     */     }
/* 301 */     return term.toString();
/*     */   }
/*     */ 
/*     */   public String toLemmaString()
/*     */   {
/* 308 */     if (this.referral != null)
/* 309 */       return this.referral.toLemmaString();
/* 310 */     Word next = this.starting;
/* 311 */     String term = next.getLemma();
/* 312 */     while (!next.equals(this.ending)) {
/* 313 */       next = next.next;
/* 314 */       term = term + " " + next.getLemma();
/*     */     }
/* 316 */     return term;
/*     */   }
/*     */ 
/*     */   public void setNormalizedString(String normalizedStr) {
/* 320 */     this.normalizedStr = normalizedStr;
/*     */   }
/*     */ 
/*     */   public String toNormalizedString()
/*     */   {
/* 329 */     if (this.referral != null)
/* 330 */       return this.referral.toNormalizedString();
/* 331 */     if (this.normalizedStr != null) {
/* 332 */       return this.normalizedStr;
/*     */     }
/* 334 */     if (this.starting.equals(this.ending)) {
/* 335 */       return this.starting.getLemma();
/*     */     }
/*     */ 
/* 338 */     ArrayList list = new ArrayList(4);
/* 339 */     Word next = this.starting;
/* 340 */     while (next != null) {
/* 341 */       if (!next.canIgnore()) {
/* 342 */         if (next.getLemma() != null)
/* 343 */           list.add(next.getLemma());
/*     */         else
/* 345 */           list.add(next.getContent().toLowerCase());
/*     */       }
/* 347 */       if (next.equals(this.ending)) break;
/* 348 */       next = next.next;
/*     */     }
/*     */ 
/* 354 */     Collections.sort(list);
/*     */ 
/* 356 */     StringBuffer term = new StringBuffer((String)list.get(0));
/* 357 */     for (int i = 1; i < list.size(); i++) {
/* 358 */       term.append(' ');
/* 359 */       term.append((String)list.get(i));
/*     */     }
/* 361 */     this.normalizedStr = term.toString();
/* 362 */     return this.normalizedStr;
/*     */   }
/*     */ 
/*     */   public int getWordNum() {
/* 366 */     return this.len;
/*     */   }
/*     */ 
/*     */   public void addFrequency(int inc) {
/* 370 */     this.freq += inc;
/*     */   }
/*     */ 
/*     */   public int getFrequency() {
/* 374 */     return this.freq;
/*     */   }
/*     */ 
/*     */   public void setFrequency(int freq) {
/* 378 */     this.freq = freq;
/*     */   }
/*     */ 
/*     */   public boolean equalTo(Concept concept)
/*     */   {
/* 384 */     String objFirst = getEntryID();
/* 385 */     String objSecond = concept.getEntryID();
/* 386 */     if ((objFirst != null) && (objSecond != null)) {
/* 387 */       return objFirst.equalsIgnoreCase(objSecond);
/*     */     }
/* 389 */     return getName().equalsIgnoreCase(concept.getName());
/*     */   }
/*     */ 
/*     */   public int getAttributeNum() {
/* 393 */     if (this.attributes == null) {
/* 394 */       return 0;
/*     */     }
/*     */ 
/* 397 */     return this.attributes.size();
/*     */   }
/*     */ 
/*     */   public int getAttributeOccurrence()
/*     */   {
/* 404 */     if (getAttributeNum() == 0) {
/* 405 */       return 0;
/*     */     }
/* 407 */     int count = 0;
/* 408 */     for (int i = 0; i < getAttributeNum(); i++) {
/* 409 */       count += getAttribute(i).getFrequency();
/*     */     }
/* 411 */     return count;
/*     */   }
/*     */ 
/*     */   public Term getAttribute(int index) {
/* 415 */     return (Term)this.attributes.get(index);
/*     */   }
/*     */ 
/*     */   public void addAttribute(Term attr) {
/* 419 */     if (this.attributes == null) {
/* 420 */       this.attributes = new SortedArray(2, new TermLemmaComparator());
/* 421 */       this.attributes.add(attr);
/*     */     }
/* 424 */     else if (!this.attributes.add(attr)) {
/* 425 */       Term cur = (Term)this.attributes.get(this.attributes.insertedPos());
/* 426 */       cur.addFrequency(attr.getFrequency());
/*     */     }
/*     */   }
/*     */ 
/*     */   public Word searchIn(Sentence sent)
/*     */   {
/* 437 */     return searchIn(sent, null);
/*     */   }
/*     */ 
/*     */   public Word searchIn(Sentence sent, Word start)
/*     */   {
/*     */     Word s;
/* 450 */     if (start == null)
/* 451 */       s = sent.getFirstWord();
/*     */     else
/* 453 */       s = start;
/* 454 */     while ((s != null) && (s.getPosInSentence() + getWordNum() <= sent.getWordNum())) {
/* 455 */       Word sCur = s;
/* 456 */       Word cCur = getStartingWord();
/* 457 */       int count = 0;
/* 458 */       while ((count < getWordNum()) && (sCur.getContent().equalsIgnoreCase(cCur.getContent()))) {
/* 459 */         count++;
/* 460 */         sCur = sCur.next;
/* 461 */         cCur = cCur.next;
/*     */       }
/* 463 */       if (count == getWordNum()) {
/* 464 */         return s;
/*     */       }
/* 466 */       s = s.next;
/*     */     }
/* 468 */     return null;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 472 */     if (this.index >= 0) {
/* 473 */       return this.index;
/*     */     }
/* 475 */     return super.hashCode();
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.Term
 * JD-Core Version:    0.6.2
 */