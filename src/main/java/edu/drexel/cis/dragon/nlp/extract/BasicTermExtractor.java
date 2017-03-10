/*     */ package edu.drexel.cis.dragon.nlp.extract;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.Term;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.nlp.ontology.Ontology;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Tagger;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class BasicTermExtractor extends AbstractTermExtractor
/*     */ {
/*     */   public BasicTermExtractor(Ontology ontology, Lemmatiser lemmatiser, Tagger tagger)
/*     */   {
/*  20 */     super(ontology, tagger, lemmatiser);
/*     */   }
/*     */ 
/*     */   public void clearAllCaches() {
/*  24 */     if (this.abbrChecker != null)
/*  25 */       this.abbrChecker.clearCachedAbbr();
/*     */   }
/*     */ 
/*     */   public ArrayList extractFromSentence(Sentence sent)
/*     */   {
/*  34 */     Word cur = sent.getFirstWord();
/*  35 */     if ((cur != null) && (cur.getPOSIndex() < 0)) this.tagger.tag(sent);
/*  36 */     Term prevTerm = null;
/*  37 */     ArrayList termList = new ArrayList(10);
/*     */ 
/*  39 */     while (cur != null)
/*     */     {
/*  41 */       if (!this.ontology.isStartingWord(cur)) {
/*  42 */         cur = cur.next;
/*     */       }
/*     */       else
/*     */       {
/*  46 */         Term curTerm = lookup(cur, prevTerm, termList);
/*  47 */         if (curTerm == null) {
/*  48 */           cur = cur.next;
/*     */         }
/*     */         else {
/*  51 */           cur = curTerm.getEndingWord().next;
/*  52 */           prevTerm = curTerm;
/*     */         }
/*     */       }
/*     */     }
/*  56 */     if (this.coordinatingCheck_enabled) {
/*  57 */       this.paraChecker.identifyParaElements(sent);
/*     */     }
/*     */ 
/*  60 */     if (this.attributeCheck_enabled) {
/*  61 */       this.attrChecker.identifyAttributes(termList);
/*     */     }
/*     */ 
/*  64 */     if ((this.coordinatingTermPredict_enabled) && (this.coordinatingCheck_enabled)) {
/*  65 */       termList = this.paraChecker.parallelTermPredict(termList);
/*     */     }
/*     */ 
/*  68 */     if (this.compoundTermPredict_enabled) {
/*  69 */       termList = this.compTermFinder.predict(termList);
/*     */     }
/*     */ 
/*  72 */     if (this.conceptFilter_enabled) {
/*  73 */       termList = filter(termList);
/*     */     }
/*     */ 
/*  76 */     return termList;
/*     */   }
/*     */ 
/*     */   protected Term lookup(Word starting, Term prevTerm, ArrayList termList)
/*     */   {
/*     */     try
/*     */     {
/*  83 */       if ((this.abbreviation_enabled) && (starting.getContent().length() <= 6))
/*     */       {
/*     */         Term referredTerm;
/*     */       
/*  84 */         if (this.abbrChecker.contains(starting.getContent())) {
/*  85 */           referredTerm = this.abbrChecker.get();
/*     */         }
/*     */         else
/*     */         {
/*     */        
/*  87 */           if (this.abbrChecker.isAbbrOfLastTerm(starting, prevTerm)) {
/*  88 */             this.abbrChecker.put(starting.getContent(), prevTerm);
/*  89 */             referredTerm = prevTerm;
/*     */           }
/*     */           else {
/*  92 */             referredTerm = null;
/*     */           }
/*     */         }
/*  94 */         if (referredTerm != null) {
/*  95 */           starting.setLemma(starting.getContent().toLowerCase());
/*  96 */           Term term = new Term(starting);
/*  97 */           term.setCandidateCUI(referredTerm.getCandidateCUI());
/*  98 */           term.setCUI(referredTerm.getCUI());
/*  99 */           term.setCandidateTUI(referredTerm.getCandidateTUI());
/* 100 */           term.setTUI(referredTerm.getTUI());
/* 101 */           term.setReferral(referredTerm);
/* 102 */           starting.setAssociatedConcept(term);
/* 103 */           termList.add(term);
/* 104 */           return term;
/*     */         }
/*     */       }
/*     */       Term term;
/*     */      
/* 108 */       if (this.subconcept_enabled) {
/* 109 */         ArrayList list = this.ontology.findAllTerms(starting);
/*     */         
/* 110 */         if ((list != null) && (list.size() > 0)) {
/* 111 */           termList.addAll(list);
/* 112 */           term = (Term)list.get(0);
/*     */         }
/*     */         else {
/* 115 */           term = null;
/*     */         }
/*     */       } else {
/* 118 */         term = this.ontology.findTerm(starting);
/* 119 */         if (term != null) termList.add(term);
/*     */       }
/* 121 */       return term;
/*     */     }
/*     */     catch (Exception e) {
/* 124 */       e.printStackTrace();
/* 125 */     }return null;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.extract.BasicTermExtractor
 * JD-Core Version:    0.6.2
 */