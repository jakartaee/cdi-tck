<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<html>
 <head>
  <title>Managed Bean Test Page</title>
 </head>
 <body>
   <f:view>
     <h1>
      <h:outputText value="#{farm.sheepInjected ? 'It works' : 'It is broken'}"/>
     </h1>
   </f:view>
 </body>
</html> 