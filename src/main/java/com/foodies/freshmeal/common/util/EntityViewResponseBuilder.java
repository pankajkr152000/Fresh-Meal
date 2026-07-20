package com.foodies.freshmeal.common.util;

import com.foodies.freshmeal.common.dto.view.EntityNavigation;
import com.foodies.freshmeal.common.dto.view.EntityViewResponse;

public final class EntityViewResponseBuilder {

	private EntityViewResponseBuilder() {
	}

	public static <T> EntityViewResponse<T> build(T data, EntityNavigation navigation) {

		EntityViewResponse<T> response = new EntityViewResponse<>();

		response.setData(data);
		response.setNavigation(navigation);

		return response;
	}
}
