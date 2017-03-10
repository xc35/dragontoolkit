/*    */ package edu.drexel.cis.dragon.nlp.ontology.umls;
/*    */ 
/*    */ public class UmlsSTY
/*    */   implements Comparable
/*    */ {
/*    */   private int index;
/*    */   private String sty;
/*    */   private String hier;
/*    */   private String desc;
/*    */   private boolean isRelation;
/*    */ 
/*    */   public UmlsSTY(int index, String sty, String desc, String hier, boolean isRelation)
/*    */   {
/* 18 */     this.index = index;
/* 19 */     this.sty = sty;
/* 20 */     this.hier = hier;
/* 21 */     this.desc = desc;
/* 22 */     this.isRelation = isRelation;
/*    */   }
/*    */ 
/*    */   public int compareTo(Object obj)
/*    */   {
/* 28 */     String objValue = ((UmlsSTY)obj).toString();
/* 29 */     return toString().compareTo(objValue);
/*    */   }
/*    */ 
/*    */   public int getIndex() {
/* 33 */     return this.index;
/*    */   }
/*    */ 
/*    */   public String getSTY() {
/* 37 */     return this.sty;
/*    */   }
/*    */ 
/*    */   public String getHier() {
/* 41 */     return this.hier;
/*    */   }
/*    */ 
/*    */   public boolean isRelation() {
/* 45 */     return this.isRelation;
/*    */   }
/*    */ 
/*    */   public boolean isSemanticType() {
/* 49 */     return !this.isRelation;
/*    */   }
/*    */ 
/*    */   public String getDescription() {
/* 53 */     return this.desc;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 57 */     return this.sty;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.ontology.umls.UmlsSTY
 * JD-Core Version:    0.6.2
 */