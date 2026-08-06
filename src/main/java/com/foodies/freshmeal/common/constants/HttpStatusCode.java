package com.foodies.freshmeal.common.constants;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

/**
 * ============================================================================
 * HTTP Status Code
 * ============================================================================
 *
 * Centralized HTTP status representation used throughout the FreshMeal
 * application.
 *
 * <p>
 * This enum provides:
 * </p>
 *
 * <ul>
 * <li>Numeric HTTP Status Code</li>
 * <li>Reason Phrase</li>
 * <li>Spring HttpStatus conversion</li>
 * <li>Status category helpers</li>
 * </ul>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public enum HttpStatusCode {

    // =========================================================================
    // 1xx Informational
    // =========================================================================

    CONTINUE(100, "Continue"),
    SWITCHING_PROTOCOLS(101, "Switching Protocols"),
    PROCESSING(102, "Processing"),
    EARLY_HINTS(103, "Early Hints"),

    // =========================================================================
    // 2xx Success
    // =========================================================================

    OK(200, "OK"),
    CREATED(201, "Created"),
    ACCEPTED(202, "Accepted"),
    NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),
    NO_CONTENT(204, "No Content"),
    RESET_CONTENT(205, "Reset Content"),
    PARTIAL_CONTENT(206, "Partial Content"),
    MULTI_STATUS(207, "Multi-Status"),
    ALREADY_REPORTED(208, "Already Reported"),
    IM_USED(226, "IM Used"),

    // =========================================================================
    // 3xx Redirection
    // =========================================================================

    MULTIPLE_CHOICES(300, "Multiple Choices"),
    MOVED_PERMANENTLY(301, "Moved Permanently"),
    FOUND(302, "Found"),
    SEE_OTHER(303, "See Other"),
    NOT_MODIFIED(304, "Not Modified"),
    TEMPORARY_REDIRECT(307, "Temporary Redirect"),
    PERMANENT_REDIRECT(308, "Permanent Redirect"),

    // =========================================================================
    // 4xx Client Error
    // =========================================================================

    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    PAYMENT_REQUIRED(402, "Payment Required"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    NOT_ACCEPTABLE(406, "Not Acceptable"),
    REQUEST_TIMEOUT(408, "Request Timeout"),
    CONFLICT(409, "Conflict"),
    GONE(410, "Gone"),
    PRECONDITION_FAILED(412, "Precondition Failed"),
    PAYLOAD_TOO_LARGE(413, "Payload Too Large"),
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),
    TOO_MANY_REQUESTS(429, "Too Many Requests"),

    // =========================================================================
    // 5xx Server Error
    // =========================================================================

    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    NOT_IMPLEMENTED(501, "Not Implemented"),
    BAD_GATEWAY(502, "Bad Gateway"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    GATEWAY_TIMEOUT(504, "Gateway Timeout");

    /**
     * Numeric HTTP status.
     */
    private final int value;

    /**
     * Reason phrase.
     */
    private final String description;

    /**
     * Lookup map.
     */
    private static final Map<Integer, HttpStatusCode> LOOKUP = Arrays.stream(values())
            .collect(Collectors.toMap(
                    hc -> hc.value(),
                    Function.identity()));

    HttpStatusCode(
            final int value,
            final String description) {

        this.value = value;
        this.description = description;
    }

    /**
     * Returns numeric HTTP status.
     *
     * @return Status code.
     */
    public int value() {

        return value;
    }

    /**
     * Returns reason phrase.
     *
     * @return Description.
     */
    public String getDescription() {

        return description;
    }

    /**
     * Returns Spring HttpStatus.
     *
     * @return Spring HttpStatus.
     */
    public HttpStatus toHttpStatus() {

        return HttpStatus.valueOf(value);
    }

    /**
     * Returns true if informational response.
     *
     * @return true if informational.
     */
    public boolean isInformational() {

        return value >= 100 && value < 200;
    }

    /**
     * Returns true if success response.
     *
     * @return true if successful.
     */
    public boolean isSuccess() {

        return value >= 200 && value < 300;
    }

    /**
     * Returns true if redirection response.
     *
     * @return true if redirection.
     */
    public boolean isRedirection() {

        return value >= 300 && value < 400;
    }

    /**
     * Returns true if client error.
     *
     * @return true if client error.
     */
    public boolean isClientError() {

        return value >= 400 && value < 500;
    }

    /**
     * Returns true if server error.
     *
     * @return true if server error.
     */
    public boolean isServerError() {

        return value >= 500;
    }

    /**
     * Finds status by numeric code.
     *
     * @param value Numeric status.
     *
     * @return Matching status.
     */
    public static HttpStatusCode valueOf(
            final int value) {

        HttpStatusCode status = LOOKUP.get(value);

        if (status == null) {
            throw new IllegalArgumentException(
                    "Unknown HTTP Status : " + value);
        }

        return status;
    }

    /**
     * Returns description from status code.
     *
     * @param value Status code.
     *
     * @return Description.
     */
    public static String getDescription(
            final int value) {

        return valueOf(value).getDescription();
    }
}

