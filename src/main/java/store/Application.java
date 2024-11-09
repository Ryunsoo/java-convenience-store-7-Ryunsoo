package store;

import store.controller.ConvenienceStore;

public class Application {
    public static void main(String[] args) {
        ConvenienceStore convenienceStore = new ConvenienceStore();
        convenienceStore.open();
    }
}
