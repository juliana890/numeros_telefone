package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DBException;
import model.dao.TelefoneDao;
import model.entity.Telefone;

public class TelefoneDaoJDBC implements TelefoneDao {

	private Connection conn;
	
	public TelefoneDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Telefone obj) {
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement("INSERT INTO telefone (numero, dataDeCadastro) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, obj.getNumero());
			ps.setDate(2, new java.sql.Date(obj.getDataDeCadastro().getTime()));
			
			int rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if(rs.next()) {
					String numero = rs.getString(1);
					obj.setNumero(numero);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DBException("Erro inesperado! Nenhuma linha foi afetada!");
			}
		}
		catch(SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
		}
	}
	
	@Override
	public List<Telefone> findEquals(Telefone obj){
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("SELECT * FROM telefone WHERE numero = ?");
			
			ps.setString(1, obj.getNumero());
			
			rs = ps.executeQuery();
			
			List<Telefone> lista = new ArrayList<>();
			
			while(rs.next()) {
				Telefone newObj = new Telefone();
				newObj.setNumero(rs.getString("numero"));
				newObj.setDataDeCadastro(new java.util.Date(rs.getTimestamp("dataDeCadastro").getTime()));
				lista.add(newObj);
			
			}
			return lista;
		}
		catch(SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Telefone> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("SELECT * FROM telefone");
			
			rs = ps.executeQuery();
			
			List<Telefone> lista = new ArrayList<>();
			
			while(rs.next()) {
				Telefone newObj = new Telefone();
				newObj.setNumero(rs.getString("numero"));
				newObj.setDataDeCadastro(new java.util.Date(rs.getTimestamp("dataDeCadastro").getTime()));
				lista.add(newObj);
			
			}
			return lista;
		}
		catch(SQLException e) {
			throw new DBException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}
}
