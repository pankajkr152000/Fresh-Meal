
package com.foodies.freshmeal.authentication.email;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

/**
 * ============================================================================
 * Component : PasswordResetSuccessEmailTemplate
 * ============================================================================
 *
 * Generates the HTML email sent after a user's password has been successfully
 * reset.
 *
 * Responsibilities:
 * - Generate a professional password-reset confirmation email.
 * - Personalize the email using the recipient's first name.
 * - Confirm successful password modification.
 * - Provide security guidance if the user did not initiate the change.
 * - Escape dynamic content before inserting it into HTML.
 *
 * Security:
 * - Does not contain passwords or password-reset tokens.
 * - Does not perform password-reset operations.
 * - Does not log sensitive authentication information.
 * - Escapes dynamic HTML content to prevent HTML injection.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Component
public class PasswordResetSuccessEmailTemplate {

    // =========================================================================
    // Email Content
    // =========================================================================

    private static final String EMAIL_TITLE = "Your FreshMeal Password Has Been Reset";

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

                                <!-- Brand Header -->

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

                                <!-- Email Body -->

                                <tr>
                                    <td style="padding: 34px 32px 30px;">

                                        <!-- Success Icon -->

                                        <table
                                            role="presentation"
                                            width="100%%"
                                            cellspacing="0"
                                            cellpadding="0"
                                            border="0">

                                            <tr>
                                                <td align="center" style="padding-bottom: 20px;">

                                                    <table
                                                        role="presentation"
                                                        cellspacing="0"
                                                        cellpadding="0"
                                                        border="0"
                                                        style="
                                                            width: 64px;
                                                            height: 64px;
                                                            background-color: #e6f4eb;
                                                            border-radius: 50%%;
                                                        ">

                                                        <tr>
                                                            <td align="center" valign="middle"
                                                                style="
                                                                    color: #198754;
                                                                    font-size: 32px;
                                                                    font-weight: 700;
                                                                ">
                                                                &#10003;
                                                            </td>
                                                        </tr>

                                                    </table>

                                                </td>
                                            </tr>

                                        </table>

                                        <h2 style="
                                            margin: 0 0 18px;
                                            color: #332b26;
                                            font-size: 23px;
                                            line-height: 1.4;
                                            text-align: center;
                                        ">
                                            Password Reset Successful!
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
                                            margin: 0 0 22px;
                                            color: #5f554e;
                                            font-size: 14px;
                                            line-height: 1.7;
                                        ">
                                            This email confirms that the password
                                            associated with your FreshMeal account
                                            has been successfully changed.
                                        </p>

                                        <!-- Confirmation Message -->

                                        <table
                                            role="presentation"
                                            width="100%%"
                                            cellspacing="0"
                                            cellpadding="0"
                                            border="0"
                                            style="
                                                background-color: #f0f8f3;
                                                border: 1px solid #dceee2;
                                                border-radius: 8px;
                                            ">

                                            <tr>
                                                <td style="padding: 18px 20px;">

                                                    <p style="
                                                        margin: 0 0 8px;
                                                        color: #198754;
                                                        font-size: 13px;
                                                        font-weight: 700;
                                                    ">
                                                        <span>&#10003;</span>
                                                        Password successfully updated
                                                    </p>

                                                    <p style="
                                                        margin: 0;
                                                        color: #52665a;
                                                        font-size: 13px;
                                                        line-height: 1.7;
                                                    ">
                                                        Your account is now protected
                                                        with your new password.
                                                        You can use it the next time
                                                        you sign in to FreshMeal.
                                                    </p>

                                                </td>
                                            </tr>

                                        </table>

                                        <!-- Security Notice -->

                                        <p style="
                                            margin: 24px 0 12px;
                                            color: #5f554e;
                                            font-size: 14px;
                                            font-weight: 700;
                                        ">
                                            Didn't make this change?
                                        </p>

                                        <p style="
                                            margin: 0 0 18px;
                                            color: #756b64;
                                            font-size: 13px;
                                            line-height: 1.7;
                                        ">
                                            If you did not reset your password,
                                            your account may be at risk.
                                            Please contact the FreshMeal support
                                            team immediately so that we can help
                                            you secure your account.
                                        </p>

                                        <hr style="
                                            border: 0;
                                            border-top: 1px solid #eee6df;
                                            margin: 24px 0;
                                        ">

                                        <p style="
                                            margin: 0;
                                            color: #756b64;
                                            font-size: 13px;
                                            line-height: 1.7;
                                        ">
                                            Thank you for being a part of FreshMeal.
                                            We are committed to keeping your
                                            account safe and secure.
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

                                <!-- Footer -->

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
     * Builds the HTML password-reset success email.
     *
     * @param firstName recipient's first name.
     * @return generated HTML email content.
     */
    public String buildHtml(final String firstName) {

        final String recipientName = StringUtils.hasText(firstName)
                ? firstName.trim()
                : DEFAULT_RECIPIENT_NAME;

        final String escapedRecipientName = HtmlUtils.htmlEscape(recipientName);

        return EMAIL_TEMPLATE.formatted(
                EMAIL_TITLE,
                escapedRecipientName);
    }
}
