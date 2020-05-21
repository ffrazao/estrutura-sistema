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

		String pacoteFiltroDTO = String.format("%s.modelo.dto", this.getEscritor().argumentos.pacoteRaiz);

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
			
			String tipoPk = null;

			for (PropriedadeInfo pi : item.propriedadeInfoList) {
				if (pi.getColuna().isChavePrimaria()) {
					tipoPk = pi.getTipoJava() == null ? "Integer" : pi.getTipoJava().getCanonicalName();
				}
			}

			try (BufferedWriter w = new BufferedWriter(new FileWriter(arquivoLocal, false))) {

				String pacoteFinal = pacote.concat(StringUtils.isNotBlank(item.pacote) ? ".".concat(item.pacote) : "");
				String pacoteFinalDao = pacoteDao
						.concat(StringUtils.isNotBlank(item.pacote) ? ".".concat(item.pacote) : "");
				String pacoteFinalFiltroDTO = pacoteFiltroDTO
						.concat(StringUtils.isNotBlank(item.pacote) ? ".".concat(item.pacote) : "");

				// pacote
				w.append(String.format("package %s;", pacoteFinal));
				w.newLine();
				w.newLine();

				// importações
				w.append("import org.springframework.beans.factory.annotation.Autowired;");
				w.newLine();
				w.append("import org.springframework.stereotype.Service;");
				w.newLine();
				w.newLine();
				w.append(String.format("import %s.CRUDBO;", pacote));
				w.newLine();
				w.append(String.format("import %s.%sDAO;", pacoteFinalDao, item.nome));
				w.newLine();
				w.append(String.format("import %s.%sFiltroDTO;", pacoteFinalFiltroDTO, item.nome));
				w.newLine();
				w.append(String.format("import %s.%s;", item.pacoteFinal, item.nome));
				w.newLine();
				w.newLine();

				// declarar a classe
				w.append(String.format("@Service"));
				w.newLine();
				w.append(String.format("public class %sBO extends CRUDBO<%s, %s, %sFiltroDTO> {", item.nome, item.nome,
						tipoPk, item.nome));
				w.newLine();
				w.newLine();

				w.append(String.format("   public %sBO(@Autowired %sDAO dao) {", item.nome,
						item.nome));
				w.newLine();
				w.append(String.format("      super(dao);"));
				w.newLine();
				w.append(String.format("   }"));
				w.newLine();
				w.newLine();
				w.append(String.format(""));
				w.newLine();

				w.append(String.format("   public %sDAO getDAO() {", item.nome));
				w.newLine();
				w.append(String.format("	  return (%sDAO) super.getDAO();", item.nome));
				w.newLine();
				w.append(String.format("   }"));
				w.newLine();
				w.newLine();
				
				w.append("}");
				w.newLine();
				w.newLine();
			}

		}
	}

	public EscritorJavaSpringBoot getEscritor() {
		return (EscritorJavaSpringBoot) this.escritor;
	}

}
