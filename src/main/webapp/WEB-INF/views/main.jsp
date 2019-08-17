<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
<head>
    <title>Money transfer</title>
    <style>

        .messages {
            background-color: #369;
            width: 500px;
            padding: 20px;
            border-radius: 3px;

        }

        .outcome{
            color: red;
        }

        .income {
            color: green;
        }

        .messages .msg {
            background-color: #fff;
            border-radius: 10px;
            margin-bottom: 10px;
            overflow: hidden;
        }

        .messages .msg .from {
            background-color: #396;
            line-height: 30px;
            text-align: center;
            color: white;
        }

        .messages .msg .text {
            padding: 10px;
        }

        textarea.msg {
            width: 540px;
            padding: 10px;
            resize: none;
            border: none;
            box-shadow: 2px 2px 5px 0 inset;
        }

    </style>

    <script>
        function getDateFromDateTime(dt) {
            return dt.date.day+"."+dt.date.month+"."+dt.date.year;
        }

        function getTimeFromDateTime(dt) {
            return dt.time.hour+":"+dt.time.minute;
        }

        let Unit = {
            init() {
                this.startbox = document.querySelector(".start");
                this.clientDataLabel = document.querySelector("#client__data__label");
                this.startBtn = document.querySelector("#startBtn");
                this.nameInput = document.querySelector("#docNumber");
                this.clientDataWrapper = document.querySelector("#client_data_wrapper");
                this.clientData = this.clientDataWrapper.querySelector("#client_data");
                this.clientData.style.display = "none";


                this.bindEvents();
            },
            bindEvents() {
                this.startBtn.addEventListener("click", e => this.openSocket());
            },
            send() {
                this.sendMessage(
                    {
                        client: this.client,
                    }
                );
            },
            onOpenSock() {
                this.clientData.style.display = "block";
                this.clientDataLabel.style.display = "block";
                this.startbox.style.display = "none";
                let clientBlock = document.createElement("div");
                this.clientData.appendChild(clientBlock);
                this.send();
            },
            onMessage(msg) {
                console.log(msg);
                let firstName = msg.client.firstName;
                let lastName = msg.client.lastName;
                let name = document.createElement("h4");
                name.innerText = firstName + " " + lastName;

                let doc = msg.client.document;
                let phone = msg.client.phone;
                let sex = msg.client.sex;
                let address = msg.client.address;
                let personData = document.createElement("h5");
                personData.innerText = "Passport number " + doc + "\n" +
                    "Address " + address + "\n" +
                    "Phone " + phone + "\n" +
                    "Sex " + sex;

                this.clientData.appendChild(name);
                this.clientData.appendChild(personData);

                let accountList = msg.accounts;
                let accountData = document.createElement("div");
                accountData.className = "account_items";
                let accounts = document.createElement("h4");
                accounts.innerText = "Your accounts:";
                accountData.appendChild(accounts);
                let listOfAccounts = document.createElement("div");
                let operations = msg.operations;

                for (let i = 0; i < accountList.length; i++) {
                    let listItem = document.createElement("li");
                    listItem.innerText = "account " + accountList[i].accountNumber + ". Balance = " + accountList[i].amount + " " + accountList[i].currencyCode;
                    let account_data = document.createElement("div");
                    account_data.appendChild(listItem);
                    let label = document.createElement("h5");
                    label.innerText = "operations:";
                    account_data.appendChild(label);


                    let incomeOpersForAcc = operations.filter(elem=>elem.to===accountList[i].accountNumber);
                    console.log(incomeOpersForAcc);
                    for (let i = 0; i < incomeOpersForAcc.length; i++) {
                        let operIncomeRecord = document.createElement("h6");
                        operIncomeRecord.classList.add("income");
                        let dt = incomeOpersForAcc[i].dateTime;
                        let date = getDateFromDateTime(dt);
                        let time = getTimeFromDateTime(dt);
                        operIncomeRecord.innerText = date+" "+time+" | "+ incomeOpersForAcc[i].amount+" "+incomeOpersForAcc[i].currency +" from "+incomeOpersForAcc[i].from;
                        account_data.appendChild(operIncomeRecord);
                    }
                    let outcomeOpersForAcc = operations.filter(elem=>elem.from===accountList[i].accountNumber);
                    console.log(outcomeOpersForAcc);

                    for (let i = 0; i < outcomeOpersForAcc.length; i++) {
                        let operOutcomeRecord = document.createElement("h6");
                        operOutcomeRecord.classList.add("outcome");
                        let dt = outcomeOpersForAcc[i].dateTime;
                        let date = getDateFromDateTime(dt);
                        let time = getTimeFromDateTime(dt);
                        operOutcomeRecord.innerText = date+" "+time+" | "+ outcomeOpersForAcc[i].amount+" "+outcomeOpersForAcc[i].currency +" to "+outcomeOpersForAcc[i].to;
                        account_data.appendChild(operOutcomeRecord);
                    }
                    listOfAccounts.appendChild(account_data);
                }
                accountData.appendChild(listOfAccounts);
                this.clientData.appendChild(accountData);
                // let msgBlock = document.createElement("div");
                // msgBlock.className = "msg";
                //
                // let fromBlock = document.createElement("div");
                // fromBlock.className = "from";
                // fromBlock.innerText = msg.name;
                //
                // let textBlock = document.createElement("div");
                // textBlock.className = "text";
                // textBlock.innerText = msg.text;
                //
                // msgBlock.appendChild(fromBlock);
                // msgBlock.appendChild(textBlock);
            },
            onClose() {

            },
            sendMessage(client) {
                this.ws.send(JSON.stringify(client));
            },
            openSocket() {
                this.client = this.nameInput.value;
                this.ws = new WebSocket("ws://localhost:8080/client/" + this.client);
                this.ws.onopen = (e) => this.onOpenSock(e);
                this.ws.onmessage = (e) => this.onMessage(JSON.parse(e.data));
                this.ws.onclose = () => this.onClose();

            }
        };
        window.addEventListener("load", e => Unit.init());
    </script>
</head>
<body>
<div class="start">
    <input id="docNumber" type="text" class="username" placeholder="enter document number...">
    <button id="startBtn">start</button>
</div>
<div class="client" id="client_data_wrapper">
    <h1 id="client__data__label" style="display: none">Client data</h1>
    <div id="client_data"></div>
</div>
</body>
</html>
