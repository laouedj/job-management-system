package org.prototype.study.job.samples.emailsending;

import org.prototype.study.job.AbstractJob;
import org.prototype.study.job.parameters.JobInputParameters;

import java.util.Scanner;

public class SendingMailsJob extends AbstractJob {

    private MailSender mailSender;


    public SendingMailsJob(JobInputParameters jobInputParameters) {
        super(jobInputParameters);
        this.mailSender = new DummyMailSender();
    }

    @Override
    protected void doExecute() {

        String filePath = (String) getJobExecutionContext().getJobInputParameters().getJobInputParameter("file.path");
        System.out.println("Scanning file ... " + filePath);

        Scanner fileScanner = new Scanner(SendingMailsJob.class.getResourceAsStream(filePath), "UTF-8");

        try {
            while (fileScanner.hasNextLine()) {

                String line = fileScanner.nextLine();

                if ((line != null) && !line.isEmpty()) {
                    String [] parameters = line.split(";");
                    String destination = parameters[0];
                    String message = parameters[1];
                    this.mailSender.send(destination,message);
                }
            }
        } finally {
            fileScanner.close();
        }
    }
}
