<%@page import="model.ModelLogin"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>
<!-- Carregando a lib JSTL -->
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html>
<html lang="en">

<jsp:include page="head.jsp"></jsp:include>

  <body>
  <!-- Pre-loader start -->
  
  <jsp:include page="theme-loader.jsp"></jsp:include>
  
  <!-- Pre-loader end -->
  <div id="pcoded" class="pcoded">
      <div class="pcoded-overlay-box"></div>
      <div class="pcoded-container navbar-wrapper">
          
          <jsp:include page="navbar.jsp"></jsp:include>

          <div class="pcoded-main-container">
              <div class="pcoded-wrapper">
                  
                  <jsp:include page="navbarmainmenu.jsp"></jsp:include>
                  
                  <div class="pcoded-content">
                      <!-- Page-header start -->
                      
                      <jsp:include page="page-header.jsp"></jsp:include>
                      
                      <!-- Page-header end -->
                        <div class="pcoded-inner-content">
                            <!-- Main-body start -->
                            <div class="main-body">
                                <div class="page-wrapper">
                                    <!-- Page-body start -->
                                    <div class="page-body">
                                              
                                           <div class="row">
                                            <div class="col-sm-12">
                                                <!-- Basic Form Inputs card start -->
                                                <div class="card">
                                                    <div class="card-block">
                                                        <h4 class="sub-title">Cad. Usuário</h4>
		                                              
          												 <form class="form-material" enctype="multipart/form-data" action="<%= request.getContextPath() %>/ServletUsuarioController" method="post" id="formUser">
          												 
          												 	<input type="hidden" name="acao" id="acao" value="">
          												 	
                                                            <div class="form-group form-default form-static-label">
                                                                <input type="text" name="id" id="id" class="form-control"  readonly="readonly" value="${modolLogin.id}">
                                                                <span class="form-bar"></span>
                                                                <label class="float-label">ID:</label>
                                                            </div>
                                                            
                                                            <div class="form-group form-default input-group mb-4">
                                                            	<div class="input-group-pretend">
                                                            		<c:if test="${modolLogin.fotoUser != '' && modolLogin.fotoUser != null}">
                                                            			<a href="<%= request.getContextPath()%>/ServletUsuarioController?acao=downloadFoto&id=${modolLogin.id}">
                                                            				<img alt="Imagem User" id="fotoBase64" src="${modolLogin.fotoUser}" width="70px">
                                                            			</a>
                                                            		</c:if>
                                                            		<c:if test="${modolLogin.fotoUser == '' || modolLogin.fotoUser == null}">
                                                            			<img alt="Imagem User" id="fotoBase64" src="assets/images/user.png" width="70px">
                                                            		</c:if>
                                                            	</div>
                                                            	<input type="file" id="fileFoto" name="fileFoto" accept="image/*" onchange="visualizarImage('fotoBase64', 'fileFoto')" class="form-control-file" style="margin-top: 15px; margin-left: 5px;"/>
                                                            </div>
                                                            
                                                             <div class="form-group form-default">
                                                                <input type="text" name="nome" id="nome" class="form-control" required="required" value="${modolLogin.nome}">
                                                                <span class="form-bar"></span>
                                                                <label class="float-label">Nome:</label>
                                                            </div>
                                                            
                                                            <div class="form-group form-default">
                                                                <input type="email" name="email" id="email" class="form-control" required="required" autocomplete="off" value="${modolLogin.email}">
                                                                <span class="form-bar"></span>
                                                                <label class="float-label">E-mail:</label>
                                                            </div>
                                                            <div class="form-group form-default">
	                                                            <select class="form-control" aria-label="Default select example" name="perfil">
	                                                            	<option disabled="disabled">--- Selecione o Perfil ---</option>
	                                                            	<option value="ADMIN" <%
	                                                            	
		                                                            	ModelLogin modelLogin = (ModelLogin) request.getAttribute("modolLogin");
		                                                            	
		                                                            	if (modelLogin != null && modelLogin.getPerfil().equals("ADMIN")) {
		                                                            		out.print("");
		                                                            		out.print("selected=\"selected\"");
		                                                            		out.print("");
		                                                            	}%>
	                                                            	>Admin</option>
	                                                            	<option value="SECRETARIA" <%

	   	                                                            	modelLogin = (ModelLogin) request.getAttribute("modolLogin");
	   	                                                            	
	   	                                                            	if (modelLogin != null && modelLogin.getPerfil().equals("SECRETARIA")) {
		                                                            		out.print("");
		                                                            		out.print("selected=\"selected\"");
		                                                            		out.print("");
		                                                            	}%>
	                                                            	>Secretária</option>
	                                                            	<option value="AUXILIAR" <%

	   	                                                            	modelLogin = (ModelLogin) request.getAttribute("modolLogin");
	   	                                                            	
	 	                                                            	if (modelLogin != null && modelLogin.getPerfil().equals("AUXILIAR")) {
		                                                            		out.print("");
		                                                            		out.print("selected=\"selected\"");
		                                                            		out.print("");
		                                                            	}%>
	                                                            	>Auxiliar</option>
	                                                            </select>
	                                                            <span class="form-bar"></span>
	                                                            <label class="float-label">Perfil:</label>
                                                            </div>
                                                            
                                                            <div class="form-group form-default">
                                                                <input onblur="pesquisaCep();" type="text" name="cep" id="cep" class="form-control" required="required" autocomplete="off" value="${modolLogin.cep}">
                                                                <span class="form-bar"></span>
                                                                <label class="float-label">CEP</label>
                                                            </div>
                                                            
                                                            <div class="form-group form-default">
                                                                <input type="text" name="logradouro" id="logradouro" class="form-control" required="required" autocomplete="off" value="${modolLogin.logradouro}">
                                                                <span class="form-bar"></span>
                                                                <label class="float-label">Logradouro</label>
                                                            </div>
                                                            
                                                            <div class="form-group form-default">
                                                                <input type="text" name="numero" id="numero" class="form-control" required="required" autocomplete="off" value="${modolLogin.numero}">
                                                                <span class="form-bar"></span>
                                                                <label class="float-label">Numero</label>
                                                            </div>
                                                            
                                                            <div class="form-group form-default">
                                                                <input type="text" name="bairro" id="bairro" class="form-control" required="required" autocomplete="off" value="${modolLogin.bairro}">
                                                                <span class="form-bar"></span>
                                                                <label class="float-label">Bairro</label>
                                                            </div>
                                                            
                                                            <div class="form-group form-default">
                                                                <input type="text" name="cidade" id="cidade" class="form-control" required="required" autocomplete="off" value="${modolLogin.localidade}">
                                                                <span class="form-bar"></span>
                                                                <label class="float-label">Cidade</label>
                                                            </div>
                                                            
                                                            <div class="form-group form-default">
                                                                <input type="text" name="estado" id="estado" class="form-control" required="required" autocomplete="off" value="${modolLogin.estado}">
                                                                <span class="form-bar"></span>
                                                                <label class="float-label">Estado</label>
                                                            </div>
                                                            
                                                           <div class="form-group form-default">
                                                                <input type="text" name="login" id="login" class="form-control" required="required" autocomplete="off" value="${modolLogin.login}">
                                                                <span class="form-bar"></span>
                                                                <label class="float-label">Login</label>
                                                            </div>
                                                            
                                                            <div class="form-group form-default">
                                                                <input type="password" name="senha" id="senha" class="form-control" required="required" autocomplete="off" value="${modolLogin.senha}">
                                                                <span class="form-bar"></span>
                                                                <label class="float-label">Senha</label>
                                                            </div>
                                                            
                                                            <div class="form-group form-defautl form-static-label">
                                                            	<input type="radio" name="sexo" checked="checked" value="MASCULINO" 
                                                            	<%
                                                            		modelLogin = (ModelLogin) request.getAttribute("modolLogin");
	                                                            	
	                                                            	if (modelLogin != null && modelLogin.getSexo().equals("MASCULINO")) {
	                                                            		out.print("");
	                                                            		out.print("checked=\"checked\"");
	                                                            		out.print("");
                                                            		}%>
                                                            	>Masculino</>
                                                            	<input type="radio" name="sexo" value="FEMININO" 
                                                            	<%
	                                                            	modelLogin = (ModelLogin) request.getAttribute("modolLogin");
	                                                            	
	                                                            	if (modelLogin != null && modelLogin.getSexo().equals("FEMININO")) {
		                                                        		out.print("");
		                                                        		out.print("checked=\"checked\"");
		                                                        		out.print("");
                                                            		}%>
                                                            	>Feminino</>
                                                            </div>
                                                            
                                                            <button type="button" class="btn btn-primary" onclick="limparForm();" >Novo</button>
												            <button type="submit" class="btn btn-success">Salvar</button>
												            <button type="button" class="btn btn-danger" onclick="criarDeleteAjax();">Excluir</button>
												            <c:if test="${modolLogin.id > 0}">
												            	<a href="<%= request.getContextPath()%>/ServletTelefone?idUser=${modolLogin.id}" class="btn btn-warning">Telefone</a>
												            </c:if>
												            <button type="button" class="btn btn-dark" data-toggle="modal" data-target="#exampleModalUsuario">Consultar</button>
                                                        </form> 
                                                   
                                                </div>
                                                </div>
                                                </div>
                                                </div>
                                                <span id="msg">${msg}</span>
                                                
                                                <div style="height: 300px;overflow: scroll;">
													<table class="table" id="tabelaResultadosView">
													  <thead>
													    <tr>
													      <th scope="col">ID</th>
													      <th scope="col">Nome</th>
													      <th scope="col">Editar</th>
													    </tr>
													  </thead>
													  <tbody>
													  	<c:forEach items="${modelLogins}" var="ml">
													  		<tr>
													  			<td><c:out value="${ml.id}"></c:out></td>
													  			<td><c:out value="${ml.nome}"></c:out></td>
													  			<td><a class="btn btn-success" href="<%=request.getContextPath()%>/ServletUsuarioController?acao=editarUser&id=${ml.id}">Editar</a></td>
													  		</tr>
													  	</c:forEach>  
													  </tbody>
													</table>
												</div>
												
												<nav aria-label="Page navigation example">
												  <ul class="pagination">
												  	<%
												  		int totalPagina = (int) request.getAttribute("totalPagina");
												  		for(int p = 0; p < totalPagina; p++){
												  			String url = request.getContextPath() + "/ServletUsuarioController?acao=paginar&pagina=" + (p * 5);
												  			out.print("<li class=\"page-item\"><a class=\"page-link\" href=\""+url+"\">"+(p + 1)+"</a></li>");
												  		}
												  	%>
												  </ul>
												</nav>
                                                
                                    </div>
                                    <!-- Page-body end -->
                                </div>
                                <div id="styleSelector"> </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
   
   
