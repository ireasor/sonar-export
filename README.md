# SonarQube Exporter

The SonarQube exporter is a self-contained JAR file that can be run from the command line to output issues list(s) from one or more SonarQube projects to an Excel workbook.

Build it using:

``mvn clean install``

To run it, cd into the target directory from your Maven build and run:

``java -jar sonar-export-1.0.1.jar ADDRESS PROJECTID1,PROJECTID2 USERNAME:PASSWORD [-includeCoverage]``

For example:

``java -jar sonar-export-1.0.1.jar http://localhost:9000 Customer-ProjectA,Customer-ProjectB admin:admin -includeCoverage``

-includeCoverage is an optional parameter that will include a coverage report.  The file will be output to the current directory as a file named results.xls.

If you would like to distribute the binary to somebody or move it from the Maven target directory, you will need to include the /lib directory from the target directory as well.  When running the jar, it expects the lib directory to be in the same location.

## Caveats and limitations:

HTTPS is supported, but currently will trust all clients.  This is the best that I'm able to do without jumping through some hoops to allow for the importing of custom key stores.

Currently, we only support logging in with basic auth.  Using a Sonar token is another possibility down the road, but would require a fair amount of development.

If anyone is motivated to implement either of these features, please let me know and I'd be happy to work with you to vet your pull request.