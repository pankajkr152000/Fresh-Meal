package com.foodies.freshmeal.common.constants;

/**
 * ============================================================================
 * Class : ContentTypeConstants
 * ============================================================================
 *
 * Centralized MIME(Content-Type) constants used throughout the FreshMeal
 * application.
 *
 * <p>
 * This class eliminates hardcoded MIME types and ensures consistency across
 * controllers, REST clients, file uploads, downloads and integrations.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 1.0
 *        ============================================================================
 */
public final class ContentTypeConstants {

    /**
     * Private constructor.
     */
    private ContentTypeConstants() {
        throw new IllegalStateException("Utility class");
    }

    // =========================================================
    // Application Types
    // =========================================================

    public static final String APPLICATION_JSON = "application/json";

    public static final String APPLICATION_XML = "application/xml";

    public static final String APPLICATION_PDF = "application/pdf";

    public static final String APPLICATION_ZIP = "application/zip";

    public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";

    // =========================================================
    // Multipart
    // =========================================================

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    public static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";

    // =========================================================
    // Text Types
    // =========================================================

    public static final String TEXT_PLAIN = "text/plain";

    public static final String TEXT_HTML = "text/html";

    public static final String TEXT_CSV = "text/csv";

    // =========================================================
    // Image Types
    // =========================================================

    public static final String IMAGE_JPEG = "image/jpeg";

    public static final String IMAGE_JPG = "image/jpg";

    public static final String IMAGE_PNG = "image/png";

    public static final String IMAGE_GIF = "image/gif";

    public static final String IMAGE_WEBP = "image/webp";

    public static final String IMAGE_SVG = "image/svg+xml";

    // =========================================================
    // Microsoft Office
    // =========================================================

    public static final String APPLICATION_EXCEL = "application/vnd.ms-excel";

    public static final String APPLICATION_EXCEL_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static final String APPLICATION_WORD = "application/msword";

    public static final String APPLICATION_WORD_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    public static final String APPLICATION_POWERPOINT = "application/vnd.ms-powerpoint";

    public static final String APPLICATION_POWERPOINT_PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation";

    // =========================================================
    // Archives
    // =========================================================

    public static final String APPLICATION_RAR = "application/vnd.rar";

    public static final String APPLICATION_7Z = "application/x-7z-compressed";

}
