package model.dao;

import db.DB;
import model.dao.impl.TelefoneDaoJDBC;

public class DaoFactory {
	
	public static TelefoneDao createTelefoneDao() {
		return new TelefoneDaoJDBC(DB.getConnection());
	}

}