<jsp:include page="javascripfile.jsp"></jsp:include>

<!-- Modal Pesquisa de Usuario-->
<div class="modal fade" id="exampleModalUsuario" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Pesquisa de Usuario</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <div class="input-group mb-3">
		  <input type="text" class="form-control" id="nomeBusca" placeholder="Nome" aria-label="Pesquisar por nome de Usuario" aria-describedby="basic-addon2">
			  <div class="input-group-append">
			    <button class="btn btn-success" type="button" onclick="buscarUsuario();">Buscar</button>
			  </div>
		</div>
		<div style="height: 300px;overflow: scroll;">
			<table class="table" id="tabelaResultados">
			  <thead>
			    <tr>
			      <th scope="col">ID</th>
			      <th scope="col">Nome</th>
			      <th scope="col">Editar</th>
			    </tr>
			  </thead>
			  <tbody>
			    
			  </tbody>
			</table>
		</div>
		<nav aria-label="Page navigation example">
			<ul class="pagination" id="ulPaginacaoUserAjax">
			</ul>
		</nav>
		<span id="totalResultados"></span>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Fechar</button>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">

	function editarUser(id){
		var urlAction = document.getElementById('formUser').action;
		window.location.href = urlAction + '?acao=editarUser&id='+id;
	};

	function limparForm(){
		var elements = document.getElementById("formUser").elements;//Retorna os elementos html dentro do form
		
		for(p=0; p < elements.length; p++){
			elements[p].value = '';
		}
	};
	
	function criarDelete(){
		
		if(confirm('Deseja realmente excluir os dados?')){
			document.getElementById("formUser").method = 'get';
			document.getElementById("acao").value = 'deletar';
			document.getElementById("formUser").submit();
		}
	};
	
	function criarDeleteAjax(){
		if(confirm('Deseja realmente excluir os dados?')){
			var urlAction = document.getElementById('formUser').action;
			var idUser = document.getElementById('id').value;
			
			$.ajax({
				
				method: 'get',
				url: urlAction,
				data: 'id=' + idUser + '&acao=deletarajax',
				success: function(response){
					limparForm();
					document.getElementById('msg').textContent = response;
				}
			}).fail(function(xhr, status, errorThrown){
				alert('Erro ao deletar usuário por id: ' + xhr.responseText);
			});
		}
	};
	
	function buscarUsuario(){
		
		var nomeBusca = document.getElementById('nomeBusca').value;
		
		if(nomeBusca != null && nomeBusca != '' && nomeBusca.trim() != ''){//Validando se tem valor para buscar no banco
			
			var urlAction = document.getElementById('formUser').action;
		
			$.ajax({
				
				method: 'get',
				url: urlAction,
				data: 'nomeBusca=' + nomeBusca + '&acao=buscarUserAjax',
				success: function(response, textStatus, xhr){
					var json = JSON.parse(response);
					$('#tabelaResultados > tbody > tr').remove();
					$("#ulPaginacaoUserAjax > li").remove();
					
					for(var p = 0; p < json.length; p++){//Percorre a lista q esta vindo do servlet
						$('#tabelaResultados > tbody').append('<tr> <td>'+json[p].id+'</td> <td>'+json[p].nome+'</td> <td><button onclick="editarUser('+json[p].id+')" type="button" class="btn btn-info" data-dismiss="modal">Editar</button></td></tr>')
					}
					
					document.getElementById('totalResultados').textContent = 'Resultados: ' + json.length;
					
					var totalPagina = xhr.getResponseHeader("totalPagina");
					
					for (var p = 0; p < totalPagina; p++) {
						var url = 'nomeBusca=' + nomeBusca + '&acao=buscarUserAjaxPage&pagina=' + (p * 5);
						$("#ulPaginacaoUserAjax").append('<li class="page-item"><a class="page-link" href="#" onclick="buscarUserPagAjax(\''+url+'\')">'+ (p + 1) +'</a></li>');
					}
				}
			}).fail(function(xhr, status, errorThrown){
				alert('Erro ao buscar usuário pelo nome: ' + xhr.responseText);
			});
		}
	};
	
	function buscarUserPagAjax(url){
		
		var urlAction = document.getElementById('formUser').action;
		var nomeBusca = document.getElementById('nomeBusca').value;
		
		$.ajax({
			
			method: 'get',
			url: urlAction,
			data: url,
			success: function(response, textStatus, xhr){
				var json = JSON.parse(response);
				$('#tabelaResultados > tbody > tr').remove();
				$("#ulPaginacaoUserAjax > li").remove();
				
				for(var p = 0; p < json.length; p++){//Percorre a lista q esta vindo do servlet
					$('#tabelaResultados > tbody').append('<tr> <td>'+json[p].id+'</td> <td>'+json[p].nome+'</td> <td><button onclick="editarUser('+json[p].id+')" type="button" class="btn btn-info" data-dismiss="modal">Editar</button></td></tr>')
				}
				
				document.getElementById('totalResultados').textContent = 'Resultados: ' + json.length;
				
				var totalPagina = xhr.getResponseHeader("totalPagina");
				
				for (var p = 0; p < totalPagina; p++) {
					var url = 'nomeBusca=' + nomeBusca + '&acao=buscarUserAjaxPage&pagina=' + (p * 5);
					$("#ulPaginacaoUserAjax").append('<li class="page-item"><a class="page-link" href="#" onclick="buscarUserPagAjax(\''+url+'\')">'+ (p + 1) +'</a></li>');
				}
			}
		}).fail(function(xhr, status, errorThrown){
			alert('Erro ao buscar usuário pelo nome: ' + xhr.responseText);
		});
	}
	
	function visualizarImage(fotoBase64, fileFoto){
		var preview = document.getElementById(fotoBase64); //campo img html
		var fileUser = document.getElementById(fileFoto).files[0];
		var reader = new FileReader();
		
		reader.onloadend = function (){
			preview.src = reader.result; //Carrega a foto na tela
		}
		
		if(fileUser){
			reader.readAsDataURL(fileUser);//Preview da img
		}else{
			preview.src = '';
		}
	};
	
	function pesquisaCep(){
		var cep = document.getElementById('cep').value;
		//Consulta o webservice viacep.com.br
        $.getJSON("https://viacep.com.br/ws/"+ cep +"/json/?callback=?", function(dados) {
			if(!("erro" in dados)){
				$("#logradouro").val(dados.logradouro);
				$("#bairro").val(dados.bairro);
				$("#cidade").val(dados.localidade);
				$("#estado").val(dados.uf);
				$("#cep").val(dados.cep);
			}
		});
	};
</script>
</body>
</html>
    