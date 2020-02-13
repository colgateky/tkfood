package com.tk.center.service;

public interface VariableService {
    long getLongAndInc(String key);
    String generateId(Class entityCls, int size);
    String generateId(Class entityCls, int size, String prefix);
}
