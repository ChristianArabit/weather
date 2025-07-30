System Requirements:
- Installed: Java SE Development Kit 21.0.8

Initial setup
1. Download zip file
2. Extract

Run Application: Through VSCode or IntelliJ
1. Open generated folder
2. Go to "...\weather"
3. Right click within the directory
4. Select either:
- Open Folder as IntelliJ...
- Open with Code
5. Press: Ctrl + `
6. In the terminal, enter these in order:
- .\mvnw clean install
- .\mvnw spring-boot:run

Run Application: Through Eclipse
1. Open Eclipse
2. Go to File > Import...
3. Select Maven > Existing Maven Projects
4. Click Browse
5. Navigate until you reach "...\weather" directory
6. Click Select Folder > Finish
7. Right-click "weather"
8. Click Maven > Update Project > OK
9. Navigate to src/main/java > com.cvarabit.weather > WeatherApplication.java
10. Right-click within the file > Run as > Java Application

The Spring Boot application should be running.

Feel free to test, in example:
1. Open your browser
2. Type this in the URL:
- http://localhost:8080/v1/weather?city=Melbourne
