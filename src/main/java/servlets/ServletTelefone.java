package servlets;

import java.io.IOException;
import java.util.List;

import dao.DAOUsuarioRepository;
import dao.DAOTelefoneRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ModelLogin;
import model.ModelTelefone;

@WebServlet("/ServletTelefone")
public class ServletTelefone extends ServletGenericUtil {
	
	private static final long serialVersionUID = 1L;
	
	private DAOUsuarioRepository daoUsu = new DAOUsuarioRepository();
	
	private DAOTelefoneRepository daoFone = new DAOTelefoneRepository();
       
    public ServletTelefone() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String idUser = request.getParameter("idUser");
			String acao = request.getParameter("acao");
			
			if(acao != null && !acao.isEmpty() && acao.equals("excluir")) {
				String idFone = request.getParameter("id");
				String userPai = request.getParameter("userPai");
				
				daoFone.deleteTelefone(Long.parseLong(idFone));
				
				ModelLogin modelLogin = daoUsu.consultaUsuarioID(Long.parseLong(userPai));
				List<ModelTelefone> modelTelefones = daoFone.listTelefone(modelLogin.getId());
				
				request.setAttribute("modelTelefones", modelTelefones);
				request.setAttribute("modelLogin", modelLogin);
				request.setAttribute("msg", "Excluido com sucesso");
				request.getRequestDispatcher("principal/telefone.jsp").forward(request, response);
				return;
			}
		
			if(idUser != null && !idUser.isEmpty()) {
				ModelLogin modelLogin = daoUsu.consultaUsuarioID(Long.parseLong(idUser));
				List<ModelTelefone> modelTelefones = daoFone.listTelefone(modelLogin.getId());
				
				request.setAttribute("modelTelefones", modelTelefones);
				request.setAttribute("modelLogin", modelLogin);
				request.getRequestDispatcher("principal/telefone.jsp").forward(request, response);
				
			}else {
				List<ModelLogin> modelLogins = daoUsu.consultaUsuarioList(super.getUserLogado(request));
				request.setAttribute("modelLogins", modelLogins);
				request.setAttribute("totalPagina", daoUsu.totalPagina(this.getUserLogado(request)));
				request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			String usuario_pai_id = request.getParameter("id");
			String numero = request.getParameter("numero");
			
			if(!daoFone.existeFone(numero, Long.valueOf(usuario_pai_id))) {
				
				ModelTelefone modelTelefone = new ModelTelefone();
				
				modelTelefone.setNumero(numero);
				modelTelefone.setUsuario_pai_id(daoUsu.consultaUsuarioID(Long.parseLong(usuario_pai_id)));
				modelTelefone.setUsuario_cad_id(super.getUserLogadoObject(request));
				
				daoFone.gravaTelefone(modelTelefone);
				
				request.setAttribute("msg", "Salvo com sucesso");
				
			}else {
				request.setAttribute("msg", "Telefone já existe");
			}
			
			List<ModelTelefone> modelTelefones = daoFone.listTelefone(Long.parseLong(usuario_pai_id));
			
			ModelLogin modelLogin = daoUsu.consultaUsuarioID(Long.parseLong(usuario_pai_id));
			request.setAttribute("modelLogin", modelLogin);
			request.setAttribute("modelTelefones", modelTelefones);
			request.getRequestDispatcher("principal/telefone.jsp").forward(request, response);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
