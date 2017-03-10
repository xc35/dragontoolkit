/*     */ package edu.drexel.cis.dragon.qa.extract;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.qa.query.QueryWord;
/*     */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*     */ import edu.drexel.cis.dragon.qa.score.CandidateScorer;
/*     */ import edu.drexel.cis.dragon.qa.score.SimilarityScorer;
/*     */ import edu.drexel.cis.dragon.qa.system.Candidate;
/*     */ import edu.drexel.cis.dragon.qa.util.UnitUtil;
/*     */ import edu.drexel.cis.dragon.util.SimpleDictionary;
/*     */ import java.util.ArrayList;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class BasicNumberFinder extends AbstractCandidateFinder
/*     */ {
/*     */   public static final int TYPE_NUM = 0;
/*     */   public static final int TYPE_UNIT = 1;
/*     */   public static final int TYPE_CAT = 2;
/*     */   private static final String CURRENCY = "$£€";
/*     */   private UnitUtil unitDict;
/*     */   private SimpleDictionary enNumDict;
/*     */   private SimpleDictionary catDict;
/*     */   private SimpleDictionary funcDict;
/*     */   private Pattern numPattern;
/*     */ 
/*     */   public BasicNumberFinder(UnitUtil unitDict, SimpleDictionary enNumDict, SimpleDictionary funcDict)
/*     */   {
/*  37 */     this.sentThreshold = 0.25D;
/*  38 */     this.unitDict = unitDict;
/*  39 */     this.enNumDict = enNumDict;
/*  40 */     this.funcDict = funcDict;
/*  41 */     this.scorer = new SimilarityScorer();
/*  42 */     this.numPattern = Pattern.compile("(^([0-9]+)(\\W)?)+");
/*  43 */     this.catDict = new SimpleDictionary(true);
/*  44 */     this.catDict.add(".");
/*  45 */     this.catDict.add("-");
/*  46 */     this.catDict.add(":");
/*  47 */     this.catDict.add("/");
/*  48 */     this.catDict.add(",");
/*  49 */     this.catDict.add(")");
/*  50 */     this.catDict.add("per");
/*  51 */     this.catDict.add("of");
/*     */   }
/*     */ 
/*     */   public ArrayList extract(Sentence sent, QuestionQuery query)
/*     */   {
/*  63 */     ArrayList list = null;
/*  64 */     if (sent.getWordNum() > 255) {
/*  65 */       return list;
/*     */     }
/*  67 */     QueryWord[] arrQWord = new QueryWord[sent.getWordNum()];
/*  68 */     Word[] arrWord = new Word[sent.getWordNum()];
/*  69 */     Word cur = sent.getFirstWord();
/*  70 */     int i = 0;
/*  71 */     while (cur != null) {
/*  72 */       arrWord[i] = cur;
/*  73 */       cur = cur.next;
/*  74 */       i++;
/*     */     }
/*     */ 
/*  78 */     markQueryWord(arrWord, arrQWord, query, this.funcDict);
/*  79 */     if (this.scorer.initialize(query, sent, arrQWord) < this.sentThreshold) {
/*  80 */       return null;
/*     */     }
/*  82 */     list = new ArrayList();
/*     */ 
/*  85 */     int[] arrType = new int[arrWord.length];
/*  86 */     for (i = 0; i < arrType.length; i++) {
/*  87 */       if (isCurrencySign(arrWord[i].getContent()))
/*  88 */         arrType[i] = 0;
/*  89 */       else if (isNumericWord(arrWord[i].getContent()))
/*  90 */         arrType[i] = 0;
/*  91 */       else if (this.unitDict.search(arrWord[i].getContent()) != null)
/*  92 */         arrType[i] = 1;
/*  93 */       else if (this.catDict.exist(arrWord[i].getContent()))
/*  94 */         arrType[i] = 2;
/*     */       else {
/*  96 */         arrType[i] = -1;
/*     */       }
/*     */     }
/*     */ 
/* 100 */     i = 0;
/* 101 */     int start = 0;
/* 102 */     int end = 0;
/* 103 */     while (i < arrWord.length)
/* 104 */       if (arrType[i] != 0) {
/* 105 */         i++;
/*     */       }
/*     */       else
/*     */       {
/* 109 */         start = i;
/*     */ 
/* 112 */         int j = i + 1;
/* 113 */         while ((j < arrWord.length) && (arrType[j] >= 0)) {
/* 114 */           if ((arrType[j] == 2) && (arrType[(j - 1)] == 2))
/*     */             break;
/* 116 */           if ((arrType[j] == 2) && (arrWord[j].getContent().equals(","))) {
/* 117 */             String content = arrWord[(j - 1)].getContent();
/* 118 */             if ((content.length() >= 4) && (Character.isDigit(content.charAt(content.length() - 1))))
/* 119 */               break;
/*     */           } else {
/* 121 */             if ((arrType[j] == 1) && (arrType[(j - 1)] == 2) && (!arrWord[(j - 1)].getContent().equals("-"))) break;
/*     */           }
/* 123 */           j++;
/*     */         }
/* 125 */         end = j - 1;
/* 126 */         while (arrType[end] == 2) {
/* 127 */           end--;
/*     */         }
/*     */ 
/* 130 */         if ((arrQWord[start] != null) && (arrQWord[end] != null)) {
/* 131 */           i = end + 1;
/*     */         }
/*     */         else
/*     */         {
/* 136 */           Candidate curCand = new Candidate(arrWord[start], arrWord[end]);
/* 137 */           this.scorer.score(curCand);
/*     */ 
/* 139 */           if (list == null)
/* 140 */             list = new ArrayList();
/* 141 */           list.add(curCand);
/* 142 */           i = end + 1;
/*     */         }
/*     */       }
/* 144 */     return list;
/*     */   }
/*     */ 
/*     */   private boolean isNumericWord(String w) {
/* 148 */     if (w != null) {
/* 149 */       return (this.numPattern.matcher(w).find()) || (this.enNumDict.exist(w));
/*     */     }
/* 151 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean isCurrencySign(String w) {
/* 155 */     if (w.length() > 1) {
/* 156 */       return false;
/*     */     }
/* 158 */     return "$£€".indexOf(w) >= 0;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.extract.BasicNumberFinder
 * JD-Core Version:    0.6.2
 */