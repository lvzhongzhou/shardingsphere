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

package org.apache.shardingsphere.infra.metadata.refresh;

import org.apache.shardingsphere.infra.database.type.DatabaseType;
import org.apache.shardingsphere.infra.metadata.model.ShardingSphereMetaData;
import org.apache.shardingsphere.sql.parser.sql.common.statement.SQLStatement;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Meta data refresh strategy.
 *
 * @param <T> type of SQL statement
 */
public interface MetaDataRefreshStrategy<T extends SQLStatement> {
    
    /**
     * Refresh meta data.
     *
     * @param metaData ShardingSphere meta data
     * @param databaseType database type
     * @param routeDataSourceNames route dataSource names
     * @param sqlStatement SQL statement
     * @param callback callback
     * @throws SQLException SQL exception
     */
    void refreshMetaData(ShardingSphereMetaData metaData, DatabaseType databaseType, Collection<String> routeDataSourceNames, T sqlStatement, TableMetaDataLoaderCallback callback) throws SQLException;
}
