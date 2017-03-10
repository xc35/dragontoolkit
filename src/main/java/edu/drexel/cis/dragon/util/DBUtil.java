/*     */ package edu.drexel.cis.dragon.util;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.ResultSetMetaData;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ 
/*     */ public class DBUtil
/*     */ {
/*     */   public static void executeQuery(Connection con, String sql)
/*     */   {
/*     */     try
/*     */     {
/*  22 */       Statement st = con.createStatement();
/*  23 */       st.executeUpdate(sql);
/*     */     }
/*     */     catch (Exception e) {
/*  26 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void executeBatchQuery(Connection con, String scriptFile)
/*     */   {
/*     */     try
/*     */     {
/*  36 */       BufferedReader br = FileUtil.getTextReader(scriptFile);
/*  37 */       Statement st = con.createStatement();
/*     */       String line;
/*  38 */       while ((line = br.readLine()) != null)
/*     */       {
/*  39 */         System.out.println(line);
/*  40 */         st.executeUpdate(line);
/*     */       }
/*  42 */       System.out.println("Done!");
/*     */     }
/*     */     catch (Exception e) {
/*  45 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void printResultSet(Connection con, String sql, PrintWriter out)
/*     */   {
/*  53 */     ResultSet rs = getResultSet(con, sql);
/*  54 */     printResultSet(rs, out);
/*     */     try {
/*  56 */       rs.close();
/*  57 */       rs.getStatement().close();
/*     */     }
/*     */     catch (Exception e) {
/*  60 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void printResultSet(ResultSet rs, PrintWriter out) {
/*  65 */     printResultSet(rs, out, 0);
/*     */   }
/*     */ 
/*     */   public static void printResultSet(ResultSet rs, PrintWriter out, int top)
/*     */   {
/*     */     try
/*     */     {
/*  73 */       int count = 0;
/*  74 */       ResultSetMetaData rsMeta = rs.getMetaData();
/*  75 */       int fieldNum = rsMeta.getColumnCount();
/*  76 */       while ((rs.next()) && ((count < top) || (top <= 0)))
/*     */       {
/*  78 */         for (int i = 0; i < fieldNum; i++)
/*  79 */           out.write(rs.getString(i + 1) + "\t");
/*  80 */         out.write("\n");
/*  81 */         out.flush();
/*  82 */         count++;
/*     */       }
/*  84 */       out.close();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  88 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Connection getAccessCon(String file)
/*     */   {
/*     */     try {
/*  95 */       Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
/*     */ 
/*  97 */       String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
/*  98 */       database = database + file + ";DriverID=22;READONLY=true}";
/*     */ 
/* 100 */       return DriverManager.getConnection(database, "", "");
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 104 */       e.printStackTrace();
/* 105 */     }return null;
/*     */   }
/*     */ 
/*     */   public static Connection getMSSQLCon(String server, String db, String uid, String pwd)
/*     */   {
/*     */     try
/*     */     {
/* 112 */       Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
/*     */ 
/* 114 */       String database = "jdbc:odbc:Driver={SQL Server};Server=" + server;
/* 115 */       database = database + ";Database=" + db + ";UID=" + uid + ";PWD=" + pwd + ";";
/*     */ 
/* 117 */       return DriverManager.getConnection(database, "", "");
/*     */     }
/*     */     catch (Exception e) {
/*     */     }
/* 121 */     return null;
/*     */   }
/*     */ 
/*     */   public static Connection getMSSQL2000Con(String server, String db, String uid, String pwd)
/*     */   {
/*     */     try
/*     */     {
/* 128 */       Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
/*     */ 
/* 130 */       String database = "jdbc:microsoft:sqlserver://" + server + ":1433;";
/* 131 */       database = database + "databasename=" + db + ";user=" + uid + ";password=" + pwd + ";";
/*     */ 
/* 133 */       return DriverManager.getConnection(database);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 137 */       e.printStackTrace();
/* 138 */     }return null;
/*     */   }
/*     */ 
/*     */   public static Connection getMSSQL2005Con(String server, String db, String uid, String pwd)
/*     */   {
/*     */     try
/*     */     {
/* 148 */       Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
/*     */ 
/* 150 */       String database = "jdbc:sqlserver://" + server + ":8081;";
/* 151 */       database = database + "databasename=" + db + ";user=" + uid + ";password=" + pwd + ";";
/*     */ 
/* 153 */       return DriverManager.getConnection(database);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 157 */       e.printStackTrace();
/* 158 */     }return null;
/*     */   }
/*     */ 
/*     */   public static Connection getDB2Connection(String server, String db, String uid, String pwd)
/*     */   {
/* 166 */     String sConnect = "jdbc:db2://" + server + ":50000/" + db;
/*     */     try {
/* 168 */       Class.forName("com.ibm.db2.jcc.DB2Driver");
/* 169 */       return DriverManager.getConnection(sConnect, uid, pwd);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 173 */       e.printStackTrace();
/* 174 */     }return null;
/*     */   }
/*     */ 
/*     */   public static void closeConnection(Connection con)
/*     */   {
/*     */     try {
/* 180 */       con.close();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 184 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static ResultSet getResultSet(Connection con, String sql) {
/*     */     try {
/* 190 */       Statement st = con.createStatement(1004, 1007);
/* 191 */       return st.executeQuery(sql);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 196 */       e.printStackTrace();
/* 197 */     }return null;
/*     */   }
/*     */ 
/*     */   public static int getRecordCount(ResultSet rs)
/*     */   {
/* 203 */     if (rs == null) return 0;
/*     */     try
/*     */     {
/* 206 */       if (rs.getType() == 1003) {
/* 207 */         return 0;
/*     */       }
/* 209 */       int pos = rs.getRow();
/* 210 */       rs.last();
/* 211 */       int count = rs.getRow();
/* 212 */       if (pos == 0) rs.first(); else {
/* 213 */         rs.absolute(pos);
/*     */       }
/* 215 */       return count;
/*     */     }
/*     */     catch (SQLException e) {
/*     */     }
/* 219 */     return 0;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.util.DBUtil
 * JD-Core Version:    0.6.2
 */