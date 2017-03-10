/*    */ package edu.drexel.cis.dragon.ir.index.sequence;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*    */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*    */ import edu.drexel.cis.dragon.ir.index.IRDocIndexList;
/*    */ import edu.drexel.cis.dragon.ir.index.IRRelation;
/*    */ import edu.drexel.cis.dragon.ir.index.IRTerm;
/*    */ import edu.drexel.cis.dragon.ir.index.IRTermIndexList;
/*    */ import edu.drexel.cis.dragon.ir.index.IndexWriter;
/*    */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*    */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*    */ import edu.drexel.cis.dragon.util.SortedArray;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public abstract class AbstractSequenceIndexWriter
/*    */   implements IndexWriter
/*    */ {
/* 18 */   protected static int doc_cache_size = 5000;
/*    */   protected SortedArray termCache;
/*    */   protected SimpleElementList termKeyList;
/*    */   protected SimpleElementList docKeyList;
/*    */   protected IRTermIndexList termIndexList;
/*    */   protected IRDocIndexList docIndexList;
/*    */   protected SequenceWriter doctermMatrix;
/*    */   protected IRCollection collection;
/*    */   protected boolean initialized;
/*    */ 
/*    */   public boolean indexed(String docKey)
/*    */   {
/* 29 */     return this.docKeyList.contains(docKey);
/*    */   }
/*    */ 
/*    */   public int size() {
/* 33 */     return this.docIndexList.size();
/*    */   }
/*    */ 
/*    */   public void flush() {
/* 37 */     this.collection.setDocNum(this.docIndexList.size());
/* 38 */     this.collection.setTermNum(this.termIndexList.size());
/*    */   }
/*    */ 
/*    */   public synchronized boolean write(IRDoc curDoc, IRTerm[] arrTerms, IRRelation[] arrRelations) {
/* 42 */     return false;
/*    */   }
/*    */ 
/*    */   public synchronized boolean write(IRDoc curDoc, IRTerm[] arrTerm)
/*    */   {
/*    */     try
/*    */     {
/* 51 */       int docIndex = this.docKeyList.add(curDoc.getKey());
/* 52 */       if (docIndex != this.docKeyList.size() - 1) {
/* 53 */         System.out.println("#" + curDoc.getKey() + " is already indexed");
/* 54 */         return false;
/*    */       }
/* 56 */       curDoc.setIndex(docIndex);
/*    */ 
/* 58 */       processIRTerms(arrTerm);
/* 59 */       int[] arrSeq = new int[arrTerm.length];
/* 60 */       SortedArray termList = new SortedArray(new IndexComparator());
/* 61 */       for (int i = 0; i < arrTerm.length; i++) {
/* 62 */         arrTerm[i].setDocFrequency(1);
/* 63 */         arrSeq[i] = arrTerm[i].getIndex();
/* 64 */         if (!termList.add(arrTerm[i]))
/* 65 */           ((IRTerm)termList.get(termList.insertedPos())).addFrequency(1);
/*    */       }
/* 67 */       curDoc.setTermCount(arrTerm.length);
/* 68 */       curDoc.setTermNum(termList.size());
/*    */ 
/* 70 */       this.docIndexList.add(curDoc);
/* 71 */       for (int i = 0; i < termList.size(); i++) {
/* 72 */         this.termIndexList.add((IRTerm)termList.get(i));
/*    */       }
/* 74 */       this.doctermMatrix.addSequence(curDoc.getIndex(), arrSeq);
/* 75 */       this.collection.addTermCount(curDoc.getTermCount());
/* 76 */       return true;
/*    */     }
/*    */     catch (Exception e) {
/* 79 */       e.printStackTrace();
/* 80 */     }return false;
/*    */   }
/*    */ 
/*    */   private void processIRTerms(IRTerm[] arrTerm)
/*    */   {
/* 88 */     this.termCache.clear();
/* 89 */     for (int i = 0; i < arrTerm.length; i++) {
/* 90 */       IRTerm cur = arrTerm[i];
/* 91 */       if (this.termCache.add(cur))
/* 92 */         cur.setIndex(this.termKeyList.add(cur.getKey()));
/*    */       else
/* 94 */         cur.setIndex(((IRTerm)this.termCache.get(this.termCache.insertedPos())).getIndex());
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.sequence.AbstractSequenceIndexWriter
 * JD-Core Version:    0.6.2
 */