function getDateFromDateTime(dt) {
    return dt.date.day + "." + dt.date.month + "." + dt.date.year;
}

function getTimeFromDateTime(dt) {
    return dt.time.hour + ":" + dt.time.minute;
}

let Unit = {
    init() {
        this.startbox = document.querySelector("#start");
        this.clientDataLabel = document.querySelector("#client__data__label");
        this.startBtn = document.querySelector("#startBtn");
        this.nameInput = document.querySelector("#docNumber");
        this.leftSide = document.querySelector("#left_div");
        this.rightSide = document.querySelector("#right_div");
        this.clientData = document.querySelector("#client_data");
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
        this.leftSide.style.display = "block";
        this.rightSide.style.display = "block";
        this.startbox.style.display = "none";
        let clientBlock = document.createElement("div");
        this.clientData.appendChild(clientBlock);
        this.send();
    },
    onMessage(msg) {
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


            let incomeOpersForAcc = operations.filter(elem => elem.to === accountList[i].accountNumber);
            for (let i = 0; i < incomeOpersForAcc.length; i++) {
                let operIncomeRecord = document.createElement("h6");
                operIncomeRecord.classList.add("income");
                let dt = incomeOpersForAcc[i].dateTime;
                let date = getDateFromDateTime(dt);
                let time = getTimeFromDateTime(dt);
                operIncomeRecord.innerText = date + " " + time + " | " + incomeOpersForAcc[i].amount + " " + incomeOpersForAcc[i].currency + " from " + incomeOpersForAcc[i].from;
                account_data.appendChild(operIncomeRecord);
            }
            let outcomeOpersForAcc = operations.filter(elem => elem.from === accountList[i].accountNumber);

            for (let i = 0; i < outcomeOpersForAcc.length; i++) {
                let operOutcomeRecord = document.createElement("h6");
                operOutcomeRecord.classList.add("outcome");
                let dt = outcomeOpersForAcc[i].dateTime;
                let date = getDateFromDateTime(dt);
                let time = getTimeFromDateTime(dt);
                operOutcomeRecord.innerText = date + " " + time + " | " + outcomeOpersForAcc[i].amount + " " + outcomeOpersForAcc[i].currency + " to " + outcomeOpersForAcc[i].to;
                account_data.appendChild(operOutcomeRecord);
            }
            listOfAccounts.appendChild(account_data);
        }
        accountData.appendChild(listOfAccounts);
        this.clientData.appendChild(accountData);
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