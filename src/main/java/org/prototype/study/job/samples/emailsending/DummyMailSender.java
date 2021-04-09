package org.prototype.study.job.samples.emailsending;

public class DummyMailSender implements MailSender {

    @Override
    public void send(String destination, String message) {
        System.out.println("Sending email " + message + " to " + destination );
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
