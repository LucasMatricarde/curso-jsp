package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import beanDTO.BeanGraficoSalarioUserDTO;
import connection.SingleConnectionBanco;
import model.ModelLogin;
import model.ModelTelefone;

public class DAOUsuarioRepository {
	
	private Connection connection;
	
	public DAOUsuarioRepository() {
		connection = SingleConnectionBanco.getConnection();
	}
	
	public BeanGraficoSalarioUserDTO montarGraficoMediaSalario(Long userLogado) throws Exception {
		String sql = "SELECT AVG(rendamensal) as media_salarial, perfil FROM model_login WHERE usuario_id = ? GROUP BY perfil";
		PreparedStatement ps = connection.prepareStatement(sql);
		
		ps.setLong(1, userLogado);
		
		ResultSet rs = ps.executeQuery();
		
		List<String> perfils = new ArrayList<String>();
		List<Double> salarios = new ArrayList<Double>();
		BeanGraficoSalarioUserDTO beanGraficoSalario = new BeanGraficoSalarioUserDTO();
		
		while (rs.next()) {
			Double media_salarial = rs.getDouble("media_salarial");
			String perfil = rs.getString("perfil");
			
			perfils.add(perfil);
			salarios.add(media_salarial);
		}
		
		beanGraficoSalario.setPerfils(perfils);
		beanGraficoSalario.setSalarios(salarios);
		
		return beanGraficoSalario;
	}
	
	public ModelLogin gravarUsuario(ModelLogin objeto, Long usuarioLogado) throws Exception {
		
		if(objeto.isNovo()) {//Grava um novo
			String sql = "INSERT INTO model_login(login, senha, nome, email, usuario_id, perfil, sexo, cep, logradouro, numero, bairro, localidade, estado, dtnascimento, rendamensal)  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement statement = connection.prepareStatement(sql);
			
			statement.setString(1, objeto.getLogin());
			statement.setString(2, objeto.getSenha());
			statement.setString(3, objeto.getNome());
			statement.setString(4, objeto.getEmail());
			statement.setLong(5, usuarioLogado);
			statement.setString(6, objeto.getPerfil());
			statement.setString(7, objeto.getSexo());
			statement.setString(8, objeto.getCep());
			statement.setString(9, objeto.getLogradouro());
			statement.setString(10, objeto.getNumero());
			statement.setString(11, objeto.getBairro());
			statement.setString(12, objeto.getLocalidade());
			statement.setString(13, objeto.getEstado());
			statement.setDate(14, objeto.getDtNascimento());
			statement.setDouble(15, objeto.getRendaMensal());
			statement.execute();
			
			connection.commit();
			
			if(objeto.getFotoUser() != null && !objeto.getFotoUser().isEmpty()) {
				sql = "UPDATE model_login set fotouser = ?, extensaofotouser = ? WHERE login = ?";
				statement = connection.prepareStatement(sql);
				statement.setString(1, objeto.getFotoUser());
				statement.setString(2, objeto.getExtensaoFotoUser());
				statement.setString(3, objeto.getLogin());
				statement.execute();
				
				connection.commit();
			}
			
		}else {// atualizar
			String sql = "UPDATE model_login SET login = ?, senha = ?, nome = ?, email = ?, perfil = ?, sexo = ?, cep = ?, logradouro = ?, numero = ?, bairro = ?, localidade = ?, estado = ?, dtnascimento = ?, rendamensal = ? WHERE id = "+objeto.getId()+";";
			PreparedStatement statement = connection.prepareStatement(sql);
			
			statement.setString(1, objeto.getLogin());
			statement.setString(2, objeto.getSenha());
			statement.setString(3, objeto.getNome());
			statement.setString(4, objeto.getEmail());
			statement.setString(5, objeto.getPerfil());
			statement.setString(6, objeto.getSexo());
			statement.setString(7, objeto.getCep());
			statement.setString(8, objeto.getLogradouro());
			statement.setString(9, objeto.getNumero());
			statement.setString(10, objeto.getBairro());
			statement.setString(11, objeto.getLocalidade());
			statement.setString(12, objeto.getEstado());
			statement.setDate(13, objeto.getDtNascimento());
			statement.setDouble(14, objeto.getRendaMensal());
			
			statement.executeUpdate();
			
			connection.commit();
			
			if(objeto.getFotoUser() != null && !objeto.getFotoUser().isEmpty()) {
				sql = "UPDATE model_login set fotouser = ?, extensaofotouser = ? WHERE id = ?";
				statement = connection.prepareStatement(sql);
				statement.setString(1, objeto.getFotoUser());
				statement.setString(2, objeto.getExtensaoFotoUser());
				statement.setLong(3, objeto.getId());
				statement.execute();
				
				connection.commit();
			}
		}
		
		return this.consultaUsuario(objeto.getLogin(), usuarioLogado);
	}
	
