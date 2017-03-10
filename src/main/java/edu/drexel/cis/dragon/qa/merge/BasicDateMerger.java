/*     */ package edu.drexel.cis.dragon.qa.merge;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.nlp.compare.WeightComparator;
/*     */ import edu.drexel.cis.dragon.nlp.compare.WeightSortable;
/*     */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*     */ import edu.drexel.cis.dragon.qa.system.Candidate;
/*     */ import edu.drexel.cis.dragon.util.SimpleDictionary;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ public class BasicDateMerger
/*     */   implements CandidateMerger
/*     */ {
/*     */   private SimpleDictionary dateDict;
/*     */   private Word[] buf;
/*     */   private TreeMap dateMap;
/*     */ 
/*     */   public BasicDateMerger(SimpleDictionary dateWord)
/*     */   {
/*  27 */     this.dateDict = dateWord;
/*  28 */     this.buf = new Word[20];
/*  29 */     this.dateMap = getDateTreeMap();
/*     */   }
/*     */ 
/*     */   public ArrayList merge(QuestionQuery query, ArrayList candidates)
/*     */   {
/*  49 */     ArrayList newList = new ArrayList();
/*  50 */     SortedArray dateList = new SortedArray();
/*  51 */     for (int i = 0; i < candidates.size(); i++) {
/*  52 */       Candidate term = (Candidate)candidates.get(i);
/*  53 */       if ((term.getTUI() != null) && (!term.getTUI().equalsIgnoreCase("date"))) {
/*  54 */         newList.add(term);
/*     */       } else {
/*  56 */         DateUtil cur = convertDateString(term);
/*  57 */         if (cur != null) {
/*  58 */           cur.setCandidate(term);
/*     */ 
/*  60 */           if (!dateList.add(cur)) {
/*  61 */             DateUtil old = (DateUtil)dateList.get(dateList.insertedPos());
/*  62 */             Candidate oldTerm = old.getCandidate();
/*  63 */             oldTerm.merge(term);
/*     */           }
/*     */         }
/*     */         else {
/*  67 */           newList.add(term);
/*     */         }
/*     */       }
/*     */     }
/*  71 */     dateList.setComparator(new WeightComparator(true));
/*     */ 
/*  73 */     Candidate[] arrTerm = new Candidate[dateList.size()];
/*  74 */     DateUtil[] arrDate = new DateUtil[dateList.size()];
/*  75 */     for (int i = 0; i < dateList.size(); i++) {
/*  76 */       DateUtil cur = (DateUtil)dateList.get(i);
/*  77 */       arrTerm[i] = cur.getCandidate();
/*  78 */       arrDate[i] = cur;
/*     */     }
/*  80 */     dateList.clear();
/*     */ 
/*  82 */     for (int i = 0; i < arrTerm.length; i++) {
/*  83 */       if (arrDate[i] != null)
/*     */       {
/*  85 */         for (int j = i + 1; j < arrTerm.length; j++) {
/*  86 */           if (arrDate[j] != null)
/*     */           {
/*  88 */             if (arrDate[i].length() > arrDate[j].length()) {
/*  89 */               if (arrDate[i].canMerge(arrDate[j])) {
/*  90 */                 arrDate[j] = null;
/*  91 */                 arrTerm[i].merge(arrTerm[j]);
/*     */               }
/*     */             }
/*  94 */             else if ((arrDate[i].length() < arrDate[j].length()) && 
/*  95 */               (arrDate[j].canMerge(arrDate[i]))) {
/*  96 */               arrTerm[j].merge(arrTerm[i]);
/*  97 */               arrTerm[i] = arrTerm[j];
/*  98 */               arrDate[i] = arrDate[j];
/*  99 */               arrTerm[j] = null;
/* 100 */               arrDate[j] = null;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 106 */     for (int i = 0; i < arrDate.length; i++) {
/* 107 */       if (arrDate[i] != null)
/* 108 */         newList.add(arrTerm[i]);
/*     */     }
/* 110 */     Collections.sort(newList, new WeightComparator(true));
/* 111 */     return newList;
/*     */   }
/*     */ 
/*     */   private DateUtil convertDateString(Candidate term)
/*     */   {
/*     */     try
/*     */     {
/* 121 */       int date = -1;
/* 122 */       int month = -1;
/* 123 */       int year = -1;
/* 124 */       int num = term.getWordNum();
/* 125 */       Word cur = term.getStartingWord();
/* 126 */       int count = 0;
/* 127 */       int i = 0;
/* 128 */       boolean found = false;
/*     */ 
/* 130 */       while (i < num) {
/* 131 */         if (cur.isNumber()) {
/* 132 */           this.buf[count] = cur;
/* 133 */           count++;
/*     */         }
/* 135 */         else if (cur.isWord()) {
/* 136 */           if (cur.getContent().equalsIgnoreCase("of")) {
/* 137 */             if (!cur.next.isWord())
/* 138 */               return null;
/*     */           }
/*     */           else {
/* 141 */             if ((!this.dateDict.exist(cur.getContent())) && (!Character.isDigit(cur.getContent().charAt(0))))
/* 142 */               return null;
/* 143 */             this.buf[count] = cur;
/* 144 */             count++;
/*     */           }
/*     */         }
/* 147 */         i++;
/* 148 */         cur = cur.next;
/*     */       }
/*     */ 
/* 151 */       for (i = 0; i < count; i++) {
/* 152 */         if (this.buf[i].isNumber()) {
/* 153 */           int val = Integer.parseInt(this.buf[i].getContent());
/* 154 */           if (val <= 31) {
/* 155 */             if (val > 12) {
/* 156 */               if (date > 0) {
/* 157 */                 return null;
/*     */               }
/* 159 */               date = val;
/*     */             }
/* 162 */             else if (month < 0) {
/* 163 */               month = val;
/* 164 */             } else if (date < 0) {
/* 165 */               date = val;
/*     */             } else {
/* 167 */               return null;
/*     */             }
/*     */           }
/* 170 */           else if ((val > 1000) && (val < 2050)) {
/* 171 */             if (year > 0) {
/* 172 */               return null;
/*     */             }
/* 174 */             year = val;
/* 175 */             term.setTUI("date");
/*     */           }
/*     */           else
/*     */           {
/* 179 */             return null;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/*     */           int val;
/* 182 */           if (((val = convertEnglishDate(this.buf[i].getContent())) >= 0) || ((val = convertMixedDate(this.buf[i].getContent())) > 0)) {
/* 183 */             if (val > 31)
/* 184 */               return null;
/* 185 */             if (date > 0) {
/* 186 */               return null;
/*     */             }
/* 188 */             date = val;
/*     */           }
/* 191 */           else if ((val = convertMonth(this.buf[i].getContent())) > 0) {
/* 192 */             found = true;
/* 193 */             term.setTUI("date");
/* 194 */             if (month < 0) {
/* 195 */               month = val;
/* 196 */             } else if (date < 0) {
/* 197 */               date = month;
/* 198 */               month = val;
/*     */             }
/*     */             else {
/* 201 */               return null;
/*     */             }
/*     */           }
/*     */           else {
/* 205 */             term.setTUI("date");
/* 206 */             return null;
/*     */           }
/*     */         }
/*     */       }
/* 210 */       if ((year > 0) || ((month > 0) && (found))) {
/* 211 */         term.setTUI("date");
/* 212 */         return new DateUtil(date, month, year);
/*     */       }
/*     */ 
/* 215 */       return null;
/*     */     } catch (Exception e) {
/*     */     }
/* 218 */     return null;
/*     */   }
/*     */ 
/*     */   private int convertMonth(String m)
/*     */   {
/* 223 */     m = m.toLowerCase();
/* 224 */     if (m.indexOf("jan") != -1) {
/* 225 */       return 1;
/*     */     }
/* 227 */     if (m.indexOf("feb") != -1) {
/* 228 */       return 2;
/*     */     }
/* 230 */     if (m.indexOf("mar") != -1) {
/* 231 */       return 3;
/*     */     }
/* 233 */     if (m.indexOf("apr") != -1) {
/* 234 */       return 4;
/*     */     }
/* 236 */     if (m.indexOf("may") != -1) {
/* 237 */       return 5;
/*     */     }
/* 239 */     if (m.indexOf("jun") != -1) {
/* 240 */       return 6;
/*     */     }
/* 242 */     if (m.indexOf("july") != -1) {
/* 243 */       return 7;
/*     */     }
/* 245 */     if (m.indexOf("aug") != -1) {
/* 246 */       return 8;
/*     */     }
/* 248 */     if (m.indexOf("sept") != -1) {
/* 249 */       return 9;
/*     */     }
/* 251 */     if (m.indexOf("oct") != -1) {
/* 252 */       return 10;
/*     */     }
/* 254 */     if (m.indexOf("nov") != -1) {
/* 255 */       return 11;
/*     */     }
/* 257 */     if (m.indexOf("dec") != -1) {
/* 258 */       return 12;
/*     */     }
/* 260 */     return -1;
/*     */   }
/*     */ 
/*     */   private int convertEnglishDate(String d)
/*     */   {
/* 266 */     String obj = (String)this.dateMap.get(d.toLowerCase());
/* 267 */     if (obj == null) {
/* 268 */       return -1;
/*     */     }
/* 270 */     return Integer.parseInt(obj);
/*     */   }
/*     */ 
/*     */   private int convertMixedDate(String d) {
/*     */     try {
/* 275 */       if (d.length() < 3)
/* 276 */         return -1;
/* 277 */       if ("rd st nd th".indexOf(d.substring(d.length() - 2)) < 0) {
/* 278 */         return -1;
/*     */       }
/* 280 */       return Integer.parseInt(d.substring(0, d.length() - 2));
/*     */     } catch (Exception e) {
/*     */     }
/* 283 */     return -1;
/*     */   }
/*     */ 
/*     */   private TreeMap getDateTreeMap()
/*     */   {
/* 290 */     TreeMap map = new TreeMap();
/* 291 */     map.put("first", "1");
/* 292 */     map.put("second", "2");
/* 293 */     map.put("third", "3");
/* 294 */     map.put("fourth", "4");
/* 295 */     map.put("fifth", "5");
/* 296 */     map.put("sixth", "6");
/* 297 */     map.put("seventh", "7");
/* 298 */     map.put("eighth", "8");
/* 299 */     map.put("ninth", "9");
/* 300 */     map.put("tenth", "10");
/* 301 */     map.put("eleventh", "11");
/* 302 */     map.put("twelveth", "12");
/* 303 */     map.put("thirteenth", "13");
/* 304 */     map.put("fourteenth", "14");
/* 305 */     map.put("fifteenth", "15");
/* 306 */     map.put("sixteenth", "16");
/* 307 */     map.put("seventeenth", "17");
/* 308 */     map.put("eighteenth", "18");
/* 309 */     map.put("ninteenth", "19");
/* 310 */     map.put("twentieth", "20");
/* 311 */     map.put("thirtieth", "30");
/* 312 */     return map; } 
/*     */   public static void main(String[] args) {  } 
/*     */   private class DateUtil implements WeightSortable, Comparable { private Candidate term;
/*     */     private int date;
/*     */     private int month;
/*     */     private int year;
/*     */ 
/* 320 */     public DateUtil(int date, int month, int year) { this.date = date;
/* 321 */       this.month = month;
/* 322 */       this.year = year; }
/*     */ 
/*     */     public int getDate()
/*     */     {
/* 326 */       return this.date;
/*     */     }
/*     */ 
/*     */     public int getMonth() {
/* 330 */       return this.month;
/*     */     }
/*     */ 
/*     */     public int getYear() {
/* 334 */       return this.year;
/*     */     }
/*     */ 
/*     */     public boolean canMerge(DateUtil oDate)
/*     */     {
/* 340 */       int common = 0;
/* 341 */       int dif = 0;
/* 342 */       if ((this.date > 0) && (oDate.getDate() > 0)) {
/* 343 */         if (this.date == oDate.getDate())
/* 344 */           common++;
/*     */         else {
/* 346 */           dif++;
/*     */         }
/*     */       }
/* 349 */       if ((this.month > 0) && (oDate.getMonth() > 0)) {
/* 350 */         if (this.month == oDate.getMonth())
/* 351 */           common++;
/*     */         else {
/* 353 */           dif++;
/*     */         }
/*     */       }
/* 356 */       if ((this.year > 0) && (oDate.getYear() > 0)) {
/* 357 */         if (this.year == oDate.getYear())
/* 358 */           common++;
/*     */         else
/* 360 */           dif++;
/*     */       }
/* 362 */       return (common > 0) && (dif == 0);
/*     */     }
/*     */ 
/*     */     public int length() {
/* 366 */       int len = 0;
/*     */ 
/* 368 */       if (this.year > 0)
/* 369 */         len++;
/* 370 */       if (this.month > 0)
/* 371 */         len++;
/* 372 */       if (this.date > 0)
/* 373 */         len++;
/* 374 */       return len;
/*     */     }
/*     */ 
/*     */     public int compareTo(Object obj)
/*     */     {
/* 380 */       int oYear = ((DateUtil)obj).getYear();
/* 381 */       if (oYear != this.year) {
/* 382 */         return compare(this.year, oYear);
/*     */       }
/* 384 */       int oMonth = ((DateUtil)obj).getMonth();
/* 385 */       if (this.month != oMonth) {
/* 386 */         return compare(this.month, oMonth);
/*     */       }
/* 388 */       return compare(this.date, ((DateUtil)obj).getDate());
/*     */     }
/*     */ 
/*     */     private int compare(int a, int b)
/*     */     {
/* 393 */       if (a == b)
/* 394 */         return 0;
/* 395 */       if (a > b) {
/* 396 */         return 1;
/*     */       }
/* 398 */       return -1;
/*     */     }
/*     */ 
/*     */     public void setCandidate(Candidate term) {
/* 402 */       this.term = term;
/*     */     }
/*     */ 
/*     */     public Candidate getCandidate() {
/* 406 */       return this.term;
/*     */     }
/*     */ 
/*     */     public double getWeight() {
/* 410 */       return this.term.getWeight();
/*     */     }
/*     */ 
/*     */     public void setWeight(double weight) {
/* 414 */       this.term.setWeight(weight);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.merge.BasicDateMerger
 * JD-Core Version:    0.6.2
 */