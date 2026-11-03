package com.foodies.freshmeal.authentication.email;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

/**
 * Generates the HTML email used to deliver email verification OTPs.
 *
 * @author FreshMeal Team
 */
@Component
public class EmailVerificationOtpTemplate {

    private static final String EMAIL_TITLE = "Verify Your Email - FreshMeal";

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

                                <!-- Verification Content -->
                                <tr>
                                    <td style="padding: 36px 32px 24px;">

                                        <div style="
                                            width: 64px;
                                            height: 64px;
                                            line-height: 64px;
                                            margin: 0 auto 22px;
                                            border-radius: 50%%;
                                            background-color: #fff1e5;
                                            color: #e87524;
                                            text-align: center;
                                            font-size: 30px;
                                            font-weight: bold;
                                        ">
                                            &#9993;
                                        </div>

                                        <h2 style="
                                            margin: 0 0 18px;
                                            color: #292929;
                                            font-size: 25px;
                                            text-align: center;
                                        ">
                                            Verify Your Email Address
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
                                            margin: 0 0 20px;
                                            color: #555555;
                                            font-size: 15px;
                                            line-height: 1.7;
                                        ">
                                            Welcome to FreshMeal! We're excited to have you
                                            join us.
                                            To complete your registration and activate your
                                            account, please verify your email address using
                                            the One-Time Password (OTP) below.
                                        </p>

                                        <!-- OTP Display -->
                                        <table role="presentation" width="100%%" cellspacing="0"
                                               cellpadding="0" border="0"
                                               style="
                                                   background-color: #fff8f1;
                                                   border: 1px solid #f5dfca;
                                                   border-radius: 10px;
                                                   margin-bottom: 20px;
                                               ">
                                            <tr>
                                                <td align="center" style="padding: 24px 16px;">

                                                    <p style="
                                                        margin: 0 0 12px;
                                                        color: #78695d;
                                                        font-size: 13px;
                                                        font-weight: 600;
                                                        letter-spacing: 1px;
                                                        text-transform: uppercase;
                                                    ">
                                                        Your Verification Code
                                                    </p>

                                                    <p style="
                                                        margin: 0;
                                                        color: #e87524;
                                                        font-size: 34px;
                                                        font-weight: 700;
                                                        letter-spacing: 9px;
                                                    ">
                                                        %s
                                                    </p>

                                                    <p style="
                                                        margin: 14px 0 0;
                                                        color: #777777;
                                                        font-size: 12px;
                                                    ">
                                                        Enter this code to verify your email
                                                    </p>

                                                </td>
                                            </tr>
                                        </table>

                                        <!-- OTP Expiry Notice -->
                                        <table role="presentation" width="100%%" cellspacing="0"
                                               cellpadding="0" border="0"
                                               style="
                                                   background-color: #fff8e8;
                                                   border-left: 4px solid #e8a23b;
                                                   margin-bottom: 22px;
                                               ">
                                            <tr>
                                                <td style="padding: 14px 16px;">
                                                    <p style="
                                                        margin: 0;
                                                        color: #795c27;
                                                        font-size: 13px;
                                                        line-height: 1.6;
                                                    ">
                                                        <strong>Important:</strong>
                                                        This verification code is valid for
                                                        <strong>10 minutes</strong>.
                                                        Please do not share it with anyone.
                                                    </p>
                                                </td>
                                            </tr>
                                        </table>

                                        <p style="
                                            margin: 0;
                                            color: #777777;
                                            font-size: 13px;
                                            line-height: 1.7;
                                        ">
                                            If you did not create a FreshMeal account,
                                            please ignore this email. No further action
                                            is required.
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
     * Builds the HTML email containing the email verification OTP.
     *
     * @param firstName recipient's first name
     * @param otp       email verification OTP
     * @return generated HTML email content
     */
    public String buildHtml(final String firstName, final String otp) {

        final String recipientName = StringUtils.hasText(firstName)
                ? firstName.trim()
                : DEFAULT_RECIPIENT_NAME;

        if (!StringUtils.hasText(otp)) {
            throw new IllegalArgumentException("Email verification OTP must not be blank.");
        }

        final String escapedRecipientName = HtmlUtils.htmlEscape(recipientName);
        final String escapedOtp = HtmlUtils.htmlEscape(otp.trim());

        return EMAIL_TEMPLATE.formatted(
                EMAIL_TITLE,
                escapedRecipientName,
                escapedOtp);
    }
}