	public List<ModelLogin> consultaUsuarioListPaginada(Long userLogado, Integer offset) throws Exception {
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		String sql = "SELECT * FROM model_login WHERE useradmin is false AND usuario_id = "+userLogado+" ORDER BY id OFFSET "+offset+" LIMIT 5";
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado =  statement.executeQuery();
		
		while (resultado.next()) /*Se tem resultado*/ {
			ModelLogin modelLogin = new ModelLogin();
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			//modelLogin.setSenha(resultado.getString("senha"));
			
			retorno.add(modelLogin);
		}
		
		return retorno;
	}
	
	public List<ModelLogin> consultaUsuarioListRel(Long userLogado, String dtInicial, String dtFinal) throws Exception {
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		String sql = "SELECT * FROM model_login WHERE useradmin is false AND usuario_id = "+userLogado+" AND dtnascimento >= ? and dtnascimento <= ? ORDER BY id";
		PreparedStatement statement = connection.prepareStatement(sql);
		
		statement.setDate(1, Date.valueOf(new SimpleDateFormat("yyyy-mm-dd").format(new SimpleDateFormat("dd/mm/yyyy").parse(dtInicial))));
		statement.setDate(2, Date.valueOf(new SimpleDateFormat("yyyy-mm-dd").format(new SimpleDateFormat("dd/mm/yyyy").parse(dtFinal))));
		
		ResultSet resultado =  statement.executeQuery();
		
		while (resultado.next()) /*Se tem resultado*/ {
			ModelLogin modelLogin = new ModelLogin();
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setDtNascimento(resultado.getDate("dtNascimento"));
			
			modelLogin.setTelefones(this.listTelefone(modelLogin.getId()));
			retorno.add(modelLogin);
		}
		
		return retorno;
	}
	
	public List<ModelLogin> consultaUsuarioListRel(Long userLogado) throws Exception {
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		String sql = "SELECT * FROM model_login WHERE useradmin is false AND usuario_id = "+userLogado+" ORDER BY id";
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado =  statement.executeQuery();
		
		while (resultado.next()) /*Se tem resultado*/ {
			ModelLogin modelLogin = new ModelLogin();
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setDtNascimento(resultado.getDate("dtNascimento"));
			
			modelLogin.setTelefones(this.listTelefone(modelLogin.getId()));
			retorno.add(modelLogin);
		}
		
		return retorno;
	}
	
	public List<ModelLogin> consultaUsuarioList(Long userLogado) throws Exception {
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		String sql = "SELECT * FROM model_login WHERE useradmin is false AND usuario_id = "+userLogado+" ORDER BY id LIMIT 5";
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado =  statement.executeQuery();
		
		while (resultado.next()) /*Se tem resultado*/ {
			ModelLogin modelLogin = new ModelLogin();
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			//modelLogin.setSenha(resultado.getString("senha"));
			
			retorno.add(modelLogin);
		}
		
		return retorno;
	}
	
