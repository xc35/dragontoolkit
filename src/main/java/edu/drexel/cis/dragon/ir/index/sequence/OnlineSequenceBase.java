/*    */ package edu.drexel.cis.dragon.ir.index.sequence;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class OnlineSequenceBase
/*    */   implements SequenceReader, SequenceWriter
/*    */ {
/*    */   private ArrayList list;
/*    */   private boolean initialized;
/*    */ 
/*    */   public OnlineSequenceBase()
/*    */   {
/* 18 */     this.list = new ArrayList();
/* 19 */     this.initialized = false;
/*    */   }
/*    */ 
/*    */   public void initialize() {
/* 23 */     if (this.initialized)
/* 24 */       return;
/* 25 */     this.list.clear();
/*    */   }
/*    */ 
/*    */   public void close() {
/* 29 */     this.initialized = false;
/*    */   }
/*    */ 
/*    */   public int[] getSequence(int index) {
/* 33 */     return (int[])this.list.get(index);
/*    */   }
/*    */ 
/*    */   public int getSequenceLength(int index)
/*    */   {
/* 39 */     int[] seq = getSequence(index);
/* 40 */     if (seq == null) {
/* 41 */       return 0;
/*    */     }
/* 43 */     return seq.length;
/*    */   }
/*    */ 
/*    */   public boolean addSequence(int index, int[] seq) {
/* 47 */     if (index < this.list.size())
/* 48 */       return false;
/* 49 */     while (this.list.size() < index)
/* 50 */       this.list.add(null);
/* 51 */     this.list.add(seq);
/* 52 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.sequence.OnlineSequenceBase
 * JD-Core Version:    0.6.2
 */