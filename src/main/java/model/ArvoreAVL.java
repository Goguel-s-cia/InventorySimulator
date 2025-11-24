package model;

import java.io.FileWriter;
import java.io.IOException;

public class ArvoreAVL {

    private NoAVL raiz;

    public ArvoreAVL() {
        this.raiz = null;
    }

    // Retorna a altura de um nó ou 0 se for nulo
    private int obterAltura(NoAVL no) {
        if (no == null) {
            return 0;
        }
        return no.getAltura();
    }

    // Recalcula a altura do nó com base nos filhos
    private void atualizarAltura(NoAVL no) {
        if (no != null) {
            no.setAltura(1 + Math.max(obterAltura(no.getEsquerdo()), obterAltura(no.getDireito())));
        }
    }
    
    // Calcula a diferença de altura entre esquerda e direita
    private int obterFatorBalanceamento(NoAVL no) {
        if (no == null) {
            return 0;
        }
        return obterAltura(no.getEsquerdo()) - obterAltura(no.getDireito());
    }
    
    // Executa rotação simples à direita
    private NoAVL rotacaoDireita(NoAVL y) {
        NoAVL x = y.getEsquerdo();
        NoAVL t2 = x.getDireito();

        // Rotaciona
        x.setDireito(y);
        y.setEsquerdo(t2);

        // Atualiza alturas (filho primeiro, depois pai)
        atualizarAltura(y);
        atualizarAltura(x);

        return x;
    }

    // Executa rotação simples à esquerda
    private NoAVL rotacaoEsquerda(NoAVL x) {
        NoAVL y = x.getDireito();
        NoAVL t2 = y.getEsquerdo();

        // Rotaciona
        y.setEsquerdo(x);
        x.setDireito(t2);

        // Atualiza alturas
        atualizarAltura(x);
        atualizarAltura(y);

        return y;
    }

    // Verifica o fator de balanceamento e aplica as rotações necessárias
    private NoAVL balancear(NoAVL no) {
        if (no == null) return null;

        atualizarAltura(no);
        int fb = obterFatorBalanceamento(no);

        // Desequilíbrio à esquerda (Left Heavy)
        if (fb > 1) {
            // Rotação dupla: Esquerda-Direita
            if (obterFatorBalanceamento(no.getEsquerdo()) < 0) {
                no.setEsquerdo(rotacaoEsquerda(no.getEsquerdo()));
            }
            return rotacaoDireita(no);
        }

        // Desequilíbrio à direita (Right Heavy)
        if (fb < -1) {
            // Rotação dupla: Direita-Esquerda
            if (obterFatorBalanceamento(no.getDireito()) > 0) {
                no.setDireito(rotacaoDireita(no.getDireito()));
            }
            return rotacaoEsquerda(no);
        }

        return no;
    }
    
    // Insere um novo produto na árvore
    public void inserir(Eletrodomestico produto) {
        this.raiz = inserirRecursivo(this.raiz, produto);
    }

    private NoAVL inserirRecursivo(NoAVL noAtual, Eletrodomestico produto) {
        // Base: encontrou posição de inserção
        if (noAtual == null) {
            return new NoAVL(produto);
        }

        // Recursão para esquerda ou direita
        if (produto.getCodigo() < noAtual.getDado().getCodigo()) {
            noAtual.setEsquerdo(inserirRecursivo(noAtual.getEsquerdo(), produto));
        } else if (produto.getCodigo() > noAtual.getDado().getCodigo()) {
            noAtual.setDireito(inserirRecursivo(noAtual.getDireito(), produto));
        } else {
            // Ignora duplicatas
            return noAtual;
        }

        // Rebalanceia o nó no retorno da recursão
        return balancear(noAtual);
    }

    // Busca um produto pelo código
    public Eletrodomestico buscar(int codigo) {
        NoAVL noEncontrado = buscarRecursivo(this.raiz, codigo);
        if (noEncontrado != null) {
            return noEncontrado.getDado();
        }
        return null;
    }

    private NoAVL buscarRecursivo(NoAVL noAtual, int codigo) {
        // Base: não encontrado
        if (noAtual == null) {
            return null;
        }

        // Base: encontrado
        if (codigo == noAtual.getDado().getCodigo()) {
            return noAtual;
        }

        // Navegação
        if (codigo < noAtual.getDado().getCodigo()) {
            return buscarRecursivo(noAtual.getEsquerdo(), codigo);
        } else {
            return buscarRecursivo(noAtual.getDireito(), codigo);
        }
    }
    
    // Exibe os produtos ordenados (In-Order)
    public void listarEmOrdem() {
        System.out.println("--- Listando Produtos em Ordem ---");
        listarEmOrdemRecursivo(this.raiz);
        System.out.println("----------------------------------");
    }