	public int consultaUsuarioListTotalPaginaPaginacao(String nome, Long userLogado) throws Exception {
		String sql = "SELECT COUNT(1) as total FROM model_login WHERE UPPER(nome) LIKE UPPER(?) AND useradmin is false AND usuario_id = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		
		statement.setString(1, "%" + nome + "%");
		statement.setLong(2, userLogado);
		ResultSet resultado =  statement.executeQuery();
		
		resultado.next();
		
		Double cadastros = resultado.getDouble("total");
		Double porPagina = 5.0;
		Double pagina =	cadastros / porPagina;
		Double resto = pagina % 2;
		
		if(resto > 0) {
			pagina ++;
		}
		
		return pagina.intValue();
	}
	
	public List<ModelLogin> consultaUsuarioListOffSet(String nome, Long userLogado, int offSet) throws Exception {
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		String sql = "SELECT * FROM model_login WHERE UPPER(nome) LIKE UPPER(?) AND useradmin is false AND usuario_id = ? ORDER BY id OFFSET "+offSet+" LIMIT 5";
		PreparedStatement statement = connection.prepareStatement(sql);
		
		statement.setString(1, "%" + nome + "%");
		statement.setLong(2, userLogado);
		ResultSet resultado =  statement.executeQuery();
		
		while (resultado.next()) /*Se tem resultado*/ {
			ModelLogin modelLogin = new ModelLogin();
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			//modelLogin.setSenha(resultado.getString("senha"));
			
			retorno.add(modelLogin);
		}
		
		return retorno;
	}
	
	public List<ModelLogin> consultaUsuarioList(String nome, Long userLogado) throws Exception {
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		String sql = "SELECT * FROM model_login WHERE UPPER(nome) LIKE UPPER(?) AND useradmin is false AND usuario_id = ? ORDER BY id LIMIT 5";
		PreparedStatement statement = connection.prepareStatement(sql);
		
		statement.setString(1, "%" + nome + "%");
		statement.setLong(2, userLogado);
		ResultSet resultado =  statement.executeQuery();
		
		while (resultado.next()) /*Se tem resultado*/ {
			ModelLogin modelLogin = new ModelLogin();
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			//modelLogin.setSenha(resultado.getString("senha"));
			
			retorno.add(modelLogin);
		}
		
		return retorno;
	}
	
