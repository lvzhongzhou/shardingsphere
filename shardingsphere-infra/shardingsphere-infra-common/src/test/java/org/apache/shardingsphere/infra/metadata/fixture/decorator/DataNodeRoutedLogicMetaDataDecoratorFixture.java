/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.infra.metadata.fixture.decorator;

import org.apache.shardingsphere.infra.metadata.fixture.rule.DataNodeRoutedFixtureRule;
import org.apache.shardingsphere.infra.metadata.model.logic.spi.LogicMetaDataDecorator;
import org.apache.shardingsphere.infra.metadata.model.physical.model.column.PhysicalColumnMetaData;
import org.apache.shardingsphere.infra.metadata.model.physical.model.table.PhysicalTableMetaData;

import java.util.Collections;

public final class DataNodeRoutedLogicMetaDataDecoratorFixture implements LogicMetaDataDecorator<DataNodeRoutedFixtureRule> {
    
    @Override
    public PhysicalTableMetaData decorate(final String tableName, final PhysicalTableMetaData tableMetaData, final DataNodeRoutedFixtureRule rule) {
        PhysicalColumnMetaData columnMetaData = new PhysicalColumnMetaData("id", 1, "INT", true, true, false);
        return new PhysicalTableMetaData(Collections.singletonList(columnMetaData), Collections.emptyList());
    }
    
    @Override
    public int getOrder() {
        return 1;
    }
    
    @Override
    public Class<DataNodeRoutedFixtureRule> getTypeClass() {
        return DataNodeRoutedFixtureRule.class;
    }
}
