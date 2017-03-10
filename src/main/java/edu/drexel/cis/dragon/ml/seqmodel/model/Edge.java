/*    */ package edu.drexel.cis.dragon.ml.seqmodel.model;
/*    */ 
/*    */ public class Edge
/*    */   implements Comparable
/*    */ {
/*    */   private int start;
/*    */   private int end;
/*    */ 
/*    */   public Edge()
/*    */   {
/* 17 */     this.start = -1;
/* 18 */     this.end = -1;
/*    */   }
/*    */ 
/*    */   public Edge(int s, int e) {
/* 22 */     this.start = s;
/* 23 */     this.end = e;
/*    */   }
/*    */ 
/*    */   public int getStart() {
/* 27 */     return this.start;
/*    */   }
/*    */ 
/*    */   public void setStart(int start) {
/* 31 */     this.start = start;
/*    */   }
/*    */ 
/*    */   public int getEnd() {
/* 35 */     return this.end;
/*    */   }
/*    */ 
/*    */   public void setEnd(int end) {
/* 39 */     this.end = end;
/*    */   }
/*    */ 
/*    */   String tostring() {
/* 43 */     return this.start + " -> " + this.end;
/*    */   }
/*    */ 
/*    */   public int compareTo(Object o) {
/* 47 */     Edge e = (Edge)o;
/* 48 */     return this.start != e.start ? this.start - e.start : this.end - e.end;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.model.Edge
 * JD-Core Version:    0.6.2
 */