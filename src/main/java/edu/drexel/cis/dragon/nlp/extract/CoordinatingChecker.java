/*     */ package edu.drexel.cis.dragon.nlp.extract;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.Term;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.nlp.tool.MedPostTagger;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Tagger;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class CoordinatingChecker
/*     */ {
/*     */   private int threshold;
/*     */   private int minCommaNum;
/*     */ 
/*     */   public CoordinatingChecker()
/*     */   {
/*  21 */     this.threshold = 4;
/*  22 */     this.minCommaNum = 2;
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*  28 */     CoordinatingChecker checker = new CoordinatingChecker();
/*     */ 
/*  31 */     Sentence sent = new EngDocumentParser().parseSentence("Obesity and type 2 diabetes mellitus are associated with many metabolic disorders including insulin resistance, dyslipidemia, hypertension or atherosclerosis");
/*  32 */     Tagger tagger = new MedPostTagger(System.getProperty("user.dir"));
/*  33 */     tagger.tag(sent);
/*  34 */     checker.identifyParaElements(sent);
/*  35 */     Word curWord = sent.getFirstWord();
/*  36 */     while (curWord != null)
/*     */     {
/*  38 */       System.out.print(curWord.getContent());
/*  39 */       System.out.print(" ");
/*  40 */       System.out.print(curWord.getParallelGroup());
/*  41 */       System.out.print("\r\n");
/*  42 */       curWord = curWord.next;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int identifyParaElements(Sentence sent)
/*     */   {
/*  51 */     int commaNum = 0;
/*  52 */     int groupNo = 0;
/*  53 */     int offset = 0;
/*  54 */     int firstComma = -1;
/*  55 */     int lastComma = -1;
/*  56 */     Word cur = sent.getFirstWord();
/*  57 */     while (cur != null) {
/*  58 */       if ((cur.isPunctuation()) && (cur.getContent().equalsIgnoreCase(",")))
/*     */       {
/*  60 */         if (commaNum == 0)
/*     */         {
/*  62 */           commaNum = 1;
/*  63 */           firstComma = offset;
/*  64 */           lastComma = offset;
/*     */         }
/*  66 */         else if (offset <= lastComma + this.threshold + 1) {
/*  67 */           commaNum++;
/*  68 */           lastComma = offset;
/*     */         }
/*  70 */         else if (commaNum < this.minCommaNum) {
/*  71 */           commaNum = 1;
/*  72 */           firstComma = offset;
/*  73 */           lastComma = offset;
/*     */         }
/*  77 */         else if (processParallelGroup(sent, groupNo, firstComma, lastComma, commaNum)) {
/*  78 */           groupNo++;
/*  79 */           commaNum = 1;
/*  80 */           firstComma = offset;
/*  81 */           lastComma = offset;
/*     */         }
/*     */       }
/*     */ 
/*  85 */       offset++;
/*  86 */       cur = cur.next;
/*     */     }
/*  88 */     if ((commaNum >= this.minCommaNum) && 
/*  89 */       (processParallelGroup(sent, groupNo, firstComma, lastComma, commaNum)))
/*  90 */       groupNo++;
/*  91 */     return groupNo;
/*     */   }
/*     */ 
/*     */   private boolean processParallelGroup(Sentence sent, int groupNo, int firstComma, int lastComma, int commaNum)
/*     */   {
/*  99 */     if (firstComma <= 0) return false;
/*     */ 
/* 101 */     Word cur = sent.getWord(firstComma - 1);
/* 102 */     int step = 0;
/* 103 */     while (cur != null)
/*     */     {
/* 105 */       int pos = cur.getPOSIndex();
/* 106 */       if ((pos == 2) || (pos == 4) || (pos == 5) || (pos == 0) || (pos == 8))
/*     */       {
/*     */         break;
/*     */       }
/* 110 */       cur = cur.prev;
/* 111 */       step++;
/*     */     }
/*     */     Word start;
/* 114 */     if (step == 0)
/*     */     {
/* 116 */        start = cur.next.next;
/* 117 */       commaNum--;
/*     */     }
/*     */     else
/*     */     {
/* 119 */       if (cur == null)
/* 120 */         start = sent.getFirstWord();
/*     */       else {
/* 122 */         start = cur.next;
/*     */       }
/*     */     }
/* 125 */     cur = sent.getWord(lastComma + 1);
/* 126 */     step = 0;
/* 127 */     while (cur != null)
/*     */     {
/* 129 */       int pos = cur.getPOSIndex();
/* 130 */       if ((pos == 2) || (pos == 4) || (pos == 5) || (pos == 0) || (
/* 131 */         (pos == 8) && (!cur.getContent().equalsIgnoreCase("and")) && (!cur.getContent().equalsIgnoreCase("or"))))
/*     */       {
/*     */         break;
/*     */       }
/* 135 */       cur = cur.next;
/* 136 */       step++;
/*     */     }
/*     */     Word end;
/* 139 */     if (step == 0)
/*     */     {
/* 141 */        end = cur.prev.prev;
/* 142 */       commaNum--;
/*     */     }
/*     */     else
/*     */     {
/* 144 */       if (cur == null)
/* 145 */         end = sent.getLastWord();
/*     */       else
/* 147 */         end = cur.prev;
/*     */     }
/* 149 */     if (commaNum < this.minCommaNum) return false;
/*     */ 
/* 151 */     cur = start;
/* 152 */     while (!cur.equals(end)) {
/* 153 */       cur.setParallelGroup(groupNo);
/* 154 */       cur = cur.next;
/*     */     }
/* 156 */     end.setParallelGroup(groupNo);
/*     */ 
/* 158 */     return true;
/*     */   }
/*     */ 
/*     */   public ArrayList parallelTermPredict(ArrayList termList)
/*     */   {
/* 167 */     for (int i = 0; i < termList.size(); i++) {
/* 168 */       Term curTerm = (Term)termList.get(i);
/* 169 */       int curParaGroup = curTerm.getStartingWord().getParallelGroup();
/* 170 */       if (curParaGroup >= 0)
/*     */       {
/* 175 */         Word curWord = curTerm.getStartingWord().prev;
/* 176 */         Word endWord = curWord;
/* 177 */         Word prevWord = curTerm.getStartingWord();
/* 178 */         int insertPos = i;
/*     */ 
/* 180 */         while ((endWord != null) && (curWord != null) && (curWord.getParallelGroup() == curParaGroup) && 
/* 181 */           (curWord.getAssociatedConcept() == null)) {
/* 182 */           if ((curWord.getContent().equalsIgnoreCase(",")) || (curWord.getContent().equalsIgnoreCase("and"))) {
/* 183 */             if (!curWord.equals(endWord)) {
/* 184 */               Term newTerm = new Term(prevWord, endWord);
/* 185 */               newTerm.setPredictedTerm(true);
/* 186 */               termList.add(insertPos, newTerm);
/* 187 */               prevWord.setAssociatedConcept(newTerm);
/* 188 */               i++;
/*     */             }
/* 190 */             endWord = curWord.prev;
/*     */           }
/* 192 */           prevWord = curWord;
/* 193 */           curWord = curWord.prev;
/*     */         }
/* 195 */         if (((curWord == null) || (curWord.getParallelGroup() != curParaGroup)) && 
/* 196 */           (endWord != null) && (prevWord.getPosInSentence() <= endWord.getPosInSentence())) {
/* 197 */           Term newTerm = new Term(prevWord, endWord);
/* 198 */           newTerm.setPredictedTerm(true);
/* 199 */           termList.add(insertPos, newTerm);
/* 200 */           prevWord.setAssociatedConcept(newTerm);
/* 201 */           i++;
/*     */         }
/*     */ 
/* 206 */         curWord = curTerm.getStartingWord().next;
/* 207 */         Word startWord = curWord;
/* 208 */         prevWord = curTerm.getStartingWord();
/* 209 */         insertPos = i + 1;
/*     */ 
/* 211 */         while ((startWord != null) && (curWord != null) && (curWord.getParallelGroup() == curParaGroup) && 
/* 212 */           (curWord.getAssociatedConcept() == null)) {
/* 213 */           if ((curWord.getContent().equalsIgnoreCase(",")) || (curWord.getContent().equalsIgnoreCase("and"))) {
/* 214 */             if (!curWord.equals(startWord)) {
/* 215 */               Term newTerm = new Term(startWord, prevWord);
/* 216 */               newTerm.setPredictedTerm(true);
/* 217 */               termList.add(insertPos, newTerm);
/* 218 */               prevWord.setAssociatedConcept(newTerm);
/* 219 */               i++;
/*     */             }
/* 221 */             startWord = curWord.next;
/*     */           }
/* 223 */           prevWord = curWord;
/* 224 */           curWord = curWord.next;
/*     */         }
/* 226 */         if (((curWord == null) || (curWord.getParallelGroup() != curParaGroup)) && 
/* 227 */           (startWord != null) && (prevWord.getPosInSentence() >= startWord.getPosInSentence())) {
/* 228 */           Term newTerm = new Term(startWord, prevWord);
/* 229 */           newTerm.setPredictedTerm(true);
/* 230 */           termList.add(insertPos, newTerm);
/* 231 */           prevWord.setAssociatedConcept(newTerm);
/* 232 */           i++;
/*     */         }
/*     */       }
/*     */     }
/* 236 */     return termList;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.extract.CoordinatingChecker
 * JD-Core Version:    0.6.2
 */