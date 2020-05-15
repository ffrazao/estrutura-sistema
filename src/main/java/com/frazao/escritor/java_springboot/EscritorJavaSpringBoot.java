package com.frazao.escritor.java_springboot;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frazao.Argumentos;
import com.frazao.escritor.Escritor;
import com.frazao.escritor.java_springboot.estrutura.Bo;
import com.frazao.escritor.java_springboot.estrutura.Dao;
import com.frazao.escritor.java_springboot.estrutura.Dominio;
import com.frazao.escritor.java_springboot.estrutura.Entidade;
import com.frazao.escritor.java_springboot.estrutura.Filtro;
import com.frazao.escritor.java_springboot.estrutura.Rest;
import com.frazao.leitor.bd.Esquema;

public class EscritorJavaSpringBoot implements Escritor {

	public Argumentos argumentos;

	public Bo bo;

	public List<Esquema> conteudo;

	public Dao dao;

	public Map<String, File> diretorios;

	public Dominio dominio;

	public Entidade entidade;

	public Filtro filtro;

	public Rest rest;

	public EscritorJavaSpringBoot(Argumentos argumentos) throws Exception {
		this.argumentos = argumentos;
	}

	private void copiarArquivosEstaticos(List<Esquema> conteudo, Map<String, File> diretorios) {
		// TODO Auto-generated method stub

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

	@Override
	public void escrever(List<Esquema> conteudo) throws Exception {

		this.conteudo = conteudo;

		this.diretorios = criarDiretoriosPadrao();

		copiarArquivosEstaticos(conteudo, diretorios);

		dominio = new Dominio(this);
		entidade = new Entidade(this);
		filtro = new Filtro(this);
		dao = new Dao(this);
		bo = new Bo(this);
		rest = new Rest(this);

		dominio.escrever();
		entidade.escrever();
		filtro.escrever();
		dao.escrever();
		bo.escrever();
		rest.escrever();

	}

}
