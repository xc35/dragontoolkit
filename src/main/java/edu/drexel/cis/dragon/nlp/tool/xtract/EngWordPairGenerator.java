/*    */ package edu.drexel.cis.dragon.nlp.tool.xtract;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.Sentence;
/*    */ import edu.drexel.cis.dragon.nlp.Word;
/*    */ import edu.drexel.cis.dragon.util.SortedArray;
/*    */ 
/*    */ public class EngWordPairGenerator
/*    */   implements WordPairGenerator
/*    */ {
/*    */   protected int maxSpan;
/*    */   protected SortedArray list;
/*    */ 
/*    */   public EngWordPairGenerator(int maxSpan)
/*    */   {
/* 21 */     this.maxSpan = maxSpan;
/* 22 */     this.list = new SortedArray();
/*    */   }
/*    */ 
/*    */   public void setMaxSpan(int maxSpan) {
/* 26 */     this.maxSpan = maxSpan;
/*    */   }
/*    */ 
/*    */   public int generate(Sentence sent)
/*    */   {
/* 34 */     this.list.clear();
/* 35 */     Word start = sent.getFirstWord();
/* 36 */     while (start != null)
/* 37 */       if ((start.getPOSIndex() != 1) && (start.getPOSIndex() != 3)) {
/* 38 */         start = start.next;
/*    */       }
/*    */       else
/*    */       {
/* 42 */         Word end = start.next;
/* 43 */         int span = 1;
/* 44 */         while ((end != null) && (span <= this.maxSpan)) {
/* 45 */           int pos = end.getPOSIndex();
/* 46 */           if (pos == 1) {
/* 47 */             if (start.getIndex() <= end.getIndex()) {
/* 48 */               WordPairStat curPair = new WordPairStat(start.getIndex(), end.getIndex(), this.maxSpan);
/* 49 */               curPair.addFrequency(span, 1);
/* 50 */               if (!this.list.add(curPair)) {
/* 51 */                 curPair = (WordPairStat)this.list.get(this.list.insertedPos());
/* 52 */                 curPair.addFrequency(span, 1);
/*    */               }
/*    */             }
/*    */             else {
/* 56 */               WordPairStat curPair = new WordPairStat(end.getIndex(), start.getIndex(), this.maxSpan);
/* 57 */               curPair.addFrequency(0 - span, 1);
/* 58 */               if (!this.list.add(curPair)) {
/* 59 */                 curPair = (WordPairStat)this.list.get(this.list.insertedPos());
/* 60 */                 curPair.addFrequency(0 - span, 1);
/*    */               }
/*    */             }
/*    */           }
/*    */           else {
/* 65 */             if (pos != 3)
/*    */               break;
/*    */           }
/* 68 */           span++;
/* 69 */           end = end.next;
/*    */         }
/* 71 */         start = start.next;
/*    */       }
/* 73 */     return this.list.size();
/*    */   }
/*    */ 
/*    */   public WordPairStat getWordPairs(int index) {
/* 77 */     return (WordPairStat)this.list.get(index);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.tool.xtract.EngWordPairGenerator
 * JD-Core Version:    0.6.2
 */