package br.com.aurum.astrea.service;

import com.google.appengine.api.datastore.Query.FilterOperator;

public class ContactField {
	
	public enum Field {
		NAME("filterName", "name"),
		EMAIL("emails", "email"),
		CPF("cpf", "cpf");
		
		private String fieldName;
		private String paramName;

		private Field(String fieldName, String paramName) {
			this.fieldName = fieldName;
			this.paramName = paramName;
		}
		
		public String getFieldName() {
			return this.fieldName;
		}
		
		public static Field getFieldByParamName(String paramName) {
			for (Field field : Field.values()) {
				if (paramName.equals(field.paramName)) {
					return field;
				}
			}
			
			return null;
		}
	}
	
	private Field field;
	
	private String value;
	
	private FilterOperator filterOperator;
	
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

	public Field getField() {
		return this.field;
	}

	public void setField(Field field) {
		this.field = field;
	}
}