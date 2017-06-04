package br.com.aurum.astrea.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.LoadResult;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.Query;
import com.googlecode.objectify.cmd.QueryKeys;

import br.com.aurum.astrea.domain.Contact;
import br.com.aurum.astrea.service.ContactField;

public class ContactDao {
	
	static {
		ObjectifyService.register(Contact.class);
	}
	
	public void save(Contact contact) throws IllegalArgumentException {
		if (!this.validate(contact)) {
			throw new IllegalArgumentException("O nome do contato não foi informado.");
		}
		
		contact.setDateTime(new Date());
		contact.setFilterName(this.getFilterNormalize(contact.getName()));
		ofy().save().entity(contact).now();
	}
	
	private boolean validate(Contact contact) {
		return !StringUtils.isBlank(contact.getName());
	}
	
	private String getFilterNormalize(String name) {
		if (StringUtils.isBlank(name)) {
			return null;
		}
		
		String filterNormalize = name;
		filterNormalize = filterNormalize.replace("%20", " ");
		filterNormalize = filterNormalize.replace("\\", "\\\\");
		filterNormalize = filterNormalize.replace("'", "\\'");
		filterNormalize = filterNormalize.replace("\0", "\\0");
		filterNormalize = filterNormalize.replace("\n", "\\n");
		filterNormalize = filterNormalize.replace("\r", "\\r");
		filterNormalize = filterNormalize.replace("\"", "\\\"");
		filterNormalize = filterNormalize.replace("\\x1a", "\\Z");
		return filterNormalize.toUpperCase();
	}
	
	public List<Contact> list() {
		return ofy().load().type(Contact.class).order("dateTime").list();
	}
	
	public Contact getContactById(Long id) {
		Key<Contact> key = Key.create(Contact.class, id);
		LoadResult<Contact> loadResult = ofy().load().key(key);
		return loadResult.now();
	}
	
	public void delete(Long id) {
		Key<Contact> key = Key.create(Contact.class, id);
		ofy().delete().key(key).now();
	}
	
	public boolean isContactExists(Long id) {
		Key<Contact> key = Key.create(Contact.class, id);
		QueryKeys<Object> keys = ofy().load().filterKey(key).keys();
		Key<Object> keyResult = keys.first().now();
		return keyResult != null;
	}

	public void update(Long id, final Contact newContact) {
		ofy().transact(new WorkUpdate(id, newContact));
	}
	
	private final class WorkUpdate implements Work<Contact> {

		private Long id;

		private Contact newContact;

		public WorkUpdate(Long id, Contact newContact) {
			this.id = id;
			this.newContact = newContact;
		}

		@Override
		public Contact run() {
			Key<Contact> key = Key.create(Contact.class, this.id);
			
			Contact contact = ofy().load().key(key).now();
			this.cloneProperties(contact, this.newContact);
			ofy().save().entity(contact);
			return contact;
		}
		
		private void cloneProperties(Contact destContact, Contact origContact) {
			destContact.setName(origContact.getName());
			destContact.setBirthDay(origContact.getBirthDay());
			destContact.setBirthMonth(origContact.getBirthMonth());
			destContact.setBirthYear(origContact.getBirthYear());
			destContact.setCpf(origContact.getCpf());
			destContact.setRg(origContact.getRg());
			destContact.setAddress(origContact.getAddress());
			destContact.setObservation(origContact.getObservation());
			
			destContact.setPhones(origContact.getPhones());
			destContact.setEmails(origContact.getEmails());
		}
	}

	public List<Contact> getContactsByFields(List<ContactField> listContactFields) {
		Query<Contact> query = ofy().load().type(Contact.class);

		for (ContactField contactField : listContactFields) {
			String value = contactField.getValue();
			FilterOperator filterOperator = contactField.getFilterOperator();
			if (FilterOperator.EQUAL.equals(filterOperator)) {
				value = this.getFilterNormalize(value);
			}
			
			Filter filter = new FilterPredicate(contactField.getName(), filterOperator, value);
			query = query.filter(filter);
		}
		
		List<Contact> list = query.order("filterName").list();
		return list;
	}

//	private void teste(String value) {
////		String name = "dhiego_henrique20@hotmail.com";
//		String name = value;
//		name = this.getFilterNormalize(name);
//		System.err.println("buscarPor: " + name);
//		
//		Contact contact = ofy().load().type(Contact.class).filter("filterName", name).first().now();
//		if (contact != null) {
//			System.err.println("entrou aqui1: " + contact.getName());
//		} else {
//			System.err.println("não achou contato");
//		}
//		
//		List<Contact> list = ofy().load().type(Contact.class).filter("filterName = ", name).list();
//		for (Contact contact2 : list) {
//			System.err.println("entrou aqui2: " + contact2.getName());
//		}
//		
//		list = ofy().load().type(Contact.class).filter("filterName == ", name).list();
//		for (Contact contact3 : list) {
//			System.err.println("entrou aqui3: " + contact3.getName());
//		}
//		
//		Query<Contact> filter = ofy().load().type(Contact.class).filter("filterName =", name);
//		for (Contact c: filter) {
//		    System.out.println("entrou aqui4: " + c.getName());
//		}
//	}
}