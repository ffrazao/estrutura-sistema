package com.frazao.leitor;

import java.util.List;

import com.frazao.Argumentos;
import com.frazao.leitor.bd.Esquema;
import com.frazao.leitor.mysql.LeitorMySQL;

public interface Leitor {

	public static Leitor preparar(Argumentos argumentos) throws Exception {
		switch (argumentos.leitorModelo) {
		case MYSQL:
			return new LeitorMySQL(argumentos);
		}
		
		return null;
	}
	
	public abstract List<Esquema> ler() throws Exception;

}