	public ModelLogin consultaUsuario(String login) throws Exception  {
		
		ModelLogin modelLogin = new ModelLogin();
		
		String sql = "select * from model_login where upper(login) = upper('"+login+"') AND useradmin is false";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado =  statement.executeQuery();
		
		while (resultado.next()) /*Se tem resultado*/ {
			
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
		}
		
		
		return modelLogin;
		
	}
	
public ModelLogin consultaUsuarioLogado(String login) throws Exception  {
		
		ModelLogin modelLogin = new ModelLogin();
		
		String sql = "select * from model_login where upper(login) = upper('"+login+"')";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado =  statement.executeQuery();
		
		while (resultado.next()) /*Se tem resultado*/ {
			
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setUseradmin(resultado.getBoolean("useradmin"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotoUser(resultado.getString("fotoUser"));
			modelLogin.setCep(resultado.getString("cep"));
			modelLogin.setLogradouro(resultado.getString("logradouro"));
			modelLogin.setNumero(resultado.getString("numero"));
			modelLogin.setLocalidade(resultado.getString("localidade"));
			modelLogin.setBairro(resultado.getString("bairro"));
			modelLogin.setEstado(resultado.getString("estado"));
			modelLogin.setDtNascimento(resultado.getDate("dtnascimento"));
			modelLogin.setRendaMensal(resultado.getDouble("rendamensal"));
		}
		
		
		return modelLogin;
		
	}
	
	public ModelLogin consultaUsuario(String login, Long userLogado) throws Exception  {
		
		ModelLogin modelLogin = new ModelLogin();
		
		String sql = "select * from model_login where upper(login) = upper('"+login+"') AND useradmin is false AND usuario_id ="+userLogado;
		
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado =  statement.executeQuery();
		
		while (resultado.next()) /*Se tem resultado*/ {
			
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotoUser(resultado.getString("fotoUser"));
			modelLogin.setCep(resultado.getString("cep"));
			modelLogin.setLogradouro(resultado.getString("logradouro"));
			modelLogin.setNumero(resultado.getString("numero"));
			modelLogin.setLocalidade(resultado.getString("localidade"));
			modelLogin.setBairro(resultado.getString("bairro"));
			modelLogin.setEstado(resultado.getString("estado"));
			modelLogin.setDtNascimento(resultado.getDate("dtnascimento"));
			modelLogin.setRendaMensal(resultado.getDouble("rendamensal"));
		}
		
		
		return modelLogin;
		
	}
	
	public ModelLogin consultaUsuarioID(Long id) throws Exception  {
		
		ModelLogin modelLogin = new ModelLogin();
		
		String sql = "select * from model_login where id = ?";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, id);
		ResultSet resultado =  statement.executeQuery();
		
		while (resultado.next()) /*Se tem resultado*/ {
			
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotoUser(resultado.getString("fotouser"));
			modelLogin.setExtensaoFotoUser(resultado.getString("extensaofotouser"));
			modelLogin.setCep(resultado.getString("cep"));
			modelLogin.setLogradouro(resultado.getString("logradouro"));
			modelLogin.setNumero(resultado.getString("numero"));
			modelLogin.setLocalidade(resultado.getString("localidade"));
			modelLogin.setBairro(resultado.getString("bairro"));
			modelLogin.setEstado(resultado.getString("estado"));
			modelLogin.setDtNascimento(resultado.getDate("dtnascimento"));
			modelLogin.setRendaMensal(resultado.getDouble("rendamensal"));
		}
		
		
		return modelLogin;
		
	}
	
	public ModelLogin consultaUsuarioId(String id, Long userLogado) throws Exception  {
		
		ModelLogin modelLogin = new ModelLogin();
		
		String sql = "select * from model_login where id = ? AND useradmin is false AND usuario_id = ?";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, Long.parseLong(id));
		statement.setLong(2, userLogado);
		ResultSet resultado =  statement.executeQuery();
		
		while (resultado.next()) /*Se tem resultado*/ {
			
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotoUser(resultado.getString("fotouser"));
			modelLogin.setExtensaoFotoUser(resultado.getString("extensaofotouser"));
			modelLogin.setCep(resultado.getString("cep"));
			modelLogin.setLogradouro(resultado.getString("logradouro"));
			modelLogin.setNumero(resultado.getString("numero"));
			modelLogin.setLocalidade(resultado.getString("localidade"));
			modelLogin.setBairro(resultado.getString("bairro"));
			modelLogin.setEstado(resultado.getString("estado"));
			modelLogin.setDtNascimento(resultado.getDate("dtnascimento"));
			modelLogin.setRendaMensal(resultado.getDouble("rendamensal"));
		}
		
		
		return modelLogin;
		
	}
	
	public boolean validarLogin(String login) throws Exception {
		String sql = "select count(1) > 0 as existe from model_login where upper(login) = upper('"+login+"');";
		
        PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resutlado =  statement.executeQuery();
		
		resutlado.next();/*Pra ele entrar nos resultados do sql*/
		return resutlado.getBoolean("existe");
		
	}
	
	public void deletarUser(String idUser) throws Exception {
		String sql = "DELETE FROM model_login WHERE id = ? AND useradmin is false";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, Long.parseLong(idUser));
		statement.executeUpdate();
		connection.commit();
	}
	
	//Logica para saber quantas paginas vai ter e paginar
	public int totalPagina(Long userLogado) throws Exception {
		
		String sql = "SELECT COUNT(1) as total FROM model_login WHERE usuario_id = " + userLogado;
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado = statement.executeQuery();
		
		resultado.next();
		
		Double cadastros = resultado.getDouble("total");
		Double porPagina = 5.0;
		Double pagina =	cadastros / porPagina;
		Double resto = pagina % 2;
		
		if(resto > 0) {
			pagina ++;
		}
		
		return pagina.intValue();
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
			telefone.setUsuario_cad_id(this.consultaUsuarioID(rs.getLong("usuario_cad_id")));
			telefone.setUsuario_pai_id(this.consultaUsuarioID(rs.getLong("usuario_pai_id")));
			
			retorno.add(telefone);
		}
		
		return retorno;
	}
}
