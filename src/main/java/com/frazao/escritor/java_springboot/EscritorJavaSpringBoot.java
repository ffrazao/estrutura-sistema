package com.frazao.escritor.java_springboot;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frazao.Argumentos;
import com.frazao.bd.Esquema;
import com.frazao.escritor.Escritor;

public class EscritorJavaSpringBoot implements Escritor {

	private Argumentos argumentos;

	private Dominio dominio;

	private Entidade entidade;

	private Filtro filtro;

	private Dao dao;

	private Bo bo;

	private Rest rest;

	public EscritorJavaSpringBoot(Argumentos argumentos) throws Exception {
		this.argumentos = argumentos;
	}

	@Override
	public void escrever(List<Esquema> conteudo) throws Exception {

		Map<String, File> diretorios = criarDiretoriosPadrao();

		dominio = new Dominio(this.argumentos, conteudo, diretorios);
		entidade = new Entidade(this.argumentos, conteudo, diretorios);
		filtro = new Filtro(this.argumentos, conteudo, diretorios);
		dao = new Dao(this.argumentos, conteudo, diretorios);
		bo = new Bo(this.argumentos, conteudo, diretorios);
		rest = new Rest(this.argumentos, conteudo, diretorios);

		dominio.escrever(this);
		entidade.escrever(this);
		filtro.escrever(this);
		dao.escrever(this);
		bo.escrever(this);
		rest.escrever(this);

	}

	private Map<String, File> criarDiretoriosPadrao() {
		Map<String, File> result = new HashMap<>();

		File raiz = this.argumentos.diretorioLocal;
		if (raiz.exists()) {
			if (!raiz.isDirectory()) {
				throw new IllegalArgumentException("Diretório de saída inválido " + raiz.getAbsolutePath());
			}
		} else {
			raiz.mkdirs();
		}

		raiz = new File(raiz, "src");
		raiz.mkdirs();

		raiz = new File(raiz, "main");
		raiz.mkdirs();

		File resources = new File(raiz, "resources");
		resources.mkdirs();
		result.put("resources", resources);

		raiz = new File(raiz, "java");
		raiz.mkdirs();
		result.put("java", raiz);

		String[] pacotes = this.argumentos.pacoteRaiz.split("\\.");
		for (String pacote : pacotes) {
			raiz = new File(raiz, pacote);
			raiz.mkdirs();
		}

		result.put("raiz", raiz);

		File modelo = new File(raiz, "modelo");
		modelo.mkdirs();
		result.put("modelo", modelo);

		File dominio = new File(modelo, "dominio");
		dominio.mkdirs();
		result.put("dominio", dominio);

		File entidade = new File(modelo, "entidade");
		entidade.mkdirs();
		result.put("entidade", entidade);

		File dto = new File(modelo, "dto");
		dto.mkdirs();
		result.put("dto", dto);

		File dao = new File(raiz, "dao");
		dao.mkdirs();
		result.put("dao", dao);

		File bo = new File(raiz, "bo");
		bo.mkdirs();
		result.put("bo", bo);

		File rest = new File(raiz, "rest");
		rest.mkdirs();
		result.put("rest", rest);

		System.out.println("Preparando para escrever no diretorio " + raiz.getAbsolutePath());
		return result;
	}

}
