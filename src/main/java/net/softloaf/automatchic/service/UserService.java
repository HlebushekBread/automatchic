package net.softloaf.automatchic.service;

import net.softloaf.automatchic.dto.NewUserDto;

public interface UserService {
    void saveNewUser(NewUserDto newUserDto);
}
