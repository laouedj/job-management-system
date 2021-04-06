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
    public void shouldCompleteJobSuccessfully() throws InterruptedException{

        JobManager jobManager = new DefaultJobManager();
        JobInputDataList jobInputDataList = new JobInputDataList();
        JobContext jobContext = jobManager.launch(jobInputDataList);
        jobContext.getDoneSignal().await();
        assertEquals(jobContext.getStatus(), JobState.SUCCESS);
        assertNotNull(jobContext.getStartTime());
        assertNotNull(jobContext.getEndTime());
    }
}
