package io.github.ireasor.sonarexport.entity;

public class Component {
	private String path;
	private String language;
	private Measure[] measures;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public Measure[] getMeasures() {
		return measures;
	}
	public void setMeasures(Measure[] measures) {
		this.measures = measures;
	}
	
}
