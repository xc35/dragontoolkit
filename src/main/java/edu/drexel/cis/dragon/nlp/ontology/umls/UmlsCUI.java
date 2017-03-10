/*    */ package edu.drexel.cis.dragon.nlp.ontology.umls;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.compare.IndexSortable;
/*    */ 
/*    */ public class UmlsCUI
/*    */   implements IndexSortable, Comparable
/*    */ {
/*    */   private String[] arrSTY;
/*    */   private int index;
/*    */   private String cui;
/*    */   private String name;
/*    */ 
/*    */   public UmlsCUI(int index, String cui, String[] stys)
/*    */   {
/* 20 */     this.cui = cui;
/* 21 */     this.index = index;
/* 22 */     this.arrSTY = stys;
/* 23 */     this.name = null;
/*    */   }
/*    */ 
/*    */   public UmlsCUI(int index, String cui, String[] stys, String name) {
/* 27 */     this.cui = cui;
/* 28 */     this.index = index;
/* 29 */     this.arrSTY = stys;
/* 30 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public int compareTo(Object obj)
/*    */   {
/* 36 */     String objValue = ((UmlsCUI)obj).toString();
/* 37 */     return toString().compareTo(objValue);
/*    */   }
/*    */ 
/*    */   public String getCUI() {
/* 41 */     return this.cui;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 45 */     return this.name;
/*    */   }
/*    */ 
/*    */   public void setName(String name) {
/* 49 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public int getIndex() {
/* 53 */     return this.index;
/*    */   }
/*    */ 
/*    */   public void setIndex(int index) {
/* 57 */     this.index = index;
/*    */   }
/*    */ 
/*    */   public int getSTYNum() {
/* 61 */     if (this.arrSTY == null) {
/* 62 */       return 0;
/*    */     }
/* 64 */     return this.arrSTY.length;
/*    */   }
/*    */ 
/*    */   public String getSTY(int index) {
/* 68 */     return this.arrSTY[index];
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 72 */     return this.cui;
/*    */   }
/*    */ 
/*    */   public String[] getAllSTY() {
/* 76 */     return this.arrSTY;
/*    */   }
/*    */ 
/*    */   public boolean addSTY(String sty) {
/* 80 */     if (this.arrSTY == null)
/*    */     {
/* 82 */       this.arrSTY = new String[1];
/* 83 */       this.arrSTY[0] = sty;
/* 84 */       return true;
/*    */     }
/* 86 */     if (sty.compareTo(this.arrSTY[(this.arrSTY.length - 1)]) > 0) {
/* 87 */       String[] arrTemp = new String[this.arrSTY.length + 1];
/* 88 */       System.arraycopy(this.arrSTY, 0, arrTemp, 0, this.arrSTY.length);
/* 89 */       arrTemp[this.arrSTY.length] = sty;
/* 90 */       this.arrSTY = arrTemp;
/* 91 */       return true;
/*    */     }
/*    */ 
/* 94 */     return false;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.ontology.umls.UmlsCUI
 * JD-Core Version:    0.6.2
 */