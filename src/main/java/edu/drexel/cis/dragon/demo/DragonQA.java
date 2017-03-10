package edu.drexel.cis.dragon.demo;


/*     */ import edu.drexel.cis.dragon.util.*;
/*     */ import edu.drexel.cis.dragon.qa.query.QuestionQuery;
/*     */ import edu.drexel.cis.dragon.qa.system.Candidate;
/*     */ import edu.drexel.cis.dragon.qa.system.QASystem;
/*     */ import edu.drexel.cis.dragon.qa.system.SupportingDoc;
/*     */ import edu.drexel.cis.dragon.qa.system.WebQASystem;
/*     */ import edu.drexel.cis.dragon.util.EnvVariable;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.text.DecimalFormat;
import java.util.ArrayList;
/*     */ 
/*     */ public class DragonQA 
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
/*     */   private QASystem controller;
/*     */   private DecimalFormat df;
/*     */   public DragonQA(String dragonHome){
					edu.drexel.cis.dragon.util.EnvVariable.setDragonHome(dragonHome);
	  				this.init();
			}
/*     */   public void init()
/*     */   {
/*  29 */     this.controller = new WebQASystem(EnvVariable.getDragonHome() + "/nlpdata/qa");
/*  30 */     this.df = new DecimalFormat();
/*  31 */     this.df.setMaximumFractionDigits(2);
/*  32 */     this.df.setMinimumFractionDigits(2);
/*  33 */     this.df.setMinimumIntegerDigits(1);
/*     */   }
/*     */ 
/*     */   public void getQuestion(String question)
/*     */   {
/*     */     try {
/*     */       String query;
/*     */       ArrayList list;
/*  49 */       if ((question != null) && (question.trim().length() > 0)) {
/*  50 */          list = this.controller.answer(question);
/*  51 */         if (this.controller.getLastQuestionQuery() != null)
/*  52 */           query = this.controller.getLastQuestionQuery().getUsedQuery();
/*     */         else
/*  54 */           query = null;
/*     */       }
/*     */       else {
/*  57 */         list = null;
/*  58 */         query = null;
/*     */       }
/*  70 */       if (list != null) {
/*  72 */         for (int i = 0; i < list.size(); i++) {
/*  74 */           Candidate curTerm = (Candidate)list.get(i);
/*  75 */           ArrayList suppList = this.controller.getSupportingDoc(curTerm);
/*  76 */           SupportingDoc curDoc = (SupportingDoc)suppList.get(0);
/*  80 */           for (int j = 0; j < suppList.size(); j++) {
/*  81 */             curDoc = (SupportingDoc)suppList.get(j);
						System.out.println(curDoc.getSnippet());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  94 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */
/*     */ 
/*     */   public void destroy() {
/* 104 */     this.controller.close();
/*     */   }
			
			public static void main( String[] args ){
				DragonQA QA = new DragonQA("DragonDemo/");
				String question="How tall is Yao Ming?";
				QA.getQuestion(question);
				QA.destroy();
			}


/*     */ }

/* Location:           C:\Users\Zunyan\Desktop\Desktop\
 * Qualified Name:     dragon.demo.DragonQA
 * JD-Core Version:    0.6.2
 */