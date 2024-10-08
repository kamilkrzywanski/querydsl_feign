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
package com.querydsl.r2dbc.mysql;

import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.r2dbc.AbstractR2DBCQueryFactory;
import com.querydsl.r2dbc.Configuration;
import com.querydsl.r2dbc.MySQLTemplates;
import com.querydsl.r2dbc.R2DBCConnectionProvider;
import com.querydsl.r2dbc.SQLTemplates;
import com.querydsl.r2dbc.dml.R2DBCInsertClause;
import com.querydsl.sql.RelationalPath;

/**
 * MySQL specific implementation of SQLQueryFactory
 *
 * @author mc_fish
 */
public class R2DBCMySQLQueryFactory extends AbstractR2DBCQueryFactory<R2DBCMySQLQuery<?>> {

  public R2DBCMySQLQueryFactory(Configuration configuration, R2DBCConnectionProvider connection) {
    super(configuration, connection);
  }

  public R2DBCMySQLQueryFactory(R2DBCConnectionProvider connection) {
    this(new Configuration(new MySQLTemplates()), connection);
  }

  public R2DBCMySQLQueryFactory(SQLTemplates templates, R2DBCConnectionProvider connection) {
    this(new Configuration(templates), connection);
  }

  /**
   * Create a INSERT IGNORE INTO clause
   *
   * @param entity table to insert to
   * @return insert clause
   */
  public R2DBCInsertClause insertIgnore(RelationalPath<?> entity) {
    var insert = insert(entity);
    insert.addFlag(Position.START_OVERRIDE, "insert ignore into ");
    return insert;
  }

  /**
   * Create a INSERT ... ON DUPLICATE KEY UPDATE clause
   *
   * @param entity table to insert to
   * @param clause clause
   * @return insert clause
   */
  public R2DBCInsertClause insertOnDuplicateKeyUpdate(RelationalPath<?> entity, String clause) {
    var insert = insert(entity);
    insert.addFlag(Position.END, " on duplicate key update " + clause);
    return insert;
  }

  /**
   * Create a INSERT ... ON DUPLICATE KEY UPDATE clause
   *
   * @param entity table to insert to
   * @param clause clause
   * @return insert clause
   */
  public R2DBCInsertClause insertOnDuplicateKeyUpdate(
      RelationalPath<?> entity, Expression<?> clause) {
    var insert = insert(entity);
    insert.addFlag(
        Position.END,
        ExpressionUtils.template(String.class, " on duplicate key update {0}", clause));
    return insert;
  }

  /**
   * Create a INSERT ... ON DUPLICATE KEY UPDATE clause
   *
   * @param entity table to insert to
   * @param clauses clauses
   * @return insert clause
   */
  public R2DBCInsertClause insertOnDuplicateKeyUpdate(
      RelationalPath<?> entity, Expression<?>... clauses) {
    var insert = insert(entity);
    var flag = new StringBuilder(" on duplicate key update ");
    for (var i = 0; i < clauses.length; i++) {
      flag.append(i > 0 ? ", " : "").append("{").append(i).append("}");
    }
    insert.addFlag(Position.END, ExpressionUtils.template(String.class, flag.toString(), clauses));
    return insert;
  }

  @Override
  public R2DBCMySQLQuery<?> query() {
    return new R2DBCMySQLQuery<Void>(connection, configuration);
  }

  //    public MyR2DBCReplaceClause replace(RelationalPath<?> entity) {
  //        return new MyR2DBCReplaceClause(connection.getConnection(), configuration, entity);
  //    }

  @Override
  public <T> R2DBCMySQLQuery<T> select(Expression<T> expr) {
    return query().select(expr);
  }

  @Override
  public R2DBCMySQLQuery<Tuple> select(Expression<?>... exprs) {
    return query().select(exprs);
  }

  @Override
  public <T> R2DBCMySQLQuery<T> selectDistinct(Expression<T> expr) {
    return query().select(expr).distinct();
  }

  @Override
  public R2DBCMySQLQuery<Tuple> selectDistinct(Expression<?>... exprs) {
    return query().select(exprs).distinct();
  }

  @Override
  public R2DBCMySQLQuery<Integer> selectZero() {
    return select(Expressions.ZERO);
  }

  @Override
  public R2DBCMySQLQuery<Integer> selectOne() {
    return select(Expressions.ONE);
  }

  @Override
  public <T> R2DBCMySQLQuery<T> selectFrom(RelationalPath<T> expr) {
    return select(expr).from(expr);
  }
}
