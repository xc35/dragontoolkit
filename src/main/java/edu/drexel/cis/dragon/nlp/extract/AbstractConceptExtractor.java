/*     */ package edu.drexel.cis.dragon.nlp.extract;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Concept;
/*     */ import edu.drexel.cis.dragon.nlp.Document;
/*     */ import edu.drexel.cis.dragon.nlp.DocumentParser;
/*     */ import edu.drexel.cis.dragon.nlp.Paragraph;
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.compare.ConceptEntryIDComparator;
/*     */ import edu.drexel.cis.dragon.nlp.compare.ConceptNameComparator;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ 
/*     */ public abstract class AbstractConceptExtractor
/*     */   implements ConceptExtractor
/*     */ {
/*     */   protected ArrayList conceptList;
/*     */   protected boolean conceptFilter_enabled;
/*     */   protected boolean subconcept_enabled;
/*     */   protected ConceptFilter cf;
/*     */   protected DocumentParser parser;
/*     */ 
/*     */   public AbstractConceptExtractor()
/*     */   {
/*  27 */     this.conceptFilter_enabled = false;
/*  28 */     this.subconcept_enabled = false;
/*  29 */     this.cf = null;
/*  30 */     this.conceptList = null;
/*  31 */     this.parser = new EngDocumentParser();
/*     */   }
/*     */ 
/*     */   public void setSubConceptOption(boolean option) {
/*  35 */     this.subconcept_enabled = option;
/*     */   }
/*     */ 
/*     */   public boolean getSubConceptOption() {
/*  39 */     return this.subconcept_enabled;
/*     */   }
/*     */ 
/*     */   public boolean getFilteringOption() {
/*  43 */     return this.conceptFilter_enabled;
/*     */   }
/*     */ 
/*     */   public void setFilteringOption(boolean option) {
/*  47 */     this.conceptFilter_enabled = option;
/*     */   }
/*     */ 
/*     */   public void setConceptFilter(ConceptFilter cf) {
/*  51 */     this.cf = cf;
/*  52 */     this.conceptFilter_enabled = (cf != null);
/*     */   }
/*     */ 
/*     */   public ConceptFilter getConceptFilter() {
/*  56 */     return this.cf;
/*     */   }
/*     */ 
/*     */   public ArrayList getConceptList() {
/*  60 */     return this.conceptList;
/*     */   }
/*     */ 
/*     */   public void print(PrintWriter out)
/*     */   {
/*  65 */     if ((this.conceptList == null) || (this.conceptList.size() == 0)) {
/*  66 */       return;
/*     */     }
/*  68 */     print(out, this.conceptList);
/*     */   }
/*     */ 
/*     */   public void print(PrintWriter out, ArrayList conceptList)
/*     */   {
/*     */     try
/*     */     {
/*  76 */       out.write(conceptList.size() + "\n");
/*  77 */       for (int i = 0; i < conceptList.size(); i++) {
/*  78 */         Concept t = (Concept)conceptList.get(i);
/*  79 */         out.write(t.getName() + "\t" + t.getFrequency() + "\n");
/*  80 */         out.flush();
/*     */       }
/*  82 */       out.close();
/*     */     }
/*     */     catch (Exception ex) {
/*  85 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public SortedArray mergeConceptByEntryID(ArrayList termList) {
/*  90 */     return mergeTerms(termList, new ConceptEntryIDComparator());
/*     */   }
/*     */ 
/*     */   public SortedArray mergeConceptByName(ArrayList termList) {
/*  94 */     return mergeTerms(termList, new ConceptNameComparator());
/*     */   }
/*     */ 
/*     */   private SortedArray mergeTerms(ArrayList termList, Comparator comparator)
/*     */   {
/* 102 */     SortedArray newList = new SortedArray(comparator);
/* 103 */     for (int i = 0; i < termList.size(); i++) {
/* 104 */       Concept oldTerm = (Concept)termList.get(i);
/* 105 */       int pos = newList.binarySearch(oldTerm);
/* 106 */       if (pos >= 0) {
/* 107 */         Concept newTerm = (Concept)newList.get(pos);
/* 108 */         newTerm.addFrequency(oldTerm.getFrequency());
/*     */       }
/*     */       else
/*     */       {
/* 112 */         pos = pos * -1 - 1;
/* 113 */         Concept newTerm = oldTerm.copy();
/* 114 */         newTerm.setIndex(newList.size());
/* 115 */         newList.add(pos, newTerm);
/*     */       }
/*     */     }
/* 118 */     return newList;
/*     */   }
/*     */ 
/*     */   public synchronized ArrayList extractFromDoc(String doc) {
/* 122 */     return extractFromDoc(this.parser.parse(doc));
/*     */   }
/*     */ 
/*     */   public synchronized ArrayList extractFromDoc(Document doc)
/*     */   {
/*     */     try
/*     */     {
/* 130 */       this.conceptList = new ArrayList(20);
/* 131 */       if (doc == null)
/* 132 */         return this.conceptList;
/* 133 */       Paragraph pg = doc.getFirstParagraph();
/* 134 */       while (pg != null) {
/* 135 */         Sentence sent = pg.getFirstSentence();
/* 136 */         while (sent != null)
/*     */         {
/* 138 */           if (sent.getFirstWord() != null) {
/* 139 */             ArrayList curTermList = extractFromSentence(sent);
/* 140 */             if (curTermList != null) {
/* 141 */               this.conceptList.addAll(curTermList);
/* 142 */               curTermList.clear();
/*     */             }
/*     */           }
/* 145 */           sent = sent.next;
/*     */         }
/* 147 */         pg = pg.next;
/*     */       }
/* 149 */       return this.conceptList;
/*     */     }
/*     */     catch (Exception e) {
/* 152 */       e.printStackTrace();
/* 153 */     }return null;
/*     */   }
/*     */ 
/*     */   public DocumentParser getDocumentParser()
/*     */   {
/* 158 */     return this.parser;
/*     */   }
/*     */ 
/*     */   public void setDocumentParser(DocumentParser parser) {
/* 162 */     this.parser = parser;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.extract.AbstractConceptExtractor
 * JD-Core Version:    0.6.2
 */