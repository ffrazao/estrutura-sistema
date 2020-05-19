package com.frazao.escritor.java_springboot.estrutura;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.apache.commons.lang3.StringUtils;

import com.frazao.escritor.Escritor;
import com.frazao.escritor.java_springboot.EscritorJavaSpringBoot;

public class Dao extends EstruturaBasica {

	public Dao(Escritor escritor) {
		super(escritor);
	}

	@Override
	public void escrever() throws Exception {
		File dir = this.getEscritor().diretorios.get("dao");

		String pacote = String.format("%s.dao", this.getEscritor().argumentos.pacoteRaiz);

		for (EntidadeInfo item : this.getEscritor().entidade.mapa.values()) {
			if (item.pacote == null) {
				continue;
			}

			File arquivoLocal = dir;
			if (StringUtils.isNotBlank(item.pacote)) {
				arquivoLocal = new File(dir, item.pacote);
			}
			arquivoLocal.mkdirs();

			arquivoLocal = new File(arquivoLocal, item.nome.concat("DAO.java"));

			try (BufferedWriter w = new BufferedWriter(new FileWriter(arquivoLocal, false))) {

				String pacoteFinal = pacote.concat(StringUtils.isNotBlank(item.pacote) ? ".".concat(item.pacote) : "");

				// pacote
				w.append("package ").append(pacoteFinal).append(";");
				w.newLine();
				w.newLine();

				// importações
				w.append("import ").append(item.pacoteFinal).append(".").append(item.nome).append(";");
				w.newLine();
				w.newLine();
				w.append("import org.springframework.data.jpa.repository.JpaRepository;");
				w.newLine();
				w.append("import org.springframework.stereotype.Repository;");
				w.newLine();
				w.newLine();

				String tipoPk = null;
				for (PropriedadeInfo pi : item.propriedadeInfoList) {
					if (pi.getColuna().isChavePrimaria()) {
						tipoPk = pi.getTipoJava() == null ? "Integer" : pi.getTipoJava().getCanonicalName();
					}
				}

				// declarar a classe
				w.append(String.format("@Repository public interface %sDAO extends JpaRepository<%s, %s> {", item.nome, item.nome,
						tipoPk));
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