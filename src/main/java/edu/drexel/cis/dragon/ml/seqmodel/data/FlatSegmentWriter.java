/*    */ package edu.drexel.cis.dragon.ml.seqmodel.data;
/*    */ 
/*    */ import edu.drexel.cis.dragon.util.FileUtil;
/*    */ import java.io.PrintWriter;
/*    */ 
/*    */ public class FlatSegmentWriter
/*    */   implements DataWriter
/*    */ {
/*    */   private PrintWriter out;
/*    */   private LabelConverter labelConverter;
/*    */   private String tagDelimit;
/*    */ 
/*    */   public FlatSegmentWriter(String outFile, LabelConverter labelConverter)
/*    */   {
/* 20 */     this.labelConverter = labelConverter;
/* 21 */     this.out = FileUtil.getPrintWriter(outFile);
/* 22 */     this.tagDelimit = "|";
/*    */   }
/*    */ 
/*    */   public boolean write(Dataset dataset) {
/* 26 */     dataset.startScan();
/* 27 */     while (dataset.hasNext())
/* 28 */       write(dataset.next());
/* 29 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean write(DataSequence dataSeq)
/*    */   {
/*    */     try
/*    */     {
/* 37 */       int segStart = 0;
/* 38 */       while (segStart < dataSeq.length()) {
/* 39 */         int segEnd = dataSeq.getSegmentEnd(segStart);
/* 40 */         StringBuffer segment = new StringBuffer(dataSeq.getToken(segStart).getContent());
/* 41 */         for (int i = segStart + 1; i <= segEnd; i++)
/* 42 */           segment.append(" " + dataSeq.getToken(i).getContent());
/* 43 */         segment.append(this.tagDelimit);
/* 44 */         segment.append(this.labelConverter.getExternalLabelString(dataSeq.getOriginalLabel(segStart)));
/* 45 */         this.out.println(segment.toString());
/* 46 */         segStart = segEnd + 1;
/*    */       }
/* 48 */       this.out.println();
/* 49 */       return true;
/*    */     }
/*    */     catch (Exception e) {
/* 52 */       e.printStackTrace();
/* 53 */     }return false;
/*    */   }
/*    */ 
/*    */   public void close()
/*    */   {
/* 58 */     this.out.close();
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.data.FlatSegmentWriter
 * JD-Core Version:    0.6.2
 */