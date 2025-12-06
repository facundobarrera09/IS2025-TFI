package org.app.models;

public interface IErrorResponse<T> {
    public String getEvent();
    public String getReason();
    public T getContext();
}
