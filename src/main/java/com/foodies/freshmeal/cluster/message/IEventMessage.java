package com.foodies.freshmeal.cluster.message;

import java.io.InputStream;

import org.springframework.integration.transformer.MessageTransformationException;
import org.w3c.dom.Document;

public interface IEventMessage {
    public String getAttribute(String key) throws MessageTransformationException;

    public void setAttribute(String key, String value) throws MessageTransformationException;

    public String getMessageCode();

    public void setMessageCode(String messageCode);

    public String getMessage() throws MessageTransformationException;

    public Document getDocumentAsJson();

    public void setMessage(String message) throws MessageTransformationException;

    public void setMessage(InputStream inputStream) throws MessageTransformationException;

    public void setMessageAsJson(Document document) throws MessageTransformationException;

}
