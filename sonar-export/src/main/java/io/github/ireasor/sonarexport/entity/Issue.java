package io.github.ireasor.sonarexport.entity;

public class Issue {

	private String rule;
	private String severity;
	private String component;
	private String project;
	private String message;
	private String type;
	
	
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public String getComponent() {
		//Remove the project name from the component name
		return component.replace(project + ":", "");
	}
	public void setComponent(String component) {
		this.component = component;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
