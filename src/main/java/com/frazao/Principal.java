package com.frazao;

import java.util.List;

import com.frazao.bd.Esquema;
import com.frazao.escritor.Escritor;
import com.frazao.leitor.Leitor;

public class Principal {

	public static void main(String[] args) throws Exception {
		Argumentos argumentos = new Argumentos(args);

		List<Esquema> conteudo = Leitor.preparar(argumentos).ler();
				
		for (Escritor escritor: Escritor.preparar(argumentos)) {
			escritor.escrever(conteudo);
		}
	}

}
