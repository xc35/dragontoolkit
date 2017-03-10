/*     */ package edu.drexel.cis.dragon.qa.score;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.qa.query.QueryWord;
/*     */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*     */ import edu.drexel.cis.dragon.qa.system.Candidate;
/*     */ import edu.drexel.cis.dragon.util.MathUtil;
/*     */ 
/*     */ public class SimilarityScorer
/*     */   implements CandidateScorer
/*     */ {
/*     */   private double semanticDiscount;
/*     */   private double yearDiscount;
/*     */   private boolean checkSemantics;
/*     */   private boolean checkYear;
/*     */   private boolean selfScore;
/*     */   private boolean singleSide;
/*     */   private QueryWord[] arrQWord;
/*     */   private QuestionQuery query;
/*     */   private int[] arrHeadNoun;
/*     */   private double sentScore;
/*     */   private boolean noYear;
/*     */ 
/*     */   public SimilarityScorer()
/*     */   {
/*  26 */     this(false);
/*     */   }
/*     */ 
/*     */   public SimilarityScorer(boolean selfScore) {
/*  30 */     this.checkSemantics = true;
/*  31 */     this.semanticDiscount = 0.5D;
/*  32 */     this.checkYear = false;
/*  33 */     this.yearDiscount = 0.5D;
/*  34 */     this.singleSide = true;
/*  35 */     this.selfScore = selfScore;
/*     */   }
/*     */ 
/*     */   public double initialize(QuestionQuery query, Sentence sent, QueryWord[] arrQWord)
/*     */   {
/*  42 */     this.query = query;
/*  43 */     this.arrQWord = arrQWord;
/*     */ 
/*  46 */     this.noYear = false;
/*  47 */     if (this.checkYear) {
/*  48 */       for (int i = 0; i < query.size(); i++) {
/*  49 */         QueryWord qword = query.getQueryWord(i);
/*  50 */         if ((qword.isYear()) && (qword.getFrequency() == 0)) {
/*  51 */           this.noYear = true;
/*  52 */           break;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  58 */     if ((query.getHeadNoun() != null) && (query.getAnswerType() == 0) && (this.checkSemantics)) {
/*  59 */       int i = 0;
/*  60 */       while ((i < arrQWord.length) && ((arrQWord[i] == null) || (!arrQWord[i].isHeadNoun())))
/*  61 */         i++;
/*  62 */       if (i >= arrQWord.length) {
/*  63 */         this.arrHeadNoun = null;
/*     */       }
/*     */       else {
/*  66 */         this.arrHeadNoun = new int[arrQWord[i].getFrequency()];
/*  67 */         int j = 0;
/*  68 */         for (; i < arrQWord.length; i++)
/*  69 */           if ((arrQWord[i] != null) && (arrQWord[i].isHeadNoun()))
/*  70 */             this.arrHeadNoun[(j++)] = i;
/*     */       }
/*     */     } else {
/*  73 */       this.arrHeadNoun = null;
/*     */     }
/*  75 */     this.sentScore = scoreSentence();
/*  76 */     return this.sentScore;
/*     */   }
/*     */ 
/*     */   protected double scoreSentence()
/*     */   {
/*  85 */     double unigramScore = 0.0D;
/*  86 */     for (int i = 0; i < this.query.size(); i++) {
/*  87 */       QueryWord word = this.query.getQueryWord(i);
/*  88 */       if (word.getFrequency() > 0) {
/*  89 */         unigramScore += word.getWeight();
/*     */       }
/*     */     }
/*     */ 
/*  93 */     double maxScore = 0.0D;
/*  94 */     double curScore = 0.0D;
/*  95 */     for (int i = 0; i < this.arrQWord.length; i++)
/*  96 */       if (this.arrQWord[i] == null) {
/*  97 */         if (curScore > maxScore)
/*  98 */           maxScore = curScore;
/*  99 */         curScore = 0.0D;
/*     */       }
/*     */       else {
/* 102 */         curScore += this.arrQWord[i].getWeight();
/*     */       }
/* 104 */     if (curScore > maxScore)
/* 105 */       maxScore = curScore;
/* 106 */     maxScore = Math.min(1.0D, maxScore);
/*     */ 
/* 108 */     return 0.5D * (unigramScore + maxScore);
/*     */   }
/*     */ 
/*     */   public double score(Candidate curCand)
/*     */   {
/*     */     try
/*     */     {
/* 115 */       Word start = curCand.getStartingWord();
/* 116 */       Word end = curCand.getEndingWord();
/*     */ 
/* 118 */       if (this.singleSide)
/* 119 */         curCand.setWeight(this.sentScore * scoreSingleSide(this.query, this.arrQWord, start.getPosInSentence(), end.getPosInSentence()));
/*     */       else {
/* 121 */         curCand.setWeight(this.sentScore * scoreDoubleSide(this.query, this.arrQWord, start.getPosInSentence(), end.getPosInSentence()));
/*     */       }
/*     */ 
/* 124 */       if (this.noYear) {
/* 125 */         curCand.setWeight(curCand.getWeight() * this.yearDiscount);
/*     */       }
/*     */ 
/* 128 */       if ((this.query.getAnswerType() == 0) && (this.checkSemantics)) {
/* 129 */         if (this.query.getHeadNoun() != null) {
/* 130 */           if (!connectHeadNoun(start.getPosInSentence(), end.getPosInSentence(), this.arrHeadNoun))
/* 131 */             curCand.setWeight(curCand.getWeight() * this.semanticDiscount);
/*     */           else
/* 133 */             curCand.setSemanticFrequency(1);
/*     */         }
/* 135 */         else if (this.query.getHeadVerb() != null) {
/* 136 */           if ((end.next == null) || (!end.next.getContent().equalsIgnoreCase(this.query.getHeadVerb())))
/* 137 */             curCand.setWeight(curCand.getWeight() * this.semanticDiscount);
/*     */           else
/* 139 */             curCand.setSemanticFrequency(1);
/*     */         }
/*     */       }
/* 142 */       return curCand.getWeight();
/*     */     }
/*     */     catch (Exception e) {
/* 145 */       e.printStackTrace();
/* 146 */       curCand.setWeight(0.0D);
/* 147 */     }return 0.0D;
/*     */   }
/*     */ 
/*     */   protected double scoreSingleSide(QuestionQuery query, QueryWord[] arrWord, int start, int end)
/*     */   {
/* 155 */     int lastPos = -1;
/*     */ 
/* 157 */     double[] arrScore = new double[query.size()];
/* 158 */     if (this.selfScore) {
/* 159 */       for (int i = start; i <= end; i++)
/* 160 */         if ((arrWord[i] != null) && (arrWord[i].getIndex() >= 0))
/* 161 */           arrScore[arrWord[i].getIndex()] = arrWord[i].getWeight();
/*     */     }
/* 163 */     for (int i = start - 1; i >= 0; i--)
/* 164 */       if (arrWord[i] != null) {
/* 165 */         if (lastPos == -1)
/* 166 */           lastPos = i;
/* 167 */         if (arrWord[i].getWeight() > 0.0D) {
/* 168 */           double score = arrWord[i].getWeight() * (arrWord.length + 1 + lastPos - start) / arrWord.length;
/* 169 */           if (score > arrScore[arrWord[i].getIndex()])
/* 170 */             arrScore[arrWord[i].getIndex()] = score;
/*     */         }
/*     */       }
/*     */       else {
/* 174 */         lastPos = -1;
/*     */       }
/* 176 */     double left = MathUtil.sumArray(arrScore);
/*     */ 
/* 178 */     lastPos = -1;
/* 179 */     MathUtil.initArray(arrScore, 0.0D);
/* 180 */     if (this.selfScore) {
/* 181 */       for (int i = start; i <= end; i++)
/* 182 */         if ((arrWord[i] != null) && (arrWord[i].getIndex() >= 0))
/* 183 */           arrScore[arrWord[i].getIndex()] = arrWord[i].getWeight();
/*     */     }
/* 185 */     for (int i = end + 1; i < arrWord.length; i++)
/* 186 */       if (arrWord[i] != null) {
/* 187 */         if (lastPos == -1)
/* 188 */           lastPos = i;
/* 189 */         if (arrWord[i].getWeight() > 0.0D) {
/* 190 */           double score = arrWord[i].getWeight() * (arrWord.length + 1 + end - lastPos) / arrWord.length;
/* 191 */           if (score > arrScore[arrWord[i].getIndex()])
/* 192 */             arrScore[arrWord[i].getIndex()] = score;
/*     */         }
/*     */       }
/*     */       else {
/* 196 */         lastPos = -1;
/*     */       }
/* 198 */     double right = MathUtil.sumArray(arrScore);
/*     */ 
/* 200 */     return Math.max(left, right);
/*     */   }
/*     */ 
/*     */   protected double scoreDoubleSide(QuestionQuery query, QueryWord[] arrWord, int start, int end)
/*     */   {
/* 207 */     int lastPos = -1;
/*     */ 
/* 209 */     double[] arrScore = new double[query.size()];
/* 210 */     if (this.selfScore) {
/* 211 */       for (int i = start; i <= end; i++) {
/* 212 */         if ((arrWord[i] != null) && (arrWord[i].getIndex() >= 0))
/* 213 */           arrScore[arrWord[i].getIndex()] = arrWord[i].getWeight();
/*     */       }
/*     */     }
/* 216 */     for (int i = start - 1; i >= 0; i--) {
/* 217 */       if (arrWord[i] != null) {
/* 218 */         if (lastPos == -1)
/* 219 */           lastPos = i;
/* 220 */         if (arrWord[i].getWeight() > 0.0D) {
/* 221 */           double score = arrWord[i].getWeight() * (arrWord.length + 1 + lastPos - start) / arrWord.length;
/* 222 */           if (score > arrScore[arrWord[i].getIndex()])
/* 223 */             arrScore[arrWord[i].getIndex()] = score;
/*     */         }
/*     */       }
/*     */       else {
/* 227 */         lastPos = -1;
/*     */       }
/*     */     }
/* 230 */     lastPos = -1;
/* 231 */     for (int i = end + 1; i < arrWord.length; i++)
/* 232 */       if (arrWord[i] != null) {
/* 233 */         if (lastPos == -1)
/* 234 */           lastPos = i;
/* 235 */         if (arrWord[i].getWeight() > 0.0D) {
/* 236 */           double score = arrWord[i].getWeight() * (arrWord.length + 1 + end - lastPos) / arrWord.length;
/* 237 */           if (score > arrScore[arrWord[i].getIndex()])
/* 238 */             arrScore[arrWord[i].getIndex()] = score;
/*     */         }
/*     */       }
/*     */       else {
/* 242 */         lastPos = -1;
/*     */       }
/* 244 */     return MathUtil.sumArray(arrScore);
/*     */   }
/*     */ 
/*     */   protected boolean connectHeadNoun(int start, int end, int[] arrHeadNoun)
/*     */   {
					int i;
/* 250 */     if (arrHeadNoun == null) {
/* 251 */       return false;
/*     */     }
/* 253 */     int pos = -1;
/* 254 */     for ( i = 0; i < arrHeadNoun.length; i++) {
/* 255 */       if (arrHeadNoun[i] > start) {
/*     */         break;
/*     */       }
/* 258 */       if (arrHeadNoun[i] > pos)
/* 259 */         pos = arrHeadNoun[i];
/*     */     }
/* 261 */     if ((pos >= 0) && (connectHeadNoun(start, end, pos))) {
/* 262 */       return true;
/*     */     }

/* 264 */     for (; i < arrHeadNoun.length; i++) {
/* 265 */       if (arrHeadNoun[i] >= end)
/*     */         break;
/*     */     }
/* 268 */     if (i < arrHeadNoun.length)
/* 269 */       return connectHeadNoun(start, end, arrHeadNoun[i]);
/* 270 */     return false;
/*     */   }
/*     */ 
/*     */   protected boolean connectHeadNoun(int start, int end, int headNounPos)
/*     */   {
/* 276 */     if ((headNounPos == start) || (headNounPos == end)) {
/* 277 */       return true;
/*     */     }
/* 279 */     if (headNounPos > end) {
/* 280 */       if ((this.arrQWord[(end + 1)] == null) || (this.arrQWord[(end + 1)].getContent().equals("'")))
/* 281 */         return false;
/* 282 */       for (int i = end + 1; i < headNounPos; i++)
/* 283 */         if ((this.arrQWord[i] == null) || (headNounBlock(this.arrQWord[i])))
/* 284 */           return false;
/*     */     }
/*     */     else {
/* 287 */       for (int i = start - 1; i > headNounPos; i--)
/* 288 */         if ((this.arrQWord[i] == null) || (headNounBlock(this.arrQWord[i])))
/* 289 */           return false;
/*     */     }
/* 291 */     return true;
/*     */   }
/*     */ 
/*     */   protected boolean headNounBlock(QueryWord word) {
/* 295 */     if (((word.getPOSTag() == 5) && (!word.getContent().equalsIgnoreCase("of"))) || (
/* 296 */       (word.getPOSTag() == 2) && (!word.isVerbBe()))) {
/* 297 */       return true;
/*     */     }
/* 299 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.score.SimilarityScorer
 * JD-Core Version:    0.6.2
 */