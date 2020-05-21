package com.frazao.escritor.java_springboot.estrutura;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.apache.commons.lang3.StringUtils;

import com.frazao.escritor.Escritor;
import com.frazao.escritor.java_springboot.EscritorJavaSpringBoot;

public class FiltroDTO extends EstruturaBasica {

	public FiltroDTO(Escritor escritor) {
		super(escritor);
	}

	@Override
	public void escrever() throws Exception {
		File dir = this.getEscritor().diretorios.get("dto");

		String pacote = String.format("%s.modelo.dto", this.getEscritor().argumentos.pacoteRaiz);

		for (EntidadeInfo item : this.getEscritor().entidade.mapa.values()) {
			if (item.pacote == null) {
				continue;
			}

			File arquivoLocal = dir;
			if (StringUtils.isNotBlank(item.pacote)) {
				arquivoLocal = new File(dir, item.pacote);
			}
			arquivoLocal.mkdirs();

			arquivoLocal = new File(arquivoLocal, item.nome.concat("FiltroDTO.java"));

			try (BufferedWriter w = new BufferedWriter(new FileWriter(arquivoLocal, false))) {

				String pacoteFinal = pacote.concat(StringUtils.isNotBlank(item.pacote) ? ".".concat(item.pacote) : "");

				// pacote
				w.append("package ").append(pacoteFinal).append(";");
				w.newLine();
				w.newLine();

				w.append("import lombok.Data;");
				w.newLine();
				w.append("import lombok.EqualsAndHashCode;");
				w.newLine();
				w.append("import lombok.NoArgsConstructor;");
				w.newLine();
				w.append("import lombok.ToString;");
				w.newLine();
				w.newLine();

				// importações
				w.append("import ").append(pacote).append(".FiltroDTO;");
				w.newLine();
				w.newLine();

				// declarar a classe
				w.append("@Data");
				w.newLine();
				w.append("@NoArgsConstructor");
				w.newLine();
				w.append("@EqualsAndHashCode");
				w.newLine();
				w.append("@ToString");
				w.newLine();
				w.append(String.format("public class %sFiltroDTO implements FiltroDTO {", item.nome));
				w.newLine();
				w.newLine();

				w.append("   ");
				w.append(String.format("   private static final long serialVersionUID = 1L;"));
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
