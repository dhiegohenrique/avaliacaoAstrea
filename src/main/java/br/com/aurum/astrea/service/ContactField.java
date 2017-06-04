package br.com.aurum.astrea.service;

import com.google.appengine.api.datastore.Query.FilterOperator;

public class ContactField {
	
	private String name;
	private String value;
	
	private FilterOperator filterOperator;
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public FilterOperator getFilterOperator() {
		return this.filterOperator;
	}

	public void setFilterOperator(FilterOperator filterOperator) {
		this.filterOperator = filterOperator;
	}
}
