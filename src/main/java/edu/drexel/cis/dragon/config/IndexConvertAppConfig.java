/*    */ package edu.drexel.cis.dragon.config;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IndexConverter;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class IndexConvertAppConfig
/*    */ {
/*    */   public static void main(String[] args)
/*    */   {
/* 23 */     if (args.length != 2) {
/* 24 */       System.out.println("Please input two parameters: configuration xml file and indexing applicaiton id");
/* 25 */       return;
/*    */     }
/*    */ 
/* 28 */     ConfigureNode root = new BasicConfigureNode(args[0]);
/* 29 */     ConfigUtil util = new ConfigUtil();
/* 30 */     ConfigureNode indexAppNode = util.getConfigureNode(root, "indexconvertapp", Integer.parseInt(args[1]));
/* 31 */     if (indexAppNode == null)
/* 32 */       return;
/* 33 */     IndexConvertAppConfig indexApp = new IndexConvertAppConfig();
/* 34 */     indexApp.convert(indexAppNode);
/*    */   }
/*    */ 
/*    */   public void convert(ConfigureNode node)
/*    */   {
/* 42 */     String task = node.getString("task");
/* 43 */     String indexFolder = node.getString("indexfolder");
/* 44 */     if ((task == null) || (indexFolder == null))
/* 45 */       return;
/* 46 */     String doctermFile = node.getString("doctermfile");
/* 47 */     String doclinkFile = node.getString("doclinkagefile");
/* 48 */     boolean outputTransposedMatrix = node.getBoolean("gentransposedmatrix", false);
/* 49 */     IndexConverter converter = new IndexConverter();
/* 50 */     if (task.equalsIgnoreCase("import")) {
/* 51 */       if ((doctermFile != null) && (doctermFile.trim().length() > 0))
/* 52 */         converter.importIndex(indexFolder, doctermFile);
/* 53 */       if ((doclinkFile != null) && (doclinkFile.trim().length() > 0))
/* 54 */         converter.importDocLinkage(indexFolder, doclinkFile, outputTransposedMatrix);
/*    */     }
/* 56 */     else if (task.equalsIgnoreCase("export")) {
/* 57 */       if ((doctermFile != null) && (doctermFile.trim().length() > 0))
/* 58 */         converter.exportIndex(indexFolder, doctermFile);
/* 59 */       if ((doclinkFile != null) && (doclinkFile.trim().length() > 0))
/* 60 */         converter.exportDocLinkage(indexFolder, doclinkFile);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.IndexConvertAppConfig
 * JD-Core Version:    0.6.2
 */