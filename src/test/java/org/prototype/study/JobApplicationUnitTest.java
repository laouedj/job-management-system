package org.prototype.study;


import org.junit.jupiter.api.Test;
import org.prototype.study.job.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class JobApplicationUnitTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void shouldCompleteJobSuccessfully() {

        JobManager jobManager = new DefaultJobManager();
        JobInputDataList jobInputDataList = new JobInputDataList();
        Job job = jobManager.create(jobInputDataList);
        job.execute();
        JobContext jobContext = job.getJobExecutionContext();
        assertEquals(jobContext.getStatus(), JobState.SUCCESS);
        assertNotNull(jobContext.getStartTime());
        assertNotNull(jobContext.getEndTime());
    }
}
