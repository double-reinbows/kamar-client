package com.martabak.kamar.util;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;

import java.nio.charset.StandardCharsets;

public class EmailSender {

    private static EmailSender instance;
    private AmazonSimpleEmailServiceClient sesClient;

    private EmailSender(Context c) {
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                c,
                Constants.AWS_SES_IDENTITY_POOL,
                Constants.AWS_SES_REGION
        );
        sesClient = new AmazonSimpleEmailServiceClient(credentialsProvider);
    }

    public static EmailSender getInstance(Context c) {
        if (instance == null) {
            instance = new EmailSender(c);
        }
        return instance;
    }

    /**
     * Send an email.
     * @param subject The text subject.
     * @param body The HTML body string.
     * @param addresses The addresses to send to.
     */
    public void sendEmail(String subject, String body, String... addresses) {
        SendEmailRequest request = new SendEmailRequest()
                .withSource(Constants.TEAM_EMAIL)
                .withDestination(new Destination().withToAddresses(addresses))
                .withMessage(new Message()
                        .withSubject(contentOf(subject))
                        .withBody(new Body().withHtml(contentOf(body))));
        new EmailSendTask().execute(request);
    }

    private Content contentOf(String content) {
        return new Content().withData(content).withCharset(StandardCharsets.UTF_8.name());
    }

    private class EmailSendTask extends AsyncTask<SendEmailRequest, Void, SendEmailResult> {

        protected SendEmailResult doInBackground(SendEmailRequest... requests) {
            SendEmailResult result = null;
            try {
                result = sesClient.sendEmail(requests[0]);
                Log.d(EmailSendTask.class.getCanonicalName(), "Sent message with ID: " + result.getMessageId());
            } catch (Exception e) {
                Log.e(EmailSendTask.class.getCanonicalName(), "Failed sending email", e);
            }
            return result;
        }

        protected void onPostExecute(SendEmailResult result) {}
    }

}
