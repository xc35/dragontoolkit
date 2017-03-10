/*     */ package edu.drexel.cis.dragon.nlp.ontology.umls;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.PrintStream;
/*     */ import java.sql.Connection;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.Statement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ 
/*     */ public class UmlsSTYList extends SortedArray
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*  24 */     UmlsSTYList list = new UmlsSTYList("sir/semantictype.list");
/*  25 */     System.out.println(list.lookup("T028").getDescription());
/*     */   }
/*     */ 
/*     */   public UmlsSTYList(String styFile) {
/*  29 */     loadSTYList(styFile);
/*     */   }
/*     */ 
/*     */   public UmlsSTYList(Connection con) {
/*  33 */     loadSTYList(con);
/*     */   }
/*     */ 
/*     */   public UmlsSTY styAt(int index)
/*     */   {
/*  38 */     return (UmlsSTY)get(index);
/*     */   }
/*     */ 
/*     */   public UmlsSTY lookup(String tui)
/*     */   {
/*  44 */     int pos = binarySearch(new UmlsSTY(0, tui, null, null, false));
/*  45 */     if (pos < 0) {
/*  46 */       return null;
/*     */     }
/*  48 */     return (UmlsSTY)get(pos);
/*     */   }
/*     */ 
/*     */   public UmlsSTY lookup(UmlsSTY tui)
/*     */   {
/*  54 */     int pos = binarySearch(tui);
/*  55 */     if (pos < 0) {
/*  56 */       return null;
/*     */     }
/*  58 */     return (UmlsSTY)get(pos);
/*     */   }
/*     */ 
/*     */   private boolean loadSTYList(String filename)
/*     */   {
/*     */     try
/*     */     {
/*  71 */       BufferedReader br = FileUtil.getTextReader(filename);
/*  72 */       String line = br.readLine();
/*  73 */       int total = Integer.parseInt(line);
/*  74 */       ArrayList list = new ArrayList(total);
/*     */ 
/*  76 */       for (int i = 0; i < total; i++) {
/*  77 */         line = br.readLine();
/*  78 */         String[] arrField = line.split("\t");
/*     */         boolean isRelation;
 
/*  79 */         if (arrField[0].equals("STY"))
/*  80 */           isRelation = false;
/*     */         else
/*  82 */           isRelation = true;
/*  83 */         UmlsSTY cur = new UmlsSTY(i, arrField[1], arrField[2], arrField[3], isRelation);
/*  84 */         list.add(cur);
/*     */       }
/*  86 */       br.close();
/*  87 */       Collections.sort(list);
/*  88 */       addAll(list);
/*  89 */       return true;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  93 */       e.printStackTrace();
/*  94 */     }return false;
/*     */   }
/*     */ 
/*     */   public boolean loadSTYList(Connection con)
/*     */   {
/*     */     try
/*     */     {
/* 108 */       String sql = "select RT, UI, STYRL,STNRTN from SRDEF";
/* 109 */       Statement st = con.createStatement();
/* 110 */       ResultSet rs = st.executeQuery(sql);
/* 111 */       int count = 0;
/* 112 */       ArrayList list = new ArrayList();
/*     */ 
/* 114 */       while (rs.next())
/*     */       {
/*     */         boolean isRelation;
 
/* 116 */         if (rs.getString("RT").equalsIgnoreCase("STY"))
/* 117 */           isRelation = false;
/*     */         else
/* 119 */           isRelation = true;
/* 120 */         UmlsSTY cur = new UmlsSTY(count, rs.getString("UI"), rs.getString("STYRL"), rs.getString("STNRTN"), isRelation);
/* 121 */         list.add(cur);
/*     */       }
/* 123 */       rs.close();
/* 124 */       st.close();
/* 125 */       Collections.sort(list);
/* 126 */       addAll(list);
/* 127 */       return true;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 131 */       e.printStackTrace();
/* 132 */     }return false;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.ontology.umls.UmlsSTYList
 * JD-Core Version:    0.6.2
 */