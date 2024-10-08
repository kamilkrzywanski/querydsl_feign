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
package com.querydsl.collections;

import static com.querydsl.core.alias.Alias.$;
import static com.querydsl.core.alias.Alias.alias;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class IterationTest {

  public static class Data {

    private String data = "data";

    public String getData() {
      return data;
    }
  }

  private List<Data> allData = Arrays.asList(new Data(), new Data());

  private Data lt = alias(Data.class, "Data");

  private List<String> expected = Arrays.asList("data", "data");

  @Test
  public void test() {
    assertThat(CollQueryFactory.from($(lt), allData).select($(lt.getData())).fetch())
        .isEqualTo(expected);
  }

  @Test
  public void test2() {
    assertThat(
            CollQueryFactory.<Data>from($(lt), Arrays.<Data>asList(allData.toArray(Data[]::new)))
                .select($(lt.getData()))
                .fetch())
        .isEqualTo(expected);
  }

  @Test
  public void test3() {
    assertThat(CollQueryFactory.from(lt, allData).select($(lt.getData())).fetch())
        .isEqualTo(expected);
  }

  @Test
  public void test4() {
    assertThat(
            CollQueryFactory.<Data>from(lt, Arrays.<Data>asList(allData.toArray(Data[]::new)))
                .select($(lt.getData()))
                .fetch())
        .isEqualTo(expected);
  }
}
