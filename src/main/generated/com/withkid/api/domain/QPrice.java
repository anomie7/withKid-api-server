package com.withkid.api.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPrice is a Querydsl query type for Price
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPrice extends EntityPathBase<Price> {

    private static final long serialVersionUID = 1358241422L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPrice price1 = new QPrice("price1");

    public final BooleanPath defaultPrice = createBoolean("defaultPrice");

    public final StringPath extraInfo = createString("extraInfo");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QInterParkContent interpark;

    public final StringPath name = createString("name");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final StringPath ticketInfo = createString("ticketInfo");

    public QPrice(String variable) {
        this(Price.class, forVariable(variable), INITS);
    }

    public QPrice(Path<? extends Price> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPrice(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPrice(PathMetadata metadata, PathInits inits) {
        this(Price.class, metadata, inits);
    }

    public QPrice(Class<? extends Price> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.interpark = inits.isInitialized("interpark") ? new QInterParkContent(forProperty("interpark"), inits.get("interpark")) : null;
    }

}

