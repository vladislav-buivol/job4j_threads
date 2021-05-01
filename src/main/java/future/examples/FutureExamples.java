package future.examples;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static future.examples.FutureExamples.RunAsyncExample.goToTrash;
import static future.examples.FutureExamples.RunAsyncExample.iWork;
import static future.examples.FutureExamples.SupplyAsyncExample.buyProduct;

public class FutureExamples {
    public static void main(String[] args) throws Exception {
        //RunAsyncExample.runAsyncExample();
        //SupplyAsyncExample.supplyAsyncExample();
        //ThenRunExample.thenRunExample();
        //ThenAcceptExamples.thenAcceptExample();
        //ThenApplyExample.thenApplyExample();
        /*ThenComposeExample.thenComposeExample();
        ThenComposeExample.thenCombineExample();*/
        AllOfAnyOfExamples.allOfExample();
        AllOfAnyOfExamples.anyOfExample();
    }

    static class RunAsyncExample {

        protected static void iWork() throws InterruptedException {
            int count = 0;
            while (count < 10) {
                System.out.println("Вы: Я работаю");
                TimeUnit.SECONDS.sleep(1);
                count++;
            }
        }

        public static CompletableFuture<Void> goToTrash() {
            return CompletableFuture.runAsync(
                    () -> {
                        System.out.println("Сын: Мам/Пам, я пошел выносить мусор");
                        try {
                            TimeUnit.SECONDS.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Сын: Мам/Пап, я вернулся!");
                    }
            );
        }

        public static void runAsyncExample() throws Exception {
            CompletableFuture<Void> gtt = goToTrash();
            iWork();
        }
    }

    static class SupplyAsyncExample {
        public static CompletableFuture<String> buyProduct(String product) {
            return CompletableFuture.supplyAsync(
                    () -> {
                        System.out.println("Сын: Мам/Пам, я пошел в магазин");
                        try {
                            TimeUnit.SECONDS.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Сын: Мам/Пап, я купил " + product);
                        return product;
                    }
            );
        }

        public static void supplyAsyncExample() throws Exception {
            CompletableFuture<String> bm = buyProduct("Молоко");
            iWork();
            System.out.println("Куплено: " + bm.get());
        }
    }

    protected static class ThenRunExample {
        public static void thenRunExample() throws Exception {
            CompletableFuture<Void> gtt = goToTrash();
            gtt.thenRun(() -> {
                int count = 0;
                while (count < 3) {
                    System.out.println("Сын: я мою руки");
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count++;
                }
                System.out.println("Сын: Я помыл руки");
            });
            iWork();
        }
    }

    protected static class ThenAcceptExamples {
        public static void thenAcceptExample() throws Exception {
            CompletableFuture<String> bm = buyProduct("Молоко");
            bm.thenAccept((product) -> System.out.println("Сын: Я убрал " + product + " в холодильник "));
            iWork();
            System.out.println("Куплено: " + bm.get());
        }
    }

    protected static class ThenApplyExample {
        public static void thenApplyExample() throws Exception {
            CompletableFuture<String> bm = buyProduct("Молоко")
                    .thenApply((product) -> "Сын: я налил тебе в кружку " + product + ". Держи.");
            iWork();
            System.out.println(bm.get());
        }
    }

    protected static class ThenComposeExample {
        public static void thenComposeExample() throws Exception {
            CompletableFuture<Void> result = buyProduct("Молоко").thenCompose(a -> goToTrash());
            iWork();
        }

        public static void thenCombineExample() throws Exception {
            CompletableFuture<String> result = buyProduct("Молоко")
                    .thenCombine(buyProduct("Хлеб"), (r1, r2) -> "Куплены " + r1 + " и " + r2);
            iWork();
            System.out.println(result.get());
        }
    }

    protected static class AllOfAnyOfExamples {
        public static CompletableFuture<Void> washHands(String name) {
            return CompletableFuture.runAsync(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(name + ", моет руки");
            });
        }

        public static void allOfExample() throws Exception {
            CompletableFuture<Void> all = CompletableFuture.allOf(
                    washHands("Папа"), washHands("Мама"),
                    washHands("Ваня"), washHands("Боря")
            );
            TimeUnit.SECONDS.sleep(3);
        }

        public static CompletableFuture<String> whoWashHands(String name) {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return name + ", моет руки";
            });
        }

        public static void anyOfExample() throws Exception {
            CompletableFuture<Object> first = CompletableFuture.anyOf(
                    whoWashHands("Папа"), whoWashHands("Мама"),
                    whoWashHands("Ваня"), whoWashHands("Боря")
            );
            System.out.println("Кто сейчас моет руки?");
            TimeUnit.SECONDS.sleep(1);
            System.out.println(first.get());
        }
    }

}
