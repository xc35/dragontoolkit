/*     */ package edu.drexel.cis.dragon.nlp.ontology;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.Term;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*     */ import edu.drexel.cis.dragon.util.EnvVariable;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class BasicOntology extends AbstractOntology
/*     */ {
/*     */   private BasicTermList list;
/*     */ 
/*     */   public BasicOntology(String termFilename, Lemmatiser lemmatiser)
/*     */   {
/*  21 */     super(lemmatiser);
/*  22 */     if ((!FileUtil.exist(termFilename)) && (FileUtil.exist(EnvVariable.getDragonHome() + "/" + termFilename)))
/*  23 */       termFilename = EnvVariable.getDragonHome() + "/" + termFilename;
/*  24 */     setNonBoundaryPunctuation(".-");
/*  25 */     this.list = new BasicTermList(termFilename);
/*     */   }
/*     */ 
/*     */   public String[] getSemanticType(String[] cuis) {
/*  29 */     return null;
/*     */   }
/*     */ 
/*     */   public String[] getSemanticType(String cui) {
/*  33 */     return null;
/*     */   }
/*     */ 
/*     */   public String[] getCUI(String term)
/*     */   {
/*  39 */     BasicTerm cur = this.list.lookup(term);
/*  40 */     if (cur == null) {
/*  41 */       return null;
/*     */     }
/*  43 */     return cur.getAllCUI();
/*     */   }
/*     */ 
/*     */   public String[] getCUI(Word starting, Word ending) {
/*  47 */     return getCUI(buildString(starting, ending, getLemmaOption()));
/*     */   }
/*     */ 
/*     */   public boolean isTerm(String term) {
/*  51 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isTerm(Word starting, Word ending) {
/*  55 */     return isTerm(buildString(starting, ending, getLemmaOption()));
/*     */   }
/*     */ 
/*     */   public Term findTerm(Word starting) {
/*  59 */     return findTerm(starting, null);
/*     */   }
/*     */ 
/*     */   public Term findTerm(Word start, Word end)
/*     */   {
/*  69 */     Sentence sent = start.getParent();
/*     */ 
/*  71 */     Word curWord = start.next;
/*  72 */     if (end == null) {
/*  73 */       int j = 0;
/*  74 */       while ((j < 4) && (curWord != null) && (end == null)) {
/*  75 */         if (isBoundaryWord(curWord))
/*  76 */           end = curWord.prev;
/*  77 */         j++;
/*  78 */         curWord = curWord.next;
/*     */       }
/*  80 */       if (curWord == null)
/*  81 */         curWord = sent.getLastWord();
/*  82 */       if (end == null) {
/*  83 */         end = curWord;
/*     */       }
/*     */     }
/*  86 */     curWord = end;
/*  87 */     String[] arrCandidateCUI = (String[])null;
/*  88 */     while ((curWord != null) && (curWord.getPosInSentence() >= start.getPosInSentence())) {
/*  89 */       int posIndex = curWord.getPOSIndex();
/*  90 */       if ((posIndex == 1) || ((posIndex == 9) && (curWord.getPosInSentence() > start.getPosInSentence()))) {
/*  91 */         arrCandidateCUI = getCUI(start, curWord);
/*  92 */         if (arrCandidateCUI != null)
/*     */           break;
/*     */       }
/*  95 */       curWord = curWord.prev;
/*     */     }
/*     */ 
/*  98 */     if (arrCandidateCUI == null) return null;
/*  99 */     Term curTerm = new Term(start, curWord);
/* 100 */     start.setAssociatedConcept(curTerm);
/* 101 */     curTerm.setCandidateCUI(arrCandidateCUI);
/* 102 */     if (arrCandidateCUI.length == 1)
/* 103 */       curTerm.setCUI(arrCandidateCUI[0]);
/* 104 */     return curTerm;
/*     */   }
/*     */ 
/*     */   public ArrayList findAllTerms(Word starting) {
/* 108 */     return findAllTerms(starting, null);
/*     */   }
/*     */ 
/*     */   public ArrayList findAllTerms(Word starting, Word ending)
/*     */   {
/* 115 */     Term cur = findTerm(starting, ending);
/* 116 */     if (cur == null) {
/* 117 */       return null;
/*     */     }
/* 119 */     ArrayList termList = new ArrayList(1);
/* 120 */     termList.add(cur);
/* 121 */     return termList;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.ontology.BasicOntology
 * JD-Core Version:    0.6.2
 */