package com.frazao.escritor.java_springboot;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.frazao.Argumentos;
import com.frazao.bd.Esquema;

public abstract class EstruturaBasica {

	protected List<Esquema> conteudo;

	protected Argumentos argumentos;

	protected Map<String, File> diretorios;

	public EstruturaBasica(Argumentos argumentos, List<Esquema> conteudo, Map<String, File> diretorios) {
		super();
		this.conteudo = conteudo;
		this.argumentos = argumentos;
		this.diretorios = diretorios;
	}
	
	public abstract void escrever(EscritorJavaSpringBoot escritor) throws Exception;

}
