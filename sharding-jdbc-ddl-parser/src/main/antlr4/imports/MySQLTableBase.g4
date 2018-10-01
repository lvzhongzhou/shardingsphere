grammar MySQLTableBase;

import MySQLKeyword,MySQLBase, DataType, Keyword, BaseRule,  Symbol;

tableOptions:
    tableOption (COMMA? tableOption)*
    ;

tableOption:
     AUTO_INCREMENT EQ_OR_ASSIGN? NUMBER
    |AVG_ROW_LENGTH EQ_OR_ASSIGN? NUMBER
    | DEFAULT? characterSetWithEqual
    | CHECKSUM EQ_OR_ASSIGN? NUMBER
    | DEFAULT? collateClauseWithEqual
    | COMMENT EQ_OR_ASSIGN? STRING
    | COMPRESSION EQ_OR_ASSIGN? (ZLIB|STRING|NONE)
    | CONNECTION EQ_OR_ASSIGN? STRING
    | (DATA|INDEX) DIRECTORY EQ_OR_ASSIGN? STRING
    | DELAY_KEY_WRITE EQ_OR_ASSIGN? NUMBER
    | ENCRYPTION EQ_OR_ASSIGN? STRING
    | ENGINE EQ_OR_ASSIGN? engineName
    | INSERT_METHOD EQ_OR_ASSIGN? ( NO | FIRST | LAST )
    | KEY_BLOCK_SIZE EQ_OR_ASSIGN? NUMBER
    | MAX_ROWS EQ_OR_ASSIGN? NUMBER
    | MIN_ROWS EQ_OR_ASSIGN? NUMBER
    | PACK_KEYS EQ_OR_ASSIGN? (NUMBER | DEFAULT)
    | PASSWORD EQ_OR_ASSIGN? STRING
    | ROW_FORMAT EQ_OR_ASSIGN? (DEFAULT|DYNAMIC|FIXED|COMPRESSED|REDUNDANT|COMPACT)
    | STATS_AUTO_RECALC EQ_OR_ASSIGN? (DEFAULT|NUMBER)
    | STATS_PERSISTENT EQ_OR_ASSIGN? (DEFAULT|NUMBER)
    | STATS_SAMPLE_PAGES EQ_OR_ASSIGN? NUMBER
    | TABLESPACE tablespaceName (STORAGE (DISK|MEMORY|DEFAULT))?
    | UNION EQ_OR_ASSIGN? idList
    ;  
    
engineName:
    ID
    |MEMORY
    ;
    
columnDefinition:
    columnName dataType (dataTypeOption* | dataTypeGenerated)?
    ;
    
dataType:
    ((INTEGER | INT | TINYINT | SMALLINT | MEDIUMINT | BIGINT) dataTypeLength? UNSIGNED? ZEROFILL?)
    | ((DECIMAL | DEC | NUMERIC | FIXED) dataTypeLength? UNSIGNED? ZEROFILL?)
    | ((FLOAT | DOUBLE | DOUBLE PRECISION) dataTypeLength? UNSIGNED? ZEROFILL?)
    | BIT dataTypeLength?
    | (BOOL | BOOLEAN)
    | DATE
    | ((DATETIME | TIMESTAMP | TIME | YEAR) dataTypeLength?)
    | (((NATIONAL? CHAR) | (NATIONAL? VARCHAR) | TEXT) dataTypeLength? characterSet? collateClause?)
    | ((BINARY | VARBINARY | BLOB) dataTypeLength?)
    | (TINYBLOB | MEDIUMBLOB | LONGBLOB)
    | ((TINYTEXT | MEDIUMTEXT | LONGTEXT) characterSet? collateClause?)
    | ((ENUM | SET) (LEFT_PAREN STRING (COMMA STRING)* RIGHT_PAREN characterSet? collateClause?))
    | (GEOMETRY | POINT | LINESTRING | POLYGON | MULTIPOINT | MULTILINESTRING | MULTIPOLYGON | GEOMETRYCOLLECTION | JSON)
    ;
    
dataTypeOption:
    (NOT? NULL)
    | (DEFAULT defaultValue)
    | AUTO_INCREMENT
    | (UNIQUE KEY?)
    | (PRIMARY? KEY)
    | (COMMENT STRING)
    | (COLUMN_FORMAT (FIXED | DYNAMIC | DEFAULT))
    | (STORAGE (DISK|MEMORY|DEFAULT))
    | referenceDefinition
    ;

defaultValue:
    NULL
    | NUMBER
    | STRING
    | (currentTimestampType (ON UPDATE currentTimestampType)?)
    ;

