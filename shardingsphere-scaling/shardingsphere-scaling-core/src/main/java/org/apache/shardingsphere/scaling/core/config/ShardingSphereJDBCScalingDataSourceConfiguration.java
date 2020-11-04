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

package org.apache.shardingsphere.scaling.core.config;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.shardingsphere.infra.database.type.DatabaseType;
import org.apache.shardingsphere.infra.database.type.DatabaseTypeRegistry;
import org.apache.shardingsphere.scaling.core.utils.ConfigurationYamlConverter;

/**
 * ShardingSphere-JDBC scaling data source configuration.
 */
@Getter
@Setter
@EqualsAndHashCode(exclude = "databaseType")
public final class ShardingSphereJDBCScalingDataSourceConfiguration implements ScalingDataSourceConfiguration {
    
    private String dataSource;
    
    private String rule;
    
    private DatabaseType databaseType;
    
    public ShardingSphereJDBCScalingDataSourceConfiguration(final String dataSource, final String rule) {
        this.dataSource = dataSource;
        this.rule = rule;
        databaseType = getDatabaseType();
    }
    
    @Override
    public DatabaseType getDatabaseType() {
        if (null == databaseType) {
            databaseType = DatabaseTypeRegistry.getDatabaseTypeByURL(
                    ConfigurationYamlConverter.loadDataSourceConfigurations(dataSource).values().iterator().next().getProps().get("jdbcUrl").toString());
        }
        return databaseType;
    }
}
