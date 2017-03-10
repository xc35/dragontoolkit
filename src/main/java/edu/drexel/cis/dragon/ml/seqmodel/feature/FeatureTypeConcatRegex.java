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
/*     */ public class FeatureTypeConcatRegex extends AbstractFeatureType
/*     */ {
/*  55 */   private String[][] patternString = { 
/*  56 */     { "isWord", "[a-zA-Z][a-zA-Z]+" }, 
/*  57 */     { "singleCapLetterWithDot", "[A-Z]\\." }, 
/*  58 */     { "singleCapLetter", "[A-Z]" }, 
/*  59 */     { "isDigits", "\\d+" }, 
/*  60 */     { "singleDot", "[.]" }, 
/*  61 */     { "singleComma", "[,]" }, 
/*  62 */     { "isSpecialCharacter", "[#;:\\-/<>'\"()&]" }, 
/*  63 */     { "containsSpecialCharacters", ".*[#;:\\-/<>'\"()&].*" }, 
/*  64 */     { "isInitCapital", "[A-Z][a-z]+" }, 
/*  65 */     { "isAllCapital", "[A-Z]+" }, 
/*  66 */     { "isAllSmallCase", "[a-z]+" }, 
/*  67 */     { "isAlpha", "[a-zA-Z]+" }, 
/*  68 */     { "isAlphaNumeric", "[a-zA-Z0-9]+" }, 
/*  69 */     { "endsWithDot", "\\p{Alnum}+\\." }, 
/*  70 */     { "endsWithComma", "\\w+[,]" }, 
/*  71 */     { "endsWithPunctuation", "\\w+[;:,.?!]" }, 
/*  72 */     { "singlePunctuation", "\\p{Punct}" }, 
/*  73 */     { "singleAmp", "[&]" }, 
/*  74 */     { "containsDigit", ".*\\d+.*" }, 
/*  75 */     { "singleDigit", "\\s*\\d\\s*" }, 
/*  76 */     { "twoDigits", "\\s*\\d{2}\\s*" }, 
/*  77 */     { "threeDigits", "\\s*\\d{3}\\s*" }, 
/*  78 */     { "fourDigits", "\\s*\\(*\\d{4}\\)*\\s*" }, 
/*  79 */     { "isNumberRange", "\\d+\\s*([-]{1,2}\\s*\\d+)?" }, 
/*  80 */     { "isDashSeparatedWords", "(\\w[-])+\\w" }, 
/*  81 */     { "isDashSeparatedSeq", "((\\p{Alpha}+|\\p{Digit}+)[-])+(\\p{Alpha}+|\\p{Digit}+)" }, 
/*  82 */     { "isURL", "\\p{Alpha}+://(\\w+\\.)\\w+(:(\\d{2}|\\d{4}))?(/\\w+)*(/|(/\\w+\\.\\w+))?" }, 
/*  83 */     { "isEmailId", "\\w+@(\\w+\\.)+\\w+" }, 
/*  84 */     { "containsDashes", ".*--.*" } };
/*     */   private Pattern[] p;
/*     */   protected transient DataSequence data;
/*     */   protected int index;
/*     */   protected int idbase;
/*     */   protected int curId;
/*     */   protected int window;
/*     */   protected int relSegmentStart;
/*     */   protected int relSegmentEnd;
/*     */   protected int maxSegmentLength;
/*     */   protected int left;
/*     */   protected int right;
/*     */ 
/*     */   public FeatureTypeConcatRegex(int relSegmentStart, int relSegmentEnd, int maxSegmentLength, String patternFile)
/*     */   {
/* 112 */     super(false);
/* 113 */     this.relSegmentStart = relSegmentStart;
/* 114 */     this.relSegmentEnd = relSegmentEnd;
/* 115 */     this.maxSegmentLength = maxSegmentLength;
/*     */ 
/* 117 */     this.window = getWindowSize(relSegmentStart, relSegmentEnd);
/* 118 */     this.idbase = ((int)Math.pow(2.0D, this.window));
/* 119 */     this.patternString = getPatterns(patternFile);
/* 120 */     this.p = new Pattern[this.patternString.length];
/* 121 */     for (int i = 0; i < this.patternString.length; i++)
/* 122 */       this.p[i] = Pattern.compile(this.patternString[i][1]);
/*     */   }
/*     */ 
/*     */   public FeatureTypeConcatRegex(int relSegmentStart, int relSegmentEnd, int maxSegmentLength, String[][] patternString)
/*     */   {
/* 127 */     super(false);
/* 128 */     this.relSegmentStart = relSegmentStart;
/* 129 */     this.relSegmentEnd = relSegmentEnd;
/* 130 */     this.maxSegmentLength = maxSegmentLength;
/* 131 */     this.window = getWindowSize(relSegmentStart, relSegmentEnd);
/* 132 */     this.idbase = ((int)Math.pow(2.0D, this.window));
/* 133 */     this.patternString = patternString;
/* 134 */     this.p = new Pattern[patternString.length];
/* 135 */     for (int i = 0; i < patternString.length; i++)
/* 136 */       this.p[i] = Pattern.compile(patternString[i][1]);
/*     */   }
/*     */ 
/*     */   public FeatureTypeConcatRegex(int relSegmentStart, int relSegmentEnd, int maxSegmentLength)
/*     */   {
/* 141 */     super(false);
/* 142 */     this.relSegmentStart = relSegmentStart;
/* 143 */     this.relSegmentEnd = relSegmentEnd;
/* 144 */     this.maxSegmentLength = maxSegmentLength;
/* 145 */     this.window = getWindowSize(relSegmentStart, relSegmentEnd);
/* 146 */     this.idbase = ((int)Math.pow(2.0D, this.window));
/* 147 */     this.p = new Pattern[this.patternString.length];
/* 148 */     for (int i = 0; i < this.patternString.length; i++)
/* 149 */       this.p[i] = Pattern.compile(this.patternString[i][1]);
/*     */   }
/*     */ 
/*     */   public FeatureTypeConcatRegex(int relSegmentStart, int relSegmentEnd)
/*     */   {
/* 154 */     this(relSegmentStart, relSegmentEnd, 1);
/*     */   }
/*     */ 
/*     */   public FeatureTypeConcatRegex(int relSegmentStart, int relSegmentEnd, String patternFile) {
/* 158 */     this(relSegmentStart, relSegmentEnd, 1, patternFile);
/*     */   }
/*     */ 
/*     */   private int getWindowSize(int relSegmentStart, int relSegmentEnd) {
/* 162 */     if ((sign(relSegmentEnd) == sign(relSegmentStart)) && (relSegmentStart != 0)) {
/* 163 */       return relSegmentEnd - relSegmentStart + 1;
/*     */     }
/* 165 */     return relSegmentEnd - relSegmentStart + this.maxSegmentLength;
/*     */   }
/*     */ 
/*     */   private int sign(int boundary) {
/* 169 */     if (boundary == 0)
/* 170 */       return 0;
/* 171 */     if (boundary < 0) {
/* 172 */       return -1;
/*     */     }
/* 174 */     return 1;
/*     */   }
/*     */ 
/*     */   public boolean startScanFeaturesAt(DataSequence data, int startPos, int endPos) {
/* 178 */     this.data = data;
/* 179 */     this.index = 0;
/* 180 */     if (this.relSegmentStart <= 0)
/* 181 */       this.left = (startPos + this.relSegmentStart);
/*     */     else {
/* 183 */       this.left = (endPos + this.relSegmentStart);
/*     */     }
/*     */ 
/* 186 */     if (this.relSegmentEnd < 0)
/* 187 */       this.right = (startPos + this.relSegmentEnd);
/*     */     else {
/* 189 */       this.right = (endPos + this.relSegmentEnd);
/*     */     }
/*     */ 
/* 192 */     if ((this.left < 0) || (this.left >= data.length()) || (this.right < 0) || (this.right >= data.length()))
/* 193 */       this.index = this.patternString.length;
/* 194 */     advance();
/* 195 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean hasNext() {
/* 199 */     return this.index < this.patternString.length;
/*     */   }
/*     */ 
/*     */   public Feature next()
/*     */   {
/* 208 */     int curState = -1;
/* 209 */     String name = this.patternString[this.index][0] + "_" + this.window + "_" + Integer.toBinaryString(this.curId);
/* 210 */     FeatureIdentifier id = new FeatureIdentifier(name, this.curId + this.idbase * this.index++, curState);
/* 211 */     Feature f = new BasicFeature(id, curState, 1.0D);
/* 212 */     advance();
/* 213 */     return f;
/*     */   }
/*     */ 
/*     */   private void advance()
/*     */   {
/* 220 */     this.curId = 0;
/* 221 */     while ((this.curId <= 0) && (this.index < this.patternString.length)) {
/* 222 */       int base = 1;
/* 223 */       for (int k = this.left; k <= this.right; k++) {
/* 224 */         boolean match = this.p[this.index].matcher(this.data.getToken(k).getContent()).matches();
/* 225 */         this.curId += base * (match ? 1 : 0);
/* 226 */         base *= 2;
/*     */       }
/* 228 */       if (this.curId > 0)
/*     */         break;
/* 230 */       this.index += 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   private String[][] getPatterns(String patternFile)
/*     */   {
/*     */     try
/*     */     {
/* 245 */       BufferedReader in = new BufferedReader(new FileReader(patternFile));
/* 246 */       int len = Integer.parseInt(in.readLine());
/* 247 */       String[][] patterns = new String[len][2];
/*     */ 
/* 249 */       for (int k = 0; k < len; k++) {
/* 250 */         StringTokenizer strTokenizer = new StringTokenizer(in.readLine());
/* 251 */         patterns[k][0] = strTokenizer.nextToken();
/* 252 */         patterns[k][1] = strTokenizer.nextToken();
/*     */       }
/* 254 */       return patterns;
/*     */     }
/*     */     catch (IOException ioe) {
/* 257 */       System.err.println("Could not read pattern file : " + patternFile);
/* 258 */       ioe.printStackTrace();
/* 259 */     }return null;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.FeatureTypeConcatRegex
 * JD-Core Version:    0.6.2
 */