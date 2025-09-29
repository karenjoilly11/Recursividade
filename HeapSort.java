import java.util.Comparator;

public class HeapSort<T> implements IOrdenator<T> {
    
    private long tempoOrdenacao;
    
    @Override
    public T[] ordenar(T[] vetor, Comparator<T> comparador) {
        long inicio = System.currentTimeMillis();
        
        if (vetor == null || vetor.length <= 1) {
            tempoOrdenacao = System.currentTimeMillis() - inicio;
            return vetor;
        }
        
        // Construir heap máximo
        int n = vetor.length;
        
        // Construir heap (reorganizar array)
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(vetor, n, i, comparador);
        }
        
        // Extrair elementos do heap um por um
        for (int i = n - 1; i > 0; i--) {
            // Mover raiz atual para o final
            T temp = vetor[0];
            vetor[0] = vetor[i];
            vetor[i] = temp;
            
            // Chamar heapify na heap reduzida
            heapify(vetor, i, 0, comparador);
        }
        
        tempoOrdenacao = System.currentTimeMillis() - inicio;
        return vetor;
    }
    
    private void heapify(T[] vetor, int n, int i, Comparator<T> comparador) {
        int maior = i; // Inicializar maior como raiz
        int esquerda = 2 * i + 1;
        int direita = 2 * i + 2;
        
        // Se filho esquerdo é maior que a raiz
        if (esquerda < n && comparador.compare(vetor[esquerda], vetor[maior]) > 0) {
            maior = esquerda;
        }
        
        // Se filho direito é maior que o maior até agora
        if (direita < n && comparador.compare(vetor[direita], vetor[maior]) > 0) {
            maior = direita;
        }
        
        // Se o maior não é a raiz
        if (maior != i) {
            T swap = vetor[i];
            vetor[i] = vetor[maior];
            vetor[maior] = swap;
            
            // Recursivamente heapify a subárvore afetada
            heapify(vetor, n, maior, comparador);
        }
    }
    
    @Override
    public long getTempoOrdenacao() {
        return tempoOrdenacao;
    }
}