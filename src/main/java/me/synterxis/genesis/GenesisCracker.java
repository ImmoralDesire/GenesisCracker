package me.synterxis.genesis;

import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;

public interface GenesisCracker {

    GenesisCracker setMaxTries(double tries);

    GenesisCracker setStart(double start);

    GenesisCracker setAdjectiveFile(InputStream stream);

    GenesisCracker setColorFile(InputStream stream);

    GenesisCracker setAnimalFile(InputStream stream);

    GenesisCracker setEmail(String email);

    Proxy getProxy();

    int getTries();

    String getPassword();

    boolean crack() throws IOException;

    void cancel();
}
