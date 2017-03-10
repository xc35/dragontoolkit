/*     */ package edu.drexel.cis.dragon.ir.search.feedback;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.query.IRQuery;
/*     */ import edu.drexel.cis.dragon.ir.query.Operator;
/*     */ import edu.drexel.cis.dragon.ir.query.RelSimpleQuery;
/*     */ import edu.drexel.cis.dragon.ir.query.SimpleExpression;
/*     */ import edu.drexel.cis.dragon.ir.query.SimpleTermPredicate;
/*     */ import edu.drexel.cis.dragon.ir.search.Searcher;
/*     */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*     */ import edu.drexel.cis.dragon.nlp.compare.WeightComparator;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public abstract class AbstractFeedback
/*     */   implements Feedback
/*     */ {
/*     */   protected int feedbackDocNum;
/*     */   protected Searcher searcher;
/*     */   protected double feedbackCoeffi;
/*     */ 
/*     */   protected abstract ArrayList estimateNewQueryModel(IRQuery paramIRQuery);
/*     */ 
/*     */   public AbstractFeedback(Searcher searcher, int feedbackDocNum, double feedbackCoeffi)
/*     */   {
/*  26 */     this.searcher = searcher;
/*  27 */     this.feedbackDocNum = feedbackDocNum;
/*  28 */     this.feedbackCoeffi = feedbackCoeffi;
/*     */   }
/*     */ 
/*     */   public int getFeedbackDocNum() {
/*  32 */     return this.feedbackDocNum;
/*     */   }
/*     */ 
/*     */   public void setFeedbackDocNum(int docNum) {
/*  36 */     this.feedbackDocNum = docNum;
/*     */   }
/*     */ 
/*     */   public Searcher getSearcher() {
/*  40 */     return this.searcher;
/*     */   }
/*     */ 
/*     */   public void setSearcher(Searcher searcher) {
/*  44 */     this.searcher = searcher;
/*     */   }
/*     */ 
/*     */   public IRQuery updateQueryModel(IRQuery oldQuery)
/*     */   {
/*  55 */     ArrayList newTermList = estimateNewQueryModel(oldQuery);
/*  56 */     if ((newTermList == null) || (newTermList.size() == 0)) {
/*  57 */       return oldQuery;
/*     */     }
/*  59 */     int termNum = newTermList.size();
/*  60 */     SortedArray oldTermList = new SortedArray(new IndexComparator());
/*  61 */     double weightSum = 0.0D;
/*  62 */     for (int i = 0; i < oldQuery.getChildNum(); i++) {
/*  63 */       SimpleTermPredicate curPredicate = ((SimpleTermPredicate)oldQuery.getChild(i)).copy();
/*  64 */       if (curPredicate.getDocFrequency() > 0) {
/*  65 */         oldTermList.add(curPredicate);
/*  66 */         weightSum += curPredicate.getWeight();
/*     */       }
/*     */     }
				 int i;
/*  69 */     for (i = 0; i < oldTermList.size(); i++) {
/*  70 */       SimpleTermPredicate curPredicate = (SimpleTermPredicate)oldTermList.get(i);
/*  71 */       curPredicate.setWeight(curPredicate.getWeight() / weightSum * (1.0D - this.feedbackCoeffi));
/*     */     }
/*     */ 
/*  74 */     for (i = 0; i < newTermList.size(); i++) {
/*  75 */       SimpleTermPredicate curPredicate = (SimpleTermPredicate)newTermList.get(i);
/*  76 */       curPredicate.setWeight(curPredicate.getWeight() * this.feedbackCoeffi);
/*  77 */       if (!oldTermList.add(curPredicate)) {
/*  78 */         SimpleTermPredicate oldPredicate = (SimpleTermPredicate)oldTermList.get(oldTermList.insertedPos());
/*  79 */         oldPredicate.setWeight(oldPredicate.getWeight() + curPredicate.getWeight());
/*     */       }
/*     */     }
/*  82 */     oldTermList.setComparator(new WeightComparator(true));
/*     */ 
/*  84 */     weightSum = 0.0D;
/*  85 */     for (i = 0; i < termNum; i++) {
/*  86 */       weightSum += ((SimpleTermPredicate)oldTermList.get(i)).getWeight();
/*     */     }
/*  88 */     RelSimpleQuery query = new RelSimpleQuery();
/*  89 */     for (i = 0; i < termNum; i++) {
/*  90 */       SimpleTermPredicate curPredicate = (SimpleTermPredicate)oldTermList.get(i);
/*  91 */       curPredicate.setWeight(curPredicate.getWeight() / weightSum);
/*  92 */       query.add(curPredicate);
/*     */     }
/*  94 */     return query;
/*     */   }
/*     */ 
/*     */   protected IRTerm buildIRTerm(SimpleTermPredicate predicate)
/*     */   {
/* 100 */     IRTerm cur = new IRTerm(predicate.getIndex(), predicate.getFrequency(), predicate.getDocFrequency());
/* 101 */     cur.setKey(predicate.getKey());
/* 102 */     return cur;
/*     */   }
/*     */ 
/*     */   protected SimpleTermPredicate buildSimpleTermPredicate(int termIndex, double queryWeight)
/*     */   {
/* 109 */     IRTerm curTerm = this.searcher.getIndexReader().getIRTerm(termIndex);
/* 110 */     SimpleTermPredicate predicate = new SimpleTermPredicate(new SimpleExpression("TERM", new Operator("="), curTerm.getKey()));
/* 111 */     predicate.setWeight(queryWeight);
/* 112 */     predicate.setFrequency(curTerm.getFrequency());
/* 113 */     predicate.setDocFrequency(curTerm.getDocFrequency());
/* 114 */     predicate.setIndex(termIndex);
/* 115 */     return predicate;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.feedback.AbstractFeedback
 * JD-Core Version:    0.6.2
 */