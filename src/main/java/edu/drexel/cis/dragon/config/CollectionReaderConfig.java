/*     */ package edu.drexel.cis.dragon.config;
/*     */ 
/*     */ import edu.drexel.cis.dragon.onlinedb.ArticleParser;
/*     */ import edu.drexel.cis.dragon.onlinedb.BasicCollectionReader;
/*     */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*     */ import edu.drexel.cis.dragon.onlinedb.SimpleCollectionReader;
/*     */ import edu.drexel.cis.dragon.onlinedb.trec.EarlyTrecTopicReader;
/*     */ import edu.drexel.cis.dragon.onlinedb.trec.Genomics2004TopicReader;
/*     */ import edu.drexel.cis.dragon.onlinedb.trec.Genomics2005TopicReader;
/*     */ import edu.drexel.cis.dragon.onlinedb.trec.TrecCollectionReader;
/*     */ import edu.drexel.cis.dragon.onlinedb.trec.TrecFileReader;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class CollectionReaderConfig extends ConfigUtil
/*     */ {
/*     */   public CollectionReaderConfig()
/*     */   {
/*     */   }
/*     */ 
/*     */   public CollectionReaderConfig(ConfigureNode root)
/*     */   {
/*  22 */     super(root);
/*     */   }
/*     */ 
/*     */   public CollectionReaderConfig(String configFile) {
/*  26 */     super(configFile);
/*     */   }
/*     */ 
/*     */   public CollectionReader getCollectionReader(int collectionID) {
/*  30 */     return loadCollectionReader(this.root, collectionID);
/*     */   }
/*     */ 
/*     */   public CollectionReader getCollectionReader(ConfigureNode node, int extractorID) {
/*  34 */     return loadCollectionReader(node, extractorID);
/*     */   }
/*     */ 
/*     */   private CollectionReader loadCollectionReader(ConfigureNode node, int extractorID)
/*     */   {
/*  41 */     ConfigureNode extractorNode = getConfigureNode(node, "collectionreader", extractorID);
/*  42 */     if (extractorNode == null)
/*  43 */       return null;
/*  44 */     String extractorName = extractorNode.getNodeName();
/*  45 */     return loadCollectionReader(extractorName, extractorNode);
/*     */   }
/*     */ 
/*     */   protected CollectionReader loadCollectionReader(String collectionName, ConfigureNode collectionNode) {
/*  49 */     if (collectionName.equalsIgnoreCase("BasicCollectionReader"))
/*  50 */       return loadBasicCollectionReader(collectionNode);
/*  51 */     if (collectionName.equalsIgnoreCase("SimpleCollectionReader"))
/*  52 */       return loadSimpleCollectionReader(collectionNode);
/*  53 */     if (collectionName.equalsIgnoreCase("TrecCollectionReader"))
/*  54 */       return loadTrecCollectionReader(collectionNode);
/*  55 */     if (collectionName.equalsIgnoreCase("TrecFileReader"))
/*  56 */       return loadTrecFileReader(collectionNode);
/*  57 */     if (collectionName.equalsIgnoreCase("EarlyTrecTopicReader"))
/*  58 */       return new EarlyTrecTopicReader(collectionNode.getString("topicfile"));
/*  59 */     if (collectionName.equalsIgnoreCase("Genomics2005TopicReader"))
/*  60 */       return new Genomics2005TopicReader(collectionNode.getString("topicfile"));
/*  61 */     if (collectionName.equalsIgnoreCase("Genomics2004TopicReader")) {
/*  62 */       return new Genomics2004TopicReader(collectionNode.getString("topicfile"));
/*     */     }
/*  64 */     return (CollectionReader)loadResource(collectionNode);
/*     */   }
/*     */ 
/*     */   private CollectionReader loadBasicCollectionReader(ConfigureNode curNode)
/*     */   {
/*     */     try
/*     */     {
/*  74 */       String collectionPath = curNode.getString("collectionpath");
/*  75 */       String collectionName = curNode.getString("collectionname");
/*  76 */       String collectionFile = curNode.getString("collectionfile");
/*  77 */       String indexFile = curNode.getString("indexfile");
/*  78 */       String articleParser = curNode.getString("articleparser", "dragon.onlinedb.BasicArticleParser");
/*     */       CollectionReader reader;
/*  79 */       if (collectionFile == null) {
/*  80 */         collectionFile = collectionPath + "/" + collectionName + ".collection";
/*  81 */         indexFile = collectionPath + "/" + collectionName + ".index";
/*  82 */         reader = new BasicCollectionReader(collectionFile, indexFile);
/*     */       }
/*     */       else {
/*  85 */         reader = new BasicCollectionReader(collectionFile, indexFile);
/*  86 */       }ArticleParser parser = getArticleParser(articleParser);
/*  87 */       if (parser == null) {
/*  88 */         System.out.println("Can not load the article parser.");
/*  89 */         return null;
/*     */       }
/*  91 */       reader.setArticleParser(parser);
/*  92 */       return reader;
/*     */     }
/*     */     catch (Exception e) {
/*  95 */       e.printStackTrace();
/*  96 */     }return null;
/*     */   }
/*     */ 
/*     */   private CollectionReader loadSimpleCollectionReader(ConfigureNode curNode)
/*     */   {
/*     */     try
/*     */     {
/* 107 */       String collectionPath = curNode.getString("collectionpath", null);
/* 108 */       String articleParser = curNode.getString("articleparser", "dragon.onlinedb.SimpleArticleParser");
/* 109 */       ArticleParser parser = getArticleParser(articleParser);
/* 110 */       if (parser == null) {
/* 111 */         System.out.println("Can not load the article parser.");
/* 112 */         return null;
/*     */       }
/*     */       CollectionReader reader;
/* 115 */       if (collectionPath == null) {
/* 116 */         reader = new SimpleCollectionReader(parser);
/*     */       }
/* 118 */       return new SimpleCollectionReader(collectionPath, parser);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 122 */       e.printStackTrace();
/* 123 */     }return null;
/*     */   }
/*     */ 
/*     */   private CollectionReader loadTrecCollectionReader(ConfigureNode curNode)
/*     */   {
/*     */     try
/*     */     {
/* 134 */       String articleRoot = curNode.getString("articleroot", "DOC");
/* 135 */       String collectionPath = curNode.getString("collectionpath", null);
/* 136 */       String indexFile = curNode.getString("indexfile", null);
/* 137 */       String articleParser = curNode.getString("articleparser", "dragon.onlinedb.BasicArticleParser");
/* 138 */       ArticleParser parser = getArticleParser(articleParser);
/* 139 */       if (parser == null) {
/* 140 */         System.out.println("Can not load the article parser.");
/* 141 */         return null;
/*     */       }
/*     */       CollectionReader reader;
/* 143 */       if ((collectionPath == null) && (indexFile == null)) {
/* 144 */         reader = new TrecCollectionReader(parser, articleRoot);
/*     */       }
/*     */       else
/*     */       {
/* 145 */         if (indexFile == null)
/* 146 */           reader = new TrecCollectionReader(collectionPath, parser, articleRoot);
/*     */       }
/* 148 */       return new TrecCollectionReader(collectionPath, indexFile, parser, articleRoot);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 152 */       e.printStackTrace();
/* 153 */     }return null;
/*     */   }
/*     */ 
/*     */   private CollectionReader loadTrecFileReader(ConfigureNode curNode)
/*     */   {
/*     */     try
/*     */     {
/* 164 */       String articleRoot = curNode.getString("articleroot", "DOC");
/* 165 */       String filename = curNode.getString("filename", null);
/* 166 */       String articleParser = curNode.getString("articleparser", "dragon.onlinedb.BasicArticleParser");
/* 167 */       ArticleParser parser = getArticleParser(articleParser);
/* 168 */       if (parser == null) {
/* 169 */         System.out.println("Can not load the article parser.");
/* 170 */         return null;
/*     */       }
/*     */       CollectionReader reader;
/* 172 */       if (filename == null) {
/* 173 */         reader = new TrecFileReader(parser, articleRoot);
/*     */       }
/* 175 */       return new TrecFileReader(new File(filename), parser, articleRoot);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 179 */       e.printStackTrace();
/* 180 */     }return null;
/*     */   }
/*     */ 
/*     */   protected ArticleParser getArticleParser(String className)
/*     */   {
/*     */     try
/*     */     {
/* 189 */       Class myClass = Class.forName(className);
/* 190 */       return (ArticleParser)myClass.newInstance();
/*     */     }
/*     */     catch (Exception e) {
/* 193 */       e.printStackTrace();
/* 194 */     }return null;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.CollectionReaderConfig
 * JD-Core Version:    0.6.2
 */