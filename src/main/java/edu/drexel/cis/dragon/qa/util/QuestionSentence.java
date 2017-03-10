/*     */ package edu.drexel.cis.dragon.qa.util;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ 
/*     */ public class QuestionSentence
/*     */ {
/*     */   private static final String qwords = " who whose whom what which when where how why ";
/*     */   private Sentence sent;
/*     */ 
/*     */   public QuestionSentence(Sentence sent)
/*     */   {
/*  12 */     this.sent = sent;
/*     */   }
/*     */ 
/*     */   public Word getQuestionWord()
/*     */   {
/*  18 */     Word cur = this.sent.getFirstWord();
/*  19 */     while (cur != null) {
/*  20 */       if (isQuestionWord(cur.getContent())) {
/*  21 */         return cur;
/*     */       }
/*  23 */       cur = cur.next;
/*     */     }
/*  25 */     return cur;
/*     */   }
/*     */ 
/*     */   public Word getFirstVerb() {
/*  29 */     return getFirstVerb(this.sent.getFirstWord());
/*     */   }
/*     */ 
/*     */   public Word getFirstVerb(Word start)
/*     */   {
/*  35 */     Word cur = start;
/*  36 */     while (cur != null) {
/*  37 */       if ((cur.getPOSIndex() == 2) || (cur.getContent().equals("is"))) {
/*     */         break;
/*     */       }
/*  40 */       cur = cur.next;
/*     */     }
/*  42 */     if (cur == null) {
/*  43 */       return null;
/*     */     }
/*  45 */     return cur;
/*     */   }
/*     */ 
/*     */   public Word getHeadNoun(Word start)
/*     */   {
/*     */     Word cur;
/*  53 */     if (start != null) {
/*  54 */       cur = start.next;
/*     */     } else {
/*  56 */       cur = this.sent.getFirstWord();
/*  57 */       if (cur.getContent().equalsIgnoreCase("name")) {
/*  58 */         cur.setPOS("VB", 2);
/*     */       }
/*     */     }
/*  61 */     while (cur != null) {
/*  62 */       if (cur.getPOSIndex() == 1)
/*     */         break;
/*  64 */       cur = cur.next;
/*     */     }
/*  66 */     if (cur == null)
/*  67 */       return cur;
/*     */     boolean followQuestionNoun;
/*  69 */     if ((start != null) && (isQuestionWord(start.getContent())) && (start.next.equals(cur)))
/*  70 */       followQuestionNoun = true;
/*     */     else {
/*  72 */       followQuestionNoun = false;
/*     */     }
/*     */ 
/*  75 */     while ((cur.next != null) && (cur.next.getPOSIndex() == 1)) {
/*  76 */       cur = cur.next;
/*     */     }
/*     */ 
/*  79 */     if ((cur.next == null) || (!cur.next.getContent().equals("'")) || (followQuestionNoun)) {
/*  80 */       return cur;
/*     */     }
/*  82 */     Word noun = cur;
/*  83 */      cur = cur.next.next;
/*  84 */     if (cur == null)
/*  85 */       return noun;
/*  86 */     if (cur.getContent().equalsIgnoreCase("s"))
/*  87 */       cur = cur.next;
/*  88 */     while (cur != null) {
/*  89 */       if (cur.getPOSIndex() == 1)
/*     */         break;
/*  91 */       cur = cur.next;
/*     */     }
/*  93 */     if (cur == null)
/*  94 */       return noun;
/*  95 */     while ((cur.next != null) && (cur.next.getPOSIndex() == 1))
/*  96 */       cur = cur.next;
/*  97 */     return cur;
/*     */   }
/*     */ 
/*     */   public Word getPostModifierNoun(Word headNoun) {
/* 101 */     if (headNoun == null)
/* 102 */       return null;
/* 103 */     if ((headNoun.next == null) || (headNoun.next.getPOSIndex() != 5))
/* 104 */       return null;
/* 105 */     headNoun = headNoun.next;
/* 106 */     if (" of for ".indexOf(headNoun.getContent().toLowerCase()) < 0)
/* 107 */       return null;
/* 108 */     return getHeadNoun(headNoun);
/*     */   }
/*     */ 
/*     */   public boolean isBigramQuestionWord(Word questionWord)
/*     */   {
/* 114 */     if ((questionWord == null) || (questionWord.next == null) || (!questionWord.getContent().equalsIgnoreCase("how")))
/* 115 */       return false;
/* 116 */     Word cur = questionWord.next;
/* 117 */     if (" many much long often fast ".indexOf(" " + cur.getContent().toLowerCase() + " ") >= 0)
/* 118 */       return true;
/* 119 */     if ((cur.getPOSIndex() != 3) && (cur.getPOSIndex() != 4)) {
/* 120 */       return false;
/*     */     }
/* 122 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean isQuestionWord(String word) {
/* 126 */     return " who whose whom what which when where how why ".indexOf(" " + word.toLowerCase() + " ") >= 0;
/*     */   }
/*     */ 
/*     */   public static boolean isVerbBe(String verb) {
/* 130 */     return " is was am were are ".indexOf(" " + verb.toLowerCase() + " ") >= 0;
/*     */   }
/*     */ 
/*     */   public static boolean isVerbDo(String verb) {
/* 134 */     return " do does did ".indexOf(" " + verb.toLowerCase() + " ") >= 0;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.util.QuestionSentence
 * JD-Core Version:    0.6.2
 */