import java.util.Comparator;

public class ComparadorPorDataPedido implements Comparator<Pedido> {
    @Override
    public int compare(Pedido p1, Pedido p2) {
        int comparacaoData = p1.getDataPedido().compareTo(p2.getDataPedido());
        if (comparacaoData == 0) {
            // Desempate por código
            return Integer.compare(p1.getIdPedido(), p2.getIdPedido());
        }
        return comparacaoData;
    }
}