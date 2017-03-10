/*    */ package edu.drexel.cis.dragon.onlinedb.trec;
/*    */ 
/*    */ import edu.drexel.cis.dragon.onlinedb.BasicArticle;
/*    */ import java.util.ArrayList;
/*    */ import javax.xml.parsers.DocumentBuilder;
/*    */ import javax.xml.parsers.DocumentBuilderFactory;
/*    */ import org.w3c.dom.Document;
/*    */ import org.w3c.dom.Node;
/*    */ import org.w3c.dom.NodeList;
/*    */ 
/*    */ public class Genomics2004TopicReader extends AbstractTopicReader
/*    */ {
/*    */   public Genomics2004TopicReader(String topicFile)
/*    */   {
/* 19 */     super(topicFile);
/*    */   }
/*    */ 
/*    */   protected ArrayList loadTopics(String topicFile)
/*    */   {
/*    */     try
/*    */     {
/* 33 */       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/* 34 */       DocumentBuilder parser = factory.newDocumentBuilder();
/* 35 */       Document doc = parser.parse(topicFile);
/* 36 */       NodeList topics = doc.getElementsByTagName("TOPIC");
/* 37 */       ArrayList query = new ArrayList(topics.getLength());
/* 38 */       for (int i = 0; i < topics.getLength(); i++) {
/* 39 */         BasicArticle cur = new BasicArticle();
/* 40 */         cur.setKey(String.valueOf(i + 1));
/* 41 */         cur.setCategory(i + 1);
/* 42 */         Node topic = topics.item(i);
/* 43 */         NodeList children = topic.getChildNodes();
/* 44 */         cur.setTitle(children.item(3).getFirstChild().getNodeValue() + ".");
/* 45 */         cur.setAbstract(children.item(5).getFirstChild().getNodeValue());
/* 46 */         cur.setBody(children.item(7).getFirstChild().getNodeValue());
/* 47 */         query.add(cur);
/*    */       }
/* 49 */       return query;
/*    */     }
/*    */     catch (Exception e) {
/* 52 */       e.printStackTrace();
/* 53 */     }return null;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.trec.Genomics2004TopicReader
 * JD-Core Version:    0.6.2
 */