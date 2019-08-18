<%@ page import="entities.Client" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="entities.dto.ErrorDto" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
<head>
    <title>Money transfer</title>
    <link href="${pageContext.request.contextPath}/css/search.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/drop.css" rel="stylesheet">

</head>
<body>
<div class="header__item" id="start">
    <form class="search" action="${pageContext.request.contextPath}/clientgot" method="post">
        <input id="docNumber" name="docNumber" required minlength="2" type="text"
               placeholder="Enter your passport number" class="search__input">
        <button id="startBtn" type="button" onclick="findClient()">
            <img src="icons/glass.svg" alt="search">
        </button>
    </form>
</div>


<div id="main">
    <div id="left_div">
        <div class="client" id="client_data_wrapper">
            <h1 id="client__data__label">Client data</h1>
            <div id="client_data">

            </div>
            <button id="getAccBtn" type="button" onclick="getAccounts()" style="display: none">show accounts</button>
        </div>
    </div>
    <div id="right_div">
        <div class="account_data_wrapper">

            <h1 id="accounts">Accounts</h1>
            <div id="account_details">
            </div>
        </div>
        <div id="payment">
            <form action="" name="payment_form" class="payment_form" onsubmit="return false">
                <ul>
                    <li>
                        <h2>Payment details</h2>
                    </li>
                    <li>
                        <label for="payer">Payer account:</label>
                        <input type="text" name="payer" id="payer" readonly/>
                    </li>
                    <li>
                        <label for="payee">Account number of payee:</label>
                        <input type="text" name="payee" id="payee"/>
                    </li>
                    <li>
                        <label for="amount">Amount:</label>
                        <input type="number" step="0.01" min="0" placeholder="0,00" name="amount" id="amount"/>
                    </li>
                    <li>
                        <button class="button" type="button" id="sbmtPayment">Submit Form</button>
                    </li>
                </ul>
            </form>

        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/main.js"></script>

</body>
</html>
