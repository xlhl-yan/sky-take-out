package com.xlhl.sky.service.user;

import com.xlhl.sky.entity.AddressBook;

import java.util.List;

public interface UserAddressBookService {

    List<AddressBook> list(AddressBook addressBook);

    void save(AddressBook addressBook);

    AddressBook getById(Long id);

    void update(AddressBook addressBook);

    void setDefault(AddressBook addressBook);

    void deleteById(Long id);

}
