/*     */ package edu.drexel.cis.dragon.qa.qc;
/*     */ 
/*     */ import edu.drexel.cis.dragon.config.ClassificationEvaAppConfig;
/*     */ import edu.drexel.cis.dragon.ir.classification.DocClass;
/*     */ import edu.drexel.cis.dragon.ir.classification.DocClassSet;
/*     */ import edu.drexel.cis.dragon.ir.classification.SVMLightClassifier;
/*     */ import edu.drexel.cis.dragon.ir.classification.featureselection.DocFrequencySelector;
/*     */ import edu.drexel.cis.dragon.ir.classification.featureselection.FeatureSelector;
/*     */ import edu.drexel.cis.dragon.ir.classification.multiclass.HingeLoss;
/*     */ import edu.drexel.cis.dragon.ir.classification.multiclass.LossMultiClassDecoder;
/*     */ import edu.drexel.cis.dragon.ir.classification.multiclass.OVACodeMatrix;
/*     */ import edu.drexel.cis.dragon.ir.index.BasicIndexReader;
/*     */ import edu.drexel.cis.dragon.ir.index.IRCollection;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.matrix.IntRow;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElement;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ public class Evaluator
/*     */ {
/*     */   public static void main(String[] args)
/*     */   {
/*  24 */     Evaluator eva = new Evaluator();
/*     */ 
/*  27 */     eva.train("qa/qac_dragon/semantic", "qa/qac_dragon/experiment/answerkey_training_coarse.list", false, "qa/qac_dragon/training");
/*     */   }
/*     */ 
/*     */   public void evaluate(String indexFolder, String trainingAnswerKey, String testingAnswerKey, String runName, String outputFile)
/*     */   {
/*  36 */     IndexReader indexReader = getIndexReader(indexFolder);
/*  37 */     int classNum = getClassNum(trainingAnswerKey);
/*  38 */     SVMLightClassifier classifier = new SVMLightClassifier(indexReader);
/*  39 */     classifier.setUseLinearKernel();
/*  40 */     classifier.setFeatureSelector(new DocFrequencySelector(1));
/*  41 */     classifier.setCodeMatrix(new OVACodeMatrix(classNum));
/*  42 */     classifier.setMultiClassDecoder(new LossMultiClassDecoder(new HingeLoss()));
/*  43 */     ClassificationEvaAppConfig config = new ClassificationEvaAppConfig();
/*  44 */     config.evaluateManual(classifier, classNum, trainingAnswerKey, null, testingAnswerKey, runName, outputFile);
/*     */   }
/*     */ 
/*     */   public void evaluate(boolean separateClassifier, String indexFolder, String trainingAnswerKeyFile, String testingAnswerKeyFile, String outputFile)
/*     */   {
/*  60 */     PrintWriter bw = FileUtil.getPrintWriter(outputFile);
/*  61 */     IndexReader indexReader = getIndexReader(indexFolder);
/*  62 */     SVMLightClassifier classifier = new SVMLightClassifier(indexReader);
/*  63 */     classifier.setUseLinearKernel();
/*  64 */     classifier.setFeatureSelector(new DocFrequencySelector(1));
/*  65 */     classifier.setMultiClassDecoder(new LossMultiClassDecoder(new HingeLoss()));
/*  66 */     ArrayList trainingAnswerKeyList = getAnswerKeyList(trainingAnswerKeyFile);
/*  67 */     ArrayList testingAnswerKeyList = getAnswerKeyList(testingAnswerKeyFile);
/*     */     String[] arrQuestionWord;
/*  69 */     if (separateClassifier) {
/*  70 */       arrQuestionWord = "who whose whom what which how why when where unknown".split(" ");
/*     */     }
/*     */     else {
/*  73 */       arrQuestionWord = new String[1];
/*  74 */       arrQuestionWord[0] = "all";
/*     */     }
/*  76 */     int totalTested = 0;
/*  77 */     int totalCorrected = 0;
/*     */ 
/*  79 */     for (int i = 0; i < arrQuestionWord.length; i++)
/*     */     {
/*  81 */       ArrayList list = filterAnswerKey(trainingAnswerKeyList, arrQuestionWord[i]);
/*  82 */       if (list.size() != 0)
/*     */       {
/*  84 */         int classNum = getClassNum(list);
/*  85 */         classifier.setCodeMatrix(new OVACodeMatrix(classNum));
/*     */ 
/*  87 */         bw.println("Classifier for question word: " + arrQuestionWord[i]);
/*  88 */         bw.println("Number of training cases: " + list.size());
/*  89 */         bw.println("Number of categories: " + classNum);
/*     */ 
/*  91 */         classifier.train(getTrainingSet(indexReader, list));
/*     */ 
/*  94 */         list.clear();
/*  95 */         list = filterAnswerKey(testingAnswerKeyList, arrQuestionWord[i]);
/*  96 */         int corrected = 0;
/*     */ 
/*  98 */         bw.println("Number of testing cases: " + list.size());
/*     */ 
/* 100 */         if (list.size() != 0)
/*     */         {
/* 102 */           totalTested += list.size();
/* 103 */           for (int j = 0; j < list.size(); j++) {
/* 104 */             Question curQuest = (Question)list.get(j);
/* 105 */             IRDoc curDoc = indexReader.getDoc(curQuest.getQuestionKey());
/* 106 */             int[] arrIndex = indexReader.getTermIndexList(curDoc.getIndex());
/* 107 */             int[] arrFreq = indexReader.getTermFrequencyList(curDoc.getIndex());
/* 108 */             IntRow row = new IntRow(curDoc.getIndex(), arrIndex.length, arrIndex, arrFreq);
/* 109 */             String predictLabel = classifier.getClassLabel(classifier.classify(row));
/* 110 */             if (predictLabel.equalsIgnoreCase(curQuest.getCategory())) {
/* 111 */               corrected++;
/* 112 */               totalCorrected++;
/*     */             }
/* 114 */             bw.println(curQuest.getQuestionKey() + "\t" + curQuest.getCategory() + "\t" + predictLabel);
/*     */           }
/* 116 */           bw.println("Precision: " + corrected / list.size());
/* 117 */           bw.flush();
/* 118 */           System.out.println("Classifier for question word " + arrQuestionWord[i] + ": " + list.size() + " " + corrected + " " + corrected / list.size());
/* 119 */           list.clear();
/*     */         }
/*     */       }
/*     */     }
/* 121 */     bw.println("Precision for all questions: " + totalCorrected / totalTested);
/* 122 */     bw.close();
/* 123 */     System.out.println("Precision for all questions: " + totalCorrected / totalTested);
/*     */   }
/*     */ 
/*     */   public void train(String indexFolder, String trainingAnswerKeyFile, boolean fine, String outputFolder)
/*     */   {
/*     */     String termkeyFile;
/*     */     String modelFile;
/* 135 */     if (fine) {
/* 136 */        modelFile = outputFolder + "/model_fine.bin";
/* 137 */       termkeyFile = outputFolder + "/termkey_fine.list";
/*     */     }
/*     */     else {
/* 140 */       modelFile = outputFolder + "/model_coarse.bin";
/* 141 */       termkeyFile = outputFolder + "/termkey_coarse.list";
/*     */     }
/*     */ 
/* 144 */     IndexReader indexReader = getIndexReader(indexFolder);
/* 145 */     SVMLightClassifier classifier = new SVMLightClassifier(indexReader);
/* 146 */     classifier.setUseLinearKernel();
/* 147 */     classifier.setFeatureSelector(new DocFrequencySelector(1));
/* 148 */     classifier.setCodeMatrix(new OVACodeMatrix());
/* 149 */     classifier.setMultiClassDecoder(new LossMultiClassDecoder(new HingeLoss()));
/* 150 */     classifier.train(getTrainingSet(indexReader, getAnswerKeyList(trainingAnswerKeyFile)));
/* 151 */     classifier.saveModel(modelFile);
/*     */ 
/* 153 */     int total = indexReader.getCollection().getTermNum();
/* 154 */     FeatureSelector selector = classifier.getFeatureSelector();
/* 155 */     File file = new File(termkeyFile);
/* 156 */     if (file.exists())
/* 157 */       file.delete();
/* 158 */     SimpleElementList wordKeyList = new SimpleElementList(termkeyFile, true);
/* 159 */     for (int i = 0; i < total; i++) {
/* 160 */       if (selector.map(i) >= 0)
/* 161 */         wordKeyList.add(new SimpleElement(indexReader.getTermKey(i), selector.map(i)));
/*     */     }
/* 163 */     wordKeyList.close();
/* 164 */     indexReader.close();
/*     */   }
/*     */ 
/*     */   private ArrayList filterAnswerKey(ArrayList answerKeyList, String questionWord)
/*     */   {
/* 172 */     if ((questionWord == null) || (questionWord.equalsIgnoreCase("all"))) {
/* 173 */       return answerKeyList;
/*     */     }
/* 175 */     ArrayList list = new ArrayList();
/* 176 */     for (int i = 0; i < answerKeyList.size(); i++) {
/* 177 */       Question curQuest = (Question)answerKeyList.get(i);
/* 178 */       if (questionWord.equalsIgnoreCase(curQuest.getQuestionWord()))
/* 179 */         list.add(curQuest);
/*     */     }
/* 181 */     return list;
/*     */   }
/*     */ 
/*     */   private DocClassSet getTrainingSet(IndexReader indexReader, ArrayList answerKeyList)
/*     */   {
/*     */     try
/*     */     {
/* 193 */       TreeMap map = new TreeMap();
/* 194 */       int maxCategory = 0;
/* 195 */       int classNum = getClassNum(answerKeyList);
/* 196 */       DocClassSet trainingSet = new DocClassSet(classNum);
/* 197 */       for (int i = 0; i < answerKeyList.size(); i++) {
/* 198 */         Question curQuest = (Question)answerKeyList.get(i);
/* 199 */         IRDoc irDoc = indexReader.getDoc(curQuest.getQuestionKey());
/* 200 */         if ((irDoc != null) && (irDoc.getTermNum() >= 1))
/*     */         {
/* 202 */           Integer curCategory = (Integer)map.get(curQuest.getCategory());
/* 203 */           if (curCategory == null) {
/* 204 */             curCategory = new Integer(maxCategory);
/* 205 */             maxCategory++;
/* 206 */             map.put(curQuest.getCategory(), curCategory);
/* 207 */             trainingSet.getDocClass(curCategory.intValue()).setClassName(curQuest.getCategory());
/*     */           }
/* 209 */           trainingSet.addDoc(curCategory.intValue(), irDoc);
/*     */         }
/*     */       }
/* 211 */       return trainingSet;
/*     */     }
/*     */     catch (Exception ex) {
/* 214 */       ex.printStackTrace();
/* 215 */     }return null;
/*     */   }
/*     */ 
/*     */   private ArrayList getAnswerKeyList(String answerKeyFile)
/*     */   {
/*     */     try
/*     */     {
/* 226 */       BufferedReader br = FileUtil.getTextReader(answerKeyFile);
/* 227 */       ArrayList answerKeyList = new ArrayList(Integer.parseInt(br.readLine()));
/*     */       String line;
/* 228 */       while ((line = br.readLine()) != null)
/*     */       {
/* 229 */         String[] arrTopic = line.split("\t");
/* 230 */         Question curQuest = new Question(arrTopic[1]);
/* 231 */         curQuest.setCategory(arrTopic[0]);
/* 232 */         curQuest.setQuestionWord(arrTopic[2]);
/* 233 */         answerKeyList.add(curQuest);
/*     */       }
/* 235 */       br.close();
/* 236 */       return answerKeyList;
/*     */     }
/*     */     catch (Exception ex) {
/* 239 */       ex.printStackTrace();
/* 240 */     }return null;
/*     */   }
/*     */ 
/*     */   private int getClassNum(ArrayList answerKeyList)
/*     */   {
/*     */     try
/*     */     {
/* 249 */       SortedArray list = new SortedArray();
/* 250 */       for (int i = 0; i < answerKeyList.size(); i++) {
/* 251 */         list.add(((Question)answerKeyList.get(i)).getCategory());
/*     */       }
/* 253 */       return list.size();
/*     */     } catch (Exception e) {
/*     */     }
/* 256 */     return 0;
/*     */   }
/*     */ 
/*     */   private int getClassNum(String trainingAnswerKey)
/*     */   {
/*     */     try
/*     */     {
/* 266 */       SortedArray list = new SortedArray();
/* 267 */       BufferedReader br = FileUtil.getTextReader(trainingAnswerKey);
/* 268 */       br.readLine();
/*     */       String line;
/* 270 */       while ((line = br.readLine()) != null)
/*     */       {
/* 271 */         list.add(line.substring(0, line.indexOf('\t')));
/*     */       }
/* 273 */       br.close();
/* 274 */       return list.size();
/*     */     } catch (Exception e) {
/*     */     }
/* 277 */     return 0;
/*     */   }
/*     */ 
/*     */   private IndexReader getIndexReader(String indexFolder)
/*     */   {
/* 284 */     BasicIndexReader indexReader = new BasicIndexReader(indexFolder + "/all", false);
/* 285 */     indexReader.initialize();
/* 286 */     indexReader.setIRDocKeyList(new SimpleElementList(indexFolder + "/dockey.list", false));
/* 287 */     indexReader.setIRTermKeyList(new SimpleElementList(indexFolder + "/termkey.list", false));
/* 288 */     return indexReader;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.qc.Evaluator
 * JD-Core Version:    0.6.2
 */