package model;

public class NoAVL {

	private Eletrodomestico dado;
	private NoAVL esquerdo;
	private NoAVL direito;
	private int altura;

	public NoAVL(Eletrodomestico dado) {
        this.dado = dado;
        this.esquerdo = null;
        this.direito = null;
        this.altura = 1; 
    }

	public Eletrodomestico getDado() {
		return dado;
	}

	public void setDado(Eletrodomestico dado) {
		this.dado = dado;
	}

	public NoAVL getEsquerdo() {
		return esquerdo;
	}

	public void setEsquerdo(NoAVL esquerdo) {
		this.esquerdo = esquerdo;
	}

	public NoAVL getDireito() {
		return direito;
	}

	public void setDireito(NoAVL direito) {
		this.direito = direito;
	}

	public int getAltura() {
		return altura;
	}

	public void setAltura(int altura) {
		this.altura = altura;
	}
}