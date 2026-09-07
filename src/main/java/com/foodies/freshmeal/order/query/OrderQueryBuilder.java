package com.foodies.freshmeal.order.query;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.foodies.freshmeal.order.dto.OrderSearchRequest;

/**
 * ============================================================================
 * Component : OrderQueryBuilder
 * ============================================================================
 *
 * Builds MongoDB query criteria for the Admin Order Management use case.
 *
 * <p>
 * This component translates the application-level {@link OrderSearchRequest}
 * into MongoDB {@link Query} objects without exposing MongoDB field names to
 * the API layer.
 * </p>
 *
 * <p>
 * Responsibilities:
 * </p>
 *
 * <ul>
 * <li>General order search</li>
 * <li>Status filtering</li>
 * <li>Delivery status filtering</li>
 * <li>Payment status filtering</li>
 * <li>Order type filtering</li>
 * <li>Customer filtering</li>
 * <li>Restaurant filtering</li>
 * <li>Order date filtering</li>
 * <li>Safe sorting</li>
 * </ul>
 *
 * <p>
 * Pagination is intentionally not applied here. The service layer can use the
 * same criteria query for both count and data retrieval while applying
 * pagination only to the data query.
 * </p>
 *
 * ============================================================================
 */
@Component
public class OrderQueryBuilder {

	// =========================================================================
	// MongoDB Field Paths
	// =========================================================================

	private static final String FIELD_ORDER_NUMBER = "orderNumber";

	private static final String FIELD_CUSTOMER_ID = "customer.customerId";

	private static final String FIELD_CUSTOMER_NAME = "customer.customerName";

	private static final String FIELD_CUSTOMER_MOBILE = "customer.mobileNumber";

	private static final String FIELD_RESTAURANT_ID = "restaurant.restaurantId";

	private static final String FIELD_ORDER_TYPE = "orderType";

	private static final String FIELD_ORDER_STATUS = "orderStatus";

	private static final String FIELD_DELIVERY_STATUS = "deliveryStatus";

	private static final String FIELD_PAYMENT_STATUS = "paymentInfo.paymentStatus";

	private static final String FIELD_ORDERED_AT = "timeline.orderedAt";

	private static final String FIELD_UPDATED_AT = "updatedAt";

	private static final String FIELD_GRAND_TOTAL_AMOUNT = "priceSummary.grandTotal.amount";

	// =========================================================================
	// Public API
	// =========================================================================

	/**
	 * Builds the MongoDB criteria and sorting query for an Order search.
	 *
	 * <p>
	 * Pagination is not applied by this method.
	 * </p>
	 *
	 * @param request order search criteria
	 *
	 * @return MongoDB query containing filtering and sorting criteria
	 */
	public Query build(final OrderSearchRequest request) {

		final Query query = new Query();

		if (request == null) {
			return query;
		}

		applySearch(query, request.getSearch());

		applyOrderStatusFilter(query, request.getOrderStatuses());

		applyDeliveryStatusFilter(query, request.getDeliveryStatuses());

		applyPaymentStatusFilter(query, request.getPaymentStatuses());

		applyOrderTypeFilter(query, request.getOrderTypes());

		applyCustomerFilter(query, request.getCustomerId());

		applyRestaurantFilter(query, request.getRestaurantId());

		applyDateRange(query, request.getFromDate(), request.getToDate());

		applySorting(query, request.getSortBy(), request.getSortDirection());

		return query;
	}

	// =========================================================================
	// Search
	// =========================================================================

	/**
	 * Applies general order search.
	 *
	 * <p>
	 * Search is performed against the fields intentionally exposed as searchable by
	 * the Order Management use case:
	 * </p>
	 *
	 * <ul>
	 * <li>Order number</li>
	 * <li>Customer name</li>
	 * <li>Customer mobile number</li>
	 * </ul>
	 *
	 * <p>
	 * Matching is case-insensitive and partial.
	 * </p>
	 *
	 * @param query  MongoDB query
	 * @param search search text
	 */
	private void applySearch(final Query query, final String search) {

		if (!StringUtils.hasText(search)) {
			return;
		}

		final String keyword = search.trim();

		/*
		 * Pattern.quote prevents user-provided search text from being treated as a
		 * regular-expression expression.
		 */
		final String regex = Pattern.quote(keyword);

		final Criteria searchCriteria = new Criteria().orOperator(

				Criteria.where(FIELD_ORDER_NUMBER).regex(regex, "i"),

				Criteria.where(FIELD_CUSTOMER_NAME).regex(regex, "i"),

				Criteria.where(FIELD_CUSTOMER_MOBILE).regex(regex, "i"));

		query.addCriteria(searchCriteria);
	}

	// =========================================================================
	// Status Filters
	// =========================================================================

	/**
	 * Applies OrderStatus filtering.
	 *
	 * @param query    MongoDB query
	 * @param statuses requested order statuses
	 */
	private void applyOrderStatusFilter(final Query query, final List<String> statuses) {

		addInCriteria(query, FIELD_ORDER_STATUS, statuses);
	}

	/**
	 * Applies DeliveryStatus filtering.
	 *
	 * @param query    MongoDB query
	 * @param statuses requested delivery statuses
	 */
	private void applyDeliveryStatusFilter(final Query query, final List<String> statuses) {

		addInCriteria(query, FIELD_DELIVERY_STATUS, statuses);
	}

