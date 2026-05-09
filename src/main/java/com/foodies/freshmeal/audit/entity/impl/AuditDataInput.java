package com.foodies.freshmeal.audit.entity.impl;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.foodies.freshmeal.audit.entity.IAuditLog;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
@SuppressWarnings("unused")
public class AuditDataInput implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String txnUserId;
	
	private Long txnIdLong;
	
	private Long txnInstanceIdLong;
	
	private Timestamp timestamp;
	
	private List<IAuditLog> auditLog;
	
	public AuditDataInput() {
		auditLog = new ArrayList<IAuditLog>();
	}

	public AuditDataInput(String txnUserId, Long txnIdLong, Long txnInstanceIdLong, Timestamp timestamp,
			List<IAuditLog> auditLog) {
		super();
		this.txnUserId = txnUserId;
		this.txnIdLong = txnIdLong;
		this.txnInstanceIdLong = txnInstanceIdLong;
		this.timestamp = timestamp;
		this.auditLog = auditLog;
	}
	
	
	
}
