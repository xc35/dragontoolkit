/*     */ package edu.drexel.cis.dragon.ir.search.expand;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.query.IRQuery;
/*     */ import edu.drexel.cis.dragon.ir.query.Operator;
/*     */ import edu.drexel.cis.dragon.ir.query.Predicate;
/*     */ import edu.drexel.cis.dragon.ir.query.RelSimpleQuery;
/*     */ import edu.drexel.cis.dragon.ir.query.SimpleExpression;
/*     */ import edu.drexel.cis.dragon.ir.query.SimpleTermPredicate;
/*     */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class AbstractQE
/*     */ {
/*     */   IndexReader indexReader;
/*     */ 
/*     */   public AbstractQE(IndexReader indexReader)
/*     */   {
/*  21 */     this.indexReader = indexReader;
/*     */   }
/*     */ 
/*     */   protected SimpleTermPredicate[] checkSimpleTermQuery(RelSimpleQuery query)
/*     */   {
/*  30 */     ArrayList list = new ArrayList();
/*  31 */     for (int i = 0; i < query.getChildNum(); i++) {
/*  32 */       if (((Predicate)query.getChild(i)).isTermPredicate()) {
/*  33 */         SimpleTermPredicate predicate = (SimpleTermPredicate)query.getChild(i);
/*  34 */         if (predicate.getDocFrequency() <= 0) {
/*  35 */           IRTerm curIRTerm = this.indexReader.getIRTerm(predicate.getKey());
/*  36 */           if (curIRTerm != null) {
/*  37 */             predicate.setDocFrequency(curIRTerm.getDocFrequency());
/*  38 */             predicate.setFrequency(curIRTerm.getFrequency());
/*  39 */             predicate.setIndex(curIRTerm.getIndex());
/*     */           }
/*     */         }
/*  42 */         if (predicate.getDocFrequency() > 0) {
/*  43 */           list.add(predicate);
/*     */         }
/*     */       }
/*     */     }
/*  47 */     SimpleTermPredicate[] arrPredicate = new SimpleTermPredicate[list.size()];
/*  48 */     for (int i = 0; i < list.size(); i++) {
/*  49 */       arrPredicate[i] = ((SimpleTermPredicate)list.get(i)).copy();
/*     */     }
/*  51 */     return arrPredicate;
/*     */   }
/*     */ 
/*     */   protected SimpleTermPredicate buildSimpleTermPredicate(int termIndex, double queryWeight)
/*     */   {
/*  58 */     IRTerm curTerm = this.indexReader.getIRTerm(termIndex);
/*  59 */     SimpleTermPredicate predicate = new SimpleTermPredicate(new SimpleExpression("TERM", new Operator("="), curTerm.getKey()));
/*  60 */     predicate.setWeight(queryWeight);
/*  61 */     predicate.setFrequency(curTerm.getFrequency());
/*  62 */     predicate.setDocFrequency(curTerm.getDocFrequency());
/*  63 */     predicate.setIndex(termIndex);
/*  64 */     return predicate;
/*     */   }
/*     */ 
/*     */   protected IRQuery buildQuery(SimpleTermPredicate[] oldQuery, SimpleTermPredicate[] newQuery, double expandCoeffi)
/*     */   {
/*  73 */     RelSimpleQuery query = new RelSimpleQuery();
/*  74 */     SortedArray list = new SortedArray(new IndexComparator());
/*  75 */     normalizeQuery(oldQuery);
/*  76 */     normalizeQuery(newQuery);
/*  77 */     for (int i = 0; i < oldQuery.length; i++) {
/*  78 */       oldQuery[i].setWeight(oldQuery[i].getWeight() * (1.0D - expandCoeffi));
/*  79 */       list.add(oldQuery[i]);
/*     */     }
/*  81 */     for (int i = 0; i < newQuery.length; i++) {
/*  82 */       newQuery[i].setWeight(newQuery[i].getWeight() * expandCoeffi);
/*  83 */       if (!list.add(newQuery[i])) {
/*  84 */         double weight = ((SimpleTermPredicate)list.get(list.insertedPos())).getWeight();
/*  85 */         ((SimpleTermPredicate)list.get(list.insertedPos())).setWeight(weight + newQuery[i].getWeight());
/*     */       }
/*     */     }
/*  88 */     for (int i = 0; i < list.size(); i++)
/*  89 */       query.add((SimpleTermPredicate)list.get(i));
/*  90 */     return query;
/*     */   }
/*     */ 
/*     */   private void normalizeQuery(SimpleTermPredicate[] query)
/*     */   {
/*  97 */     double sum = 0.0D;
/*  98 */     for (int i = 0; i < query.length; i++)
/*  99 */       sum += query[i].getWeight();
/* 100 */     for (int i = 0; i < query.length; i++)
/* 101 */       query[i].setWeight(query[i].getWeight() / sum);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.expand.AbstractQE
 * JD-Core Version:    0.6.2
 */