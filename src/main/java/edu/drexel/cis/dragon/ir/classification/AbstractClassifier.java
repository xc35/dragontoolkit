/*     */ package edu.drexel.cis.dragon.ir.classification;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.classification.featureselection.DocFrequencySelector;
/*     */ import edu.drexel.cis.dragon.ir.classification.featureselection.FeatureSelector;
/*     */ import edu.drexel.cis.dragon.ir.classification.featureselection.NullFeatureSelector;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.matrix.DenseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleRow;
/*     */ import edu.drexel.cis.dragon.matrix.IntRow;
/*     */ import edu.drexel.cis.dragon.matrix.Row;
/*     */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ public abstract class AbstractClassifier
/*     */   implements Classifier
/*     */ {
/*     */   protected IndexReader indexReader;
/*     */   protected SparseMatrix sparseMatrix;
/*     */   protected DenseMatrix denseMatrix;
/*     */   protected DocClassSet validatingDocSet;
/*     */   protected FeatureSelector featureSelector;
/*     */   protected String[] arrLabel;
/*     */   protected int classNum;
/*     */ 
/*     */   public AbstractClassifier(IndexReader indexReader)
/*     */   {
/*  34 */     this.featureSelector = new DocFrequencySelector(1);
/*  35 */     this.indexReader = indexReader;
/*  36 */     this.sparseMatrix = null;
/*  37 */     this.denseMatrix = null;
/*  38 */     this.validatingDocSet = null;
/*  39 */     this.arrLabel = null;
/*     */   }
/*     */ 
/*     */   public AbstractClassifier(SparseMatrix doctermMatrix) {
/*  43 */     this.featureSelector = new DocFrequencySelector(1);
/*  44 */     this.indexReader = null;
/*  45 */     this.sparseMatrix = doctermMatrix;
/*  46 */     this.denseMatrix = null;
/*  47 */     this.validatingDocSet = null;
/*  48 */     this.arrLabel = null;
/*     */   }
/*     */ 
/*     */   public AbstractClassifier(DenseMatrix doctermMatrix) {
/*  52 */     this.featureSelector = new NullFeatureSelector();
/*  53 */     this.indexReader = null;
/*  54 */     this.sparseMatrix = null;
/*  55 */     this.denseMatrix = doctermMatrix;
/*  56 */     this.validatingDocSet = null;
/*  57 */     this.arrLabel = null;
/*     */   }
/*     */ 
/*     */   public AbstractClassifier() {
/*  61 */     this.featureSelector = new NullFeatureSelector();
/*  62 */     this.indexReader = null;
/*  63 */     this.sparseMatrix = null;
/*  64 */     this.denseMatrix = null;
/*  65 */     this.validatingDocSet = null;
/*  66 */     this.arrLabel = null;
/*     */   }
/*     */ 
/*     */   public String getClassLabel(int index) {
/*  70 */     if ((this.arrLabel == null) || (index >= this.arrLabel.length)) {
/*  71 */       return null;
/*     */     }
/*  73 */     return this.arrLabel[index];
/*     */   }
/*     */ 
/*     */   public IndexReader getIndexReader() {
/*  77 */     return this.indexReader;
/*     */   }
/*     */ 
/*     */   public FeatureSelector getFeatureSelector() {
/*  81 */     return this.featureSelector;
/*     */   }
/*     */ 
/*     */   public void setFeatureSelector(FeatureSelector selector) {
/*  85 */     this.featureSelector = selector;
/*     */   }
/*     */ 
/*     */   public DocClassSet classify(DocClassSet trainingDocSet, DocClass testingDocs) {
/*  89 */     train(trainingDocSet);
/*  90 */     return classify(testingDocs);
/*     */   }
/*     */ 
/*     */   public DocClassSet classify(DocClassSet trainingDocSet, DocClassSet validatingDocSet, DocClass testingDocs) {
/*  94 */     this.validatingDocSet = validatingDocSet;
/*  95 */     return classify(trainingDocSet, testingDocs);
/*     */   }
/*     */ 
/*     */   public void train(String answerKeyFile, String docKeyFile)
/*     */   {
/* 101 */     if ((docKeyFile == null) && (this.indexReader == null)) {
/* 102 */       System.out.println("The doc key file should be provided because the index reader is not available");
/*     */       return;
/*     */     }
/*     */     SimpleElementList docKeyList;
/* 106 */     if (docKeyFile == null)
/* 107 */       docKeyList = null;
/*     */     else
/* 109 */       docKeyList = new SimpleElementList(docKeyFile, false);
/* 110 */     train(getDocClassSet(this.indexReader, docKeyList, answerKeyFile));
/*     */   }
/*     */ 
/*     */   public void train(DocClassSet trainingDocSet, DocClassSet validatingDocSet) {
/* 114 */     this.validatingDocSet = validatingDocSet;
/* 115 */     train(trainingDocSet);
/*     */   }
/*     */ 
/*     */   public DocClassSet classify(DocClass testingDocs)
/*     */   {
/* 122 */     if ((this.indexReader == null) && (this.sparseMatrix == null) && (this.denseMatrix == null))
/* 123 */       return null;
/*     */     try
/*     */     {
/* 126 */       DocClassSet docSet = new DocClassSet(this.classNum);
/* 127 */       for (int i = 0; i < this.classNum; i++)
/* 128 */         docSet.getDocClass(i).setClassName(getClassLabel(i));
/* 129 */       for (int i = 0; i < testingDocs.getDocNum(); i++) {
/* 130 */         int label = classify(testingDocs.getDoc(i));
/* 131 */         if (label >= 0)
/* 132 */           docSet.addDoc(label, testingDocs.getDoc(i));
/*     */       }
/* 134 */       return docSet;
/*     */     }
/*     */     catch (Exception e) {
/* 137 */       e.printStackTrace();
/* 138 */     }return null;
/*     */   }
/*     */ 
/*     */   public int classify(IRDoc doc)
/*     */   {
/* 143 */     return classify(getRow(doc.getIndex()));
/*     */   }
/*     */ 
/*     */   protected Row getRow(int docIndex)
/*     */   {
/* 149 */     if (this.indexReader != null) {
/* 150 */       int[] columns = this.indexReader.getTermIndexList(docIndex);
/* 151 */       if ((columns == null) || (columns.length == 0))
/* 152 */         return null;
/* 153 */       return new IntRow(docIndex, columns.length, columns, this.indexReader.getTermFrequencyList(docIndex));
/*     */     }
/* 155 */     if (this.sparseMatrix != null) {
/* 156 */       int[] columns = this.sparseMatrix.getNonZeroColumnsInRow(docIndex);
/* 157 */       if ((columns == null) || (columns.length == 0))
/* 158 */         return null;
/* 159 */       return new DoubleRow(docIndex, columns.length, columns, this.sparseMatrix.getNonZeroDoubleScoresInRow(docIndex));
/*     */     }
/*     */ 
/* 162 */     int[] columns = new int[this.denseMatrix.columns()];
/* 163 */     for (int i = 0; i < columns.length; i++)
/* 164 */       columns[i] = i;
/* 165 */     return new DoubleRow(docIndex, columns.length, columns, this.denseMatrix.getDouble(docIndex));
/*     */   }
/*     */ 
/*     */   protected void trainFeatureSelector(DocClassSet trainingSet)
/*     */   {
/* 170 */     if (this.indexReader != null)
/* 171 */       this.featureSelector.train(this.indexReader, trainingSet);
/* 172 */     else if (this.sparseMatrix != null)
/* 173 */       this.featureSelector.train(this.sparseMatrix, trainingSet);
/*     */     else
/* 175 */       this.featureSelector.train(this.denseMatrix, trainingSet);
/*     */   }
/*     */ 
/*     */   protected DocClassSet getDocClassSet(IndexReader indexReader, SimpleElementList dockeyList, String answerKeyFile)
/*     */   {
/*     */     try
/*     */     {
/* 190 */       int maxCategory = 0;
/* 191 */       ArrayList labelList = new ArrayList();
/* 192 */       TreeMap map = new TreeMap();
/* 193 */       BufferedReader br = FileUtil.getTextReader(answerKeyFile);
/* 194 */       SortedArray answerKeyList = new SortedArray(Integer.parseInt(br.readLine()), new IndexComparator());
/*     */       String line;
/* 195 */       while ((line = br.readLine()) != null)
/*     */       {
/* 196 */         String[] arrTopic = line.split("\t");
/*     */         IRDoc irDoc;
/* 197 */         if (dockeyList == null) {
/* 198 */            irDoc = indexReader.getDoc(arrTopic[1]);
/* 199 */           if (irDoc == null) continue; if (irDoc.getTermNum() < 1)
/* 200 */             continue;
/*     */         }
/*     */         else {
/* 203 */           irDoc = new IRDoc(arrTopic[1]);
/* 204 */           irDoc.setIndex(dockeyList.search(arrTopic[1]));
/*     */         }
/* 206 */         Integer curCategory = (Integer)map.get(arrTopic[0]);
/* 207 */         if (curCategory == null) {
/* 208 */           curCategory = new Integer(maxCategory);
/* 209 */           maxCategory++;
/* 210 */           map.put(arrTopic[0], curCategory);
/* 211 */           labelList.add(arrTopic[0]);
/*     */         }
/* 213 */         irDoc.setCategory(curCategory.intValue());
/* 214 */         answerKeyList.add(irDoc);
/*     */       }
/* 216 */       br.close();
/*     */ 
/* 218 */       DocClassSet set = new DocClassSet(maxCategory);
/* 219 */       for (int i = 0; i < answerKeyList.size(); i++) {
/* 220 */         IRDoc irDoc = (IRDoc)answerKeyList.get(i);
/* 221 */         set.addDoc(irDoc.getCategory(), irDoc.copy());
/*     */       }
/* 223 */       for (int i = 0; i < set.getClassNum(); i++)
/* 224 */         set.getDocClass(i).setClassName((String)labelList.get(i));
/* 225 */       return set;
/*     */     }
/*     */     catch (Exception ex) {
/* 228 */       ex.printStackTrace();
/* 229 */     }return null;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.classification.AbstractClassifier
 * JD-Core Version:    0.6.2
 */