package inteldt.aspider.custom.entity;

import java.util.List;

public class Entity {
	private String tablename;
	private int fieldsize;
	private List<String> fieldNames;
	private List<String> fieldValues;
	private List<Class<?>> fieldTypes;
	
	public int getFieldsize() {
		return fieldsize;
	}
	public void setFieldsize(int fieldsize) {
		this.fieldsize = fieldsize;
	}
	public List<String> getFieldNames() {
		return fieldNames;
	}
	public void setFieldNames(List<String> fieldNames) {
		this.fieldNames = fieldNames;
	}
	
	public List<String> getFieldValues() {
		return fieldValues;
	}
	public void setFieldValues(List<String> fieldValues) {
		this.fieldValues = fieldValues;
	}
	
	public List<Class<?>> getFieldTypes() {
		return fieldTypes;
	}
	public void setFieldTypes(List<Class<?>> fieldTypes) {
		this.fieldTypes = fieldTypes;
	}
	
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
		
}
