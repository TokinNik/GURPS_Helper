package com.example.testapp

import com.example.testapp.util.RollUtil
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import java.util.concurrent.TimeUnit

class RxTest : ObservableOnSubscribe<Int>{

    private val rollUtil: RollUtil by inject()

    init {
        val appScope = Toothpick.openScope("APP")
        Toothpick.inject(this, appScope)
        println(rollUtil.roll3D6())
    }

    override fun subscribe(emitter: ObservableEmitter<Int>) {
        for(i in 1..5) {
            emitter.onNext(rollUtil.roll3D6())
            TimeUnit.SECONDS.sleep(1)
        }
        emitter.onComplete()
    }


}