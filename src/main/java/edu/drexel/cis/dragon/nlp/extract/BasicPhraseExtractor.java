/*    */ package edu.drexel.cis.dragon.nlp.extract;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.Phrase;
/*    */ import edu.drexel.cis.dragon.nlp.Sentence;
/*    */ import edu.drexel.cis.dragon.nlp.Word;
/*    */ import edu.drexel.cis.dragon.nlp.ontology.Vocabulary;
/*    */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*    */ import edu.drexel.cis.dragon.nlp.tool.Tagger;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class BasicPhraseExtractor extends AbstractPhraseExtractor
/*    */ {
/*    */   protected boolean overlappedPhrase;
/*    */ 
/*    */   public BasicPhraseExtractor(Vocabulary vocabulary, Lemmatiser lemmatiser, Tagger tagger)
/*    */   {
/* 21 */     this(vocabulary, lemmatiser, tagger, false);
/*    */   }
/*    */ 
/*    */   public BasicPhraseExtractor(Vocabulary vocabulary, Lemmatiser lemmatiser, Tagger tagger, boolean overlappedPhrase) {
/* 25 */     super(vocabulary, tagger, lemmatiser);
/* 26 */     this.overlappedPhrase = overlappedPhrase;
/*    */   }
/*    */ 
/*    */   public ArrayList extractFromSentence(Sentence sent)
/*    */   {
/* 36 */     Word cur = sent.getFirstWord();
/* 37 */     if ((cur != null) && (cur.getPOSIndex() < 0)) this.tagger.tag(sent);
/* 38 */     while (cur != null) {
/* 39 */       int posIndex = cur.getPOSIndex();
/* 40 */       if (((posIndex == 3) && (this.useAdj)) || (posIndex == 1) || ((posIndex == 2) && (this.useVerb)))
/* 41 */         cur.setLemma(this.lemmatiser.lemmatize(cur.getContent(), posIndex));
/*    */       else
/* 43 */         cur.setLemma(cur.getContent().toLowerCase());
/* 44 */       cur = cur.next;
/*    */     }
/*    */ 
/* 47 */     int lastPhraseEndPos = -1;
/* 48 */     cur = sent.getFirstWord();
/* 49 */     ArrayList phraseList = new ArrayList(30);
/* 50 */     while (cur != null)
/*    */     {
/* 52 */       if (!this.vocabulary.isStartingWord(cur)) {
/* 53 */         int posIndex = cur.getPOSIndex();
/* 54 */         if ((cur.getPosInSentence() > lastPhraseEndPos) && (((posIndex == 1) && (this.useNoun)) || ((posIndex == 3) && (this.useAdj)) || ((posIndex == 2) && (this.useVerb))))
/* 55 */           addPhrase(cur, cur, false, false, phraseList);
/* 56 */         cur = cur.next;
/*    */       }
/*    */       else
/*    */       {
/* 60 */         Word end = this.vocabulary.findPhrase(cur);
/*    */         Phrase curPhrase;
/* 61 */         if ((end == null) || ((curPhrase = addPhrase(cur, end, true, false, phraseList)) == null)) {
/* 62 */           int posIndex = cur.getPOSIndex();
/* 63 */           if ((cur.getPosInSentence() > lastPhraseEndPos) && (((posIndex == 1) && (this.useNoun)) || ((posIndex == 3) && (this.useAdj)) || ((posIndex == 2) && (this.useVerb))))
/* 64 */             addPhrase(cur, cur, false, false, phraseList);
/* 65 */           cur = cur.next;
/*    */         }
/*    */         else
/*    */         {
/*    */          
/* 68 */           Word start = cur;
/* 69 */           if ((curPhrase.getWordNum() >= 2) && (getSubConceptOption())) {
/* 70 */             while ((cur != null) && (cur.getPosInSentence() <= end.getPosInSentence())) {
/* 71 */               int posIndex = cur.getPOSIndex();
/* 72 */               if ((cur.getPosInSentence() > lastPhraseEndPos) && (((posIndex == 1) && (this.useNoun)) || ((posIndex == 3) && (this.useAdj))))
/* 73 */                 addPhrase(cur, cur, false, true, phraseList);
/* 74 */               cur = cur.next;
/*    */             }
/*    */           }
/* 77 */           lastPhraseEndPos = end.getPosInSentence();
/* 78 */           if (this.overlappedPhrase)
/* 79 */             cur = start.next;
/*    */           else
/* 81 */             cur = end.next; 
/*    */         }
/*    */       }
/*    */     }
/* 84 */     return phraseList;
/*    */   }
/*    */ 
/*    */   protected Phrase addPhrase(Word start, Word end, boolean forRelation, boolean subphrase, ArrayList phraseList)
/*    */   {
/* 90 */     Phrase phrase = new Phrase(start, end);
/* 91 */     phrase.setSubConcept(subphrase);
/* 92 */     if ((this.conceptFilter_enabled) && (!this.cf.keep(phrase.getName())))
/* 93 */       return null;
/* 94 */     if (forRelation)
/* 95 */       start.setAssociatedConcept(phrase);
/* 96 */     phrase.setFrequency(1);
/* 97 */     phraseList.add(phrase);
/* 98 */     return phrase;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.extract.BasicPhraseExtractor
 * JD-Core Version:    0.6.2
 */