/*     */ package edu.drexel.cis.dragon.qa.system;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Term;
/*     */ import edu.drexel.cis.dragon.nlp.extract.EngDocumentParser;
/*     */ import edu.drexel.cis.dragon.nlp.tool.BrillTagger;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Tagger;
/*     */ import edu.drexel.cis.dragon.nlp.tool.lemmatiser.EngLemmatiser;
/*     */ import edu.drexel.cis.dragon.onlinedb.ArrayCollectionReader;
/*     */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*     */ import edu.drexel.cis.dragon.onlinedb.searchengine.GoogleEngine;
/*     */ import edu.drexel.cis.dragon.qa.expand.BasicEntityExpander;
/*     */ import edu.drexel.cis.dragon.qa.expand.CandidateExpander;
/*     */ import edu.drexel.cis.dragon.qa.expand.NullExpander;
/*     */ import edu.drexel.cis.dragon.qa.extract.BasicEntityFinder;
/*     */ import edu.drexel.cis.dragon.qa.extract.BasicNumberFinder;
/*     */ import edu.drexel.cis.dragon.qa.extract.BasicWebFinder;
/*     */ import edu.drexel.cis.dragon.qa.extract.CandidateFinder;
/*     */ import edu.drexel.cis.dragon.qa.filter.BasicEntityFilter;
/*     */ import edu.drexel.cis.dragon.qa.filter.BasicNumberFilter;
/*     */ import edu.drexel.cis.dragon.qa.filter.BasicWebFilter;
/*     */ import edu.drexel.cis.dragon.qa.filter.CandidateFilter;
/*     */ import edu.drexel.cis.dragon.qa.merge.BasicEntityMerger;
/*     */ import edu.drexel.cis.dragon.qa.merge.BasicNumberMerger;
/*     */ import edu.drexel.cis.dragon.qa.merge.BasicWebMerger;
/*     */ import edu.drexel.cis.dragon.qa.merge.CandidateMerger;
/*     */ import edu.drexel.cis.dragon.qa.qc.QAClassifier;
/*     */ import edu.drexel.cis.dragon.qa.query.BasicQueryGenerator;
/*     */ import edu.drexel.cis.dragon.qa.query.GroupScorer;
/*     */ import edu.drexel.cis.dragon.qa.query.QueryGenerator;
/*     */ import edu.drexel.cis.dragon.qa.query.QueryScorer;
/*     */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*     */ import edu.drexel.cis.dragon.qa.rank.CandidateRanker;
/*     */ import edu.drexel.cis.dragon.qa.rank.ScorerRanker;
/*     */ import edu.drexel.cis.dragon.qa.util.UnitUtil;
/*     */ import edu.drexel.cis.dragon.qa.util.VerbUtil;
/*     */ import edu.drexel.cis.dragon.util.EnvVariable;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.SimpleDictionary;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class WebQASystem extends AbstractQASystem
/*     */ {
/*     */   private EngDocumentParser[] arrParser;
/*     */   private CandidateFinder[] arrFinder;
/*     */   private CandidateMerger[] arrMerger;
/*     */   private CandidateFilter[] arrFilter;
/*     */   private CandidateRanker[] arrRanker;
/*     */   private CandidateExpander[] arrExpander;
/*     */   private SimpleDictionary allowedfuncDict;
/*     */   private SimpleDictionary funcDict;
/*     */   private SimpleDictionary enNumDict;
/*     */   private SimpleDictionary nouseHeadNounDict;
/*     */   private UnitUtil unitDict;
/*     */   private PrintWriter history;
/*     */   private QAClassifier classifier;
/*     */   private VerbUtil verbUtil;
/*     */   private WebDownloader downloader;
/*     */ 
/*     */   public WebQASystem()
/*     */   {
/*  46 */     this("nlpdata/qa", new BrillTagger(), new EngLemmatiser(false, false));
/*     */   }
/*     */ 
/*     */   public WebQASystem(String workDir) {
/*  50 */     this(workDir, new BrillTagger(), new EngLemmatiser(false, false));
/*     */   }
/*     */ 
/*     */   public WebQASystem(String workDir, Tagger tagger, Lemmatiser lemmatiser)
/*     */   {
/*  56 */     Term.setNameMode(0);
/*  57 */     EnvVariable.setCharSet("UTF-8");
/*  58 */     this.top = 50;
/*  59 */     GoogleEngine google = new GoogleEngine(50);
/*  60 */     google.setRemoveTagOption(true);
/*  61 */     google.setSummaryOnlyOption(true);
/*  62 */     this.funcDict = new SimpleDictionary(workDir + "/functionword.lst");
/*  63 */     this.allowedfuncDict = new SimpleDictionary(workDir + "/allowedfunctionword.lst");
/*  64 */     this.nouseHeadNounDict = new SimpleDictionary(workDir + "/nouseheadnoun.lst");
/*  65 */     this.verbUtil = new VerbUtil(workDir + "/verb.lst", workDir + "/irregularverb.lst");
/*  66 */     this.classifier = new QAClassifier(tagger, lemmatiser, workDir + "/qac_model.bin", workDir + "/qac_term.lst", workDir + "/qac.vob");
/*  67 */     this.history = FileUtil.getPrintWriter(workDir + "/history.txt", true);
/*     */ 
/*  69 */     this.arrFinder = new CandidateFinder[3];
/*  70 */     this.arrFilter = new CandidateFilter[3];
/*  71 */     this.arrMerger = new CandidateMerger[3];
/*  72 */     this.arrParser = new EngDocumentParser[3];
/*  73 */     this.arrRanker = new CandidateRanker[3];
/*  74 */     this.arrExpander = new CandidateExpander[3];
/*  75 */     this.arrParser[0] = new EngDocumentParser(" \r\n\t_;,?/\"`:(){}!+[]><=%$#*@&^~|\\");
/*  76 */     this.arrFilter[0] = new BasicEntityFilter(workDir + "/entity.stopword");
/*  77 */     this.arrFinder[0] = new BasicEntityFinder(tagger, this.funcDict, this.allowedfuncDict);
/*  78 */     this.arrMerger[0] = new BasicEntityMerger(lemmatiser, this.allowedfuncDict);
/*  79 */     this.arrRanker[0] = new ScorerRanker();
/*  80 */     this.arrExpander[0] = new BasicEntityExpander();
/*     */ 
/*  82 */     this.unitDict = new UnitUtil(workDir + "/unit.lst");
/*  83 */     this.enNumDict = new SimpleDictionary(workDir + "/numbers.lst");
/*  84 */     this.arrParser[1] = new EngDocumentParser(" \r\n\t_-;,?/\"`:(){}!+[]><=%$#*@&^~|\\");
/*  85 */     this.arrFilter[1] = new BasicNumberFilter();
/*  86 */     this.arrFinder[1] = new BasicNumberFinder(this.unitDict, this.enNumDict, this.funcDict);
/*  87 */     this.arrMerger[1] = new BasicNumberMerger(this.unitDict, new SimpleDictionary(workDir + "/dates.lst"));
/*  88 */     this.arrRanker[1] = new ScorerRanker();
/*  89 */     this.arrExpander[1] = new NullExpander();
/*     */ 
/*  91 */     this.arrParser[2] = new EngDocumentParser(" \r\n\t_.;,?/\"`:(){}!+[]><=%$#*@&^~|\\");
/*  92 */     this.arrFilter[2] = new BasicWebFilter();
/*  93 */     this.arrFinder[2] = new BasicWebFinder(this.funcDict);
/*  94 */     this.arrMerger[2] = new BasicWebMerger();
/*  95 */     this.arrRanker[2] = new ScorerRanker();
/*  96 */     this.arrExpander[2] = new NullExpander();
/*     */ 
/*  98 */     this.generator = new BasicQueryGenerator(this.funcDict, this.nouseHeadNounDict, this.arrParser[0], tagger, lemmatiser, this.verbUtil, this.classifier, new GroupScorer());
/*  99 */     this.downloader = new WebDownloader(this.generator, google, this.top, 25);
/*     */   }
/*     */ 
/*     */   public ArrayList answer(String question)
/*     */   {
/*     */     try
/*     */     {
/* 108 */       this.query = this.generator.generate(question);
/* 109 */       if (this.query == null)
/* 110 */         return null;
/* 111 */       this.history.println(question);
/* 112 */       this.history.flush();
/*     */ 
/* 114 */       this.query.setUsedQuery(this.query.getQuery(0));
/* 115 */       ArrayCollectionReader reader = this.downloader.webSearch(this.query.getUsedQuery());
/* 116 */       int i = 1;
/* 117 */       while ((i < this.query.getQueryNum()) && (reader.size() < this.downloader.getMinDocumentNum())) {
/* 118 */         this.query.setUsedQuery(this.query.getQuery(i++));
/* 119 */         reader = this.downloader.webSearch(reader, this.query.getUsedQuery());
/*     */       }
/*     */ 
/* 122 */       int answerType = this.query.getAnswerType();
/* 123 */       if (answerType > 2)
/* 124 */         answerType = 0;
/* 125 */       ArrayList list = findCandidate(this.query, this.arrFinder[answerType], this.arrMerger[answerType], this.arrFilter[answerType], this.arrRanker[answerType], this.arrExpander[answerType], this.arrParser[answerType], reader);
/* 126 */       while (list.size() > 10)
/* 127 */         list.remove(list.size() - 1);
/* 128 */       return list;
/*     */     }
/*     */     catch (Exception e) {
/* 131 */       e.printStackTrace();
/* 132 */     }return null;
/*     */   }
/*     */ 
/*     */   public ArrayList answer(CollectionReader reader, QuestionQuery query)
/*     */   {
/* 139 */     int answerType = query.getAnswerType();
/* 140 */     if (answerType > 2)
/* 141 */       answerType = 0;
/* 142 */     return findCandidate(query, this.arrFinder[answerType], this.arrMerger[answerType], this.arrFilter[answerType], this.arrRanker[answerType], this.arrExpander[answerType], this.arrParser[answerType], reader);
/*     */   }
/*     */ 
/*     */   public CandidateFinder getCandidateFinder(int answerType) {
/* 146 */     if (answerType > 2) {
/* 147 */       return this.arrFinder[0];
/*     */     }
/* 149 */     return this.arrFinder[answerType];
/*     */   }
/*     */ 
/*     */   public void setCandidateRanker(CandidateRanker ranker, int answerType) {
/* 153 */     this.arrRanker[answerType] = ranker;
/*     */   }
/*     */ 
/*     */   public WebDownloader getWebDownloader() {
/* 157 */     return this.downloader;
/*     */   }
/*     */ 
/*     */   public void setQueryScorer(QueryScorer scorer) {
/* 161 */     this.generator.setQueryScorer(scorer);
/*     */   }
/*     */ 
/*     */   public void setTopDocumentNum(int top) {
/* 165 */     this.top = top;
/* 166 */     this.downloader.setMaxDocumentNum(top);
/*     */   }
/*     */ 
/*     */   public void close() {
/* 170 */     this.history.close();
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.system.WebQASystem
 * JD-Core Version:    0.6.2
 */