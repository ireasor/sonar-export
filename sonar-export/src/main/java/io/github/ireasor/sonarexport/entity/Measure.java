package io.github.ireasor.sonarexport.entity;

public class Measure {
	public static final String COVERAGE = "coverage";
	public static final String UNCOVERED_LINES = "uncovered_lines";
	public static final Object UNCOVERED_CONDITIONS = "uncovered_conditions";
	
	private String metric;
	private String value;
	
	public String getMetric() {
		return metric;
	}
	public void setMetric(String metric) {
		this.metric = metric;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
