package com.foodies.freshmeal.cart.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Response DTO exposing the current cart lifecycle status and the
 * transitions permitted from that status.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartStatusResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String value;

    private String label;

    private List<String> allowedTransitions;
}