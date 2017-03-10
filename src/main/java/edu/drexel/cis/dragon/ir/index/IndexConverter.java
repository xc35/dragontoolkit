/*     */ package edu.drexel.cis.dragon.ir.index;
/*     */ 
/*     */ import edu.drexel.cis.dragon.matrix.DoubleGiantSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSuperSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.IntGiantSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.IntSparseMatrix;
/*     */ import edu.drexel.cis.dragon.matrix.SparseMatrix;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class IndexConverter
/*     */ {
/*     */   public void importIndex(String indexFolder, String doctermFile)
/*     */   {
/*     */     try
/*     */     {
/*  31 */       int sectionID = 0;
/*  32 */       ArrayList conceptList = new ArrayList(500);
/*  33 */       BasicIndexWriteController controller = new BasicIndexWriteController(indexFolder, false, false);
/*  34 */       controller.addSection(new IRSection(sectionID, "all"));
/*  35 */       controller.initialize();
/*  36 */       BufferedReader br = FileUtil.getTextReader(doctermFile);
/*     */       String line;
/*  37 */       while ((line = br.readLine()) != null)
/*     */       {
/*  38 */         String[] arrField = line.split("\t");
/*  39 */         String docKey = arrField[0];
/*  40 */         int featureNum = Integer.parseInt(arrField[1]);
/*  41 */         if (controller.setDoc(docKey))
/*     */         {
/*  44 */           conceptList.clear();
/*  45 */           for (int i = 0; i < featureNum; i++) {
/*  46 */             Token token = new Token(arrField[(2 + i * 2)]);
/*  47 */             token.setFrequency(Integer.parseInt(arrField[(3 + i * 2)]));
/*  48 */             conceptList.add(token);
/*     */           }
/*  50 */           controller.write(sectionID, conceptList);
/*     */         }
/*     */       }
/*  52 */       controller.close();
/*     */     }
/*     */     catch (Exception e) {
/*  55 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void importDocLinkage(String indexFolder, String doclinkFile, boolean outputTransposedMatrix)
/*     */   {
				String line;
/*     */     try
/*     */     {
/*  68 */       SimpleElementList docList = new SimpleElementList(indexFolder + "/dockey.list", false);
/*  69 */       DoubleSuperSparseMatrix matrix = new DoubleSuperSparseMatrix(indexFolder + "/doclinkage.index", indexFolder + "/doclinkage.matrix", false, false);
/*     */       DoubleSuperSparseMatrix matrixT;
/*  70 */       if (outputTransposedMatrix)
/*  71 */         matrixT = new DoubleSuperSparseMatrix(indexFolder + "/doclinkaget.index", 
/*  72 */           indexFolder + "/doclinkaget.matrix", false, false);
/*     */       else
/*  74 */         matrixT = null;
/*  75 */       BufferedReader br = FileUtil.getTextReader(doclinkFile);
/*     */       int num;
/*     */       int i;
 
					
		            while((line=br.readLine())!=null){
		            	String[]arrField=line.split("\t");
		                int src=docList.search(arrField[0]);
		                num=Integer.parseInt(arrField[1]);
		                if(src<0 || num==0)
		                    continue;
		                for(i=0;i<num;i++){
		                    int dest = docList.search(arrField[2+2*i]);
		                    if (dest < 0)
		                        continue;
		                    double weight = Double.parseDouble(arrField[3+2*i]);
		                    matrix.add(src, dest, weight);
		                    if (matrixT != null)
		                        matrixT.add(dest, src, weight);
		                }
		            }
/*     */ 
/*  92 */       docList.close();
/*  93 */       matrix.finalizeData(true);
/*  94 */       matrix.close();
/*  95 */       if (matrixT != null) {
/*  96 */         matrixT.finalizeData(true);
/*  97 */         matrixT.close();
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 101 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void exportIndex(String indexFolder, String contentFile) {
/* 106 */     exportIndex(indexFolder, "all", contentFile);
/*     */   }
/*     */ 
/*     */   public void exportIndex(String indexFolder, String section, String contentFile)
/*     */   {
/* 113 */     SimpleElementList docList = new SimpleElementList(indexFolder + "/dockey.list", false);
/* 114 */     SimpleElementList termList = new SimpleElementList(indexFolder + "/termkey.list", false);
/* 115 */     IntGiantSparseMatrix matrix = new IntGiantSparseMatrix(indexFolder + "/all/docterm.index", indexFolder + "/all/docterm.matrix");
/* 116 */     exportMatrix(docList, docList, matrix, contentFile);
/* 117 */     docList.close();
/* 118 */     termList.close();
/* 119 */     matrix.close();
/*     */   }
/*     */ 
/*     */   public void exportDocLinkage(String indexFolder, String docRelationFile)
/*     */   {
/* 126 */     SimpleElementList docList = new SimpleElementList(indexFolder + "/dockey.list", false);
/* 127 */     DoubleGiantSparseMatrix matrix = new DoubleGiantSparseMatrix(indexFolder + "/doclinkage.index", indexFolder + "/doclinkage.matrix");
/* 128 */     exportMatrix(docList, docList, matrix, docRelationFile);
/* 129 */     docList.close();
/* 130 */     matrix.close();
/*     */   }
/*     */ 
/*     */   public void exportMatrix(SimpleElementList rowList, SimpleElementList colList, DoubleSparseMatrix matrix, String outputFile) {
/* 134 */     exportMatrix(rowList, colList, matrix, false, outputFile);
/*     */   }
/*     */ 
/*     */   public void exportMatrix(SimpleElementList rowList, SimpleElementList colList, IntSparseMatrix matrix, String outputFile) {
/* 138 */     exportMatrix(rowList, colList, matrix, true, outputFile);
/*     */   }
/*     */ 
/*     */   public void exportMatrix(SimpleElementList rowList, SimpleElementList colList, SparseMatrix matrix, boolean exportAsInteger, String outputFile)
/*     */   {
/* 147 */     PrintWriter out = FileUtil.getPrintWriter(outputFile);
/* 148 */     int[] arrIntWeight = (int[])null;
/* 149 */     double[] arrDblWeight = (double[])null;
/*     */ 
/* 151 */     for (int i = 0; i < matrix.rows(); i++) {
/* 152 */       out.print(rowList.search(i));
/* 153 */       out.print('\t');
/* 154 */       int[] arrIndex = matrix.getNonZeroColumnsInRow(i);
/* 155 */       if (exportAsInteger)
/* 156 */         arrIntWeight = matrix.getNonZeroIntScoresInRow(i);
/*     */       else
/* 158 */         arrDblWeight = matrix.getNonZeroDoubleScoresInRow(i);
/*     */       int termNum;
/* 159 */       if (arrIndex == null)
/* 160 */         termNum = 0;
/*     */       else
/* 162 */         termNum = arrIndex.length;
/* 163 */       out.print(termNum);
/* 164 */       for (int j = 0; j < termNum; j++) {
/* 165 */         out.print('\t');
/* 166 */         out.print(colList.search(arrIndex[j]));
/* 167 */         out.print('\t');
/* 168 */         if (exportAsInteger)
/* 169 */           out.print(arrIntWeight[j]);
/*     */         else
/* 171 */           out.print(arrDblWeight[j]);
/*     */       }
/* 173 */       out.print('\n');
/* 174 */       out.flush();
/*     */     }
/* 176 */     out.close();
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.IndexConverter
 * JD-Core Version:    0.6.2
 */