The project has be done as a test for employee aplication to Revolut company.
API's:
http://localhost:8888/clientgot - getting personal data of client
POST with json like 
{"docNumber": "1616161616"} - where 1616161616 is a passport number of client

http://localhost:8888/getaccounts - getting list of accounts of client 
POST with json like
{"docNumber": "1616161616"} - where 1616161616 is a passport number of client

http://localhost:8888/getoperations - getting list of operations by account number
POST with json like
{"operId": "111"} - where 111 is an account number


http://localhost:8888/payment - processing an money transfer operation
POST with json like
{
"accountFrom":"111", 
"accountTo":"222", 
"amount":"15.50"
}

installing: 
In target folder run command
java -jar banking.war

If there's an exception, related with missing tables in database, copy into target/db files from ROOT/db 

