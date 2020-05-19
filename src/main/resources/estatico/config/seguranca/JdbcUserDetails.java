package com.frazao.lacodeamorrest.config.seguranca;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.frazao.lacodeamorrest.modelo.dominio.Confirmacao;
import com.frazao.lacodeamorrest.modelo.entidade.Usuario;
import com.frazao.lacodeamorrest.negocio.UsuarioBO;

/**
 * Created by Frazão
 */
public class JdbcUserDetails implements UserDetailsService {

	@Autowired
	private UsuarioBO bo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = bo.findByLogin(username);

		if (usuario == null) {
			throw new UsernameNotFoundException("Usuário " + username + " não encontrado");
		}

		List<GrantedAuthority> perfilList = new ArrayList<>();
		//perfilList.add(new SimpleGrantedAuthority(""));
		perfilList.add(new SimpleGrantedAuthority(usuario.getPerfil().getDescricao()));

		User user = new User(username, usuario.getSenha(), Confirmacao.S.equals(usuario.getAtivo()), true, true, true,
				perfilList);
		return user;
	}

	public static void main(String[] args) {
		org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder e = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder(
				4);

		System.out.println(e.encode(new String("laco-de-amor")));
		System.out.println(e.encode(new String("a")));
	}

}
