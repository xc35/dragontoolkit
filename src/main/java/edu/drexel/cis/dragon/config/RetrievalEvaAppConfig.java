/*     */ package edu.drexel.cis.dragon.config;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.query.IRQuery;
/*     */ import edu.drexel.cis.dragon.ir.query.RelSimpleQuery;
/*     */ import edu.drexel.cis.dragon.ir.search.Searcher;
/*     */ import edu.drexel.cis.dragon.ir.search.evaluate.TrecEva;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElement;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.FormatUtil;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ public class RetrievalEvaAppConfig
/*     */ {
/*     */   public static void main(String[] args)
/*     */   {
/*  31 */     if (args.length != 2) {
/*  32 */       System.out.println("Please input two parameters: configuration xml file and retrieval evaluation id");
/*  33 */       return;
/*     */     }
/*     */ 
/*  36 */     ConfigureNode root = new BasicConfigureNode(args[0]);
/*  37 */     ConfigUtil util = new ConfigUtil();
/*  38 */     ConfigureNode retrievalAppNode = util.getConfigureNode(root, "retrievalevaapp", Integer.parseInt(args[1]));
/*  39 */     if (retrievalAppNode == null)
/*  40 */       return;
/*  41 */     RetrievalEvaAppConfig retrievalApp = new RetrievalEvaAppConfig();
/*  42 */     retrievalApp.evaluate(retrievalAppNode);
/*     */   }
/*     */ 
/*     */   public void evaluate(ConfigureNode node)
/*     */   {
/*  52 */     SearcherConfig config = new SearcherConfig();
/*  53 */     int searcherID = node.getInt("searcher", 0);
/*  54 */     if (searcherID <= 0)
/*  55 */       return;
/*  56 */     Searcher searcher = config.getSearcher(node, searcherID);
/*  57 */     String judgmentFile = node.getString("judgmentfile", null);
/*  58 */     String queryFile = node.getString("queryfile", null);
/*  59 */     String resultFolder = node.getString("resultfolder", null);
/*  60 */     if ((judgmentFile == null) || (queryFile == null) || (resultFolder == null))
/*  61 */       return;
/*  62 */     int start = node.getInt("startopic", -1);
/*  63 */     int end = node.getInt("endtopic", -1);
/*  64 */     String excludedTopics = node.getString("excludedtopics", null);
/*     */     ArrayList excludedQueries;
/*  65 */     if ((excludedTopics == null) || (excludedTopics.trim().length() == 0)) {
/*  66 */       excludedQueries = null;
/*     */     } else {
/*  68 */       String[] arrTopic = excludedTopics.trim().split(";");
/*  69 */       excludedQueries = new ArrayList(arrTopic.length);
/*  70 */       for (int i = 0; i < arrTopic.length; i++)
/*  71 */         excludedQueries.add(new Integer(arrTopic[i]));
/*     */     }
/*  73 */     evaluate(searcher, judgmentFile, queryFile, resultFolder, start, end, excludedQueries);
/*     */   }
/*     */ 
/*     */   public void evaluate(Searcher searcher, String judgmentFile, String queryFile, String outputFolder, int start, int end, ArrayList excludedQueries)
/*     */   {
/*     */     try
/*     */     {
/*  89 */       new File(outputFolder).mkdirs();
/*  90 */       SimpleElement[] arrQuery = readQuery(queryFile);
/*  91 */       TreeMap arrRelevant = loadJudgmentFile(searcher.getIndexReader(), judgmentFile);
/*  92 */       TrecEva eva = new TrecEva(outputFolder);
/*  93 */       double[] resultSum = new double[10];
/*  94 */       for (int i = 0; i < 10; i++) resultSum[i] = 0.0D;
/*  95 */       DecimalFormat df = FormatUtil.getNumericFormat(2, 2);
/*  96 */       int total = 0;
/*  97 */       PrintWriter out = FileUtil.getPrintWriter(outputFolder + "/all.eva");
/*  98 */       out.write("Query\tRetrieved\tRelevant\tP@10\tR@10\tP@100\tR@100\tP\tR\tAP\r\n");
/*  99 */       for (int i = 0; i < arrQuery.length; i++) {
/* 100 */         int queryNo = arrQuery[i].getIndex();
/* 101 */         if (((queryNo >= start) || (start < 0)) && ((queryNo <= end) || (end < 0))) {
/* 102 */           ArrayList list = (ArrayList)arrRelevant.get(new Integer(queryNo));
/* 103 */           if ((list != null) && (list.size() != 0) && (
/* 104 */             (excludedQueries == null) || (!excludedQueries.contains(new Integer(queryNo))))) {
/* 105 */             total++;
/*     */ 
/* 107 */             System.out.print("Processing Query #" + queryNo);
/* 108 */             IRQuery irQuery = new RelSimpleQuery(arrQuery[i].getKey());
/* 109 */             irQuery.setQueryKey(queryNo);
/* 110 */             searcher.search(irQuery);
/* 111 */             double[] result = eva.evaluateQuery(irQuery, searcher.getRankedDocumentList(), list, searcher.getIndexReader());
/* 112 */             printEvaStat(result, out);
/* 113 */             System.out.print(" " + df.format(result[9]) + "%\n");
/* 114 */             for (int j = 0; j < 10; j++) resultSum[j] += result[j]; 
/*     */           }
/*     */         }
/*     */       }
/* 117 */       for (int i = 0; i < 10; i++) resultSum[i] /= total;
/* 118 */       System.out.println("MAP:  " + df.format(resultSum[9]) + "%");
/* 119 */       System.out.println("P@10: " + df.format(resultSum[3]) + "%");
/* 120 */       System.out.println("P@100:" + df.format(resultSum[5]) + "%");
/*     */     }
/*     */     catch (Exception e) {
/* 123 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void printEvaStat(double[] result, PrintWriter out)
/*     */   {
/*     */     try
/*     */     {
/* 132 */       DecimalFormat df = FormatUtil.getNumericFormat(2, 2);
/* 133 */       DecimalFormat dfInteger = FormatUtil.getNumericFormat(0, 0);
/*     */ 
/* 135 */       for (int i = 0; i < 3; i++) {
/* 136 */         if (i > 0) out.write(9);
/* 137 */         out.write(dfInteger.format(result[i]));
/*     */       }
/* 139 */       for (int i = 3; i <= 9; i++) {
/* 140 */         out.write(9);
/* 141 */         out.write(df.format(result[i]));
/*     */       }
/* 143 */       out.write("\r\n");
/* 144 */       out.flush();
/*     */     }
/*     */     catch (Exception e) {
/* 147 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private SimpleElement[] readQuery(String queryFile)
/*     */   {
/*     */     try
/*     */     {
/* 157 */       BufferedReader br = new BufferedReader(new FileReader(new File(queryFile)));
/* 158 */       String line = br.readLine();
/* 159 */       int total = Integer.parseInt(line);
/* 160 */       SimpleElement[] arrQuery = new SimpleElement[total];
/* 161 */       int count = 0;
/* 162 */       while ((line = br.readLine()) != null) {
/* 163 */         int j = line.indexOf('\t');
/* 164 */         int lineNo = Integer.parseInt(line.substring(0, j));
/* 165 */         arrQuery[count] = new SimpleElement(line.substring(j + 1), lineNo);
/* 166 */         count++;
/*     */       }
/* 168 */       br.close();
/* 169 */       return arrQuery;
/*     */     }
/*     */     catch (Exception e) {
/* 172 */       e.printStackTrace();
/* 173 */     }return null;
/*     */   }
/*     */ 
/*     */   private TreeMap loadJudgmentFile(IndexReader reader, String filename)
/*     */   {
/*     */     try
/*     */     {
/* 188 */       TreeMap arrRelevant = new TreeMap();
/* 189 */       int count = 0;
/* 190 */       BufferedReader br = FileUtil.getTextReader(filename);
/*     */       String line;
/* 191 */       while ((line = br.readLine()) != null)
/*     */       {
/* 192 */         count++;
/* 193 */         int start = line.indexOf('\t');
/* 194 */         int end = line.indexOf('\t', start + 1);
/* 195 */         int topicID = Integer.parseInt(line.substring(0, start));
/* 196 */         String pmid = line.substring(start + 1, end);
/* 197 */         int relevance = Integer.parseInt(line.substring(end + 1));
/* 198 */         if ((relevance > 0) && (relevance < 3)) {
/* 199 */           IRDoc cur = reader.getDoc(pmid);
/* 200 */           if (cur != null) {
/* 201 */             ArrayList list = (ArrayList)arrRelevant.get(new Integer(topicID));
/* 202 */             if (list == null) {
/* 203 */               list = new ArrayList();
/* 204 */               arrRelevant.put(new Integer(topicID), list);
/*     */             }
/* 206 */             list.add(cur);
/*     */           }
/*     */         }
/*     */       }
/* 210 */       return arrRelevant;
/*     */     }
/*     */     catch (Exception e) {
/* 213 */       e.printStackTrace();
/* 214 */     }return null;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.RetrievalEvaAppConfig
 * JD-Core Version:    0.6.2
 */