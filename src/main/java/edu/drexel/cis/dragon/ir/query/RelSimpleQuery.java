/*     */ package edu.drexel.cis.dragon.ir.query;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class RelSimpleQuery extends AbstractIRQuery
/*     */ {
/*     */   public RelSimpleQuery()
/*     */   {
/*  15 */     this.children = null;
/*  16 */     this.optr = null;
/*     */   }
/*     */ 
/*     */   public RelSimpleQuery(String query) {
/*  20 */     parse(query);
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*  27 */     String expression = "R(1.0,TERM1=C0007114 AND TERM2=C0012899) T(0.3,TERM=Y0000004)";
/*  28 */     RelSimpleQuery query = new RelSimpleQuery(expression);
/*  29 */     for (int i = 0; i < query.getChildNum(); i++)
/*  30 */       System.out.println(query.toString());
/*     */   }
/*     */ 
/*     */   public boolean isRelSimpleQuery() {
/*  34 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean add(Predicate predicate) {
/*  38 */     if (!predicate.isSimplePredicate())
/*  39 */       return false;
/*  40 */     if (this.children == null)
/*  41 */       this.children = new ArrayList();
/*  42 */     this.children.add(predicate);
/*  43 */     return true;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  50 */     if (getChildNum() == 0)
/*  51 */       return null;
/*  52 */     if (getChildNum() == 1) {
/*  53 */       return getChild(0).toString();
/*     */     }
/*  55 */     StringBuffer sb = new StringBuffer();
/*  56 */     for (int i = 0; i < getChildNum(); i++) {
/*  57 */       if (i > 0)
/*  58 */         sb.append(' ');
/*  59 */       sb.append(getChild(i).toString());
/*     */     }
/*  61 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   protected void parse(String[] arrToken)
/*     */   {
/*  69 */     this.optr = new Operator("OR");
/*  70 */     this.children = new ArrayList();
/*  71 */     int i = 0;
/*  72 */     int flag = 0;
/*  73 */     int leftParenthesis = 0;
/*  74 */     int rightParenthesis = 0;
/*  75 */     int start = 0;
/*     */ 
/*  77 */     while (i < arrToken.length)
/*     */     {
/*  79 */       String curToken = arrToken[i];
/*  80 */       if (curToken.equals("(")) {
/*  81 */         if ((i >= 1) && ("TMR".indexOf(arrToken[(i - 1)]) >= 0)) {
/*  82 */           start = i - 1;
/*  83 */           leftParenthesis = 1;
/*  84 */           rightParenthesis = 0;
/*  85 */           flag = 1;
/*     */         }
/*     */         else {
/*  88 */           leftParenthesis++;
/*     */         }
/*  90 */       } else if (curToken.equals(")")) {
/*  91 */         rightParenthesis++;
/*  92 */         if ((flag == 1) && (leftParenthesis == rightParenthesis))
/*     */         {
/*  94 */           flag = 0;
/*  95 */           this.children.add(getPredicate(arrToken, start, i - start));
/*     */         }
/*     */       }
/*  98 */       i++;
/*     */     }
/*     */   }
/*     */ 
/*     */   private Predicate getPredicate(String[] expression, int start, int length)
/*     */   {
/*     */     double weight;
/*     */     String[] part;
/* 107 */     if (expression[(start + 3)].equalsIgnoreCase(",")) {
/* 108 */        weight = Double.parseDouble(expression[(start + 2)]);
/* 109 */        part = new String[length - 4];
/* 110 */       System.arraycopy(expression, start + 4, part, 0, part.length);
/*     */     }
/*     */     else {
/* 113 */       weight = 1.0D;
/* 114 */       part = new String[length - 2];
/* 115 */       System.arraycopy(expression, start + 2, part, 0, part.length);
/*     */     }
/*     */     Predicate predicate;
/* 118 */     if (expression[start].equalsIgnoreCase("T")) {
/* 119 */       predicate = new SimpleTermPredicate(part);
/*     */     }
/*     */     else
/*     */     {
/* 120 */       if (expression[start].equalsIgnoreCase("R"))
/* 121 */         predicate = new SimpleRelationPredicate(part);
/*     */       else
/* 123 */         return null;
/*     */     }
/* 124 */     predicate.setWeight(weight);
/* 125 */     return predicate;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.query.RelSimpleQuery
 * JD-Core Version:    0.6.2
 */