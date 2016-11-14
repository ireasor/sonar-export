# SonarQube Exporter

The SonarQube exporter is a self-contained JAR file that can be run from the command line to output issues list(s) from one or more SonarQube projects to an Excel workbook.

Build it using:

``mvn clean compile assembly:single``


Run it using:

``java -jar target/sonar-export-1.0-jar-with-dependencies.jar SERVER:PORT PROJECTID1,PROJECTID2 USERNAME:PASSWORD``

For example:

``java -jar target/sonar-export-1.0-jar-with-dependencies.jar localhost:9000 Customer-ProjectA,Customer-ProjectB admin:admin``

The file will be output to the current directory as a file named results.xls.

## Caveats and limitations:

Currently, only HTTP is supported.

Currently, we only support logging in with basic auth.  Using a Sonar token is another possibility down the road.