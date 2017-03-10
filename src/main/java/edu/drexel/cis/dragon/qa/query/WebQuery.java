/*     */ package edu.drexel.cis.dragon.qa.query;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.extract.EngDocumentParser;
/*     */ import edu.drexel.cis.dragon.nlp.tool.BrillTagger;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Tagger;
/*     */ import edu.drexel.cis.dragon.nlp.tool.lemmatiser.EngLemmatiser;
/*     */ import edu.drexel.cis.dragon.qa.qc.QAClassifier;
/*     */ import edu.drexel.cis.dragon.qa.util.VerbUtil;
/*     */ import edu.drexel.cis.dragon.util.SimpleDictionary;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class WebQuery
/*     */ {
/*     */   private SimpleDictionary expDict;
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*  27 */     Tagger tagger = new BrillTagger();
/*  28 */     EngLemmatiser lemmatiser = new EngLemmatiser(false, false);
/*  29 */     SimpleDictionary funcDict = new SimpleDictionary("nlpdata/qa/functionword.lst");
/*  30 */     SimpleDictionary nouseHeadNounDict = new SimpleDictionary("nlpdata/qa/nouseheadnoun.lst");
/*  31 */     VerbUtil verbUtil = new VerbUtil("nlpdata/qa/verb.lst", "nlpdata/qa/irregularverb.lst");
/*  32 */     QAClassifier classifier = new QAClassifier(tagger, lemmatiser, "nlpdata/qa/qac_model.bin", "nlpdata/qa/qac_term.lst", "nlpdata/qa/qac.vob");
/*  33 */     EngDocumentParser parser = new EngDocumentParser(" \r\n\t_;,?/\"`:(){}!+[]><=%$#*@&^~|\\");
/*  34 */     BasicQueryGenerator generator = new BasicQueryGenerator(funcDict, nouseHeadNounDict, parser, tagger, lemmatiser, verbUtil, classifier, new GroupScorer());
/*  35 */     WebQuery webQuery = new WebQuery();
/*     */ 
/*  37 */     String question = "What is the email of He Zhao at Drexel";
/*  38 */     QuestionQuery query = generator.generate(question);
/*  39 */     ArrayList list = webQuery.generateGroupQuery(query);
/*  40 */     for (int i = 0; i < list.size(); i++)
/*  41 */       System.out.println((String)list.get(i));
/*     */   }
/*     */ 
/*     */   public WebQuery() {
/*  45 */     this.expDict = new SimpleDictionary();
/*  46 */     this.expDict.add("phone number");
/*  47 */     this.expDict.add("telephone number");
/*  48 */     this.expDict.add("fax number");
/*  49 */     this.expDict.add("email address");
/*  50 */     this.expDict.add("mailing address");
/*  51 */     this.expDict.add("cell phone number");
/*  52 */     this.expDict.add("cell number");
/*     */   }
/*     */ 
/*     */   public ArrayList generateGroupQuery(QuestionQuery query)
/*     */   {
/*  62 */     ArrayList list = new ArrayList();
/*  63 */     if (existDoubleQuote(query)) {
/*  64 */       list.add(generateWordQuery(query));
/*  65 */       return list;
/*     */     }
/*     */ 
/*  68 */     int grpNum = 20;
/*  69 */     int[] arrGrpStart = new int[grpNum];
/*  70 */     int[] arrGrpEnd = new int[grpNum];
/*  71 */     int[] arrGrpWord = new int[grpNum];
/*  72 */     int[] arrGrpNonFuncWord = new int[grpNum];
/*  73 */     boolean[] arrExp = new boolean[grpNum];
/*  74 */     int lastGrp = -1;
/*  75 */     int headNounGrp = -1;
/*  76 */     int targetGrp = -1;
/*  77 */     for (int i = 0; i < query.size(); i++) {
/*  78 */       QueryWord qword = query.getQueryWord(i);
/*  79 */       int curGrp = qword.getGroupID();
/*  80 */       if (curGrp != lastGrp) {
/*  81 */         if (lastGrp >= 0)
/*  82 */           arrGrpEnd[lastGrp] = (i - 1);
/*  83 */         arrGrpStart[curGrp] = i;
/*     */       }
/*     */ 
/*  86 */       arrGrpWord[curGrp] += 1;
/*  87 */       if ((qword.isInitialCapital()) || (!qword.isFunctionalWord()))
/*  88 */         arrGrpNonFuncWord[curGrp] += 1;
/*  89 */       if (qword.isHeadNoun())
/*  90 */         headNounGrp = curGrp;
/*  91 */       lastGrp = curGrp;
/*     */     }
/*  93 */     arrGrpEnd[lastGrp] = (query.size() - 1);
/*  94 */     for (int i = 0; i < grpNum; i++) {
/*  95 */       arrExp[i] = isExceptionGroup(query, arrGrpStart[i], arrGrpEnd[i]);
/*     */     }
/*  97 */     int start = -1;
/*  98 */     int end = -1;
/*     */ 
/* 101 */     if ((query.getAnswerType() == 0) && (headNounGrp >= 0) && (arrGrpNonFuncWord[headNounGrp] > 1) && (arrExp[headNounGrp] == false))
/* 102 */       targetGrp = headNounGrp;
/*     */     else {
/* 104 */       for (int i = 0; i < query.size(); i++) {
/* 105 */         int curGrp = query.getQueryWord(i).getGroupID();
/* 106 */         if ((arrGrpNonFuncWord[curGrp] > 1) && (arrExp[curGrp] == false)) {
/* 107 */           targetGrp = query.getQueryWord(i).getGroupID();
/* 108 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 113 */     if (targetGrp >= 0) {
/* 114 */       start = arrGrpStart[targetGrp];
/* 115 */       end = arrGrpEnd[targetGrp];
/*     */ 
/* 117 */       if (query.getQueryWord(start).getContent().equals("'")) {
/* 118 */         QueryWord qword = query.getQueryWord(start - 1);
/* 119 */         if (arrGrpNonFuncWord[qword.getGroupID()] == 1) {
/* 120 */           list.add(generateGroupQuery(arrGrpStart[qword.getGroupID()], end, query));
/*     */         }
/*     */ 
/*     */       }
/* 125 */       else if (query.size() > end + 1) {
/* 126 */         QueryWord qword = query.getQueryWord(end + 1);
/* 127 */         if ((arrGrpNonFuncWord[qword.getGroupID()] == 1) && (
/* 128 */           (qword.getContent().equals("'")) || (qword.getContent().equalsIgnoreCase("of")))) {
/* 129 */           list.add(generateGroupQuery(start, arrGrpEnd[qword.getGroupID()], query));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 137 */       for (int i = 1; i < query.size(); i++) {
/* 138 */         QueryWord qword = query.getQueryWord(i);
/* 139 */         if ((i < query.size() - 1) && ((qword.getContent().equals("'")) || ((qword.getPOSTag() == 5) && 
/* 140 */           (query.getQueryWord(i - 1).getPOSTag() != 2)))) {
/* 141 */           end = arrGrpEnd[qword.getGroupID()];
/* 142 */           start = arrGrpStart[query.getQueryWord(i - 1).getGroupID()];
/* 143 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 148 */     if ((start < 0) && (query.size() <= 4)) {
/* 149 */       start = 0;
/* 150 */       end = query.size() - 1;
/*     */     }
/*     */ 
/* 153 */     list.add(generateGroupQuery(start, end, query));
/* 154 */     return list;
/*     */   }
/*     */ 
/*     */   public String generateWordQuery(QuestionQuery query) {
/* 158 */     return query.toString().replaceAll(" ' ", "' ").replaceAll("' s ", "'s ");
/*     */   }
/*     */ 
/*     */   protected String generateGroupQuery(int start, int end, QuestionQuery query)
/*     */   {
/* 167 */     if (start >= 0) {
/* 168 */       while ((!query.getQueryWord(start).isInitialCapital()) && (query.getQueryWord(start).isFunctionalWord())) {
/* 169 */         start++;
/*     */       }
/*     */     }
/* 172 */     if ((start < 0) || (start == end)) {
/* 173 */       return generateWordQuery(query);
/*     */     }
/*     */ 
/* 176 */     StringBuffer buf = new StringBuffer();
/* 177 */     for (int i = 0; i < start; i++) {
/* 178 */       buf.append(query.getQueryWord(i).getContent());
/* 179 */       buf.append(' ');
/*     */     }
/* 181 */     buf.append("\" ");
/* 182 */     for (int i = start; i <= end; i++) {
/* 183 */       buf.append(query.getQueryWord(i).getContent());
/* 184 */       buf.append(' ');
/*     */     }
/* 186 */     buf.append("\"");
/* 187 */     for (int i = end + 1; i < query.size(); i++) {
/* 188 */       buf.append(' ');
/* 189 */       buf.append(query.getQueryWord(i).getContent());
/*     */     }
/*     */ 
/* 192 */     String str = buf.toString().replaceAll(" ' ", "' ").replaceAll("' s ", "'s ");
/* 193 */     str = str.replaceAll("\"'s ", "\" ").replaceAll("\"' ", "\" ");
/* 194 */     return str;
/*     */   }
/*     */ 
/*     */   protected boolean existDoubleQuote(QuestionQuery query) {
/* 198 */     for (int i = 0; i < query.size(); i++)
/* 199 */       if (query.getQueryWord(i).getContent().equals("\""))
/* 200 */         return true;
/* 201 */     return false;
/*     */   }
/*     */ 
/*     */   protected boolean isExceptionGroup(QuestionQuery query, int start, int end)
/*     */   {
/* 207 */     if ((start < 0) || (end < 0) || (start == end)) {
/* 208 */       return false;
/*     */     }
/*     */ 
/* 211 */     if (start >= 0) {
/* 212 */       while ((start <= end) && (!query.getQueryWord(start).isInitialCapital()) && (query.getQueryWord(start).isFunctionalWord()))
/* 213 */         start++;
/*     */     }
/* 215 */     if (start >= end) {
/* 216 */       return false;
/*     */     }
/* 218 */     StringBuffer buf = new StringBuffer(query.getQueryWord(start).getLemma());
/*     */     do {
/* 220 */       buf.append(" " + query.getQueryWord(start).getLemma());
/*     */ 
/* 219 */       start++; } while (start <= end);
/*     */ 
/* 221 */     return this.expDict.exist(buf.toString());
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.query.WebQuery
 * JD-Core Version:    0.6.2
 */