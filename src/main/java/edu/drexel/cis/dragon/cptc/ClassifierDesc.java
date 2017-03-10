/*     */ package edu.drexel.cis.dragon.cptc;
/*     */ 
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSuperSparseMatrix;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.onlinedb.BasicCollectionReader;
/*     */ import edu.drexel.cis.dragon.onlinedb.BasicCollectionWriter;
/*     */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*     */ import edu.drexel.cis.dragon.onlinedb.CollectionWriter;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ 
/*     */ public class ClassifierDesc
/*     */ {
/*     */   private String workDir;
/*     */   private SortedArray classList;
/*     */ 
/*     */   public ClassifierDesc(String workDir)
/*     */   {
/*  17 */     this.workDir = workDir;
/*  18 */     this.classList = new SortedArray();
/*  19 */     if (FileUtil.exist(workDir + "/classifierdesc.txt")) {
/*  20 */       String content = FileUtil.readTextFile(workDir + "/classifierdesc.txt");
/*  21 */       int start = content.indexOf("<class>");
/*  22 */       while (start >= 0) {
/*  23 */         int end = content.indexOf("</class>", start) + 8;
/*  24 */         this.classList.add(ClassDesc.load(content.substring(start, end)));
/*  25 */         start = content.indexOf("<class>", end);
/*     */       }
/*     */     }
/*     */ 
/*  29 */     addNewConceptFromFile(workDir + "/concepts.txt");
/*     */   }
/*     */ 
/*     */   public void addNewConceptFromFile(String filename)
/*     */   {
/*     */     try
/*     */     {
/*  38 */       if (!FileUtil.exist(filename))
/*  39 */         return;
/*  40 */       String content = FileUtil.readTextFile(filename);
/*  41 */       content = content.replaceAll("\r\n", "\n");
/*  42 */       int start = content.indexOf("<category ");
/*  43 */       while (start >= 0) {
/*  44 */         int end = content.indexOf("</category>", start);
/*  45 */         String segment = content.substring(start, end);
/*  46 */         start = content.indexOf("<category ", end);
/*     */ 
/*  49 */         int m = segment.indexOf('"');
/*  50 */         int n = segment.indexOf('"', m + 1);
/*  51 */         String category = segment.substring(m + 1, n);
/*  52 */         ClassDesc curClass = getClass(category);
/*  53 */         if (curClass == null) {
/*  54 */           curClass = new ClassDesc(category);
/*  55 */           addClass(curClass);
/*     */         }
/*  57 */         m = segment.indexOf('>', n);
/*  58 */         segment = segment.substring(m + 1).trim();
/*  59 */         if (segment.length() >= 2)
/*     */         {
/*  62 */           if (segment.charAt(0) == '\n')
/*  63 */             segment = segment.substring(1);
/*  64 */           if (segment.charAt(segment.length() - 1) == '\n')
/*  65 */             segment = segment.substring(0, segment.length() - 1).trim();
/*  66 */           if (segment.length() != 0)
/*     */           {
/*  68 */             String[] arrConcept = segment.split("\n");
/*  69 */             for (m = 0; m < arrConcept.length; m++)
/*  70 */               curClass.addConcept(new ConceptDesc(arrConcept[m])); 
/*     */           }
/*     */         }
/*     */       }
/*     */     } catch (Exception e) { e.printStackTrace(); }
/*     */   }
/*     */ 
/*     */   public int getClassNum()
/*     */   {
/*  79 */     return this.classList.size();
/*     */   }
/*     */ 
/*     */   public ClassDesc getClass(int index) {
/*  83 */     return (ClassDesc)this.classList.get(index);
/*     */   }
/*     */ 
/*     */   public ClassDesc getClass(String className)
/*     */   {
/*  89 */     int pos = this.classList.binarySearch(new ClassDesc(className));
/*  90 */     if (pos >= 0) {
/*  91 */       return getClass(pos);
/*     */     }
/*  93 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean addClass(ClassDesc classDesc) {
/*  97 */     return this.classList.add(classDesc);
/*     */   }
/*     */ 
/*     */   public void save()
/*     */   {
/*     */     try
/*     */     {
/* 105 */       BufferedWriter bw = FileUtil.getTextWriter(this.workDir + "/classifierdesc.txt");
/* 106 */       bw.write("<classifier>\n");
/* 107 */       for (int i = 0; i < this.classList.size(); i++)
/* 108 */         bw.write(getClass(i).toString());
/* 109 */       bw.write("</classifier>\n");
/* 110 */       bw.close();
/*     */     }
/*     */     catch (Exception e) {
/* 113 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public CollectionWriter getCollectionWriter() {
/* 118 */     return new BasicCollectionWriter(this.workDir + "/trainingdoc.collection", this.workDir + "/trainingdoc.index", true);
/*     */   }
/*     */ 
/*     */   public CollectionWriter getCollectionWriter2() {
/* 122 */     return new BasicCollectionWriter(this.workDir + "/trainingdoc2.collection", this.workDir + "/trainingdoc2.index", true);
/*     */   }
/*     */ 
/*     */   public CollectionReader getCollectionReader() {
/* 126 */     return new BasicCollectionReader(this.workDir + "/trainingdoc.collection", this.workDir + "/trainingdoc.index");
/*     */   }
/*     */ 
/*     */   public CollectionReader getCollectionReader2() {
/* 130 */     return new BasicCollectionReader(this.workDir + "/trainingdoc2.collection", this.workDir + "/trainingdoc2.index");
/*     */   }
/*     */ 
/*     */   public String getIndexFolder()
/*     */   {
/* 135 */     return this.workDir + "/tokeindexer";
/*     */   }
/*     */ 
/*     */   public String getIndexFolder2() {
/* 139 */     return this.workDir + "/tokeindexer2";
/*     */   }
/*     */ 
/*     */   public String getEvaluationResultFile() {
/* 143 */     return this.workDir + "/exp.txt";
/*     */   }
/*     */ 
/*     */   public DoubleSparseMatrix getConceptMatrix(boolean writable) {
/* 147 */     if (writable) {
/* 148 */       return new DoubleSuperSparseMatrix(this.workDir + "/concept.index", this.workDir + "/concept.matrix", false, false);
/*     */     }
/* 150 */     return new DoubleSuperSparseMatrix(this.workDir + "/concept.index", this.workDir + "/concept.matrix");
/*     */   }
/*     */ 
/*     */   public SimpleElementList getConceptList(boolean writable) {
/* 154 */     return new SimpleElementList(this.workDir + "/conceptkey.list", writable);
/*     */   }
/*     */ 
/*     */   public SimpleElementList getWordList(boolean writable) {
/* 158 */     return new SimpleElementList(this.workDir + "/wordkey.list", writable);
/*     */   }
/*     */ 
/*     */   public String getConceptListFilename() {
/* 162 */     return this.workDir + "/conceptkey.list";
/*     */   }
/*     */ 
/*     */   public String getWordListFilename() {
/* 166 */     return this.workDir + "/wordkey.list";
/*     */   }
/*     */ 
/*     */   public String getWordIDFFilename() {
/* 170 */     return this.workDir + "/wordidf.bin";
/*     */   }
/*     */ 
/*     */   public String getAnswerKeyFilename() {
/* 174 */     return this.workDir + "/answerkey.list";
/*     */   }
/*     */ 
/*     */   public String getModelFilename() {
/* 178 */     return this.workDir + "/model.bin";
/*     */   }
/*     */ 
/*     */   public String getHistoryFilename() {
/* 182 */     return this.workDir + "/history.list";
/*     */   }
/*     */ 
/*     */   public void cleanConceptFiles()
/*     */   {
/* 188 */     File file = new File(getConceptListFilename());
/* 189 */     if (file.exists())
/* 190 */       file.delete();
/* 191 */     file = new File(getAnswerKeyFilename());
/* 192 */     if (file.exists())
/* 193 */       file.delete();
/* 194 */     file = new File(getWordListFilename());
/* 195 */     if (file.exists())
/* 196 */       file.delete();
/* 197 */     file = new File(getWordIDFFilename());
/* 198 */     if (file.exists())
/* 199 */       file.delete();
/* 200 */     file = new File(this.workDir + "/concept.index");
/* 201 */     if (file.exists())
/* 202 */       file.delete();
/* 203 */     file = new File(this.workDir + "/concept.matrix");
/* 204 */     if (file.exists())
/* 205 */       file.delete();
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.cptc.ClassifierDesc
 * JD-Core Version:    0.6.2
 */