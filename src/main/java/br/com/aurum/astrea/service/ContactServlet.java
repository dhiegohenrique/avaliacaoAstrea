package br.com.aurum.astrea.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.common.net.MediaType;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.aurum.astrea.dao.ContactDao;
import br.com.aurum.astrea.domain.Contact;
import br.com.aurum.astrea.service.ContactField.Field;
import br.com.aurum.astrea.utils.CPFUtils;

@SuppressWarnings("serial")
public class ContactServlet extends HttpServlet {
	
	private static final ContactDao DAO = new ContactDao();
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Contact contact = this.getContact(req);

		try {
			DAO.save(contact);
		} catch (IllegalArgumentException e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			return;
		}

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("id", contact.getId());
		this.writeJSON(resp, jsonObject);
		
		resp.setStatus(HttpServletResponse.SC_CREATED);
	}
	
	private void writeJSON(HttpServletResponse resp, JsonObject jsonObject) throws IOException {
		resp.setContentType(MediaType.JSON_UTF_8.toString());
		resp.setStatus(HttpServletResponse.SC_OK);
		
		PrintWriter out = resp.getWriter();
		out.println(jsonObject);
		out.close();
	}
	
	private void writeJSON(HttpServletResponse resp, String jsonResponse) throws IOException {
		resp.setContentType(MediaType.JSON_UTF_8.toString());
		resp.setStatus(HttpServletResponse.SC_OK);
		
		PrintWriter out = resp.getWriter();
		out.println(jsonResponse);
		out.close();
	}
	
	private void writeJSON(HttpServletResponse resp, List<Contact> listContacts) throws IOException {
		String jsonResponse = new Gson().toJson(listContacts);
		this.writeJSON(resp, jsonResponse);
	}
	
	private Contact getContact(HttpServletRequest req) throws IOException {
		BufferedReader reader = req.getReader();
		String line = reader.readLine();
		return new Gson().fromJson(line, Contact.class);
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		if (this.hasParameters(req)) {
			this.doGetByParameters(req, resp);
			return;
		}
		
		Long id = this.getId(req);
		if (id == null) {
			this.doGetAllContacts(resp);
		} else {
			this.doGetContactById(id, req, resp);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private boolean hasParameters(HttpServletRequest req) {
		Enumeration parameterNames = req.getParameterNames();
		if (parameterNames == null) {
			return false;
		}
		
		return parameterNames.hasMoreElements();
	}
	
	@SuppressWarnings("unchecked")
	private void doGetByParameters(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Enumeration<String> parameterNames = req.getParameterNames();
		List<ContactField> listContactFields = new ArrayList<>();
		
		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			Field field = Field.getFieldByParamName(paramName);
			if (field == null) {
				continue;
			}
			
			ContactField contactField = null;
			
			switch (paramName) {
				case "cpf":
					String cpf = req.getParameterValues(paramName)[0];
					contactField = this.doGetByCpf(cpf, resp);
					break;
	
				case "name":
					String name = req.getParameterValues(paramName)[0];
					contactField = this.doGetByName(name, resp);
					break;
					
				default:
					String email = req.getParameterValues(paramName)[0];
					contactField = this.doGetByEmail(email, resp);
					break;
			}
			
			if (contactField == null) {
				return;
			}

			contactField.setField(field);
			listContactFields.add(contactField);
		}
		
		this.doGetByContactFields(listContactFields, resp);
	}
	
	private ContactField doGetByCpf(String cpf, HttpServletResponse resp) throws IOException {
		Boolean isCpfValid = this.validateCpf(cpf, resp);
		if (isCpfValid == null) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "O cpf do contato deve ser informado.");
			return null;
		} else if (!isCpfValid) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "O cpf informado não é válido.");
			return null;
		}
		
		ContactField contactField = new ContactField();
		contactField.setFilterOperator(FilterOperator.EQUAL);
		contactField.setValue(CPFUtils.normalizeCpf(cpf));
		return contactField;
	}

	private Boolean validateCpf(String cpf, HttpServletResponse resp) throws IOException {
		if (StringUtils.isBlank(cpf)) {
			return null;
		}
		
		if (!CPFUtils.isValidCPF(cpf)) {
			return false;
		}
		
		return true;
	}
	
	private ContactField doGetByName(String name, HttpServletResponse resp) throws IOException {
		if (!this.validateName(name, resp)) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "O nome do contato deve ser informado.");
			return null;
		}
		
		ContactField contactField = new ContactField();
		contactField.setFilterOperator(FilterOperator.EQUAL);
		contactField.setValue(name);
		return contactField;
	}
	
	private boolean validateName(String name, HttpServletResponse resp) throws IOException {
		return !StringUtils.isBlank(name);
	}
	
	private ContactField doGetByEmail(String email, HttpServletResponse resp) throws IOException {
		Boolean isEmailValid = this.validateEmail(email, resp);
		if (isEmailValid == null) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "O email do contato deve ser informado.");
			return null;
		} else if (!isEmailValid) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "O email informado não é válido.");
			return null;
		}
		
		ContactField contactField = new ContactField();
		contactField.setFilterOperator(FilterOperator.IN);
		contactField.setValue(email);
		return contactField;
	}

	private Boolean validateEmail(String email, HttpServletResponse resp) {
		if (StringUtils.isBlank(email)) {
			return null;
		}
		
		String regex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
		return email.matches(regex);
	}

	private void doGetByContactFields(List<ContactField> listContactFields, HttpServletResponse resp) throws IOException {
		resp.setStatus(HttpServletResponse.SC_OK);
		List<Contact> listContacts = DAO.getContactsByFields(listContactFields);
		if (listContacts == null || listContacts.isEmpty()) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Contato(s) não encontrado(s).");
			return;
		}
		
		this.writeJSON(resp, listContacts);
	}

	private Long getId(HttpServletRequest req) {
		String requestURI = req.getRequestURI();
		String id = StringUtils.substringAfterLast(requestURI, "contacts/");
		
		if (StringUtils.isBlank(id)) {
			return null;
		}
		
		return Long.valueOf(id);
	}
	
	private void doGetAllContacts(HttpServletResponse resp) throws IOException {
		List<Contact> listContacts = DAO.list();
		this.writeJSON(resp, listContacts);
	}
	
	private void doGetContactById(Long id, HttpServletRequest req, HttpServletResponse resp) throws IOException {
		if (!this.validateRequest(req, resp)) {
			return;
		}
		
		Contact contact = DAO.getContactById(id);
		this.writeJSON(resp, contact);
	}
	
	private void writeJSON(HttpServletResponse resp, Contact contact) throws IOException {
		String jsonResponse = new Gson().toJson(contact);
		this.writeJSON(resp, jsonResponse);
	}
	
	@Override
	public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		if (!this.validateRequest(req, resp)) {
			return;
		}
		
		DAO.delete(this.getId(req));
		resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
	}
	
	private boolean validateRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Long id = this.getId(req);
		if (id == null) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "O ID do contato deve ser informado.");
			return false;
		}
		
		if (!DAO.isContactExists(id)) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Contato não encontrado.");
			return false;
		}
		
		return true;
	}
	
	@Override
	public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!this.validateRequest(req, resp)) {
			return;
		}
		
		Contact contact = this.getContact(req);
		if (contact == null) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "O contato deve ser informado.");
			return;
		}
		
		DAO.update(this.getId(req), contact);
		resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
	}
}