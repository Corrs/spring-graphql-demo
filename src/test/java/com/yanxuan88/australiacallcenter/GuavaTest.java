package com.yanxuan88.australiacallcenter;

import com.google.common.hash.Hashing;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

public class GuavaTest {
    @Test
    void testHash() {
        // System.out.println(Hashing.sipHash24().newHasher().putString(UUID.randomUUID().toString(), UTF_8).hash());
        // slat  4b083d8bff412020
        // pwd   14e1b600b1fd579f47433b88e8d85291
        System.out.println(Hashing.md5().newHasher().putString("4b083d8bff41202014e1b600b1fd579f47433b88e8d85291", UTF_8).hash());
    }
}
