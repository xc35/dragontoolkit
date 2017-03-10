/*     */ package edu.drexel.cis.dragon.config;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.BasicIndexReader;
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.index.OnlineIndexReader;
/*     */ import edu.drexel.cis.dragon.ir.index.OnlineIndexer;
/*     */ import edu.drexel.cis.dragon.ir.index.sentence.BasicSentenceIndexReader;
/*     */ import edu.drexel.cis.dragon.ir.index.sentence.OnlineSentenceIndexReader;
/*     */ import edu.drexel.cis.dragon.ir.index.sentence.OnlineSentenceIndexer;
/*     */ import edu.drexel.cis.dragon.ir.index.sequence.BasicSequenceIndexReader;
/*     */ import edu.drexel.cis.dragon.ir.index.sequence.OnlineSequenceIndexReader;
/*     */ import edu.drexel.cis.dragon.ir.index.sequence.OnlineSequenceIndexer;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.nlp.extract.ConceptExtractor;
/*     */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*     */ 
/*     */ public class IndexReaderConfig extends ConfigUtil
/*     */ {
/*     */   public IndexReaderConfig()
/*     */   {
/*     */   }
/*     */ 
/*     */   public IndexReaderConfig(ConfigureNode root)
/*     */   {
/*  25 */     super(root);
/*     */   }
/*     */ 
/*     */   public IndexReaderConfig(String configFile) {
/*  29 */     super(configFile);
/*     */   }
/*     */ 
/*     */   public IRCollection getIRCollectionStat(int collectionID) {
/*  33 */     return getIRCollectionStat(this.root, collectionID);
/*     */   }
/*     */ 
/*     */   public IRCollection getIRCollectionStat(ConfigureNode node, int collectionID)
/*     */   {
/*  42 */     ConfigureNode collectionNode = getConfigureNode(node, "collectionstat", collectionID);
/*  43 */     if (collectionNode == null)
/*  44 */       return null;
/*  45 */     String collectionName = collectionNode.getNodeName();
/*  46 */     if (!collectionName.equalsIgnoreCase("IRCollection"))
/*  47 */       return null;
/*  48 */     String indexFolder = collectionNode.getString("indexfolder");
/*  49 */     String indexSection = collectionNode.getString("indexsection", null);
/*  50 */     IRCollection collection = new IRCollection();
/*  51 */     if ((indexSection == null) || (indexSection.trim().length() == 0))
/*  52 */       collection.load(indexFolder + "/collection.stat");
/*     */     else
/*  54 */       collection.load(indexFolder + "/" + indexSection + "/collection.stat");
/*  55 */     return collection;
/*     */   }
/*     */ 
/*     */   public IndexReader getIndexReader(int indexReaderID) {
/*  59 */     return getIndexReader(this.root, indexReaderID);
/*     */   }
/*     */ 
/*     */   public IndexReader getIndexReader(ConfigureNode node, int indexReaderID) {
/*  63 */     return loadIndexReader(node, indexReaderID);
/*     */   }
/*     */ 
/*     */   private IndexReader loadIndexReader(ConfigureNode node, int indexReaderID)
/*     */   {
/*  69 */     ConfigureNode indexReaderNode = getConfigureNode(node, "indexreader", indexReaderID);
/*  70 */     if (indexReaderNode == null)
/*  71 */       return null;
/*  72 */     String indexReaderName = indexReaderNode.getNodeName();
/*  73 */     return loadIndexReader(indexReaderName, indexReaderNode);
/*     */   }
/*     */ 
/*     */   protected IndexReader loadIndexReader(String indexReaderName, ConfigureNode indexReaderNode) {
/*  77 */     if (indexReaderName.equalsIgnoreCase("BasicIndexReader"))
/*  78 */       return loadBasicIndexReader(indexReaderNode);
/*  79 */     if (indexReaderName.equalsIgnoreCase("OnlineIndexReader"))
/*  80 */       return loadOnlineIndexReader(indexReaderNode);
/*  81 */     if (indexReaderName.equalsIgnoreCase("BasicSentenceIndexReader"))
/*  82 */       return loadBasicSentenceIndexReader(indexReaderNode);
/*  83 */     if (indexReaderName.equalsIgnoreCase("OnlineSentenceIndexReader"))
/*  84 */       return loadOnlineSentenceIndexReader(indexReaderNode);
/*  85 */     if (indexReaderName.equalsIgnoreCase("BasicSequenceIndexReader"))
/*  86 */       return loadBasicSequenceIndexReader(indexReaderNode);
/*  87 */     if (indexReaderName.equalsIgnoreCase("OnlineSequenceIndexReader")) {
/*  88 */       return loadOnlineSequenceIndexReader(indexReaderNode);
/*     */     }
/*  90 */     return (IndexReader)loadResource(indexReaderNode);
/*     */   }
/*     */ 
/*     */   private IndexReader loadBasicSentenceIndexReader(ConfigureNode curNode)
/*     */   {
/*  97 */     String folder = curNode.getString("indexfolder");
/*  98 */     BasicSentenceIndexReader reader = new BasicSentenceIndexReader(folder, true);
/*  99 */     reader.initialize();
/* 100 */     return reader;
/*     */   }
/*     */ 
/*     */   private IndexReader loadOnlineSentenceIndexReader(ConfigureNode curNode)
/*     */   {
/* 109 */     int indexerID = curNode.getInt("onlineindexer");
/* 110 */     int collectionReaderID = curNode.getInt("collectionreader");
/* 111 */     OnlineSentenceIndexer indexer = (OnlineSentenceIndexer)new IndexerConfig().getIndexer(curNode, indexerID);
/* 112 */     CollectionReader collectionReader = new CollectionReaderConfig().getCollectionReader(curNode, collectionReaderID);
/* 113 */     OnlineSentenceIndexReader reader = new OnlineSentenceIndexReader(indexer, collectionReader);
/* 114 */     reader.initialize();
/* 115 */     return reader;
/*     */   }
/*     */ 
/*     */   private IndexReader loadBasicSequenceIndexReader(ConfigureNode curNode)
/*     */   {
/* 124 */     int collectionReaderID = curNode.getInt("collectionreader", -1);
/*     */     CollectionReader collectionReader;
/* 125 */     if (collectionReaderID == -1)
/* 126 */       collectionReader = null;
/*     */     else
/* 128 */       collectionReader = new CollectionReaderConfig().getCollectionReader(curNode, collectionReaderID);
/* 129 */     String folder = curNode.getString("indexfolder");
/* 130 */     BasicSequenceIndexReader reader = new BasicSequenceIndexReader(folder, collectionReader);
/* 131 */     reader.initialize();
/* 132 */     return reader;
/*     */   }
/*     */ 
/*     */   private IndexReader loadOnlineSequenceIndexReader(ConfigureNode curNode)
/*     */   {
/* 142 */     int collectionReaderID = curNode.getInt("collectionreader");
/* 143 */     if (collectionReaderID <= 0) {
/* 144 */       return null;
/*     */     }
/* 146 */     CollectionReader collectionReader = new CollectionReaderConfig().getCollectionReader(curNode, collectionReaderID);
/* 147 */     int extractorID = curNode.getInt("conceptextractor");
/* 148 */     if (extractorID > 0) {
/* 149 */       ConceptExtractor ce = new ConceptExtractorConfig().getConceptExtractor(curNode, extractorID);
/* 150 */       OnlineSequenceIndexReader reader = new OnlineSequenceIndexReader(ce, collectionReader);
/* 151 */       reader.initialize();
/* 152 */       return reader;
/*     */     }
/*     */ 
/* 155 */     int indexerID = curNode.getInt("onlineindexer");
/* 156 */     OnlineSequenceIndexer indexer = (OnlineSequenceIndexer)new IndexerConfig().getIndexer(curNode, indexerID);
/* 157 */     if (indexer == null)
/* 158 */       return null;
/* 159 */     OnlineSequenceIndexReader reader = new OnlineSequenceIndexReader(indexer, collectionReader);
/* 160 */     reader.initialize();
/* 161 */     return reader;
/*     */   }
/*     */ 
/*     */   private IndexReader loadOnlineIndexReader(ConfigureNode curNode)
/*     */   {
/* 171 */     int indexerID = curNode.getInt("onlineindexer");
/* 172 */     int collectionReaderID = curNode.getInt("collectionreader");
/* 173 */     OnlineIndexer indexer = (OnlineIndexer)new IndexerConfig().getIndexer(curNode, indexerID);
/* 174 */     CollectionReader collectionReader = new CollectionReaderConfig().getCollectionReader(curNode, collectionReaderID);
/* 175 */     OnlineIndexReader reader = new OnlineIndexReader(indexer, collectionReader);
/* 176 */     reader.initialize();
/* 177 */     return reader;
/*     */   }
/*     */ 
/*     */   private IndexReader loadBasicIndexReader(ConfigureNode curNode)
/*     */   {
/* 186 */     int collectionReaderID = curNode.getInt("collectionreader", -1);
/*     */     CollectionReader collectionReader;
/* 187 */     if (collectionReaderID == -1)
/* 188 */       collectionReader = null;
/*     */     else
/* 190 */       collectionReader = new CollectionReaderConfig().getCollectionReader(curNode, collectionReaderID);
/* 191 */     String folder = curNode.getString("indexfolder");
/* 192 */     String indexSection = curNode.getString("indexsection", null);
/*     */     BasicIndexReader reader;
/* 193 */     if ((indexSection == null) || (indexSection.trim().length() == 0)) {
/* 194 */        reader = new BasicIndexReader(folder, true, collectionReader);
/* 195 */       reader.initialize();
/*     */     }
/*     */     else {
/* 198 */       reader = new BasicIndexReader(folder + "/" + indexSection, true, collectionReader);
/* 199 */       reader.initialize();
/* 200 */       reader.setIRDocKeyList(new SimpleElementList(folder + "/dockey.list", false));
/* 201 */       reader.setIRTermKeyList(new SimpleElementList(folder + "/termkey.list", false));
/*     */     }
/* 203 */     return reader;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.IndexReaderConfig
 * JD-Core Version:    0.6.2
 */