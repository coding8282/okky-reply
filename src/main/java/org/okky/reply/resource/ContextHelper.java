package org.okky.reply.resource;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public abstract class ContextHelper {
    private static String id;

    public static String getId() {
        return id;
    }

    public static void setId(String requesterId) {
        id = requesterId;
    }
}

