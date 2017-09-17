package me.synterxis.genesis;

import me.synterxis.genesis.impl.GenesisCrackerImpl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

import java.net.Proxy;

public class GenesisCrackerBuilder {

    protected final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    protected Proxy proxy = Proxy.NO_PROXY;
    protected OkHttpClient.Builder builder = new OkHttpClient.Builder().proxy(proxy);
    protected OkHttpClient client = new OkHttpClient();

    public GenesisCrackerBuilder(Proxy proxy) {
        this.proxy = proxy;
        this.builder = new OkHttpClient.Builder().proxy(this.proxy);
        this.client = this.builder.build();
    }

    public GenesisCrackerBuilder() {
        this(Proxy.NO_PROXY);
    }

    public GenesisCrackerImpl build() {
        GenesisCrackerImpl genesisCracker = new GenesisCrackerImpl(this.builder, this.proxy);
        return genesisCracker;
    }
}
