package com.foodies.freshmeal.common.sequence.entity;

import com.foodies.freshmeal.common.entity.IEntity;

public interface IDatabaseSequence extends IEntity {

    public String getId();

    public void setId(String id);

    public long getSeq();

    public void setSeq(long seq);

}
