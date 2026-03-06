package dev.dead.r2dbctdd;

import io.vavr.collection.Stream;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

//@SpringBootTest
class R2dbcTddApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testVavr() {
        val a = Stream.of("a", "b", "c")
                .map(String::toUpperCase)
                .asJava();
        System.out.println(a);

    }

    @Test
    void immutableCollection() {
        List<String> list = List.of("a", "b", "c");
        val a = Collections.unmodifiableCollection(list);
        assertThrows(UnsupportedOperationException.class, () -> a.add("d"));
    }

}
