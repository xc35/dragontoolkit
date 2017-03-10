/*     */ package edu.drexel.cis.dragon.config;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.classification.ClassificationEva;
/*     */ import edu.drexel.cis.dragon.ir.classification.Classifier;
/*     */ import edu.drexel.cis.dragon.ir.classification.DocClass;
/*     */ import edu.drexel.cis.dragon.ir.classification.DocClassSet;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.matrix.vector.DoubleVector;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.FormatUtil;
/*     */ import edu.drexel.cis.dragon.util.MathUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Random;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ public class ClassificationEvaAppConfig
/*     */ {
/*     */   private DecimalFormat df;
/*     */   private PrintWriter out;
/*     */   private TreeMap map;
/*     */   private ArrayList labelList;
/*     */   private int maxCategory;
/*     */ 
/*     */   public ClassificationEvaAppConfig()
/*     */   {
/*  34 */     this.df = FormatUtil.getNumericFormat(1, 4);
/*  35 */     this.map = new TreeMap();
/*  36 */     this.maxCategory = 0;
/*  37 */     this.labelList = new ArrayList();
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*  45 */     if (args.length != 2) {
/*  46 */       System.out.println("Please input two parameters: configuration xml file and clustering evaluation id");
/*  47 */       return;
/*     */     }
/*     */ 
/*  50 */     ConfigureNode root = new BasicConfigureNode(args[0]);
/*  51 */     ConfigUtil util = new ConfigUtil();
/*  52 */     ConfigureNode classificationAppNode = util.getConfigureNode(root, "classificationevaapp", Integer.parseInt(args[1]));
/*  53 */     if (classificationAppNode == null)
/*  54 */       return;
/*  55 */     ClassificationEvaAppConfig classificationApp = new ClassificationEvaAppConfig();
/*  56 */     classificationApp.evaluate(classificationAppNode);
/*     */   }
/*     */ 
/*     */   public void evaluate(ConfigureNode node)
/*     */   {
/*  67 */     String dockeyFile = node.getString("dockeyfile", null);
/*     */     SimpleElementList dockeyList;
/*  68 */     if (dockeyFile == null)
/*  69 */       dockeyList = null;
/*     */     else
/*  71 */       dockeyList = new SimpleElementList(dockeyFile, false);
/*  72 */     int classNum = node.getInt("classnum");
/*  73 */     int classifierID = node.getInt("classifier");
/*  74 */     Classifier classifier = new ClassifierConfig().getClassifier(node, classifierID);
/*  75 */     String validatingKeyFile = node.getString("validatingkeyfile", null);
/*  76 */     String outputFile = node.getString("outputfile");
/*  77 */     String runName = node.getString("runname");
/*  78 */     String mode = node.getString("mode");
/*  79 */     if (mode == null)
/*  80 */       return;
/*  81 */     if (mode.equalsIgnoreCase("CrossValidation")) {
/*  82 */       String answerKeyFile = node.getString("answerkeyfile");
/*  83 */       int foldNum = node.getInt("foldnum", 5);
/*  84 */       evaluateCrossValidation(classifier, dockeyList, classNum, answerKeyFile, validatingKeyFile, foldNum, runName, outputFile);
/*     */     }
/*  86 */     else if (mode.equalsIgnoreCase("Percentage")) {
/*  87 */       int randomSeed = node.getInt("randomseed", -1);
/*  88 */       int runs = node.getInt("runs", 1);
/*  89 */       String answerKeyFile = node.getString("answerkeyfile");
/*  90 */       double percentage = node.getDouble("percentage", 0.67D);
/*  91 */       if (percentage > 1.0D)
/*  92 */         percentage /= 100.0D;
/*  93 */       evaluatePercentage(classifier, dockeyList, classNum, answerKeyFile, validatingKeyFile, percentage, runs, randomSeed, runName, outputFile);
/*     */     }
/*  95 */     else if (mode.equalsIgnoreCase("Manual")) {
/*  96 */       String trainingKeyFile = node.getString("trainingkeyfile");
/*  97 */       String testingKeyFile = node.getString("testingkeyfile");
/*  98 */       evaluateManual(classifier, dockeyList, classNum, trainingKeyFile, validatingKeyFile, testingKeyFile, runName, outputFile);
/*     */     }
/*     */     else {
/* 101 */       return;
/* 102 */     }this.out.close();
/*     */   }
/*     */ 
/*     */   public void evaluateCrossValidation(Classifier classifier, int classNum, String answerKeyFile, String validatingKeyFile, int foldNum, String runName, String outputFile)
/*     */   {
/* 121 */     evaluateCrossValidation(classifier, null, classNum, answerKeyFile, validatingKeyFile, foldNum, runName, outputFile);
/*     */   }
/*     */ 
/*     */   public void evaluateCrossValidation(Classifier classifier, SimpleElementList dockeyList, int classNum, String answerKeyFile, String validatingKeyFile, int foldNum, String runName, String outputFile)
/*     */   {
/* 134 */     this.map.clear();
/* 135 */     this.maxCategory = 0;
/* 136 */     this.labelList.clear();
/* 137 */     this.out = FileUtil.getPrintWriter(outputFile, true);
/* 138 */     IndexReader indexReader = classifier.getIndexReader();
/*     */     DocClassSet validatingSet;
/* 139 */     if (validatingKeyFile != null)
/* 140 */       validatingSet = getDocClassSet(getAnswerKeyList(indexReader, dockeyList, validatingKeyFile), classNum);
/*     */     else
/* 142 */       validatingSet = null;
/* 143 */     ArrayList answerKeyList = getAnswerKeyList(indexReader, dockeyList, answerKeyFile);
/* 144 */     Collections.shuffle(answerKeyList, new Random(100L));
/* 145 */     ArrayList[] folds = split(answerKeyList, foldNum);
/*     */ 
/* 147 */     int top = Math.min(5, classNum - 1);
/* 148 */     DoubleVector result = new DoubleVector(7 + top);
/* 149 */     result.assign(0.0D);
/* 150 */     write("Number of Classes: " + classNum + "\n");
/* 151 */     write("Number of Documents: " + answerKeyList.size() + "\n");
/* 152 */     write("Number of Folds: " + foldNum + "\n");
/* 153 */     printHeader(top);
/* 154 */     for (int i = 0; i < foldNum; i++) {
/* 155 */       DocClassSet trainingSet = getCrossTrainingSet(folds, i, classNum);
/* 156 */       int[] answers = getAnswers(folds[i]);
/* 157 */       DocClass testingDocs = getDocClass(folds[i]);
/* 158 */       classifier.train(trainingSet, validatingSet);
/* 159 */       result.add(evaluate(classifier, classNum, testingDocs, answers, runName + "_" + (i + 1)));
/*     */     }
/* 161 */     result.multiply(1.0D / foldNum);
/* 162 */     writeResult("Average", result);
/*     */   }
/*     */ 
/*     */   public void evaluatePercentage(Classifier classifier, int classNum, String answerKeyFile, String validatingKeyFile, double percentage, int runs, int randomSeed, String runName, String outputFile)
/*     */   {
/* 185 */     evaluatePercentage(classifier, null, classNum, answerKeyFile, validatingKeyFile, percentage, runs, randomSeed, runName, outputFile);
/*     */   }
/*     */ 
/*     */   public void evaluatePercentage(Classifier classifier, SimpleElementList dockeyList, int classNum, String answerKeyFile, String validatingKeyFile, double percentage, int runs, int randomSeed, String runName, String outputFile)
/*     */   {
/* 199 */     this.map.clear();
/* 200 */     this.maxCategory = 0;
/* 201 */     this.labelList.clear();
/* 202 */     this.out = FileUtil.getPrintWriter(outputFile, true);
/*     */     Random random;
/* 203 */     if (randomSeed >= 0)
/* 204 */       random = new Random(randomSeed);
/*     */     else
/* 206 */       random = new Random();
/* 207 */     IndexReader indexReader = classifier.getIndexReader();
/*     */     DocClassSet validatingSet;
/* 208 */     if (validatingKeyFile != null)
/* 209 */       validatingSet = getDocClassSet(getAnswerKeyList(indexReader, dockeyList, validatingKeyFile), classNum);
/*     */     else
/* 211 */       validatingSet = null;
/* 212 */     ArrayList answerKeyList = getAnswerKeyList(indexReader, dockeyList, answerKeyFile);
/*     */ 
/* 214 */     int top = Math.min(5, classNum - 1);
/* 215 */     DoubleVector result = new DoubleVector(7 + top);
/* 216 */     result.assign(0.0D);
/* 217 */     write("Number of Classes: " + classNum + "\n");
/* 218 */     write("Number of Documents: " + answerKeyList.size() + "\n");
/* 219 */     write("Percentage of Training: " + this.df.format(percentage) + "\n");
/* 220 */     write("Number of Runs: " + runs + "\n");
/* 221 */     write("Random Seed: " + randomSeed + "\n");
/* 222 */     printHeader(top);
/* 223 */     for (int i = 0; i < runs; i++) {
/* 224 */       ArrayList list = new ArrayList(answerKeyList);
/* 225 */       Collections.shuffle(list, random);
/* 226 */       ArrayList[] arrList = split(list, classNum, percentage);
/* 227 */       DocClassSet trainingSet = getDocClassSet(arrList[0], classNum);
/* 228 */       int[] answers = getAnswers(arrList[1]);
/* 229 */       DocClass testingDocs = getDocClass(arrList[1]);
/* 230 */       classifier.train(trainingSet, validatingSet);
/* 231 */       result.add(evaluate(classifier, classNum, testingDocs, answers, runName + "_" + (i + 1)));
/*     */     }
/* 233 */     result.multiply(1.0D / runs);
/* 234 */     writeResult("Average", result);
/*     */   }
/*     */ 
/*     */   public void evaluateManual(Classifier classifier, int classNum, String trainingKeyFile, String validatingKeyFile, String testingKeyFile, String runName, String outputFile)
/*     */   {
/* 253 */     evaluateManual(classifier, null, classNum, trainingKeyFile, validatingKeyFile, testingKeyFile, runName, outputFile);
/*     */   }
/*     */ 
/*     */   public void evaluateManual(Classifier classifier, SimpleElementList dockeyList, int classNum, String trainingKeyFile, String validatingKeyFile, String testingKeyFile, String runName, String outputFile)
/*     */   {
/* 264 */     this.map.clear();
/* 265 */     this.maxCategory = 0;
/* 266 */     this.labelList.clear();
/* 267 */     this.out = FileUtil.getPrintWriter(outputFile, true);
/* 268 */     IndexReader indexReader = classifier.getIndexReader();
/*     */     DocClassSet validatingSet;
/* 269 */     if (validatingKeyFile != null)
/* 270 */       validatingSet = getDocClassSet(getAnswerKeyList(indexReader, dockeyList, validatingKeyFile), classNum);
/*     */     else
/* 272 */       validatingSet = null;
/* 273 */     ArrayList trainingList = getAnswerKeyList(indexReader, dockeyList, trainingKeyFile);
/* 274 */     DocClassSet trainingSet = getDocClassSet(trainingList, classNum);
/* 275 */     ArrayList testingList = getAnswerKeyList(indexReader, dockeyList, testingKeyFile);
/* 276 */     int[] answers = getAnswers(testingList);
/* 277 */     DocClass testingDocs = getDocClass(testingList);
/*     */ 
/* 279 */     write("Number of Classes: " + classNum + "\n");
/* 280 */     write("Number of Training Documents: " + trainingList.size() + "\n");
/* 281 */     write("Number of Testing Documents: " + testingList.size() + "\n");
/*     */ 
/* 283 */     classifier.train(trainingSet, validatingSet);
/* 284 */     printHeader(Math.min(5, classNum - 1));
/* 285 */     evaluate(classifier, classNum, testingDocs, answers, runName);
/*     */   }
/*     */ 
/*     */   private DoubleVector evaluate(Classifier classifier, int classNum, DocClass testingSet, int[] answer, String runName)
/*     */   {
/* 294 */     int[][] arrPrediction = new int[testingSet.getDocNum()][];
/* 295 */     for (int i = 0; i < testingSet.getDocNum(); i++) {
/* 296 */       int label = classifier.classify(testingSet.getDoc(i));
/* 297 */       if (label >= 0)
/* 298 */         arrPrediction[i] = classifier.rank();
/*     */     }
/* 300 */     int dim = Math.min(5, classNum - 1);
/* 301 */     DoubleVector result = new DoubleVector(7 + dim);
/* 302 */     ClassificationEva eva = new ClassificationEva();
/* 303 */     eva.evaluate(classNum, answer, arrPrediction);
/* 304 */     result.set(0, eva.getMicroRecall());
/* 305 */     result.set(1, eva.getMicroPrecision());
/* 306 */     result.set(2, eva.getMicroFScore());
/* 307 */     result.set(3, eva.getMacroRecall());
/* 308 */     result.set(4, eva.getMacroPrecision());
/* 309 */     result.set(5, eva.getMacroFScore());
/* 310 */     result.set(6, eva.getMRR());
/* 311 */     for (int i = 0; i < dim; i++)
/* 312 */       result.set(7 + i, eva.getPrecisionN(i));
/* 313 */     writeResult(runName, result);
/* 314 */     return result;
/*     */   }
/*     */ 
/*     */   private void printHeader(int top)
/*     */   {
/* 320 */     write("Run\tmicro-R\tmicro-P\tmicro-F\tmacro-R\tmacro-P\tmacro-F\tMRR");
/* 321 */     for (int i = 1; i <= top; i++)
/* 322 */       write("\tP" + i);
/* 323 */     write("\n");
/*     */   }
/*     */ 
/*     */   private void writeResult(String runName, DoubleVector result)
/*     */   {
/* 329 */     write(runName);
/* 330 */     for (int i = 0; i < result.size(); i++) {
/* 331 */       write("\t");
/* 332 */       write(this.df.format(result.get(i)));
/*     */     }
/* 334 */     write("\n");
/*     */   }
/*     */ 
/*     */   private void write(String message) {
/* 338 */     System.out.print(message);
/* 339 */     this.out.write(message);
/* 340 */     this.out.flush();
/*     */   }
/*     */ 
/*     */   private DocClassSet getCrossTrainingSet(ArrayList[] folds, int leaveoutFold, int classNum)
/*     */   {
/* 347 */     ArrayList list = new ArrayList();
/* 348 */     for (int i = 0; i < folds.length; i++)
/* 349 */       if (i != leaveoutFold)
/* 350 */         list.addAll(folds[i]);
/* 351 */     return getDocClassSet(list, classNum);
/*     */   }
/*     */ 
/*     */   private ArrayList[] split(ArrayList docList, int foldNum)
/*     */   {
/* 358 */     ArrayList[] folds = new ArrayList[foldNum];
/* 359 */     for (int i = 0; i < foldNum; i++) {
/* 360 */       folds[i] = new ArrayList(docList.size() / foldNum + 1);
/*     */     }
/* 362 */     for (int i = 0; i < docList.size(); i++)
/*     */     {
/* 364 */       folds[(i % foldNum)].add(docList.get(i));
/*     */     }
/* 366 */     return folds;
/*     */   }
/*     */ 
/*     */   private ArrayList[] split(ArrayList docList, int classNum, double trainingPercentage)
/*     */   {
/* 374 */     int[] arrStat = new int[classNum];
/* 375 */     MathUtil.initArray(arrStat, 0);
/* 376 */     for (int i = 0; i < docList.size(); i++)
/* 377 */       arrStat[((IRDoc)docList.get(i)).getCategory()] += 1;
/* 378 */     for (int i = 0; i < classNum; i++) {
/* 379 */       arrStat[i] = ((int)(arrStat[i] * trainingPercentage + 0.5D));
/* 380 */       if (arrStat[i] == 0) {
/* 381 */         arrStat[i] = 1;
/*     */       }
/*     */     }
/* 384 */     ArrayList[] arrList = new ArrayList[2];
/* 385 */     arrList[0] = new ArrayList();
/* 386 */     arrList[1] = new ArrayList();
/* 387 */     int[] arrCount = new int[classNum];
/* 388 */     MathUtil.initArray(arrCount, 0);
/* 389 */     for (int i = 0; i < docList.size(); i++) {
/* 390 */       IRDoc curDoc = (IRDoc)docList.get(i);
/* 391 */       if (arrCount[curDoc.getCategory()] >= arrStat[curDoc.getCategory()]) {
/* 392 */         arrList[1].add(curDoc);
/*     */       } else {
/* 394 */         arrList[0].add(curDoc);
/* 395 */         arrCount[curDoc.getCategory()] += 1;
/*     */       }
/*     */     }
/* 398 */     return arrList;
/*     */   }
/*     */ 
/*     */   private DocClass getDocClass(ArrayList docList)
/*     */   {
/* 406 */     DocClass set = new DocClass(-1);
/* 407 */     for (int i = 0; i < docList.size(); i++) {
/* 408 */       IRDoc curDoc = (IRDoc)docList.get(i);
/* 409 */       set.addDoc(curDoc.copy());
/*     */     }
/* 411 */     return set;
/*     */   }
/*     */ 
/*     */   private DocClassSet getDocClassSet(ArrayList docList, int classNum)
/*     */   {
/* 419 */     DocClassSet set = new DocClassSet(classNum);
/* 420 */     for (int i = 0; i < docList.size(); i++) {
/* 421 */       IRDoc curDoc = (IRDoc)docList.get(i);
/* 422 */       set.addDoc(curDoc.getCategory(), curDoc.copy());
/*     */     }
/* 424 */     for (int i = 0; i < set.getClassNum(); i++)
/* 425 */       set.getDocClass(i).setClassName((String)this.labelList.get(i));
/* 426 */     return set;
/*     */   }
/*     */ 
/*     */   private int[] getAnswers(ArrayList docList)
/*     */   {
/* 434 */     SortedArray list = new SortedArray();
/* 435 */     list.addAll(docList);
/* 436 */     list.setComparator(new IndexComparator());
/* 437 */     int[] answers = new int[list.size()];
/* 438 */     for (int i = 0; i < list.size(); i++)
/* 439 */       answers[i] = ((IRDoc)list.get(i)).getCategory();
/* 440 */     return answers;
/*     */   }
/*     */ 
/*     */   private ArrayList getAnswerKeyList(IndexReader indexReader, SimpleElementList dockeyList, String answerKeyFile)
/*     */   {
/*     */     try
/*     */     {
/* 451 */       BufferedReader br = FileUtil.getTextReader(answerKeyFile);
/* 452 */       SortedArray answerKeyList = new SortedArray(Integer.parseInt(br.readLine()), new IndexComparator());
/*     */       String line;
/* 453 */       while ((line = br.readLine()) != null)
/*     */       {
/* 454 */         String[] arrTopic = line.split("\t");
/*     */         IRDoc irDoc;
/* 455 */         if (indexReader != null) {
/* 456 */            irDoc = indexReader.getDoc(arrTopic[1]);
/* 457 */           if (irDoc == null) continue; if (irDoc.getTermNum() < 1)
/* 458 */             continue;
/*     */         }
/*     */         else {
/* 461 */           irDoc = new IRDoc(arrTopic[1]);
/* 462 */           irDoc.setIndex(dockeyList.search(arrTopic[1]));
/*     */         }
/* 464 */         Integer curCategory = (Integer)this.map.get(arrTopic[0]);
/* 465 */         if (curCategory == null) {
/* 466 */           curCategory = new Integer(this.maxCategory);
/* 467 */           this.maxCategory += 1;
/* 468 */           this.map.put(arrTopic[0], curCategory);
/* 469 */           this.labelList.add(arrTopic[0]);
/*     */         }
/* 471 */         irDoc.setCategory(curCategory.intValue());
/* 472 */         answerKeyList.add(irDoc);
/*     */       }
/* 474 */       br.close();
/* 475 */       return answerKeyList;
/*     */     }
/*     */     catch (Exception ex) {
/* 478 */       ex.printStackTrace();
/* 479 */     }return null;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.ClassificationEvaAppConfig
 * JD-Core Version:    0.6.2
 */