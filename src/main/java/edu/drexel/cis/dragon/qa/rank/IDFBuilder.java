/*    */ package edu.drexel.cis.dragon.qa.rank;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.Document;
/*    */ import edu.drexel.cis.dragon.nlp.Token;
/*    */ import edu.drexel.cis.dragon.nlp.extract.BasicTokenExtractor;
/*    */ import edu.drexel.cis.dragon.util.SortedArray;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class IDFBuilder
/*    */ {
/*    */   private BasicTokenExtractor extractor;
/*    */   private SortedArray list;
/*    */   private int docNum;
/*    */ 
/*    */   public IDFBuilder()
/*    */   {
/* 14 */     this.extractor = new BasicTokenExtractor(null);
/* 15 */     this.list = new SortedArray();
/* 16 */     this.docNum = 0;
/*    */   }
/*    */ 
/*    */   public void initialize() {
/* 20 */     this.list.clear();
/* 21 */     this.docNum = 0;
/*    */   }
/*    */ 
/*    */   public void add(Document doc)
/*    */   {
/* 29 */     ArrayList tokenList = this.extractor.extractFromDoc(doc);
/* 30 */     if ((tokenList == null) || (tokenList.size() == 0)) {
/* 31 */       return;
/*    */     }
/* 33 */     this.docNum += 1;
/* 34 */     SortedArray sortedList = new SortedArray();
/* 35 */     for (int i = 0; i < tokenList.size(); i++) {
/* 36 */       sortedList.add(tokenList.get(i));
/*    */     }
/* 38 */     for (int i = 0; i < sortedList.size(); i++)
/* 39 */       if (!this.list.add(sortedList.get(i)))
/* 40 */         ((Token)this.list.get(this.list.insertedPos())).addFrequency(1);
/*    */   }
/*    */ 
/*    */   public double getIDF(String word)
/*    */   {
/* 47 */     int pos = this.list.binarySearch(new Token(word));
/* 48 */     if (pos < 0) {
/* 49 */       return -1.0D;
/*    */     }
/* 51 */     return Math.log(this.docNum / ((Token)this.list.get(pos)).getFrequency());
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.rank.IDFBuilder
 * JD-Core Version:    0.6.2
 */