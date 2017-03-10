/*     */ package edu.drexel.cis.dragon.nlp.extract;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Term;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.BufferedReader;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class CompoundTermFinder
/*     */ {
/*     */   private SortedArray suffixList;
/*     */   private boolean subterm_enabled;
/*     */ 
/*     */   public CompoundTermFinder()
/*     */   {
/*  23 */     this.suffixList = null;
/*  24 */     this.subterm_enabled = false;
/*     */   }
/*     */ 
/*     */   public CompoundTermFinder(String suffixFile) {
/*  28 */     this.suffixList = loadlist(suffixFile);
/*  29 */     this.subterm_enabled = false;
/*     */   }
/*     */ 
/*     */   public void setSubTermOption(boolean option) {
/*  33 */     this.subterm_enabled = option;
/*     */   }
/*     */ 
/*     */   public boolean getSubTermOption() {
/*  37 */     return this.subterm_enabled;
/*     */   }
/*     */ 
/*     */   protected ArrayList predict(ArrayList termList)
/*     */   {
/*  46 */     if (termList.size() <= 1) return termList;
/*     */ 
/*  48 */     ArrayList newList = new ArrayList(termList.size());
/*  49 */     int start = 0;
/*  50 */     while (start < termList.size()) {
/*  51 */       int end = searchEndTerm(termList, start);
/*     */ 
/*  53 */       if ((this.suffixList != null) && (end > start)) {
/*  54 */         Term term = (Term)termList.get(end);
/*  55 */         while ((end > start) && (term.getWordNum() == 1) && (!this.suffixList.contains(term.toLemmaString()))) {
/*  56 */           end--;
/*  57 */           term = (Term)termList.get(end);
/*     */         }
/*     */       }
/*     */ 
/*  61 */       if (end == start) {
/*  62 */         newList.add(termList.get(start));
/*     */       }
/*     */       else
/*     */       {
/*  66 */         Word startingWord = ((Term)termList.get(start)).getStartingWord();
/*  67 */         Word endingWord = ((Term)termList.get(end)).getEndingWord();
/*  68 */         Term term = new Term(startingWord, endingWord);
/*  69 */         term.setPredictedTerm(true);
/*  70 */         term.getStartingWord().setAssociatedConcept(term);
/*  71 */         newList.add(term);
/*     */ 
/*  73 */         for (int i = start; i <= end; i++) {
/*  74 */           term = (Term)termList.get(i);
/*  75 */           if (i > start) term.getStartingWord().setAssociatedConcept(null);
/*  76 */           term.setSubConcept(true);
/*  77 */           if (this.subterm_enabled) newList.add(term);
/*     */         }
/*     */       }
/*  80 */       start = getNextNonSubTerm(termList, end + 1);
/*     */     }
/*  82 */     return newList;
/*     */   }
/*     */ 
/*     */   private int searchEndTerm(ArrayList termList, int start)
/*     */   {
/*  90 */     Term curTerm = (Term)termList.get(start);
/*  91 */     int end = start;
/*  92 */     start = getNextNonSubTerm(termList, start + 1);
/*  93 */     Word nextWord = curTerm.getEndingWord().next;
/*  94 */     int skippedWords = 0;
/*     */ 
/*  96 */     while ((start < termList.size()) && (nextWord != null)) {
/*  97 */       Term nextTerm = (Term)termList.get(start);
/*     */ 
/* 100 */       if (nextTerm.getStartingWord().equals(nextWord)) {
/* 101 */         end = start;
/* 102 */         curTerm = nextTerm;
/* 103 */         start = getNextNonSubTerm(termList, start + 1);
/* 104 */         skippedWords = 0;
/* 105 */         nextWord = curTerm.getEndingWord().next;
/*     */       }
/*     */       else
/*     */       {
/* 109 */         if (skippedWords >= 1) return end;
/*     */ 
/* 111 */         int posIndex = nextWord.getPOSIndex();
/* 112 */         if ((posIndex == 3) || (posIndex == 1)) {
/* 113 */           skippedWords++;
/* 114 */           nextWord = nextWord.next;
/*     */         }
/* 116 */         else if ((posIndex == 0) && (".-".indexOf(nextWord.getContent()) >= 0)) {
/* 117 */           nextWord = nextWord.next;
/*     */         }
/*     */         else {
/* 120 */           return end;
/*     */         }
/*     */       }
/*     */     }
/* 123 */     return end;
/*     */   }
/*     */ 
/*     */   private int getNextNonSubTerm(ArrayList list, int start)
/*     */   {
/* 129 */     int i = start;
/* 130 */     while (i < list.size()) {
/* 131 */       if (!((Term)list.get(i)).isSubConcept()) break;
/* 132 */       i++;
/*     */     }
/*     */ 
/* 136 */     return i;
/*     */   }
/*     */ 
/*     */   private SortedArray loadlist(String filename)
/*     */   {
/*     */     try
/*     */     {
/* 146 */       if ((filename == null) || (filename.trim().length() == 0)) {
/* 147 */         return null;
/*     */       }
/* 149 */       BufferedReader br = FileUtil.getTextReader(filename);
/* 150 */       String line = br.readLine();
/* 151 */       int total = Integer.parseInt(line);
/* 152 */       SortedArray list = new SortedArray(total);
/*     */ 
/* 154 */       for (int i = 0; i < total; i++) {
/* 155 */         line = br.readLine();
/* 156 */         int pos = line.indexOf('\t');
/* 157 */         if (pos > 0)
/* 158 */           line = line.substring(0, pos);
/* 159 */         list.add(line.trim());
/*     */       }
/* 161 */       br.close();
/* 162 */       return list;
/*     */     }
/*     */     catch (Exception e) {
/* 165 */       e.printStackTrace();
/* 166 */     }return null;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.extract.CompoundTermFinder
 * JD-Core Version:    0.6.2
 */