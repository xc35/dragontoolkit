/*     */ package edu.drexel.cis.dragon.cptc;
/*     */ 
/*     */ import edu.drexel.cis.dragon.config.ClassificationEvaAppConfig;
/*     */ import edu.drexel.cis.dragon.ir.classification.DocClass;
/*     */ import edu.drexel.cis.dragon.ir.classification.DocClassSet;
/*     */ import edu.drexel.cis.dragon.ir.classification.SVMLightClassifier;
/*     */ import edu.drexel.cis.dragon.ir.classification.featureselection.DocFrequencySelector;
/*     */ import edu.drexel.cis.dragon.ir.classification.multiclass.HingeLoss;
/*     */ import edu.drexel.cis.dragon.ir.classification.multiclass.LossMultiClassDecoder;
/*     */ import edu.drexel.cis.dragon.ir.classification.multiclass.OVACodeMatrix;
/*     */ import edu.drexel.cis.dragon.ir.clustering.featurefilter.DocFrequencyFilter;
/*     */ import edu.drexel.cis.dragon.ir.clustering.featurefilter.FeatureFilter;
/*     */ import edu.drexel.cis.dragon.ir.index.BasicIndexReader;
/*     */ import edu.drexel.cis.dragon.ir.index.BasicIndexer;
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleRow;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSparseMatrix;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElement;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.compare.FrequencyComparator;
/*     */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*     */ import edu.drexel.cis.dragon.nlp.extract.BasicConceptFilter;
/*     */ import edu.drexel.cis.dragon.nlp.extract.BasicTokenExtractor;
/*     */ import edu.drexel.cis.dragon.nlp.extract.ConceptExtractor;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*     */ import edu.drexel.cis.dragon.nlp.tool.lemmatiser.EngLemmatiser;
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*     */ import edu.drexel.cis.dragon.onlinedb.CollectionWriter;
/*     */ import edu.drexel.cis.dragon.onlinedb.searchengine.GoogleEngine;
/*     */ import edu.drexel.cis.dragon.util.EnvVariable;
/*     */ import edu.drexel.cis.dragon.util.FastBinaryWriter;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class Controller
/*     */ {
/*     */   private ClassifierDesc io;
/*     */   private boolean useSelection;
/*     */   private boolean ranking;
/*     */   private boolean summaryOnly;
/*     */   private int minDocFrequency;
/*     */   private int top;
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*     */     String folder;
/*  34 */     if ((args != null) && (args.length > 0))
/*  35 */       folder = "cptc/" + args[0];
/*     */     else
/*  37 */       folder = "cptc/plo";
/*  38 */     ClassifierDesc io = new ClassifierDesc(folder);
/*  39 */     Controller controller = new Controller(io);
/*     */ 
/*  43 */     controller.buildConceptMatrix();
/*     */ 
/*  45 */     controller.trainModel();
/*     */   }
/*     */ 
/*     */   public Controller(ClassifierDesc io) {
/*  49 */     this.io = io;
/*  50 */     EnvVariable.setCharSet("UTF-8");
/*  51 */     this.useSelection = false;
/*  52 */     this.ranking = true;
/*  53 */     this.summaryOnly = true;
/*  54 */     this.minDocFrequency = 10;
/*  55 */     this.top = 20;
/*     */   }
/*     */ 
/*     */   public void download() {
/*  59 */     download(null);
/*     */   }
/*     */ 
/*     */   public void download(String site)
/*     */   {
/*  70 */     CollectionWriter writer = null;
/*     */     try
/*     */     {
/*  73 */       GoogleEngine google = new GoogleEngine();
/*  74 */       google.setSummaryOnlyOption(this.summaryOnly);
/*  75 */       google.setSiteRestriction(site);
/*  76 */       writer = this.io.getCollectionWriter();
/*     */ 
/*  78 */       for (int i = 0; i < this.io.getClassNum(); i++) {
/*  79 */         ClassDesc curClass = this.io.getClass(i);
/*  80 */         for (int j = 0; j < curClass.getConceptNum(); j++) {
/*  81 */           ConceptDesc curConcept = curClass.getConcept(j);
/*  82 */           if (!curConcept.isDoneDownload())
/*     */           {
/*  86 */             System.out.println("Downloading articles for concept: " + curConcept.getConcept());
/*  87 */             google.setSearchTerm(curConcept.getConcept());
/*  88 */             google.initQuery();
/*  89 */             int count = 0;
/*     */             Article curArticle;
/*  90 */             while ((count < this.top) && ((curArticle = google.getNextArticle()) != null))
/*     */             {
/*  91 */               if (curArticle.getBody() != null)
/*     */               {
/*  93 */                 count++;
/*  94 */                 System.out.println(new Date().toString() + " " + curArticle.getKey());
/*  95 */                 curArticle.setKey(curArticle.getKey().toLowerCase());
/*  96 */                 writer.add(curArticle);
/*  97 */                 curConcept.addArticle(curArticle.getKey(), count);
/*     */               }
/*     */             }
/*  99 */             curConcept.setDoneDownloadFlag(true);
/* 100 */             if (this.summaryOnly)
/* 101 */               GoogleEngine.sleepManySeconds(2L);
/*     */           }
/*     */         }
/*     */       }
/* 105 */       google.close();
/* 106 */       writer.close();
/* 107 */       this.io.save();
/*     */     }
/*     */     catch (Exception e) {
/* 110 */       if (writer != null)
/* 111 */         writer.close();
/* 112 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void generateCollection2()
/*     */   {
/* 129 */     CollectionWriter writer = this.io.getCollectionWriter2();
/* 130 */     CollectionReader reader = this.io.getCollectionReader();
/* 131 */     IndexReader indexReader = getIndexReader(this.io.getIndexFolder());
/* 132 */     Lemmatiser lemmatiser = new EngLemmatiser(true, false);
/* 133 */     ConceptExtractor extractor = new BasicTokenExtractor(null);
/* 134 */     ContentSelector selector = new ContentSelector(lemmatiser, 40, 200, 200);
/*     */ 
/* 136 */     for (int i = 0; i < this.io.getClassNum(); i++) {
/* 137 */       ClassDesc curClass = this.io.getClass(i);
/* 138 */       for (int j = 0; j < curClass.getConceptNum(); j++) {
/* 139 */         ConceptDesc curConcept = curClass.getConcept(j);
/* 140 */         System.out.println(new Date().toString() + " " + curConcept.getConcept());
/* 141 */         String[] arrWord = getRankedWords(curConcept.getConcept(), indexReader, extractor, lemmatiser);
/* 142 */         for (int k = 0; k < curConcept.getArticleNum(); k++) {
/* 143 */           String articleKey = curConcept.getArticleKey(k);
/* 144 */           Article article = reader.getArticleByKey(articleKey);
/* 145 */           if (article != null)
/*     */           {
/* 147 */             article.setKey(article.getKey() + "_" + curConcept.getConcept());
/* 148 */             if (arrWord.length == 1)
/* 149 */               article.setBody(selector.select(article.getBody(), arrWord[0]));
/* 150 */             else if (arrWord.length == 2)
/* 151 */               article.setBody(selector.select(article.getBody(), arrWord[0], arrWord[1]));
/* 152 */             writer.add(article);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 156 */     writer.close();
/* 157 */     reader.close();
/* 158 */     indexReader.close();
/*     */   }
/*     */ 
/*     */   public void index()
/*     */   {
/*     */     try
/*     */     {
/* 173 */       Lemmatiser lemmatiser = new EngLemmatiser(true, true);
/* 174 */       ContextualFeatureExtractor extractor = new ContextualFeatureExtractor(lemmatiser);
/* 175 */       extractor.setFeatureOption(true, true);
/* 176 */       extractor.setConceptFilter(new BasicConceptFilter(EnvVariable.getDragonHome() + "/nlpdata/exp/rijsbergen.stopword"));
/* 177 */       extractor.setFilteringOption(true);
/*     */       CollectionReader reader;
/*     */       BasicIndexer indexer;
/* 178 */       if (this.useSelection) {
/* 179 */         new File(this.io.getIndexFolder2()).delete();
/* 180 */         indexer = new BasicIndexer(extractor, false, this.io.getIndexFolder2());
/* 181 */         reader = this.io.getCollectionReader2();
/*     */       }
/*     */       else {
/* 184 */         new File(this.io.getIndexFolder()).delete();
/* 185 */         indexer = new BasicIndexer(extractor, false, this.io.getIndexFolder());
/* 186 */         reader = this.io.getCollectionReader();
/*     */       }
/* 188 */       indexer.setSectionIndexOption(true, false, false, false, false);
/* 189 */       indexer.initialize();
/*     */ 
/* 191 */       for (int i = 0; i < this.io.getClassNum(); i++) {
/* 192 */         ClassDesc curClass = this.io.getClass(i);
/* 193 */         for (int j = 0; j < curClass.getConceptNum(); j++) {
/* 194 */           ConceptDesc curConcept = curClass.getConcept(j);
/* 195 */           extractor.setSearchTerm(curConcept.getConcept());
/* 196 */           System.out.println("Indexing articles for concept: " + curConcept.getConcept());
/*     */ 
/* 198 */           for (int k = 0; k < curConcept.getArticleNum(); k++) {
/* 199 */             String articleKey = curConcept.getArticleKey(k);
/* 200 */             if (this.useSelection)
/* 201 */               articleKey = articleKey + "_" + curConcept.getConcept();
/* 202 */             if (!indexer.indexed(articleKey))
/*     */             {
/* 204 */               Article article = reader.getArticleByKey(articleKey);
/* 205 */               if (article == null) {
/* 206 */                 System.out.println(articleKey + " null pointer");
/*     */               }
/*     */               else {
/* 209 */                 if ((this.summaryOnly) && (article.getBody() != null))
/* 210 */                   article.setBody(article.getBody().replaceAll("\\.\\.\\.", "\n\n"));
/* 211 */                 System.out.println(new Date().toString() + " " + article.getKey());
/* 212 */                 indexer.index(article);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 216 */       indexer.close();
/*     */     }
/*     */     catch (Exception e) {
/* 219 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void buildConceptMatrix()
/*     */   {
/*     */     try
/*     */     {
/* 240 */       this.io.cleanConceptFiles();
/*     */ 
/* 242 */       SimpleElementList wordKeyList = this.io.getWordList(true);
/* 243 */       SimpleElementList conceptKeyList = this.io.getConceptList(true);
/* 244 */       DoubleSparseMatrix matrix = this.io.getConceptMatrix(true);
/* 245 */       BufferedWriter bw = FileUtil.getTextWriter(this.io.getAnswerKeyFilename());
/*     */       IndexReader indexReader;
/* 246 */       if (this.useSelection)
/* 247 */         indexReader = getIndexReader(this.io.getIndexFolder2());
/*     */       else
/* 249 */         indexReader = getIndexReader(this.io.getIndexFolder());
/* 250 */       FeatureFilter filter = new DocFrequencyFilter(this.minDocFrequency);
/* 251 */       filter.initialize(indexReader, null);
/* 252 */       ConceptVectorGenerator generator = new ConceptVectorGenerator(indexReader, filter);
/* 253 */       generator.setTop(this.top);
/* 254 */       generator.setUseRanking(this.ranking);
/*     */ 
/* 256 */       int total = 0;
/* 257 */       for (int i = 0; i < this.io.getClassNum(); i++) {
/* 258 */         total += this.io.getClass(i).getConceptNum();
/*     */       }
/* 260 */       bw.write(total + "\n");
/*     */ 
/* 262 */       int count = 0;
/* 263 */       for (int i = 0; i < this.io.getClassNum(); i++) {
/* 264 */         ClassDesc curClass = this.io.getClass(i);
/* 265 */         for (int j = 0; j < curClass.getConceptNum(); j++) {
/* 266 */           ConceptDesc curConcept = curClass.getConcept(j);
/* 267 */           DoubleRow row = generator.generateVector(curConcept);
/* 268 */           for (int k = 0; k < row.getNonZeroNum(); k++)
/* 269 */             matrix.add(count, row.getNonZeroColumn(k), row.getNonZeroDoubleScore(k));
/* 270 */           conceptKeyList.add(new SimpleElement(curConcept.getConcept(), count));
/* 271 */           bw.write(curClass.getClassName() + "\t" + curConcept.getConcept() + "\n");
/* 272 */           count++;
/*     */         }
/*     */       }
/* 275 */       matrix.finalizeData();
/* 276 */       matrix.close();
/* 277 */       bw.close();
/* 278 */       conceptKeyList.close();
/*     */ 
/* 281 */       SortedArray wordList = new SortedArray(new IndexComparator());
/* 282 */       total = indexReader.getCollection().getTermNum();
/* 283 */       count = indexReader.getCollection().getDocNum();
/* 284 */       for (int i = 0; i < total; i++)
/* 285 */         if (filter.map(i) >= 0) {
/* 286 */           wordKeyList.add(new SimpleElement(indexReader.getTermKey(i), filter.map(i)));
/* 287 */           Token curToken = new Token(null);
/* 288 */           curToken.setIndex(filter.map(i));
/* 289 */           curToken.setWeight(Math.log(count / indexReader.getIRTerm(i).getDocFrequency()));
/* 290 */           wordList.add(curToken);
/*     */         }
/* 292 */       wordKeyList.close();
/*     */ 
/* 294 */       FastBinaryWriter fbw = new FastBinaryWriter(this.io.getWordIDFFilename());
/* 295 */       fbw.writeInt(wordList.size());
/* 296 */       for (int i = 0; i < wordList.size(); i++) {
/* 297 */         Token curToken = (Token)wordList.get(i);
/* 298 */         fbw.writeDouble(curToken.getWeight());
/*     */       }
/* 300 */       fbw.close();
/*     */ 
/* 302 */       indexReader.close();
/*     */     }
/*     */     catch (Exception e) {
/* 305 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void evaluate(String runName) {
/* 310 */     evaluate(this.io.getEvaluationResultFile(), runName);
/*     */   }
/*     */ 
/*     */   public void evaluate(String outputFile, String runName)
/*     */   {
/* 318 */     SVMLightClassifier classifier = new SVMLightClassifier(this.io.getConceptMatrix(false));
/* 319 */     classifier.setFeatureSelector(new DocFrequencySelector(this.minDocFrequency));
/* 320 */     classifier.setCodeMatrix(new OVACodeMatrix(this.io.getClassNum()));
/* 321 */     classifier.setMultiClassDecoder(new LossMultiClassDecoder(new HingeLoss()));
/*     */ 
/* 323 */     ClassificationEvaAppConfig config = new ClassificationEvaAppConfig();
/* 324 */     config.evaluateCrossValidation(classifier, this.io.getConceptList(false), this.io.getClassNum(), this.io.getAnswerKeyFilename(), null, 10, runName, outputFile);
/*     */   }
/*     */ 
/*     */   public void trainModel()
/*     */   {
/* 337 */     SVMLightClassifier classifier = new SVMLightClassifier(this.io.getConceptMatrix(false));
/* 338 */     classifier.setFeatureSelector(new DocFrequencySelector(this.minDocFrequency));
/* 339 */     classifier.setCodeMatrix(new OVACodeMatrix(this.io.getClassNum()));
/* 340 */     classifier.setMultiClassDecoder(new LossMultiClassDecoder(new HingeLoss()));
/* 341 */     SimpleElementList conceptList = this.io.getConceptList(false);
/* 342 */     DocClassSet set = new DocClassSet(this.io.getClassNum());
/* 343 */     for (int i = 0; i < this.io.getClassNum(); i++) {
/* 344 */       ClassDesc curClass = this.io.getClass(i);
/* 345 */       set.getDocClass(i).setClassName(curClass.getClassName());
/* 346 */       for (int j = 0; j < curClass.getConceptNum(); j++) {
/* 347 */         ConceptDesc curConcept = curClass.getConcept(j);
/* 348 */         IRDoc curDoc = new IRDoc(curConcept.getConcept());
/* 349 */         curDoc.setIndex(conceptList.search(curConcept.getConcept()));
/* 350 */         if (curDoc.getIndex() == -1) {
/* 351 */           System.out.println("");
/*     */         }
/* 353 */         set.addDoc(i, curDoc);
/*     */       }
/*     */     }
/*     */ 
/* 357 */     classifier.train(set);
/* 358 */     classifier.saveModel(this.io.getModelFilename());
/*     */   }
/*     */ 
/*     */   private IndexReader getIndexReader(String indexFolder)
/*     */   {
/* 364 */     BasicIndexReader indexReader = new BasicIndexReader(indexFolder + "/all", false);
/* 365 */     indexReader.initialize();
/* 366 */     indexReader.setIRDocKeyList(new SimpleElementList(indexFolder + "/dockey.list", false));
/* 367 */     indexReader.setIRTermKeyList(new SimpleElementList(indexFolder + "/termkey.list", false));
/* 368 */     return indexReader;
/*     */   }
/*     */ 
/*     */   private String[] getRankedWords(String concept, IndexReader reader, ConceptExtractor extractor, Lemmatiser lemmatiser)
/*     */   {
/* 379 */     ArrayList wordList = extractor.extractFromDoc(concept);
/*     */ 
/* 381 */     SortedArray list = new SortedArray();
/* 382 */     for (int i = 0; i < wordList.size(); i++) {
/* 383 */       Token cur = (Token)wordList.get(i);
/* 384 */       IRTerm term = reader.getIRTerm(lemmatiser.lemmatize(cur.getValue()));
/* 385 */       if (term != null) {
/* 386 */         cur.setFrequency(term.getDocFrequency());
/* 387 */         list.add(cur);
/*     */       }
/*     */     }
/* 390 */     list.setComparator(new FrequencyComparator(false));
/* 391 */     String[] arrWords = new String[Math.min(2, list.size())];
/* 392 */     for (int i = 0; i < arrWords.length; i++)
/* 393 */       arrWords[i] = ((Token)list.get(i)).getValue();
/* 394 */     return arrWords;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.cptc.Controller
 * JD-Core Version:    0.6.2
 */