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

package org.hisp.dhis.android.core.legendset;

import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import org.hisp.dhis.android.core.data.api.Field;
import org.hisp.dhis.android.core.data.api.Fields;

import java.util.Date;

@AutoValue
public abstract class Legend {
    private final static String UID = "id";
    private final static String NAME = "name";
    private final static String DISPLAY_NAME = "displayName";
    private final static String CREATED = "created";
    private final static String LAST_UPDATED = "lastUpdated";
    private final static String START_VALUE = "startValue";
    private final static String END_VALUE = "endValue";
    private final static String COLOR = "color";

    private static final Field<Legend, String> uid = Field.create(UID);
    private static final Field<Legend, String> name = Field.create(NAME);
    private static final Field<Legend, String> displayName = Field.create(DISPLAY_NAME);
    private static final Field<Legend, String> created = Field.create(CREATED);
    private static final Field<Legend, String> lastUpdated = Field.create(LAST_UPDATED);
    private static final Field<Legend, Double> startValue = Field.create(START_VALUE);
    private static final Field<Legend, Double> endValue = Field.create(END_VALUE);
    private static final Field<Legend, String> color = Field.create(COLOR);

    static final Fields<Legend> allFields = Fields.<Legend>builder().fields(
            uid, name, displayName, created, lastUpdated, startValue, endValue, color).build();

    @JsonProperty(UID)
    public abstract String uid();

    @Nullable
    @JsonProperty(NAME)
    public abstract String name();

    @Nullable
    @JsonProperty(DISPLAY_NAME)
    public abstract String displayName();

    @Nullable
    @JsonProperty(CREATED)
    public abstract Date created();

    @Nullable
    @JsonProperty(LAST_UPDATED)
    public abstract Date lastUpdated();

    @Nullable
    @JsonProperty(START_VALUE)
    abstract Double startValue();

    @Nullable
    @JsonProperty(END_VALUE)
    abstract Double endValue();

    @Nullable
    @JsonProperty(COLOR)
    abstract String color();

    @JsonCreator
    static Legend create(
            @JsonProperty(UID) String uid,
            @JsonProperty(NAME) String name,
            @JsonProperty(DISPLAY_NAME) String displayName,
            @JsonProperty(CREATED) Date created,
            @JsonProperty(LAST_UPDATED) Date lastUpdated,
            @JsonProperty(START_VALUE) Double startValue,
            @JsonProperty(END_VALUE) Double endValue,
            @JsonProperty(COLOR) String color) {

        return new AutoValue_Legend(uid, name, displayName, created, lastUpdated, startValue, endValue, color);
    }
}
