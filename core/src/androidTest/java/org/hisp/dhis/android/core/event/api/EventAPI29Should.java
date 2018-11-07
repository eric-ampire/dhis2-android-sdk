package org.hisp.dhis.android.core.event.api;

import android.support.test.runner.AndroidJUnit4;

import org.hisp.dhis.android.core.data.server.RealServerMother;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EventAPI29Should extends EventAPIShould {

    public EventAPI29Should() {
        super(RealServerMother.url2_29, "CREATE_AND_UPDATE");
    }

    @Test
    public void stub() throws Exception {
    }
}