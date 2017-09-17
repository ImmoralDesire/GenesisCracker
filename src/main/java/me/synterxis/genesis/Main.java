package me.synterxis.genesis;

import me.synterxis.genesis.exception.GotAnswer;
import me.synterxis.genesis.utils.Files;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ProtocolException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    public List<Thread> threadList = new ArrayList<>();

    public List<Proxy> proxyList = new ArrayList<>();
    public InputStream proxyFile;

    public int numProxies;
    public int maxCombos;

    public List<GenesisCracker> crackerList = new ArrayList<>();

    public ExecutorService service = Executors.newCachedThreadPool();

    public Main() {
        this.proxyFile = this.getClass().getResourceAsStream("/proxies.txt");

        try {
            this.numProxies = Files.getLines(this.getClass().getResourceAsStream("/proxies.txt"));
            this.maxCombos  = Files.getLines(Main.class.getResourceAsStream("/adjectives.txt")) *
                              Files.getLines(Main.class.getResourceAsStream("/colors.txt")) *
                              Files.getLines(Main.class.getResourceAsStream("/animals.txt")) * 10;
        } catch(Exception e) {
            e.printStackTrace();
        }

        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.proxyFile))) {
            for(String l; (l = bufferedReader.readLine()) != null;) {
                System.out.println("read line");
                String[] parts = l.split(":");
                Proxy p = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(parts[0], Integer.parseInt(parts[1])));
                this.proxyList.add(p);
            }

        } catch(Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public static void main(String[] args) throws IOException {
        Main main = new Main();

        CompletionService<Void> completionService = new ExecutorCompletionService<Void>( main.service );
        List<Future<Void>> results = new ArrayList<Future<Void>>();

        double combosPerProxy = Math.ceil(main.maxCombos / main.numProxies);

        for(Proxy p : main.proxyList) {
            GenesisCracker genesisCracker = new GenesisCrackerBuilder(p).build();

            genesisCracker.setEmail("22ss0717@wwprsd.org")
                          .setAdjectiveFile(Main.class.getResourceAsStream("/adjectives.txt"))
                          .setColorFile(Main.class.getResourceAsStream("/colors.txt"))
                          .setAnimalFile(Main.class.getResourceAsStream("/animals.txt"))
                          .setStart(main.proxyList.indexOf(p) * combosPerProxy)
                          .setMaxTries((main.proxyList.indexOf(p) * combosPerProxy) + combosPerProxy);
            main.crackerList.add(genesisCracker);
        }

        for(GenesisCracker genesisCracker : main.crackerList) {
            System.out.println("HI");
            results.add(completionService.submit(new Callable<Void>() {

                @Override
                public Void call() throws Exception {

                    if (genesisCracker.crack()) {
                        Thread.currentThread().interrupt();
                        for (Future<Void> future : results) {
                            future.cancel(true);
                        }
                        for(GenesisCracker g : main.crackerList) {
                            g.cancel();
                        }
                        results.clear();
                        main.service.shutdownNow();

                        System.out.println("The password is: " + genesisCracker.getPassword());
                    }

                    return null;
                }
            }
            ));
        }
    }
}
