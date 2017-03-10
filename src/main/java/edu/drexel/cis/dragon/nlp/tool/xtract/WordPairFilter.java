/*     */ package edu.drexel.cis.dragon.nlp.tool.xtract;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class WordPairFilter
/*     */ {
/*     */   private String workDir;
/*     */   private int maxSpan;
/*     */   private double minStrength;
/*     */   private double minSpread;
/*     */   private double minZScore;
/*     */ 
/*     */   public WordPairFilter(String workDir, int maxSpan, double minStrength, double minSpread, double minZScore)
/*     */   {
/*  24 */     this.minStrength = minStrength;
/*  25 */     this.minSpread = minSpread;
/*  26 */     this.minZScore = minZScore;
/*  27 */     this.workDir = workDir;
/*  28 */     this.maxSpan = maxSpan;
/*     */   }
/*     */ 
/*     */   public WordPairStat[] execute()
/*     */   {
/*  36 */     WordPairStatList list = new WordPairStatList(this.workDir + "/pairstat.list", this.maxSpan, false);
/*  37 */     int wordNum = readWordNum();
/*  38 */     double[][] arrWordStat = computeWordStat(wordNum, list);
/*  39 */     return filterWordPair(arrWordStat, list);
/*     */   }
/*     */ 
/*     */   private double[][] computeWordStat(int wordNum, WordPairStatList list)
/*     */   {
/*  47 */     System.out.println(new Date().toString() + " Computing Word Stat...");
/*     */ 
/*  49 */     double[][] arrWordStat = new double[wordNum][3];
/*  50 */     for (int i = 0; i < wordNum; i++) {
/*  51 */       for (int j = 0; j < 3; j++)
/*  52 */         arrWordStat[i][j] = 0.0D;
/*     */     }
/*  54 */     for (int i = 0; i < list.size(); i++) {
/*  55 */       WordPairStat curPair = list.get(i);
/*  56 */       arrWordStat[curPair.getFirstWord()][0] += curPair.getTotalFrequency();
/*  57 */       arrWordStat[curPair.getFirstWord()][1] += curPair.getTotalFrequency() * curPair.getTotalFrequency();
/*  58 */       arrWordStat[curPair.getFirstWord()][2] += 1.0D;
/*     */ 
/*  60 */       arrWordStat[curPair.getSecondWord()][0] += curPair.getTotalFrequency();
/*  61 */       arrWordStat[curPair.getSecondWord()][1] += curPair.getTotalFrequency() * curPair.getTotalFrequency();
/*  62 */       arrWordStat[curPair.getSecondWord()][2] += 1.0D;
/*     */     }
/*     */ 
/*  65 */     for (int i = 0; i < wordNum; i++) {
/*  66 */       if (arrWordStat[i][2] > 0.0D) {
/*  67 */         arrWordStat[i][0] /= arrWordStat[i][2];
/*  68 */         arrWordStat[i][1] = Math.sqrt(arrWordStat[i][1] / arrWordStat[i][2] - Math.pow(arrWordStat[i][0], 2.0D));
/*     */       }
/*     */     }
/*  71 */     return arrWordStat;
/*     */   }
/*     */ 
/*     */   private WordPairStat[] filterWordPair(double[][] arrWordStat, WordPairStatList list)
/*     */   {
/*  80 */     ArrayList selectedList = new ArrayList();
/*  81 */     for (int i = 0; i < list.size(); i++) {
/*  82 */       if (i % 10000 == 0) System.out.println(new Date().toString() + " processed: " + i);
/*  83 */       WordPairStat curPair = list.get(i);
/*     */       double strength;
 
/*  84 */       if (arrWordStat[curPair.getFirstWord()][1] == 0.0D)
/*  85 */         strength = 0.0D;
/*     */       else
/*  87 */         strength = (curPair.getTotalFrequency() - arrWordStat[curPair.getFirstWord()][0]) / arrWordStat[curPair.getFirstWord()][1];
/*  88 */       if (strength < this.minStrength) {
/*  89 */         if (arrWordStat[curPair.getSecondWord()][1] == 0.0D)
/*  90 */           strength = 0.0D;
/*     */         else
/*  92 */           strength = (curPair.getTotalFrequency() - arrWordStat[curPair.getSecondWord()][0]) / arrWordStat[curPair.getSecondWord()][1];
/*     */       }
/*  94 */       if (strength >= this.minStrength) {
/*  95 */         WordPairStat filteredPair = filterWordPair(curPair);
/*  96 */         if (filteredPair != null) {
/*  97 */           selectedList.add(filteredPair);
/*     */         }
/*     */       }
/*     */     }
/* 101 */     WordPairStat[] arrSelected = new WordPairStat[selectedList.size()];
/* 102 */     for (int i = 0; i < arrSelected.length; i++)
/* 103 */       arrSelected[i] = ((WordPairStat)selectedList.get(i));
/* 104 */     return arrSelected;
/*     */   }
/*     */ 
/*     */   private WordPairStat filterWordPair(WordPairStat pair)
/*     */   {
/* 112 */     double sum = 0.0D;
/* 113 */     double squareSum = 0.0D;
/* 114 */     for (int i = 1; i <= this.maxSpan; i++) {
/* 115 */       int freq = pair.getFrequency(i);
/* 116 */       sum += freq;
/* 117 */       squareSum += freq * freq;
/*     */ 
/* 119 */       freq = pair.getFrequency(-i);
/* 120 */       sum += freq;
/* 121 */       squareSum += freq * freq;
/*     */     }
/* 123 */     double mean = sum / 2.0D / this.maxSpan;
/* 124 */     double spread = squareSum / 2.0D / this.maxSpan - mean * mean;
/* 125 */     if (spread < this.minSpread) {
/* 126 */       return null;
/*     */     }
/* 128 */     boolean found = false;
/* 129 */     spread = Math.sqrt(spread);
/* 130 */     for (int i = 1; i <= this.maxSpan; i++) {
/* 131 */       int freq = pair.getFrequency(i);
/* 132 */       if ((freq - mean) / spread >= this.minZScore)
/* 133 */         found = true;
/*     */       else {
/* 135 */         pair.addFrequency(i, -freq);
/*     */       }
/* 137 */       freq = pair.getFrequency(-i);
/* 138 */       if ((freq - mean) / spread >= this.minZScore)
/* 139 */         found = true;
/*     */       else {
/* 141 */         pair.addFrequency(-i, -freq);
/*     */       }
/*     */     }
/* 144 */     if (found) {
/* 145 */       return pair;
/*     */     }
/* 147 */     return null;
/*     */   }
/*     */ 
/*     */   private int readWordNum()
/*     */   {
/*     */     try
/*     */     {
/* 155 */       BufferedReader br = FileUtil.getTextReader(this.workDir + "/wordkey.list");
/* 156 */       int num = Integer.parseInt(br.readLine());
/* 157 */       br.close();
/* 158 */       return num;
/*     */     }
/*     */     catch (Exception e) {
/* 161 */       e.printStackTrace();
/* 162 */     }return 0;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.tool.xtract.WordPairFilter
 * JD-Core Version:    0.6.2
 */