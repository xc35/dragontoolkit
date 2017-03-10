/*    */ package edu.drexel.cis.dragon.ir.query;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class BoolExpression extends AbstractExpression
/*    */ {
/*    */   private ArrayList children;
/*    */ 
/*    */   public BoolExpression(String[] expression)
/*    */   {
/* 17 */     this.children = new ArrayList();
/* 18 */     this.expressionType = 1;
/* 19 */     parse(expression);
/*    */   }
/*    */ 
/*    */   public int getChildNum() {
/* 23 */     if (this.children == null) {
/* 24 */       return 0;
/*    */     }
/* 26 */     return this.children.size();
/*    */   }
/*    */ 
/*    */   public Expression getChild(int index) {
/* 30 */     if ((index >= getChildNum()) || (index < 0))
/* 31 */       return null;
/* 32 */     return (Expression)this.children.get(index);
/*    */   }
/*    */ 
/*    */   public Operator getOperator() {
/* 36 */     return this.optr;
/*    */   }
/*    */ 
/*    */   public String toSQLExpression() {
/* 40 */     if (getChildNum() == 0)
/* 41 */       return null;
/* 42 */     if (getChildNum() == 1) {
/* 43 */       return getChild(0).toSQLExpression();
/*    */     }
/*    */ 
/* 46 */     StringBuffer buf = new StringBuffer();
/* 47 */     if (this.optr.toString().equalsIgnoreCase("OR")) buf.append('(');
/* 48 */     for (int i = 0; i < getChildNum(); i++)
/*    */     {
/* 50 */       buf.append(getChild(i).toSQLExpression());
/* 51 */       if (i < getChildNum() - 1) {
/* 52 */         buf.append(' ');
/* 53 */         buf.append(getOperator().toString());
/* 54 */         buf.append(' ');
/*    */       }
/*    */     }
/* 57 */     if (this.optr.toString().equalsIgnoreCase("OR")) buf.append(')');
/* 58 */     return buf.toString();
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 63 */     if (this.children.size() == 1) {
/* 64 */       return getChild(0).toString();
/*    */     }
/*    */ 
/* 67 */     StringBuffer buf = new StringBuffer();
/* 68 */     if (this.optr.toString().equalsIgnoreCase("OR")) buf.append('(');
/* 69 */     for (int i = 0; i < getChildNum(); i++)
/*    */     {
/* 71 */       buf.append(getChild(i).toString());
/* 72 */       if (i < getChildNum() - 1) {
/* 73 */         buf.append(' ');
/* 74 */         buf.append(getOperator().toString());
/* 75 */         buf.append(' ');
/*    */       }
/*    */     }
/* 78 */     if (this.optr.toString().equalsIgnoreCase("OR")) buf.append(')');
/* 79 */     return buf.toString();
/*    */   }
/*    */ 
/*    */   private void parse(String[] expression)
/*    */   {
/* 92 */     ArrayList andList = new ArrayList();
/* 93 */     int start = 0;
/* 94 */     int end = expression.length - 1;
/* 95 */     boolean found = false;
/*    */ 
/* 97 */     while (!found) {
/* 98 */       int lastOR = start - 1;
/* 99 */       int leftParenthesis = 0;
/* 100 */       int rightParenthesis = 0;
/* 101 */       for (int i = start; i <= end; i++) {
/* 102 */         if (expression[i].equalsIgnoreCase("("))
/* 103 */           leftParenthesis++;
/* 104 */         else if (expression[i].equalsIgnoreCase(")"))
/* 105 */           rightParenthesis++;
/* 106 */         else if (expression[i].equalsIgnoreCase("OR"))
/*    */         {
/* 108 */           if (leftParenthesis == rightParenthesis) {
/* 109 */             found = true;
/* 110 */             String[] part = new String[i - lastOR - 1];
/* 111 */             System.arraycopy(expression, lastOR + 1, part, 0, part.length);
/* 112 */             BoolExpression curBoolExpression = new BoolExpression(part);
/* 113 */             this.children.add(curBoolExpression);
/* 114 */             lastOR = i;
/*    */           }
/*    */         }
/* 117 */         else if (expression[i].equalsIgnoreCase("AND"))
/*    */         {
/* 119 */           if (leftParenthesis == rightParenthesis) {
/* 120 */             andList.add(new Integer(i));
/*    */           }
/*    */         }
/*    */       }
/* 124 */       if (found) {
/* 125 */         this.optr = new Operator("OR");
/* 126 */         String[] part = new String[end - lastOR];
/* 127 */         System.arraycopy(expression, lastOR + 1, part, 0, part.length);
/* 128 */         BoolExpression curBoolExpression = new BoolExpression(part);
/* 129 */         this.children.add(curBoolExpression);
/*    */       }
/* 133 */       else if (andList.size() >= 1)
/*    */       {
/* 135 */         found = true;
/* 136 */         this.optr = new Operator("AND");
/* 137 */         int lastAND = start - 1;
/* 138 */         andList.add(new Integer(end + 1));
/*    */ 
/* 140 */         for (int i = 0; i < andList.size(); i++) {
/* 141 */           int j = ((Integer)andList.get(i)).intValue();
/* 142 */           String[] part = new String[j - lastAND - 1];
/* 143 */           System.arraycopy(expression, lastAND + 1, part, 0, part.length);
/* 144 */           BoolExpression curBoolExpression = new BoolExpression(part);
/* 145 */           this.children.add(curBoolExpression);
/* 146 */           lastAND = j;
/*    */         }
/*    */       }
/* 149 */       else if ((expression[start].equalsIgnoreCase("(")) && (expression[end].equalsIgnoreCase(")")))
/*    */       {
/* 151 */         start++;
/* 152 */         end--;
/*    */       }
/*    */       else
/*    */       {
/* 156 */         this.optr = new Operator("AND");
/* 157 */         found = true;
/* 158 */         SimpleExpression curSimpleExpression = new SimpleExpression(expression);
/* 159 */         this.children.add(curSimpleExpression);
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.query.BoolExpression
 * JD-Core Version:    0.6.2
 */