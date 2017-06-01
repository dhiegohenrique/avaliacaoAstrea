package br.com.aurum.astrea.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.LoadResult;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.QueryKeys;

import br.com.aurum.astrea.domain.Contact;

public class ContactDao {
	
	static {
		ObjectifyService.register(Contact.class);
	}
	
	public void save(Contact contact) throws IllegalArgumentException {
		if (!this.validate(contact)) {
			throw new IllegalArgumentException("Você deve informar o nome do contato");
		}
		
		// TODO: É preciso pesquisar como se usa o Objectify para armazenar a entidade contato no banco de dados.
		this.saveContact(contact);
	}
	
	private void saveContact(Contact contact) {
		ofy().save().entity(contact).now();
	}
	
	private boolean validate(Contact contact) {
		return !StringUtils.isBlank(contact.getName());
	}
	
	public List<Contact> list() {
		// TODO: É preciso pesquisar como se usa o Objectify para listar as entidades de contato.
//		return new ArrayList<>();
		return ofy().load().type(Contact.class).list();
	}
	
	public Contact getContactById(Long id) {
		Key<Contact> key = Key.create(Contact.class, id);
		LoadResult<Contact> loadResult = ofy().load().key(key);
		return loadResult.now();
	}
	
	public void delete(Long id) {
		Key<Contact> key = Key.create(Contact.class, id);
		
		// TODO: É preciso pesquisar como se usa o Objectify para deletar entidades do banco de dados.
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
			ContactDao.this.cloneProperties(contact, this.newContact);
			ofy().save().entity(contact);
			return contact;
		}
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