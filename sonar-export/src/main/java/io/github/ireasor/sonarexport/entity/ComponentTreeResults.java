package io.github.ireasor.sonarexport.entity;

public class ComponentTreeResults {

	private Paging paging;
	private Component[] components;
	
	public Paging getPaging() {
		return paging;
	}
	public void setPaging(Paging paging) {
		this.paging = paging;
	}
	public Component[] getComponents() {
		return components;
	}
	public void setComponents(Component[] components) {
		this.components = components;
	}
	
}
