/*    */ package edu.drexel.cis.dragon.nlp.ontology.umls;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.ontology.Ontology;
/*    */ import edu.drexel.cis.dragon.nlp.ontology.SemanticNet;
/*    */ 
/*    */ public class UmlsSemanticNet
/*    */   implements SemanticNet
/*    */ {
/*    */   private Ontology ontology;
/*    */   private UmlsSTYList styList;
/*    */   private UmlsRelationNet relationNet;
/*    */ 
/*    */   public UmlsSemanticNet(Ontology ontology, UmlsSTYList styList, UmlsRelationNet relationNet)
/*    */   {
/* 19 */     this.ontology = ontology;
/* 20 */     this.styList = styList;
/* 21 */     this.relationNet = relationNet;
/*    */   }
/*    */ 
/*    */   public String[] getRelations(String[] arrFirstST, String[] arrSecondST) {
/* 25 */     return this.relationNet.getRelations(arrFirstST, arrSecondST);
/*    */   }
/*    */ 
/*    */   public String[] getRelations(String firstST, String secondST)
/*    */   {
/* 31 */     String relation = this.relationNet.getRelations(firstST, secondST);
/* 32 */     if (relation == null) {
/* 33 */       return null;
/*    */     }
/*    */ 
/* 36 */     String[] arrRelation = new String[1];
/* 37 */     arrRelation[0] = relation;
/* 38 */     return arrRelation;
/*    */   }
/*    */ 
/*    */   public boolean isSemanticRelated(String[] arrFirstST, String[] arrSecondST)
/*    */   {
/* 43 */     return this.relationNet.isSemanticRelated(arrFirstST, arrSecondST);
/*    */   }
/*    */ 
/*    */   public boolean isSemanticRelated(String firstST, String secondST) {
/* 47 */     return this.relationNet.isSemanticRelated(firstST, secondST);
/*    */   }
/*    */ 
/*    */   public String getSemanticTypeDesc(String id)
/*    */   {
/* 52 */     return this.styList.lookup(id).getDescription();
/*    */   }
/*    */ 
/*    */   public String getRelationDesc(String id) {
/* 56 */     return this.styList.lookup(id).getDescription();
/*    */   }
/*    */ 
/*    */   public String getHierarchy(String id) {
/* 60 */     return this.styList.lookup(id).getHier();
/*    */   }
/*    */ 
/*    */   public Ontology getOntology() {
/* 64 */     return this.ontology;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.ontology.umls.UmlsSemanticNet
 * JD-Core Version:    0.6.2
 */