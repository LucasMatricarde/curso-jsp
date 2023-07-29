package servlets;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.DAOUsuarioRepository;
import model.ModelLogin;

@WebServlet("/ServletTelefone")
public class ServletTelefone extends ServletGenericUtil {
	
	private static final long serialVersionUID = 1L;
	
	private DAOUsuarioRepository daoUsu = new DAOUsuarioRepository();
       
    public ServletTelefone() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String idUser = request.getParameter("idUser");
		
			if(idUser != null && !idUser.isEmpty()) {
				ModelLogin modelLogin = daoUsu.consultaUsuarioID(Long.parseLong(idUser));
				
				request.setAttribute("usuario", modelLogin);
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
		doGet(request, response);
	}

}
