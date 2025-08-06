import java.util.Scanner;

public class RecursividadeFatorial{



        public static void main(String[] args){

            Scanner scanner = new Scanner(System.in);

            int continuar = 1;
            while(continuar == 1){
                System.out.println("\n============================================");

                System.out.println("Bem vindo ao metodo recursivo: Fatorial!");
                System.out.println("Informe o valor de N: ");
                int n = scanner.nextInt();

                System.out.println("O Fatorial de " + n + ": " + Fatorial(n));

                System.out.println("Deseja continuar? (1 - Sim, 0 - Nao)");
                continuar = scanner.nextInt();
            }
            


            scanner.close();
        }

        public static int Fatorial(int n){

            //caso base
            if(n == 0){
                return 1;
            }

            //regra recursiva
            else{
                return n * Fatorial(n-1);
            }
        }

}