/*     */ package edu.drexel.cis.dragon.qa.filter;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.qa.extract.BasicNumberFinder;
/*     */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*     */ import edu.drexel.cis.dragon.qa.system.Candidate;
/*     */ import edu.drexel.cis.dragon.util.SimpleDictionary;
/*     */ 
/*     */ public class BasicNumberFilter extends AbstractFilter
/*     */ {
/*     */   private SimpleDictionary stopDict;
/*     */ 
/*     */   public BasicNumberFilter()
/*     */   {
/*  13 */     this.stopDict = new SimpleDictionary();
/*  14 */     this.stopDict.add("1");
/*  15 */     this.stopDict.add("first");
/*  16 */     this.stopDict.add("one");
/*     */   }
/*     */ 
/*     */   public boolean keep(QuestionQuery query, Candidate term)
/*     */   {
/*  21 */     if ((term.getWordNum() == 1) && (this.stopDict.exist(term.getStartingWord().getContent()))) {
/*  22 */       return false;
/*     */     }
/*  24 */     int subType = query.getSubAnswerType();
/*     */ 
/*  26 */     if (subType == 10)
/*  27 */       return isPhoneNumber(term);
/*  28 */     if (subType == 11)
/*  29 */       return isPrice(term);
/*  30 */     if (subType == 6)
/*  31 */       return (isRank(term)) || ((term.getWordNum() == 1) && (term.getStartingWord().isNumber()));
/*  32 */     if (subType == 0) {
/*  33 */       return isDate(term);
/*     */     }
/*  35 */     if ((isDate(term)) || (isPrice(term)) || (isRank(term))) {
/*  36 */       return false;
/*     */     }
/*  38 */     if (subType == 2) {
/*  39 */       if ((term.getTUI() == null) || (!term.getTUI().equalsIgnoreCase("dist")))
/*  40 */         return false;
/*     */     }
/*  42 */     else if (subType == 7) {
/*  43 */       if ((term.getTUI() == null) || (!term.getTUI().equalsIgnoreCase("percentage")))
/*  44 */         return false;
/*     */     }
/*  46 */     else if (subType == 4) {
/*  47 */       if ((term.getTUI() == null) || (!term.getTUI().equalsIgnoreCase("size")))
/*  48 */         return false;
/*     */     }
/*  50 */     else if (subType == 8) {
/*  51 */       if ((term.getTUI() == null) || (!term.getTUI().equalsIgnoreCase("period")))
/*  52 */         return false;
/*     */     }
/*  54 */     else if (subType == 3) {
/*  55 */       if ((term.getTUI() == null) || (!term.getTUI().equalsIgnoreCase("weight")))
/*  56 */         return false;
/*     */     }
/*  58 */     else if (subType == 5) {
/*  59 */       if ((term.getTUI() == null) || ((!term.getTUI().equalsIgnoreCase("speed")) && (!term.getTUI().equalsIgnoreCase("period"))))
/*  60 */         return false;
/*     */     }
/*  62 */     else if (subType == 1)
/*  63 */       return isCount(term);
/*  64 */     return true;
/*     */   }
/*     */ 
/*     */   protected boolean isPrice(Candidate curCand) {
/*  68 */     if ((curCand.getTUI() != null) && (curCand.getTUI().equalsIgnoreCase("price"))) {
/*  69 */       return true;
/*     */     }
/*  71 */     return BasicNumberFinder.isCurrencySign(curCand.getStartingWord().getContent());
/*     */   }
/*     */ 
/*     */   protected boolean isPhoneNumber(Candidate term) {
/*  75 */     if (term.getTUI() != null)
/*  76 */       return false;
/*  77 */     if (term.toString().length() < 7)
/*  78 */       return false;
/*  79 */     return true;
/*     */   }
/*     */ 
/*     */   protected boolean isDate(Candidate term) {
/*  83 */     return (term.getTUI() != null) && (term.getTUI().equalsIgnoreCase("date"));
/*     */   }
/*     */ 
/*     */   protected boolean isCount(Candidate term)
/*     */   {
/*  90 */     if ((term.getTUI() != null) && (term.getTUI().equalsIgnoreCase("count"))) {
/*  91 */       return true;
/*     */     }
/*  93 */     Word cur = term.getStartingWord();
/*  94 */     int i = 0;
/*  95 */     while (i < term.getWordNum()) {
/*  96 */       if (".-/%:)".indexOf(cur.getContent()) >= 0)
/*  97 */         return false;
/*  98 */       cur = cur.next;
/*  99 */       i++;
/*     */     }
/* 101 */     return !isRank(term);
/*     */   }
/*     */ 
/*     */   protected boolean isRank(Candidate term)
/*     */   {
/* 107 */     if (term.getTUI() != null)
/* 108 */       return false;
/* 109 */     Word cur = term.getEndingWord();
/* 110 */     if (!cur.isWord())
/* 111 */       return false;
/* 112 */     if ((cur.getContent().endsWith("st")) || (cur.getContent().endsWith("nd")) || 
/* 113 */       (cur.getContent().endsWith("rd")) || (cur.getContent().endsWith("th"))) {
/* 114 */       return true;
/*     */     }
/* 116 */     return false;
/*     */   }
/*     */ 
/*     */   protected boolean isInvalidNum(Candidate term)
/*     */   {
/* 121 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.filter.BasicNumberFilter
 * JD-Core Version:    0.6.2
 */