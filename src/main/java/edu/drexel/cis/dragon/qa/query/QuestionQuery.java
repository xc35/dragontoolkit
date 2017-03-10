/*     */ package edu.drexel.cis.dragon.qa.query;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class QuestionQuery
/*     */ {
/*     */   public static final int ANSWER_ENTY = 0;
/*     */   public static final int ANSWER_NUM = 1;
/*     */   public static final int ANSWER_WEB = 2;
/*     */   public static final int ANSWER_DESC = 3;
/*     */   public static final int ANSWER_ABBR = 4;
/*     */   public static final int ENTY_PERSON = 0;
/*     */   public static final int ENTY_ORG = 1;
/*     */   public static final int ENTY_LOC = 2;
/*     */   public static final int ENTY_TITLE = 3;
/*     */   public static final int ENTY_WORD = 4;
/*     */   public static final int ENTY_LETTER = 5;
/*     */   public static final int ENTY_OTHER = 6;
/*     */   public static final int NUM_DATE = 0;
/*     */   public static final int NUM_COUNT = 1;
/*     */   public static final int NUM_DISTANCE = 2;
/*     */   public static final int NUM_WEIGHT = 3;
/*     */   public static final int NUM_SIZE = 4;
/*     */   public static final int NUM_SPEED = 5;
/*     */   public static final int NUM_RANK = 6;
/*     */   public static final int NUM_PERCENT = 7;
/*     */   public static final int NUM_PERIOD = 8;
/*     */   public static final int NUM_TEMP = 9;
/*     */   public static final int NUM_PHONE = 10;
/*     */   public static final int NUM_PRICE = 11;
/*     */   public static final int NUM_OTHER = 12;
/*     */   public static final int DESC_DESC = 0;
/*     */   public static final int DESC_DEF = 1;
/*     */   public static final int DESC_PERSON = 2;
/*     */   public static final int DESC_REASON = 3;
/*     */   public static final int DESC_MANNER = 4;
/*     */   public static final int ABBR_ABBR = 0;
/*     */   public static final int ABBR_EXP = 1;
/*     */   public static final int WEB_URL = 0;
/*     */   public static final int WEB_EMAIL = 1;
/*     */   private SortedArray sortedList;
/*     */   private ArrayList list;
/*     */   private ArrayList queryList;
/*     */   private String headNoun;
/*     */   private String headVerb;
/*     */   private String qword;
/*     */   private String qbigram;
/*     */   private String baseQuery;
/*     */   private String wordQuery;
/*     */   private String groupQuery;
/*     */   private String usedQuery;
/*     */   private int answerType;
/*     */   private int subAnswerType;
/*     */ 
/*     */   public QuestionQuery()
/*     */   {
/*  55 */     this.sortedList = new SortedArray(15);
/*  56 */     this.list = new ArrayList(15);
/*  57 */     this.queryList = new ArrayList();
/*  58 */     this.answerType = -1;
/*  59 */     this.subAnswerType = -1;
/*     */   }
/*     */ 
/*     */   public void setUsedQuery(String query) {
/*  63 */     this.usedQuery = query;
/*     */   }
/*     */ 
/*     */   public String getUsedQuery() {
/*  67 */     return this.usedQuery;
/*     */   }
/*     */ 
/*     */   public void setBaseQuery(String query) {
/*  71 */     this.baseQuery = query;
/*     */   }
/*     */ 
/*     */   public String getBaseQuery() {
/*  75 */     return this.baseQuery;
/*     */   }
/*     */ 
/*     */   public void setWordQuery(String query) {
/*  79 */     this.wordQuery = query;
/*     */   }
/*     */ 
/*     */   public String getWordQuery() {
/*  83 */     return this.wordQuery;
/*     */   }
/*     */ 
/*     */   public void setGroupQuery(String query) {
/*  87 */     this.groupQuery = query;
/*     */   }
/*     */ 
/*     */   public String getGroupQuery() {
/*  91 */     return this.groupQuery;
/*     */   }
/*     */ 
/*     */   public int getQueryNum() {
/*  95 */     return this.queryList.size();
/*     */   }
/*     */ 
/*     */   public String getQuery(int index) {
/*  99 */     return (String)this.queryList.get(index);
/*     */   }
/*     */ 
/*     */   public void addQuery(String query) {
/* 103 */     this.queryList.add(query);
/*     */   }
/*     */ 
/*     */   public void addQueryWord(QueryWord word) {
/* 107 */     word.setIndex(this.list.size());
/* 108 */     this.list.add(word);
/* 109 */     this.sortedList.add(word);
/*     */   }
/*     */ 
/*     */   public QueryWord search(String word)
/*     */   {
/* 115 */     int pos = this.sortedList.binarySearch(new QueryWord(word));
/* 116 */     if (pos >= 0) {
/* 117 */       return (QueryWord)this.sortedList.get(pos);
/*     */     }
/* 119 */     return null;
/*     */   }
/*     */ 
/*     */   public QueryWord getQueryWord(int index) {
/* 123 */     return (QueryWord)this.list.get(index);
/*     */   }
/*     */ 
/*     */   public int size() {
/* 127 */     return this.list.size();
/*     */   }
/*     */ 
/*     */   public String getQuestionWord() {
/* 131 */     return this.qword;
/*     */   }
/*     */ 
/*     */   public void setQuestionWord(String word) {
/* 135 */     this.qword = word;
/*     */   }
/*     */ 
/*     */   public String getQuestionBigram() {
/* 139 */     return this.qbigram;
/*     */   }
/*     */ 
/*     */   public void setQuestionBigram(String word) {
/* 143 */     this.qbigram = word;
/*     */   }
/*     */ 
/*     */   public int getAnswerType() {
/* 147 */     return this.answerType;
/*     */   }
/*     */ 
/*     */   public void setAnswerType(int answerType) {
/* 151 */     this.answerType = answerType;
/*     */   }
/*     */ 
/*     */   public int getSubAnswerType() {
/* 155 */     return this.subAnswerType;
/*     */   }
/*     */ 
/*     */   public void setSubAnswerType(int answerType) {
/* 159 */     this.subAnswerType = answerType;
/*     */   }
/*     */ 
/*     */   public String getHeadNoun() {
/* 163 */     return this.headNoun;
/*     */   }
/*     */ 
/*     */   public void setHeadNoun(String headNoun) {
/* 167 */     this.headNoun = headNoun;
/*     */   }
/*     */ 
/*     */   public String getHeadVerb() {
/* 171 */     return this.headVerb;
/*     */   }
/*     */ 
/*     */   public void setHeadVerb(String headVerb) {
/* 175 */     this.headVerb = headVerb;
/*     */   }
/*     */ 
/*     */   public boolean existBigram(String first, String second)
/*     */   {
/* 181 */     for (int i = 0; i < this.list.size() - 1; i++) {
/* 182 */       if ((getQueryWord(i).getContent().equalsIgnoreCase(first)) && (getQueryWord(i + 1).getContent().equalsIgnoreCase(second)))
/* 183 */         return true;
/*     */     }
/* 185 */     return false;
/*     */   }
/*     */ 
/*     */   public void printQueryWordScore()
/*     */   {
/* 192 */     for (int i = 0; i < size(); i++) {
/* 193 */       QueryWord cur = getQueryWord(i);
/* 194 */       if (cur.getWeight() > 0.0D)
/* 195 */         System.out.println(cur.getContent() + "\t" + cur.getGroupID() + "\t" + cur.getWeight());
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 204 */     if (this.list.size() == 0) {
/* 205 */       return "";
/*     */     }
/* 207 */     StringBuffer buf = new StringBuffer();
/* 208 */     buf.append(getQueryWord(0).getContent());
/* 209 */     for (int i = 1; i < this.list.size(); i++) {
/* 210 */       buf.append(' ');
/* 211 */       buf.append(getQueryWord(i).getContent());
/*     */     }
/*     */ 
/* 214 */     return buf.toString();
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.query.QuestionQuery
 * JD-Core Version:    0.6.2
 */