package com.foodies.freshmeal.io.service;

import java.io.Serializable;

import com.foodies.freshmeal.io.IDataContext;

public interface IServiceOutput<T> extends Serializable {
    
    public T getOutput();

    public void setOutput(T output);

    public IServiceContext getServiceContext();

    public void setServiceContext(IServiceContext serviceContext);

    public IDataContext getDataContext();

    public void setDataContext(IDataContext dataContext);

}
