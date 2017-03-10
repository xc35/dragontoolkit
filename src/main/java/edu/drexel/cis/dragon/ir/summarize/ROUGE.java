/*     */ package edu.drexel.cis.dragon.ir.summarize;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Counter;
/*     */ import edu.drexel.cis.dragon.nlp.Document;
/*     */ import edu.drexel.cis.dragon.nlp.DocumentParser;
/*     */ import edu.drexel.cis.dragon.nlp.Paragraph;
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.nlp.SimplePair;
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*     */ import edu.drexel.cis.dragon.nlp.extract.BasicConceptFilter;
/*     */ import edu.drexel.cis.dragon.nlp.extract.BasicTokenExtractor;
/*     */ import edu.drexel.cis.dragon.nlp.extract.TokenExtractor;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*     */ import edu.drexel.cis.dragon.nlp.tool.PorterStemmer;
/*     */ import edu.drexel.cis.dragon.util.EnvVariable;
/*     */ import edu.drexel.cis.dragon.util.MathUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ROUGE
/*     */ {
/*     */   public static final int ROUGE_N = 1;
/*     */   public static final int ROUGE_L = 2;
/*     */   public static final int ROUGE_W = 3;
/*     */   public static final int ROUGE_S = 4;
/*     */   public static final int ROUGE_SU = 5;
/*     */   public static final int MULTIPLE_MAX = 1;
/*     */   public static final int MULTIPLE_MIN = 2;
/*     */   public static final int MULTIPLE_AVG = 3;
/*     */   private static final String stopwordFile = "nlpdata/rouge/rouge.stopword";
/*     */   private TokenExtractor tokenExtractor;
/*     */   private double beta;
/*     */   private double[][] evaStat;
/*     */   private int metric;
/*     */   private int multipleMode;
/*     */   private int gram;
/*     */   private int maxSkip;
/*     */   private boolean caseSensitive;
/*     */ 
/*     */   public ROUGE()
/*     */   {
/*  42 */     this.caseSensitive = false;
/*  43 */     this.tokenExtractor = new BasicTokenExtractor(null);
/*  44 */     this.beta = 1.0D;
/*  45 */     this.metric = 1;
/*  46 */     this.gram = 2;
/*     */   }
/*     */ 
/*     */   public void setBeta(double beta) {
/*  50 */     if (beta > 0.0D)
/*  51 */       this.beta = beta;
/*     */   }
/*     */ 
/*     */   public double getBeta() {
/*  55 */     return this.beta;
/*     */   }
/*     */ 
/*     */   public void setLemmatiser(Lemmatiser lemmatiser) {
/*  59 */     this.tokenExtractor.setLemmatiser(lemmatiser);
/*     */   }
/*     */ 
/*     */   public Lemmatiser getLemmatiser() {
/*  63 */     return this.tokenExtractor.getLemmatiser();
/*     */   }
/*     */ 
/*     */   public void setLemmatiserOption(boolean option) {
/*  67 */     if (option)
/*  68 */       this.tokenExtractor.setLemmatiser(new PorterStemmer());
/*     */     else
/*  70 */       this.tokenExtractor.setLemmatiser(null);
/*     */   }
/*     */ 
/*     */   public boolean getLemmatiserOption() {
/*  74 */     return this.tokenExtractor.getLemmatiser() != null;
/*     */   }
/*     */ 
/*     */   public void setMultipleReferenceMode(int mode) {
/*  78 */     this.multipleMode = mode;
/*     */   }
/*     */ 
/*     */   public void setStopwordOption(boolean option) {
/*  82 */     if (option)
/*  83 */       this.tokenExtractor.setConceptFilter(new BasicConceptFilter(EnvVariable.getDragonHome() + "/" + "nlpdata/rouge/rouge.stopword"));
/*     */     else
/*  85 */       this.tokenExtractor.setFilteringOption(false);
/*     */   }
/*     */ 
/*     */   public boolean getStopwordOption() {
/*  89 */     return this.tokenExtractor.getFilteringOption();
/*     */   }
/*     */ 
/*     */   public void setStopwordFile(String stopwordFile) {
/*  93 */     this.tokenExtractor.setConceptFilter(new BasicConceptFilter(stopwordFile));
/*     */   }
/*     */ 
/*     */   public void setCaseOption(boolean sensitive) {
/*  97 */     this.caseSensitive = sensitive;
/*     */   }
/*     */ 
/*     */   public boolean getCaseOption() {
/* 101 */     return this.caseSensitive;
/*     */   }
/*     */ 
/*     */   public void useRougeN(int gram) {
/* 105 */     this.gram = gram;
/* 106 */     this.metric = 1;
/*     */   }
/*     */ 
/*     */   public int getGram() {
/* 110 */     return this.gram;
/*     */   }
/*     */ 
/*     */   public void useRougeS() {
/* 114 */     this.maxSkip = 2147483647;
/* 115 */     this.metric = 4;
/*     */   }
/*     */ 
/*     */   public void useRougeS(int maxSkip) {
/* 119 */     this.maxSkip = maxSkip;
/* 120 */     this.metric = 4;
/*     */   }
/*     */ 
/*     */   public double getPrecision() {
/* 124 */     return getEvaResult(1);
/*     */   }
/*     */ 
/*     */   public double getRecall() {
/* 128 */     return getEvaResult(0);
/*     */   }
/*     */ 
/*     */   public double getFScore() {
/* 132 */     return getEvaResult(2);
/*     */   }
/*     */ 
/*     */   private double getEvaResult(int dimension)
/*     */   {
/* 139 */     double[] results = new double[this.evaStat.length];
/* 140 */     for (int i = 0; i < results.length; i++)
/* 141 */       results[i] = this.evaStat[i][dimension];
/* 142 */     if (this.multipleMode == 1)
/* 143 */       return MathUtil.max(results);
/* 144 */     if (this.multipleMode == 3)
/* 145 */       return MathUtil.average(results);
/* 146 */     if (this.multipleMode == 2) {
/* 147 */       return MathUtil.min(results);
/*     */     }
/* 149 */     return -1.0D;
/*     */   }
/*     */ 
/*     */   public synchronized boolean evaluate(String testSummary, String[] refSummaries)
/*     */   {
/* 155 */     boolean ret = true;
/* 156 */     if (this.metric == 1)
/* 157 */       computeRougeN(testSummary, refSummaries);
/* 158 */     else if (this.metric == 4)
/* 159 */       computeRougeS(testSummary, refSummaries);
/* 160 */     else if (this.metric == 2)
/* 161 */       computeRougeL(testSummary, refSummaries);
/* 162 */     else if (this.metric == 5)
/* 163 */       computeRougeSU(testSummary, refSummaries);
/*     */     else
/* 165 */       ret = false;
/* 166 */     return ret;
/*     */   }
/*     */ 
/*     */   public void printResult()
/*     */   {
/* 171 */     for (int k = 0; k < 50; k++) {
/* 172 */       System.out.print("-");
/*     */     }
/* 174 */     System.out.println();
/* 175 */     for (int j = 0; j < this.evaStat.length; j++) {
/* 176 */       System.out.println("ReferenceModel: " + (j + 1));
/* 177 */       System.out.println("Average_R: " + this.evaStat[j][0]);
/* 178 */       System.out.println("Average_P: " + this.evaStat[j][1]);
/* 179 */       System.out.println("Average_F: " + this.evaStat[j][2]);
/* 180 */       System.out.println();
/*     */     }
/* 182 */     for (int k = 0; k < 50; k++) {
/* 183 */       System.out.print("-");
/*     */     }
/* 185 */     System.out.println();
/*     */   }
/*     */ 
/*     */   private void computeRougeN(String testSummary, String[] refSummaries)
/*     */   {
/* 193 */     ArrayList testList = tokenize(testSummary);
/* 194 */     this.evaStat = new double[refSummaries.length][3];
/* 195 */     HashMap testHash = computeNgrams(testList, this.gram);
/* 196 */     int test = testList.size() - this.gram + 1;
/*     */ 
/* 198 */     for (int j = 0; j < refSummaries.length; j++) {
/* 199 */       ArrayList referenceList = tokenize(refSummaries[j]);
/* 200 */       HashMap refHash = computeNgrams(referenceList, this.gram);
/* 201 */       int match = matchNgrams(testHash, refHash);
/* 202 */       int reference = referenceList.size() - this.gram + 1;
/*     */ 
/* 204 */       if (reference <= 0)
/* 205 */         this.evaStat[j][0] = 0.0D;
/*     */       else {
/* 207 */         this.evaStat[j][0] = (match / reference);
/*     */       }
/* 209 */       if (test <= 0)
/* 210 */         this.evaStat[j][1] = 0.0D;
/*     */       else {
/* 212 */         this.evaStat[j][1] = (match / test);
/*     */       }
/* 214 */       this.evaStat[j][2] = computeFScore(this.evaStat[j][1], this.evaStat[j][0]);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void computeRougeS(String testSummary, String[] refSummaries)
/*     */   {
/* 224 */     SimpleElementList keyList = new SimpleElementList();
/* 225 */     ArrayList testList = index(tokenize(testSummary), keyList);
/* 226 */     int test = countSkipBigram(testList.size(), this.maxSkip);
/* 227 */     this.evaStat = new double[refSummaries.length][3];
/*     */ 
/* 229 */     for (int j = 0; j < refSummaries.length; j++) {
/* 230 */       ArrayList referenceList = index(tokenize(refSummaries[j]), keyList);
/* 231 */       HashSet hashGrams = computeSkipBigram(referenceList, this.maxSkip);
/* 232 */       int match = matchSkipBigram(testList, this.maxSkip, hashGrams);
/* 233 */       int reference = countSkipBigram(testList.size(), this.maxSkip);
/*     */ 
/* 235 */       if (reference <= 0)
/* 236 */         this.evaStat[j][0] = 0.0D;
/*     */       else {
/* 238 */         this.evaStat[j][0] = (match / reference);
/*     */       }
/* 240 */       if (test <= 0)
/* 241 */         this.evaStat[j][1] = 0.0D;
/*     */       else {
/* 243 */         this.evaStat[j][1] = (match / test);
/*     */       }
/* 245 */       this.evaStat[j][2] = computeFScore(this.evaStat[j][1], this.evaStat[j][0]);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void computeRougeSU(String testSummary, String[] refSummaries)
/*     */   {
/* 255 */     SimpleElementList keyList = new SimpleElementList();
/* 256 */     ArrayList testList = index(tokenize(testSummary), keyList);
/* 257 */     int test = countSkipBigram(testList.size(), this.maxSkip) + testList.size();
/* 258 */     this.evaStat = new double[refSummaries.length][3];
/*     */ 
/* 260 */     for (int j = 0; j < refSummaries.length; j++) {
/* 261 */       ArrayList referenceList = index(tokenize(refSummaries[j]), keyList);
/* 262 */       HashSet hashGrams = computeSkipBigram(referenceList, this.maxSkip);
/* 263 */       int match = matchSkipBigram(testList, this.maxSkip, hashGrams);
/* 264 */       int reference = countSkipBigram(testList.size(), this.maxSkip) + referenceList.size();
/*     */ 
/* 266 */       if (reference <= 0)
/* 267 */         this.evaStat[j][0] = 0.0D;
/*     */       else {
/* 269 */         this.evaStat[j][0] = (match / reference);
/*     */       }
/* 271 */       if (test <= 0)
/* 272 */         this.evaStat[j][1] = 0.0D;
/*     */       else {
/* 274 */         this.evaStat[j][1] = (match / test);
/*     */       }
/* 276 */       this.evaStat[j][2] = computeFScore(this.evaStat[j][1], this.evaStat[j][0]);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void computeRougeL(String testSummary, String[] refSummaries)
/*     */   {
/* 287 */     DocumentParser parser = this.tokenExtractor.getDocumentParser();
/* 288 */     Document testDoc = parser.parse(testSummary);
/* 289 */     int test = tokenize(testDoc).size();
/* 290 */     this.evaStat = new double[refSummaries.length][3];
/*     */ 
/* 292 */     for (int j = 0; j < refSummaries.length; j++) {
/* 293 */       int match = 0;
/* 294 */       Document refDoc = parser.parse(refSummaries[j]);
/* 295 */       Paragraph curPara = refDoc.getFirstParagraph();
/* 296 */       while (curPara != null) {
/* 297 */         Sentence curSent = curPara.getFirstSentence();
/* 298 */         while (curSent != null) {
/* 299 */           match += matchLCS(curSent, testDoc);
/* 300 */           curSent = curSent.next;
/*     */         }
/* 302 */         curPara = curPara.next;
/*     */       }
/* 304 */       int reference = tokenize(refDoc).size();
/*     */ 
/* 306 */       if (reference <= 0)
/* 307 */         this.evaStat[j][0] = 0.0D;
/*     */       else {
/* 309 */         this.evaStat[j][0] = (match / reference);
/*     */       }
/* 311 */       if (test <= 0)
/* 312 */         this.evaStat[j][1] = 0.0D;
/*     */       else {
/* 314 */         this.evaStat[j][1] = (match / test);
/*     */       }
/* 316 */       this.evaStat[j][2] = computeFScore(this.evaStat[j][1], this.evaStat[j][0]);
/*     */     }
/*     */   }
/*     */ 
/*     */   private HashMap computeNgrams(ArrayList wordList, int nGram)
/*     */   {
/* 326 */     int start = 0;
/* 327 */     int end = nGram;
/* 328 */     HashMap hashGrams = new HashMap();
/* 329 */     while (end <= wordList.size()) {
/* 330 */       String gramStr = getNgram(wordList, start, end);
/* 331 */       Counter counter = (Counter)hashGrams.get(gramStr);
/* 332 */       if (counter != null)
/* 333 */         counter.addCount(1);
/*     */       else
/* 335 */         hashGrams.put(gramStr, new Counter(1));
/* 336 */       start++;
/* 337 */       end++;
/*     */     }
/* 339 */     return hashGrams;
/*     */   }
/*     */ 
/*     */   private int matchNgrams(HashMap testHash, HashMap refMap)
/*     */   {
/* 348 */     int count = 0;
/* 349 */     Iterator iterator = testHash.keySet().iterator();
/* 350 */     while (iterator.hasNext()) {
/* 351 */       String gramStr = (String)iterator.next();
/* 352 */       Counter testCounter = (Counter)testHash.get(gramStr);
/* 353 */       Counter refCounter = (Counter)refMap.get(gramStr);
/* 354 */       if (refCounter != null)
/* 355 */         count += Math.min(testCounter.getCount(), refCounter.getCount());
/*     */     }
/* 357 */     return count;
/*     */   }
/*     */ 
/*     */   private String getNgram(ArrayList wordList, int start, int end)
/*     */   {
/* 364 */     String gramStr = null;
/* 365 */     for (int i = start; i < end; i++) {
/* 366 */       if (i == 0)
/* 367 */         gramStr = ((Token)wordList.get(i)).getName();
/*     */       else
/* 369 */         gramStr = gramStr + "\t" + ((Token)wordList.get(i)).getName();
/*     */     }
/* 371 */     return gramStr;
/*     */   }
/*     */ 
/*     */   private HashSet computeSkipBigram(ArrayList list, int maxSkip)
/*     */   {
/* 378 */     HashSet hash = new HashSet();
/* 379 */     int start = 0;
/* 380 */     int end = Math.min(start + maxSkip + 1, list.size() - 1);
/* 381 */     while (start < end) {
/* 382 */       int first = ((Token)list.get(start)).getIndex();
/* 383 */       for (int i = start + 1; i <= end; i++) {
/* 384 */         int second = ((Token)list.get(i)).getIndex();
/* 385 */         hash.add(new SimplePair(hash.size(), first, second));
/*     */       }
/* 387 */       start++;
/* 388 */       end = Math.min(start + maxSkip + 1, list.size() - 1);
/*     */     }
/* 390 */     return hash;
/*     */   }
/*     */ 
/*     */   private int matchSkipBigram(ArrayList list, int maxSkip, HashSet reference)
/*     */   {
/* 396 */     int start = 0;
/* 397 */     int count = 0;
/* 398 */     int end = Math.min(start + maxSkip + 1, list.size() - 1);
/* 399 */     while (start < end) {
/* 400 */       int first = ((Token)list.get(start)).getIndex();
/* 401 */       for (int i = start + 1; i <= end; i++) {
/* 402 */         int second = ((Token)list.get(i)).getIndex();
/* 403 */         if (reference.contains(new SimplePair(-1, first, second)))
/* 404 */           count++;
/*     */       }
/* 406 */       start++;
/* 407 */       end = Math.min(start + maxSkip + 1, list.size() - 1);
/*     */     }
/* 409 */     return count;
/*     */   }
/*     */ 
/*     */   private int countSkipBigram(int textLength, int maxSkip)
/*     */   {
/* 415 */     int start = 0;
/* 416 */     int count = 0;
/* 417 */     int end = Math.min(start + maxSkip + 1, textLength - 1);
/* 418 */     while (start < end) {
/* 419 */       count += end - start;
/* 420 */       start++;
/* 421 */       end = Math.min(start + maxSkip + 1, textLength - 1);
/*     */     }
/* 423 */     return count;
/*     */   }
/*     */ 
/*     */   private int matchLCS(Sentence refSent, Document testDoc)
/*     */   {
/* 434 */     SimpleElementList keyList = new SimpleElementList();
/* 435 */     SortedArray list = new SortedArray(new IndexComparator());
/* 436 */     ArrayList refList = index(tokenize(refSent), keyList);
/* 437 */     Paragraph curPara = testDoc.getFirstParagraph();
/* 438 */     while (curPara != null) {
/* 439 */       Sentence curSent = curPara.getFirstSentence();
/* 440 */       while (curSent != null) {
/* 441 */         ArrayList testList = index(tokenize(curSent), keyList);
/* 442 */         ArrayList lcsList = computeLCS(refList, testList);
/* 443 */         for (int i = 0; i < lcsList.size(); i++)
/* 444 */           list.add(lcsList.get(i));
/* 445 */         curSent = curSent.next;
/*     */       }
/* 447 */       curPara = curPara.next;
/*     */     }
/* 449 */     return list.size();
/*     */   }
/*     */   private ArrayList computeLCS(ArrayList first, ArrayList second) {
/* 452 */     return null;
/*     */   }
/*     */ 
/*     */   private ArrayList index(ArrayList list, SimpleElementList keyList)
/*     */   {
/* 459 */     for (int i = 0; i < list.size(); i++) {
/* 460 */       Token curToken = (Token)list.get(i);
/* 461 */       curToken.setIndex(keyList.add(curToken.getValue()));
/*     */     }
/* 463 */     return list;
/*     */   }
/*     */ 
/*     */   private ArrayList tokenize(String doc)
/*     */   {
/* 471 */     ArrayList list = this.tokenExtractor.extractFromDoc(doc);
/* 472 */     if (!this.caseSensitive)
/*     */     {
/* 474 */       for (int i = 0; i < list.size(); i++) {
/* 475 */         Token curToken = (Token)list.get(i);
/* 476 */         curToken.setValue(curToken.getValue().toLowerCase());
/*     */       }
/*     */     }
/* 479 */     return list;
/*     */   }
/*     */ 
/*     */   private ArrayList tokenize(Document doc)
/*     */   {
/* 487 */     ArrayList list = this.tokenExtractor.extractFromDoc(doc);
/* 488 */     if (!this.caseSensitive)
/*     */     {
/* 490 */       for (int i = 0; i < list.size(); i++) {
/* 491 */         Token curToken = (Token)list.get(i);
/* 492 */         curToken.setValue(curToken.getValue().toLowerCase());
/*     */       }
/*     */     }
/* 495 */     return list;
/*     */   }
/*     */ 
/*     */   private ArrayList tokenize(Sentence sent)
/*     */   {
/* 503 */     ArrayList list = this.tokenExtractor.extractFromSentence(sent);
/* 504 */     if (!this.caseSensitive)
/*     */     {
/* 506 */       for (int i = 0; i < list.size(); i++) {
/* 507 */         Token curToken = (Token)list.get(i);
/* 508 */         curToken.setValue(curToken.getValue().toLowerCase());
/*     */       }
/*     */     }
/* 511 */     return list;
/*     */   }
/*     */ 
/*     */   private double computeFScore(double precision, double recall) {
/* 515 */     if ((precision == 0.0D) || (recall == 0.0D))
/* 516 */       return 0.0D;
/* 517 */     if (this.beta == 1.7976931348623157E+308D) {
/* 518 */       return recall;
/*     */     }
/* 520 */     return (1.0D + this.beta * this.beta) * precision * recall / (recall + this.beta * this.beta * precision);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.summarize.ROUGE
 * JD-Core Version:    0.6.2
 */