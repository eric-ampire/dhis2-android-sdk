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

package org.hisp.dhis.android.core.systeminfo;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class SMSVersionShould {

    @Test
    public void return_sms_version_if_patch_version_exists() {
        SMSVersion smsVersion = SMSVersion.getValue("2.33.2");
        assertThat(smsVersion).isEqualByComparingTo(SMSVersion.V1);
    }

    @Test
    public void return_latest_sms_version_if_patch_does_not_exist() {
        SMSVersion smsVersion = SMSVersion.getValue("2.33.100");
        assertThat(smsVersion).isEqualByComparingTo(SMSVersion.V2);
    }

    @Test
    public void return_null_if_patch_version_has_no_support() {
        SMSVersion smsVersion = SMSVersion.getValue("2.32.1");
        assertThat(smsVersion).isNull();
    }

    @Test
    public void return_null_if_patch_does_not_exist() {
        SMSVersion smsVersion = SMSVersion.getValue("2.32.100");
        assertThat(smsVersion).isNull();
    }
}