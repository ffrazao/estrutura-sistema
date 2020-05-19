package com.frazao.escritor.java_springboot.estrutura;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;

import com.frazao.escritor.Escritor;
import com.frazao.escritor.java_springboot.EscritorJavaSpringBoot;
import com.frazao.leitor.bd.Coluna;
import com.frazao.leitor.bd.Esquema;
import com.frazao.leitor.bd.Relacionamento;
import com.frazao.leitor.bd.Tabela;

public class Entidade extends EstruturaBasica {

	public Map<String, EntidadeInfo> mapa = new HashMap<>();

	public Entidade(Escritor escritor) {
		super(escritor);

		for (Esquema esquema : this.getEscritor().conteudo) {
			for (Tabela tabela : esquema.getTabelas()) {
				EntidadeInfo entidadeInfo = new EntidadeInfo(esquema, tabela);

				// ajustar as propriedades
				for (Coluna coluna : tabela.getColunas()) {
					entidadeInfo.addPropriedadeInfo(new PropriedadeInfo(entidadeInfo, coluna));
				}

				// armanzenar entidade
				if (mapa.containsKey(entidadeInfo.pacote.concat(entidadeInfo.nome))) {
					throw new IllegalStateException(
							"Definição repetida " + entidadeInfo.pacote.concat(entidadeInfo.nome));
				} else {
					mapa.put(entidadeInfo.pacote.concat(entidadeInfo.nome), entidadeInfo);
				}
			}
		}

		// ajustar os relacionamentos
		for (EntidadeInfo entidadeInfo : mapa.values()) {
			Tabela tabela = entidadeInfo.tabela;

			// ajustar os relacionamentosmapamapa
			for (Relacionamento relacionamento : tabela.getRelacionamentos()) {

				// identificar a entidade externa
				Tabela tabelaRef = relacionamento.getTabelaRef();
				EntidadeInfo entidadeInfoRef = this.encontraEntidade(mapa, tabelaRef);

				// identificar o vinculo de colunas
				for (Entry<Coluna, Coluna> rel : relacionamento.getColunas().entrySet()) {
					entidadeInfo.getPropriedade(rel.getKey())
							.setRefExterna(entidadeInfoRef.getPropriedade(rel.getValue()));
					entidadeInfoRef.addList(entidadeInfo);
				}
			}
		}

	}

	private EntidadeInfo encontraEntidade(Map<String, EntidadeInfo> mapa, Tabela tabela) {
		for (EntidadeInfo item : mapa.values()) {
			if (item.esquema.getNome().equals(tabela.getEsquema().getNome())
					&& item.tabela.getNome().equals(tabela.getNome())) {
				return item;
			}
		}
		return null;
	}

	@Override
	public void escrever() throws Exception {
		File dir = this.getEscritor().diretorios.get("entidade");

		String pacote = String.format("%s.modelo.entidade", this.getEscritor().argumentos.pacoteRaiz);

		for (EntidadeInfo item : mapa.values()) {
			if (item.pacote == null) {
				continue;
			}

			File arquivoLocal = dir;
			if (StringUtils.isNotBlank(item.pacote)) {
				arquivoLocal = new File(dir, item.pacote);
			}
			arquivoLocal.mkdirs();

			arquivoLocal = new File(arquivoLocal, item.nome.concat(".java"));

			Propriedade propriedade = new Propriedade(this.escritor);

			try (BufferedWriter w = new BufferedWriter(new FileWriter(arquivoLocal, false))) {

				item.pacoteFinal = pacote.concat(StringUtils.isNotBlank(item.pacote) ? ".".concat(item.pacote) : "");

				// pacote
				w.append("package ").append(item.pacoteFinal).append(";");
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
				w.append(String.format("import javax.persistence.OneToOne;"));
				w.newLine();
				w.append(String.format("import javax.persistence.ManyToOne;"));
				w.newLine();
				w.append(String.format("import javax.persistence.JoinColumn;"));
				w.newLine();
				w.append(String.format("import javax.persistence.Transient;"));
				w.newLine();
				w.append(String.format("import javax.persistence.FetchType;"));
				w.newLine();
				w.append(String.format("import javax.persistence.MapsId;"));
				w.newLine();
				w.append(String.format(""));
				w.newLine();
				w.append(String.format("import com.fasterxml.jackson.annotation.JsonIgnore;"));
				w.newLine();
				w.append(String.format("import lombok.Data;"));
				w.newLine();
				w.append(String.format("import lombok.EqualsAndHashCode;"));
				w.newLine();
				w.append(String.format("import java.util.List;"));
				w.newLine();
				w.append(String.format("import java.math.BigDecimal;"));
				w.newLine();
				w.append(String.format(""));
				w.newLine();
				w.newLine();
				w.append(
						String.format("import %s.modelo.EntidadeBaseTemId;", this.getEscritor().argumentos.pacoteRaiz));
				w.newLine();
				w.append(String.format(""));

				// declarar a classe
				w.append(String.format("@Entity(name = \"%s\")", item.nome));
				w.newLine();
				w.append(String.format("@Table(schema = \"%s\", name = \"%s\")", item.esquema, item.tabela));
				w.newLine();
				w.append(String.format("@Data"));
				w.newLine();
				w.append(String.format("@EqualsAndHashCode(callSuper = true)"));
				w.newLine();
				w.append(String.format("public class %s extends EntidadeBaseTemId {", item.nome));
				w.newLine();

				w.append("   ");
				w.append(String.format("private static final long serialVersionUID = 1L;"));
				w.newLine();

				// adicionar propriedades
				for (PropriedadeInfo pi : item.propriedadeInfoList) {
					w.newLine();
					w.append(propriedade.escrever(pi));
					w.newLine();
				}
				
				// adicionar referencias externas
				for (EntidadeInfo ei: item.getList()) {
					w.newLine();
					w.append(String.format(" @Transient private List<%s> %sList;", ei.nome, 
							CaseUtils.toCamelCase(ei.tabela.getNome(), false, new char[] { '_', '.' })));
					w.newLine();
				}

				w.newLine();
				w.newLine();

				w.append("}");
			}

		}
	}

	public EscritorJavaSpringBoot getEscritor() {
		return (EscritorJavaSpringBoot) this.escritor;
	}

}
