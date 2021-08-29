package model.entity;

import java.io.Serializable;
import java.util.Date;

public class Telefone implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String numero;
	private Date dataDeCadastro;
	
	public Telefone() {}

	public Telefone(String numero, Date dataDeCadastro) {
		this.numero = numero;
		this.dataDeCadastro = dataDeCadastro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Date getDataDeCadastro() {
		return dataDeCadastro;
	}

	public void setDataDeCadastro(Date dataDeCadastro) {
		this.dataDeCadastro = dataDeCadastro;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Telefone other = (Telefone) obj;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}
		
	
}
