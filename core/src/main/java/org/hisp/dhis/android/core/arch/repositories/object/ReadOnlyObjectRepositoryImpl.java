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
package org.hisp.dhis.android.core.arch.repositories.object;

import org.hisp.dhis.android.core.arch.repositories.children.ChildrenAppender;
import org.hisp.dhis.android.core.arch.repositories.children.ChildrenAppenderExecutor;
import org.hisp.dhis.android.core.arch.repositories.scope.RepositoryScope;
import org.hisp.dhis.android.core.arch.repositories.scope.RepositoryScopeHelper;
import org.hisp.dhis.android.core.common.Model;

import java.util.Collection;

public abstract class ReadOnlyObjectRepositoryImpl<M extends Model, R extends ReadOnlyObjectRepository<M>>
        implements ReadOnlyObjectRepository<M> {

    private final Collection<ChildrenAppender<M>> childrenAppenders;
    final RepositoryScope scope;
    private final ObjectRepositoryFactory<R> repositoryFactory;

    ReadOnlyObjectRepositoryImpl(Collection<ChildrenAppender<M>> childrenAppenders,
                                 RepositoryScope scope,
                                 ObjectRepositoryFactory<R> repositoryFactory) {
        this.childrenAppenders = childrenAppenders;
        this.scope = scope;
        this.repositoryFactory = repositoryFactory;
    }

    private M getWithAllChildren() {
        return ChildrenAppenderExecutor.appendInObject(getWithoutChildren(), childrenAppenders);
    }

    abstract M getWithoutChildren();

    @Override
    public final M get() {
        if (scope.children().areAllChildrenSelected) {
            return getWithAllChildren();
        } else {
            return getWithoutChildren();
        }
    }

    @Override
    public boolean exists() {
        return getWithoutChildren() != null;
    }

    public R withAllChildren() {
        return repositoryFactory.updated(RepositoryScopeHelper.withAllChildren(scope));
    }
}
