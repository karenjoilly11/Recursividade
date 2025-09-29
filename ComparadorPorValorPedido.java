import java.util.Comparator;

public class ComparadorPorValorPedido implements Comparator<Pedido> {
    @Override
    public int compare(Pedido p1, Pedido p2) {
        int comparacaoValor = Double.compare(p1.calcularTotal(), p2.calcularTotal());
        if (comparacaoValor == 0) {
            // Primeiro desempate: quantidade de produtos
            int comparacaoQuantidade = Integer.compare(p1.getQuantidadeItens(), p2.getQuantidadeItens());
            if (comparacaoQuantidade == 0) {
                // Segundo desempate: código
                return Integer.compare(p1.getIdPedido(), p2.getIdPedido());
            }
            return comparacaoQuantidade;
        }
        return comparacaoValor;
    }
}