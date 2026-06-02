package com.foodies.freshmeal.common.io.service;

import java.io.Serializable;

import com.foodies.freshmeal.common.io.IDataContext;

public interface IServiceOutput<T> extends Serializable {
    
    public T getOutput();

    public void setOutput(T output);

    public IServiceContext getServiceContext();

    public void setServiceContext(IServiceContext serviceContext);

    public void setServiceContext(IDataContext dataContext);

}
