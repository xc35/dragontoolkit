/*    */ package edu.drexel.cis.dragon.qa.query;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class QuestionTrim
/*    */ {
/*    */   public String trim(String question)
/*    */   {
/*  7 */     String newQuest = question.replaceAll("the (name|type) of", " ");
/*  8 */     newQuest = newQuest.replaceAll("'(s|) name", " ");
/*  9 */     newQuest = newQuest.replaceAll("(?i)What( )?'s ", "What is ");
/* 10 */     newQuest = newQuest.replaceAll("(?i)Who( )?'s ", "Who is ");
/* 11 */     return newQuest;
/*    */   }
/*    */ 
/*    */   public static void main(String[] args)
/*    */   {
/* 17 */     QuestionTrim trimer = new QuestionTrim();
/* 18 */     System.out.println(trimer.trim("who's a dog?"));
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.query.QuestionTrim
 * JD-Core Version:    0.6.2
 */