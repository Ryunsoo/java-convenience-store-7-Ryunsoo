package store;

import store.controller.ConvenienceStore;
import store.domain.store.Staff;
import store.view.setup.FileStoreDataProvider;
import store.view.setup.StoreDataProvider;

public class Application {
    public static void main(String[] args) {
        StoreDataProvider storeDataProvider = new FileStoreDataProvider();
        Staff staff = new Staff(storeDataProvider);

        ConvenienceStore convenienceStore = new ConvenienceStore(staff);
        convenienceStore.open();
    }
}
