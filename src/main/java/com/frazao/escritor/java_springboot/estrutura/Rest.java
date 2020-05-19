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

		for (EntidadeInfo item : this.getEscritor().entidade.mapa.values()) {
			if (item.pacote == null) {
				continue;
			}

			File arquivoLocal = dir;
			if (StringUtils.isNotBlank(item.pacote)) {
				arquivoLocal = new File(dir, item.pacote);
			}
			arquivoLocal.mkdirs();

			arquivoLocal = new File(arquivoLocal, item.nome.concat("REST.java"));

			try (BufferedWriter w = new BufferedWriter(new FileWriter(arquivoLocal, false))) {

				String pacoteFinal = pacote.concat(StringUtils.isNotBlank(item.pacote) ? ".".concat(item.pacote) : "");
				String pacoteFinalBo = pacoteBo
						.concat(StringUtils.isNotBlank(item.pacote) ? ".".concat(item.pacote) : "");

				// pacote
				w.append("package ").append(pacoteFinal).append(";");
				w.newLine();
				w.newLine();

				// importações
				w.append("import ").append(pacote).append(".CRUDREST;");
				w.newLine();
				w.append("import org.springframework.beans.factory.annotation.Autowired;");
				w.newLine();
				w.append("import org.springframework.web.bind.annotation.GetMapping;");
				w.newLine();
				w.append("import org.springframework.web.bind.annotation.PathVariable;");
				w.newLine();
				w.append("import org.springframework.web.bind.annotation.RequestMapping;");
				w.newLine();
				w.append("import org.springframework.web.bind.annotation.RestController;");
				w.newLine();			
				w.newLine();			
				w.newLine();			
				w.append(String.format("import %s.%s;", item.pacoteFinal, item.nome));
				w.newLine();			
				w.append(String.format("import %s.%sBO;", pacoteFinalBo, item.nome));
				w.newLine();			
				
				String tipoPk = null;
				for (PropriedadeInfo pi : item.propriedadeInfoList) {
					if (pi.getColuna().isChavePrimaria()) {
						tipoPk = pi.getTipoJava() == null ? "Integer" : pi.getTipoJava().getCanonicalName();
					}
				}

				// declarar a classe
				w.append(String.format("@RestController"));
				w.newLine();			
				w.append(String.format("@RequestMapping(value = \"%s\")", item.tabela.getNome().replaceAll("_", "-")));
				w.newLine();			
				
				w.append(String.format("public class %sREST extends CRUDREST<%s.%s, %s, %s.%sBO> {", item.nome, item.pacoteFinal,
						item.nome, tipoPk, pacoteFinalBo, item.nome));
				w.newLine();
				w.newLine();

				w.append(String.format("   public %sREST(@Autowired %s.%sBO bo) {", item.nome, pacoteFinalBo, item.nome));
				w.newLine();
				w.append(String.format("      super(bo);"));
				w.newLine();
				w.append(String.format("   }"));
				w.newLine();
				w.newLine();
				w.newLine();
				w.append(String.format(""));
				w.newLine();

				w.append(String.format("public %s.%sBO getBO() {", pacoteFinalBo, item.nome));
				w.newLine();
				w.append(String.format("	return this.getBO();"));
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
