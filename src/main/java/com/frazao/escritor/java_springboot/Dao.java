package com.frazao.escritor.java_springboot;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.frazao.Argumentos;
import com.frazao.bd.Esquema;

public class Dao  extends EstruturaBasica {

	public Dao(Argumentos argumentos, List<Esquema> conteudo, Map<String, File> diretorios) {
		super(argumentos, conteudo, diretorios);
	}

	@Override
	public void escrever(EscritorJavaSpringBoot escritor) throws Exception {

	}
}