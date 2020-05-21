package com.frazao.escritor.java_springboot.estrutura;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.apache.commons.lang3.StringUtils;

import com.frazao.escritor.Escritor;
import com.frazao.escritor.java_springboot.EscritorJavaSpringBoot;

public class Rest extends EstruturaBasica {

	public Rest(Escritor escritor) {
		super(escritor);
	}

	@Override
	public void escrever() throws Exception {
		File dir = this.getEscritor().diretorios.get("rest");

		String pacote = String.format("%s.rest", this.getEscritor().argumentos.pacoteRaiz);

		String pacoteBo = String.format("%s.bo", this.getEscritor().argumentos.pacoteRaiz);

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

			arquivoLocal = new File(arquivoLocal, item.nome.concat("CRUDREST.java"));

			String tipoPk = null;
			
			for (PropriedadeInfo pi : item.propriedadeInfoList) {
				if (pi.getColuna().isChavePrimaria()) {
					tipoPk = pi.getTipoJava() == null ? "Integer" : pi.getTipoJava().getCanonicalName();
				}
			}

			try (BufferedWriter w = new BufferedWriter(new FileWriter(arquivoLocal, false))) {

				String pacoteFinal = pacote.concat(StringUtils.isNotBlank(item.pacote) ? ".".concat(item.pacote) : "");
				String pacoteFinalBo = pacoteBo
						.concat(StringUtils.isNotBlank(item.pacote) ? ".".concat(item.pacote) : "");
				String pacoteFinalFiltroDTO = pacoteFiltroDTO
						.concat(StringUtils.isNotBlank(item.pacote) ? ".".concat(item.pacote) : "");

				// pacote
				w.append("package ").append(pacoteFinal).append(";");
				w.newLine();
				w.newLine();

				// importações
				w.append("import org.springframework.beans.factory.annotation.Autowired;");
				w.newLine();
//				w.append("import org.springframework.web.bind.annotation.GetMapping;");
//				w.newLine();
//				w.append("import org.springframework.web.bind.annotation.PathVariable;");
//				w.newLine();
				w.append("import org.springframework.web.bind.annotation.RequestMapping;");
				w.newLine();
				w.append("import org.springframework.web.bind.annotation.RestController;");
				w.newLine();
				w.newLine();
				w.append("import ").append(pacote).append(".CRUDREST;");
				w.newLine();
				w.append(String.format("import %s.%s;", item.pacoteFinal, item.nome));
				w.newLine();
				w.append(String.format("import %s.%sBO;", pacoteFinalBo, item.nome));
				w.newLine();
				w.append(String.format("import %s.%sFiltroDTO;", pacoteFinalFiltroDTO, item.nome));
				w.newLine();
				w.newLine();


				// declarar a classe
				w.append(String.format("@RestController"));
				w.newLine();
				w.append(String.format("@RequestMapping(value = \"%s\")", item.tabela.getNome().replaceAll("_", "-")));
				w.newLine();

				w.append(String.format("public class %sCRUDREST extends CRUDREST<%s, %s, %sFiltroDTO, %sBO> {",
						item.nome, item.nome, tipoPk, item.nome, item.nome));
				w.newLine();
				w.newLine();

				w.append(String.format("   public %sCRUDREST(@Autowired %sBO bo) {", item.nome, item.nome));
				w.newLine();
				w.append(String.format("      super(bo);"));
				w.newLine();
				w.append(String.format("   }"));
				w.newLine();
				w.newLine();

				w.append(String.format("   public %sBO getBO() {", item.nome));
				w.newLine();
				w.append(String.format("	  return this.getBO();"));
				w.newLine();
				w.append(String.format("   }"));
				w.newLine();
				w.newLine();

				w.append(String.format("	@Override"));
				w.newLine();
				w.append(String.format("	public %s novo(%s modelo) throws Exception {", item.nome, item.nome));
				w.newLine();
				w.append(String.format("		return modelo == null ? new %s() : modelo;", item.nome));
				w.newLine();
				w.append(String.format("	}"));
				w.newLine();
				w.newLine();
				w.append("}");
				w.newLine();
			}

		}
	}

	public EscritorJavaSpringBoot getEscritor() {
		return (EscritorJavaSpringBoot) this.escritor;
	}

}
