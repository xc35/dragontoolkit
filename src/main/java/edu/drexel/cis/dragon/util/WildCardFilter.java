/*     */ package edu.drexel.cis.dragon.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FilenameFilter;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class WildCardFilter
/*     */   implements FilenameFilter
/*     */ {
/*  35 */   String wildPattern = null;
/*  36 */   Vector pattern = new Vector();
/*     */ 
/*  38 */   final String FIND = "find";
/*  39 */   final String EXPECT = "expect";
/*  40 */   final String ANYTHING = "anything";
/*  41 */   final String NOTHING = "nothing";
/*     */ 
/*     */   public WildCardFilter(String wildString) {
/*  44 */     this.wildPattern = wildString;
/*     */ 
/*  48 */     wildString = wildString.toLowerCase();
/*     */ 
/*  52 */     int i = wildString.indexOf("**");
/*  53 */     while (i >= 0) {
/*  54 */       wildString = wildString.substring(0, i + 1) + 
/*  55 */         wildString.substring(i + 2);
/*     */ 
/*  57 */       i = wildString.indexOf("**");
/*     */     }
/*     */ 
/*  62 */     StringTokenizer tokens = new StringTokenizer(wildString, "*", true);
/*  63 */     String token = null;
/*  64 */     while (tokens.hasMoreTokens()) {
/*  65 */       token = tokens.nextToken();
/*     */ 
/*  67 */       if (token.equals("*")) {
/*  68 */         this.pattern.addElement("find");
/*  69 */         if (tokens.hasMoreTokens()) {
/*  70 */           token = tokens.nextToken();
/*  71 */           this.pattern.addElement(token);
/*     */         } else {
/*  73 */           this.pattern.addElement("anything");
/*     */         }
/*     */       } else {
/*  76 */         this.pattern.addElement("expect");
/*  77 */         this.pattern.addElement(token);
/*     */       }
/*     */     }
/*     */ 
/*  81 */     if (!token.equals("*")) {
/*  82 */       this.pattern.addElement("expect");
/*  83 */       this.pattern.addElement("nothing");
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean accept(File dir, String name)
/*     */   {
/*  92 */     String path = dir.getPath();
/*  93 */     if ((!path.endsWith("/")) && (!path.endsWith("\\"))) {
/*  94 */       path = path + File.separator;
/*     */     }
/*  96 */     File tempFile = new File(path, name);
/*     */ 
/*  98 */     if (tempFile.isDirectory()) {
/*  99 */       return true;
/*     */     }
/*     */ 
/* 104 */     name = name.toLowerCase();
/*     */ 
/* 108 */     boolean acceptName = true;
/*     */ 
/* 110 */     String command = null;
/* 111 */     String param = null;
/*     */ 
/* 113 */     int currPos = 0;
/* 114 */     int cmdPos = 0;
/*     */ 
/* 116 */     while (cmdPos < this.pattern.size()) {
/* 117 */       command = (String)this.pattern.elementAt(cmdPos);
/* 118 */       param = (String)this.pattern.elementAt(cmdPos + 1);
/*     */ 
/* 120 */       if (command.equals("find"))
/*     */       {
/* 124 */         if (param.equals("anything"))
/*     */         {
/*     */           break;
/*     */         }
/*     */ 
/* 131 */         int nextPos = name.indexOf(param, currPos);
/* 132 */         if (nextPos >= 0)
/*     */         {
/* 135 */           currPos = nextPos + param.length();
/*     */         } else {
/* 137 */           acceptName = false;
/* 138 */           break;
/*     */         }
/*     */       }
/* 141 */       else if (command.equals("expect"))
/*     */       {
/* 145 */         if (param.equals("nothing")) {
/* 146 */           if (currPos == name.length()) break;
/* 147 */           acceptName = false;
/*     */ 
/* 153 */           break;
/*     */         }
/*     */ 
/* 158 */         int nextPos = name.indexOf(param, currPos);
/* 159 */         if (nextPos != currPos) {
/* 160 */           acceptName = false;
/* 161 */           break;
/*     */         }
/*     */ 
/* 167 */         currPos += param.length();
/*     */       }
/*     */ 
/* 172 */       cmdPos += 2;
/*     */     }
/*     */ 
/* 175 */     return acceptName;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 179 */     return this.wildPattern;
/*     */   }
/*     */ 
/*     */   public String toPattern() {
/* 183 */     StringBuffer out = new StringBuffer();
/*     */ 
/* 185 */     int i = 0;
/* 186 */     while (i < this.pattern.size()) {
/* 187 */       out.append("(");
/* 188 */       out.append((String)this.pattern.elementAt(i));
/* 189 */       out.append(" ");
/* 190 */       out.append((String)this.pattern.elementAt(i + 1));
/* 191 */       out.append(") ");
/*     */ 
/* 193 */       i += 2;
/*     */     }
/*     */ 
/* 196 */     return out.toString();
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.util.WildCardFilter
 * JD-Core Version:    0.6.2
 */