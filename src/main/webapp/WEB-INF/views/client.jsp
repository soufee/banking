<%--
  Created by IntelliJ IDEA.
  User: Shoma
  Date: 18.08.2019
  Time: 14:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<script>
    window.onload = function () {

        function gotPayment(json) {
            console.log(json);
        }

        function sendRequest(url, req, foo, element) {
            var xhr = new XMLHttpRequest();
            xhr.open("POST", url, true);
            xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
            xhr.onreadystatechange = function () {
                if (xhr.readyState == XMLHttpRequest.DONE) {
                    gotJson = xhr.responseText;
                    foo(gotJson, element);
                }
            };
            xhr.send(req);
            return false;
        }
        let url = "${pageContext.request.contextPath}/payment";
        let req = {"id":1,"accountNumber":"111","currencyCode":"RUB","amount":500.5,"owner":1,"isBlocked":false};
        sendRequest(url, req, gotPayment, acc) ;
    }
</script>
</body>
</html>
