<%
final String contextPath=request.getContextPath();
%>
<html>
<!--
  Copyright 2004 The Apache Software Foundation

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->


<jsp:useBean id="cart" scope="session" class="demo.cart.DummyCart" />

<jsp:setProperty name="cart" property="*" />

<%
  cart.processRequest(request);

  // session attribute must be set on every request for
  // data to be updated in the cluster
  session.setAttribute("cart", cart);
%>

<%@ page import="java.util.Date" %>
<%@ page import="java.net.InetAddress" %>
<%
    if (request.getParameter("key") != null) {
        request.getSession().setAttribute(String.valueOf(request.getParameter("key")), request.getParameter("val"));
    }
    if (request.getParameter("invalidate") != null) {
        request.getSession().invalidate();
    }
    HttpSession sess = request.getSession(false);

%>

<head><title>Web sessions cart example</title></head>
<body bgcolor="white">



<FONT size = 5 COLOR="#CC0000">
<br> You have the following items in your cart:
<ol>
<% 
   String[] items = cart.getItems();
   for (int i=0; i<items.length; i++) {
%>
   <li> <% out.print(demo.cart.HTMLFilter.filter(items[i])); %> 

<%
   }
%>

</ol>


</FONT>

<hr>

<font size = 5 color="#CC0000">

<form type=POST name="frmItems" id="frmItems">


<BR>
Please enter item to add or remove:
<br>
Add Items:

<SELECT NAME="item">
<OPTION>Beavis & Butt-head Video collection
<OPTION>X-files movie
<OPTION>Twin peaks tapes
<OPTION>NIN CD
<OPTION>JSP Book
<OPTION>Concert tickets
<OPTION>Love life
<OPTION>Switch blade
<OPTION>Rex, Rugs & Rock n' Roll
</SELECT>


<br> <br>

<INPUT TYPE=submit name="submit" value="add" onclick="submitAdd(this.form);">
<INPUT TYPE=submit name="submit" value="remove" onclick="submitRemove(this.form);">

</form>

<script>


var proxypass = "\t";
var appname = "\Cart";

function submitAdd(form){

 if('<%=request.getHeader("X-FORWARDED-FOR")%>' == null){
   form.action = this.appname;
 }
 else {
   form.action = this.proxypass;
 }

 document.getElementById("frmItems").submit();

}

function submitRemove(form){

 if('<%=request.getHeader("X-FORWARDED-FOR")%>' == null){
   form.action = this.appname;
 }
 else {
   form.action = this.proxypass;
 }
 document.getElementById("frmItems").submit();

}
</script>


      
</FONT>


<hr style="margin-top: 10px"> 
<div id="sessionsInfo" style="background:white; font-size: 85%; font-family: sans-serif; border: 3px solid gray; padding: 15px; margin: 50px 80px">
    <table style="font-size: 100%">
        <tr style="vertical-align: top"><td>
        <table style="font-size: 100%">

<!-- - - - - - - - - - - - - -->
<!--  Start: Cluster Demo    -->
<!--  Terracotta, Inc.       -->
<!-- - - - - - - - - - - - - -->
<script>
hostname = "<%=  InetAddress.getLocalHost().getHostName()  %>";
cartSize = <%= cart.getItems().length %>;
isLocalhost8081On = true;
isLocalhost8082On = true;
isLoadBalancerOn = true;
currentPort = <%=request.getServerPort()%>;
localMode =  (currentPort == 8081 || currentPort == 8082) ? true : false;
otherServer = currentPort == 8081 ? 8082 : 8081;
serverColor = currentPort == 8081 ? "goldenrod" : "darkseagreen";
msgNoDsoEmptyCart = 'This web application is currently running in regular non-clustered mode, and your cart is empty.&nbsp;  Use the "add" button to put an item in your cart.<br><br>If you just jumped over from the other server, your cart is empty because the session data is not being shared between servers.&nbsp;  Try running with Terracotta Sessions enabled.';
msgNoDsoFullCart =  'This web application is currently running in regular non-clustered mode, and you have added an item to your cart.&nbsp;  Now click the link on the left to jump over to the other server.&nbsp;  Since the session data is not being shared across servers, your cart will be lost when you jump to the other server.';
msgDsoEmptyCart =   'This web application is currently running clustered on Terracotta Sessions.&nbsp;  Your cart is empty.&nbsp;  Use the "add" button to put an item in your cart.&nbsp;  Terracotta Sessions makes the session data transparently available to the other server, so your cart and its contents will be maintained if you jump to another server.';
msgDsoFullCart  =   'This web application is currently running clustered on Terracotta Sessions, and you have added an item to your cart.<br><br>You can click the link on the left to jump over to the other server.&nbsp;  Since the session data is being shared across servers, your cart will be preserved.<br><br>If you just jumped over from the other server, your cart has maintained its contents because Terracotta Sessions replicated the session data. To view the session data, connect to the cluster using the Developer Console, then click the <bold>Sessions</bold> button to open the <bold>Sessions</bold> panels under the <bold>My application</bold> node.';

rowStart =  "<tr><td style='text-align: right'><nobr>";
rowMiddle = "</nobr></td><td><nobr><b>";
rowEnd =    "</b></nobr></td></tr>";

document.getElementById("sessionsInfo").style.backgroundColor = serverColor;
document.writeln( rowStart + 'Items in Cart:' + rowMiddle + cartSize + rowEnd );
if(localMode) {
    document.writeln( rowStart + "Current Server:" + rowMiddle + currentPort + rowEnd);
    document.writeln( rowStart + 'Go to:' + rowMiddle + '<a href="http://' + location.hostname + ':' + otherServer + '<%=contextPath%>"><b>Server ' + otherServer + '</b></a>' + rowEnd );
} else {
    document.writeln( rowStart + "Current Server:" + rowMiddle + hostname + rowEnd);
    document.writeln( rowStart + 'Go to:' + rowMiddle + '<a href="' + window.location.href +'"><b>Reload page</b></a>' + rowEnd );
}

// document.writeln( rowStart + 'Go to: ' + rowMiddle + '<a href="http://' + location.hostname +'/t"<b>Balancer </b></a>');

</script>
</table></td></tr></table></div>

<% if (sess == null) { %>
<p>NO HTTP SESSION !</p>
<% } else { %>
<p>
    <div>Session ID: <%=sess.getId()%></div>
    <div>Session creation time: <%=new Date(sess.getCreationTime()).toString()%></div>
    <div>Session last access: <%=new Date(sess.getLastAccessedTime()).toString()%></div>
    <div>Hostname: <%= InetAddress.getLocalHost().getHostName() %></div>
</p>
<% } %>

<!-- - - - - - - - - - - - -->
<!--  End: Cluster Demo    -->
<!-- - - - - - - - - - - - -->


</body>
</html>
