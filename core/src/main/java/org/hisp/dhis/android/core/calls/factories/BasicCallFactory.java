package org.hisp.dhis.android.core.calls.factories;

import org.hisp.dhis.android.core.calls.Call;
import org.hisp.dhis.android.core.arch.db.implementations.DatabaseAdapter;

import retrofit2.Retrofit;

public interface BasicCallFactory<T> {
    Call<T> create(DatabaseAdapter databaseAdapter, Retrofit retrofit);
}
