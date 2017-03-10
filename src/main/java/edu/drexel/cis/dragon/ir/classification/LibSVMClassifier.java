/*     */ package edu.drexel.cis.dragon.ir.classification;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.classification.featureselection.FeatureSelector;
/*     */ import edu.drexel.cis.dragon.ir.classification.multiclass.AllPairCodeMatrix;
/*     */ import edu.drexel.cis.dragon.ir.classification.multiclass.CodeMatrix;
/*     */ import edu.drexel.cis.dragon.ir.classification.multiclass.MultiClassDecoder;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.matrix.DenseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.Row;
/*     */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*     */ import edu.drexel.cis.dragon.util.MathUtil;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Vector;
/*     */ import libsvm.svm;
/*     */ import libsvm.svm_model;
/*     */ import libsvm.svm_node;
/*     */ import libsvm.svm_parameter;
/*     */ import libsvm.svm_problem;
/*     */ 
/*     */ public class LibSVMClassifier extends AbstractClassifier
/*     */ {
/*     */   private svm_parameter param;
/*     */   private svm_model model;
/*     */   private CodeMatrix codeMatrix;
/*     */   private MultiClassDecoder classDecoder;
/*     */   private double[] arrProb;
/*     */   private double[] arrConfidence;
/*     */   private boolean scale;
/*     */   private int[] rank;
/*     */ 
/*     */   public LibSVMClassifier(String modelFile)
/*     */   {
/*     */     try
/*     */     {
/*  35 */       ObjectInputStream oin = new ObjectInputStream(new FileInputStream(modelFile));
/*  36 */       this.model = ((svm_model)oin.readObject());
/*  37 */       this.param = ((svm_parameter)oin.readObject());
/*  38 */       this.codeMatrix = ((CodeMatrix)oin.readObject());
/*  39 */       this.classDecoder = ((MultiClassDecoder)oin.readObject());
/*  40 */       this.classNum = oin.readInt();
/*  41 */       this.scale = oin.readBoolean();
/*  42 */       this.featureSelector = ((FeatureSelector)oin.readObject());
/*  43 */       this.arrLabel = new String[this.classNum];
/*  44 */       for (int i = 0; i < this.arrLabel.length; i++)
/*  45 */         this.arrLabel[i] = ((String)oin.readObject());
/*     */     }
/*     */     catch (Exception e) {
/*  48 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public LibSVMClassifier(IndexReader indexReader) {
/*  53 */     super(indexReader);
/*  54 */     this.param = getDefaultParameter();
/*  55 */     this.codeMatrix = new AllPairCodeMatrix(1);
/*  56 */     this.classDecoder = null;
/*  57 */     //svm.showErrorMessage = true;
/*  58 */     //svm.showMessage = false;
/*  59 */     this.model = null;
/*  60 */     this.scale = true;
/*     */   }
/*     */ 
/*     */   public LibSVMClassifier(SparseMatrix doctermMatrix) {
/*  64 */     super(doctermMatrix);
/*  65 */     this.param = getDefaultParameter();
/*  66 */     this.codeMatrix = new AllPairCodeMatrix(1);
/*  67 */     this.classDecoder = null;
/*  68 */     //svm.showErrorMessage = true;
/*  69 */     //svm.showMessage = false;
/*  70 */     this.model = null;
/*  71 */     this.scale = true;
/*     */   }
/*     */ 
/*     */   public LibSVMClassifier(DenseMatrix doctermMatrix) {
/*  75 */     super(doctermMatrix);
/*  76 */     this.param = getDefaultParameter();
/*  77 */     this.codeMatrix = new AllPairCodeMatrix(1);
/*  78 */     this.classDecoder = null;
/*  79 */     //svm.showErrorMessage = true;
/*  80 */     //svm.showMessage = false;
/*  81 */     this.model = null;
/*  82 */     this.scale = true;
/*     */   }
/*     */ 
/*     */   public void setMultiClassDecoder(MultiClassDecoder decoder)
/*     */   {
/*  90 */     this.classDecoder = decoder;
/*     */   }
/*     */ 
/*     */   public void setUseProbEstimate(boolean option) {
/*  94 */     this.param.probability = (option ? 1 : 0);
/*     */   }
/*     */ 
/*     */   public void setScalingOption(boolean option)
/*     */   {
/* 102 */     this.scale = option;
/*     */   }
/*     */ 
/*     */   public void train(DocClassSet trainingDocSet)
/*     */   {
/* 109 */     if ((this.indexReader == null) && (this.sparseMatrix == null)) {
/* 110 */       return;
/*     */     }
/* 112 */     trainFeatureSelector(trainingDocSet);
/* 113 */     this.arrLabel = new String[trainingDocSet.getClassNum()];
/* 114 */     for (int i = 0; i < trainingDocSet.getClassNum(); i++)
/* 115 */       this.arrLabel[i] = trainingDocSet.getDocClass(i).getClassName();
/* 116 */     this.classNum = trainingDocSet.getClassNum();
/* 117 */     this.codeMatrix.setClassNum(this.classNum);
/* 118 */     svm_problem prob = getTrainingProblem(trainingDocSet);
/* 119 */     this.model = svm.svm_train(prob, this.param);
/*     */   }
/*     */ 
/*     */   public int classify(Row doc)
/*     */   {
/* 126 */     svm_node[] curDoc = readDoc(doc);
/* 127 */     if (curDoc == null)
/* 128 */       return -1;
/*     */     int label;
/* 129 */     if (this.classDecoder == null)
/*     */     {
/* 132 */       if (this.param.probability == 1) {
/* 133 */         if ((this.arrProb == null) || (this.arrProb.length != this.classNum))
/* 134 */           this.arrProb = new double[this.classNum];
/* 135 */          label = (int)svm.svm_predict_probability(this.model, curDoc, this.arrProb);
/* 136 */         this.rank = MathUtil.rankElementInArray(this.arrProb, true);
/*     */       }
/*     */       else {
/* 139 */         label = (int)svm.svm_predict(this.model, curDoc);
/*     */       }
/*     */     }
/*     */     else {
/* 143 */       if ((this.arrConfidence == null) || (this.arrConfidence.length != this.codeMatrix.getClassifierNum()))
/* 144 */         this.arrConfidence = new double[this.codeMatrix.getClassifierNum()];
/* 145 */       svm.svm_predict_values(this.model, curDoc, this.arrConfidence);
/* 146 */       label = this.classDecoder.decode(this.codeMatrix, this.arrConfidence);
/*     */     }
/* 148 */     return label;
/*     */   }
/*     */ 
/*     */   public int[] rank() {
/* 152 */     if (this.classDecoder == null) {
/* 153 */       return this.rank;
/*     */     }
/* 155 */     return this.classDecoder.rank();
/*     */   }
/*     */ 
/*     */   public void saveModel(String modelFile)
/*     */   {
/*     */     try
/*     */     {
/* 163 */       if (this.model == null)
/* 164 */         return;
/* 165 */       ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(modelFile));
/* 166 */       out.writeObject(this.model);
/* 167 */       out.writeObject(this.param);
/* 168 */       out.writeObject(this.codeMatrix);
/* 169 */       out.writeObject(this.classDecoder);
/* 170 */       out.writeInt(this.classNum);
/* 171 */       out.writeBoolean(this.scale);
/* 172 */       out.writeObject(this.featureSelector);
/* 173 */       for (int i = 0; i < this.classNum; i++)
/* 174 */         out.writeObject(getClassLabel(i));
/* 175 */       out.flush();
/* 176 */       out.close();
/*     */     }
/*     */     catch (Exception e) {
/* 179 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private svm_parameter getDefaultParameter()
/*     */   {
/* 186 */     svm_parameter param = new svm_parameter();
/* 187 */     param.svm_type = 0;
/* 188 */     param.kernel_type = 0;
/* 189 */     param.degree = 3;
/* 190 */     param.gamma = 0.0D;
/* 191 */     param.coef0 = 0.0D;
/* 192 */     param.nu = 0.5D;
/* 193 */     param.cache_size = 100.0D;
/* 194 */     param.C = 1.0D;
/* 195 */     param.eps = 0.001D;
/* 196 */     param.p = 0.1D;
/* 197 */     param.shrinking = 1;
/* 198 */     param.probability = 1;
/* 199 */     param.nr_weight = 0;
/* 200 */     param.weight_label = new int[0];
/* 201 */     param.weight = new double[0];
/* 202 */     return param;
/*     */   }
/*     */ 
/*     */   private svm_problem getTrainingProblem(DocClassSet trainingDocSet)
/*     */   {
/* 212 */     Vector vx = new Vector();
/* 213 */     Vector vy = new Vector();
/* 214 */     int maxIndex = 0;
/* 215 */     for (int i = 0; i < trainingDocSet.getClassNum(); i++) {
/* 216 */       DocClass curClass = trainingDocSet.getDocClass(i);
/* 217 */       for (int j = 0; j < curClass.getDocNum(); j++) {
/* 218 */         svm_node[] curDoc = readDoc(getRow(curClass.getDoc(j).getIndex()));
/* 219 */         if (curDoc != null) {
/* 220 */           vx.addElement(curDoc);
/* 221 */           vy.addElement(new Integer(curClass.getClassID()));
/* 222 */           maxIndex = Math.max(maxIndex, curDoc[(curDoc.length - 1)].index);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 227 */     svm_problem prob = new svm_problem();
/* 228 */     prob.l = vy.size();
/* 229 */     prob.x = new svm_node[prob.l][];
/* 230 */     for (int i = 0; i < prob.l; i++)
/* 231 */       prob.x[i] = ((svm_node[])vx.elementAt(i));
/* 232 */     prob.y = new double[prob.l];
/* 233 */     for (int i = 0; i < prob.l; i++) {
/* 234 */       prob.y[i] = ((Integer)vy.elementAt(i)).intValue();
/*     */     }
/* 236 */     if (this.param.gamma == 0.0D) {
/* 237 */       this.param.gamma = (1.0D / maxIndex);
/*     */     }
/* 239 */     return prob;
/*     */   }
/*     */ 
/*     */   protected svm_node[] readDoc(Row curDoc)
/*     */   {
/* 248 */     if (curDoc == null)
/* 249 */       return null;
/* 250 */     int num = 0;
/* 251 */     for (int j = 0; j < curDoc.getNonZeroNum(); j++)
/* 252 */       if (this.featureSelector.map(curDoc.getNonZeroColumn(j)) >= 0)
/* 253 */         num++;
/* 254 */     if (num == 0)
/* 255 */       return null;
/* 256 */     svm_node[] arrNode = new svm_node[num];
/* 257 */     num = 0;
/* 258 */     for (int j = 0; j < curDoc.getNonZeroNum(); j++) {
/* 259 */       int newIndex = this.featureSelector.map(curDoc.getNonZeroColumn(j));
/* 260 */       if (newIndex >= 0) {
/* 261 */         arrNode[num] = new svm_node();
/* 262 */         arrNode[num].index = newIndex;
/* 263 */         arrNode[num].value = curDoc.getNonZeroDoubleScore(j);
/* 264 */         num++;
/*     */       }
/*     */     }
/*     */ 
/* 268 */     if (this.scale) {
/* 269 */       double sum = 0.0D;
/* 270 */       for (int j = 0; j < num; j++) {
/* 271 */         sum += arrNode[j].value * arrNode[j].value;
/* 272 */         sum = Math.sqrt(sum);
/* 273 */         for (j = 0; j < num; j++)
/* 274 */           arrNode[j].value /= sum;
/*     */       }
/*     */     }
/* 277 */     return arrNode;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.classification.LibSVMClassifier
 * JD-Core Version:    0.6.2
 */