/*    */ package edu.drexel.cis.dragon.ml.seqmodel.model;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public abstract class AbstractModel
/*    */   implements ModelGraph
/*    */ {
/*    */   protected int numLabels;
/*    */   protected String name;
/*    */ 
/*    */   public AbstractModel(int labelNum, String name)
/*    */   {
/* 19 */     this.numLabels = labelNum;
/* 20 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public int getLabelNum() {
/* 24 */     return this.numLabels;
/*    */   }
/*    */ 
/*    */   public int getStateNum() {
/* 28 */     return this.numLabels;
/*    */   }
/*    */ 
/*    */   public int getOriginalLabelNum() {
/* 32 */     return this.numLabels;
/*    */   }
/*    */ 
/*    */   public int getMarkovOrder() {
/* 36 */     return 1;
/*    */   }
/*    */ 
/*    */   public boolean mapStateToLabel(DataSequence seq)
/*    */   {
/* 41 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean mapLabelToState(DataSequence seq)
/*    */   {
/* 46 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean mapLabelToState(DataSequence data, int len, int start)
/*    */   {
/* 51 */     return true;
/*    */   }
/*    */ 
/*    */   public void printGraph() {
/* 55 */     System.out.println("Numnodes = " + getStateNum() + " NumEdges " + getEdgeNum());
/* 56 */     EdgeIterator iter = getEdgeIterator();
/* 57 */     for (iter.start(); iter.hasNext(); ) {
/* 58 */       Edge edge = iter.next();
/* 59 */       System.out.println(edge.getStart() + "-->" + edge.getEnd());
/*    */     }
/* 61 */     System.out.print("Start states");
/* 62 */     for (int i = 0; i < getStartStateNum(); i++) {
/* 63 */       System.out.print(" " + getStartState(i));
/*    */     }
/* 65 */     System.out.println("");
/*    */ 
/* 67 */     System.out.print("End states");
/* 68 */     for (int i = 0; i < getEndStateNum(); i++) {
/* 69 */       System.out.print(" " + getEndState(i));
/*    */     }
/* 71 */     System.out.println("");
/*    */   }
/*    */ 
/*    */   public static ModelGraph getNewModelGraph(int numLabels, String modelSpecs)
/*    */   {
/*    */     try
/*    */     {
/* 78 */       modelSpecs = modelSpecs.toLowerCase().trim();
/* 79 */       if (modelSpecs.equalsIgnoreCase("noEdge"))
/* 80 */         return new NoEdgeModel(numLabels);
/* 81 */       if ((modelSpecs.equalsIgnoreCase("naive")) || (modelSpecs.equalsIgnoreCase("semi-markov")))
/* 82 */         return new CompleteModel(numLabels);
/* 83 */       if ((modelSpecs.startsWith("naive")) && (modelSpecs.indexOf(',') < 0)) {
/* 84 */         int markovOrder = Integer.parseInt(modelSpecs.substring(modelSpecs.indexOf(' ') + 1));
/* 85 */         return new CompleteModel(numLabels, markovOrder);
/*    */       }
/* 87 */       return new NestedModel(numLabels, modelSpecs);
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 91 */       e.printStackTrace();
/* 92 */     }return null;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.model.AbstractModel
 * JD-Core Version:    0.6.2
 */