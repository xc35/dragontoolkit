/*    */ package edu.drexel.cis.dragon.ml.seqmodel.feature;
/*    */ 
/*    */ public class FeatureIdentifier
/*    */   implements Comparable
/*    */ {
/*    */   private int id;
/*    */   private String name;
/*    */   private int state;
/*    */ 
/*    */   public FeatureIdentifier(String name, int id, int state)
/*    */   {
/* 20 */     this.name = name;
/* 21 */     this.id = id;
/* 22 */     this.state = state;
/*    */   }
/*    */ 
/*    */   public FeatureIdentifier(String strRep)
/*    */   {
/* 28 */     int start = strRep.indexOf(':');
/* 29 */     this.name = strRep.substring(0, start);
/* 30 */     int end = strRep.lastIndexOf(':');
/* 31 */     this.id = Integer.parseInt(strRep.substring(start + 1, end));
/* 32 */     this.state = Integer.parseInt(strRep.substring(end + 1));
/*    */   }
/*    */ 
/*    */   public FeatureIdentifier copy() {
/* 36 */     return new FeatureIdentifier(this.name, this.id, this.state);
/*    */   }
/*    */ 
/*    */   public int hashCode() {
/* 40 */     return this.id;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object o) {
/* 44 */     return this.id == ((FeatureIdentifier)o).getId();
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 48 */     return this.name + ":" + this.id + ":" + this.state;
/*    */   }
/*    */ 
/*    */   public int getId() {
/* 52 */     return this.id;
/*    */   }
/*    */ 
/*    */   public void setId(int id) {
/* 56 */     this.id = id;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 60 */     return this.name;
/*    */   }
/*    */ 
/*    */   public void setName(String name) {
/* 64 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public int getState() {
/* 68 */     return this.state;
/*    */   }
/*    */ 
/*    */   public void setState(int state) {
/* 72 */     this.state = state;
/*    */   }
/*    */ 
/*    */   public int compareTo(Object o)
/*    */   {
/* 78 */     int newId = ((FeatureIdentifier)o).getId();
/* 79 */     if (this.id > newId)
/* 80 */       return 1;
/* 81 */     if (this.id == newId) {
/* 82 */       return 0;
/*    */     }
/* 84 */     return -1;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.FeatureIdentifier
 * JD-Core Version:    0.6.2
 */