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

package org.apache.shardingsphere.infra.metadata.refresh.impl;

import org.apache.shardingsphere.infra.database.type.DatabaseType;
import org.apache.shardingsphere.infra.metadata.refresh.AbstractMetaDataRefreshStrategyTest;
import org.apache.shardingsphere.infra.metadata.refresh.MetaDataRefreshStrategy;
import org.apache.shardingsphere.sql.parser.sql.common.segment.ddl.index.IndexSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.table.SimpleTableSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.table.TableNameSegment;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.CreateIndexStatement;
import org.apache.shardingsphere.sql.parser.sql.common.value.identifier.IdentifierValue;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.mysql.ddl.MySQLCreateIndexStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.oracle.ddl.OracleCreateIndexStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.postgresql.ddl.PostgreSQLCreateIndexStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.sqlserver.ddl.SQLServerCreateIndexStatement;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public final class CreateIndexStatementMetaDataRefreshStrategyTest extends AbstractMetaDataRefreshStrategyTest {
    
    @Test
    public void refreshMetaDataForMySQL() throws SQLException {
        refreshMetaData(new MySQLCreateIndexStatement());
    }
    
    @Test
    public void refreshMetaDataForOracle() throws SQLException {
        refreshMetaData(new OracleCreateIndexStatement());
    }
    
    @Test
    public void refreshMetaDataForPostgreSQL() throws SQLException {
        refreshMetaData(new PostgreSQLCreateIndexStatement());
    }
    
    @Test
    public void refreshMetaDataForSQLServer() throws SQLException {
        refreshMetaData(new SQLServerCreateIndexStatement());
    }
    
    private void refreshMetaData(final CreateIndexStatement createIndexStatement) throws SQLException {
        MetaDataRefreshStrategy<CreateIndexStatement> metaDataRefreshStrategy = new CreateIndexStatementMetaDataRefreshStrategy();
        createIndexStatement.setIndex(new IndexSegment(1, 2, new IdentifierValue("t_order_index")));
        createIndexStatement.setTable(new SimpleTableSegment(new TableNameSegment(1, 3, new IdentifierValue("t_order"))));
        metaDataRefreshStrategy.refreshMetaData(getMetaData(), mock(DatabaseType.class), Collections.emptyList(), createIndexStatement, tableName -> Optional.empty());
        assertTrue(getMetaData().getSchemaMetaData().getConfiguredSchemaMetaData().get("t_order").getIndexes().containsKey("t_order_index"));
    }
    
    @Test
    public void refreshMetaDataIfIndexIsNullForMySQL() throws SQLException {
        refreshMetaDataIfIndexIsNull(new MySQLCreateIndexStatement());
    }
    
    @Test
    public void refreshMetaDataIfIndexIsNullForOracle() throws SQLException {
        refreshMetaDataIfIndexIsNull(new OracleCreateIndexStatement());
    }
    
    @Test
    public void refreshMetaDataIfIndexIsNullForPostgreSQL() throws SQLException {
        refreshMetaDataIfIndexIsNull(new PostgreSQLCreateIndexStatement());
    }
    
    @Test
    public void refreshMetaDataIfIndexIsNullForSQLServer() throws SQLException {
        refreshMetaDataIfIndexIsNull(new SQLServerCreateIndexStatement());
    }
    
    private void refreshMetaDataIfIndexIsNull(final CreateIndexStatement createIndexStatement) throws SQLException {
        MetaDataRefreshStrategy<CreateIndexStatement> metaDataRefreshStrategy = new CreateIndexStatementMetaDataRefreshStrategy();
        createIndexStatement.setTable(new SimpleTableSegment(new TableNameSegment(1, 3, new IdentifierValue("t_order"))));
        metaDataRefreshStrategy.refreshMetaData(getMetaData(), mock(DatabaseType.class), Collections.emptyList(), createIndexStatement, tableName -> Optional.empty());
        assertFalse(getMetaData().getSchemaMetaData().getConfiguredSchemaMetaData().get("t_order").getIndexes().containsKey("t_order_index"));
    }
}
