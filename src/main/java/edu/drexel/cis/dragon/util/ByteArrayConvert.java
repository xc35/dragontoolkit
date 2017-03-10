/*     */ package edu.drexel.cis.dragon.util;
/*     */ 
/*     */ public class ByteArrayConvert
/*     */ {
/*     */   public static final byte[] toByte(int i)
/*     */   {
/*  16 */     byte[] abyte0 = new byte[4];
/*  17 */     for (byte byte0 = 0; byte0 <= 3; byte0 = (byte)(byte0 + 1))
/*  18 */       abyte0[byte0] = ((byte)(i >>> (3 - byte0) * 8));
/*  19 */     return abyte0;
/*     */   }
/*     */ 
/*     */   public static final void toByte(int i, byte[] abyte0, int offset)
/*     */   {
/*  24 */     for (byte byte0 = 0; byte0 <= 3; byte0 = (byte)(byte0 + 1))
/*  25 */       abyte0[(byte0 + offset)] = ((byte)(i >>> (3 - byte0) * 8));
/*     */   }
/*     */ 
/*     */   public static final byte[] toByte(short word0)
/*     */   {
/*  30 */     byte[] abyte0 = new byte[2];
/*  31 */     for (byte byte0 = 0; byte0 <= 1; byte0 = (byte)(byte0 + 1))
/*  32 */       abyte0[byte0] = ((byte)(word0 >>> (1 - byte0) * 8));
/*  33 */     return abyte0;
/*     */   }
/*     */ 
/*     */   public static final void toByte(short word0, byte[] abyte0, int offset)
/*     */   {
/*  38 */     for (byte byte0 = 0; byte0 <= 1; byte0 = (byte)(byte0 + 1))
/*  39 */       abyte0[(byte0 + offset)] = ((byte)(word0 >>> (1 - byte0) * 8));
/*     */   }
/*     */ 
/*     */   public static final byte[] toByte(long l)
/*     */   {
/*  44 */     byte[] abyte0 = new byte[8];
/*  45 */     for (byte byte0 = 0; byte0 <= 7; byte0 = (byte)(byte0 + 1))
/*  46 */       abyte0[byte0] = ((byte)(int)(l >>> (7 - byte0) * 8));
/*  47 */     return abyte0;
/*     */   }
/*     */ 
/*     */   public static final void toByte(long l, byte[] abyte0, int offset)
/*     */   {
/*  52 */     for (byte byte0 = 0; byte0 <= 7; byte0 = (byte)(byte0 + 1))
/*  53 */       abyte0[(byte0 + offset)] = ((byte)(int)(l >>> (7 - byte0) * 8));
/*     */   }
/*     */ 
/*     */   public static final byte[] toByte(char c)
/*     */   {
/*  58 */     byte[] abyte0 = new byte[2];
/*  59 */     for (byte byte0 = 0; byte0 <= 1; byte0 = (byte)(byte0 + 1))
/*  60 */       abyte0[byte0] = ((byte)(c >>> (1 - byte0) * 8));
/*  61 */     return abyte0;
/*     */   }
/*     */ 
/*     */   public static final void toByte(char c, byte[] abyte0, int offset)
/*     */   {
/*  66 */     for (byte byte0 = 0; byte0 <= 1; byte0 = (byte)(byte0 + 1))
/*  67 */       abyte0[(byte0 + offset)] = ((byte)(c >>> (1 - byte0) * 8));
/*     */   }
/*     */ 
/*     */   public static final byte[] toByte(float f)
/*     */   {
/*  72 */     byte[] abyte0 = new byte[4];
/*  73 */     int i = Float.floatToIntBits(f);
/*  74 */     abyte0 = toByte(i);
/*  75 */     return abyte0;
/*     */   }
/*     */ 
/*     */   public static final void toByte(float f, byte[] abyte0, int offset)
/*     */   {
/*  80 */     int i = Float.floatToIntBits(f);
/*  81 */     toByte(i, abyte0, offset);
/*     */   }
/*     */ 
/*     */   public static final byte[] toByte(double d)
/*     */   {
/*  86 */     byte[] abyte0 = new byte[8];
/*  87 */     long l = Double.doubleToLongBits(d);
/*  88 */     abyte0 = toByte(l);
/*  89 */     return abyte0;
/*     */   }
/*     */ 
/*     */   public static final void toByte(double d, byte[] abyte0, int offset)
/*     */   {
/*  94 */     long l = Double.doubleToLongBits(d);
/*  95 */     toByte(l, abyte0, offset);
/*     */   }
/*     */ 
/*     */   public static final int toInt(byte[] abyte0, int offset)
/*     */   {
/* 100 */     int i = 0;
/*     */ 
/* 102 */     for (int byte0 = offset; byte0 <= offset + 3; byte0++)
/*     */     {
/*     */       int j;
/* 105 */       if (abyte0[byte0] < 0)
/*     */       {
/* 107 */         abyte0[byte0] = ((byte)(abyte0[byte0] & 0x7F));
/* 108 */          j = abyte0[byte0];
/* 109 */         j |= 128;
/*     */       }
/*     */       else {
/* 112 */         j = abyte0[byte0];
/*     */       }
/* 114 */       i |= j;
/* 115 */       if (byte0 < 3 + offset)
/* 116 */         i <<= 8;
/*     */     }
/* 118 */     return i;
/*     */   }
/*     */ 
/*     */   public static final int toInt(byte[] abyte0)
/*     */   {
/* 123 */     return toInt(abyte0, 0);
/*     */   }
/*     */ 
/*     */   public static final short toShort(byte[] abyte0, int offset)
/*     */   {
/* 128 */     short word0 = 0;
/*     */ 
/* 130 */     for (int byte0 = offset; byte0 <= offset + 1; byte0++)
/*     */     {
/*     */       short word1;
/* 133 */       if (abyte0[byte0] < 0)
/*     */       {
/* 135 */         abyte0[byte0] = ((byte)(abyte0[byte0] & 0x7F));
/* 136 */          word1 = abyte0[byte0];
/* 137 */         word1 = (short)(word1 | 0x80);
/*     */       }
/*     */       else {
/* 140 */         word1 = abyte0[byte0];
/*     */       }
/* 142 */       word0 = (short)(word0 | word1);
/* 143 */       if (byte0 < 1 + offset) {
/* 144 */         word0 = (short)(word0 << 8);
/*     */       }
/*     */     }
/* 147 */     return word0;
/*     */   }
/*     */ 
/*     */   public static final short toShort(byte[] abyte0)
/*     */   {
/* 152 */     return toShort(abyte0, 0);
/*     */   }
/*     */ 
/*     */   public static final long toLong(byte[] abyte0, int offset)
/*     */   {
/* 157 */     long l = 0L;
/*     */ 
/* 159 */     for (int byte0 = offset; byte0 <= offset + 7; byte0++)
/*     */     {
/*     */       long l1;
/* 162 */       if (abyte0[byte0] < 0)
/*     */       {
/* 164 */         abyte0[byte0] = ((byte)(abyte0[byte0] & 0x7F));
/* 165 */          l1 = abyte0[byte0];
/* 166 */         l1 |= 128L;
/*     */       }
/*     */       else {
/* 169 */         l1 = abyte0[byte0];
/*     */       }
/* 171 */       l |= l1;
/* 172 */       if (byte0 < 7 + offset)
/* 173 */         l <<= 8;
/*     */     }
/* 175 */     return l;
/*     */   }
/*     */ 
/*     */   public static final long toLong(byte[] abyte0)
/*     */   {
/* 180 */     return toLong(abyte0, 0);
/*     */   }
/*     */ 
/*     */   public static final char toChar(byte[] abyte0, int offset)
/*     */   {
/* 185 */     char c = '\000';
/*     */ 
/* 187 */     c = (char)((c | (char)abyte0[offset]) << '\b');
/* 188 */     c = (char)(c | (char)abyte0[(offset + 1)]);
/* 189 */     return c;
/*     */   }
/*     */ 
/*     */   public static final char toChar(byte[] abyte0)
/*     */   {
/* 194 */     return toChar(abyte0, 0);
/*     */   }
/*     */ 
/*     */   public static final float toFloat(byte[] abyte0, int offset)
/*     */   {
/* 199 */     float f = 0.0F;
/* 200 */     int i = toInt(abyte0, offset);
/* 201 */     f = Float.intBitsToFloat(i);
/* 202 */     return f;
/*     */   }
/*     */ 
/*     */   public static final float toFloat(byte[] abyte0)
/*     */   {
/* 207 */     return toFloat(abyte0, 0);
/*     */   }
/*     */ 
/*     */   public static final double toDouble(byte[] abyte0, int offset)
/*     */   {
/* 212 */     double d = 0.0D;
/* 213 */     long l = toLong(abyte0, offset);
/* 214 */     d = Double.longBitsToDouble(l);
/* 215 */     return d;
/*     */   }
/*     */ 
/*     */   public static final double toDouble(byte[] abyte0)
/*     */   {
/* 220 */     return toDouble(abyte0, 0);
/*     */   }
/*     */ 
/*     */   public static String toHexString(byte[] data, int offset, int len) {
/* 224 */     StringBuffer sb = new StringBuffer(len * 2);
/*     */ 
/* 226 */     for (int i = offset; i < len; i++) {
/* 227 */       sb.append(Integer.toHexString(data[i] & 0xFF));
/*     */     }
/* 229 */     return sb.toString();
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.util.ByteArrayConvert
 * JD-Core Version:    0.6.2
 */
