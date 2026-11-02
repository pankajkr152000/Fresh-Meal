
package com.foodies.freshmeal.authentication.email;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

/**
 * ============================================================================
 * Component : PasswordResetEmailTemplate
 * ============================================================================
 *
 * Generates the HTML email content used during FreshMeal password recovery.
 *
 * Responsibilities:
 * - Generate a professional password-reset email.
 * - Personalize the email using the recipient's first name.
 * - Include the frontend password-reset URL.
 * - Escape dynamic content before inserting it into HTML.
 *
 * Security:
 * - Does not generate or persist password-reset tokens.
 * - Does not construct password-reset URLs.
 * - Does not log sensitive password-reset information.
 * - Escapes dynamic HTML content to prevent HTML injection.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Component
public class PasswordResetEmailTemplate {

    // =========================================================================
    // Email Content
    // =========================================================================

    private static final String EMAIL_TITLE = "Reset Your FreshMeal Password";

    private static final String DEFAULT_RECIPIENT_NAME = "there";

    private static final String EMAIL_TEMPLATE = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <meta name="color-scheme" content="light">
                <meta name="supported-color-schemes" content="light">
                <title>%s</title>
            </head>

            <body style="
                margin: 0;
                padding: 0;
                background-color: #fff8f3;
                font-family: Arial, Helvetica, sans-serif;
                color: #332b26;
            ">

                <table
                    role="presentation"
                    width="100%%"
                    cellspacing="0"
                    cellpadding="0"
                    border="0"
                    style="background-color: #fff8f3; padding: 35px 12px;">

                    <tr>
                        <td align="center">

                            <table
                                role="presentation"
                                width="100%%"
                                cellspacing="0"
                                cellpadding="0"
                                border="0"
                                style="
                                    max-width: 560px;
                                    background-color: #ffffff;
                                    border: 1px solid #f0e5dc;
                                    border-radius: 12px;
                                ">

                                <tr>
                                    <td style="
                                        padding: 30px 32px;
                                        text-align: center;
                                        background-color: #fff0e5;
                                        border-radius: 12px 12px 0 0;
                                    ">
                                        <h1 style="
                                            margin: 0;
                                            color: #e66a22;
                                            font-size: 26px;
                                            font-weight: 700;
                                        ">
                                            FreshMeal
                                        </h1>

                                        <p style="
                                            margin: 8px 0 0;
                                            color: #806d60;
                                            font-size: 12px;
                                            letter-spacing: 1px;
                                        ">
                                            FRESH FOOD. BETTER MOMENTS.
                                        </p>
                                    </td>
                                </tr>

                                <tr>
                                    <td style="padding: 34px 32px 30px;">

                                        <h2 style="
                                            margin: 0 0 18px;
                                            color: #332b26;
                                            font-size: 23px;
                                            line-height: 1.4;
                                        ">
                                            Reset your password
                                        </h2>

                                        <p style="
                                            margin: 0 0 16px;
                                            color: #5f554e;
                                            font-size: 14px;
                                            line-height: 1.7;
                                        ">
                                            Hello %s,
                                        </p>

                                        <p style="
                                            margin: 0 0 24px;
                                            color: #5f554e;
                                            font-size: 14px;
                                            line-height: 1.7;
                                        ">
                                            We received a request to reset the
                                            password associated with your
                                            FreshMeal account.
                                        </p>

                                        <table
                                            role="presentation"
                                            width="100%%"
                                            cellspacing="0"
                                            cellpadding="0"
                                            border="0">

                                            <tr>
                                                <td align="center" style="padding: 0 0 24px;">

                                                    <a href="%s"
                                                       target="_blank"
                                                       style="
                                                           display: inline-block;
                                                           padding: 14px 28px;
                                                           background-color: #e66a22;
                                                           color: #ffffff;
                                                           font-size: 14px;
                                                           font-weight: 700;
                                                           text-decoration: none;
                                                           border-radius: 7px;
                                                       ">
                                                        Reset My Password
                                                    </a>

                                                </td>
                                            </tr>

                                        </table>

                                        <p style="
                                            margin: 0 0 14px;
                                            color: #756b64;
                                            font-size: 13px;
                                            line-height: 1.7;
                                        ">
                                            This password-reset link expires
                                            in <strong>15 minutes</strong>
                                            and can only be used once.
                                        </p>

                                        <p style="
                                            margin: 0 0 8px;
                                            color: #756b64;
                                            font-size: 13px;
                                            line-height: 1.7;
                                        ">
                                            If the button does not work, copy
                                            and paste the following link into
                                            your browser:
                                        </p>

                                        <p style="
                                            margin: 0 0 24px;
                                            overflow-wrap: anywhere;
                                            word-break: break-word;
                                            font-size: 12px;
                                            line-height: 1.7;
                                        ">
                                            <a href="%s"
                                               target="_blank"
                                               style="color: #d65e1b;">
                                                %s
                                            </a>
                                        </p>

                                        <hr style="
                                            border: 0;
                                            border-top: 1px solid #eee6df;
                                            margin: 24px 0;
                                        ">

                                        <p style="
                                            margin: 0 0 12px;
                                            color: #756b64;
                                            font-size: 13px;
                                            line-height: 1.7;
                                        ">
                                            If you did not request a password
                                            reset, you can safely ignore this
                                            email. Your password will not be
                                            changed unless the reset process
                                            is successfully completed.
                                        </p>

                                        <p style="
                                            margin: 24px 0 0;
                                            color: #756b64;
                                            font-size: 13px;
                                            line-height: 1.7;
                                        ">
                                            Regards,<br>
                                            <strong>FreshMeal Team</strong>
                                        </p>

                                    </td>
                                </tr>

                                <tr>
                                    <td style="
                                        padding: 18px 24px;
                                        text-align: center;
                                        background-color: #faf7f4;
                                        border-radius: 0 0 12px 12px;
                                    ">
                                        <p style="
                                            margin: 0;
                                            color: #95877e;
                                            font-size: 11px;
                                        ">
                                            This is an automated email.
                                            Please do not reply.
                                        </p>
                                    </td>
                                </tr>

                            </table>

                        </td>
                    </tr>
                </table>

            </body>
            </html>
            """;

    // =========================================================================
    // Template Generation
    // =========================================================================

    /**
     * Builds the HTML password-reset email.
     *
     * @param firstName recipient's first name.
     * @param resetUrl  frontend password-reset URL.
     * @return generated HTML email content.
     *
     * @throws IllegalArgumentException when the reset URL is missing.
     */
    public String buildHtml(final String firstName, final String resetUrl) {

        if (!StringUtils.hasText(resetUrl)) {
            throw new IllegalArgumentException(
                    "Password reset URL must not be empty.");
        }

        final String recipientName = StringUtils.hasText(firstName)
                ? firstName.trim()
                : DEFAULT_RECIPIENT_NAME;

        final String escapedRecipientName = HtmlUtils.htmlEscape(recipientName);

        final String escapedResetUrl = HtmlUtils.htmlEscape(resetUrl);

        return EMAIL_TEMPLATE.formatted(
                EMAIL_TITLE,
                escapedRecipientName,
                escapedResetUrl,
                escapedResetUrl,
                escapedResetUrl);
    }
}
