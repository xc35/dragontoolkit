/*    */ package edu.drexel.cis.dragon.qa.qc;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.Sentence;
/*    */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*    */ import edu.drexel.cis.dragon.nlp.Word;
/*    */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*    */ import edu.drexel.cis.dragon.nlp.tool.Tagger;
/*    */ import edu.drexel.cis.dragon.nlp.tool.xtract.EngWordPairIndexer;
/*    */ import edu.drexel.cis.dragon.nlp.tool.xtract.WordPairGenerator;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class SimpleWordPairIndexer extends EngWordPairIndexer
/*    */ {
/*    */   public SimpleWordPairIndexer(String folder, int maxSpan, Tagger tagger, Lemmatiser lemmatiser)
/*    */   {
/* 21 */     super(folder, maxSpan, tagger, lemmatiser, new SimpleWordPairGenerator(maxSpan));
/*    */   }
/*    */ 
/*    */   public SimpleWordPairIndexer(String folder, int maxSpan, Tagger tagger, Lemmatiser lemmatiser, WordPairGenerator pairGenerator) {
/* 25 */     super(folder, maxSpan, tagger, lemmatiser, pairGenerator);
/*    */   }
/*    */ 
/*    */   protected void preprocessSentence(Sentence sent)
/*    */   {
/* 37 */     if (this.tagger != null) this.tagger.tag(sent);
/*    */ 
/* 40 */     Word cur = sent.getFirstWord();
/* 41 */     while (cur != null) {
/* 42 */       if (this.lemmatiser != null)
/* 43 */         cur.setLemma(this.lemmatiser.lemmatize(cur.getContent(), cur.getPOSIndex()));
/*    */       else
/* 45 */         cur.setLemma(cur.getContent().toLowerCase());
/* 46 */       if (cur.getLemma().equals("doe")) {
/* 47 */         System.out.println();
/*    */       }
/* 49 */       cur.setIndex(this.wordKeyList.add(cur.getLemma()));
/* 50 */       cur = cur.next;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.qc.SimpleWordPairIndexer
 * JD-Core Version:    0.6.2
 */