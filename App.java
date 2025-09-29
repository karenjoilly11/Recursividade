import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class App {

	/** Nome do arquivo de dados. O arquivo deve estar localizado na raiz do projeto */
    static String nomeArquivoDados;
    
    /** Scanner para leitura de dados do teclado */
    static Scanner teclado;

    /** Vetor de produtos cadastrados */
    static Produto[] produtosCadastrados;

    /** Quantidade de produtos cadastrados atualmente no vetor */
    static int quantosProdutos = 0;

    /** Vetor de pedidos cadastrados */
    static Pedido[] pedidosCadastrados;
    
    /** Vetor de pedidos ordenados pela data do pedido - TAREFA 2 */
    static Pedido[] pedidosOrdenadosPorData;
    
    /** Quantidade de pedidos cadastrados atualmente no vetor */
    static int quantPedidos = 0;
    
    static IOrdenator<Pedido> ordenador;
    
    static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /** Gera um efeito de pausa na CLI. Espera por um enter para continuar */
    static void pausa() {
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    /** Cabeçalho principal da CLI do sistema */
    static void cabecalho() {
        limparTela();
        System.out.println("AEDs II COMÉRCIO DE COISINHAS");
        System.out.println("=============================");
    }
    
    static <T extends Number> T lerOpcao(String mensagem, Class<T> classe) {
        
    	T valor;
        
    	System.out.println(mensagem);
    	try {
            valor = classe.getConstructor(String.class).newInstance(teclado.nextLine());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException 
        		| InvocationTargetException | NoSuchMethodException | SecurityException e) {
            return null;
        }
        return valor;
    }
    
    /**
     * Lê os dados de um arquivo-texto e retorna um vetor de produtos. Arquivo-texto no formato
     * N  (quantidade de produtos) <br/>
     * tipo;descrição;preçoDeCusto;margemDeLucro;[dataDeValidade] <br/>
     * Deve haver uma linha para cada um dos produtos. Retorna um vetor vazio em caso de problemas com o arquivo.
     * @param nomeArquivoDados Nome do arquivo de dados a ser aberto.
     * @return Um vetor com os produtos carregados, ou vazio em caso de problemas de leitura.
     */
    static Produto[] lerProdutos(String nomeArquivoDados) {
    	
    	Scanner arquivo = null;
    	int numProdutos;
    	String linha;
    	Produto produto;
    	Produto[] produtosCadastrados;
    	
    	try {
    		arquivo = new Scanner(new File(nomeArquivoDados), Charset.forName("UTF-8"));
    		
    		numProdutos = Integer.parseInt(arquivo.nextLine());
    		produtosCadastrados = new Produto[numProdutos];
    		
    		for (int i = 0; i < numProdutos; i++) {
    			linha = arquivo.nextLine();
    			produto = Produto.criarDoTexto(linha);
    			produtosCadastrados[i] = produto;
    		}
    		quantosProdutos = numProdutos;
    		
    	} catch (IOException excecaoArquivo) {
    		produtosCadastrados = null;
    	} finally {
    		if (arquivo != null) {
    			arquivo.close();
    		}
    	}
    	
    	return produtosCadastrados;
    }
    
    /**
     * Lê os dados de um arquivo-texto e retorna um vetor de pedidos. Arquivo-texto no formato
     * N  (quantidade de pedidos) <br/>
     * dataDoPedido;formaDePagamento;descrições dos produtos do pedido <br/>
     * Deve haver uma linha para cada um dos pedidos. Retorna um vetor vazio em caso de problemas com o arquivo.
     * @param nomeArquivoDados Nome do arquivo de dados a ser aberto.
     * @return Um vetor com os pedidos carregados, ou vazio em caso de problemas de leitura.
     */
    static Pedido[] lerPedidos(String nomeArquivoDados) {
    	
    	Pedido[] pedidosCadastrados;
    	Scanner arquivo = null;
    	int numPedidos;
    	String linha;
    	Pedido pedido;
    	
    	try {
    		arquivo = new Scanner(new File(nomeArquivoDados), Charset.forName("UTF-8"));
    		
    		numPedidos = Integer.parseInt(arquivo.nextLine());
    		pedidosCadastrados = new Pedido[numPedidos];
    		
    		for (int i = 0; i < numPedidos; i++) {
    			linha = arquivo.nextLine();
    			pedido = criarPedido(linha);
    			pedidosCadastrados[i] = pedido;
    		}
    		quantPedidos = numPedidos;
    		
    		// TAREFA 2: CRIAR CÓPIA ORDENADA POR DATA
    		if (quantPedidos > 0) {
    			pedidosOrdenadosPorData = Arrays.copyOf(pedidosCadastrados, quantPedidos);
    			Arrays.sort(pedidosOrdenadosPorData, new ComparadorPorDataPedido());
    		}
    		
    	} catch (IOException excecaoArquivo) {
    		pedidosCadastrados = null;
    		pedidosOrdenadosPorData = null;
    	} finally {
    		if (arquivo != null) {
    			arquivo.close();
    		}
    	}
    	
    	return pedidosCadastrados;
    }
    
    private static Pedido criarPedido(String dados) {
    	
    	String[] dadosPedido;
    	DateTimeFormatter formatoData;
    	LocalDate dataDoPedido;
    	int formaDePagamento;
    	Pedido pedido;
    	Produto produto;
    	
    	dadosPedido = dados.split(";");
    	
    	formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    	dataDoPedido = LocalDate.parse(dadosPedido[0], formatoData);
    	
    	formaDePagamento = Integer.parseInt(dadosPedido[1]);
    	
    	pedido = new Pedido(dataDoPedido, formaDePagamento);
    	
    	for (int i = 2; i < dadosPedido.length; i++) {
    		produto = pesquisarProduto(dadosPedido[i]);
    		if (produto != null) {
    			pedido.incluirProduto(produto);
    		}
    	}
    	return pedido;
    }
    
    /** Localiza um produto no vetor de produtos cadastrados, a partir do nome de produto passado como parâmetro para esse método. 
     *  A busca não é sensível ao caso.
     *  @param pesquisado Nome do produto a ser pesquisado no vetor de produtos cadastrados. 
     *  @return O produto encontrado ou null, caso o produto não tenha sido localizado no vetor de produtos cadastrados.
     */
    static Produto pesquisarProduto(String pesquisado) {
        
    	Produto produto = null;
    	Boolean localizado = false;
    	
    	for (int i = 0; (i < quantosProdutos && !localizado); i++) {
        	if (produtosCadastrados[i].descricao.equals(pesquisado)) {
        		produto = produtosCadastrados[i];
        		localizado = true;
        	}
        }
        
        if (!localizado) {
        	return null;
        } else {
        	return(produto);
        }     
    }
    
    /** Imprime o menu principal, lê a opção do usuário e a retorna (int).
     * @return Um inteiro com a opção do usuário.
    */
    static int menu() {
        cabecalho();
        System.out.println("1 - Procurar por pedidos realizados em uma data");
        System.out.println("2 - Ordenar pedidos");
        System.out.println("3 - Embaralhar pedidos");
        System.out.println("4 - Listar todos os pedidos");
        System.out.println("0 - Finalizar");
        
        return lerOpcao("Digite sua opção: ", Integer.class);
    }
    
    /** TAREFA 2: Localiza pedidos no vetor de pedidos, a partir da data do pedido informada pelo usuário,
     *  e imprime seus dados.
     *  O método solicita ao usuário a data desejada (no formato dd/MM/yyyy),
     *  e, em seguida, realiza a busca  
     *  por todos os pedidos que correspondem à data informada.
     *  A busca é otimizada pela ordenação prévia do vetor de pedidos por data.
     *  Em caso de não encontrar nenhum pedido, imprime uma mensagem padrão */
    static void localizarPedidosPorData() {
        
        cabecalho();
        System.out.println("BUSCAR PEDIDOS POR DATA");
        
        if (quantPedidos == 0 || pedidosOrdenadosPorData == null) {
            System.out.println("Nenhum pedido carregado.");
            return;
        }
        
        // Ler data do usuário
        System.out.print("Digite a data (dd/mm/aaaa): ");
        String dataStr = teclado.nextLine();
        
        LocalDate data;
        try {
            data = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            System.out.println("Data inválida!");
            return;
        }
        
        System.out.println("Pedidos realizados em: " + 
            data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("====================");
        
        // Busca binária na cópia ordenada por data
        int inicio = 0;
        int fim = quantPedidos - 1;
        boolean encontrou = false;
        
        // Buscar primeiro pedido com a data especificada
        int posicao = -1;
        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;
            int comparacao = pedidosOrdenadosPorData[meio].getDataPedido().compareTo(data);
            
            if (comparacao == 0) {
                posicao = meio;
                break;
            } else if (comparacao < 0) {
                inicio = meio + 1;
            } else {
                fim = meio - 1;
            }
        }
        
        // Se encontrou, mostrar todos os pedidos com essa data
        if (posicao != -1) {
            // Encontrar o primeiro pedido com esta data
            int primeiro = posicao;
            while (primeiro > 0 && pedidosOrdenadosPorData[primeiro - 1].getDataPedido().equals(data)) {
                primeiro--;
            }
            
            // Mostrar todos os pedidos com esta data
            for (int i = primeiro; i < quantPedidos && pedidosOrdenadosPorData[i].getDataPedido().equals(data); i++) {
                System.out.println(pedidosOrdenadosPorData[i].toString());
                System.out.println("--------------------");
                encontrou = true;
            }
        }
        
        if (!encontrou) {
            System.out.println("Nenhum pedido encontrado para esta data.");
        }
    }
    
    static int exibirMenuOrdenadores() {
        cabecalho();
        System.out.println("1 - Bolha");
        System.out.println("2 - Inserção"); 
        System.out.println("3 - Seleção"); 
        System.out.println("4 - Mergesort"); 
        System.out.println("5 - Heapsort"); 
        System.out.println("0 - Finalizar");
       
        return lerOpcao("Digite sua opção: ", Integer.class);
    }
    
    static int exibirMenuComparadores() {
        cabecalho();
        System.out.println("1 - Por código");
        System.out.println("2 - Por data");
        System.out.println("3 - Por valor");
        
        return lerOpcao("Digite sua opção: ", Integer.class);
    }
    
    /** TAREFA 1: Ordena o vetor de pedidos cadastrados empregando um método de ordenação selecionado pelo
     *  usuário, dentre os seguintes: bolha, seleção, inserção, mergesort e heapsort.
     *  O usuário também escolhe um critério de ordenação, a saber: por código, data ou valor do pedido. 
     *  O método interage com o usuário por meio de menus e aplica 
     *  a ordenação escolhida. 
     *  Se o critério de ordenação escolhido for a data do pedido, em caso de empate, o critério de 
     *  desempate é o código identificador do pedido.
     *  Se o critério de ordenação escolhido for o valor final do pedido, em caso de empate, o critério de 
     *  desempate é a quantidade de produtos no pedido. Em caso de novo empate, o critério de 
     *  desempate é o código identificador do pedido.
     *  Ao final, exibe o tempo total gasto no processo de ordenação, em ms. */
    static void ordenarPedidos(){
    	
        cabecalho();
        System.out.println("ORDENAR PEDIDOS");
        
        if (quantPedidos == 0) {
            System.out.println("Nenhum pedido carregado.");
            return;
        }
        
        // Menu de métodos de ordenação
        int opcaoMetodo = exibirMenuOrdenadores();
        if (opcaoMetodo < 1 || opcaoMetodo > 5) {
            System.out.println("Opção inválida!");
            return;
        }
        
        // Menu de critérios de ordenação
        int opcaoCriterio = exibirMenuComparadores();
        if (opcaoCriterio < 1 || opcaoCriterio > 3) {
            System.out.println("Opção inválida!");
            return;
        }
        
        // Configurar ordenador
        IOrdenator<Pedido> ordenador = null;
        switch (opcaoMetodo) {
            case 1 -> ordenador = new Bubblesort<>();
            case 2 -> ordenador = new InsertSort<>();
            case 3 -> ordenador = new SelectionSort<>();
            case 4 -> ordenador = new Mergesort<>();
            case 5 -> ordenador = new Heapsort<>();
        }
        
        // Configurar comparador
        Comparator<Pedido> comparador = null;
        switch (opcaoCriterio) {
            case 1 -> comparador = new ComparadorPorCodigoPedido();
            case 2 -> comparador = new ComparadorPorDataPedido();
            case 3 -> comparador = new ComparadorPorValorPedido();
        }
        
        // Aplicar ordenação
        if (ordenador != null && comparador != null) {
            pedidosCadastrados = ordenador.ordenar(pedidosCadastrados, comparador);
            System.out.println("Pedidos ordenados com sucesso!");
            System.out.println("Tempo gasto: " + ordenador.getTempoOrdenacao() + " ms.");
            
            // Atualizar cópia ordenada por data se necessário
            if (opcaoCriterio != 2) { // Se não foi ordenado por data, manter cópia específica
                pedidosOrdenadosPorData = Arrays.copyOf(pedidosCadastrados, quantPedidos);
                Arrays.sort(pedidosOrdenadosPorData, new ComparadorPorDataPedido());
            } else {
                // Se foi ordenado por data, atualizar a referência
                pedidosOrdenadosPorData = pedidosCadastrados;
            }
        } else {
            System.out.println("Erro ao configurar ordenação.");
        }
    }

    static void embaralharPedidos(){
        if (quantPedidos > 0) {
            Collections.shuffle(Arrays.asList(pedidosCadastrados).subList(0, quantPedidos));
            System.out.println("Pedidos embaralhados!");
            
            // Manter cópia ordenada por data atualizada
            pedidosOrdenadosPorData = Arrays.copyOf(pedidosCadastrados, quantPedidos);
            Arrays.sort(pedidosOrdenadosPorData, new ComparadorPorDataPedido());
        } else {
            System.out.println("Nenhum pedido para embaralhar.");
        }
    }

    /** Lista todos os pedidos cadastrados, numerados, um por linha */
    static void listarTodosOsPedidos() {
    	
        cabecalho();
        System.out.println("\nPedidos cadastrados: ");
        if (quantPedidos == 0) {
            System.out.println("Nenhum pedido cadastrado.");
        } else {
            for (int i = 0; i < quantPedidos; i++) {
                System.out.println(String.format("%02d - %s\n", (i + 1), pedidosCadastrados[i].toString()));
            }
        }
    }
    
    public static void main(String[] args) {
		
    	teclado = new Scanner(System.in, Charset.forName("UTF-8"));
        
    	nomeArquivoDados = "produtos.txt";
        produtosCadastrados = lerProdutos(nomeArquivoDados);
       
        String nomeArquivoPedidos = "pedidos.txt";
        pedidosCadastrados = lerPedidos(nomeArquivoPedidos);
        
        int opcao = -1;
      
        do{
        	opcao = menu();
            switch (opcao) {
                case 1 -> localizarPedidosPorData();
                case 2 -> ordenarPedidos();
                case 3 -> embaralharPedidos();
                case 4 -> listarTodosOsPedidos();
                case 0 -> System.out.println("FLW VLW OBG VLT SMP.");
            }
            pausa();
        } while (opcao != 0);       

        teclado.close();    
    }
}