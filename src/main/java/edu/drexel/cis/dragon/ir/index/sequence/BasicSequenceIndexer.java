/*    */ package edu.drexel.cis.dragon.ir.index.sequence;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.extract.ConceptExtractor;
/*    */ 
/*    */ public class BasicSequenceIndexer extends AbstractSequenceIndexer
/*    */ {
/*    */   public BasicSequenceIndexer(ConceptExtractor ce, String indexFolder)
/*    */   {
/* 17 */     super(ce);
/* 18 */     this.writer = new BasicSequenceIndexWriter(indexFolder);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.sequence.BasicSequenceIndexer
 * JD-Core Version:    0.6.2
 */