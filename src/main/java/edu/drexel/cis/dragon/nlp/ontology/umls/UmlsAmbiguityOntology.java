/*     */ package edu.drexel.cis.dragon.nlp.ontology.umls;
/*     */ 
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSuperSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.Term;
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.nlp.ontology.Ontology;
/*     */ import edu.drexel.cis.dragon.nlp.ontology.SemanticNet;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*     */ import edu.drexel.cis.dragon.util.EnvVariable;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class UmlsAmbiguityOntology extends UmlsOntology
/*     */   implements Ontology
/*     */ {
/*     */   private double minScore;
/*     */   private double subtermMinScore;
/*     */   private int maxSkippedWords;
/*     */   private double minSelectivity;
/*     */   private SparseMatrix wtMatrix;
/*     */   private UmlsTokenList tokenList;
/*     */   private UmlsCUIList cuiList;
/*     */   private ArrayList cuiListByIndex;
/*     */   private File directory;
/*     */   private UmlsSemanticNet snNet;
/*     */ 
/*     */   public UmlsAmbiguityOntology(Lemmatiser lemmatiser)
/*     */   {
/*  32 */     this(EnvVariable.getDragonHome() + "/nlpdata/umls", lemmatiser);
/*     */   }
/*     */ 
/*     */   public UmlsAmbiguityOntology(String workDir, Lemmatiser lemmatiser) {
/*  36 */     super(lemmatiser);
/*  37 */     if ((!FileUtil.exist(workDir)) && (FileUtil.exist(EnvVariable.getDragonHome() + "/" + workDir)))
/*  38 */       workDir = EnvVariable.getDragonHome() + "/" + workDir;
/*  39 */     this.directory = new File(workDir);
/*  40 */     System.out.println(new Date() + " Loading Token CUI Matrix...");
/*  41 */     if (FileUtil.exist(workDir + "/tokencui.index"))
/*  42 */       this.wtMatrix = new DoubleSuperSparseMatrix(this.directory + "/tokencui.index", this.directory + "/tokencui.matrix");
/*     */     else
/*  44 */       this.wtMatrix = new DoubleSuperSparseMatrix(this.directory + "/index.list", this.directory + "/tokencui.matrix");
/*  45 */     if (FileUtil.exist(workDir + "/token.bin"))
/*  46 */       this.tokenList = new UmlsTokenList(this.directory + "/token.bin", true);
/*     */     else
/*  48 */       this.tokenList = new UmlsTokenList(this.directory + "/token.list", false);
/*  49 */     if (FileUtil.exist(workDir + "/cui.bin"))
/*  50 */       this.cuiList = new UmlsCUIList(this.directory + "/cui.bin", true, false);
/*     */     else
/*  52 */       this.cuiList = new UmlsCUIList(this.directory + "/cui.list", false);
/*  53 */     this.cuiListByIndex = this.cuiList.getListSortedByIndex();
/*  54 */     UmlsSTYList styList = new UmlsSTYList(this.directory + "/semantictype.list");
/*  55 */     UmlsRelationNet relationNet = new UmlsRelationNet(this.directory + "/semanticrelation.list", styList);
/*  56 */     this.snNet = new UmlsSemanticNet(this, styList, relationNet);
/*  57 */     System.out.println(new Date() + " Ontology Loading Done!");
/*     */ 
/*  59 */     this.maxSkippedWords = 1;
/*  60 */     this.minScore = 0.95D;
/*  61 */     this.subtermMinScore = 0.99D;
/*  62 */     this.minSelectivity = 0.0D;
/*     */   }
/*     */ 
/*     */   public void setMinScore(double minScore) {
/*  66 */     this.minScore = minScore;
/*     */   }
/*     */ 
/*     */   public double getMinScore() {
/*  70 */     return this.minScore;
/*     */   }
/*     */ 
/*     */   public void setMinSelectivity(double minSelectivity) {
/*  74 */     this.minSelectivity = minSelectivity;
/*     */   }
/*     */ 
/*     */   public double getMinSelectivity() {
/*  78 */     return this.minSelectivity;
/*     */   }
/*     */ 
/*     */   public void setMaxSkippedWords(int num) {
/*  82 */     this.maxSkippedWords = num;
/*     */   }
/*     */ 
/*     */   public int getMaxSkippedWords() {
/*  86 */     return this.maxSkippedWords;
/*     */   }
/*     */ 
/*     */   public SemanticNet getSemanticNet() {
/*  90 */     return this.snNet;
/*     */   }
/*     */ 
/*     */   public String[] getSemanticType(String[] cuis)
/*     */   {
/*  98 */     SortedArray typeList = new SortedArray(3);
/*  99 */     for (int i = 0; i < cuis.length; i++)
/*     */     {
/* 101 */       String[] arrTypes = getSemanticType(cuis[i]);
/* 102 */       if (arrTypes != null) {
/* 103 */         for (int j = 0; j < arrTypes.length; j++)
/* 104 */           typeList.add(arrTypes[j]);
/*     */       }
/*     */     }
/* 107 */     if (typeList.size() > 0) {
/* 108 */       String[] arrTypes = new String[typeList.size()];
/* 109 */       for (int i = 0; i < typeList.size(); i++)
/* 110 */         arrTypes[i] = ((String)typeList.get(i));
/* 111 */       return arrTypes;
/*     */     }
/*     */ 
/* 114 */     return null;
/*     */   }
/*     */ 
/*     */   public String[] getSemanticType(String cui)
/*     */   {
/* 121 */     UmlsCUI cur = this.cuiList.lookup(cui);
/* 122 */     if (cur == null) {
/* 123 */       return null;
/*     */     }
/* 125 */     return cur.getAllSTY();
/*     */   }
/*     */ 
/*     */   public String[] getCUI(String term) {
/* 129 */     return null;
/*     */   }
/*     */ 
/*     */   public String[] getCUI(Word starting, Word ending) {
/* 133 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isTerm(String term) {
/* 137 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isTerm(Word starting, Word ending) {
/* 141 */     return false;
/*     */   }
/*     */ 
/*     */   public ArrayList findAllTerms(Word start) {
/* 145 */     return findAllTerms(start, null);
/*     */   }
/*     */ 
/*     */   public ArrayList findAllTerms(Word start, Word end)
/*     */   {
/* 155 */     ArrayList termList = null;
/* 156 */     Term curTerm = null;
/* 157 */     ArrayList canTermList = searchAllCandidates(start, end, this.minScore);
/* 158 */     if ((canTermList == null) || (canTermList.size() <= 0)) {
/* 159 */       return null;
/*     */     }
/* 161 */     termList = new ArrayList();
/*     */ 
/* 163 */     for (int i = canTermList.size() - 1; i >= 0; i--) {
/* 164 */       CandidateTerm canTerm = (CandidateTerm)canTermList.get(i);
/* 165 */       if ((getSenseDisambiguationOption()) && (canTerm.getCandidateCUINum() > 1)) {
/* 166 */         canTerm = disambiguateCandidateTerm(canTerm);
/*     */       }
/* 168 */       curTerm = generateTerm(canTerm, true);
/* 169 */       termList.add(curTerm);
/*     */     }
/* 171 */     curTerm = (Term)termList.get(0);
/* 172 */     curTerm.setSubConcept(false);
/* 173 */     curTerm.getStartingWord().setAssociatedConcept(curTerm);
/* 174 */     end = curTerm.getEndingWord();
/* 175 */     Word curWord = start.next;
/*     */ 
/* 177 */     while ((curWord != null) && (curWord.getPosInSentence() <= end.getPosInSentence()))
/* 178 */       if (!isStartingWord(curWord)) {
/* 179 */         curWord = curWord.next;
/*     */       }
/*     */       else {
/* 182 */         canTermList = searchAllCandidates(curWord, end, this.subtermMinScore);
/* 183 */         if (canTermList != null) {
/* 184 */           for (int i = canTermList.size() - 1; i >= 0; i--) {
/* 185 */             CandidateTerm canTerm = (CandidateTerm)canTermList.get(i);
/* 186 */             if ((getSenseDisambiguationOption()) && (canTerm.getCandidateCUINum() > 1)) {
/* 187 */               canTerm = disambiguateCandidateTerm(canTerm);
/*     */             }
/* 189 */             curTerm = generateTerm(canTerm, true);
/* 190 */             termList.add(curTerm);
/*     */           }
/*     */         }
/* 193 */         curWord = curWord.next;
/*     */       }
/* 195 */     return termList;
/*     */   }
/*     */ 
/*     */   public Term findTerm(Word start) {
/* 199 */     return findTerm(start, null);
/*     */   }
/*     */ 
/*     */   public Term findTerm(Word start, Word end)
/*     */   {
/* 206 */     ArrayList canTermList = searchAllCandidates(start, end, this.minScore);
/* 207 */     if ((canTermList == null) || (canTermList.size() <= 0)) return null;
/*     */ 
/* 209 */     CandidateTerm canTerm = (CandidateTerm)canTermList.get(canTermList.size() - 1);
/*     */ 
/* 212 */     if ((getSenseDisambiguationOption()) && (canTerm.getCandidateCUINum() > 1))
/*     */     {
/* 214 */       canTerm = disambiguateCandidateTerm(canTerm);
/*     */     }
/*     */ 
/* 217 */     return generateTerm(canTerm, false);
/*     */   }
/*     */ 
/*     */   private Term generateTerm(CandidateTerm canTerm, boolean isSubTerm)
/*     */   {
/* 226 */     int i = 0;
/* 227 */     int candidateNum = canTerm.getCandidateCUINum();
/* 228 */     while ((i < candidateNum) && (canTerm.getCandidateCUI(i).getScore() >= 1.0D))
/* 229 */       i++;
/* 230 */     if (i > 0) {
/* 231 */       candidateNum = i;
/*     */     }
/*     */ 
/* 234 */     String[] arrCUI = new String[candidateNum];
/* 235 */     for (i = 0; i < candidateNum; i++) {
/* 236 */       arrCUI[i] = ((UmlsCUI)this.cuiListByIndex.get(canTerm.getCandidateCUI(i).getIndex())).toString();
/*     */     }
/* 238 */     Term curTerm = new Term(canTerm.getStartingWord(), canTerm.getEndingWord());
/* 239 */     curTerm.setSubConcept(isSubTerm);
/* 240 */     if (!curTerm.isSubConcept())
/* 241 */       canTerm.getStartingWord().setAssociatedConcept(curTerm);
/* 242 */     if ((candidateNum <= 1) || (canTerm.getCandidateCUI(1).getScore() < canTerm.getCandidateCUI(0).getScore()))
/* 243 */       curTerm.setCUI(arrCUI[0]);
/* 244 */     curTerm.setCandidateCUI(arrCUI);
/* 245 */     if (curTerm.getCUI() == null) {
/* 246 */       curTerm.setCandidateTUI(getSemanticType(curTerm.getCandidateCUI()));
/*     */     }
/*     */     else {
/* 249 */       curTerm.setCandidateTUI(getSemanticType(curTerm.getCUI()));
/*     */     }
/* 251 */     if (curTerm.getCandidateTUINum() == 1) {
/* 252 */       curTerm.setTUI(curTerm.getCandidateTUI(0));
/*     */     }
/* 254 */     return curTerm;
/*     */   }
/*     */ 
/*     */   private CandidateTerm disambiguateCandidateTerm(CandidateTerm canTerm)
/*     */   {
/* 266 */     int candidateNum = canTerm.getCandidateCUINum();
/* 267 */     int[] arrCandidateCUI = new int[candidateNum];
/* 268 */     double[] arrCandidateScore = new double[candidateNum];
/* 269 */     for (int i = 0; i < candidateNum; i++)
/*     */     {
/* 271 */       arrCandidateCUI[i] = canTerm.getCandidateCUI(i).getIndex();
/* 272 */       arrCandidateScore[i] = canTerm.getCandidateCUI(i).getScore();
/*     */     }
/* 274 */     ArrayList contextList = generateContextWindow(canTerm.getStartingWord().getParent(), canTerm.getStartingWord(), canTerm.getEndingWord());
/*     */ 
/* 276 */     for (int i = 0; i < contextList.size(); i++)
/*     */     {
/* 278 */       Word curWord = (Word)contextList.get(i);
/* 279 */       int index = getIndexInTokenList(curWord);
/* 280 */       if (index >= 0) {
/* 281 */         int narrowedNum = 0;
/* 282 */         for (int j = 0; j < candidateNum; j++)
/*     */         {
/*     */           double score;
/* 283 */           if ((score = this.wtMatrix.getDouble(index, arrCandidateCUI[j])) > 0.0D) {
/* 284 */             arrCandidateCUI[narrowedNum] = arrCandidateCUI[j];
/* 285 */             arrCandidateScore[j] += score;
/* 286 */             narrowedNum++;
/*     */           }
/*     */         }
/* 289 */         if (narrowedNum > 0) candidateNum = narrowedNum; 
/*     */       }
/*     */     }
/* 291 */     if (candidateNum < canTerm.getCandidateCUINum())
/* 292 */       canTerm = buildCandidateTerm(canTerm.getStartingWord(), canTerm.getEndingWord(), arrCandidateCUI, arrCandidateScore, candidateNum, this.minScore);
/* 293 */     contextList.clear();
/* 294 */     return canTerm;
/*     */   }
/*     */ 
/*     */   private ArrayList searchAllCandidates(Word start, Word end, double minScore)
/*     */   {
/* 309 */     int index;
/* 309 */     if ((index = getIndexInTokenList(start)) < 0) return null;
/* 310 */     Sentence sent = start.getParent();
/*     */ 
/* 313 */     Word curWord = start.next;
/* 314 */     if (end == null) {
/* 315 */       int j = 0;
/* 316 */       while ((j < 4) && (curWord != null) && (end == null)) {
/* 317 */         if (isBoundaryWord(curWord))
/* 318 */           end = curWord.prev;
/* 319 */         if (!curWord.isPunctuation())
/* 320 */           j++;
/* 321 */         curWord = curWord.next;
/*     */       }
/* 323 */       if (curWord == null)
/* 324 */         curWord = sent.getLastWord();
/* 325 */       if (end == null) {
/* 326 */         end = curWord;
/*     */       }
/*     */     }
/*     */ 
/* 330 */     int candidateNum = this.wtMatrix.getNonZeroNumInRow(index);
/*     */ 
/* 332 */     if (candidateNum <= 0) return null;
/*     */ 
/* 334 */     int[] arrCandidateCUI = this.wtMatrix.getNonZeroColumnsInRow(index);
/*     */ 
/* 336 */     double[] arrCandidateScore = this.wtMatrix.getNonZeroDoubleScoresInRow(index);
/*     */ 
/* 338 */     ArrayList canTermList = new ArrayList(3);
/*     */     CandidateTerm canTerm;
/* 339 */     if ((canTerm = buildCandidateTerm(start, start, arrCandidateCUI, arrCandidateScore, candidateNum, minScore)) != null) {
/* 340 */       canTermList.add(canTerm);
/*     */     }
/*     */ 
/* 343 */     curWord = start.next;
/* 344 */     int skippedWords = 0;
/* 345 */     while ((curWord != null) && (skippedWords <= this.maxSkippedWords) && (curWord.getPosInSentence() <= end.getPosInSentence()))
/* 346 */       if (!isUsefulForTerm(curWord)) {
/* 347 */         curWord = curWord.next;
/*     */       }
/*     */       else
/*     */       {
/* 351 */         index = getIndexInTokenList(curWord);
/* 352 */         if (index < 0) {
/* 353 */           curWord = curWord.next;
/* 354 */           skippedWords++;
/*     */         }
/*     */         else {
/* 357 */           int narrowedNum = 0;
/* 358 */           for (int j = 0; j < candidateNum; j++)
/*     */           {
/*     */             double score;
/* 359 */             if ((score = this.wtMatrix.getDouble(index, arrCandidateCUI[j])) > 0.0D) {
/* 360 */               arrCandidateCUI[narrowedNum] = arrCandidateCUI[j];
/* 361 */               arrCandidateScore[j] += score;
/* 362 */               narrowedNum++;
/*     */             }
/*     */           }
/* 365 */           if (narrowedNum > 0)
/*     */           {
/* 367 */             candidateNum = narrowedNum;
/* 368 */             if ((canTerm = buildCandidateTerm(start, curWord, arrCandidateCUI, arrCandidateScore, candidateNum, minScore)) != null)
/* 369 */               canTermList.add(canTerm);
/* 370 */             skippedWords = 0;
/*     */           }
/*     */           else {
/* 373 */             skippedWords++;
/*     */           }
/* 375 */           curWord = curWord.next;
/*     */         }
/*     */       }
/* 377 */     return canTermList;
/*     */   }
/*     */ 
/*     */   private ArrayList generateContextWindow(Sentence sent, Word start, Word end)
/*     */   {
/* 387 */     ArrayList contexts = new ArrayList(6);
/* 388 */     Word cur = start.prev;
/* 389 */     int i = 0;
/* 390 */     while ((i < 3) && (cur != null)) {
/* 391 */       if ((cur.getPOSIndex() == 1) || (cur.getPOSIndex() == 3)) {
/* 392 */         contexts.add(cur);
/* 393 */         i++;
/*     */       }
/* 395 */       cur = cur.prev;
/*     */     }
/*     */ 
/* 398 */     cur = start.next;
/* 399 */     i = 0;
/* 400 */     while ((i < 3) && (cur != null)) {
/* 401 */       if ((cur.getPOSIndex() == 1) || (cur.getPOSIndex() == 3)) {
/* 402 */         contexts.add(cur);
/* 403 */         i++;
/*     */       }
/* 405 */       cur = cur.next;
/*     */     }
/*     */ 
/* 408 */     return contexts;
/*     */   }
/*     */ 
/*     */   private CandidateTerm buildCandidateTerm(Word starting, Word ending, int[] arrCandidateCUI, double[] arrCandidateScore, int candidateNum, double minScore)
/*     */   {
/* 415 */     if ((ending.getPOSIndex() == 3) && ((!getAdjectiveTermOption()) || (!ending.equals(starting)))) return null;
/* 416 */     if ((ending.getPOSIndex() == 9) && (ending.equals(starting))) return null;
/* 417 */     if (1.0D / candidateNum < this.minSelectivity) return null;
/*     */ 
/* 419 */     CandidateTerm cTerm = new CandidateTerm(starting, ending);
/* 420 */     for (int i = 0; i < candidateNum; i++)
/* 421 */       if (arrCandidateScore[i] >= minScore)
/* 422 */         cTerm.addCandidateCUI(new CandidateCUI(arrCandidateCUI[i], arrCandidateScore[i]));
/* 423 */     if (cTerm.getCandidateCUINum() > 0) {
/* 424 */       if ((ending.getPOSIndex() == 3) && (cTerm.getCandidateCUI(0).getScore() < 1.0D)) {
/* 425 */         return null;
/*     */       }
/* 427 */       return cTerm;
/*     */     }
/*     */ 
/* 430 */     return null;
/*     */   }
/*     */ 
/*     */   private int getIndexInTokenList(Word word)
/*     */   {
/* 435 */     if (word.getIndex() == -2147483648)
/*     */     {
/* 437 */       Token token = this.tokenList.lookup(getLemma(word));
/* 438 */       if (token == null)
/* 439 */         word.setIndex(-1);
/*     */       else
/* 441 */         word.setIndex(token.getIndex());
/*     */     }
/* 443 */     return word.getIndex();
/*     */   }
/*     */   private class CandidateCUI implements Comparable {
/*     */     private int index;
/*     */     private double score;
/*     */ 
/*     */     public CandidateCUI(int index, double score) {
/* 451 */       this.index = index;
/* 452 */       this.score = score;
/*     */     }
/*     */ 
/*     */     public double getScore() {
/* 456 */       return this.score;
/*     */     }
/*     */ 
/*     */     public int getIndex() {
/* 460 */       return this.index;
/*     */     }
/*     */ 
/*     */     public int compareTo(Object obj)
/*     */     {
/* 467 */       double objScore = ((CandidateCUI)obj).getScore();
/* 468 */       if (this.score > objScore)
/* 469 */         return -1;
/* 470 */       if (this.score < objScore) {
/* 471 */         return 1;
/*     */       }
/*     */ 
/* 474 */       int objIndex = ((CandidateCUI)obj).getIndex();
/* 475 */       if (this.index > objIndex)
/* 476 */         return 1;
/* 477 */       if (this.index < objIndex) {
/* 478 */         return -1;
/*     */       }
/* 480 */       return 0;
/*     */     }
/*     */   }
/*     */   private class CandidateTerm {
/*     */     Word starting;
/*     */     Word ending;
/*     */     SortedArray candidates;
/*     */ 
/* 490 */     public CandidateTerm(Word starting, Word ending) { this.starting = starting;
/* 491 */       this.ending = ending;
/* 492 */       this.candidates = new SortedArray(); }
/*     */ 
/*     */     public void addCandidateCUI(UmlsAmbiguityOntology.CandidateCUI cui)
/*     */     {
/* 496 */       this.candidates.add(cui);
/*     */     }
/*     */ 
/*     */     public Word getStartingWord() {
/* 500 */       return this.starting;
/*     */     }
/*     */ 
/*     */     public Word getEndingWord() {
/* 504 */       return this.ending;
/*     */     }
/*     */ 
/*     */     public UmlsAmbiguityOntology.CandidateCUI getCandidateCUI(int index) {
/* 508 */       return (UmlsAmbiguityOntology.CandidateCUI)this.candidates.get(index);
/*     */     }
/*     */ 
/*     */     public int getCandidateCUINum() {
/* 512 */       return this.candidates.size();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.ontology.umls.UmlsAmbiguityOntology
 * JD-Core Version:    0.6.2
 */