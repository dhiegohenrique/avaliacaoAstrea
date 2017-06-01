package br.com.aurum.astrea.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.LoadResult;
import com.googlecode.objectify.ObjectifyService;

import br.com.aurum.astrea.domain.Contact;

public class ContactDao {
	
	static {
		ObjectifyService.register(Contact.class);
	}
	
	public void save(Contact contact) {
		// TODO: É preciso pesquisar como se usa o Objectify para armazenar a entidade contato no banco de dados.
		ofy().save().entity(contact).now();
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
	
	public void delete(Long contactId) {
		// TODO: É preciso pesquisar como se usa o Objectify para deletar entidades do banco de dados.
		ofy().delete().key(Key.create(Contact.class, contactId)).now();
	}
}