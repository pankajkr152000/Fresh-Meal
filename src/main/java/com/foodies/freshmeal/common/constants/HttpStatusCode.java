package com.foodies.freshmeal.common.constants;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

// @SuppressWarnings("checkstyle:ConstantName")
// @SuppressWarnings("java:S00115")
public final class HttpStatusCode {

    private HttpStatusCode() {
        throw new UnsupportedOperationException("Utility class");
    }
   // 1xx Informational
private static final String HTTP_100 = "CONTINUE";
private static final String HTTP_101 = "SWITCHING PROTOCOLS";
private static final String HTTP_102 = "PROCESSING";
private static final String HTTP_103 = "EARLY HINTS";

// 2xx Success
private static final String HTTP_200 = "OK";
private static final String HTTP_201 = "CREATED";
private static final String HTTP_202 = "ACCEPTED";
private static final String HTTP_203 = "NON-AUTHORITATIVE INFORMATION";
private static final String HTTP_204 = "NO CONTENT";
private static final String HTTP_205 = "RESET CONTENT";
private static final String HTTP_206 = "PARTIAL CONTENT";
private static final String HTTP_207 = "MULTI-STATUS";
private static final String HTTP_208 = "ALREADY REPORTED";
private static final String HTTP_226 = "IM USED";

// 3xx Redirection
private static final String HTTP_300 = "MULTIPLE CHOICES";
private static final String HTTP_301 = "MOVED PERMANENTLY";
private static final String HTTP_302 = "FOUND";
private static final String HTTP_303 = "SEE OTHER";
private static final String HTTP_304 = "NOT MODIFIED";
private static final String HTTP_305 = "USE PROXY";
private static final String HTTP_307 = "TEMPORARY REDIRECT";
private static final String HTTP_308 = "PERMANENT REDIRECT";

// 4xx Client Error
private static final String HTTP_400 = "BAD REQUEST";
private static final String HTTP_401 = "UNAUTHORIZED";
private static final String HTTP_402 = "PAYMENT REQUIRED";
private static final String HTTP_403 = "FORBIDDEN";
private static final String HTTP_404 = "NOT FOUND";
private static final String HTTP_405 = "METHOD NOT ALLOWED";
private static final String HTTP_406 = "NOT ACCEPTABLE";
private static final String HTTP_407 = "PROXY AUTHENTICATION REQUIRED";
private static final String HTTP_408 = "REQUEST TIMEOUT";
private static final String HTTP_409 = "CONFLICT";
private static final String HTTP_410 = "GONE";
private static final String HTTP_411 = "LENGTH REQUIRED";
private static final String HTTP_412 = "PRECONDITION FAILED";
private static final String HTTP_413 = "PAYLOAD TOO LARGE";
private static final String HTTP_414 = "URI TOO LONG";
private static final String HTTP_415 = "UNSUPPORTED MEDIA TYPE";
private static final String HTTP_416 = "RANGE NOT SATISFIABLE";
private static final String HTTP_417 = "EXPECTATION FAILED";
private static final String HTTP_418 = "I'M A TEAPOT";
private static final String HTTP_421 = "MISDIRECTED REQUEST";
private static final String HTTP_422 = "UNPROCESSABLE ENTITY";
private static final String HTTP_423 = "LOCKED";
private static final String HTTP_424 = "FAILED DEPENDENCY";
private static final String HTTP_425 = "TOO EARLY";
private static final String HTTP_426 = "UPGRADE REQUIRED";
private static final String HTTP_428 = "PRECONDITION REQUIRED";
private static final String HTTP_429 = "TOO MANY REQUESTS";
private static final String HTTP_431 = "REQUEST HEADER FIELDS TOO LARGE";
private static final String HTTP_451 = "UNAVAILABLE FOR LEGAL REASONS";

// 5xx Server Error
private static final String HTTP_500 = "INTERNAL SERVER ERROR";
private static final String HTTP_501 = "NOT IMPLEMENTED";
private static final String HTTP_502 = "BAD GATEWAY";
private static final String HTTP_503 = "SERVICE UNAVAILABLE";
private static final String HTTP_504 = "GATEWAY TIMEOUT";
private static final String HTTP_505 = "HTTP VERSION NOT SUPPORTED";
private static final String HTTP_506 = "VARIANT ALSO NEGOTIATES";
private static final String HTTP_507 = "INSUFFICIENT STORAGE";
private static final String HTTP_508 = "LOOP DETECTED";
private static final String HTTP_510 = "NOT EXTENDED";
private static final String HTTP_511 = "NETWORK AUTHENTICATION REQUIRED";

    
    private static final Map<Integer, String> HTTP_STATUS_MAP;

    static {
        Map<Integer, String> map = new LinkedHashMap<>();

       // 1xx Informational
        map.put(100, HTTP_100);
        map.put(101, HTTP_101);
        map.put(102, HTTP_102);
        map.put(103, HTTP_103);

        // 2xx Success
        map.put(200, HTTP_200);
        map.put(201, HTTP_201);
        map.put(202, HTTP_202);
        map.put(203, HTTP_203);
        map.put(204, HTTP_204);
        map.put(205, HTTP_205);
        map.put(206, HTTP_206);
        map.put(207, HTTP_207);
        map.put(208, HTTP_208);
        map.put(226, HTTP_226);

        // 3xx Redirection
        map.put(300, HTTP_300);
        map.put(301, HTTP_301);
        map.put(302, HTTP_302);
        map.put(303, HTTP_303);
        map.put(304, HTTP_304);
        map.put(305, HTTP_305);
        map.put(307, HTTP_307);
        map.put(308, HTTP_308);

        // 4xx Client Error
        map.put(400, HTTP_400);
        map.put(401, HTTP_401);
        map.put(402, HTTP_402);
        map.put(403, HTTP_403);
        map.put(404, HTTP_404);
        map.put(405, HTTP_405);
        map.put(406, HTTP_406);
        map.put(407, HTTP_407);
        map.put(408, HTTP_408);
        map.put(409, HTTP_409);
        map.put(410, HTTP_410);
        map.put(411, HTTP_411);
        map.put(412, HTTP_412);
        map.put(413, HTTP_413);
        map.put(414, HTTP_414);
        map.put(415, HTTP_415);
        map.put(416, HTTP_416);
        map.put(417, HTTP_417);
        map.put(418, HTTP_418);
        map.put(421, HTTP_421);
        map.put(422, HTTP_422);
        map.put(423, HTTP_423);
        map.put(424, HTTP_424);
        map.put(425, HTTP_425);
        map.put(426, HTTP_426);
        map.put(428, HTTP_428);
        map.put(429, HTTP_429);
        map.put(431, HTTP_431);
        map.put(451, HTTP_451);

        // 5xx Server Error
        map.put(500, HTTP_500);
        map.put(501, HTTP_501);
        map.put(502, HTTP_502);
        map.put(503, HTTP_503);
        map.put(504, HTTP_504);
        map.put(505, HTTP_505);
        map.put(506, HTTP_506);
        map.put(507, HTTP_507);
        map.put(508, HTTP_508);
        map.put(510, HTTP_510);
        map.put(511, HTTP_511);

        HTTP_STATUS_MAP = Collections.unmodifiableMap(map);
    }

    public static String getDescription(int statusCode) {
        return HTTP_STATUS_MAP.getOrDefault(statusCode, "UNKNOWN STATUS");
    }
}