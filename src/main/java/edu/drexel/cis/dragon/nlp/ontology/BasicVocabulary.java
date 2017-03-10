/*    */ package edu.drexel.cis.dragon.nlp.ontology;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.Sentence;
/*    */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*    */ import edu.drexel.cis.dragon.nlp.Word;
/*    */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*    */ 
/*    */ public class BasicVocabulary extends AbstractVocabulary
/*    */ {
/*    */   public BasicVocabulary(String termFilename)
/*    */   {
/* 18 */     super(termFilename);
/*    */   }
/*    */ 
/*    */   public BasicVocabulary(String termFilename, Lemmatiser lemmatiser) {
/* 22 */     super(termFilename, lemmatiser);
/*    */   }
/*    */ 
/*    */   public boolean isPhrase(String term) {
/* 26 */     return this.list.contains(term);
/*    */   }
/*    */ 
/*    */   public boolean isPhrase(Word start, Word end) {
/* 30 */     return isPhrase(buildString(start, end, getLemmaOption()));
/*    */   }
/*    */ 
/*    */   public Word findPhrase(Word start)
/*    */   {
/* 39 */     Sentence sent = start.getParent();
/*    */ 
/* 41 */     Word curWord = start.next;
/* 42 */     Word end = null;
/* 43 */     int j = 0;
/* 44 */     while ((j < this.maxPhraseLength - 1) && (curWord != null) && (end == null)) {
/* 45 */       if (isBoundaryWord(curWord))
/* 46 */         end = curWord.prev;
/* 47 */       j++;
/* 48 */       curWord = curWord.next;
/*    */     }
/*    */ 
/* 51 */     if (curWord == null)
/* 52 */       curWord = sent.getLastWord();
/* 53 */     if (end == null) {
/* 54 */       end = curWord;
/* 55 */       j = end.getPosInSentence() - start.getPosInSentence() + 1;
/*    */     }
/* 57 */     if (j < this.minPhraseLength) {
/* 58 */       return null;
/*    */     }
/* 60 */     curWord = end;
/* 61 */     boolean found = false;
/* 62 */     while ((curWord != null) && (curWord.getPosInSentence() >= start.getPosInSentence()) && (j >= this.minPhraseLength)) {
/* 63 */       int posIndex = curWord.getPOSIndex();
/* 64 */       if (((posIndex == 1) || ((posIndex == 9) && (curWord.getPosInSentence() > start.getPosInSentence()))) && 
/* 65 */         (isPhrase(start, curWord))) {
/* 66 */         found = true;
/* 67 */         break;
/*    */       }
/*    */ 
/* 70 */       curWord = curWord.prev;
/* 71 */       j--;
/*    */     }
/*    */ 
/* 74 */     if (found) {
/* 75 */       return curWord;
/*    */     }
/* 77 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.ontology.BasicVocabulary
 * JD-Core Version:    0.6.2
 */