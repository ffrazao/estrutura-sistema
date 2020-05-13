package com.frazao.escritor;

import java.util.ArrayList;
import java.util.List;

import com.frazao.Argumentos;
import com.frazao.bd.Esquema;
import com.frazao.escritor.java_springboot.EscritorJavaSpringBoot;

public interface Escritor {

	public static List<Escritor> preparar(Argumentos argumentos) throws Exception {
		List<Escritor> result = new ArrayList<>();
		for (EscritorModelo modelo : argumentos.escritorModelo) {
			switch (modelo) {
			case JAVA_SPRINGBOOT:
				result.add(new EscritorJavaSpringBoot(argumentos));
			}
		}

		return result;
	}

	public void escrever(List<Esquema> conteudo) throws Exception;

}
