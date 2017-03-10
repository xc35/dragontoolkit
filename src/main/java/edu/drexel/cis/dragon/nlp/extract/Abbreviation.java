/*     */ package edu.drexel.cis.dragon.nlp.extract;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.Term;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ public class Abbreviation
/*     */ {
/*     */   private TreeMap abbrList;
/*     */   private Term referringTerm;
/*     */   private char firstUpperChar;
/*     */   private char lastUpperChar;
/*     */ 
/*     */   public Abbreviation()
/*     */   {
/*  23 */     this.abbrList = new TreeMap();
/*     */   }
/*     */ 
/*     */   public static void main(String[] args) {
/*  27 */     Abbreviation abbr = new Abbreviation();
/*     */ 
/*  31 */     Sentence sent = new EngDocumentParser().parseSentence("divalent metal transporter (DMT1).");
/*  32 */     Term term = new Term(sent.getFirstWord(), sent.getWord(2));
/*  33 */     abbr.isAbbrOfLastTerm(sent.getWord(4), term);
/*     */   }
/*     */ 
/*     */   public void clearCachedAbbr() {
/*  37 */     this.abbrList.clear();
/*     */   }
/*     */ 
/*     */   public boolean contains(String abbr) {
/*  41 */     if (abbr.charAt(abbr.length() - 1) == 's')
/*  42 */       abbr = abbr.substring(0, abbr.length() - 1);
/*  43 */     this.referringTerm = ((Term)this.abbrList.get(abbr.toUpperCase()));
/*  44 */     if (this.referringTerm != null) {
/*  45 */       return true;
/*     */     }
/*  47 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isAbbrOfLastTerm(Word word, Term lastTerm)
/*     */   {
/*  54 */     if (lastTerm == null) return false;
/*  55 */     if (word.prev == null) return false;
/*  56 */     if (word.next == null) return false;
/*  57 */     if (!word.prev.getContent().equalsIgnoreCase("(")) return false;
/*  58 */     if (!word.next.getContent().equalsIgnoreCase(")")) return false;
/*  59 */     if (!word.prev.prev.equals(lastTerm.getEndingWord())) return false;
/*     */ 
/*  61 */     String abbr = word.getContent();
/*  62 */     int upperCharNum = analyzeUpperLetter(abbr);
/*  63 */     if ((upperCharNum < 2) || (upperCharNum > lastTerm.getWordNum())) return false;
/*     */ 
/*  65 */     if (!equalCharIgnoreCase(lastTerm.getStartingWord().getContent().charAt(0), this.firstUpperChar)) return false;
/*  66 */     if (!equalCharIgnoreCase(lastTerm.getEndingWord().getContent().charAt(0), this.lastUpperChar)) return false;
/*  67 */     return true;
/*     */   }
/*     */ 
/*     */   public void put(String abbr, Term associatedTerm) {
/*  71 */     if (abbr.charAt(abbr.length() - 1) == 's')
/*  72 */       abbr = abbr.substring(0, abbr.length() - 1);
/*  73 */     if (!this.abbrList.containsKey(abbr.toUpperCase()))
/*  74 */       this.abbrList.put(abbr.toUpperCase(), associatedTerm);
/*     */   }
/*     */ 
/*     */   public Term get()
/*     */   {
/*  79 */     return this.referringTerm;
/*     */   }
/*     */ 
/*     */   public Term get(String abbr)
/*     */   {
/*  85 */     Term cur = (Term)this.abbrList.get(abbr.toUpperCase());
/*  86 */     return cur;
/*     */   }
/*     */ 
/*     */   private boolean equalCharIgnoreCase(char a, char b) {
/*  90 */     if ((a == b) || (Math.abs(a - b) == 32)) {
/*  91 */       return true;
/*     */     }
/*  93 */     return false;
/*     */   }
/*     */ 
/*     */   private int analyzeUpperLetter(String abbr)
/*     */   {
/*  99 */     int count = 0;
/* 100 */     this.firstUpperChar = '\000';
/* 101 */     this.lastUpperChar = '\000';
/* 102 */     for (int i = 0; i < abbr.length(); i++)
/*     */     {
/* 104 */       if (Character.isUpperCase(abbr.charAt(i)))
/*     */       {
/* 106 */         count++;
/* 107 */         if (this.firstUpperChar == 0) this.firstUpperChar = abbr.charAt(i);
/* 108 */         this.lastUpperChar = abbr.charAt(i);
/*     */       }
/*     */     }
/* 111 */     return count;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.extract.Abbreviation
 * JD-Core Version:    0.6.2
 */