package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import connection.SingleConnectionBanco;
import model.ModelTelefone;

public class DaoTelefoneRepository {
	
	private Connection connection;
	private DAOUsuarioRepository daoUsu = new DAOUsuarioRepository();
	
	public DaoTelefoneRepository() {
		connection = SingleConnectionBanco.getConnection();
	}
	
	public List<ModelTelefone> listTelefone(Long id_user_pai) throws Exception {
		List<ModelTelefone> retorno = new ArrayList<ModelTelefone>();
		
		String sql = "SELECT * FROM telefone WHERE usuario_pai_id = ?";
		
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setLong(1, id_user_pai);
		
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			ModelTelefone telefone = new ModelTelefone();
			telefone.setId(rs.getLong("id"));
			telefone.setNumero(rs.getString("numero"));
			telefone.setUsuario_cad_id(daoUsu.consultaUsuarioID(rs.getLong("usuario_cad_id")));
			telefone.setUsuario_pai_id(daoUsu.consultaUsuarioID(rs.getLong("usuario_pai_id")));
			
			retorno.add(telefone);
		}
		
		return retorno;
	}
	
	public void gravaTelefone(ModelTelefone modelTelefone) throws Exception {
		
		String sql = "INSERT INTO telefone (numero, usuario_pai_id, usuario_cad_id) VALUES (?,?,?)";
		
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setString(1, modelTelefone.getNumero());
		ps.setLong(2, modelTelefone.getUsuario_pai_id().getId());
		ps.setLong(3, modelTelefone.getUsuario_cad_id().getId());
		
		ps.execute();
		connection.commit();
	}
	
	public void deleteTelefone(Long id) throws Exception {
		
		String sql = "DELETE FROM telefone WHERE id = ?";
		
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setLong(1, id);
		
		ps.executeUpdate();
		connection.commit();
	}
}
