# Mutiny in Quarkus leak reproducer

- Create a Uni, and put here some callbacks.
- On top of that call `.ifNoItem().after(someTimeout).fail()`
- Complete the Uni.

Expected behavior:
All callback-related objects can be collected by GC because they are not needed for completed Uni. (You can see it in UniLeakTest).

In Quarkus behaviour:
The link is kept until the `someTimout` is passed. (You can see it in UniLeakQuarkusTest).
