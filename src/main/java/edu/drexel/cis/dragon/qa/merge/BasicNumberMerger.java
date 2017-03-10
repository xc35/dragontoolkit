/*     */ package edu.drexel.cis.dragon.qa.merge;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.nlp.compare.WeightComparator;
/*     */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*     */ import edu.drexel.cis.dragon.qa.system.Candidate;
/*     */ import edu.drexel.cis.dragon.qa.util.Unit;
/*     */ import edu.drexel.cis.dragon.qa.util.UnitUtil;
/*     */ import edu.drexel.cis.dragon.util.SimpleDictionary;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.TreeMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class BasicNumberMerger
/*     */   implements CandidateMerger
/*     */ {
/*     */   private UnitUtil unitDict;
/*     */   private Pattern numPattern;
/*     */   private TreeMap numDict;
/*     */   private BasicDateMerger dateMerger;
/*     */   private SurfaceNumberMerger surfaceMerger;
/*     */ 
/*     */   public BasicNumberMerger(UnitUtil unitDict, SimpleDictionary dateDict)
/*     */   {
/*  23 */     this.unitDict = unitDict;
/*  24 */     this.numPattern = Pattern.compile("(\\d)+");
/*  25 */     this.numDict = getNumTreeMap();
/*  26 */     this.dateMerger = new BasicDateMerger(dateDict);
/*  27 */     this.surfaceMerger = new SurfaceNumberMerger();
/*     */   }
/*     */ 
/*     */   public ArrayList merge(QuestionQuery query, ArrayList candidates) {
/*  31 */     return this.dateMerger.merge(query, this.surfaceMerger.merge(query, unitMerge(candidates)));
/*     */   }
/*     */ 
/*     */   protected ArrayList unitMerge(ArrayList list)
/*     */   {
/*  42 */     SortedArray tokenList = new SortedArray();
/*  43 */     for (int i = 0; i < list.size(); i++) {
/*  44 */       Candidate curTerm = (Candidate)list.get(i);
/*  45 */       String val = normalize(curTerm);
/*  46 */       Token curToken = new Token(val);
/*  47 */       curToken.setMemo(curTerm);
/*  48 */       if (!tokenList.add(curToken)) {
/*  49 */         Token oldToken = (Token)tokenList.get(tokenList.insertedPos());
/*  50 */         Candidate oldTerm = (Candidate)oldToken.getMemo();
/*  51 */         if (curTerm.getWordNum() > oldTerm.getWordNum()) {
/*  52 */           curTerm.merge(oldTerm);
/*  53 */           oldToken.setMemo(curTerm);
/*     */         }
/*     */         else {
/*  56 */           oldTerm.merge(curTerm);
/*     */         }
/*     */       }
/*     */     }
/*  60 */     ArrayList newList = new ArrayList(tokenList.size());
/*  61 */     for (int i = 0; i < tokenList.size(); i++)
/*  62 */       newList.add(((Token)tokenList.get(i)).getMemo());
/*  63 */     Collections.sort(newList, new WeightComparator(true));
/*  64 */     return newList;
/*     */   }
/*     */ 
/*     */   private String normalize(Candidate term)
/*     */   {
/*  75 */     int count = term.getWordNum();
/*  76 */     int i = 0;
/*  77 */     StringBuffer buf = new StringBuffer();
/*  78 */     Word cur = term.getStartingWord();
/*  79 */     while (i < count) {
/*  80 */       String word = cur.getContent();
/*  81 */       if (word.equals("-")) {
/*  82 */         i++;
/*  83 */         cur = cur.next;
/*     */       }
/*     */       else {
/*  86 */         if (Character.isDigit(word.charAt(0))) {
/*  87 */           Matcher matcher = this.numPattern.matcher(word);
/*  88 */           matcher.find();
/*  89 */           buf.append(' ');
/*  90 */           buf.append(matcher.group());
/*  91 */           if (matcher.end() < word.length()) {
/*  92 */             buf.append(' ');
/*  93 */             Unit unit = this.unitDict.search(word.substring(matcher.end()));
/*  94 */             if (unit != null)
/*     */             {
/*  96 */               if ((term.getTUI() == null) || (!term.getTUI().equalsIgnoreCase("size")))
/*  97 */                 term.setTUI(unit.getCategory());
/*     */             }
/*     */             else
/* 100 */               buf.append(word.substring(matcher.end()));
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/*     */           String num;
/* 103 */           if ((num = (String)this.numDict.get(word.toLowerCase())) != null) {
/* 104 */             buf.append(' ');
/* 105 */             buf.append(num);
/*     */           }
/*     */           else
/*     */           {
/*     */             Unit unit;
/* 107 */             if ((i > 0) && ((unit = this.unitDict.search(cur.getContent())) != null))
/*     */             {
/* 109 */               if ((term.getTUI() == null) || (!term.getTUI().equalsIgnoreCase("size")))
/* 110 */                 term.setTUI(unit.getCategory());
/*     */             }
/*     */             else {
/* 113 */               buf.append(' ');
/* 114 */               buf.append(cur.getContent());
/*     */             }
/*     */           }
/*     */         }
/* 116 */         i++;
/* 117 */         cur = cur.next;
/*     */       }
/*     */     }
/* 119 */     return buf.toString().trim();
/*     */   }
/*     */ 
/*     */   private TreeMap getNumTreeMap()
/*     */   {
/* 125 */     TreeMap map = new TreeMap();
/* 126 */     map.put("one", "1");
/* 127 */     map.put("two", "2");
/* 128 */     map.put("three", "3");
/* 129 */     map.put("four", "4");
/* 130 */     map.put("five", "5");
/* 131 */     map.put("six", "6");
/* 132 */     map.put("seven", "7");
/* 133 */     map.put("eight", "8");
/* 134 */     map.put("nine", "9");
/* 135 */     map.put("ten", "10");
/* 136 */     map.put("eleven", "11");
/* 137 */     map.put("twelve", "12");
/* 138 */     map.put("thirteen", "13");
/* 139 */     map.put("fourteen", "14");
/* 140 */     map.put("fifteen", "15");
/* 141 */     map.put("sixteen", "16");
/* 142 */     map.put("seventeen", "17");
/* 143 */     map.put("eighteen", "18");
/* 144 */     map.put("ninteen", "19");
/* 145 */     map.put("twenty", "20");
/* 146 */     map.put("thirty", "30");
/* 147 */     map.put("forty", "40");
/* 148 */     map.put("fifty", "50");
/* 149 */     map.put("sixty", "60");
/* 150 */     map.put("seventy", "70");
/* 151 */     map.put("eighty", "80");
/* 152 */     map.put("ninty", "90");
/* 153 */     return map;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.merge.BasicNumberMerger
 * JD-Core Version:    0.6.2
 */