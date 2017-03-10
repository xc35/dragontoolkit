/*    */ package edu.drexel.cis.dragon.qa.qc;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.Phrase;
/*    */ import edu.drexel.cis.dragon.nlp.Sentence;
/*    */ import edu.drexel.cis.dragon.nlp.Word;
/*    */ import edu.drexel.cis.dragon.nlp.extract.AbstractPhraseExtractor;
/*    */ import edu.drexel.cis.dragon.nlp.ontology.Vocabulary;
/*    */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*    */ import edu.drexel.cis.dragon.nlp.tool.Tagger;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class SimplePhraseExtractor extends AbstractPhraseExtractor
/*    */ {
/*    */   public SimplePhraseExtractor(Vocabulary vocabulary, Lemmatiser lemmatiser, Tagger tagger)
/*    */   {
/* 15 */     super(vocabulary, tagger, lemmatiser);
/*    */   }
/*    */ 
/*    */   public ArrayList extractFromSentence(Sentence sent)
/*    */   {
/* 24 */     Word cur = sent.getFirstWord();
/* 25 */     if ((cur != null) && (cur.getPOSIndex() < 0)) this.tagger.tag(sent);
/* 26 */     while (cur != null) {
/* 27 */       if (cur.getLemma() == null)
/* 28 */         cur.setLemma(this.lemmatiser.lemmatize(cur.getContent(), cur.getPOSIndex()));
/* 29 */       cur = cur.next;
/*    */     }
/*    */ 
/* 32 */     int maxPhraseLen = this.vocabulary.maxPhraseLength();
/* 33 */     int minPhraseLen = this.vocabulary.minPhraseLength();
/*    */ 
/* 35 */     cur = sent.getFirstWord();
/* 36 */     ArrayList phraseList = new ArrayList(30);
/* 37 */     while (cur != null)
/*    */     {
/* 39 */       if (cur.isPunctuation()) {
/* 40 */         cur = cur.next;
/*    */       }
/*    */       else
/*    */       {
/* 44 */         int phraseLen = 1;
/*    */         Word end;
/* 45 */         Word start = end = cur;
/*    */         do {
/* 47 */           cur = cur.next;
/* 48 */           end = cur;
/* 49 */           phraseLen++;
/*    */ 
/* 46 */           if ((phraseLen >= maxPhraseLen) || (cur.next == null)) break;  } while (!cur.next.isPunctuation());
/*    */ 
/* 52 */         for (; phraseLen >= minPhraseLen; phraseLen--) {
/* 53 */           if (this.vocabulary.isPhrase(buildString(start, end))) {
/* 54 */             phraseList.add(new Phrase(start, end));
/* 55 */             cur = end.next;
/* 56 */             break;
/*    */           }
/*    */ 
/* 59 */           end = end.prev;
/*    */         }
/*    */ 
/* 62 */         cur = start.next;
/*    */       }
/*    */     }
/* 64 */     return phraseList;
/*    */   }
/*    */ 
/*    */   private String buildString(Word start, Word end)
/*    */   {
/* 71 */     Word next = start;
/* 72 */     String term = next.getLemma();
/* 73 */     while (!next.equals(end)) {
/* 74 */       next = next.next;
/* 75 */       term = term + " " + next.getLemma();
/*    */     }
/* 77 */     return term;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.qc.SimplePhraseExtractor
 * JD-Core Version:    0.6.2
 */