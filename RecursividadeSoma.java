import java.util.Scanner;
public class RecursividadeSoma {
    
    public static void main(String[] args){

        //Scanner para entrada de dados
        Scanner scanner = new Scanner(System.in);

        int continuar = 1;
        while(continuar == 1){

            System.out.println("\n============================================");
            System.out.println("Bem vindo ao metodo recursivo: Soma de Numeros!");

            
            System.out.println("Escolha uma opcao: ");
            System.out.println("1. Calcular a soma dos numeros de 1 ate N");
            System.out.println("2. Calcular dois numeros inteiros");
            int opcao = scanner.nextInt();

            if(opcao == 1) {
                System.out.println("Informe o valor de N: ");
                int n = scanner.nextInt();
                System.out.println("A soma dos numeros de 1 ate " + n + ": " + Soma(n));
            }
            if(opcao == 2) {
                System.out.println("Informe o primeiro numero: ");
                int num1 = scanner.nextInt();
                System.out.println("Informe o segundo numero: ");
                int num2 = scanner.nextInt();
                System.out.println("A soma de " + num1 + " e " + num2 + ": " + Somatorio(num1, num2));
            }

            
            

            System.out.println("Deseja continuar? (1 - Sim, 0 - Nao)");
            continuar = scanner.nextInt();
    }
        //Fechar o scanner
        scanner.close();

}
    public static int Soma(int n){

        //caso base

        if(n == 0){
            return 0;
        }

        //regra recursiva
        else {
            return n + Soma(n-1);
        }
    }

    public static int Somatorio(int num1, int num2){

        //caso base
        if(num2 == 0){
            return num1;
        }

        //regra recursiva
        
        else{
            return Somatorio(num1+1, num2-1);
        }
    }

}

