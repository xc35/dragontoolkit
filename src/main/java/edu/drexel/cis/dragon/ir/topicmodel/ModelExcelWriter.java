/*     */ package edu.drexel.cis.dragon.ir.topicmodel;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.compare.WeightComparator;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import jxl.Workbook;
/*     */ import jxl.write.Label;
/*     */ import jxl.write.Number;
/*     */ import jxl.write.WritableCellFormat;
/*     */ import jxl.write.WritableFont;
/*     */ import jxl.write.WritableSheet;
/*     */ import jxl.write.WritableWorkbook;
/*     */ 
/*     */ public class ModelExcelWriter
/*     */ {
/*     */   public void write(TopicModel model, int top, String outputFile)
/*     */   {
/*  25 */     write(model, null, top, outputFile);
/*     */   }
/*     */ 
/*     */   public void write(TopicModel model, String[] termNameList, int top, String outputFile)
/*     */   {
/*     */     try
/*     */     {
/*  38 */       int topicNum = model.getTopicNum();
/*  39 */       WritableFont arial14font = new WritableFont(WritableFont.ARIAL, 14);
/*  40 */       WritableCellFormat arial14format = new WritableCellFormat(arial14font);
/*  41 */       WritableWorkbook workbook = Workbook.createWorkbook(new File(outputFile));
/*  42 */       WritableSheet sheet = workbook.createSheet("Topic Model", 0);
/*  43 */       if (termNameList == null) {
/*  44 */         termNameList = new String[model.getTermNum()];
/*  45 */         for (int i = 0; i < model.getTermNum(); i++) {
/*  46 */           termNameList[i] = model.getTermName(i);
/*     */         }
/*     */       }
/*  49 */       for (int i = 0; i < topicNum; i++) {
/*  50 */         sheet.addCell(new Label(i * 2, 0, "Topic" + (i + 1), arial14format));
/*  51 */         ArrayList termList = sortThemeTerm(model.getTopic(i), termNameList, top);
/*  52 */         saveTermList(sheet, i * 2, 1, termList);
/*     */       }
/*  54 */       workbook.write();
/*  55 */       workbook.close();
/*     */     }
/*     */     catch (Exception ex) {
/*  58 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(TwoDimensionModel model, int top, String outputFile) {
/*  63 */     write(model, null, null, top, outputFile);
/*     */   }
/*     */ 
/*     */   public void write(TwoDimensionModel model, String[] viewTermList, String[] topicTermList, int top, String outputFile)
/*     */   {
/*     */     try
/*     */     {
/*  77 */       int viewNum = model.getViewNum();
/*  78 */       int topicNum = model.getTopicNum();
/*  79 */       WritableFont arial14font = new WritableFont(WritableFont.ARIAL, 14);
/*  80 */       WritableCellFormat arial14format = new WritableCellFormat(arial14font);
/*  81 */       WritableWorkbook workbook = Workbook.createWorkbook(new File(outputFile));
/*  82 */       WritableSheet sheet = workbook.createSheet("View-Topic Model", 0);
/*  83 */       sheet.addCell(new Label(0, 0, "View-Topic", arial14format));
/*  84 */       sheet.addCell(new Label(1, 0, "View Content", arial14format));
/*  85 */       if (viewTermList == null) {
/*  86 */         viewTermList = new String[model.getViewTermNum()];
/*  87 */         for (int i = 0; i < viewTermList.length; i++)
/*  88 */           viewTermList[i] = model.getViewTermName(i);
/*     */       }
/*  90 */       if (topicTermList == null) {
/*  91 */         topicTermList = new String[model.getTopicTermNum()];
/*  92 */         for (int i = 0; i < topicTermList.length; i++) {
/*  93 */           topicTermList[i] = model.getTopicTermName(i);
/*     */         }
/*     */       }
/*  96 */       for (int i = 0; i < viewNum; i++) {
/*  97 */         sheet.addCell(new Label(0, (i + 1) * top + 1, "View " + (i + 1), arial14format));
/*  98 */         ArrayList termList = sortThemeTerm(model.getView(i), viewTermList, top);
/*  99 */         saveTermList(sheet, 1, (i + 1) * top + 1, termList);
/* 100 */         for (int j = 0; j < topicNum; j++) {
/* 101 */           termList = sortThemeTerm(model.getViewTopic(i, j), topicTermList, top);
/* 102 */           saveTermList(sheet, 2 + j * 2 + 1, (i + 1) * top + 1, termList);
/*     */         }
/*     */       }
/*     */ 
/* 106 */       for (int i = 0; i < topicNum; i++) {
/* 107 */         sheet.addCell(new Label(3 + i * 2, 0, "Topic " + (i + 1), arial14format));
/* 108 */         ArrayList termList = sortThemeTerm(model.getCommonTopic(i), topicTermList, top);
/* 109 */         saveTermList(sheet, 3 + i * 2, 1, termList);
/*     */       }
/* 111 */       workbook.write();
/* 112 */       workbook.close();
/*     */     }
/*     */     catch (Exception ex) {
/* 115 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void saveTermList(WritableSheet sheet, int colNum, int rowNum, ArrayList list)
/*     */   {
/*     */     try
/*     */     {
/* 124 */       for (int i = 0; i < list.size(); i++) {
/* 125 */         Token token = (Token)list.get(i);
/* 126 */         sheet.addCell(new Label(colNum, rowNum + i, token.getName()));
/* 127 */         sheet.addCell(new Number(colNum + 1, rowNum + i, token.getWeight()));
/*     */       }
/*     */     }
/*     */     catch (Exception ex) {
/* 131 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private ArrayList sortThemeTerm(double[] termProbs, String[] termKeyList, int top)
/*     */   {
/* 141 */     ArrayList newList = new ArrayList();
/* 142 */     ArrayList termList = new ArrayList();
/* 143 */     for (int i = 0; i < termProbs.length; i++) {
/* 144 */       String termKey = termKeyList[i];
/* 145 */       Token token = new Token(termKey);
/* 146 */       token.setIndex(i);
/* 147 */       token.setWeight(termProbs[i]);
/* 148 */       termList.add(token);
/*     */     }
/*     */ 
/* 151 */     Collections.sort(termList, new WeightComparator());
/* 152 */     if (top > termList.size()) {
/* 153 */       top = termList.size();
/*     */     }
/* 155 */     for (int i = 0; i < top; i++)
/* 156 */       newList.add(termList.get(i));
/* 157 */     return newList;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.topicmodel.ModelExcelWriter
 * JD-Core Version:    0.6.2
 */