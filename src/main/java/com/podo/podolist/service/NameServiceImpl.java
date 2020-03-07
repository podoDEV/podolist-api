package com.podo.podolist.service;

import org.springframework.stereotype.Service;

@Service
public class NameServiceImpl implements NameService {

    private static final String NAME_ANONYMOUS = "anonymous";

    @Override
    public String getRandomName() {
        return NAME_ANONYMOUS;
    }
}
