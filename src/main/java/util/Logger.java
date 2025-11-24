package util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
	private static final String CAMINHO_LOG = "log_avl.txt";
    private static PrintWriter arquivoLog;

    // Inicializa o arquivo de log (limpa o anterior se existir)
    public static void iniciar() {
        try {
            // O 'false' no FileWriter faz ele sobrescrever o arquivo a cada nova execução
            arquivoLog = new PrintWriter(new FileWriter(CAMINHO_LOG, false), true);
        } catch (IOException e) {
            System.err.println("Erro ao criar arquivo de log: " + e.getMessage());
        }
    }

    // O método principal que todos vão chamar
    public static void log(String mensagem) {
        // Exibe na tela
        System.out.println(mensagem);

        // Escreve no arquivo
        if (arquivoLog != null) {
            String hora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            arquivoLog.println("[" + hora + "] " + mensagem);
        }
    }

    // Fecha o arquivo corretamente ao final do programa
    public static void fechar() {
        if (arquivoLog != null) {
            arquivoLog.close();
        }
    }
}
