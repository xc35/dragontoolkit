/*    */ package edu.drexel.cis.dragon.util;
/*    */ 
/*    */ import java.io.DataInputStream;
/*    */ import java.io.File;
/*    */ import java.io.RandomAccessFile;
/*    */ 
/*    */ public class FastBinaryReader extends DataInputStream
/*    */ {
/*    */   public FastBinaryReader(String filename)
/*    */   {
/* 16 */     super(new FastFileInputStream(filename));
/*    */   }
/*    */ 
/*    */   public FastBinaryReader(File file) {
/* 20 */     super(new FastFileInputStream(file));
/*    */   }
/*    */ 
/*    */   public FastBinaryReader(RandomAccessFile raf, long length) {
/* 24 */     super(new FastFileInputStream(raf, length));
/*    */   }
/*    */ 
/*    */   public long remaining() {
/* 28 */     return ((FastFileInputStream)this.in).remaining();
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.util.FastBinaryReader
 * JD-Core Version:    0.6.2
 */