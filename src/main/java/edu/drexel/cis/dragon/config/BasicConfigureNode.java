/*     */ package edu.drexel.cis.dragon.config;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class BasicConfigureNode
/*     */   implements ConfigureNode
/*     */ {
/*     */   private Node node;
/*     */ 
/*     */   public BasicConfigureNode(String configFile)
/*     */   {
/*     */     try
/*     */     {
/*  25 */       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*  26 */       DocumentBuilder parser = factory.newDocumentBuilder();
/*  27 */       Document doc = parser.parse(new File(configFile));
/*  28 */       this.node = doc.getDocumentElement();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  32 */       e.printStackTrace();
/*  33 */       this.node = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public BasicConfigureNode(Node node) {
/*  38 */     if (node.getNodeName().equalsIgnoreCase("param"))
/*  39 */       this.node = null;
/*     */     else
/*  41 */       this.node = node;
/*     */   }
/*     */ 
/*     */   public ConfigureNode getParentNode() {
/*  45 */     if (this.node.getParentNode() == null)
/*  46 */       return null;
/*  47 */     if (this.node.getParentNode().getParentNode() == null) {
/*  48 */       return null;
/*     */     }
/*  50 */     return new BasicConfigureNode(this.node.getParentNode());
/*     */   }
/*     */ 
/*     */   public ConfigureNode getFirstChild()
/*     */   {
/*  56 */     Node curNode = this.node.getFirstChild();
/*  57 */     while (curNode != null) {
/*  58 */       if (curNode.getNodeName().equalsIgnoreCase("param"))
/*  59 */         curNode = curNode.getNextSibling();
/*     */       else
/*  61 */         return new BasicConfigureNode(curNode);
/*     */     }
/*  63 */     return null;
/*     */   }
/*     */ 
/*     */   public ConfigureNode getNextSibling()
/*     */   {
/*  69 */     Node curNode = this.node.getNextSibling();
/*  70 */     while (curNode != null) {
/*  71 */       if (curNode.getNodeName().equalsIgnoreCase("param"))
/*  72 */         curNode = curNode.getNextSibling();
/*     */       else
/*  74 */         return new BasicConfigureNode(curNode);
/*     */     }
/*  76 */     return null;
/*     */   }
/*     */ 
/*     */   public String getNodeName() {
/*  80 */     return this.node.getNodeName();
/*     */   }
/*     */ 
/*     */   public int getNodeID()
/*     */   {
/*  86 */     if (!this.node.hasAttributes())
/*  87 */       return -1;
/*  88 */     Node curNode = this.node.getAttributes().getNamedItem("id");
/*  89 */     if (curNode == null)
/*  90 */       return -1;
/*  91 */     return Integer.parseInt(curNode.getNodeValue());
/*     */   }
/*     */ 
/*     */   public Class getNodeClass()
/*     */   {
/*  97 */     String className = null;
/*     */     try
/*     */     {
/* 101 */       if (!this.node.hasAttributes()) {
/* 102 */         System.out.println("Please specify the class name the resource corresponds to in the configuration flie!");
/* 103 */         return null;
/*     */       }
/* 105 */       Node curNode = this.node.getAttributes().getNamedItem("class");
/* 106 */       if (curNode == null) {
/* 107 */         System.out.println("Please specify the class name the resource corresponds to in the configuration flie!");
/* 108 */         return null;
/*     */       }
/* 110 */       className = curNode.getNodeValue();
/* 111 */       return Class.forName(className);
/*     */     }
/*     */     catch (Exception e) {
/* 114 */       if (className != null)
/* 115 */         System.out.println("Can't load the class: " + className);
/*     */     }
/* 117 */     return null;
/*     */   }
/*     */ 
/*     */   public String getNodeType()
/*     */   {
/* 124 */     if (!this.node.hasAttributes())
/* 125 */       return "";
/* 126 */     Node curNode = this.node.getAttributes().getNamedItem("type");
/* 127 */     if (curNode == null)
/* 128 */       return "";
/* 129 */     return curNode.getNodeValue();
/*     */   }
/*     */ 
/*     */   public String getString(String key) {
/* 133 */     return getParam(key);
/*     */   }
/*     */ 
/*     */   public String getString(String key, String def)
/*     */   {
/* 139 */     String ret = getParam(key);
/* 140 */     if (ret == null) {
/* 141 */       return def;
/*     */     }
/* 143 */     return ret;
/*     */   }
/*     */ 
/*     */   public int getInt(String key)
/*     */   {
/* 149 */     String ret = getParam(key);
/* 150 */     if (ret == null) {
/* 151 */       return 0;
/*     */     }
/* 153 */     return Integer.parseInt(ret);
/*     */   }
/*     */ 
/*     */   public int getInt(String key, int def)
/*     */   {
/* 159 */     String ret = getParam(key);
/* 160 */     if (ret == null) {
/* 161 */       return def;
/*     */     }
/* 163 */     return Integer.parseInt(ret);
/*     */   }
/*     */ 
/*     */   public boolean getBoolean(String key)
/*     */   {
/* 169 */     String ret = getParam(key);
/* 170 */     if (ret == null) {
/* 171 */       return false;
/*     */     }
/* 173 */     return Boolean.getBoolean(ret);
/*     */   }
/*     */ 
/*     */   public boolean getBoolean(String key, boolean def)
/*     */   {
/* 179 */     String ret = getParam(key);
/* 180 */     if (ret == null) {
/* 181 */       return def;
/*     */     }
/* 183 */     return new Boolean(ret).booleanValue();
/*     */   }
/*     */ 
/*     */   public double getDouble(String key)
/*     */   {
/* 189 */     String ret = getParam(key);
/* 190 */     if (ret == null) {
/* 191 */       return 0.0D;
/*     */     }
/* 193 */     return Double.parseDouble(ret);
/*     */   }
/*     */ 
/*     */   public double getDouble(String key, double def)
/*     */   {
/* 200 */     String ret = getParam(key);
/* 201 */     if (ret == null) {
/* 202 */       return def;
/*     */     }
/* 204 */     return Double.parseDouble(ret);
/*     */   }
/*     */ 
/*     */   public boolean exist(String key) {
/* 208 */     return getParam(key) != null;
/*     */   }
/*     */ 
/*     */   private String getParam(String key)
/*     */   {
/* 214 */     Node curNode = this.node.getFirstChild();
/* 215 */     while (curNode != null) {
/* 216 */       if ((curNode.getNodeName().equalsIgnoreCase("param")) && 
/* 217 */         (curNode.hasAttributes())) {
/* 218 */         Node attribute = curNode.getAttributes().getNamedItem("name");
/* 219 */         if ((attribute != null) && (attribute.getNodeValue().equalsIgnoreCase(key))) {
/* 220 */           attribute = curNode.getAttributes().getNamedItem("value");
/* 221 */           if (attribute == null) {
/* 222 */             return null;
/*     */           }
/* 224 */           return attribute.getNodeValue();
/*     */         }
/*     */       }
/*     */ 
/* 228 */       curNode = curNode.getNextSibling();
/*     */     }
/* 230 */     return null;
/*     */   }
/*     */ 
/*     */   public String getParameterType(String key) {
/* 234 */     return getParameterType(key, null);
/*     */   }
/*     */ 
/*     */   public String getParameterType(String key, String defType)
/*     */   {
/* 240 */     Node curNode = this.node.getFirstChild();
/* 241 */     while (curNode != null) {
/* 242 */       if ((curNode.getNodeName().equalsIgnoreCase("param")) && 
/* 243 */         (curNode.hasAttributes())) {
/* 244 */         Node attribute = curNode.getAttributes().getNamedItem("name");
/* 245 */         if ((attribute != null) && (attribute.getNodeValue().equalsIgnoreCase(key))) {
/* 246 */           attribute = curNode.getAttributes().getNamedItem("type");
/* 247 */           if (attribute == null) {
/* 248 */             return defType;
/*     */           }
/* 250 */           return attribute.getNodeValue();
/*     */         }
/*     */       }
/*     */ 
/* 254 */       curNode = curNode.getNextSibling();
/*     */     }
/* 256 */     return defType;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.BasicConfigureNode
 * JD-Core Version:    0.6.2
 */