	/**
	 * Applies PaymentStatus filtering.
	 *
	 * @param query    MongoDB query
	 * @param statuses requested payment statuses
	 */
	private void applyPaymentStatusFilter(final Query query, final List<String> statuses) {

		addInCriteria(query, FIELD_PAYMENT_STATUS, statuses);
	}

	/**
	 * Applies OrderType filtering.
	 *
	 * @param query order query
	 * @param types requested order types
	 */
	private void applyOrderTypeFilter(final Query query, final List<String> types) {

		addInCriteria(query, FIELD_ORDER_TYPE, types);
	}

	/**
	 * Adds an IN criterion when the supplied values contain at least one non-blank
	 * value.
	 *
	 * @param query  MongoDB query
	 * @param field  MongoDB field path
	 * @param values filter values
	 */
	private void addInCriteria(final Query query, final String field, final List<String> values) {

		if (values == null || values.isEmpty()) {
			return;
		}

		final List<String> normalizedValues = values.stream().filter(StringUtils::hasText)
				.filter(value -> !value.isEmpty()).map(value -> value.trim()).distinct().toList();

		if (!normalizedValues.isEmpty()) {
			query.addCriteria(Criteria.where(field).in(normalizedValues));
		}
	}

	// =========================================================================
	// Customer / Restaurant
	// =========================================================================

	/**
	 * Filters orders belonging to a specific customer.
	 *
	 * @param query      MongoDB query
	 * @param customerId customer identifier
	 */
	private void applyCustomerFilter(final Query query, final String customerId) {

		if (!StringUtils.hasText(customerId)) {
			return;
		}

		query.addCriteria(Criteria.where(FIELD_CUSTOMER_ID).is(customerId.trim()));
	}

	/**
	 * Filters orders belonging to a specific restaurant.
	 *
	 * @param query        MongoDB query
	 * @param restaurantId restaurant identifier
	 */
	private void applyRestaurantFilter(final Query query, final String restaurantId) {

		if (!StringUtils.hasText(restaurantId)) {
			return;
		}

		query.addCriteria(Criteria.where(FIELD_RESTAURANT_ID).is(restaurantId.trim()));
	}

	// =========================================================================
	// Date Range
	// =========================================================================

	/**
	 * Applies the order placement date range.
	 *
	 * <p>
	 * The domain timestamp used for this filter is:
	 * </p>
	 *
	 * <pre>
	 * timeline.orderedAt
	 * </pre>
	 *
	 * <p>
	 * Both boundaries are inclusive.
	 * </p>
	 *
	 * @param query    MongoDB query
	 * @param fromDate lower date/time boundary
	 * @param toDate   upper date/time boundary
	 */
	private void applyDateRange(final Query query, final java.time.LocalDateTime fromDate,
			final java.time.LocalDateTime toDate) {

		if (fromDate == null && toDate == null) {
			return;
		}

		final Criteria criteria = Criteria.where(FIELD_ORDERED_AT);

		if (fromDate != null) {
			criteria.gte(fromDate);
		}

		if (toDate != null) {
			criteria.lte(toDate);
		}

		query.addCriteria(criteria);
	}

	// =========================================================================
	// Sorting
	// =========================================================================

	/**
	 * Applies safe sorting based on API-level sort keys.
	 *
	 * <p>
	 * MongoDB field paths are intentionally hidden from the API contract.
	 * </p>
	 *
	 * <pre>
	 * orderedAt  -> timeline.orderedAt
	 * updatedAt  -> updatedAt
	 * grandTotal -> priceSummary.grandTotal.amount
	 * orderNumber -> orderNumber
	 * </pre>
	 *
	 * @param query         MongoDB query
	 * @param sortBy        API-level sort field
	 * @param sortDirection API-level sort direction
	 */
	private void applySorting(final Query query, final String sortBy, final String sortDirection) {

		final String mongoField = resolveSortField(sortBy);

		final Sort.Direction direction = resolveSortDirection(sortDirection);

		query.with(Sort.by(direction, mongoField));
	}

	/**
	 * Resolves an API-level sort field to a MongoDB field path.
	 *
	 * <p>
	 * Unknown values intentionally fall back to orderedAt instead of allowing
	 * arbitrary MongoDB field paths from the client.
	 * </p>
	 *
	 * @param sortBy API-level sort field
	 *
	 * @return approved MongoDB field path
	 */
	private String resolveSortField(final String sortBy) {

		if (!StringUtils.hasText(sortBy)) {
			return FIELD_ORDERED_AT;
		}

		return switch (sortBy.trim()) {

		case "orderedAt" -> FIELD_ORDERED_AT;

		case "updatedAt" -> FIELD_UPDATED_AT;

		case "grandTotal" -> FIELD_GRAND_TOTAL_AMOUNT;

		case "orderNumber" -> FIELD_ORDER_NUMBER;

		default -> FIELD_ORDERED_AT;
		};
	}

	/**
	 * Resolves the API sort direction.
	 *
	 * @param sortDirection API-level sort direction
	 *
	 * @return MongoDB sort direction
	 */
	private Sort.Direction resolveSortDirection(final String sortDirection) {

		if (!StringUtils.hasText(sortDirection)) {
			return Sort.Direction.DESC;
		}

		return switch (sortDirection.trim().toUpperCase(Locale.ROOT)) {

		case "ASC" -> Sort.Direction.ASC;

		case "DESC" -> Sort.Direction.DESC;

		default -> Sort.Direction.DESC;
		};
	}
}