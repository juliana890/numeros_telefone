package model.service;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.TelefoneDao;
import model.entity.Telefone;

public class TelefoneService {

	private TelefoneDao dao = DaoFactory.createTelefoneDao();

	// Ira inserir um novo telefone
	public void insert(Telefone obj) {
		dao.insert(obj);
	}

	// Irá retornar uma lista dos números iguais
	public List<Telefone> findEquals(Telefone obj) {
		return dao.findEquals(obj);
	}
	
	public List<Telefone> findAll(){
		return dao.findAll();
	}

}
