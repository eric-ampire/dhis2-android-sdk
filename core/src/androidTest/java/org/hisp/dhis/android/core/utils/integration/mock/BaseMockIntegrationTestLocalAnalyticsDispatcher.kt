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
package org.hisp.dhis.android.core.utils.integration.mock

import org.hisp.dhis.android.core.D2DIComponentAccessor
import org.hisp.dhis.android.core.data.server.RealServerMother
import org.hisp.dhis.android.core.organisationunit.OrganisationUnit
import org.hisp.dhis.android.localanalytics.LocalAnalyticsData
import org.junit.BeforeClass

abstract class BaseMockIntegrationTestLocalAnalyticsDispatcher : BaseMockIntegrationTest() {

    companion object BaseMockIntegrationTestLocalAnalyticsDispatcher {
        @BeforeClass
        @Throws(Exception::class)
        @JvmStatic
        fun setUpClass() {
            val isNewInstance = setUpClass(MockIntegrationTestDatabaseContent.LocalAnalyticsDispatcher)
            if (isNewInstance) {
                objects.dhis2MockServer.setRequestDispatcher()
                objects.d2.userModule().blockingLogIn(RealServerMother.username, RealServerMother.password,
                        objects.dhis2MockServer.baseEndpoint)
            }

            val d2DIComponent = D2DIComponentAccessor.getD2DIComponent(objects.d2)
            val handler = d2DIComponent.internalModules().organisationUnit.organisationUnitHandler
            handler.setData(d2.userModule().user().blockingGet(), OrganisationUnit.Scope.SCOPE_DATA_CAPTURE)
            handler.handle(LocalAnalyticsData.getOrganisationUnit())
        }
    }
}