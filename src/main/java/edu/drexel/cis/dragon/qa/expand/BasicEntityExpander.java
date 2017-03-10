/*     */ package edu.drexel.cis.dragon.qa.expand;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*     */ import edu.drexel.cis.dragon.qa.system.Candidate;
/*     */ import edu.drexel.cis.dragon.qa.system.CandidateBase;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class BasicEntityExpander
/*     */   implements CandidateExpander
/*     */ {
/*     */   private Word[] arrOldLeft;
/*     */   private Word[] arrOldRight;
/*     */   private int[] arrCase;
/*     */   private int top;
/*     */   private int minFrequency;
/*     */   private int maxFrequency;
/*     */   private double minRate;
/*     */ 
/*     */   public BasicEntityExpander()
/*     */   {
/*  18 */     this.top = 5;
/*  19 */     this.minFrequency = 4;
/*  20 */     this.maxFrequency = 12;
/*  21 */     this.minRate = 0.6D;
/*  22 */     this.arrOldLeft = new Word[100];
/*  23 */     this.arrOldRight = new Word[100];
/*  24 */     this.arrCase = new int[100];
/*     */   }
/*     */ 
/*     */   public ArrayList expand(QuestionQuery query, CandidateBase base, ArrayList list)
/*     */   {
/*  35 */     boolean expanded = false;
/*  36 */     int num = Math.min(this.top, list.size());
/*  37 */     Candidate[] arrCan = new Candidate[num];
/*  38 */     for (int i = 0; i < num; i++)
/*  39 */       arrCan[i] = ((Candidate)list.get(i));
/*  40 */     for (int i = 0; i < num; i++)
/*  41 */       if ((arrCan[i] != null) && (needExpand(arrCan[i])))
/*     */       {
/*  43 */         int minFreq = Math.min(this.maxFrequency, (int)Math.max(this.minFrequency, this.minRate * arrCan[i].getInitialFrequency()));
/*  44 */         Candidate newCan = expand(query, base, arrCan[i], minFreq);
/*  45 */         if (newCan != null)
/*     */         {
/*  47 */           expanded = true;
/*  48 */           arrCan[i] = newCan;
/*  49 */           String str = newCan.toString().toLowerCase();
/*  50 */           for (int j = i + 1; j < num; j++)
/*  51 */             if ((arrCan[j] != null) && (str.indexOf(arrCan[j].toString().toLowerCase()) >= 0))
/*  52 */               arrCan[j] = null; 
/*     */         }
/*     */       }
/*  54 */     if (!expanded) {
/*  55 */       return list;
/*     */     }
/*  57 */     ArrayList newList = new ArrayList(list.size());
/*  58 */     for (int i = 0; i < num; i++)
/*  59 */       if (arrCan[i] != null)
/*  60 */         newList.add(arrCan[i]);
/*  61 */     for (int i = num; i < list.size(); i++)
/*  62 */       newList.add(list.get(i));
/*  63 */     return newList;
/*     */   }
/*     */ 
/*     */   protected Candidate expand(QuestionQuery query, CandidateBase base, Candidate curCand, int minFreq)
/*     */   {
/*     */     try
/*     */     {
/*  73 */       int count = 0;
/*  74 */       int left = 0;
/*  75 */       int right = 0;
/*  76 */       int[] arrIndex = base.getCandidateSentences(curCand.getIndex());
/*  77 */       for (int i = 0; i < arrIndex.length; i++) {
/*  78 */         Word start = curCand.searchIn(base.getSentence(arrIndex[i]));
/*  79 */         if (start != null) {
/*  80 */           this.arrOldLeft[count] = start;
/*  81 */           int j = 1;
/*  82 */           while (j < curCand.getWordNum()) {
/*  83 */             start = start.next;
/*  84 */             j++;
/*     */           }
/*  86 */           this.arrOldRight[count] = start;
/*  87 */           this.arrCase[count] = getCaseInfo(this.arrOldRight[count]);
/*  88 */           count++;
/*     */         }
/*     */       }
/*  91 */       if (count < minFreq) {
/*  92 */         return null;
/*     */       }
/*  94 */       while (expand(query, minFreq, count, true, left) != null)
/*  95 */         left++;
/*  96 */       while (expand(query, minFreq, count, false, right) != null) {
/*  97 */         right++;
/*     */       }
/*  99 */       if ((left == 0) && (right == 0)) {
/* 100 */         return null;
/*     */       }
/* 102 */      int  i = 0;
/*     */       do { i++; if (i >= count) break;  } while ((this.arrOldLeft[i] == null) || (this.arrOldRight[i] == null));
/* 103 */       while ((this.arrOldLeft[i].isPunctuation()) || (this.arrOldLeft[i].getContent().equals("s")) || (this.arrOldLeft[i].getPOSIndex() == 8) || 
/* 104 */         (this.arrOldLeft[i].getPOSIndex() == 5) || (this.arrOldLeft[i].getPOSIndex() == 7)) {
/* 105 */         this.arrOldLeft[i] = this.arrOldLeft[i].next;
/* 106 */         left--;
/*     */       }
/* 108 */       while ((this.arrOldRight[i].isPunctuation()) || (this.arrOldRight[i].getContent().equals("s")) || (this.arrOldRight[i].getPOSIndex() == 8) || 
/* 109 */         (this.arrOldRight[i].getPOSIndex() == 5) || (this.arrOldRight[i].getPOSIndex() == 7)) {
/* 110 */         this.arrOldRight[i] = this.arrOldRight[i].prev;
/* 111 */         right--;
/*     */       }
/* 113 */       if ((left < 0) || (right < 0) || ((left == 0) && (right == 0)))
/* 114 */         return null;
/* 115 */       Candidate newCand = new Candidate(this.arrOldLeft[i], this.arrOldRight[i]);
/* 116 */       newCand.copyFrom(curCand);
/* 117 */       return newCand;
/*     */     }
/*     */     catch (Exception e) {
/* 120 */       e.printStackTrace();
/* 121 */     }return null;
/*     */   }
/*     */ 
/*     */   protected Word expand(QuestionQuery query, int minFreq, int count, boolean left, int offset)
/*     */   {
/* 131 */     SortedArray list = new SortedArray();
/* 132 */     for (int i = 0; i < count; i++)
/*     */     {
/*     */       Word cur;
/* 133 */       if (left) {
/* 134 */         if (this.arrOldLeft[i] == null)
/*     */           continue;
/* 136 */         cur = this.arrOldLeft[i].prev;
/*     */       }
/*     */       else {
/* 139 */         if (this.arrOldRight[i] == null)
/*     */           continue;
/* 141 */         cur = this.arrOldRight[i].next;
/*     */       }
/* 143 */       if (cur != null)
/*     */       {
/* 145 */         int curCase = getCaseInfo(cur);
/* 146 */         if (curCase >= 0) {
/* 147 */           if (this.arrCase[i] < 0)
/* 148 */             this.arrCase[i] = curCase;
/* 149 */           else if (this.arrCase[i] != curCase)
/*     */               continue;
/*     */         }
/* 152 */         Token curToken = new Token(cur.getContent());
/* 153 */         curToken.setMemo(cur);
/* 154 */         if (!list.add(curToken)) {
/* 155 */           Token oldToken = (Token)list.get(list.insertedPos());
/* 156 */           oldToken.addFrequency(curToken.getFrequency());
/*     */         }
/*     */       }
/*     */     }
/* 159 */     int maxIndex = -1;
/* 160 */     int maxFreq = 0;
/* 161 */     for (int i = 0; i < list.size(); i++) {
/* 162 */       Token curToken = (Token)list.get(i);
/* 163 */       if (curToken.getFrequency() > maxFreq) {
/* 164 */         maxFreq = curToken.getFrequency();
/* 165 */         maxIndex = i;
/*     */       }
/*     */     }
/* 168 */     if (maxFreq < minFreq) {
/* 169 */       return null;
/*     */     }
/* 171 */     Token curToken = (Token)list.get(maxIndex);
/* 172 */     Word cur = (Word)curToken.getMemo();
/* 173 */     int pos = cur.getPOSIndex();
/* 174 */     if ((cur.isPunctuation()) && (",'".indexOf(cur.getContent()) < 0))
/* 175 */       return null;
/* 176 */     if ((!curToken.getValue().equals("s")) && (!cur.isPunctuation())) {
/* 177 */       if ((pos >= 1) && (pos <= 4) && (query.search(curToken.getValue()) != null))
/* 178 */         return null;
/* 179 */       if ((offset == 0) && (pos == 1))
/* 180 */         return null;
/*     */     }
/* 182 */     for (int i = 0; i < count; i++) {
/* 183 */       if (left) {
/* 184 */         if (this.arrOldLeft[i] != null)
/* 185 */           this.arrOldLeft[i] = this.arrOldLeft[i].prev;
/* 186 */         if ((this.arrOldLeft[i] != null) && (!this.arrOldLeft[i].getContent().equalsIgnoreCase(curToken.getValue())))
/* 187 */           this.arrOldLeft[i] = null;
/*     */       }
/*     */       else {
/* 190 */         if (this.arrOldRight[i] != null)
/* 191 */           this.arrOldRight[i] = this.arrOldRight[i].next;
/* 192 */         if ((this.arrOldRight[i] != null) && (!this.arrOldRight[i].getContent().equalsIgnoreCase(curToken.getValue())))
/* 193 */           this.arrOldRight[i] = null;
/*     */       }
/*     */     }
/* 196 */     return cur;
/*     */   }
/*     */ 
/*     */   protected boolean needExpand(Candidate curCand) {
/* 200 */     if ((curCand.getInitialFrequency() < this.minFrequency) || (curCand.getWordNum() > 5)) {
/* 201 */       return false;
/*     */     }
/* 203 */     return true;
/*     */   }
/*     */ 
/*     */   protected int getCaseInfo(Word cur) {
/* 207 */     if ((cur.getContent().equals("s")) || (cur.isPunctuation()) || 
/* 208 */       (cur.getPOSIndex() == 7) || (cur.getPOSIndex() == 8) || (cur.getPOSIndex() == 5)) {
/* 209 */       return -1;
/*     */     }
/* 211 */     if (cur.prev != null) {
/* 212 */       return cur.isInitialCapital() ? 1 : 0;
/*     */     }
/* 214 */     return cur.isInitialCapital() ? -1 : 0;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.expand.BasicEntityExpander
 * JD-Core Version:    0.6.2
 */