/*    */ package edu.drexel.cis.dragon.nlp.ontology.mesh;
/*    */ 
/*    */ import edu.drexel.cis.dragon.util.FileUtil;
/*    */ import edu.drexel.cis.dragon.util.SortedArray;
/*    */ import java.io.BufferedReader;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ 
/*    */ public class MeshNodeList extends SortedArray
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public MeshNodeList(String fileName)
/*    */   {
/* 20 */     loadMeshNodeList(fileName);
/*    */   }
/*    */ 
/*    */   public MeshNode lookup(String path)
/*    */   {
/* 26 */     int pos = binarySearch(new MeshNode(path));
/* 27 */     if (pos < 0) {
/* 28 */       return null;
/*    */     }
/* 30 */     return (MeshNode)get(pos);
/*    */   }
/*    */ 
/*    */   public MeshNode lookup(MeshNode node)
/*    */   {
/* 37 */     int pos = binarySearch(node.getPath());
/* 38 */     if (pos < 0) {
/* 39 */       return null;
/*    */     }
/* 41 */     return (MeshNode)get(pos);
/*    */   }
/*    */ 
/*    */   private boolean loadMeshNodeList(String filename)
/*    */   {
/*    */     try
/*    */     {
/* 54 */       BufferedReader br = FileUtil.getTextReader(filename);
/* 55 */       String line = br.readLine();
/* 56 */       int total = Integer.parseInt(line);
/* 57 */       ArrayList list = new ArrayList(total);
/*    */ 
/* 59 */       for (int i = 0; i < total; i++) {
/* 60 */         line = br.readLine();
/* 61 */         String[] arrField = line.split(";");
/* 62 */         MeshNode cur = new MeshNode(arrField[0], arrField[1]);
/* 63 */         list.add(cur);
/*    */       }
/* 65 */       br.close();
/* 66 */       Collections.sort(list);
/* 67 */       addAll(list);
/*    */ 
/* 70 */       for (int i = 0; i < list.size(); i++) {
/* 71 */         MeshNode cur = (MeshNode)list.get(i);
/* 72 */         String path = cur.getPath();
/* 73 */         int startPos = path.indexOf('.');
/* 74 */         while (startPos >= 0) {
/* 75 */           MeshNode parent = lookup(path.substring(0, startPos));
/* 76 */           if (parent != null)
/* 77 */             parent.setDescendantNum(parent.getDescendantNum() + 1);
/* 78 */           startPos = path.indexOf('.', startPos + 1);
/*    */         }
/*    */       }
/* 81 */       return true;
/*    */     }
/*    */     catch (Exception e) {
/* 84 */       e.printStackTrace();
/* 85 */     }return false;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.ontology.mesh.MeshNodeList
 * JD-Core Version:    0.6.2
 */