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

package org.apache.shardingsphere.core.route.pagination;

import lombok.Getter;
import org.apache.shardingsphere.core.parse.sql.segment.dml.pagination.NumberLiteralPaginationValueSegment;
import org.apache.shardingsphere.core.parse.sql.segment.dml.pagination.PaginationValueSegment;
import org.apache.shardingsphere.core.parse.sql.segment.dml.pagination.ParameterMarkerPaginationValueSegment;
import org.apache.shardingsphere.core.parse.sql.segment.dml.pagination.limit.LimitValueSegment;
import org.apache.shardingsphere.core.parse.sql.statement.dml.SelectStatement;

import java.util.List;

/**
 * Pagination.
 *
 * @author zhangliang
 * @author caohao
 * @author zhangyonglun
 */
@Getter
public final class Pagination {
    
    private final PaginationValueSegment offsetSegment;
    
    private final PaginationValueSegment rowCountSegment;
    
    private final int actualOffset;
    
    private final int actualRowCount;
    
    public Pagination(final PaginationValueSegment offsetSegment, final PaginationValueSegment rowCountSegment, final List<Object> parameters) {
        this.offsetSegment = offsetSegment;
        this.rowCountSegment = rowCountSegment;
        actualOffset = null == offsetSegment ? 0 : getValue(offsetSegment, parameters);
        actualRowCount = null == rowCountSegment ? -1 : getValue(rowCountSegment, parameters); 
    }
    
    private int getValue(final PaginationValueSegment paginationValueSegment, final List<Object> parameters) {
        return paginationValueSegment instanceof ParameterMarkerPaginationValueSegment
                ? (int) parameters.get(((ParameterMarkerPaginationValueSegment) paginationValueSegment).getParameterIndex())
                : ((NumberLiteralPaginationValueSegment) paginationValueSegment).getValue();
    }
    
    /**
     * Get revised offset.
     *
     * @return revised offset
     */
    public int getRevisedOffset() {
        return 0;
    }
    
    /**
     * Get revised row count.
     * 
     * @param selectStatement select statement
     * @return revised row count
     */
    public int getRevisedRowCount(final SelectStatement selectStatement) {
        if (isMaxRowCount(selectStatement)) {
            return Integer.MAX_VALUE;
        }
        return rowCountSegment instanceof LimitValueSegment ? actualOffset + actualRowCount : actualRowCount;
    }
    
    private boolean isMaxRowCount(final SelectStatement selectStatement) {
        return (!selectStatement.getGroupByItems().isEmpty() || !selectStatement.getAggregationSelectItems().isEmpty()) && !selectStatement.isSameGroupByAndOrderByItems();
    }
}