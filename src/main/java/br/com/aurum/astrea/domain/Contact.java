package br.com.aurum.astrea.domain;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Contact {

	@Id
	private Long id;
	
	@Index
	private String name;
	
	@Index
	private String filterName;

	private String birthDay;
	private String birthMonth;
	private String birthYear;

	@Index
	private String cpf;
	
	private String rg;

	private List<String> phones;
	
	@Index
	private List<String> emails;

	private String address;

	private String observation;
	
	@Index
	private Date dateTime;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthDay() {
		return this.birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	public String getBirthMonth() {
		return this.birthMonth;
	}

	public void setBirthMonth(String birthMonth) {
		this.birthMonth = birthMonth;
	}

	public String getBirthYear() {
		return this.birthYear;
	}

	public void setBirthYear(String birthYear) {
		this.birthYear = birthYear;
	}

	public String getCpf() {
		return this.cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRg() {
		return this.rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public List<String> getPhones() {
		return this.phones;
	}

	public void setPhones(List<String> phones) {
		this.phones = phones;
	}

	public List<String> getEmails() {
		return this.emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String addresses) {
		this.address = addresses;
	}

	public String getObservation() {
		return this.observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public Long getId() {
		return this.id;
	}

	public Date getDateTime() {
		return this.dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getFilterName() {
		return this.filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}
}