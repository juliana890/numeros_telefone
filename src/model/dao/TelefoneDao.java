package model.dao;

import java.util.List;

import model.entity.Telefone;

public interface TelefoneDao {

	void insert(Telefone obj);
	List<Telefone> findEquals(Telefone obj);
	List<Telefone> findAll();
}
