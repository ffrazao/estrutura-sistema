package com.frazao.escritor.java_springboot.estrutura;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.apache.commons.lang3.StringUtils;

import com.frazao.escritor.Escritor;
import com.frazao.escritor.java_springboot.EscritorJavaSpringBoot;

public class Bo extends EstruturaBasica {

	public Bo(Escritor escritor) {
		super(escritor);
	}

	@Override
	public void escrever() throws Exception {
		File dir = this.getEscritor().diretorios.get("bo");

		String pacote = String.format("%s.bo", this.getEscritor().argumentos.pacoteRaiz);

		String pacoteDao = String.format("%s.dao", this.getEscritor().argumentos.pacoteRaiz);

		for (EntidadeInfo item : this.getEscritor().entidade.mapa.values()) {
			if (item.pacote == null) {
				continue;
			}

			File arquivoLocal = dir;
			if (StringUtils.isNotBlank(item.pacote)) {
				arquivoLocal = new File(dir, item.pacote);
			}
			arquivoLocal.mkdirs();

			arquivoLocal = new File(arquivoLocal, item.nome.concat("BO.java"));

			try (BufferedWriter w = new BufferedWriter(new FileWriter(arquivoLocal, false))) {

				String pacoteFinal = pacote.concat(StringUtils.isNotBlank(item.pacote) ? ".".concat(item.pacote) : "");
				String pacoteFinalDao = pacoteDao
						.concat(StringUtils.isNotBlank(item.pacote) ? ".".concat(item.pacote) : "");

				// pacote
				w.append("package ").append(pacoteFinal).append(";");
				w.newLine();
				w.newLine();

				// importações
				w.append("import ").append(pacote).append(".CRUDBO;");
				w.newLine();
				w.append("import org.springframework.beans.factory.annotation.Autowired;");
				w.newLine();
				w.append("import org.springframework.stereotype.Service;");
				w.newLine();
				w.newLine();
				
				
				
				String tipoPk = null;
				for (PropriedadeInfo pi : item.propriedadeInfoList) {
					if (pi.getColuna().isChavePrimaria()) {
						tipoPk = pi.getTipoJava() == null ? "Integer" : pi.getTipoJava().getCanonicalName();
					}
				}

				// declarar a classe
				w.append(String.format("@Service public class %sBO extends CRUDBO<%s.%s, %s> {", item.nome, item.pacoteFinal,
						item.nome, tipoPk));
				w.newLine();
				w.newLine();

				w.append(String.format("   public %sBO(@Autowired %s.%sDAO dao) {", item.nome, pacoteFinalDao, item.nome));
				w.newLine();
				w.append(String.format("      super(dao);"));
				w.newLine();
				w.append(String.format("   }"));
				w.newLine();
				w.newLine();
				w.newLine();
				w.append(String.format(""));
				w.newLine();

				w.append(String.format("public %s.%sDAO getDAO() {", pacoteFinalDao, item.nome));
				w.newLine();
				w.append(String.format("	return (%s.%sDAO) super.getDAO();", pacoteFinalDao, item.nome));
				w.newLine();
				w.append(String.format("}"));
				w.newLine();
				
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
