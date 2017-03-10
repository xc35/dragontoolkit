/*     */ package edu.drexel.cis.dragon.nlp.ontology.umls;
/*     */ 
/*     */ import edu.drexel.cis.dragon.matrix.DoubleFlatSparseMatrix;
/*     */ import edu.drexel.cis.dragon.util.DBUtil;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.BufferedReader;
/*     */ import java.sql.Connection;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.Statement;
/*     */ 
/*     */ public class UmlsRelationNet
/*     */ {
/*     */   protected DoubleFlatSparseMatrix matrix;
/*     */   protected UmlsSTYList list;
/*     */ 
/*     */   public UmlsRelationNet(String relationFile, UmlsSTYList list)
/*     */   {
/*  22 */     this.list = list;
/*  23 */     loadRelations(relationFile);
/*     */   }
/*     */ 
/*     */   public UmlsRelationNet(Connection con, UmlsSTYList list) {
/*  27 */     this.list = list;
/*  28 */     loadRelations(con);
/*     */   }
/*     */ 
/*     */   private boolean loadRelations(String filename)
/*     */   {
/*     */     try
/*     */     {
/*  39 */       BufferedReader br = FileUtil.getTextReader(filename);
/*  40 */       this.matrix = new DoubleFlatSparseMatrix();
/*     */       String line;
/*  41 */       while ((line = br.readLine()) != null)
/*     */       {
 
/*  42 */         String[] arrField = line.split("\t");
/*  43 */         int first = this.list.lookup(arrField[0]).getIndex();
/*  44 */         int second = this.list.lookup(arrField[2]).getIndex();
/*  45 */         double relation = this.list.lookup(arrField[1]).getIndex() + 1;
/*  46 */         this.matrix.add(first, second, relation);
/*     */       }
/*  48 */       br.close();
/*  49 */       this.matrix.finalizeData(false);
/*  50 */       return true;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  54 */       e.printStackTrace();
/*  55 */     }return false;
/*     */   }
/*     */ 
/*     */   private boolean loadRelations(Connection con)
/*     */   {
/*     */     try
/*     */     {
/*  65 */       ResultSet rs = DBUtil.getResultSet(con, "select * from SRSTRE1");
/*  66 */       this.matrix = new DoubleFlatSparseMatrix();
/*  67 */       while (rs.next()) {
/*  68 */         int first = this.list.lookup(rs.getString(1)).getIndex();
/*  69 */         int second = this.list.lookup(rs.getString(3)).getIndex();
/*  70 */         double relation = this.list.lookup(rs.getString(2)).getIndex() + 1;
/*  71 */         this.matrix.add(first, second, relation);
/*     */       }
/*  73 */       rs.close();
/*  74 */       rs.getStatement().close();
/*  75 */       this.matrix.finalizeData(false);
/*  76 */       return true;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  80 */       e.printStackTrace();
/*  81 */     }return false;
/*     */   }
/*     */ 
/*     */   public String[] getRelations(String[] arrFirstST, String[] arrSecondST)
/*     */   {
/*  92 */     if ((arrFirstST == null) || (arrSecondST == null)) return null;
/*     */ 
/*  94 */     SortedArray typeList = new SortedArray(3);
/*  95 */     for (int i = 0; i < arrFirstST.length; i++)
/*     */     {
/*  97 */       int first = this.list.lookup(arrFirstST[i]).getIndex();
/*  98 */       for (int j = 0; j < arrSecondST.length; j++) {
/*  99 */         String relation = getRelations(first, this.list.lookup(arrSecondST[j]).getIndex());
/* 100 */         if (relation != null) {
/* 101 */           typeList.add(relation);
/*     */         }
/*     */       }
/*     */     }
/* 105 */     if (typeList.size() > 0) {
/* 106 */       String[] arrTypes = new String[typeList.size()];
/* 107 */       for (int i = 0; i < typeList.size(); i++)
/* 108 */         arrTypes[i] = ((String)typeList.get(i));
/* 109 */       return arrTypes;
/*     */     }
/*     */ 
/* 112 */     return null;
/*     */   }
/*     */ 
/*     */   public String getRelations(String firstST, String secondST) {
/* 116 */     if ((firstST == null) || (secondST == null)) {
/* 117 */       return null;
/*     */     }
/* 119 */     return getRelations(this.list.lookup(firstST).getIndex(), this.list.lookup(secondST).getIndex());
/*     */   }
/*     */ 
/*     */   public String getRelations(int firstST, int secondST)
/*     */   {
/* 125 */     int index = (int)this.matrix.get(firstST, secondST) - 1;
/* 126 */     if (index <= 0) {
/* 127 */       index = (int)this.matrix.get(secondST, firstST) - 1;
/*     */     }
/* 129 */     if (index > 0) {
/* 130 */       return ((UmlsSTY)this.list.get(index)).toString();
/*     */     }
/* 132 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isSemanticRelated(String[] arrFirstST, String[] arrSecondST)
/*     */   {
/* 139 */     if ((arrFirstST == null) || (arrSecondST == null)) return false;
/* 140 */     for (int i = 0; i < arrFirstST.length; i++) {
/* 141 */       int first = this.list.lookup(arrFirstST[i]).getIndex();
/* 142 */       for (int j = 0; j < arrSecondST.length; j++)
/* 143 */         if (isSemanticRelated(first, this.list.lookup(arrSecondST[j]).getIndex()))
/* 144 */           return true;
/*     */     }
/* 146 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isSemanticRelated(String firstST, String secondST) {
/* 150 */     if ((firstST == null) || (secondST == null))
/* 151 */       return false;
/* 152 */     return isSemanticRelated(this.list.lookup(firstST).getIndex(), this.list.lookup(secondST).getIndex());
/*     */   }
/*     */ 
/*     */   public boolean isSemanticRelated(int firstST, int secondST) {
/* 156 */     return (this.matrix.get(firstST, secondST) > 0.0D) || (this.matrix.get(secondST, firstST) > 0.0D);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.ontology.umls.UmlsRelationNet
 * JD-Core Version:    0.6.2
 */