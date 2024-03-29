package servlets;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.fasterxml.jackson.databind.ObjectMapper;

import beanDTO.BeanGraficoSalarioUserDTO;
import dao.DAOUsuarioRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.ModelLogin;
import util.ReportUtil;

@MultipartConfig
@WebServlet( urlPatterns = {"/ServletUsuarioController"})
public class ServletUsuarioController extends ServletGenericUtil {
	
	private static final long serialVersionUID = 1L;
	
	
	private DAOUsuarioRepository daoUsuarioRepository = new DAOUsuarioRepository();

    public ServletUsuarioController() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			String acao = request.getParameter("acao");
			
			if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("deletar")) {
				
				String idUser = request.getParameter("id");
				daoUsuarioRepository.deletarUser(idUser);
				List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList(super.getUserLogado(request));
				request.setAttribute("modelLogins", modelLogins);
				request.setAttribute("msg", "Excluido com sucesso!");
				request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogado(request)));
				request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
				
			}else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("deletarajax")) {
				
				String idUser = request.getParameter("id");
				daoUsuarioRepository.deletarUser(idUser);
				request.setAttribute("msg", "Excluido com sucesso!");
				response.getWriter().write("Excluido com sucesso!");
				
			}else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarUserAjax")) {
				
				String nomeUsuario = request.getParameter("nomeBusca");
				List<ModelLogin> dadosJson = daoUsuarioRepository.consultaUsuarioList(nomeUsuario, super.getUserLogado(request));
				ObjectMapper object = new ObjectMapper();
			    String json = object.writeValueAsString(dadosJson);
			    response.addHeader("totalPagina",""+ daoUsuarioRepository.consultaUsuarioListTotalPaginaPaginacao(nomeUsuario, super.getUserLogado(request)));
				response.getWriter().write(json);
				
			}else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarUserAjaxPage")) {
				
				String nomeUsuario = request.getParameter("nomeBusca");
				String pagina = request.getParameter("pagina");
				List<ModelLogin> dadosJson = daoUsuarioRepository.consultaUsuarioListOffSet(nomeUsuario, super.getUserLogado(request), Integer.parseInt(pagina));
				ObjectMapper object = new ObjectMapper();
			    String json = object.writeValueAsString(dadosJson);
			    response.addHeader("totalPagina",""+ daoUsuarioRepository.consultaUsuarioListTotalPaginaPaginacao(nomeUsuario, super.getUserLogado(request)));
				response.getWriter().write(json);
				
			}else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("editarUser")) {
				
				String idUser = request.getParameter("id");
				ModelLogin modelLogin = daoUsuarioRepository.consultaUsuarioId(idUser, super.getUserLogado(request));
				List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList(super.getUserLogado(request));
				request.setAttribute("msg", "Usuario em ediçao");
				request.setAttribute("modolLogin", modelLogin);
				request.setAttribute("modelLogins", modelLogins);
				request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogado(request)));
				request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
				
			}else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("listarUser")) {
				
				List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList(super.getUserLogado(request));
				request.setAttribute("msg", "Usuario em ediçao");
				request.setAttribute("modelLogins", modelLogins);
				request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogado(request)));
				request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
				
			}else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("downloadFoto")) {
				String idUser = request.getParameter("id");
				
				ModelLogin modelLogin = daoUsuarioRepository.consultaUsuarioId(idUser, super.getUserLogado(request));
				if(modelLogin.getFotoUser() != null && !modelLogin.getFotoUser().isEmpty()) {
					response.setHeader("Content-Disposition", "attachment;filename=arquivo." + modelLogin.getExtensaoFotoUser());
					response.getOutputStream().write(new Base64().decodeBase64(modelLogin.getFotoUser().split("\\,")[1]));
				}
			}else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("paginar")) {
				Integer offset = Integer.parseInt(request.getParameter("pagina"));
				List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioListPaginada(this.getUserLogado(request), offset);
				
				request.setAttribute("modelLogins", modelLogins);
				request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogado(request)));
				request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
			}else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("imprimirRelatorioUser")) {
				String dtInicial = request.getParameter("dtInicial");
				String dtFinal = request.getParameter("dtFinal");
				
				if(dtInicial == null || dtInicial.isEmpty() && dtFinal == null || dtFinal.isEmpty()){
					request.setAttribute("listaUser", daoUsuarioRepository.consultaUsuarioListRel(super.getUserLogado(request)));
				}else {
					request.setAttribute("listaUser", daoUsuarioRepository.consultaUsuarioListRel(super.getUserLogado(request), dtInicial, dtFinal));
				}
				
				request.setAttribute("dtInicial", dtInicial);
				request.setAttribute("dtFinal", dtFinal);
				request.getRequestDispatcher("principal/relatorioUser.jsp").forward(request, response);
			}else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("imprimirRelatorioPDF") || acao.equalsIgnoreCase("imprimirRelatorioExcel")) {
				String dtInicial = request.getParameter("dtInicial");
				String dtFinal = request.getParameter("dtFinal");
				
				List<ModelLogin> modelLogins = null; 
				
				if(dtInicial == null || dtInicial.isEmpty() && dtFinal == null || dtFinal.isEmpty()){
					modelLogins = daoUsuarioRepository.consultaUsuarioListRel(super.getUserLogado(request));
				}else {
					modelLogins = daoUsuarioRepository.consultaUsuarioListRel(super.getUserLogado(request), dtInicial, dtFinal);
				}
				
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("PARAM_SUB_REPORT", request.getServletContext().getRealPath("relatorio") + File.separator);
				
				byte[] relatorio = null;
				String extensao = "";
				
				if(acao.equalsIgnoreCase("imprimirRelatorioPDF")) {
					relatorio = new ReportUtil().geraRelatorioPDF(modelLogins, "rel-user-jsp", params, request.getServletContext());
					extensao = "pdf";
				}else if(acao.equalsIgnoreCase("imprimirRelatorioExcel")){
					relatorio = new ReportUtil().geraRelatorioExcel(modelLogins, "rel-user-jsp", params, request.getServletContext());
					extensao = "xls";
				}
				
				response.setHeader("Content-Disposition", "attachment;filename=arquivo." + extensao);
				response.getOutputStream().write(relatorio);
			}else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("graficoSalario")){
				String dtInicial = request.getParameter("dtInicial");
				String dtFinal = request.getParameter("dtFinal");
				
				if(dtInicial == null || dtInicial.isEmpty() && dtFinal == null || dtFinal.isEmpty()){
					BeanGraficoSalarioUserDTO beanGrafico = daoUsuarioRepository.montarGraficoMediaSalario(super.getUserLogado(request));
					ObjectMapper mapper = new ObjectMapper();
					String json = mapper.writeValueAsString(beanGrafico);
					
					response.getWriter().write(json);
				}else {
					BeanGraficoSalarioUserDTO beanGrafico = daoUsuarioRepository.montarGraficoMediaSalario(super.getUserLogado(request), dtInicial, dtFinal);
					ObjectMapper mapper = new ObjectMapper();
					String json = mapper.writeValueAsString(beanGrafico);
					
					response.getWriter().write(json);
				}
			}else {
				List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList(super.getUserLogado(request));
				request.setAttribute("modelLogins", modelLogins);
				request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogado(request)));
				request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
			}
		}catch (Exception e) {
			e.printStackTrace();
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
		String msg = "Operaçao realizada com sucesso!";	
		
		String id = request.getParameter("id");
		String nome = request.getParameter("nome");
		String email = request.getParameter("email");
		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		String perfil = request.getParameter("perfil");
		String sexo = request.getParameter("sexo");
		String cep = request.getParameter("cep");
		String logradouro = request.getParameter("logradouro");
		String numero = request.getParameter("numero");
		String bairro = request.getParameter("bairro");
		String cidade = request.getParameter("cidade");
		String estado = request.getParameter("estado");
		String dtNascimento = request.getParameter("dtNascimento");
		String rendaMensal = request.getParameter("rendaMensal");
		rendaMensal = rendaMensal.split("\\ ")[1].replaceAll("\\.", "").replaceAll("\\,", ".");
		
		ModelLogin modelLogin = new ModelLogin();
		
		modelLogin.setId(id != null && !id.isEmpty() ? Long.parseLong(id) : null);
		modelLogin.setNome(nome);
		modelLogin.setEmail(email);
		modelLogin.setLogin(login);
		modelLogin.setSenha(senha);
		modelLogin.setPerfil(perfil);
		modelLogin.setSexo(sexo);
		modelLogin.setCep(cep);
		modelLogin.setLogradouro(logradouro);
		modelLogin.setNumero(numero);
		modelLogin.setBairro(bairro);
		modelLogin.setLocalidade(cidade);
		modelLogin.setEstado(estado);
		modelLogin.setDtNascimento(Date.valueOf(new SimpleDateFormat("yyyy-mm-dd").format(new SimpleDateFormat("dd/mm/yyyy").parse(dtNascimento))));
		modelLogin.setRendaMensal(Double.parseDouble(rendaMensal));
		
		if(ServletFileUpload.isMultipartContent(request)) {
			Part part = request.getPart("fileFoto");//Pega foto da tela
			if(part.getSize() > 0) {
				byte[] foto = IOUtils.toByteArray(part.getInputStream());//Converte imagem para byte
				String imagemBase64 = "data:image/" + part.getContentType().split("\\/")[1] + ";base64," + new Base64().encodeBase64String(foto);
				modelLogin.setFotoUser(imagemBase64);
				modelLogin.setExtensaoFotoUser(part.getContentType().split("\\/")[1]);
			}
		}
		
		
		if (daoUsuarioRepository.validarLogin(modelLogin.getLogin()) && modelLogin.getId() == null) {
			msg = "Ja existe usuario com o mesmo login, informe outro login;";
		}else {
			if(modelLogin.isNovo()) {
				msg = "Gravado com sucesso!";
			}else {
				msg = "Atualizado com sucesso!";
			}
		   modelLogin = daoUsuarioRepository.gravarUsuario(modelLogin, super.getUserLogado(request));
		}
		
		
		request.setAttribute("msg", msg);
		request.setAttribute("modolLogin", modelLogin);
		List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList(super.getUserLogado(request));
		request.setAttribute("modelLogins", modelLogins);
		request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogado(request)));
		request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
		
		} catch (Exception e) {
			e.printStackTrace();
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}
		
	}

}
