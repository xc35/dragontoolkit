/*     */ package edu.drexel.cis.dragon.nlp.ontology.umls;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.ontology.Ontology;
/*     */ import edu.drexel.cis.dragon.nlp.ontology.SemanticNet;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*     */ import edu.drexel.cis.dragon.util.DBUtil;
/*     */ import java.sql.Connection;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.Statement;
/*     */ 
/*     */ public class UmlsDBOntology extends UmlsExactOntology
/*     */   implements Ontology
/*     */ {
/*     */   private Connection con;
/*     */   private UmlsSemanticNet snNet;
/*     */ 
/*     */   public UmlsDBOntology(Connection con, Lemmatiser lemmatiser)
/*     */   {
/*  22 */     super(lemmatiser);
/*  23 */     this.con = con;
/*     */ 
/*  25 */     UmlsSTYList styList = new UmlsSTYList(con);
/*  26 */     UmlsRelationNet relationNet = new UmlsRelationNet(con, styList);
/*  27 */     this.snNet = new UmlsSemanticNet(this, styList, relationNet);
/*     */   }
/*     */ 
/*     */   public Connection getConnection() {
/*  31 */     return this.con;
/*     */   }
/*     */ 
/*     */   public void closeConnection() {
/*     */     try {
/*  36 */       if (this.con != null) {
/*  37 */         this.con.close();
/*  38 */         this.con = null;
/*     */       }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  43 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public SemanticNet getSemanticNet()
/*     */   {
/*  49 */     return this.snNet;
/*     */   }
/*     */ 
/*     */   public String[] getSemanticType(String[] cuis)
/*     */   {
/*  55 */     if (cuis == null) return null;
/*  56 */     StringBuffer sSQL = new StringBuffer("select distinct tui from mrsty where cui in (");
/*  57 */     for (int i = 0; i < cuis.length; i++) {
/*  58 */       sSQL.append('\'');
/*  59 */       sSQL.append(cuis[i]);
/*  60 */       sSQL.append('\'');
/*  61 */       if (i < cuis.length - 1)
/*  62 */         sSQL.append(',');
/*     */       else
/*  64 */         sSQL.append(')');
/*     */     }
/*  66 */     return readSemanticTypes(sSQL.toString());
/*     */   }
/*     */ 
/*     */   public String[] getSemanticType(String cui)
/*     */   {
/*  72 */     String sSQL = "select distinct tui from mrsty where cui='" + cui + "'";
/*  73 */     return readSemanticTypes(sSQL);
/*     */   }
/*     */ 
/*     */   private String[] readSemanticTypes(String sql)
/*     */   {
/*     */     try
/*     */     {
/*  82 */       ResultSet rs = DBUtil.getResultSet(this.con, sql);
/*  83 */       int count = DBUtil.getRecordCount(rs);
/*     */       String[] arrTUI;
 
/*  84 */       if (count == 0)
/*     */       {
/*  86 */         arrTUI = (String[])null;
/*     */       }
/*     */       else
/*     */       {
/*  90 */         arrTUI = new String[count];
/*  91 */         int i = 0;
/*  92 */         while (i < count) {
/*  93 */           arrTUI[i] = rs.getString("tui");
/*  94 */           rs.next();
/*  95 */           i++;
/*     */         }
/*     */       }
/*  98 */       rs.close();
/*  99 */       rs.getStatement().close();
/* 100 */       return arrTUI;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 104 */       e.printStackTrace();
/* 105 */     }return null;
/*     */   }
/*     */ 
/*     */   public String[] getCUI(String term)
/*     */   {
/* 116 */     String sSQL = "select distinct cui from mrxns_eng where NSTR='" + term + "'";
/*     */     try {
/* 118 */       ResultSet rs = DBUtil.getResultSet(this.con, sSQL);
/* 119 */       int count = DBUtil.getRecordCount(rs);
/*     */       String[] arrCUI;
 
/* 120 */       if (count == 0)
/*     */       {
/* 122 */         arrCUI = (String[])null;
/*     */       }
/*     */       else
/*     */       {
/* 126 */         arrCUI = new String[count];
/* 127 */         int i = 0;
/* 128 */         while (i < count) {
/* 129 */           arrCUI[i] = rs.getString("cui");
/* 130 */           rs.next();
/* 131 */           i++;
/*     */         }
/*     */       }
/* 134 */       rs.close();
/* 135 */       rs.getStatement().close();
/* 136 */       return arrCUI;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 140 */       e.printStackTrace();
/* 141 */     }return null;
/*     */   }
/*     */ 
/*     */   public boolean isTerm(String term)
/*     */   {
/* 150 */     String sSQL = "select cui from mrxns_eng where NSTR='" + term + "' fetch first 1 rows only";
/*     */     try {
/* 152 */       ResultSet rs = DBUtil.getResultSet(this.con, sSQL);
/* 153 */       int count = DBUtil.getRecordCount(rs);
/* 154 */       rs.close();
/* 155 */       rs.getStatement().close();
/* 156 */       return count > 0;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 160 */       e.printStackTrace();
/* 161 */     }return false;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.ontology.umls.UmlsDBOntology
 * JD-Core Version:    0.6.2
 */