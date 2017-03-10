/*     */ package edu.drexel.cis.dragon.ir.query;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.kngbase.KnowledgeBase;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSparseMatrix;
/*     */ import edu.drexel.cis.dragon.nlp.Document;
/*     */ import edu.drexel.cis.dragon.nlp.DocumentParser;
/*     */ import edu.drexel.cis.dragon.nlp.Phrase;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*     */ import edu.drexel.cis.dragon.nlp.compare.WeightComparator;
/*     */ import edu.drexel.cis.dragon.nlp.extract.PhraseExtractor;
/*     */ import edu.drexel.cis.dragon.nlp.extract.TokenExtractor;
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import edu.drexel.cis.dragon.util.FormatUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class PhraseQEGenerator extends AbstractQueryGenerator
/*     */ {
/*     */   private DoubleSparseMatrix translationMatrix;
/*     */   private SimpleElementList phraseKeyList;
/*     */   private SimpleElementList tokenKeyList;
/*     */   private PhraseExtractor phraseExtractor;
/*     */   private TokenExtractor tokenExtractor;
/*     */   private DocumentParser parser;
/*     */   private double transCoefficient;
/*     */   private int expandTermNum;
/*     */   private boolean useTitle;
/*     */   private boolean useAbt;
/*     */   private boolean useBody;
/*     */   private boolean useMeta;
/*     */ 
/*     */   public PhraseQEGenerator(KnowledgeBase tokenKngBase, TokenExtractor tokenExtractor, double transCoefficient, int expandTermNum)
/*     */   {
/*  33 */     this(tokenKngBase, null, tokenExtractor, transCoefficient, expandTermNum);
/*     */   }
/*     */ 
/*     */   public PhraseQEGenerator(KnowledgeBase phraseKngBase, PhraseExtractor phraseExtractor, TokenExtractor tokenExtractor, double transCoefficient, int expandTermNum) {
/*  37 */     this.translationMatrix = phraseKngBase.getKnowledgeMatrix();
/*  38 */     this.phraseKeyList = phraseKngBase.getRowKeyList();
/*  39 */     this.tokenKeyList = phraseKngBase.getColumnKeyList();
/*  40 */     this.phraseExtractor = phraseExtractor;
/*  41 */     if (phraseExtractor != null) {
/*  42 */       phraseExtractor.setSubConceptOption(false);
/*  43 */       phraseExtractor.setSingleAdjectiveOption(false);
/*  44 */       phraseExtractor.setSingleNounOption(false);
/*  45 */       phraseExtractor.setSingleVerbOption(false);
/*  46 */       this.parser = phraseExtractor.getDocumentParser();
/*     */     }
/*     */     else {
/*  49 */       this.parser = tokenExtractor.getDocumentParser();
/*  50 */     }this.tokenExtractor = tokenExtractor;
/*  51 */     this.transCoefficient = transCoefficient;
/*  52 */     this.expandTermNum = expandTermNum;
/*     */ 
/*  54 */     this.useTitle = true;
/*  55 */     this.useAbt = false;
/*  56 */     this.useBody = false;
/*  57 */     this.useMeta = false;
/*     */   }
/*     */ 
/*     */   public void initialize(boolean useTitle, boolean useAbt, boolean useBody, boolean useMeta) {
/*  61 */     this.useTitle = useTitle;
/*  62 */     this.useAbt = useAbt;
/*  63 */     this.useBody = useBody;
/*  64 */     this.useMeta = useMeta;
/*     */   }
/*     */ 
/*     */   public IRQuery generate(Article topic) {
/*  68 */     return new RelSimpleQuery(genQueryString(genQuery(topic)));
/*     */   }
/*     */ 
/*     */   private String genQueryString(ArrayList queryTerms)
/*     */   {
/*  77 */     StringBuffer buf = new StringBuffer();
/*  78 */     DecimalFormat df = FormatUtil.getNumericFormat(1, 3);
/*  79 */     for (int i = 0; i < queryTerms.size(); i++) {
/*  80 */       Token curToken = (Token)queryTerms.get(i);
/*  81 */       buf.append("T(");
/*  82 */       buf.append(df.format(curToken.getWeight()));
/*  83 */       buf.append(",TERM=");
/*  84 */       buf.append(curToken.getName());
/*  85 */       buf.append(") ");
/*     */     }
/*  87 */     return buf.toString().trim();
/*     */   }
/*     */ 
/*     */   private ArrayList genQuery(Article article)
/*     */   {
/*  99 */     SortedArray tokenList = new SortedArray();
/* 100 */     SortedArray phraseList = new SortedArray();
/* 101 */     Document doc = getDocument(article);
/*     */ 
/* 104 */     ArrayList list = this.tokenExtractor.extractFromDoc(doc);
/* 105 */     int total = 0;
/* 106 */     for (int i = 0; i < list.size(); i++) {
/* 107 */       Token curToken = (Token)list.get(i);
/* 108 */       total += curToken.getFrequency();
/* 109 */       if (!tokenList.add(curToken)) {
/* 110 */         Token oldToken = (Token)tokenList.get(tokenList.insertedPos());
/* 111 */         oldToken.addFrequency(curToken.getFrequency());
/*     */       }
/*     */     }
/* 114 */     for (int i = 0; i < tokenList.size(); i++) {
/* 115 */       Token curToken = (Token)tokenList.get(i);
/* 116 */       curToken.setWeight(curToken.getFrequency() / total);
/*     */     }
/* 118 */     int termNum = tokenList.size() + this.expandTermNum;
/*     */ 
/* 121 */     if (this.phraseExtractor != null) {
/* 122 */       list = this.phraseExtractor.extractFromDoc(doc);
/* 123 */       for (int i = 0; i < list.size(); i++) {
/* 124 */         Phrase curPhrase = (Phrase)list.get(i);
/* 125 */         if (!curPhrase.getStartingWord().equals(curPhrase.getEndingWord()))
/*     */         {
/* 127 */           Token curToken = new Token(curPhrase.getName(), -1, curPhrase.getFrequency());
/* 128 */           if (!phraseList.add(curToken)) {
/* 129 */             Token oldToken = (Token)phraseList.get(phraseList.insertedPos());
/* 130 */             oldToken.addFrequency(curToken.getFrequency());
/*     */           }
/*     */         }
/*     */       }
/* 133 */       list = translation(phraseList);
/*     */     }
/*     */     else {
/* 136 */       list = translation(tokenList);
/* 137 */     }if (list != null) {
/* 138 */       for (int i = 0; i < tokenList.size(); i++) {
/* 139 */         Token curToken = (Token)tokenList.get(i);
/* 140 */         curToken.setWeight(curToken.getWeight() * (1.0D - this.transCoefficient));
/*     */       }
/*     */ 
/* 143 */       for (int i = 0; i < list.size(); i++) {
/* 144 */         Token newToken = (Token)list.get(i);
/* 145 */         newToken.setWeight(newToken.getWeight() * this.transCoefficient);
/* 146 */         if (!tokenList.add(newToken)) {
/* 147 */           Token oldToken = (Token)tokenList.get(tokenList.insertedPos());
/* 148 */           oldToken.setWeight(oldToken.getWeight() + newToken.getWeight());
/*     */         }
/*     */       }
/*     */ 
/* 152 */       if (tokenList.size() > termNum) {
/* 153 */         tokenList.setComparator(new WeightComparator(true));
/* 154 */         while (tokenList.size() > termNum) {
/* 155 */           tokenList.remove(tokenList.size() - 1);
/*     */         }
/* 157 */         double sum = 0.0D;
/* 158 */         for (int i = 0; i < tokenList.size(); i++) {
/* 159 */           Token curToken = (Token)tokenList.get(i);
/* 160 */           sum += curToken.getWeight();
/*     */         }
/* 162 */         for (int i = 0; i < tokenList.size(); i++) {
/* 163 */           Token curToken = (Token)tokenList.get(i);
/* 164 */           curToken.setWeight(curToken.getWeight() / sum);
/*     */         }
/*     */       }
/*     */     }
/* 168 */     return tokenList;
/*     */   }
/*     */ 
/*     */   private Document getDocument(Article article)
/*     */   {
/* 174 */     Document doc = new Document();
/* 175 */     if (this.useTitle)
/* 176 */       doc.addParagraph(this.parser.parseParagraph(article.getTitle()));
/* 177 */     if (this.useAbt)
/* 178 */       doc.addParagraph(this.parser.parseParagraph(article.getAbstract()));
/* 179 */     if (this.useBody)
/* 180 */       doc.addParagraph(this.parser.parseParagraph(article.getBody()));
/* 181 */     if (this.useMeta)
/* 182 */       doc.addParagraph(this.parser.parseParagraph(article.getMeta()));
/* 183 */     return doc;
/*     */   }
/*     */ 
/*     */   private ArrayList translation(ArrayList phraseList)
/*     */   {
/* 193 */     int total = 0;
/* 194 */     ArrayList newList = new ArrayList();
/* 195 */     for (int i = 0; i < phraseList.size(); i++) {
/* 196 */       Token curToken = (Token)phraseList.get(i);
/* 197 */       int index = this.phraseKeyList.search(curToken.getName());
/* 198 */       if ((index >= 0) && (this.translationMatrix.getNonZeroNumInRow(index) > 0)) {
/* 199 */         curToken.setIndex(index);
/* 200 */         total += curToken.getFrequency();
/* 201 */         newList.add(curToken);
/*     */       }
/*     */     }
/*     */ 
/* 205 */     if (newList.size() == 0) {
/* 206 */       return null;
/*     */     }
/* 208 */     SortedArray tokenList = new SortedArray(new IndexComparator());
/* 209 */     for (int i = 0; i < newList.size(); i++) {
/* 210 */       Token curToken = (Token)newList.get(i);
/* 211 */       int[] arrIndex = this.translationMatrix.getNonZeroColumnsInRow(curToken.getIndex());
/* 212 */       double[] arrProb = this.translationMatrix.getNonZeroDoubleScoresInRow(curToken.getIndex());
/* 213 */       for (int j = 0; j < arrIndex.length; j++) {
/* 214 */         Token newToken = new Token(this.tokenKeyList.search(arrIndex[j]), arrIndex[j], 0);
/* 215 */         newToken.setWeight(curToken.getFrequency() / total * arrProb[j]);
/* 216 */         if (!tokenList.add(newToken)) {
/* 217 */           Token oldToken = (Token)tokenList.get(tokenList.insertedPos());
/* 218 */           oldToken.setWeight(oldToken.getWeight() + newToken.getWeight());
/*     */         }
/*     */       }
/*     */     }
/* 222 */     return tokenList;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.query.PhraseQEGenerator
 * JD-Core Version:    0.6.2
 */