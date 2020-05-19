package com.frazao.bo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.frazao.modelo.dto.FiltroDTO;

public class CRUDBO<T, Id> implements BO {

	private JpaRepository<T, Id> dao;

	public CRUDBO(JpaRepository<T, Id> dao) {
		this.dao = dao;
	}

	public JpaRepository<T, Id> getDAO() {
		return this.dao;
	}

	public Id create(T t) {
		this.getDAO().save(t);
		return null;
	}

	@SuppressWarnings("unchecked")
	public T restore(Id id) {
		T result = (T) this.getDAO().findById(id);
		return result;
	}

	public T update(Id id, T t) {
		T result = (T) this.getDAO().save(t);
		return result;
	}

	public void delete(Id id) {
		T result = this.restore(id);
		this.getDAO().delete(result);
	}

	public List<T> filter(FiltroDTO filtro) {
		List<T> result = this.getDAO().findAll();
		return result;
	}

}
