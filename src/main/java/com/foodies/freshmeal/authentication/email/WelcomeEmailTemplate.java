package com.foodies.freshmeal.authentication.email;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

/**
 * Generates the HTML welcome email sent after successful email verification.
 *
 * @author FreshMeal Team
 */
@Component
public class WelcomeEmailTemplate {

    private static final String EMAIL_TITLE = "Welcome to FreshMeal!";

    private static final String DEFAULT_RECIPIENT_NAME = "there";

    private static final String EMAIL_TEMPLATE = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <meta name="color-scheme" content="light">
                <title>%s</title>
            </head>

            <body style="
                margin: 0;
                padding: 0;
                background-color: #f8f7f3;
                font-family: Arial, Helvetica, sans-serif;
                color: #292929;
            ">

                <table role="presentation" width="100%%" cellspacing="0" cellpadding="0"
                       border="0" style="background-color: #f8f7f3; padding: 35px 12px;">
                    <tr>
                        <td align="center">

                            <table role="presentation" width="100%%" cellspacing="0"
                                   cellpadding="0" border="0"
                                   style="
                                       max-width: 600px;
                                       background-color: #ffffff;
                                       border-radius: 12px;
                                       overflow: hidden;
                                   ">

                                <!-- Brand Header -->
                                <tr>
                                    <td align="center" style="
                                        background-color: #fef3e7;
                                        padding: 32px 24px;
                                    ">
                                        <h1 style="
                                            margin: 0;
                                            color: #e87524;
                                            font-size: 30px;
                                            font-weight: 700;
                                            letter-spacing: -0.5px;
                                        ">
                                            FreshMeal
                                        </h1>

                                        <p style="
                                            margin: 8px 0 0;
                                            color: #6b6259;
                                            font-size: 14px;
                                            letter-spacing: 1px;
                                        ">
                                            Fresh Food. Better Moments.
                                        </p>
                                    </td>
                                </tr>

                                <!-- Welcome Content -->
                                <tr>
                                    <td style="padding: 36px 32px 24px;">

                                        <div style="
                                            width: 64px;
                                            height: 64px;
                                            line-height: 64px;
                                            margin: 0 auto 22px;
                                            border-radius: 50%%;
                                            background-color: #e8f5e9;
                                            color: #2e9d50;
                                            text-align: center;
                                            font-size: 32px;
                                            font-weight: bold;
                                        ">
                                            &#10003;
                                        </div>

                                        <h2 style="
                                            margin: 0 0 18px;
                                            color: #292929;
                                            font-size: 25px;
                                            text-align: center;
                                        ">
                                            Welcome to FreshMeal!
                                        </h2>

                                        <p style="
                                            margin: 0 0 18px;
                                            color: #444444;
                                            font-size: 15px;
                                            line-height: 1.7;
                                        ">
                                            Hello %s,
                                        </p>

                                        <p style="
                                            margin: 0 0 16px;
                                            color: #555555;
                                            font-size: 15px;
                                            line-height: 1.7;
                                        ">
                                            We're delighted to welcome you to FreshMeal!
                                            Your email address has been successfully verified,
                                            and your account is now active.
                                        </p>

                                        <p style="
                                            margin: 0 0 22px;
                                            color: #555555;
                                            font-size: 15px;
                                            line-height: 1.7;
                                        ">
                                            You're all set to explore delicious food, discover
                                            new favourites, and enjoy a convenient food ordering
                                            experience.
                                        </p>

                                        <!-- Account Confirmation -->
                                        <table role="presentation" width="100%%" cellspacing="0"
                                               cellpadding="0" border="0"
                                               style="
                                                   background-color: #f0f8f1;
                                                   border: 1px solid #d9eddb;
                                                   border-radius: 8px;
                                                   margin-bottom: 24px;
                                               ">
                                            <tr>
                                                <td style="padding: 18px 20px;">
                                                    <p style="
                                                        margin: 0;
                                                        color: #287d3c;
                                                        font-size: 14px;
                                                        font-weight: 700;
                                                    ">
                                                        &#10003; Your account is ready!
                                                    </p>

                                                    <p style="
                                                        margin: 7px 0 0;
                                                        color: #4d6651;
                                                        font-size: 13px;
                                                        line-height: 1.6;
                                                    ">
                                                        You can now log in to FreshMeal and
                                                        start exploring.
                                                    </p>
                                                </td>
                                            </tr>
                                        </table>

                                        <p style="
                                            margin: 0;
                                            color: #555555;
                                            font-size: 15px;
                                            line-height: 1.7;
                                        ">
                                            Thank you for choosing FreshMeal. We look forward
                                            to being a part of your better food moments!
                                        </p>

                                    </td>
                                </tr>

                                <!-- Brand Footer -->
                                <tr>
                                    <td align="center" style="
                                        background-color: #faf9f6;
                                        padding: 24px 20px;
                                        border-top: 1px solid #eeeeee;
                                    ">
                                        <p style="
                                            margin: 0 0 8px;
                                            color: #e87524;
                                            font-size: 15px;
                                            font-weight: 700;
                                        ">
                                            Fresh Food. Better Moments.
                                        </p>

                                        <p style="
                                            margin: 0 0 12px;
                                            color: #777777;
                                            font-size: 13px;
                                        ">
                                            Regards,<br>
                                            FreshMeal Team
                                        </p>

                                        <p style="
                                            margin: 0;
                                            color: #999999;
                                            font-size: 11px;
                                            line-height: 1.6;
                                        ">
                                            This is an automated email. Please do not reply
                                            to this message.
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

    /**
     * Builds the HTML welcome email for a successfully verified user.
     *
     * @param firstName recipient's first name
     * @return generated HTML email content
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