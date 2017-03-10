/*    */ package edu.drexel.cis.dragon.ir.topicmodel;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*    */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*    */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*    */ import edu.drexel.cis.dragon.matrix.vector.DoubleVector;
/*    */ import java.io.PrintStream;
/*    */ import java.io.PrintWriter;
/*    */ 
/*    */ public class AbstractModel
/*    */ {
/*    */   protected int iterations;
/*    */   protected int seed;
/*    */   protected PrintWriter statusOut;
/*    */ 
/*    */   public AbstractModel()
/*    */   {
/* 22 */     this.seed = -1;
/* 23 */     this.iterations = 100;
/*    */   }
/*    */ 
/*    */   public int getIterationNum() {
/* 27 */     return this.iterations;
/*    */   }
/*    */ 
/*    */   public void setIterationNum(int num) {
/* 31 */     this.iterations = num;
/*    */   }
/*    */ 
/*    */   public void setRandomSeed(int seed) {
/* 35 */     this.seed = seed;
/*    */   }
/*    */ 
/*    */   public void setStatusOut(PrintWriter out) {
/* 39 */     this.statusOut = out;
/*    */   }
/*    */ 
/*    */   protected void printStatus(String line) {
/*    */     try {
/* 44 */       System.out.println(line);
/* 45 */       if (this.statusOut != null) {
/* 46 */         this.statusOut.write(line + "\n");
/* 47 */         this.statusOut.flush();
/*    */       }
/*    */     }
/*    */     catch (Exception e) {
/* 51 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   protected DoubleVector getBkgModel(IndexReader indexReader)
/*    */   {
/* 59 */     DoubleVector bkgModel = new DoubleVector(indexReader.getCollection().getTermNum());
/* 60 */     for (int i = 0; i < bkgModel.size(); i++)
/* 61 */       bkgModel.set(i, indexReader.getIRTerm(i).getFrequency());
/* 62 */     bkgModel.multiply(1.0D / indexReader.getCollection().getTermCount());
/* 63 */     return bkgModel;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.topicmodel.AbstractModel
 * JD-Core Version:    0.6.2
 */
