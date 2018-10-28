# transfer-service

To project is built with target JDK 10

To run build and execute tests run: 

``` ./mvnw clean compile test```

To execute the standalone server run:

``` ./mvnw exec:java ```

##Endpoints
The server is listening over port `4567`

####Banking Controller
The banking accounts for this test are under `/banking` 
resource

* GET `/`: 

  retrieves every account registered

* GET `/{accountId: Integer}`: 
    
    retrieves specific account registered
* POST `/`: 

    registers account into banking accounts, use JSON as body:
    ```
    {
      "accountId": 123,
      "balance": 100.00
    }
    ```
* DELETE `/`:

    deletes every account registered, for testing purposes

####Transfer Controller
The transfer service resources are under `/transfer`

* POST `/{balance}/from/{debitAccount}/to/{creditAccount}`:

    transfers `balance` from `debitAccount` to `creditAccount`, 
    if the `debitAccount` has enough balance to be transferred.
    The endpoint response is `200` for successful transfer and `400`
    for failed transfer (e.g.: not enough balance in `debitAccount`)