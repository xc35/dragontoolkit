/*    */ package edu.drexel.cis.dragon.config;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.topicmodel.ModelExcelWriter;
/*    */ import edu.drexel.cis.dragon.ir.topicmodel.TopicModel;
/*    */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class TopicModelAppConfig
/*    */ {
/*    */   public static void main(String[] args)
/*    */   {
/* 24 */     if (args.length != 2) {
/* 25 */       System.out.println("Please input two parameters: configuration xml file and indexing applicaiton id");
/* 26 */       return;
/*    */     }
/*    */ 
/* 29 */     ConfigureNode root = new BasicConfigureNode(args[0]);
/* 30 */     ConfigUtil util = new ConfigUtil();
/* 31 */     ConfigureNode topicNode = util.getConfigureNode(root, "topicmodelapp", Integer.parseInt(args[1]));
/* 32 */     if (topicNode == null)
/* 33 */       return;
/* 34 */     TopicModelAppConfig topicApp = new TopicModelAppConfig();
/* 35 */     topicApp.runTopicModel(topicNode);
/*    */   }
/*    */ 
/*    */   public void runTopicModel(ConfigureNode node)
/*    */   {
/* 44 */     int topicNum = node.getInt("topicnum");
/* 45 */     int top = node.getInt("top", 20);
/* 46 */     int modelID = node.getInt("topicmodel");
/* 47 */     TopicModel topicModel = new TopicModelConfig().getTopicModel(node, modelID);
/* 48 */     String outputFile = node.getString("outputfile");
/* 49 */     if (!outputFile.endsWith(".xls"))
/* 50 */       outputFile = outputFile + ".xls";
/* 51 */     String termKeyFile = node.getString("termkeyfile", null);
/*    */     String[] termKeyList;
/* 52 */     if (termKeyFile == null)
/* 53 */       termKeyList = (String[])null;
/*    */     else
/* 55 */       termKeyList = getTermKeyList(termKeyFile);
/* 56 */     if (!topicModel.estimateModel(topicNum))
/* 57 */       return;
/* 58 */     ModelExcelWriter writer = new ModelExcelWriter();
/* 59 */     writer.write(topicModel, termKeyList, top, outputFile);
/*    */   }
/*    */ 
/*    */   private String[] getTermKeyList(String termKeyFile)
/*    */   {
/* 67 */     SimpleElementList list = new SimpleElementList(termKeyFile, false);
/* 68 */     String[] termKeyList = new String[list.size()];
/* 69 */     for (int i = 0; i < termKeyList.length; i++)
/* 70 */       termKeyList[i] = list.search(i);
/* 71 */     return termKeyList;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.TopicModelAppConfig
 * JD-Core Version:    0.6.2
 */