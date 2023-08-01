package dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import connection.SingleConnectionBanco;

public class DAOVersionadorBanco implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Connection connection;
	
	public DAOVersionadorBanco() {
		connection = SingleConnectionBanco.getConnection();
	}
	
	public void gravaSqlRodado (String nome_file) throws Exception {
		String sql = "INSERT INTO versionadorbanco(arquivo_sql) VALUES (?)";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setString(1, nome_file);
		ps.execute();
	}
	public boolean arquivoSqlRodado (String nome_arquivo) throws Exception {
		String sql = "SELECT count(1) > 0 as rodado FROM versionadorbanco WHERE arquivo_sql = ?";
		
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setString(1, nome_arquivo);
		
		ResultSet rs = ps.executeQuery();
		rs.next();
		
		return rs.getBoolean("rodado");
	}
}
