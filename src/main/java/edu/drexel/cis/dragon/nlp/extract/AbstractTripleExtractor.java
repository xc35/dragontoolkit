/*     */ package edu.drexel.cis.dragon.nlp.extract;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Concept;
/*     */ import edu.drexel.cis.dragon.nlp.Document;
/*     */ import edu.drexel.cis.dragon.nlp.DocumentParser;
/*     */ import edu.drexel.cis.dragon.nlp.Paragraph;
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.Triple;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ 
/*     */ public abstract class AbstractTripleExtractor
/*     */   implements TripleExtractor
/*     */ {
/*     */   protected ConceptExtractor conceptExtractor;
/*     */   protected ConceptFilter cf;
/*     */   protected CoReference coReference;
/*     */   protected ClauseFinder clauseFinder;
/*     */   protected CoordinatingChecker coordinatingChecker;
/*     */   protected ArrayList conceptList;
/*     */   protected ArrayList tripleList;
/*     */   protected boolean coReference_enabled;
/*     */   protected boolean semanticCheck_enabled;
/*     */   protected boolean relationCheck_enabled;
/*     */   protected boolean clauseIdentify_enabled;
/*     */   protected boolean coordinatingCheck_enabled;
/*     */   protected boolean conceptFilter_enabled;
/*     */ 
/*     */   public AbstractTripleExtractor(ConceptExtractor te)
/*     */   {
/*  32 */     this.conceptExtractor = te;
/*  33 */     this.coReference_enabled = false;
/*  34 */     this.relationCheck_enabled = false;
/*  35 */     this.clauseIdentify_enabled = false;
/*  36 */     this.semanticCheck_enabled = true;
/*  37 */     this.coReference = new CoReference();
/*  38 */     this.clauseFinder = new ClauseFinder();
/*  39 */     this.coordinatingChecker = new CoordinatingChecker();
/*     */   }
/*     */ 
/*     */   public void initDocExtraction() {
/*  43 */     this.conceptExtractor.initDocExtraction();
/*     */   }
/*     */ 
/*     */   public ArrayList getConceptList() {
/*  47 */     return this.conceptList;
/*     */   }
/*     */ 
/*     */   public ArrayList getTripleList() {
/*  51 */     return this.tripleList;
/*     */   }
/*     */ 
/*     */   public ConceptExtractor getConceptExtractor() {
/*  55 */     return this.conceptExtractor;
/*     */   }
/*     */ 
/*     */   public boolean getFilteringOption() {
/*  59 */     return this.conceptFilter_enabled;
/*     */   }
/*     */ 
/*     */   public void setFilteringOption(boolean option) {
/*  63 */     this.conceptFilter_enabled = option;
/*     */   }
/*     */ 
/*     */   public void setConceptFilter(ConceptFilter cf) {
/*  67 */     this.cf = cf;
/*  68 */     this.conceptFilter_enabled = (cf != null);
/*     */   }
/*     */ 
/*     */   public ConceptFilter getConceptFilter() {
/*  72 */     return this.cf;
/*     */   }
/*     */ 
/*     */   public void setCoordinatingCheckOption(boolean option) {
/*  76 */     this.coordinatingCheck_enabled = option;
/*     */   }
/*     */ 
/*     */   public boolean getCoordinatingCheckOption() {
/*  80 */     return this.coordinatingCheck_enabled;
/*     */   }
/*     */ 
/*     */   public void setCoReferenceOption(boolean option) {
/*  84 */     this.coReference_enabled = option;
/*     */   }
/*     */ 
/*     */   public boolean getCoReferenceOption() {
/*  88 */     return this.coReference_enabled;
/*     */   }
/*     */ 
/*     */   public boolean getSemanticCheckOption() {
/*  92 */     return this.semanticCheck_enabled;
/*     */   }
/*     */ 
/*     */   public void setSemanticCheckOption(boolean option) {
/*  96 */     this.semanticCheck_enabled = option;
/*     */   }
/*     */ 
/*     */   public boolean getRelationCheckOption() {
/* 100 */     return this.relationCheck_enabled;
/*     */   }
/*     */ 
/*     */   public void setRelationCheckOption(boolean option) {
/* 104 */     this.relationCheck_enabled = option;
/*     */   }
/*     */ 
/*     */   public boolean getClauseIdentifyOption() {
/* 108 */     return this.clauseIdentify_enabled;
/*     */   }
/*     */ 
/*     */   public void setClauseIdentifyOption(boolean option) {
/* 112 */     this.clauseIdentify_enabled = option;
/*     */   }
/*     */ 
/*     */   public void print(PrintWriter out) {
/* 116 */     print(out, this.conceptList, this.tripleList);
/*     */   }
/*     */ 
/*     */   public void print(PrintWriter out, ArrayList conceptList, ArrayList tripleList)
/*     */   {
/*     */     try
/*     */     {
/* 125 */       for (int i = 0; i < conceptList.size(); i++) {
/* 126 */         Concept concept = (Concept)conceptList.get(i);
/* 127 */         out.write(concept.getName() + ": ");
/* 128 */         out.write(String.valueOf(concept.getFrequency()));
/* 129 */         if (concept.getEntryID() != null) {
/* 130 */           out.write(", " + concept.getEntryID());
/*     */         }
/* 132 */         if (concept.getSemanticType() != null) {
/* 133 */           out.write(", " + concept.getSemanticType());
/*     */         }
/* 135 */         out.write("\n");
/*     */       }
/*     */ 
/* 138 */       for (int i = 0; i < tripleList.size(); i++) {
/* 139 */         Triple triple = (Triple)tripleList.get(i);
/* 140 */         out.write(triple.getFirstConcept().getName());
/* 141 */         out.write("<->");
/* 142 */         out.write(triple.getSecondConcept().getName());
/* 143 */         out.write(40);
/* 144 */         out.write(String.valueOf(triple.getFrequency()));
/* 145 */         out.write(41);
/* 146 */         out.write("\n");
/*     */       }
/* 148 */       out.flush();
/*     */     }
/*     */     catch (Exception e) {
/* 151 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean extractFromDoc(String doc) {
/* 156 */     return extractFromDoc(this.conceptExtractor.getDocumentParser().parse(doc));
/*     */   }
/*     */ 
/*     */   public boolean extractFromDoc(Document doc)
/*     */   {
/* 164 */     this.conceptList = new ArrayList();
/* 165 */     this.tripleList = new ArrayList();
/*     */ 
/* 168 */     Paragraph pg = doc.getFirstParagraph();
/* 169 */     while (pg != null)
/*     */     {
/* 171 */       Sentence sent = pg.getFirstSentence();
/* 172 */       while (sent != null)
/*     */       {
/* 175 */         if (sent.getFirstWord() != null) {
/* 176 */           ArrayList curTermList = this.conceptExtractor.extractFromSentence(sent);
/* 177 */           if (curTermList != null) {
/* 178 */             this.conceptList.addAll(curTermList);
/* 179 */             ArrayList curTripleList = extractFromSentence(sent);
/* 180 */             if (curTripleList != null) {
/* 181 */               this.tripleList.addAll(curTripleList);
/* 182 */               curTripleList.clear();
/*     */             }
/* 184 */             curTermList.clear();
/*     */           }
/*     */         }
/* 187 */         sent = sent.next;
/*     */       }
/* 189 */       pg = pg.next;
/*     */     }
/* 191 */     return true;
/*     */   }
/*     */ 
/*     */   public SortedArray mergeTriples(SortedArray mergedConceptList, ArrayList tripleList)
/*     */   {
/* 199 */     SortedArray newList = new SortedArray();
/* 200 */     for (int i = 0; i < tripleList.size(); i++) {
/* 201 */       Triple triple = (Triple)tripleList.get(i);
/* 202 */       int left = mergedConceptList.binarySearch(triple.getFirstConcept());
/* 203 */       if (left >= 0) {
/* 204 */         int right = mergedConceptList.binarySearch(triple.getSecondConcept());
/* 205 */         if (right >= 0)
/*     */         {
/*     */           Triple newTriple;
 
/* 206 */           if (mergedConceptList.getComparator().compare(mergedConceptList.get(left), mergedConceptList.get(right)) > 0)
/* 207 */             newTriple = new Triple((Concept)mergedConceptList.get(right), (Concept)mergedConceptList.get(left));
/*     */           else
/* 209 */             newTriple = new Triple((Concept)mergedConceptList.get(left), (Concept)mergedConceptList.get(right));
/* 210 */           if (!newList.add(newTriple)) {
/* 211 */             ((Triple)newList.get(newList.insertedPos())).addFrequency(triple.getFrequency());
/*     */           }
/*     */           else {
/* 214 */             newTriple.setFrequency(triple.getFrequency());
/* 215 */             newTriple.setTUI(triple.getTUI());
/* 216 */             newTriple.setCandidateTUI(triple.getCandidateTUI());
/* 217 */             newTriple.setIndex(newList.size() - 1);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 222 */     return newList;
/*     */   }
/*     */ 
/*     */   protected boolean checkCoordinateTerms(Concept first, Concept second)
/*     */   {
/* 229 */     Word cur = first.getEndingWord().next;
/* 230 */     if ((cur == null) || (!cur.getContent().equalsIgnoreCase("and"))) return false;
/* 231 */     int gap = second.getStartingWord().getPosInSentence() - cur.getPosInSentence();
/* 232 */     if ((gap < 1) || (gap > 2)) return false;
/* 233 */     cur = first.getStartingWord().prev;
/* 234 */     if (cur != null)
/*     */     {
/* 236 */       if ((cur.getContent().equalsIgnoreCase("of")) || (cur.getContent().equalsIgnoreCase("between"))) {
/* 237 */         return false;
/*     */       }
/* 239 */       cur = cur.prev;
/* 240 */       if ((cur != null) && ((cur.getContent().equalsIgnoreCase("of")) || (cur.getContent().equalsIgnoreCase("between")))) {
/* 241 */         return false;
/*     */       }
/*     */     }
/* 244 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.extract.AbstractTripleExtractor
 * JD-Core Version:    0.6.2
 */