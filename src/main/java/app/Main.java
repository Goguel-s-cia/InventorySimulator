package app;

import model.ArvoreAVL;
import model.Eletrodomestico;
import util.LeitorCSV;
import util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        // Inicializa sistema de log e estruturas de dados
        Logger.iniciar();
        ArvoreAVL arvore = new ArvoreAVL();
        List<Integer> codigosParaSorteio = new ArrayList<>(); // Lista auxiliar apenas para o sorteio 
        LeitorCSV leitor = new LeitorCSV();
        Random random = new Random();

        // Carrega o catálogo inicial do arquivo CSV
        leitor.carregarArquivo("catalogo.csv", arvore, codigosParaSorteio); 

        // Gera o estado inicial da árvore para visualização no GraphViz
        arvore.gerarArquivoDOT("inicio_simulacao.dot");

        Logger.log("\n=== INICIANDO SIMULAÇÃO DE CONSUMO AUTOMÁTICO ===");

        int ciclo = 1;
        // Executa a simulação até que todos os produtos sejam consumidos e a árvore fique vazia 
        while (!arvore.estaVazia()) {
            try {
                Thread.sleep(200); // Pausa para permitir leitura do log em tempo real
            } catch (InterruptedException e) { e.printStackTrace(); }

            // Garante que a lista não esteja vazia antes de sortear
            if (codigosParaSorteio.isEmpty()) break;
            
            // 1. Sorteio: escolhe um código aleatório da lista auxiliar
            int indexSorteado = random.nextInt(codigosParaSorteio.size());
            int codigoSorteado = codigosParaSorteio.get(indexSorteado);

            Logger.log("\n[Ciclo " + ciclo + "] Sorteado código: " + codigoSorteado);

            // 2. Busca: verifica se o produto ainda existe na árvore
            Eletrodomestico produto = arvore.buscar(codigoSorteado);

            if (produto == null) {
                // Caso: Produto sorteado já foi removido anteriormente (estoque esgotado)
                Logger.log("Alerta: Produto " + codigoSorteado + " fora de estoque.");
            } else {
                // 3. Ações: Simula o consumo reduzindo o estoque
                produto.reduzirEstoque(1);

                if (produto.getQuantidadeEstoque() > 0) {
                    // Consumo parcial: apenas atualiza a quantidade, mantém na árvore
                    Logger.log("Venda realizada: " + produto.getNome() + ". Estoque restante: " + produto.getQuantidadeEstoque());
                } else {
                    // Consumo total: remove o nó da árvore e rebalanceia automaticamente 
                    Logger.log("ESTOQUE ZERADO! Removendo produto " + codigoSorteado + " (" + produto.getNome() + ").");
                    arvore.remover(codigoSorteado);
                    
                    // Opcional: gera visualização da árvore após a remoção para debug
                    // arvore.gerarArquivoDOT("passo_" + ciclo + ".dot");
                    
                    Logger.log("Produto removido. Total de itens na árvore: " + arvore.contarNos());
                }
            }
            ciclo++;
        }

        // Encerramento da simulação 
        Logger.log("\n============================================================");
        Logger.log("Simulação encerrada: árvore AVL vazia. Todos os produtos foram consumidos.");
        Logger.fechar();
    }
}