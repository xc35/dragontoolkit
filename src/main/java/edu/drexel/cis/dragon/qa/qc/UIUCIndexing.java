/*     */ package edu.drexel.cis.dragon.qa.qc;
/*     */ 
/*     */ import edu.drexel.cis.dragon.config.PhraseExtractAppConfig;
/*     */ import edu.drexel.cis.dragon.ir.index.BasicIndexer;
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.nlp.extract.BasicTokenExtractor;
/*     */ import edu.drexel.cis.dragon.nlp.extract.ConceptExtractor;
/*     */ import edu.drexel.cis.dragon.nlp.extract.EngDocumentParser;
/*     */ import edu.drexel.cis.dragon.nlp.extract.TokenExtractor;
/*     */ import edu.drexel.cis.dragon.nlp.ontology.BasicVocabulary;
/*     */ import edu.drexel.cis.dragon.nlp.tool.BrillTagger;
/*     */ import edu.drexel.cis.dragon.nlp.tool.HeppleTagger;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Tagger;
/*     */ import edu.drexel.cis.dragon.nlp.tool.lemmatiser.EngLemmatiser;
/*     */ import edu.drexel.cis.dragon.nlp.tool.xtract.SimpleXtract;
/*     */ import edu.drexel.cis.dragon.nlp.tool.xtract.WordPairIndexer;
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import edu.drexel.cis.dragon.onlinedb.BasicArticle;
/*     */ import edu.drexel.cis.dragon.onlinedb.BasicCollectionReader;
/*     */ import edu.drexel.cis.dragon.onlinedb.BasicCollectionWriter;
/*     */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*     */ import edu.drexel.cis.dragon.qa.util.QuestionSentence;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class UIUCIndexing
/*     */ {
/*     */   private String wordDelimitor;
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*  24 */     UIUCIndexing indexing = new UIUCIndexing();
/*  25 */     CollectionReader cr = new BasicCollectionReader("qa/dragon/uiuc.collection", "qa/dragon/uiuc.index");
/*  26 */     indexing.index(true, cr, "qa/dragon/semantic");
/*     */   }
/*     */ 
/*     */   public UIUCIndexing()
/*     */   {
/*  33 */     this.wordDelimitor = " \r\n\t_;,?/\"'`:(){}!+[]><=%$#*@&^~|\\";
/*     */   }
/*     */ 
/*     */   public void index(boolean semantic, CollectionReader cr, String indexFolder)
/*     */   {
/*     */     TokenExtractor extractor;
/*  40 */     if (semantic) {
/*  41 */       extractor = new SemanticFeatureExtractor(new BasicVocabulary("qa/uiuc/uiuc.vob"), new BrillTagger(), new EngLemmatiser(false, false));
/*     */     }
/*     */     else
/*  44 */       extractor = new BasicTokenExtractor(new EngLemmatiser(true, false));
/*  45 */     EngDocumentParser parser = new EngDocumentParser(this.wordDelimitor);
/*  46 */     extractor.setDocumentParser(parser);
/*  47 */     index(extractor, cr, indexFolder);
/*     */   }
/*     */ 
/*     */   public void index(ConceptExtractor ce, CollectionReader cr, String indexFolder)
/*     */   {
/*  54 */     BasicIndexer indexer = new BasicIndexer(ce, false, indexFolder);
/*  55 */     indexer.setSectionIndexOption(true, false, false, false, false);
/*  56 */     indexer.initialize();
/*     */     Article article;
/*  57 */     while ((article = cr.getNextArticle()) != null)
/*     */     {
/*  58 */       if (!indexer.indexed(article.getKey())) {
/*  59 */         System.out.println("Indexing #" + article.getKey());
/*  60 */         indexer.index(article);
/*     */       }
/*     */     }
/*  63 */     cr.close();
/*  64 */     indexer.close();
/*     */   }
/*     */ 
/*     */   public void generatePhrases(CollectionReader cr, String workingDir)
/*     */   {
/*  73 */     SimpleXtract xtract = new SimpleXtract(4, workingDir);
/*  74 */     Lemmatiser lemmatiser = new EngLemmatiser(false, false);
/*  75 */     Tagger tagger = new HeppleTagger();
/*  76 */     WordPairIndexer indexer = new SimpleWordPairIndexer(workingDir, 4, tagger, lemmatiser);
/*  77 */     indexer.setDocumentParser(new EngDocumentParser(this.wordDelimitor));
/*  78 */     xtract.index(cr, indexer);
/*  79 */     xtract.extract(new SimpleWordPairExpand(4, workingDir, 0.8D), 1.0D, 8.0D, 1.0D, workingDir + "/phrase.list");
/*  80 */     new PhraseExtractAppConfig().generateVocabulary(workingDir + "/phrase.list", 3, workingDir + "/phrase.vob");
/*     */   }
/*     */ 
/*     */   public void generateHeadNounReport(String questReportFile, String headNounRptFile)
/*     */   {
/*     */     try
/*     */     {
/*  92 */       BufferedWriter bw = FileUtil.getTextWriter(headNounRptFile);
/*  93 */       BufferedReader br = FileUtil.getTextReader(questReportFile);
/*  94 */       br.readLine();
/*  95 */       SortedArray classList = new SortedArray();
/*     */       String line;
/*  96 */       while ((line = br.readLine()) != null)
/*     */       {
/*  97 */         String[] arrField = line.split("\t");
/*  98 */         if (arrField[1].equalsIgnoreCase("yes"))
/*     */         {
/* 100 */           if ("what which".indexOf(arrField[5].toLowerCase()) >= 0)
/*     */           {
/* 102 */             String label = arrField[4];
/*     */             String headNoun;
/* 103 */             if (arrField.length >= 8) {
/* 104 */                headNoun = arrField[7];
/* 105 */               if (arrField.length >= 9)
/* 106 */                 headNoun = headNoun + " " + arrField[8];
/*     */             }
/*     */             else {
/* 109 */               headNoun = null;
/* 110 */             }if ((headNoun != null) && (headNoun.trim().length() != 0))
/*     */             {
/* 113 */               Token curCategory = new Token(label);
/* 114 */               if (classList.add(curCategory)) {
/* 115 */                 SortedArray list = new SortedArray();
/* 116 */                 list.add(new Token(headNoun));
/* 117 */                 curCategory.setMemo(list);
/*     */               }
/*     */               else {
/* 120 */                 curCategory = (Token)classList.get(classList.insertedPos());
/* 121 */                 SortedArray list = (SortedArray)curCategory.getMemo();
/* 122 */                 Token curNoun = new Token(headNoun);
/* 123 */                 if (!list.add(curNoun))
/* 124 */                   ((Token)list.get(list.insertedPos())).addFrequency(1);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 130 */       for (int i = 0; i < classList.size(); i++) {
/* 131 */         Token curCategory = (Token)classList.get(i);
/* 132 */         SortedArray list = (SortedArray)curCategory.getMemo();
/* 133 */         bw.write("<category name=\"" + curCategory.getValue() + "\">\n");
/* 134 */         for (int j = 0; j < list.size(); j++) {
/* 135 */           Token curNoun = (Token)list.get(j);
/* 136 */           bw.write(curNoun.getValue() + "\t" + curNoun.getFrequency() + "\n");
/*     */         }
/* 138 */         bw.write("</category>\n");
/* 139 */         list.clear();
/*     */       }
/* 141 */       bw.close();
/*     */     }
/*     */     catch (Exception e) {
/* 144 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void prepareCollection(String trainingFile, String testingFile, String outputFolder, String collectionName)
/*     */   {
/*     */     try
/*     */     {
/* 165 */       BasicArticle article = new BasicArticle();
/* 166 */       BasicCollectionWriter bcw = new BasicCollectionWriter(outputFolder + "/" + collectionName + ".collection", outputFolder + "/" + collectionName + ".index", false);
/* 167 */       BufferedWriter xls = FileUtil.getTextWriter(outputFolder + "/" + collectionName + ".xls");
/* 168 */       xls.write("id\ttraining\tcontent\tcoarse label\tfine label\tqword\tbigram qword\theadnoun\tpost noun\r\n");
/* 169 */       Tagger tagger = new HeppleTagger();
/* 170 */       EngDocumentParser parser = new EngDocumentParser(this.wordDelimitor);
/* 171 */       Lemmatiser lemmatiser = new EngLemmatiser(false, false);
/*     */ 
/* 174 */       int count = 0;
/* 175 */       ArrayList coarseList = new ArrayList();
/* 176 */       ArrayList fineList = new ArrayList();
/* 177 */       BufferedReader br = FileUtil.getTextReader(trainingFile);
/*     */       String line;
/* 178 */       while ((line = br.readLine()) != null)
/*     */       {
/* 179 */         count++;
/* 180 */         article.setKey("q" + count);
/* 181 */         int pos = line.indexOf(' ');
/* 182 */         article.setBody(line.substring(pos + 1));
/* 183 */         bcw.add(article);
/*     */ 
/* 185 */         System.out.println("Processing #" + article.getKey());
/* 186 */         Sentence sent = parser.parseSentence(article.getBody());
/* 187 */         tagger.tag(sent);
/* 188 */         QuestionSentence qsent = new QuestionSentence(sent);
/* 189 */         Word qword = qsent.getQuestionWord();
/*     */         String questWord;
/* 190 */         if (qword != null)
/* 191 */           questWord = qword.getContent();
/*     */         else
/* 193 */           questWord = "unknown";
/*     */         String bigramQuestWord;
/* 194 */         if (qsent.isBigramQuestionWord(qword))
/* 195 */           bigramQuestWord = qword.getContent() + " " + qword.next.getContent();
/*     */         else
/* 197 */           bigramQuestWord = "";
/*     */         Word hword;
/* 198 */         if ((qword == null) || (" what which ".indexOf(qword.getContent().toLowerCase()) >= 0))
/* 199 */           hword = qsent.getHeadNoun(qword);
/*     */         else
/* 201 */           hword = null;
/*     */         String headNoun;
/* 202 */         if (hword != null)
/* 203 */           headNoun = lemmatiser.lemmatize(hword.getContent(), hword.getPOSIndex());
/*     */         else
/* 205 */           headNoun = "";
/* 206 */          hword = qsent.getPostModifierNoun(hword);
/*     */         String postHeadNoun;
/* 207 */         if (hword != null)
/* 208 */           postHeadNoun = lemmatiser.lemmatize(hword.getContent(), hword.getPOSIndex());
/*     */         else {
/* 210 */           postHeadNoun = "";
/*     */         }
/* 212 */         String fineCategory = line.substring(0, pos);
/* 213 */         Question curQuest = new Question(article.getKey());
/* 214 */         curQuest.setCategory(fineCategory);
/* 215 */         curQuest.setQuestionWord(questWord);
/* 216 */         fineList.add(curQuest);
/*     */ 
/* 218 */         pos = fineCategory.indexOf(':');
/* 219 */         String coarseCategory = fineCategory.substring(0, pos);
/* 220 */         curQuest = new Question(article.getKey());
/* 221 */         curQuest.setCategory(coarseCategory);
/* 222 */         curQuest.setQuestionWord(questWord);
/* 223 */         coarseList.add(curQuest);
/*     */ 
/* 225 */         xls.write(count + "\tyes\t" + article.getBody() + "\t" + coarseCategory + "\t" + fineCategory + "\t" + questWord + "\t");
/* 226 */         xls.write(bigramQuestWord + "\t" + headNoun + "\t" + postHeadNoun + "\r\n");
/*     */       }
/*     */ 
/* 229 */       BufferedWriter bw = FileUtil.getTextWriter(outputFolder + "/experiment/answerkey_training_fine.list");
/* 230 */       bw.write(fineList.size() + "\n");
/* 231 */       for (int i = 0; i < fineList.size(); i++) {
/* 232 */         Question curQuest = (Question)fineList.get(i);
/* 233 */         bw.write(curQuest.getCategory());
/* 234 */         bw.write(9);
/* 235 */         bw.write(curQuest.getQuestionKey());
/* 236 */         bw.write(9);
/* 237 */         bw.write(curQuest.getQuestionWord());
/* 238 */         bw.write(10);
/*     */       }
/* 240 */       fineList.clear();
/* 241 */       bw.close();
/*     */ 
/* 243 */       bw = FileUtil.getTextWriter(outputFolder + "/experiment/answerkey_training_coarse.list");
/* 244 */       bw.write(coarseList.size() + "\n");
/* 245 */       for (int i = 0; i < coarseList.size(); i++) {
/* 246 */         Question curQuest = (Question)coarseList.get(i);
/* 247 */         bw.write(curQuest.getCategory());
/* 248 */         bw.write(9);
/* 249 */         bw.write(curQuest.getQuestionKey());
/* 250 */         bw.write(9);
/* 251 */         bw.write(curQuest.getQuestionWord());
/* 252 */         bw.write(10);
/*     */       }
/* 254 */       coarseList.clear();
/* 255 */       bw.close();
/*     */ 
/* 258 */       br = FileUtil.getTextReader(testingFile);
/* 259 */       while ((line = br.readLine()) != null) {
/* 260 */         count++;
/* 261 */         article.setKey("q" + count);
/* 262 */         int pos = line.indexOf(' ');
/* 263 */         article.setBody(line.substring(pos + 1));
/* 264 */         bcw.add(article);
/*     */ 
/* 266 */         System.out.println("Processing #" + article.getKey());
/* 267 */         Sentence sent = parser.parseSentence(article.getBody());
/* 268 */         tagger.tag(sent);
/* 269 */         QuestionSentence qsent = new QuestionSentence(sent);
/* 270 */         Word qword = qsent.getQuestionWord();
/*     */         String questWord;
/* 271 */         if (qword != null)
/* 272 */           questWord = qword.getContent();
/*     */         else
/* 274 */           questWord = "unknown";
/*     */         String bigramQuestWord;
/* 275 */         if (qsent.isBigramQuestionWord(qword))
/* 276 */           bigramQuestWord = qword.getContent() + " " + qword.next.getContent();
/*     */         else
/* 278 */           bigramQuestWord = "";
/*     */         Word hword;
/* 279 */         if ((qword == null) || (" what which ".indexOf(qword.getContent().toLowerCase()) >= 0))
/* 280 */           hword = qsent.getHeadNoun(qword);
/*     */         else
/* 282 */           hword = null;
/*     */         String headNoun;
/* 283 */         if (hword != null)
/* 284 */           headNoun = lemmatiser.lemmatize(hword.getContent(), hword.getPOSIndex());
/*     */         else
/* 286 */           headNoun = "";
/* 287 */          hword = qsent.getPostModifierNoun(hword);
/*     */         String postHeadNoun;
/* 288 */         if (hword != null)
/* 289 */           postHeadNoun = lemmatiser.lemmatize(hword.getContent(), hword.getPOSIndex());
/*     */         else {
/* 291 */           postHeadNoun = "";
/*     */         }
/* 293 */         String fineCategory = line.substring(0, pos);
/* 294 */         Question curQuest = new Question(article.getKey());
/* 295 */         curQuest.setCategory(fineCategory);
/* 296 */         curQuest.setQuestionWord(questWord);
/* 297 */         fineList.add(curQuest);
/*     */ 
/* 299 */         pos = fineCategory.indexOf(':');
/* 300 */         String coarseCategory = fineCategory.substring(0, pos);
/* 301 */         curQuest = new Question(article.getKey());
/* 302 */         curQuest.setCategory(coarseCategory);
/* 303 */         curQuest.setQuestionWord(questWord);
/* 304 */         coarseList.add(curQuest);
/*     */ 
/* 306 */         xls.write(count + "\tno\t" + article.getBody() + "\t" + coarseCategory + "\t" + fineCategory + "\t" + questWord + "\t");
/* 307 */         xls.write(bigramQuestWord + "\t" + headNoun + "\t" + postHeadNoun + "\r\n");
/*     */       }
/*     */ 
/* 310 */       bw = FileUtil.getTextWriter(outputFolder + "/experiment/answerkey_testing_fine.list");
/* 311 */       bw.write(fineList.size() + "\n");
/* 312 */       for (int i = 0; i < fineList.size(); i++) {
/* 313 */         Question curQuest = (Question)fineList.get(i);
/* 314 */         bw.write(curQuest.getCategory());
/* 315 */         bw.write(9);
/* 316 */         bw.write(curQuest.getQuestionKey());
/* 317 */         bw.write(9);
/* 318 */         bw.write(curQuest.getQuestionWord());
/* 319 */         bw.write(10);
/*     */       }
/* 321 */       fineList.clear();
/* 322 */       bw.close();
/*     */ 
/* 324 */       bw = FileUtil.getTextWriter(outputFolder + "/experiment/answerkey_testing_coarse.list");
/* 325 */       bw.write(coarseList.size() + "\n");
/* 326 */       for (int i = 0; i < coarseList.size(); i++) {
/* 327 */         Question curQuest = (Question)coarseList.get(i);
/* 328 */         bw.write(curQuest.getCategory());
/* 329 */         bw.write(9);
/* 330 */         bw.write(curQuest.getQuestionKey());
/* 331 */         bw.write(9);
/* 332 */         bw.write(curQuest.getQuestionWord());
/* 333 */         bw.write(10);
/*     */       }
/* 335 */       coarseList.clear();
/* 336 */       bw.close();
/* 337 */       bcw.close();
/* 338 */       xls.close();
/*     */     }
/*     */     catch (Exception e) {
/* 341 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.qc.UIUCIndexing
 * JD-Core Version:    0.6.2
 */