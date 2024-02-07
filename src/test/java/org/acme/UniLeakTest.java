package org.acme;

import io.smallrye.mutiny.Uni;
import java.lang.ref.WeakReference;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;

class UniLeakTest {
    @Test
    void something() throws ExecutionException, InterruptedException {
        System.out.println("Start test");
        var weakObj = new WeakReference<>(new Object());
        UniLeakTest.createUni(weakObj).subscribeAsCompletionStage().get();
        System.out.println("check weakObj before GC: " + weakObj.get());
        System.gc();
        Thread.sleep(100);
        System.out.println("check weakObj after GC: " + weakObj.get());
        Thread.sleep(6000);
        System.gc();
        System.out.println("check weakObj after long timeout: " + weakObj.get());
        System.out.println("stop test");
    }

    public static Uni<Integer> createUni(WeakReference<Object> weakObj) {
        var theObj = weakObj.get();
        return Uni.createFrom().item(UniLeakTest::getNumber)
                .onItemOrFailure().invoke((i, t) -> {
                    System.out.println("using the object: " + theObj);
                })
                .ifNoItem().after(Duration.ofSeconds(10)).fail();
    }

    public static Integer getNumber() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return 143;
    }
}
