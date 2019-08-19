var gotJson;
var clientData = document.querySelector("#client_data");
var accountData = document.querySelector("#account_details");
var startbox = document.querySelector("#start");
var rightSide = document.querySelector("#right_div");
var leftSide = document.querySelector("#left_div");
var paymentDiv = document.querySelector("#payment");
var accBtn = document.querySelector("#getAccBtn");
var client_document = "";

function findClient() {
    let field = document.querySelector("#docNumber");
    let docnum = field.value;
    client_document = docnum;
    let client = {docNumber: docnum};

    let url = "/clientgot";
    console.log(JSON.stringify(client));
    sendRequest(url, JSON.stringify(client), setClientData);
}

function getAccounts() {
    while (accountData.firstChild) {
        accountData.removeChild(accountData.firstChild)
    }
    let url = "/getaccounts";
    client = {docNumber: client_document};
    sendRequest(url, JSON.stringify(client), setAccountsData);
    accBtn.style.display = "none";
}

function pay(acc) {
    paymentDiv.style.display = 'block';
    paymentDiv.querySelector("#payer").value = acc.accountNumber;
    paymentDiv.querySelector("#sbmtPayment").onclick = submitPayment;
}

function finishPayment(msg) {
    if (!msg.error) {
        paymentDiv.style.display = 'none';
        getAccounts();
    }
}

function submitPayment() {
    if (!paymentDiv.querySelector("#payer").value) {
        alert("Incorrect payer");
        return false;
    }
    if (!paymentDiv.querySelector("#payee").value) {
        alert("incorrect payee");
        return false;
    }
    if (!paymentDiv.querySelector("#amount").value) {
        alert("incorrect amount");
        return false;
    }
    let payment = {
        accountFrom: paymentDiv.querySelector("#payer").value,
        accountTo: paymentDiv.querySelector("#payee").value,
        amount: paymentDiv.querySelector("#amount").value
    };
    let req = JSON.stringify(payment);
    let url = "/payment";
    sendRequest(url, req, finishPayment);
}

function setAccountsData(json) {
    rightSide.style.display = 'block';
    let err = accountData.querySelector(".error");
    if (err) {
        accountData.removeChild(err);
    }
    let obj = JSON.parse(json);
    for (let i = 0; i < obj.length; i++) {
        let account = obj[i];
        let elementDiv = document.createElement("div");
        elementDiv.className = "accountElement";
        elementDiv.id = account.accountNumber + "_Account";
        let element = document.createElement("h4");
        element.className = "accountHeader";
        element.innerText = "account #" + account.accountNumber + ". Balance = " + account.amount + " " + account.currencyCode;
        element.onclick = function (event) {
            showOperations(elementDiv);
            let target = event.target;
        };
        let payBtn = document.createElement("button");
        payBtn.id = account.accountNumber + "_btn";
        payBtn.innerText = "pay";
        payBtn.onclick = function (event) {
            pay(account);
        };
        elementDiv.appendChild(payBtn);
        elementDiv.appendChild(element);
        accountData.appendChild(elementDiv);
    }

}

function showOperations(element) {

    let accountNum = element.getAttribute("id").substring(0, element.getAttribute("id").indexOf("_"));
    getOperationsById(accountNum, element);
}

function getOperationsById(id, element) {
    let account = {operId: id};
    let url = "/getoperations";
    sendRequest(url, JSON.stringify(account), setOperDataData, element);
}

function setOperDataData(json, element) {
    let liElements = element.getElementsByTagName("li");
    for (let i = liElements.length - 1; i >= 0; i--) {
        element.removeChild(liElements[i]);
    }

    let obj = JSON.parse(json);
    let accountNum = element.getAttribute("id").substring(0, element.getAttribute("id").indexOf("_"));
    for (let i = 0; i < obj.length; i++) {
        let oper = obj[i];
        let elementDiv = document.createElement("li");
        if (oper.to === accountNum) {
            elementDiv.className = "income";
            let dt = oper.dateTime;
            let date = getDateFromDateTime(dt);
            let time = getTimeFromDateTime(dt);
            elementDiv.innerText = date + " " + time + " | " + oper.amount + " " + oper.currency + " from " + oper.from;
            element.appendChild(elementDiv);
        } else if (oper.from === accountNum) {
            elementDiv.className = "outcome";
            let dt = oper.dateTime;
            let date = getDateFromDateTime(dt);
            let time = getTimeFromDateTime(dt);
            elementDiv.innerText = date + " " + time + " | " + oper.amount + " " + oper.currency + " to " + oper.to;
            element.appendChild(elementDiv);
        }

    }
}

function getDateFromDateTime(dt) {
    return dt.date.day + "." + dt.date.month + "." + dt.date.year;
}

function getTimeFromDateTime(dt) {
    return dt.time.hour + ":" + dt.time.minute;
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

function setClientData(json) {
    let err = clientData.querySelector(".error");
    if (err) {
        clientData.removeChild(err);
    }
    let obj = JSON.parse(json);

    if (obj.message) {
        let err = document.createElement("h4");
        err.className = "error";
        err.innerText = "Error: " + obj.code + ": " + obj.message;
        clientData.appendChild(err);
        leftSide.style.display = 'block';
        clientData.style.display = 'block';
    } else if (obj.document) {
        let err = clientData.querySelector(".error");
        if (err) {
            clientData.removeChild(err);
        }
        let client = document.createElement("h4");
        client.className = "client";
        client.innerText = "name " + obj.firstName + " " + obj.lastName + "\n" +
            "document number " + obj.document + "\nphone " + obj.phone + "\nsex " + obj.sex + "\naddress " + obj.address
        ;
        clientData.appendChild(client);
        clientData.style.display = 'block';
        leftSide.style.display = 'block';
        accBtn.style.display = 'block';
        startbox.style.display = 'none';
    }
}