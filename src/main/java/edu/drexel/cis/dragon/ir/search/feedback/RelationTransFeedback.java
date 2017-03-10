/*     */ package edu.drexel.cis.dragon.ir.search.feedback;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IRRelation;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.ir.query.IRQuery;
/*     */ import edu.drexel.cis.dragon.ir.query.SimpleTermPredicate;
/*     */ import edu.drexel.cis.dragon.ir.search.Searcher;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSparseMatrix;
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*     */ import edu.drexel.cis.dragon.nlp.compare.WeightComparator;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class RelationTransFeedback extends AbstractFeedback
/*     */ {
/*     */   DoubleSparseMatrix transMatrix;
/*     */   private int expandTermNum;
/*     */   private double bkgCoeffi;
/*     */   private boolean selfTranslation;
/*     */   private boolean generativeModel;
/*     */ 
/*     */   public RelationTransFeedback(Searcher searcher, int feedbackDocNum, int expandTermNum, double feedbackCoeffi)
/*     */   {
/*  28 */     super(searcher, feedbackDocNum, feedbackCoeffi);
/*  29 */     this.expandTermNum = expandTermNum;
/*  30 */     this.transMatrix = null;
/*  31 */     this.selfTranslation = true;
/*  32 */     this.generativeModel = false;
/*     */   }
/*     */ 
/*     */   public RelationTransFeedback(Searcher searcher, int feedbackDocNum, int expandTermNum, double feedbackCoeffi, DoubleSparseMatrix transMatrix) {
/*  36 */     super(searcher, feedbackDocNum, feedbackCoeffi);
/*  37 */     this.expandTermNum = expandTermNum;
/*  38 */     this.transMatrix = transMatrix;
/*  39 */     this.selfTranslation = false;
/*  40 */     this.generativeModel = false;
/*     */   }
/*     */ 
/*     */   public RelationTransFeedback(Searcher searcher, int feedbackDocNum, int expandTermNum, double feedbackCoeffi, double bkgCoeffi) {
/*  44 */     super(searcher, feedbackDocNum, feedbackCoeffi);
/*  45 */     this.expandTermNum = expandTermNum;
/*  46 */     this.bkgCoeffi = bkgCoeffi;
/*  47 */     this.transMatrix = null;
/*  48 */     this.selfTranslation = true;
/*  49 */     this.generativeModel = true;
/*     */   }
/*     */ 
/*     */   public RelationTransFeedback(Searcher searcher, int feedbackDocNum, int expandTermNum, double feedbackCoeffi, double bkgCoeffi, DoubleSparseMatrix transMatrix) {
/*  53 */     super(searcher, feedbackDocNum, feedbackCoeffi);
/*  54 */     this.expandTermNum = expandTermNum;
/*  55 */     this.bkgCoeffi = bkgCoeffi;
/*  56 */     this.transMatrix = transMatrix;
/*  57 */     this.selfTranslation = false;
/*  58 */     this.generativeModel = true;
/*     */   }
/*     */ 
/*     */   protected ArrayList estimateNewQueryModel(IRQuery oldQuery)
/*     */   {
/*  68 */     this.searcher.search(oldQuery);
/*  69 */     int docNum = this.feedbackDocNum < this.searcher.getRetrievedDocNum() ? this.feedbackDocNum : this.searcher.getRetrievedDocNum();
/*  70 */     if (docNum == 0) return null;
/*     */     ArrayList relationList;
/*  72 */     if (this.generativeModel)
/*  73 */       relationList = generativeModel(docNum);
/*     */     else
/*  75 */       relationList = associationModel(docNum, oldQuery);
/*  76 */     ArrayList termList = translate(relationList);
/*     */ 
/*  79 */     int predicateNum = oldQuery.getChildNum() + this.expandTermNum < termList.size() ? oldQuery.getChildNum() + this.expandTermNum : termList.size();
/*  80 */     ArrayList newPredicateList = new ArrayList(predicateNum);
/*  81 */     double weightSum = 0.0D;
/*  82 */     for (int i = 0; i < predicateNum; i++) weightSum += ((Token)termList.get(i)).getWeight();
/*  83 */     for (int i = 0; i < predicateNum; i++) {
/*  84 */       Token curToken = (Token)termList.get(i);
/*  85 */       SimpleTermPredicate curPredicate = buildSimpleTermPredicate(curToken.getIndex(), curToken.getWeight() / weightSum);
/*  86 */       newPredicateList.add(curPredicate);
/*     */     }
/*  88 */     return newPredicateList;
/*     */   }
/*     */ 
/*     */   private ArrayList associationModel(int docNum, IRQuery oldQuery)
/*     */   {
/* 102 */     IndexReader indexReader = this.searcher.getIndexReader();
/* 103 */     SortedArray relationList = new SortedArray(new IndexComparator());
/* 104 */     SortedArray oldTermList = new SortedArray();
/* 105 */     for (int i = 0; i < oldQuery.getChildNum(); i++) {
/* 106 */       SimpleTermPredicate curPredicate = (SimpleTermPredicate)oldQuery.getChild(i);
/* 107 */       if (curPredicate.getDocFrequency() > 0) {
/* 108 */         oldTermList.add(new Integer(curPredicate.getIndex()));
/*     */       }
/*     */     }
/*     */ 
/* 112 */     double weightSum = 0.0D;
/* 113 */     for (int i = 0; i < docNum; i++) {
/* 114 */       IRDoc curDoc = this.searcher.getIRDoc(i);
/* 115 */       int[] arrRelationIndex = indexReader.getRelationIndexList(curDoc.getIndex());
/* 116 */       int[] arrRelationFreq = indexReader.getRelationFrequencyList(curDoc.getIndex());
/* 117 */       for (int j = 0; j < arrRelationIndex.length; j++) {
/* 118 */         IRRelation curRelation = indexReader.getIRRelation(arrRelationIndex[j]);
/* 119 */         if ((oldTermList.contains(new Integer(curRelation.getFirstTerm()))) || 
/* 120 */           (oldTermList.contains(new Integer(curRelation.getSecondTerm())))) {
/* 121 */           Token curToken = new Token(null);
/* 122 */           curToken.setWeight(arrRelationFreq[j]);
/* 123 */           curToken.setIndex(arrRelationIndex[j]);
/* 124 */           if (!relationList.add(curToken)) {
/* 125 */             Token oldToken = (Token)relationList.get(relationList.insertedPos());
/* 126 */             oldToken.setWeight(oldToken.getWeight() + curToken.getWeight());
/*     */           }
/* 128 */           weightSum += curToken.getWeight();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 133 */     for (int i = 0; i < relationList.size(); i++) {
/* 134 */       Token curToken = (Token)relationList.get(i);
/* 135 */       curToken.setWeight(curToken.getWeight() / weightSum);
/*     */     }
/* 137 */     relationList.setComparator(new WeightComparator(true));
/* 138 */     return relationList;
/*     */   }
/*     */ 
/*     */   private ArrayList generativeModel(int docNum)
/*     */   {
/* 151 */     IndexReader indexReader = this.searcher.getIndexReader();
/*     */ 
/* 154 */     SortedArray relationList = new SortedArray(new IndexComparator());
/* 155 */     for (int i = 0; i < docNum; i++) {
/* 156 */       IRDoc curDoc = this.searcher.getIRDoc(i);
/* 157 */       int[] arrIndex = indexReader.getRelationIndexList(curDoc.getIndex());
/* 158 */       int[] arrFreq = indexReader.getRelationFrequencyList(curDoc.getIndex());
/* 159 */       for (int j = 0; j < arrIndex.length; j++) {
/* 160 */         Token curToken = new Token(null);
/* 161 */         curToken.setIndex(arrIndex[j]);
/* 162 */         curToken.setFrequency(arrFreq[j]);
/* 163 */         if (!relationList.add(curToken)) {
/* 164 */           ((Token)relationList.get(relationList.insertedPos())).addFrequency(curToken.getFrequency());
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 170 */     int iterationNum = 15;
/* 171 */     double[] arrProb = new double[relationList.size()];
/* 172 */     double[] arrCollectionProb = new double[relationList.size()];
/* 173 */     double collectionRelationCount = indexReader.getCollection().getRelationCount();
/* 174 */     for (int i = 0; i < relationList.size(); i++) {
/* 175 */       Token curToken = (Token)relationList.get(i);
/* 176 */       curToken.setWeight(1.0D / relationList.size());
/* 177 */       arrCollectionProb[i] = (this.bkgCoeffi * indexReader.getIRRelation(curToken.getIndex()).getFrequency() / collectionRelationCount);
/*     */     }
/*     */ 
/* 181 */     for (int i = 0; i < iterationNum; i++) {
/* 182 */       double weightSum = 0.0D;
/* 183 */       for (int j = 0; j < relationList.size(); j++) {
/* 184 */         Token curToken = (Token)relationList.get(j);
/* 185 */         arrProb[j] = ((1.0D - this.bkgCoeffi) * curToken.getWeight() / ((1.0D - this.bkgCoeffi) * curToken.getWeight() + arrCollectionProb[j]) * curToken.getFrequency());
/* 186 */         weightSum += arrProb[j];
/*     */       }
/* 188 */       for (int j = 0; j < relationList.size(); j++)
/* 189 */         ((Token)relationList.get(j)).setWeight(arrProb[j] / weightSum);
/*     */     }
/* 191 */     relationList.setComparator(new WeightComparator(true));
/* 192 */     return relationList;
/*     */   }
/*     */ 
/*     */   private SortedArray translate(ArrayList relationList)
/*     */   {
/* 206 */     IndexReader reader = this.searcher.getIndexReader();
/* 207 */     double[] arrWeight = new double[reader.getCollection().getTermNum()];
/* 208 */     for (int i = 0; i < arrWeight.length; i++) arrWeight[i] = 0.0D;
/*     */     int relationNum;
/* 210 */     if (this.selfTranslation)
/* 211 */       relationNum = relationList.size();
/*     */     else {
/* 213 */       relationNum = 100 < relationList.size() ? 100 : relationList.size();
/*     */     }
/* 215 */     for (int i = 0; i < relationNum; i++) {
/* 216 */       Token curToken = (Token)relationList.get(i);
/* 217 */       if (this.selfTranslation) {
/* 218 */         IRRelation curRelation = reader.getIRRelation(curToken.getIndex());
/* 219 */         arrWeight[curRelation.getFirstTerm()] += curToken.getWeight();
/* 220 */         arrWeight[curRelation.getSecondTerm()] += curToken.getWeight();
/*     */       }
/*     */       else {
/* 223 */         int[] arrIndex = this.transMatrix.getNonZeroColumnsInRow(curToken.getIndex());
/* 224 */         double[] arrTransProb = this.transMatrix.getNonZeroDoubleScoresInRow(curToken.getIndex());
/* 225 */         if (arrIndex != null) {
/* 226 */           for (int j = 0; j < arrIndex.length; j++) {
/* 227 */             arrWeight[arrIndex[j]] += curToken.getWeight() * arrTransProb[j];
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 232 */     SortedArray termList = new SortedArray(new WeightComparator(true));
/* 233 */     for (int i = 0; i < arrWeight.length; i++) {
/* 234 */       if (arrWeight[i] > 0.0D) {
/* 235 */         Token curToken = new Token(null);
/* 236 */         curToken.setWeight(arrWeight[i]);
/* 237 */         curToken.setIndex(i);
/* 238 */         termList.add(curToken);
/*     */       }
/*     */     }
/* 241 */     return termList;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.search.feedback.RelationTransFeedback
 * JD-Core Version:    0.6.2
 */