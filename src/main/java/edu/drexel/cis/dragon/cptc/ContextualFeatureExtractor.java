/*     */ package edu.drexel.cis.dragon.cptc;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.Term;
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.nlp.extract.AbstractTokenExtractor;
/*     */ import edu.drexel.cis.dragon.nlp.extract.BasicConceptFilter;
/*     */ import edu.drexel.cis.dragon.nlp.extract.ConceptFilter;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class ContextualFeatureExtractor extends AbstractTokenExtractor
/*     */ {
/*     */   private Term searchTerm;
/*     */   private boolean subjectFeature;
/*     */   private boolean localFeature;
/*     */ 
/*     */   public ContextualFeatureExtractor(Lemmatiser lemmatiser)
/*     */   {
/*  16 */     this(lemmatiser, null);
/*     */   }
/*     */ 
/*     */   public ContextualFeatureExtractor(Lemmatiser lemmatiser, String stoplistFile) {
/*  20 */     super(lemmatiser);
/*  21 */     if (stoplistFile != null)
/*  22 */       setConceptFilter(new BasicConceptFilter(stoplistFile));
/*  23 */     this.subjectFeature = true;
/*  24 */     this.localFeature = true;
/*     */   }
/*     */ 
/*     */   public void setFeatureOption(boolean subjectFeature, boolean localFeature) {
/*  28 */     this.subjectFeature = subjectFeature;
/*  29 */     this.localFeature = localFeature;
/*     */   }
/*     */ 
/*     */   public void setSearchTerm(String query)
/*     */   {
/*  37 */     String[] arrStr = query.split(" ");
/*  38 */     Word[] arrWord = new Word[arrStr.length];
/*  39 */     for (int i = 0; i < arrStr.length; i++) {
/*  40 */       arrWord[i] = new Word(arrStr[i]);
/*  41 */       if (i > 0) {
/*  42 */         arrWord[(i - 1)].next = arrWord[i];
/*  43 */         arrWord[i].prev = arrWord[(i - 1)];
/*     */       }
/*     */     }
/*  46 */     this.searchTerm = new Term(arrWord[0], arrWord[(arrWord.length - 1)]);
/*     */   }
/*     */ 
/*     */   public ArrayList extractFromSentence(Sentence sent)
/*     */   {
/*  54 */     ArrayList tokenList = new ArrayList();
/*     */ 
/*  57 */     if (this.subjectFeature) {
/*  58 */       Word cur = sent.getFirstWord();
/*  59 */       while (cur != null) {
/*  60 */         if (!cur.isPunctuation())
/*  61 */           addSubjectFeature(new String(cur.getContent()), tokenList);
/*  62 */         cur = cur.next;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  67 */     if (this.localFeature) {
/*  68 */       Word start = this.searchTerm.searchIn(sent);
/*  69 */       while (start != null) {
/*  70 */         Word cur = start.prev;
/*  71 */         if ((cur != null) && (!cur.isPunctuation())) {
/*  72 */           if (cur.isNumber())
/*  73 */             addLocalFeature("L_NUM", tokenList);
/*     */           else
/*  75 */             addLocalFeature("L_" + cur.getContent(), tokenList);
/*  76 */           cur = cur.prev;
/*  77 */           if ((cur != null) && (!cur.isPunctuation())) {
/*  78 */             addLocalFeature("L_" + cur.getContent() + "_" + cur.next.getContent(), tokenList);
/*     */           }
/*     */         }
/*     */ 
/*  82 */         int num = this.searchTerm.getWordNum();
/*  83 */         Word end = start;
/*  84 */         while (num > 1) {
/*  85 */           end = end.next;
/*  86 */           num--;
/*     */         }
/*     */ 
/*  89 */         cur = end.next;
/*  90 */         if ((cur != null) && (!cur.isPunctuation())) {
/*  91 */           if (cur.isNumber())
/*  92 */             addLocalFeature("R_NUM", tokenList);
/*     */           else
/*  94 */             addLocalFeature("R_" + cur.getContent(), tokenList);
/*  95 */           cur = cur.next;
/*  96 */           if ((cur != null) && (!cur.isPunctuation())) {
/*  97 */             addLocalFeature("R_" + cur.prev.getContent() + "_" + cur.getContent(), tokenList);
/*     */           }
/*     */         }
/*     */ 
/* 101 */         if (end.next != null)
/* 102 */           start = this.searchTerm.searchIn(sent, end.next);
/*     */         else {
/* 104 */           start = null;
/*     */         }
/*     */       }
/*     */     }
/* 108 */     return tokenList;
/*     */   }
/*     */ 
/*     */   private Token addLocalFeature(String value, ArrayList tokenList)
/*     */   {
/* 114 */     Token token = new Token(value);
/* 115 */     tokenList.add(token);
/* 116 */     return token;
/*     */   }
/*     */ 
/*     */   private Token addSubjectFeature(String value, ArrayList tokenList)
/*     */   {
/* 122 */     if (this.lemmatiser != null)
/* 123 */       value = this.lemmatiser.lemmatize(value);
/* 124 */     if ((this.conceptFilter_enabled) && (!this.cf.keep(value))) return null;
/*     */ 
/* 126 */     Token token = new Token(value);
/* 127 */     tokenList.add(token);
/* 128 */     return token;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.cptc.ContextualFeatureExtractor
 * JD-Core Version:    0.6.2
 */