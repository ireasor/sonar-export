package io.github.ireasor.sonarexport.entity;

public class Results {
	
	private int total;
	private int p;
	private int ps;
	private Issue[] issues;
	
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getP() {
		return p;
	}
	public void setP(int p) {
		this.p = p;
	}
	public int getPs() {
		return ps;
	}
	public void setPs(int ps) {
		this.ps = ps;
	}
	public Issue[] getIssues() {
		return issues;
	}
	public void setIssues(Issue[] issues) {
		this.issues = issues;
	}
}
