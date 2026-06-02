package com.foodies.freshmeal.common.io.service.impl;

import com.foodies.freshmeal.common.io.IDataContext;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceOutput;

public class ServiceOutput<T> implements IServiceOutput<T> {

    private T output;

    private IServiceContext serviceContext = new ServiceContext();

    public ServiceOutput() {
        // Default constructor
    }

    public ServiceOutput(T output, IServiceContext serviceContext) {
        this.output = output;
        this.serviceContext = serviceContext;
    }

    public ServiceOutput(T output, IDataContext dataContext) {
        this.output = output;
        initializeServiceContext(dataContext);
    }

    public ServiceOutput(IServiceContext serviceContext) {
        this.serviceContext = serviceContext;
    }

    private void initializeServiceContext(IDataContext dataContext) {
        if (dataContext == null) {
            return;
        }
        if (dataContext instanceof IServiceContext iServiceContext) {
            this.serviceContext = iServiceContext;
            return;
        }
        if (this.serviceContext == null) {
            this.serviceContext = new ServiceContext();
        }
        this.serviceContext.setAttributes(dataContext.getAllAttributes());
        this.serviceContext.setAsOfBusinessDate(dataContext.getAsOfBusinessDate());
    }

    @Override
    public void setServiceContext(IServiceContext serviceContext) {
        this.serviceContext = serviceContext;
    }

    public ServiceOutput(T output) {
        this.output = output;
    }

    @Override
    public T getOutput() {
        return output;
    }

    @Override
    public void setOutput(T output) {
        this.output = output;
    }

    @Override
    public IServiceContext getServiceContext() {
        return serviceContext;
    }

    @Override
    public void setServiceContext(IDataContext dataContext) {

        if (dataContext == null) {
            return;
        }
        if (dataContext instanceof IServiceContext iServiceContext) {
            this.serviceContext = iServiceContext;
            return;
        }
        if (this.serviceContext == null) {
            this.serviceContext = new ServiceContext();
        }
        this.serviceContext.setAttributes(dataContext.getAllAttributes());
        this.serviceContext.setAsOfBusinessDate(dataContext.getAsOfBusinessDate());

    }
}
