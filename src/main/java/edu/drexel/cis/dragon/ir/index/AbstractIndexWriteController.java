/*     */ package edu.drexel.cis.dragon.ir.index;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Concept;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.nlp.SimplePairList;
/*     */ import edu.drexel.cis.dragon.nlp.Triple;
/*     */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public abstract class AbstractIndexWriteController
/*     */ {
/*     */   protected SimpleElementList termKeyList;
/*     */   protected SimpleElementList docKeyList;
/*     */   protected SimplePairList relationKeyList;
/*     */   protected int curDocIndex;
/*     */   protected String curDocKey;
/*     */   protected int processedDoc;
/*     */   protected boolean relationSupported;
/*     */   protected boolean initialized;
/*     */   private SortedArray termList;
/*     */   private SortedArray relationList;
/*     */   private boolean indexConceptEntry;
/*     */ 
/*     */   public AbstractIndexWriteController(boolean relationSupported, boolean indexConceptEntry)
/*     */   {
/*  29 */     this.relationSupported = relationSupported;
/*  30 */     this.indexConceptEntry = indexConceptEntry;
/*  31 */     this.processedDoc = 0;
/*  32 */     this.initialized = false;
/*  33 */     this.termList = new SortedArray();
/*  34 */     if (relationSupported)
/*  35 */       this.relationList = new SortedArray();
/*     */   }
/*     */ 
/*     */   public abstract void initialize();
/*     */ 
/*     */   public boolean indexed(String docKey) {
/*  41 */     return this.docKeyList.contains(docKey);
/*     */   }
/*     */ 
/*     */   public int size() {
/*  45 */     return this.processedDoc;
/*     */   }
/*     */ 
/*     */   public boolean isRelationSupported() {
/*  49 */     return this.relationSupported;
/*     */   }
/*     */ 
/*     */   public boolean setDoc(String docKey) {
/*  53 */     this.curDocIndex = this.docKeyList.add(docKey);
/*  54 */     this.termList.clear();
/*  55 */     if (this.relationSupported) {
/*  56 */       this.relationList.clear();
/*     */     }
/*  58 */     if (this.curDocIndex == this.docKeyList.size() - 1) {
/*  59 */       this.curDocKey = docKey;
/*  60 */       this.processedDoc += 1;
/*  61 */       return true;
/*     */     }
/*     */ 
/*  65 */     this.curDocKey = null;
/*  66 */     return false;
/*     */   }
/*     */ 
/*     */   protected IRTerm[] getIRTermArray(ArrayList irtermList, IRDoc curDoc)
/*     */   {
/*  74 */     IRTerm[] arrTerms = new IRTerm[irtermList.size()];
/*  75 */     curDoc.setTermNum(arrTerms.length);
/*  76 */     int count = 0;
/*  77 */     for (int i = 0; i < irtermList.size(); i++) {
/*  78 */       arrTerms[i] = ((IRTerm)irtermList.get(i));
/*  79 */       count += arrTerms[i].getFrequency();
/*     */     }
/*  81 */     curDoc.setTermCount(count);
/*  82 */     return arrTerms;
/*     */   }
/*     */ 
/*     */   protected IRRelation[] getIRRelationArray(ArrayList irRelationList, IRDoc curDoc)
/*     */   {
/*  89 */     IRRelation[] arrRelations = new IRRelation[irRelationList.size()];
/*  90 */     curDoc.setRelationNum(arrRelations.length);
/*  91 */     int count = 0;
/*  92 */     for (int i = 0; i < irRelationList.size(); i++) {
/*  93 */       arrRelations[i] = ((IRRelation)irRelationList.get(i));
/*  94 */       count += arrRelations[i].getFrequency();
/*     */     }
/*  96 */     curDoc.setRelationCount(count);
/*  97 */     return arrRelations;
/*     */   }
/*     */ 
/*     */   protected SortedArray generateIRTermList(ArrayList termList)
/*     */   {
/* 105 */     SortedArray newList = new SortedArray();
/* 106 */     for (int i = 0; i < termList.size(); i++) {
/* 107 */       IRTerm curIRTerm = getIRTerm((Concept)termList.get(i));
/* 108 */       if (!newList.add(curIRTerm)) {
/* 109 */         ((IRTerm)newList.get(newList.insertedPos())).addFrequency(curIRTerm.getFrequency());
/*     */       }
/*     */     }
/* 112 */     return newList;
/*     */   }
/*     */ 
/*     */   protected SortedArray generateIRRelationList(ArrayList tripleList)
/*     */   {
/* 120 */     SortedArray newList = new SortedArray(new IndexComparator());
/* 121 */     for (int i = 0; i < tripleList.size(); i++) {
/* 122 */       IRRelation curIRRelation = getIRRelation((Triple)tripleList.get(i));
/* 123 */       if (!newList.add(curIRRelation)) {
/* 124 */         ((IRRelation)newList.get(newList.insertedPos())).addFrequency(curIRRelation.getFrequency());
/*     */       }
/*     */     }
/* 127 */     return newList;
/*     */   }
/*     */ 
/*     */   private IRRelation getIRRelation(Triple triple)
/*     */   {
/* 134 */     int first = getIRTerm(triple.getFirstConcept()).getIndex();
/* 135 */     int second = getIRTerm(triple.getSecondConcept()).getIndex();
/*     */     IRRelation cur;
/* 137 */     if (first > second)
/* 138 */       cur = new IRRelation(second, first, triple.getFrequency());
/*     */     else
/* 140 */       cur = new IRRelation(first, second, triple.getFrequency());
/* 141 */     if (this.relationList.add(cur)) {
/* 142 */       int index = this.relationKeyList.add(cur.getFirstTerm(), cur.getSecondTerm());
/* 143 */       cur.setIndex(index);
/*     */     }
/*     */     else {
/* 146 */       cur.setIndex(((IRRelation)this.relationList.get(this.relationList.insertedPos())).getIndex());
/*     */     }
/* 148 */     return cur;
/*     */   }
/*     */ 
/*     */   private IRTerm getIRTerm(Concept concept)
/*     */   {
/*     */     IRTerm cur;
/* 155 */     if (this.indexConceptEntry)
/* 156 */       cur = new IRTerm(new String(concept.getEntryID()), -1, concept.getFrequency());
/*     */     else
/* 158 */       cur = new IRTerm(new String(concept.getName()), -1, concept.getFrequency());
/* 159 */     if (this.termList.add(cur)) {
/* 160 */       int index = this.termKeyList.add(cur.getKey());
/* 161 */       cur.setIndex(index);
/*     */     }
/*     */     else {
/* 164 */       cur.setIndex(((IRTerm)this.termList.get(this.termList.insertedPos())).getIndex());
/*     */     }
/* 166 */     return cur;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.AbstractIndexWriteController
 * JD-Core Version:    0.6.2
 */