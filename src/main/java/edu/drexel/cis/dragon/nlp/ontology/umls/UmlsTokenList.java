/*     */ package edu.drexel.cis.dragon.nlp.ontology.umls;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.util.FastBinaryReader;
/*     */ import edu.drexel.cis.dragon.util.FastBinaryWriter;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class UmlsTokenList extends SortedArray
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */ 
/*     */   public UmlsTokenList()
/*     */   {
/*     */   }
/*     */ 
/*     */   public UmlsTokenList(String tokenFile)
/*     */   {
/*  24 */     loadTokenList(tokenFile);
/*     */   }
/*     */ 
/*     */   public UmlsTokenList(String tokenFile, boolean binary) {
/*  28 */     loadTokenList(tokenFile, binary);
/*     */   }
/*     */ 
/*     */   public int addToken(Token token) {
/*  32 */     token.setIndex(size());
/*  33 */     if (add(token)) {
/*  34 */       token.setFrequency(1);
/*  35 */       return token.getIndex();
/*     */     }
/*     */ 
/*  38 */     token = (Token)get(insertedPos());
/*  39 */     token.addFrequency(1);
/*  40 */     return token.getIndex();
/*     */   }
/*     */ 
/*     */   public Token tokenAt(int index)
/*     */   {
/*  45 */     return (Token)get(index);
/*     */   }
/*     */ 
/*     */   public Token lookup(String token)
/*     */   {
/*  51 */     int pos = binarySearch(new Token(token));
/*  52 */     if (pos < 0) {
/*  53 */       return null;
/*     */     }
/*  55 */     return (Token)get(pos);
/*     */   }
/*     */ 
/*     */   public Token lookup(Token token)
/*     */   {
/*  61 */     int pos = binarySearch(token);
/*  62 */     if (pos < 0) {
/*  63 */       return null;
/*     */     }
/*  65 */     return (Token)get(pos);
/*     */   }
/*     */ 
/*     */   private void loadTokenList(String tokenFilename)
/*     */   {
/*     */     try
/*     */     {
/*  76 */       System.out.println(new Date() + " Loading Token List...");
/*  77 */       BufferedReader br = FileUtil.getTextReader(tokenFilename);
/*  78 */       String line = br.readLine();
/*  79 */       int len = Integer.parseInt(line);
/*  80 */       ArrayList tokenList = new ArrayList(len);
/*     */ 
/*  82 */       for (int i = 0; i < len; i++) {
/*  83 */         line = br.readLine();
/*  84 */         String[] strArr = line.split("\t");
/*  85 */         Token token = new Token(strArr[1]);
/*  86 */         token.setIndex(Integer.parseInt(strArr[0]));
/*  87 */         int freq = Integer.parseInt(strArr[2]);
/*  88 */         token.setFrequency(freq);
/*  89 */         token.setWeight(1.0D / freq);
/*  90 */         tokenList.add(token);
/*     */       }
/*  92 */       br.close();
/*  93 */       Collections.sort(tokenList);
/*  94 */       addAll(tokenList);
/*     */     }
/*     */     catch (Exception ex) {
/*  97 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void loadTokenList(String tokenFilename, boolean binary)
/*     */   {
/* 108 */     if (!binary) {
/* 109 */       loadTokenList(tokenFilename);
/* 110 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 114 */       System.out.println(new Date() + " Loading Token List...");
/* 115 */       FastBinaryReader fbr = new FastBinaryReader(tokenFilename);
/* 116 */       byte[] buf = new byte[1024];
/* 117 */       int total = fbr.readInt();
/* 118 */       ArrayList tokenList = new ArrayList(total);
/*     */ 
/* 120 */       for (int i = 0; i < total; i++) {
/* 121 */         int index = fbr.readInt();
/* 122 */         int freq = fbr.readShort();
/* 123 */         int len = fbr.readShort();
/* 124 */         len = fbr.read(buf, 0, len);
/* 125 */         Token token = new Token(new String(buf, 0, len));
/* 126 */         token.setIndex(index);
/* 127 */         token.setFrequency(freq);
/* 128 */         token.setWeight(1.0D / freq);
/* 129 */         tokenList.add(token);
/*     */       }
/* 131 */       fbr.close();
/* 132 */       Collections.sort(tokenList);
/* 133 */       addAll(tokenList);
/*     */     }
/*     */     catch (Exception ex) {
/* 136 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void saveTo(String filename)
/*     */   {
/*     */     try
/*     */     {
/* 146 */       System.out.println(new Date() + " Saving Token List...");
/* 147 */       BufferedWriter bw = FileUtil.getTextWriter(filename);
/* 148 */       bw.write(size());
/* 149 */       bw.write("\n");
/* 150 */       for (int i = 0; i < size(); i++) {
/* 151 */         Token t = (Token)get(i);
/* 152 */         bw.write(t.getIndex() + "\t" + t.getValue() + "\t" + t.getFrequency() + "\n");
/* 153 */         bw.flush();
/*     */       }
/* 155 */       bw.close();
/*     */     }
/*     */     catch (Exception ex) {
/* 158 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void saveTo(String filename, boolean binary)
/*     */   {
/* 167 */     if (!binary) {
/* 168 */       saveTo(filename);
/* 169 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 173 */       FastBinaryWriter fbw = new FastBinaryWriter(filename);
/* 174 */       fbw.writeInt(size());
/* 175 */       for (int i = 0; i < size(); i++) {
/* 176 */         Token token = tokenAt(i);
/* 177 */         fbw.writeInt(token.getIndex());
/* 178 */         fbw.writeShort(token.getFrequency());
/* 179 */         fbw.writeShort(token.getValue().length());
/* 180 */         fbw.writeBytes(token.getValue());
/*     */       }
/* 182 */       fbw.close();
/*     */     }
/*     */     catch (Exception e) {
/* 185 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.ontology.umls.UmlsTokenList
 * JD-Core Version:    0.6.2
 */