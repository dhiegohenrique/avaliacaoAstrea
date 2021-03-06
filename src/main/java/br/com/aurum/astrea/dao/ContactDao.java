package br.com.aurum.astrea.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Arrays;
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
import br.com.aurum.astrea.service.ContactField.Field;
import br.com.aurum.astrea.utils.FilterUtils;

public class ContactDao {
	
	static {
		ObjectifyService.register(Contact.class);
	}
	
	public void save(Contact contact) throws IllegalArgumentException {
		if (!this.validate(contact)) {
			throw new IllegalArgumentException("O nome do contato não foi informado.");
		}
		
		contact.setDateTime(new Date());
		contact.setFilterName(FilterUtils.getFilterNormalize(contact.getName()));
		ofy().save().entity(contact).now();
	}
	
	private boolean validate(Contact contact) {
		return !StringUtils.isBlank(contact.getName());
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
			Object value = contactField.getValue();
			FilterOperator filterOperator = contactField.getFilterOperator();
			if (FilterOperator.EQUAL.equals(filterOperator)) {
				value = FilterUtils.getFilterNormalize(value.toString());
			} else if (FilterOperator.IN.equals(filterOperator)) {
				value = Arrays.asList(value.toString());
			}
			
			Filter filter = new FilterPredicate(contactField.getField().getFieldName(), filterOperator, value);
			query = query.filter(filter);
		}
		
		List<Contact> list = query.order(Field.NAME.getFieldName()).list();
		return list;
	}
}