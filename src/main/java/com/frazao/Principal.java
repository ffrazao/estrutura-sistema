package com.frazao;

import java.util.List;

import com.frazao.escritor.Escritor;
import com.frazao.leitor.Leitor;
import com.frazao.leitor.bd.Esquema;

public class Principal {

	public static void main(String[] args) throws Exception {
		final Argumentos argumentos = new Argumentos(args);

		final List<Esquema> conteudo = Leitor.preparar(argumentos).ler();
		
		for (final Escritor escritor: Escritor.preparar(argumentos)) {
			escritor.escrever(conteudo);
		}
	}

}
