package com.foodies.freshmeal.common.io.service.impl;

import com.foodies.freshmeal.common.io.IDataContext;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;

public class ServiceInput<T> implements IServiceInput<T> {

    private static final long serialVersionUID = -6238300946079606943L;

	private T input;

    private IServiceContext serviceContext = new ServiceContext();

    private IDataContext dataContext;

    public ServiceInput() {
        // Default constructor
    }

    public ServiceInput(T input, IServiceContext serviceContext) {
        this.input = input;
        this.serviceContext = serviceContext;
    }

    public ServiceInput(T input, IServiceContext serviceContext, IDataContext dataContext) {
        this.input = input;
        this.serviceContext = serviceContext;
        this.dataContext = dataContext;
    }

    public ServiceInput(T input, IDataContext dataContext) {
        this.input = input;
        this.dataContext = dataContext;
    }

    @Override
    public T getInput() {
        return input;
    }

    @Override
    public void setInput(T input) {
        this.input = input;
    }

    @Override
    public IServiceContext getServiceContext() {
        return serviceContext;
    }

    @Override
    public void setServiceContext(IServiceContext serviceContext) {
        this.serviceContext = serviceContext;
    }

    @Override
    public IDataContext getDataContext() {
        return dataContext;
    }

    @Override
    public void setDataContext(IDataContext dataContext) {
        this.dataContext = dataContext;
    }

}
