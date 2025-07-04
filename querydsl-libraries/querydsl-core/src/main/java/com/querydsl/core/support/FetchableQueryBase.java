/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.core.support;

import com.querydsl.core.CloseableIterator;
import com.querydsl.core.Fetchable;
import com.querydsl.core.FetchableQuery;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.ResultTransformer;
import com.querydsl.core.types.SubQueryExpression;
import java.util.List;
import org.jetbrains.annotations.Nullable;

/**
 * {@code FetchableQueryBase} extends the {@link QueryBase} class to provide default implementations
 * of the methods of the {@link com.querydsl.core.Fetchable} interface
 *
 * @param <T> result type
 * @param <Q> concrete subtype
 * @author tiwe
 */
public abstract class FetchableQueryBase<T, Q extends FetchableQueryBase<T, Q>> extends QueryBase<Q>
    implements Fetchable<T> {

  public FetchableQueryBase(QueryMixin<Q> queryMixin) {
    super(queryMixin);
  }

  @Override
  public List<T> fetch() {
    return CloseableIterator.asList(iterate());
  }

  @Override
  public final T fetchFirst() {
    return limit(1).fetchOne();
  }

  public <T> T transform(ResultTransformer<T> transformer) {
    return transformer.transform((FetchableQuery<?, ?>) this);
  }

  @Nullable
  protected <T> T uniqueResult(CloseableIterator<T> it) {
    try {
      if (it.hasNext()) {
        var rv = it.next();
        if (it.hasNext()) {
          throw new NonUniqueResultException();
        }
        return rv;
      } else {
        return null;
      }
    } finally {
      it.close();
    }
  }

  @Override
  public final boolean equals(Object o) {
    if (o == this) {
      return true;
    } else if (o instanceof SubQueryExpression<?> s) {
      return s.getMetadata().equals(queryMixin.getMetadata());
    } else {
      return false;
    }
  }
}
