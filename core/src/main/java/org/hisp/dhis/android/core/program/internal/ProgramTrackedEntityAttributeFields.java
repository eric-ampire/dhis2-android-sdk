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

package org.hisp.dhis.android.core.program.internal;

import org.hisp.dhis.android.core.arch.api.fields.internal.Fields;
import org.hisp.dhis.android.core.arch.fields.internal.FieldsHelper;
import org.hisp.dhis.android.core.common.ValueTypeRendering;
import org.hisp.dhis.android.core.program.ProgramTrackedEntityAttribute;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityAttribute;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityAttributeFields;

public final class ProgramTrackedEntityAttributeFields {

    public static final String MANDATORY = "mandatory";
    public static final String TRACKED_ENTITY_ATTRIBUTE = "trackedEntityAttribute";
    public static final String ALLOW_FUTURE_DATE = "allowFutureDate";
    public static final String DISPLAY_IN_LIST = "displayInList";
    public static final String PROGRAM = "program";
    public static final String SORT_ORDER = "sortOrder";
    public static final String SEARCHABLE = "searchable";
    private static final String RENDER_TYPE = "renderType";

    private static FieldsHelper<ProgramTrackedEntityAttribute> fh = new FieldsHelper<>();

    public static final Fields<ProgramTrackedEntityAttribute> allFields
            = Fields.<ProgramTrackedEntityAttribute>builder()
            .fields(fh.getNameableFields())
            .fields(
                    fh.<String>field(MANDATORY),
                    fh.nestedFieldWithUid(PROGRAM),
                    fh.<Boolean>field(ALLOW_FUTURE_DATE),
                    fh.<Boolean>field(DISPLAY_IN_LIST),
                    fh.<Integer>field(SORT_ORDER),
                    fh.<Boolean>field(SEARCHABLE),
                    fh.<TrackedEntityAttribute>nestedField(TRACKED_ENTITY_ATTRIBUTE)
                            .with(TrackedEntityAttributeFields.allFields),
                    fh.<ValueTypeRendering>field(RENDER_TYPE)
                    ).build();

    private ProgramTrackedEntityAttributeFields() {
    }
}