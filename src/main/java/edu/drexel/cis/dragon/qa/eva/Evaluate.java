/*     */ package edu.drexel.cis.dragon.qa.eva;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.onlinedb.BasicCollectionReader;
/*     */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*     */ import edu.drexel.cis.dragon.onlinedb.searchengine.AbstractSearchEngine;
/*     */ import edu.drexel.cis.dragon.qa.system.Candidate;
/*     */ import edu.drexel.cis.dragon.qa.system.CandidateBase;
/*     */ import edu.drexel.cis.dragon.qa.system.QASystem;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import jxl.Cell;
/*     */ import jxl.write.Label;
/*     */ import jxl.write.Number;
/*     */ import jxl.write.WritableSheet;
/*     */ 
/*     */ public class Evaluate
/*     */ {
/*     */   protected QASystem system;
/*     */ 
/*     */   public Evaluate(QASystem system)
/*     */   {
/*  21 */     this.system = system;
/*     */   }
/*     */ 
/*     */   public void evaluate(String collectionFolder, String questionFile, WritableSheet sheet)
/*     */   {
/*     */     try
/*     */     {
/*  33 */       BufferedReader br = FileUtil.getTextReader(questionFile);
/*  34 */       sheet.addCell(new Label(0, 0, "ID", sheet.getCell(0, 0).getCellFormat()));
/*  35 */       for (int i = 1; i <= 5; i++) {
/*  36 */         sheet.addCell(new Label(i, 0, "Top " + i, sheet.getCell(i, 0).getCellFormat()));
/*     */       }
/*  38 */       int row = 1;
/*     */       String line;
/*  39 */       while ((line = br.readLine()) != null)
/*     */       {
/*  40 */         String qno = line.substring(0, line.indexOf('\t'));
/*  41 */         String question = line.substring(line.indexOf('\t') + 1);
/*  42 */         System.out.println("Processing Question #" + qno);
/*  43 */         CollectionReader reader = new BasicCollectionReader(collectionFolder + "/" + qno + ".collection", collectionFolder + "/" + qno + ".index");
/*  44 */         ArrayList list = this.system.answer(reader, this.system.generateQuery(question));
/*  45 */         sheet.addCell(new Number(0, row, Integer.parseInt(qno)));
/*  46 */         int num = Math.min(list.size(), 5);
/*  47 */         for (int i = 1; i <= num; i++) {
/*  48 */           Candidate curCand = (Candidate)list.get(i - 1);
/*  49 */           if ((curCand.getWordNum() == 1) && (curCand.getStartingWord().isNumber()))
/*  50 */             sheet.addCell(new Number(i, row, Double.parseDouble(curCand.getStartingWord().getContent())));
/*     */           else
/*  52 */             sheet.addCell(new Label(i, row, curCand.toString()));
/*     */         }
/*  54 */         row++;
/*     */       }
/*  56 */       br.close();
/*     */     }
/*     */     catch (Exception e) {
/*  59 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void evaluate(String collectionFolder, String questionFile, String answerFile)
/*     */   {
/*     */     try
/*     */     {
/*  72 */       int totalSent = 0;
/*  73 */       int totalSentFiltered = 0;
/*     */ 
/*  75 */       BufferedReader br = FileUtil.getTextReader(questionFile);
/*  76 */       BufferedWriter bw = FileUtil.getTextWriter(answerFile);
/*     */       String line;
/*  77 */       while ((line = br.readLine()) != null)
/*     */       {
/*  78 */         String qno = line.substring(0, line.indexOf('\t'));
/*  79 */         String question = line.substring(line.indexOf('\t') + 1);
/*  80 */         System.out.println("Processing Question #" + qno);
/*  81 */         CollectionReader reader = new BasicCollectionReader(collectionFolder + "/" + qno + ".collection", collectionFolder + "/" + qno + ".index");
/*  82 */         ArrayList list = this.system.answer(reader, this.system.generateQuery(question));
/*  83 */         bw.write(qno);
/*  84 */         int num = Math.min(list.size(), 5);
/*  85 */         for (int i = 0; i < num; i++) {
/*  86 */           bw.write(9);
/*  87 */           bw.write(((Candidate)list.get(i)).toString());
/*     */         }
/*  89 */         bw.write(10);
/*  90 */         bw.flush();
/*  91 */         totalSent += this.system.getCandidateBase().getSentenceNum();
/*  92 */         totalSentFiltered += this.system.getCandidateBase().getSentenceFiltered();
/*     */       }
/*  94 */       br.close();
/*  95 */       bw.close();
/*  96 */       System.out.println("total snetence: " + totalSent);
/*  97 */       System.out.println("total snetence filtered: " + totalSentFiltered);
/*     */     }
/*     */     catch (Exception e) {
/* 100 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void evaluate(String questionFile, String answerFile)
/*     */   {
/*     */     try
/*     */     {
/* 112 */       BufferedReader br = FileUtil.getTextReader(questionFile);
/* 113 */       BufferedWriter bw = FileUtil.getTextWriter(answerFile);
/*     */       String line;
/* 114 */       while ((line = br.readLine()) != null)
/*     */       {
/* 115 */         String qno = line.substring(0, line.indexOf('\t'));
/* 116 */         String question = line.substring(line.indexOf('\t') + 1);
/* 117 */         System.out.println("Processing Question #" + qno);
/* 118 */         ArrayList list = this.system.answer(question);
/* 119 */         bw.write(qno);
/* 120 */         int num = Math.min(list.size(), 5);
/* 121 */         for (int i = 0; i < num; i++) {
/* 122 */           bw.write(9);
/* 123 */           bw.write(((Candidate)list.get(i)).toString());
/*     */         }
/* 125 */         bw.write(10);
/* 126 */         bw.flush();
/* 127 */         AbstractSearchEngine.sleepManySeconds(1L);
/*     */       }
/* 129 */       br.close();
/* 130 */       bw.close();
/*     */     }
/*     */     catch (Exception e) {
/* 133 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.eva.Evaluate
 * JD-Core Version:    0.6.2
 */