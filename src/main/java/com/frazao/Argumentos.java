package com.frazao;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.frazao.escritor.EscritorModelo;
import com.frazao.leitor.LeitorModelo;

public class Argumentos {
	
	private final static String DIRETORIO_LOCAL_PADRAO = "../saida";

	private static final String PACOTE_LOCAL_PADRAO = "com.frazao";

	public List<EscritorModelo> escritorModelo;

	public List<String> esquema;

	public LeitorModelo leitorModelo;

	public String senha;

	public List<String> tabela;

	public String url;

	public String usuario;
	
	public File diretorioLocal;
	
	public String pacoteRaiz;

	public Argumentos(String[] args) throws Exception {

		CommandLine cmd = getCmd(args);

		this.leitorModelo = LeitorModelo
				.valueOf(cmd.getOptionValue("modeloLeitor", LeitorModelo.getPadrao().toString()).toUpperCase());
		this.url = cmd.getOptionValue("url", LeitorModelo.getPadrao().getUrlBasica());
		this.usuario = cmd.getOptionValue("usuario", this.usuario);
		this.senha = cmd.getOptionValue("senha", this.senha);
		this.esquema = new ArrayList<>(Arrays
				.asList(ArrayUtils.addAll(this.leitorModelo.getEsquemasAIgnorar(), cmd.getOptionValues("esquema"))));
		this.tabela = new ArrayList<>(Arrays.asList(ArrayUtils.addAll(new String[] {}, cmd.getOptionValues("tabela"))));
		this.escritorModelo = Arrays
				.asList(ObjectUtils.defaultIfNull(cmd.getOptionValues("escritorModelo"),
						new String[] { EscritorModelo.getPadrao().toString() }))
				.stream().map(s -> EscritorModelo.valueOf(s.toUpperCase())).collect(Collectors.toList());
		this.diretorioLocal = new File(cmd.getOptionValue("diretorioLocal", DIRETORIO_LOCAL_PADRAO));
		this.pacoteRaiz = cmd.getOptionValue("pacoteRaiz", PACOTE_LOCAL_PADRAO);

	}

	private CommandLine getCmd(String[] args) throws Exception {

		Options options = new Options();

		Option leitorModelo = new Option("lm", "leitorModelo", true, "Modelo de banco de dados a ser lido");
		leitorModelo.setRequired(false);
		options.addOption(leitorModelo);

		Option url = new Option(null, "url", true, "Url de conexão ao banco de dados ex: \"mysql://localhost/\"");
		url.setRequired(false);
		options.addOption(url);

		Option usuario = new Option("u", "usuario", true, "Usuário do banco de dados");
		usuario.setRequired(false);
		options.addOption(usuario);

		Option senha = new Option("s", "senha", true, "Senha do usuário do banco de dados");
		senha.setRequired(false);
		options.addOption(senha);

		Option esquema = new Option("e", "esquema", true, "Lista de esquemas do banco de dados a ser utilizado");
		esquema.setRequired(false);
		esquema.setType(String[].class);
		esquema.setArgs(Option.UNLIMITED_VALUES);
		esquema.setValueSeparator(',');
		options.addOption(esquema);

		Option tabela = new Option("t", "tabela", true, "Tabela do esquema do banco de dados a ser utilizado");
		tabela.setRequired(false);
		tabela.setType(String[].class);
		tabela.setArgs(Option.UNLIMITED_VALUES);
		tabela.setValueSeparator(',');
		options.addOption(tabela);

		Option escritorModelo = new Option("em", "escritorModelo", true, "Modelo de framework a ser escrito");
		escritorModelo.setRequired(false);
		escritorModelo.setType(String[].class);
		escritorModelo.setArgs(Option.UNLIMITED_VALUES);
		escritorModelo.setValueSeparator(',');
		options.addOption(escritorModelo);
		
		Option diretorioLocal = new Option("d", "diretorioLocal", true, "Diretorio local de saida dos dados");
		diretorioLocal.setRequired(false);
		options.addOption(diretorioLocal);
		
		Option pacoteRaiz = new Option("p", "pacoteRaiz", true, "Pacote raiz do sistema");
		pacoteRaiz.setRequired(false);
		options.addOption(pacoteRaiz);
		
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();

		try {
			return parser.parse(options, args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("Estrutura de Sistema", options);
			System.exit(1);
			throw e;
		}

	}

	@Override
	public String toString() {

		return ToStringBuilder.reflectionToString(this);

	}
}
