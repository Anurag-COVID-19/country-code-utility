# country-code-utility
Problem: We have a list of orders to be processed which needs to be imported, split by country and stored. This list arrives in the form of a CSV file and the country needs to be determined based on the client phone number.

Language: Java (OpenJDK 19)
Framework: Spring Boot - (Production ready module, can we easily integrated with microservice based architecture or this utiltiy can be shared independently)
DB: H2 database used - lightweight
Build Tool: Maven
Executable: Jar

Executable jar will be available in target folder directory {installed_directory}\target

API's:
1. /country-code-utility/upload/csv/{csvFile}
    - This API will be taking csv file as an input and returns a status of Success/Fail
    - This API will be used to store the csv data into DB
    - for the given number, there will be an country name and country code entry
2. /orders/all
    - This API will be responsible in fetching all the orders   
3. /country-code-utility/upload/xlsx/{xlsxFile}
    - This API will be taking xlsx file as an input and returns a status of Success/Fail
    - This API created the automate the xlsx converion to csv, during conversion csv file be created the deployed directory

Steps to Run to utility:
1. Place this utility in a {working_directory}
2. Run the application
3. If user have a xlsx file use this API to convert xlsx->csv API-3
   3.1 CSV file will generated in an {working directory}
4. Use the API-1 api to persit data into DB
5. Use the API-2 api to fetch all orders
