/*    */ package edu.drexel.cis.dragon.ml.seqmodel.evaluate;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.Dataset;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class TokenEvaluator extends AbstractEvaluator
/*    */ {
/*    */   private int labelNum;
/*    */   private int[] truePos;
/*    */   private int[] totalMarkedPos;
/*    */   private int[] totalPos;
/*    */   private int[][] confuseMatrix;
/*    */ 
/*    */   public TokenEvaluator(int labelNum)
/*    */   {
/* 22 */     this.labelNum = labelNum;
/* 23 */     this.truePos = new int[labelNum];
/* 24 */     this.totalMarkedPos = new int[labelNum];
/* 25 */     this.totalPos = new int[labelNum];
/* 26 */     this.confuseMatrix = new int[labelNum][labelNum];
/*    */   }
/*    */ 
/*    */   public void evaluate(Dataset human, Dataset machine)
/*    */   {
/* 34 */     if (machine.size() != human.size()) {
/* 35 */       System.out.println("Length Mismatch - Auto: " + machine.size() + " Man: " + human.size());
/* 36 */       return;
/*    */     }
/*    */ 
/* 39 */     human.startScan();
/* 40 */     machine.startScan();
/* 41 */     while ((human.hasNext()) && (machine.hasNext())) {
/* 42 */       DataSequence humanSeq = human.next();
/* 43 */       DataSequence machineSeq = machine.next();
/* 44 */       if (humanSeq.length() != machineSeq.length()) {
/* 45 */         System.out.println("Length Mismatch - Manual: " + humanSeq.length() + " Auto: " + machineSeq.length());
/*    */       }
/*    */       else {
/* 48 */         int len = humanSeq.length();
/* 49 */         for (int i = 0; i < len; i++) {
/* 50 */           int machineLabel = machineSeq.getOriginalLabel(i);
/* 51 */           this.totalLabels += 1;
/* 52 */           if (machineLabel >= 0)
/*    */           {
/* 54 */             int humanLabel = humanSeq.getOriginalLabel(i);
/* 55 */             this.totalMarkedPos[machineLabel] += 1;
/* 56 */             this.annotatedLabels += 1;
/* 57 */             this.totalPos[humanLabel] += 1;
/* 58 */             this.confuseMatrix[humanLabel][machineLabel] += 1;
/* 59 */             if (humanLabel == machineLabel) {
/* 60 */               this.correctAnnotatedLabels += 1;
/* 61 */               this.truePos[humanLabel] += 1;
/*    */             }
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/* 66 */     System.out.println("\n\nCalculations:");
/* 67 */     System.out.println();
/* 68 */     System.out.println("Label\tTrue+\tMarked+\tActual+\tPrec.\tRecall\tF1");
/*    */ 
/* 70 */     for (int i = 0; i < this.labelNum; i++) {
/* 71 */       double prec = this.totalMarkedPos[i] == 0 ? 0.0D : this.truePos[i] * 100000 / this.totalMarkedPos[i] / 1000.0D;
/* 72 */       double recall = this.totalPos[i] == 0 ? 0.0D : this.truePos[i] * 100000 / this.totalPos[i] / 1000.0D;
/* 73 */       System.out.println(i + ":\t" + this.truePos[i] + "\t" + this.totalMarkedPos[i] + "\t" + this.totalPos[i] + "\t" + prec + 
/* 74 */         "\t" + recall + "\t" + 2.0D * prec * recall / (prec + recall));
/*    */     }
/*    */ 
/* 78 */     System.out.println("---------------------------------------------------------");
/* 79 */     System.out.println("Ov:\t" + this.correctAnnotatedLabels + "\t" + this.annotatedLabels + "\t" + this.totalLabels + 
/* 80 */       "\t" + precision() + "\t" + recall() + "\t" + 2.0D * precision() * recall() / (precision() + recall()));
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.evaluate.TokenEvaluator
 * JD-Core Version:    0.6.2
 */