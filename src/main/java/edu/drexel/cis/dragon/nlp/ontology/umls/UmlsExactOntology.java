/*     */ package edu.drexel.cis.dragon.nlp.ontology.umls;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.Term;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ 
/*     */ public abstract class UmlsExactOntology extends UmlsOntology
/*     */ {
/*     */   public UmlsExactOntology(Lemmatiser lemmatiser)
/*     */   {
/*  18 */     super(lemmatiser);
/*     */   }
/*     */ 
/*     */   public String[] getSemanticType(String[] cuis)
/*     */   {
/*  26 */     SortedArray typeList = new SortedArray(3);
/*  27 */     for (int i = 0; i < cuis.length; i++)
/*     */     {
/*  29 */       String[] arrTypes = getSemanticType(cuis[i]);
/*  30 */       if (arrTypes != null) {
/*  31 */         for (int j = 0; j < arrTypes.length; j++)
/*  32 */           typeList.add(arrTypes[j]);
/*     */       }
/*     */     }
/*  35 */     if (typeList.size() > 0) {
/*  36 */       String[] arrTypes = new String[typeList.size()];
/*  37 */       for (int i = 0; i < typeList.size(); i++)
/*  38 */         arrTypes[i] = ((String)typeList.get(i));
/*  39 */       return arrTypes;
/*     */     }
/*     */ 
/*  42 */     return null;
/*     */   }
/*     */ 
/*     */   public abstract String[] getSemanticType(String paramString);
/*     */ 
/*     */   public abstract String[] getCUI(String paramString);
/*     */ 
/*     */   public String[] getCUI(Word starting, Word ending) {
/*  50 */     return getCUI(buildNormalizedTerm(starting, ending));
/*     */   }
/*     */ 
/*     */   public abstract boolean isTerm(String paramString);
/*     */ 
/*     */   public boolean isTerm(Word starting, Word ending) {
/*  56 */     return isTerm(buildNormalizedTerm(starting, ending));
/*     */   }
/*     */ 
/*     */   public ArrayList findAllTerms(Word start) {
/*  60 */     return findAllTerms(start, null);
/*     */   }
/*     */ 
/*     */   public ArrayList findAllTerms(Word start, Word end) {
/*  64 */     return null;
/*     */   }
/*     */ 
/*     */   public Term findTerm(Word start) {
/*  68 */     return findTerm(start, null);
/*     */   }
/*     */ 
/*     */   public Term findTerm(Word start, Word end)
/*     */   {
/*  78 */     Sentence sent = start.getParent();
/*     */ 
/*  80 */     Word curWord = start.next;
/*  81 */     if (end == null) {
/*  82 */       int j = 0;
/*  83 */       while ((j < 4) && (curWord != null) && (end == null)) {
/*  84 */         if (isBoundaryWord(curWord))
/*  85 */           end = curWord.prev;
/*  86 */         if (!curWord.isPunctuation())
/*  87 */           j++;
/*  88 */         curWord = curWord.next;
/*     */       }
/*  90 */       if (curWord == null)
/*  91 */         curWord = sent.getLastWord();
/*  92 */       if (end == null) {
/*  93 */         end = curWord;
/*     */       }
/*     */     }
/*  96 */     curWord = end;
/*  97 */     String[] arrCandidateCUI = (String[])null;
/*  98 */     while ((curWord != null) && (curWord.getPosInSentence() >= start.getPosInSentence())) {
/*  99 */       int posIndex = curWord.getPOSIndex();
/* 100 */       if ((posIndex == 1) || ((posIndex == 9) && (curWord.getPosInSentence() > start.getPosInSentence()))) {
/* 101 */         arrCandidateCUI = getCUI(start, curWord);
/* 102 */         if (arrCandidateCUI != null)
/*     */           break;
/*     */       }
/* 105 */       curWord = curWord.prev;
/*     */     }
/*     */ 
/* 108 */     if (arrCandidateCUI == null) return null;
/* 109 */     Term curTerm = new Term(start, end);
/* 110 */     start.setAssociatedConcept(curTerm);
/* 111 */     curTerm.setCandidateCUI(arrCandidateCUI);
/* 112 */     if (arrCandidateCUI.length == 1)
/* 113 */       curTerm.setCUI(arrCandidateCUI[0]);
/* 114 */     if (curTerm.getCUI() == null) {
/* 115 */       curTerm.setCandidateTUI(getSemanticType(curTerm.getCandidateCUI()));
/*     */     }
/*     */     else {
/* 118 */       curTerm.setCandidateTUI(getSemanticType(curTerm.getCUI()));
/*     */     }
/* 120 */     if (curTerm.getCandidateTUINum() == 1) {
/* 121 */       curTerm.setTUI(curTerm.getCandidateTUI(0));
/*     */     }
/* 123 */     return curTerm;
/*     */   }
/*     */ 
/*     */   public String buildNormalizedTerm(Word start, Word end)
/*     */   {
/* 133 */     if (start.equals(end)) return getLemma(start);
/*     */ 
/* 135 */     ArrayList list = new ArrayList(6);
/* 136 */     Word next = start;
/* 137 */     while (next != null) {
/* 138 */       if (isUsefulForTerm(next)) {
/* 139 */         list.add(getLemma(next));
/*     */       }
/* 141 */       if (next.equals(end)) break;
/* 142 */       next = next.next;
/*     */     }
/*     */ 
/* 146 */     Collections.sort(list);
/*     */ 
/* 148 */     StringBuffer term = new StringBuffer((String)list.get(0));
/* 149 */     for (int i = 1; i < list.size(); i++) {
/* 150 */       term.append(' ');
/* 151 */       term.append((String)list.get(i));
/*     */     }
/* 153 */     return term.toString();
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.ontology.umls.UmlsExactOntology
 * JD-Core Version:    0.6.2
 */