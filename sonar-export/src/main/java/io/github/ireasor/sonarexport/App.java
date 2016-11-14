package io.github.ireasor.sonarexport;

import java.util.List;

import io.github.ireasor.sonarexport.entity.Issue;
import io.github.ireasor.sonarexport.excelbuilder.ExcelBuilder;
import io.github.ireasor.sonarexport.sonarclient.SonarClient;

public class App 
{

    public static void main( String[] args )
    {
    	if (args.length != 3) {
    		System.out.println("Syntax: java -jar sonar-export.jar <SERVER ADDRESS> <PROJECTS> <LOGIN INFO>");
    		System.out.println("For example: java -jar sonar-export.jar localhost:8080 myFirstProject,mySecondProject admin:admin");
    	} else {
    		String serverAddress = args[0];
        	
        	String projectsList = args[1];
        	String[] projects = projectsList.split(",");
        	
        	String loginInfo = args[2];
        	String[] loginKeys = loginInfo.split(":");
        	String username = loginKeys[0];
        	String password = loginKeys[1];

        	ExcelBuilder builder = new ExcelBuilder();
        	
    		try {
    			SonarClient client = new SonarClient(serverAddress, username, password);
    			
    			for (String project:projects) {
    				List<Issue> searchResults = client.executeSearch(project);
    				builder.addSheet(project, searchResults);
    			}
    			
    			builder.saveWorkbook("results.xls");
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				builder.close();
			}
    		
    	}
    }
}
