/*    */ package edu.drexel.cis.dragon.qa.qc;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.Sentence;
/*    */ import edu.drexel.cis.dragon.nlp.Word;
/*    */ import edu.drexel.cis.dragon.nlp.tool.xtract.EngWordPairGenerator;
/*    */ import edu.drexel.cis.dragon.nlp.tool.xtract.WordPairStat;
/*    */ import edu.drexel.cis.dragon.util.SortedArray;
/*    */ 
/*    */ public class SimpleWordPairGenerator extends EngWordPairGenerator
/*    */ {
/*    */   public SimpleWordPairGenerator(int maxSpan)
/*    */   {
/* 19 */     super(maxSpan);
/*    */   }
/*    */ 
/*    */   public int generate(Sentence sent)
/*    */   {
/* 27 */     this.list.clear();
/* 28 */     Word start = sent.getFirstWord();
/* 29 */     while (start != null)
/* 30 */       if (start.isPunctuation()) {
/* 31 */         start = start.next;
/*    */       }
/*    */       else
/*    */       {
/* 35 */         Word end = start.next;
/* 36 */         int span = 1;
/* 37 */         while ((end != null) && (span <= this.maxSpan)) {
/* 38 */           if (end.isPunctuation()) break;
/* 39 */           if (start.getIndex() <= end.getIndex()) {
/* 40 */             WordPairStat curPair = new WordPairStat(start.getIndex(), end.getIndex(), this.maxSpan);
/* 41 */             curPair.addFrequency(span, 1);
/* 42 */             if (!this.list.add(curPair)) {
/* 43 */               curPair = (WordPairStat)this.list.get(this.list.insertedPos());
/* 44 */               curPair.addFrequency(span, 1);
/*    */             }
/*    */           }
/*    */           else {
/* 48 */             WordPairStat curPair = new WordPairStat(end.getIndex(), start.getIndex(), this.maxSpan);
/* 49 */             curPair.addFrequency(0 - span, 1);
/* 50 */             if (!this.list.add(curPair)) {
/* 51 */               curPair = (WordPairStat)this.list.get(this.list.insertedPos());
/* 52 */               curPair.addFrequency(0 - span, 1);
/*    */             }
/*    */ 
/*    */           }
/*    */ 
/* 60 */           span++;
/* 61 */           end = end.next;
/*    */         }
/* 63 */         start = start.next;
/*    */       }
/* 65 */     return this.list.size();
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.qc.SimpleWordPairGenerator
 * JD-Core Version:    0.6.2
 */