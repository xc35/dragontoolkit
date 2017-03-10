/*     */ package edu.drexel.cis.dragon.nlp.tool.xtract;
/*     */ 
/*     */ import edu.drexel.cis.dragon.matrix.IntGiantSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.IntSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.IntSuperSparseMatrix;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.compare.FrequencyComparator;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class EngWordPairExpand
/*     */   implements WordPairExpand
/*     */ {
/*     */   protected IntSparseMatrix sentMatrix;
/*     */   protected SimpleElementList wordList;
/*     */   protected int maxSpan;
/*     */   protected String indexFolder;
/*     */   protected IntSparseMatrix[] arrPairSentLeftMatrix;
/*     */   protected IntSparseMatrix[] arrPairSentRightMatrix;
/*     */   protected double threshold;
/*     */ 
/*     */   public EngWordPairExpand(int maxSpan, String indexFolder, double threshold)
/*     */   {
/*  28 */     this.maxSpan = maxSpan;
/*  29 */     this.threshold = threshold;
/*  30 */     this.indexFolder = indexFolder;
/*     */ 
/*  32 */     this.wordList = new SimpleElementList(indexFolder + "/wordkey.list", false);
/*  33 */     this.arrPairSentRightMatrix = new IntSparseMatrix[maxSpan];
/*  34 */     this.sentMatrix = new IntSuperSparseMatrix(indexFolder + "/sentencebase.index", indexFolder + "/sentencebase.matrix");
/*  35 */     for (int i = 1; i <= maxSpan; i++) {
/*  36 */       this.arrPairSentRightMatrix[(i - 1)] = new IntGiantSparseMatrix(indexFolder + "/pairsentr" + i + ".index", 
/*  37 */         indexFolder + "/pairsentr" + i + ".matrix");
/*     */     }
/*  39 */     this.arrPairSentLeftMatrix = new IntSparseMatrix[maxSpan];
/*  40 */     for (int i = 1; i <= maxSpan; i++)
/*  41 */       this.arrPairSentLeftMatrix[(i - 1)] = new IntGiantSparseMatrix(indexFolder + "/pairsentl" + i + ".index", 
/*  42 */         indexFolder + "/pairsentl" + i + ".matrix");
/*     */   }
/*     */ 
/*     */   public ArrayList expand(WordPairStat wordPairStat, int span)
/*     */   {
/*     */     try
/*     */     {
/*  54 */       int firstWord = wordPairStat.getFirstWord();
/*  55 */       int secondWord = wordPairStat.getSecondWord();
/*  56 */       String expandStr = null;
/*     */ 
/*  59 */       ArrayList sentList = getSentenceList(wordPairStat, span);
/*  60 */       int sentNum = sentList.size();
/*     */ 
/*  63 */       boolean pass = true;
/*  64 */       if ((span > 1) || (span < -1))
/*     */       {
/*     */         Token token;
/*  65 */         if (span > 1)
/*  66 */           token = expandSecion(1, span - 1, sentNum, false, 0, sentList);
/*     */         else
/*  68 */           token = expandSecion(1, -span - 1, sentNum, true, 0, sentList);
/*  69 */         if (token == null) {
/*  70 */           pass = false;
/*     */         } else {
/*  72 */           pass = true;
/*  73 */           sentList = (ArrayList)token.getMemo();
/*  74 */           if (span > 1)
/*  75 */             expandStr = getWordContent(firstWord) + " " + token.getName().trim() + " " + getWordContent(secondWord);
/*     */           else {
/*  77 */             expandStr = getWordContent(secondWord) + " " + token.getName().trim() + " " + getWordContent(firstWord);
/*     */           }
/*     */         }
/*     */       }
/*  81 */       else if (span == 1) {
/*  82 */         expandStr = (getWordContent(firstWord) + " " + getWordContent(secondWord)).trim();
/*     */       } else {
/*  84 */         expandStr = (getWordContent(secondWord).trim() + " " + getWordContent(firstWord)).trim();
/*     */       }
/*     */ 
/*  87 */       if (!pass)
/*  88 */         return null;
/*     */       Token token;
/*  91 */       if (span > 0)
/*  92 */         token = expandSecion(1, this.maxSpan, sentNum, true, -1, sentList);
/*     */       else
/*  94 */         token = expandSecion(-span + 1, this.maxSpan - span, sentNum, true, -1, sentList);
/*  95 */       if (token != null) {
/*  96 */         sentList = (ArrayList)token.getMemo();
/*  97 */         expandStr = token.getName().trim() + " " + expandStr;
/*     */       }
/*     */ 
/* 101 */       if (span > 0)
/* 102 */         token = expandSecion(span + 1, span + this.maxSpan, sentNum, false, 1, sentList);
/*     */       else
/* 104 */         token = expandSecion(1, this.maxSpan, sentNum, false, 1, sentList);
/* 105 */       if (token != null) {
/* 106 */         sentList = (ArrayList)token.getMemo();
/* 107 */         expandStr = expandStr + " " + token.getName().trim();
/*     */       }
/*     */ 
/* 110 */       ArrayList phraseList = new ArrayList(1);
/* 111 */       phraseList.add(new Token(expandStr.trim()));
/* 112 */       return phraseList;
/*     */     }
/*     */     catch (Exception e) {
/* 115 */       e.printStackTrace();
/* 116 */     }return null;
/*     */   }
/*     */ 
/*     */   protected ArrayList getSentenceList(WordPairStat wordPairStat, int span)
/*     */   {
/* 126 */     int pairIndex = wordPairStat.getIndex();
/* 127 */     int firstWord = wordPairStat.getFirstWord();
/* 128 */     int secondWord = wordPairStat.getSecondWord();
/*     */     IntSparseMatrix pairSentMatrix;
/* 130 */     if (span < 0)
/* 131 */       pairSentMatrix = this.arrPairSentLeftMatrix[(-span - 1)];
/*     */     else
/* 133 */       pairSentMatrix = this.arrPairSentRightMatrix[(span - 1)];
/* 134 */     int sentNum = pairSentMatrix.getNonZeroNumInRow(pairIndex);
/* 135 */     ArrayList sentList = new ArrayList(sentNum);
/*     */ 
/* 137 */     for (int i = 0; i < sentNum; i++) {
/* 138 */       int sentIndex = pairSentMatrix.getNonZeroColumnInRow(pairIndex, i);
/* 139 */       int sentLength = this.sentMatrix.getNonZeroNumInRow(sentIndex);
/* 140 */       for (int j = 0; j < sentLength; j++) {
/* 141 */         int wordKey = this.sentMatrix.getNonZeroColumnInRow(sentIndex, j);
/* 142 */         if (wordKey == firstWord)
/*     */         {
/* 144 */           if ((j + span < 0) || (j + span >= sentLength) || 
/* 145 */             (this.sentMatrix.getNonZeroColumnInRow(sentIndex, j + span) != secondWord)) break;
/* 146 */           Token sentToken = new Token(String.valueOf(sentIndex));
/* 147 */           sentToken.setIndex(j);
/* 148 */           sentToken.setFrequency(this.sentMatrix.getNonZeroIntScoreInRow(sentIndex, j + span));
/* 149 */           sentList.add(sentToken);
/*     */ 
/* 151 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 154 */     return sentList;
/*     */   }
/*     */ 
/*     */   protected Token expandSecion(int start, int end, int sentNum, boolean inverse, int direction, ArrayList sentList)
/*     */   {
/* 164 */     String expandStr = "";
/* 165 */     String marginalWord = null;
/* 166 */     int marginalPOS = -1;
/*     */         int i;
/* 168 */     for ( i = start; i <= end; i++)
/*     */     {
/*     */       int j;
/* 169 */       if (inverse)
/* 170 */         j = -i;
/*     */       else
/* 172 */         j = i;
/* 173 */       Token token = checkSentPos(j, sentList);
/* 174 */       if (token == null) break;
/* 175 */       sentList = (ArrayList)token.getMemo();
/* 176 */       if (token.getFrequency() / sentNum < this.threshold)
/*     */         break;
/* 178 */       String word = getWordContent(Integer.parseInt(token.getName()));
/* 179 */       int posIndex = token.getIndex();
/* 180 */       if ((direction != 0) && (!checkValidation(word, posIndex))) break;
/* 181 */       if (inverse)
/* 182 */         expandStr = word + " " + expandStr;
/*     */       else
/* 184 */         expandStr = expandStr + " " + word;
/* 185 */       expandStr = expandStr.trim();
/* 186 */       if (((direction == 1) && (!inverse)) || ((direction == -1) && (inverse))) {
/* 187 */         marginalWord = word;
/* 188 */         marginalPOS = posIndex;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 201 */     if ((i <= end) && (direction == 0))
/* 202 */       return null;
/* 203 */     if (!expandStr.equals("")) {
/* 204 */       if ((direction == 1) && (!inverse) && (!checkEndingWordValidation(marginalWord, marginalPOS))) {
/* 205 */         int pos = expandStr.lastIndexOf(' ');
/* 206 */         if (pos >= 0)
/* 207 */           expandStr = expandStr.substring(0, pos);
/*     */         else
/* 209 */           return null;
/*     */       }
/* 211 */       else if ((direction == -1) && (inverse) && (!checkStartingWordValidation(marginalWord, marginalPOS))) {
/* 212 */         int pos = expandStr.indexOf(' ');
/* 213 */         if (pos >= 0)
/* 214 */           expandStr = expandStr.substring(pos + 1);
/*     */         else
/* 216 */           return null;
/*     */       }
/* 218 */       Token token = new Token(expandStr);
/* 219 */       token.setMemo(sentList);
/* 220 */       return token;
/*     */     }
/*     */ 
/* 223 */     return null;
/*     */   }
/*     */ 
/*     */   protected Token checkSentPos(int spanFromFirstWord, ArrayList sentList)
/*     */   {
/* 232 */     SortedArray tokenList = new SortedArray();
/*     */ 
/* 234 */     for (int i = 0; i < sentList.size(); i++) {
/* 235 */       Token sentToken = (Token)sentList.get(i);
/* 236 */       int sentIndex = Integer.parseInt(sentToken.getName());
/* 237 */       int sentLength = this.sentMatrix.getNonZeroNumInRow(sentIndex);
/* 238 */       int firstWordPos = sentToken.getIndex();
/*     */ 
/* 240 */       if ((firstWordPos + spanFromFirstWord >= 0) && (firstWordPos + spanFromFirstWord < sentLength)) {
/* 241 */         int wordKey = this.sentMatrix.getNonZeroColumnInRow(sentIndex, firstWordPos + spanFromFirstWord);
/* 242 */         Token wordToken = new Token(String.valueOf(wordKey));
/* 243 */         int tokenIndex = tokenList.binarySearch(wordToken);
/* 244 */         if (tokenIndex < 0) {
/* 245 */           ArrayList sList = new ArrayList();
/* 246 */           sList.add(sentToken);
/* 247 */           wordToken.setFrequency(1);
/* 248 */           wordToken.setIndex(this.sentMatrix.getNonZeroIntScoreInRow(sentIndex, firstWordPos + spanFromFirstWord));
/* 249 */           wordToken.setMemo(sList);
/* 250 */           tokenList.add(wordToken);
/*     */         }
/*     */         else {
/* 253 */           wordToken = (Token)tokenList.get(tokenIndex);
/* 254 */           wordToken.addFrequency(1);
/* 255 */           ArrayList sList = (ArrayList)wordToken.getMemo();
/* 256 */           sList.add(sentToken);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 261 */     if (tokenList.size() > 0) {
/* 262 */       tokenList.setComparator(new FrequencyComparator(true));
/* 263 */       Token wordToken = (Token)tokenList.get(0);
/* 264 */       tokenList.clear();
/* 265 */       return wordToken;
/*     */     }
/*     */ 
/* 268 */     return null;
/*     */   }
/*     */ 
/*     */   protected String getWordContent(int index) {
/* 272 */     return this.wordList.search(index).trim();
/*     */   }
/*     */ 
/*     */   protected boolean checkValidation(String word, int posIndex) {
/* 276 */     if ((posIndex == 3) || (posIndex == 1) || ((posIndex == 0) && (word.equals("-"))))
/* 277 */       return true;
/* 278 */     return false;
/*     */   }
/*     */ 
/*     */   protected boolean checkEndingWordValidation(String word, int posIndex) {
/* 282 */     if (posIndex == 1) {
/* 283 */       return true;
/*     */     }
/* 285 */     return false;
/*     */   }
/*     */ 
/*     */   protected boolean checkStartingWordValidation(String word, int posIndex) {
/* 289 */     if ((posIndex == 1) || (posIndex == 3)) {
/* 290 */       return true;
/*     */     }
/* 292 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.tool.xtract.EngWordPairExpand
 * JD-Core Version:    0.6.2
 */