/*
 * Copyright (c) 2017, University of Oslo
 *
 * All rights reserved.
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

package org.hisp.dhis.android.core.relationship;

import org.hisp.dhis.android.core.arch.api.executors.APICallExecutor;
import org.hisp.dhis.android.core.arch.handlers.SyncHandler;
import org.hisp.dhis.android.core.calls.factories.ListCallFactoryImpl;
import org.hisp.dhis.android.core.calls.fetchers.CallFetcher;
import org.hisp.dhis.android.core.calls.fetchers.PayloadResourceCallFetcher;
import org.hisp.dhis.android.core.calls.processors.CallProcessor;
import org.hisp.dhis.android.core.calls.processors.TransactionalResourceSyncCallProcessor;
import org.hisp.dhis.android.core.common.GenericCallData;
import org.hisp.dhis.android.core.common.Payload;
import org.hisp.dhis.android.core.resource.Resource;

import javax.inject.Inject;

import dagger.Reusable;

@Reusable
final class RelationshipTypeEndpointCallFactory extends ListCallFactoryImpl<RelationshipType> {

    private final Resource.Type resourceType = Resource.Type.RELATIONSHIP_TYPE;

    private final RelationshipTypeService service;
    private final SyncHandler<RelationshipType> handler;

    @Inject
    RelationshipTypeEndpointCallFactory(GenericCallData data,
                                        APICallExecutor apiCallExecutor,
                                        RelationshipTypeService service,
                                        SyncHandler<RelationshipType> handler) {
        super(data, apiCallExecutor);
        this.service = service;
        this.handler = handler;
    }

    @Override
    protected CallFetcher<RelationshipType> fetcher() {

        return new PayloadResourceCallFetcher<RelationshipType>(data.resourceHandler(), resourceType,
                apiCallExecutor) {
            @Override
            protected retrofit2.Call<Payload<RelationshipType>> getCall(String lastUpdated) {
                return service.getRelationshipTypes(RelationshipTypeFields.allFields,
                        RelationshipTypeFields.lastUpdated.gt(lastUpdated), Boolean.FALSE);
            }
        };
    }

    @Override
    protected CallProcessor<RelationshipType> processor() {
        return new TransactionalResourceSyncCallProcessor<>(
                data,
                handler,
                resourceType
        );
    }
}