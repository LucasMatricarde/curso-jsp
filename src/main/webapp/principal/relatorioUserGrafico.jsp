<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

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
													<h4 class="sub-title">Relátorio Usuário</h4>

													<form class="form-material" action="<%=request.getContextPath()%>/ServletUsuarioController" method="get" id="formUser">

														<input type="hidden" id="acaoRelatorioImprimirTipo" name="acao" value="imprimirRelatorioUser">
														<div class="form-row align-items-center">
															<div class="col-sm-3 my-1">
																<label class="sr-only" for="dtInicial">Data Inicial</label> 
																<input value="${dtInicial}" type="text" class="form-control" id="dtInicial" name="dtInicial">
															</div>
															<div class="col-sm-3 my-1">
																<label class="sr-only" for="dtFinal">Data Final</label> 
																<input value="${dtFinal}" type="text" class="form-control" id="dtFinal" name="dtFinal">
															</div>
															<div class="col-auto my-1">
																<button type="button" onclick="gerarGrafico();" class="btn btn-primary">Gerar Gráfico</button>
															</div>
														</div>
													</form>

													<div>
													
														<div>
															<canvas id="myChart"></canvas>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<!-- Page-body end -->
							</div>
							<div id="styleSelector"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<jsp:include page="javascripfile.jsp"></jsp:include>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script type="text/javascript">

	function gerarGrafico(){
		debugger;
		var urlAction = document.getElementById('formUser').action;
		var dtInicial = document.getElementById('dtInicial').value;
		var dtFinal = document.getElementById('dtFinal').value;
		
		$.ajax({
			
			method: 'get',
			url: urlAction,
			data: 'dtInicial=' + dtInicial + '&dtFinal=' + dtFinal + '&acao=graficoSalario',
			success: function(response){
				var json = JSON.parse(response);
				var myChart = new Chart(document.getElementById('myChart'), {
					  type: 'line',
					  data: {
							labels: json.perfils,
							datasets: [{
								label: 'Gráfico de média salarial por tipo',
								backgroundColor: 'rgb(255, 99, 132)',
								borderColor: 'rgb(255, 99, 132)',
								data: json.salarios,
							}]
						},
					options: {},
				});
			}
		}).fail(function(xhr, status, errorThrown){
			alert('Erro ao buscar dados para o gráfico: ' + xhr.responseText);
		});
	}
	
	var dtInicial = $("#dtInicial").val();
	
	if(dtInicial != ""){
		var dtFormat = new Date(dtInicial);
	
		$("#dtInicial").val(dtFormat.toLocaleDateString('pt-BR', {
			timezone : 'UTC'
		}));
	}
	
	var dtFinal = $("#dtFinal").val();
	if(dtFinal != ""){
		var dtFormat1 = new Date(dtFinal);
	
		$("#dtFinal").val(dtFormat1.toLocaleDateString('pt-BR', {
			timezone : 'UTC'
		}));
	}

	$( function() {
		  $("#dtInicial").datepicker({
			    dateFormat: 'dd/mm/yy',
			    dayNames: ['Domingo','Segunda','Terça','Quarta','Quinta','Sexta','Sábado'],
			    dayNamesMin: ['D','S','T','Q','Q','S','S','D'],
			    dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex','Sáb','Dom'],
			    monthNames: ['Janeiro','Fevereiro','Março','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'],
			    monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun','Jul','Ago','Set','Out','Nov','Dez'],
			    nextText: 'Próximo',
			    prevText: 'Anterior'
			});
		} );

	$( function() {
		  $("#dtFinal").datepicker({
			    dateFormat: 'dd/mm/yy',
			    dayNames: ['Domingo','Segunda','Terça','Quarta','Quinta','Sexta','Sábado'],
			    dayNamesMin: ['D','S','T','Q','Q','S','S','D'],
			    dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex','Sáb','Dom'],
			    monthNames: ['Janeiro','Fevereiro','Março','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'],
			    monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun','Jul','Ago','Set','Out','Nov','Dez'],
			    nextText: 'Próximo',
			    prevText: 'Anterior'
			});
		} );
	
	function imprimirPdf(){
		document.getElementById("acaoRelatorioImprimirTipo").value = 'imprimirRelatorioPDF';
		$("#formUser").submit();
		return false;
	}
	
</script>
</body>

</html>
