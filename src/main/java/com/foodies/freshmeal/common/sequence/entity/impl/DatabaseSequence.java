package com.foodies.freshmeal.common.sequence.entity.impl;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.foodies.freshmeal.common.entity.IEntity;
import com.foodies.freshmeal.common.sequence.entity.IDatabaseSequence;

@Document(collection = "fm_database_sequences")
public class DatabaseSequence implements IDatabaseSequence {

    private static final long serialVersionUID = -4030816117428671912L;
    
	@Id
    private String id;
    private long seq;

    public DatabaseSequence() {
    }

    public static IEntity create() {
        return new DatabaseSequence();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public long getSeq() {
        return seq;
    }

    @Override
    public void setSeq(long seq) {
        this.seq = seq;
    }

}
