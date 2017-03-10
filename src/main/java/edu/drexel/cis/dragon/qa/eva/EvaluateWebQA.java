/*    */ package edu.drexel.cis.dragon.qa.eva;
/*    */ 
/*    */ import edu.drexel.cis.dragon.onlinedb.ArrayCollectionReader;
/*    */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*    */ import edu.drexel.cis.dragon.qa.extract.CandidateFinder;
/*    */ import edu.drexel.cis.dragon.qa.query.GroupScorer;
/*    */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*    */ import edu.drexel.cis.dragon.qa.query.WordScorer;
/*    */ import edu.drexel.cis.dragon.qa.score.FrequencyScorer;
/*    */ import edu.drexel.cis.dragon.qa.score.SimilarityScorer;
/*    */ import edu.drexel.cis.dragon.qa.system.Candidate;
/*    */ import edu.drexel.cis.dragon.qa.system.QASystem;
/*    */ import edu.drexel.cis.dragon.qa.system.WebDownloader;
/*    */ import edu.drexel.cis.dragon.qa.system.WebQASystem;
/*    */ import java.io.File;
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
/*    */ import jxl.SheetSettings;
/*    */ import jxl.Workbook;
/*    */ import jxl.write.WritableSheet;
/*    */ import jxl.write.WritableWorkbook;
/*    */ 
/*    */ public class EvaluateWebQA extends Evaluate
/*    */ {
/*    */   private WebDownloader downloader;
/*    */ 
/*    */   public static void main(String[] args)
/*    */   {
/* 25 */     EvaluateWebQA eva = new EvaluateWebQA();
/* 26 */     eva.test("Who founded Google?");
/*    */   }
/*    */ 
/*    */   public EvaluateWebQA()
/*    */   {
/* 34 */     super(new WebQASystem());
/* 35 */     this.downloader = ((WebQASystem)this.system).getWebDownloader();
/*    */   }
/*    */ 
/*    */   public void test(String question)
/*    */   {
/* 45 */     QuestionQuery query = this.system.generateQuery(question);
/* 46 */     if (query == null) {
/* 47 */       return;
/*    */     }
/* 49 */     query.setUsedQuery(query.getQuery(0));
/* 50 */     System.out.println(query.getUsedQuery());
/* 51 */     CollectionReader reader = this.downloader.webSearch(query.getUsedQuery());
/* 52 */     int i = 1;
/* 53 */     while ((i < query.getQueryNum()) && (reader.size() < this.downloader.getMinDocumentNum())) {
/* 54 */       query.setUsedQuery(query.getQuery(i++));
/* 55 */       System.out.println(query.getUsedQuery());
/* 56 */       reader = this.downloader.webSearch((ArrayCollectionReader)reader, query.getUsedQuery());
/*    */     }
/*    */ 
/* 62 */     ArrayList list = this.system.answer(reader, query);
/* 63 */     int total = Math.min(50, list.size());
/* 64 */     for (i = 0; i < total; i++) {
/* 65 */       Candidate curTerm = (Candidate)list.get(i);
/* 66 */       System.out.println("#" + (i + 1) + ": " + curTerm.getName() + "\t" + curTerm.getWeight() + "\t" + curTerm.getFrequency());
/*    */     }
/*    */   }
/*    */ 
/*    */   public void download(String collectionFolder) {
/* 71 */     download(collectionFolder, "questions.txt", true, 15);
/*    */   }
/*    */ 
/*    */   public void download(String collectionFolder, String questionFile, boolean group, int delay) {
/* 75 */     this.downloader.setDelay(delay);
/* 76 */     this.downloader.download(collectionFolder, collectionFolder + "/" + questionFile, group);
/*    */   }
/*    */ 
/*    */   public void evaluateWebQA(String dir, boolean excel)
/*    */   {
/*    */     try
/*    */     {
/* 88 */       String questionFile = dir + "/questions.txt";
/*    */       WritableWorkbook workbook;
/* 89 */       if (excel) {
/* 90 */         Workbook template = Workbook.getWorkbook(new File(dir + "/eva_template.xls"));
/* 91 */         workbook = Workbook.createWorkbook(new File(dir + "/eva_partial.xls"), template);
/*    */       }
/*    */       else {
/* 94 */         Workbook template = null;
/* 95 */         workbook = null;
/*    */       }
/*    */ 
/* 98 */       String query = "g";
/* 99 */       String collectionFolder = dir;
/* 100 */       for (int j = 0; j < 2; j++)
/*    */       {
/*    */         String scorer;
/* 101 */         if (j == 0) {
/* 102 */           ((WebQASystem)this.system).getCandidateFinder(0).setCandidateScorer(new SimilarityScorer());
/* 103 */           ((WebQASystem)this.system).getCandidateFinder(1).setCandidateScorer(new SimilarityScorer());
/* 104 */           scorer = "w";
/*    */         }
/*    */         else {
/* 107 */           ((WebQASystem)this.system).getCandidateFinder(0).setCandidateScorer(new FrequencyScorer());
/* 108 */           ((WebQASystem)this.system).getCandidateFinder(1).setCandidateScorer(new FrequencyScorer());
/* 109 */           scorer = "f";
/*    */         }
/* 111 */         for (int m = 0; m < 2; m++)
/*    */         {
/*    */           String qscorer;
/* 112 */           if (m == 0) {
/* 113 */             ((WebQASystem)this.system).setQueryScorer(new GroupScorer());
/* 114 */             qscorer = "g";
/*    */           }
/*    */           else {
/* 117 */             ((WebQASystem)this.system).setQueryScorer(new WordScorer());
/* 118 */             qscorer = "w";
/*    */           }
/* 120 */           if (scorer.equalsIgnoreCase("w")) {
/* 121 */             for (int n = 0; n < 4; n++) {
/* 122 */               String threshold = n == 0 ? "00" : Integer.toString(n * 25);
/* 123 */               ((WebQASystem)this.system).getCandidateFinder(0).setMinSentenceScore(n / 4.0D);
/* 124 */               ((WebQASystem)this.system).getCandidateFinder(1).setMinSentenceScore(n / 4.0D);
/* 125 */               String run = query + scorer + qscorer + threshold;
/* 126 */               System.out.println("Evaluating " + run);
/* 127 */               if (!excel) {
/* 128 */                 evaluate(collectionFolder, questionFile, dir + "/" + run + ".txt");
/*    */               } else {
/* 130 */                 WritableSheet sheet = workbook.getSheet(run);
/* 131 */                 sheet.getSettings().setAutomaticFormulaCalculation(true);
/* 132 */                 evaluate(collectionFolder, questionFile, sheet);
/*    */               }
/*    */             }
/*    */           }
/*    */           else {
/* 137 */             ((WebQASystem)this.system).getCandidateFinder(0).setMinSentenceScore(0.0D);
/* 138 */             ((WebQASystem)this.system).getCandidateFinder(1).setMinSentenceScore(0.0D);
/* 139 */             String run = query + scorer;
/* 140 */             System.out.println("Evaluating " + run);
/* 141 */             if (!excel) {
/* 142 */               evaluate(collectionFolder, questionFile, dir + "/" + run + ".txt");
/*    */             } else {
/* 144 */               WritableSheet sheet = workbook.getSheet(run);
/* 145 */               sheet.getSettings().setAutomaticFormulaCalculation(true);
/* 146 */               evaluate(collectionFolder, questionFile, sheet);
/*    */             }
/*    */           }
/*    */         }
/*    */       }
/* 151 */       if (excel) {
/* 152 */         workbook.write();
/* 153 */         workbook.close();
/*    */       }
/*    */     }
/*    */     catch (Exception e) {
/* 157 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.eva.EvaluateWebQA
 * JD-Core Version:    0.6.2
 */