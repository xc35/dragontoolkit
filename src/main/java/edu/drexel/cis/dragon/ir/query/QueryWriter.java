/*     */ package edu.drexel.cis.dragon.ir.query;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRSection;
/*     */ import edu.drexel.cis.dragon.nlp.Concept;
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.Triple;
/*     */ import edu.drexel.cis.dragon.util.FormatUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class QueryWriter
/*     */ {
/*     */   private static final int MAX_SEC = 10;
/*     */   private boolean indexConceptEntry;
/*     */   private double subtermWeight;
/*     */   private SortedArray conceptList;
/*     */   private SortedArray relationList;
/*     */   private IRSection[] arrSections;
/*     */ 
/*     */   public QueryWriter(boolean relationSupported, boolean indexConceptEntry)
/*     */   {
/*  26 */     this.indexConceptEntry = indexConceptEntry;
/*  27 */     this.subtermWeight = 1.0D;
/*  28 */     this.arrSections = new IRSection[10];
/*  29 */     this.conceptList = new SortedArray();
/*  30 */     this.relationList = new SortedArray();
/*     */   }
/*     */ 
/*     */   public void initNewQuery() {
/*  34 */     this.conceptList.clear();
/*  35 */     this.relationList.clear();
/*     */   }
/*     */ 
/*     */   public boolean addSection(IRSection section) {
/*  39 */     if (section.getSectionID() >= 10)
/*  40 */       return false;
/*  41 */     this.arrSections[section.getSectionID()] = section;
/*  42 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean write(int section, ArrayList conceptList)
/*     */   {
/*  49 */     if (this.arrSections[section] == null) return false;
/*     */ 
/*  51 */     for (int i = 0; i < conceptList.size(); i++) {
/*  52 */       Concept curCpt = (Concept)conceptList.get(i);
/*  53 */       addConcept(section, curCpt);
/*     */     }
/*  55 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean write(int section, ArrayList conceptList, ArrayList tripleList)
/*     */   {
/*  62 */     if (this.arrSections[section] == null) return false;
/*     */ 
/*  64 */     write(section, conceptList);
/*  65 */     for (int i = 0; i < tripleList.size(); i++) {
/*  66 */       Triple curTriple = (Triple)tripleList.get(i);
/*  67 */       addTriple(section, curTriple);
/*     */     }
/*  69 */     return true;
/*     */   }
/*     */ 
/*     */   private void addTriple(int section, Triple triple)
/*     */   {
/*     */     Token first;
/*  77 */     if (this.indexConceptEntry)
/*  78 */       first = new Token(triple.getFirstConcept().getEntryID());
/*     */     else
/*  80 */       first = new Token(triple.getFirstConcept().getName());
/*  81 */     int pos;
/*  81 */     if ((pos = this.conceptList.binarySearch(first)) < 0) return;
/*  82 */      first = (Token)this.conceptList.get(pos);
/*     */     Token second;
/*  84 */     if (this.indexConceptEntry)
/*  85 */       second = new Token(triple.getSecondConcept().getEntryID());
/*     */     else
/*  87 */       second = new Token(triple.getSecondConcept().getName());
/*  88 */     if ((pos = this.conceptList.binarySearch(second)) < 0) return;
/*  89 */      second = (Token)this.conceptList.get(pos);
/*     */     Triple cur;
/*  91 */     if (first.getIndex() > second.getIndex())
/*  92 */       cur = new Triple(second, first);
/*     */     else
/*  94 */       cur = new Triple(first, second);
/*  95 */     cur.setWeight(this.arrSections[section].getWeight());
/*  96 */     cur.setIndex(this.relationList.size());
/*  97 */     if (!this.relationList.add(cur)) {
/*  98 */       Triple old = (Triple)this.relationList.get(this.relationList.insertedPos());
/*  99 */       if (cur.getWeight() > old.getWeight())
/* 100 */         old.setWeight(cur.getWeight());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addConcept(int section, Concept concept)
/*     */   {
/*     */     Token cur;
/* 106 */     if (this.indexConceptEntry)
/* 107 */       cur = new Token(concept.getEntryID());
/*     */     else
/* 109 */       cur = new Token(concept.getName());
/* 110 */     cur.setWeight(this.arrSections[section].getWeight());
/* 111 */     if (concept.isSubConcept())
/* 112 */       cur.setWeight(cur.getWeight() * this.subtermWeight);
/* 113 */     cur.setIndex(this.conceptList.size());
/* 114 */     if (!this.conceptList.add(cur)) {
/* 115 */       Token old = (Token)this.conceptList.get(this.conceptList.insertedPos());
/* 116 */       if (cur.getWeight() > old.getWeight())
/* 117 */         old.setWeight(cur.getWeight());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setSubTermWeight(double weight) {
/* 122 */     this.subtermWeight = weight;
/*     */   }
/*     */ 
/*     */   public String getQuery()
/*     */   {
/* 132 */     DecimalFormat df = FormatUtil.getNumericFormat(1, 2);
/* 133 */     StringBuffer query = new StringBuffer();
/*     */ 
/* 135 */     for (int i = 0; i < this.relationList.size(); i++) {
/* 136 */       Triple curTriple = (Triple)this.relationList.get(i);
/* 137 */       query.append("R(");
/* 138 */       if (curTriple.getWeight() != 1.0D) {
/* 139 */         query.append(df.format(curTriple.getWeight()));
/* 140 */         query.append(",");
/*     */       }
/* 142 */       query.append("TERM=");
/* 143 */       Token curToken = (Token)curTriple.getFirstConcept();
/* 144 */       query.append(curToken.getValue());
/* 145 */       query.append(" AND TERM2=");
/* 146 */       curToken = (Token)curTriple.getSecondConcept();
/* 147 */       query.append(curToken.getValue());
/* 148 */       query.append(") ");
/*     */     }
/*     */ 
/* 151 */     for (int i = 0; i < this.conceptList.size(); i++) {
/* 152 */       Token curToken = (Token)this.conceptList.get(i);
/* 153 */       query.append(" T(");
/* 154 */       if (curToken.getWeight() != 1.0D) {
/* 155 */         query.append(df.format(curToken.getWeight()));
/* 156 */         query.append(",");
/*     */       }
/* 158 */       query.append("TERM=");
/* 159 */       query.append(curToken.getValue());
/* 160 */       query.append(")");
/*     */     }
/*     */ 
/* 163 */     return query.toString();
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.query.QueryWriter
 * JD-Core Version:    0.6.2
 */