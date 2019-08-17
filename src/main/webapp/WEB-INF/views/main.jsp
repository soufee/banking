<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
<head>
    <title>Money transfer</title>
    <style>


        .outcome {
            color: red;
        }

        .income {
            color: green;
        }

        #start {
            position: relative;
            padding-top: 1.5rem;
            display: block;
        }

        label {
            position: absolute;
            top: 0;
            font-size: 10;
            opacity: 1;
            transform: translateY(0);
            transition: all 0.2s ease-out;
        }

        input:placeholder-shown + label {
            opacity: 0;
            transform: translateY(1rem);
        }

        #left_div {
            float: left;
            width: 50%;
            background-color: aliceblue;
            display: none;

        }

        #right_div {
            float: right;
            width: 50%;
            background-color: antiquewhite;
            display: none;
        }

        h1{
            padding: 10px;
            margin: 10px;
            text-align: center;
        }
    </style>

    <script src="${pageContext.request.contextPath}/js/main.js"></script>
    <script>
        let passportInput = document.querySelector("#docNumber");

    </script>
</head>
<body>
<div id="main">
    <div id="start">
        <label for="docNumber">Enter your passport number</label>
        <input type="text" id="docNumber" placeholder="enter document number...">
        <button id="startBtn">start</button>

    </div>
    <div id="left_div">
        <div class="client" id="client_data_wrapper">
            <h1 id="client__data__label">Client data</h1>
            <div id="client_data"></div>
        </div>
    </div>
    <div id="right_div">
        <h1 id="accounts">Accounts</h1>

    </div>
</div>

</body>
</html>
