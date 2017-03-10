/*     */ package edu.drexel.cis.dragon.ml.seqmodel.data;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public class FlatSegmentReader
/*     */   implements DataReader
/*     */ {
/*     */   private int originalLabelNum;
/*     */   private int markovOrder;
/*     */   private String delimit;
/*     */   private String tagDelimit;
/*     */   private String impDelimit;
/*     */   private BufferedReader tin;
/*     */   private LabelConverter labelConverter;
/*     */   private int[] labels;
/*     */   boolean fixedColFormat;
/*     */   boolean tagged;
/*     */ 
/*     */   public FlatSegmentReader(int originalLabelNum, int markovOrder, String taggedFile, LabelConverter labelConverter)
/*     */   {
/*  28 */     this.originalLabelNum = originalLabelNum;
/*  29 */     this.markovOrder = markovOrder;
/*  30 */     this.tin = FileUtil.getTextReader(taggedFile);
/*  31 */     this.labelConverter = labelConverter;
/*  32 */     this.delimit = ",\t/ -():.;'?\\#`&\"_";
/*  33 */     this.tagDelimit = "|";
/*  34 */     this.impDelimit = ",";
/*     */ 
/*  36 */     this.labels = readHeaderInfo(this.tin);
/*  37 */     if (this.labels != null)
/*  38 */       this.fixedColFormat = true;
/*     */     else
/*  40 */       this.fixedColFormat = false;
/*  41 */     this.tagged = true;
/*     */   }
/*     */ 
/*     */   public FlatSegmentReader(int originalLabelNum, int markovOrder, String rawFile) {
/*  45 */     this.originalLabelNum = originalLabelNum;
/*  46 */     this.markovOrder = markovOrder;
/*  47 */     this.tin = FileUtil.getTextReader(rawFile);
/*  48 */     this.labelConverter = null;
/*  49 */     this.delimit = " \t";
/*  50 */     this.tagDelimit = "|";
/*  51 */     this.impDelimit = "";
/*  52 */     this.tagged = false;
/*  53 */     this.fixedColFormat = false;
/*     */   }
/*     */ 
/*     */   public Dataset read()
/*     */   {
/*  60 */     BasicDataset dataset = new BasicDataset(this.originalLabelNum, this.markovOrder);
/*     */     while (true) {
/*  62 */       DataSequence dataSeq = readRow();
/*  63 */       if ((dataSeq == null) || (dataSeq.length() == 0)) {
/*     */         break;
/*     */       }
/*  66 */       dataset.add(dataSeq);
/*     */     }
/*     */     DataSequence dataSeq;
/*  68 */     return dataset;
/*     */   }
/*     */ 
/*     */   public DataSequence readRow() {
/*     */     try {
/*  73 */       if (this.tagged) {
/*  74 */         if (this.fixedColFormat) {
/*  75 */           return readRowFixedCol(this.tin, this.labels);
/*     */         }
/*  77 */         return readRowVarCol(this.tin);
/*     */       }
/*  79 */       return readRaw();
/*     */     }
/*     */     catch (Exception e) {
/*  82 */       e.printStackTrace();
/*  83 */     }return null;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*     */     try {
/*  89 */       this.tin.close();
/*     */     }
/*     */     catch (Exception e) {
/*  92 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private String[] getTokenList(String text)
/*     */   {
/* 101 */     StringTokenizer textTok = new StringTokenizer(text.toLowerCase(), this.delimit, true);
/* 102 */     int tlen = 0;
/* 103 */     while (textTok.hasMoreTokens()) {
/* 104 */       String tokStr = textTok.nextToken();
/* 105 */       if ((this.delimit.indexOf(tokStr) == -1) || (this.impDelimit.indexOf(tokStr) != -1)) {
/* 106 */         tlen++;
/*     */       }
/*     */     }
/* 109 */     String[] cArray = new String[tlen];
/* 110 */     tlen = 0;
/* 111 */     textTok = new StringTokenizer(text.toLowerCase(), this.delimit, true);
/* 112 */     while (textTok.hasMoreTokens()) {
/* 113 */       String tokStr = textTok.nextToken();
/* 114 */       if ((this.delimit.indexOf(tokStr) == -1) || (this.impDelimit.indexOf(tokStr) != -1)) {
/* 115 */         cArray[(tlen++)] = tokStr;
/*     */       }
/*     */     }
/* 118 */     return cArray;
/*     */   }
/*     */ 
/*     */   private DataSequence readRowVarCol(BufferedReader tin)
/*     */     throws IOException
/*     */   {
/* 128 */     BasicDataSequence dataSeq = new BasicDataSequence();
/*     */     while (true) {
/* 130 */       String line = tin.readLine();
/* 131 */       StringTokenizer firstSplit = null;
/* 132 */       if (line != null) {
/* 133 */         firstSplit = new StringTokenizer(line.toLowerCase(), this.tagDelimit);
/*     */       }
/* 135 */       if ((line == null) || (firstSplit.countTokens() < 2))
/*     */       {
/* 137 */         return dataSeq;
/*     */       }
/* 139 */       String w = firstSplit.nextToken();
/*     */       int label;
 
/* 140 */       if (this.labelConverter != null)
/* 141 */         label = this.labelConverter.getInternalLabel(firstSplit.nextToken());
/*     */       else
/* 143 */         label = Integer.parseInt(firstSplit.nextToken());
/* 144 */       String[] arrToken = getTokenList(w);
/* 145 */       for (int i = 0; i < arrToken.length; i++) {
/* 146 */         BasicToken token = new BasicToken(arrToken[i], label);
/* 147 */         if (i == 0)
/* 148 */           token.setSegmentMarker(true);
/*     */         else
/* 150 */           token.setSegmentMarker(false);
/* 151 */         dataSeq.add(token);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private DataSequence readRowFixedCol(BufferedReader tin, int[] labels)
/*     */     throws IOException
/*     */   {
/* 163 */     String line = tin.readLine();
/* 164 */     if (line == null) {
/* 165 */       return null;
/*     */     }
/* 167 */     BasicDataSequence dataSeq = new BasicDataSequence();
/* 168 */     StringTokenizer firstSplit = new StringTokenizer(line.toLowerCase(), this.tagDelimit, true);
/* 169 */     for (int i = 0; (i < labels.length) && (firstSplit.hasMoreTokens()); i++)
/*     */     {
/*     */       int label;
 
/* 170 */       if (this.labelConverter != null)
/* 171 */         label = this.labelConverter.getInternalLabel(labels[i]);
/*     */       else
/* 173 */         label = labels[i];
/* 174 */       String w = firstSplit.nextToken();
/* 175 */       if (this.tagDelimit.indexOf(w) == -1)
/*     */       {
/* 179 */         if (firstSplit.hasMoreTokens())
/*     */         {
/* 181 */           firstSplit.nextToken();
/*     */         }
/*     */ 
/* 184 */         if ((label >= 0) && (label < this.originalLabelNum)) {
/* 185 */           String[] arrToken = getTokenList(w);
/* 186 */           for (i = 0; i < arrToken.length; i++) {
/* 187 */             BasicToken token = new BasicToken(arrToken[i], label);
/* 188 */             if (i == 0)
/* 189 */               token.setSegmentMarker(true);
/*     */             else
/* 191 */               token.setSegmentMarker(false);
/* 192 */             dataSeq.add(token);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 196 */     return dataSeq;
/*     */   }
/*     */ 
/*     */   private int[] readHeaderInfo(BufferedReader tin)
/*     */   {
/*     */     try
/*     */     {
/* 205 */       tin.mark(1000);
/* 206 */       String line = tin.readLine();
/* 207 */       if (line == null)
/* 208 */         return null;
/* 209 */       if (!line.toLowerCase().startsWith("fixed-column-format")) {
/* 210 */         tin.reset();
/* 211 */         return null;
/*     */       }
/*     */ 
/* 214 */       line = tin.readLine();
/* 215 */       StringTokenizer firstSplit = new StringTokenizer(line, this.tagDelimit);
/* 216 */       int[] labels = new int[this.originalLabelNum];
/* 217 */       for (int i = 0; (i < this.originalLabelNum) && (firstSplit.hasMoreTokens()); ) {
/* 218 */         labels[(i++)] = Integer.parseInt(firstSplit.nextToken());
/*     */       }
/* 220 */       return labels;
/*     */     }
/*     */     catch (Exception e) {
/* 223 */       e.printStackTrace();
/* 224 */     }return null;
/*     */   }
/*     */ 
/*     */   private DataSequence readRaw()
/*     */     throws IOException
/*     */   {
/* 233 */     String line = this.tin.readLine();
/* 234 */     BasicDataSequence dataSeq = new BasicDataSequence();
/* 235 */     StringTokenizer tok = new StringTokenizer(line.toLowerCase(), this.delimit, true);
/* 236 */     while (tok.hasMoreTokens()) {
/* 237 */       String tokStr = tok.nextToken();
/* 238 */       if ((this.delimit.indexOf(tokStr) == -1) || (this.impDelimit.indexOf(tokStr) != -1)) {
/* 239 */         dataSeq.add(new BasicToken(tokStr));
/*     */       }
/*     */     }
/* 242 */     return dataSeq;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.data.FlatSegmentReader
 * JD-Core Version:    0.6.2
 */