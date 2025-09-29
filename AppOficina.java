
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/**
 * MIT License
 *
 * Copyright(c) 2022-25 João Caram <caram@pucminas.br>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

public class AppOficina {

    static final int MAX_PEDIDOS = 100;
    static Produto[] produtos;
    static int quantProdutos = 0;
    static String nomeArquivoDados = "produtos.txt";
    static IOrdenador<Produto> ordenador;


    // duas listas ordenadas Tarefa 2
    static Produto[] produtosPorCodigo;
    static Produto[] produtosPorDescricao;

    static Pedido[] pedidos;
    static int quantPedidos = 0;  // ← ESTA LINHA É ESSENCIAL!
    static final String ARQUIVO_PEDIDOS = "pedidos.txt";


    // #region utilidades
    static Scanner teclado;

    

    static <T extends Number> T lerNumero(String mensagem, Class<T> classe) {
        System.out.print(mensagem + ": ");
        T valor;
        try {
            valor = classe.getConstructor(String.class).newInstance(teclado.nextLine());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            return null;
        }
        return valor;
    }

    static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void pausa() {
        System.out.println("Tecle Enter para continuar.");
        teclado.nextLine();
    }

    static void cabecalho() {
        limparTela();
        System.out.println("XULAMBS COMÉRCIO DE COISINHAS v0.2\n================");
    }
    
    //tarefa --
    static int exibirMenuPrincipal() {
        cabecalho();
        System.out.println("1 - Procurar produto");
        System.out.println("2 - Filtrar produtos por preço máximo");
        System.out.println("3 - Ordenar produtos");
        System.out.println("4 - Embaralhar produtos");
        System.out.println("5 - Listar produtos");
        
        System.out.println("6 - Carregar pedidos");
        System.out.println("7 - Buscar pedidos por data");
        
        System.out.println("0 - Finalizar");
       
        return lerNumero("Digite sua opção", Integer.class);
    }

    static int exibirMenuOrdenadores() {
        cabecalho();
        System.out.println("1 - Bolha");
        System.out.println("2 - Inserção");
        System.out.println("3 - Seleção");
        System.out.println("4 - Mergesort");
        System.out.println("0 - Finalizar");
       
        return lerNumero("Digite sua opção", Integer.class);
    }

    static int exibirMenuComparadores() {
        cabecalho();
        System.out.println("1 - Padrão"); //alfabético 
        System.out.println("2 - Por código");
        
        return lerNumero("Digite sua opção", Integer.class);
    }

    // #endregion
    static Produto[] carregarProdutos(String nomeArquivo){
        Scanner dados;
        Produto[] dadosCarregados;
        try{
            dados = new Scanner(new File(nomeArquivo));
            int tamanho = Integer.parseInt(dados.nextLine());
            
            dadosCarregados = new Produto[tamanho];
            while (dados.hasNextLine()) {
                Produto novoProduto = Produto.criarDoTexto(dados.nextLine());
                dadosCarregados[quantProdutos] = novoProduto;
                quantProdutos++;
            }

            dados.close();

            // CRIAR AS CÓPIAS ORDENADAS APÓS CARREGAMENTO
            produtosPorCodigo = Arrays.copyOf(dadosCarregados, quantProdutos);
            produtosPorDescricao = Arrays.copyOf(dadosCarregados, quantProdutos);
        
            // ORDENAR AS CÓPIAS
            Arrays.sort(produtosPorCodigo, (a,b) -> Integer.compare(a.hashCode(), b.hashCode()));

            // Ordenar por descrição (usando toString se não houver getDescricao)
            Arrays.sort(produtosPorDescricao, (a,b) -> a.toString().compareToIgnoreCase(b.toString()));

            
            


            
            
        }catch (FileNotFoundException fex){
        System.out.println("Arquivo não encontrado. Produtos não carregados");
        // CORREÇÃO: Inicializar arrays vazios em caso de erro
        dadosCarregados = new Produto[0];
        produtosPorCodigo = new Produto[0];
        produtosPorDescricao = new Produto[0];
        quantProdutos = 0; // Reset da quantidade
    }
    return dadosCarregados;
}

    //Tarefa 2 carregar pedidos

    /**
     * Lê os dados de pedidos de um arquivo-texto e retorna um vetor com os pedidos carregados
     */
    static Pedido[] carregarPedidos(String nomeArquivo) {
    Scanner dados;
    Pedido[] pedidosCarregados = new Pedido[MAX_PEDIDOS];
    int quantCarregados = 0; // Use uma variável local
    
    try {
        dados = new Scanner(new File(nomeArquivo));
        
        while (dados.hasNextLine() && quantCarregados < MAX_PEDIDOS) {
            String linha = dados.nextLine();
            Pedido novoPedido = Pedido.criarDoTexto(linha, produtos);
            if (novoPedido != null) {
                pedidosCarregados[quantCarregados] = novoPedido;
                quantCarregados++;
            }
        }
        
        dados.close();
        quantPedidos = quantCarregados; // Atualize a variável global aqui
        System.out.println(quantPedidos + " pedidos carregados com sucesso.");
        
    } catch (FileNotFoundException fex) {
        System.out.println("Arquivo de pedidos não encontrado. Nenhum pedido carregado.");
        quantPedidos = 0; // Reset da variável global
        pedidosCarregados = new Pedido[0];
    }
    
    return Arrays.copyOf(pedidosCarregados, quantPedidos);
}

    /**
     * Localiza os pedidos realizados em uma determinada data e imprime seus dados
     */
    static void localizarPedidosPorData(java.time.LocalDate data) {
    cabecalho();
    System.out.println("Pedidos realizados em: " + 
        data.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    System.out.println("====================");
    
    boolean encontrou = false;
    
    for (int i = 0; i < quantPedidos; i++) {
        // ADICIONE ESTA VERIFICAÇÃO DE NULL
        if (pedidos[i] != null && pedidos[i].getDataPedido().equals(data)) {
            System.out.println(pedidos[i].toString());
            System.out.println("--------------------");
            encontrou = true;
        }
    }
    
    if (!encontrou) {
        System.out.println("Nenhum pedido encontrado para esta data.");
    }
}

    /**
     * Método auxiliar para ler uma data do usuário
     */
    static java.time.LocalDate lerData(String mensagem) {
        System.out.print(mensagem + " (dd/mm/aaaa): ");
        String dataStr = teclado.nextLine();
        try {
            return java.time.LocalDate.parse(dataStr, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            System.out.println("Data inválida. Usando data atual.");
            return java.time.LocalDate.now();
        }
    }


    // Tarefa 2 - busca por codigo
    static Produto buscarPorCodigoBinario(int codigo) {
    int inicio = 0;
    int fim = quantProdutos - 1;
    
    while (inicio <= fim) {
        int meio = (inicio + fim) / 2;
        int codigoMeio = produtosPorCodigo[meio].hashCode();
        
        if (codigoMeio == codigo) {
            return produtosPorCodigo[meio];
        } else if (codigoMeio < codigo) {
            inicio = meio + 1;
        } else {
            fim = meio - 1;
        }
    }
    return null; // Não encontrado
}

    // Tarefa 2 - busca por descricao
    
    static Produto buscarPorDescricaoBinaria(String descricao) {
    int inicio = 0;
    int fim = quantProdutos - 1;
    
    while (inicio <= fim) {
        int meio = (inicio + fim) / 2;
        int comparacao = produtosPorDescricao[meio].toString().compareToIgnoreCase(descricao);
        
        if (comparacao == 0) {
            return produtosPorDescricao[meio];
        } else if (comparacao < 0) {
            inicio = meio + 1;
        } else {
            fim = meio - 1;
        }
    }
    return null;
}


    


    static Produto localizarProduto() {
        cabecalho();
        System.out.println("Localizando um produto");
        int numero = lerNumero("Digite o identificador do produto", Integer.class);
        Produto localizado = null;
        
        
        /**for (int i = 0; i < quantProdutos && localizado == null; i++) {
            if (produtos[i].hashCode() == numero)
                localizado = produtos[i];
        }
        return localizado;**/

        // USAR BUSCA BINÁRIA EM VEZ DE BUSCA LINEAR
        localizado = buscarPorCodigoBinario(numero);
    
    return localizado;
    }

    private static void mostrarProduto(Produto produto) {
        cabecalho();
        String mensagem = "Dados inválidos";
        
        if(produto!=null){
            mensagem = String.format("Dados do produto:\n%s", produto);            
        }
        
        System.out.println(mensagem);
    }

    private static void filtrarPorPrecoMaximo(){
    cabecalho();
    System.out.println("Filtrando por valor máximo:");
    double valor = lerNumero("valor", Double.class);
    StringBuilder relatorio = new StringBuilder();
    // CORREÇÃO: Verificar se produtos não é null
    if (produtos != null) {
        for (int i = 0; i < quantProdutos; i++) {
            if(produtos[i].valorDeVenda() < valor)
            relatorio.append(produtos[i]+"\n");
        }
    }
    System.out.println(relatorio.toString());
}

    static void ordenarProdutos(){
        cabecalho();
        
        int opcao = exibirMenuOrdenadores();
        switch (opcao) {
            case 1 -> ordenador = new Bubblesort<>();
            case 2 -> ordenador = new InsertSort<>();
            case 3 -> ordenador = new SelectionSort<>();
            case 4 -> ordenador = new Mergesort<>();
        }

        if(ordenador!=null){
        opcao = exibirMenuComparadores();
        switch (opcao) {
            case 1 -> {
                // Ordenar por descrição (usando toString)
                produtos = ordenador.ordenar(produtos, (a,b) -> a.toString().compareToIgnoreCase(b.toString()));
                produtosPorDescricao = Arrays.copyOf(produtos, quantProdutos);
            }
            case 2 -> {
                // Ordenar por código
                produtos = ordenador.ordenar(produtos, (a,b) -> Integer.compare(a.hashCode(), b.hashCode()));
                produtosPorCodigo = Arrays.copyOf(produtos, quantProdutos);
            }
            default -> produtos = ordenador.ordenar(produtos);
        }
        
        System.out.println("Tempo gasto: "+ordenador.getTempoOrdenacao()+" ms.");
    }
        ordenador = null;
    }

    

    

    

    static void embaralharProdutos(){
    // CORREÇÃO: Verificar se produtos não é null e tem elementos
    if (produtos != null && quantProdutos > 0) {
        // CORREÇÃO: Usar subList para evitar problemas com arrays parcialmente preenchidos
        Collections.shuffle(Arrays.asList(produtos).subList(0, quantProdutos));

        // Manter as cópias ordenadas
        produtosPorCodigo = Arrays.copyOf(produtos, quantProdutos);
        produtosPorDescricao = Arrays.copyOf(produtos, quantProdutos);
        
        // Reordenar as cópias
        Arrays.sort(produtosPorCodigo, (a,b) -> Integer.compare(a.hashCode(), b.hashCode()));
        Arrays.sort(produtosPorDescricao, (a,b) -> a.toString().compareToIgnoreCase(b.toString()));
    } else {
        System.out.println("Nenhum produto carregado para embaralhar.");
    }
}





    static void verificarSubstituicao(Produto[] dadosOriginais, Produto[] copiaDados){
        cabecalho();
        System.out.print("Deseja sobrescrever os dados originais pelos ordenados (S/N)?");
        String resposta = teclado.nextLine().toUpperCase();
        if(resposta.equals("S"))
            dadosOriginais = Arrays.copyOf(copiaDados, copiaDados.length);
    }

    static void listarProdutos(){
    cabecalho();
    // CORREÇÃO: Verificar se produtos não é null
    if (produtos != null) {
        for (int i = 0; i < quantProdutos; i++) {
            System.out.println(produtos[i]);
        }
    } else {
        System.out.println("Nenhum produto carregado.");
    }
}

    public static void main(String[] args) {
        teclado = new Scanner(System.in);
        
        
        produtos = carregarProdutos(nomeArquivoDados);
        
        // === INICIALIZAR PEDIDOS ===
    pedidos = new Pedido[MAX_PEDIDOS];
    quantPedidos = 0;
    // ===========================
    
    
    // CORREÇÃO: Só embaralhar se produtos foram carregados
    if (produtos != null && quantProdutos > 0) {
        embaralharProdutos();
    } else {
        System.out.println("Nenhum produto carregado. Continuando sem produtos...");
    }
        int opcao = -1;
        
        do {
            opcao = exibirMenuPrincipal();
            switch (opcao) {
                case 1 -> mostrarProduto(localizarProduto());
                case 2 -> filtrarPorPrecoMaximo();
                case 3 -> ordenarProdutos();
                case 4 -> embaralharProdutos();
                case 5 -> listarProdutos();
                // === ADICIONE ESTES NOVOS CASOS ===
                case 6 -> {
                    pedidos = carregarPedidos(ARQUIVO_PEDIDOS);
                }
                case 7 -> {
                    java.time.LocalDate data = lerData("Digite a data para buscar pedidos");
                    localizarPedidosPorData(data);
                }
                // ==================================
                case 0 -> System.out.println("FLW VLW OBG VLT SMP.");
            }
            pausa();
        } while (opcao != 0);
        teclado.close();
    }                        
}


