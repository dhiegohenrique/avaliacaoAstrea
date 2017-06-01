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

import com.google.gson.Gson;

import br.com.aurum.astrea.dao.ContactDao;
import br.com.aurum.astrea.domain.Contact;

@SuppressWarnings("serial")
public class ContactServlet extends HttpServlet {
	
	private static final ContactDao DAO = new ContactDao();

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		BufferedReader reader = req.getReader();
		String line = reader.readLine();
		Contact contact = new Gson().fromJson(line, Contact.class);
		DAO.save(contact);
		resp.setStatus(HttpServletResponse.SC_CREATED);
		resp.setContentType("application/json;charset=UTF-8");
		
		PrintWriter out = resp.getWriter();
		out.println(String.format("{id : %s}", contact.getId()));
		out.close();
		
//		try {
//			escreveJSON(req, resp, contact);
//		} catch (ServletException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		// TODO: Implementar um método que irá ler o corpo da requisição e, com essas informações,
		// salvar no banco de dados uma entidade do tipo 'Contato' com essas informações.
	}
	
	private void escreveJSON(HttpServletRequest req, HttpServletResponse resp, Contact contact) throws ServletException, IOException {
		resp.setContentType("application/json;charset=UTF-8");
		
		PrintWriter out = resp.getWriter();
		out.println(new Gson().toJson(contact));
		out.close();
		
		
//		try {
//			resp.setContentType("application/json;charset=UTF-8");
//			MappedNamespaceConvention con = new MappedNamespaceConvention();
//
//			XMLStreamWriter xmlStreamWriter = new MappedXMLStreamWriter(con,
//					resp.getWriter());
//
//			Marshaller marshaller = context.createMarshaller();
//			marshaller.marshal(objetoAEscrever, xmlStreamWriter);
//			
//		} catch (JAXBException e) {
//			resp.sendError(500);
//		}
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		System.err.println("entrou aqui2");
		// TODO: Implementar um método que irá listar todas as entidades do tipo 'Contato' e devolver para o client essa listagem.
		
		resp.setContentType("application/json;charset=UTF-8");

		Long id = this.getId(req);
		if (id == null) {
			this.doGetAllContacts(resp);
		} else {
			this.doGetContactById(id, resp);
		}
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
		
		PrintWriter out = resp.getWriter();
		out.println(new Gson().toJson(listContacts));
		out.close();
	}
	
	private void doGetContactById(Long id, HttpServletResponse resp) throws IOException {
		Contact contact = DAO.getContactById(id);
		if (contact == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Contato não encontrado.");
			return;
		}
		
		PrintWriter out = resp.getWriter();
		out.println(new Gson().toJson(contact));
		out.close();
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		System.err.println("entrou aqui3");
		// TODO: Implementar um método que irá deletar uma entidade do tipo 'Contato', dado parâmetro de identificação.
		String contactId = req.getParameter("contactId");
		if (contactId != null) {
			DAO.delete(Long.valueOf(contactId));
		}
	}
}
