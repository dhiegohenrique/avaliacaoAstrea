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

		resp.setStatus(HttpServletResponse.SC_CREATED);
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("id", contact.getId());
		this.writeJSON(resp, jsonObject);
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
		return parameterNames.hasMoreElements();
	}
	
	@SuppressWarnings("unchecked")
	private void doGetByParameters(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Enumeration<String> parameterNames = req.getParameterNames();
		List<ContactField> listContactFields = new ArrayList<>();
		
		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			if (!(paramName.equals("cpf") || paramName.equals("name"))) {
				continue;
			}
			
			ContactField contactField = new ContactField();

			if (paramName.equals("cpf")) {
				String cpf = req.getParameterValues(paramName)[0];
				if (!this.validateCpf(cpf, resp)) {
					return;
				}
				
				contactField.setName(paramName);
				contactField.setFilterOperator(FilterOperator.EQUAL);
				contactField.setValue(this.normalizeCpf(cpf));
			} else {
				String name = req.getParameterValues(paramName)[0];
				if (!this.validateName(name, resp)) {
					return;
				}
				
				contactField.setName("filterName");
				contactField.setFilterOperator(FilterOperator.EQUAL);
				contactField.setValue(name);
			}
			
			listContactFields.add(contactField);
		}
		
		this.doGetByParameters(listContactFields, resp);
	}
	
	private boolean validateCpf(String cpf, HttpServletResponse resp) throws IOException {
		if (StringUtils.isBlank(cpf)) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "O cpf do contato deve ser informado.");
			return false;
		}
		
		cpf = this.normalizeCpf(cpf);
		if (cpf.length() != 11) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "O cpf informado não é válido.");
			return false;
		}
		
		return true;
	}
	
	private String normalizeCpf(String cpf) {
		String normalizeCpf = cpf;
		return normalizeCpf.replaceAll("[^0-9]", "");
	}
	
	private boolean validateName(String name, HttpServletResponse resp) throws IOException {
		if (StringUtils.isBlank(name)) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "O nome do contato deve ser informado.");
			return false;
		}
		
		return true;
	}

	private void doGetByParameters(List<ContactField> listContactFields, HttpServletResponse resp) throws IOException {
		resp.setStatus(HttpServletResponse.SC_OK);
		List<Contact> listContacts = DAO.getContactsByFields(listContactFields);
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