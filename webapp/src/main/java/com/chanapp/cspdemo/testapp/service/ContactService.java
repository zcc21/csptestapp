package com.chanapp.cspdemo.testapp.service;

import java.util.LinkedHashMap;

import com.chanapp.cspdemo.testapp.businessobject.api.contact.IContactRow;

public interface ContactService {

	public void deleteById(Long id);

	public IContactRow getById(Long id);

	public IContactRow addNew(LinkedHashMap<String, Object> contactMap);

	public IContactRow update(Long id, LinkedHashMap<String, Object> updatedContactMap);

}