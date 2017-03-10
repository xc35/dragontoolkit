/*     */ package edu.drexel.cis.dragon.util;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ import org.apache.commons.httpclient.HostConfiguration;
/*     */ import org.apache.commons.httpclient.HttpClient;
/*     */ import org.apache.commons.httpclient.HttpConnectionManager;
/*     */ import org.apache.commons.httpclient.methods.GetMethod;
/*     */ import org.apache.commons.httpclient.params.HttpClientParams;
/*     */ import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
/*     */ import org.apache.commons.httpclient.params.HttpMethodParams;
/*     */ 
/*     */ public class HttpUtil
/*     */ {
/*     */   private HttpClient http;
/*     */   private String defaultCharSet;
/*     */   private String lastCharSet;
/*     */   private byte[] buf;
/*     */   private boolean autoRefresh;
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*  29 */     HttpUtil web = new HttpUtil("www.google.com");
/*  30 */     web.setAutoRefresh(true);
/*  31 */     String content = web.get("/search?q=killed+abraham+lincoln&hl=en&newwindow=1&rlz=1T4GZHY_enUS237US237&start=40&sa=N");
/*  32 */     FileUtil.saveTextFile("test.txt", content, "UTF-16LE");
/*  33 */     FileUtil.saveTextFile("test_notag.txt", new HttpContent().extractText(content), "UTF-16LE");
/*     */   }
/*     */ 
/*     */   public HttpUtil(String host) {
/*  37 */     this(host, 80, null);
/*     */   }
/*     */ 
/*     */   public HttpUtil(String host, String charSet) {
/*  41 */     this(host, 80, charSet);
/*     */   }
/*     */ 
/*     */   public HttpUtil(String host, int port) {
/*  45 */     this(host, port, null);
/*     */   }
/*     */ 
/*     */   public HttpUtil(String host, int port, String charSet)
/*     */   {
/*  51 */     this.buf = new byte[1048576];
/*  52 */     this.lastCharSet = null;
/*  53 */     this.autoRefresh = false;
/*  54 */     this.defaultCharSet = charSet;
/*  55 */     this.http = new HttpClient();
/*  56 */     HostConfiguration hostConfig = new HostConfiguration();
/*  57 */     hostConfig.setHost(host, port);
/*  58 */     this.http.setHostConfiguration(hostConfig);
/*  59 */     setSocketTimeout(10000);
/*  60 */     setConnectionTimeout(10000);
/*     */   }
/*     */ 
/*     */   public void setHost(String host) {
/*  64 */     setHost(host, 80, null);
/*     */   }
/*     */ 
/*     */   public void setHost(String host, String charSet) {
/*  68 */     setHost(host, 80, charSet);
/*     */   }
/*     */ 
/*     */   public void setHost(String host, int port) {
/*  72 */     setHost(host, port, this.defaultCharSet);
/*     */   }
/*     */ 
/*     */   public void setHost(String host, int port, String charSet)
/*     */   {
/*  78 */     this.defaultCharSet = charSet;
/*  79 */     HostConfiguration hostConfig = this.http.getHostConfiguration();
/*  80 */     hostConfig.setHost(host, port);
/*  81 */     this.http.setHostConfiguration(hostConfig);
/*     */   }
/*     */ 
/*     */   public void setAutoRefresh(boolean enable) {
/*  85 */     this.autoRefresh = enable;
/*     */   }
/*     */ 
/*     */   public boolean getAutoRefresh() {
/*  89 */     return this.autoRefresh;
/*     */   }
/*     */ 
/*     */   public String getHost() {
/*  93 */     return this.http.getHostConfiguration().getHost();
/*     */   }
/*     */ 
/*     */   public int getPort() {
/*  97 */     return this.http.getHostConfiguration().getPort();
/*     */   }
/*     */ 
/*     */   public void setConnectionTimeout(int time) {
/* 101 */     this.http.getHttpConnectionManager().getParams().setConnectionTimeout(time);
/*     */   }
/*     */ 
/*     */   public int getConnectionTimeout() {
/* 105 */     return this.http.getHttpConnectionManager().getParams().getConnectionTimeout();
/*     */   }
/*     */ 
/*     */   public void setSocketTimeout(int time) {
/* 109 */     this.http.getParams().setParameter("http.socket.timeout", new Integer(time));
/*     */   }
/*     */ 
/*     */   public int getSocketTimeout() {
/* 113 */     return ((Integer)this.http.getParams().getParameter("http.socket.timeout")).intValue();
/*     */   }
/*     */ 
/*     */   public String getCharSet()
/*     */   {
/* 121 */     return this.lastCharSet;
/*     */   }
/*     */ 
/*     */   public String get(String url) {
/* 125 */     return get(url, null);
/*     */   }
/*     */ 
/*     */   public String get(String url, String charSet)
/*     */   {
/* 132 */     String content = internalGet(url, charSet);
/* 133 */     if ((content == null) || (!this.autoRefresh))
/* 134 */       return content;
/* 135 */     URL newUrl = getDirectedURL(content);
/* 136 */     if (newUrl == null) {
/* 137 */       return content;
/*     */     }
/* 139 */     if (newUrl.getHost() != "") {
/* 140 */       if (newUrl.getPort() > 0)
/* 141 */         setHost(newUrl.getHost(), newUrl.getPort());
/*     */       else
/* 143 */         setHost(newUrl.getHost());
/*     */     }
/* 145 */     url = newUrl.getFile();
/* 146 */     while (url.charAt(0) == '.')
/* 147 */       url = url.substring(1);
/* 148 */     if (url.charAt(0) != '/')
/* 149 */       url = "/" + url;
/* 150 */     return internalGet(url, charSet);
/*     */   }
/*     */ 
/*     */   private URL getDirectedURL(String message)
/*     */   {
/*     */     try
/*     */     {
/* 158 */       if ((message == null) || (message.length() >= 512))
/* 159 */         return null;
/* 160 */       message = message.toLowerCase();
/* 161 */       int start = message.indexOf("http-equiv=\"refresh\"");
/* 162 */       if (start < 0)
/* 163 */         start = message.indexOf("http-equiv='refresh'");
/* 164 */       if (start < 0)
/* 165 */         return null;
/* 166 */       int end = message.indexOf(">", start);
/* 167 */       if (end < 0)
/* 168 */         return null;
/* 169 */       message = message.substring(start, end);
/* 170 */       start = message.indexOf("url=");
/* 171 */       if (start < 0)
/* 172 */         return null;
/* 173 */       start += 4;
/* 174 */       end = message.indexOf("\"", start);
/* 175 */       if (end < 0)
/* 176 */         end = message.indexOf("'", start);
/* 177 */       message = message.substring(start, end);
/* 178 */       if (!message.startsWith("http"))
/* 179 */         message = "http:" + message;
/* 180 */       return new URL(message);
/*     */     } catch (Exception e) {
/*     */     }
/* 183 */     return null;
/*     */   }
/*     */ 
/*     */   private String internalGet(String url, String charSet)
/*     */   {
/* 192 */     GetMethod method = null;
/*     */     try {
/* 194 */       method = new GetMethod(url);
/* 195 */       method.getParams().setCookiePolicy("compatibility");
/* 196 */       this.http.executeMethod(method);
/* 197 */       if (method.getStatusCode() != 200) {
/* 198 */         method.releaseConnection();
/* 199 */         return null;
/*     */       }
/*     */ 
/* 202 */       int len = read(method.getResponseBodyAsStream(), this.buf);
/* 203 */       String curCharSet = recognizeStreamEncode(this.buf);
/* 204 */       if (curCharSet == null)
/* 205 */         curCharSet = charSet;
/* 206 */       if (curCharSet == null)
/* 207 */         curCharSet = method.getResponseCharSet();
/* 208 */       if ((curCharSet != null) && (curCharSet.equalsIgnoreCase("ISO-8859-1")))
/* 209 */         curCharSet = null;
/*     */       String content;
/* 211 */       if (curCharSet != null) {
/* 212 */         content = new String(this.buf, 0, len, charsetNameConvert(curCharSet));
/*     */       }
/*     */       else
/*     */       {
/* 214 */         if (this.defaultCharSet != null) {
/* 215 */           curCharSet = this.defaultCharSet;
/* 216 */           content = new String(this.buf, 0, len, charsetNameConvert(curCharSet));
/*     */         }
/*     */         else {
/* 219 */           content = new String(this.buf, 0, len);
/* 220 */         }charSet = readCharSet(content);
/* 221 */         if ((charSet != null) && (!compatibleCharSet(curCharSet, charSet))) {
/* 222 */           curCharSet = charSet;
/* 223 */           content = new String(this.buf, 0, len, charsetNameConvert(curCharSet));
/*     */         }
/*     */       }
/* 226 */       this.lastCharSet = curCharSet;
/* 227 */       method.releaseConnection();
/* 228 */       return content;
/*     */     }
/*     */     catch (Exception e) {
/* 231 */       if (method != null)
/* 232 */         method.releaseConnection(); 
/*     */     }
/* 233 */     return null;
/*     */   }
/*     */ 
/*     */   private int read(InputStream in, byte[] buf)
/*     */   {
/*     */     try
/*     */     {
/* 241 */       int offset = 0;
/* 242 */       int len = in.read(buf);
/* 243 */       while (len >= 0) {
/* 244 */         offset += len;
/* 245 */         if (offset == buf.length)
/*     */           break;
/* 247 */         len = in.read(buf, offset, buf.length - offset);
/*     */       }
/* 249 */       if (offset == buf.length) {
/* 250 */         System.out.println("Warning: The web page is too big and truncated!");
/*     */       }
/* 252 */       return offset;
/*     */     } catch (Exception e) {
/*     */     }
/* 255 */     return 0;
/*     */   }
/*     */ 
/*     */   private boolean compatibleCharSet(String charsetA, String charsetB)
/*     */   {
/* 260 */     if ((charsetA == null) || (charsetB == null))
/* 261 */       return false;
/* 262 */     if (charsetA.equalsIgnoreCase(charsetB))
/* 263 */       return true;
/* 264 */     if ((charsetA.equalsIgnoreCase("gbk")) && (charsetB.equalsIgnoreCase("gb2312"))) {
/* 265 */       return true;
/*     */     }
/* 267 */     return false;
/*     */   }
/*     */ 
/*     */   private String charsetNameConvert(String charsetName) {
/* 271 */     if (charsetName.equalsIgnoreCase("gb2312")) {
/* 272 */       return "gbk";
/*     */     }
/* 274 */     return charsetName;
/*     */   }
/*     */ 
/*     */   private String recognizeStreamEncode(byte[] buf)
/*     */   {
/*     */     try {
/* 280 */       String hex = ByteArrayConvert.toHexString(buf, 0, 3).toUpperCase();
/* 281 */       if ((hex.startsWith("FFFE")) || (hex.startsWith("3C00")))
/* 282 */         return "UTF-16LE";
/* 283 */       if ((hex.startsWith("FEFF")) || (hex.startsWith("003C")))
/* 284 */         return "UTF-16BE";
/* 285 */       if (hex.startsWith("EFBBBF")) {
/* 286 */         return "UTF-8";
/*     */       }
/* 288 */       return null;
/*     */     } catch (Exception e) {
/*     */     }
/* 291 */     return null;
/*     */   }
/*     */ 
/*     */   private String readCharSet(String message)
/*     */   {
/*     */     try
/*     */     {
/* 299 */       int end = Math.min(message.length(), 2048);
/* 300 */       message = message.substring(0, end).toLowerCase();
/* 301 */       int start = message.indexOf("http-equiv=");
/* 302 */       if (start < 0)
/* 303 */         return null;
/* 304 */       end = message.indexOf(">", start);
/* 305 */       message = message.substring(start, end).trim();
/* 306 */       start = message.indexOf("charset=");
/* 307 */       if (start < 0)
/* 308 */         return null;
/* 309 */       start += 8;
/* 310 */       end = message.indexOf("\"", start);
/* 311 */       if (end < 0)
/* 312 */         end = message.indexOf("'", start);
/* 313 */       if (end < 0) {
/* 314 */         return null;
/*     */       }
/* 316 */       return message.substring(start, end).trim();
/*     */     } catch (Exception e) {
/*     */     }
/* 319 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.util.HttpUtil
 * JD-Core Version:    0.6.2
 */