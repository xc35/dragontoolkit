/*    */ package edu.drexel.cis.dragon.nlp.ontology.umls;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.ontology.BasicTerm;
/*    */ import edu.drexel.cis.dragon.nlp.ontology.BasicTermList;
/*    */ import edu.drexel.cis.dragon.nlp.ontology.Ontology;
/*    */ import edu.drexel.cis.dragon.nlp.ontology.SemanticNet;
/*    */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*    */ import edu.drexel.cis.dragon.util.EnvVariable;
/*    */ import edu.drexel.cis.dragon.util.FileUtil;
/*    */ import java.io.File;
/*    */ import java.io.PrintStream;
/*    */ import java.util.Date;
/*    */ 
/*    */ public class UmlsFileBackedOntology extends UmlsExactOntology
/*    */   implements Ontology
/*    */ {
/*    */   private UmlsCUIList cuiList;
/*    */   private BasicTermList termList;
/*    */   private File directory;
/*    */   private UmlsSemanticNet snNet;
/*    */ 
/*    */   public UmlsFileBackedOntology(Lemmatiser lemmatiser)
/*    */   {
/* 24 */     this(EnvVariable.getDragonHome() + "/nlpdata/umls", lemmatiser);
/*    */   }
/*    */ 
/*    */   public UmlsFileBackedOntology(String workDir, Lemmatiser lemmatiser) {
/* 28 */     super(lemmatiser);
/* 29 */     if ((!FileUtil.exist(workDir)) && (FileUtil.exist(EnvVariable.getDragonHome() + "/" + workDir)))
/* 30 */       workDir = EnvVariable.getDragonHome() + "/" + workDir;
/* 31 */     this.directory = new File(workDir);
/* 32 */     this.cuiList = new UmlsCUIList(this.directory + "/cui.list");
/* 33 */     this.termList = new BasicTermList(this.directory + "/termindex.list");
/* 34 */     UmlsSTYList styList = new UmlsSTYList(this.directory + "/semantictype.list");
/* 35 */     UmlsRelationNet relationNet = new UmlsRelationNet(this.directory + "/semanticrelation.list", styList);
/* 36 */     this.snNet = new UmlsSemanticNet(this, styList, relationNet);
/* 37 */     System.out.println(new Date() + " Ontology Loading Done!");
/*    */   }
/*    */ 
/*    */   public SemanticNet getSemanticNet() {
/* 41 */     return this.snNet;
/*    */   }
/*    */ 
/*    */   public String[] getSemanticType(String cui)
/*    */   {
/* 48 */     UmlsCUI cur = this.cuiList.lookup(cui);
/* 49 */     if (cur == null) {
/* 50 */       return null;
/*    */     }
/* 52 */     return cur.getAllSTY();
/*    */   }
/*    */ 
/*    */   public String[] getCUI(String term)
/*    */   {
/* 58 */     BasicTerm cur = this.termList.lookup(term);
/* 59 */     if (cur == null) {
/* 60 */       return null;
/*    */     }
/* 62 */     return cur.getAllCUI();
/*    */   }
/*    */ 
/*    */   public boolean isTerm(String term) {
/* 66 */     return this.termList.lookup(term) != null;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.ontology.umls.UmlsFileBackedOntology
 * JD-Core Version:    0.6.2
 */