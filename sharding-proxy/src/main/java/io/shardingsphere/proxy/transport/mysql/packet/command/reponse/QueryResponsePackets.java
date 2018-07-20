/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.shardingsphere.proxy.transport.mysql.packet.command.reponse;

import io.shardingsphere.proxy.transport.mysql.constant.ColumnType;
import io.shardingsphere.proxy.transport.mysql.packet.command.text.query.ColumnDefinition41Packet;
import io.shardingsphere.proxy.transport.mysql.packet.command.text.query.FieldCountPacket;
import io.shardingsphere.proxy.transport.mysql.packet.generic.EofPacket;
import lombok.Getter;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Query response packets.
 *
 * @author zhangliang
 */
@Getter
public final class QueryResponsePackets extends CommandResponsePackets {
    
    private final FieldCountPacket fieldCountPacket;
    
    private final Collection<ColumnDefinition41Packet> columnDefinition41Packets;
    
    public QueryResponsePackets(final FieldCountPacket fieldCountPacket, final Collection<ColumnDefinition41Packet> columnDefinition41Packets, final EofPacket eofPacket) {
        getPackets().add(fieldCountPacket);
        getPackets().addAll(columnDefinition41Packets);
        getPackets().add(eofPacket);
        this.fieldCountPacket = fieldCountPacket;
        this.columnDefinition41Packets = columnDefinition41Packets;
    }
    
    /**
     * Get column count.
     *
     * @return column count
     */
    public int getColumnCount() {
        return fieldCountPacket.getColumnCount();
    }
    
    /**
     * Get column types.
     *
     * @return column types
     */
    public Collection<ColumnType> getColumnTypes() {
        Collection<ColumnType> result = new LinkedList<>();
        for (ColumnDefinition41Packet each : columnDefinition41Packets) {
            result.add(each.getColumnType());
        }
        return result;
    }
}
