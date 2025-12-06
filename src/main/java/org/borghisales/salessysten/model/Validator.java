package org.borghisales.salessysten.model;

public abstract class Validator <T>{
    protected abstract boolean validate(T entity);
}