    private void listarEmOrdemRecursivo(NoAVL noAtual) {
        if (noAtual != null) {
            listarEmOrdemRecursivo(noAtual.getEsquerdo());
            System.out.println(noAtual.getDado().toString());
            listarEmOrdemRecursivo(noAtual.getDireito());
        }
    }
    
    // Gera arquivo .dot para visualização no GraphViz
    public void gerarArquivoDOT(String nomeArquivo) {
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            writer.write("digraph AVLTree {\n");
            writer.write("  node [shape=box, style=filled, color=\"lightblue\"];\n");

            if (this.raiz == null) {
                writer.write("  null [shape=plaintext];\n");
            } else {
                gerarNosERelacoesDOT(this.raiz, writer);
            }

            writer.write("}\n");
        } catch (IOException e) {
            System.err.println("Erro ao gerar arquivo DOT: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void gerarNosERelacoesDOT(NoAVL noAtual, FileWriter writer) throws IOException {
        if (noAtual == null) return;

        // Formata o rótulo do nó com código, nome, estoque e altura
        String label = String.format("\"[%d] %s\\nEst: %d | Alt: %d\"",
                noAtual.getDado().getCodigo(),
                noAtual.getDado().getNome(),
                noAtual.getDado().getQuantidadeEstoque(),
                noAtual.getAltura()
        );
        writer.write("  " + noAtual.getDado().getCodigo() + " [label=" + label + "];\n");

        // Define relações com filho esquerdo
        if (noAtual.getEsquerdo() != null) {
            writer.write("  " + noAtual.getDado().getCodigo() + " -> " + noAtual.getEsquerdo().getDado().getCodigo() + ";\n");
            gerarNosERelacoesDOT(noAtual.getEsquerdo(), writer);
        } else {
            writer.write("  null_esq_" + noAtual.getDado().getCodigo() + " [shape=plaintext, label=\"null\"];\n");
            writer.write("  " + noAtual.getDado().getCodigo() + " -> null_esq_" + noAtual.getDado().getCodigo() + ";\n");
        }

        // Define relações com filho direito
        if (noAtual.getDireito() != null) {
            writer.write("  " + noAtual.getDado().getCodigo() + " -> " + noAtual.getDireito().getDado().getCodigo() + ";\n");
            gerarNosERelacoesDOT(noAtual.getDireito(), writer);
        } else {
            writer.write("  null_dir_" + noAtual.getDado().getCodigo() + " [shape=plaintext, label=\"null\"];\n");
            writer.write("  " + noAtual.getDado().getCodigo() + " -> null_dir_" + noAtual.getDado().getCodigo() + ";\n");
        }
    }
    
    // Remove um produto pelo código e rebalanceia a árvore
    public void remover(int codigo) {
        this.raiz = removerRecursivo(this.raiz, codigo);
    }

    private NoAVL removerRecursivo(NoAVL noAtual, int codigo) {
        if (noAtual == null) return null;

        // Navegação para encontrar o nó
        if (codigo < noAtual.getDado().getCodigo()) {
            noAtual.setEsquerdo(removerRecursivo(noAtual.getEsquerdo(), codigo));
        } else if (codigo > noAtual.getDado().getCodigo()) {
            noAtual.setDireito(removerRecursivo(noAtual.getDireito(), codigo));
        } else {
            // Nó encontrado
        	// Casos: folha ou apenas um filho
            if ((noAtual.getEsquerdo() == null) || (noAtual.getDireito() == null)) {
                NoAVL temp = null;
                if (temp == noAtual.getEsquerdo()) {
                    temp = noAtual.getDireito();
                } else {
                    temp = noAtual.getEsquerdo();
                }

                if (temp == null) {
                    temp = noAtual;
                    noAtual = null;
                } else {
                    noAtual = temp; // Copia o filho existente
                }
            } else {
                // Caso: dois filhos. Substitui pelo sucessor (menor da direita)
                NoAVL temp = encontrarMenor(noAtual.getDireito());
                noAtual.setDado(temp.getDado());
                noAtual.setDireito(removerRecursivo(noAtual.getDireito(), temp.getDado().getCodigo()));
            }
        }

        if (noAtual == null) return null;

        // Passo essencial: atualiza altura e rebalanceia na volta da recursão
        return balancear(noAtual);
    }

    // Encontra o nó mais à esquerda (menor valor)
    private NoAVL encontrarMenor(NoAVL no) {
        NoAVL atual = no;
        while (atual.getEsquerdo() != null) {
            atual = atual.getEsquerdo();
        }
        return atual;
    }
    
    public boolean estaVazia() {
        return this.raiz == null;
    }

    // Conta o total de nós na árvore
    public int contarNos() {
        return contarNosRecursivo(this.raiz);
    }

    private int contarNosRecursivo(NoAVL no) {
        if (no == null) return 0;
        return 1 + contarNosRecursivo(no.getEsquerdo()) + contarNosRecursivo(no.getDireito());
    }
}