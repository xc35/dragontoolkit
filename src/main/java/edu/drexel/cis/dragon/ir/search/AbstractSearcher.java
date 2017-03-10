/*    */ package edu.drexel.cis.dragon.ir.search;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*    */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*    */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*    */ import edu.drexel.cis.dragon.ir.query.IRQuery;
/*    */ import edu.drexel.cis.dragon.ir.query.Predicate;
/*    */ import edu.drexel.cis.dragon.ir.query.RelSimpleQuery;
/*    */ import edu.drexel.cis.dragon.ir.query.SimpleTermPredicate;
/*    */ import edu.drexel.cis.dragon.ir.search.smooth.Smoother;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public abstract class AbstractSearcher
/*    */   implements Searcher
/*    */ {
/*    */   protected IndexReader indexReader;
/*    */   protected ArrayList hitlist;
/*    */   protected IRQuery query;
/*    */   protected Smoother smoother;
/*    */   private boolean queryWeighting;
/*    */ 
/*    */   public AbstractSearcher(IndexReader indexReader, Smoother smoother)
/*    */   {
/* 25 */     this.indexReader = indexReader;
/* 26 */     this.smoother = smoother;
/* 27 */     this.queryWeighting = true;
/*    */   }
/*    */ 
/*    */   protected SimpleTermPredicate[] checkSimpleTermQuery(RelSimpleQuery query)
/*    */   {
/* 36 */     ArrayList list = new ArrayList();
/* 37 */     for (int i = 0; i < query.getChildNum(); i++) {
/* 38 */       if (((Predicate)query.getChild(i)).isTermPredicate()) {
/* 39 */         SimpleTermPredicate predicate = (SimpleTermPredicate)query.getChild(i);
/* 40 */         if (predicate.getDocFrequency() <= 0) {
/* 41 */           IRTerm curIRTerm = this.indexReader.getIRTerm(predicate.getKey());
/* 42 */           if (curIRTerm != null) {
/* 43 */             predicate.setDocFrequency(curIRTerm.getDocFrequency());
/* 44 */             predicate.setFrequency(curIRTerm.getFrequency());
/* 45 */             predicate.setIndex(curIRTerm.getIndex());
/*    */           }
/*    */         }
/* 48 */         if (predicate.getDocFrequency() > 0) {
/* 49 */           list.add(predicate);
/*    */         }
/*    */       }
/*    */     }
/* 53 */     SimpleTermPredicate[] arrPredicate = new SimpleTermPredicate[list.size()];
/* 54 */     for (int i = 0; i < list.size(); i++) {
/* 55 */       arrPredicate[i] = ((SimpleTermPredicate)list.get(i)).copy();
/* 56 */       if (!this.queryWeighting) {
/* 57 */         arrPredicate[i].setWeight(1.0D);
/*    */       }
/*    */     }
/* 60 */     return arrPredicate;
/*    */   }
/*    */ 
/*    */   public IRDoc getIRDoc(int ranking) {
/* 64 */     return (IRDoc)this.hitlist.get(ranking);
/*    */   }
/*    */ 
/*    */   public ArrayList getRankedDocumentList() {
/* 68 */     return this.hitlist;
/*    */   }
/*    */ 
/*    */   public int getRetrievedDocNum() {
/* 72 */     return this.hitlist.size();
/*    */   }
/*    */ 
/*    */   public Smoother getSmoother() {
/* 76 */     return this.smoother;
/*    */   }
/*    */ 
/*    */   public IndexReader getIndexReader() {
/* 80 */     return this.indexReader;
/*    */   }
/*    */ 
/*    */   public IRQuery getQuery() {
/* 84 */     return this.query;
/*    */   }
/*    */ 
/*    */   public void setQueryWeightingOption(boolean option) {
/* 88 */     this.queryWeighting = option;
/*    */   }
/*    */ 
/*    */   public boolean getQueryWeightingOption() {
/* 92 */     return this.queryWeighting;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.AbstractSearcher
 * JD-Core Version:    0.6.2
 */