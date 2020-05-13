package com.frazao.escritor.java_springboot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;

import com.frazao.Argumentos;
import com.frazao.bd.Coluna;
import com.frazao.bd.Esquema;
import com.frazao.bd.Tabela;

public class Dominio extends EstruturaBasica {

	private Map<String, DominioInfo> mapa = new HashMap<>();

	public Dominio(Argumentos argumentos, List<Esquema> conteudo, Map<String, File> diretorios) {
		super(argumentos, conteudo, diretorios);

		// iniciar alguns dominios comuns para definir o nome
		String[] t;
		t = new String[] { "S", "N" };
		mapa.put(Arrays.asList(t).toString(), new DominioInfo(t, "Confirmacao"));
		t = new String[] { "N", "S" };
		mapa.put(Arrays.asList(t).toString(), new DominioInfo(t, "Confirmacao"));
		t = new String[] { "PF", "PJ" };
		mapa.put(Arrays.asList(t).toString(), new DominioInfo(t, "PessoaTipo"));
		t = new String[] { "M", "F" };
		mapa.put(Arrays.asList(t).toString(), new DominioInfo(t, "PessoaGenero"));

		for (Esquema esquema : this.conteudo) {
			for (Tabela tabela : esquema.getTabelas()) {
				for (Coluna coluna : tabela.getColunas()) {
					if ("enum".equalsIgnoreCase(coluna.getTipo()) || "set".equalsIgnoreCase(coluna.getTipo())) {

						String c = coluna.getComplemento().replaceAll("\',\'", ",");
						String[] valores = c.substring(c.indexOf("(") + 2, c.lastIndexOf(")") - 1).split(",");
						String local = String.format("%s.%s.%s", esquema.getNome(), tabela.getNome(), coluna.getNome());

						if (!mapa.containsKey(Arrays.asList(valores).toString())) {
							String nome = CaseUtils.toCamelCase(
									String.format("%s_%s", tabela.getNome(), coluna.getNome()), true,
									new char[] { '_', '.' });
							mapa.put(Arrays.asList(valores).toString(), new DominioInfo(valores, local, nome));
						} else {
							mapa.get(Arrays.asList(valores).toString()).local.add(local);
						}
					}
				}
			}
		}

		// definir os nomes de pacote
		for (Entry<String, DominioInfo> item : mapa.entrySet()) {
			if (item.getValue().local.size() > 1) {
				item.getValue().pacote = "";
			} else if (item.getValue().local.size() == 1) {
				item.getValue().pacote = item.getValue().local.get(0).substring(0,
						item.getValue().local.get(0).indexOf("."));
			}
		}
	}

	@Override
	public void escrever(EscritorJavaSpringBoot escritor) throws Exception {
		File dir = this.diretorios.get("dominio");

		String pacote = String.format("%s.modelo.dominio", this.argumentos.pacoteRaiz);

		for (Entry<String, DominioInfo> item : mapa.entrySet()) {
			if (item.getValue().pacote == null) {
				continue;
			}

			File arquivoLocal = dir;
			if (StringUtils.isNotBlank(item.getValue().pacote)) {
				arquivoLocal = new File(dir, item.getValue().pacote);
			}
			arquivoLocal.mkdirs();

			arquivoLocal = new File(arquivoLocal, item.getValue().nome.concat(".java"));

			try (BufferedWriter w = new BufferedWriter(new FileWriter(arquivoLocal, false))) {

				item.getValue().pacoteFinal = pacote.concat(
						StringUtils.isNotBlank(item.getValue().pacote) ? ".".concat(item.getValue().pacote) : "");

				// pacote
				w.append("package ").append(item.getValue().pacoteFinal).append(";");
				w.newLine();
				w.newLine();

				// declarar o enum
				w.append("public enum ").append(item.getValue().nome).append(" {");
				w.newLine();
				w.newLine();
				w.append("   ");

				// valores
				for (int i = 0; i < item.getValue().valor.length; i++) {
					String vlr = item.getValue().valor[i];
					if (i > 0) {
						w.append(", ");
					}
					w.append(vlr.toUpperCase()).append("(\"").append(vlr).append("\")");
				}
				w.append(";");
				w.newLine();
				w.newLine();

				// definicao propriedade privadas
				w.append("   private String descricao;");
				w.newLine();
				w.newLine();

				// construtor
				w.append("   private ").append(item.getValue().nome).append("(String descricao) {");
				w.newLine();
				w.append("      this.descricao = descricao;");
				w.newLine();
				w.append("   }");
				w.newLine();
				w.newLine();

				// get publico
				w.append("   public String getDescricao() {");
				w.newLine();
				w.append("      return this.descricao;");
				w.newLine();
				w.append("   }");
				w.newLine();
				w.newLine();

				w.append("   public String toString() {");
				w.newLine();
				w.append("      return this.getDescricao();");
				w.newLine();
				w.append("   }");
				w.newLine();
				w.newLine();

				w.append("}");
			}

		}
	}

	public DominioInfo getDominio(String esquema, String tabela, String coluna) {
		return getDominio(String.format("%s.%s.%s", esquema, tabela, coluna));
	}

	public DominioInfo getDominio(String local) {
		for (Entry<String, DominioInfo> item : mapa.entrySet()) {
			for (String l : item.getValue().local) {
				if (local.equals(l)) {
					return item.getValue();
				}
			}
		}
		throw new NullPointerException();
	}

}
