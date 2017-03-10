/*     */ package edu.drexel.cis.dragon.config;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.summarize.GenericMultiDocSummarizer;
/*     */ import edu.drexel.cis.dragon.ir.summarize.ROUGE;
/*     */ import edu.drexel.cis.dragon.matrix.vector.DoubleVector;
/*     */ import edu.drexel.cis.dragon.onlinedb.ArticleParser;
/*     */ import edu.drexel.cis.dragon.onlinedb.SimpleCollectionReader;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.FormatUtil;
/*     */ import edu.drexel.cis.dragon.util.WildCardFilter;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class SummarizationEvaAppConfig
/*     */ {
/*     */   PrintWriter out;
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*  28 */     if (args.length != 2) {
/*  29 */       System.out.println("Please input two parameters: configuration xml file and retrieval evaluation id");
/*  30 */       return;
/*     */     }
/*     */ 
/*  33 */     ConfigureNode root = new BasicConfigureNode(args[0]);
/*  34 */     ConfigUtil util = new ConfigUtil();
/*  35 */     ConfigureNode sumAppNode = util.getConfigureNode(root, "summarizationevaapp", Integer.parseInt(args[1]));
/*  36 */     if (sumAppNode == null)
/*  37 */       return;
/*  38 */     SummarizationEvaAppConfig sumApp = new SummarizationEvaAppConfig();
/*  39 */     sumApp.evaluate(sumAppNode);
/*     */   }
/*     */ 
/*     */   public void evaluate(ConfigureNode node)
/*     */   {
/*  48 */     int summarizerID = node.getInt("summarizer");
/*  49 */     GenericMultiDocSummarizer summarizer = new SummarizerConfig().getGenericMultiDocSummarizer(node, summarizerID);
/*  50 */     ArticleParser parser = getArticleParser(node.getString("articleparser"));
/*  51 */     String testDataFolder = node.getString("testdatafolder");
/*  52 */     String modelSummaryFolder = node.getString("modelsummaryfolder");
/*  53 */     String outputFolder = node.getString("outputfolder");
/*  54 */     int maxLength = node.getInt("maxlength");
/*  55 */     evaluate(summarizer, parser, testDataFolder, modelSummaryFolder, outputFolder, maxLength);
/*     */   }
/*     */ 
/*     */   public void evaluate(GenericMultiDocSummarizer summarizer, ArticleParser parser, String testDataFolder, String modelSummaryFolder, String outputFolder, int maxLength)
/*     */   {
/*  68 */     ROUGE rouge = new ROUGE();
/*  69 */     rouge.setStopwordOption(false);
/*  70 */     rouge.setLemmatiserOption(false);
/*  71 */     rouge.setCaseOption(false);
/*  72 */     DecimalFormat df = FormatUtil.getNumericFormat(1, 4);
/*  73 */     DoubleVector result = new DoubleVector(6);
/*  74 */     result.assign(0.0D);
/*  75 */     String[] clusterNames = new File(testDataFolder).list();
/*  76 */     new File(outputFolder).mkdirs();
/*  77 */     this.out = FileUtil.getPrintWriter(outputFolder + "/eva.txt");
/*  78 */     printHeader();
/*  79 */     for (int i = 0; i < clusterNames.length; i++) {
/*  80 */       SimpleCollectionReader reader = new SimpleCollectionReader(testDataFolder + "/" + clusterNames[i], parser);
/*  81 */       String[] refSummaries = getModelSummaries(modelSummaryFolder, clusterNames[i]);
/*  82 */       String summary = summarizer.summarize(reader, maxLength);
/*  83 */       reader.close();
/*  84 */       FileUtil.saveTextFile(outputFolder + "/" + clusterNames[i] + "_sum.txt", summary);
/*  85 */       result.add(evaluate(rouge, clusterNames[i], summary, refSummaries));
/*     */     }
/*  87 */     result.multiply(1.0D / clusterNames.length);
/*  88 */     write("Average\t" + df.format(result.get(0)) + "\t" + df.format(result.get(1)) + "\t" + df.format(result.get(2)) + "\t" + 
/*  89 */       df.format(result.get(3)) + "\t" + df.format(result.get(4)) + "\t" + df.format(result.get(5)));
/*  90 */     this.out.close();
/*     */   }
/*     */ 
/*     */   private void printHeader() {
/*  94 */     write("Run\tR-1.Min\tR-1.Max\tR-1.Avg\tR-2.Min\tR-2.Max\tR-2.Avg");
/*     */   }
/*     */ 
/*     */   private DoubleVector evaluate(ROUGE rouge, String clusterName, String autoSummary, String[] refSummaries)
/*     */   {
/* 101 */     DecimalFormat df = FormatUtil.getNumericFormat(1, 4);
/* 102 */     DoubleVector result = new DoubleVector(6);
/* 103 */     rouge.useRougeN(1);
/* 104 */     rouge.evaluate(autoSummary, refSummaries);
/* 105 */     rouge.setMultipleReferenceMode(2);
/* 106 */     result.set(0, rouge.getRecall());
/* 107 */     rouge.setMultipleReferenceMode(1);
/* 108 */     result.set(1, rouge.getRecall());
/* 109 */     rouge.setMultipleReferenceMode(3);
/* 110 */     result.set(2, rouge.getRecall());
/* 111 */     rouge.useRougeN(2);
/* 112 */     rouge.evaluate(autoSummary, refSummaries);
/* 113 */     rouge.setMultipleReferenceMode(2);
/* 114 */     result.set(3, rouge.getRecall());
/* 115 */     rouge.setMultipleReferenceMode(1);
/* 116 */     result.set(4, rouge.getRecall());
/* 117 */     rouge.setMultipleReferenceMode(3);
/* 118 */     result.set(5, rouge.getRecall());
/* 119 */     write(clusterName + "\t" + df.format(result.get(0)) + "\t" + df.format(result.get(1)) + "\t" + df.format(result.get(2)) + "\t" + 
/* 120 */       df.format(result.get(3)) + "\t" + df.format(result.get(4)) + "\t" + df.format(result.get(5)));
/* 121 */     return result;
/*     */   }
/*     */ 
/*     */   private String[] getModelSummaries(String modelFolder, String clusterName)
/*     */   {
/* 130 */     File modelDir = new File(modelFolder);
/* 131 */     File[] arrSum = modelDir.listFiles(new WildCardFilter(clusterName + "*"));
/* 132 */     if ((arrSum == null) || (arrSum.length == 0)) {
/* 133 */       clusterName = clusterName.substring(0, clusterName.length() - 1);
/* 134 */       arrSum = modelDir.listFiles(new WildCardFilter(clusterName + "*"));
/*     */     }
/* 136 */     ArrayList list = new ArrayList(arrSum.length);
/* 137 */     String[] summaries = new String[arrSum.length];
/* 138 */     for (int i = 0; i < arrSum.length; i++)
/* 139 */       if (arrSum[i].isFile())
/* 140 */         list.add(FileUtil.readTextFile(arrSum[i]));
/* 141 */     summaries = new String[list.size()];
/* 142 */     for (int i = 0; i < summaries.length; i++)
/* 143 */       summaries[i] = ((String)list.get(i));
/* 144 */     return summaries;
/*     */   }
/*     */ 
/*     */   private void write(String message) {
/* 148 */     System.out.println(message);
/* 149 */     if (this.out != null) {
/* 150 */       this.out.println(message);
/* 151 */       this.out.flush();
/*     */     }
/*     */   }
/*     */ 
/*     */   private ArticleParser getArticleParser(String className)
/*     */   {
/*     */     try
/*     */     {
/* 159 */       Class myClass = Class.forName(className);
/* 160 */       return (ArticleParser)myClass.newInstance();
/*     */     }
/*     */     catch (Exception e) {
/* 163 */       e.printStackTrace();
/* 164 */     }return null;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.SummarizationEvaAppConfig
 * JD-Core Version:    0.6.2
 */