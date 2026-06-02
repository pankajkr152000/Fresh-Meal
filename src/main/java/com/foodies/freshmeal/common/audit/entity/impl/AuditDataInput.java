package com.foodies.freshmeal.common.audit.entity.impl;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.foodies.freshmeal.common.audit.entity.IAuditLog;

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
	
	private LocalDateTime localDateTime;
	
	private List<IAuditLog> auditLog;
	
	public AuditDataInput() {
		auditLog = new ArrayList<>();
	}

	public AuditDataInput(String txnUserId, Long txnIdLong, Long txnInstanceIdLong, LocalDateTime localDateTime,
			List<IAuditLog> auditLog) {
		super();
		this.txnUserId = txnUserId;
		this.txnIdLong = txnIdLong;
		this.txnInstanceIdLong = txnInstanceIdLong;
		this.localDateTime = localDateTime;
		this.auditLog = auditLog;
	}
	
	
	
}
