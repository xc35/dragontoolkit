/*     */ package edu.drexel.cis.dragon.qa.merge;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.nlp.compare.WeightComparator;
/*     */ import edu.drexel.cis.dragon.qa.query.QueryWord;
/*     */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*     */ import edu.drexel.cis.dragon.qa.system.Candidate;
/*     */ import edu.drexel.cis.dragon.util.SimpleDictionary;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ 
/*     */ public class AbbrEntityMerger
/*     */   implements CandidateMerger
/*     */ {
/*     */   private SimpleDictionary funcDict;
/*     */ 
/*     */   public AbbrEntityMerger(SimpleDictionary allowedFuncWord)
/*     */   {
/*  19 */     this.funcDict = allowedFuncWord;
/*     */   }
/*     */ 
/*     */   public ArrayList merge(QuestionQuery query, ArrayList candidates)
/*     */   {
/*  31 */     SortedArray tokenList = new SortedArray();
/*  32 */     ArrayList newList = new ArrayList();
/*  33 */     SortedArray abbrList = getAbbrList(query);
/*  34 */     String initial = getInitial(query);
/*     */ 
/*  36 */     for (int i = 0; i < candidates.size(); i++) {
/*  37 */       Candidate curTerm = (Candidate)candidates.get(i);
/*     */ 
/*  39 */       String abbr = getAbbr(curTerm);
/*  40 */       if (abbr == null) {
/*  41 */         newList.add(curTerm);
/*     */       } else {
/*  43 */         if (abbr.equals("USA")) {
/*  44 */           abbr = "US";
/*     */         }
/*     */ 
/*  47 */         if (query.getAnswerType() == 0) {
/*  48 */           if (abbrList.binarySearch(abbr) < 0)
/*     */           {
/*  50 */             if ((curTerm.getWordNum() == 1) && (initial.indexOf(abbr) >= 0));
/*     */           }
/*     */         }
/*     */         else {
/*  54 */           Token curToken = new Token(abbr);
/*  55 */           curToken.setMemo(curTerm);
/*  56 */           if (!tokenList.add(curToken)) {
/*  57 */             Token oldToken = (Token)tokenList.get(tokenList.insertedPos());
/*  58 */             Candidate oldTerm = (Candidate)oldToken.getMemo();
/*     */ 
/*  60 */             boolean needMerge = false;
/*  61 */             if ((oldTerm.getWordNum() == 1) || (curTerm.getWordNum() == 1))
/*  62 */               needMerge = true;
/*     */             else
/*  64 */               needMerge = compareCandidate(curTerm, oldTerm);
/*  65 */             if (needMerge) {
/*  66 */               oldTerm.merge(curTerm);
/*     */             } else {
/*  68 */               curToken.setValue(curTerm.toString());
/*  69 */               tokenList.add(curToken);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  75 */     for (int i = 0; i < tokenList.size(); i++)
/*  76 */       newList.add(((Token)tokenList.get(i)).getMemo());
/*  77 */     Collections.sort(newList, new WeightComparator(true));
/*  78 */     return newList;
/*     */   }
/*     */ 
/*     */   private boolean compareCandidate(Candidate first, Candidate second)
/*     */   {
/*  86 */     if (first.getWordNum() != second.getWordNum())
/*  87 */       return false;
/*  88 */     int len = first.getWordNum();
/*  89 */     Word start1 = first.getStartingWord();
/*  90 */     Word start2 = second.getStartingWord();
/*  91 */     int count = 0;
/*  92 */     while (count < len) {
/*  93 */       String val1 = getString(start1);
/*  94 */       String val2 = getString(start2);
/*  95 */       if ((val1.length() <= 2) || (val2.length() <= 2) || (val1.equalsIgnoreCase(val2))) {
/*  96 */         start1 = start1.next;
/*  97 */         start2 = start2.next;
/*  98 */         count++;
/*     */       }
/*     */       else {
/* 101 */         return false;
/*     */       }
/*     */     }
/* 103 */     return true;
/*     */   }
/*     */ 
/*     */   private SortedArray getAbbrList(QuestionQuery query)
/*     */   {
/* 111 */     SortedArray list = new SortedArray();
/* 112 */     for (int i = 0; i < query.size(); i++) {
/* 113 */       QueryWord word = query.getQueryWord(i);
/* 114 */       if (word.getAbbr() != null)
/* 115 */         list.add(word.getAbbr());
/*     */     }
/* 117 */     return list;
/*     */   }
/*     */ 
/*     */   private String getInitial(QuestionQuery query)
/*     */   {
/* 125 */     StringBuffer initial = new StringBuffer();
/* 126 */     for (int i = 0; i < query.size(); i++) {
/* 127 */       QueryWord word = query.getQueryWord(i);
/* 128 */       if (!this.funcDict.exist(word.getContent()))
/*     */       {
/* 131 */         initial.append(word.getContent().charAt(0));
/*     */       }
/*     */     }
/* 133 */     return initial.toString().replaceAll("'s", "");
/*     */   }
/*     */ 
/*     */   private String getAbbr(Candidate term)
/*     */   {
/* 141 */     int len = term.getWordNum();
/* 142 */     Word start = term.getStartingWord();
/* 143 */     if (len == 1) {
/* 144 */       if (isAbbr(start.getContent())) {
/* 145 */         return getAbbr(start.getContent());
/*     */       }
/* 147 */       return null;
/*     */     }
/*     */ 
/* 150 */     StringBuffer abbr = new StringBuffer();
/* 151 */     int count = 0;
/* 152 */     while (count < len) {
/* 153 */       if (this.funcDict.exist(start.getContent())) {
/* 154 */         count++;
/* 155 */         start = start.next;
/*     */       } else {
/* 157 */         if (!Character.isUpperCase(start.getContent().charAt(0))) {
/* 158 */           return null;
/*     */         }
/* 160 */         abbr.append(start.getContent().charAt(0));
/* 161 */         start = start.next;
/* 162 */         count++;
/*     */       }
/*     */     }
/* 165 */     return abbr.toString();
/*     */   }
/*     */ 
/*     */   public static String getAbbr(String word) {
/* 169 */     return word.replaceAll("\\.", "");
/*     */   }
/*     */ 
/*     */   public static boolean isAbbr(String word)
/*     */   {
/* 175 */     int count = 0;
/* 176 */     for (int i = 0; i < word.length(); i++) {
/* 177 */       if (Character.isUpperCase(word.charAt(i))) {
/* 178 */         count++;
/*     */       }
/* 180 */       else if (word.charAt(i) != '.')
/* 181 */         return false;
/*     */     }
/* 183 */     return count > 1;
/*     */   }
/*     */ 
/*     */   private String getString(Word word)
/*     */   {
/*     */     String val;
/* 189 */     if (word.getLemma() != null)
/* 190 */       val = word.getLemma();
/*     */     else
/* 192 */       val = word.getContent();
/* 193 */      val = val.replaceAll("\\.", "");
/* 194 */     val = val.replaceAll("-", "");
/* 195 */     if (val.endsWith("s"))
/* 196 */       val = val.substring(0, val.length() - 1);
/* 197 */     return val;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.merge.AbbrEntityMerger
 * JD-Core Version:    0.6.2
 */