/*     */ package edu.drexel.cis.dragon.util;
/*     */ 
/*     */ public class MathUtil
/*     */ {
/*  13 */   public static double LOG0 = -1.797693134862316E+38D;
/*  14 */   public static double LOG2 = 0.69314718055D;
/*     */   private static final double MINUS_LOG_EPSILON = 30.0D;
/*     */ 
/*     */   public static void initArray(double[] array, double initVal)
/*     */   {
/*  18 */     for (int i = 0; i < array.length; i++)
/*  19 */       array[i] = initVal;
/*     */   }
/*     */ 
/*     */   public static void initArray(int[] array, int initVal) {
/*  23 */     for (int i = 0; i < array.length; i++)
/*  24 */       array[i] = initVal;
/*     */   }
/*     */ 
/*     */   public static void copyArray(double[] srcArray, double[] destArray) {
/*  28 */     for (int i = 0; i < srcArray.length; i++)
/*  29 */       destArray[i] = srcArray[i];
/*     */   }
/*     */ 
/*     */   public static void multiArray(double[] array, double multiplier) {
/*  33 */     for (int i = 0; i < array.length; i++)
/*  34 */       array[i] *= multiplier;
/*     */   }
/*     */ 
/*     */   public static double sumArray(double[] array)
/*     */   {
/*  41 */     double sum = 0.0D;
/*  42 */     int i = 0;
/*  43 */     while (i < array.length) {
/*  44 */       sum += array[i];
/*  45 */       i++;
/*     */     }
/*  47 */     return sum;
/*     */   }
/*     */ 
/*     */   public static long sumArray(int[] array)
/*     */   {
/*  54 */     long sum = 0L;
/*  55 */     int i = 0;
/*  56 */     while (i < array.length) {
/*  57 */       sum += array[i];
/*  58 */       i++;
/*     */     }
/*  60 */     return sum;
/*     */   }
/*     */ 
/*     */   public static void incArray(double[] array, double[] incArray) {
/*  64 */     for (int i = 0; i < array.length; i++)
/*  65 */       array[i] += incArray[i];
/*     */   }
/*     */ 
/*     */   public static double max(double[] array)
/*     */   {
/*  72 */     double max = array[0];
/*  73 */     int i = 1;
/*  74 */     while (i < array.length) {
/*  75 */       if (array[i] > max)
/*  76 */         max = array[i];
/*  77 */       i++;
/*     */     }
/*  79 */     return max;
/*     */   }
/*     */ 
/*     */   public static int max(int[] array)
/*     */   {
/*  86 */     int max = array[0];
/*  87 */     int i = 1;
/*  88 */     while (i < array.length) {
/*  89 */       if (array[i] > max)
/*  90 */         max = array[i];
/*  91 */       i++;
/*     */     }
/*  93 */     return max;
/*     */   }
/*     */ 
/*     */   public static double min(double[] array)
/*     */   {
/* 101 */     double min = array[0];
/* 102 */     int i = 1;
/* 103 */     while (i < array.length) {
/* 104 */       if (array[i] < min)
/* 105 */         min = array[i];
/* 106 */       i++;
/*     */     }
/* 108 */     return min;
/*     */   }
/*     */ 
/*     */   public static int min(int[] array)
/*     */   {
/* 115 */     int min = array[0];
/* 116 */     int i = 1;
/* 117 */     while (i < array.length) {
/* 118 */       if (array[i] < min)
/* 119 */         min = array[i];
/* 120 */       i++;
/*     */     }
/* 122 */     return min;
/*     */   }
/*     */ 
/*     */   public static double average(double[] array)
/*     */   {
/* 129 */     double sum = 0.0D;
/* 130 */     for (int i = 0; i < array.length; i++)
/* 131 */       sum += array[i];
/* 132 */     return sum / array.length;
/*     */   }
/*     */ 
/*     */   public static double average(int[] array)
/*     */   {
/* 139 */     double sum = 0.0D;
/* 140 */     for (int i = 0; i < array.length; i++)
/* 141 */       sum += array[i];
/* 142 */     return sum / array.length;
/*     */   }
/*     */ 
/*     */   public static int maxElementInArray(double[] array)
/*     */   {
/* 149 */     double max = array[0];
/* 150 */     int maxIndex = 0;
/* 151 */     int i = 1;
/* 152 */     while (i < array.length) {
/* 153 */       if (array[i] > max) {
/* 154 */         max = array[i];
/* 155 */         maxIndex = i;
/*     */       }
/* 157 */       i++;
/*     */     }
/* 159 */     return maxIndex;
/*     */   }
/*     */ 
/*     */   public static int maxElementInArray(int[] array)
/*     */   {
/* 166 */     double max = array[0];
/* 167 */     int maxIndex = 0;
/* 168 */     int i = 1;
/* 169 */     while (i < array.length) {
/* 170 */       if (array[i] > max) {
/* 171 */         max = array[i];
/* 172 */         maxIndex = i;
/*     */       }
/* 174 */       i++;
/*     */     }
/* 176 */     return maxIndex;
/*     */   }
/*     */ 
/*     */   public static int[] rankElementInArray(double[] array, boolean desc)
/*     */   {
/* 183 */     int[] rank = new int[array.length];
/* 184 */     initArray(rank, -1);
/* 185 */     for (int i = 0; i < array.length; i++) {
/* 186 */       int count = 0;
/* 187 */       for (int j = 0; j < array.length; j++)
/* 188 */         if (array[j] > array[i])
/* 189 */           count++;
/* 190 */       if (desc) {
/* 191 */         while (rank[count] >= 0) count++;
/* 192 */         rank[count] = i;
/*     */       }
/*     */       else {
/* 195 */         while (rank[(array.length - 1 - count)] >= 0) count++;
/* 196 */         rank[(array.length - 1 - count)] = i;
/*     */       }
/*     */     }
/* 199 */     return rank;
/*     */   }
/*     */ 
/*     */   public static int[] rankElementInArray(int[] array, boolean desc)
/*     */   {
/* 206 */     int[] rank = new int[array.length];
/* 207 */     initArray(rank, -1);
/* 208 */     for (int i = 0; i < array.length; i++) {
/* 209 */       int count = 0;
/* 210 */       for (int j = 0; j < array.length; j++)
/* 211 */         if (array[j] > array[i])
/* 212 */           count++;
/* 213 */       if (desc) {
/* 214 */         while (rank[count] >= 0) count++;
/* 215 */         rank[count] = i;
/*     */       }
/*     */       else {
/* 218 */         while (rank[(array.length - 1 - count)] >= 0) count++;
/* 219 */         rank[(array.length - 1 - count)] = i;
/*     */       }
/*     */     }
/* 222 */     return rank;
/*     */   }
/*     */ 
/*     */   public static double exp(double d) {
/* 226 */     if ((Double.isInfinite(d)) || ((d < 0.0D) && (Math.abs(d) > 30.0D)))
/* 227 */       return 0.0D;
/* 228 */     return Math.exp(d);
/*     */   }
/*     */ 
/*     */   public static double log(double val) {
/* 232 */     return Math.abs(val - 1.0D) < 4.9E-324D ? 0.0D : Math.log(val);
/*     */   }
/*     */ 
/*     */   public static double logSumExp(double v1, double v2)
/*     */   {
/* 238 */     if (Math.abs(v1 - v2) < 4.9E-324D)
/* 239 */       return v1 + LOG2;
/* 240 */     double vmin = Math.min(v1, v2);
/* 241 */     double vmax = Math.max(v1, v2);
/* 242 */     if (vmax > vmin + 30.0D) {
/* 243 */       return vmax;
/*     */     }
/* 245 */     return vmax + Math.log(Math.exp(vmin - vmax) + 1.0D);
/*     */   }
/*     */ 
/*     */   public static void logSumExp(double[] v1, double[] v2)
/*     */   {
/* 251 */     for (int i = 0; i < v1.length; i++)
/* 252 */       v1[i] = logSumExp(v1[i], v2[i]);
/*     */   }
/*     */ 
/*     */   public static double logSumExp(double[] logArray)
/*     */   {
/* 260 */     double ret = logArray[0];
/* 261 */     for (int i = 1; i < logArray.length; i++)
/* 262 */       ret = logSumExp(ret, logArray[i]);
/* 263 */     return ret;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.util.MathUtil
 * JD-Core Version:    0.6.2
 */