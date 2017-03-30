package net.maiatday.a7minworkout;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;
/**
 * Created by maia on 2017/03/30.
 */

public class A7MinApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
             //   .deleteRealmIfMigrationNeeded() //If the schema changes, whack the db
                .build();
        Realm.setDefaultConfiguration(config);

        if (BuildConfig.DEBUG) {
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                            .build());
        }
    }
}
