/*     */ package edu.drexel.cis.dragon.ir.query;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public abstract class AbstractIRQuery
/*     */   implements IRQuery
/*     */ {
/*     */   protected Operator optr;
/*     */   protected ArrayList children;
/*     */   private int queryKey;
/*     */ 
/*     */   public int getQueryKey()
/*     */   {
/*  22 */     return this.queryKey;
/*     */   }
/*     */ 
/*     */   public void setQueryKey(int key) {
/*  26 */     this.queryKey = key;
/*     */   }
/*     */ 
/*     */   public boolean isPredicate() {
/*  30 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isCompoundQuery() {
/*  34 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isRelSimpleQuery() {
/*  38 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isRelBoolQuery() {
/*  42 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean parse(String query) {
/*  46 */     parse(getTokenList(query));
/*  47 */     return true;
/*     */   }
/*     */ 
/*     */   public int getChildNum() {
/*  51 */     if (this.children == null) {
/*  52 */       return 0;
/*     */     }
/*  54 */     return this.children.size();
/*     */   }
/*     */ 
/*     */   public IRQuery getChild(int index) {
/*  58 */     if ((index >= getChildNum()) || (index < 0))
/*  59 */       return null;
/*  60 */     return (IRQuery)this.children.get(index);
/*     */   }
/*     */ 
/*     */   public double getSelectivity() {
/*  64 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   public Operator getOperator() {
/*  68 */     return this.optr;
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  72 */     if (getChildNum() == 0)
/*  73 */       return null;
/*  74 */     if (getChildNum() == 1) {
/*  75 */       return getChild(0).toString();
/*     */     }
/*     */ 
/*  78 */     StringBuffer buf = new StringBuffer();
/*  79 */     if (this.optr.toString().equalsIgnoreCase("OR")) buf.append('(');
/*  80 */     for (int i = 0; i < getChildNum(); i++)
/*     */     {
/*  82 */       buf.append(getChild(i).toString());
/*  83 */       if (i < getChildNum() - 1) {
/*  84 */         buf.append(' ');
/*  85 */         buf.append(getOperator().toString());
/*  86 */         buf.append(' ');
/*     */       }
/*     */     }
/*  89 */     if (this.optr.toString().equalsIgnoreCase("OR")) buf.append(')');
/*  90 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   protected String[] getTokenList(String query)
/*     */   {
/* 100 */     boolean lastSpace = false;
/* 101 */     StringBuffer sb = new StringBuffer(query.length());
/* 102 */     for (int i = 0; i < query.length(); i++)
/*     */     {
/* 104 */       if ("()=,".indexOf(query.charAt(i)) >= 0)
/*     */       {
/* 106 */         if (!lastSpace)
/* 107 */           sb.append(' ');
/* 108 */         sb.append(query.charAt(i));
/* 109 */         sb.append(' ');
/* 110 */         lastSpace = true;
/*     */       }
/* 112 */       else if (query.charAt(i) != ' ')
/*     */       {
/* 114 */         sb.append(query.charAt(i));
/* 115 */         lastSpace = false;
/*     */       }
/* 119 */       else if (!lastSpace)
/*     */       {
/* 123 */         sb.append(' ');
/* 124 */         lastSpace = true;
/*     */       }
/*     */     }
/*     */ 
/* 128 */     query = sb.toString().trim();
/* 129 */     String[] arrToken = query.split(" ");
/* 130 */     return arrToken;
/*     */   }
/*     */ 
/*     */   protected abstract void parse(String[] paramArrayOfString);
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.query.AbstractIRQuery
 * JD-Core Version:    0.6.2
 */