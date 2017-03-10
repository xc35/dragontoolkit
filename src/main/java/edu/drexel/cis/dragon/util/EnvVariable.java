/*    */ package edu.drexel.cis.dragon.util;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.InputStreamReader;
/*    */ import java.util.Properties;
/*    */ 
/*    */ public class EnvVariable
/*    */ {
/* 16 */   private static String DRAGONHOME = null;
/* 17 */   private static String CHARSET = null;
/*    */ 
/*    */   public static Properties getEnv()
/*    */   {
/*    */     try
/*    */     {
/* 28 */       Runtime r = Runtime.getRuntime();
/* 29 */       Properties envVars = new Properties();
/* 30 */       String OS = System.getProperty("os.name").toLowerCase();
/*    */       Process p;
/* 31 */       if (OS.indexOf("windows 9") > -1) {
/* 32 */         p = r.exec("command.com /c set");
/*    */       }
/*    */       else
/*    */       {
/* 33 */         if ((OS.indexOf("nt") > -1) || (OS.indexOf("windows") > -1))
/*    */         {
/* 35 */           p = r.exec("cmd.exe /c set");
/*    */         }
/*    */         else {
/* 38 */           p = r.exec("env");
/*    */         }
/*    */       }
/* 41 */       BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
/*    */       String line;
/* 42 */       while ((line = br.readLine()) != null)
/*    */       {
/* 43 */         int idx = line.indexOf('=');
/* 44 */         String key = line.substring(0, idx);
/* 45 */         String value = line.substring(idx + 1);
/* 46 */         envVars.setProperty(key, value);
/*    */       }
/* 48 */       return envVars;
/*    */     }
/*    */     catch (Exception e) {
/* 51 */       e.printStackTrace();
/* 52 */     }return null;
/*    */   }
/*    */ 
/*    */   public static String getEnv(String key)
/*    */   {
/* 59 */     Properties env = getEnv();
/* 60 */     if (env != null) {
/* 61 */       return env.getProperty(key);
/*    */     }
/* 63 */     return null;
/*    */   }
/*    */ 
/*    */   public static void setDragonHome(String home) {
/* 67 */     DRAGONHOME = home;
/*    */   }
/*    */ 
/*    */   public static String getDragonHome() {
/* 71 */     if (DRAGONHOME != null) {
/* 72 */       return DRAGONHOME;
/*    */     }
/* 74 */     return getEnv("DRAGONTOOL");
/*    */   }
/*    */ 
/*    */   public static void setCharSet(String charSet) {
/* 78 */     CHARSET = charSet;
/*    */   }
/*    */ 
/*    */   public static String getCharSet()
/*    */   {
/* 83 */     if (CHARSET != null)
/* 84 */       return CHARSET;
/* 85 */     String charSet = getEnv("DRAGONCHARSET");
/* 86 */     if ((charSet != null) && (charSet.trim().length() == 0))
/* 87 */       charSet = null;
/* 88 */     return charSet;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.util.EnvVariable
 * JD-Core Version:    0.6.2
 */