/*    */ package edu.drexel.cis.dragon.qa.qc;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.classification.SVMLightClassifier;
/*    */ import edu.drexel.cis.dragon.matrix.IntRow;
/*    */ import edu.drexel.cis.dragon.nlp.Sentence;
/*    */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*    */ import edu.drexel.cis.dragon.nlp.Token;
/*    */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*    */ import edu.drexel.cis.dragon.nlp.extract.EngDocumentParser;
/*    */ import edu.drexel.cis.dragon.nlp.ontology.BasicVocabulary;
/*    */ import edu.drexel.cis.dragon.nlp.tool.BrillTagger;
/*    */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*    */ import edu.drexel.cis.dragon.nlp.tool.Tagger;
/*    */ import edu.drexel.cis.dragon.nlp.tool.lemmatiser.EngLemmatiser;
/*    */ import edu.drexel.cis.dragon.util.SortedArray;
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class QAClassifier
/*    */ {
/*    */   private SVMLightClassifier svm;
/*    */   private SemanticFeatureExtractor extractor;
/*    */   private SimpleElementList wordKeyList;
/*    */   private EngDocumentParser parser;
/*    */   private SortedArray mergedWordList;
/*    */ 
/*    */   public static void main(String[] args)
/*    */   {
/* 25 */     QAClassifier classifier = new QAClassifier(new BrillTagger(), new EngLemmatiser(false, false), "qac/uiuc/training/model_coarse.bin", 
/* 26 */       "qac/uiuc/training/termkey_coarse.list", "qac/uiuc/training/uiuc.vob");
/* 27 */     System.out.println(classifier.classify("which country are you from?"));
/*    */   }
/*    */ 
/*    */   public QAClassifier(Tagger tagger, Lemmatiser lemmatiser, String modelFile, String termkeyFile, String vobFile) {
/* 31 */     this.svm = new SVMLightClassifier(modelFile);
/* 32 */     this.extractor = new SemanticFeatureExtractor(new BasicVocabulary(vobFile), tagger, lemmatiser);
/* 33 */     this.wordKeyList = new SimpleElementList(termkeyFile, false);
/* 34 */     this.parser = new EngDocumentParser(" \r\n\t_;,?/\"'`:(){}!+[]><=%$#*@&^~|\\");
/* 35 */     this.mergedWordList = new SortedArray();
/*    */   }
/*    */ 
/*    */   public String classify(String sentence)
/*    */   {
/* 42 */     Sentence sent = this.parser.parseSentence(sentence);
/* 43 */     int label = this.svm.classify(processSentence(sent));
/* 44 */     return this.svm.getClassLabel(label);
/*    */   }
/*    */ 
/*    */   public String classify(Sentence sent)
/*    */   {
/* 50 */     int label = this.svm.classify(processSentence(sent));
/* 51 */     return this.svm.getClassLabel(label);
/*    */   }
/*    */ 
/*    */   public int[] getRankedPrediction() {
/* 55 */     return this.svm.rank();
/*    */   }
/*    */ 
/*    */   public double[] getConfidenceScore() {
/* 59 */     return this.svm.getBinaryClassifierConfidence();
/*    */   }
/*    */ 
/*    */   private IntRow processSentence(Sentence sent)
/*    */   {
/* 69 */     ArrayList list = this.extractor.extractFromSentence(sent);
/* 70 */     this.mergedWordList.clear();
/* 71 */     for (int i = 0; i < list.size(); i++) {
/* 72 */       Token curToken = (Token)list.get(i);
/* 73 */       if (!this.mergedWordList.add(curToken)) {
/* 74 */         ((Token)this.mergedWordList.get(this.mergedWordList.insertedPos())).addFrequency(curToken.getFrequency());
/*    */       }
/*    */     }
/*    */ 
/* 78 */     SortedArray wordList = new SortedArray(new IndexComparator());
/* 79 */     for (int i = 0; i < this.mergedWordList.size(); i++) {
/* 80 */       Token curToken = (Token)this.mergedWordList.get(i);
/* 81 */       int index = this.wordKeyList.search(curToken.getName());
/* 82 */       if (index >= 0) {
/* 83 */         curToken.setIndex(index);
/* 84 */         wordList.add(curToken);
/*    */       }
/*    */     }
/*    */ 
/* 88 */     int[] arrIndex = new int[wordList.size()];
/* 89 */     int[] arrFreq = new int[wordList.size()];
/* 90 */     for (int i = 0; i < wordList.size(); i++) {
/* 91 */       Token curToken = (Token)wordList.get(i);
/* 92 */       arrIndex[i] = curToken.getIndex();
/* 93 */       arrFreq[i] = curToken.getFrequency();
/*    */     }
/* 95 */     list.clear();
/* 96 */     return new IntRow(0, arrIndex.length, arrIndex, arrFreq);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.qc.QAClassifier
 * JD-Core Version:    0.6.2
 */