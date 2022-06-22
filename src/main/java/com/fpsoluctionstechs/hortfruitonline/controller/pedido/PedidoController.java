   package com.fpsoluctionstechs.hortfruitonline.controller.pedido;

import java.net.URI;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import com.fpsoluctionstechs.hortfruitonline.controller.pedido.response.ProdutosPedidosRelatorioResponse;
import com.fpsoluctionstechs.hortfruitonline.enums.StatusPedido;
import com.fpsoluctionstechs.hortfruitonline.service.relatorio.PedidosProdutosRelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import com.fpsoluctionstechs.hortfruitonline.controller.pedido.request.PedidoAtualizacaoStatusRequest;
import com.fpsoluctionstechs.hortfruitonline.controller.pedido.request.PedidoIdRequest;
import com.fpsoluctionstechs.hortfruitonline.controller.pedido.request.PedidoRequest;
import com.fpsoluctionstechs.hortfruitonline.controller.pedido.response.PedidoResponse;
import com.fpsoluctionstechs.hortfruitonline.service.PedidoService;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

	@Autowired
	private PedidoService PedidoService;

	@Autowired
	private PedidosProdutosRelatorioService pedidosProdutosRelatorioService;

	@PostMapping("")
	public ResponseEntity<PedidoResponse> cadastro(@RequestBody @Validated PedidoRequest pedidoRequest, UriComponentsBuilder uriBuilder){
		PedidoResponse pedido = PedidoService.salvarPedido(pedidoRequest);
		URI uri = uriBuilder.path("/pedido/{id}").buildAndExpand(pedido.getId()).toUri();
		return ResponseEntity.created(uri).body(pedido);
	}

	@GetMapping("")
	public List<PedidoResponse> listar() {
		return PedidoService.listar();
	}

	@PostMapping("/id")
	public ResponseEntity<PedidoResponse> buscar(@RequestBody @Validated  PedidoIdRequest pedidoIdRequest){
		try {
			return ResponseEntity.ok(PedidoService.buscarPedido(pedidoIdRequest));
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	
	}

	@PutMapping("")
	public ResponseEntity<PedidoResponse> atualizacao(@RequestBody @Validated  PedidoAtualizacaoStatusRequest pedidoAtualizacaoStatusRequest) {
		try {
			return ResponseEntity.ok(PedidoService.atualizarStatusPedido(pedidoAtualizacaoStatusRequest));
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/relatorio")
	public List<ProdutosPedidosRelatorioResponse> relatorio() {
		return pedidosProdutosRelatorioService.filtrarProdutosPedidosByStatus(StatusPedido.AGURADANDO_PROCESSAMENTO);
	}

}
