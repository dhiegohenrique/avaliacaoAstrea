package br.com.aurum.astrea;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gson.Gson;
import com.googlecode.objectify.ObjectifyService;

import br.com.aurum.astrea.domain.Contact;
import br.com.aurum.astrea.service.ContactServlet;

public class ContactServletTest {
	
	private ContactServlet contactServlet;
	
	private HttpServletRequest req;
	
	private HttpServletResponse resp;
	
	private Gson gson;
	
	private static LocalServiceTestHelper helper;
	
	@BeforeClass
	public static void init() {
		helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
		helper.setEnvIsLoggedIn(true);
		helper.setEnvAuthDomain("localhost");
	}
	
	@Before
	public void setUp() {
		helper.setUp();
		this.contactServlet = new ContactServlet();
		this.req = mock(HttpServletRequest.class);
		this.resp = mock(HttpServletResponse.class);
		this.gson = new Gson();
	}
	
	@After
	public void tearDown() {
		helper.tearDown();
		ObjectifyService.reset();
	}
	
	@Test
	public void shouldBeReturnAllContacts() throws IOException {
		List<Contact> listInsertContacts = this.insertListContacts();

		StringWriter stringWriter = new StringWriter();
		when(this.resp.getWriter()).thenReturn(new PrintWriter(stringWriter));

		this.contactServlet.doGet(this.req, this.resp);

		String stringResponse = stringWriter.toString();
		List<Contact> listContacts = this.getListContacts(stringResponse);
		assertEquals(listInsertContacts.size(), listContacts.size());
		
		for (int index = 0; index < listInsertContacts.size(); index++) {
			Contact contactInsert = listInsertContacts.get(index);
			Contact contactReturn = listContacts.get(index);
			
			assertEquals(contactInsert.getId(), contactReturn.getId());
			assertEquals(contactInsert.getName(), contactReturn.getName());
		}
	}
	
	@Test
	public void sholdBeReturnContactById() throws IOException {
		Contact contact = this.insertContact();
		
		StringWriter stringWriter = new StringWriter();
		when(this.req.getRequestURI()).thenReturn("contacts/" + contact.getId());
		when(this.resp.getWriter()).thenReturn(new PrintWriter(stringWriter));
		
		this.contactServlet.doGet(this.req, this.resp);
		
		String stringResponse = stringWriter.toString();
		Contact contactResult = this.gson.fromJson(stringResponse, Contact.class);
		
		assertEquals(contactResult.getId(), contact.getId());
		assertEquals(contactResult.getName(), contact.getName());
	}
	
	@Test
	public void sholdBeReturnStatus404WhenContactNotFound() throws IOException {
		when(this.req.getRequestURI()).thenReturn("contacts/1234567");
		
		ArgumentCaptor<Integer> intArg = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<String> stringArg = ArgumentCaptor.forClass(String.class);
		doNothing().when(this.resp).sendError(intArg.capture(), stringArg.capture());

		this.contactServlet.doGet(this.req, this.resp);

		assertEquals(HttpServletResponse.SC_NOT_FOUND, intArg.getValue().intValue());
	}
	
	@Test
	public void shouldBeSaveNewContact() throws IOException {
		Contact contact = new Contact();
		contact.setName("Novo contato");
		
		BufferedReader bufferedReader = new BufferedReader(new StringReader(this.gson.toJson(contact)));
		StringWriter stringWriter = new StringWriter();
		when(this.req.getReader()).thenReturn(bufferedReader);
		when(this.resp.getWriter()).thenReturn(new PrintWriter(stringWriter));
		
		ArgumentCaptor<Integer> intArg = ArgumentCaptor.forClass(Integer.class);
		doNothing().when(this.resp).setStatus(intArg.capture());
		
		this.contactServlet.doPost(this.req, this.resp);
		
		String stringResponse = stringWriter.toString();
		assertEquals(HttpServletResponse.SC_CREATED, intArg.getValue().intValue());
		assertTrue(stringResponse.contains("id"));
	}
	
