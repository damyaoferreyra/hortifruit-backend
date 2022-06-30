package com.fpsoluctionstechs.hortfruitonline;

import com.fpsoluctionstechs.hortfruitonline.controller.endereco.request.EnderecoRequest;
import com.fpsoluctionstechs.hortfruitonline.controller.medida.request.MedidaIdRequest;
import com.fpsoluctionstechs.hortfruitonline.controller.pedido.request.PedidoRequest;
import com.fpsoluctionstechs.hortfruitonline.controller.produto.request.ProdutoIdRequest;
import com.fpsoluctionstechs.hortfruitonline.controller.produtoPedido.request.ProdutoPedidoRequest;
import com.fpsoluctionstechs.hortfruitonline.enums.StatusPedido;
import com.fpsoluctionstechs.hortfruitonline.model.*;
import com.fpsoluctionstechs.hortfruitonline.respository.*;
import com.fpsoluctionstechs.hortfruitonline.service.FilesService;
import com.fpsoluctionstechs.hortfruitonline.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
public class HortfruitOnlineApplication {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MedidaRepository medidaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private FilesService filesService;

    public static void main(String[] args) {
        SpringApplication.run(HortfruitOnlineApplication.class, args);

    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            filesService.init();

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            Usuario usuario = Usuario.builder()
                    .username("mauro")
                    .password(bCryptPasswordEncoder.encode("mauro"))
                    .build();
            usuarioRepository.save(usuario);


            Categoria categoriaFruta = Categoria.builder().nome("Frutas").orderExibicao(1).build();
            Categoria categoriaPromocao = Categoria.builder().nome("Promoção").orderExibicao(1).build();

            categoriaRepository.save(categoriaFruta);
            categoriaRepository.save(categoriaPromocao);

            Medida medidaKG = Medida.builder().nome("KG").unidadeEmGramas(BigDecimal.valueOf(1000l)).build();
            medidaRepository.save(medidaKG);


            Produto banana = criarESalvarProduto("Banana prata madura", "1656552162550-banana-da-prata.webp", Arrays.asList(categoriaFruta, categoriaPromocao).stream().collect(Collectors.toSet()), medidaKG);
            Produto maca = criarESalvarProduto("Maça", "1656552204750-maca.jpg", Arrays.asList(categoriaFruta, categoriaPromocao).stream().collect(Collectors.toSet()), medidaKG);
            Produto goiaba = criarESalvarProduto("Goiaba", "1656552191905-goiaba.jpg", Arrays.asList(categoriaFruta, categoriaPromocao).stream().collect(Collectors.toSet()), medidaKG);
            Produto morango = criarESalvarProduto("Morango", "1656552259199-morango.webp", Arrays.asList(categoriaFruta, categoriaPromocao).stream().collect(Collectors.toSet()), medidaKG);
            Produto mamao = criarESalvarProduto("Mamão", "1656552216023-mamao.jpg", Arrays.asList(categoriaFruta, categoriaPromocao).stream().collect(Collectors.toSet()), medidaKG);
            Produto abacaxi = criarESalvarProduto("Abacaxi", "1656552129382-abacaxi.jpg", Arrays.asList(categoriaFruta, categoriaPromocao).stream().collect(Collectors.toSet()), medidaKG);
            Produto maracuja = criarESalvarProduto("Maracujá", "1656552246063-maracuja.webp", Arrays.asList(categoriaFruta, categoriaPromocao).stream().collect(Collectors.toSet()), medidaKG);
            Produto batata = criarESalvarProduto("Batata", "1656552177531-batata.jpg", Arrays.asList(categoriaFruta, categoriaPromocao).stream().collect(Collectors.toSet()), medidaKG);
            Produto alface = criarESalvarProduto("Alface", "1656552147600-alface.webp", Arrays.asList(categoriaFruta, categoriaPromocao).stream().collect(Collectors.toSet()), medidaKG);


            adicionarPedidosMock(banana);
            adicionarPedidosMock(maca);
            adicionarPedidosMock(goiaba);
            adicionarPedidosMock(morango);
            adicionarPedidosMock(mamao);
            adicionarPedidosMock(abacaxi);
            adicionarPedidosMock(maracuja);
            adicionarPedidosMock(batata);
            adicionarPedidosMock(alface);

            salvarPedidoByService();
        };


    }

    private void adicionarPedidosMock(Produto produto){
        registrarPedido(produto, "Mauro", 10);
        registrarPedido(produto, "Damiao", 5);
        registrarPedido(produto, "Wenderson", 2);
    }


    private Produto criarESalvarProduto(String nome, String urlImagem, Set<Categoria> categorias, Medida medida) {

        Produto produto = Produto.builder()
                .nome(nome)
                .descricao(nome)
                .imagem(urlImagem)
                .categorias(categorias)
                .build();

        produto.setMedidas(
                Arrays.asList(
                        MedidaProduto.builder()
                                .medida(medida).preco(BigDecimal.valueOf(12.5)).produto(produto)
                                .build()
                ).stream().collect(Collectors.toSet())
        );
        return produtoRepository.save(produto);
    }

    private void registrarPedido(Produto produto, String nomeCliente, int quantidade) {

        MedidaProduto medidaProduto = produto.getMedidas().stream().findFirst().get();

        ProdutoPedido produtoPedido = ProdutoPedido.builder()
                .produto(produto)
                .quantidade(quantidade)
                .medidaProduto(medidaProduto)
                .unidadeMedidaGrama(medidaProduto.getMedida().getUnidadeEmGramas())
                .totalMedidaGrama(medidaProduto.getMedida().getUnidadeEmGramas().multiply(BigDecimal.valueOf(quantidade)))
                .precoUnidade(medidaProduto.getPreco())
                .precoTotal(medidaProduto.getPreco().multiply(BigDecimal.valueOf(quantidade)))
                .build();

        Pedido pedido = Pedido.builder()
                .contato("83 986757463")
                .cliente(nomeCliente)
                .endereco(Endereco.builder()
                        .logradouro("Rua não conheço")
                        .numero("S/N")
                        .bairro("AP. 202")
                        .bairro("Imaculada")
                        .referencia("Escola senhor do bonfim")
                        .complemento("casa")
                        .build())
                .produtoPedidos(Arrays.asList(produtoPedido))
                .build();

        produtoPedido.setPedido(pedido);
        pedido.setStatus(StatusPedido.AGUARDANDO_CLIENTE_CONFIRMAR_PEDIDO);
        pedido.setValorTotal(medidaProduto.getPreco().multiply(BigDecimal.valueOf(quantidade)));
        pedidoRepository.save(pedido);
    }


    private void salvarPedidoByService() {
        PedidoRequest pedidoRequest = new PedidoRequest();
        pedidoRequest.setCliente("João Ferreira");
        pedidoRequest.setContato("21 9988312");

        EnderecoRequest enderecoRequest = new EnderecoRequest();
        enderecoRequest.setLogradouro("Rua chico duarte");
        enderecoRequest.setNumero("S/N");
        enderecoRequest.setComplemento("AP. 202");
        enderecoRequest.setBairro("Imaculada");
        enderecoRequest.setReferencia("Senhor do bonfim");
        pedidoRequest.setEndereco(enderecoRequest);


        ProdutoPedidoRequest produtoPedidoRequest = new ProdutoPedidoRequest();
        produtoPedidoRequest.setQuantidade(90);

        MedidaIdRequest medidaIdRequest = new MedidaIdRequest();
        medidaIdRequest.setId(1l);
        produtoPedidoRequest.setMedida(medidaIdRequest);

        ProdutoIdRequest produtoIdRequest = new ProdutoIdRequest();
        produtoIdRequest.setId(1l);
        produtoPedidoRequest.setProduto(produtoIdRequest);

        pedidoRequest.setProdutoPedidos(Arrays.asList(produtoPedidoRequest));

        pedidoService.salvarPedido(pedidoRequest);
    }


}
