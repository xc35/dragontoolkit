/*     */ package edu.drexel.cis.dragon.qa.query;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.DocumentParser;
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*     */ import edu.drexel.cis.dragon.nlp.tool.PorterStemmer;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Tagger;
/*     */ import edu.drexel.cis.dragon.qa.qc.QAClassifier;
/*     */ import edu.drexel.cis.dragon.qa.util.QuestionSentence;
/*     */ import edu.drexel.cis.dragon.qa.util.VerbUtil;
/*     */ import edu.drexel.cis.dragon.util.SimpleDictionary;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class BasicQueryGenerator
/*     */   implements QueryGenerator
/*     */ {
/*     */   private SimpleDictionary funcDict;
/*     */   private SimpleDictionary nouseHeadNounDict;
/*     */   private DocumentParser parser;
/*     */   private Tagger tagger;
/*     */   private Lemmatiser lemmatiser;
/*     */   private Lemmatiser stemmer;
/*     */   private QuestionTrim trimer;
/*     */   private QAClassifier classifier;
/*     */   private VerbUtil verbUtil;
/*     */   private QueryScorer scorer;
/*     */   private WebQuery webQuery;
/*     */ 
/*     */   public BasicQueryGenerator(SimpleDictionary funcDict, SimpleDictionary nouseHeadNounDict, DocumentParser parser, Tagger tagger, Lemmatiser lemmatiser, VerbUtil verbUtil, QAClassifier classifier, QueryScorer scorer)
/*     */   {
/*  24 */     this.funcDict = funcDict;
/*  25 */     this.nouseHeadNounDict = nouseHeadNounDict;
/*  26 */     this.parser = parser;
/*  27 */     this.tagger = tagger;
/*  28 */     this.lemmatiser = lemmatiser;
/*  29 */     this.classifier = classifier;
/*  30 */     this.verbUtil = verbUtil;
/*  31 */     this.trimer = new QuestionTrim();
/*  32 */     this.scorer = scorer;
/*  33 */     this.webQuery = new WebQuery();
/*  34 */     this.stemmer = new PorterStemmer();
/*     */   }
/*     */ 
/*     */   public void setQueryScorer(QueryScorer scorer) {
/*  38 */     this.scorer = scorer;
/*     */   }
/*     */ 
/*     */   public QueryScorer getQueryScorer() {
/*  42 */     return this.scorer;
/*     */   }
/*     */ 
/*     */   public QuestionQuery generate(String question)
/*     */   {
/*     */     try
/*     */     {
/*  57 */       QuestionQuery query = new QuestionQuery();
/*  58 */       question = this.trimer.trim(question);
/*  59 */       Sentence sent = this.parser.parseSentence(question);
/*  60 */       tag(sent);
/*  61 */       lemmatise(sent);
/*     */ 
/*  64 */       int groupNum = GroupScorer.divideSentence(sent);
/*     */ 
/*  67 */       QuestionSentence qsent = new QuestionSentence(sent);
/*  68 */       Word qword = qsent.getQuestionWord();
/*  69 */       if (qword != null)
/*  70 */         query.setQuestionWord(qword.getContent().toLowerCase());
/*     */       Word qbiword;
/*  72 */       if (qsent.isBigramQuestionWord(qword))
/*  73 */         qbiword = qword.next;
/*     */       else
/*  75 */         qbiword = null;
/*  76 */       if (qbiword != null)
/*  77 */         query.setQuestionBigram(qbiword.getContent().toLowerCase());
/*  78 */       Word verb = qsent.getFirstVerb();
/*     */ 
/*  81 */       query.setHeadNoun(getHeadNoun(qsent, qword, verb));
/*     */ 
/*  84 */       query.setHeadVerb(getHeadVerb(qword, verb));
/*     */ 
/*  87 */       classifyAnswerType(query, sent);
/*  88 */       if (query.getAnswerType() == 1) {
/*  89 */         classifyNumType(query, sent);
/*     */       }
/*     */ 
/*  92 */       if (qword != null) {
/*  93 */         if (qword.prev == null)
/*     */         {
/*  95 */           if ((qbiword != null) && ("long often much many fast".indexOf(qbiword.getContent().toLowerCase()) >= 0))
/*  96 */             sent.resetBoundary(qbiword.next, sent.getLastWord());
/*  97 */           else if (this.nouseHeadNounDict.exist(query.getHeadNoun()))
/*  98 */             sent.resetBoundary(qword.next.next, sent.getLastWord());
/*     */           else
/* 100 */             sent.resetBoundary(qword.next, sent.getLastWord());
/*     */         }
/* 102 */         else if (qword.next != null)
/*     */         {
/* 104 */           Word curWord = qword;
/* 105 */           Word end = qword;
/* 106 */           if ((this.nouseHeadNounDict.exist(query.getHeadNoun())) && (qword.next.next != null))
/* 107 */             end = qword.next;
/* 108 */           curWord.prev.next = end.next;
/* 109 */           end.next.prev = curWord.prev;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 114 */       if ((qword != null) && (verb != null))
/*     */       {
/* 116 */         if (QuestionSentence.isVerbBe(verb.getContent())) {
/* 117 */           if (qword.getPosInSentence() == 0) {
/* 118 */             moveVerbBe(sent, verb);
/* 119 */             if (qword.getContent().equalsIgnoreCase("where")) {
/* 120 */               groupNum = reformulateWhereQuestion(sent, groupNum);
/*     */             }
/*     */           }
/*     */         }
/* 124 */         else if (QuestionSentence.isVerbDo(verb.getContent()))
/*     */         {
/* 126 */           adjustVerbForm(sent, verb);
/*     */ 
/* 128 */           moveVerbDo(sent, verb);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 133 */       Word curWord = sent.getFirstWord();
/* 134 */       while (curWord != null) {
/* 135 */         QueryWord curQWord = new QueryWord(curWord.getContent());
/* 136 */         if ((curQWord.getAbbr() != null) || (!isFunctionWord(curWord)))
/* 137 */           curQWord.setFunctionFlag(false);
/*     */         else
/* 139 */           curQWord.setFunctionFlag(true);
/* 140 */         curQWord.setPOSTag(curWord.getPOSIndex());
/* 141 */         if (curQWord.getAbbr() == null)
/* 142 */           curQWord.setLemma(curWord.getLemma());
/*     */         else
/* 144 */           curQWord.setLemma(curQWord.getAbbr());
/* 145 */         curQWord.setGroupID(curWord.getClauseID());
/* 146 */         if (curWord.getContent().equalsIgnoreCase(query.getHeadNoun()))
/* 147 */           curQWord.setHeadNounFlag(true);
/* 148 */         query.addQueryWord(curQWord);
/* 149 */         if ((curQWord.getPOSTag() == 2) && (QuestionSentence.isVerbBe(curQWord.getContent())))
/* 150 */           curQWord.setVerbBeFlag(true);
/* 151 */         curQWord.setStem(this.stemmer.lemmatize(curQWord.getContent()));
/* 152 */         curWord = curWord.next;
/*     */       }
/*     */ 
/* 156 */       query.setBaseQuery(question);
/* 157 */       query.setWordQuery(this.webQuery.generateWordQuery(query));
/* 158 */       ArrayList list = this.webQuery.generateGroupQuery(query);
/* 159 */       query.setGroupQuery((String)list.get(0));
/* 160 */       for (int i = 0; i < list.size(); i++)
/* 161 */         query.addQuery((String)list.get(i));
/* 162 */       query.addQuery(query.getWordQuery());
/*     */ 
/* 165 */       this.scorer.score(query);
/*     */ 
/* 167 */       return query;
/*     */     }
/*     */     catch (Exception e) {
/* 170 */       e.printStackTrace();
/* 171 */     }return null;
/*     */   }
/*     */ 
/*     */   protected void tag(Sentence sent)
/*     */   {
/* 179 */     this.tagger.tag(sent);
/*     */ 
/* 181 */     Word cur = sent.getFirstWord();
/* 182 */     while (cur != null) {
/* 183 */       if (cur.getPOSIndex() == 2) {
/* 184 */         if (cur.getContent().endsWith("ing")) {
/* 185 */           if (cur.isInitialCapital())
/* 186 */             cur.setPOS(cur.getPOSLabel(), 1);
/* 187 */           else if ((cur.prev != null) && (cur.prev.getPOSIndex() == 7))
/* 188 */             cur.setPOS(cur.getPOSLabel(), 3);
/*     */         }
/* 190 */         else if ((cur.prev != null) && ((cur.prev.getContent().equals("s")) || (cur.prev.getContent().equals("'"))))
/* 191 */           cur.setPOS(cur.getPOSLabel(), 1);
/* 192 */         else if (cur.getContent().equals("s"))
/* 193 */           cur.setPOS(cur.getPOSLabel(), 1);
/*     */       }
/* 195 */       else if (cur.getContent().equalsIgnoreCase("to"))
/* 196 */         cur.setPOS(cur.getPOSLabel(), 5);
/* 197 */       if (((cur.getContent().equalsIgnoreCase("email")) || (cur.getContent().equalsIgnoreCase("e-mail"))) && (
/* 198 */         (cur.next == null) || ((cur.next.getPOSIndex() != 1) && (cur.next.getPOSIndex() != 6)))) {
/* 199 */         cur.setPOS(cur.getPOSLabel(), 1);
/*     */       }
/* 201 */       cur = cur.next;
/*     */     }
/*     */ 
/* 204 */     QuestionSentence qsent = new QuestionSentence(sent);
/* 205 */     Word verb = qsent.getFirstVerb();
/* 206 */     if ((verb != null) && (QuestionSentence.isVerbDo(verb.getContent())) && (qsent.getFirstVerb(verb.next) == null)) {
/* 207 */       cur = verb.next;
/* 208 */       while (cur != null) {
/* 209 */         if ((!cur.isInitialCapital()) && (isPossibleVerbPrefix(cur.prev)) && (this.verbUtil.exist(cur.getContent()))) {
/* 210 */           cur.setPOS("VB", 2);
/* 211 */           return;
/*     */         }
/* 213 */         cur = cur.next;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void lemmatise(Sentence sent)
/*     */   {
/* 221 */     Word curWord = sent.getFirstWord();
/* 222 */     while (curWord != null) {
/* 223 */       curWord.setLemma(this.lemmatiser.lemmatize(curWord.getContent(), curWord.getPOSIndex()));
/* 224 */       if (curWord.getContent().equalsIgnoreCase("email"))
/* 225 */         curWord.setLemma("e-mail");
/* 226 */       curWord = curWord.next;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean isPossibleVerbPrefix(Word cur) {
/* 231 */     if ((cur.getPOSIndex() == 3) || (cur.getPOSIndex() == 7) || (cur.getPOSIndex() == 5) || 
/* 232 */       (cur.getContent().equalsIgnoreCase("s")) || (cur.getContent().equals("'"))) {
/* 233 */       return false;
/*     */     }
/* 235 */     return true;
/*     */   }
/*     */ 
/*     */   protected String getHeadVerb(Word qword, Word verb) {
/* 239 */     if ((verb == null) || (qword == null) || (qword.getPosInSentence() > verb.getPosInSentence()) || 
/* 240 */       (" what which who ".indexOf(qword.getContent().toLowerCase()) < 0))
/* 241 */       return null;
/* 242 */     if (" is was am were are do did does done has have had can could may might should ought ".indexOf(" " + verb.getContent() + " ") < 0) {
/* 243 */       return verb.getContent().toLowerCase();
/*     */     }
/* 245 */     return null;
/*     */   }
/*     */ 
/*     */   protected String getHeadNoun(QuestionSentence qsent, Word qword, Word verb)
/*     */   {
/* 251 */     Word headNoun = null;
/* 252 */     if ((qword != null) && (" what which who ".indexOf(qword.getContent().toLowerCase()) >= 0)) {
/* 253 */       if ((verb != null) && (verb.getPosInSentence() > qword.getPosInSentence() + 1)) {
/* 254 */         Word cur = qword.next;
/* 255 */         while ((!cur.getContent().equals("'")) && (cur.getPosInSentence() < verb.getPosInSentence()))
/* 256 */           cur = cur.next;
/* 257 */         headNoun = cur.prev;
/*     */       }
/*     */       else {
/* 260 */         headNoun = qsent.getHeadNoun(qword);
/* 261 */       }if ((headNoun != null) && (verb != null) && (!QuestionSentence.isVerbBe(verb.getContent())) && (headNoun.getPosInSentence() > verb.getPosInSentence()))
/* 262 */         headNoun = null;
/* 263 */       if ((headNoun != null) && 
/* 264 */         (" name type kind ".indexOf(" " + headNoun.getContent().toLowerCase() + " ") >= 0)) {
/* 265 */         headNoun = qsent.getPostModifierNoun(headNoun);
/*     */       }
/*     */     }
/*     */ 
/* 269 */     if (headNoun == null) {
/* 270 */       return null;
/*     */     }
/* 272 */     return headNoun.getContent().toLowerCase();
/*     */   }
/*     */ 
/*     */   protected void classifyAnswerType(QuestionQuery query, Sentence sent)
/*     */   {
/* 278 */     if (("when".equalsIgnoreCase(query.getQuestionWord())) || (
/* 279 */       ("how".equalsIgnoreCase(query.getQuestionWord())) && (query.getQuestionBigram() != null))) {
/* 280 */       query.setAnswerType(1);
/*     */     } else {
/* 282 */       String label = this.classifier.classify(sent);
/* 283 */       if (label.equalsIgnoreCase("NUM"))
/* 284 */         query.setAnswerType(1);
/* 285 */       else if (label.equalsIgnoreCase("ENTY"))
/* 286 */         query.setAnswerType(0);
/* 287 */       else if (label.equalsIgnoreCase("ABBR"))
/* 288 */         query.setAnswerType(4);
/* 289 */       else if (label.equalsIgnoreCase("DESC")) {
/* 290 */         query.setAnswerType(3);
/*     */       }
/* 292 */       if (query.getHeadNoun() != null)
/* 293 */         if ((query.getHeadNoun().equalsIgnoreCase("email")) || (query.getHeadNoun().equalsIgnoreCase("e-mail"))) {
/* 294 */           query.setAnswerType(2);
/* 295 */           query.setSubAnswerType(1);
/*     */         }
/* 297 */         else if ((query.getHeadNoun().equalsIgnoreCase("website")) || (query.getHeadNoun().equalsIgnoreCase("page")) || 
/* 298 */           (query.getHeadNoun().equalsIgnoreCase("homepage"))) {
/* 299 */           query.setAnswerType(2);
/* 300 */           query.setSubAnswerType(0);
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void classifyNumType(QuestionQuery query, Sentence sent)
/*     */   {
/* 311 */     if (query.getQuestionWord() == null) {
/* 312 */       return;
/*     */     }
/* 314 */     SimpleDictionary dict = new SimpleDictionary();
/* 315 */     Word cur = sent.getFirstWord();
/* 316 */     while (cur != null) {
/* 317 */       dict.add(cur.getContent());
/* 318 */       cur = cur.next;
/*     */     }
/*     */ 
/* 321 */     query.setSubAnswerType(12);
/* 322 */     String qword = query.getQuestionWord();
/* 323 */     String qbigram = query.getQuestionBigram();
/* 324 */     String headnoun = query.getHeadNoun();
/*     */ 
/* 326 */     if (qword.equals("when")) {
/* 327 */       query.setSubAnswerType(0);
/*     */     }
/* 329 */     else if (qword.equals("how")) {
/* 330 */       if (qbigram == null)
/* 331 */         return;
/* 332 */       if (qbigram.equals("many")) {
/* 333 */         query.setSubAnswerType(1);
/* 334 */       } else if ((qbigram.equals("old")) || (qbigram.equals("long"))) {
/* 335 */         query.setSubAnswerType(8);
/* 336 */       } else if (qbigram.equals("hot")) {
/* 337 */         query.setSubAnswerType(9);
/* 338 */       } else if (qbigram.equals("fast")) {
/* 339 */         query.setSubAnswerType(5);
/* 340 */       } else if (" tall deep far ".indexOf(" " + qbigram + " ") >= 0) {
/* 341 */         query.setSubAnswerType(2);
/* 342 */       } else if (qbigram.equals("big")) {
/* 343 */         query.setSubAnswerType(4);
/* 344 */       } else if (qbigram.equals("much")) {
/* 345 */         if (sent.getWordNum() < 3)
/* 346 */           return;
/* 347 */         String word = sent.getWord(2).getContent();
/* 348 */         if (word.equalsIgnoreCase("of"))
/* 349 */           query.setSubAnswerType(7);
/*     */         else
/* 351 */           query.setSubAnswerType(11);
/*     */       }
/*     */     }
/* 354 */     else if (((qword.equals("what")) || (qword.equals("which"))) && 
/* 355 */       (headnoun != null)) {
/* 356 */       if (" season year date day month birthday birthdate century".indexOf(" " + headnoun + " ") >= 0)
/* 357 */         query.setSubAnswerType(0);
/* 358 */       else if (headnoun.equals("weight"))
/* 359 */         query.setSubAnswerType(3);
/* 360 */       else if (headnoun.equals("temperature"))
/* 361 */         query.setSubAnswerType(9);
/* 362 */       else if (headnoun.equals("size"))
/* 363 */         query.setSubAnswerType(4);
/* 364 */       else if (headnoun.equals("speed"))
/* 365 */         query.setSubAnswerType(5);
/* 366 */       else if (" time age period span expectancy ".indexOf(" " + headnoun + " ") >= 0)
/* 367 */         query.setSubAnswerType(8);
/* 368 */       else if (" odds chance rate ratio percent percentage probability fraction ".indexOf(" " + headnoun + " ") >= 0)
/* 369 */         query.setSubAnswerType(7);
/* 370 */       else if (" distance length width height wingspan diameter radius elevation ".indexOf(" " + headnoun + " ") >= 0)
/* 371 */         query.setSubAnswerType(2);
/* 372 */       else if ((headnoun.equalsIgnoreCase("phone")) || (headnoun.equalsIgnoreCase("fax")) || ((headnoun.equalsIgnoreCase("number")) && (
/* 373 */         (dict.exist("telephone")) || (dict.exist("phone")) || (dict.exist("fax")) || (dict.exist("cell"))))) {
/* 374 */         query.setSubAnswerType(10);
/*     */       }
/* 376 */       else if ((headnoun.equals("price")) || (headnoun.equals("cost")))
/* 377 */         query.setSubAnswerType(11);
/* 378 */       else if ((headnoun.equals("population")) || (headnoun.equals("populations")))
/* 379 */         query.setSubAnswerType(1);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean isFunctionWord(Word word)
/*     */   {
/* 385 */     if (word.isPunctuation()) {
/* 386 */       return true;
/*     */     }
/* 388 */     if (word.isWord()) {
/* 389 */       return this.funcDict.exist(word.getContent());
/*     */     }
/*     */ 
/* 392 */     return false;
/*     */   }
/*     */ 
/*     */   protected void moveVerbBe(Sentence sent, Word be)
/*     */   {
/* 400 */     Word insert = sent.getLastWord();
/* 401 */     while ((insert != null) && ((insert.getPOSIndex() == 2) || (insert.getPOSIndex() == 5))) {
/* 402 */       insert = insert.prev;
/*     */     }
/* 404 */     if ((insert == null) || (insert.next == null)) {
/* 405 */       return;
/*     */     }
/* 407 */     insert = insert.next;
/*     */ 
/* 410 */     if (be.prev != null) {
/* 411 */       sent.getLastWord().next = sent.getFirstWord();
/* 412 */       sent.getFirstWord().prev = sent.getLastWord();
/* 413 */       sent.resetBoundary(be, be.prev);
/*     */     }
/*     */ 
/* 418 */     sent.resetBoundary(be.next, sent.getLastWord());
/* 419 */     insert.prev.next = be;
/* 420 */     be.prev = insert.prev;
/* 421 */     be.next = insert;
/* 422 */     insert.prev = be;
/*     */   }
/*     */ 
/*     */   protected void moveVerbDo(Sentence sent, Word verb)
/*     */   {
/* 429 */     if (verb.next == null)
/* 430 */       return;
/* 431 */     Word cur = verb.next;
/* 432 */     while ((cur.next != null) && (cur.getPOSIndex() != 2))
/* 433 */       cur = cur.next;
/* 434 */     if (cur == null)
/*     */       return;
/*     */     Word first;
/* 438 */     if ("you we ".indexOf(verb.next.getContent().toLowerCase()) >= 0) {
/* 439 */        first = verb.next.next;
/* 440 */       if (first.next != null)
/* 441 */         first = first.next;
/*     */     }
/*     */     else {
/* 444 */       first = verb.next;
/*     */     }
/*     */ 
/* 447 */     if (verb.prev != null) {
/* 448 */       if ((sent.getLastWord().getPOSIndex() == 5) || ((cur.next != null) && (cur.next.getPOSIndex() != 5)))
/* 449 */         cur = sent.getLastWord();
/* 450 */       verb.prev.next = cur.next;
/* 451 */       cur.next = sent.getFirstWord();
/* 452 */       sent.getFirstWord().prev = cur;
/* 453 */       if (verb.prev.next == null)
/* 454 */         sent.resetBoundary(first, verb.prev);
/*     */       else
/* 456 */         sent.resetBoundary(first, sent.getLastWord());
/*     */     }
/*     */     else {
/* 459 */       sent.resetBoundary(first, sent.getLastWord());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void adjustVerbForm(Sentence sent, Word verbDo)
/*     */   {
/* 466 */     if ((!verbDo.getContent().equalsIgnoreCase("does")) && (!verbDo.getContent().equalsIgnoreCase("did"))) {
/* 467 */       return;
/*     */     }
/* 469 */     Word verb = null;
/* 470 */     Word cur = verbDo.next;
/* 471 */     while (cur != null) {
/* 472 */       if (cur.getPOSIndex() == 2) {
/* 473 */         if (verb != null) {
/* 474 */           return;
/*     */         }
/* 476 */         verb = cur;
/*     */       }
/* 478 */       cur = cur.next;
/*     */     }
/* 480 */     if (verb == null)
/* 481 */       return;
/* 482 */     String content = verb.getContent();
/* 483 */     if ((!content.equalsIgnoreCase(verb.getLemma())) && (!content.equalsIgnoreCase("found"))) {
/* 484 */       return;
/*     */     }
/* 486 */     if (verbDo.getContent().equalsIgnoreCase("does"))
/* 487 */       verb.setContent(this.verbUtil.getThirdPerson(content));
/* 488 */     else if (verbDo.getContent().equalsIgnoreCase("did"))
/* 489 */       verb.setContent(this.verbUtil.getSimplePast(content));
/* 490 */     verb.setLemma(content);
/*     */   }
/*     */ 
/*     */   protected int reformulateWhereQuestion(Sentence sent, int groupNum)
/*     */   {
/* 496 */     Word cur = sent.getLastWord();
/* 497 */     if (cur.getPOSIndex() == 5)
/* 498 */       return groupNum;
/*     */     Word insert;
/* 501 */     if (cur.getPOSIndex() == 2) {
/* 502 */        insert = new Word("in");
/* 503 */       insert.setPOS("IN", 5);
/* 504 */       cur.next = insert;
/* 505 */       insert.prev = cur;
/* 506 */       sent.resetBoundary(sent.getFirstWord(), insert);
/*     */     }
/*     */     else {
/* 509 */       insert = new Word("located");
/* 510 */       insert.setPOS("VBZ", 2);
/* 511 */       insert.setLemma("locate");
/* 512 */       Word first = sent.getFirstWord();
/* 513 */       Word head = first.next;
/* 514 */       insert.next = null;
/* 515 */       insert.prev = first;
/* 516 */       first.next = insert;
/* 517 */       first.prev = cur;
/* 518 */       cur.next = first;
/* 519 */       sent.resetBoundary(head, insert);
/*     */     }
/* 521 */     insert.setClauseID(groupNum);
/* 522 */     return groupNum + 1;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.query.BasicQueryGenerator
 * JD-Core Version:    0.6.2
 */