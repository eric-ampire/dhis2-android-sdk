package org.hisp.dhis.android.testapp.maintenance;

import android.support.test.runner.AndroidJUnit4;

import org.hisp.dhis.android.core.maintenance.ForeignKeyViolation;
import org.hisp.dhis.android.testapp.arch.BasePublicAccessShould;
import org.junit.runner.RunWith;
import org.mockito.Mock;

@RunWith(AndroidJUnit4.class)
public class ForeignKeyViolationPublicAccessShould extends BasePublicAccessShould<ForeignKeyViolation> {

    @Mock
    private ForeignKeyViolation object;

    @Override
    public ForeignKeyViolation object() {
        return object;
    }

    @Override
    public void has_public_create_method() {
        ForeignKeyViolation.create(null);
    }

    @Override
    public void has_public_builder_method() {
        ForeignKeyViolation.builder();
    }

    @Override
    public void has_public_to_builder_method() {
        object().toBuilder();
    }
}