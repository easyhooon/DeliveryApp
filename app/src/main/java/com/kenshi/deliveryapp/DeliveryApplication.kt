package com.kenshi.deliveryapp

import android.app.Application
import android.content.Context
import com.kenshi.deliveryapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

//ApplicationContext 를 주입받아 사용할 수 있도록 추가
//manifest 에 android:name 으로 추가 해줘야함
class DeliveryApplication : Application() {

    //onCreate 시에 ApplicationContext 를 사용해야 함
    override fun onCreate() {
        super.onCreate()
        appContext = this

        //appModule 을 application 에서 시작해서 주입해줄 수 있도록
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@DeliveryApplication)
            modules(appModule)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        appContext = null
    }

    companion object {
        //종료 될 경우 null 로 바꿔줘야 함
        var appContext: Context? = null
            private set
    }

}