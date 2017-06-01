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
		List<Contact> listInsertContacts = this.insertContacts();

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
	public void sholdBeRetornContactById() throws IOException {
		List<Contact> listInsertContacts = this.insertContacts();
		Contact contact = listInsertContacts.get(0);
		
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
	public void sholdBeRetornStatusNotFound() throws IOException {
		this.insertContacts();
		
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
		assertTrue(stringResponse.contains("{id : "));
	}
	
	private List<Contact> getListContacts(String stringResponse) {
		Contact[] arrayContacts = this.gson.fromJson(stringResponse, Contact[].class);
		return Arrays.asList(arrayContacts);
	}
	
	private List<Contact> insertContacts() {
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
}