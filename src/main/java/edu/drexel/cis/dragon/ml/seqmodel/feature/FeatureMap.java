/*     */ package edu.drexel.cis.dragon.ml.seqmodel.feature;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public class FeatureMap
/*     */ {
/*     */   private Hashtable strToInt;
/*     */   private String[] idToName;
/*     */   private boolean frozen;
/*     */ 
/*     */   public FeatureMap()
/*     */   {
/*  25 */     this.frozen = false;
/*  26 */     this.strToInt = new Hashtable();
/*     */   }
/*     */ 
/*     */   public boolean isFrozen() {
/*  30 */     return this.frozen;
/*     */   }
/*     */ 
/*     */   public int getId(FeatureIdentifier key)
/*     */   {
/*  36 */     Integer id = (Integer)this.strToInt.get(key);
/*  37 */     if (id != null) {
/*  38 */       return id.intValue();
/*     */     }
/*     */ 
/*  41 */     return -1;
/*     */   }
/*     */ 
/*     */   public int add(FeatureIdentifier id)
/*     */   {
/*  47 */     if (this.frozen) {
/*  48 */       return -1;
/*     */     }
/*  50 */     int newId = this.strToInt.size();
/*  51 */     this.strToInt.put(id.copy(), new Integer(newId));
/*  52 */     return newId;
/*     */   }
/*     */ 
/*     */   public void freezeFeatures()
/*     */   {
/*  58 */     this.idToName = new String[this.strToInt.size()];
/*  59 */     for (Enumeration e = this.strToInt.keys(); e.hasMoreElements(); ) {
/*  60 */       FeatureIdentifier key = (FeatureIdentifier)e.nextElement();
/*  61 */       this.idToName[getId(key)] = key.toString();
/*     */     }
/*  63 */     this.frozen = true;
/*     */   }
/*     */ 
/*     */   public int getFeatureNum() {
/*  67 */     return this.strToInt.size();
/*     */   }
/*     */ 
/*     */   public void write(PrintWriter out)
/*     */     throws IOException
/*     */   {
/*  73 */     out.println(this.strToInt.size());
/*  74 */     for (Enumeration e = this.strToInt.keys(); e.hasMoreElements(); ) {
/*  75 */       FeatureIdentifier key = (FeatureIdentifier)e.nextElement();
/*  76 */       out.println(key.toString() + " " + ((Integer)this.strToInt.get(key)).intValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   public int read(BufferedReader in)
/*     */     throws IOException
/*     */   {
/*  86 */     int len = Integer.parseInt(in.readLine());
/*     */     String line;
/*  87 */     for (int l = 0; (l < len) && ((line = in.readLine()) != null); l++)
/*     */     {
/*  88 */       StringTokenizer entry = new StringTokenizer(line, " ");
/*  89 */       FeatureIdentifier key = new FeatureIdentifier(entry.nextToken());
/*  90 */       int pos = Integer.parseInt(entry.nextToken());
/*  91 */       this.strToInt.put(key, new Integer(pos));
/*     */     }
/*  93 */     freezeFeatures();
/*  94 */     return this.strToInt.size();
/*     */   }
/*     */ 
/*     */   public String getIdentifier(int id) {
/*  98 */     return this.idToName[id];
/*     */   }
/*     */ 
/*     */   public String getName(int id) {
/* 102 */     return this.idToName[id];
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.feature.FeatureMap
 * JD-Core Version:    0.6.2
 */