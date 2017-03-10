/*     */ package edu.drexel.cis.dragon.ml.seqmodel.feature;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ml.seqmodel.data.BasicToken;
/*     */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class FeatureTypeRegexCount extends AbstractFeatureType
/*     */ {
/*  14 */   private String[][] patternString = { 
/*  15 */     { "isInitCapitalWord", "[A-Z][a-z]+" }, 
/*  16 */     { "isAllCapitalWord", "[A-Z][A-Z]+" }, 
/*  17 */     { "isAllSmallCase", "[a-z]+" }, 
/*  18 */     { "isWord", "[a-zA-Z][a-zA-Z]+" }, 
/*  19 */     { "isAlphaNumeric", "[a-zA-Z0-9]+" }, 
/*  20 */     { "singleCapLetter", "[A-Z]" }, 
/*  21 */     { "isSpecialCharacter", "[#;:\\-/<>'\"()&]" }, 
/*  22 */     { "singlePunctuation", "\\p{Punct}" }, 
/*  23 */     { "singleDot", "[.]" }, 
/*  24 */     { "singleComma", "[,]" }, 
/*  25 */     { "containsDigit", ".*\\d+.*" }, 
/*  26 */     { "isDigits", "\\d+" } };
/*     */   private Pattern[] p;
/*     */   private int[] patternOccurence;
/*     */   private int index;
/*     */   private int maxSegmentLength;
/*     */ 
/*     */   public FeatureTypeRegexCount(int maxSegmentLength, String patternFile)
/*     */   {
/*  32 */     super(false);
/*  33 */     this.maxSegmentLength = maxSegmentLength;
/*  34 */     this.patternString = getPatterns(patternFile);
/*  35 */     this.p = new Pattern[this.patternString.length];
/*  36 */     for (int i = 0; i < this.patternString.length; i++) {
/*  37 */       this.p[i] = Pattern.compile(this.patternString[i][1]);
/*     */     }
/*  39 */     this.patternOccurence = new int[this.patternString.length];
/*     */   }
/*     */ 
/*     */   public FeatureTypeRegexCount(int maxSegmentLength, String[][] patternString) {
/*  43 */     super(false);
/*  44 */     this.maxSegmentLength = maxSegmentLength;
/*  45 */     this.patternString = patternString;
/*  46 */     this.p = new Pattern[patternString.length];
/*  47 */     for (int i = 0; i < patternString.length; i++) {
/*  48 */       this.p[i] = Pattern.compile(patternString[i][1]);
/*     */     }
/*  50 */     this.patternOccurence = new int[patternString.length];
/*     */   }
/*     */ 
/*     */   public FeatureTypeRegexCount(int maxSegmentLength) {
/*  54 */     super(false);
/*  55 */     this.maxSegmentLength = maxSegmentLength;
/*  56 */     this.p = new Pattern[this.patternString.length];
/*  57 */     for (int i = 0; i < this.patternString.length; i++) {
/*  58 */       this.p[i] = Pattern.compile(this.patternString[i][1]);
/*     */     }
/*  60 */     this.patternOccurence = new int[this.patternString.length];
/*     */   }
/*     */ 
/*     */   public boolean startScanFeaturesAt(DataSequence data, int pos) {
/*  64 */     return startScanFeaturesAt(data, pos, pos);
/*     */   }
/*     */ 
/*     */   public boolean startScanFeaturesAt(DataSequence data, int startPos, int endPos)
/*     */   {
/*  70 */     for (int j = 0; j < this.patternOccurence.length; j++) {
/*  71 */       this.patternOccurence[j] = 0;
/*     */     }
/*  73 */     for (int i = startPos; i <= endPos; i++) {
/*  74 */       for (int j = 0; j < this.p.length; j++) {
/*  75 */         if (this.p[j].matcher(data.getToken(i).getContent()).matches()) {
/*  76 */           this.patternOccurence[j] += 1;
/*     */         }
/*     */       }
/*     */     }
/*  80 */     this.index = -1;
/*  81 */     return advance();
/*     */   }
/*     */ 
/*     */   private boolean advance() {
/*  85 */     while ((++this.index < this.patternOccurence.length) && (this.patternOccurence[this.index] <= 0));
/*  86 */     return this.index < this.patternOccurence.length;
/*     */   }
/*     */ 
/*     */   public boolean hasNext() {
/*  90 */     return this.index < this.patternOccurence.length;
/*     */   }
/*     */ 
/*     */   public Feature next()
/*     */   {
/*  99 */     int curLabel = -1;
/* 100 */     this.patternOccurence[this.index] = Math.min(this.maxSegmentLength, this.patternOccurence[this.index]);
/* 101 */     String name = this.patternString[this.index][0] + "_Count_" + this.patternOccurence[this.index];
/* 102 */     FeatureIdentifier id = new FeatureIdentifier(name, this.maxSegmentLength * (this.index + 1) + this.patternOccurence[this.index], curLabel);
/* 103 */     BasicFeature f = new BasicFeature(id, curLabel, 1.0D);
/* 104 */     advance();
/* 105 */     return f;
/*     */   }
/*     */ 
/*     */   private String[][] getPatterns(String patternFile)
/*     */   {
/*     */     try
/*     */     {
/* 119 */       BufferedReader in = new BufferedReader(new FileReader(patternFile));
/* 120 */       int len = Integer.parseInt(in.readLine());
/* 121 */       String[][] patterns = new String[len][2];
/*     */ 
/* 123 */       for (int k = 0; k < len; k++) {
/* 124 */         StringTokenizer strTokenizer = new StringTokenizer(in.readLine());
/* 125 */         patterns[k][0] = strTokenizer.nextToken();
/* 126 */         patterns[k][1] = strTokenizer.nextToken();
/*     */       }
/* 128 */       return patterns;
/*     */     }
/*     */     catch (IOException ioe) {
/* 131 */       System.err.println("Could not read pattern file : " + patternFile);
/* 132 */       ioe.printStackTrace();
/* 133 */     }return null;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.FeatureTypeRegexCount
 * JD-Core Version:    0.6.2
 */