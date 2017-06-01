package br.com.aurum.astrea.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.google.common.net.MediaType;
import com.google.gson.Gson;

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
		
		String jsonResponse = String.format("{id : %s}", contact.getId());
		this.writeJSON(resp, jsonResponse);
	}
	
	private Contact getContact(HttpServletRequest req) throws IOException {
		BufferedReader reader = req.getReader();
		String line = reader.readLine();
		return new Gson().fromJson(line, Contact.class);
	}
	
	private void writeJSON(HttpServletResponse resp, String jsonResponse) throws IOException {
		resp.setContentType(MediaType.JSON_UTF_8.toString());
		
		PrintWriter out = resp.getWriter();
		out.println(jsonResponse);
		out.close();
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Long id = this.getId(req);
		if (id == null) {
			this.doGetAllContacts(resp);
		} else {
			this.doGetContactById(id, req, resp);
		}
		
		resp.setStatus(HttpServletResponse.SC_OK);
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
		String jsonResponse = new Gson().toJson(listContacts);
		this.writeJSON(resp, jsonResponse);
	}
	
	private void doGetContactById(Long id, HttpServletRequest req, HttpServletResponse resp) throws IOException {
		if (!this.validateRequest(req, resp)) {
			return;
		}
		
		Contact contact = DAO.getContactById(id);
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