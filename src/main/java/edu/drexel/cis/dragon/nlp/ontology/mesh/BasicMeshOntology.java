/*    */ package edu.drexel.cis.dragon.nlp.ontology.mesh;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.ontology.BasicOntology;
/*    */ import edu.drexel.cis.dragon.nlp.ontology.SimilarityMetric;
/*    */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*    */ import edu.drexel.cis.dragon.util.EnvVariable;
/*    */ import edu.drexel.cis.dragon.util.FileUtil;
/*    */ 
/*    */ public class BasicMeshOntology extends BasicOntology
/*    */ {
/*    */   private MeshSimilarity metric;
/*    */ 
/*    */   public BasicMeshOntology(Lemmatiser lemmatiser, int similarityMode)
/*    */   {
/* 21 */     this(lemmatiser, similarityMode, EnvVariable.getDragonHome() + "/nlpdata/mesh/mesh.hier", 
/* 21 */       EnvVariable.getDragonHome() + "/nlpdata/mesh/mesh.cui");
/*    */   }
/*    */ 
/*    */   public BasicMeshOntology(Lemmatiser lemmatiser, int similarityMode, String hierFile, String conceptFile) {
/* 25 */     super(conceptFile, lemmatiser);
/* 26 */     if ((!FileUtil.exist(hierFile)) && (FileUtil.exist(EnvVariable.getDragonHome() + "/" + hierFile)))
/* 27 */       hierFile = EnvVariable.getDragonHome() + "/" + hierFile;
/* 28 */     this.metric = new MeshSimilarity(hierFile, similarityMode);
/*    */   }
/*    */ 
/*    */   public SimilarityMetric getSimilarityMetric() {
/* 32 */     return this.metric;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.ontology.mesh.BasicMeshOntology
 * JD-Core Version:    0.6.2
 */