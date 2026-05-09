package com.foodies.freshmeal.common.io.service;

import java.io.Serializable;

import com.foodies.freshmeal.common.io.IDataContext;

public interface IServiceInput<T> extends Serializable {
    
    public T getInput();

    public void setInput(T input);

    public IServiceContext getServiceContext();

    public void setServiceContext(IServiceContext serviceContext);

    public IDataContext getDataContext();

    public void setDataContext(IDataContext dataContext);

}
