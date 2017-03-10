/*    */ package edu.drexel.cis.dragon.ir.search.smooth;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*    */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*    */ import edu.drexel.cis.dragon.ir.query.RelSimpleQuery;
/*    */ import edu.drexel.cis.dragon.ir.query.SimpleTermPredicate;
/*    */ import edu.drexel.cis.dragon.ir.search.FullRankSearcher;
/*    */ import edu.drexel.cis.dragon.ir.search.Searcher;
/*    */ 
/*    */ public class DocTransMixtureWeightEM extends AbstractMixtureWeightEM
/*    */ {
/*    */   private RelSimpleQuery query;
/*    */   private QueryFirstTransSmoother querySmoother;
/*    */   private double queryWeight;
/*    */   private DocFirstTransSmoother docSmoother;
/*    */ 
/*    */   public DocTransMixtureWeightEM(IndexReader indexReader, int iterationNum, QueryFirstTransSmoother querySmoother)
/*    */   {
/* 27 */     super(indexReader, 2, iterationNum, false);
/* 28 */     this.querySmoother = querySmoother;
/*    */   }
/*    */ 
/*    */   public DocTransMixtureWeightEM(IndexReader indexReader, int iterationNum, DocFirstTransSmoother docSmoother) {
/* 32 */     super(indexReader, 2, iterationNum, true);
/* 33 */     this.docSmoother = docSmoother;
/*    */   }
/*    */ 
/*    */   protected void setInitialParameters(double[] arrCoefficient, IRDoc[] arrDoc)
/*    */   {
/* 42 */     arrCoefficient[0] = 0.7D;
/* 43 */     arrCoefficient[1] = 0.3D;
/*    */ 
/* 45 */     Searcher searcher = new FullRankSearcher(this.indexReader, new OkapiSmoother(this.indexReader.getCollection()));
/* 46 */     int docNum = searcher.search(this.query);
/*    */ 
/* 48 */     double sum = 0.0D;
/* 49 */     for (int i = 0; i < docNum; i++) {
/* 50 */       arrDoc[i] = this.indexReader.getDoc(i);
/* 51 */       arrDoc[i].setWeight(1.0D / docNum);
/* 52 */       if (arrDoc[i].getTermCount() == 0)
/* 53 */         arrDoc[i].setTermCount(1);
/* 54 */       if (arrDoc[i].getRelationCount() == 0) {
/* 55 */         arrDoc[i].setRelationCount(1);
/*    */       }
/*    */     }
/* 58 */     for (int i = 0; i < docNum; i++) {
/* 59 */       IRDoc curDoc = searcher.getIRDoc(i);
/* 60 */       arrDoc[curDoc.getIndex()].setWeight(curDoc.getWeight());
/* 61 */       sum += curDoc.getWeight();
/*    */     }
/* 63 */     for (int i = 0; i < arrDoc.length; i++) arrDoc[i].setWeight(arrDoc[i].getWeight() / sum); 
/*    */   }
/*    */ 
/*    */   protected void setDoc(IRDoc curDoc)
/*    */   {
/* 67 */     this.docSmoother.setDoc(curDoc);
/*    */   }
/*    */ 
/*    */   protected void setQueryTerm(SimpleTermPredicate curQueryTerm) {
/* 71 */     this.queryWeight = curQueryTerm.getWeight();
/* 72 */     this.querySmoother.setQueryTerm(curQueryTerm);
/*    */   }
/*    */ 
/*    */   protected void init(RelSimpleQuery query) {
/* 76 */     this.query = query;
/*    */   }
/*    */ 
/*    */   protected void getComponentValue(IRDoc curDoc, int freq, double[] arrComp) {
/* 80 */     this.querySmoother.setDoc(curDoc);
/* 81 */     arrComp[0] = (this.querySmoother.getBasicSmoother().getSmoothedProb(freq) / this.queryWeight);
/* 82 */     arrComp[1] = this.querySmoother.getTranslationProb(curDoc.getIndex());
/*    */   }
/*    */ 
/*    */   protected void getComponentValue(SimpleTermPredicate curQueryTerm, int freq, double[] arrComp) {
/* 86 */     this.docSmoother.setQueryTerm(curQueryTerm);
/* 87 */     arrComp[0] = (this.docSmoother.getBasicSmoother().getSmoothedProb(freq) / curQueryTerm.getWeight());
/* 88 */     arrComp[1] = this.docSmoother.getTranslationProb(curQueryTerm.getIndex());
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.smooth.DocTransMixtureWeightEM
 * JD-Core Version:    0.6.2
 */