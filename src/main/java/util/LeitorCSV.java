package util;

import model.ArvoreAVL;
import model.Eletrodomestico;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class LeitorCSV {
	public void carregarArquivo(String caminho, ArvoreAVL arvore, List<Integer> listaCodigos) {
        Logger.log("--- Iniciando carregamento do catálogo: " + caminho + " ---");

        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha = br.readLine(); // Lê a primeira linha e ignora
            
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");

                // Verifica se a linha está completa para evitar erros
                if (dados.length >= 4) {
                    try {
                        int codigo = Integer.parseInt(dados[0].trim());
                        String nome = dados[1].trim();
                        int estoque = Integer.parseInt(dados[2].trim());
                        double preco = Double.parseDouble(dados[3].trim().replace(",", ".")); // Garante ponto decimal

                        if (estoque > 0) {
                            Eletrodomestico produto = new Eletrodomestico(codigo, nome, estoque, preco);
                            
                            // Insere na Árvore (ela já balanceia automaticamente)
                            arvore.inserir(produto);
                            
                            // Adiciona na lista para o sorteio
                            listaCodigos.add(codigo);
                            
                            Logger.log("Inserindo produto " + codigo + ": " + nome);
                        }
                    } catch (NumberFormatException e) {
                        Logger.log("Erro ao formatar linha: " + linha);
                    }
                }
            }
            Logger.log("Carregamento concluído. Total na AVL: " + arvore.contarNos());
            Logger.log("--------------------------------------------------");

        } catch (IOException e) {
            Logger.log("Erro crítico ao ler arquivo: " + e.getMessage());
        }
    }
}