// package com.foodies.freshmeal.common.constants;
//
// import java.util.Collections;
// import java.util.LinkedHashMap;
// import java.util.Map;
//
// import org.springframework.http.HttpStatus;
//
//// @SuppressWarnings("checkstyle:ConstantName")
//// @SuppressWarnings("java:S00115")
// public final class HttpStatusCode {
//
// private HttpStatusCode() {
// throw new UnsupportedOperationException("Utility class");
// }
//
// // 1xx Informational
// private static final String HTTP_100 = "CONTINUE";
// private static final String HTTP_101 = "SWITCHING PROTOCOLS";
// private static final String HTTP_102 = "PROCESSING";
// private static final String HTTP_103 = "EARLY HINTS";
//
// // 2xx Success
// private static final String HTTP_200 = "OK";
// private static final String HTTP_201 = "CREATED";
// private static final String HTTP_202 = "ACCEPTED";
// private static final String HTTP_203 = "NON-AUTHORITATIVE INFORMATION";
// private static final String HTTP_204 = "NO CONTENT";
// private static final String HTTP_205 = "RESET CONTENT";
// private static final String HTTP_206 = "PARTIAL CONTENT";
// private static final String HTTP_207 = "MULTI-STATUS";
// private static final String HTTP_208 = "ALREADY REPORTED";
// private static final String HTTP_226 = "IM USED";
//
// // 3xx Redirection
// private static final String HTTP_300 = "MULTIPLE CHOICES";
// private static final String HTTP_301 = "MOVED PERMANENTLY";
// private static final String HTTP_302 = "FOUND";
// private static final String HTTP_303 = "SEE OTHER";
// private static final String HTTP_304 = "NOT MODIFIED";
// private static final String HTTP_305 = "USE PROXY";
// private static final String HTTP_307 = "TEMPORARY REDIRECT";
// private static final String HTTP_308 = "PERMANENT REDIRECT";
//
// // 4xx Client Error
// private static final String HTTP_400 = "BAD REQUEST";
// private static final String HTTP_401 = "UNAUTHORIZED";
// private static final String HTTP_402 = "PAYMENT REQUIRED";
// private static final String HTTP_403 = "FORBIDDEN";
// private static final String HTTP_404 = "NOT FOUND";
// private static final String HTTP_405 = "METHOD NOT ALLOWED";
// private static final String HTTP_406 = "NOT ACCEPTABLE";
// private static final String HTTP_407 = "PROXY AUTHENTICATION REQUIRED";
// private static final String HTTP_408 = "REQUEST TIMEOUT";
// private static final String HTTP_409 = "CONFLICT";
// private static final String HTTP_410 = "GONE";
// private static final String HTTP_411 = "LENGTH REQUIRED";
// private static final String HTTP_412 = "PRECONDITION FAILED";
// private static final String HTTP_413 = "PAYLOAD TOO LARGE";
// private static final String HTTP_414 = "URI TOO LONG";
// private static final String HTTP_415 = "UNSUPPORTED MEDIA TYPE";
// private static final String HTTP_416 = "RANGE NOT SATISFIABLE";
// private static final String HTTP_417 = "EXPECTATION FAILED";
// private static final String HTTP_418 = "I'M A TEAPOT";
// private static final String HTTP_421 = "MISDIRECTED REQUEST";
// private static final String HTTP_422 = "UNPROCESSABLE ENTITY";
// private static final String HTTP_423 = "LOCKED";
// private static final String HTTP_424 = "FAILED DEPENDENCY";
// private static final String HTTP_425 = "TOO EARLY";
// private static final String HTTP_426 = "UPGRADE REQUIRED";
// private static final String HTTP_428 = "PRECONDITION REQUIRED";
// private static final String HTTP_429 = "TOO MANY REQUESTS";
// private static final String HTTP_431 = "REQUEST HEADER FIELDS TOO LARGE";
// private static final String HTTP_451 = "UNAVAILABLE FOR LEGAL REASONS";
//
// // 5xx Server Error
// private static final String HTTP_500 = "INTERNAL SERVER ERROR";
// private static final String HTTP_501 = "NOT IMPLEMENTED";
// private static final String HTTP_502 = "BAD GATEWAY";
// private static final String HTTP_503 = "SERVICE UNAVAILABLE";
// private static final String HTTP_504 = "GATEWAY TIMEOUT";
// private static final String HTTP_505 = "HTTP VERSION NOT SUPPORTED";
// private static final String HTTP_506 = "VARIANT ALSO NEGOTIATES";
// private static final String HTTP_507 = "INSUFFICIENT STORAGE";
// private static final String HTTP_508 = "LOOP DETECTED";
// private static final String HTTP_510 = "NOT EXTENDED";
// private static final String HTTP_511 = "NETWORK AUTHENTICATION REQUIRED";
//
// private static final Map<Integer, String> HTTP_STATUS_MAP;
// public static final Integer INTERNAL_SERVER_ERROR = null;
//
// static {
// Map<Integer, String> map = new LinkedHashMap<>();
//
// // 1xx Informational
// map.put(100, HTTP_100);
// map.put(101, HTTP_101);
// map.put(102, HTTP_102);
// map.put(103, HTTP_103);
//
// // 2xx Success
// map.put(200, HTTP_200);
// map.put(201, HTTP_201);
// map.put(202, HTTP_202);
// map.put(203, HTTP_203);
// map.put(204, HTTP_204);
// map.put(205, HTTP_205);
// map.put(206, HTTP_206);
// map.put(207, HTTP_207);
// map.put(208, HTTP_208);
// map.put(226, HTTP_226);
//
// // 3xx Redirection
// map.put(300, HTTP_300);
// map.put(301, HTTP_301);
// map.put(302, HTTP_302);
// map.put(303, HTTP_303);
// map.put(304, HTTP_304);
// map.put(305, HTTP_305);
// map.put(307, HTTP_307);
// map.put(308, HTTP_308);
//
// // 4xx Client Error
// map.put(400, HTTP_400);
// map.put(401, HTTP_401);
// map.put(402, HTTP_402);
// map.put(403, HTTP_403);
// map.put(404, HTTP_404);
// map.put(405, HTTP_405);
// map.put(406, HTTP_406);
// map.put(407, HTTP_407);
// map.put(408, HTTP_408);
// map.put(409, HTTP_409);
// map.put(410, HTTP_410);
// map.put(411, HTTP_411);
// map.put(412, HTTP_412);
// map.put(413, HTTP_413);
// map.put(414, HTTP_414);
// map.put(415, HTTP_415);
// map.put(416, HTTP_416);
// map.put(417, HTTP_417);
// map.put(418, HTTP_418);
// map.put(421, HTTP_421);
// map.put(422, HTTP_422);
// map.put(423, HTTP_423);
// map.put(424, HTTP_424);
// map.put(425, HTTP_425);
// map.put(426, HTTP_426);
// map.put(428, HTTP_428);
// map.put(429, HTTP_429);
// map.put(431, HTTP_431);
// map.put(451, HTTP_451);
//
// // 5xx Server Error
// map.put(500, HTTP_500);
// map.put(501, HTTP_501);
// map.put(502, HTTP_502);
// map.put(503, HTTP_503);
// map.put(504, HTTP_504);
// map.put(505, HTTP_505);
// map.put(506, HTTP_506);
// map.put(507, HTTP_507);
// map.put(508, HTTP_508);
// map.put(510, HTTP_510);
// map.put(511, HTTP_511);
//
// HTTP_STATUS_MAP = Collections.unmodifiableMap(map);
// }
//
// public static String getDescription(int statusCode) {
// return HTTP_STATUS_MAP.getOrDefault(statusCode, "UNKNOWN STATUS");
// }
//
// public static String getDescription(HttpStatus status) {
// return getDescription(status.value());
// }
// }
