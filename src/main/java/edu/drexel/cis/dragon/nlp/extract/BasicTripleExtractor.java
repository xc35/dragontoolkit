/*     */ package edu.drexel.cis.dragon.nlp.extract;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Concept;
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.Term;
/*     */ import edu.drexel.cis.dragon.nlp.Triple;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.nlp.ontology.Ontology;
/*     */ import edu.drexel.cis.dragon.nlp.ontology.SemanticNet;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class BasicTripleExtractor extends AbstractTripleExtractor
/*     */ {
/*     */   private SemanticNet snNet;
/*     */ 
/*     */   public BasicTripleExtractor(TermExtractor te)
/*     */   {
/*  23 */     super(te);
/*  24 */     this.snNet = te.getOntology().getSemanticNet();
/*     */   }
/*     */ 
/*     */   public void extractTripleFromFile(String filename)
/*     */   {
/*     */     try
/*     */     {
/*  33 */       String workDir = System.getProperty("user.dir");
/*  34 */       extractFromDoc(FileUtil.readTextFile(workDir + "/inout/" + filename));
/*  35 */       PrintWriter out = FileUtil.getPrintWriter(workDir + "/inout/" + filename + ".triple");
/*  36 */       print(out, this.conceptList, this.tripleList);
/*  37 */       out.close();
/*  38 */       PrintWriter mergedOut = FileUtil.getPrintWriter(workDir + "/inout/" + filename + ".mergedtriple");
/*  39 */       SortedArray mergedTermList = this.conceptExtractor.mergeConceptByEntryID(this.conceptList);
/*  40 */       print(mergedOut, mergedTermList, mergeTriples(mergedTermList, this.tripleList));
/*  41 */       mergedOut.close();
/*     */     }
/*     */     catch (Exception e) {
/*  44 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void print(PrintWriter out, ArrayList termList, ArrayList tripleList)
/*     */   {
/*     */     try
/*     */     {
/*  55 */       for (int i = 0; i < termList.size(); i++) {
/*  56 */         Term term = (Term)termList.get(i);
/*  57 */         out.write(term.toString());
/*  58 */         for (int j = 0; j < term.getAttributeNum(); j++)
/*     */         {
/*  60 */           out.write(47);
/*  61 */           out.write(term.getAttribute(j).toString());
/*     */         }
/*  63 */         out.write(40);
/*  64 */         out.write(String.valueOf(term.getFrequency()));
/*  65 */         out.write(41);
/*  66 */         String[] arrStr = term.getCandidateTUI();
/*  67 */         if (arrStr != null) {
/*  68 */           out.write(": ");
/*  69 */           for (int j = 0; j < arrStr.length; j++) {
/*  70 */             out.write(arrStr[j]);
/*  71 */             if (j == arrStr.length - 1)
/*  72 */               out.write(" (");
/*     */             else
/*  74 */               out.write(59);
/*     */           }
/*  76 */           arrStr = term.getCandidateCUI();
/*  77 */           for (int j = 0; j < arrStr.length; j++) {
/*  78 */             out.write(arrStr[j]);
/*  79 */             if ((term.getCUI() != null) && (term.getCUI().equalsIgnoreCase(arrStr[j])))
/*  80 */               out.write("*");
/*  81 */             if (j == arrStr.length - 1)
/*  82 */               out.write(41);
/*     */             else
/*  84 */               out.write(44);
/*     */           }
/*     */         }
/*  87 */         if (term.isPredicted())
/*     */         {
/*  89 */           out.write("(Predicted)");
/*     */         }
/*  91 */         out.write("\r\n");
/*     */       }
/*     */ 
/*  94 */       for (int i = 0; i < tripleList.size(); i++) {
/*  95 */         Triple triple = (Triple)tripleList.get(i);
/*  96 */         String[] arrStr = triple.getCandidateTUI();
/*  97 */         out.write(triple.getFirstConcept().toString());
/*  98 */         out.write("<->");
/*  99 */         out.write(triple.getSecondConcept().toString());
/* 100 */         out.write(40);
/* 101 */         out.write(String.valueOf(triple.getFrequency()));
/* 102 */         out.write(41);
/* 103 */         if (arrStr != null) {
/* 104 */           out.write(": ");
/* 105 */           for (int j = 0; j < arrStr.length; j++) {
/* 106 */             out.write(arrStr[j]);
/*     */ 
/* 108 */             if (j < arrStr.length - 1)
/* 109 */               out.write(59);
/*     */           }
/*     */         }
/* 112 */         out.write("\r\n");
/*     */       }
/* 114 */       out.flush();
/*     */     }
/*     */     catch (Exception e) {
/* 117 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public ArrayList extractFromSentence(Sentence sent)
/*     */   {
/* 134 */     if (this.clauseIdentify_enabled) this.clauseFinder.clauseIdentify(sent);
/*     */ 
/* 137 */     Boolean trueObj = new Boolean(true);
/* 138 */     Boolean falseObj = new Boolean(false);
/* 139 */     Word next = sent.getFirstWord();
/* 140 */     while (next != null) {
/* 141 */       Term term = (Term)next.getAssociatedConcept();
/* 142 */       if (term != null) {
/* 143 */         if ((this.conceptFilter_enabled) && (this.cf != null)) {
/* 144 */           if (this.cf.keep(term))
/* 145 */             term.setMemo(trueObj);
/*     */           else
/* 147 */             term.setMemo(falseObj);
/*     */         }
/*     */         else {
/* 150 */           term.setMemo(trueObj);
/*     */         }
/* 152 */         if ((term.getWordNum() == 1) && (term.getStartingWord().getPOSIndex() == 3))
/* 153 */           term.setMemo(falseObj);
/*     */       }
/* 155 */       next = next.next;
/*     */     }
/*     */ 
/* 158 */     ArrayList list = new ArrayList();
/* 159 */     Word first = sent.getFirstWord();
/* 160 */     while (first != null)
/*     */     {
/* 162 */       if ((first.getAssociatedConcept() == null) || (first.getAssociatedConcept().getMemo().equals(falseObj))) {
/* 163 */         first = first.next;
/*     */       }
/*     */       else {
/* 166 */         int firstGroup = first.getParallelGroup();
/* 167 */         Word second = first.next;
/* 168 */         while (second != null)
/* 169 */           if ((second.getAssociatedConcept() == null) || (second.getAssociatedConcept().getMemo().equals(falseObj))) {
/* 170 */             second = second.next;
/*     */           }
/*     */           else
/*     */           {
/* 174 */             if (second.getAssociatedConcept().equalTo(first.getAssociatedConcept())) {
/*     */               break;
/*     */             }
/* 177 */             int secondGroup = second.getParallelGroup();
/* 178 */             if (this.coordinatingCheck_enabled) {
/* 179 */               if ((secondGroup != -1) && (firstGroup == secondGroup)) {
/* 180 */                 second = second.next;
/* 181 */                 continue;
/*     */               }
/* 183 */               if (checkCoordinateTerms((Term)first.getAssociatedConcept(), (Term)second.getAssociatedConcept()))
/*     */               {
/* 185 */                 second = second.next;
/* 186 */                 continue;
/*     */               }
/*     */             }
/* 189 */             if ((this.clauseIdentify_enabled) && (first.getClauseID() != second.getClauseID())) {
/* 190 */               second = second.next;
/*     */             }
/*     */             else
/*     */             {
/* 194 */               Triple triple = lookup((Term)first.getAssociatedConcept(), (Term)second.getAssociatedConcept());
/* 195 */               if (triple != null)
/* 196 */                 list.add(triple);
/* 197 */               second = second.next;
/*     */             }
/*     */           }
/* 199 */         first = first.next;
/*     */       }
/*     */     }
/* 201 */     return list;
/*     */   }
/*     */ 
/*     */   private Triple lookup(Term first, Term second)
/*     */   {
/*     */     try
/*     */     {
/* 210 */       if (!this.semanticCheck_enabled)
/*     */       {
/* 212 */         return new Triple(first, second);
/*     */       }
/*     */ 
/* 215 */       if ((first.getCandidateTUI() == null) || (second.getCandidateTUI() == null))
/*     */       {
/* 217 */         return new Triple(first, second);
/*     */       }
/*     */ 
/* 220 */       if (this.relationCheck_enabled)
/*     */       {
/*     */         String[] rel;
 
/* 221 */         if ((first.getTUI() != null) && (second.getTUI() != null))
/* 222 */           rel = this.snNet.getRelations(first.getTUI(), second.getTUI());
/*     */         else
/* 224 */           rel = this.snNet.getRelations(first.getCandidateTUI(), second.getCandidateTUI());
/* 225 */         if (rel != null) {
/* 226 */           Triple triple = new Triple(first, second);
/* 227 */           triple.setCandidateTUI(rel);
/* 228 */           return triple;
/*     */         }
/*     */ 
/* 231 */         return null;
/*     */       }
/*     */       boolean found;
 
/* 235 */       if ((first.getTUI() != null) && (second.getTUI() != null))
/* 236 */         found = this.snNet.isSemanticRelated(first.getTUI(), second.getTUI());
/*     */       else
/* 238 */         found = this.snNet.isSemanticRelated(first.getCandidateTUI(), second.getCandidateTUI());
/* 239 */       if (found) {
/* 240 */         return new Triple(first, second);
/*     */       }
/*     */ 
/* 244 */       return null;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 248 */       e.printStackTrace();
/* 249 */     }return null;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.extract.BasicTripleExtractor
 * JD-Core Version:    0.6.2
 */