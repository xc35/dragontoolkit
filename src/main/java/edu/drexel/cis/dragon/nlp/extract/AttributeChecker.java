/*     */ package edu.drexel.cis.dragon.nlp.extract;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Term;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class AttributeChecker
/*     */ {
/*     */   private SortedArray attributes;
/*     */ 
/*     */   public AttributeChecker(String attributeFile)
/*     */   {
/*  21 */     loadAttributes(attributeFile);
/*     */   }
/*     */ 
/*     */   public boolean loadAttributes(String filename)
/*     */   {
/*     */     try
/*     */     {
/*  30 */       String content = FileUtil.readTextFile(filename);
/*  31 */       String[] arrTerms = content.split("\r\n");
/*  32 */       int termNum = arrTerms.length;
/*  33 */       while ((termNum > 0) && (arrTerms[(termNum - 1)].trim().length() == 0))
/*  34 */         termNum--;
/*  35 */       this.attributes = new SortedArray(termNum);
/*  36 */       for (int i = 0; i < termNum; i++) {
/*  37 */         this.attributes.add(i, arrTerms[i].toLowerCase());
/*     */       }
/*  39 */       return true;
/*     */     }
/*     */     catch (Exception e) {
/*  42 */       e.printStackTrace();
/*  43 */     }return false;
/*     */   }
/*     */ 
/*     */   public boolean isAttribute(Term attr)
/*     */   {
/*  49 */     return this.attributes.contains(attr.toLemmaString());
/*     */   }
/*     */ 
/*     */   public boolean isAttribute(String attr) {
/*  53 */     return this.attributes.contains(attr.toLowerCase());
/*     */   }
/*     */ 
/*     */   public int identifyAttributes(ArrayList termList)
/*     */   {
/*  62 */     if (termList == null) return 0;
/*  63 */     if (termList.size() < 2) return 0;
/*     */      int j;
/*  65 */     int count = 0;
/*  66 */     Term curTerm = (Term)termList.get(0);
/*  67 */     for (int i = 1; i < termList.size(); i++) {
/*  68 */       Word word = curTerm.getEndingWord().next;
/*  69 */       Term nextTerm = (Term)termList.get(i);
/*     */ 
/*  71 */       if ((word.getPOSIndex() == 5) && ("of for".indexOf(word.getContent().toLowerCase()) >= 0))
/*     */       {
/*  73 */         if ((nextTerm.getStartingWord().getPosInSentence() - curTerm.getEndingWord().getPosInSentence() <= 4) && (isAttribute(curTerm)));
/*  75 */         curTerm.getStartingWord().setAssociatedConcept(null);
/*  76 */         nextTerm.addAttribute(curTerm);
/*  77 */         count++;
/*     */ 
/*  80 */         termList.remove(i - 1);
/*  81 */         i--;
/*     */ 
/*  86 */         int groupNo = nextTerm.getEndingWord().getParallelGroup();
/*  87 */         if (groupNo > curTerm.getEndingWord().getParallelGroup()) {
/*  88 */           for (  j = i + 1; j < termList.size(); j++)
/*     */           {
/*  90 */             Term term = (Term)termList.get(j);
/*  91 */             if (term.getEndingWord().getParallelGroup() != groupNo)
/*     */             {
/*     */               break;
/*     */             }
/*  95 */             term.addAttribute(curTerm);
/*     */           }
/*     */ 
/*  98 */           i = j - 1;
/*     */         }
/* 100 */         curTerm = (Term)termList.get(i);
/*     */       }
/*     */       else
/*     */       {
/* 105 */         curTerm = nextTerm;
/*     */       }
/*     */     }
/* 108 */     return count;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.extract.AttributeChecker
 * JD-Core Version:    0.6.2
 */