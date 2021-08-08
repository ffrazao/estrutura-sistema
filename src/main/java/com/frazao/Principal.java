package com.frazao;

import java.util.List;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.CollectionUtils;

import com.frazao.escritor.Escritor;
import com.frazao.leitor.Leitor;
import com.frazao.leitor.bd.Esquema;

@SpringBootApplication
public class Principal {

	public static void main(String[] args) throws Exception {
		final Argumentos argumentos = new Argumentos(args);

		final List<Esquema> conteudo = Leitor.preparar(argumentos).ler();
		
		if (!CollectionUtils.isEmpty(conteudo)) {			
			for (final Escritor escritor: Escritor.preparar(argumentos)) {
				escritor.escrever(conteudo);
			}
		}

	}

}
