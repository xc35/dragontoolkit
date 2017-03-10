/*     */ package edu.drexel.cis.dragon.ir.query;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class RelBoolQuery extends AbstractIRQuery
/*     */ {
/*     */   public RelBoolQuery()
/*     */   {
/*  15 */     this.optr = null;
/*  16 */     this.children = null;
/*     */   }
/*     */ 
/*     */   public RelBoolQuery(String[] expression) {
/*  20 */     parse(expression);
/*     */   }
/*     */ 
/*     */   public RelBoolQuery(String expression) {
/*  24 */     parse(expression);
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*  31 */     String expression = "T((tui=T116 or tui=T028) and string like %aaa%)";
/*  32 */     RelBoolQuery query = new RelBoolQuery(expression);
/*  33 */     System.out.println(query.toString());
/*     */   }
/*     */ 
/*     */   public boolean isRelBoolQuery() {
/*  37 */     return true;
/*     */   }
/*     */ 
/*     */   protected void parse(String[] expression)
/*     */   {
/*  48 */     this.optr = null;
/*  49 */     this.children = new ArrayList();
/*     */ 
/*  51 */     ArrayList andList = new ArrayList();
/*  52 */     int start = 0;
/*  53 */     int end = expression.length - 1;
/*  54 */     boolean found = false;
/*     */ 
/*  56 */     while (!found) {
/*  57 */       int lastOR = start - 1;
/*  58 */       int leftParenthesis = 0;
/*  59 */       int rightParenthesis = 0;
/*  60 */       for (int i = start; i <= end; i++) {
/*  61 */         if (expression[i].equalsIgnoreCase("("))
/*  62 */           leftParenthesis++;
/*  63 */         else if (expression[i].equalsIgnoreCase(")"))
/*  64 */           rightParenthesis++;
/*  65 */         else if (expression[i].equalsIgnoreCase("OR"))
/*     */         {
/*  67 */           if (leftParenthesis == rightParenthesis) {
/*  68 */             found = true;
/*  69 */             String[] part = new String[i - lastOR - 1];
/*  70 */             System.arraycopy(expression, lastOR + 1, part, 0, part.length);
/*  71 */             IRQuery curQuery = new RelBoolQuery(part);
/*  72 */             this.children.add(curQuery);
/*  73 */             lastOR = i;
/*     */           }
/*     */         }
/*  76 */         else if (expression[i].equalsIgnoreCase("AND"))
/*     */         {
/*  78 */           if (leftParenthesis == rightParenthesis) {
/*  79 */             andList.add(new Integer(i));
/*     */           }
/*     */         }
/*     */       }
/*  83 */       if (found) {
/*  84 */         this.optr = new Operator("OR");
/*  85 */         String[] part = new String[end - lastOR];
/*  86 */         System.arraycopy(expression, lastOR + 1, part, 0, part.length);
/*  87 */         IRQuery curQuery = new RelBoolQuery(part);
/*  88 */         this.children.add(curQuery);
/*     */       }
/*  92 */       else if (andList.size() >= 1)
/*     */       {
/*  94 */         found = true;
/*  95 */         this.optr = new Operator("AND");
/*  96 */         int lastAND = start - 1;
/*  97 */         andList.add(new Integer(end + 1));
/*  98 */         for (int i = 0; i < andList.size(); i++) {
/*  99 */           int j = ((Integer)andList.get(i)).intValue();
/* 100 */           String[] part = new String[j - lastAND - 1];
/* 101 */           System.arraycopy(expression, lastAND + 1, part, 0, part.length);
/* 102 */           IRQuery curQuery = new RelBoolQuery(part);
/* 103 */           this.children.add(curQuery);
/* 104 */           lastAND = j;
/*     */         }
/*     */       }
/* 107 */       else if ((expression[start].equalsIgnoreCase("(")) && (expression[end].equalsIgnoreCase(")")))
/*     */       {
/* 109 */         start++;
/* 110 */         end--;
/*     */       }
/*     */       else
/*     */       {
/* 114 */         found = true;
/* 115 */         this.optr = new Operator("AND");
/* 116 */         this.children.add(getPredicate(expression, start, end - start));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private Predicate getPredicate(String[] expression, int start, int length)
/*     */   {
/*     */     double weight;
/*     */     String[] part;
/* 128 */     if (expression[(start + 3)].equalsIgnoreCase(",")) {
/* 129 */        weight = Double.parseDouble(expression[(start + 2)]);
/* 130 */        part = new String[length - 4];
/* 131 */       System.arraycopy(expression, start + 4, part, 0, part.length);
/*     */     }
/*     */     else {
/* 134 */       weight = 1.0D;
/* 135 */       part = new String[length - 2];
/* 136 */       System.arraycopy(expression, start + 2, part, 0, part.length);
/*     */     }
/*     */     Predicate curPredicate;
/* 139 */     if (expression[start].equalsIgnoreCase("T")) {
/* 140 */       curPredicate = new BoolTermPredicate(part);
/*     */     }
/*     */     else
/*     */     {
/* 141 */       if (expression[start].equalsIgnoreCase("R")) {
/* 142 */         curPredicate = new BoolRelationPredicate(part);
/*     */       }
/*     */       else
/*     */       {
/* 143 */         if (expression[start].equalsIgnoreCase("M"))
/* 144 */           curPredicate = new BoolQualifierPredicate(part);
/*     */         else
/* 146 */           return null;
/*     */       }
/*     */     }
/* 148 */     curPredicate.setWeight(weight);
/* 149 */     return curPredicate;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.query.RelBoolQuery
 * JD-Core Version:    0.6.2
 */