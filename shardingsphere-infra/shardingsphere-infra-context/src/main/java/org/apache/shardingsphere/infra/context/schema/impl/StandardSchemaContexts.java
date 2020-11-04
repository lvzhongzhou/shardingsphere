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

package org.apache.shardingsphere.infra.context.schema.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.shardingsphere.infra.auth.Authentication;
import org.apache.shardingsphere.infra.config.properties.ConfigurationProperties;
import org.apache.shardingsphere.infra.context.schema.SchemaContexts;
import org.apache.shardingsphere.infra.database.DefaultSchema;
import org.apache.shardingsphere.infra.database.type.DatabaseType;
import org.apache.shardingsphere.infra.database.type.dialect.MySQLDatabaseType;
import org.apache.shardingsphere.infra.executor.kernel.ExecutorKernel;
import org.apache.shardingsphere.infra.schema.ShardingSphereSchema;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Standard schema contexts.
 */
@RequiredArgsConstructor
@Getter
public final class StandardSchemaContexts implements SchemaContexts {
    
    private final Map<String, ShardingSphereSchema> schemas;
    
    private final ExecutorKernel executorKernel;
    
    private final Authentication authentication;
    
    private final ConfigurationProperties props;
    
    private final DatabaseType databaseType;
    
    private final boolean isCircuitBreak;
    
    public StandardSchemaContexts() {
        // TODO MySQLDatabaseType is invalid because it can not update again
        this(new HashMap<>(), null, new Authentication(), new ConfigurationProperties(new Properties()), new MySQLDatabaseType(), false);
    }
    
    public StandardSchemaContexts(final Map<String, ShardingSphereSchema> schemas, final ExecutorKernel executorKernel, 
                                  final Authentication authentication, final ConfigurationProperties props, final DatabaseType databaseType) {
        this(schemas, executorKernel, authentication, props, databaseType, false);
    }
    
    @Override
    public ShardingSphereSchema getDefaultSchema() {
        return schemas.get(DefaultSchema.LOGIC_NAME);
    }
    
    @Override
    public void close() {
        executorKernel.close();
    }
}
