package com.frazao.escritor.java_springboot.estrutura;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.frazao.escritor.Escritor;
import com.frazao.escritor.java_springboot.EscritorJavaSpringBoot;
import com.frazao.leitor.bd.Coluna;
import com.frazao.leitor.bd.Esquema;
import com.frazao.leitor.bd.Tabela;

public class Entidade extends EstruturaBasica {

	private Map<String, EntidadeInfo> mapa = new HashMap<>();
	
	public EscritorJavaSpringBoot getEscritor() {
		return (EscritorJavaSpringBoot) this.escritor;
	}

	public Entidade(Escritor escritor) {
		super(escritor);
		
		for (Esquema esquema : this.getEscritor().conteudo) {
			for (Tabela tabela : esquema.getTabelas()) {
				EntidadeInfo entidade = new EntidadeInfo(esquema, tabela);
				for (Coluna coluna : tabela.getColunas()) {
					entidade.addPropriedadeInfo(new PropriedadeInfo(esquema, tabela, coluna));
				}
				if (mapa.containsKey(entidade.pacote.concat(entidade.nome))) {
					throw new IllegalStateException("Definição repetida");
				} else {
					mapa.put(entidade.pacote.concat(entidade.nome), entidade);
				}
			}
		}

	}

	@Override
	public void escrever() throws Exception {
		File dir = this.getEscritor().diretorios.get("entidade");

		String pacote = String.format("%s.modelo.entidade", this.getEscritor().argumentos.pacoteRaiz);

		for (Entry<String, EntidadeInfo> item : mapa.entrySet()) {
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

				// importações
				w.append(String.format("import javax.persistence.Column;"));
				w.newLine();
				w.append(String.format("import javax.persistence.Entity;"));
				w.newLine();
				w.append(String.format("import javax.persistence.EnumType;"));
				w.newLine();
				w.append(String.format("import javax.persistence.Enumerated;"));
				w.newLine();
				w.append(String.format("import javax.persistence.GeneratedValue;"));
				w.newLine();
				w.append(String.format("import javax.persistence.GenerationType;"));
				w.newLine();
				w.append(String.format("import javax.persistence.Id;"));
				w.newLine();
				w.append(String.format("import javax.persistence.Table;"));
				w.newLine();
				w.append(String.format("import javax.persistence.Lob;"));
				w.newLine();
				w.append(String.format("import javax.persistence.Basic;"));
				w.newLine();
				w.append(String.format(""));
				w.newLine();
				w.append(String.format("import com.fasterxml.jackson.annotation.JsonIgnore;"));
				w.newLine();
				w.append(String.format("import lombok.Data;"));
				w.newLine();
				w.append(String.format("import lombok.EqualsAndHashCode;"));
				w.newLine();
				w.append(String.format(""));
				w.newLine();
				w.newLine();
				w.append(String.format("import %s.modelo.EntidadeBaseTemId;", this.getEscritor().argumentos.pacoteRaiz));
				w.newLine();
				w.append(String.format(""));

				// declarar a classe
				w.append(String.format("@Entity(name = \"%s\")", item.getValue().nome));
				w.newLine();
				w.append(String.format("@Table(schema = \"%s\", name = \"%s\")", item.getValue().esquema,
						item.getValue().tabela));
				w.newLine();
				w.append(String.format("@Data"));
				w.newLine();
				w.append(String.format("@EqualsAndHashCode(callSuper = true)"));
				w.newLine();
				w.append(String.format("public class %s extends EntidadeBaseTemId {", item.getValue().nome));
				w.newLine();

				w.append("   ");
				w.append(String.format("private static final long serialVersionUID = 1L;"));
				w.newLine();

				for (PropriedadeInfo pi: item.getValue().propriedadeInfoList) {
					w.newLine();
					w.append(pi.toString());
					w.newLine();
				}
				
				w.newLine();
				w.newLine();

				w.append("}");
			}

		}
	}

}
