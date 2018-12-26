package com.withkid.api.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInterParkContent is a Querydsl query type for InterParkContent
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QInterParkContent extends EntityPathBase<InterParkContent> {

    private static final long serialVersionUID = -1848576946L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInterParkContent interParkContent = new QInterParkContent("interParkContent");

    public final QAddress address;

    public final EnumPath<DeleteFlag> deleteflag = createEnum("deleteflag", DeleteFlag.class);

    public final EnumPath<InterparkType> dtype = createEnum("dtype", InterparkType.class);

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageFilePath = createString("imageFilePath");

    public final StringPath interparkCode = createString("interparkCode");

    public final StringPath location = createString("location");

    public final StringPath name = createString("name");

    public final ListPath<Price, QPrice> price = this.<Price, QPrice>createList("price", Price.class, QPrice.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public QInterParkContent(String variable) {
        this(InterParkContent.class, forVariable(variable), INITS);
    }

    public QInterParkContent(Path<? extends InterParkContent> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInterParkContent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInterParkContent(PathMetadata metadata, PathInits inits) {
        this(InterParkContent.class, metadata, inits);
    }

    public QInterParkContent(Class<? extends InterParkContent> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.address = inits.isInitialized("address") ? new QAddress(forProperty("address")) : null;
    }

}