	@Test
	public void shouldBeReturnStatus500WhenSavingAContactWithABlankName() throws IOException {
		Contact contact = new Contact();
		
		BufferedReader bufferedReader = new BufferedReader(new StringReader(this.gson.toJson(contact)));
		when(this.req.getReader()).thenReturn(bufferedReader);
		
		ArgumentCaptor<Integer> intArg = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<String> stringArg = ArgumentCaptor.forClass(String.class);
		doNothing().when(this.resp).sendError(intArg.capture(), stringArg.capture());

		this.contactServlet.doPost(this.req, this.resp);
		
		assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, intArg.getValue().intValue());
	}
	
	@Test
	public void shouldBeReturnStatus500WhenEditingAContactWithoutId() throws IOException, ServletException {
		when(this.req.getRequestURI()).thenReturn("contacts/");
		
		ArgumentCaptor<Integer> intArg = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<String> stringArg = ArgumentCaptor.forClass(String.class);
		doNothing().when(this.resp).sendError(intArg.capture(), stringArg.capture());

		this.contactServlet.doPut(this.req, this.resp);

		assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, intArg.getValue().intValue());
	}
	
	@Test
	public void shouldBeReturnStatus404WhenEditingAContactNotFound() throws IOException, ServletException {
		when(this.req.getRequestURI()).thenReturn("contacts/1234567");
		
		ArgumentCaptor<Integer> intArg = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<String> stringArg = ArgumentCaptor.forClass(String.class);
		doNothing().when(this.resp).sendError(intArg.capture(), stringArg.capture());

		this.contactServlet.doPut(this.req, this.resp);

		assertEquals(HttpServletResponse.SC_NOT_FOUND, intArg.getValue().intValue());
	}
	
	@Test
	public void shouldBeReturnStatus500WhenEditingAnUninformedContact() throws IOException, ServletException {
		List<Contact> listInsertContacts = this.insertListContacts();
		when(this.req.getRequestURI()).thenReturn("contacts/" + listInsertContacts.get(0).getId());
		
		BufferedReader bufferedReader = new BufferedReader(new StringReader(""));
		when(this.req.getReader()).thenReturn(bufferedReader);
		
		ArgumentCaptor<Integer> intArg = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<String> stringArg = ArgumentCaptor.forClass(String.class);
		doNothing().when(this.resp).sendError(intArg.capture(), stringArg.capture());

		this.contactServlet.doPut(this.req, this.resp);
		assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, intArg.getValue().intValue());
	}
	
	@Test
	public void shouldBeReturnStatus204WhenEditingAContact() throws IOException, ServletException {
		Contact contact = this.insertContact();
		
		BufferedReader bufferedReader = new BufferedReader(new StringReader(this.gson.toJson(contact)));
		
		when(this.req.getRequestURI()).thenReturn("contacts/" + contact.getId());
		when(this.req.getReader()).thenReturn(bufferedReader);
		
		ArgumentCaptor<Integer> intArg = ArgumentCaptor.forClass(Integer.class);
		doNothing().when(this.resp).setStatus(intArg.capture());

		this.contactServlet.doPut(this.req, this.resp);
		assertEquals(HttpServletResponse.SC_NO_CONTENT, intArg.getValue().intValue());
	}
	
	@Test
	public void shouldBeReturnStatus500WhenDeletingAContactWithoutId() throws IOException, ServletException {
		when(this.req.getRequestURI()).thenReturn("contacts/");
		
		ArgumentCaptor<Integer> intArg = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<String> stringArg = ArgumentCaptor.forClass(String.class);
		doNothing().when(this.resp).sendError(intArg.capture(), stringArg.capture());

		this.contactServlet.doDelete(this.req, this.resp);
		assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, intArg.getValue().intValue());
	}
	
	@Test
	public void shouldBeReturnStatus404WhenDeletingAContactNotFound() throws IOException, ServletException {
		when(this.req.getRequestURI()).thenReturn("contacts/1234567");
		
		ArgumentCaptor<Integer> intArg = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<String> stringArg = ArgumentCaptor.forClass(String.class);
		doNothing().when(this.resp).sendError(intArg.capture(), stringArg.capture());

		this.contactServlet.doDelete(this.req, this.resp);
		assertEquals(HttpServletResponse.SC_NOT_FOUND, intArg.getValue().intValue());
	}
	
	@Test
	public void shouldBeReturnStatus204WhenDeletingAContact() throws IOException, ServletException {
		Contact contact = this.insertContact();
		
		BufferedReader bufferedReader = new BufferedReader(new StringReader(this.gson.toJson(contact)));
		
		when(this.req.getRequestURI()).thenReturn("contacts/" + contact.getId());
		when(this.req.getReader()).thenReturn(bufferedReader);
		
		ArgumentCaptor<Integer> intArg = ArgumentCaptor.forClass(Integer.class);
		doNothing().when(this.resp).setStatus(intArg.capture());

		this.contactServlet.doDelete(this.req, this.resp);
		assertEquals(HttpServletResponse.SC_NO_CONTENT, intArg.getValue().intValue());
	}
	
	@Test
	public void shouldBeReturnContactByName() throws IOException {
		Contact contact = this.insertContact();

		StringWriter stringWriter = new StringWriter();
		when(this.req.getRequestURI()).thenReturn("contacts?name=" + contact.getName());
		when(this.resp.getWriter()).thenReturn(new PrintWriter(stringWriter));

		this.contactServlet.doGet(this.req, this.resp);

		String stringResponse = stringWriter.toString();
		List<Contact> listContacts = this.getListContacts(stringResponse);
		
		for (Contact contactReturn : listContacts) {
			assertEquals(contact.getName(), contactReturn.getName());
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void shouldBeReturnStatus500WhenRecoveringContactByEmptyName() throws IOException, ServletException {
		Contact contact = this.insertContact();
		
		BufferedReader bufferedReader = new BufferedReader(new StringReader(this.gson.toJson(contact)));
		
		Vector parameterNames = new Vector();
		parameterNames.addElement("name");
		
		when(this.req.getParameterNames()).thenReturn(parameterNames.elements());
		when(this.req.getParameterValues(parameterNames.get(0).toString())).thenReturn(new String[]{""});
		when(this.req.getRequestURI()).thenReturn("contacts");
		when(this.req.getReader()).thenReturn(bufferedReader);
		
		ArgumentCaptor<Integer> intArg = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<String> stringArg = ArgumentCaptor.forClass(String.class);
		doNothing().when(this.resp).sendError(intArg.capture(), stringArg.capture());

		this.contactServlet.doGet(this.req, this.resp);
		assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, intArg.getValue().intValue());
	}
	
	@Test
	public void shouldBeReturnContactByEmail() throws IOException {
		Contact contact = this.insertContact();

		StringWriter stringWriter = new StringWriter();
		when(this.req.getRequestURI()).thenReturn("contacts?email=" + contact.getEmails().get(0));
		when(this.resp.getWriter()).thenReturn(new PrintWriter(stringWriter));

		this.contactServlet.doGet(this.req, this.resp);

		String stringResponse = stringWriter.toString();
		List<Contact> listContacts = this.getListContacts(stringResponse);
		
		for (Contact contactReturn : listContacts) {
			assertEquals(contact.getEmails().get(0), contactReturn.getEmails().get(0));
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void shouldBeReturnStatus500WhenRecoveringContactByEmptyEmail() throws IOException, ServletException {
		Contact contact = this.insertContact();
		
		BufferedReader bufferedReader = new BufferedReader(new StringReader(this.gson.toJson(contact)));
		
		Vector parameterNames = new Vector();
		parameterNames.addElement("email");
		
		when(this.req.getParameterNames()).thenReturn(parameterNames.elements());
		when(this.req.getParameterValues(parameterNames.get(0).toString())).thenReturn(new String[]{""});
		when(this.req.getRequestURI()).thenReturn("contacts");
		when(this.req.getReader()).thenReturn(bufferedReader);
		
		ArgumentCaptor<Integer> intArg = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<String> stringArg = ArgumentCaptor.forClass(String.class);
		doNothing().when(this.resp).sendError(intArg.capture(), stringArg.capture());

		this.contactServlet.doGet(this.req, this.resp);
		assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, intArg.getValue().intValue());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void shouldBeReturnStatus500WhenRecoveringContactByInvalidEmail() throws IOException, ServletException {
		Contact contact = this.insertContact();
		
		BufferedReader bufferedReader = new BufferedReader(new StringReader(this.gson.toJson(contact)));
		
		Vector parameterNames = new Vector();
		parameterNames.addElement("email");
		
		when(this.req.getParameterNames()).thenReturn(parameterNames.elements());
		when(this.req.getParameterValues(parameterNames.get(0).toString())).thenReturn(new String[]{"EMAIL 124 . &%$@@hotmail.com"});
		when(this.req.getRequestURI()).thenReturn("contacts");
		when(this.req.getReader()).thenReturn(bufferedReader);
		
		ArgumentCaptor<Integer> intArg = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<String> stringArg = ArgumentCaptor.forClass(String.class);
		doNothing().when(this.resp).sendError(intArg.capture(), stringArg.capture());
		
		this.contactServlet.doGet(this.req, this.resp);
		assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, intArg.getValue().intValue());
	}
	
	@Test
	public void shouldBeReturnContactByCpf() throws IOException {
		Contact contact = this.insertContact();

		StringWriter stringWriter = new StringWriter();
		when(this.req.getRequestURI()).thenReturn("contacts?cpf=" + contact.getCpf());
		when(this.resp.getWriter()).thenReturn(new PrintWriter(stringWriter));

		this.contactServlet.doGet(this.req, this.resp);

		String stringResponse = stringWriter.toString();
		List<Contact> listContacts = this.getListContacts(stringResponse);
		
		for (Contact contactReturn : listContacts) {
			assertEquals(contact.getCpf(), contactReturn.getCpf());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void shouldBeReturnStatus500WhenRecoveringContactByEmptyCpf() throws IOException, ServletException {
		Contact contact = this.insertContact();
		
		BufferedReader bufferedReader = new BufferedReader(new StringReader(this.gson.toJson(contact)));
		
		Vector parameterNames = new Vector();
		parameterNames.addElement("cpf");
		
		when(this.req.getParameterNames()).thenReturn(parameterNames.elements());
		when(this.req.getParameterValues(parameterNames.get(0).toString())).thenReturn(new String[]{""});
		when(this.req.getRequestURI()).thenReturn("contacts");
		
		when(this.req.getReader()).thenReturn(bufferedReader);
		
		ArgumentCaptor<Integer> intArg = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<String> stringArg = ArgumentCaptor.forClass(String.class);
		doNothing().when(this.resp).sendError(intArg.capture(), stringArg.capture());

		this.contactServlet.doGet(this.req, this.resp);
		assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, intArg.getValue().intValue());
	}
	
	@Test
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void shouldBeReturnStatus500WhenRecoveringContactByInvalidCpf() throws IOException, ServletException {
		Contact contact = this.insertContact();
		
		BufferedReader bufferedReader = new BufferedReader(new StringReader(this.gson.toJson(contact)));
		
		Vector parameterNames = new Vector();
		parameterNames.addElement("cpf");
		
		when(this.req.getParameterNames()).thenReturn(parameterNames.elements());
		when(this.req.getParameterValues(parameterNames.get(0).toString())).thenReturn(new String[]{"11111111111"});
		when(this.req.getRequestURI()).thenReturn("contacts");
		when(this.req.getReader()).thenReturn(bufferedReader);
		
		ArgumentCaptor<Integer> intArg = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<String> stringArg = ArgumentCaptor.forClass(String.class);
		doNothing().when(this.resp).sendError(intArg.capture(), stringArg.capture());

		this.contactServlet.doGet(this.req, this.resp);
		assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, intArg.getValue().intValue());
	}
	
	@Test
	public void shouldBeReturnContactByNameAndCpf() throws IOException {
		Contact contact = this.insertContact();

		StringWriter stringWriter = new StringWriter();
		when(this.req.getRequestURI()).thenReturn("contacts?name=" + contact.getName() + "&cpf=" + contact.getCpf());
		when(this.resp.getWriter()).thenReturn(new PrintWriter(stringWriter));

		this.contactServlet.doGet(this.req, this.resp);

		String stringResponse = stringWriter.toString();
		List<Contact> listContacts = this.getListContacts(stringResponse);
		
		for (Contact contactReturn : listContacts) {
			assertEquals(contact.getName(), contactReturn.getName());
			assertEquals(contact.getCpf(), contactReturn.getCpf());
		}
	}
	
	private List<Contact> getListContacts(String stringResponse) {
		Contact[] arrayContacts = this.gson.fromJson(stringResponse, Contact[].class);
		return Arrays.asList(arrayContacts);
	}
	
	private List<Contact> insertListContacts() {
		List<Contact> listContacts = new ArrayList<>();
		for (int index = 0; index < 5; index++) {
			listContacts.add(this.getContact(index));
		}
		
		ObjectifyService.ofy().save().entities(listContacts).now();
		return listContacts;
	}
	
	private Contact getContact(int index) {
		Contact contact = new Contact();
		contact.setName("Contato" + index);
		return contact;
	}

	private Contact insertContact() {
		Contact contact = new Contact();
		contact.setName("Maria");
		contact.setCpf("88461676009");
		
		List<String> listEmails = new ArrayList<>();
		listEmails.add("maria1@hotmail.com");
		listEmails.add("maria2@hotmail.com");
		contact.setEmails(listEmails);
		
		ObjectifyService.ofy().save().entity(contact).now();
		return contact;
	}
}