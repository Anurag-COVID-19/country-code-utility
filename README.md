# country-code-utility
Problem: We have a list of orders to be processed which needs to be imported, split by country and stored. This list arrives in the form of a CSV file and the country needs to be determined based on the client phone number.

Language: Java (OpenJDK 17)
Framework: Spring Boot - (Production ready module, can we easily integrated with microservice based architecture or this utiltiy can be shared independently)
DB: H2 database used - lightweight
Build Tool: Maven

API's:
1. /country-code-utility/upload/csv/{csvFile}
    - This API will be taking csv file as an input and returns a status of Success/Fail
    - This API will be used to store the csv data into DB
    - for the given number, there will be an country name and country code entry
2. /country-code-utility/upload/xlsx/{xlsxFile}
    - This API will be taking csv file as an input and returns a status of Success/Fail
    - This API created the automate the xlsx converion to csv, during conversion csv file be created the deployed directory
