package io.github.ireasor.sonarexport;

import java.util.List;

import io.github.ireasor.sonarexport.entity.Component;
import io.github.ireasor.sonarexport.entity.Issue;
import io.github.ireasor.sonarexport.excelbuilder.ExcelBuilder;
import io.github.ireasor.sonarexport.sonarclient.HttpClientProvider;
import io.github.ireasor.sonarexport.sonarclient.SonarClient;

public class App 
{

    public static void main( String[] args )
    {
    	if (args.length < 3) {
    		System.out.println("Syntax: java -jar sonar-export.jar <SERVER ADDRESS> <PROJECTS> <LOGIN INFO> [-includeCoverage]");
    		System.out.println("For example: java -jar sonar-export.jar localhost:8080 myFirstProject,mySecondProject admin:admin -includeCoverage");
    	} else {
    		String serverAddress = args[0];
        	
        	String projectsList = args[1];
        	String[] projects = projectsList.split(",");
        	
        	String loginInfo = args[2];
        	String[] loginKeys = loginInfo.split(":");
        	String username = loginKeys[0];
        	String password = loginKeys[1];
        	
        	boolean includeCoverage = false;
        	
        	if (args.length > 3) {
        		if (args[3].equals("-includeCoverage")) {
        			includeCoverage = true;
        		}
        	}

        	ExcelBuilder builder = new ExcelBuilder();
        	
    		try {
    			HttpClientProvider httpClientProvider = new HttpClientProvider(serverAddress, username, password);
    			SonarClient client = new SonarClient(httpClientProvider);
    			
    			for (String project:projects) {
    				List<Issue> searchResults = client.executeSearch(project);
    				builder.addIssueSheet(project, searchResults);
    				
    				if (includeCoverage) {
    					List<Component> componentCoverageResults = client.getCoverage(project);
    					builder.addCoverageSheet(project, componentCoverageResults);
    				}
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
