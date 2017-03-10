/*     */ package edu.drexel.cis.dragon.nlp.ontology.umls;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*     */ import edu.drexel.cis.dragon.util.FastBinaryReader;
/*     */ import edu.drexel.cis.dragon.util.FastBinaryWriter;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.FormatUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class UmlsCUIList extends SortedArray
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */ 
/*     */   public UmlsCUIList()
/*     */   {
/*     */   }
/*     */ 
/*     */   public UmlsCUIList(String cuiFile)
/*     */   {
/*  25 */     loadCUIList(cuiFile, false);
/*     */   }
/*     */ 
/*     */   public UmlsCUIList(String cuiFile, boolean loadConceptName) {
/*  29 */     loadCUIList(cuiFile, loadConceptName);
/*     */   }
/*     */ 
/*     */   public UmlsCUIList(String cuiFile, boolean binary, boolean loadConceptName) {
/*  33 */     loadCUIList(cuiFile, binary, loadConceptName);
/*     */   }
/*     */ 
/*     */   public UmlsCUI cuiAt(int index) {
/*  37 */     return (UmlsCUI)get(index);
/*     */   }
/*     */ 
/*     */   public ArrayList getListSortedByIndex()
/*     */   {
/*  43 */     ArrayList list = new ArrayList(size());
/*  44 */     list.addAll(this);
/*  45 */     Collections.sort(list, new IndexComparator());
/*  46 */     return list;
/*     */   }
/*     */ 
/*     */   public boolean add(String cui, String[] stys)
/*     */   {
/*  52 */     UmlsCUI cur = new UmlsCUI(size(), cui, stys);
/*  53 */     return add(cur);
/*     */   }
/*     */ 
/*     */   public UmlsCUI lookup(String cui)
/*     */   {
/*  59 */     int pos = binarySearch(new UmlsCUI(0, cui, null));
/*  60 */     if (pos < 0) {
/*  61 */       return null;
/*     */     }
/*  63 */     return (UmlsCUI)get(pos);
/*     */   }
/*     */ 
/*     */   public UmlsCUI lookup(UmlsCUI cui)
/*     */   {
/*  69 */     int pos = binarySearch(cui);
/*  70 */     if (pos < 0) {
/*  71 */       return null;
/*     */     }
/*  73 */     return (UmlsCUI)get(pos);
/*     */   }
/*     */ 
/*     */   public void saveTo(String filename)
/*     */   {
/*     */     try
/*     */     {
/*  82 */       System.out.println(new Date() + " Saving CUI List...");
/*  83 */       PrintWriter bw = FileUtil.getPrintWriter(filename);
/*  84 */       bw.write(size() + "\n");
/*  85 */       for (int i = 0; i < size(); i++) {
/*  86 */         UmlsCUI cur = (UmlsCUI)get(i);
/*  87 */         bw.write(cur.getIndex() + "\t" + cur.toString() + "\t" + cur.getSTY(0));
/*  88 */         for (int j = 1; j < cur.getSTYNum(); j++)
/*  89 */           bw.write("_" + cur.getSTY(j));
/*  90 */         if ((cur.getName() != null) && (cur.getName().length() > 0))
/*  91 */           bw.write("\t" + cur.getName());
/*  92 */         bw.write(10);
/*  93 */         bw.flush();
/*     */       }
/*  95 */       bw.close();
/*     */     }
/*     */     catch (Exception ex) {
/*  98 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void saveTo(String filename, boolean binary)
/*     */   {
/* 107 */     if (!binary) {
/* 108 */       saveTo(filename);
/* 109 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 113 */       FastBinaryWriter fbw = new FastBinaryWriter(filename);
/* 114 */       fbw.writeInt(size());
/* 115 */       for (int i = 0; i < size(); i++) {
/* 116 */         UmlsCUI cui = cuiAt(i);
/* 117 */         fbw.writeInt(cui.getIndex());
/* 118 */         fbw.write(cui.getCUI().getBytes());
/* 119 */         fbw.writeShort(cui.getSTYNum());
/* 120 */         for (int j = 0; j < cui.getSTYNum(); j++)
/* 121 */           fbw.writeShort(Integer.parseInt(cui.getSTY(j).substring(1)));
/*     */       }
/* 123 */       fbw.close();
/*     */     }
/*     */     catch (Exception e) {
/* 126 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean loadCUIList(String filename, boolean loadConceptName)
/*     */   {
/*     */     try
/*     */     {
/* 140 */       System.out.println(new Date() + " Loading CUI List...");
/* 141 */       BufferedReader br = FileUtil.getTextReader(filename);
/* 142 */       String line = br.readLine();
/* 143 */       int total = Integer.parseInt(line);
/* 144 */       ArrayList list = new ArrayList(total);
/* 145 */       String prevSTY = "";
/* 146 */       String[] prevArrSTY = (String[])null;
/*     */ 
/* 148 */       for (int i = 0; i < total; i++) {
/* 149 */         line = br.readLine();
/* 150 */         String[] arrField = line.split("\t");
/*     */         String[] arrSTY;
 
/* 151 */         if (prevSTY.equals(arrField[2])) {
/* 152 */           arrSTY = prevArrSTY;
/*     */         }
/*     */         else {
/* 155 */           arrSTY = arrField[2].split("_");
/* 156 */           prevSTY = arrField[2];
/* 157 */           prevArrSTY = arrSTY;
/*     */         }
/* 159 */         UmlsCUI cur = new UmlsCUI(Integer.parseInt(arrField[0]), arrField[1], arrSTY);
/* 160 */         if ((arrField.length >= 4) && (loadConceptName))
/* 161 */           cur.setName(arrField[3]);
/* 162 */         list.add(cur);
/*     */       }
/* 164 */       br.close();
/* 165 */       Collections.sort(list);
/* 166 */       addAll(list);
/* 167 */       return true;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 171 */       e.printStackTrace();
/* 172 */     }return false;
/*     */   }
/*     */ 
/*     */   private boolean loadCUIList(String filename, boolean binary, boolean loadConceptName)
/*     */   {
/* 185 */     if (!binary)
/* 186 */       return loadCUIList(filename, loadConceptName);
/*     */     try
/*     */     {
/* 189 */       System.out.println(new Date() + " Loading CUI List...");
/* 190 */       FastBinaryReader fbr = new FastBinaryReader(filename);
/* 191 */       int total = fbr.readInt();
/*     */ 
/* 193 */       ArrayList list = new ArrayList(total);
/* 194 */       byte[] buf = new byte[8];
/* 195 */       String[] stys = new String[512];
/* 196 */       DecimalFormat df = FormatUtil.getNumericFormat(3, 0);
/* 197 */       for (int i = 0; i < stys.length; i++) {
/* 198 */         stys[i] = ("T" + df.format(i));
/*     */       }
/* 200 */       for (int i = 0; i < total; i++) {
/* 201 */         int index = fbr.readInt();
/* 202 */         fbr.read(buf);
/* 203 */         String[] arrSTY = new String[fbr.readShort()];
/* 204 */         for (int j = 0; j < arrSTY.length; j++)
/* 205 */           arrSTY[j] = stys[fbr.readShort()];
/* 206 */         UmlsCUI cur = new UmlsCUI(index, new String(buf), arrSTY);
/* 207 */         list.add(cur);
/*     */       }
/* 209 */       fbr.close();
/* 210 */       Collections.sort(list);
/* 211 */       addAll(list);
/* 212 */       return true;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 216 */       e.printStackTrace();
/* 217 */     }return false;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.ontology.umls.UmlsCUIList
 * JD-Core Version:    0.6.2
 */