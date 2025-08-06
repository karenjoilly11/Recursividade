import java.util.Scanner;

public class RecursividadeClass {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n============================================");
            System.out.println("\nSelecione o metodo recursivo desejado:\n");
            System.out.println("1. Contagem dos Numeros Pares ate N");
            System.out.println("2. Encontrar Maior Numero do Vetor");
            System.out.println("3. Realizar a Operacao Matematica de Combinacao");
            System.out.println("4. Encontrar a posicao de um numero em um vetor");
            System.out.println("5. Sair");
            System.out.print("\nEscolha uma opcao: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    System.out.println("Bem vindo ao metodo recursivo 1: Contar de numeros pares!");
                    System.out.println("Informe o valor de N: ");
                    int n1 = scanner.nextInt();

                    System.out.println("Quantidade de Numeros Pares ate " + n1 + ": " + metodoRecursivoContarPares(n1));
                    break;
                case 2:

                    System.out.println("Bem vindo ao metodo recursivo 2: Encontrar o maior numero do vetor!");
                    System.out.println("Informe o tamanho do vetor: ");
                    int tamanhoVetor = scanner.nextInt();

                    int[] vetor = new int[tamanhoVetor];
                    System.out.println("Informe os elementos do vetor:");
                    for(int i=0; i<tamanhoVetor; i++){
                        System.out.println("Vetor[" + i + "]: " );
                        vetor[i] = scanner.nextInt();
                    }

                    System.out.println("Maior Numero do Vetor [ " + tamanhoVetor + "]" + metodoRecursivoEncontrarMaiorNumeroVetor(vetor, tamanhoVetor));

                    
                    break;
                case 3:

                    System.out.println("Bem vindo ao metodo recursivo 2: Encontrar o maior numero do vetor!");
                    System.out.println("Informe o valor de N: ");
                    int n3 = scanner.nextInt();
                    System.out.println("Informe o valor de K: ");
                    int k3 = scanner.nextInt();
                   
                    
                    
                    System.out.println("Resultado da Combinacao Simples ( " + n3 + ", " + k3 + "): " + metodoRecursivoCombinacaoSimples(n3, k3));
                    break;
                case 4:
                    System.out.print("Digite o tamanho do vetor: ");
                    int n = scanner.nextInt();
                    int[] vet = new int[n];
                    for (int i = 0; i < n; i++) {
                        System.out.print("Elemento " + (i + 1) + ": ");
                        vet[i] = scanner.nextInt();
                    }
                    System.out.print("Digite o numero a ser encontrado: ");
                    int x = scanner.nextInt();
                    System.out.println("Numero " + x + " -> posicao: " + encontrar(vet, n, x));
                    break;
                case 5:
                    System.out.println("Saindo do programa...");
                    break;
                default:
                    System.out.println("Opcao invalida. Tente novamente.");
            }
        } while (opcao != 5);

        scanner.close();
    }

    public static int metodoRecursivoContarPares(int n1) {

        //verifica se o numero e negativo
        if(n1 < 0){
            return 0;
        }
        
        //caso base
        if (n1%2==0){
            return 1 + metodoRecursivoContarPares(n1-1);
        }
        //regra recursiva
        else {
            return metodoRecursivoContarPares(n1-1);
        }
          

        }

        
    

    public static int metodoRecursivoEncontrarMaiorNumeroVetor(int [] vetor, int tamanhoVetor) {

        
        if(tamanhoVetor <= 0){
            return vetor[0];
        }

        if(tamanhoVetor == 1) {
            return vetor[0];
        }

        int maiorTemporario = metodoRecursivoEncontrarMaiorNumeroVetor(vetor, tamanhoVetor-1);
        
        //Caso base
        if(maiorTemporario > vetor[tamanhoVetor -1]){
            return maiorTemporario;
        }
        

        //Regra recursiva
        else {
            return vetor[tamanhoVetor - 1];
        }
        
        
        
    }

    public static int metodoRecursivoCombinacaoSimples(int n3, int k3) {


        // Caso Base
        if(n3 == 0 || k3 == 0) {
            return 1;
        }
        if(k3 == n3) {
            return 1;
        }
        //Regra recursiva
        else{
            return metodoRecursivoCombinacaoSimples(n3 - 1, k3 - 1) + 
                   metodoRecursivoCombinacaoSimples(n3 - 1, k3);
        }

    }

    public static int encontrar(int[] A, int n, int x){
        int pos = -1;
        if(n>0){
            if(A[n-1] == x){
                pos = n-1;
            } else {
                pos = encontrar(A, n-1, x);
            }
        }
        return pos;
    }

}
