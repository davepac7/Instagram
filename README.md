# Selenium UI Automation Tests 
This UI automation script finds Instagram influencers currently in a locale of your choice and sends them a message of your choice.
You can set rules, such as sending different messages based on influencer following.

## Following tools were used in this framework:
Maven, TestNG, Selenium WebDriver, WebDriver Extensions, Log4j, Maven Surefire Plugin(for parallel tests execution), Maven Compiler Plugin, Apache POI. All automation is done in Java.

### Pre-Requisites:
Please make sure that you have installed:
- Java
  [Installation of the JDK and the JRE on macOS](https://docs.oracle.com/javase/10/install/installation-jdk-and-jre-macos.htm#JSJIG-GUID-C5F0BF25-3487-4F33-9275-7000C8E1C58C)
  [Installation of the JDK and the JRE on Microsoft Windows Platforms](https://docs.oracle.com/javase/10/install/installation-jdk-and-jre-microsoft-windows-platforms.htm#JSJIG-GUID-A7E27B90-A28D-4237-9383-A58B416071CA)

- Maven
  [Installing Apache Maven](https://maven.apache.org/install.html)

- Intellij or Eclipse

# How to use

### Execute the following command to run tests from command line or terminal:

   -mvn clean test





### BELOW IS STILL A WORK IN PROGRESS:
### Selenium Grid with Docker
- For executing test cases with selenium grid follow below steps :
   - Execute start_dockergrid.sh - Selenium Grid hub and node are up and ready
   - Execute start_dockergrid.sh - Selenium Grid hub and node are down

- For running smoke suite with selenium grid
   - mvn clean test -DsuiteXmlFile=smoke-crossbrowser-parallel-grid.xml
  
- For running regression suite with selenium grid
   - mvn clean test -DsuiteXmlFile=regression-crossbrowser-parallel-grid.xml