currentTimestampType:
    (CURRENT_TIMESTAMP | LOCALTIME | LOCALTIMESTAMP | NOW) dataTypeLength?
    ;
    
 referenceDefinition:
    REFERENCES tableName keyParts
    (MATCH FULL | MATCH PARTIAL | MATCH SIMPLE)?
    (((ON UPDATE referenceOption)? (ON DELETE referenceOption)?)
          |((ON DELETE referenceOption)? (ON UPDATE referenceOption)?)
    )
    ;
    
referenceOption:
    (RESTRICT | CASCADE | SET NULL | NO ACTION | SET DEFAULT)
    ;
    
dataTypeGenerated:
    (GENERATED ALWAYS)? AS LEFT_PAREN expr RIGHT_PAREN
    (VIRTUAL | STORED)? (NOT NULL | NULL)?
    (UNIQUE (KEY)?)? ((PRIMARY)? KEY)?
    (COMMENT STRING)?
    ;
    
partitionOptions:
    PARTITION BY (linearPartition | rangeOrListPartition)
    (PARTITIONS NUMBER)?
    (SUBPARTITION BY linearPartition (SUBPARTITIONS NUMBER)? )?
    partitionDefinitions?
    ;

//hash(YEAR(col)) YEAR is keyword which does not match expr
linearPartition:
     LINEAR? ((HASH (yearFunctionExpr | exprWithParen)) |keyColumnList)
     ;
 
 yearFunctionExpr:
     LEFT_PAREN YEAR exprWithParen RIGHT_PAREN
     ;
     
keyColumnList:
    KEY (ALGORITHM EQ_OR_ASSIGN NUMBER)? columnList
    ;
    
exprWithParen:
    LEFT_PAREN expr RIGHT_PAREN
    ;
      
rangeOrListPartition:
     (RANGE | LIST ) exprOrColumns
     ;

exprOrColumns:
    LEFT_PAREN expr RIGHT_PAREN | COLUMNS columnList  
    ;
           
partitionDefinitions:
    LEFT_PAREN partitionDefinition (COMMA partitionDefinition)* RIGHT_PAREN
    ;
    
partitionDefinition:
    PARTITION partitionName
    (VALUES (lessThanPartition | IN valueListWithParen))?
    (STORAGE? ENGINE EQ_OR_ASSIGN? engineName)?
    (COMMENT EQ_OR_ASSIGN? STRING )?
    (DATA DIRECTORY EQ_OR_ASSIGN? STRING)?
    (INDEX DIRECTORY EQ_OR_ASSIGN? STRING)?
    (MAX_ROWS EQ_OR_ASSIGN? NUMBER)?
    (MIN_ROWS EQ_OR_ASSIGN? NUMBER)?
    (TABLESPACE EQ_OR_ASSIGN? tablespaceName)?
    (subpartitionDefinition (COMMA subpartitionDefinition)*)?
    ;

lessThanPartition:
    LESS THAN ((LEFT_PAREN (expr | valueList) RIGHT_PAREN) | MAXVALUE)
    ;
    
subpartitionDefinition:
    SUBPARTITION partitionName
    ((STORAGE)? ENGINE EQ_OR_ASSIGN? engineName)?
    (COMMENT EQ_OR_ASSIGN? STRING )?
    (DATA DIRECTORY EQ_OR_ASSIGN? STRING)?
    (INDEX DIRECTORY EQ_OR_ASSIGN? STRING)?
    (MAX_ROWS EQ_OR_ASSIGN? NUMBER)?
    (MIN_ROWS EQ_OR_ASSIGN? NUMBER)?
    (TABLESPACE EQ_OR_ASSIGN? tablespaceName)?
    ;   
    
indexDefinition:
    (((FULLTEXT | SPATIAL) indexAndKey?)|indexAndKey) indexDefOption
    ;
    
indexDefOption:
    indexName? indexType? keyParts indexOption?
    ;

constraintDefinition:
    (CONSTRAINT symbol?)? (primaryKeyOption | uniqueOption | foreignKeyOption)
    ;
    
primaryKeyOption:    
    primaryKey indexType? keyParts indexOption?
    ;
    
uniqueOption:    
    UNIQUE (INDEX|KEY)? indexName? indexType? keyParts indexOption?
    ;
    
foreignKeyOption:    
    FOREIGN KEY indexName? LEFT_PAREN columnName (COMMA columnName)* RIGHT_PAREN  referenceDefinition
    ;

 