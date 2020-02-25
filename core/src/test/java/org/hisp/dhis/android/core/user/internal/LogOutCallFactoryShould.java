/*
 * Copyright (c) 2004-2019, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.hisp.dhis.android.core.user.internal;

import org.hisp.dhis.android.core.arch.db.access.DatabaseAdapter;
import org.hisp.dhis.android.core.arch.storage.internal.Credentials;
import org.hisp.dhis.android.core.arch.storage.internal.ObjectSecureStore;
import org.hisp.dhis.android.core.maintenance.D2Error;
import org.hisp.dhis.android.core.maintenance.D2ErrorCode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Completable;
import io.reactivex.observers.TestObserver;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class LogOutCallFactoryShould {

    @Mock
    private ObjectSecureStore<Credentials> credentialsSecureStore;

    @Mock
    private Credentials credentials;

    @Mock
    private DatabaseAdapter databaseAdapter;

    private Completable logOutCompletable;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(credentials.username()).thenReturn("user");
        when(credentials.password()).thenReturn("password");

        logOutCompletable = new LogOutCallFactory(databaseAdapter, credentialsSecureStore).logOut();
    }

    @Test
    public void clear_user_credentials() {
        when(credentialsSecureStore.get()).thenReturn(credentials);

        logOutCompletable.blockingAwait();

        verify(credentialsSecureStore, times(1)).remove();
    }

    @Test
    public void throw_d2_exception_if_no_authenticated_user() {
        when(credentialsSecureStore.get()).thenReturn(null);

        TestObserver<Void> testObserver = logOutCompletable.test();
        testObserver.awaitTerminalEvent();

        D2Error d2Error = (D2Error) testObserver.errors().get(0);
        assertThat(d2Error.errorCode()).isEqualTo(D2ErrorCode.NO_AUTHENTICATED_USER);

        testObserver.dispose();
    }
}