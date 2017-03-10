/*    */ package edu.drexel.cis.dragon.ir.index.sequence;
/*    */ 
/*    */ import edu.drexel.cis.dragon.util.FastBinaryReader;
/*    */ import java.io.RandomAccessFile;
/*    */ 
/*    */ public class SequenceFileReader
/*    */   implements SequenceReader
/*    */ {
/*    */   private RandomAccessFile rafMatrix;
/*    */   private long[] arrRowStart;
/*    */   private int rowNum;
/*    */ 
/*    */   public SequenceFileReader(String seqIndexFile, String seqMatrixFile)
/*    */   {
/*    */     try
/*    */     {
/* 22 */       loadIndexFile(seqIndexFile);
/* 23 */       this.rafMatrix = new RandomAccessFile(seqMatrixFile, "r");
/*    */     }
/*    */     catch (Exception e) {
/* 26 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   public void initialize()
/*    */   {
/*    */   }
/*    */ 
/*    */   public int getSequenceLength(int seqIndex) {
/* 35 */     if ((seqIndex < 0) || (seqIndex >= this.rowNum)) {
/* 36 */       return 0;
/*    */     }
/* 38 */     return (int)((this.arrRowStart[(seqIndex + 1)] - this.arrRowStart[seqIndex] - 8L) / 4L);
/*    */   }
/*    */ 
/*    */   public int[] getSequence(int seqIndex)
/*    */   {
/*    */     try
/*    */     {
/* 46 */       int len = getSequenceLength(seqIndex);
/* 47 */       if (len <= 0)
/* 48 */         return null;
/* 49 */       this.rafMatrix.seek(this.arrRowStart[seqIndex] + 8L);
/* 50 */       FastBinaryReader fbr = new FastBinaryReader(this.rafMatrix, len * 4);
/* 51 */       int[] arrSeq = new int[len];
/* 52 */       for (int i = 0; i < len; i++)
/* 53 */         arrSeq[i] = fbr.readInt();
/* 54 */       return arrSeq;
/*    */     }
/*    */     catch (Exception e) {
/* 57 */       e.printStackTrace();
/* 58 */     }return null;
/*    */   }
/*    */ 
/*    */   public void close()
/*    */   {
/*    */     try {
/* 64 */       if (this.rafMatrix != null)
/* 65 */         this.rafMatrix.close();
/*    */     }
/*    */     catch (Exception e) {
/* 68 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   private void loadIndexFile(String indexFile)
/*    */   {
/*    */     try
/*    */     {
/* 77 */       int len = 0;
/* 78 */       FastBinaryReader fbr = new FastBinaryReader(indexFile);
/* 79 */       this.rowNum = fbr.readInt();
/* 80 */       fbr.readInt();
/* 81 */       fbr.readInt();
/* 82 */       this.arrRowStart = new long[this.rowNum + 1];
/* 83 */       for (int i = 0; i < this.rowNum; i++) {
/* 84 */         fbr.readInt();
/* 85 */         this.arrRowStart[i] = fbr.readLong();
/* 86 */         len = fbr.readInt();
/*    */       }
/* 88 */       this.arrRowStart[this.rowNum] = (this.arrRowStart[(this.rowNum - 1)] + (len + 2) * 4);
/*    */     }
/*    */     catch (Exception e) {
/* 91 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.sequence.SequenceFileReader
 * JD-Core Version:    0.6.2
 */