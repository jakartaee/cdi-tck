package org.jboss.jsr299.tck.tests.context.conversation.client;

import java.util.HashSet;
import java.util.Set;

import org.jboss.jsr299.tck.AbstractJSR299Test;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public abstract class AbstractConversationTest extends AbstractJSR299Test
{
   
   public static final String CID_REQUEST_PARAMETER_NAME = "cid";
   
   public static final String CID_HEADER_NAME = "org.jboss.jsr299.tck.cid";
   
   public static final String LONG_RUNNING_HEADER_NAME = "org.jboss.jsr299.tck.longRunning";
   
   protected boolean isCloudDestroyed(WebClient client) throws Exception
   {
      client.setThrowExceptionOnFailingStatusCode(false);
      Page page = client.getPage(getConversationStatusPath("cloudDestroyed"));
      return page.getWebResponse().getStatusCode() == 200;
   }
   
   protected void resetCloud(WebClient client) throws Exception
   {
      client.getPage(getConversationStatusPath("resetCloud"));
   }
   
   protected void invalidateSession(WebClient client) throws Exception
   {
      client.getPage(getConversationStatusPath("invalidateSession"));
   }
   
   protected String getConversationStatusPath(String method)
   {
      return super.getContextPath() + "conversation-status?method=" + method;
   }
   
   protected String getPath(String viewId)
   {
      return getContextPath() + viewId;
   }
   
   protected String getPath(String viewId, String cid)
   {
      return getContextPath() + viewId + "?" + CID_REQUEST_PARAMETER_NAME + "=" + cid;
   }
   
   protected <T> Set<T> getElements(HtmlElement rootElement, Class<T> elementClass)
   {
     Set<T> result = new HashSet<T>();
     
     for (HtmlElement element : rootElement.getAllHtmlChildElements())
     {
        result.addAll(getElements(element, elementClass));
     }
     
     if (elementClass.isInstance(rootElement))
     {
        result.add(elementClass.cast(rootElement));
     }
     return result;
     
   }
 
   protected <T extends HtmlElement> T getFirstMatchingElement(HtmlPage page, Class<T> elementClass, String id)
   {
     
     Set<T> inputs = getElements(page.getBody(), elementClass);
     for (T input : inputs)
     {
         if (input.getId().contains(id))
         {
            return input;
         }
     }
     return null;
   }
   
   protected String getCid(Page page)
   {
      return page.getWebResponse().getResponseHeaderValue(AbstractConversationTest.CID_HEADER_NAME);
   }
   
   protected Boolean hasRained(Page page)
   {
      return Boolean.valueOf(page.getWebResponse().getResponseHeaderValue(Cloud.RAINED_HEADER_NAME));
   }
   
   protected Boolean isLongRunning(Page page)
   {
      return Boolean.valueOf(page.getWebResponse().getResponseHeaderValue(LONG_RUNNING_HEADER_NAME));
   }
   
   
}
