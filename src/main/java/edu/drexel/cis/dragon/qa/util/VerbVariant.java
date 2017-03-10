/*    */ package edu.drexel.cis.dragon.qa.util;
/*    */ 
/*    */ public class VerbVariant
/*    */   implements Comparable
/*    */ {
/*    */   private String past;
/*    */   private String particle;
/*    */   private String base;
/*    */ 
/*    */   public VerbVariant(String base, String past, String particle)
/*    */   {
/*  7 */     this.base = base;
/*  8 */     this.particle = particle;
/*  9 */     this.past = past;
/*    */   }
/*    */ 
/*    */   public String getBaseForm() {
/* 13 */     return this.base;
/*    */   }
/*    */ 
/*    */   public String getSimplePast() {
/* 17 */     return this.past;
/*    */   }
/*    */ 
/*    */   public String getPastParticle() {
/* 21 */     return this.particle;
/*    */   }
/*    */ 
/*    */   public int compareTo(Object obj) {
/* 25 */     return this.base.compareToIgnoreCase(((VerbVariant)obj).getBaseForm());
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.util.VerbVariant
 * JD-Core Version:    0.6.2
 */